/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.funpay4j.client.jsoup;

import com.google.gson.JsonParser;
import lombok.NonNull;
import okhttp3.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import ru.funpay4j.client.FunPayParser;
import ru.funpay4j.core.exceptions.FunPayApiException;
import ru.funpay4j.core.objects.game.PromoGame;
import ru.funpay4j.core.objects.game.PromoGameCounter;
import ru.funpay4j.core.objects.lot.Lot;
import ru.funpay4j.core.objects.lot.LotCounter;
import ru.funpay4j.core.objects.offer.Offer;
import ru.funpay4j.core.objects.offer.PreviewOffer;
import ru.funpay4j.core.objects.user.PreviewSeller;
import ru.funpay4j.core.objects.user.Seller;
import ru.funpay4j.core.objects.user.SellerReview;
import ru.funpay4j.core.objects.user.User;

import java.io.IOException;
import java.util.*;

/**
 * This implementation of FunPayParser uses Jsoup to parse
 *
 * @author panic08
 * @since 1.0.0
 */
public class JsoupFunPayParser implements FunPayParser {
    @NonNull
    private final OkHttpClient httpClient;

    @NonNull
    private final String baseURL;

    public JsoupFunPayParser(@NonNull OkHttpClient httpClient, @NonNull String baseURL) {
        this.httpClient = httpClient;
        this.baseURL = baseURL;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Lot parseLot(long lotId) {
        try (Response funPayHtmlPage = httpClient.newCall(new Request.Builder().get().url(baseURL + "/lots/" + lotId + "/").build()).execute()) {
            String funPayHtmlPageBody = funPayHtmlPage.body().string();

            Document funPayDocument = Jsoup.parse(funPayHtmlPageBody);

            if (isNonExistentFunPayPage(funPayDocument)) {
                throw new FunPayApiException("Lot with lotId " + lotId + " does not exist");
            }

            Element funPayContentWithCdElement = funPayDocument.getElementsByClass("content-with-cd").first();

            String title = funPayContentWithCdElement.selectFirst("h1").text();
            String description = funPayContentWithCdElement.selectFirst("p").text();
            List<LotCounter> lotCounters = new ArrayList<>();
            List<PreviewOffer> previewOffers = new ArrayList<>();

            List<Element> funPayCountersElements = funPayDocument.getElementsByClass("counter-list")
                    .first()
                    .select("a");

            for (Element counterItem : funPayCountersElements) {
                String counterHrefAttributeValue = counterItem.attr("href");

                //Skip chips, as they are not supported yet
                if (counterHrefAttributeValue.contains("chips")) {
                    continue;
                }

                long counterLotId = Integer.parseInt(counterHrefAttributeValue.substring(24, counterHrefAttributeValue.length() - 1));

                if (lotId == counterLotId) {
                    continue;
                }

                String counterParam = counterItem.getElementsByClass("counter-param").text();
                int counterValue = Integer.parseInt(counterItem.getElementsByClass("counter-value").text());

                lotCounters.add(
                        LotCounter.builder()
                                .lotId(counterLotId)
                                .param(counterParam)
                                .counter(counterValue)
                                .build()
                );
            }

            List<Element> funPayPreviewOffersElements = funPayDocument.getElementsByClass("tc")
                    .first()
                    .select("a");

            for (Element previewOffer : funPayPreviewOffersElements) {
                String previewOfferHrefAttributeValue = previewOffer.attr("href");
                String previewOfferSellerStyleAttributeValue = previewOffer.getElementsByClass("avatar-photo").attr("style");

                long offerId = Long.parseLong(previewOfferHrefAttributeValue.substring(33));
                String shortDescription = previewOffer.getElementsByClass("tc-desc-text").text();
                double price = Double.parseDouble(previewOffer.getElementsByClass("tc-price").attr("data-s"));
                boolean isAutoDelivery = previewOffer.getElementsByClass("auto-dlv-icon").first() != null;
                boolean isPromo = previewOffer.getElementsByClass("promo-offer-icon").first() != null;

                String sellerDataHrefAttributeValue = previewOffer.getElementsByClass("avatar-photo")
                        .attr("data-href");
                Element sellerReviewCountElement = previewOffer.getElementsByClass("rating-mini-count").first();

                long sellerUserId = Long.parseLong(sellerDataHrefAttributeValue.substring(25, sellerDataHrefAttributeValue.length() - 1));
                String sellerUsername = previewOffer.getElementsByClass("media-user-name").text();
                boolean isSellerOnline = previewOffer.getElementsByClass("media media-user online style-circle").first() != null;
                int sellerReviewCount = sellerReviewCountElement == null ? 0 : Integer.parseInt(sellerReviewCountElement.text());

                previewOffers.add(
                        PreviewOffer.builder()
                                .offerId(offerId)
                                .shortDescription(shortDescription)
                                .price(price)
                                .isAutoDelivery(isAutoDelivery)
                                .isPromo(isPromo)
                                .seller(
                                        PreviewSeller.builder()
                                                .avatarPhotoLink(previewOfferSellerStyleAttributeValue.substring(22, previewOfferSellerStyleAttributeValue.length() - 2))
                                                .userId(sellerUserId)
                                                .username(sellerUsername)
                                                .isOnline(isSellerOnline)
                                                .reviewCount(sellerReviewCount)
                                                .build()
                                )
                                .build()
                );
            }

            return Lot.builder()
                    .id(lotId)
                    .title(title)
                    .description(description)
                    .lotCounters(lotCounters)
                    .previewOffers(previewOffers)
                    .build();
        } catch (IOException e) {
            throw new FunPayApiException(e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<PromoGame> parsePromoGames(@NonNull String query) {
        List<PromoGame> currentPromoGames = new ArrayList<>();

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("query", query)
                .build();

        try (Response response = httpClient.newCall(new Request.Builder().post(requestBody).url(baseURL + "/games/promoFilter")
                .addHeader("x-requested-with", "XMLHttpRequest").build()).execute()) {
            String promoGamesHtml = JsonParser.parseString(response.body().string()).getAsJsonObject().get("html").getAsString();

            List<Element> promoGameElements = Jsoup.parse(promoGamesHtml).getElementsByClass("promo-games");

            for (Element promoGameElement : promoGameElements) {
                Element titleElement = promoGameElement.getElementsByClass("game-title").first().selectFirst("a");
                String titleElementHrefAttributeValue = titleElement.attr("href");

                long lotId = Long.parseLong(titleElementHrefAttributeValue.substring(24, titleElementHrefAttributeValue.length() - 1));
                String title = titleElement.text();

                List<PromoGameCounter> promoGameCounters = new ArrayList<>();

                for (Element promoGameCounterElement : promoGameElement.getElementsByClass("list-inline").select("li")) {
                    Element counterTitleElement = promoGameCounterElement.selectFirst("a");
                    String counterTitleElementHrefAttributeValue = counterTitleElement.attr("href");

                    long counterLotId = Long.parseLong(counterTitleElementHrefAttributeValue.substring(24, counterTitleElementHrefAttributeValue.length() - 1));

                    if (counterLotId == lotId) {
                        continue;
                    }

                    String counterTitle = counterTitleElement.text();

                    promoGameCounters.add(PromoGameCounter.builder().lotId(counterLotId).title(counterTitle).build());
                }

                currentPromoGames.add(PromoGame.builder()
                        .lotId(lotId)
                        .title(title)
                        .promoGameCounters(promoGameCounters)
                        .build());
            }

            return currentPromoGames;
        } catch (IOException e) {
            throw new FunPayApiException(e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Offer parseOffer(long offerId) {
        try (Response funPayHtmlPage = httpClient.newCall(new Request.Builder().get().url(baseURL + "/lots/offer?id=" + offerId).build()).execute()) {
            String funPayHtmlPageBody = funPayHtmlPage.body().string();

            Document funPayDocument = Jsoup.parse(funPayHtmlPageBody);

            if (isNonExistentFunPayPage(funPayDocument)) {
                throw new FunPayApiException("Offer with offerId " + offerId + " does not exist");
            }

            Element paramListElement = funPayDocument.getElementsByClass("param-list").first();
            //Get paramItemElements nested in the current item and not in any other way
            List<Element> paramItemElements = funPayDocument.select(".param-list > .param-item");

            //Selected total price in rubles
            String totalPriceValue = funPayDocument.getElementsByClass("form-control input-lg selectpicker")
                    .first()
                    .children()
                    .get(0)
                    .attr("data-content");

            String shortDescription = null;
            String detailedDescription = null;

            //if there is no shortDescription
            if (paramItemElements.size() == 1) {
                detailedDescription = paramItemElements.get(0).selectFirst("div").text();
            } else if (paramItemElements.size() >= 2) {
                shortDescription = paramItemElements.get(0).selectFirst("div").text();
                detailedDescription = paramItemElements.get(1).selectFirst("div").text();
            }

            boolean isAutoDelivery = !funPayDocument.getElementsByClass("offer-header-auto-dlv-label").isEmpty();
            //Select a floating point number from a string like "from 1111.32 ₽"
            double price = Double.parseDouble(totalPriceValue.replaceAll("[^0-9.]", "").split("\\s+")[0]);
            List<String> attachmentLinks = new ArrayList<>();

            //if the offer has attachments
            if (paramItemElements.size() > 2) {
                for (Element attachmentElement : paramItemElements.get(2).getElementsByClass("attachments-item")) {
                    String attachmentLink = attachmentElement.selectFirst("a").attr("href");

                    attachmentLinks.add(attachmentLink);
                }
            }

            Map<String, String> parameters = new HashMap<>();

            for (Element paramItemElement : paramListElement.getElementsByClass("row").first().getElementsByClass("col-xs-6")) {
                Element parameterElement = paramItemElement.getElementsByClass("param-item").first();

                String key = parameterElement.selectFirst("h5").text();
                String value = parameterElement.selectFirst("div").text();

                parameters.put(key, value);
            }

            Element sellerUsernameElement = funPayDocument.getElementsByClass("media-user-name").first()
                    .selectFirst("a");
            Element sellerImgElement = funPayDocument.getElementsByClass("media-user").first()
                            .selectFirst("img");
            Element sellerReviewCountElement = funPayDocument.getElementsByClass("text-mini text-light mb5").first();

            String sellerUsernameElementHrefAttributeValue = sellerUsernameElement.attr("href");

            long sellerUserId = Long.parseLong(sellerUsernameElementHrefAttributeValue.substring(25, sellerUsernameElementHrefAttributeValue.length() - 1));
            String sellerUsername = sellerUsernameElement.text();
            String sellerAvatarPhotoLink = sellerImgElement.attr("src");
            //Select rating from string like "219 reviews over 2 years"
            int sellerReviewCount = Integer.parseInt(sellerReviewCountElement.text().replaceAll("\\D.*", ""));
            boolean isSellerOnline = funPayDocument.getElementsByClass("media media-user online").first() != null;

            return Offer.builder()
                    .id(offerId)
                    .shortDescription(shortDescription)
                    .detailedDescription(detailedDescription)
                    .isAutoDelivery(isAutoDelivery)
                    .price(price)
                    .attachmentLinks(attachmentLinks)
                    .parameters(parameters)
                    .seller(PreviewSeller.builder()
                            .userId(sellerUserId)
                            .username(sellerUsername)
                            .avatarPhotoLink(sellerAvatarPhotoLink)
                            .reviewCount(sellerReviewCount)
                            .isOnline(isSellerOnline)
                            .build())
                    .build();
        } catch (IOException e) {
            throw new FunPayApiException(e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User parseUser(long userId) {
        try (Response funPayHtmlPage = httpClient.newCall(new Request.Builder().get().url(baseURL + "/users/" + userId + "/").build()).execute()) {
            String funPayHtmlPageBody = funPayHtmlPage.body().string();

            Document funPayDocument = Jsoup.parse(funPayHtmlPageBody);

            if (isNonExistentFunPayPage(funPayDocument)) {
                throw new FunPayApiException("User with userId " + userId + " does not exist");
            }

            Element containerProfileHeader = funPayDocument.getElementsByClass("container profile-header").first();

            Element profileElement = funPayDocument.getElementsByClass("profile").first();

            Element avatarPhotoElement = containerProfileHeader.getElementsByClass("avatar-photo").first();
            Element userBadgesElement = profileElement.getElementsByClass("user-badges").first();

            String avatarPhotoElementStyle = avatarPhotoElement.attr("style");

            String username = profileElement.getElementsByClass("mr4").text();
            String avatarPhotoLink = avatarPhotoElementStyle.substring(22, avatarPhotoElementStyle.length() - 2);
            boolean isOnline = profileElement.getElementsByClass("mb40 online").first() != null;
            List<String> badges = new ArrayList<>();

            if (userBadgesElement != null) {
                for (Element badgeElement : userBadgesElement.children()) {
                    badges.add(badgeElement.text());
                }
            }

            String registeredAt = profileElement.getElementsByClass("text-nowrap").first().text();

            Element sellerElement = funPayDocument.getElementsByClass("param-item mb10").first();

            //if user is seller too
            if (sellerElement != null) {
                String ratingStr = sellerElement.getElementsByClass("big").first().text();

                double rating = ratingStr.equals("?") ? 0 : Double.parseDouble(ratingStr);
                //Select rating from string like "219 reviews over 2 years"
                int reviewCount = Integer.parseInt(sellerElement.getElementsByClass("text-mini text-light mb5").text()
                        .replaceAll("\\D.*", ""));

                List<PreviewOffer> previewOffers = new ArrayList<>();

                List<Element> previewOfferElements = funPayDocument.getElementsByClass("tc-item");

                for (Element previewOfferElement : previewOfferElements) {
                    Element previewOfferPriceElement = previewOfferElement.getElementsByClass("tc-price").first();

                    String previewOfferElementHrefAttributeValue = previewOfferElement.attr("href");

                    long offerId = Long.parseLong(previewOfferElementHrefAttributeValue.substring(33));
                    String shortDescription = previewOfferElement.getElementsByClass("tc-desc-text").text();
                    double price = Double.parseDouble(previewOfferPriceElement.attr("data-s"));
                    boolean isAutoDelivery = previewOfferPriceElement.getElementsByClass("auto-dlv-icon").first() != null;
                    //Since the promo value is not shown in the profile in offers
                    boolean isPromo = false;

                    previewOffers.add(PreviewOffer.builder()
                            .offerId(offerId)
                            .shortDescription(shortDescription)
                            .price(price)
                            .isAutoDelivery(isAutoDelivery)
                            .isPromo(isPromo)
                            .seller(PreviewSeller.builder()
                                    .userId(userId)
                                    .username(username)
                                    .avatarPhotoLink(avatarPhotoLink)
                                    .isOnline(isOnline)
                                    .reviewCount(reviewCount)
                                    .build())
                            .build());
                }

                List<SellerReview> lastReviews = new ArrayList<>();

                List<Element> lastReviewElements = funPayDocument.getElementsByClass("review-container");

                for (Element lastReviewElement : lastReviewElements) {
                    Element reviewCompiledReviewElement = lastReviewElement.getElementsByClass("review-compiled-review").first();
                    Element starsElement = reviewCompiledReviewElement.getElementsByClass("rating").first();

                    String[] gameTitlePriceSplit = reviewCompiledReviewElement.getElementsByClass("review-item-detail").text()
                            .split(", ");

                    String gameTitle = gameTitlePriceSplit[0];
                    //Select a floating point number from a string like "from 1111.32 ₽"
                    double price = Double.parseDouble(gameTitlePriceSplit[1].replaceAll("[^0-9.]", "").split("\\s+")[0]);
                    String text = reviewCompiledReviewElement.getElementsByClass("review-item-text").text();
                    int stars = 0;

                    //if the review has rating
                    if (starsElement != null) {
                        stars = Integer.parseInt(starsElement.child(0).className().substring(6));
                    }

                    lastReviews.add(SellerReview.builder()
                            .gameTitle(gameTitle)
                            .price(price)
                            .text(text)
                            .stars(stars)
                            .build());
                }

                return Seller.builder()
                        .id(userId)
                        .username(username)
                        .avatarPhotoLink(avatarPhotoLink)
                        .isOnline(isOnline)
                        .badges(badges)
                        .registeredAt(registeredAt)
                        .rating(rating)
                        .reviewCount(reviewCount)
                        .previewOffers(previewOffers)
                        .lastReviews(lastReviews)
                        .build();
            } else {
                return User.builder()
                        .id(userId)
                        .username(username)
                        .avatarPhotoLink(avatarPhotoLink)
                        .isOnline(isOnline)
                        .badges(badges)
                        .registeredAt(registeredAt)
                        .build();
            }

        } catch (IOException e) {
            throw new FunPayApiException(e.getMessage());
        }
    }

    private boolean isNonExistentFunPayPage(Document funPayHtmlPageBody) {
        Element pageContentFullElement = funPayHtmlPageBody.getElementsByClass("page-content-full").first();

        if (pageContentFullElement == null) {
            return false;
        }

        Element pageHeaderElement = pageContentFullElement
                .getElementsByClass("page-header")
                .first();

        return pageHeaderElement != null;
    }
}

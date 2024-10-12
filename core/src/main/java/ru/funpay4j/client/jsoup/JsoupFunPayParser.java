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
import org.jetbrains.annotations.Nullable;
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
import ru.funpay4j.util.FunPayUserUtil;

import java.io.IOException;
import java.text.ParseException;
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
    public Lot parseLot(long lotId) throws FunPayApiException {
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
                if (counterHrefAttributeValue.contains("chips")) continue;

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
                String previewOfferShortDescription = previewOffer.getElementsByClass("tc-desc-text").text();
                double previewOfferPrice = Double.parseDouble(previewOffer.getElementsByClass("tc-price").attr("data-s"));
                boolean isHasPreviewOfferAutoDelivery = previewOffer.getElementsByClass("auto-dlv-icon").first() != null;
                boolean isHasPreviewOfferPromo = previewOffer.getElementsByClass("promo-offer-icon").first() != null;

                String previewSellerDataHrefAttributeValue = previewOffer.getElementsByClass("avatar-photo")
                        .attr("data-href");
                Element previewSellerReviewCountElement = previewOffer.getElementsByClass("rating-mini-count").first();

                long previewSellerUserId = Long.parseLong(previewSellerDataHrefAttributeValue.substring(25, previewSellerDataHrefAttributeValue.length() - 1));
                String previewSellerUsername = previewOffer.getElementsByClass("media-user-name").text();
                String previewSellerAvatarPhotoLink = previewOfferSellerStyleAttributeValue.substring(22, previewOfferSellerStyleAttributeValue.length() - 2);
                boolean isPreviewSellerOnline = previewOffer.getElementsByClass("media media-user online style-circle").first() != null;
                int previewSellerReviewCount = previewSellerReviewCountElement == null ? 0 : Integer.parseInt(previewSellerReviewCountElement.text());

                //if the previewUser has a regular photo
                if (previewSellerAvatarPhotoLink.equals("/img/layout/avatar.png")) previewSellerAvatarPhotoLink = null;

                previewOffers.add(
                        PreviewOffer.builder()
                                .offerId(offerId)
                                .shortDescription(previewOfferShortDescription)
                                .price(previewOfferPrice)
                                .isAutoDelivery(isHasPreviewOfferAutoDelivery)
                                .isPromo(isHasPreviewOfferPromo)
                                .seller(
                                        PreviewSeller.builder()
                                                .userId(previewSellerUserId)
                                                .username(previewSellerUsername)
                                                .avatarPhotoLink(previewSellerAvatarPhotoLink)
                                                .isOnline(isPreviewSellerOnline)
                                                .reviewCount(previewSellerReviewCount)
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
            throw new FunPayApiException(e.getLocalizedMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<PromoGame> parsePromoGames(@NonNull String query) throws FunPayApiException {
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

                //Skip chips, as they are not supported yet
                if (titleElementHrefAttributeValue.contains("chips")) continue;

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
            throw new FunPayApiException(e.getLocalizedMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Offer parseOffer(long offerId) throws FunPayApiException {
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

            if (paramItemElements.size() == 1) {
                //if there is no shortDescription

                detailedDescription = paramItemElements.get(0).selectFirst("div").text();
            } else if (paramItemElements.size() >= 2) {
                shortDescription = paramItemElements.get(0).selectFirst("div").text();
                detailedDescription = paramItemElements.get(1).selectFirst("div").text();
            }

            boolean isAutoDelivery = !funPayDocument.getElementsByClass("offer-header-auto-dlv-label").isEmpty();
            //Select a floating point number from a string like "from 1111.32 ₽"
            double price = Double.parseDouble(totalPriceValue.replaceAll("[^0-9.]", "").split("\\s+")[0]);
            List<String> attachmentLinks = new ArrayList<>();

            if (paramItemElements.size() > 2) {
                //if the offer has attachments

                for (Element attachmentElement : paramItemElements.get(2).getElementsByClass("attachments-item")) {
                    String attachmentLink = attachmentElement.selectFirst("a").attr("href");

                    attachmentLinks.add(attachmentLink);
                }
            }

            Map<String, String> parameters = new HashMap<>();

            for (Element paramItemElement : paramListElement.getElementsByClass("row").first().getElementsByClass("col-xs-6")) {
                Element parameterElement = paramItemElement.getElementsByClass("param-item").first();

                String key = parameterElement.selectFirst("h5").text();
                String value = parameterElement.getElementsByClass("text-bold").text();

                parameters.put(key, value);
            }

            Element previewSellerUsernameElement = funPayDocument.getElementsByClass("media-user-name").first()
                    .selectFirst("a");
            Element previewSellerImgElement = funPayDocument.getElementsByClass("media-user").first()
                            .selectFirst("img");
            Element previewSellerReviewCountElement = funPayDocument.getElementsByClass("text-mini text-light mb5").first();

            String previewSellerUsernameElementHrefAttributeValue = previewSellerUsernameElement.attr("href");

            long previewSellerUserId = Long.parseLong(previewSellerUsernameElementHrefAttributeValue.substring(25, previewSellerUsernameElementHrefAttributeValue.length() - 1));
            String previewSellerUsername = previewSellerUsernameElement.text();
            String previewSellerAvatarPhotoLink = previewSellerImgElement.attr("src");

            //if the previewUser has a regular photo
            if (previewSellerAvatarPhotoLink.equals("/img/layout/avatar.png")) previewSellerAvatarPhotoLink = null;

            //Select rating from string like "219 reviews over 2 years"
            int previewSellerReviewCount = Integer.parseInt(previewSellerReviewCountElement.text().replaceAll("\\D.*", ""));
            boolean isPreviewSellerOnline = funPayDocument.getElementsByClass("media media-user online").first() != null;

            return Offer.builder()
                    .id(offerId)
                    .shortDescription(shortDescription)
                    .detailedDescription(detailedDescription)
                    .isAutoDelivery(isAutoDelivery)
                    .price(price)
                    .attachmentLinks(attachmentLinks)
                    .parameters(parameters)
                    .seller(PreviewSeller.builder()
                            .userId(previewSellerUserId)
                            .username(previewSellerUsername)
                            .avatarPhotoLink(previewSellerAvatarPhotoLink)
                            .reviewCount(previewSellerReviewCount)
                            .isOnline(isPreviewSellerOnline)
                            .build())
                    .build();
        } catch (IOException e) {
            throw new FunPayApiException(e.getLocalizedMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User parseUser(long userId) throws FunPayApiException {
        try (Response funPayHtmlPage = httpClient.newCall(new Request.Builder().get().url(baseURL + "/users/" + userId + "/").build()).execute()) {
            String funPayHtmlPageBody = funPayHtmlPage.body().string();

            Document funPayDocument = Jsoup.parse(funPayHtmlPageBody);

            if (isNonExistentFunPayPage(funPayDocument)) {
                throw new FunPayApiException("User with userId " + userId + " does not exist");
            }

            Element containerProfileHeader = funPayDocument.getElementsByClass("container profile-header").first();

            Element profileElement = funPayDocument.getElementsByClass("profile").first();

            Element mediaUserStatusElement = profileElement.getElementsByClass("media-user-status").first();
            Element avatarPhotoElement = containerProfileHeader.getElementsByClass("avatar-photo").first();
            Element userBadgesElement = profileElement.getElementsByClass("user-badges").first();

            String avatarPhotoElementStyle = avatarPhotoElement.attr("style");

            String username = profileElement.getElementsByClass("mr4").text();
            String avatarPhotoLink = avatarPhotoElementStyle.substring(22, avatarPhotoElementStyle.length() - 2);

            //if the user has a regular photo
            if (avatarPhotoLink.equals("/img/layout/avatar.png")) avatarPhotoLink = null;

            boolean isOnline = profileElement.getElementsByClass("mb40 online").first() != null;
            List<String> badges = new ArrayList<>();

            if (userBadgesElement != null) {
                for (Element badgeElement : userBadgesElement.children()) {
                    badges.add(badgeElement.text());
                }
            }

            String registeredAtStr = profileElement.getElementsByClass("text-nowrap").first().text();
            Date registeredAt;

            try {
                registeredAt = FunPayUserUtil.convertRegisterDateStringToDate(registeredAtStr);
            } catch (ParseException e) {
                //might be the case if the account was created a few seconds/minutes/hours ago
                //such cases are not taken into account yet, so the logical thing to do is to cast a new Date
                registeredAt = new Date();
            }

            String lastSeenAtStr = mediaUserStatusElement == null ? "" : mediaUserStatusElement.text();
            Date lastSeenAt;

            if (lastSeenAtStr.contains("После регистрации на сайт не заходил")) {
                //if the user has not accessed the site after authorization

                lastSeenAt = new Date(registeredAt.getTime());
            } else if (lastSeenAtStr.contains("Онлайн")) {
                //if the user is online then the last time of login will be the current time

                lastSeenAt = new Date();
            } else {
                try {
                    lastSeenAt = FunPayUserUtil.convertLastSeenAtStringToDate(lastSeenAtStr);
                } catch (ParseException e) {
                    lastSeenAt = null;
                }
            }

            Element sellerElement = funPayDocument.getElementsByClass("param-item mb10").first();

            if (sellerElement != null) {
                //if user is seller too

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
                    String previewOfferShortDescription = previewOfferElement.getElementsByClass("tc-desc-text").text();
                    double previewOfferPrice = Double.parseDouble(previewOfferPriceElement.attr("data-s"));
                    boolean isHasPreviewOfferAutoDelivery = previewOfferPriceElement.getElementsByClass("auto-dlv-icon").first() != null;
                    //Since the promo value is not shown in the profile in offers
                    boolean isHasPreviewOfferPromo = false;

                    previewOffers.add(PreviewOffer.builder()
                            .offerId(offerId)
                            .shortDescription(previewOfferShortDescription)
                            .price(previewOfferPrice)
                            .isAutoDelivery(isHasPreviewOfferAutoDelivery)
                            .isPromo(isHasPreviewOfferPromo)
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

                extractReviewsFromReviewsHtml(funPayDocument, lastReviews);

                return Seller.builder()
                        .id(userId)
                        .username(username)
                        .avatarPhotoLink(avatarPhotoLink)
                        .isOnline(isOnline)
                        .badges(badges)
                        .lastSeenAt(lastSeenAt)
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
                        .lastSeenAt(lastSeenAt)
                        .registeredAt(registeredAt)
                        .build();
            }

        } catch (IOException e) {
            throw new FunPayApiException(e.getLocalizedMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<SellerReview> parseSellerReviews(long userId, int pages, @Nullable Integer starsFilter) throws FunPayApiException {
        List<SellerReview> currentSellerReviews = new ArrayList<>();

        String userIdFormData = String.valueOf(userId);
        String starsFilterFormData = starsFilter == null ? "" : String.valueOf(starsFilter);
        String continueArg = null;

        for (int currentPageCount = 0; currentPageCount < pages; currentPageCount++) {
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("user_id", userIdFormData)
                    .addFormDataPart("filter", starsFilterFormData)
                    .addFormDataPart("continue", continueArg == null ? "" : continueArg)
                    .build();

            try (Response response = httpClient.newCall(new Request.Builder().post(requestBody).url(baseURL + "/users/reviews")
                    .addHeader("x-requested-with", "XMLHttpRequest").build()).execute()) {
                if (response.code() == 404) throw new FunPayApiException("User with userId " + userId + " does not exist/seller");

                Document reviewsHtml = Jsoup.parse(response.body().string());

                extractReviewsFromReviewsHtml(reviewsHtml, currentSellerReviews);

                Element dynTableFormElement = reviewsHtml.getElementsByClass("dyn-table-form")
                        .first();

                if (dynTableFormElement == null) break;

                List<Element> inputElements = dynTableFormElement.select("input");

                Element continueElement = inputElements.isEmpty() ? null : inputElements.get(1);

                if (continueElement == null || continueElement.attr("value").isEmpty()) break;

                continueArg = continueElement.attr("value");
            } catch (IOException e) {
                throw new FunPayApiException(e.getLocalizedMessage());
            }
        }

        return currentSellerReviews;
    }

    private void extractReviewsFromReviewsHtml(Document reviewsHtml, List<SellerReview> currentSellerReviews) {
        List<Element> reviewContainerElements = reviewsHtml.getElementsByClass("review-container");

        for (Element lastReviewElement : reviewContainerElements) {
            Element reviewCompiledReviewElement = lastReviewElement.getElementsByClass("review-compiled-review").first();
            Element starsElement = reviewCompiledReviewElement.getElementsByClass("rating").first();

            String[] gameTitlePriceSplit = reviewCompiledReviewElement.getElementsByClass("review-item-detail").text()
                    .split(", ");

            String lastReviewGameTitle = gameTitlePriceSplit[0];
            //Select a floating point number from a string like "from 1111.32 ₽"
            double lastReviewPrice = Double.parseDouble(gameTitlePriceSplit[gameTitlePriceSplit.length - 1].replaceAll("[^0-9.]", "").split("\\s+")[0]);
            String lastReviewText = reviewCompiledReviewElement.getElementsByClass("review-item-text").text();
            int lastReviewStars = 0;

            if (starsElement != null) {
                //if the review has rating

                lastReviewStars = Integer.parseInt(starsElement.child(0).className().substring(6));
            }

            currentSellerReviews.add(SellerReview.builder()
                    .gameTitle(lastReviewGameTitle)
                    .price(lastReviewPrice)
                    .text(lastReviewText)
                    .stars(lastReviewStars)
                    .build());
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

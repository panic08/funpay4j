package ru.funpay4j.client.jsoup;

import com.google.gson.JsonParser;
import lombok.NonNull;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import ru.funpay4j.client.FunPayParser;
import ru.funpay4j.client.FunPayURL;
import ru.funpay4j.core.commands.game.GetOffer;
import ru.funpay4j.core.commands.game.GetPromoGames;
import ru.funpay4j.core.commands.lot.GetLot;
import ru.funpay4j.core.exceptions.FunPayApiException;
import ru.funpay4j.core.objects.game.PromoGame;
import ru.funpay4j.core.objects.game.PromoGameCounter;
import ru.funpay4j.core.objects.lot.Lot;
import ru.funpay4j.core.objects.lot.LotCounter;
import ru.funpay4j.core.objects.offer.Offer;
import ru.funpay4j.core.objects.offer.PreviewOffer;
import ru.funpay4j.core.objects.user.PreviewUser;

import java.io.IOException;
import java.util.*;

public class JsoupFunPayParser implements FunPayParser {
    @NonNull
    private final OkHttpClient httpClient;

    @NonNull
    private final String baseURL;

    public JsoupFunPayParser(@NotNull OkHttpClient httpClient) {
        this.httpClient = httpClient;
        this.baseURL = FunPayURL.DEFAULT_URL;
    }

    public JsoupFunPayParser(@NotNull OkHttpClient httpClient, @NotNull String baseURL) {
        this.httpClient = httpClient;
        this.baseURL = baseURL;
    }

    @Override
    public Lot parse(GetLot command) {
        String getLotByIdURL = baseURL + "/lots/" + command.getLotId() + "/";
        try (Response funPayHtmlPage = httpClient.newCall(new Request.Builder().get().url(getLotByIdURL).build()).execute()) {
            String funPayHtmlPageBody = funPayHtmlPage.body().string();

            Document funPayDocument = Jsoup.parse(funPayHtmlPageBody);

            if (isNonExistentFunPayPage(funPayDocument)) {
                throw new FunPayApiException("Lot with lotId " + command.getLotId() + " does not exist");
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

                int lotId = Integer.parseInt(counterHrefAttributeValue.substring(24, counterHrefAttributeValue.length() - 1));

                if (lotId == command.getLotId()) {
                    continue;
                }

                String counterParam = counterItem.getElementsByClass("counter-param").text();
                int counterValue = Integer.parseInt(counterItem.getElementsByClass("counter-value").text());

                lotCounters.add(
                        LotCounter.builder()
                                .lotId(lotId)
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
                Element sellerRatingCountElement = previewOffer.getElementsByClass("rating-mini-count").first();

                long sellerUserId = Long.parseLong(sellerDataHrefAttributeValue.substring(25, sellerDataHrefAttributeValue.length() - 1));
                String sellerUsername = previewOffer.getElementsByClass("media-user-name").text();
                boolean isSellerOnline = previewOffer.getElementsByClass("media media-user online style-circle").first() != null;
                int sellerRatingCount = sellerRatingCountElement == null ? 0 : Integer.parseInt(sellerRatingCountElement.text());

                previewOffers.add(
                        PreviewOffer.builder()
                                .offerId(offerId)
                                .shortDescription(shortDescription)
                                .price(price)
                                .isAutoDelivery(isAutoDelivery)
                                .isPromo(isPromo)
                                .seller(
                                        PreviewUser.builder()
                                                .avatarPhotoLink(previewOfferSellerStyleAttributeValue.substring(22, previewOfferSellerStyleAttributeValue.length() - 2))
                                                .userId(sellerUserId)
                                                .username(sellerUsername)
                                                .isOnline(isSellerOnline)
                                                .ratingCount(sellerRatingCount)
                                                .build()
                                )
                                .build()
                );
            }

            return Lot.builder()
                    .id(command.getLotId())
                    .title(title)
                    .description(description)
                    .lotCounters(lotCounters)
                    .previewOffers(previewOffers)
                    .build();
        } catch (IOException e) {
            throw new FunPayApiException(e.getMessage());
        }
    }

    @Override
    public List<PromoGame> parse(GetPromoGames command) {
        List<PromoGame> currentPromoGames = new ArrayList<>();

        String getPromoGamesURL = baseURL + "/games/promoFilter";
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("query", command.getQuery())
                .build();

        try (Response response = httpClient.newCall(new Request.Builder().post(requestBody).url(getPromoGamesURL)
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

    @Override
    public Offer parse(GetOffer command) {
        String getLotByIdURL = baseURL + "/lots/offer?id=" + command.getOfferId();
        try (Response funPayHtmlPage = httpClient.newCall(new Request.Builder().get().url(getLotByIdURL).build()).execute()) {
            String funPayHtmlPageBody = funPayHtmlPage.body().string();

            Document funPayDocument = Jsoup.parse(funPayHtmlPageBody);

            if (isNonExistentFunPayPage(funPayDocument)) {
                throw new FunPayApiException("Offer with offerId " + command.getOfferId() + " does not exist");
            }

            Element paramListElement = funPayDocument.getElementsByClass("param-list").first();
            List<Element> paramItemElements = funPayDocument.select(".param-list > .param-item");

            String totalPriceValue = funPayDocument.getElementsByClass("form-control input-lg selectpicker")
                    .first()
                    .children()
                    .get(0)
                    .attr("data-content");

            String shortDescription = null;
            String detailedDescription = null;

            if (paramItemElements.size() == 1) {
                detailedDescription = paramItemElements.get(0).selectFirst("div").text();
            } else if (paramItemElements.size() >= 2) {
                shortDescription = paramItemElements.get(0).selectFirst("div").text();
                detailedDescription = paramItemElements.get(1).selectFirst("div").text();
            }

            boolean isAutoDelivery = !funPayDocument.getElementsByClass("offer-header-auto-dlv-label").isEmpty();
            //Select a floating point number from a string like “jgrejgjer23.99gfdgf”
            double price = Double.parseDouble(totalPriceValue.replaceAll("[^0-9.]", "").split("\\s+")[0]);
            List<String> attachmentLinks = new ArrayList<>();
            Map<String, String> parameters = new HashMap<>();

            if (paramItemElements.size() > 2) {
                for (Element attachmentElement : paramItemElements.get(2).getElementsByClass("attachments-item")) {
                    String attachmentLink = attachmentElement.selectFirst("a").attr("href");

                    attachmentLinks.add(attachmentLink);
                }
            }

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
            Element sellerRatingCountElement = funPayDocument.getElementsByClass("text-mini text-light mb5").first();

            String sellerUsernameElementHrefAttributeValue = sellerUsernameElement.attr("href");

            long sellerUserId = Long.parseLong(sellerUsernameElementHrefAttributeValue.substring(25, sellerUsernameElementHrefAttributeValue.length() - 1));
            String sellerUsername = sellerUsernameElement.text();
            String sellerAvatarPhotoLink = sellerImgElement.attr("src");
            //Select price from string
            int sellerRatingCount = Integer.parseInt(sellerRatingCountElement.text().replaceAll("\\D.*", ""));
            boolean isSellerOnline = funPayDocument.getElementsByClass("media media-user online").first() != null;

            return Offer.builder()
                    .id(command.getOfferId())
                    .shortDescription(shortDescription)
                    .detailedDescription(detailedDescription)
                    .isAutoDelivery(isAutoDelivery)
                    .price(price)
                    .attachmentLinks(attachmentLinks)
                    .parameters(parameters)
                    .seller(PreviewUser.builder()
                            .userId(sellerUserId)
                            .username(sellerUsername)
                            .avatarPhotoLink(sellerAvatarPhotoLink)
                            .ratingCount(sellerRatingCount)
                            .isOnline(isSellerOnline)
                            .build())
                    .build();
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

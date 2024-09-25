package ru.funpay4j.client.jsoup;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import ru.funpay4j.client.FunPayParser;
import ru.funpay4j.client.FunPayURL;
import ru.funpay4j.core.commands.lot.GetLot;
import ru.funpay4j.core.exceptions.FunPayApiException;
import ru.funpay4j.core.objects.lot.Lot;
import ru.funpay4j.core.objects.lot.LotCounter;
import ru.funpay4j.core.objects.offer.PreviewOffer;
import ru.funpay4j.core.objects.user.PreviewUser;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class JsoupFunPayParser implements FunPayParser {
    private final OkHttpClient httpClient;
    private final String baseURL;

    public JsoupFunPayParser(OkHttpClient httpClient) {
        this.httpClient = Objects.requireNonNull(httpClient);
        this.baseURL = FunPayURL.DEFAULT_URL;
    }

    public JsoupFunPayParser(OkHttpClient httpClient, String baseURL) {
        this.httpClient = Objects.requireNonNull(httpClient);
        this.baseURL = Objects.requireNonNull(baseURL);
    }

    @Override
    public Lot parse(GetLot command) {
        Lot currentLot = new Lot();

        currentLot.setId(command.getLotId());
        currentLot.setLotCounters(new LinkedList<>());
        currentLot.setPreviewOffers(new LinkedList<>());

        String getLotByIdURL = baseURL + "/lots/" + command.getLotId() + "/";
        try (Response funPayHtmlPage = httpClient.newCall(new Request.Builder().get().url(getLotByIdURL).build()).execute()){
            String funPayHtmlPageBody = funPayHtmlPage.body().string();

            Document funPayDocument = Jsoup.parse(funPayHtmlPageBody);

            Element funPayContentWithCdElement = funPayDocument.getElementsByClass("content-with-cd").first();

            String lotTitle = funPayContentWithCdElement.selectFirst("h1").text();
            String lotDescription = funPayContentWithCdElement.selectFirst("p").text();

            currentLot.setTitle(lotTitle);
            currentLot.setDescription(lotDescription);

            List<Element> funPayCountersElements = funPayDocument.getElementsByClass("counter-list")
                    .first()
                    .select("a");

            for (Element counterItem : funPayCountersElements) {
                String counterHrefAttributeValue = counterItem.attr("href");

                int lotId = Integer.parseInt(counterHrefAttributeValue.substring(24, counterHrefAttributeValue.length() - 1));

                if (lotId == command.getLotId()) {
                    continue;
                }

                String counterParam = counterItem.getElementsByClass("counter-param").text();
                int counterValue = Integer.parseInt(counterItem.getElementsByClass("counter-value").text());

                currentLot.getLotCounters().add(
                        LotCounter.builder()
                                .lotId(lotId)
                                .param(counterParam)
                                .counter(counterValue)
                                .build()
                );
            }

            List<Element> funPayPreviewOffersElements = funPayDocument.getElementsByClass("tc table-hover table-clickable tc-short showcase-table tc-lazyload tc-sortable showcase-has-promo")
                    .first()
                    .select("a");

            for (Element offerItem : funPayPreviewOffersElements) {
                String offerHrefAttributeValue = offerItem.attr("href");
                String offerSellerStyleAttributeValue = offerItem.getElementsByClass("avatar-photo").attr("style");

                long offerId = Long.parseLong(offerHrefAttributeValue.substring(33));
                String shortDescription = offerItem.getElementsByClass("tc-desc-text").text();
                double price = Double.parseDouble(offerItem.getElementsByClass("tc-price").attr("data-s"));
                boolean isAutoDelivery = offerItem.getElementsByClass("auto-dlv-icon").first() != null;
                boolean isPromo = offerItem.getElementsByClass("promo-offer-icon").first() != null;

                String sellerDataHrefAttributeValue = offerItem.getElementsByClass("avatar-photo")
                        .attr("data-href");
                Element sellerRatingCountElement = offerItem.getElementsByClass("rating-mini-count").first();

                long sellerUserId = Long.parseLong(sellerDataHrefAttributeValue.substring(25, sellerDataHrefAttributeValue.length() - 1));
                String sellerUsername = offerItem.getElementsByClass("media-user-name").text();
                boolean isSellerOnline = offerItem.getElementsByClass("media media-user online style-circle").first() != null;
                int sellerRatingCount = sellerRatingCountElement == null ? 0 : Integer.parseInt(sellerRatingCountElement.text());

                currentLot.getPreviewOffers().add(
                        PreviewOffer.builder()
                                .offerId(offerId)
                                .shortDescription(shortDescription)
                                .price(price)
                                .isAutoDelivery(isAutoDelivery)
                                .isPromo(isPromo)
                                .seller(
                                        PreviewUser.builder()
                                                .avatarPhotoLink(offerSellerStyleAttributeValue.substring(22, offerSellerStyleAttributeValue.length() - 2))
                                                .userId(sellerUserId)
                                                .username(sellerUsername)
                                                .isOnline(isSellerOnline)
                                                .ratingCount(sellerRatingCount)
                                                .build()
                                )
                                .build()
                );
            }

        } catch (IOException e) {
            throw new FunPayApiException(e.getMessage());
        }

        return currentLot;
    }
}

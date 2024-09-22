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
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
        String getLotURL = baseURL + "/lots/" + command.getLotId() + "/";

        try (Response funPayHtmlPage = httpClient.newCall(new Request.Builder().get().url(getLotURL).build()).execute()) {
            String funPayHtmlPageBody = Objects.requireNonNull(funPayHtmlPage.body()).string();
            Document funPayDocument = Jsoup.parse(funPayHtmlPageBody);

            return parseLot(funPayDocument, command);
        } catch (IOException e) {
            throw new FunPayApiException("Error fetching or parsing data " + e.getMessage());
        }
    }

    private Lot parseLot(Document doc, GetLot command) {
        Lot lot = new Lot();
        lot.setId(command.getLotId());

        Element contentElement = doc.getElementsByClass("content-with-cd").first();
        lot.setTitle(contentElement.selectFirst("h1").text());
        lot.setDescription(contentElement.selectFirst("p").text());

        lot.setCounters(parseCounters(doc, command));
        lot.setPreviewOffers(parsePreviewOffers(doc));

        return lot;
    }

    private List<LotCounter> parseCounters(Document doc, GetLot command) {
        return doc.getElementsByClass("counter-list").first()
                .select("a").stream()
                .map(this::parseCounter)
                .filter(counter -> counter.getLotId() != command.getLotId())
                .collect(Collectors.toList());
    }

    private LotCounter parseCounter(Element counterItem) {
        String href = counterItem.attr("href");
        int lotId = Integer.parseInt(href.substring(24, href.length() - 1));
        String param = counterItem.getElementsByClass("counter-param").text();
        int value = Integer.parseInt(counterItem.getElementsByClass("counter-value").text());

        return LotCounter.builder()
                .lotId(lotId)
                .param(param)
                .counter(value)
                .build();
    }

    private List<PreviewOffer> parsePreviewOffers(Document doc) {
        return doc.getElementsByClass("tc table-hover table-clickable tc-short showcase-table tc-lazyload tc-sortable showcase-has-promo").first()
                .select("a").stream()
                .map(this::parsePreviewOffer)
                .collect(Collectors.toList());
    }

    private PreviewOffer parsePreviewOffer(Element offerItem) {
        long offerId = Long.parseLong(offerItem.attr("href").substring(33));
        String shortDescription = offerItem.getElementsByClass("tc-desc-text").text();
        double price = Double.parseDouble(offerItem.getElementsByClass("tc-price").attr("data-s"));
        boolean isAutoDelivery = offerItem.getElementsByClass("auto-dlv-icon").first() != null;
        boolean isPromo = offerItem.getElementsByClass("promo-offer-icon").first() != null;

        Element ratingCountElement = offerItem.getElementsByClass("rating-mini-count").first();
        int sellerRatingCount = ratingCountElement == null ? 0 : Integer.parseInt(ratingCountElement.text());

        PreviewUser seller = PreviewUser.builder()
                .userId(parseUserIdFromPreviewOffer(offerItem))
                .username(offerItem.getElementsByClass("media-user-name").text())
                .avatarPhotoLink(parseUserAvatarPhotoLinkFromPreviewOffer(offerItem))
                .isOnline(offerItem.getElementsByClass("media media-user online style-circle").first() != null)
                .ratingCount(sellerRatingCount)
                .build();

        return PreviewOffer.builder()
                .offerId(offerId)
                .shortDescription(shortDescription)
                .price(price)
                .isAutoDelivery(isAutoDelivery)
                .isPromo(isPromo)
                .seller(seller)
                .build();
    }

    private long parseUserIdFromPreviewOffer(Element offerItem) {
        String href = offerItem.getElementsByClass("avatar-photo").attr("data-href");
        return Long.parseLong(href.substring(25, href.length() - 1));
    }

    private String parseUserAvatarPhotoLinkFromPreviewOffer(Element offerItem) {
        String style = offerItem.getElementsByClass("avatar-photo").attr("style");
        return style.substring(22, style.length() - 2);
    }
}

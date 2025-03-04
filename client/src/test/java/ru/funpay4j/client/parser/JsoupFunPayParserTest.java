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

package ru.funpay4j.client.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ru.funpay4j.client.exceptions.lot.LotNotFoundException;
import ru.funpay4j.client.exceptions.offer.OfferNotFoundException;
import ru.funpay4j.client.exceptions.user.UserNotFoundException;
import ru.funpay4j.client.objects.CsrfTokenAndPHPSESSID;
import ru.funpay4j.client.objects.game.ParsedPromoGame;
import ru.funpay4j.client.objects.game.ParsedPromoGameCounter;
import ru.funpay4j.client.objects.lot.ParsedLot;
import ru.funpay4j.client.objects.lot.ParsedLotCounter;
import ru.funpay4j.client.objects.offer.ParsedOffer;
import ru.funpay4j.client.objects.offer.ParsedPreviewOffer;
import ru.funpay4j.client.objects.user.ParsedAdvancedSellerReview;
import ru.funpay4j.client.objects.user.ParsedPreviewSeller;
import ru.funpay4j.client.objects.user.ParsedSeller;
import ru.funpay4j.client.objects.user.ParsedSellerReview;
import ru.funpay4j.client.objects.user.ParsedUser;

/**
 * @author panic08
 * @since 1.0.6
 */
class JsoupFunPayParserTest {
    private OkHttpClient httpClient;
    private MockWebServer mockWebServer;
    private JsoupFunPayParser parser;

    private static final String GET_LOT_HTML_RESPONSE_PATH =
            "src/test/resources/html/client/getLotResponse.html";
    private static final String GET_PROMO_GAMES_JSON_RESPONSE_PATH =
            "src/test/resources/json/client/getPromoGamesResponse.json";
    private static final String GET_OFFER_HTML_RESPONSE_PATH =
            "src/test/resources/html/client/getOfferResponse.html";
    private static final String GET_USER_HTML_RESPONSE_PATH =
            "src/test/resources/html/client/getUserResponse.html";
    private static final String GET_SELLER_REVIEWS_HTML_RESPONSE_PATH =
            "src/test/resources/html/client/getSellerReviewsResponse.html";
    private static final String BASE_URL = "/";

    @BeforeEach
    void setUp() throws IOException {
        this.httpClient = new OkHttpClient();
        this.mockWebServer = new MockWebServer();
        this.mockWebServer.start();
        this.parser =
                new JsoupFunPayParser(this.httpClient, this.mockWebServer.url(BASE_URL).toString());
    }

    @AfterEach
    void tearDown() throws IOException {
        this.mockWebServer.shutdown();
    }

    @Test
    void testParseLot() throws Exception {
        String htmlContent = new String(Files.readAllBytes(Paths.get(GET_LOT_HTML_RESPONSE_PATH)));
        mockWebServer.enqueue(new MockResponse().setBody(htmlContent).setResponseCode(200));

        long lotId = 149L;
        ParsedLot result = parser.parseLot(lotId);

        assertNotNull(result);
        assertEquals(lotId, result.getId());
        assertNotNull(result.getTitle());
        assertNotNull(result.getDescription());
        assertEquals(41, result.getGameId());
        assertFalse(result.getLotCounters().isEmpty());
        assertFalse(result.getPreviewOffers().isEmpty());

        ParsedLotCounter lotCounter = result.getLotCounters().get(0);
        assertTrue(lotCounter.getLotId() > 0);
        assertNotNull(lotCounter.getParam());
        assertTrue(lotCounter.getCounter() >= 0);

        ParsedPreviewOffer previewOffer = result.getPreviewOffers().get(0);
        assertTrue(previewOffer.getOfferId() > 0);
        assertNotNull(previewOffer.getShortDescription());
        assertTrue(previewOffer.getPrice() > 0);
        assertNotNull(previewOffer.getSeller());
        assertTrue(previewOffer.getSeller().getUserId() > 0);
        assertNotNull(previewOffer.getSeller().getUsername());
    }

    @Test
    void testParseLotNotFound() throws Exception {
        String notFoundHtml =
                "<div class=\"page-content-full\"><div class=\"page-header\"></div></div>";
        mockWebServer.enqueue(new MockResponse().setBody(notFoundHtml).setResponseCode(200));

        long lotId = 999L;
        assertThrows(LotNotFoundException.class, () -> parser.parseLot(lotId));
    }

    @Test
    void testParsePromoGames() throws Exception {
        String jsonContent =
                new String(Files.readAllBytes(Paths.get(GET_PROMO_GAMES_JSON_RESPONSE_PATH)));
        mockWebServer.enqueue(new MockResponse().setBody(jsonContent).setResponseCode(200));

        String query = "dota";
        List<ParsedPromoGame> result = parser.parsePromoGames(query);

        assertNotNull(result);
        assertFalse(result.isEmpty());

        ParsedPromoGame promoGame = result.get(0);
        assertTrue(promoGame.getLotId() > 0);
        assertNotNull(promoGame.getTitle());
        assertFalse(promoGame.getPromoGameCounters().isEmpty());

        ParsedPromoGameCounter promoGameCounter = promoGame.getPromoGameCounters().get(0);
        assertTrue(promoGameCounter.getLotId() > 0);
        assertNotNull(promoGameCounter.getTitle());
    }

    @Test
    void testParseOffer() throws Exception {
        String htmlContent =
                new String(Files.readAllBytes(Paths.get(GET_OFFER_HTML_RESPONSE_PATH)));
        mockWebServer.enqueue(new MockResponse().setBody(htmlContent).setResponseCode(200));

        long offerId = 33502824L;
        ParsedOffer result = parser.parseOffer(offerId);

        assertNotNull(result);
        assertEquals(offerId, result.getId());
        assertNotNull(result.getDetailedDescription());
        assertTrue(result.isAutoDelivery());
        assertFalse(result.getAttachmentLinks().isEmpty());
        assertFalse(result.getParameters().isEmpty());
        assertTrue(result.getPrice() > 0);

        Map<String, String> parameters = result.getParameters();
        assertFalse(parameters.isEmpty());
        assertTrue(parameters.keySet().stream().anyMatch(key -> !key.isEmpty()));
        assertTrue(parameters.values().stream().anyMatch(value -> !value.isEmpty()));

        ParsedPreviewSeller seller = result.getSeller();
        assertNotNull(seller);
        assertTrue(seller.getUserId() > 0);
        assertNotNull(seller.getUsername());
        assertTrue(seller.isOnline());
        assertTrue(seller.getReviewCount() > 0);
    }

    @Test
    void testParseOfferNotFound() throws Exception {
        String notFoundHtml =
                "<div class=\"page-content-full\"><div class=\"page-header\"></div></div>";
        mockWebServer.enqueue(new MockResponse().setBody(notFoundHtml).setResponseCode(200));

        long offerId = 99999999L;
        assertThrows(OfferNotFoundException.class, () -> parser.parseOffer(offerId));
    }

    @Test
    void testParseUserWithoutGoldenKey() throws Exception {
        String htmlContent = new String(Files.readAllBytes(Paths.get(GET_USER_HTML_RESPONSE_PATH)));
        mockWebServer.enqueue(new MockResponse().setBody(htmlContent).setResponseCode(200));

        long userId = 2L;
        ParsedUser result = parser.parseUser(userId);

        assertNotNull(result);
        assertEquals(userId, result.getId());
        assertNotNull(result.getUsername());
        assertNotNull(result.getRegisteredAt());
        assertFalse(result.isOnline());
        assertFalse(result.getBadges().isEmpty());

        assertTrue(result instanceof ParsedSeller);
        ParsedSeller seller = (ParsedSeller) result;
        assertTrue(seller.getRating() >= 0);
        assertTrue(seller.getReviewCount() > 0);
        assertFalse(seller.getLastReviews().isEmpty());
        assertFalse(seller.getPreviewOffers().isEmpty());

        ParsedSellerReview review = seller.getLastReviews().get(0);
        assertNotNull(review.getGameTitle());
        assertTrue(review.getPrice() > 0);
        assertNotNull(review.getText());

        ParsedPreviewOffer previewOffer = seller.getPreviewOffers().get(0);
        assertTrue(previewOffer.getOfferId() > 0);
        assertNotNull(previewOffer.getShortDescription());
        assertTrue(previewOffer.getPrice() > 0);
    }

    @Test
    void testParseUserWithGoldenKey() throws Exception {
        String htmlContent = new String(Files.readAllBytes(Paths.get(GET_USER_HTML_RESPONSE_PATH)));
        mockWebServer.enqueue(new MockResponse().setBody(htmlContent).setResponseCode(200));

        String goldenKey = "some_golden_key";
        long userId = 2L;
        ParsedUser result = parser.parseUser(goldenKey, userId);

        assertNotNull(result);
        assertEquals(userId, result.getId());
        assertNotNull(result.getUsername());
        assertNotNull(result.getRegisteredAt());
        assertFalse(result.isOnline());
        assertFalse(result.getBadges().isEmpty());

        assertTrue(result instanceof ParsedSeller);
        ParsedSeller seller = (ParsedSeller) result;
        assertTrue(seller.getRating() >= 0);
        assertTrue(seller.getReviewCount() > 0);
        assertFalse(seller.getLastReviews().isEmpty());
        assertFalse(seller.getPreviewOffers().isEmpty());
    }

    @Test
    void testParseUserNotFound() throws Exception {
        String notFoundHtml =
                "<div class=\"page-content-full\"><div class=\"page-header\"></div></div>";
        mockWebServer.enqueue(new MockResponse().setBody(notFoundHtml).setResponseCode(200));

        long userId = 999L;
        assertThrows(UserNotFoundException.class, () -> parser.parseUser(userId));
    }

    @Test
    void testParseSellerReviews() throws Exception {
        String htmlContent =
                new String(Files.readAllBytes(Paths.get(GET_SELLER_REVIEWS_HTML_RESPONSE_PATH)));
        mockWebServer.enqueue(new MockResponse().setBody(htmlContent).setResponseCode(200));

        long userId = 2L;
        int pages = 1;
        List<ParsedSellerReview> result = parser.parseSellerReviews(userId, pages);

        assertNotNull(result);
        assertEquals(2, result.size());

        ParsedSellerReview firstReview = result.get(0);
        assertTrue(firstReview instanceof ParsedAdvancedSellerReview);
        ParsedAdvancedSellerReview advancedReview = (ParsedAdvancedSellerReview) firstReview;
        assertNotNull(advancedReview.getSenderUsername());
        assertNotNull(advancedReview.getOrderId());
        assertNull(advancedReview.getSenderAvatarLink());
        assertNotNull(advancedReview.getText());
        assertNotNull(advancedReview.getGameTitle());
        assertNotNull(advancedReview.getCreatedAt());
        assertNull(advancedReview.getSellerReplyText());

        ParsedSellerReview secondReview = result.get(1);
        assertNotNull(secondReview.getSellerReplyText());
        assertFalse(secondReview.getSellerReplyText().isEmpty());
    }

    @Test
    void testParseSellerReviewsUserNotFound() throws Exception {
        mockWebServer.enqueue(new MockResponse().setResponseCode(404));

        long userId = 999L;
        int pages = 1;
        assertThrows(UserNotFoundException.class, () -> parser.parseSellerReviews(userId, pages));
    }

    @Test
    void testParseCsrfTokenAndPHPSESSID() throws Exception {
        String goldenKey = "some_golden_key";
        String csrfToken = "some_csrf_token";
        String phpSessId = "some_phpsessid";
        String html = "<body data-app-data='{\"csrf-token\": \"" + csrfToken + "\"}'></body>";
        mockWebServer.enqueue(
                new MockResponse()
                        .setBody(html)
                        .addHeader("Set-Cookie", "PHPSESSID=" + phpSessId + "; path=/")
                        .setResponseCode(200));

        CsrfTokenAndPHPSESSID result = parser.parseCsrfTokenAndPHPSESSID(goldenKey);

        assertNotNull(result);
        assertEquals(csrfToken, result.getCsrfToken());
        assertEquals(phpSessId, result.getPHPSESSID());
    }
}

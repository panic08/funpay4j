package io.github.therepanic.funpay4j;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.github.therepanic.funpay4j.commands.game.GetPromoGames;
import io.github.therepanic.funpay4j.commands.lot.GetLot;
import io.github.therepanic.funpay4j.commands.offer.GetOffer;
import io.github.therepanic.funpay4j.commands.user.GetSellerReviews;
import io.github.therepanic.funpay4j.commands.user.GetUser;
import io.github.therepanic.funpay4j.objects.game.PromoGame;
import io.github.therepanic.funpay4j.objects.lot.Lot;
import io.github.therepanic.funpay4j.objects.offer.Offer;
import io.github.therepanic.funpay4j.objects.user.AdvancedSellerReview;
import io.github.therepanic.funpay4j.objects.user.Seller;
import io.github.therepanic.funpay4j.objects.user.SellerReview;

/**
 * @author therepanic
 * @since 1.0.0
 */
class FunPayExecutorTest {
    private FunPayExecutor funPayExecutor;

    private MockWebServer mockWebServer;

    private static final String GET_USER_HTML_RESPONSE_PATH = "html/client/getUserResponse.html";
    private static final String GET_LOT_HTML_RESPONSE_PATH = "html/client/getLotResponse.html";
    private static final String GET_OFFER_HTML_RESPONSE_PATH = "html/client/getOfferResponse.html";
    private static final String GET_SELLER_REVIEWS_HTML_RESPONSE_PATH =
            "html/client/getSellerReviewsResponse.html";
    private static final String GET_PROMO_GAMES_JSON_RESPONSE_PATH =
            "json/client/getPromoGamesResponse.json";

    @BeforeEach
    void setUp() {
        this.mockWebServer = new MockWebServer();
        this.funPayExecutor = new FunPayExecutor(this.mockWebServer.url("/").toString());
    }

    @AfterEach
    void tearDown() throws Exception {
        mockWebServer.shutdown();
    }

    @Test
    void testGetLot() throws Exception {
        String htmlContent = readResource(GET_LOT_HTML_RESPONSE_PATH);

        mockWebServer.enqueue(new MockResponse().setBody(htmlContent).setResponseCode(200));

        Lot result = funPayExecutor.execute(GetLot.builder().lotId(149L).build());

        assertNotNull(result);
        assertFalse(result.getPreviewOffers().isEmpty());
        assertFalse(result.getLotCounters().isEmpty());
        assertEquals(result.getGameId(), 41);
    }

    @Test
    void testGetPromoGames() throws Exception {
        String jsonContent = readResource(GET_PROMO_GAMES_JSON_RESPONSE_PATH);

        mockWebServer.enqueue(new MockResponse().setBody(jsonContent).setResponseCode(200));

        List<PromoGame> result =
                funPayExecutor.execute(GetPromoGames.builder().query("dota").build());

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertFalse(result.get(0).getPromoGameCounters().isEmpty());
    }

    @Test
    void testGetOffer() throws Exception {
        String htmlContent = readResource(GET_OFFER_HTML_RESPONSE_PATH);

        mockWebServer.enqueue(new MockResponse().setBody(htmlContent).setResponseCode(200));

        Offer result = funPayExecutor.execute(GetOffer.builder().offerId(33502824L).build());

        assertNotNull(result);
        assertTrue(result.isAutoDelivery());
        assertFalse(result.getAttachmentLinks().isEmpty());
        assertFalse(result.getParameters().isEmpty());
        assertNotNull(result.getSeller());
        assertTrue(result.getSeller().isOnline());
    }

    @Test
    void testGetUser() throws Exception {
        String htmlContent = readResource(GET_USER_HTML_RESPONSE_PATH);

        mockWebServer.enqueue(new MockResponse().setBody(htmlContent).setResponseCode(200));

        Seller result = (Seller) funPayExecutor.execute(GetUser.builder().userId(2L).build());

        assertNotNull(result);
        assertNotNull(result.getRegisteredAt());
        assertFalse(result.isOnline());
        assertFalse(result.getBadges().isEmpty());
        assertFalse(result.getLastReviews().isEmpty());
        assertFalse(result.getPreviewOffers().isEmpty());
    }

    @Test
    void testGetSellerReviews() throws Exception {
        String htmlContent = readResource(GET_SELLER_REVIEWS_HTML_RESPONSE_PATH);

        mockWebServer.enqueue(new MockResponse().setBody(htmlContent).setResponseCode(200));

        List<SellerReview> result =
                funPayExecutor.execute(GetSellerReviews.builder().pages(1).userId(2L).build());

        assertEquals(2, result.size());

        AdvancedSellerReview firstSellerReview = (AdvancedSellerReview) result.get(0);
        assertNotNull(firstSellerReview.getSenderUsername());
        assertNotNull(firstSellerReview.getOrderId());
        assertNull(firstSellerReview.getSenderAvatarLink());
        assertNotNull(firstSellerReview.getText());
        assertNotNull(firstSellerReview.getGameTitle());
        assertNotNull(firstSellerReview.getCreatedAt());

        assertNull(firstSellerReview.getSellerReplyText());

        SellerReview secondSellerReview = result.get(1);
        assertTrue(
                secondSellerReview.getSellerReplyText() != null
                        && !secondSellerReview.getSellerReplyText().isEmpty());
    }

    private static String readResource(String resourcePath) throws IOException {
        try (InputStream is =
                FunPayExecutorTest.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (is == null) {
                throw new FileNotFoundException("Resource not found: " + resourcePath);
            }
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            byte[] data = new byte[4096];
            int nRead;
            while ((nRead = is.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }
            return new String(buffer.toByteArray(), StandardCharsets.UTF_8);
        }
    }
}

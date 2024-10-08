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

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.funpay4j.core.FunPayExecutor;
import ru.funpay4j.core.commands.offer.GetOffer;
import ru.funpay4j.core.commands.game.GetPromoGames;
import ru.funpay4j.core.commands.lot.GetLot;
import ru.funpay4j.core.commands.user.GetUser;
import ru.funpay4j.core.objects.game.PromoGame;
import ru.funpay4j.core.objects.lot.Lot;
import ru.funpay4j.core.objects.offer.Offer;
import ru.funpay4j.core.objects.user.Seller;
import ru.funpay4j.util.FunPayUserUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author panic08
 * @since 1.0.0
 */
class FunPayExecutorTest {
    private FunPayExecutor funPayExecutor;
    private MockWebServer mockWebServer;

    private static final String GET_USER_HTML_RESPONSE_PATH = "src/test/resources/html/client/getUserResponse.html";
    private static final String GET_LOT_HTML_RESPONSE_PATH = "src/test/resources/html/client/getLotResponse.html";
    private static final String GET_OFFER_HTML_RESPONSE_PATH = "src/test/resources/html/client/getOfferResponse.html";
    private static final String GET_PROMO_GAMES_JSON_RESPONSE_PATH = "src/test/resources/json/client/getPromoGamesResponse.json";

    @BeforeEach
    void setUp() {
        this.mockWebServer = new MockWebServer();
        this.funPayExecutor = new FunPayExecutor(this.mockWebServer.url("/").toString());
    }

    @AfterEach
    void tearDown() throws IOException {
        this.mockWebServer.shutdown();
    }

    @Test
    void testGetLot() throws IOException {
        String htmlContent = new String(Files.readAllBytes(Paths.get(GET_LOT_HTML_RESPONSE_PATH)));

        this.mockWebServer.enqueue(
                new MockResponse()
                        .setBody(htmlContent)
                        .setResponseCode(200)
        );

        Lot result = funPayExecutor.execute(GetLot.builder().lotId(149).build());

        assertNotNull(result);
        assertFalse(result.getPreviewOffers().isEmpty());
        assertFalse(result.getLotCounters().isEmpty());
    }

    @Test
    void testGetPromoGames() throws IOException {
        String jsonContent = new String(Files.readAllBytes(Paths.get(GET_PROMO_GAMES_JSON_RESPONSE_PATH)));

        this.mockWebServer.enqueue(
                new MockResponse()
                        .setBody(jsonContent)
                        .setResponseCode(200)
        );

        List<PromoGame> result = funPayExecutor.execute(GetPromoGames.builder().query("dota").build());

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertFalse(result.get(0).getPromoGameCounters().isEmpty());
    }

    @Test
    void testGetOffer() throws IOException {
        String htmlContent = new String(Files.readAllBytes(Paths.get(GET_OFFER_HTML_RESPONSE_PATH)));

        this.mockWebServer.enqueue(
                new MockResponse()
                        .setBody(htmlContent)
                        .setResponseCode(200)
        );

        Offer result = funPayExecutor.execute(GetOffer.builder().offerId(33502824).build());

        assertNotNull(result);
        assertTrue(result.isAutoDelivery());
        assertFalse(result.getAttachmentLinks().isEmpty());
        assertFalse(result.getParameters().isEmpty());
        assertNotNull(result.getSeller());
        assertTrue(result.getSeller().isOnline());
    }

    @Test
    void testGetUser() throws IOException {
        String htmlContent = new String(Files.readAllBytes(Paths.get(GET_USER_HTML_RESPONSE_PATH)));

        this.mockWebServer.enqueue(
                new MockResponse()
                        .setBody(htmlContent)
                        .setResponseCode(200)
        );

        Seller result = (Seller) funPayExecutor.execute(GetUser.builder().userId(2).build());

        assertNotNull(result);
        assertNotNull(result.getRegisteredAt());
        assertFalse(result.isOnline());
        assertFalse(result.getBadges().isEmpty());
        assertFalse(result.getLastReviews().isEmpty());
        assertFalse(result.getPreviewOffers().isEmpty());
    }
}

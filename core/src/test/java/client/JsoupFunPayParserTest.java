package client;

import okhttp3.OkHttpClient;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.funpay4j.client.jsoup.JsoupFunPayParser;
import ru.funpay4j.core.commands.game.GetPromoGames;
import ru.funpay4j.core.commands.lot.GetLot;
import ru.funpay4j.core.objects.game.PromoGame;
import ru.funpay4j.core.objects.lot.Lot;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author panic08
 * @since 1.0.0
 */
class JsoupFunPayParserTest {
    private JsoupFunPayParser parser;
    private MockWebServer mockWebServer;

    private static final String GET_LOT_HTML_RESPONSE_PATH = "src/test/resources/html/client/getLotResponse.html";
    private static final String GET_PROMO_GAMES_JSON_RESPONSE_PATH = "src/test/resources/json/client/getPromoGamesResponse.json";

    @BeforeEach
    void setUp() {
        this.mockWebServer = new MockWebServer();
        this.parser = new JsoupFunPayParser(new OkHttpClient(), this.mockWebServer.url("/").toString());
    }

    @AfterEach
    void tearDown() throws IOException {
        this.mockWebServer.shutdown();
    }

    @Test
    void testGetLot() throws IOException{
        String htmlContent = new String(Files.readAllBytes(Paths.get(GET_LOT_HTML_RESPONSE_PATH)));

        this.mockWebServer.enqueue(
                new MockResponse()
                        .setBody(htmlContent)
                        .setResponseCode(200)
        );

        Lot result = parser.parse(GetLot.builder().lotId(149).build());

        assertNotNull(result);
        assertFalse(result.getPreviewOffers().isEmpty());
        assertFalse(result.getLotCounters().isEmpty());
    }

    @Test
    void testGetPromoGame() throws IOException {
        String jsonContent = new String(Files.readAllBytes(Paths.get(GET_PROMO_GAMES_JSON_RESPONSE_PATH)));

        this.mockWebServer.enqueue(
                new MockResponse()
                        .setBody(jsonContent)
                        .setResponseCode(200)
        );

        List<PromoGame> promoGames = parser.parse(GetPromoGames.builder().query("dota").build());

        assertNotNull(promoGames);
        assertFalse(promoGames.get(0).getPromoGameCounters().isEmpty());
    }
}

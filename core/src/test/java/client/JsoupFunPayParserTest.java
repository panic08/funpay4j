package client;

import okhttp3.OkHttpClient;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.funpay4j.client.jsoup.JsoupFunPayParser;
import ru.funpay4j.core.commands.lot.GetLot;
import ru.funpay4j.core.objects.lot.Lot;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author panic08
 * @since 1.0.0
 */
class JsoupFunPayParserTest {
    private JsoupFunPayParser parser;
    private MockWebServer mockWebServer;

    private static final String GET_LOT_HTML_PATH = "src/test/resources/html/client/getLotTest.html";

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
        String htmlContent = new String(Files.readAllBytes(Paths.get(GET_LOT_HTML_PATH)));

        this.mockWebServer.enqueue(
                new MockResponse()
                        .setBody(htmlContent)
                        .setHeader("Content-Type", "text/html; charset=utf-8")
                        .setResponseCode(200)
        );

        Lot result = parser.parse(GetLot.builder().lotId(149).build());

        assertNotNull(result);
        assertFalse(result.getPreviewOffers().isEmpty());
        assertFalse(result.getCounters().isEmpty());
    }
}

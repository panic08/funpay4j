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

package ru.funpay4j;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.funpay4j.core.AuthorizedFunPayExecutor;
import ru.funpay4j.core.commands.offer.CreateOffer;
import ru.funpay4j.core.commands.offer.RaiseAllOffers;
import ru.funpay4j.core.commands.user.UpdateAvatar;
import ru.funpay4j.core.exceptions.InvalidGoldenKeyException;
import ru.funpay4j.core.exceptions.offer.OfferAlreadyRaisedException;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author panic08
 * @since 1.0.3
 */
class AuthorizedFunPayExecutorTest {
    private AuthorizedFunPayExecutor funPayExecutor;

    private MockWebServer mockWebServer;

    private static final String GET_CSRF_TOKEN_AND_PHPSESSID_HTML_RESPONSE_PATH = "src/test/resources/html/client/getCsrfTokenAndPHPSESSIDResponse.html";
    private static final String UPDATE_AVATAR_IMG_PATH = "src/test/resources/img/client/updateAvatar.jpeg";

    @BeforeEach
    void setUp() throws Exception {
        this.mockWebServer = new MockWebServer();

        //we do this because when creating a new instance of AuthorizedFunPayExecutor in the constructor, csrfToken and PHPSESSID are obtained
        String htmlContent = new String(Files.readAllBytes(Paths.get(GET_CSRF_TOKEN_AND_PHPSESSID_HTML_RESPONSE_PATH)));

        mockWebServer.enqueue(
                new MockResponse()
                        .setBody(htmlContent)
                        .setHeader("Set-Cookie", "PHPSESSID=old;")
                        .setResponseCode(200)
        );

        this.funPayExecutor = new AuthorizedFunPayExecutor("example", this.mockWebServer.url("/").toString());
        this.funPayExecutor.setCsrfToken("old");
    }

    @AfterEach
    void tearDown() throws Exception {
        mockWebServer.shutdown();
    }

    @Test
    void testUpdateCsrfTokenAndPHPSESSID() throws Exception {
        String oldCsrfToken = funPayExecutor.getCsrfToken();
        String oldPHPSESSID = funPayExecutor.getPHPSESSID();

        String htmlContent = new String(Files.readAllBytes(Paths.get(GET_CSRF_TOKEN_AND_PHPSESSID_HTML_RESPONSE_PATH)));

        mockWebServer.enqueue(
                new MockResponse()
                        .setBody(htmlContent)
                        .setHeader("Set-Cookie", "PHPSESSID=new;")
                        .setResponseCode(200)
        );

        funPayExecutor.updateCsrfTokenAndPHPSESSID();

        assertNotNull(funPayExecutor.getCsrfToken());
        assertNotNull(funPayExecutor.getPHPSESSID());

        assertNotEquals(oldCsrfToken, funPayExecutor.getCsrfToken());
        assertNotEquals(oldPHPSESSID, funPayExecutor.getPHPSESSID());
    }

    @Test
    void testUpdateAvatarInvalidGoldenKeyException() throws Exception {
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(403)
        );

        assertThrows(InvalidGoldenKeyException.class, () -> {
            funPayExecutor.execute(UpdateAvatar.builder().newAvatar(Files.readAllBytes(Paths.get(UPDATE_AVATAR_IMG_PATH))).build());
        });
    }

    @Test
    void testRaiseAllOffersInvalidGoldenKeyException() throws Exception {
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(403)
        );

        assertThrows(InvalidGoldenKeyException.class, () -> {
            funPayExecutor.execute(RaiseAllOffers.builder().lotId(1).gameId(1).build());
        });
    }

    @Test
    void testRaiseAllOffersOfferAlreadyRaisedException() throws Exception {
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody("{\"msg\":\"Подождите 4 часа.\", \"error\": 1}")
        );

        assertThrows(OfferAlreadyRaisedException.class, () -> {
            funPayExecutor.execute(RaiseAllOffers.builder().lotId(1).gameId(1).build());
        });
    }

    @Test
    void testCreateOfferInvalidCsrfTokenOrPHPSESSIDException() throws Exception {
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(400)
                        .setBody("{\"msg\": \"Обновите страницу и повторите попытку.\", \"error\": 1}")
        );

        String htmlContent = new String(Files.readAllBytes(Paths.get(GET_CSRF_TOKEN_AND_PHPSESSID_HTML_RESPONSE_PATH)));

        mockWebServer.enqueue(
                new MockResponse()
                        .setBody(htmlContent)
                        .setHeader("Set-Cookie", "PHPSESSID=new;")
                        .setResponseCode(200)
        );

        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
        );

        String currentCsrfToken = funPayExecutor.getCsrfToken();
        String currentPHPSESSID = funPayExecutor.getPHPSESSID();

        funPayExecutor.execute(CreateOffer.builder().shortDescriptionEn("test").fields(new HashMap<>()).build());

        assertNotNull(funPayExecutor.getCsrfToken());
        assertNotNull(funPayExecutor.getPHPSESSID());
        assertNotEquals(currentCsrfToken, funPayExecutor.getCsrfToken());
        assertNotEquals(currentPHPSESSID, funPayExecutor.getPHPSESSID());
    }
}

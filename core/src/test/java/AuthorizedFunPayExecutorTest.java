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
import ru.funpay4j.core.AuthorizedFunPayExecutor;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author panic08
 * @since 1.0.3
 */
class AuthorizedFunPayExecutorTest {
    private AuthorizedFunPayExecutor funPayExecutor;

    private MockWebServer mockWebServer;

    private static final String GET_CSRF_TOKEN_AND_PHPSESSID_HTML_RESPONSE_PATH = "src/test/resources/html/client/getCsrfTokenAndPHPSESSIDResponse.html";

    @BeforeEach
    void setUp() throws Exception {
        this.mockWebServer = new MockWebServer();

        //we do this because when creating a new instance of AuthorizedFunPayExecutor in the constructor, csrfToken and PHPSESSID are obtained
        String htmlContent = new String(Files.readAllBytes(Paths.get(GET_CSRF_TOKEN_AND_PHPSESSID_HTML_RESPONSE_PATH)));

        mockWebServer.enqueue(
                new MockResponse()
                        .setBody(htmlContent)
                        .setResponseCode(200)
        );

        this.funPayExecutor = new AuthorizedFunPayExecutor(this.mockWebServer.url("/").toString());

        this.funPayExecutor.setCsrfToken("old");
        this.funPayExecutor.setPHPSESSID("old");
    }

    @AfterEach
    void tearDown() throws Exception {
        mockWebServer.shutdown();
    }

    @Test
    void testUpdateCsrfTokenAndPHPSESSID() throws Exception {
        String oldCsrfToken = funPayExecutor.getCsrfToken();
        String oldPHPSESSID = funPayExecutor.getPHPSESSID();

        funPayExecutor.updateCsrfTokenAndPHPSESSID();

        assertNotNull(funPayExecutor.getCsrfToken());
        assertNotNull(funPayExecutor.getPHPSESSID());

        assertNotEquals(oldCsrfToken, funPayExecutor.getCsrfToken());
        assertNotEquals(oldPHPSESSID, funPayExecutor.getPHPSESSID());
    }
}

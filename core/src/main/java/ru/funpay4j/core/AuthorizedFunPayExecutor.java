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

package ru.funpay4j.core;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import ru.funpay4j.core.commands.user.UpdateAvatar;
import ru.funpay4j.core.exceptions.FunPayApiException;
import ru.funpay4j.core.objects.CsrfTokenAndPHPSESSID;

import java.net.Proxy;

/**
 * This Authorized FunPay executor is used to execute authorized commands
 *
 * @author panic08
 * @since 1.0.3
 */
@Getter
@Setter
public class AuthorizedFunPayExecutor extends FunPayExecutor {
    private final String goldenKey;

    private String PHPSESSID;

    private String csrfToken;

    public AuthorizedFunPayExecutor(@NonNull String goldenKey) {
        super();

        this.goldenKey = goldenKey;
        try {
            updateCsrfTokenAndPHPSESSID();
        } catch (FunPayApiException e) {
            throw new RuntimeException(e.getCause());
        }
    }

    public AuthorizedFunPayExecutor(@NonNull String goldenKey, @NonNull String baseURL, @NonNull Proxy proxy) {
        super(baseURL, proxy);

        this.goldenKey = goldenKey;
        try {
            updateCsrfTokenAndPHPSESSID();
        } catch (FunPayApiException e) {
            throw new RuntimeException(e.getCause());
        }
    }

    public AuthorizedFunPayExecutor(@NonNull String goldenKey, @NonNull String baseURL) {
        super(baseURL);

        this.goldenKey = goldenKey;
        try {
            updateCsrfTokenAndPHPSESSID();
        } catch (FunPayApiException e) {
            throw new RuntimeException(e.getCause());
        }
    }

    public AuthorizedFunPayExecutor(@NonNull String goldenKey, @NonNull Proxy proxy) {
        super(proxy);

        this.goldenKey = goldenKey;
        try {
            updateCsrfTokenAndPHPSESSID();
        } catch (FunPayApiException e) {
            throw new RuntimeException(e.getCause());
        }
    }

    /**
     * Execute to update avatar
     *
     * @param command command that will be executed
     * @throws FunPayApiException if the goldenKey is incorrect or other api-related exception
     */
    public void execute(UpdateAvatar command) throws FunPayApiException {
        funPayClient.updateAvatar(goldenKey, command.getNewAvatar());
    }

    public void updateCsrfTokenAndPHPSESSID() throws FunPayApiException {
        //TODO: It might be worth reconsidering and finding another way to update csrf and PHPSESSID
        // that doesn't require making such relatively expensive queries

        CsrfTokenAndPHPSESSID csrfTokenAndPHPSESSID = funPayParser.parseCsrfTokenAndPHPSESSID(goldenKey);

        this.csrfToken = csrfTokenAndPHPSESSID.getCsrfToken();
        this.PHPSESSID = csrfTokenAndPHPSESSID.getPHPSESSID();
    }
}

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

package ru.funpay4j.client.okhttp;

import com.google.gson.JsonParser;
import lombok.NonNull;
import okhttp3.*;
import ru.funpay4j.client.FunPayClient;
import ru.funpay4j.core.exceptions.FunPayApiException;
import ru.funpay4j.core.exceptions.InvalidGoldenKeyException;
import ru.funpay4j.core.exceptions.offer.OfferAlreadyRaisedException;

import java.io.IOException;

/**
 * This implementation of FunPayClient uses OkHttp to send request
 *
 * @author panic08
 * @since 1.0.3
 */
public class OkHttpFunPayClient implements FunPayClient {
    @NonNull
    private final OkHttpClient httpClient;

    @NonNull
    private final String baseURL;

    public OkHttpFunPayClient(@NonNull OkHttpClient httpClient, @NonNull String baseURL) {
        this.httpClient = httpClient;
        this.baseURL = baseURL;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateAvatar(@NonNull String goldenKey, byte @NonNull [] newAvatar) throws FunPayApiException, InvalidGoldenKeyException {
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", "image.jpg", RequestBody.create(newAvatar))
                .build();

        try (Response response = httpClient.newCall(new Request.Builder().post(requestBody).url(baseURL + "/file/avatar")
                .addHeader("Cookie", "golden_key=" + goldenKey)
                .addHeader("x-requested-with", "XMLHttpRequest")
                .build()).execute()) {
            if (response.code() == 403) {
                throw new InvalidGoldenKeyException("goldenKey is invalid");
            }
        } catch (IOException e) {
            throw new FunPayApiException(e.getLocalizedMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void raiseAllOffers(String goldenKey, long gameId, long lotId) throws FunPayApiException, InvalidGoldenKeyException, OfferAlreadyRaisedException {
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("game_id", String.valueOf(gameId))
                .addFormDataPart("node_id", String.valueOf(lotId))
                .build();

        try (Response response = httpClient.newCall(new Request.Builder().post(requestBody).url(baseURL + "/lots/raise")
                .addHeader("Cookie", "golden_key=" + goldenKey)
                .addHeader("x-requested-with", "XMLHttpRequest")
                .build()).execute()) {
            if (response.code() == 403) {
                throw new InvalidGoldenKeyException("goldenKey is invalid");
            }

            if (JsonParser.parseString(response.body().string())
                    .getAsJsonObject().get("msg")
                    .getAsString().startsWith("Подождите")) {
                throw new OfferAlreadyRaisedException("Offer already raised");
            }
        } catch (IOException e) {
            throw new FunPayApiException(e.getLocalizedMessage());
        }
    }
}

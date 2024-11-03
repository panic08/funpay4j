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

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.NonNull;
import okhttp3.*;
import ru.funpay4j.client.FunPayClient;
import ru.funpay4j.client.request.SaveOfferRequest;
import ru.funpay4j.core.exceptions.FunPayApiException;
import ru.funpay4j.core.exceptions.InvalidCsrfTokenOrPHPSESSIDException;
import ru.funpay4j.core.exceptions.InvalidGoldenKeyException;
import ru.funpay4j.core.exceptions.offer.OfferAlreadyRaisedException;

import java.io.IOException;
import java.util.Map;

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
    public void raiseAllOffers(@NonNull String goldenKey, long gameId, long lotId) throws FunPayApiException, InvalidGoldenKeyException, OfferAlreadyRaisedException {
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

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveOffer(@NonNull String goldenKey, @NonNull String csrfToken,
                          @NonNull String PHPSESSID, @NonNull SaveOfferRequest request) throws FunPayApiException, InvalidGoldenKeyException{
        MultipartBody.Builder multipartBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("csrf_token", csrfToken)
                .addFormDataPart("offer_id", request.getOfferId() == null ? "" : String.valueOf(request.getOfferId()))
                .addFormDataPart("node_id", request.getNodeId() == null ? "" : String.valueOf(request.getNodeId()))
                .addFormDataPart("deleted", request.isDeleted() ? "1" : "")
                .addFormDataPart("auto_delivery", request.isAutoDelivery() ? "on" : "")
                .addFormDataPart("active", request.isActive() ? "on" : "")
                .addFormDataPart("secrets", request.getSecrets() == null ? "" : String.join("\n", request.getSecrets()))
                .addFormDataPart("price", request.getPrice() == null ? "" : String.valueOf(request.getPrice()))
                .addFormDataPart("amount", request.getAmount() == null ? "" : String.valueOf(request.getAmount()))
                .addFormDataPart("form_created_at", String.valueOf(System.currentTimeMillis()))
                .addFormDataPart("fields[summary][ru]", request.getSummaryRu() == null ? "" : request.getSummaryRu())
                .addFormDataPart("fields[summary][en]", request.getSummaryEn() == null ? "" : request.getSummaryEn())
                .addFormDataPart("fields[desc][ru]", request.getDescRu() == null ? "" : request.getDescRu())
                .addFormDataPart("fields[desc][en]", request.getDescEn() == null ? "" : request.getDescEn())
                .addFormDataPart("fields[payment_msg][ru]", request.getPaymentMessageRu() == null ? "" : request.getPaymentMessageRu())
                .addFormDataPart("fields[payment_msg][en]", request.getPaymentMessageEn() == null ? "" : request.getPaymentMessageEn());

        if (request.getFields() != null) {
            for (Map.Entry<String, String> field : request.getFields().entrySet()) {
                multipartBody.addFormDataPart(field.getKey(), field.getValue());
            }
        }

        try (Response response = httpClient.newCall(new Request.Builder().post(multipartBody.build()).url(baseURL + "/lots/offerSave")
                .addHeader("Cookie", "golden_key=" + goldenKey + "; PHPSESSID=" + PHPSESSID)
                .addHeader("x-requested-with", "XMLHttpRequest")
                .build()).execute()) {
            String responseBodyString = response.body().string();

            JsonObject responseJsonObject = null;

            if (!responseBodyString.isEmpty()) {
                responseJsonObject = JsonParser.parseString(responseBodyString).getAsJsonObject();
            }

            if (response.code() == 403) {
                throw new InvalidGoldenKeyException("goldenKey is invalid");
            } else if (response.code() == 400 && responseJsonObject != null && responseJsonObject.get("msg") != null
                    && responseJsonObject.get("msg").getAsString().equals("Обновите страницу и повторите попытку.")) {
                throw new InvalidCsrfTokenOrPHPSESSIDException("csrf token or PHPSESSID is invalid");
            }

            if (responseJsonObject != null && !responseJsonObject.get("done").getAsBoolean()) {
                //TODO: Throw something more contextual than RuntimeException
                throw new RuntimeException(responseJsonObject.get("error") + " " + responseJsonObject.get("errors").toString());
            }
        } catch (IOException e) {
            throw new FunPayApiException(e.getLocalizedMessage());
        }
    }
}

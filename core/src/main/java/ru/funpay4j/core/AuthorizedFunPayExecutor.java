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
import ru.funpay4j.client.request.SaveOfferRequest;
import ru.funpay4j.core.commands.offer.*;
import ru.funpay4j.core.commands.user.UpdateAvatar;
import ru.funpay4j.core.exceptions.FunPayApiException;
import ru.funpay4j.core.exceptions.InvalidCsrfTokenOrPHPSESSIDException;
import ru.funpay4j.core.exceptions.InvalidGoldenKeyException;
import ru.funpay4j.core.exceptions.offer.OfferAlreadyRaisedException;
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
     * Execute to update user avatar
     *
     * @param command command that will be executed
     * @throws FunPayApiException if the other api-related exception
     * @throws InvalidGoldenKeyException if the golden key is incorrect
     */
    public void execute(UpdateAvatar command) throws FunPayApiException, InvalidGoldenKeyException {
        funPayClient.updateAvatar(goldenKey, command.getNewAvatar());
    }

    /**
     * Execute to raise all offers
     *
     * @param command command that will be executed
     * @throws FunPayApiException if the other api-related exception
     * @throws InvalidGoldenKeyException if the golden key is incorrect
     * @throws OfferAlreadyRaisedException if the offer already raised
     */
    public void execute(RaiseAllOffers command) throws FunPayApiException, InvalidGoldenKeyException, OfferAlreadyRaisedException {
        funPayClient.raiseAllOffers(goldenKey, command.getGameId(), command.getLotId());
    }

    /**
     * Execute to create offer
     *
     * @param command command that will be executed
     * @throws FunPayApiException if the other api-related exception
     * @throws InvalidGoldenKeyException if the golden key is incorrect
     */
    public void execute(CreateOffer command) throws FunPayApiException, InvalidGoldenKeyException {
        SaveOfferRequest request = SaveOfferRequest.builder()
                .nodeId(command.getLotId())
                .summaryRu(command.getShortDescriptionRu())
                .summaryEn(command.getShortDescriptionEn())
                .descRu(command.getDescriptionRu())
                .descEn(command.getDescriptionEn())
                .paymentMessageRu(command.getPaymentMessageRu())
                .paymentMessageEn(command.getPaymentMessageEn())
                .fields(command.getFields())
                .isAutoDelivery(command.isAutoDelivery())
                .isActive(command.isActive())
                .isDeleted(false)
                .secrets(command.getSecrets())
                .price(command.getPrice())
                .amount(command.getAmount())
                .build();

        //attempt to regenerate csrfToken and PHPSESSID
        try {
            funPayClient.saveOffer(goldenKey, csrfToken, PHPSESSID, request);
        } catch (InvalidCsrfTokenOrPHPSESSIDException e) {
            updateCsrfTokenAndPHPSESSID();

            try {
                funPayClient.saveOffer(goldenKey, csrfToken, PHPSESSID, request);
            } catch (InvalidCsrfTokenOrPHPSESSIDException e1) {
                //TODO: Throw something more contextual than RuntimeException
                throw new RuntimeException(e1.getLocalizedMessage());
            }
        }
    }

    /**
     * Execute to edit offer
     *
     * @param command command that will be executed
     * @throws FunPayApiException if the other api-related exception
     * @throws InvalidGoldenKeyException if the golden key is incorrect
     */
    public void execute(EditOffer command) throws FunPayApiException, InvalidGoldenKeyException {
        SaveOfferRequest request = SaveOfferRequest.builder()
                .nodeId(command.getLotId())
                .offerId(command.getOfferId())
                .summaryRu(command.getShortDescriptionRu())
                .summaryEn(command.getShortDescriptionEn())
                .descRu(command.getDescriptionRu())
                .descEn(command.getDescriptionEn())
                .paymentMessageRu(command.getPaymentMessageRu())
                .paymentMessageEn(command.getPaymentMessageEn())
                .fields(command.getFields())
                .isAutoDelivery(command.isAutoDelivery())
                .isActive(command.isActive())
                .isDeleted(false)
                .secrets(command.getSecrets())
                .price(command.getPrice())
                .amount(command.getAmount())
                .build();

        //attempt to regenerate csrfToken and PHPSESSID
        try {
            funPayClient.saveOffer(goldenKey, csrfToken, PHPSESSID, request);
        } catch (InvalidCsrfTokenOrPHPSESSIDException e) {
            updateCsrfTokenAndPHPSESSID();

            try {
                funPayClient.saveOffer(goldenKey, csrfToken, PHPSESSID, request);
            } catch (InvalidCsrfTokenOrPHPSESSIDException e1) {
                //TODO: Throw something more contextual than RuntimeException
                throw new RuntimeException(e1.getLocalizedMessage());
            }
        }
    }

    /**
     * Execute to delete offer
     *
     * @param command command that will be executed
     * @throws FunPayApiException if the other api-related exception
     * @throws InvalidGoldenKeyException if the golden key is incorrect
     */
    public void execute(DeleteOffer command) throws FunPayApiException, InvalidGoldenKeyException {
        SaveOfferRequest request = SaveOfferRequest.builder()
                .nodeId(command.getLotId())
                .offerId(command.getOfferId())
                .isDeleted(true)
                .build();

        //attempt to regenerate csrfToken and PHPSESSID
        try {
            funPayClient.saveOffer(goldenKey, csrfToken, PHPSESSID, request);
        } catch (InvalidCsrfTokenOrPHPSESSIDException e) {
            updateCsrfTokenAndPHPSESSID();

            try {
                funPayClient.saveOffer(goldenKey, csrfToken, PHPSESSID, request);
            } catch (InvalidCsrfTokenOrPHPSESSIDException e1) {
                //TODO: Throw something more contextual than RuntimeException
                throw new RuntimeException(e1.getLocalizedMessage());
            }
        }
    }

    /**
     * Execute to create offer image
     *
     * @param command command that will be executed
     * @return fileId
     * @throws FunPayApiException if the other api-related exception
     * @throws InvalidGoldenKeyException if the golden key is incorrect
     */
    public Long execute(CreateOfferImage command) throws FunPayApiException, InvalidGoldenKeyException {
        return funPayClient.addOfferImage(goldenKey, command.getImage());
    }

    public void updateCsrfTokenAndPHPSESSID() throws FunPayApiException {
        //TODO: It might be worth reconsidering and finding another way to update csrf and PHPSESSID
        // that doesn't require making such relatively expensive queries

        CsrfTokenAndPHPSESSID csrfTokenAndPHPSESSID = funPayParser.parseCsrfTokenAndPHPSESSID(goldenKey);

        this.csrfToken = csrfTokenAndPHPSESSID.getCsrfToken();
        this.PHPSESSID = csrfTokenAndPHPSESSID.getPHPSESSID();
    }
}

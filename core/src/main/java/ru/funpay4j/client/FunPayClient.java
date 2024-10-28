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

package ru.funpay4j.client;

import ru.funpay4j.core.exceptions.FunPayApiException;
import ru.funpay4j.core.exceptions.InvalidGoldenKeyException;
import ru.funpay4j.core.exceptions.offer.OfferAlreadyRaisedException;

/**
 * Interface for sending thematic requests to FunPay
 *
 * @author panic08
 * @since 1.0.3
 */
public interface FunPayClient {
    /**
     * Send a request to update avatar
     *
     * @param goldenKey golden key which will be used to authorize the user
     * @param newAvatar avatar to be updated
     * @throws FunPayApiException if the other api-related exception
     * @throws InvalidGoldenKeyException if the golden key is invalid
     */
    void updateAvatar(String goldenKey, byte[] newAvatar) throws FunPayApiException, InvalidGoldenKeyException;

    /**
     * Send a request to raise all offers
     *
     * @param goldenKey golden key which will be used to authorize the user
     * @param gameId game id for which offers will be raised
     * @param lotId lot id for which offers will be raised
     * @throws FunPayApiException if the other api-related exception
     * @throws InvalidGoldenKeyException if the golden key is invalid
     * @throws OfferAlreadyRaisedException if the offer already raised
     */
    void raiseAllOffers(String goldenKey, long gameId, long lotId) throws FunPayApiException, InvalidGoldenKeyException, OfferAlreadyRaisedException;
}

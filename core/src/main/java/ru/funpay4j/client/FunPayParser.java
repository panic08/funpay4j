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
import ru.funpay4j.core.objects.CsrfTokenAndPHPSESSID;
import ru.funpay4j.core.objects.game.PromoGame;
import ru.funpay4j.core.objects.lot.Lot;
import ru.funpay4j.core.objects.offer.Offer;
import ru.funpay4j.core.objects.user.SellerReview;
import ru.funpay4j.core.objects.user.User;

import java.util.List;

/**
 * Interface for parsing data from FunPay
 * Defines the methods that any FunPay parser implementation must provide
 *
 * @author panic08
 * @since 1.0.0
 */
public interface FunPayParser {
    /**
     * Parse lot
     *
     * @param lotId lotId by which lot will be parsed
     * @return lot
     * @throws FunPayApiException if the user with id does not exist or other api-related exception
     */
    Lot parseLot(long lotId) throws FunPayApiException;

    /**
     * Parse promoGames
     *
     * @param query query by which promoGames will be parsed
     * @return promoGames
     * @throws FunPayApiException if other api-related exception
     */
    List<PromoGame> parsePromoGames(String query) throws FunPayApiException;

    /**
     * Parse offer
     *
     * @param offerId offerId by which offer will be parsed
     * @return offer
     * @throws FunPayApiException if the offer with id does not exist or other api-related exception
     */
    Offer parseOffer(long offerId) throws FunPayApiException;

    /**
     * Parse user
     *
     * @param userId userId by which user will be parsed
     * @return user
     * @throws FunPayApiException if the user with id does not exist or other api-related exception
     */
    User parseUser(long userId) throws FunPayApiException;

    /**
     * Parse seller reviews
     *
     * @param userId userId by which seller reviews pages will be parsed
     * @param pages number of pages indicating how many seller reviews will be parsed
     * @param starsFilter number of stars by which the reviews will be parsed
     * @return sellerReviews
     * @throws FunPayApiException if the user with id does not exist/seller or other api-related exception
     */
    List<SellerReview> parseSellerReviews(long userId, int pages, Integer starsFilter) throws FunPayApiException;

    /**
     * Parse csrf and PHPSESSID
     *
     * @param goldenKey goldenKey which will be used to authorize the user
     * @return csrfToken and PHPSESSID
     * @throws FunPayApiException if other api-related exception
     */
    CsrfTokenAndPHPSESSID parseCsrfTokenAndPHPSESSID(String goldenKey) throws FunPayApiException;
}

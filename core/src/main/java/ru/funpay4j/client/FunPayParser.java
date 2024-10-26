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
import ru.funpay4j.core.exceptions.offer.OfferNotFoundException;
import ru.funpay4j.core.exceptions.lot.LotNotFoundException;
import ru.funpay4j.core.exceptions.user.UserNotFoundException;
import ru.funpay4j.core.objects.CsrfTokenAndPHPSESSID;
import ru.funpay4j.core.objects.game.PromoGame;
import ru.funpay4j.core.objects.lot.Lot;
import ru.funpay4j.core.objects.offer.Offer;
import ru.funpay4j.core.objects.user.SellerReview;
import ru.funpay4j.core.objects.user.User;

import java.util.List;

/**
 * Interface for parsing data from FunPay
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
     * @throws FunPayApiException if the other api-related exception
     * @throws LotNotFoundException if the lot with id does not found
     */
    Lot parseLot(long lotId) throws FunPayApiException, LotNotFoundException;

    /**
     * Parse promoGames
     *
     * @param query query by which promoGames will be parsed
     * @return promoGames
     * @throws FunPayApiException if the other api-related exception
     */
    List<PromoGame> parsePromoGames(String query) throws FunPayApiException;

    /**
     * Parse offer
     *
     * @param offerId offerId by which offer will be parsed
     * @return offer
     * @throws FunPayApiException if the other api-related exception
     * @throws OfferNotFoundException if the offer with id does not found
     */
    Offer parseOffer(long offerId) throws FunPayApiException, OfferNotFoundException;

    /**
     * Parse user
     *
     * @param userId userId by which user will be parsed
     * @return user
     * @throws FunPayApiException if the other api-related exception
     * @throws UserNotFoundException if the user with id does not found
     */
    User parseUser(long userId) throws FunPayApiException, UserNotFoundException;

    /**
     * Parse seller reviews
     *
     * @param userId userId by which seller reviews pages will be parsed
     * @param pages number of pages indicating how many seller reviews will be parsed
     * @param starsFilter number of stars by which the reviews will be parsed
     * @return sellerReviews
     * @throws FunPayApiException if the other api-related exception
     * @throws UserNotFoundException if the user with id does not found/seller
     */
    List<SellerReview> parseSellerReviews(long userId, int pages, Integer starsFilter) throws FunPayApiException, UserNotFoundException;

    /**
     * Parse csrf and PHPSESSID
     *
     * @param goldenKey goldenKey which will be used to authorize the user
     * @return csrfToken and PHPSESSID
     * @throws FunPayApiException if the other api-related exception
     */
    CsrfTokenAndPHPSESSID parseCsrfTokenAndPHPSESSID(String goldenKey) throws FunPayApiException;
}

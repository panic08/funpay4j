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

import lombok.NonNull;
import okhttp3.OkHttpClient;
import ru.funpay4j.client.FunPayClient;
import ru.funpay4j.client.FunPayParser;
import ru.funpay4j.client.jsoup.JsoupFunPayParser;
import ru.funpay4j.client.okhttp.OkHttpFunPayClient;
import ru.funpay4j.core.commands.offer.GetOffer;
import ru.funpay4j.core.commands.game.GetPromoGames;
import ru.funpay4j.core.commands.lot.GetLot;
import ru.funpay4j.core.commands.user.GetSellerReviews;
import ru.funpay4j.core.exceptions.FunPayApiException;
import ru.funpay4j.core.commands.user.GetUser;
import ru.funpay4j.core.exceptions.lot.LotNotFoundException;
import ru.funpay4j.core.exceptions.offer.OfferNotFoundException;
import ru.funpay4j.core.exceptions.user.UserNotFoundException;
import ru.funpay4j.core.objects.game.PromoGame;
import ru.funpay4j.core.objects.lot.Lot;
import ru.funpay4j.core.objects.offer.Offer;
import ru.funpay4j.core.objects.user.SellerReview;
import ru.funpay4j.core.objects.user.User;

import java.net.Proxy;
import java.util.List;

/**
 * This FunPay executor is used to execute commands
 *
 * @author panic08
 * @since 1.0.0
 */
public class FunPayExecutor {
    @NonNull
    protected final FunPayParser funPayParser;

    @NonNull
    protected final FunPayClient funPayClient;

    /**
     * Creates a new FunPayExecutor instance
     */
    public FunPayExecutor() {
        OkHttpClient httpClient = new OkHttpClient();

        this.funPayParser = new JsoupFunPayParser(httpClient, FunPayURL.BASE_URL);
        this.funPayClient = new OkHttpFunPayClient(httpClient, FunPayURL.BASE_URL);
    }

    /**
     * Creates a new FunPayExecutor instance
     *
     * @param baseURL base URL of the primary server
     * @param proxy proxy for forwarding requests
     */
    public FunPayExecutor(@NonNull String baseURL, @NonNull Proxy proxy) {
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .proxy(proxy)
                .build();

        this.funPayParser = new JsoupFunPayParser(httpClient, baseURL);
        this.funPayClient = new OkHttpFunPayClient(httpClient, baseURL);
    }

    /**
     * Creates a new FunPayExecutor instance
     *
     * @param baseURL base URL of the primary server
     */
    public FunPayExecutor(@NonNull String baseURL) {
        OkHttpClient httpClient = new OkHttpClient();

        this.funPayParser = new JsoupFunPayParser(httpClient, baseURL);
        this.funPayClient = new OkHttpFunPayClient(httpClient, baseURL);
    }

    /**
     * Creates a new FunPayExecutor instance
     *
     * @param proxy proxy for forwarding requests
     */
    public FunPayExecutor(@NonNull Proxy proxy) {
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .proxy(proxy)
                .build();

        this.funPayParser = new JsoupFunPayParser(httpClient, FunPayURL.BASE_URL);
        this.funPayClient = new OkHttpFunPayClient(httpClient, FunPayURL.BASE_URL);
    }

    /**
     * Execute to get lot
     *
     * @param command command that will be executed
     * @return lot
     * @throws FunPayApiException if the other api-related exception
     * @throws LotNotFoundException if the lot with id does not found
     */
    public Lot execute(GetLot command) throws FunPayApiException, LotNotFoundException {
        return funPayParser.parseLot(command.getLotId());
    }

    /**
     * Execute to get promo games
     *
     * @param command command that will be executed
     * @return promo games
     * @throws FunPayApiException if the other api-related exception
     */
    public List<PromoGame> execute(GetPromoGames command) throws FunPayApiException {
        return funPayParser.parsePromoGames(command.getQuery());
    }

    /**
     * Execute to get offer
     *
     * @param command command that will be executed
     * @return offer
     * @throws FunPayApiException if the other api-related exception
     * @throws OfferNotFoundException if the offer with id does not found
     */
    public Offer execute(GetOffer command) throws FunPayApiException, OfferNotFoundException {
        return funPayParser.parseOffer(command.getOfferId());
    }

    /**
     * Execute to get user
     *
     * @param command command that will be executed
     * @return user
     * @throws FunPayApiException if the other api-related exception
     * @throws UserNotFoundException if the user with id does not found
     */
    public User execute(GetUser command) throws FunPayApiException, UserNotFoundException {
        return funPayParser.parseUser(command.getUserId());
    }

    /**
     * Execute to get seller reviews
     *
     * @param command command that will be executed
     * @return seller reviews
     * @throws FunPayApiException if the other api-related exception
     * @throws UserNotFoundException if the user with id does not found/seller
     */
    public List<SellerReview> execute(GetSellerReviews command) throws FunPayApiException, UserNotFoundException {
        if (command.getStarsFilter() != null) {
            return funPayParser.parseSellerReviews(command.getUserId(), command.getPages(), command.getStarsFilter());
        } else {
            return funPayParser.parseSellerReviews(command.getUserId(), command.getPages());
        }
    }
}

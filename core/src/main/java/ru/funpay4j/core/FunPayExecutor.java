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
import ru.funpay4j.client.FunPayParser;
import ru.funpay4j.client.jsoup.JsoupFunPayParser;
import ru.funpay4j.core.commands.offer.GetOffer;
import ru.funpay4j.core.commands.game.GetPromoGames;
import ru.funpay4j.core.commands.lot.GetLot;
import ru.funpay4j.core.commands.user.GetSellerReviews;
import ru.funpay4j.core.exceptions.FunPayApiException;
import ru.funpay4j.core.commands.user.GetUser;
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

    public FunPayExecutor() {
        OkHttpClient httpClient = new OkHttpClient();

        this.funPayParser = new JsoupFunPayParser(httpClient, FunPayURL.BASE_URL);
    }

    public FunPayExecutor(@NonNull String baseURL, @NonNull Proxy proxy) {
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .proxy(proxy)
                .build();

        this.funPayParser = new JsoupFunPayParser(httpClient, baseURL);
    }

    public FunPayExecutor(@NonNull String baseURL) {
        OkHttpClient httpClient = new OkHttpClient();

        this.funPayParser = new JsoupFunPayParser(httpClient, baseURL);
    }

    public FunPayExecutor(@NonNull Proxy proxy) {
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .proxy(proxy)
                .build();

        this.funPayParser = new JsoupFunPayParser(httpClient, FunPayURL.BASE_URL);
    }

    /**
     * Execute to get lot
     *
     * @param command command that will be executed
     * @return lot
     * @throws FunPayApiException if the lot with id does not exist or other api-related exception
     */
    public Lot execute(GetLot command) throws FunPayApiException {
        return funPayParser.parseLot(command.getLotId());
    }

    /**
     * Execute to get promoGames
     *
     * @param command command that will be executed
     * @return promoGames
     * @throws FunPayApiException if other api-related exception
     */
    public List<PromoGame> execute(GetPromoGames command) throws FunPayApiException {
        return funPayParser.parsePromoGames(command.getQuery());
    }

    /**
     * Execute to get offer
     *
     * @param command command that will be executed
     * @return offer
     * @throws FunPayApiException if the offer with id does not exist or other api-related exception
     */
    public Offer execute(GetOffer command) throws FunPayApiException {
        return funPayParser.parseOffer(command.getOfferId());
    }

    /**
     * Execute to get user
     *
     * @param command command that will be executed
     * @return user
     * @throws FunPayApiException if the user with id does not exist or other api-related exception
     */
    public User execute(GetUser command) throws FunPayApiException {
        return funPayParser.parseUser(command.getUserId());
    }

    /**
     * Execute to get sellerReviews
     *
     * @param command command that will be executed
     * @return sellerReviews
     * @throws FunPayApiException if the user with id does not exist/seller or other api-related exception
     */
    public List<SellerReview> execute(GetSellerReviews command) throws FunPayApiException {
        return funPayParser.parseSellerReviews(command.getUserId(), command.getPages(), command.getStarsFilter());
    }
}

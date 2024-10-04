package ru.funpay4j.core;

import lombok.NonNull;
import okhttp3.OkHttpClient;
import ru.funpay4j.client.FunPayParser;
import ru.funpay4j.client.jsoup.JsoupFunPayParser;
import ru.funpay4j.core.commands.offer.GetOffer;
import ru.funpay4j.core.commands.game.GetPromoGames;
import ru.funpay4j.core.commands.lot.GetLot;
import ru.funpay4j.core.exceptions.FunPayApiException;
import ru.funpay4j.core.commands.user.GetUser;
import ru.funpay4j.core.objects.game.PromoGame;
import ru.funpay4j.core.objects.lot.Lot;
import ru.funpay4j.core.objects.offer.Offer;
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
        this.funPayParser = new JsoupFunPayParser(httpClient);
    }

    public FunPayExecutor(@NonNull Proxy proxy) {
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .proxy(proxy)
                .build();

        this.funPayParser = new JsoupFunPayParser(httpClient);
    }

    /**
     * Execute get lot command
     * @param command command that will be executed
     * @return lot
     * @throws FunPayApiException if the lot with id does not exist or other api-related exception
     */
    public Lot execute(GetLot command) throws FunPayApiException {
        return funPayParser.parse(command);
    }

    /**
     * Execute get promoGames command
     * @param command command that will be executed
     * @return promoGames
     * @throws FunPayApiException if api-related exception
     */
    public List<PromoGame> execute(GetPromoGames command) throws FunPayApiException {
        return funPayParser.parse(command);
    }

    /**
     * Execute get offer command
     * @param command command that will be executed
     * @return offer
     * @throws FunPayApiException if the offer with id does not exist or other api-related exception
     */
    public Offer execute(GetOffer command) throws FunPayApiException {
        return funPayParser.parse(command);
    }

    /**
     * Execute get user command
     * @param command command that will be executed
     * @return user
     * @throws FunPayApiException if the user with id does not exist or other api-related exception
     */
    public User execute(GetUser command) throws FunPayApiException {
        return funPayParser.parse(command);
    }
}

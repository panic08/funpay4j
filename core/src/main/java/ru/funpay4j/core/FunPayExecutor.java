package ru.funpay4j.core;

import lombok.NonNull;
import okhttp3.OkHttpClient;
import ru.funpay4j.client.FunPayParser;
import ru.funpay4j.client.jsoup.JsoupFunPayParser;
import ru.funpay4j.core.methods.offer.GetOffer;
import ru.funpay4j.core.methods.game.GetPromoGames;
import ru.funpay4j.core.methods.lot.GetLot;
import ru.funpay4j.core.exceptions.FunPayApiException;
import ru.funpay4j.core.methods.user.GetUser;
import ru.funpay4j.core.objects.game.PromoGame;
import ru.funpay4j.core.objects.lot.Lot;
import ru.funpay4j.core.objects.offer.Offer;
import ru.funpay4j.core.objects.user.User;

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

    public FunPayExecutor(OkHttpClient httpClient) {
        this.funPayParser = new JsoupFunPayParser(httpClient);
    }

    public Lot execute(GetLot command) throws FunPayApiException {
        return funPayParser.parse(command);
    }

    public List<PromoGame> execute(GetPromoGames command) throws FunPayApiException {
        return funPayParser.parse(command);
    }

    public Offer execute(GetOffer command) throws FunPayApiException {
        return funPayParser.parse(command);
    }

    public User execute(GetUser command) throws FunPayApiException {
        return funPayParser.parse(command);
    }
}

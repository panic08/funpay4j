package ru.funpay4j.core;

import com.google.gson.Gson;
import okhttp3.OkHttpClient;
import ru.funpay4j.client.FunPayParser;
import ru.funpay4j.client.jsoup.JsoupFunPayParser;
import ru.funpay4j.core.commands.lot.GetLot;
import ru.funpay4j.core.exceptions.FunPayApiException;
import ru.funpay4j.core.objects.lot.Lot;

/**
 * This FunPay executor is used to execute commands
 *
 * @author panic08
 * @since 1.0.0
 */
public class FunPayExecutor {
    protected final FunPayParser funPayParser;

    public FunPayExecutor() {
        OkHttpClient httpClient = new OkHttpClient();
        Gson objectMapper = new Gson();
        this.funPayParser = new JsoupFunPayParser(httpClient);
    }

    public Lot execute(GetLot command) throws FunPayApiException {
        return funPayParser.parse(command);
    }
}

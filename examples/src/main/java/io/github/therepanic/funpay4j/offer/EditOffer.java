package io.github.therepanic.funpay4j.offer;

import io.github.therepanic.funpay4j.AuthorizedFunPayExecutor;
import io.github.therepanic.funpay4j.exceptions.FunPayApiException;
import io.github.therepanic.funpay4j.exceptions.InvalidGoldenKeyException;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.HashMap;
import java.util.Map;

/**
 * This is an example of how to edit offer
 *
 * @author therepanic
 */
public class EditOffer {
    public static void main(String[] args) {
        //if we want to use a proxy
        Proxy proxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress("127.0.0.1", 8000));

        AuthorizedFunPayExecutor executor = new AuthorizedFunPayExecutor("test-golden-key", proxy);


        try {
            Map<String, String> fields = new HashMap<>();
            fields.put("fields[type]", "Одежда");
            fields.put("fields[hero]", "Abaddon");
            fields.put("fields[rare]", "Common");
            fields.put("fields[quality]", "Inscribed");
            fields.put("fields[method]", "Мгновенно");

            executor.execute(io.github.therepanic.funpay4j.commands.offer.EditOffer.builder()
                    .lotId(210L)
                    .offerId(53453453L)
                    .price(200D)
                    .amount(5)
                    .shortDescriptionEn("Dota 2 Item!")
                    .fields(fields)
                    .build());
        } catch (FunPayApiException e) {
            throw new RuntimeException(e);
        } catch (InvalidGoldenKeyException e) {
            System.out.println("golden key is invalid!");
        }
    }
}

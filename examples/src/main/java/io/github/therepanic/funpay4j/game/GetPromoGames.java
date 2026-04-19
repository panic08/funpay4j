package io.github.therepanic.funpay4j.game;

import io.github.therepanic.funpay4j.FunPayExecutor;
import io.github.therepanic.funpay4j.exceptions.FunPayApiException;
import io.github.therepanic.funpay4j.objects.game.PromoGame;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.List;

/**
 * This is an example of how to get promo games
 *
 * @author therepanic
 */
public class GetPromoGames {
    public static void main(String[] args) {
        //if we want to use a proxy
        Proxy proxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress("127.0.0.1", 8000));

        FunPayExecutor executor = new FunPayExecutor(proxy);

        List<PromoGame> promoGames;

        try {
            promoGames = executor.execute(io.github.therepanic.funpay4j.commands.game.GetPromoGames.builder()
                    .query("dota")
                    .build());

            System.out.println(promoGames);
        } catch (FunPayApiException e) {
            throw new RuntimeException(e);
        }
    }
}

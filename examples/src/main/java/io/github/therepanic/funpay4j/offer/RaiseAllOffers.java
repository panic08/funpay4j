package io.github.therepanic.funpay4j.offer;

import io.github.therepanic.funpay4j.AuthorizedFunPayExecutor;
import io.github.therepanic.funpay4j.exceptions.FunPayApiException;
import io.github.therepanic.funpay4j.exceptions.InvalidGoldenKeyException;
import io.github.therepanic.funpay4j.exceptions.offer.OfferAlreadyRaisedException;

import java.net.InetSocketAddress;
import java.net.Proxy;

/**
 * This is an example of how to raise all offers
 *
 * @author therepanic
 */
public class RaiseAllOffers {
    public static void main(String[] args) {
        //if we want to use a proxy
        Proxy proxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress("127.0.0.1", 8000));

        AuthorizedFunPayExecutor authorizedExecutor = new AuthorizedFunPayExecutor("test-golden-key", proxy);

        try {
            authorizedExecutor.execute(io.github.therepanic.funpay4j.commands.offer.RaiseAllOffers.builder()
                    .lotId(123L)
                    .gameId(123L)
                    .build());
        } catch (FunPayApiException e) {
            throw new RuntimeException(e);
        } catch (OfferAlreadyRaisedException e) {
            System.out.println("The offer already raised!");
        } catch (InvalidGoldenKeyException e) {
            System.out.println("golden key is invalid!");
        }
    }
}

package io.github.therepanic.funpay4j.offer;

import io.github.therepanic.funpay4j.AuthorizedFunPayExecutor;
import io.github.therepanic.funpay4j.exceptions.FunPayApiException;
import io.github.therepanic.funpay4j.exceptions.InvalidGoldenKeyException;

import java.net.InetSocketAddress;
import java.net.Proxy;

/**
 * This is an example of how to delete offer
 *
 * @author therepanic
 */
public class DeleteOffer {
    public static void main(String[] args) {
        //if we want to use a proxy
        Proxy proxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress("127.0.0.1", 8000));

        AuthorizedFunPayExecutor executor = new AuthorizedFunPayExecutor("test-golden-key", proxy);


        try {
            executor.execute(io.github.therepanic.funpay4j.commands.offer.DeleteOffer.builder()
                    .lotId(210L)
                    .offerId(34626245L)
                    .build());
        } catch (FunPayApiException e) {
            throw new RuntimeException(e);
        } catch (InvalidGoldenKeyException e) {
            System.out.println("golden key is invalid!");
        }
    }
}

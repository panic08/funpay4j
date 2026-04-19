package io.github.therepanic.funpay4j.offer;

import io.github.therepanic.funpay4j.FunPayExecutor;
import io.github.therepanic.funpay4j.exceptions.FunPayApiException;
import io.github.therepanic.funpay4j.exceptions.offer.OfferNotFoundException;
import io.github.therepanic.funpay4j.objects.offer.Offer;

import java.net.InetSocketAddress;
import java.net.Proxy;

/**
 * This is an example of how to get offer
 *
 * @author therepanic
 */
public class GetOffer {
    public static void main(String[] args) {
        //if we want to use a proxy
        Proxy proxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress("127.0.0.1", 8000));

        FunPayExecutor executor = new FunPayExecutor(proxy);

        Offer offer;

        try {
            offer = executor.execute(io.github.therepanic.funpay4j.commands.offer.GetOffer.builder()
                    .offerId(26021761L)
                    .build());

            System.out.println(offer);
        } catch (FunPayApiException e) {
            throw new RuntimeException(e);
        } catch (OfferNotFoundException e) {
            System.out.println("The offer with such an id does not found!");
        }
    }
}

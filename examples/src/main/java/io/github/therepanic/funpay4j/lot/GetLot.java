package io.github.therepanic.funpay4j.lot;

import io.github.therepanic.funpay4j.FunPayExecutor;
import io.github.therepanic.funpay4j.exceptions.FunPayApiException;
import io.github.therepanic.funpay4j.exceptions.lot.LotNotFoundException;
import io.github.therepanic.funpay4j.objects.lot.Lot;

import java.net.InetSocketAddress;
import java.net.Proxy;

/**
 * This is an example of how to get lot
 *
 * @author therepanic
 */
public class GetLot {
    public static void main(String[] args) {
        //if we want to use a proxy
        Proxy proxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress("127.0.0.1", 8000));

        FunPayExecutor executor = new FunPayExecutor(proxy);

        Lot lot;

        try {
            lot = executor.execute(io.github.therepanic.funpay4j.commands.lot.GetLot.builder()
                    .lotId(81L)
                    .build());

            System.out.println(lot);
        } catch (FunPayApiException e) {
            throw new RuntimeException(e);
        } catch (LotNotFoundException e) {
            System.out.println("The lot with such an id does not found!");
        }
    }
}

package io.github.therepanic.funpay4j.offer;

import io.github.therepanic.funpay4j.AuthorizedFunPayExecutor;
import io.github.therepanic.funpay4j.exceptions.FunPayApiException;
import io.github.therepanic.funpay4j.exceptions.InvalidGoldenKeyException;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * This is an example of how to create offer image
 *
 * @author therepanic
 */
public class CreateOfferImage {
    public static void main(String[] args) throws IOException {
        //if we want to use a proxy
        Proxy proxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress("127.0.0.1", 8000));

        AuthorizedFunPayExecutor executor = new AuthorizedFunPayExecutor("test-golden-key", proxy);


        try {
            Long imageId = executor.execute(io.github.therepanic.funpay4j.commands.offer.CreateOfferImage.builder()
                    .image(Files.readAllBytes(Paths.get("PATH-TO-IMAGE")))
                    .build());

            System.out.println(imageId);
        } catch (FunPayApiException e) {
            throw new RuntimeException(e);
        } catch (InvalidGoldenKeyException e) {
            System.out.println("golden key is invalid!");
        }
    }
}

/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.funpay4j.examples.offer;

import ru.funpay4j.core.AuthorizedFunPayExecutor;
import ru.funpay4j.core.exceptions.FunPayApiException;
import ru.funpay4j.core.exceptions.InvalidGoldenKeyException;
import ru.funpay4j.core.exceptions.offer.OfferAlreadyRaisedException;

import java.net.InetSocketAddress;
import java.net.Proxy;

/**
 * This is an example of how to raise all offers
 *
 * @author panic08
 */
public class RaiseAllOffers {
    public static void main(String[] args) {
        //if we want to use a proxy
        Proxy proxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress("127.0.0.1", 8000));

        AuthorizedFunPayExecutor authorizedExecutor = new AuthorizedFunPayExecutor("test-golden-key", proxy);

        try {
            authorizedExecutor.execute(ru.funpay4j.core.commands.offer.RaiseAllOffers.builder()
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

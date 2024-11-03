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

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.HashMap;

/**
 * This is an example of how to edit offer
 *
 * @author panic08
 */
public class EditOffer {
    public static void main(String[] args) {
        //if we want to use a proxy
        Proxy proxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress("127.0.0.1", 8000));

        AuthorizedFunPayExecutor executor = new AuthorizedFunPayExecutor("test-golden-key", proxy);


        try {
            executor.execute(ru.funpay4j.core.commands.offer.EditOffer.builder()
                    .lotId(210L)
                    .offerId(53453453L)
                    .price(200D)
                    .amount(5)
                    .shortDescriptionEn("Dota 2 Item!")
                    .fields(new HashMap<String, String>(){{
                        put("fields[type]", "Одежда");
                        put("fields[hero]", "Abaddon");
                        put("fields[rare]", "Common");
                        put("fields[quality]", "Inscribed");
                        put("fields[method]", "Мгновенно");
                    }})
                    .build());
        } catch (FunPayApiException e) {
            throw new RuntimeException(e);
        } catch (InvalidGoldenKeyException e) {
            System.out.println("golden key is invalid!");
        }
    }
}

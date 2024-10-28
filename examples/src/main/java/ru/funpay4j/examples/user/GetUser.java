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

package ru.funpay4j.examples.user;

import ru.funpay4j.core.FunPayExecutor;
import ru.funpay4j.core.exceptions.FunPayApiException;
import ru.funpay4j.core.exceptions.user.UserNotFoundException;
import ru.funpay4j.core.objects.user.Seller;
import ru.funpay4j.core.objects.user.User;

import java.net.InetSocketAddress;
import java.net.Proxy;

/**
 * This is an example of how to get user
 *
 * @author panic08
 */
public class GetUser {
    public static void main(String[] args) {
        //if we want to use a proxy
        Proxy proxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress("127.0.0.1", 8000));

        FunPayExecutor executor = new FunPayExecutor(proxy);

        User user;

        try {
            user = executor.execute(ru.funpay4j.core.commands.user.GetUser.builder()
                    .userId(1940073)
                    .build());

            System.out.println(user);

            if (user instanceof Seller) {
                //сhecking whether the user is also a Seller

                Seller seller = (Seller) user;
            }
        } catch (FunPayApiException e) {
            throw new RuntimeException(e);
        } catch (UserNotFoundException e) {
            System.out.println("The user with such an id does not found!");
        }
    }
}

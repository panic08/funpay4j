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

package ru.funpay4j.client;

import ru.funpay4j.core.exceptions.FunPayApiException;

/**
 * Interface for sending thematic requests to FunPay
 * Defines the methods that any FunPay client implementation must provide
 *
 * @author panic08
 * @since 1.0.3
 */
public interface FunPayClient {
    /**
     * Send a request to update avatar with goldenKey and avatar
     *
     * @param goldenKey goldenKey which will be used to authorize the user
     * @param newAvatar avatar to be updated
     * @throws FunPayApiException if the goldenKey is incorrect or other api-related exception
     */
    void updateAvatar(String goldenKey, byte[] newAvatar) throws FunPayApiException;
}

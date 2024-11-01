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

package ru.funpay4j.client.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

@Getter
@Builder
public class SaveOfferRequest {
    private long offerId;

    private long nodeId;

    @Nullable
    private String summaryRu;

    @NonNull
    private String summaryEn;

    @Nullable
    private String descRu;

    @Nullable
    private String descEn;

    @Nullable
    private String paymentMessageRu;

    @Nullable
    private String paymentMessageEn;

    @NonNull
    private Map<String, String> fields;

    private boolean isAutoDelivery;

    private boolean isActive;

    @Nullable
    private List<String> secrets;

    private double price;

    private int amount;
}
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

package ru.funpay4j.core.objects.offer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.funpay4j.core.objects.user.PreviewSeller;

import java.util.List;
import java.util.Map;

/**
 * This object represents the FunPay Offer
 *
 * @author panic08
 * @since 1.0.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Offer {
    private long id;

    private String shortDescription;

    private String detailedDescription;

    private Map<String, String> parameters;

    private double price;

    private List<String> attachmentLinks;

    private boolean isAutoDelivery;

    private PreviewSeller seller;
}

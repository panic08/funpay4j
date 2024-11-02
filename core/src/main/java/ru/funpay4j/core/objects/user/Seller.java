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

package ru.funpay4j.core.objects.user;

import lombok.*;
import lombok.experimental.SuperBuilder;
import ru.funpay4j.core.objects.offer.PreviewOffer;

import java.util.List;

/**
 * This object represents the FunPay seller
 *
 * @author panic08
 * @since 1.0.0
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@ToString(callSuper = true)
@SuperBuilder
public class Seller extends User {
    private double rating;

    private int reviewCount;

    private List<PreviewOffer> previewOffers;

    private List<SellerReview> lastReviews;
}

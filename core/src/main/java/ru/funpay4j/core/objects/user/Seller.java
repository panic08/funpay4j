package ru.funpay4j.core.objects.user;

import lombok.*;
import lombok.experimental.SuperBuilder;
import ru.funpay4j.core.objects.offer.PreviewOffer;

import java.util.List;

/**
 * This object represents the FunPay Seller
 *
 * @author panic08
 * @since 1.0.0
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@SuperBuilder
public class Seller extends User {
    private double rating;

    private int reviewCount;

    private List<PreviewOffer> previewOffers;

    private List<SellerReview> lastReviews;
}

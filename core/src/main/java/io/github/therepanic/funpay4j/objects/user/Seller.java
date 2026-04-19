package io.github.therepanic.funpay4j.objects.user;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import io.github.therepanic.funpay4j.objects.offer.PreviewOffer;

/**
 * This object represents the FunPay seller
 *
 * @author therepanic
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

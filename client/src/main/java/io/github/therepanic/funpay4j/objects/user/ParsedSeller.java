package io.github.therepanic.funpay4j.objects.user;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import io.github.therepanic.funpay4j.objects.offer.ParsedPreviewOffer;

/**
 * This object represents the parsed FunPay seller
 *
 * @author therepanic
 * @since 1.0.6
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@ToString(callSuper = true)
@SuperBuilder
public class ParsedSeller extends ParsedUser {
    private double rating;

    private int reviewCount;

    private List<ParsedPreviewOffer> previewOffers;

    private List<ParsedSellerReview> lastReviews;
}

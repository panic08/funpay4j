package io.github.therepanic.funpay4j.objects.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import org.jspecify.annotations.Nullable;

/**
 * This object represents the parsed FunPay seller review
 *
 * @author therepanic
 * @since 1.0.6
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class ParsedSellerReview {
    private String gameTitle;

    private double price;

    private String text;

    private int stars;

    @Nullable private String sellerReplyText;
}

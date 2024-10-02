package ru.funpay4j.core.objects.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This object represents the FunPay Seller Review
 *
 * @author panic08
 * @since 1.0.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SellerReview {
    private String gameTitle;

    private double price;

    private String text;

    private int stars;
}

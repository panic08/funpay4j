package io.github.therepanic.funpay4j.objects.offer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import io.github.therepanic.funpay4j.objects.user.PreviewSeller;

/**
 * This object represents the FunPay preview offer
 *
 * @author therepanic
 * @since 1.0.0
 */
@Data
@AllArgsConstructor
@Builder
public class PreviewOffer {
    private long offerId;

    private String shortDescription;

    private double price;

    private boolean isAutoDelivery;

    private boolean isPromo;

    private PreviewSeller seller;
}

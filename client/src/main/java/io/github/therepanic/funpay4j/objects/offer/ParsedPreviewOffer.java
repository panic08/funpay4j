package io.github.therepanic.funpay4j.objects.offer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import io.github.therepanic.funpay4j.objects.user.ParsedPreviewSeller;

/**
 * This object represents the parsed FunPay preview offer
 *
 * @author therepanic
 * @since 1.0.6
 */
@Data
@AllArgsConstructor
@Builder
public class ParsedPreviewOffer {
    private long offerId;

    private String shortDescription;

    private double price;

    private boolean isAutoDelivery;

    private boolean isPromo;

    private ParsedPreviewSeller seller;
}

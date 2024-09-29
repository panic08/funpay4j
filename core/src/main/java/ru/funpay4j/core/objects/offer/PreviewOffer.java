package ru.funpay4j.core.objects.offer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.funpay4j.core.objects.user.PreviewSeller;
import ru.funpay4j.core.objects.user.PreviewUser;

import java.util.Objects;

/**
 * This object represents the FunPay PreviewOffer
 *
 * @author panic08
 * @since 1.0.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PreviewOffer {
    private long offerId;

    private String shortDescription;

    private double price;

    private boolean isAutoDelivery;

    private boolean isPromo;

    private PreviewSeller seller;
}

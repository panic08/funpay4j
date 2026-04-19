package io.github.therepanic.funpay4j.objects.game;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * This object represents the FunPay promo game counter
 *
 * @author therepanic
 * @since 1.0.0
 */
@Data
@AllArgsConstructor
@Builder
public class PromoGameCounter {
    private long lotId;

    private String title;
}

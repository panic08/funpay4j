package io.github.therepanic.funpay4j.objects.game;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * This object represents the parsed FunPay promo game counter
 *
 * @author therepanic
 * @since 1.0.6
 */
@Data
@AllArgsConstructor
@Builder
public class ParsedPromoGameCounter {
    private long lotId;

    private String title;
}

package io.github.therepanic.funpay4j.objects.game;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * This object represents the FunPay promo game
 *
 * @author therepanic
 * @since 1.0.0
 */
@Data
@AllArgsConstructor
@Builder
public class PromoGame {
    private long lotId;

    private String title;

    private List<PromoGameCounter> promoGameCounters;
}

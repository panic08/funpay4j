package io.github.therepanic.funpay4j.objects.game;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * This object represents the parsed FunPay promo game
 *
 * @author therepanic
 * @since 1.0.6
 */
@Data
@AllArgsConstructor
@Builder
public class ParsedPromoGame {
    private long lotId;

    private String title;

    private List<ParsedPromoGameCounter> promoGameCounters;
}

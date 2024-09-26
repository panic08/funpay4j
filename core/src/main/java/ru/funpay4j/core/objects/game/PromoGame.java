package ru.funpay4j.core.objects.game;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * This object represents the FunPay PromoGame
 *
 * @author panic08
 * @since 1.0.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PromoGame {
    private long lotId;

    private String title;

    private List<PromoGameCounter> promoGameCounters;
}

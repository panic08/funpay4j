package ru.funpay4j.core.objects.game;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This object represents the FunPay PromoGameCounter
 *
 * @author panic08
 * @since 1.0.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PromoGameCounter {
    private long lotId;

    private String title;
}

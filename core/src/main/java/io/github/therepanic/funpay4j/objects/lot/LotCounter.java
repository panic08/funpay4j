package io.github.therepanic.funpay4j.objects.lot;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * This object represents the FunPay lot counter
 *
 * @author therepanic
 * @since 1.0.0
 */
@Data
@AllArgsConstructor
@Builder
public class LotCounter {
    private long lotId;

    private String param;

    private int counter;
}

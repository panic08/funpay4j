package ru.funpay4j.core.objects.lot;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This object represents the FunPay LotCounter
 *
 * @author panic08
 * @since 1.0.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LotCounter {
    private int lotId;

    private String param;

    private int counter;
}

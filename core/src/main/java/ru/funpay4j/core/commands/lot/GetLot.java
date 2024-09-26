package ru.funpay4j.core.commands.lot;

import lombok.*;

/**
 * Use this method to get Lot
 *
 * @author panic08
 * @since 1.0.0
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetLot {
    private long lotId;
}

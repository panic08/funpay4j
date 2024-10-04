package ru.funpay4j.core.commands.offer;

import lombok.*;

/**
 * Use this method to get PromoGames
 *
 * @author panic08
 * @since 1.0.0
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetOffer {
    private long offerId;
}

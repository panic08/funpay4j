package io.github.therepanic.funpay4j.commands.offer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Use this command to raise all offers
 *
 * @author therepanic
 * @since 1.0.3
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
public class RaiseAllOffers {
    private Long gameId;

    private Long lotId;
}

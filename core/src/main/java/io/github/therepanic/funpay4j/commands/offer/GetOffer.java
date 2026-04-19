package io.github.therepanic.funpay4j.commands.offer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Use this command to get offer
 *
 * @author therepanic
 * @since 1.0.0
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
public class GetOffer {
    private Long offerId;
}

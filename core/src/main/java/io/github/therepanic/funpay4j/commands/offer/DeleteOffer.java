package io.github.therepanic.funpay4j.commands.offer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Use this command to delete offer
 *
 * @author therepanic
 * @since 1.0.4
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
public class DeleteOffer {
    private Long lotId;

    private Long offerId;
}

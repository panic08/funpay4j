package io.github.therepanic.funpay4j.commands.game;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Use this command to get promo games
 *
 * @author therepanic
 * @since 1.0.0
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
public class GetPromoGames {
    private String query;
}

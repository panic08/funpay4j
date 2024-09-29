package ru.funpay4j.core.methods.game;

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
public class GetPromoGames {
    @NonNull
    private String query;
}

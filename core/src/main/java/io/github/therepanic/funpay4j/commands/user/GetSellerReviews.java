package io.github.therepanic.funpay4j.commands.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import org.jspecify.annotations.Nullable;

/**
 * Use this command to get seller reviews
 *
 * @author therepanic
 * @since 1.0.1
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
public class GetSellerReviews {
    private Long userId;

    private Integer pages;

    @Nullable private Integer starsFilter;
}

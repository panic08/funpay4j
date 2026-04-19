package io.github.therepanic.funpay4j.commands.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Use this command to get user
 *
 * @author therepanic
 * @since 1.0.0
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
public class GetUser {
    private Long userId;
}

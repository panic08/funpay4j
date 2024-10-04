package ru.funpay4j.core.commands.user;

import lombok.*;

/**
 * Use this method to get User
 *
 * @author panic08
 * @since 1.0.0
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetUser {
    private long userId;
}

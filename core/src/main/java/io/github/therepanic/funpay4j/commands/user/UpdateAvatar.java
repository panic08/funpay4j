package io.github.therepanic.funpay4j.commands.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Use this command to update user avatar
 *
 * @author therepanic
 * @since 1.0.3
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
public class UpdateAvatar {
    private byte[] newAvatar;
}

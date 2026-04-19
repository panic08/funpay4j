package io.github.therepanic.funpay4j.objects.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import org.jspecify.annotations.Nullable;

/**
 * This object represents the parsed FunPay preview user
 *
 * @author therepanic
 * @since 1.0.6
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class ParsedPreviewUser {
    private long userId;

    private String username;

    @Nullable private String avatarPhotoLink;

    private boolean isOnline;
}

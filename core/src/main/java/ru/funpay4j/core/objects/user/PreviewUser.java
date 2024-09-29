package ru.funpay4j.core.objects.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * This object represents the FunPay PreviewUser
 *
 * @author panic08
 * @since 1.0.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class PreviewUser {
    private long userId;

    private String username;

    private String avatarPhotoLink;

    private boolean isOnline;
}

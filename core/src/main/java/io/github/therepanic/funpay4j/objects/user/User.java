package io.github.therepanic.funpay4j.objects.user;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import org.jspecify.annotations.Nullable;

/**
 * This object represents the FunPay user
 *
 * @author therepanic
 * @since 1.0.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class User {
    private long id;

    private String username;

    @Nullable private String avatarPhotoLink;

    private boolean isOnline;

    private List<String> badges;

    @Nullable private Date lastSeenAt;

    private Date registeredAt;
}

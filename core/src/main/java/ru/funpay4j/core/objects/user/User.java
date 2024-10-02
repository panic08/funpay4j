package ru.funpay4j.core.objects.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Date;
import java.util.List;

/**
 * This object represents the FunPay User
 *
 * @author panic08
 * @since 1.0.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class User {
    private long id;

    private String username;

    private String avatarPhotoLink;

    private boolean isOnline;

    private List<String> badges;

    //It is worth changing this type to Date in the future
    private String registeredAt;
}

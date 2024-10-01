package ru.funpay4j.core.objects.user;

import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * This object represents the FunPay PreviewSeller
 *
 * @author panic08
 * @since 1.0.0
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@SuperBuilder
public class PreviewSeller extends PreviewUser {
    private int reviewCount;
}

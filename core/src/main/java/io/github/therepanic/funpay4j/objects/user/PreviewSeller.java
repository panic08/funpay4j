package io.github.therepanic.funpay4j.objects.user;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * This object represents the FunPay preview seller
 *
 * @author therepanic
 * @since 1.0.0
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@ToString(callSuper = true)
@SuperBuilder
public class PreviewSeller extends PreviewUser {
    private int reviewCount;
}

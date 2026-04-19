package io.github.therepanic.funpay4j.objects;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * This object represents the storage of csrfToken and PHPSESSID
 *
 * @author therepanic
 * @since 1.0.4
 */
@Data
@AllArgsConstructor
@Builder
public class CsrfTokenAndPHPSESSID {
    private String csrfToken;

    private String PHPSESSID;
}

package io.github.therepanic.funpay4j.objects.order;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import io.github.therepanic.funpay4j.objects.user.ParsedPreviewUser;

/**
 * This object represents the FunPay order
 *
 * @author therepanic
 * @since 1.0.7
 */
@Data
@AllArgsConstructor
@Builder
public class Order {

    private String id;

    private List<String> statuses;

    private String shortDescription;

    private String detailedDescription;

    private Map<String, String> params;

    private double price;

    private ParsedPreviewUser other;
}

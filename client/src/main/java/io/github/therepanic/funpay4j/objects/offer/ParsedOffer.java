package io.github.therepanic.funpay4j.objects.offer;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import io.github.therepanic.funpay4j.objects.user.ParsedPreviewSeller;

/**
 * This object represents the parsed FunPay offer
 *
 * @author therepanic
 * @since 1.0.6
 */
@Data
@AllArgsConstructor
@Builder
public class ParsedOffer {
    private long id;

    private String shortDescription;

    private String detailedDescription;

    private Map<String, String> parameters;

    private double price;

    private List<String> attachmentLinks;

    private boolean isAutoDelivery;

    private ParsedPreviewSeller seller;
}

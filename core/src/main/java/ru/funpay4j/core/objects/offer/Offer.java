package ru.funpay4j.core.objects.offer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.funpay4j.core.objects.user.PreviewSeller;
import ru.funpay4j.core.objects.user.PreviewUser;

import java.util.List;
import java.util.Map;

/**
 * This object represents the FunPay Offer
 *
 * @author panic08
 * @since 1.0.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Offer {
    private long id;

    private String shortDescription;

    private String detailedDescription;

    private Map<String, String> parameters;

    private double price;

    private List<String> attachmentLinks;

    private boolean isAutoDelivery;

    private PreviewSeller seller;
}

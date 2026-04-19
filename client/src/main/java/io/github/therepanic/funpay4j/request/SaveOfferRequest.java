package io.github.therepanic.funpay4j.request;

import java.util.List;
import java.util.Map;

import lombok.Builder;
import lombok.Getter;

/**
 * This object represents the shell for sending a request to save an offer
 *
 * @author therepanic
 * @since 1.0.4
 */
@Getter
@Builder
public class SaveOfferRequest {
    private Long offerId;

    private Long nodeId;

    private String summaryRu;

    private String summaryEn;

    private String descRu;

    private String descEn;

    private String paymentMessageRu;

    private String paymentMessageEn;

    private Map<String, String> fields;

    private boolean isAutoDelivery;

    private boolean isActive;

    private boolean isDeleted;

    private List<String> secrets;

    private List<Long> images;

    private Double price;

    private Integer amount;
}

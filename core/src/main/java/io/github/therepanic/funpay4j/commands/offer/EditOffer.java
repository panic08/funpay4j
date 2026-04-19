package io.github.therepanic.funpay4j.commands.offer;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import org.jspecify.annotations.Nullable;

/**
 * Use this command to edit offer
 *
 * @author therepanic
 * @since 1.0.4
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
public class EditOffer {
    private Long lotId;

    private Long offerId;

    @Nullable private String shortDescriptionRu;

    private String shortDescriptionEn;

    @Nullable private String descriptionRu;

    @Nullable private String descriptionEn;

    @Nullable private String paymentMessageRu;

    @Nullable private String paymentMessageEn;

    @Nullable private Map<String, String> fields;

    private boolean isAutoDelivery;

    private boolean isActive;

    @Nullable private List<String> secrets;

    @Nullable private List<Long> imageIds;

    private Double price;

    private Integer amount;
}

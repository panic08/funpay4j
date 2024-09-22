package ru.funpay4j.core.objects.lot;

import lombok.*;
import ru.funpay4j.core.objects.offer.PreviewOffer;

import java.util.List;

/**
 * This object represents the FunPay Lot
 *
 * @author panic08
 * @since 1.0.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Lot {
    private long id;

    private String title;

    private String description;

    private List<LotCounter> counters;

    private List<PreviewOffer> previewOffers;
}

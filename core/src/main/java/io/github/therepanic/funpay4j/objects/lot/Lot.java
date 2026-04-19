package io.github.therepanic.funpay4j.objects.lot;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import io.github.therepanic.funpay4j.objects.offer.PreviewOffer;

/**
 * This object represents the FunPay lot
 *
 * @author therepanic
 * @since 1.0.0
 */
@Data
@AllArgsConstructor
@Builder
public class Lot {
    private long id;

    private long gameId;

    private String title;

    private String description;

    private List<LotCounter> lotCounters;

    private List<PreviewOffer> previewOffers;
}

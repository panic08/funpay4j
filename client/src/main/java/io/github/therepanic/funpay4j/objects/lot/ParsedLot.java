package io.github.therepanic.funpay4j.objects.lot;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import io.github.therepanic.funpay4j.objects.offer.ParsedPreviewOffer;

/**
 * This object represents the parsed FunPay lot
 *
 * @author therepanic
 * @since 1.0.6
 */
@Data
@AllArgsConstructor
@Builder
public class ParsedLot {
    private long id;

    private long gameId;

    private String title;

    private String description;

    private List<ParsedLotCounter> lotCounters;

    private List<ParsedPreviewOffer> previewOffers;
}

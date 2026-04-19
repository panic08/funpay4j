package io.github.therepanic.funpay4j.objects.transaction;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import org.jspecify.annotations.Nullable;

/**
 * This object represents the parsed FunPay transaction
 *
 * @author therepanic
 * @since 1.0.6
 */
@Data
@AllArgsConstructor
@Builder
public class ParsedTransaction {
    private long id;

    private String title;

    private double price;

    private ParsedTransactionStatus status;

    @Nullable private String paymentNumber;

    private Date date;
}

package io.github.therepanic.funpay4j.objects.transaction;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import org.jspecify.annotations.Nullable;

/**
 * This object represents the FunPay transaction
 *
 * @author therepanic
 * @since 1.0.6
 */
@Data
@AllArgsConstructor
@Builder
public class Transaction {
    private long id;

    private String title;

    private double price;

    private TransactionStatus status;

    @Nullable private String paymentNumber;

    private Date date;
}

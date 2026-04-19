package io.github.therepanic.funpay4j.commands.transaction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import org.jspecify.annotations.Nullable;

import io.github.therepanic.funpay4j.objects.transaction.TransactionType;

/**
 * Use this command to get transactions
 *
 * @author therepanic
 * @since 1.0.6
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
public class GetTransactions {
    private Long userId;

    @Nullable private TransactionType type;

    private Integer pages;
}

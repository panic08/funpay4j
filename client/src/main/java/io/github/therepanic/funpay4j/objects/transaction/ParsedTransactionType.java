package io.github.therepanic.funpay4j.objects.transaction;

/**
 * Represents the type of parsed FunPay transaction
 *
 * @author therepanic
 * @since 1.0.6
 */
public enum ParsedTransactionType {
    PAYMENT,
    WITHDRAW,
    ORDER,
    OTHER
}

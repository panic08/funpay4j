package io.github.therepanic.funpay4j.commands.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Use this command to get order
 *
 * @author therepanic
 * @since 1.0.7
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
public class GetOrder {

    private String orderId;
}

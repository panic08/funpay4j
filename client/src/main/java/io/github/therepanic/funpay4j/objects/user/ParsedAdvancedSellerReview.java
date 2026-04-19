package io.github.therepanic.funpay4j.objects.user;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@ToString(callSuper = true)
@SuperBuilder
public class ParsedAdvancedSellerReview extends ParsedSellerReview {
    private long senderUserId;

    private String senderUsername;

    private String senderAvatarLink;

    private String orderId;

    private Date createdAt;
}

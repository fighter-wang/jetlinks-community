package org.jetlinks.community.product.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hswebframework.web.dict.EnumDict;

/**
 * 订单状态枚举类
 *
 * @author wangsheng
 * @since 1.0
 */

@Getter
@AllArgsConstructor
public enum OrderStatus implements EnumDict<String> {
    PENDING_PAYMENT("pendingPayment", "待付款"),
    PAYMENT_SUCCESS("paymentSuccess","付款成功"),
    CONFIRMED("confirmed", "确认收货");
//    REFUNDED("refunded", "已退款");
    //    SHIPPED("shipped", "已发货"),
//    COMPLETED("completed", "已完成"),
//    CANCELLED("cancelled", "已取消"),
    //    PREPARING_FOR_SHIPMENT("prepareForShipment", "准备发货"),
//    PENDING_CONFIRMATION("pendingConfirmation", "待确认");

    private final String id;

    private final String name;

    @Override
    public String getValue() {
        return id;
    }

    @Override
    public String getText() {
        return name;
    }
}

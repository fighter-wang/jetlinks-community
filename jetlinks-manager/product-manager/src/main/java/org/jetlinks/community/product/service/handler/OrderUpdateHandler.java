package org.jetlinks.community.product.service.handler;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hswebframework.web.crud.events.EntityBeforeSaveEvent;
import org.jetlinks.community.product.entity.OrderEntity;
import org.jetlinks.community.product.enums.OrderStatus;
import org.jetlinks.core.event.EventBus;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 订单更新事件监听
 *
 * @author wangsheng
 * @since 1.0
 */
@Component
@AllArgsConstructor
@Slf4j
public class OrderUpdateHandler {

    private final EventBus eventBus;

    public static final String TOPIC_UPDATE = "/order/updated";

    @EventListener
    public void handleEvent(EntityBeforeSaveEvent<OrderEntity> event) {
        List<OrderEntity> orderList = event
            .getEntity()
            .stream()
            .filter(orderEntity -> orderEntity.getStatus().getId().equals(OrderStatus.CONFIRMED.getId()))
            .collect(Collectors.toList());
        event.async(
            Flux
                .fromIterable(orderList)
                //发布到事件总线
                .flatMap(e -> eventBus.publish(TOPIC_UPDATE, e))
                .then());
    }
}

package org.jetlinks.community.product.service.handler;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hswebframework.web.crud.events.EntityBeforeSaveEvent;
import org.jetlinks.community.product.entity.ItemEntity;
import org.jetlinks.community.product.entity.OrderEntity;
import org.jetlinks.community.product.service.ItemService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

/**
 * @author wangsheng
 * @since 1.0
 */
@Component
@AllArgsConstructor
@Slf4j
public class OrderBeforeCreateHandler {

    private final ItemService itemService;

    @EventListener
    public void handleEvent(EntityBeforeSaveEvent<OrderEntity> event) {

        event.async(
            itemService
                .createQuery()
                .in(ItemEntity::getId, event
                    .getEntity()
                    .stream()
                    .map(OrderEntity::getItemId)
                    .collect(Collectors.toList()))
                .fetch()
                .switchIfEmpty(Mono.error(new Exception("商品信息不存在")))
                .then());
    }
}
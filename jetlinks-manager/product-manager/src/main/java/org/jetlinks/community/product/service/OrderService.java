package org.jetlinks.community.product.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hswebframework.web.crud.service.GenericReactiveCrudService;
import org.jetlinks.community.product.entity.ItemEntity;
import org.jetlinks.community.product.entity.OrderEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * @author wangsheng
 * @since 1.0
 */
@Service
@AllArgsConstructor
@Slf4j
public class OrderService extends GenericReactiveCrudService<OrderEntity, String> {

    private final ItemService itemService;

    /**
     * 根据商品批次查询关联的订单的数量接口
     *
     * @param batch 商品批次
     * @return
     */
    public Mono<Integer> getOrderAccountByItemBatch(String batch) {
        return itemService
            .createQuery()
            .where(ItemEntity::getBatch, batch)
            .fetch()
            .map(ItemEntity::getId)
            .flatMap(itemId -> createQuery().where(OrderEntity::getItemId, itemId).fetch())
            .collectList()
            .map(list -> list.size());
    }
}

package org.jetlinks.community.product.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hswebframework.web.bean.FastBeanCopier;
import org.hswebframework.web.crud.service.GenericReactiveCrudService;
import org.jetlinks.community.product.entity.ItemEntity;
import org.jetlinks.community.product.entity.OrderEntity;
import org.jetlinks.community.product.service.data.OrderInfoDetail;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
     * 根据订单ID获取订单详细信息
     *
     * @param id 订单ID
     * @return
     */
    public Mono<OrderInfoDetail> getOrderInfoDetail(String id) {
        createQuery()
            .where()
            .and(ItemEntity::getId, ExtendedTermBuilder.termType, "ess")
            .fetch();

        return createQuery()
            .where(OrderEntity::getId, id)
            .fetchOne()
            .flatMap(order -> itemService
                .createQuery()
                .in(ItemEntity::getId, order.getItemIds())
                .fetch()
                .collectList()
                .map(list -> {
                    ArrayList<OrderInfoDetail> objects = new ArrayList<>();
                    OrderInfoDetail orderInfoDetail = FastBeanCopier.copy(order, OrderInfoDetail.class);
                    orderInfoDetail.setItemList(list);
                    return orderInfoDetail;
                })
            );
    }

    /**
     * 根据商品批次查询关联的订单的数量接口
     *
     * @param batch 商品批次
     * @return
     */
    public Mono<Integer> getOrderAccountByItemBatch(String batch) {
        // 1、根据批次batch先查询商品ids
        // 2、根据商品id去订单表中进行模糊查询是否存在当前商品的id，like %id%，去重
        return itemService
            .createQuery()
            .where(ItemEntity::getBatch, batch)
            .fetch()
            .map(item -> item.getId())
            .flatMap(itemId -> this
                .createQuery()
                .$like$(OrderEntity::getItemIds, itemId)
                .fetch()
            )
            .distinct(OrderEntity::getId)
            .collectList()
            .map(list -> list.size());
    }
}

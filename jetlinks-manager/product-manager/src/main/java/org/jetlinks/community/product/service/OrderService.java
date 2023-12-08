package org.jetlinks.community.product.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hswebframework.ezorm.rdb.mapping.defaults.SaveResult;
import org.hswebframework.web.bean.FastBeanCopier;
import org.hswebframework.web.crud.events.EntityPrepareSaveEvent;
import org.hswebframework.web.crud.service.GenericReactiveCrudService;
import org.hswebframework.web.exception.BusinessException;
import org.hswebframework.web.i18n.LocaleUtils;
import org.jetlinks.community.product.entity.ItemEntity;
import org.jetlinks.community.product.entity.OrderEntity;
import org.jetlinks.community.product.service.data.OrderInfoDetail;
import org.jetlinks.core.ProtocolSupport;
import org.jetlinks.core.event.EventBus;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wangsheng
 * @since 1.0
 */
@Service
@AllArgsConstructor
@Slf4j
public class OrderService extends GenericReactiveCrudService<OrderEntity, String> {

    private final ItemService itemService;

    private final EventBus eventBus;
    ;

//    @Transactional
//    public Mono<SaveResult> saveOrder(OrderInfoDetail orderInfoDetail) {
//        // 1、按照商品ID查询商品信息，当商品信息中存在OrderId，表示当前商品已经售出,报错返回
//        Mono<List<ItemEntity>> itemList = itemService
//            .createQuery()
//            .where()
//            .in(ItemEntity::getId, orderInfoDetail.getItemIds())
//            .and()
//            .fetch()
//            .collectList();
//
//
//        // 2、将查询到的商品塞入OrderId，并更新商品信息
//        return itemList
//            .flatMap(items -> {
//                // 3、将订单中的商品信息更新
//                List<ItemEntity> itemEntityList = items
//                    .stream()
////                    .map(item -> {
////                        item.setOrderId(orderInfoDetail.getId());
////                        return item;
////                    })
//                    .collect(Collectors.toList());
//                itemService.save(itemEntityList).subscribe();
//
//                // 4、保存订单信息
//                OrderEntity orderEntity = FastBeanCopier.copy(orderInfoDetail, new OrderEntity());
//                orderEntity.setItemList(itemEntityList);
//                return this.save(orderEntity);
//            });
//    }

    /**
     * 根据订单ID查询订单详情
     *
     * @param id
     * @return
     */
    public Mono<OrderEntity> getOrderInfo(String id) {
        String itemID = "1";
        return createQuery()
            .where()
            .and(OrderEntity::getItemId, "user-third", itemID)
            .fetchOne();
    }

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

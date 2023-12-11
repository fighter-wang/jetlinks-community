package org.jetlinks.community.product.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hswebframework.web.api.crud.entity.PagerResult;
import org.hswebframework.web.api.crud.entity.QueryParamEntity;
import org.hswebframework.web.authorization.annotation.Resource;
import org.hswebframework.web.crud.query.QueryHelper;
import org.hswebframework.web.crud.web.reactive.ReactiveServiceCrudController;
import org.jetlinks.community.product.entity.ItemEntity;
import org.jetlinks.community.product.entity.OrderEntity;
import org.jetlinks.community.product.service.OrderService;
import org.jetlinks.community.product.service.data.OrderDetailInfo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * @author wangsheng
 * @since 1.0
 */
@RestController
@RequestMapping("/prod/order")
@Getter
@AllArgsConstructor
@Resource(id = "order", name = "订单管理接口")
@Tag(name = "订单管理")
public class OrderController implements ReactiveServiceCrudController<OrderEntity, String> {

    private final OrderService service;

    private final QueryHelper queryHelper;

    @GetMapping("/get_detail_list")
    @Operation(summary = "分页查询订单信息")
    public Mono<PagerResult<OrderDetailInfo>> getOrderInfoDetail(@Parameter QueryParamEntity queryParam){
        queryParam.setPaging(true);
        queryParam.setPageSize(10);
        return queryHelper
            .select(OrderDetailInfo.class)
            .all(OrderEntity.class)
            .as(ItemEntity::getName, OrderDetailInfo::setItemName)
            .as(ItemEntity::getType, OrderDetailInfo::setItemType)
            .from(OrderEntity.class)
            .leftJoin(ItemEntity.class, item -> item.is(ItemEntity::getId, OrderEntity::getItemId))
            .where(queryParam)
            .fetchPaged();
    }

//    @GetMapping("/account_order/{batch}")
//    @Operation(summary = "根据商品批次查询关联的订单的数量接口")
//    public Mono<Integer> getOrderAccount(@PathVariable String batch) {
//        return service.getOrderAccountByItemBatch(batch);
//    }

}

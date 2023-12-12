package org.jetlinks.community.product.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.hswebframework.web.api.crud.entity.PagerResult;
import org.hswebframework.web.api.crud.entity.QueryParamEntity;
import org.hswebframework.web.authorization.annotation.Resource;
import org.hswebframework.web.crud.query.QueryHelper;
import org.hswebframework.web.crud.web.reactive.ReactiveServiceCrudController;
import org.jetlinks.community.product.entity.ItemEntity;
import org.jetlinks.community.product.entity.OrderEntity;
import org.jetlinks.community.product.property.OrderTimerProperties;
import org.jetlinks.community.product.service.OrderService;
import org.jetlinks.community.product.service.data.OrderDetailInfo;
import org.jetlinks.community.product.timer.OrderClearTimer;
import org.springframework.web.bind.annotation.*;
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
@Slf4j
public class OrderController implements ReactiveServiceCrudController<OrderEntity, String> {

    private final OrderService service;

    private final QueryHelper queryHelper;

    private final OrderClearTimer orderClearTimer;

    private final OrderTimerProperties orderTimerProperties;

    @GetMapping("/get_detail_list")
    @Operation(summary = "分页查询订单信息")
    public Mono<PagerResult<OrderDetailInfo>> getOrderInfoDetail(@Parameter QueryParamEntity queryParam) {
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

    @GetMapping("/account_order/{batch}")
    @Operation(summary = "根据商品批次查询关联的订单的数量接口")
    public Mono<Integer> getOrderAccount(@PathVariable String batch) {
        return service.getOrderAccountByItemBatch(batch);
    }

    @GetMapping("/execute_or_stop_order_timer")
    @Operation(summary = "订单定时清理执行或停止")
    public Mono<Void> executeOrderTimer(@Parameter Boolean flag) throws Exception {
        if (flag) {
            orderClearTimer.run();
        } else {
            orderClearTimer.shutdown();
        }
        return Mono.empty();
    }

    @PostMapping("/update_order_time")
    @Operation(summary = "更新订单定时器参数")
    public Mono<Void> updateOrderTime(@RequestBody OrderTimerProperties orderTimer) {
        Long expirationTime = orderTimer.getExpirationTime();
        Long executionCycle = orderTimer.getExecutionCycle();
        if (expirationTime > 0) {
            orderTimerProperties.setExpirationTime(expirationTime);
            log.info("过期时间设置成功：{}", expirationTime);
        }

        if (executionCycle > 0) {
            orderTimerProperties.setExecutionCycle(executionCycle);
            log.info("执行周期设置成功：{}", executionCycle);
        }
        // 停止并重启定时任务
        orderClearTimer.shutdown();
        orderClearTimer.run();
        log.info("定时任务已重启！");

        return Mono.empty();
    }
}

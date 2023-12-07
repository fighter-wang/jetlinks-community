package org.jetlinks.community.product.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hswebframework.web.authorization.annotation.Resource;
import org.hswebframework.web.crud.web.reactive.ReactiveServiceCrudController;
import org.jetlinks.community.product.entity.OrderEntity;
import org.jetlinks.community.product.service.OrderService;
import org.jetlinks.community.product.service.data.OrderInfoDetail;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("/get_detail/{id}")
    @Operation(summary = "根据订单ID查询")
    public Mono<OrderInfoDetail> getOrderInfoDetail(@PathVariable String id){

        return service.getOrderInfoDetail(id);
    }

    @GetMapping("/account_order/{batch}")
    @Operation(summary = "根据商品批次查询关联的订单的数量接口")
    public Mono<Integer> getOrderAccount(@PathVariable String batch) {
        return service.getOrderAccountByItemBatch(batch);
    }

}

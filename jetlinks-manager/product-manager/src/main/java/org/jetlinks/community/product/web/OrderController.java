package org.jetlinks.community.product.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hswebframework.ezorm.rdb.mapping.defaults.SaveResult;
import org.hswebframework.web.authorization.annotation.Resource;
import org.hswebframework.web.crud.web.reactive.ReactiveServiceCrudController;
import org.jetlinks.community.product.entity.OrderEntity;
import org.jetlinks.community.product.service.OrderService;
import org.jetlinks.community.product.service.data.OrderInfoDetail;
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
public class OrderController implements ReactiveServiceCrudController<OrderEntity, String> {

    private final OrderService service;

//    @PostMapping("/save_order")
//    @Operation(summary = "保存订单信息")
//    public Mono<SaveResult> save(@RequestBody OrderEntity order){
//        return service.saveOrder(order);
//    }
//    /**
//     * 新增订单
//     * @param order
//     * @return
//     */
//    public Mono<SaveResult> saveOrder(OrderEntity order){
//        return this.save(order);
//    }
    @GetMapping("/get_detail/{id}")
    @Operation(summary = "根据订单ID查询")
    public Mono<OrderEntity> getOrderInfoDetail(@PathVariable String id){
        return service.getOrderInfo(id);
    }

//    @GetMapping("/account_order/{batch}")
//    @Operation(summary = "根据商品批次查询关联的订单的数量接口")
//    public Mono<Integer> getOrderAccount(@PathVariable String batch) {
//        return service.getOrderAccountByItemBatch(batch);
//    }

}

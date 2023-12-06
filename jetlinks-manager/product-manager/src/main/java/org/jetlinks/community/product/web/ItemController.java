package org.jetlinks.community.product.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hswebframework.web.authorization.annotation.Resource;
import org.hswebframework.web.crud.web.reactive.ReactiveServiceCrudController;
import org.jetlinks.community.product.entity.ItemEntity;
import org.jetlinks.community.product.service.ItemService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


/**
 * 商品管理接口
 *
 * @author wangsheng
 * @since 1.0
 */

@RestController
@RequestMapping("/prod/crud")
@AllArgsConstructor
@Getter
@Resource(id = "example", name = "增删改查演示")//
@Tag(name = "商品管理") //swagger
public class ItemController implements ReactiveServiceCrudController<ItemEntity, String> {

    private final ItemService service;

    @GetMapping("/getItems")
    @Operation(summary = "根据商品名查询商品")
    public Flux<ItemEntity> findByName(String name) {
        return service.findByName(name);
    }

    @PostMapping("/saveItem")
    @Operation(summary = "保存商品信息")
    public Mono<Integer> save(ItemEntity item) {
        return service.insert(item);
    }

}

package org.jetlinks.community.product.web;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hswebframework.web.authorization.annotation.Resource;
import org.hswebframework.web.crud.web.reactive.ReactiveServiceCrudController;
import org.jetlinks.community.product.entity.ItemEntity;
import org.jetlinks.community.product.service.ItemService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 商品管理接口
 *
 * @author wangsheng
 * @since 1.0
 */

@RestController
@RequestMapping("/prod/item")
@AllArgsConstructor
@Getter
@Resource(id = "item", name = "商品管理接口")
@Tag(name = "商品管理") //swagger
public class ItemController implements ReactiveServiceCrudController<ItemEntity, String> {

    private final ItemService service;

}

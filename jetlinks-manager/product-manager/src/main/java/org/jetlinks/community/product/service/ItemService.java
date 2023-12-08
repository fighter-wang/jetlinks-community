package org.jetlinks.community.product.service;

import lombok.AllArgsConstructor;
import org.hswebframework.ezorm.rdb.mapping.defaults.SaveResult;
import org.hswebframework.web.crud.service.GenericReactiveCrudService;
import org.jetlinks.community.product.entity.ItemEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

/**
 * @author wangsheng
 * @since 1.0
 */
@Service
public class ItemService extends GenericReactiveCrudService<ItemEntity, String> {

}

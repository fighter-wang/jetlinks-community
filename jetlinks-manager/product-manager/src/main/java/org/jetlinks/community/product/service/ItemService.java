package org.jetlinks.community.product.service;

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
    public Mono<Integer> saveItem(ItemEntity item){
        return insert(item);
    }

    public Flux<ItemEntity> findByName(String name) {
        return createQuery()
            .where()
            .like(ItemEntity::getName, Objects.requireNonNull(name))
            .fetch();
    }

    public Mono<Integer> updateName(String id, String name){
        return createUpdate()
            .set(ItemEntity::getName, name)
            .where(ItemEntity::getId, id)
            .execute();
    }

    public Mono<Integer> deleteItem(String id){
        return createDelete()
            .where(ItemEntity::getId, id)
            .execute();
    }
}

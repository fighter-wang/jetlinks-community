package product.manager;

import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.jetlinks.community.product.entity.ItemEntity;
import org.jetlinks.community.product.service.ItemService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;

/**
 * 商品测试类
 *
 * @author wangsheng
 * @since 1.0
 */

@Slf4j
@SpringBootTest
public class ProductTest {
//    private final ItemService itemService;

    @Test
    public void test01(){
        ItemEntity itemEntity = new ItemEntity();
        itemEntity.setName("矿泉水");
        itemEntity.setBatch("165145156432");
        itemEntity.setType("水资源");
//        Mono<Integer> flag = itemService.insert(itemEntity);
//        log.info("添加成功？{}", flag);
    }
}

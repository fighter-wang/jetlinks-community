package test.java.product.manager;


import lombok.extern.slf4j.Slf4j;
import org.jetlinks.community.product.entity.ItemEntity;
import org.jetlinks.community.product.service.ItemService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Mono;

/**
 * 商品测试类
 *
 * @author wangsheng
 * @since 1.0
 */

@Slf4j
@SpringBootTest(classes = ProductTest.class)
@RunWith(SpringRunner.class)
public class ProductTest {
    private final ItemService itemService = new ItemService();

    @Test
    public void test01() {
        ItemEntity itemEntity = new ItemEntity();
        itemEntity.setName("矿泉水");
        itemEntity.setBatch("165145156432");
        itemEntity.setType("水资源");
        Mono<Integer> flag = itemService.saveItem(itemEntity);
        log.info("添加成功？{}", flag);
    }
}

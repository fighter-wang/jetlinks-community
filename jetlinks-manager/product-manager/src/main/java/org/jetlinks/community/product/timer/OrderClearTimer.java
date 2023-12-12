package org.jetlinks.community.product.timer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetlinks.community.product.entity.OrderEntity;
import org.jetlinks.community.product.property.OrderTimerProperties;
import org.jetlinks.community.product.service.OrderService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import javax.annotation.PreDestroy;
import java.time.Duration;

/**
 * 过期订单定时清理
 *
 * @author wangsheng
 * @since 1.0
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class OrderClearTimer implements CommandLineRunner {

    private final OrderService service;

    private final OrderTimerProperties orderTimerProperties;

    /**
     * 删除指定时间前未修改过的数据
     *
     * @param time 过期时间
     * @return
     */
    private Mono<Integer> delete(Duration time) {
        log.info("删除代码执行成功");
        return service
            .createDelete()
            .where()
            .lt(OrderEntity::getModifyTime, System.currentTimeMillis() - time.toMillis())
            .execute();
    }

    private Disposable disposable;

    /**
     * 停止定时任务
     * 当存在运行中的实例，则直接停止，且将实例赋值为空；反正则打印信息，直接返回
     */
    @PreDestroy
    public void shutdown() {
        // 停止定时任务
        if (disposable != null) {
            disposable.dispose();
            disposable = null;
            log.info("订单定时清理任务已关闭！");
            return;
        }
        log.info("暂未开启订单定时清理器任务！");
    }


    /**
     * 设置定时删除订单任务：执行间隔1秒、删除3分钟之前的所有订单数据
     *
     * @param args
     * @throws Exception
     */
    @Override
    public void run(String... args) {
        if (disposable != null) {
            log.error("您已启动订单定时清理任务，请勿重复启动!{}", disposable);
            return;
        }
        // 在当前热配置定时器中，一定不能使用声明局部属性的方法来获取配置值
        // executionCycle、expirationTime属性只会在调用run方法的时候重新取值
        // 而实际为定时任务只运行subscribe内的代码，因此不可使用以下方法取值作为，Duration中方法的实参
        // Long executionCycle = orderTimerProperties.getExecutionCycle();
        // Long expirationTime = orderTimerProperties.getExpirationTime();

        disposable = Flux
            .interval(Duration.ofSeconds(orderTimerProperties.getExecutionCycle()))
            .subscribe(tick -> {
                log.info("定时执行任务:{}", tick);
                log.info("执行周期：{}", orderTimerProperties.getExecutionCycle());
                log.info("过期时间：{}", orderTimerProperties.getExpirationTime());
                this
                    .delete(Duration.ofMinutes(orderTimerProperties.getExpirationTime()))
                    .subscribe(res -> {
                        log.info("代码执行成功：{}", res);
                    });
            });
    }
}

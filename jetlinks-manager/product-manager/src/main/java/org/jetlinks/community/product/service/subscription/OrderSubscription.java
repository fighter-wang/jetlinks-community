package org.jetlinks.community.product.service.subscription;

import lombok.AllArgsConstructor;
import org.hswebframework.ezorm.rdb.mapping.ReactiveRepository;
import org.jetlinks.community.gateway.external.SubscribeRequest;
import org.jetlinks.community.gateway.external.SubscriptionProvider;
import org.jetlinks.community.product.entity.OrderEntity;
import org.jetlinks.community.product.service.handler.OrderUpdateHandler;
import org.jetlinks.core.event.EventBus;
import org.jetlinks.core.event.Subscription;
import org.jetlinks.core.utils.TopicUtils;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.time.Duration;

/**
 * 订单实时订阅类
 *
 * @author wangsheng
 * @since 1.0
 */
@Component
@AllArgsConstructor
public class OrderSubscription implements SubscriptionProvider {

    private final EventBus eventBus;

    private final ReactiveRepository<OrderEntity, String> entityRepository;

    @Override
    public String id() {
        return "order";
    }

    @Override
    public String name() {
        return "Order";
    }

    @Override
    public String[] getTopicPattern() {
        return new String[]{
            "/order/*"
        };
    }

    @Override
    public Flux<?> subscribe(SubscribeRequest request) {
        String id = TopicUtils.getPathVariables("/order/{id}", request.getTopic())
                              .get("id");

        //定时查询
        Duration interval = request.getDuration("interval").orElse(null);
        if (null != interval) {
            return Flux
                .interval(Duration.ZERO, interval)
                .onBackpressureDrop()
                .concatMap(ignore -> entityRepository.findById(id));
        }

        //订阅事件总线来获取数据变更
        return Flux.concat(
            //查询数据库的最新数据
            entityRepository.findById(id),
            //订阅本地或者集群其他节点的保存事件
            eventBus.subscribe(
                Subscription
                    .builder()
                    .topics(OrderUpdateHandler.TOPIC_UPDATE)
                    //订阅者ID
                    .subscriberId("order-event-subscriber")
                    //订阅本地
                    .local()
                    //订阅集群
                    .broker()
                    .build(),
                OrderEntity.class
            )
        );
    }
}

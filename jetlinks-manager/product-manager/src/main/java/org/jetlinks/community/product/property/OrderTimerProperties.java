package org.jetlinks.community.product.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 订单定时任务相关值
 *
 * @author wangsheng
 * @since 1.0
 */
@Component
@ConfigurationProperties(prefix = "order.timer")
@Setter
@Getter
public class OrderTimerProperties {
    /**
     * 执行周期
     */
    private Long executionCycle;

    /**
     * 过期时间
     */
    private Long expirationTime;
}

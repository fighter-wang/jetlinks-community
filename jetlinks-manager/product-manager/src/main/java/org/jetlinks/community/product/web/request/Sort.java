package org.jetlinks.community.product.web.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author wangsheng
 * @since 1.0
 */
@AllArgsConstructor
@Getter
@Setter
public class Sort {
    private String name;

    private String order;
}
package org.jetlinks.community.product.web.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author wangsheng
 * @since 1.0
 */
@Getter
@Setter
@AllArgsConstructor
public class UserDetailRequest {
    private int pageIndex;

    private int pageSize;

    private List<Sort> sorts;
}
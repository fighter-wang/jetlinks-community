package org.jetlinks.community.product.web.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hswebframework.web.api.crud.entity.PagerResult;

/**
 * @author wangsheng
 * @since 1.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ResponseBody<E>{
    private String message;

    private PagerResult<E> result;

    private Long status;

    private Long timestamp;
}
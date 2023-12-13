package org.jetlinks.community.product.web.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author wangsheng
 * @since 1.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserCreateResponseBody {
    private String message;

    private String result;

    private Long status;

    private Long timestamp;
}

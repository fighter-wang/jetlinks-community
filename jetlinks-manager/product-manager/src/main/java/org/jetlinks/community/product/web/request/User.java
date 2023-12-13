package org.jetlinks.community.product.web.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @author wangsheng
 * @since 1.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User {
    private String confirmPassword;

    private String name;

    private String password;

    private List<String> roleIdList;

    private String username;
}

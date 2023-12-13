package org.jetlinks.community.product.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.hswebframework.web.authorization.annotation.Resource;
import org.jetlinks.community.product.factory.WebClientPoolFactory;
import org.jetlinks.community.product.web.request.Sort;
import org.jetlinks.community.product.web.request.User;
import org.jetlinks.community.product.web.request.UserCreateRequest;
import org.jetlinks.community.product.web.request.UserDetailRequest;
import org.jetlinks.community.product.web.response.ResponseBody;
import org.jetlinks.community.product.web.response.UserCreateResponseBody;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.HashMap;

/**
 * WebClient调用测试类
 *
 * @author wangsheng
 * @since 1.0
 */
@AllArgsConstructor
@RestController
@RequestMapping("/webclient")
@Getter
@Resource(id = "webclient", name = "WebClient接口调用管理")
@Tag(name = "WebClient接口调用")
@Slf4j
public class WebClientController {
    /**
     * ● 持久化一个jetlinks-iot平台（本地启动即可）的WebClient客户端，使用申请的永久token头
     * ● 实现接口：使用该客户端查询iot平台的用户分页列表，并返回数据
     * ● 实现接口：使用该客户端往iot平台新增一个用户，可指定用户名称及密码，接口返回用户id
     */
    private final String commonUri = "http://localhost:9000/api";

    private static WebClient webclient = WebClientPoolFactory.createWebClient();

    @GetMapping("/get_token")
    @Operation(summary = "申请token")
    public Mono<String> init() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("username", "admin");
        map.put("password", "admin");
        map.put("remember", true);
        map.put("expires", -1);
        map.put("verifyCode", "");
        map.put("verifyKey", "");
        webclient
            .post()
            .uri("http://localhost:9000/api/authorize/login")
            .bodyValue(map)
            .retrieve()
            .bodyToMono(String.class)
            .subscribe(res -> {
                WebClientPoolFactory.token = res.substring(res.indexOf("token") + 8, res.indexOf("token") + 40);
                log.info("token值：{}", WebClientPoolFactory.token);
                webclient = webclient.mutate().defaultHeader("X-Access-Token", WebClientPoolFactory.token).build();
            });
        return Mono.just("token申请成功！");
    }

    @GetMapping("/get_user_list")
    @Operation(summary = "WebClient获取用户列表")
    public Mono<ResponseBody<User>> getUserList() {
        int pageIndex = 0;
        int pageSize = 12;
        Sort sort = new Sort("createTime", "desc");
        UserDetailRequest requestBody = new UserDetailRequest(pageIndex, pageSize, Arrays.asList(sort));
        return webclient
            .post()
            .uri(commonUri + "/user/detail/_query")
            .bodyValue(requestBody)
            .retrieve()
            .bodyToMono(String.class)
            .map(res -> {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    return objectMapper.readValue(res, ResponseBody.class);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            });
    }


    @PostMapping("/create_user")
    @Operation(summary = "WebClient创建用户")
    public Mono<String> createUser(@RequestBody UserCreateRequest userCreateRequest) {
        return webclient
            .post()
            .uri(commonUri + "/user/detail/_create")
            .bodyValue(userCreateRequest)
            .retrieve()
            .bodyToMono(String.class)
            .map(res -> {
                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    return objectMapper.readValue(res, UserCreateResponseBody.class).getResult();
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            });
    }
}

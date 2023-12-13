package org.jetlinks.community.product.factory;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

@Slf4j
@Component
public class WebClientPoolFactory {
    /**
     * 连接超时时间（单位：ms）
     */
    private static final int DEFAULT_CONNECT_TIMEOUT_MS = 5000;

    /**
     * 读取超时时间（单位：ms）
     */
    private static final int DEFAULT_READ_TIMEOUT_MS = 5000;

    /**
     * 写入超时时间（单位：ms）
     */
    private static final int DEFAULT_WRITE_TIMEOUT_MS = 5000;

    /**
     * 连接池大小，指定连接池中可同时存在的最大连接数
     */
    private static final int DEFAULT_CONNECTION_POOL_SIZE = 10;

    public static String token = "";

    public static WebClient createWebClient() {
        // 创建连接池资源
        ConnectionProvider connectionProvider = ConnectionProvider.builder("webclient-pool")
                                                                  .maxConnections(DEFAULT_CONNECTION_POOL_SIZE)
                                                                  .build();

        // 创建自定义的 HttpClient
        HttpClient httpClient = HttpClient.create(connectionProvider)
                                          .tcpConfiguration(tcpClient -> tcpClient
                                              .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, DEFAULT_CONNECT_TIMEOUT_MS)
                                              .doOnConnected(connection ->
                                                                 connection
                                                                     .addHandlerLast(new ReadTimeoutHandler(DEFAULT_READ_TIMEOUT_MS))
                                                                     .addHandlerLast(new WriteTimeoutHandler(DEFAULT_WRITE_TIMEOUT_MS))));

        // 使用自定义的 HttpClient 构建 WebClient
        WebClient webClient = WebClient.builder()
                                       .defaultHeader("Content-Type", "application/json")
                                       .clientConnector(new ReactorClientHttpConnector(httpClient))
                                       .build();

        return webClient;
    }


}
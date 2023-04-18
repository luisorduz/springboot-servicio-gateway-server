package com.formacionbdi.springboot.app.gateway.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Component
public class GlobalFilters implements GlobalFilter, Ordered {

    private final Logger logger = LoggerFactory.getLogger(GlobalFilters.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        logger.info("Ejecutando filtro pre");

        exchange.getRequest().mutate().headers(httpHeaders -> {
            httpHeaders.add("token","123456");
        });

        return chain.filter(exchange).then(Mono.fromRunnable(() -> {

            Optional.ofNullable(exchange.getRequest().getHeaders().getFirst("token")).ifPresent(valor -> {
                exchange.getResponse().getHeaders().add("token",valor);
            });
            exchange.getResponse().getCookies().add("color", ResponseCookie.from("Color", "rojo").build());
            //exchange.getResponse().getHeaders().setContentType(MediaType.TEXT_PLAIN);
            logger.info("Ejecutando filtro post");
        }));
    }

    @Override
    public int getOrder() {
        return 10;
    }
}

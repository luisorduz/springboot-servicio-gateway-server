package com.formacionbdi.springboot.app.gateway.factory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class EjemploGatewayFilterFactory extends AbstractGatewayFilterFactory<EjemploGatewayFilterFactory.Configuracion> {

    Logger logger = LoggerFactory.getLogger(GatewayFilter.class);


    public EjemploGatewayFilterFactory() {
        super(Configuracion.class);
    }

    @Override
    public GatewayFilter apply(Configuracion config) {
        return new OrderedGatewayFilter((exchange, chain) -> {
            logger.info("Ejecutando pre gateway factory: "+ config.mensaje.toString());
            Optional.ofNullable(config.cookieValor).ifPresent(s -> {
                exchange.getResponse().addCookie(ResponseCookie.from(config.cookieNombre, s).build());
            });
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                logger.info("Ejecutando post gateway factory");
            }));
        },2);
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList("mensaje","cookieNombre","cookieValor");
    }

    @Override
    public String name() {
        return "EjemploCookie";
    }

    public static class Configuracion{

        private String mensaje;
        private String cookieValor;
        private String cookieNombre;

        public String getMensaje() {
            return mensaje;
        }

        public void setMensaje(String mensaje) {
            this.mensaje = mensaje;
        }

        public String getCookieValor() {
            return cookieValor;
        }

        public void setCookieValor(String cookieValor) {
            this.cookieValor = cookieValor;
        }

        public String getCookieNombre() {
            return cookieNombre;
        }

        public void setCookieNombre(String cookieNombre) {
            this.cookieNombre = cookieNombre;
        }
    }


}

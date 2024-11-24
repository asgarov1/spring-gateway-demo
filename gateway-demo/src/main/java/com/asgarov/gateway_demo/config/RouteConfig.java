package com.asgarov.gateway_demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteConfig {

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder,
                                     @Value("${domains.booking-service}") String bookingService,
                                     @Value("${domains.order-service}") String orderService
    ) {
        return builder.routes()
                .route("booking-route", route -> route.path("/bookings/**").uri(bookingService))
                .route("order-route", route -> route.path("/orders/**").uri(orderService))
                .build();
    }
}

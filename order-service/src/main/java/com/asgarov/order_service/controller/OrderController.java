package com.asgarov.order_service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("orders")
@RestController
public class OrderController {

    @GetMapping("{orderId}")
    public String fetchOrderById(@PathVariable Long orderId) {
        return "Successfully fetched order #" + orderId;
    }
}

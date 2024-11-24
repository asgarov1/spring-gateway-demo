package com.asgarov.booking_service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("bookings")
public class BookingController {

    @GetMapping("{bookindId}")
    public String fetchBookingById(@PathVariable Long bookindId) {
        return "Successfully fetched booking #" + bookindId;
    }
}

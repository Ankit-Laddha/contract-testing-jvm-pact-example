package com.example.dateprovider.controller;

import com.example.dateprovider.model.DateResponse;
import com.example.dateprovider.service.DateService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DateController {

    private final DateService dateService;

    public DateController(DateService dateService) {
        this.dateService = dateService;
    }

    @GetMapping("/provider/validDate")
    public DateResponse getValidDate(@RequestParam(name = "date") String date) {
        return dateService.getValidDate(date);
    }
}

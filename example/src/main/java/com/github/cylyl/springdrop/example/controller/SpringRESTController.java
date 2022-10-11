package com.github.cylyl.springdrop.example.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class SpringRESTController {


    @GetMapping
    public String get() {
        return "hello from spring";
    }
}

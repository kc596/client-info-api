package com.kunal.clientinfo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/clientinfo")
public class HealthCheckController {

    @RequestMapping(value = "/status")
    public ResponseEntity<String> status(){
        return ResponseEntity.ok("Application is up and running!");
    }
}

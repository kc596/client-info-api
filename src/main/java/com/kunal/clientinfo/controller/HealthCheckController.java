package com.kunal.clientinfo.controller;

import com.kunal.clientinfo.config.ApplicationConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {
    @Autowired
    private ApplicationConfig applicationConfig;

    @RequestMapping(value = "/status")
    public ResponseEntity<String> status(){
        return ResponseEntity.ok("Application is up and running! data-center: "+applicationConfig.getDataCenter());
    }
}

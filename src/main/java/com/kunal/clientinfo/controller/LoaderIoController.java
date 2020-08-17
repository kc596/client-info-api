package com.kunal.clientinfo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class LoaderIoController {

    @RequestMapping(value = "loaderio-ac58369850c5f2aec5e4cfe47e163734/")
    public ResponseEntity<String> get() {
        return ResponseEntity.ok("loaderio-ac58369850c5f2aec5e4cfe47e163734");
    }
}

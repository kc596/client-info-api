package com.kunal.clientinfo.controller;

import com.kunal.clientinfo.service.WhoisInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/whoisinfo")
public class WhoisInfoController {

    private final WhoisInfoService whoisInfoService;

    @Autowired
    public WhoisInfoController(WhoisInfoService whoisInfoService) {
        this.whoisInfoService = whoisInfoService;
    }

    @RequestMapping(method = RequestMethod.POST, path = "/bulk")
    public ResponseEntity<List<String>> whoisInfoBulk(@RequestBody String[] urls) {
        return ResponseEntity.ok(whoisInfoService.getWhoisInfoOfUrls(urls));
    }

    @RequestMapping(path = "/")
    public ResponseEntity<String> whoisInfoSingle(@RequestParam String domain) {
        return ResponseEntity.ok(whoisInfoService.getWhoisInfoOfUrls(new String[]{domain}).get(0));
    }
}

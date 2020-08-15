package com.kunal.clientinfo.controller;

import com.kunal.clientinfo.dto.ClientInfoResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/clientinfo")
public class ClientInfoController {

    @RequestMapping(value = "/view")
    public ResponseEntity<Optional<ClientInfoResponseDTO>> view(@RequestHeader MultiValueMap<String, String> headers, HttpServletRequest request) {
        headers.forEach((key, value) -> {
            System.out.println(String.format(
                    "Header '%s' = %s", key, value.stream().collect(Collectors.joining("|"))));
        });
        headers.getFirst("user-agent");
        return ResponseEntity.ok(Optional.empty());
    }
}

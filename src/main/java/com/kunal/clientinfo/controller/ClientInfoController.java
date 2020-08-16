package com.kunal.clientinfo.controller;

import com.kunal.clientinfo.dto.ClientInfoResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/clientinfo")
public class ClientInfoController {

    @RequestMapping(value = "/view")
    public ResponseEntity<Optional<ClientInfoResponseDTO>> view(@RequestHeader MultiValueMap<String, String> headers) {
        ClientInfoResponseDTO clientInfoResponseDTO = new ClientInfoResponseDTO();
        clientInfoResponseDTO.setUserAgent(headers.getFirst("user-agent"));
        clientInfoResponseDTO.setIp(headers.getFirst("x-real-ip"));
        clientInfoResponseDTO.setRoute(headers.getFirst("x-forwarded-for"));
        clientInfoResponseDTO.setProxy(headers.getFirst("via"));
        return ResponseEntity.ok(Optional.of(clientInfoResponseDTO));
    }
}

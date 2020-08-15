package com.kunal.clientinfo.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ClientInfoResponseDTO {
    private String userAgent;
    private String ip;
    private String proxy;
}

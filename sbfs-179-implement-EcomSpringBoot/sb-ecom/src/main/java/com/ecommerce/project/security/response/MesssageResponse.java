package com.ecommerce.project.security.response;

import lombok.Data;

@Data
public class MesssageResponse {
    private String message;
    public MesssageResponse(String message) {
        this.message = message;
    }
}

package com.example.configclient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope
//@Profile("dev")
public class Controller {

    @Value("${message:DEFAULT environment}")
    private String message;

    @GetMapping("/message")
    public String getMessage() {
        System.out.println(message);
        return message;
    }
}

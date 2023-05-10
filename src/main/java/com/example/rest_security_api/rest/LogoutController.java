package com.example.rest_security_api.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/logout")
public class LogoutController {


    @GetMapping
    public ResponseEntity<Void> logout(HttpServletRequest request) {
//        request.getSession().removeAttribute("Authorization");
        request.getSession().setAttribute("Authorization", null);
        return ResponseEntity.noContent().build();
    }
}

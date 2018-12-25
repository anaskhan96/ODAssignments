package com.example.identity.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/* Controller for /login and /login/new, for login and registration purposes */
@RestController
@RequestMapping("/login")
public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @PostMapping("")
    public String login() {
        logger.info("GET /login");
        return "Login API";
    }

    @PostMapping("/new")
    public String signUp() {
        logger.info("GET /login/new");
        return "SignUp API";
    }
}

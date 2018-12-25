package com.example.identity.controllers;

import com.example.identity.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/* Controller for /home, which only authenticated users can view */
@RestController
@RequestMapping("/home")
public class HomeController {
    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @GetMapping("")
    public User getUserDetails(@RequestAttribute("name") String name) {
        logger.info("GET /home");
        return new User(-1, name, "test@email.com", "8998998998");
    }
}

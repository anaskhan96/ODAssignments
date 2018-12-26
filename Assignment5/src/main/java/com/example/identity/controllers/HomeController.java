package com.example.identity.controllers;

import com.example.identity.models.User;
import com.example.identity.models.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/* Controller for /home, which only authenticated users can view */
@RestController
@RequestMapping("/home")
public class HomeController {
    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
    @Autowired
    private UserRepository userRepository;

    @GetMapping("")
    public User getUserDetails(@RequestAttribute("number") String number) {
        logger.info("GET /home");
        return userRepository.findUserByPhoneNumber(number);
    }
}

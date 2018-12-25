package com.example.identity.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/* Controller for /logout, which invalidates the token and logs out */
@RestController
@RequestMapping("/logout")
public class LogoutController {
    private static final Logger logger = LoggerFactory.getLogger(LogoutController.class);

    @GetMapping("")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        //TODO - Invalidate token
        Map<String, String> logoutMap = new HashMap<>();
        logoutMap.put("success", "true");
        String logoutJson = "";
        try {
            logoutJson = new ObjectMapper().writeValueAsString(logoutMap);
        } catch (JsonProcessingException jpe) {
            logger.error("Error producing the JSON response in LogoutController");
        }
        response.setContentType("application/json");
        response.setContentLength(logoutJson.length());
        try {
            response.getWriter().write(logoutJson);
        } catch (IOException ie) {
            logger.error("Couldn't fetch writer to write response in LogoutController");
        }
    }
}

package com.example.identity.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/* A controller for any kind of 4xx or 5xx error to fall back on, if not implemented */
@RestController
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {
    private static final Logger logger = LoggerFactory.getLogger(ErrorController.class);
    private static final String PATH = "/error";

    @RequestMapping(value = PATH)
    public String error(HttpServletRequest request) {
        logger.info(request.getMethod() + " /error");
        return "An error occurred.";
    }

    @Override
    public String getErrorPath() {
        return PATH;
    }
}

package com.example.identity.middleware;

import com.example.identity.auth.Auth;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/* Middleware for /home */
public class HomeInterceptor extends HandlerInterceptorAdapter {
    private static final Logger logger = LoggerFactory.getLogger(HomeInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String authToken;
        try {
            authToken = request.getHeader("Authorization");
        } catch (Exception e) {
            authToken = null;
        }
        String authNumber = getAuthUser(authToken);
        // check if no authorization header is provided or auth token is incorrect
        if (authNumber.isEmpty()) {
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("error", "Authorization failed");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            String errorJson = errorMap.toString();
            try {
                errorJson = new ObjectMapper().writeValueAsString(errorMap);
            } catch (JsonProcessingException jpe) {
                logger.error("Error producing the JSON response in LoginInterceptor");
            }
            response.setContentLength(errorJson.length());
            try {
                response.getWriter().write(errorJson);
            } catch (IOException ie) {
                logger.error("Couldn't fetch writer to write response in LoginInterceptor");
            }
            return false;
        }
        request.setAttribute("number", authNumber);
        return true;
    }

    private static String getAuthUser(String token) {
        if (token == null || (token.split(" ").length < 2)) return ""; // token.split(" ")[0] is the word "Bearer"
        return Auth.verifyToken(token.split(" ")[1]);
    }
}

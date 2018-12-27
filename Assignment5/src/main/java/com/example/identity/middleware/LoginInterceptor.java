package com.example.identity.middleware;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

public class LoginInterceptor extends HandlerInterceptorAdapter {
    private static final Logger logger = LoggerFactory.getLogger(LoginInterceptor.class);
    private Queue<Boolean> requestsQueue = new LinkedList<>();
    private int limit = 15; // number of requests to be allowed in a period
    private boolean initialized = false;
    private Timer timer = new Timer();

    /* Rate limiting for /login/generateOTP API for all kinds of clients */
    /* Simple Rate Limiting Implementation instead of Redis implementation for basic purposes */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        if (!this.initialized) {
            for (int i = 0; i < this.limit; i++)
                this.requestsQueue.add(true);
            setupRateLimit();
            this.initialized = true;
        }
        boolean allowRequest;
        try {
            allowRequest = this.requestsQueue.remove();
        } catch (NoSuchElementException nse) {
            logger.info("Rate limiter prohibiting access");
            allowRequest = false;
        }
        if (!allowRequest) {
            // 429 status code for too many requests
            response.setStatus(429);
            response.setContentType("application/json");
            Map<String, String> responseMap = new HashMap<>();
            responseMap.put("error", "Too many requests within an hour, the limit is " + this.limit);
            String responseJson = new ObjectMapper().writeValueAsString(responseMap);
            response.setContentLength(responseJson.length());
            response.getWriter().write(responseJson);
        }
        return allowRequest;
    }

    private void setupRateLimit() {
        TimerTask incrementTask = new TimerTask() {
            @Override
            public void run() {
                if (requestsQueue.size() < limit)
                    requestsQueue.add(true);
            }
        };
        this.timer.schedule(incrementTask, 0, 3600000); // setting the period for an hour
    }
}

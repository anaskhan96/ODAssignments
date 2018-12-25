package com.example.identity.controllers;

import com.example.identity.models.User;
import com.example.identity.models.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

/* Controller for /login and /login/new, for login and registration purposes */
@RestController
@RequestMapping("/login")
public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    @Autowired
    private UserRepository userRepository;
    private static Map<String, Integer> otpTempStore = new HashMap<>();
    private Timer timer = new Timer();

    @PostMapping("/sendOTP")
    public void sendOtp(@RequestBody Map<String, String> requestBody, HttpServletResponse response) throws IOException {
        logger.info("GET /login/sendOTP");

        response.setContentType("application/json");
        PrintWriter respWriter = response.getWriter();
        String responseJson = "";
        Map<String, String> responseMap = new HashMap<>();

        String number = requestBody.get("phoneNumber");
        int otpHash = requestBody.get("otp").hashCode();
        if (otpTempStore.get(number) == otpHash) {
            this.timer.cancel();
            otpTempStore.remove(number);
            //TODO - generate token
            responseMap.put("success", "true");
            responseMap.put("token", "JWT12345");
            try {
                responseJson = new ObjectMapper().writeValueAsString(responseMap);
            } catch (JsonProcessingException jpe) {
                logger.error("Error producing the JSON response in LoginController:generateOtp");
            }
            response.setContentLength(responseJson.length());
            respWriter.write(responseJson);
        } else {
            responseMap.put("success", "false");
            try {
                responseJson = new ObjectMapper().writeValueAsString(responseMap);
            } catch (JsonProcessingException jpe) {
                logger.error("Error producing the JSON response in LoginController:generateOtp");
            }
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentLength(responseJson.length());
            respWriter.write(responseJson);
        }
    }

    @PostMapping("/generateOTP")
    public void generateOtp(@RequestBody User user, HttpServletResponse response) throws IOException {
        logger.info("GET /login/generateOTP");

        response.setContentType("application/json");
        PrintWriter respWriter = response.getWriter();
        String responseJson = "";
        Map<String, String> responseMap = new HashMap<>();

        // phoneNumber should be available in the request body
        if (user.getPhoneNumber() == null) {
            responseMap.put("error", "phoneNumber is required in the request body");
            try {
                responseJson = new ObjectMapper().writeValueAsString(responseMap);
            } catch (JsonProcessingException jpe) {
                logger.error("Error producing the JSON response in LoginController:generateOtp");
            }
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentLength(responseJson.length());
            respWriter.write(responseJson);
            return;
        }

        generateAndSend(user);
        userRepository.save(user);
        responseMap.put("message", "POST at /login/sendOTP with the phoneNumber and received OTP in the next two minutes");
        try {
            responseJson = new ObjectMapper().writeValueAsString(responseMap);
        } catch (JsonProcessingException jpe) {
            logger.error("Error producing the JSON response in LoginController:generateOtp");
        }
        response.setStatus(HttpServletResponse.SC_CREATED);
        response.setContentLength(responseJson.length());
        respWriter.write(responseJson);
    }

    private void generateAndSend(User user) throws IOException {
        // generating the otp
        String digits = "0123456789";
        Random random = new Random();
        char[] otpDigits = new char[4];
        for (int i = 0; i < 4; i++)
            otpDigits[i] = digits.charAt(random.nextInt(digits.length()));
        String otp = new String(otpDigits);
        logger.info("OTP generated: " + otp);

        // sending the otp
        URL url = new URL("http://api.msg91.com/api/sendhttp.php?country=91&sender=TESTIN&route=4&mobiles=" + user.getPhoneNumber() + "&authkey=253457ANDpNgBpYDw5c21e663&message=OTP-" + otp);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        logger.info(conn.getResponseMessage());

        // storing the otp for two minutes only
        otpTempStore.put(user.getPhoneNumber(), otp.hashCode());
        TimerTask deleteOtp = new TimerTask() {
            @Override
            public void run() {
                otpTempStore.remove(user.getPhoneNumber());
                userRepository.delete(user);
            }
        };
        this.timer.schedule(deleteOtp, 120000);
    }
}

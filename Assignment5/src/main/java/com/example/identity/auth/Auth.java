package com.example.identity.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Auth {
    private static final String secret = "xxxidentityxxx";
    private static List<String> tokenBlacklist = new ArrayList<>();

    /* Create a JWT token, and pass the phoneNumber as a header claim */
    public static String createToken(String phoneNumber) throws JWTCreationException {
        Map<String, Object> headerClaims = new HashMap<>();
        headerClaims.put("number", phoneNumber);
        Algorithm algorithm = Algorithm.HMAC256(secret);
        return JWT.create()
                .withIssuer("auth0")
                .withHeader(headerClaims)
                .sign(algorithm);

    }

    /* Verify the JWT token, and retrieve the phoneNumber from the decoded token */
    public static String verifyToken(String token) throws JWTVerificationException {
        // check if the token has been blacklisted by a /logout before
        if (tokenBlacklist.contains(token)) return "";
        Algorithm algorithm = Algorithm.HMAC256(secret);
        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer("auth0")
                .acceptLeeway(2)
                .acceptExpiresAt(3600) // 60 minutes for expiration
                .build();
        DecodedJWT jwt = verifier.verify(token);
        return jwt.getHeaderClaim("number").asString();
    }

    /* Invalidate the JWT token when sent during /logout */
    public static void invalidateToken(String token) {
        // add token to the blacklist to reject further requests
        tokenBlacklist.add(token);
    }
}
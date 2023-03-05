package com.example.demo;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Controller
public class ConnectionHandler {
    private static MessageDigest digest;

    private int userCount = 0;
    static String user1;
    static String user2;

    public ConnectionHandler() {
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @MessageMapping("/join")
    public void loggingIn(String token) {
        System.out.println("NEW USER JOINED:");
        System.out.println(token);
        switch (userCount) {
            case 0 -> {
                user1 = encrypt(token);
                userCount++;
            }

            case 1 -> {
                user2 = encrypt(token);
                if(user2.equals(user1))
                    return;

                userCount++;
            }
        }
    }

    static String encrypt(String token) {
        byte[] hash = digest.digest(token.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(hash);
    }
}

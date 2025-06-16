package com.example.collaboration.collab.service;

import org.springframework.stereotype.Service;

@Service
public class RequestService {

    // generate a new request id
    public String generateRequestId() {
        String prefix = "REQ-";
        String timeStamp = String.valueOf(System.currentTimeMillis());
        String randomSuffix = String.valueOf((int) (Math.random() * 10000)); // Random 4-digit number
        return prefix + timeStamp + randomSuffix;
    }
}

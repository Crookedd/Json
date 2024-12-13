package org.example;

import lombok.Data;

@Data
public class SmsMessage {
    private String phoneNumber;
    private String message;

    public SmsMessage(String phoneNumber, String message) {
        this.phoneNumber = phoneNumber;
        this.message = message;
    }
}
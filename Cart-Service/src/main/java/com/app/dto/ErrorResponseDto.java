package com.app.dto;

public class ErrorResponseDto {
    private String message;

    public ErrorResponseDto(String message) {
        this.message = message;
    }

    // Getter for message
    public String getMessage() {
        return message;
    }
}

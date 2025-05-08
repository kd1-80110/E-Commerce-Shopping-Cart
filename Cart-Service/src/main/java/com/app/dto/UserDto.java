package com.app.dto;

import lombok.Data;

@Data
public class UserDto {
    private Long id;
    private String userName;
    private String email;
    private String contactNumber;
    // You might have other user details here
}
package com.app.dto;

import lombok.Data;

@Data
public class RegisterUserDto {

	private String userName;
	private String password;
	private String email;
	private String contactNumber;
}

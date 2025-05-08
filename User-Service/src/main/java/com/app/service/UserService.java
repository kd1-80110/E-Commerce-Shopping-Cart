package com.app.service;

import com.app.dto.JwtResponse;
import com.app.dto.LoginDto;
import com.app.dto.RegisterUserDto;
import com.app.dto.UpdateUserDto;
import com.app.dto.UserDto;

public interface UserService {

	String registerUser(RegisterUserDto userDto);

	JwtResponse login(LoginDto loginDto);

	UserDto getUserProfile(String userName);

	void updateUserProfile(String userName, UpdateUserDto dto);

}

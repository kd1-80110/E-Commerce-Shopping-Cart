package com.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.dto.JwtResponse;
import com.app.dto.LoginDto;
import com.app.dto.RegisterUserDto;
import com.app.dto.UpdateUserDto;
import com.app.dto.UserDto;
import com.app.entity.User;
import com.app.repo.UserRepo;
import com.app.security.JwtUtil;
import com.app.service.UserService;

@RestController
@RequestMapping("/users")
@CrossOrigin
public class UserController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private JwtUtil jwtUtil;

	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody RegisterUserDto userDto) {
		String message = userService.registerUser(userDto);
		return ResponseEntity.ok(message);
	}

	@PostMapping("/login")
	public ResponseEntity<JwtResponse> login(@RequestBody LoginDto loginDto) {
		JwtResponse jwt = userService.login(loginDto);
		return ResponseEntity.ok(jwt);
	}
	
	@GetMapping("/profile")
    public ResponseEntity<UserDto> getProfile(Authentication authentication) {
        String username = authentication.getName();
        UserDto userDto = userService.getUserProfile(username);
        return ResponseEntity.ok(userDto);
    }

    @PutMapping("/profile")
    public ResponseEntity<String> updateProfile(@RequestBody UpdateUserDto dto, Authentication authentication) {
        String username = authentication.getName();
        userService.updateUserProfile(username, dto);
        return ResponseEntity.ok("Profile updated successfully");
    }
    
    @GetMapping("/user-id")
    public ResponseEntity<Long> getUserIdFromToken(@RequestHeader("Authorization") String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            String username = jwtUtil.extractUsername(token);

            User user = userRepo.findByUserName(username)
                    .orElseThrow(() -> new RuntimeException("User not found for username: " + username));

            return ResponseEntity.ok(user.getId());
        } else {
            return ResponseEntity.status(401).body(null); // Unauthorized
        }
    }

}

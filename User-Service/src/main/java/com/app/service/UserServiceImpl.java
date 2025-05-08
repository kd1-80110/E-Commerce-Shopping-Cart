package com.app.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.app.dto.JwtResponse;
import com.app.dto.LoginDto;
import com.app.dto.RegisterUserDto;
import com.app.dto.UpdateUserDto;
import com.app.dto.UserDto;
import com.app.entity.Role;
import com.app.entity.User;
import com.app.repo.RoleRepo;
import com.app.repo.UserRepo;
import com.app.security.JwtUtil;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private RoleRepo roleRepo;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private AuthenticationManager authManager;

	@Autowired
	private JwtUtil jwtUtil;

	@Override
	public String registerUser(RegisterUserDto userDto) {
		if (userRepo.existsByUserName(userDto.getUserName())) {
			return "Username already exists!";
		}

		User user = new User();
		user.setUserName(userDto.getUserName());
		user.setPassword(passwordEncoder.encode(userDto.getPassword()));
		user.setEmail(userDto.getEmail());
		user.setContactNumber(userDto.getContactNumber());

		Set<Role> roles = new HashSet<>();
		Role defaultRole = roleRepo.findByRoleName("ROLE_USER")
		        .orElseGet(() -> {
		            Role newRole = new Role();
		            newRole.setRoleName("ROLE_USER");
		            return roleRepo.save(newRole);
		        });
		roles.add(defaultRole);
		user.setRoles(roles);

		userRepo.save(user);
		return "User registered successfully!";
	}

	@Override
	public JwtResponse login(LoginDto loginDto) {
		Authentication auth = authManager
				.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUserName(), loginDto.getPassword()));

		String token = jwtUtil.generateToken((org.springframework.security.core.userdetails.User) auth.getPrincipal());
		return new JwtResponse(token);
	}
	
	@Override
    public UserDto getUserProfile(String userName) {
        User user = userRepo.findByUserName(userName)
            .orElseThrow(() -> new RuntimeException("User not found"));

        UserDto dto = new UserDto();
        dto.setUserName(user.getUserName());
        dto.setEmail(user.getEmail());
        dto.setContactNumber(user.getContactNumber());
        return dto;
    }

    @Override
    public void updateUserProfile(String userName, UpdateUserDto dto) {
        User user = userRepo.findByUserName(userName)
            .orElseThrow(() -> new RuntimeException("User not found"));

        if (dto.getEmail() != null) user.setEmail(dto.getEmail());
        if (dto.getContactNumber() != null) user.setContactNumber(dto.getContactNumber());

        userRepo.save(user);
    }
}

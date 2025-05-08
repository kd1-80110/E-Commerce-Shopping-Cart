package com.app.service;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.entity.User;
import com.app.repo.UserRepo;

@Service
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepo userRepo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepo.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
		
		var authorities = user.getRoles().stream()
                .map(role -> new org.springframework.security.core.authority.SimpleGrantedAuthority(role.getRoleName()))
                .collect(Collectors.toList());

        return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(), authorities);
	}

}

package com.app;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.app.entity.Role;
import com.app.entity.User;
import com.app.repo.RoleRepo;
import com.app.repo.UserRepo;

@Component
public class AdminUserInitializer implements CommandLineRunner {

	@Value("${admin.username}")
    private String adminUsername;

    @Value("${admin.password}")
    private String adminPassword;

    @Value("${admin.email}")
    private String adminEmail;
    
    @Value("${admin.contact}")
    private String adminContact;

    @Value("${admin.role}")
    private String adminRole;

    @Autowired
    private UserRepo userRepository;

    @Autowired
    private RoleRepo roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (!userRepository.existsByUserName(adminUsername)) {
            Role role = roleRepository.findByRoleName(adminRole)
                .orElseGet(() -> {
                    Role newRole = new Role();
                    newRole.setRoleName(adminRole);
                    return roleRepository.save(newRole);
                });

            User admin = new User();
            admin.setUserName(adminUsername);
            admin.setPassword(passwordEncoder.encode(adminPassword));
            admin.setEmail(adminEmail);
            admin.setContactNumber(adminContact); // default number
            admin.setRoles(Set.of(role));

            userRepository.save(admin);
            System.out.println("✅ Admin user created from envirnoment variables.");
        } else {
            System.out.println("ℹ️ Admin user already exists.");
        }
    }

}

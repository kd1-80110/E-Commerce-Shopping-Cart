package com.app.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.entity.Role;

public interface RoleRepo extends JpaRepository<Role, Long> {

	Optional<Role> findByRoleName(String roleName);
}

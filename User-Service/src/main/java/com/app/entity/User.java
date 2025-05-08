package com.app.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.Data;

@Entity
@Data
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 30, unique = true, nullable = false)
	private String userName;

	@Column(length = 300, nullable = false)
	private String password;

	@Column(length = 50, unique = true, nullable = false)
	private String email;

	@Column(length = 15)
	private String contactNumber;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
		    name = "user_roles",
		    joinColumns = @JoinColumn(name = "user_id"),
		    inverseJoinColumns = @JoinColumn(name = "role_id")
		    )
	private Set<Role> roles = new HashSet<>();

}

package com.example.springboot.repositories.user;

import org.springframework.stereotype.Repository;

import com.example.springboot.models.user.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

@Repository
public interface UserRepository extends JpaRepository<UserModel, String>{
	UserDetails findByLogin(String login);
}

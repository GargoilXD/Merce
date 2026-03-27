package com.merce.service;

import com.merce.model.db.User;
import com.merce.model.db.enums.Role;
import com.merce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User login(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public boolean isEmailTaken(String email) {
        return userRepository.existsByEmail(email);
    }

    public long countAllUsers() {
        return userRepository.countByRole(Role.USER);
    }

    public List<User> searchUsers(String keyword) {
        return userRepository.searchByNameOrEmail(keyword);
    }
}

package com.example.beassignmentjava.domain.auth.repository;

import com.example.beassignmentjava.domain.auth.entity.User;
import java.util.Optional;

public interface UserRepository {
	Optional<User> findById(Long userId);
	Optional<User> findByUsername(String username);
	User save(User user);
	boolean existsByUsername(String username);

}

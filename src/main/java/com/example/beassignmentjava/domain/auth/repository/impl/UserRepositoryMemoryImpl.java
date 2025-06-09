package com.example.beassignmentjava.domain.auth.repository.impl;

import com.example.beassignmentjava.domain.auth.entity.User;
import com.example.beassignmentjava.domain.auth.repository.UserRepository;
import com.example.beassignmentjava.exception.ApplicationException;
import com.example.beassignmentjava.exception.ErrorCode;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Component;

@Component
public class UserRepositoryMemoryImpl implements UserRepository {
	private final Map<Long, User> users;
	private final AtomicLong id;

	public UserRepositoryMemoryImpl() {
		users = new HashMap<>();
		id = new AtomicLong();
	}

	@Override
	public Optional<User> findById(Long userId) {
		User user = users.get(userId);
		return Optional.ofNullable(user);
	}

	@Override
	public Optional<User> findByUsername(String username) {
		return users.values().stream().filter(user -> user.getUsername().equals(username)).findFirst();
	}

	@Override
	public User save(User user) {

		if(user == null) {
			throw new ApplicationException(ErrorCode.INVALID_USER);
		}

		// 신규 유저일 때만 중복 확인
		if (user.getId() == null && existsByUsername(user.getUsername())) {
			throw new ApplicationException(ErrorCode.USER_ALREADY_EXISTS);
		}

		if(user.getId() == null) {
			user.assignId(id.incrementAndGet());
		}

		users.put(user.getId(), user);

		return user;
	}

	@Override
	public boolean existsByUsername(String username) {
		return users.values().stream().anyMatch(user -> user.getUsername().equals(username));
	}
}

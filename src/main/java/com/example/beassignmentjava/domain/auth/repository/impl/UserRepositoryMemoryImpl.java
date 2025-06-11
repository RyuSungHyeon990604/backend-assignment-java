package com.example.beassignmentjava.domain.auth.repository.impl;

import com.example.beassignmentjava.domain.auth.entity.User;
import com.example.beassignmentjava.domain.auth.enums.UserRole;
import com.example.beassignmentjava.domain.auth.repository.UserRepository;
import com.example.beassignmentjava.exception.ApplicationException;
import com.example.beassignmentjava.exception.ErrorCode;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Component;

@Component
public class UserRepositoryMemoryImpl implements UserRepository {
	private final Map<Long, User> users;
	private final Map<String, Long> usernameIdx;
	private final AtomicLong id;

	public UserRepositoryMemoryImpl() {
		// 동일 username 생성 가능성 존재 ==> ConcurrentHashMap 사용
		users = new ConcurrentHashMap<>();
		usernameIdx = new ConcurrentHashMap<>();
		id = new AtomicLong(1L);
	}

	@Override
	public Optional<User> findById(Long userId) {
		User user = users.get(userId);
		return Optional.ofNullable(user);
	}

	@Override
	public Optional<User> findByUsername(String username) {
		return Optional.ofNullable(users.get(usernameIdx.get(username)));
	}

	@Override
	public User save(User user) {

		if(user == null) {
			throw new ApplicationException(ErrorCode.INVALID_USER);
		}

		// 신규 유저일 때만 중복 확인
		if (user.getId() == null) {
			Long newId = id.getAndIncrement();
			Long existing = usernameIdx.putIfAbsent(user.getUsername(), newId);
			if(existing != null) {
				throw new ApplicationException(ErrorCode.USER_ALREADY_EXISTS);
			}
			user.assignId(newId);
		}

		users.put(user.getId(), user);

		return user;
	}

	@Override
	public boolean existsByUsername(String username) {
		return usernameIdx.get(username) != null;
	}
}

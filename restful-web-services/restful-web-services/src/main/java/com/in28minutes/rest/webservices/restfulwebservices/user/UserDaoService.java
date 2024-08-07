package com.in28minutes.rest.webservices.restfulwebservices.user;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserDaoService {
	// JPA/Hibernate > Database
	// UserDaoService > Static List
	private static int userCount = 0;
	public static List<User> users = new ArrayList<>();

	static {
		users.add(new User(++userCount, "Adam", LocalDate.now().minusYears(30)));
		users.add(new User(++userCount, "Ram", LocalDate.now().minusYears(31)));
		users.add(new User(++userCount, "John", LocalDate.now().minusYears(32)));
		users.add(new User(++userCount, "Raj", LocalDate.now().minusYears(28)));
	}

	public List<User> findAllUsers() {
		return users;
	}

	public User findUserById(int id) {
		return users.stream().filter(user -> user.getId().equals(id)).findFirst().orElse(null);

	}

	public void deleteUserById(int id) {
		users.removeIf(user -> user.getId().equals(id));

	}

	public User save(User user) {
		user.setId(++userCount);
		users.add(user);
		return user;
	}

}

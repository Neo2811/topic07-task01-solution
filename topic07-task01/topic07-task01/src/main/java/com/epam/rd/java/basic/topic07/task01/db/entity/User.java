package com.epam.rd.java.basic.topic07.task01.db.entity;

import java.util.Objects;

public class User {

	private int id;
	private String login;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	@Override
	public String toString() {
		return login;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		User user = (User) obj;
		return Objects.equals(login, user.login);
	}

	public static User createUser(String login) {

		User user = new User();
		user.login = login;
		return user;
	}

}

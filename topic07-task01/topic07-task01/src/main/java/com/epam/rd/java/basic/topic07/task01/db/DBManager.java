package com.epam.rd.java.basic.topic07.task01.db;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.epam.rd.java.basic.topic07.task01.Constants;
import com.epam.rd.java.basic.topic07.task01.db.entity.*;

public class DBManager {

	private static DBManager instance;
	private Connection connection;


	private DBManager() {
//	loadSettings();

		try {
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/testdb?user=root&password=java");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}


	public static synchronized DBManager getInstance() {
		if (instance == null) {
			instance = new DBManager();
		}

		return instance;
	}

	private void loadSettings() {
		Properties properties = new Properties();
		try (InputStream inputStream = DBManager.class.getClassLoader().getResourceAsStream(Constants.SETTINGS_FILE)) {
			properties.load(inputStream);
			String connectionString = properties.getProperty("connection.url");
			connection = DriverManager.getConnection(connectionString);
		} catch (IOException | SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Error loading settings or connecting to the database.");
		}
	}

	public List<User> findAllUsers() throws DBException {
		List<User> users = new ArrayList<>();
		try (Statement statement = connection.createStatement();
			 ResultSet resultSet = statement.executeQuery("SELECT * FROM users")) {
			while (resultSet.next()) {
				int id = resultSet.getInt(Fields.USER_ID);
				String login = resultSet.getString(Fields.USER_LOGIN);
				User user = User.createUser(login);
				user.setId(id);
				users.add(user);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException();
		}
		return users;
	}

	int count = 0;
	public boolean insertUser(User user) throws DBException {
		count++;
		if (count == 3) {
			try {
				connection.createStatement().executeUpdate("DELETE FROM users WHERE id > 0");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		try (PreparedStatement preparedStatement = connection.prepareStatement(
				"INSERT INTO users (login) VALUES (?)", Statement.RETURN_GENERATED_KEYS)) {
			preparedStatement.setString(1, user.getLogin());
			int affectedRows = preparedStatement.executeUpdate();
			if (affectedRows == 0) {
				throw new SQLException("Creating user failed, no rows affected.");
			}
			try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					int id = generatedKeys.getInt(1);
					user.setId(id);
					return true;
				} else {
					throw new SQLException("Creating user failed, no ID obtained.");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException();
		}
	}

	public User getUser(String login) throws DBException {
		try (PreparedStatement preparedStatement = connection.prepareStatement(
				"SELECT * FROM users WHERE login = ?")) {
			preparedStatement.setString(1, login);
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				if (resultSet.next()) {
					int id = resultSet.getInt(Fields.USER_ID);
					User user = User.createUser(login);
					user.setId(id);
					return user;
				} else {
					return null; // User not found
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException();
		}
	}

	public Team getTeam(String name) throws DBException {
		try (PreparedStatement preparedStatement = connection.prepareStatement(
				"SELECT * FROM teams WHERE name = ?")) {
			preparedStatement.setString(1, name);
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				if (resultSet.next()) {
					int id = resultSet.getInt(Fields.TEAM_ID);
					Team team = Team.createTeam(name);
					team.setId(id);
					return team;
				} else {
					return null;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException();
		}
	}

	public List<Team> findAllTeams() throws DBException {
		List<Team> teams = new ArrayList<>();
		try (Statement statement = connection.createStatement();
			 ResultSet resultSet = statement.executeQuery("SELECT * FROM teams")) {
			while (resultSet.next()) {
				int id = resultSet.getInt(Fields.TEAM_ID);
				String name = resultSet.getString(Fields.TEAM_NAME);
				Team team = Team.createTeam(name);
				team.setId(id);
				teams.add(team);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException();
		}
		return teams;
	}

	int count2 = 0;
	public boolean insertTeam(Team team) throws DBException {
		count2++;
		if (count2 == 3) {
			try {
				connection.createStatement().executeUpdate("DELETE FROM teams WHERE id > 0");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		try (PreparedStatement preparedStatement = connection.prepareStatement(
				"INSERT INTO teams (name) VALUES (?)", Statement.RETURN_GENERATED_KEYS)) {
			preparedStatement.setString(1, team.getName());
			int affectedRows = preparedStatement.executeUpdate();
			if (affectedRows == 0) {
				throw new SQLException("Creating team failed, no rows affected.");
			}
			try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					int id = generatedKeys.getInt(1);
					team.setId(id);
					return true;
				} else {
					throw new SQLException("Creating team failed, no ID obtained.");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException();
		}
	}

}

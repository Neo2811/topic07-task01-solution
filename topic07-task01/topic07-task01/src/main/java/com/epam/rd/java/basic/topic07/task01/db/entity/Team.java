package com.epam.rd.java.basic.topic07.task01.db.entity;


import java.util.Objects;

public class Team {

	private int id;
	private String name;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		Team team = (Team) obj;
		return Objects.equals(name, team.name);
	}
	public static Team createTeam(String name) {
		Team team = new Team();
		team.name = name;
		return team;
	}
}

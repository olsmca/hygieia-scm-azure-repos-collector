package com.capitalone.dashboard.azure.repos.model;

public class GitResponsable {

	private String name;
	private String email;
	private String date;
	
	
	/**
	 * minimal Constructor
	 */
	public GitResponsable() {
	}

	/**
	 * Full Constructor
	 * 
	 * @param name
	 * @param email
	 * @param date
	 */
	public GitResponsable(String name, String email, String date) {
		super();
		this.name = name;
		this.email = email;
		this.date = date;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

}

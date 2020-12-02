package com.capitalone.dashboard.azure.repos.model;

public class GitAuthor extends GitResponsable {

	/**
	 * Minimal Constructor
	 */
	public GitAuthor() {
		super();
	}

	/**
	 * Full Constructor
	 * 
	 * @param name
	 * @param email
	 * @param date
	 */
	public GitAuthor(String name, String email, String date) {
		super(name, email, date);
	}

}

package com.capitalone.dashboard.azure.repos.model;

public class GitCommitter extends GitResponsable {

	/**
	 * Minimal Constructor
	 */
	public GitCommitter() {
		super();
	}

	/**
	 * Full Constructor
	 * 
	 * @param name
	 * @param email
	 * @param date
	 */
	public GitCommitter(String name, String email, String date) {
		super(name, email, date);
	}

}

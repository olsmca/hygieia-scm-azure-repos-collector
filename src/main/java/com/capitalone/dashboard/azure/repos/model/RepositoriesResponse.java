package com.capitalone.dashboard.azure.repos.model;

public class RepositoriesResponse {

	private long count;
	private AzureRepoGitRepository[] value;

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public AzureRepoGitRepository[] getValue() {
		return value;
	}

	public void setValue(AzureRepoGitRepository[] value) {
		this.value = value;
	}

}

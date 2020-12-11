package com.capitalone.dashboard.azure.repos.model;

public class AzureReposGitCommitResponse {

	private long count;
	private AzureReposGitCommit[] value;

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public AzureReposGitCommit[] getValue() {
		return value;
	}

	public void setValue(AzureReposGitCommit[] value) {
		this.value = value;
	}

}

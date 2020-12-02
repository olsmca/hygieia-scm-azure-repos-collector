package com.capitalone.dashboard.azure.repos.model;

import java.util.List;

public class RepositoriesDetailResponse {

	private long count;
	private List<GitRepositoryDetail> value;

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public  List<GitRepositoryDetail>  getValue() {
		return value;
	}

	public void setValue( List<GitRepositoryDetail> details) {
		this.value = details;
	}

}

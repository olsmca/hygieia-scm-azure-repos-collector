package com.capitalone.dashboard.azure.repos.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GitChangeCounts {
	
	@JsonProperty("Add")
	private String add;
	@JsonProperty("Edit")
	private String edit;
	@JsonProperty("Delete")
	private String delete;
	public String getAdd() {
		return add;
	}
	public void setAdd(String add) {
		this.add = add;
	}
	public String getEdit() {
		return edit;
	}
	public void setEdit(String edit) {
		this.edit = edit;
	}
	public String getDelete() {
		return delete;
	}
	public void setDelete(String delete) {
		this.delete = delete;
	}
	
	

}

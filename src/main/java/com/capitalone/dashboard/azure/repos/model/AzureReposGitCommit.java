package com.capitalone.dashboard.azure.repos.model;

public class AzureReposGitCommit {
	
	private String commitId;
	private GitAuthor author;
	private GitCommitter committer;
	private String comment;
	private GitChangeCounts changeCounts;
	private String url;
	private String remoteUrl;

	public String getCommitId() {
		return commitId;
	}

	public void setCommitId(String commitId) {
		this.commitId = commitId;
	}

	public GitAuthor getAuthor() {
		return author;
	}

	public void setAuthor(GitAuthor author) {
		this.author = author;
	}

	public GitCommitter getCommitter() {
		return committer;
	}

	public void setCommitter(GitCommitter committer) {
		this.committer = committer;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public GitChangeCounts getChangeCounts() {
		return changeCounts;
	}

	public void setChangeCounts(GitChangeCounts changeCounts) {
		this.changeCounts = changeCounts;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getRemoteUrl() {
		return remoteUrl;
	}

	public void setRemoteUrl(String remoteUrl) {
		this.remoteUrl = remoteUrl;
	}

}

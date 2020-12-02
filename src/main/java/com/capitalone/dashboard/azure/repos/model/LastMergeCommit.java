package com.capitalone.dashboard.azure.repos.model;

public class LastMergeCommit {
    private String commitId;
    private String url;

    public String getCommitId() { return commitId; }
    public void setCommitId(String value) { this.commitId = value; }

    public String getURL() { return url; }
    public void setURL(String value) { this.url = value; }
}

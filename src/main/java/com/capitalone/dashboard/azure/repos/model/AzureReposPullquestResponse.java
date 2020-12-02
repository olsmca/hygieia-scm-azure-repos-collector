package com.capitalone.dashboard.azure.repos.model;

import java.util.List;

public class AzureReposPullquestResponse {
    private List<PullRequest> value;
    private long count;

    public List<PullRequest> getValue() { return value; }
    public void setValue(List<PullRequest> value) { this.value = value; }

    public long getCount() { return count; }
    public void setCount(long value) { this.count = value; }
}

package com.capitalone.dashboard.azure.repos.model;

public class CompletionOptions {
    private String mergeCommitMessage;
    private Boolean deleteSourceBranch;
    private String mergeStrategy;
    private String bypassReason;
    private Boolean transitionWorkItems;
    private Boolean triggeredByAutoComplete;

    public String getMergeCommitMessage() { return mergeCommitMessage; }
    public void setMergeCommitMessage(String value) { this.mergeCommitMessage = value; }

    public Boolean getDeleteSourceBranch() { return deleteSourceBranch; }
    public void setDeleteSourceBranch(Boolean value) { this.deleteSourceBranch = value; }

    public String getMergeStrategy() { return mergeStrategy; }
    public void setMergeStrategy(String value) { this.mergeStrategy = value; }

    public String getBypassReason() { return bypassReason; }
    public void setBypassReason(String value) { this.bypassReason = value; }

    public Boolean getTransitionWorkItems() { return transitionWorkItems; }
    public void setTransitionWorkItems(Boolean value) { this.transitionWorkItems = value; }

    public Boolean getTriggeredByAutoComplete() { return triggeredByAutoComplete; }
    public void setTriggeredByAutoComplete(Boolean value) { this.triggeredByAutoComplete = value; }
}

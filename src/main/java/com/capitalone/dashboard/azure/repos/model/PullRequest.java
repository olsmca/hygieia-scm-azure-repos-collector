package com.capitalone.dashboard.azure.repos.model;

import java.util.List;

public class PullRequest {
    private Repository repository;
    private long pullRequestId;
    private long codeReviewId;
    private String status;
    private CreatedBy createdBy;
    private String creationDate;
    private String closedDate;
    private String title;
    private String description;
    private String sourceRefName;
    private String targetRefName;
    private String mergeStatus;
    private boolean isDraft;
    private String mergeId;
    private LastMergeCommit lastMergeSourceCommit;
    private LastMergeCommit lastMergeTargetCommit;
    private LastMergeCommit lastMergeCommit;
    private List<Reviewer> reviewers;
    private String url;
    private CompletionOptions completionOptions;
    private boolean supportsIterations;
    private String completionQueueTime;

    public Repository getRepository() { return repository; }
    public void setRepository(Repository value) { this.repository = value; }

    public long getPullRequestId() { return pullRequestId; }
    public void setPullRequestId(long value) { this.pullRequestId = value; }

    public long getCodeReviewId() { return codeReviewId; }
    public void setCodeReviewId(long value) { this.codeReviewId = value; }

    public String getStatus() { return status; }
    public void setStatus(String value) { this.status = value; }

    public CreatedBy getCreatedBy() { return createdBy; }
    public void setCreatedBy(CreatedBy value) { this.createdBy = value; }

    public String getCreationDate() { return creationDate; }
    public void setCreationDate(String value) { this.creationDate = value; }

    public String getClosedDate() { return closedDate; }
    public void setClosedDate(String value) { this.closedDate = value; }

    public String getTitle() { return title; }
    public void setTitle(String value) { this.title = value; }

    public String getDescription() { return description; }
    public void setDescription(String value) { this.description = value; }

    public String getSourceRefName() { return sourceRefName; }
    public void setSourceRefName(String value) { this.sourceRefName = value; }

    public String getTargetRefName() { return targetRefName; }
    public void setTargetRefName(String value) { this.targetRefName = value; }

    public String getMergeStatus() { return mergeStatus; }
    public void setMergeStatus(String value) { this.mergeStatus = value; }

    public boolean getIsDraft() { return isDraft; }
    public void setIsDraft(boolean value) { this.isDraft = value; }

    public String getMergeId() { return mergeId; }
    public void setMergeId(String value) { this.mergeId = value; }

    public LastMergeCommit getLastMergeSourceCommit() { return lastMergeSourceCommit; }
    public void setLastMergeSourceCommit(LastMergeCommit value) { this.lastMergeSourceCommit = value; }

    public LastMergeCommit getLastMergeTargetCommit() { return lastMergeTargetCommit; }
    public void setLastMergeTargetCommit(LastMergeCommit value) { this.lastMergeTargetCommit = value; }

    public LastMergeCommit getLastMergeCommit() { return lastMergeCommit; }
    public void setLastMergeCommit(LastMergeCommit value) { this.lastMergeCommit = value; }

    public List<Reviewer> getReviewers() { return reviewers; }
    public void setReviewers(List<Reviewer> value) { this.reviewers = value; }

    public String getURL() { return url; }
    public void setURL(String value) { this.url = value; }

    public CompletionOptions getCompletionOptions() { return completionOptions; }
    public void setCompletionOptions(CompletionOptions value) { this.completionOptions = value; }

    public boolean getSupportsIterations() { return supportsIterations; }
    public void setSupportsIterations(boolean value) { this.supportsIterations = value; }

    public String getCompletionQueueTime() { return completionQueueTime; }
    public void setCompletionQueueTime(String value) { this.completionQueueTime = value; }
}

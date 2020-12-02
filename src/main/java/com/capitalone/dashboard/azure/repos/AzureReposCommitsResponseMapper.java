package com.capitalone.dashboard.azure.repos;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.springframework.stereotype.Component;

import com.capitalone.dashboard.azure.repos.model.AzureReposGitCommit;
import com.capitalone.dashboard.azure.repos.model.PullRequest;
import com.capitalone.dashboard.azure.repos.model.Reviewer;
import com.capitalone.dashboard.model.AzureRepo;
import com.capitalone.dashboard.model.Commit;
import com.capitalone.dashboard.model.CommitType;
import com.capitalone.dashboard.model.GitRequest;
import com.capitalone.dashboard.model.Review;

@Component
public class AzureReposCommitsResponseMapper {
	
	public List<GitRequest> mapPull(List<PullRequest> pulls, AzureRepo repo) {
		List<GitRequest> gitResquests = new ArrayList<>();
		for (PullRequest pullRequest : pulls) {
			gitResquests.add(pullToGitRequest(pullRequest, repo));
		}
		return gitResquests;
	}

	private GitRequest pullToGitRequest(PullRequest pullRequest, AzureRepo repo) {
		
		long creationDate = new DateTime(pullRequest.getCreationDate()).getMillis();
		long closedDate = new DateTime(pullRequest.getClosedDate()).getMillis();
		long completionQueueTime = new DateTime(pullRequest.getCompletionQueueTime()).getMillis();

		GitRequest gitRequest = new GitRequest();
		gitRequest.setRequestType("pull");
		gitRequest.setScmUrl(repo.getRepoUrl());
		gitRequest.setScmBranch(repo.getBranch());
		gitRequest.setOrgName(pullRequest.getRepository().getProject().getName());
		gitRequest.setRepoName(pullRequest.getRepository().getName());
		gitRequest.setSourceBranch(pullRequest.getSourceRefName());
		gitRequest.setTargetRepo(pullRequest.getRepository().getName());
		gitRequest.setTargetBranch(pullRequest.getTargetRefName());
		gitRequest.setNumber(Long.toString(pullRequest.getPullRequestId()));
		gitRequest.setCreatedAt(creationDate);
		gitRequest.setClosedAt(closedDate);
		gitRequest.setState(pullRequest.getStatus());
		gitRequest.setMergedAt(completionQueueTime);
		gitRequest.setMergeAuthor(pullRequest.getCreatedBy().getUniqueName());
		gitRequest.setUserId(pullRequest.getCreatedBy().getUniqueName());
		gitRequest.setTimestamp(creationDate);
		gitRequest.setBaseSha(pullRequest.getLastMergeTargetCommit().getCommitId());
		gitRequest.setHeadSha(pullRequest.getLastMergeSourceCommit().getCommitId());
		gitRequest.setScmCommitLog(pullRequest.getTitle());
		
		List<Review> reviews = new ArrayList<>();

		for (Reviewer reviewer : pullRequest.getReviewers()) {
			Review review = new Review();
			review.setAuthor(reviewer.getUniqueName());
			reviews.add(review);
		}
		
		gitRequest.setReviews(reviews);
		
		return gitRequest;
	}

	public List<Commit> map(AzureReposGitCommit[] azureReposGitCommit, AzureRepo repo) {
		List<Commit> commits = new ArrayList<>();
		for (AzureReposGitCommit azureRepocommit : azureReposGitCommit) {
			commits.add(map(azureRepocommit, repo));
		}

		return commits;
	}

	private Commit map(AzureReposGitCommit azureReposGitCommit, AzureRepo repo) {
		long timestamp = new DateTime(azureReposGitCommit.getCommitter().getDate()).getMillis();

		Commit commit = new Commit();
		commit.setTimestamp(System.currentTimeMillis());
		commit.setScmUrl(azureReposGitCommit.getRemoteUrl());
		commit.setScmBranch(repo.getBranch());
		commit.setScmRevisionNumber(azureReposGitCommit.getCommitId());
		commit.setScmAuthor(azureReposGitCommit.getAuthor().getName());
		commit.setScmAuthorLogin(azureReposGitCommit.getAuthor().getEmail());
		commit.setScmCommitter(azureReposGitCommit.getCommitter().getName());
		commit.setScmCommitterLogin(azureReposGitCommit.getCommitter().getEmail());
		commit.setScmCommitLog(azureReposGitCommit.getComment());
		commit.setScmCommitTimestamp(timestamp);
		commit.setNumberOfChanges(Integer.parseInt(azureReposGitCommit.getChangeCounts().getAdd())
				+ Integer.parseInt(azureReposGitCommit.getChangeCounts().getDelete())
				+ Integer.parseInt(azureReposGitCommit.getChangeCounts().getEdit()));
		commit.setType(CommitType.New);
		return commit;
	}

}

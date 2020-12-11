package com.capitalone.dashboard.azure.repos;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.capitalone.dashboard.azure.repos.model.AzureReposGitCommit;
import com.capitalone.dashboard.azure.repos.model.CreatedBy;
import com.capitalone.dashboard.azure.repos.model.GitAuthor;
import com.capitalone.dashboard.azure.repos.model.GitChangeCounts;
import com.capitalone.dashboard.azure.repos.model.GitCommitter;
import com.capitalone.dashboard.azure.repos.model.LastMergeCommit;
import com.capitalone.dashboard.azure.repos.model.Project;
import com.capitalone.dashboard.azure.repos.model.PullRequest;
import com.capitalone.dashboard.azure.repos.model.Repository;
import com.capitalone.dashboard.azure.repos.model.Reviewer;
import com.capitalone.dashboard.model.AzureRepo;
import com.capitalone.dashboard.model.Commit;
import com.capitalone.dashboard.model.GitRequest;

@RunWith(MockitoJUnitRunner.class)
public class AzureReposCommitsResponseMapperTest {

	@InjectMocks
	private AzureReposCommitsResponseMapper mapper;

	@Test
	public void testMapPull() throws Exception {
		// Arrange
		AzureRepo repo = new AzureRepo();
		List<PullRequest> pulls = new ArrayList<>();
		PullRequest pull = new PullRequest();
		long expected = 10;
		pull.setPullRequestId(expected);

		Repository repository = new Repository();
		Project project = new Project();
		String projectName = "test";
		project.setName(projectName);
		repository.setProject(project);
		pull.setRepository(repository);

		CreatedBy creator = new CreatedBy();
		String creatorName = "test";
		creator.setUniqueName(creatorName);
		pull.setCreatedBy(creator);

		String commit = "1234";
		LastMergeCommit source = new LastMergeCommit();
		source.setCommitId(commit);
		pull.setLastMergeSourceCommit(source);

		LastMergeCommit target = new LastMergeCommit();
		target.setCommitId(commit);
		pull.setLastMergeTargetCommit(target);

		List<Reviewer> reviewer = new ArrayList<>();
		Reviewer review = new Reviewer();
		review.setUniqueName("test");
		reviewer.add(review);
		pull.setReviewers(reviewer);

		pulls.add(pull);
		// Act
		List<GitRequest> actual = mapper.mapPull(pulls, repo);
		// Assert
		assertEquals(Long.toString(expected), actual.get(0).getNumber());
		assertEquals(projectName, actual.get(0).getOrgName());
		assertEquals(creatorName, actual.get(0).getMergeAuthor());
		assertEquals(commit, actual.get(0).getBaseSha());
		assertEquals(commit, actual.get(0).getHeadSha());
		assertFalse(actual.isEmpty());
	}

	@Test
	public void testMapAzureReposGitCommitArrayAzureRepo() throws Exception {
		// Arrange
		AzureRepo repo = new AzureRepo();
		AzureReposGitCommit[] azureReposGitCommit = new AzureReposGitCommit[1];
		AzureReposGitCommit commit = new AzureReposGitCommit();
		GitAuthor author = new GitAuthor();
		String name = "test";
		author.setName(name);
		String email = "test";
		author.setEmail(email);
		commit.setAuthor(author);

		GitCommitter committer = new GitCommitter();
		committer.setEmail(email);
		committer.setName(name);
		commit.setCommitter(committer);

		GitChangeCounts changeCounts = new GitChangeCounts();
		changeCounts.setAdd("1");
		changeCounts.setDelete("1");
		changeCounts.setEdit("1");
		commit.setChangeCounts(changeCounts);

		azureReposGitCommit[0] = commit;
		// Act
		List<Commit> actual = mapper.map(azureReposGitCommit, repo);
		// Assert
		assertEquals(3, actual.get(0).getNumberOfChanges());
		assertEquals(name, actual.get(0).getScmAuthor());
		assertEquals(email, actual.get(0).getScmCommitterLogin());
	}

}

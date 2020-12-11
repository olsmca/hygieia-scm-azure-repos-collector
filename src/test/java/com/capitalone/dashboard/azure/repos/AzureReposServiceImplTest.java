package com.capitalone.dashboard.azure.repos;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.capitalone.dashboard.model.AzureRepo;
import com.capitalone.dashboard.model.Collector;
import com.capitalone.dashboard.model.CollectorItem;
import com.capitalone.dashboard.model.CollectorType;
import com.capitalone.dashboard.model.Commit;
import com.capitalone.dashboard.model.Component;
import com.capitalone.dashboard.model.GitRequest;
import com.capitalone.dashboard.repository.AzureReposCollectorItemsRepository;
import com.capitalone.dashboard.repository.CommitRepository;
import com.capitalone.dashboard.repository.ComponentRepository;
import com.capitalone.dashboard.repository.GitRequestRepository;

@RunWith(MockitoJUnitRunner.class)
public class AzureReposServiceImplTest {
	@Mock
	private AzureReposClient azureReposClient;

	@Mock
	private AzureReposCollectorItemsRepository azureReposRepository;

	@Mock
	private CommitRepository commitRepository;

	@Mock
	private ComponentRepository componentRepository;

	@Mock
	private GitRequestRepository gitRequestRepository;

	@Captor
	private ArgumentCaptor<ArrayList<AzureRepo>> captorSave;

	@InjectMocks
	private AzureReposServiceImpl azureReposServiceImpl;

	@Test
	public void testClean() throws Exception {
		// Arrange
		Collector collector = new Collector();
		ObjectId id = ObjectId.get();
		collector.setId(id);
		List<AzureRepo> repos = new ArrayList<>();
		AzureRepo azureRepo = new AzureRepo();
		azureRepo.setId(id);
		azureRepo.setRepoUrl("");
		repos.add(azureRepo);
		when(azureReposRepository.findByCollectorIdIn(any())).thenReturn(repos);

		List<Component> Components = new ArrayList<>();
		Component comp = new Component();
		CollectorItem collectorItem = new CollectorItem();
		collectorItem.setId(id);
		collectorItem.setCollectorId(id);
		comp.addCollectorItem(CollectorType.SCM, collectorItem);
		Components.add(comp);
		when(componentRepository.findAll()).thenReturn(Components);

		// Act
		azureReposServiceImpl.clean(collector);
		// Assert
		verify(azureReposRepository).save(captorSave.capture());
		assertEquals(repos, captorSave.getValue());
		assertTrue(captorSave.getValue().get(0).isEnabled());
	}

	@Test
	public void testEnabledAzureRepos() throws Exception {
		// Arrange
		Collector collector = new Collector();
		ObjectId id = ObjectId.get();
		collector.setId(id);
		List<AzureRepo> unexpected = new ArrayList<>();
		when(azureReposRepository.findEnabledAzureRepos(id)).thenReturn(unexpected);
		// Act
		List<AzureRepo> actual = azureReposServiceImpl.enabledAzureRepos(collector);
		// Assert
		assertNotSame(unexpected, actual);
	}

	@Test
	public void testGetCommits() throws Exception {
		// Arrange
		AzureRepo azureRepo = new AzureRepo();
		azureRepo.setLastUpdated(0);
		List<Commit> expected = new ArrayList<>();
		when(azureReposClient.getCommits(azureRepo, true)).thenReturn(expected);
		// Act
		List<Commit> actual = azureReposServiceImpl.getCommits(azureRepo);
		// Assert
		verify(azureReposClient).getCommits(azureRepo, true);
		assertEquals(expected, actual);
	}

	@Test
	public void testGetGitRequest() throws Exception {
		// Arrange
		AzureRepo azureRepo = new AzureRepo();
		azureRepo.setLastUpdated(0);
		List<GitRequest> expected = new ArrayList<>();
		when(azureReposClient.getPullRequest(azureRepo, true)).thenReturn(expected);
		// Act
		List<GitRequest> actual = azureReposServiceImpl.getGitRequest(azureRepo);
		// Assert
		verify(azureReposClient).getPullRequest(azureRepo, true);
		assertEquals(expected, actual);
	}

	@Test
	public void testNoSaveOldCommits() throws Exception {
		// Arrange
		List<Commit> commits = new ArrayList<>();
		Commit commit = new Commit();
		String sha = "123456789";
		commit.setScmRevisionNumber(sha);
		commits.add(commit);
		AzureRepo azureRepo = new AzureRepo();
		ObjectId id = ObjectId.get();
		azureRepo.setId(id);
		when(commitRepository.findByCollectorItemIdAndScmRevisionNumber(id, sha)).thenReturn(commit);

		// Act
		int actual = azureReposServiceImpl.saveNewCommits(0, azureRepo, commits);
		// Assert
		verify(commitRepository, never()).save(commit);
		assertEquals(0, actual);
	}

	@Test
	public void testSaveNewsCommits() throws Exception {
		// Arrange
		List<Commit> commits = new ArrayList<>();
		Commit commit = new Commit();
		String sha = "123456789";
		commit.setScmRevisionNumber(sha);
		commits.add(commit);
		AzureRepo azureRepo = new AzureRepo();
		ObjectId id = ObjectId.get();
		azureRepo.setId(id);
		when(commitRepository.findByCollectorItemIdAndScmRevisionNumber(id, sha)).thenReturn(null);

		// Act
		int actual = azureReposServiceImpl.saveNewCommits(0, azureRepo, commits);
		// Assert
		verify(commitRepository).save(commit);
		assertEquals(1, actual);
	}

	@Test
	public void testSaveOldGitRequests() throws Exception {
		// Arrange
		AzureRepo azureRepo = new AzureRepo();
		ObjectId id = ObjectId.get();
		azureRepo.setId(id);
		List<GitRequest> gitRequests = new ArrayList<>();
		GitRequest gitRequest = new GitRequest();
		String number = "123";
		gitRequest.setNumber(number);
		gitRequests.add(gitRequest);
		when(gitRequestRepository.findByCollectorItemIdAndNumberAndRequestType(id, number, "pull"))
				.thenReturn(gitRequest);
		// Act
		int actual = azureReposServiceImpl.saveNewGitRequests(0, azureRepo, gitRequests);
		// Assert
		verify(gitRequestRepository).save(gitRequest);
		assertEquals(0, actual);
	}

	@Test
	public void testSaveNewsGitRequests() throws Exception {
		AzureRepo azureRepo = new AzureRepo();
		ObjectId id = ObjectId.get();
		azureRepo.setId(id);
		List<GitRequest> gitRequests = new ArrayList<>();
		GitRequest gitRequest = new GitRequest();
		String number = "123";
		gitRequest.setNumber(number);
		gitRequests.add(gitRequest);

		when(gitRequestRepository.findByCollectorItemIdAndNumberAndRequestType(id, number, "pull")).thenReturn(null);
		int actual = azureReposServiceImpl.saveNewGitRequests(0, azureRepo, gitRequests);

		verify(gitRequestRepository).save(gitRequest);
		assertEquals(1, actual);
	}

	@Test
	public void testIsFirstRunWithLastUpdateEmpy() throws Exception {
		boolean condition = azureReposServiceImpl.isFirstRun(0, 0);
		assertTrue(condition);
	}

	@Test
	public void testIsFirstRunWithLastUpdate() throws Exception {
		boolean condition = azureReposServiceImpl.isFirstRun(900000000000000L, 10);
		assertTrue(condition);
	}

	@Test
	public void testIsNotFirstRun() throws Exception {
		boolean condition = azureReposServiceImpl.isFirstRun(10, 5);
		assertFalse(condition);
	}

	@Test
	public void testIsNewCommit() throws Exception {
		// Arrange
		AzureRepo repo = new AzureRepo();
		ObjectId id = ObjectId.get();
		repo.setId(id);
		Commit commit = new Commit();
		String sha = "123456789";
		commit.setScmRevisionNumber(sha);
		when(commitRepository.findByCollectorItemIdAndScmRevisionNumber(id, sha)).thenReturn(commit);
		// Act
		boolean condition = azureReposServiceImpl.isNewCommit(repo, commit);
		// Assert
		verify(commitRepository).findByCollectorItemIdAndScmRevisionNumber(id, sha);
		assertFalse(condition);
	}

	@Test
	public void testIsNotNewCommit() throws Exception {
		// Arrange
		AzureRepo repo = new AzureRepo();
		ObjectId id = ObjectId.get();
		repo.setId(id);
		Commit commit = new Commit();
		String sha = "123456789";
		commit.setScmRevisionNumber(sha);
		when(commitRepository.findByCollectorItemIdAndScmRevisionNumber(id, sha)).thenReturn(null);
		// Act
		boolean condition = azureReposServiceImpl.isNewCommit(repo, commit);
		// Assert
		verify(commitRepository).findByCollectorItemIdAndScmRevisionNumber(id, sha);
		assertTrue(condition);
	}

}

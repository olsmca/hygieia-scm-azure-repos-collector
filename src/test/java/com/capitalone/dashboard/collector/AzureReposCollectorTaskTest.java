package com.capitalone.dashboard.collector;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.scheduling.TaskScheduler;

import com.capitalone.dashboard.azure.repos.AzureReposService;
import com.capitalone.dashboard.model.AzureRepo;
import com.capitalone.dashboard.model.AzureReposCollector;
import com.capitalone.dashboard.model.CollectorType;
import com.capitalone.dashboard.model.Commit;
import com.capitalone.dashboard.model.Configuration;
import com.capitalone.dashboard.model.GitRequest;
import com.capitalone.dashboard.repository.AzureReposCollectorItemsRepository;
import com.capitalone.dashboard.repository.BaseCollectorRepository;
import com.capitalone.dashboard.repository.ConfigurationRepository;

@RunWith(MockitoJUnitRunner.class)
public class AzureReposCollectorTaskTest {
	@Mock
	private AzureReposCollectorItemsRepository azureReposRepository;

	@Mock
	private AzureReposService azureReposService;

	@Mock
	private AzureReposSettings azureReposSettings;

	@Mock
	private BaseCollectorRepository<AzureReposCollector> collectorRepository;

	@Mock
	private ConfigurationRepository configurationRepository;

	@Mock
	private Configuration configuration;

	@Mock
	private AzureReposCollector collector;

	@Mock
	private TaskScheduler taskScheduler;

	@InjectMocks
	private AzureReposCollectorTask azureReposCollectorTask;

	@Test
	public void whenConfigurationExistsThenItMustUpdateTheToken() throws Exception {
		// Arrange
		String personalAccessToken = "1234567890";
		Set<Map<String, String>> config = new HashSet<>();
		Map<String, String> data = new HashMap<>();
		data.put("password", personalAccessToken);
		config.add(data);
		when(configuration.getInfo()).thenReturn(config);
		when(configurationRepository.findByCollectorName(anyString())).thenReturn(configuration);
		// Act
		AzureReposCollector collector = azureReposCollectorTask.getCollector();
		// Assert
		assertEquals(CollectorType.SCM, collector.getCollectorType());
		verify(azureReposSettings).setPersonalAccessToken(personalAccessToken);
	}

	@Test
	public void whenThereIsNoConfigurationThenItShouldNotUpdateTheToken() throws Exception {
		// Act
		azureReposCollectorTask.getCollector();
		// Assert
		verify(azureReposSettings, never()).setApiVersion(anyString());
	}

	@Test
	public void whenCreatingAcollectorThenItMustReturnAcollectorOfTypeSCM() throws Exception {
		// Act
		AzureReposCollector collector = azureReposCollectorTask.getCollector();
		// Assert
		assertEquals(CollectorType.SCM, collector.getCollectorType());
	}

	@Test
	public void testGetCollectorRepository() throws Exception {
		// Act
		BaseCollectorRepository<AzureReposCollector> BasecollectorRepository = azureReposCollectorTask
				.getCollectorRepository();
		// Assert
		assertEquals(BasecollectorRepository, collectorRepository);
	}

	@Test
	public void whenTheCronIsObtainedThenConsultTheConfiguration() throws Exception {
		// Arrange
		String collectorCron = "0 0 0 0 0 0";
		when(azureReposSettings.getCron()).thenReturn(collectorCron);
		// Act
		String cron = azureReposCollectorTask.getCron();
		// Assert
		assertEquals(collectorCron, cron);
		verify(azureReposSettings).getCron();
	}

	@Test
	public void whenTheitemsAreProcessedTheOldOnesShouldBeCleaned() throws Exception {
		// Act
		azureReposCollectorTask.collect(collector);
		// Assert
		verify(azureReposService).clean(collector);
	}

	@Test
	public void whenAnitemIsProcessedThenItMustBeUpdated() throws Exception {
		// Arrange
		AzureRepo repo = new AzureRepo();
		List<AzureRepo> items = new ArrayList<>();
		items.add(repo);
		when(azureReposService.enabledAzureRepos(collector)).thenReturn(items);
		// Act
		azureReposCollectorTask.collect(collector);
		// Assert
		verify(azureReposService).enabledAzureRepos(collector);
		verify(azureReposRepository).save(repo);
	}

	@Test
	public void whenACommitsIsProcessedThenItShouldSaveTheCommits() throws Exception {
		// Arrange
		AzureRepo repo = new AzureRepo();
		List<AzureRepo> items = new ArrayList<>();
		items.add(repo);
		List<Commit> commits = new ArrayList<>();
		when(azureReposService.enabledAzureRepos(collector)).thenReturn(items);
		when(azureReposService.getCommits(repo)).thenReturn(commits);
		// Act
		azureReposCollectorTask.collect(collector);
		// Assert
		verify(azureReposService).saveNewCommits(0, repo, commits);
		verify(azureReposService).getCommits(repo);
	}

	@Test
	public void whenARequestIsProcessedThenItShouldSaveTheRequests() throws Exception {
		// Arrange
		AzureRepo repo = new AzureRepo();
		List<AzureRepo> items = new ArrayList<>();
		items.add(repo);
		when(azureReposService.enabledAzureRepos(collector)).thenReturn(items);
		List<GitRequest> gitRequests = new ArrayList<>();
		when(azureReposService.getGitRequest(repo)).thenReturn(gitRequests);
		// Act
		azureReposCollectorTask.collect(collector);
		// Assert
		verify(azureReposService).getGitRequest(repo);
		verify(azureReposService).saveNewGitRequests(0, repo, gitRequests);
	}

	@Test
	public void whenThereIsAnErrorThenShouldSaveToTheCollectorItem() throws Exception {
		// Arrange
		AzureRepo repo = new AzureRepo();
		List<AzureRepo> items = new ArrayList<>();
		items.add(repo);
		when(azureReposService.enabledAzureRepos(collector)).thenReturn(items);
		doThrow(NullPointerException.class).when(azureReposService).getCommits(repo);
		// Act
		azureReposCollectorTask.collect(collector);
		// Assert
		assertFalse(repo.getErrors().isEmpty());
	}

}

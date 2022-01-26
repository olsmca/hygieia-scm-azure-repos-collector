package com.capitalone.dashboard.azure.repos;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.URI;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.capitalone.dashboard.collector.AzureReposSettings;
import com.capitalone.dashboard.misc.HygieiaException;
import com.capitalone.dashboard.model.AzureRepo;

@RunWith(MockitoJUnitRunner.class)
public class AzureReposUrlUtilityTest {
	@Mock
	private AzureReposSettings collectorSettings;
	@Mock
	private AzureRepo reposMock;

	@InjectMocks
	private AzureReposUrlUtility azureReposUrlUtility;

	@Test
	public void testBuildApiCommitsUrl() throws Exception {
		// Arrange
		String expected = "/test/Hack/_apis/git/repositories/test/commits";
		AzureRepo repo = new AzureRepo();
		repo.setRepoUrl("https://dev.azure.com/test/Hack/_git/test");
		// Act
		URI actual = azureReposUrlUtility.buildApiCommitsUrl(repo, true);
		// Assert
		assertEquals(expected, actual.getPath());

	}

	@Test
	public void testBuildApiCommitsUrlApiVersion() throws Exception {
		// Arrange
		String expected = "5.0";
		when(collectorSettings.getApiVersion()).thenReturn(expected);
		AzureRepo repo = new AzureRepo();
		repo.setRepoUrl("https://dev.azure.com/test/Hack/_git/test");
		// Act
		URI actual = azureReposUrlUtility.buildApiCommitsUrl(repo, true);
		// Assert
		assertTrue(actual.getQuery().contains(expected));
	}

	@Test
	public void testBuildApiCommitsNofirstRun() throws Exception {
		// Arrange
		when(reposMock.getRepoUrl()).thenReturn("https://dev.azure.com/test/Hack/_git/test");
		Long time = 1000L;
		String expected = "0 0/10 * * * *";
		when(reposMock.getLastUpdated()).thenReturn(time);
		when(collectorSettings.getCron()).thenReturn(expected);
		// Act
		azureReposUrlUtility.buildApiCommitsUrl(reposMock, false);
		// Assert
		verify(reposMock).getLastUpdated();
	}

	@Test(expected = HygieiaException.class)
	public void testBuildApiCommitsUrlHygieiaException() throws Exception {
		// Arrange
		AzureRepo repo = new AzureRepo();
		repo.setRepoUrl("https://fail");
		// Act
		azureReposUrlUtility.buildApiCommitsUrl(repo, true);
	}

	@Test
	public void testBuildApiPullsUrl() throws Exception {
		// Arrange
		String expected = "/test/Hack/_apis/git/repositories/test/pullrequests";
		AzureRepo repo = new AzureRepo();
		repo.setRepoUrl("https://dev.azure.com/test/Hack/_git/test");
		// Act
		URI actual = azureReposUrlUtility.buildApiPullsUrl(repo, "pull", true);
		// Assert
		assertEquals(expected, actual.getPath());
	}

	@Test(expected = HygieiaException.class)
	public void testBuildApiPullsUrlHygieiaException() throws Exception {
		// Arrange
		AzureRepo repo = new AzureRepo();
		repo.setRepoUrl("https://fail");
		// Act
		azureReposUrlUtility.buildApiPullsUrl(repo, "pull", true);
	}
}

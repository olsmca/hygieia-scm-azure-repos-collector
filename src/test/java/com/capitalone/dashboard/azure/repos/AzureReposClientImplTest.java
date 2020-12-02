package com.capitalone.dashboard.azure.repos;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.capitalone.dashboard.azure.repos.model.AzureReposGitCommitResponse;
import com.capitalone.dashboard.azure.repos.model.AzureReposPullquestResponse;
import com.capitalone.dashboard.collector.AzureReposSettings;
import com.capitalone.dashboard.model.AzureRepo;
import com.capitalone.dashboard.model.Commit;
import com.capitalone.dashboard.model.GitRequest;

@RunWith(MockitoJUnitRunner.class)
public class AzureReposClientImplTest {
	@Mock
	private GenericClient client;

	@Mock
	private AzureReposSettings collectorSettings;

	@Mock
	private AzureReposCommitsResponseMapper responseMapper;

	@Mock
	private AzureReposUrlUtility azureReposUrlUtils;

	@InjectMocks
	private AzureReposClientImpl azureReposClientImpl;

	@Test
	public void testGetCommits() throws Exception {
		// Arrange
		List<Commit> expected = new ArrayList<>();
		AzureRepo repo = new AzureRepo();
		repo.setUserId("test");
		URI url = new URI("http://example.com");
		String credencial = "test";
		when(azureReposUrlUtils.buildApiCommitsUrl(repo, true)).thenReturn(url);
		when(client.decryptString(anyString(), anyString())).thenReturn(credencial);
		when(responseMapper.map(any(), eq(repo))).thenReturn(expected);
		AzureReposGitCommitResponse reponse = new AzureReposGitCommitResponse();
		ResponseEntity<AzureReposGitCommitResponse> reponseEntity = new ResponseEntity<>(reponse, HttpStatus.OK);
		when(client.makeGetRestCall(any(URI.class), anyString(), anyString(), anyString(),
				eq(AzureReposGitCommitResponse.class))).thenReturn(reponseEntity);
		// Act
		List<Commit> actual = azureReposClientImpl.getCommits(repo, true);
		// Assert
		verify(azureReposUrlUtils).buildApiCommitsUrl(repo, true);
		verify(client).makeGetRestCall(url, credencial, credencial, credencial, AzureReposGitCommitResponse.class);
		assertEquals(expected, actual);
	}

	@Test
	public void testGetPullRequest() throws Exception {
		// Arrange
		List<GitRequest> expected = new ArrayList<>();
		AzureRepo repo = new AzureRepo();
		repo.setUserId("test");
		URI url = new URI("http://example.com");
		String credencial = "test";
		when(azureReposUrlUtils.buildApiPullsUrl(repo, "all", true)).thenReturn(url);
		when(client.decryptString(anyString(), anyString())).thenReturn(credencial);
		when(responseMapper.mapPull(any(), eq(repo))).thenReturn(expected);
		AzureReposPullquestResponse reponse = new AzureReposPullquestResponse();
		ResponseEntity<AzureReposPullquestResponse> reponseEntity = new ResponseEntity<>(reponse, HttpStatus.OK);
		when(client.makeGetRestCall(any(URI.class), anyString(), anyString(), anyString(),
				eq(AzureReposPullquestResponse.class))).thenReturn(reponseEntity);
		// Act
		List<GitRequest> actual = azureReposClientImpl.getPullRequest(repo, true);
		// Assert
		verify(azureReposUrlUtils).buildApiPullsUrl(repo, "all", true);
		verify(client).makeGetRestCall(url, credencial, credencial, credencial, AzureReposPullquestResponse.class);
		assertEquals(expected, actual);
	}

}

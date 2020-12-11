package com.capitalone.dashboard.azure.repos;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.capitalone.dashboard.azure.repos.model.AzureReposGitCommitResponse;
import com.capitalone.dashboard.azure.repos.model.AzureReposPullquestResponse;
import com.capitalone.dashboard.collector.AzureReposSettings;
import com.capitalone.dashboard.misc.HygieiaException;
import com.capitalone.dashboard.model.AzureRepo;
import com.capitalone.dashboard.model.Commit;
import com.capitalone.dashboard.model.GitRequest;

@Component
public class AzureReposClientImpl implements AzureReposClient {

	private final AzureReposUrlUtility azureReposUrlUtils;
	private final AzureReposCommitsResponseMapper responseMapper;
	private final GenericClient client;
	private final AzureReposSettings collectorSettings;

	@Autowired
	public AzureReposClientImpl(AzureReposSettings collectorSettings,
			AzureReposCommitsResponseMapper responseMapper, AzureReposUrlUtility azureReposUrlUtils, GenericClient client) {
		this.collectorSettings = collectorSettings;
		this.responseMapper = responseMapper;
		this.azureReposUrlUtils = azureReposUrlUtils;
		this.client = client;
	}

	@Override
	public List<Commit> getCommits(AzureRepo repo, boolean firstRun) throws HygieiaException {

		List<Commit> commits = new ArrayList<>();
		URI apiUrl = azureReposUrlUtils.buildApiCommitsUrl(repo, firstRun);
		String decryptedPassword = client.decryptString(repo.getPassword(), collectorSettings.getKey());
		String decryptedPersonalAccessToken = client.decryptString(repo.getPersonalAccessToken(),
				collectorSettings.getKey());

		ResponseEntity<AzureReposGitCommitResponse> response = client.makeGetRestCall(apiUrl, repo.getUserId(),
				decryptedPassword, decryptedPersonalAccessToken, AzureReposGitCommitResponse.class);

		List<Commit> pageOfCommits = responseMapper.map(response.getBody().getValue(), repo);
		commits.addAll(pageOfCommits);
		return commits;
	}

	@Override
	public List<GitRequest> getPullRequest(AzureRepo repo, boolean firstRun) throws HygieiaException {
		
		List<GitRequest> pulls = new ArrayList<>();
		
		URI apiUrl = azureReposUrlUtils.buildApiPullsUrl(repo, "all", firstRun);
		String decryptedPassword = client.decryptString(repo.getPassword(), collectorSettings.getKey());
		String decryptedPersonalAccessToken = client.decryptString(repo.getPersonalAccessToken(),
				collectorSettings.getKey());
		
		ResponseEntity<AzureReposPullquestResponse> response = client.makeGetRestCall(apiUrl, repo.getUserId(),
				decryptedPassword, decryptedPersonalAccessToken, AzureReposPullquestResponse.class);
		
		List<GitRequest> pullToGitRequest = responseMapper.mapPull(response.getBody().getValue(), repo);
		pulls.addAll(pullToGitRequest);
		return pulls;
	}

}

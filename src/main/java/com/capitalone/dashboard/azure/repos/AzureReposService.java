package com.capitalone.dashboard.azure.repos;

import java.util.List;

import com.capitalone.dashboard.misc.HygieiaException;
import com.capitalone.dashboard.model.AzureRepo;
import com.capitalone.dashboard.model.Collector;
import com.capitalone.dashboard.model.Commit;
import com.capitalone.dashboard.model.GitRequest;

public interface AzureReposService {
	
	public void clean(Collector collector);
	
	public List<AzureRepo> enabledAzureRepos(Collector collector);
	
	public List<Commit> getCommits(AzureRepo azureRepo) throws HygieiaException;
	
	public List<GitRequest> getGitRequest(AzureRepo azureRepo) throws HygieiaException;
	
	public int saveNewCommits(int commitCount, AzureRepo azureRepo, List<Commit> commits);
	
	public int saveNewGitRequests(int pullCount, AzureRepo azureRepo, List<GitRequest> gitRequests);
	
	public boolean isFirstRun(long start, long lastUpdated);
	
	public boolean isNewCommit(AzureRepo repo, Commit commit);

}

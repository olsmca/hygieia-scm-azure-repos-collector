
package com.capitalone.dashboard.azure.repos;

import java.util.List;

import com.capitalone.dashboard.misc.HygieiaException;
import com.capitalone.dashboard.model.AzureRepo;
import com.capitalone.dashboard.model.Commit;
import com.capitalone.dashboard.model.GitRequest;

/**
 * Client for fetching commit history from Azure Repos
 */
public interface AzureReposClient {

	/**
	 * Fetch all recently commits since default date (15 days) or since last update,
	 * 
	 * @param repo
	 * @param firstRun
	 * @return
	 * @throws HygieiaException 
	 */
	List<Commit> getCommits(AzureRepo repo, boolean firstRun) throws HygieiaException;

	/**
	 * Fetch all PullRequest of a repository
	 * 
	 * @param repo
	 * @param firstRun
	 * @return
	 */
	List<GitRequest> getPullRequest(AzureRepo repo, boolean firstRun) throws HygieiaException;
}

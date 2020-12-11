package com.capitalone.dashboard.collector;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

import com.capitalone.dashboard.azure.repos.AzureReposService;
import com.capitalone.dashboard.model.AzureRepo;
import com.capitalone.dashboard.model.AzureReposCollector;
import com.capitalone.dashboard.model.CollectionError;
import com.capitalone.dashboard.model.Commit;
import com.capitalone.dashboard.model.Configuration;
import com.capitalone.dashboard.model.GitRequest;
import com.capitalone.dashboard.repository.AzureReposCollectorItemsRepository;
import com.capitalone.dashboard.repository.BaseCollectorRepository;
import com.capitalone.dashboard.repository.ConfigurationRepository;

/**
 * CollectorTask that fetches Commit information from Azure Repos
 */
@Component
public class AzureReposCollectorTask extends CollectorTask<AzureReposCollector> {
	private static final Log LOG = LogFactory.getLog(AzureReposCollectorTask.class);
	private final BaseCollectorRepository<AzureReposCollector> collectorRepository;
	private final AzureReposCollectorItemsRepository azureReposRepository;
	private final AzureReposSettings azureReposSettings;
	private final ConfigurationRepository configurationRepository;
	private final AzureReposService azureReposService;

	@Autowired
	public AzureReposCollectorTask(TaskScheduler taskScheduler,
			BaseCollectorRepository<AzureReposCollector> collectorRepository,
			AzureReposCollectorItemsRepository azureReposRepository, AzureReposSettings azureReposSettings,
			ConfigurationRepository configurationRepository, AzureReposService azureReposService)
			throws NoSuchFieldException {
		super(taskScheduler, AzureReposCollector.COLLECTOR_NAME);
		this.collectorRepository = collectorRepository;
		this.azureReposRepository = azureReposRepository;
		this.azureReposSettings = azureReposSettings;
		this.configurationRepository = configurationRepository;
		this.azureReposService = azureReposService;
	}

	@Override
	public AzureReposCollector getCollector() {

		Configuration config = configurationRepository.findByCollectorName(AzureReposCollector.COLLECTOR_NAME);
		if (config != null) {
			config.decryptOrEncrptInfo();
			Set<Map<String, String>> info = config.getInfo();
			List<String> tokens = info.stream().map(x -> x.get("password")).collect(Collectors.toList());
			azureReposSettings.setPersonalAccessToken(tokens.get(0));
		}

		return AzureReposCollector.prototype();
	}

	@Override
	public BaseCollectorRepository<AzureReposCollector> getCollectorRepository() {
		return collectorRepository;
	}

	@Override
	public String getCron() {
		return azureReposSettings.getCron();
	}

	@Override
	public void collect(AzureReposCollector collector) {

		logBanner("Starting...");
		long start = System.currentTimeMillis();
		int repoCount = 0;
		int commitCount = 0;
		int pullCount = 0;

		azureReposService.clean(collector);

		List<AzureRepo> azureReposCollectorItems = azureReposService.enabledAzureRepos(collector);
		for (AzureRepo azureRepo : azureReposCollectorItems) {

			azureRepo.checkErrorOrReset(0, 0);
			try {
				List<Commit> commits = azureReposService.getCommits(azureRepo);
				commitCount = azureReposService.saveNewCommits(commitCount, azureRepo, commits);
				List<GitRequest> gitRequests = azureReposService.getGitRequest(azureRepo);
				pullCount = azureReposService.saveNewGitRequests(pullCount, azureRepo, gitRequests);
				azureRepo.setLastUpdated(System.currentTimeMillis());
				azureRepo.setLastUpdateTime(System.currentTimeMillis());
			} catch (Exception e) {

				LOG.error("Error fetching commits for:" + azureRepo.getRepoUrl(), e);
				CollectionError error = new CollectionError("Bad repo url", azureRepo.getRepoUrl());
				azureRepo.getErrors().add(error);
			}

			azureReposRepository.save(azureRepo);
			repoCount++;
		}

		log("Repo Count", start, repoCount);
		log("New Commits", start, commitCount);
		log("New Pulls", start, pullCount);
		log("Finished", start);
	}

}

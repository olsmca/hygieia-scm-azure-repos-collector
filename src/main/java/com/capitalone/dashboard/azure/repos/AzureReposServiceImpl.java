package com.capitalone.dashboard.azure.repos;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.capitalone.dashboard.misc.HygieiaException;
import com.capitalone.dashboard.model.AzureRepo;
import com.capitalone.dashboard.model.Collector;
import com.capitalone.dashboard.model.CollectorItem;
import com.capitalone.dashboard.model.CollectorType;
import com.capitalone.dashboard.model.Commit;
import com.capitalone.dashboard.model.GitRequest;
import com.capitalone.dashboard.repository.AzureReposCollectorItemsRepository;
import com.capitalone.dashboard.repository.CommitRepository;
import com.capitalone.dashboard.repository.ComponentRepository;
import com.capitalone.dashboard.repository.GitRequestRepository;

@Component
public class AzureReposServiceImpl implements AzureReposService {
	private static final Log LOG = LogFactory.getLog(AzureReposServiceImpl.class);

	private static final long FOURTEEN_DAYS_MILLISECONDS = 14 * 24 * 60 * 60 * 1000;

	private final ComponentRepository componentRepository;
	private final AzureReposCollectorItemsRepository azureReposRepository;
	private final AzureReposClient azureReposClient;
	private final CommitRepository commitRepository;
	private final GitRequestRepository gitRequestRepository;

	@Autowired
	public AzureReposServiceImpl(ComponentRepository componentRepository,
			AzureReposCollectorItemsRepository azureReposRepository, AzureReposClient azureReposClient,
			CommitRepository commitRepository, GitRequestRepository gitRequestRepository) {
		this.componentRepository = componentRepository;
		this.azureReposRepository = azureReposRepository;
		this.azureReposClient = azureReposClient;
		this.commitRepository = commitRepository;
		this.gitRequestRepository = gitRequestRepository;

	}

	@Override
	public void clean(Collector collector) {
		Set<ObjectId> uniqueIDs = new HashSet<>();
		/**
		 * Logic: For each component, retrieve the collector item list of the type SCM.
		 * Store their IDs in a unique set ONLY if their collector IDs match with GitHub
		 * collectors ID.
		 */
		for (com.capitalone.dashboard.model.Component comp : componentRepository.findAll()) {
			if (comp.getCollectorItems() != null && !comp.getCollectorItems().isEmpty()) {
				List<CollectorItem> itemList = comp.getCollectorItems().get(CollectorType.SCM);
				if (itemList != null) {
					for (CollectorItem ci : itemList) {
						if (ci != null && ci.getCollectorId().equals(collector.getId())) {
							uniqueIDs.add(ci.getId());
						}
					}
				}
			}
		}

		/**
		 * Logic: Get all the collector items from the collector_item collection for
		 * this collector. If their id is in the unique set (above), keep them enabled;
		 * else, disable them.
		 */
		List<AzureRepo> repoList = new ArrayList<>();
		Set<ObjectId> gitID = new HashSet<>();
		gitID.add(collector.getId());
		for (AzureRepo repo : azureReposRepository.findByCollectorIdIn(gitID)) {
			repo.setEnabled(uniqueIDs.contains(repo.getId()));
			repoList.add(repo);
		}
		azureReposRepository.save(repoList);
	}

	@Override
	public List<AzureRepo> enabledAzureRepos(Collector collector) {
		List<AzureRepo> repos = azureReposRepository.findEnabledAzureRepos(collector.getId());

		if (CollectionUtils.isEmpty(repos)) {
			return new ArrayList<>();
		}

		return repos;
	}

	@Override
	public List<Commit> getCommits(AzureRepo azureRepo) throws HygieiaException {
		long start = System.currentTimeMillis();
		boolean isRepoFirstRun = isFirstRun(start, azureRepo.getLastUpdateTime());
		List<Commit> commits = azureReposClient.getCommits(azureRepo, isRepoFirstRun);
		return commits;
	}

	@Override
	public List<GitRequest> getGitRequest(AzureRepo azureRepo) throws HygieiaException {
		long start = System.currentTimeMillis();
		boolean firstRun = isFirstRun(start, azureRepo.getLastUpdateTime());
		List<GitRequest> gitRequests = azureReposClient.getPullRequest(azureRepo, firstRun);
		return gitRequests;
	}

	@Override
	public int saveNewCommits(int commitCount, AzureRepo azureRepo, List<Commit> commits) {

		int totalCommitCount = commitCount;
		for (Commit commit : commits) {
			LOG.debug(commit.getTimestamp() + ":::" + commit.getScmCommitLog());
			if (isNewCommit(azureRepo, commit)) {
				commit.setCollectorItemId(azureRepo.getId());
				commitRepository.save(commit);
				totalCommitCount++;
			}
		}
		return totalCommitCount;
	}

	@Override
	public int saveNewGitRequests(int pullCount, AzureRepo azureRepo, List<GitRequest> gitRequests) {

		int count = pullCount;

		for (GitRequest entry : gitRequests) {
			LOG.debug(entry.getTimestamp() + ":::" + entry.getScmCommitLog());
			GitRequest existing = gitRequestRepository.findByCollectorItemIdAndNumberAndRequestType(azureRepo.getId(),
					entry.getNumber(), "pull");

			if (existing == null) {
				entry.setCollectorItemId(azureRepo.getId());
				count++;
			} else {
				entry.setId(existing.getId());
				entry.setCollectorItemId(azureRepo.getId());
			}
			gitRequestRepository.save(entry);
		}

		return count;
	}

	@Override
	public boolean isFirstRun(long start, long lastUpdated) {
		boolean firstRun = ((lastUpdated == 0) || ((start - lastUpdated) > FOURTEEN_DAYS_MILLISECONDS));
		return firstRun;
	}

	@Override
	public boolean isNewCommit(AzureRepo repo, Commit commit) {
		return commitRepository.findByCollectorItemIdAndScmRevisionNumber(repo.getId(),
				commit.getScmRevisionNumber()) == null;
	}

}

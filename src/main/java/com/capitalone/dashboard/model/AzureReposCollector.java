package com.capitalone.dashboard.model;

import java.util.HashMap;
import java.util.Map;

public class AzureReposCollector extends Collector {

	public static final String COLLECTOR_NAME = "AzureRepos";

	public static AzureReposCollector prototype() {
		AzureReposCollector protoType = new AzureReposCollector();
		protoType.setName(COLLECTOR_NAME);
		protoType.setCollectorType(CollectorType.SCM);
		protoType.setOnline(true);
		protoType.setEnabled(true);

		Map<String, Object> allOptions = new HashMap<>();
		allOptions.put(AzureRepo.REPO_URL, "");
		allOptions.put(AzureRepo.BRANCH, "");
		allOptions.put(AzureRepo.USER_ID, "");
		allOptions.put(AzureRepo.PASSWORD, "");
		allOptions.put(AzureRepo.PERSONAL_ACCESS_TOKEN, "");
		allOptions.put(AzureRepo.LAST_UPDATE_TIME, System.currentTimeMillis());
		protoType.setAllFields(allOptions);

		Map<String, Object> uniqueOptions = new HashMap<>();
		uniqueOptions.put(AzureRepo.REPO_URL, "");
		uniqueOptions.put(AzureRepo.BRANCH, "");
		protoType.setUniqueFields(uniqueOptions);
		return protoType;
	}
}

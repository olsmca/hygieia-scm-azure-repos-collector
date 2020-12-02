package com.capitalone.dashboard.repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;

import com.capitalone.dashboard.model.AzureRepo;

public interface AzureReposCollectorItemsRepository extends BaseCollectorItemRepository<AzureRepo> {

	@Query(value = "{'collectorId' : ?0, enabled: true}")
    List<AzureRepo> findEnabledAzureRepos(ObjectId collectorId);
}

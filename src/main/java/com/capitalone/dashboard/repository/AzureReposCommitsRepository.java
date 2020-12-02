package com.capitalone.dashboard.repository;

import org.bson.types.ObjectId;

import com.capitalone.dashboard.model.Commit;

public interface AzureReposCommitsRepository extends CommitRepository {
	
    Commit findByCollectorItemIdAndScmRevisionNumberAndScmBranch(ObjectId collectorItemId, String revisionNumber, String branch);

}

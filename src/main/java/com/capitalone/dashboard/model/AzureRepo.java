package com.capitalone.dashboard.model;

import java.util.Date;

/**
 * CollectorItem extension to store the Azure Repos
 */
public class AzureRepo extends CollectorItem {
	
    public static final String REPO_URL = "url";
    public static final String BRANCH = "branch";
    public static final String USER_ID = "userID";
    public static final String PASSWORD = "password";
    public static final String LAST_UPDATE_TIME = "lastUpdate";
    public static final String PERSONAL_ACCESS_TOKEN = "personalAccessToken";

    public String getUserId() {
        return (String) getOptions().get(USER_ID);
    }

    public void setUserId(String userId) {
        getOptions().put(USER_ID, userId);
    }
    
    public String getPassword() {
        return (String) getOptions().get(PASSWORD);
    }

    public void setPassword(String password) {
        getOptions().put(PASSWORD, password);
    }
    
    
    public String getRepoUrl() {
        return (String) getOptions().get(REPO_URL);
    }

    public void setRepoUrl(String instanceUrl) {
        getOptions().put(REPO_URL, instanceUrl);
    }
    
    public String getBranch() {
        return (String) getOptions().get(BRANCH);
    }

    public void setBranch(String branch) {
        getOptions().put(BRANCH, branch);
    }

    public Long getLastUpdateTime() {
        Object latest = getOptions().get(LAST_UPDATE_TIME);

        if (latest == null) return (long) 0;

        if (latest instanceof Date) {
            return ((Date)latest).getTime();
        }

        return (Long) latest;
    }

    public void setLastUpdateTime(Long dateInMillis) {
        getOptions().put(LAST_UPDATE_TIME, dateInMillis);
    }

    public String getPersonalAccessToken() {
    	return (String) getOptions().get(PERSONAL_ACCESS_TOKEN);
    }

    public void setPersonalAccessToken(String personalAccessToken) {
        getOptions().put(PERSONAL_ACCESS_TOKEN, personalAccessToken);
    }


    public void removeLastUpdateDate() {
        getOptions().remove(LAST_UPDATE_TIME);
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) {
        	return true;
        }
        if (o == null || getClass() != o.getClass()) {
        	return false;
        }

        AzureRepo gitHubRepo = (AzureRepo) o;

        return getRepoUrl().equals(gitHubRepo.getRepoUrl()) & getBranch().equals(gitHubRepo.getBranch());
    }

    @Override
    public int hashCode() {
        return getRepoUrl().hashCode();
    }

}

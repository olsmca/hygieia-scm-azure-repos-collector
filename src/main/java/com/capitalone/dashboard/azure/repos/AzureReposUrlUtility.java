package com.capitalone.dashboard.azure.repos;

import java.net.MalformedURLException;
import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import com.capitalone.dashboard.collector.AzureReposSettings;
import com.capitalone.dashboard.misc.HygieiaException;
import com.capitalone.dashboard.model.AzureRepo;
import com.capitalone.dashboard.model.AzureRepoParsed;

@Component
public class AzureReposUrlUtility {

	private AzureReposSettings collectorSettings;
	private static final String DEFAULT_VERSION = "6.0";
	private static final String SEGMENT_API = "_apis";
	private static final String SEGMENT_GIT = "git";
	private static final String SEGMENT_REPOSITORIES = "repositories";
	private static final String SEGMEN_COMMITS = "commits";
	private static final String SEGMEN_PULLREQUESTS = "pullrequests";
	private static final String API_VERSION = "api-version";
	private static final String TOP = "$top";
	private static final String DATE_QUERY_PARAM_KEY = "searchCriteria.fromDate";
	private static final String BRANCH_QUERY_PARAM_KEY = "branch";
	private static final String PULLREQUEST_STATUS_QUERY_PARAM_KEY = "searchCriteria.status";
	private static final String PULLREQUEST_TARGET_QUERY_PARAM_KEY = "searchCriteria.targetRefName";
	private static final String REFS_BRANCH_SUFFIX = "refs/heads/";
	private static final String DATE_FORMAT_QUERY_GIT = "MM/dd/yyyy'%20'HH:mm:ss";
	private static final int FIRST_RUN_HISTORY_DEFAULT = 90;
	private static final Integer DEFAULT_TOP = 10_000;

	@Autowired
	public AzureReposUrlUtility(AzureReposSettings collectorSettings) {
		this.collectorSettings = collectorSettings;
	}

	public URI buildApiCommitsUrl(AzureRepo repo, boolean firstRun) throws HygieiaException {

		try {

			AzureRepoParsed azureRepoParsed = new AzureRepoParsed(repo.getRepoUrl());

			String date = getDateForCommits(repo, firstRun);
			String apiVersion = getApiVersion();

			UriComponentsBuilder builderBase = UriComponentsBuilder.newInstance();

			return builderBase.scheme(azureRepoParsed.getProtocol()).host(azureRepoParsed.getHost())
					.pathSegment(azureRepoParsed.getOrgName()).pathSegment(azureRepoParsed.getProject())
					.pathSegment(SEGMENT_API).pathSegment(SEGMENT_GIT).pathSegment(SEGMENT_REPOSITORIES)
					.pathSegment(azureRepoParsed.getRepoName()).pathSegment(SEGMEN_COMMITS)
					.queryParam(BRANCH_QUERY_PARAM_KEY, repo.getBranch()).queryParam(DATE_QUERY_PARAM_KEY, date)
					.queryParam(TOP, DEFAULT_TOP).queryParam(API_VERSION, apiVersion).build(true).toUri();

		} catch (MalformedURLException | HygieiaException e) {
			throw new HygieiaException("Bad repo URL: " + e.getMessage(), HygieiaException.BAD_DATA);
		}

	}

	public URI buildApiPullsUrl(AzureRepo repo, String status, boolean firstRun) throws HygieiaException {

		try {

			AzureRepoParsed azureRepoParsed = new AzureRepoParsed(repo.getRepoUrl());
			String apiVersion = getApiVersion();
			UriComponentsBuilder builderBase = UriComponentsBuilder.newInstance();

			return builderBase.scheme(azureRepoParsed.getProtocol()).host(azureRepoParsed.getHost())
					.pathSegment(azureRepoParsed.getOrgName()).pathSegment(azureRepoParsed.getProject())
					.pathSegment(SEGMENT_API).pathSegment(SEGMENT_GIT).pathSegment(SEGMENT_REPOSITORIES)
					.pathSegment(azureRepoParsed.getRepoName()).pathSegment(SEGMEN_PULLREQUESTS)
					.queryParam(PULLREQUEST_STATUS_QUERY_PARAM_KEY, status)
					.queryParam(PULLREQUEST_TARGET_QUERY_PARAM_KEY, REFS_BRANCH_SUFFIX + repo.getBranch())
					.queryParam(TOP, DEFAULT_TOP).queryParam(API_VERSION, apiVersion).build(true).toUri();

		} catch (MalformedURLException | HygieiaException e) {
			throw new HygieiaException("Bad repo URL: " + e.getMessage(), HygieiaException.BAD_DATA);
		}

	}

	private String getApiVersion() {
		return StringUtils.isBlank(collectorSettings.getApiVersion()) ? DEFAULT_VERSION
				: collectorSettings.getApiVersion();
	}

	private String getDateForCommits(AzureRepo repo, boolean firstRun) {
		Date dt;
		if (firstRun) {
			dt = getDate(new Date(), -FIRST_RUN_HISTORY_DEFAULT, 0);
		} else {
			dt = getDate(new Date(repo.getLastUpdated()), 0, -getMinutesFromCron());
		}
		DateFormat df = new SimpleDateFormat(DATE_FORMAT_QUERY_GIT);
		return df.format(dt);
	}

	private int getMinutesFromCron(){		
		return Integer.valueOf(collectorSettings.getCron().split(" ")[1].split("/")[1]);
	}

	private Date getDate(Date dateInstance, int offsetDays, int offsetMinutes) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(dateInstance);
		cal.add(Calendar.DATE, offsetDays);
		cal.add(Calendar.MINUTE, offsetMinutes);
		return cal.getTime();
	}
}

package com.capitalone.dashboard.model;

import java.net.MalformedURLException;
import java.net.URL;

import com.capitalone.dashboard.misc.HygieiaException;

public class AzureRepoParsed {

	private String url;
	private String host;
	private String protocol;
	private String apiUrl;
	private String orgName;
	private String repoName;
	private String project;

	private static final String PUBLIC_AZURE_REPO_API = "/_apis/git/repositories/";
	private static final String PUBLIC_AZURE_REPOS_HOST_NAME = "dev.azure.com";

	public AzureRepoParsed(String url) throws MalformedURLException, HygieiaException {
		this.url = url;
		parse();
	}

	private void parse() throws MalformedURLException, HygieiaException {
		if (url.endsWith(".git")) {
			url = url.substring(0, url.lastIndexOf(".git"));
		}
		URL u = new URL(url);
		host = u.getHost();
		protocol = u.getProtocol();
		String path = u.getPath();
		String[] parts = path.split("/");
		if ((parts == null) || (parts.length < 3)) {
			throw new HygieiaException("Bad repo URL: " + url, HygieiaException.BAD_DATA);
		}

		repoName = parts[parts.length-1];

		if (host.startsWith(PUBLIC_AZURE_REPOS_HOST_NAME)) {
			orgName = parts[1];
			project = parts[2];
			
		} else {
			if (host.split("\\.").length > 2) {
				int lastIndex = host.lastIndexOf(".");
				int index = host.lastIndexOf(".", lastIndex - 1);
				orgName = host.substring(0, index);
				host = PUBLIC_AZURE_REPOS_HOST_NAME;
			}
			project = parts[1];
		}
		
		apiUrl = protocol + "://" + host + "/"+ orgName + "/"+ project + PUBLIC_AZURE_REPO_API + repoName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getApiUrl() {
		return apiUrl;
	}

	public void setApiUrl(String apiUrl) {
		this.apiUrl = apiUrl;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getRepoName() {
		return repoName;
	}

	public void setRepoName(String repoName) {
		this.repoName = repoName;
	}

	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
}

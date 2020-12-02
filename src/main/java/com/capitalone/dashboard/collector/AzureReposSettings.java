package com.capitalone.dashboard.collector;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "azure-repos")
public class AzureReposSettings {

	private String cron;
	private String apiVersion;
	private String key;
	private String personalAccessToken;

	private String proxyHost;
    private String proxyPort;
    private String nonProxy;
    
	public String getCron() {
		return cron;
	}
	public void setCron(String cron) {
		this.cron = cron;
	}
	public String getApiVersion() {
		return apiVersion;
	}
	public void setApiVersion(String apiVersion) {
		this.apiVersion = apiVersion;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getPersonalAccessToken() {
		return personalAccessToken;
	}
	public void setPersonalAccessToken(String personalAccessToken) {
		this.personalAccessToken = personalAccessToken;
	}
	public String getProxyHost() {
		return proxyHost;
	}
	public void setProxyHost(String proxyHost) {
		this.proxyHost = proxyHost;
	}
	public String getProxyPort() {
		return proxyPort;
	}
	public void setProxyPort(String proxyPort) {
		this.proxyPort = proxyPort;
	}
	public String getNonProxy() {
		return nonProxy;
	}
	public void setNonProxy(String nonProxy) {
		this.nonProxy = nonProxy;
	}

}

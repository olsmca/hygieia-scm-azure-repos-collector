package com.capitalone.dashboard.azure.repos;

import java.net.URI;
import java.nio.charset.StandardCharsets;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;

import com.capitalone.dashboard.collector.AzureReposSettings;
import com.capitalone.dashboard.util.Encryption;
import com.capitalone.dashboard.util.EncryptionException;
import com.capitalone.dashboard.util.Supplier;

@Component
public class GenericClient {

	private static final Log LOG = LogFactory.getLog(GenericClient.class);

	private RestOperations restOperations;
	private AzureReposSettings collectorSettings;

	@Autowired
	public GenericClient(AzureReposSettings collectorSettings, Supplier<RestOperations> restOperationsSupplier) {

		this.collectorSettings = collectorSettings;
		this.restOperations = restOperationsSupplier.get();
		proxyConfiguration();
	}

	public void proxyConfiguration() {

		String proxyUrl = collectorSettings.getProxyHost();
		String proxyPort = collectorSettings.getProxyPort();
		String nonProxy = collectorSettings.getNonProxy();

		if (!StringUtils.isEmpty(proxyUrl) && !StringUtils.isEmpty(proxyPort)) {
			System.getProperties().put("http.proxyHost", proxyUrl);
			System.getProperties().put("http.proxyPort", proxyPort);
			System.getProperties().put("https.proxyHost", proxyUrl);
			System.getProperties().put("https.proxyPort", proxyPort);
			
		}
		
		if(!StringUtils.isEmpty(nonProxy)) {
			System.getProperties().put("http.nonProxyHosts", nonProxy);
		}

	}

	@Retryable(maxAttempts = 10, value = Exception.class, backoff = @Backoff(delay = 1000, multiplier = 2, maxDelay = 900000))
	public <T> ResponseEntity<T> makeGetRestCall(URI url, String userId, String password, String personalAccessToken,
			Class<T> responseType) {

		if (!"".equals(userId) && !"".equals(password)) {
			return restOperations.exchange(url, HttpMethod.GET, new HttpEntity<>(createHeaders(userId, password)),
					responseType);
		} else if ((personalAccessToken != null && !"".equals(personalAccessToken))) {
			return restOperations.exchange(url, HttpMethod.GET,
					new HttpEntity<>(createHeaders("hygieia", personalAccessToken)), responseType);
		} else if (collectorSettings.getPersonalAccessToken() != null
				&& !"".equals(collectorSettings.getPersonalAccessToken())) {
			return restOperations.exchange(url, HttpMethod.GET,
					new HttpEntity<>(createHeaders("hygieia", collectorSettings.getPersonalAccessToken())),
					responseType);
		} else {
			return restOperations.exchange(url, HttpMethod.GET, null, responseType);
		}
	}

	public HttpHeaders createHeaders(final String userId, final String password) {
		String auth = userId + ":" + password;
		byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(StandardCharsets.US_ASCII));
		String authHeader = "Basic " + new String(encodedAuth);

		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", authHeader);
		return headers;
	}

	/**
	 * Decrypt string
	 * 
	 * @param string
	 * @param key
	 * @return String
	 */
	public String decryptString(String string, String key) {
		if (!StringUtils.isEmpty(string)) {
			try {
				return Encryption.decryptString(string, key);
			} catch (EncryptionException e) {
				LOG.error(e.getMessage());
			}
		}
		return "";
	}
}

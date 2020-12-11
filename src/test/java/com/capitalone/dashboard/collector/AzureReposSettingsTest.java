package com.capitalone.dashboard.collector;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AzureReposSettingsTest {

	@InjectMocks
	private AzureReposSettings azureReposSettings;

	@Test
	public void testSetCron() throws Exception {
		String expected = "0 0 0 0 0";
		azureReposSettings.setCron(expected);
		String actual = azureReposSettings.getCron();
		assertEquals(expected, actual);
	}

	@Test
	public void testSetApiVersion() throws Exception {
		String expected = "6.0";
		azureReposSettings.setApiVersion(expected);
		String actual = azureReposSettings.getApiVersion();
		assertEquals(expected, actual);
	}

	@Test
	public void testSetKey() throws Exception {
		String expected = "123456";
		azureReposSettings.setKey(expected);
		String actual = azureReposSettings.getKey();
		assertEquals(expected, actual);
	}

	@Test
	public void testSetPersonalAccessToken() throws Exception {
		String expected = "1234567890";
		azureReposSettings.setPersonalAccessToken(expected);
		String actual = azureReposSettings.getPersonalAccessToken();
		assertEquals(expected, actual);
	}

	@Test
	public void testSetProxyHost() throws Exception {
		String expected = "1.1.1.1";
		azureReposSettings.setProxyHost(expected);
		String actual = azureReposSettings.getProxyHost();
		assertEquals(expected, actual);
	}

	@Test
	public void testSetProxyPort() throws Exception {
		String expected = "8080";
		azureReposSettings.setProxyPort(expected);
		String actual = azureReposSettings.getProxyPort();
		assertEquals(expected, actual);
	}

	@Test
	public void testSetNonProxy() throws Exception {
		String expected = "x.x.x.x.x";
		azureReposSettings.setNonProxy(expected);
		String actual = azureReposSettings.getNonProxy();
		assertEquals(expected, actual);
	}

}

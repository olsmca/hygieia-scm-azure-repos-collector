package com.capitalone.dashboard.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.capitalone.dashboard.misc.HygieiaException;

@RunWith(MockitoJUnitRunner.class)
public class AzureRepoParsedTest {

	@Test
	public void testAzureRepoParsedNewUrl() throws Exception {
		// Arrange
		String url = "https://dev.azure.com/test/Hack/_git/test";
		// Act
		AzureRepoParsed azureRepoParsed = new AzureRepoParsed(url);
		// Assert
		assertEquals("dev.azure.com", azureRepoParsed.getHost());
		assertEquals("test", azureRepoParsed.getOrgName());
		assertEquals("Hack", azureRepoParsed.getProject());
		assertEquals("test", azureRepoParsed.getRepoName());
	}

	@Test
	public void testAzureRepoParsedNewOld() throws Exception {
		// Arrange
		String url = "https://test.visualstudio.com/Hack/_git/test";
		// Act
		AzureRepoParsed azureRepoParsed = new AzureRepoParsed(url);
		// Assert
		assertEquals("dev.azure.com", azureRepoParsed.getHost());
		assertEquals("test", azureRepoParsed.getOrgName());
		assertEquals("Hack", azureRepoParsed.getProject());
		assertEquals("test", azureRepoParsed.getRepoName());
	}

	@Test(expected = HygieiaException.class)
	public void testAzureRepoParsedException() throws Exception {
		// Arrange
		String url = "https://test.git";
		// Act
		new AzureRepoParsed(url);
	}

}

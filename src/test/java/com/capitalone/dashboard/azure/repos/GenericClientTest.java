package com.capitalone.dashboard.azure.repos;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.URI;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestOperations;

import com.capitalone.dashboard.azure.repos.model.AzureReposGitCommitResponse;
import com.capitalone.dashboard.collector.AzureReposSettings;
import com.capitalone.dashboard.util.Encryption;
import com.capitalone.dashboard.util.Supplier;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.Appender;

@RunWith(MockitoJUnitRunner.class)
public class GenericClientTest {

	@Mock
	private Appender<ILoggingEvent> mockedAppender;

	@Captor
	private ArgumentCaptor<LoggingEvent> loggingEventCaptor;

	@Mock
	private AzureReposSettings collectorSettings;

	@Mock
	private Supplier<RestOperations> restOperationsSupplier;

	@Mock
	private RestOperations rest;

	@InjectMocks
	private GenericClient genericClient;

	@Before
	public void setup() {
		Logger root = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
		root.addAppender(mockedAppender);
		root.setLevel(Level.ERROR);
	}

	@Test
	public void whenUserAndPasswordExistThenItIsAuthenticatedWithUsernameAndPassword() throws Exception {
		// Arrange
		String userId = "userId";
		String password = "123456";
		String personalAccessToken = "";
		URI url = new URI("http://example.com");
		AzureReposGitCommitResponse reponse = new AzureReposGitCommitResponse();
		ResponseEntity<AzureReposGitCommitResponse> reponseEntity = new ResponseEntity<>(reponse, HttpStatus.OK);
		when(rest.exchange(eq(url), eq(HttpMethod.GET), any(), eq(AzureReposGitCommitResponse.class)))
				.thenReturn(reponseEntity);
		when(restOperationsSupplier.get()).thenReturn(rest);

		GenericClient genericClient = new GenericClient(collectorSettings, restOperationsSupplier);
		// Act
		genericClient.makeGetRestCall(url, userId, password, personalAccessToken, AzureReposGitCommitResponse.class);
		// Assert
		verify(rest).exchange(eq(url), eq(HttpMethod.GET), any(), eq(AzureReposGitCommitResponse.class));
	}

	@Test
	public void whenThereIsNoUsernameAndPasswordThenItIsAuthenticatedWithTheRepoPersonalAccessToken() throws Exception {
		// Arrange
		String userId = "";
		String password = "";
		String personalAccessToken = "123456789";
		URI url = new URI("http://example.com");
		AzureReposGitCommitResponse reponse = new AzureReposGitCommitResponse();
		ResponseEntity<AzureReposGitCommitResponse> reponseEntity = new ResponseEntity<>(reponse, HttpStatus.OK);
		when(rest.exchange(eq(url), eq(HttpMethod.GET), any(), eq(AzureReposGitCommitResponse.class)))
				.thenReturn(reponseEntity);
		when(restOperationsSupplier.get()).thenReturn(rest);

		GenericClient genericClient = new GenericClient(collectorSettings, restOperationsSupplier);
		// Act
		genericClient.makeGetRestCall(url, userId, password, personalAccessToken, AzureReposGitCommitResponse.class);
		// Assert
		verify(rest).exchange(eq(url), eq(HttpMethod.GET), any(), eq(AzureReposGitCommitResponse.class));
	}

	@Test
	public void whenThereIsNoUsernameAndPasswordThenItIsAuthenticatedWithTheCollectorPersonalAccessToken()
			throws Exception {
		// Arrange
		String userId = "";
		String password = "";
		String personalAccessToken = "";
		URI url = new URI("http://example.com");
		AzureReposGitCommitResponse reponse = new AzureReposGitCommitResponse();
		ResponseEntity<AzureReposGitCommitResponse> reponseEntity = new ResponseEntity<>(reponse, HttpStatus.OK);
		when(rest.exchange(eq(url), eq(HttpMethod.GET), any(), eq(AzureReposGitCommitResponse.class)))
				.thenReturn(reponseEntity);
		when(restOperationsSupplier.get()).thenReturn(rest);
		when(collectorSettings.getPersonalAccessToken()).thenReturn("1234455");

		GenericClient genericClient = new GenericClient(collectorSettings, restOperationsSupplier);
		// Act
		genericClient.makeGetRestCall(url, userId, password, personalAccessToken, AzureReposGitCommitResponse.class);
		// Assert
		verify(rest).exchange(eq(url), eq(HttpMethod.GET), any(), eq(AzureReposGitCommitResponse.class));
	}

	@Test
	public void whenThereAreNoCredentialsThenDontCreateHeaders() throws Exception {
		// Arrange
		String userId = "";
		String password = "";
		String personalAccessToken = "";
		URI url = new URI("http://example.com");
		AzureReposGitCommitResponse reponse = new AzureReposGitCommitResponse();
		ResponseEntity<AzureReposGitCommitResponse> reponseEntity = new ResponseEntity<>(reponse, HttpStatus.OK);
		when(rest.exchange(eq(url), eq(HttpMethod.GET), any(), eq(AzureReposGitCommitResponse.class)))
				.thenReturn(reponseEntity);
		when(restOperationsSupplier.get()).thenReturn(rest);

		GenericClient genericClient = new GenericClient(collectorSettings, restOperationsSupplier);
		// Act
		genericClient.makeGetRestCall(url, userId, password, personalAccessToken, AzureReposGitCommitResponse.class);
		// Assert
		verify(rest).exchange(eq(url), eq(HttpMethod.GET), any(), eq(AzureReposGitCommitResponse.class));
	}

	@Test
	public void testCreateHeaders() throws Exception {
		// Arrange
		String userId = "userId";
		String password = "123456";
		// Act
		HttpHeaders actual = genericClient.createHeaders(userId, password);
		// Assert
		assertTrue(actual.containsKey("Authorization"));
	}

	@Test
	public void givenACredentialThenItMustBeDecrypt() throws Exception {
		// Arrange
		String actual = "123456789";
		String key = "DfvBg+AOGol5fOofjMdPYtpYGQ1rH4km";
		String token = Encryption.encryptString(actual, key);
		// Act
		String expected = genericClient.decryptString(token, key);
		// Assert
		assertEquals(expected, actual);
	}

	@Test
	public void whenCredentialIsVariousThenItReturnsEmpty() throws Exception {
		// Act
		String expected = genericClient.decryptString("", "");
		// Assert
		assertTrue(expected.isEmpty());
	}

	@Test
	public void whenDecryptFailsThenItPrintsInTheLog() throws Exception {
		// Act
		String expected = genericClient.decryptString("test", "");
		// Assert
		assertEquals(expected, "");
		verify(mockedAppender, times(1)).doAppend(loggingEventCaptor.capture());
		LoggingEvent loggingEvent = loggingEventCaptor.getAllValues().get(0);
		assertEquals(Level.ERROR, loggingEvent.getLevel());
	}

	@Test
	public void testProxyConfiguration() throws Exception {
		// Arrange
		when(collectorSettings.getProxyHost()).thenReturn("host");
		when(collectorSettings.getProxyPort()).thenReturn("8080");
		when(collectorSettings.getNonProxy()).thenReturn("1.1.1.1");
		// Act
		genericClient.proxyConfiguration();
		// Assert
		assertEquals(System.getProperty("http.proxyHost"), "host");
		assertEquals(System.getProperty("http.nonProxyHosts"), "1.1.1.1");
	}

}

package com.capitalone.dashboard.collector;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

@RunWith(MockitoJUnitRunner.class)
public class RestOperationsSupplierTest {

	@InjectMocks
	private RestOperationsSupplier restOperationsSupplier;

	@Test
	public void testGet() throws Exception {
		// Act
		RestOperations actual = restOperationsSupplier.get();
		// Assert
		assertThat(actual, instanceOf(RestTemplate.class));
	}

}

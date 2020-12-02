package com.capitalone.dashboard.collector;

import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import com.capitalone.dashboard.util.Supplier;

@Component
public class RestOperationsSupplier implements Supplier<RestOperations> {
	
	@Override
    public RestOperations get() {
		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setConnectTimeout(20000);
        requestFactory.setReadTimeout(20000);
        return new RestTemplate(requestFactory);
    }
}

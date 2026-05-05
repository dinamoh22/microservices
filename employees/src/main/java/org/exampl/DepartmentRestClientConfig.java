package org.exampl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class DepartmentRestClientConfig {

    @Bean
    @LoadBalanced
    RestClient.Builder loadBalancedRestClientBuilder() {
        return RestClient.builder();
    }

    @Bean
    RestClient departmentHttpClient(
            RestClient.Builder loadBalancedRestClientBuilder,
            @Value("${services.departments.base-url}") String baseUrl) {

        return loadBalancedRestClientBuilder
                .baseUrl(baseUrl)
                .build();
    }
}

package com.lecture.employeesservice;

import com.lecture.employeesservice.dtos.DepartmentSummaryDto;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class DepartmentRestClient {

    private final RestClient restClient;

    public DepartmentRestClient(@Qualifier("departmentHttpClient") RestClient restClient) {
        this.restClient = restClient;
    }

    public DepartmentSummaryDto getDepartmentSummary(Long departmentId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!(authentication instanceof JwtAuthenticationToken jwtAuth)) {
            throw new DownstreamServiceException("No JWT available for downstream call");
        }

        String tokenValue = jwtAuth.getToken().getTokenValue();

        return restClient.get()
                .uri("/internal/departments/{id}/summary", departmentId)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenValue)
                .retrieve()
                .body(DepartmentSummaryDto.class);
    }
}

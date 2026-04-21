package com.lecture.employeesservice;

import com.lecture.employeesservice.dtos.DepartmentSummaryDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class DepartmentRestClient {

    private final RestClient restClient;

    public DepartmentRestClient(@Qualifier("departmentHttpClient") RestClient restClient) {
        this.restClient = restClient;
    }

    public DepartmentSummaryDto getDepartmentSummary(Long departmentId) {
        return restClient.get()
                .uri("/internal/departments/{id}/summary", departmentId)
                .retrieve()
                .body(DepartmentSummaryDto.class);
    }
}
package org.exampl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Component
public class DepartmentRestClient {

    private final RestClient restClient;

    public DepartmentRestClient(@Qualifier("departmentHttpClient") RestClient restClient) {
        this.restClient = restClient;
    }

    public DepartmentSummaryDto getDepartmentSummary(Long departmentId) {
        // Propagate the caller's JWT to the downstream departments-service
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!(authentication instanceof JwtAuthenticationToken jwtAuth)) {
            throw new ServiceUnavailableException("No JWT available for downstream call to departments-service");
        }

        String tokenValue = jwtAuth.getToken().getTokenValue();

        return restClient.get()
                .uri("/internal/departments/{id}/summary", departmentId)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenValue)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (req, res) -> {
                    throw new DepartmentNotFoundException(departmentId);
                })
                .onStatus(HttpStatusCode::is5xxServerError, (req, res) -> {
                    throw new RestClientException("Departments service error");
                })
                .body(DepartmentSummaryDto.class);
    }
}

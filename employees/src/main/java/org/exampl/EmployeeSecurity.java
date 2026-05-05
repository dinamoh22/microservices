package org.exampl;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

/**
 * Authorization helper bean for ownership-based checks.
 * Referenced in @PreAuthorize via the bean name "employeeSecurity".
 */
@Component("employeeSecurity")
public class EmployeeSecurity {

    /**
     * Returns true if the authenticated user's empId claim matches the given employeeId.
     * Used for "can a USER access their own record" checks.
     */
    public boolean isSelf(Long employeeId, Authentication authentication) {
        if (authentication == null) return false;

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof Jwt jwt)) return false;

        // empId is stored as a Long in the token (set by auth-service JwtTokenService)
        Object empIdClaim = jwt.getClaim("empId");
        if (empIdClaim == null) return false;

        Long empId = ((Number) empIdClaim).longValue();
        return empId.equals(employeeId);
    }
}

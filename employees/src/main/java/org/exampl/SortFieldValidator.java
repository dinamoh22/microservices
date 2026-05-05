package org.exampl;

import org.springframework.data.domain.Sort;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.Set;

public class SortFieldValidator {

    private static final Set<String> ALLOWED_FIELDS = Set.of(
            "id", "name", "role", "yearsOfExperience", "hiredDate"
    );

    public static void validateSort(Sort sort) {
        for (Sort.Order order : sort) {
            if (!ALLOWED_FIELDS.contains(order.getProperty())) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Sorting by '" + order.getProperty() + "' is not allowed. Allowed fields: " + ALLOWED_FIELDS
                );
            }
        }
    }
}

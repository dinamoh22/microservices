package org.exampl;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, String>> handleAccessDenied(AccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(Map.of("message", "Access denied"));
    }

//    @ExceptionHandler(ServiceUnavailableException.class)
//    public ResponseEntity<Map<String, String>> handleServiceUnavailable(ServiceUnavailableException ex) {
//        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
//                .body(Map.of("message", ex.getMessage()));
//    }

    @ExceptionHandler(EmployeeNotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(
            EmployeeNotFoundException ex,
            HttpServletRequest request
    ) {
        ApiError body = new ApiError(
                Instant.now().toString(),
                404,
                "Not Found",
                ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }


    @ExceptionHandler(InvalidDepartmentException.class)
    public ResponseEntity<ApiError> handleInvalidDepartment(
            InvalidDepartmentException ex,
            HttpServletRequest request) {
        ApiError body = new ApiError(
                Instant.now().toString(),
                400,
                "Bad Request",
                ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(ServiceUnavailableException.class)
    public ResponseEntity<ApiError> handleServiceUnavailable(
            ServiceUnavailableException ex,
            HttpServletRequest request) {
        ApiError body = new ApiError(
                Instant.now().toString(),
                503,
                "Service Unavailable",
                ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(body);
    }

    // Optional: catch-all (prevents stack traces from leaking to clients)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneric(
            Exception ex,
            HttpServletRequest request
    ) {
        ApiError body = new ApiError(
                Instant.now().toString(),
                500,
                "Internal Server Error",
                "Something went wrong",
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

//
//
//    @ExceptionHandler(DepartmentNotFoundException.class)
//    public ResponseEntity<ApiError> handleDepartmentNotFound(
//            DepartmentNotFoundException ex, WebRequest request) {
//
//        ApiError errorBody = new ApiError(
//                Instant.now().toString(),             // timestamp [cite: 39]
//                HttpStatus.NOT_FOUND.value(),    // status: 404 [cite: 39]
//                "Not Found",                     // error [cite: 39]
//                ex.getMessage(),                 // message [cite: 39]
//                request.getDescription(false).replace("uri=", "") // path [cite: 39]
//        );
//
//        return new ResponseEntity<>(errorBody, HttpStatus.NOT_FOUND); // Returns 404 [cite: 37]
//    }

    // Add this to your GlobalExceptionHandler class
    @ExceptionHandler(org.springframework.web.server.ResponseStatusException.class)
    public ResponseEntity<ApiError> handleConflict(
            org.springframework.web.server.ResponseStatusException ex, WebRequest request) {

        ApiError errorBody = new ApiError(
                Instant.now().toString(),
                ex.getStatusCode().value(),
                "Conflict",
                ex.getReason(),
                request.getDescription(false).replace("uri=", "")
        );

        return new ResponseEntity<>(errorBody, ex.getStatusCode());
    }



    @ExceptionHandler(org.springframework.web.bind.MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(
            org.springframework.web.bind.MethodArgumentNotValidException ex,
            org.springframework.web.context.request.WebRequest request) {

        // 1. Collect all field errors
        List<ApiError.FieldError> details = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new ApiError.FieldError(error.getField(), error.getDefaultMessage()))
                .toList();

        // 2. Build a custom body (Task B2 usually requires the standard ApiError fields + the details)
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", java.time.LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Bad Request");
        body.put("message", "Validation failed");
        body.put("path", request.getDescription(false).replace("uri=", ""));
        body.put("errors", details); // This adds the list of specific field errors

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
}

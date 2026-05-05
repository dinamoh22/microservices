package org.exampl;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeServiceImpl employeeService;

    EmployeeController(EmployeeServiceImpl employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping
    ResponseEntity<EmployeeResponseDto> newEmployee(
            @RequestBody EmployeeRequestDto newEmployee,
            UriComponentsBuilder uriBuilder
    ) {
        Employee saved = employeeService.create(EmployeeMapper.toEntity(newEmployee));
        EmployeeResponseDto employeeResponseDto = EmployeeMapper.toDto(saved);

        URI location = uriBuilder
                .path("/employees/{id}")
                .buildAndExpand(saved.getId())
                .toUri();

        return ResponseEntity.created(location).body(employeeResponseDto);
    }

    @PostMapping("/create2")
    public ResponseEntity<EmployeeResponseDto> newEmployee2(
            @RequestBody @Valid EmployeeRequestDto request,
            UriComponentsBuilder uriBuilder
    ) {
        EmployeeResponseDto savedDto = employeeService.create2(request);

        URI location = uriBuilder
                .path("/employees/{id}")
                .buildAndExpand(savedDto.getId())
                .toUri();

        return ResponseEntity.created(location).body(savedDto);
    }

    @GetMapping("/{id}/with-department")
    public ResponseEntity<EmployeeResponseDto> getByIdWithDepartment(@PathVariable Long id) {
        EmployeeResponseDto dto = employeeService.getByIdWithDepartment(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{id}")
    EmployeeResponseDto one(@PathVariable Long id) {
        return EmployeeMapper.toDto(employeeService.findById(id));
    }

    @GetMapping("/email/{email}")
    EmployeeResponseDto email(@PathVariable String email) {
        return EmployeeMapper.toDto(employeeService.findByEmail(email));
    }

    @PutMapping("/{id}")
    ResponseEntity<EmployeeResponseDto> replaceEmployee(
            @PathVariable Long id,
            @RequestBody EmployeeRequestDto newEmployee
    ) {
        Employee updated = employeeService.updateExisting(id, EmployeeMapper.toEntity(newEmployee));
        return ResponseEntity.ok(EmployeeMapper.toDto(updated));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteByIdOrThrow(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "List employees", description = "")
    @GetMapping
    public ResponseEntity<PagedResponse<EmployeeResponseDto>> listEmployees(
            @PageableDefault(size = 10)
            @SortDefault.SortDefaults({
                    @SortDefault(sort = "id", direction = Sort.Direction.DESC)
            })
            Pageable pageable,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) String nameContains,
            @RequestParam(required = false) String emailContains,
            @RequestParam(required = false) Integer minExperience,
            @RequestParam(required = false) Integer maxExperience,
            @RequestParam(required = false) LocalDate hiredAfter,
            @RequestParam(required = false) LocalDate hiredBefore
    ) {
        SortFieldValidator.validateSort(pageable.getSort());
        return ResponseEntity.ok(employeeService.listEmployees(
                pageable, role, nameContains, emailContains,
                minExperience, maxExperience, hiredAfter, hiredBefore));
    }

    @PatchMapping("/{id}/profile")
    public ResponseEntity<EmployeeResponseDto> updateOwnProfile(
            @PathVariable Long id,
            @RequestBody @Valid UpdateProfileRequest req
    ) {
        Employee updated = employeeService.updateProfile(id, req.name(), req.email());
        return ResponseEntity.ok(EmployeeMapper.toDto(updated));
    }
}

package org.exampl;


import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/departments")
public class DepartmentController {

  //  private final DepartmentRepository departmentRepository;
    private final DepartmentServiceImpl departmentService;

//    public DepartmentController(DepartmentRepository departmentRepository) {
//        this.departmentRepository = departmentRepository;
//    }

    DepartmentController(DepartmentServiceImpl departmentService) {
        this.departmentService = departmentService;
    }




    @GetMapping
    List<DepartmentResponseDto> all() {
        return departmentService.findAll()
                .stream()
                .map(DepartmentMapper::toDto)
                .toList();
    }

//    @GetMapping("/{id}")
//    public Department getDepartmentById(@PathVariable Long id) {
//        return departmentService.findById(id)
//                .orElseThrow(() -> new RuntimeException("Department not found"));
//    }


//    @GetMapping("/{id}")
//    public DepartmentResponseDto one(@PathVariable Long id) {
//        return DepartmentMapper.toDto(DepartmentService.findById(id));
//    }

    @GetMapping("/{id}")
    public DepartmentResponseDto one(@PathVariable Long id) {
        return DepartmentMapper.toDto(departmentService.findById(id));
    }






    // POST /employees -> 201 Created + Location: /employees/{id}
    @PostMapping
    ResponseEntity<DepartmentResponseDto> newDepartment(
            @Valid @RequestBody DepartmentRequestDto newDepartment,
            UriComponentsBuilder uriBuilder
    ) {
        Department saved = departmentService.create(DepartmentMapper.toEntity(newDepartment));
        DepartmentResponseDto departmentResponseDto = DepartmentMapper.toDto(saved);

        URI location = uriBuilder
                .path("/departments/{id}")
                .buildAndExpand(saved.getId())
                .toUri();

        return ResponseEntity.created(location).body(departmentResponseDto);
    }


//    @DeleteMapping("/{id}")
//    ResponseEntity<Void> deleteDepartment(@PathVariable Long id) {
//        departmentService.deleteByIdOrThrow(id);
//        return ResponseEntity.noContent().build();
//    }
//    @DeleteMapping("/{id}")
////    ResponseEntity<Void> deleteDepartment(@PathVariable Long id) {
//        departmentService.deleteByIdOrThrow(id);
//        // Returns 204 No Content on success
//        return ResponseEntity.noContent().build();
//    }

    @PutMapping("/{id}")
    public ResponseEntity<DepartmentResponseDto> update(
            @PathVariable Long id,
            @Valid @RequestBody DepartmentRequestDto dto
    ) {
        Department updated = departmentService.updateExisting(id, dto);
        return ResponseEntity.ok(DepartmentMapper.toDto(updated));
    }

//    @GetMapping("/{id}/employees")
////    public List<EmployeeResponseDto> getDepartmentEmployees(@PathVariable Long id) {
//        return departmentService.findEmployeesByDepartment(id)
//                .stream()
//                .map(EmployeeMapper::toDto)
//                .toList();
//    }
}


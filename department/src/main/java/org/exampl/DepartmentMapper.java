package org.exampl;

public class DepartmentMapper {
    private DepartmentMapper() {}

    public static Department toEntity(DepartmentRequestDto dto) {
        Department d = new Department();
        d.setName(dto.getName());
        d.setLocation(dto.getLocation());

        return d;
    }

    public static DepartmentResponseDto toDto(Department d) {
        return new DepartmentResponseDto(
                d.getId(),
                d.getName(),
                d.getLocation()
        );
    }
}

package ru.digitalhabbits.homework3.service;

import ru.digitalhabbits.homework3.model.DepartmentRequest;
import ru.digitalhabbits.homework3.model.DepartmentResponse;
import ru.digitalhabbits.homework3.model.DepartmentShortResponse;

import javax.annotation.Nonnull;
import java.util.List;

public interface DepartmentService {

    @Nonnull
    List<DepartmentShortResponse> findAllDepartments();

    @Nonnull
    DepartmentResponse getDepartment(@Nonnull Integer id);

    @Nonnull
    Integer createDepartment(@Nonnull DepartmentRequest request);

    @Nonnull
    DepartmentResponse updateDepartment(@Nonnull Integer id, @Nonnull DepartmentRequest request);

    void deleteDepartment(@Nonnull Integer id);

    void addPersonToDepartment(@Nonnull Integer departmentId, @Nonnull Integer personId);

    void removePersonFromDepartment(@Nonnull Integer departmentId, @Nonnull Integer personId);

    void closeDepartment(@Nonnull Integer id);
}

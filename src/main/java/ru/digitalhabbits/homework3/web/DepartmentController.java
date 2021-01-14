package ru.digitalhabbits.homework3.web;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.digitalhabbits.homework3.model.*;
import ru.digitalhabbits.homework3.service.DepartmentService;

import java.net.URI;
import java.util.List;

@Tag(name = "Department operations")
@RestController
@RequestMapping(value = "/api/v1/departments", consumes = "application/json", produces = "application/json")
@RequiredArgsConstructor
public class DepartmentController {
    private final DepartmentService departmentService;

    @ApiResponse(responseCode = "200", description = "Get all departments")
    @GetMapping
    public List<DepartmentShortResponse> departments() {
        return departmentService.findAllDepartments();
    }

    @ApiResponse(responseCode = "200", description = "Get department info and persons by Id")
    @GetMapping("/{id}")
    public DepartmentResponse department(@PathVariable Integer id) {
        return departmentService.getDepartment(id);
    }

    @ApiResponse(responseCode = "201", description = "Create new department")
    @PostMapping
    public ResponseEntity<Void> createDepartment(@RequestBody DepartmentRequest request) {
        final Integer id = departmentService.createDepartment(request);
        final URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();

        return ResponseEntity.created(uri).build();
    }

    @ApiResponse(responseCode = "200", description = "Update existing department")
    @PatchMapping("/{id}")
    public DepartmentResponse updateDepartment(@PathVariable Integer id, @RequestBody DepartmentRequest request) {
        return departmentService.updateDepartment(id, request);
    }

    @ApiResponse(responseCode = "204", description = "Delete department")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteDepartment(@PathVariable Integer id) {
        departmentService.deleteDepartment(id);
    }

    @ApiResponse(responseCode = "204", description = "Add person to department")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping("/{departmentId}/{personId}")
    public void addPersonToDepartment(@PathVariable Integer departmentId, @PathVariable Integer personId) {
        departmentService.addPersonToDepartment(departmentId, personId);
    }

    @ApiResponse(responseCode = "204", description = "Remove person from department")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{departmentId}/{personId}")
    public void removePersonFromDepartment(@PathVariable Integer departmentId, @PathVariable Integer personId) {
        departmentService.removePersonFromDepartment(departmentId, personId);
    }

    @ApiResponse(responseCode = "204", description = "Close department (not allowed to add new persons) and remove all existing persons from it")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping("/{id}/close")
    public void closeDepartment(@PathVariable Integer id) {
        departmentService.closeDepartment(id);
    }
}

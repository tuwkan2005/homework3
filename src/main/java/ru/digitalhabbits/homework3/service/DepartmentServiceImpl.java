package ru.digitalhabbits.homework3.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.digitalhabbits.homework3.dao.DepartmentDao;
import ru.digitalhabbits.homework3.dao.PersonDao;
import ru.digitalhabbits.homework3.domain.Department;
import ru.digitalhabbits.homework3.domain.Person;
import ru.digitalhabbits.homework3.exception.EntityDataConflictException;
import ru.digitalhabbits.homework3.model.DepartmentRequest;
import ru.digitalhabbits.homework3.model.DepartmentResponse;
import ru.digitalhabbits.homework3.model.DepartmentShortResponse;
import ru.digitalhabbits.homework3.model.PersonInfo;
import ru.digitalhabbits.homework3.utils.PersonUtils;

import javax.annotation.Nonnull;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl
        implements DepartmentService {

    private final DepartmentDao departmentDao;
    private final PersonDao personDao;

    @Nonnull
    @Override
    public List<DepartmentShortResponse> findAllDepartments() {

        List<Department> departmentList = departmentDao.findAll();

        return departmentList.stream()
                .map(department ->
                        new DepartmentShortResponse()
                                .setId(department.getId())
                                .setName(department.getName()))
                .collect(Collectors.toList());
    }

    @Nonnull
    @Override
    public DepartmentResponse getDepartment(@Nonnull Integer id) {

        Department department = departmentDao.findById(id);

        if (department == null) {
            throw new EntityNotFoundException("Department not found");
        }

        return buildDepartmentResponse(department);
    }

    @Nonnull
    @Override
    @Transactional
    public Integer createDepartment(@Nonnull DepartmentRequest request) {

        Department department = new Department()
                .setName(request.getName());

        return departmentDao.update(department).getId();
    }

    @Nonnull
    @Override
    @Transactional
    public DepartmentResponse updateDepartment(@Nonnull Integer id, @Nonnull DepartmentRequest request) {

        Department department = departmentDao.findById(id);

        if (department == null) {
            throw new EntityNotFoundException("Department not found");
        }

        department.setName(request.getName());

        Department updatedDepartment = departmentDao.update(department);

        return buildDepartmentResponse(updatedDepartment);
    }

    @Override
    @Transactional
    public void deleteDepartment(@Nonnull Integer id) {
        Department department = departmentDao.findById(id);

        if (department == null) {
            throw new EntityNotFoundException("Department not found");
        }

        personDao.bulkUpdateDepartment(null, department);

        departmentDao.delete(id);
    }

    @Override
    @Transactional
    public void addPersonToDepartment(@Nonnull Integer departmentId, @Nonnull Integer personId) {

        Department department = departmentDao.findById(departmentId);

        if (department == null) {
            throw new EntityNotFoundException("Department not found");
        }

        if (department.isClosed()) {
            throw new EntityDataConflictException("Department closed");
        }

        Person person = personDao.findById(personId);

        if (person == null) {
            throw new EntityNotFoundException("Person not found");
        }

        person.setDepartment(department);

        personDao.update(person);
    }

    @Override
    @Transactional
    public void removePersonFromDepartment(@Nonnull Integer departmentId, @Nonnull Integer personId) {

        Department department = departmentDao.findById(departmentId);

        if (department == null) {
            throw new EntityNotFoundException("Department not found");
        }

        department.getPersons().stream()
                .filter(p -> p.getId().equals(personId))
                .findFirst()
                .map(p -> p.setDepartment(null))
                .ifPresent(personDao::update);
    }

    @Override
    @Transactional
    public void closeDepartment(@Nonnull Integer id) {

        Department department = departmentDao.findById(id);

        if (department == null) {
            throw new EntityNotFoundException("Department not found");
        }

        personDao.bulkUpdateDepartment(null, department);

        department.setClosed(true);
        departmentDao.update(department);
    }

    @Nonnull
    private DepartmentResponse buildDepartmentResponse(@Nonnull Department department) {

        List<PersonInfo> personInfoList = department.getPersons()
                .stream().map(this::buildPersonInfo)
                .collect(Collectors.toList());

        return new DepartmentResponse()
                .setId(department.getId())
                .setName(department.getName())
                .setClosed(department.isClosed())
                .setPersons(personInfoList);
    }

    @Nonnull
    private PersonInfo buildPersonInfo(@Nonnull Person person) {

        return new PersonInfo()
                .setId(person.getId())
                .setFullName(PersonUtils.getFullName(person));
    }
}

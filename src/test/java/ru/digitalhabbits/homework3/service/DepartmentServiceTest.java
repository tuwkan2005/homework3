package ru.digitalhabbits.homework3.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.digitalhabbits.homework3.dao.DepartmentDao;
import ru.digitalhabbits.homework3.dao.PersonDao;
import ru.digitalhabbits.homework3.domain.Department;
import ru.digitalhabbits.homework3.domain.Person;
import ru.digitalhabbits.homework3.model.*;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.IntStream.range;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static ru.digitalhabbits.homework3.utils.DepartmentHelper.buildDepartment;
import static ru.digitalhabbits.homework3.utils.DepartmentHelper.buildDepartmentRequest;
import static ru.digitalhabbits.homework3.utils.PersonHelper.buildPerson;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = DepartmentServiceImpl.class)
class DepartmentServiceTest {

    private static final int COUNT = 3;

    @MockBean
    private DepartmentDao departmentDao;

    @MockBean
    private PersonDao personDao;

    @Autowired
    private DepartmentService departmentService;

    @Test
    void findAllDepartments() {
        final List<Department> departmentList = range(0, COUNT).mapToObj(i -> buildDepartment()).collect(Collectors.toList());

        when(departmentDao.findAll()).thenReturn(departmentList);

        final List<DepartmentShortResponse> DepartmentShortResponseList = departmentService.findAllDepartments();

        assertEquals(COUNT, departmentList.size());

        for (DepartmentShortResponse departmentShortResponse: DepartmentShortResponseList) {
            assert departmentList.stream().anyMatch(d -> d.getId().equals(departmentShortResponse.getId()));
        }
    }

    @Test
    void getDepartment() {
        final Department department = buildDepartment();

        when(departmentDao.findById(anyInt())).thenReturn(department);

        final DepartmentResponse departmentResponse = departmentService.getDepartment(department.getId());

        assertThat(departmentResponse).isEqualToComparingOnlyGivenFields(department,"id", "name", "closed");
    }

    @Test
    void createDepartment() {
        final DepartmentRequest departmentRequest = buildDepartmentRequest();
        final Department department = buildDepartment();

        when(departmentDao.update(any(Department.class))).thenReturn(department);

        final Integer id = departmentService.createDepartment(departmentRequest);

        assertEquals(id, department.getId());
    }

    @Test
    void updateDepartment() {
        final DepartmentRequest departmentRequest = buildDepartmentRequest();
        final Department department = buildDepartment();
        final Department updatedDepartment = new Department()
                .setId(department.getId())
                .setName(departmentRequest.getName())
                .setPersons(Collections.emptySet());

        when(departmentDao.findById(department.getId())).thenReturn(department);
        when(departmentDao.update(any(Department.class))).thenReturn(updatedDepartment);

        final DepartmentResponse departmentResponse = departmentService.updateDepartment(department.getId(), departmentRequest);

        assertThat(departmentResponse).isEqualToComparingOnlyGivenFields(updatedDepartment, "id", "name", "closed");
    }

    @Test
    void deleteDepartment() {

        final Department department = buildDepartment();

        when(departmentDao.findById(department.getId())).thenReturn(department);

        departmentService.deleteDepartment(department.getId());

        verify(personDao, times(1)).bulkUpdateDepartment(null, department);
        verify(departmentDao, times(1)).delete(department.getId());
    }

    @Test
    void addPersonToDepartment() {
        final Department department = buildDepartment();
        final Person person = buildPerson();

        when(departmentDao.findById(department.getId())).thenReturn(department);
        when(personDao.findById(person.getId())).thenReturn(person);
        when(personDao.update(any(Person.class))).thenReturn(person);

        departmentService.addPersonToDepartment(department.getId(), person.getId());

        verify(departmentDao, times(1)).findById(department.getId());
        verify(personDao, times(1)).findById(person.getId());
        verify(personDao, times(1)).update(person);
    }

    @Test
    void removePersonToDepartment() {

        final Set<Person> personList = range(0, COUNT).mapToObj(i -> buildPerson()).collect(Collectors.toSet());
        final Person person = buildPerson();
        personList.add(person);

        final Department department = buildDepartment().setPersons(personList);

        when(departmentDao.findById(department.getId())).thenReturn(department);
        when(personDao.update(any(Person.class))).thenReturn(person);

        departmentService.removePersonFromDepartment(department.getId(), person.getId());

        verify(departmentDao, times(1)).findById(department.getId());
        verify(personDao, times(1)).update(any(Person.class));
    }

    @Test
    void closeDepartment() {
        final Department department = buildDepartment();

        when(departmentDao.findById(department.getId())).thenReturn(department);

        departmentService.closeDepartment(department.getId());

        verify(personDao, times(1)).bulkUpdateDepartment(null, department);
        verify(departmentDao, times(1)).update(any(Department.class));
    }
}
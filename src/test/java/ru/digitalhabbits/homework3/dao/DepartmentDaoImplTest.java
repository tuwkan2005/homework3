package ru.digitalhabbits.homework3.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import ru.digitalhabbits.homework3.domain.Department;

import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@Transactional
@SpringBootTest
@AutoConfigureTestEntityManager
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class DepartmentDaoImplTest {

    private static final int COUNT = 3;

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private DepartmentDao departmentDao;

    private static List<Department> departments;

    @BeforeEach
    void init() {
        departments = generateDepartments(COUNT).stream()
                .map(entityManager::persist)
                .collect(toList());
    }


    @Test
    void findById() {
        Department department = departmentDao.findById(departments.get(0).getId());

        assertThat(department).isEqualToComparingFieldByField(departments.get(0));
    }

    @Test
    void findAll() {
        final List<Department> departmentList = departmentDao.findAll();
        assertEquals(departments.size(), departmentList.size());

        for (Department department : departmentList) {
            assert departments.stream().anyMatch(d -> d.getId().equals(department.getId()));
        }
    }

    @Test
    void update() {
        Department department = departmentDao.findById(departments.get(0).getId());

        Department updatedDepartment = generateDepartments(1).get(0).setId(department.getId());

        department.setName(updatedDepartment.getName());

        departmentDao.update(department);

        assertThat(updatedDepartment).isEqualToComparingFieldByField(departmentDao.findById(department.getId()));
    }

    @Test
    void delete() {
        Integer departmentId = departmentDao.findById(departments.get(0).getId()).getId();

        departmentDao.delete(departmentId);

        final List<Department> departmentList = departmentDao.findAll();

        assertEquals(departments.size() - 1, departmentList.size());

        assert departmentList.stream().noneMatch(p -> p.getId().equals(departmentId));
    }

    private List<Department> generateDepartments(int count) {
        return IntStream.range(0, count)
                .mapToObj(i -> new Department()
                        .setName(randomAlphabetic(7)))
                .collect(toList());
    }
}
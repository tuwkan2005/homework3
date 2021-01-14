package ru.digitalhabbits.homework3.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.digitalhabbits.homework3.dao.PersonDao;
import ru.digitalhabbits.homework3.domain.Department;
import ru.digitalhabbits.homework3.domain.Person;
import ru.digitalhabbits.homework3.model.DepartmentInfo;
import ru.digitalhabbits.homework3.model.PersonRequest;
import ru.digitalhabbits.homework3.model.PersonResponse;
import ru.digitalhabbits.homework3.utils.PersonUtils;

import javax.annotation.Nonnull;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PersonServiceImpl
        implements PersonService {

    private final PersonDao personDao;

    @Nonnull
    @Override
    public List<PersonResponse> findAllPersons() {

        List<Person> personList = personDao.findAll();

        return personList.stream()
                .map(this::buildPersonResponse)
                .collect(Collectors.toList());
    }

    @Nonnull
    @Override
    public PersonResponse getPerson(@Nonnull Integer id) {

        Person person = personDao.findById(id);

        if (person == null) {
            throw new EntityNotFoundException("Person not found");
        }

        return buildPersonResponse(person);
    }

    @Nonnull
    @Override
    @Transactional
    public Integer createPerson(@Nonnull PersonRequest request) {

        Person person = new Person()
                .setAge(request.getAge())
                .setFirstName(request.getFirstName())
                .setLastName(request.getLastName())
                .setMiddleName(request.getMiddleName());

        return personDao.update(person).getId();
    }

    @Nonnull
    @Override
    @Transactional
    public PersonResponse updatePerson(@Nonnull Integer id, @Nonnull PersonRequest request) {

        Person person = personDao.findById(id);

        if (person == null) {
            throw new EntityNotFoundException("Person not found");
        }

        person.setAge(request.getAge())
                .setFirstName(request.getFirstName())
                .setLastName(request.getLastName())
                .setMiddleName(request.getMiddleName());

        Person updatedPerson = personDao.update(person);

        return buildPersonResponse(updatedPerson);
    }

    @Override
    @Transactional
    public void deletePerson(@Nonnull Integer id) {

        personDao.delete(id);
    }

    private DepartmentInfo getDepartmentInfo(Department department) {

        if (department == null) {
            return null;
        }

        return new DepartmentInfo()
                .setId(department.getId())
                .setName(department.getName());
    }

    @Nonnull
    private PersonResponse buildPersonResponse(@Nonnull Person person) {
        return new PersonResponse()
                .setId(person.getId())
                .setAge(person.getAge())
                .setFullName(PersonUtils.getFullName(person))
                .setDepartment(getDepartmentInfo(person.getDepartment()));
    }
}

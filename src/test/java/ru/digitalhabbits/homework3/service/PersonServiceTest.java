package ru.digitalhabbits.homework3.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.digitalhabbits.homework3.dao.PersonDao;
import ru.digitalhabbits.homework3.domain.Person;
import ru.digitalhabbits.homework3.model.PersonRequest;
import ru.digitalhabbits.homework3.model.PersonResponse;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.IntStream.range;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static ru.digitalhabbits.homework3.utils.PersonHelper.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = PersonServiceImpl.class)
class PersonServiceTest {

    private static final int COUNT = 3;

    @MockBean
    private PersonDao personDao;

    @Autowired
    private PersonService personService;

    @Test
    void findAllPersons() {

        final List<Person> personList = range(0, COUNT).mapToObj(i -> buildPerson()).collect(Collectors.toList());

        when(personDao.findAll()).thenReturn(personList);

        final List<PersonResponse> personResponseList = personService.findAllPersons();

        assertEquals(COUNT, personResponseList.size());

        for (PersonResponse person: personResponseList) {
            assert personList.stream().anyMatch(p -> p.getId().equals(person.getId()));
        }
    }

    @Test
    void getPerson() {
        final Person person = buildPerson();

        when(personDao.findById(anyInt())).thenReturn(person);

        final PersonResponse personResponse = personService.getPerson(person.getId());

        assertThat(personResponse).isEqualToComparingOnlyGivenFields(person,"id", "age");
        assertEquals(personResponse.getFullName(), getFullName(person));
    }

    @Test
    void createPerson() {
        final PersonRequest personRequest = buildPersonRequest();
        final Person person = buildPerson();

        when(personDao.update(any(Person.class))).thenReturn(person);

        final Integer id = personService.createPerson(personRequest);

        assertEquals(id, person.getId());
    }

    @Test
    void updatePerson() {
        final PersonRequest personRequest = buildPersonRequest();
        final Person person = buildPerson();
        final Person updatedPerson = new Person()
                .setId(person.getId())
                .setFirstName(personRequest.getFirstName())
                .setMiddleName(personRequest.getMiddleName())
                .setLastName(personRequest.getLastName())
                .setAge(personRequest.getAge());

        when(personDao.findById(anyInt())).thenReturn(person);
        when(personDao.update(any(Person.class))).thenReturn(updatedPerson);

        final PersonResponse personResponse = personService.updatePerson(person.getId(), personRequest);

        assertThat(personResponse).isEqualToComparingOnlyGivenFields(updatedPerson, "id", "age");
        assertEquals(personResponse.getFullName(), getFullName(updatedPerson));
    }

    @Test
    void deletePerson() {
        final Person person = buildPerson();

        when(personDao.delete(person.getId())).thenReturn(person);

        personService.deletePerson(person.getId());

        verify(personDao, times(1)).delete(person.getId());
    }
}
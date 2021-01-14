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
import ru.digitalhabbits.homework3.domain.Person;

import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomUtils.nextInt;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@Transactional
@SpringBootTest
@AutoConfigureTestEntityManager
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class PersonDaoTest {

    private static final int COUNT = 3;

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PersonDao personDao;

    private static List<Person> people;

    @BeforeEach
    void init() {
        people = generatePersons(COUNT).stream()
                .map(entityManager::persist)
                .collect(toList());
    }


    @Test
    void findById() {

        Person person = personDao.findById(people.get(0).getId());

        assertThat(person).isEqualToComparingFieldByField(people.get(0));
    }

    @Test
    void findAll() {

        final List<Person> personList = personDao.findAll();
        assertEquals(people.size(), personList.size());

        for (Person person: personList) {
            assert people.stream().anyMatch(p -> p.getId().equals(person.getId()));
        }
    }

    @Test
    void update() {

        Person person = personDao.findById(people.get(0).getId());

        Person updatedPerson = generatePersons(1).get(0).setId(person.getId());

        person.setAge(updatedPerson.getAge());
        person.setFirstName(updatedPerson.getFirstName());
        person.setMiddleName(updatedPerson.getMiddleName());
        person.setLastName(updatedPerson.getLastName());

        personDao.update(person);

        assertThat(updatedPerson).isEqualToComparingFieldByField(personDao.findById(person.getId()));
    }

    @Test
    void delete() {

        final Integer personId = personDao.findById(people.get(0).getId()).getId();

        personDao.delete(personId);

        final List<Person> personList = personDao.findAll();

        assertEquals(people.size() - 1, personList.size());

        assert personList.stream().noneMatch(p-> p.getId().equals(personId));
    }


    private List<Person> generatePersons(int count) {
        return IntStream.range(0, count)
                .mapToObj(i -> new Person()
                        .setFirstName(randomAlphabetic(7))
                        .setMiddleName(randomAlphabetic(7))
                        .setLastName(randomAlphabetic(7))
                        .setAge(nextInt(10, 50)))
                .collect(toList());
    }
}
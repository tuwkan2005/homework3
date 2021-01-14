package ru.digitalhabbits.homework3.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.digitalhabbits.homework3.domain.Person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.digitalhabbits.homework3.utils.PersonHelper.buildPerson;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = PersonUtils.class)
class PersonUtilsTest {

    @Test
    void getFullName() {
        Person person = buildPerson();
        Person personWithoutMiddleName = buildPerson().setMiddleName(null);

        String fullName = String.format("%s %s %s", person.getFirstName(), person.getMiddleName(), person.getLastName());
        String fullNameWithoutMiddleName = String.format("%s %s", personWithoutMiddleName.getFirstName(), personWithoutMiddleName.getLastName());

        assertEquals(fullName, PersonUtils.getFullName(person));
        assertEquals(fullNameWithoutMiddleName, PersonUtils.getFullName(personWithoutMiddleName));
    }
}
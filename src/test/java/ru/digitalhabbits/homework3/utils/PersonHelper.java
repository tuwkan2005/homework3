package ru.digitalhabbits.homework3.utils;

import ru.digitalhabbits.homework3.domain.Person;
import ru.digitalhabbits.homework3.model.PersonInfo;
import ru.digitalhabbits.homework3.model.PersonRequest;
import ru.digitalhabbits.homework3.model.PersonResponse;

import javax.validation.constraints.NotNull;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomUtils.nextInt;
import static ru.digitalhabbits.homework3.utils.DepartmentHelper.buildDepartmentInfo;

public class PersonHelper {

    public static Person buildPerson() {
        return new Person()
                .setId(nextInt())
                .setFirstName(randomAlphabetic(7))
                .setMiddleName(randomAlphabetic(7))
                .setLastName(randomAlphabetic(7))
                .setAge(nextInt(10, 50));
    }

    public static PersonRequest buildPersonRequest() {
        return new PersonRequest()
                .setFirstName(randomAlphabetic(7))
                .setMiddleName(randomAlphabetic(7))
                .setLastName(randomAlphabetic(7))
                .setAge(nextInt(10, 50));
    }

    public static PersonResponse buildPersonResponse() {
        return new PersonResponse()
                .setId(nextInt())
                .setFullName(String.format("%s %s %s", randomAlphabetic(7), randomAlphabetic(7), randomAlphabetic(7)))
                .setAge(nextInt(10, 50))
                .setDepartment(buildDepartmentInfo());
    }

    public static PersonResponse buildPersonResponseFromRequest(PersonRequest request) {
        return new PersonResponse()
                .setId(nextInt())
                .setFullName(String.format("%s %s %s", request.getFirstName(), request.getMiddleName(), request.getLastName()))
                .setAge(request.getAge());
    }

    public static PersonInfo buildPersonInfo() {
        return new PersonInfo()
                .setId(nextInt())
                .setFullName(String.format("%s %s %s", randomAlphabetic(7), randomAlphabetic(7), randomAlphabetic(7)));
    }

    public static String getFullName(@NotNull Person person) {
        return String.format("%s %s %s", person.getFirstName(), person.getMiddleName(), person.getLastName());
    }
}

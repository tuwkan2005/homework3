package ru.digitalhabbits.homework3.utils;

import ru.digitalhabbits.homework3.domain.Department;
import ru.digitalhabbits.homework3.model.DepartmentInfo;
import ru.digitalhabbits.homework3.model.DepartmentRequest;
import ru.digitalhabbits.homework3.model.DepartmentResponse;
import ru.digitalhabbits.homework3.model.DepartmentShortResponse;

import java.util.Collections;
import java.util.List;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomUtils.nextBoolean;
import static org.apache.commons.lang3.RandomUtils.nextInt;
import static ru.digitalhabbits.homework3.utils.PersonHelper.buildPersonInfo;

public class DepartmentHelper {

    public static Department buildDepartment() {
        return new Department()
                .setId(nextInt())
                .setName(randomAlphabetic(7))
                .setPersons(Collections.emptySet());
    }

    public static DepartmentRequest buildDepartmentRequest() {
        return new DepartmentRequest()
                .setName(randomAlphabetic(7));
    }

    public static DepartmentInfo buildDepartmentInfo() {
        return new DepartmentInfo()
                .setId(nextInt())
                .setName(randomAlphabetic(7));
    }

    public static DepartmentShortResponse buildDepartmentShortResponse() {
        return new DepartmentShortResponse()
                .setId(nextInt())
                .setName(randomAlphabetic(7));
    }

    public static DepartmentResponse buildDepartmentResponse() {
        return new DepartmentResponse()
                .setId(nextInt())
                .setName(randomAlphabetic(7))
                .setClosed(nextBoolean())
                .setPersons(List.of(buildPersonInfo()));

    }

    public static DepartmentResponse buildDepartmentResponseFromRequest(DepartmentRequest request) {
        return new DepartmentResponse()
                .setId(nextInt())
                .setName(request.getName())
                .setClosed(false)
                .setPersons(Collections.emptyList());

    }
}

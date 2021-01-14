package ru.digitalhabbits.homework3.utils;

import ru.digitalhabbits.homework3.domain.Person;

import javax.annotation.Nonnull;

public class PersonUtils {

    @Nonnull
    public static String getFullName(@Nonnull Person person) {

        StringBuilder fullName = new StringBuilder();

        fullName.append(person.getFirstName())
                .append(" ");

        if (person.getMiddleName() != null) {
            fullName.append(person.getMiddleName());
            fullName.append(" ");
        }

        fullName.append(person.getLastName());

        return fullName.toString();
    }
}

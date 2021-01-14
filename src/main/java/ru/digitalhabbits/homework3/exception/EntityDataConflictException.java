package ru.digitalhabbits.homework3.exception;

import javax.persistence.PersistenceException;

public class EntityDataConflictException extends PersistenceException {

    public EntityDataConflictException(String message) {
        super(message);
    }
}

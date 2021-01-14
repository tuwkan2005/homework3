package ru.digitalhabbits.homework3.dao;

import org.springframework.stereotype.Repository;
import ru.digitalhabbits.homework3.domain.Department;
import ru.digitalhabbits.homework3.domain.Person;

import javax.annotation.Nonnull;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class PersonDaoImpl
        implements PersonDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Person findById(@Nonnull Integer id) {
        return entityManager.find(Person.class, id);
    }

    @Override
    public List<Person> findAll() {
        return entityManager.createQuery("SELECT p FROM Person p", Person.class).getResultList();
    }

    @Override
    public Person update(Person entity) {
        entityManager.persist(entity);
        return entity;
    }

    @Override
    public Person delete(Integer integer) {
        Person person = findById(integer);

        if (person != null) {
            entityManager.remove(person);
        }
        return person;
    }

    @Override
    public void bulkUpdateDepartment(Department newDepartment, Department oldDepartment) {
        entityManager.createQuery("UPDATE Person SET department = :newDepartment WHERE department = :oldDepartment")
                .setParameter("newDepartment", newDepartment)
                .setParameter("oldDepartment", oldDepartment)
                .executeUpdate();
    }
}

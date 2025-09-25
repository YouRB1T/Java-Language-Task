package com.phonebook.repository;

import com.phonebook.model.entity.Person;

import java.util.List;
import java.util.UUID;

public interface PersonRepository {
    void createPerson(Person person);
    Person findPersonId(UUID id);
    Person findPersonNoId(String lastName, String number);
    Person findPerson(String number);
    List<Person> findAllPersons();
    void updatePerson(Person person);
    void deletePerson(UUID id);
}

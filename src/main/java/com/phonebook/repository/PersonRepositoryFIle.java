package com.phonebook.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phonebook.model.entity.Person;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class PersonRepositoryFIle implements PersonRepository{
    private final String FILENAME = "data/db.json";
    private final File file;
    private final ObjectMapper mapper;
    private final TypeReference<List<Person>> typeRef;

    public PersonRepositoryFIle() throws IOException {
        file = new File(FILENAME);
        file.createNewFile();

        mapper = new ObjectMapper();
        typeRef = new TypeReference<List<Person>>(){};
    }

    private void savePersons(List<Person> persons) throws IOException {
        mapper.writeValue(file, persons);
    }

    private List<Person> loadPersons() throws IOException {
        return mapper.readValue(file, typeRef);
    }

    @Override
    public void createPerson(Person person) {
        try {
            List<Person> personList = loadPersons();
            personList.add(person);
            savePersons(personList);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Person findPersonId(UUID id) {
        try {
            return loadPersons().stream().filter(
                    person -> person.getId().equals(id)).findFirst()
                    .orElseThrow();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Person findPersonNoId(String lastName, String number) {
        try {
            return loadPersons()
                    .stream().filter(
                            person -> person.getLastName().equals(lastName)
                            && person.getNumbers().contains(number)
                    ).findFirst().orElseThrow();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Person findPerson(String number) {
        try {
            return loadPersons().stream().filter(
                    person -> person.getNumbers().contains(number)
            ).findFirst().orElseThrow();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Person> findAllPersons() {
        try {
            return loadPersons();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updatePerson(Person person) {
        try {
            List<Person> updated = loadPersons().stream()
                    .map(person1 -> person1.getId().equals(person.getId()) ? person : person1)
                    .collect(Collectors.toList());
            savePersons(updated);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deletePerson(UUID id) {
        try {
            savePersons(
                    loadPersons().stream().filter(
                            person -> person.getId() != id
                    ).toList()
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

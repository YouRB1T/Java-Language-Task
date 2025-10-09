package com.phonebook.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phonebook.model.entity.Person;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
        return null;
    }

    @Override
    public Person findPersonNoId(String lastName, String number) {
        return null;
    }

    @Override
    public Person findPerson(String number) {
        return null;
    }

    @Override
    public List<Person> findAllPersons() {
        return List.of();
    }

    @Override
    public void updatePerson(Person person) {

    }

    @Override
    public void deletePerson(UUID id) {

    }
}

package com.phonebook.service;

import com.phonebook.model.entity.Person;
import com.phonebook.repository.PersonRepository;
import com.phonebook.repository.PersonRepositoryImpl;

import java.lang.reflect.Array;
import java.util.*;

public class PersonGenerator {
    private static final Random random = new Random();
    private static final String[] FIRST_NAMES = {
            "Ivan", "Petr", "Sergey", "Dmitry", "Nikolay", "Anna", "Olga", "Maria", "Svetlana", "Elena"
    };
    private static final String[] LAST_NAMES = {
            "Ivanov", "Petrov", "Sidorov", "Smirnov", "Kuznetsov", "Popov", "Sokolov", "Lebedev", "Kozlov", "Novikov"
    };

    public static String generateFirstName() {
        return FIRST_NAMES[random.nextInt(FIRST_NAMES.length)];
    }

    public static String generateLastName() {
        return LAST_NAMES[random.nextInt(LAST_NAMES.length)];
    }

    public static Person generatePerson() {
        Person person = new Person(
                UUID.randomUUID(),
                generateFirstName(),
                generateLastName(),
                NumberGenerator.generateNumbers(3)
        );
        return person;
    }

    public static void main(String[] args) {
        PersonRepository repository = new PersonRepositoryImpl();
        repository.createPerson(generatePerson());
    }
}

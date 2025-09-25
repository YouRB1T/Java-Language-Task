package com.phonebook.repository;

import com.phonebook.model.entity.Number;
import com.phonebook.model.entity.Person;

import java.util.List;

public interface NumberRepository {
    void createNumber(Number number, Person person);
    List<Number> findNumbers(Person person);
    void deleteNumber(String number);
}

package com.phonebook.service;

import com.phonebook.model.dto.request.DtoAddNumberRequest;
import com.phonebook.model.dto.request.DtoDeleteNumberRequest;
import com.phonebook.model.dto.response.DtoAddNumberResponse;
import com.phonebook.model.dto.response.DtoDeleteNumberResponse;
import com.phonebook.model.entity.*;
import com.phonebook.model.entity.Number;
import com.phonebook.repository.NumberRepository;
import com.phonebook.repository.NumberRepositoryImpl;
import com.phonebook.repository.PersonRepository;
import com.phonebook.repository.PersonRepositoryImpl;

import java.util.List;
import java.util.UUID;

public class NumberServiceImpl implements NumberService {

    private final NumberRepository numberRepository = new NumberRepositoryImpl();
    private final PersonRepository personRepository = new PersonRepositoryImpl();

    @Override
    public DtoAddNumberResponse addNumber(DtoAddNumberRequest request) {

        Person person = personRepository.findPersonId(request.getPersonId());
        if (person == null) {
            throw new RuntimeException("Person not found");
        }

        Number number = new Number(
                request.getId() != null ? request.getId() : UUID.randomUUID(),
                request.getNumber()
        );

        numberRepository.createNumber(number, person);

        List<Number> numbers = numberRepository.findNumbers(person);
        person.setNumbers(numbers);

        personRepository.updatePerson(person);

        return new DtoAddNumberResponse(new DtoAddNumberResponse.PersonData(
                person.getId(),
                person.getFirstName(),
                person.getLastName(),
                new DtoAddNumberResponse.Numbers(
                        numbers.size() > 0 ? numbers.get(0).getNumber() : null,
                        numbers.size() > 1 ? numbers.get(1).getNumber() : null,
                        numbers.size() > 2 ? numbers.get(2).getNumber() : null
                )
        ));
    }

    @Override
    public DtoDeleteNumberResponse deleteNumber(DtoDeleteNumberRequest request) {

        numberRepository.deleteNumber(request.getNumber());
        Person person = personRepository.findPerson(request.getNumber());

        return new DtoDeleteNumberResponse(new DtoDeleteNumberResponse.PersonData(
                person.getId(),
                person.getFirstName(),
                person.getLastName(),
                request.getNumber()
        ));
    }
}


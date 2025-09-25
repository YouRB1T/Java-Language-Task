package com.phonebook.service;

import com.phonebook.model.dto.request.DtoCreatPersonRequest;
import com.phonebook.model.dto.request.DtoDeletePersonRequest;
import com.phonebook.model.dto.request.DtoFindPersonRequest;
import com.phonebook.model.dto.request.DtoUpdatePersonRequest;
import com.phonebook.model.dto.response.*;
import com.phonebook.model.entity.*;
import com.phonebook.model.entity.Number;
import com.phonebook.repository.NumberRepository;
import com.phonebook.repository.NumberRepositoryImpl;
import com.phonebook.repository.PersonRepository;
import com.phonebook.repository.PersonRepositoryImpl;

import java.util.*;
import java.util.stream.Collectors;

public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository = new PersonRepositoryImpl();
    private final NumberRepository numberRepository = new NumberRepositoryImpl();

    @Override
    public DtoCreatePersonResponse createPerson(DtoCreatPersonRequest request) {
        Person person = new Person(
                UUID.randomUUID(),
                request.getPersonData().getFirstName(),
                request.getPersonData().getLastName(),
                new ArrayList<>()
        );

        List<String> nums = Arrays.asList(
                request.getPersonData().getNumbers().getNumber1(),
                request.getPersonData().getNumbers().getNumber2(),
                request.getPersonData().getNumbers().getNumber3()
        );

        for (String num : nums) {
            if (num != null) {
                com.phonebook.model.entity.Number number = new com.phonebook.model.entity.Number(UUID.randomUUID(), num);
                person.getNumbers().add(number);
                numberRepository.createNumber(number, person);
            }
        }

        personRepository.createPerson(person);

        return new DtoCreatePersonResponse(new DtoCreatePersonResponse.PersonData(
                person.getId(),
                person.getFirstName(),
                person.getLastName(),
                new com.phonebook.model.dto.response.DtoCreatePersonResponse.Numbers(
                        person.getNumbers().size() > 0 ? person.getNumbers().get(0).getNumber() : null,
                        person.getNumbers().size() > 1 ? person.getNumbers().get(1).getNumber() : null,
                        person.getNumbers().size() > 2 ? person.getNumbers().get(2).getNumber() : null)
                )
        );
    }

    @Override
    public DtoFindPersonResponse findPersonId(DtoFindPersonRequest request) {
        Person person = personRepository.findPersonId(request.getId());
        if (person == null) return null;
        return new DtoFindPersonResponse(new DtoFindPersonResponse.PersonData(
                person.getId(),
                person.getFirstName(),
                person.getLastName(),
                new com.phonebook.model.dto.response.DtoFindPersonResponse.Numbers(
                        person.getNumbers().size() > 0 ? person.getNumbers().get(0).getNumber() : null,
                        person.getNumbers().size() > 1 ? person.getNumbers().get(1).getNumber() : null,
                        person.getNumbers().size() > 2 ? person.getNumbers().get(2).getNumber() : null
                )
        ));
    }

    @Override
    public DtoFindPersonResponse findPersonNoId(String lastName, String number) {
        Person person = personRepository.findPersonNoId(lastName, number);
        return new DtoFindPersonResponse(new DtoFindPersonResponse.PersonData(
                person.getId(),
                person.getFirstName(),
                person.getLastName(),
                new com.phonebook.model.dto.response.DtoFindPersonResponse.Numbers(
                        person.getNumbers().size() > 0 ? person.getNumbers().get(0).getNumber() : null,
                        person.getNumbers().size() > 1 ? person.getNumbers().get(1).getNumber() : null,
                        person.getNumbers().size() > 2 ? person.getNumbers().get(2).getNumber() : null
                )
        ));
    }

    @Override
    public DtoUpdatePersonResponse updatePerson(DtoUpdatePersonRequest request) {
        Person person = new Person(
                request.getPersonData().getId(),
                request.getPersonData().getFirstName(),
                request.getPersonData().getLastName(),
                new ArrayList<>()
        );

        List<String> nums = Arrays.asList(
                request.getPersonData().getNumbers().getNumber1(),
                request.getPersonData().getNumbers().getNumber2(),
                request.getPersonData().getNumbers().getNumber3()
        );

        for (String num : nums) {
            if (num != null) {
                person.getNumbers().add(new Number(UUID.randomUUID(), num));
            }
        }

        personRepository.updatePerson(person);
        return new DtoUpdatePersonResponse(new DtoUpdatePersonResponse.PersonData(
                person.getId(),
                person.getFirstName(),
                person.getLastName(),
                new com.phonebook.model.dto.response.DtoUpdatePersonResponse.Numbers(
                        person.getNumbers().size() > 0 ? person.getNumbers().get(0).getNumber() : null,
                        person.getNumbers().size() > 1 ? person.getNumbers().get(1).getNumber() : null,
                        person.getNumbers().size() > 2 ? person.getNumbers().get(2).getNumber() : null
                )
        ));
    }

    @Override
    public DtoDeletePersonResponse deletePerson(DtoDeletePersonRequest request) {
        Person person = personRepository.findPersonId(request.getId());
        personRepository.deletePerson(request.getId());
        return new DtoDeletePersonResponse(new DtoDeletePersonResponse.PersonData(
               person.getId(),
                person.getFirstName(),
                person.getLastName(),
                new DtoDeletePersonResponse.Numbers(
                        person.getNumbers().size() > 0 ? person.getNumbers().get(0).getNumber() : null,
                        person.getNumbers().size() > 1 ? person.getNumbers().get(1).getNumber() : null,
                        person.getNumbers().size() > 2 ? person.getNumbers().get(2).getNumber() : null
                )
        ));
    }

    @Override
    public DtoFindPersonsResponse findPersons() {
        List<Person> persons = personRepository.findAllPersons();

        List<DtoFindPersonsResponse.PersonData> sorted = persons.stream()
                .sorted(Comparator.comparing((Person p) -> p.getLastName())
                        .thenComparing(p -> p.getFirstName())
                        .thenComparing(p -> p.getNumbers().stream()
                                .map(Number::getNumber)
                                .sorted()
                                .collect(Collectors.joining())))
                .map(p -> {
                    DtoFindPersonsResponse.PersonDetails.Numbers nums = new DtoFindPersonsResponse.PersonDetails.Numbers(
                            p.getNumbers().size() > 0 ? p.getNumbers().get(0).getNumber() : null,
                            p.getNumbers().size() > 1 ? p.getNumbers().get(1).getNumber() : null,
                            p.getNumbers().size() > 2 ? p.getNumbers().get(2).getNumber() : null
                    );
                    DtoFindPersonsResponse.PersonDetails details = new DtoFindPersonsResponse.PersonDetails(
                            p.getId(),
                            p.getFirstName(),
                            p.getLastName(),
                            nums
                    );
                    return new DtoFindPersonsResponse.PersonData(details);
                })
                .collect(Collectors.toList());

        return new DtoFindPersonsResponse(sorted);
    }
}


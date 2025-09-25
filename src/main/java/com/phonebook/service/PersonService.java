package com.phonebook.service;

import com.phonebook.model.dto.request.DtoCreatPersonRequest;
import com.phonebook.model.dto.request.DtoDeletePersonRequest;
import com.phonebook.model.dto.request.DtoFindPersonRequest;
import com.phonebook.model.dto.request.DtoUpdatePersonRequest;
import com.phonebook.model.dto.response.*;

public interface PersonService {
    DtoCreatePersonResponse createPerson(DtoCreatPersonRequest request);
    DtoFindPersonResponse findPersonId(DtoFindPersonRequest request);
    DtoFindPersonResponse findPersonNoId(String lastName, String number);
    DtoUpdatePersonResponse updatePerson(DtoUpdatePersonRequest request);
    DtoDeletePersonResponse deletePerson(DtoDeletePersonRequest request);
    DtoFindPersonsResponse findPersons();

}

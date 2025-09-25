package com.phonebook.service;

import com.phonebook.model.dto.request.DtoAddNumberRequest;
import com.phonebook.model.dto.request.DtoDeleteNumberRequest;
import com.phonebook.model.dto.response.DtoAddNumberResponse;
import com.phonebook.model.dto.response.DtoDeleteNumberResponse;

public interface NumberService {
    DtoAddNumberResponse addNumber(DtoAddNumberRequest request);
    DtoDeleteNumberResponse deleteNumber(DtoDeleteNumberRequest request);
}

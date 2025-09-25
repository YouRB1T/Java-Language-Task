package com.phonebook.model.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class Person {
    private UUID id;
    private String firstName;
    private String lastName;
    private List<Number> numbers;
}

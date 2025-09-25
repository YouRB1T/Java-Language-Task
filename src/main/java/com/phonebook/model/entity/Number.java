package com.phonebook.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class Number {
    private UUID id;
    private String number;
}

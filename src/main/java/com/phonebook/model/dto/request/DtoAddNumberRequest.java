package com.phonebook.model.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DtoAddNumberRequest {
    private String number;
    @JsonProperty("person_id")
    private UUID personId;
}

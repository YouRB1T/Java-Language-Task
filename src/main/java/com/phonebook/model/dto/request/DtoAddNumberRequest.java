package com.phonebook.model.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class DtoAddNumberRequest {
    private UUID id;
    private String number;
    @JsonProperty("person_id")
    private UUID personId;
}

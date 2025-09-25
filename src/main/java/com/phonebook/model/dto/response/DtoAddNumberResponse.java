package com.phonebook.model.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DtoAddNumberResponse {
    @JsonProperty("person_data")
    private PersonData personData;
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PersonData {
        private UUID id;
        private String firstName;
        private String lastName;
        private Numbers numbers;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Numbers {
        private String number1;
        private String number2;
        private String number3;

    }
}

package com.phonebook.model.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DtoFindPersonResponse {
    @JsonProperty("person_data")
    private PersonData personData;

    @Data
    @AllArgsConstructor
    public static class PersonData {
        private UUID id;
        @JsonProperty("first_name")
        private String firstName;
        @JsonProperty("last_name")
        private String lastName;
        private Numbers numbers;
    }

    @Data
    @AllArgsConstructor
    public static class Numbers {
        private String number1;
        private String number2;
        private String number3;
    }
}

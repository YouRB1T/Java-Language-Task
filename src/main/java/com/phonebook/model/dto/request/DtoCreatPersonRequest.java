package com.phonebook.model.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class DtoCreatPersonRequest {
    @JsonProperty("person_data")
    private PersonData personData;

    @Data
    public static class PersonData {
        @JsonProperty("first_name")
        private String firstName;
        @JsonProperty("last_name")
        private String lastName;
        private Numbers numbers;

        public UUID getId() {
            return null;
        }
    }

    @Data
    public static class Numbers {
        private String number1;
        private String number2;
        private String number3;
    }
}

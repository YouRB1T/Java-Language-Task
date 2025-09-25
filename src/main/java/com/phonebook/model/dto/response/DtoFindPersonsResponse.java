package com.phonebook.model.dto.response;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DtoFindPersonsResponse {

    @JsonProperty("person_list")
    private List<PersonData> personList;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PersonData {
        @JsonProperty("person_data")
        private PersonDetails personData;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PersonDetails {
        private UUID id;

        @JsonProperty("first_name")
        private String firstName;

        @JsonProperty("last_name")
        private String lastName;

        private Numbers numbers;

        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        public static class Numbers {
            private String number1;
            private String number2;
            private String number3;
        }
    }
}

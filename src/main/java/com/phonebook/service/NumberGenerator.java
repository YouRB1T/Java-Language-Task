package com.phonebook.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import com.phonebook.model.entity.Number;

public class NumberGenerator {
    private static final Random random = new Random();

    public static Number generateNumber() {
        StringBuilder sb = new StringBuilder("+79");
        for (int i = 0; i < 9; i++) {
            sb.append(random.nextInt(10));
        }
        return new Number(UUID.randomUUID(), sb.toString());
    }

    public static List<Number> generateNumbers(int count) {
        List<Number> numbers = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            numbers.add(generateNumber());
        }
        return numbers;
    }

}

package com.phonebook.repository;

import com.phonebook.config.DataBase;
import com.phonebook.model.entity.Number;
import com.phonebook.model.entity.Person;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class NumberRepositoryImpl implements NumberRepository {

    @Override
    public void createNumber(Number number, Person person) {
        String sql = "INSERT INTO numbers (id, number, person_id) VALUES (?, ?, ?)";

        try (Connection conn = DataBase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setObject(1, number.getId());
            ps.setString(2, number.getNumber());
            ps.setObject(3, person.getId());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Number> findNumbers(Person person) {
        String sql = "SELECT id, number FROM numbers WHERE person_id = ?";
        List<Number> numbers = new ArrayList<>();

        try (Connection conn = DataBase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setObject(1, person.getId());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                numbers.add(new Number(
                        (UUID) rs.getObject("id"),
                        rs.getString("number")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return numbers;
    }

    @Override
    public void deleteNumber(String number) {
        String sql = "DELETE FROM numbers WHERE number = ?";

        try (Connection conn = DataBase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, number);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


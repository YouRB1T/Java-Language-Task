package com.phonebook.repository;

import com.phonebook.config.DataBase;
import com.phonebook.model.entity.Number;
import com.phonebook.model.entity.Person;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class PersonRepositoryImpl implements PersonRepository{
    @Override
    public void createPerson(Person person) {
        String insertPerson = "INSERT INTO persons (id, first_name, last_name) VALUES (?, ?, ?)";
        String insertNumber = "INSERT INTO numbers (id, number, person_id) VALUES (?, ?, ?)";

        try (Connection conn = DataBase.getConnection();
             PreparedStatement psPerson = conn.prepareStatement(insertPerson);
             PreparedStatement psNumber = conn.prepareStatement(insertNumber)) {

            conn.setAutoCommit(false);

            psPerson.setObject(1, person.getId());
            psPerson.setString(2, person.getFirstName());
            psPerson.setString(3, person.getLastName());
            psPerson.executeUpdate();

            for (Number num : person.getNumbers()) {
                psNumber.setObject(1, num.getId());
                psNumber.setString(2, num.getNumber());
                psNumber.setObject(3, person.getId());
                psNumber.executeUpdate();
            }

            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Person findPersonId(UUID id) {
        String sql = "SELECT p.id, p.first_name, p.last_name, n.id AS number_id, n.number " +
                "FROM persons p LEFT JOIN numbers n ON p.id = n.person_id " +
                "WHERE p.id = ?";

        try (Connection conn = DataBase.getConnection();
             PreparedStatement psPerson = conn.prepareStatement(sql)) {

            psPerson.setObject(1, id);
            ResultSet rs = psPerson.executeQuery();

            Person person = null;
            List<Number> nums = new ArrayList<>();

            while (rs.next()) {
                if (person == null) {
                    person = new Person(
                            (UUID) rs.getObject("id"),
                            rs.getString("first_name"),
                            rs.getString("last_name"),
                            nums
                    );
                }
                UUID numberId = (UUID) rs.getObject("number_id");
                String numberValue = rs.getString("number");

                if (numberValue != null) {
                    nums.add(new Number(numberId, numberValue));
                }
            }

            return person;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Person findPersonNoId(String lastName, String number) {
        String sql = "SELECT p.id AS person_id, p.first_name, p.last_name, " +
                "n.id AS number_id, n.number " +
                "FROM persons p " +
                "JOIN numbers n ON p.id = n.person_id " +
                "WHERE p.last_name = ? AND n.number = ?";

        try (Connection conn = DataBase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, lastName);
            ps.setString(2, number);

            ResultSet rs = ps.executeQuery();

            Person person = null;
            List<Number> numbers = new ArrayList<>();

            while (rs.next()) {
                if (person == null) {
                    person = new Person(
                            (UUID) rs.getObject("person_id"),
                            rs.getString("first_name"),
                            rs.getString("last_name"),
                            numbers
                    );
                }
                numbers.add(new Number(
                        (UUID) rs.getObject("number_id"),
                        rs.getString("number")
                ));
            }

            return person;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Person findPerson(String number) {
        String sql = "SELECT p.id AS person_id, p.first_name, p.last_name, " +
                "n.id AS number_id, n.number " +
                "FROM persons p " +
                "JOIN numbers n ON p.id = n.person_id " +
                "WHERE p.id = (SELECT person_id FROM numbers WHERE number = ?)";

        Person person = null;
        try (Connection conn = DataBase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, number);
            ResultSet rs = ps.executeQuery();
            List<Number> numbers = new ArrayList<>();

            while (rs.next()) {
                if (person == null) {
                    person = new Person(
                            (UUID) rs.getObject("person_id"),
                            rs.getString("first_name"),
                            rs.getString("last_name"),
                            numbers
                    );
                }
                UUID numberId = (UUID) rs.getObject("number_id");
                String numStr = rs.getString("number");
                if (numberId != null && numStr != null) {
                    numbers.add(new Number(numberId, numStr));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return person;
    }


    @Override
    public List<Person> findAllPersons() {
        String sql = "SELECT p.id AS person_id, p.first_name, p.last_name, " +
                "n.id AS number_id, n.number " +
                "FROM persons p " +
                "LEFT JOIN numbers n ON p.id = n.person_id";

        Map<UUID, Person> personMap = new HashMap<>();
        try (Connection conn = DataBase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                UUID personId = (UUID) rs.getObject("person_id");
                Person person = personMap.get(personId);

                if (person == null) {
                    person = new Person(
                            personId,
                            rs.getString("first_name"),
                            rs.getString("last_name"),
                            new ArrayList<>()
                    );
                    personMap.put(personId, person);
                }

                UUID numberId = (UUID) rs.getObject("number_id");
                String numberStr = rs.getString("number");
                if (numberId != null && numberStr != null) {
                    person.getNumbers().add(new Number(numberId, numberStr));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new ArrayList<>(personMap.values());
    }

    @Override
    public void updatePerson(Person person) {
        String updatePerson = "UPDATE persons SET first_name = ?, last_name = ? WHERE id = ?";
        String deleteNumbers = "DELETE FROM numbers WHERE person_id = ?";
        String insertNumber = "INSERT INTO numbers (id, person_id, number) VALUES (?, ?, ?)";

        try (Connection conn = DataBase.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement psUpdate = conn.prepareStatement(updatePerson)) {
                psUpdate.setString(1, person.getFirstName());
                psUpdate.setString(2, person.getLastName());
                psUpdate.setObject(3, person.getId());
                psUpdate.executeUpdate();
            }

            try (PreparedStatement psDeleteNums = conn.prepareStatement(deleteNumbers)) {
                psDeleteNums.setObject(1, person.getId());
                psDeleteNums.executeUpdate();
            }

            try (PreparedStatement psInsertNum = conn.prepareStatement(insertNumber)) {
                for (Number num : person.getNumbers()) {
                    psInsertNum.setObject(1, num.getId());
                    psInsertNum.setObject(3, person.getId());
                    psInsertNum.setString(2, num.getNumber());
                    psInsertNum.executeUpdate();
                }
            }

            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deletePerson(UUID id) {
        String deleteNumbers = "DELETE FROM numbers WHERE person_id = ?";
        String deletePerson = "DELETE FROM persons WHERE id = ?";

        try (Connection conn = DataBase.getConnection();
             PreparedStatement psNums = conn.prepareStatement(deleteNumbers);
             PreparedStatement psPerson = conn.prepareStatement(deletePerson)) {

            conn.setAutoCommit(false);

            psNums.setObject(1, id);
            psNums.executeUpdate();

            psPerson.setObject(1, id);
            psPerson.executeUpdate();

            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

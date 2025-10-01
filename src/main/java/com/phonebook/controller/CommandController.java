package com.phonebook.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phonebook.model.dto.request.*;
import com.phonebook.model.dto.response.*;
import com.phonebook.service.NumberService;
import com.phonebook.service.NumberServiceImpl;
import com.phonebook.service.PersonService;
import com.phonebook.service.PersonServiceImpl;
import org.apache.commons.cli.*;

import java.util.UUID;

public class CommandController {

    private final Options options;
    private final PersonService personService;
    private final NumberService numberService;
    private final ObjectMapper objectMapper;

    public CommandController() {
        this.options = createOptions();
        this.personService = new PersonServiceImpl();
        this.numberService = new NumberServiceImpl();
        this.objectMapper = new ObjectMapper();
    }

    private Options createOptions() {
        Options options = new Options();

        OptionGroup personGroup = new OptionGroup();
        personGroup.addOption(Option.builder("all")
                .desc("Получить список всех пользователей")
                .build());
        personGroup.addOption(Option.builder("add")
                .hasArgs()
                .numberOfArgs(5)
                .valueSeparator(' ')
                .desc("Добавить пользователя: first_name last_name num1 num2 num3")
                .build());
        personGroup.addOption(Option.builder("fnd")
                .hasArgs()
                .numberOfArgs(2)
                .valueSeparator(' ')
                .desc("Найти пользователя по ID или фамилии и номеру: id_person || last_name num")
                .build());
        personGroup.addOption(Option.builder("del")
                .hasArg()
                .desc("Удалить пользователя по ID: id_person")
                .build());
        personGroup.addOption(Option.builder("upt")
                .hasArgs()
                .numberOfArgs(6)
                .valueSeparator(' ')
                .desc("Обновить данные пользователя: id_person first_name last_name num1 num2 num3")
                .build());

        options.addOptionGroup(personGroup);

        OptionGroup numberGroup = new OptionGroup();
        numberGroup.addOption(Option.builder("add")
                .hasArgs()
                .numberOfArgs(2)
                .valueSeparator(' ')
                .desc("Добавить номер пользователю: num id_person")
                .build());
        numberGroup.addOption(Option.builder("del")
                .hasArg()
                .desc("Удалить номер: num")
                .build());

        options.addOptionGroup(numberGroup);

        options.addOption("p", false, "Режим работы с пользователями");
        options.addOption("n", false, "Режим работы с номерами");
        options.addOption("h", "help", false, "Показать справку");

        return options;
    }

    public boolean execute(String[] args) throws ParseException, JsonProcessingException {
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        if (cmd.hasOption("help") || args.length == 0) {
            printHelp();
            return true;
        }

        if (cmd.hasOption("p")) {
            return handlePersonOperations(cmd);
        } else if (cmd.hasOption("n")) {
            return handleNumberOperations(cmd);
        } else {
            System.err.println("Ошибка: необходимо указать режим работы (-p или -n)");
            printHelp();
            return false;
        }
    }

    private boolean handleNumberOperations(CommandLine cmd) throws JsonProcessingException {
        if (cmd.hasOption("nadd")) {
            return handleAddNumber(cmd.getOptionValues("nadd"));
        } else if (cmd.hasOption("ndel")) {
            return handleDeleteNumber(cmd.getOptionValue("ndel"));
        } else {
            System.err.println("Ошибка: не указана операция для номеров");
            printHelp();
            return false;
        }
    }

    private boolean handleDeleteNumber(String ndel) throws JsonProcessingException {
        DtoDeleteNumberRequest request = new DtoDeleteNumberRequest(ndel);
        DtoDeleteNumberResponse response = numberService.deleteNumber(request);
        if (response == null) {
            return false;
        } else {
            printJsonResponse(response.toString());
            return true;
        }
    }

    private boolean handleAddNumber(String[] nadds) throws JsonProcessingException {
        String number = nadds[0];
        UUID personId = UUID.fromString(nadds[1]);
        DtoAddNumberRequest request = new DtoAddNumberRequest(number, personId);
        DtoAddNumberResponse response = numberService.addNumber(request);
        if (response == null) {
            return false;
        } else {
            printJsonResponse(response.toString());
            return true;
        }
    }

    private boolean handlePersonOperations(CommandLine cmd) throws JsonProcessingException {
        if (cmd.hasOption("all")) {
            return handleGetAllPersons();
        } else if (cmd.hasOption("add")) {
            return handleAddPerson(cmd.getOptionValues("add"));
        } else if (cmd.hasOption("fnd")) {
            return handleFindPerson(cmd.getOptionValues("fnd"));
        } else if (cmd.hasOption("del")) {
            return handleDeletePerson(cmd.getOptionValue("del"));
        } else if (cmd.hasOption("upt")) {
            return handleUpdatePerson(cmd.getOptionValues("upt"));
        } else {
            System.err.println("Ошибка: не указана операция для пользователей");
            printHelp();
            return false;
        }
    }

    private boolean handleUpdatePerson(String[] upts) throws JsonProcessingException {
        DtoUpdatePersonRequest request = new DtoUpdatePersonRequest(new DtoUpdatePersonRequest.PersonData(
                UUID.fromString(upts[0]),
                upts[1],
                upts[2],
                new DtoUpdatePersonRequest.Numbers(
                        upts[3],
                        upts[4],
                        upts[5]
                )
        ));

        DtoUpdatePersonResponse response = personService.updatePerson(request);

        if (response == null) {
            return false;
        } else {
            printJsonResponse(response.toString());
            return true;
        }
    }

    private boolean handleDeletePerson(String del) throws JsonProcessingException {
        DtoDeletePersonRequest request = new DtoDeletePersonRequest(UUID.fromString(del));

        DtoDeletePersonResponse response = personService.deletePerson(request);

        if (response == null) {
            return false;
        } else {
            printJsonResponse(response.toString());
            return true;
        }
    }

    private boolean handleFindPerson(String[] fnds) throws JsonProcessingException {
        DtoFindPersonResponse response;

        try {
            UUID id = UUID.fromString(fnds[0]);
            DtoFindPersonRequest request = new DtoFindPersonRequest(id);
            response = personService.findPersonId(request);
        } catch (IllegalArgumentException e) {
            String lastName = fnds[0];
            String number = fnds[1];
            response = personService.findPersonNoId(lastName, number);
        }

        if (response == null) {
            return false;
        } else {
            printJsonResponse(response.toString());
            return true;
        }
    }

    private boolean handleAddPerson(String[] adds) throws JsonProcessingException {
        DtoCreatPersonRequest request = new DtoCreatPersonRequest(new DtoCreatPersonRequest.PersonData(
                adds[0],
                adds[1],
                new DtoCreatPersonRequest.Numbers(
                        adds[2],
                        adds[3],
                        adds[4]
                )
        ));

        DtoCreatePersonResponse response = personService.createPerson(request);

        if (response == null) {
            return false;
        } else {
            printJsonResponse(response.toString());
            return true;
        }
    }

    private boolean handleGetAllPersons() throws JsonProcessingException {
        DtoFindPersonsResponse response = personService.findPersons();
        if (response == null) {
            return false;
        } else {
            printJsonResponse(response.toString());
            return true;
        }
    }

    private void printHelp() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(100);

        System.out.println("Телефонная книга\n");

        System.out.println("Работа с пользователями:\n");
        System.out.println("  telebook -p -all                    Получить всех пользователей\n");
        System.out.println("  telebook -p -add John Doe 123 456 789 Добавить пользователя\n");
        System.out.println("  telebook -p -fnd Doe 123           Найти по фамилии и номеру\n");
        System.out.println("  telebook -p -fnd uuid-person         Найти по ID\n");
        System.out.println("  telebook -p -del uuid-person         Удалить по ID\n");
        System.out.println("  telebook -p -upt uuid-person John Doe 123 456 789 Обновить данные\n");

        System.out.println("Работа с номерами:\n");
        System.out.println("  telebook -n -add 999-888 uuid-person     Добавить номер пользователю\n");
        System.out.println("  telebook -n -del 123-456          Удалить номер\n");

        System.out.println("Общие команды:\n");
        System.out.println("  telebook -h                        Показать справку\n");
    }

    private void printJsonResponse(Object response) throws JsonProcessingException {
        String json = objectMapper.writeValueAsString(response);
        System.out.println(json);
    }
}

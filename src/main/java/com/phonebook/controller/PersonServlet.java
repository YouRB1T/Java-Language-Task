package com.phonebook.controller;

import com.phonebook.model.dto.request.DtoCreatPersonRequest;
import com.phonebook.model.dto.request.DtoFindPersonRequest;
import com.phonebook.model.dto.request.DtoUpdatePersonRequest;
import com.phonebook.model.dto.response.*;
import com.phonebook.model.dto.request.DtoDeletePersonRequest;

import com.phonebook.service.PersonService;
import com.phonebook.service.PersonServiceImpl;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.UUID;

@WebServlet("/persons")
public class PersonServlet extends HttpServlet {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final PersonService personService = new PersonServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json; charset=UTF-8");

        String pathInfo = req.getPathInfo();
        String lastNameParam = req.getParameter("last_name");
        String numberParam = req.getParameter("number");

        if ((lastNameParam != null && !lastNameParam.isEmpty()) ||
                (numberParam != null && !numberParam.isEmpty())) {

            DtoFindPersonResponse response = personService.findPersonNoId(lastNameParam, numberParam);
            objectMapper.writeValue(resp.getWriter(), response);

        } else if (pathInfo == null || pathInfo.equals("/")) {

            DtoFindPersonsResponse response = personService.findPersons();
            objectMapper.writeValue(resp.getWriter(), response);

        } else {
            String idStr = pathInfo.substring(1);
            UUID id = UUID.fromString(idStr);

            DtoFindPersonResponse response = personService.findPersonId(new DtoFindPersonRequest(id));
            objectMapper.writeValue(resp.getWriter(), response);
        }
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json; charset=UTF-8");
        DtoCreatPersonRequest request = objectMapper.readValue(req.getReader(), DtoCreatPersonRequest.class);
        DtoCreatePersonResponse response = personService.createPerson(request);
        objectMapper.writeValue(resp.getWriter(), response);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json; charset=UTF-8");
        DtoUpdatePersonRequest request = objectMapper.readValue(req.getReader(), DtoUpdatePersonRequest.class);
        DtoUpdatePersonResponse response = personService.updatePerson(request);
        objectMapper.writeValue(resp.getWriter(), response);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json; charset=UTF-8");
        DtoDeletePersonRequest request = objectMapper.readValue(req.getReader(), DtoDeletePersonRequest.class);
        DtoDeletePersonResponse response = personService.deletePerson(request);
        objectMapper.writeValue(resp.getWriter(), response);
    }
}

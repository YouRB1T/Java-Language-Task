package com.phonebook.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.phonebook.model.dto.request.DtoAddNumberRequest;
import com.phonebook.model.dto.request.DtoDeleteNumberRequest;
import com.phonebook.model.dto.response.DtoAddNumberResponse;
import com.phonebook.model.dto.response.DtoDeleteNumberResponse;
import com.phonebook.service.NumberService;
import com.phonebook.service.NumberServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/numbers")
public class NumberServlet extends HttpServlet {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final NumberService numberService = new NumberServiceImpl();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json; charset=UTF-8");

        DtoAddNumberRequest request = objectMapper.readValue(req.getReader(), DtoAddNumberRequest.class);
        DtoAddNumberResponse response = numberService.addNumber(request);

        objectMapper.writeValue(resp.getWriter(), response);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json; charset=UTF-8");

        String numberParam = req.getParameter("number");
        if (numberParam == null || numberParam.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Number parameter is required");
            return;
        }

        DtoDeleteNumberRequest request = new DtoDeleteNumberRequest(numberParam);
        DtoDeleteNumberResponse response = numberService.deleteNumber(request);

        objectMapper.writeValue(resp.getWriter(), response);
    }
}

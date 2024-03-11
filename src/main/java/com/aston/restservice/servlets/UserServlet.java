package com.aston.restservice.servlets;

import com.aston.restservice.dto.UserDto;
import com.aston.restservice.exception.EntityNotFoundException;
import com.aston.restservice.exception.HttpException;
import com.aston.restservice.exception.Response;
import com.aston.restservice.service.UserService;
import com.aston.restservice.util.GetProvider;
import com.aston.restservice.util.ResponseSender;
import com.fasterxml.jackson.core.JsonProcessingException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/user/*")
public class UserServlet extends HttpServlet {

    private final UserService userService;

    public UserServlet() {
        this.userService = GetProvider.getUserService();
    }

//    public UserServlet(UserService userService) {
//        this.userService = userService;
//    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        UserDto savedUserDto;
        String body = GetProvider.getBody(req);

        try {
            UserDto userDto = userDtoFromJson(body, resp);
            savedUserDto = userService.saveUser(userDto);
            ResponseSender.sendResponse(resp, HttpServletResponse.SC_CREATED, savedUserDto);
        } catch (SQLException e) {
            e.printStackTrace();
            Response response = new Response(e.getMessage());
            ResponseSender.sendResponse(resp, HttpServletResponse.SC_BAD_REQUEST, response);
        } catch (HttpException e) {
            e.printStackTrace();
            Response response = new Response(e.getMessage());
            ResponseSender.sendResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            List<UserDto> users = userService.getAllUsers();
            ResponseSender.sendResponse(resp, HttpServletResponse.SC_OK, users);
        } catch (SQLException e) {
            e.printStackTrace();
            Response response = new Response(e.getMessage());
            ResponseSender.sendResponse(resp, HttpServletResponse.SC_BAD_REQUEST, response);
        } catch (HttpException e) {
            e.printStackTrace();
            Response response = new Response(e.getMessage());
            ResponseSender.sendResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, response);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        String requestPath = req.getPathInfo();
        try {
            Long id = userService.deleteUser(requestPath);
            ResponseSender.sendResponse(resp, HttpServletResponse.SC_OK,
                    new Response(String.format("User with id %d was successfully deleted", id)));
        } catch (SQLException | EntityNotFoundException e) {
            e.printStackTrace();
            Response response = new Response(e.getMessage());
            ResponseSender.sendResponse(resp, HttpServletResponse.SC_BAD_REQUEST, response);
        } catch (HttpException e) {
            e.printStackTrace();
            Response response = new Response(e.getMessage());
            ResponseSender.sendResponse(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, response);
        }
    }

    private UserDto userDtoFromJson(String body, HttpServletResponse resp) {
        UserDto userDto = null;
        try {
            userDto = GetProvider.getObjectMapper().readValue(body, UserDto.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            ResponseSender.sendResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                    new Response("Incorrect Json received"));
        }
        return userDto;
    }
}

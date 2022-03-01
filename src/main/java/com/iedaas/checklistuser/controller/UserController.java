package com.iedaas.checklistuser.controller;

import com.iedaas.checklistuser.AuthorizationFilter;
import com.iedaas.checklistuser.dto.UserDTO;
import com.iedaas.checklistuser.exception.ObjectNotFoundException;
import com.iedaas.checklistuser.services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;

@RestController
public class UserController {

    @Autowired
    UserServices userServices;

    @Autowired
    AuthorizationFilter authorizationFilter;

    @GetMapping("/user")
    public UserDTO getUser(HttpServletRequest request){
        String userEmail = authorizationFilter.authenticate(request);
        UserDTO userDTO;
        try {
            userDTO = userServices.getUserByEmail(userEmail);
        }
        catch (ObjectNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return userDTO;

    }

    @GetMapping("/user/exist")
    public Boolean ifUserExist(HttpServletRequest request){
        String userEmail = authorizationFilter.authenticate(request);
        return userServices.ifUserExist(userEmail);
    }

    @PostMapping("/user")
    public UserDTO addUser(@RequestBody UserDTO userDTO){
        return userServices.addUser(userDTO);
    }

    @PutMapping("/user")
    public void updateUser(HttpServletRequest request, @RequestBody UserDTO userDTO){
        String userEmail = authorizationFilter.authenticate(request);
        try {
            userServices.updateUser(userEmail, userDTO);
        }
        catch (ObjectNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/user")
    public void deleteUser(HttpServletRequest request, @RequestBody UserDTO userDTO){
        String userEmail = authorizationFilter.authenticate(request);
        boolean deleted;
        try{
            deleted = userServices.deleteUser(userEmail, userDTO);
        }
        catch (ObjectNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        if(!deleted){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }
}

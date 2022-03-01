package com.iedaas.checklistuser.services;

import com.iedaas.checklistuser.dao.UserRepository;
import com.iedaas.checklistuser.dto.UserDTO;
import com.iedaas.checklistuser.entity.User;
import com.iedaas.checklistuser.exception.ObjectNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class UserServices {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    UserRepository userRepository;

    public UserDTO getUserByEmail(String email){
        User user = userRepository.findByEmail(email);
        if(user==null){
            throw new ObjectNotFoundException(String.format("Not Found userEmail %s", email));
        }
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        return userDTO;
    }

    public Boolean ifUserExist(String email){
        User user = userRepository.findByEmail(email);
        return user != null;
    }

    public UserDTO addUser(UserDTO userDTO){
        User user = modelMapper.map(userDTO, User.class);
        User userDb = userRepository.save(user);
        UserDTO addedUserDto = modelMapper.map(userDb, UserDTO.class);
        return addedUserDto;
    }

    public void updateUser(String userEmail, UserDTO userDTO){
        User user = modelMapper.map(userDTO, User.class);
        if(user==null){
            throw new ObjectNotFoundException(String.format("Not Found userEmail %s", userEmail));
        }
        User userDb = userRepository.findByEmail(userEmail);
        userDb.setCity(user.getCity());
        userDb.setCountry(user.getCountry());
        userDb.setEmailId(user.getEmailId());
        userDb.setStatus(user.getStatus());
        userDb.setPhoneNumber(user.getPhoneNumber());

        userRepository.save(userDb);
    }

    public Boolean deleteUser(String userEmail, UserDTO userDTO){
        User user = modelMapper.map(userDTO, User.class);
        if(user==null){
            throw new ObjectNotFoundException(String.format("Not Found userEmail %s", userEmail));
        }

        if(user.getEmailId().equals(userEmail)){
            User userDb = userRepository.findByEmail(userEmail);
            userRepository.delete(userDb);
            return true;
        }
        return false;
    }
}

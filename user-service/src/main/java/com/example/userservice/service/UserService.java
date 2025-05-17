package com.example.userservice.service;

import com.example.common.dto.UserDto;

import java.util.List;

public interface UserService {
    
    List<UserDto> getAllUsers();
    
    UserDto getUserById(Long id);
    
    UserDto getUserByUsername(String username);
    
    UserDto createUser(UserDto userDto);
    
    UserDto updateUser(Long id, UserDto userDto);
    
    void deleteUser(Long id);
}

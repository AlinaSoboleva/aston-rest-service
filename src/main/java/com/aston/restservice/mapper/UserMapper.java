package com.aston.restservice.mapper;

import com.aston.restservice.dto.UserDto;
import com.aston.restservice.model.User;

public class UserMapper {
    private UserMapper() {
    }

    public static User toEntity(UserDto userDto) {
        if (userDto == null) return null;

        User user = new User();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());

        return user;
    }

    public static UserDto toDto(User user) {
        if (user == null) return null;

        return UserDto.builder()
                .name(user.getName())
                .email(user.getEmail())
                .id(user.getId())
                .build();
    }
}

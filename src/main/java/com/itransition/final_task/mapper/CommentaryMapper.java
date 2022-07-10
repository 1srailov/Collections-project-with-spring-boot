package com.itransition.final_task.mapper;

import com.itransition.final_task.dto.response.CommentaryResponse;
import com.itransition.final_task.models.Commentary;
import com.itransition.final_task.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;
import java.util.stream.Collectors;

public class CommentaryMapper {
    @Autowired
    private static UserService userService;
    @Autowired
    private static ModelMapper modelMapper;


}

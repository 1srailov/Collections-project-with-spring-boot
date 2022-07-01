package com.itransition.final_task.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class beans {

    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }

}

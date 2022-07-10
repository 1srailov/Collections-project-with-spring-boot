package com.itransition.final_task.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public org.modelmapper.ModelMapper modelMappers(){
        return new org.modelmapper.ModelMapper();
    }

}

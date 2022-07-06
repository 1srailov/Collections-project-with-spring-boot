package com.itransition.final_task.config;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;
@Configuration
public class CloudinaryConfig {


    @Bean
    public Cloudinary cloudinaryConfigs() {
        Map config = new HashMap();
        config.put("cloud_name", "collections-task");
        config.put("api_key", "625795365583388");
        config.put("api_secret", "LZ9QiU18lK721RphpAz8Zw4a5gg");

        Cloudinary cloudinary = new com.cloudinary.Cloudinary(config);
        return cloudinary;
    }
}

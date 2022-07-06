package com.itransition.final_task.controllers;


import com.itransition.final_task.dto.response.TopicResponse;
import com.itransition.final_task.services.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/topic")
public class TopicController {

    @Autowired
    private TopicService topicService;

    @GetMapping()
    public ResponseEntity<List<TopicResponse>>getAll(){
        return topicService.getAll();
    }
}

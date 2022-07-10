package com.itransition.final_task.controllers;


import com.itransition.final_task.dto.response.HashtagResponse;
import com.itransition.final_task.services.HashtagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/hashtag")
public class HashtagController {
    @Autowired
    private HashtagService hashtagService;


    @GetMapping()
    public ResponseEntity<List<HashtagResponse>> getAll(){
        return hashtagService.getAllHashtags();
    }
}

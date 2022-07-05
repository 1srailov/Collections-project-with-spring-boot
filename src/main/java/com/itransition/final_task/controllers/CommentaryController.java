package com.itransition.final_task.controllers;

import com.itransition.final_task.dto.request.CommentResponse;
import com.itransition.final_task.dto.response.MessageResponse;
import com.itransition.final_task.services.CommentaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/commentary")
public class CommentaryController {

    @Autowired
    private CommentaryService commentaryService;


    @PostMapping()
    public ResponseEntity<MessageResponse> addCommentToItem(@RequestBody CommentResponse commentResponse, HttpServletRequest request){
        return commentaryService.addCommentToItem(commentResponse.getText(),
                commentResponse.getItemId(), request.getHeader("Authorization").substring(7));
    }
}

package com.itransition.final_task.controllers;

import com.itransition.final_task.services.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/like")
public class LikeController {
    @Autowired
    private LikeService likeService;

    @PutMapping("/{itemId}/like-item")
    public void likeOrDislikeItem(@PathVariable Long itemId, HttpServletRequest request){
        likeService.likeOrDislikeItem(itemId, request.getHeader("Authorization").substring(7));
    }
}

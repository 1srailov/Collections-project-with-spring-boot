package com.itransition.final_task.controllers;

import com.itransition.final_task.services.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/like")
public class LikeController {
    @Autowired
    private LikeService likeService;

    @PostMapping("/{itemId}")
    public void likeOrDislikeItem(@PathVariable Long itemId, HttpServletRequest request){
        likeService.likeOrDislikeItem(itemId, request.getHeader("Authorization").substring(7));
    }
}

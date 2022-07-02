package com.itransition.final_task.controllers;

import com.itransition.final_task.dto.request.AddItemRequest;
import com.itransition.final_task.dto.request.CommentResponse;
import com.itransition.final_task.dto.response.MessageResponse;
import com.itransition.final_task.repository.CommentaryRepository;
import com.itransition.final_task.services.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/item")
public class ItemController {

    @Autowired
    private ItemService itemService;


    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> deleteItem(@PathVariable Long id, HttpServletRequest request){
        return itemService.deleteItemFromCollection(id, request.getHeader("Authorization").substring(7));
    }

    @PostMapping("/add-comment")
    public ResponseEntity<MessageResponse> addCommentToItem(@RequestBody CommentResponse commentResponse, HttpServletRequest request){
        return itemService.addCommentToItem(commentResponse.getText(),
                commentResponse.getItemId(), request.getHeader("Autharization").substring(7));
    }

    @PutMapping("/{itemId}/like-item")
    public void likeOrDislikeItem(@PathVariable Long itemId, HttpServletRequest request){
        itemService.likeOrDislikeItem(itemId, request.getHeader("Authorization").substring(7));
    }

    @PostMapping("/add-item")
    public void addItem(@RequestBody AddItemRequest item, HttpServletRequest request){
        itemService.addItem(item, request);
    }
}

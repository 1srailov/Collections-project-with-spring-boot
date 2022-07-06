package com.itransition.final_task.controllers;

import com.itransition.final_task.dto.request.AddItemRequest;
import com.itransition.final_task.dto.response.MessageResponse;
import com.itransition.final_task.services.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@RestController
@RequestMapping("/item")
public class ItemController {

    @Autowired
    private ItemService itemService;


    @PostMapping()
    public ResponseEntity<List<MessageResponse>> addItem(@RequestBody AddItemRequest item, HttpServletRequest request){
        return itemService.addItem(item, request.getHeader("Authorization").substring(7));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> deleteItem(@PathVariable Long id, HttpServletRequest request){
        return itemService.deleteItemFromCollection(id, request.getHeader("Authorization").substring(7));
    }



}

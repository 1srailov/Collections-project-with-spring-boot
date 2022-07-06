package com.itransition.final_task.controllers;


import com.itransition.final_task.dto.request.CollectionRequest;
import com.itransition.final_task.dto.response.CollectionToMainPageResponse;
import com.itransition.final_task.services.CollectionService;
import com.itransition.final_task.services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/collection")
public class CollectionController{

    @Autowired
    private CollectionService collectionService;

    @PostMapping()
    public ResponseEntity<?> addCollection(@RequestBody CollectionRequest collectionRequest, HttpServletRequest request){
        System.out.println(collectionRequest.getColumns());
       return collectionService.createCollection(collectionRequest, request.getHeader("Authorization").substring(7));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCollection(@PathVariable Long id, HttpServletRequest request){
        return collectionService.deleteCollection(id, request.getHeader("Authorization").substring(7));
    }

    @GetMapping("/get-all/{page}")
    public ResponseEntity<?> getAllByPagination(@PathVariable Integer page){
        return collectionService.getAllByPage(page);
    }

    @GetMapping("/my-collections")
    public ResponseEntity<List<CollectionToMainPageResponse>> getAllFromUser(HttpServletRequest request){
        return collectionService.getAllByUerId(request.getHeader("Authorization").substring(7));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id){
        return collectionService.getById(id);
    }

    @GetMapping("/get-top")
    public ResponseEntity<?> getTopCollections(){
        return collectionService.getTop5Collections();
    }


}

package com.itransition.final_task.controllers;

import com.itransition.final_task.services.CollectionColumnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/column")
public class CollectionColumnsController {

    @Autowired
    private CollectionColumnService collectionColumnService;

//    @PostMapping()
//    public ResponseEntity<MessageResponse> addColumnToCollection(@RequestBody CollectionColumnsRequest collectionColumnsRequest){
//        return collectionColumnService.addColumnToCollection(collectionColumnsRequest);
//    }
}

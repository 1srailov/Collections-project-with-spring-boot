package com.itransition.final_task.controllers;


import com.itransition.final_task.services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/file")
public class FileController{

    @Autowired
    private FileService fileService;


    @PostMapping()
    public ResponseEntity<?> inputImageToCollection(@ModelAttribute MultipartFile image, Long collectionId){
       return fileService.saveImage(image, collectionId);
    }

//    @DeleteMapping()
//    public ResponseEntity<?> deleteImage(@RequestParam Long colletionId){
//        fileService.deleteImage(colletionId);
//    }


}

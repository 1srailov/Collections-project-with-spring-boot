package com.itransition.final_task.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.itransition.final_task.config.CloudinaryConfig;
import com.itransition.final_task.dto.response.MessageResponse;
import org.apache.catalina.LifecycleState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class FileService {
    @Lazy
    @Autowired
    private CollectionService collectionService;

    @Autowired
    private Cloudinary cloudinary;

    private ResponseEntity<String[]> checkAndSaveImage(MultipartFile image){
        String[] result = new String[2];
        if(checkFileType(image.getContentType())) {
            try {
                File uploadedFile = convertMultiPartToFile(image);
                Map uploadResult = cloudinary.uploader().upload(uploadedFile, ObjectUtils.emptyMap());
                uploadedFile.delete();
                result[0] = (String)uploadResult.get("url");
                result[1] = (String)uploadResult.get("public_id");
                return ResponseEntity.ok().body(result);
            } catch (IOException e) {
                System.out.print(e.getMessage());
                result[0] = "ERROR IN SERVER";
                return ResponseEntity.status(500).body(result);
            }
        }
        result[0] = "UNSUPPORTED MEDIA TYPE";
        return ResponseEntity.status(415).body(result);
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException{
        File convFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

    private boolean checkFileType(String type) {
        if(type == null) {
            return false;
        }
        if(!(type.equalsIgnoreCase("image/jpg")
                || type.equalsIgnoreCase("image/jpeg")
                || type.equalsIgnoreCase("image/png"))) {
                return false;
        }
        return true;
    }

    public void deleteByAddress(String imageAddress){
        try {
            System.out.println(imageAddress);
            cloudinary.uploader().destroy(imageAddress, ObjectUtils.emptyMap());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ResponseEntity<MessageResponse> saveImage(MultipartFile image, Long collectionId){

        if(!collectionService.existsById(collectionId)){
           return ResponseEntity.status(405).body(new MessageResponse("COLLECTION NOT FOUND"));
        }

        ResponseEntity<String[]> responseEntity = checkAndSaveImage(image);

        if(!responseEntity.getStatusCode().isError())
        if(collectionService.addImageAddressToCollection(responseEntity.getBody()[0], responseEntity.getBody()[1], collectionId))
            return ResponseEntity.ok().body(new MessageResponse("IMAGE SUCCESSFULLY ADDED"));


        return ResponseEntity.status(responseEntity.getStatusCode()).body(new MessageResponse(responseEntity.getBody()[0]));
    }

//    public void deleteImage(Long colletionId) {
//    }
}

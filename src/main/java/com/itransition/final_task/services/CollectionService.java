package com.itransition.final_task.services;


import com.itransition.final_task.dto.request.CollectionColumnsRequest;
import com.itransition.final_task.dto.request.CollectionRequest;
import com.itransition.final_task.dto.response.CollectionResponse;
import com.itransition.final_task.mapper.ItemMapper;
import com.itransition.final_task.models.Collection;

import com.itransition.final_task.dto.response.CollectionToMainPageResponse;
import com.itransition.final_task.dto.response.MessageResponse;
import com.itransition.final_task.models.CollectionColumn;
import com.itransition.final_task.repository.CollectionRepository;
import com.itransition.final_task.security.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CollectionService {
    private final JwtUtils jwtUtils;
    private final CollectionRepository collectionRepository;
    private final ModelMapper modelMapper;
    private final FileService fileService;
    private final UserService userService;
    private final TopicService topicService;

    public ResponseEntity<MessageResponse> createCollection(CollectionRequest collectionRequest, String jwt){
        ResponseEntity<MessageResponse> responseEntity = fileService.checkAndSaveImage(collectionRequest.getImage());

        if(responseEntity.getStatusCode().isError())return responseEntity;

        MapAndSaveCollection(collectionRequest, jwt, (collectionRequest.getImage().hashCode() + ".jpg"));

        return ResponseEntity.ok().body(new MessageResponse("ADDED SUCCESSFULLY"));
    }

//    public ResponseEntity<List<CollectionToMainPageResponse>> getTop5Collections(){
//            return ResponseEntity.ok().body(collectionRepository.getTopCollections()
//                    .stream().map(a -> modelMapper.map(a, CollectionToMainPageResponse.class)).collect(Collectors.toList()));
//    }

    public ResponseEntity<?> deleteCollection(Long id, String jwt){
        Optional<Collection> collection = collectionRepository.findById(id);
        if(collection.isPresent()){
           if(userService.checkIsCanEditData(collection.get().getUserId(), jwt)){
               fileService.deleteByAddress(collection.get().getImageAddress());
               collectionRepository.deleteById(collection.get().getId());
               return ResponseEntity.ok().body(new MessageResponse("DELETED SUCCESSFULLY"));
           }
           return ResponseEntity.status(401).body(new MessageResponse("YOU DON'T HAVE PERMISSIONS FOR DO IT"));
        }
        return ResponseEntity.status(401).body(new MessageResponse("COLLECTION NOT FOUND"));
    }

    public ResponseEntity<List<CollectionToMainPageResponse>> getAllByPage(Integer page_count){
        Pageable pageable = PageRequest.of(page_count, 15);

        List<Collection> collections = collectionRepository.findByOrderByIdDesc(pageable);

        List<CollectionToMainPageResponse> collectionToMainPageResponses = setCollectionsResponse(collections);

        return ResponseEntity.ok().body(collectionToMainPageResponses);
    }

    public ResponseEntity<?> getById(Long id){
        Optional<Collection> collection = collectionRepository.findById(id);
        if(collection.isPresent()){
            CollectionResponse collectionDto = modelMapper.map(collection.get(), CollectionResponse.class);
            collectionDto.setResponseItems(collection.get().getItems().stream().map(ItemMapper::toDto).collect(Collectors.toSet()));
            collectionDto.setAuthor(userService.getUsernameById(collection.get().getUserId()));
            collectionDto.setTopic(topicService.getTopicNameById(collection.get().getTopicId()));
            return ResponseEntity.ok().body(collectionDto);
        }
        return ResponseEntity.status(500).body(new MessageResponse("COLLECTION NOT FOUND"));
    }




    public List<CollectionToMainPageResponse> setCollectionsResponse(List<Collection> collections){
        List<CollectionToMainPageResponse> collectionToMainPageRespons = new ArrayList<>();

        collections.forEach(collection -> {

            CollectionToMainPageResponse cResponse = new CollectionToMainPageResponse();

            cResponse.setId(collection.getId());

            cResponse.setName(collection.getName());

            cResponse.setTopic(topicService.getTopicNameById(collection.getTopicId()));

            cResponse.setAuthor(userService.getUsernameById(collection.getUserId()));

            cResponse.setImageAddress(collection.getImageAddress());

            collectionToMainPageRespons.add(cResponse);
        });
        return collectionToMainPageRespons;
    }


    public void MapAndSaveCollection(CollectionRequest collectionRequest, String jwt, String imageAddress){
        Collection collection = modelMapper.map(collectionRequest, Collection.class);

        collection.setUserId(userService.getUserByUsername(jwtUtils.getUserNameFromJwtToken(jwt)).getId());

        collection.setImageAddress(imageAddress);

        collectionRepository.save(collection);
    }

    public Long getUserIdById(Long collectionId){
        return collectionRepository.getUserIdById(collectionId);
    }

    public ResponseEntity<?> getTop5Collections(){
        return ResponseEntity.ok().body(collectionRepository.getTopCollections(PageRequest.of(0,5)));
    }

    public boolean existsById(Long collectionId){
        return collectionRepository.existsById(collectionId);
    }
}


package com.itransition.final_task.services;


import com.itransition.final_task.dto.request.CollectionRequest;
import com.itransition.final_task.dto.response.CollectionColumnResponse;
import com.itransition.final_task.dto.response.CollectionResponse;
import com.itransition.final_task.mapper.CollectionColumnsMapper;
import com.itransition.final_task.mapper.ItemMapper;
import com.itransition.final_task.models.Collection;

import com.itransition.final_task.dto.response.CollectionToMainPageResponse;
import com.itransition.final_task.dto.response.MessageResponse;
import com.itransition.final_task.repository.CollectionRepository;
import com.itransition.final_task.security.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
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
    private final UserService userService;
    private final TopicService topicService;
    private final FileService fileService;

    private final CollectionColumnService collectionColumnService;

    public ResponseEntity<?> createCollection(CollectionRequest collectionRequest, String jwt){

        Collection collection = MapAndSaveCollection(collectionRequest, jwt);



        if(!collectionRepository.existsById(collection.getId())){
            return ResponseEntity.status(405).body(new MessageResponse("COLLECTION NOT FOUND"));
        }
        collectionColumnService.addColumnToCollection(collectionRequest.getColumns(), collection.getId());

        CollectionResponse collectionResponse = modelMapper.map(collection, CollectionResponse.class);

        setDetailsToCollection(collectionResponse, collection);

        return ResponseEntity.ok().body(collectionResponse);
    }

    private void setDetailsToCollection(CollectionResponse collectionDto, Collection collection){
        if(collection.getItems() != null){
            collectionDto.setResponseItems(collection.getItems().stream().map(ItemMapper::toDto).collect(Collectors.toSet()));
        }
        collectionDto.setAuthor(userService.getUsernameById(collection.getUserId()));
        collectionDto.setTopic(topicService.getTopicNameById(collection.getTopicId()));
    }

    public ResponseEntity<?> deleteCollection(Long id, String jwt){
        Optional<Collection> collection = collectionRepository.findById(id);
        if(collection.isPresent()){
           if(userService.checkIsCanEditData(collection.get().getUserId(), jwt)){
               System.out.println(collection.get().getImageId());
               fileService.deleteByAddress(collection.get().getImageId());
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
            Set<CollectionColumnResponse> collectionColumnResponses = modelMapper.map(collection.get().getCollectionColumns(), new TypeToken<Set<CollectionColumnResponse>>(){}.getType());
            collectionDto.setResponseCollectionColumns(collectionColumnResponses);
            setDetailsToCollection(collectionDto, collection.get());
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


    public Collection MapAndSaveCollection(CollectionRequest collectionRequest, String jwt){
        Collection collection = modelMapper.map(collectionRequest, Collection.class);

        collection.setUserId(userService.getUserByUsername(jwtUtils.getUserNameFromJwtToken(jwt)).getId());

        return collectionRepository.save(collection);
    }

    public Long getUserIdById(Long collectionId){
        return collectionRepository.getUserIdById(collectionId);
    }

    public ResponseEntity<?> getTop5Collections(){
        List<Collection> collections = collectionRepository.getTopCollections(PageRequest.of(0,5));

        List<CollectionToMainPageResponse> collectionDtos = new ArrayList<>();

        collections.forEach(a -> collectionDtos.add(modelMapper.map(a, CollectionToMainPageResponse.class)));

        return ResponseEntity.ok().body(collectionDtos);
    }

    public Boolean existsById(Long collectionId){
        return collectionRepository.existsById(collectionId);
    }

    public boolean addImageAddressToCollection(String url, String imageId, Long collectionId){
        Optional<Collection> collection = collectionRepository.findById(collectionId);

        if(collection.isPresent()) {
            collection.get().setImageAddress(url);
            collection.get().setImageId(imageId);
            collectionRepository.save(collection.get());
            return true;
        }

        return false;
    }

    public ResponseEntity<List<CollectionToMainPageResponse>> getAllByUerId(String jwt){
        Long userId = userService.getUserIdFromJwt(jwt);

        List<Collection> collections = collectionRepository.findAllByUserId(userId);

        if(collections != null){
            List<CollectionToMainPageResponse> responses = new ArrayList<>();
            collections.forEach(a -> responses.add(modelMapper.map(a, CollectionToMainPageResponse.class)));
            return ResponseEntity.ok().body(responses);
        }
       return ResponseEntity.status(405).body(new ArrayList<>());
    }
}


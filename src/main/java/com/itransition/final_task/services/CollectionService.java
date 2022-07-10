package com.itransition.final_task.services;


import com.itransition.final_task.dto.request.CollectionRequest;
import com.itransition.final_task.dto.response.*;
import com.itransition.final_task.mapper.CommentaryMapper;
import com.itransition.final_task.mapper.ItemValueMapper;
import com.itransition.final_task.models.Collection;

import com.itransition.final_task.models.Commentary;
import com.itransition.final_task.repository.CollectionRepository;
import com.itransition.final_task.security.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.context.annotation.Lazy;
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
    private final HashtagService hashtagService;

    public ResponseEntity<?> createCollection(CollectionRequest collectionRequest, String jwt){

        Collection collection = MapAndSaveCollection(collectionRequest, jwt);

        ResponseEntity<MessageResponse> response =
                collectionColumnService.addColumnToCollection(collectionRequest.getColumns(), collection.getId());

        if(!response.getStatusCode().is2xxSuccessful()){
            return response;
        }

        CollectionResponse collectionResponse = modelMapper.map(collection, CollectionResponse.class);

        setDetailsToCollection(collectionResponse, collection);

        return ResponseEntity.ok().body(collectionResponse);
    }

    private void setDetailsToCollection(CollectionResponse collectionDto, Collection collection){
        if(collection.getItems() != null){
            collectionDto.setResponseItems(collection.getItems().stream().map(
                    a -> {
                        ItemResponse item = ItemValueMapper.toDto(a);
                        item.setHashtags(hashtagService.entitiesToResponses(a.getHashtags()));
                        item.setCommentaries(commentaryEntitiesToResponses(a.getCommentaries()));
                        return item;
                    }
            ).collect(Collectors.toSet()));
        }

        collectionDto.setAuthor(userService.getUsernameById(collection.getUserId()));
        collectionDto.setTopic(topicService.getTopicNameById(collection.getTopicId()));
    }

    public ResponseEntity<?> deleteCollection(Long id, String jwt){
        Optional<Collection> collection = collectionRepository.findById(id);
        if(collection.isPresent()){
           if(userService.checkIsCanEditData(collection.get().getUserId(), jwt)){
               fileService.deleteByAddress(collection.get().getImageId());
               collectionRepository.deleteCollectionById(collection.get().getId());
               return ResponseEntity.ok().body(new MessageResponse("DELETED SUCCESSFULLY"));
           }
           return ResponseEntity.status(401).body(new MessageResponse("YOU DON'T HAVE PERMISSIONS FOR DO IT"));
        }
        return ResponseEntity.status(401).body(new MessageResponse("COLLECTION NOT FOUND"));
    }

    public ResponseEntity<List<CollectionToMainPageResponse>> getAllByPage(Integer page_count){
        Pageable pageable = PageRequest.of(page_count, 15);

        List<CollectionToMainPageResponse> collectionToMainPageResponses =
                setCollectionsResponse(collectionRepository.findByOrderByIdDesc(pageable));

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

    public Long getUserIdByCollectionId(Long collectionId){
        return collectionRepository.getUserIdById(collectionId);
    }

    public ResponseEntity<List<CollectionToMainPageResponse>> getTop5Collections(){

        List<CollectionToMainPageResponse> collectionToMainPageResponses =
                setCollectionsResponse(collectionRepository.getTopCollections(PageRequest.of(0,5)));

        return ResponseEntity.ok().body(collectionToMainPageResponses);
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

    public Set<CommentaryResponse> commentaryEntitiesToResponses(Set<Commentary> commentaries){
        System.out.println(commentaries);
        return commentaries != null ? commentaries.stream().map(
                a -> {
                    CommentaryResponse commentaryResponse = modelMapper.map(a, CommentaryResponse.class);
                    System.out.println(userService.getUsernameById(7L));
                    commentaryResponse.setUsername(userService.getUsernameById(a.getUserId()));
                    return commentaryResponse;
                }).collect(Collectors.toSet()) : null;
    }
}


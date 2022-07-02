package com.itransition.final_task.services;


import com.itransition.final_task.dto.request.CollectionColumnsRequest;
import com.itransition.final_task.dto.request.CollectionRequest;
import com.itransition.final_task.dto.response.CollectionResponse;
import com.itransition.final_task.models.Collection;

import com.itransition.final_task.dto.response.CollectionToMainPageResponse;
import com.itransition.final_task.dto.response.MessageResponse;
import com.itransition.final_task.models.CollectionColumn;
import com.itransition.final_task.repository.CollectionColumnRepository;
import com.itransition.final_task.repository.CollectionRepository;
import com.itransition.final_task.repository.TopicRepository;
import com.itransition.final_task.repository.UserRepository;
import com.itransition.final_task.security.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CollectionService {

    private final JwtUtils jwtUtils;

    private final CollectionRepository collectionRepository;

    private final ModelMapper modelMapper;

    private  final UserRepository userRepository;

    private final FileService fileService;

    private final UserService userService;

    private final TopicRepository topicRepository;

    private final CollectionColumnRepository collectionColumnRepository;

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

    public ResponseEntity<?> getAllByPage(Integer page_count){
        Pageable pageable = PageRequest.of(page_count, 15);

        List<Collection> collections = collectionRepository.findByOrderByIdDesc(pageable);

        List<CollectionToMainPageResponse> collectionToMainPageResponses = setCollectionsResponse(collections);

        return ResponseEntity.ok().body(collectionToMainPageResponses);
    }

    public ResponseEntity<?> getById(Long id){
        Optional<Collection> collection = collectionRepository.findById(id);
        if(collection.isPresent()){
            CollectionResponse collectionDto = modelMapper.map(collection.get(), CollectionResponse.class);
            collectionDto.setAuthor(userRepository.getUsernameById(collection.get().getUserId()));
            collectionDto.setTopic(topicRepository.getTopicById(collection.get().getTopicId()));
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

            cResponse.setTopic(topicRepository.getTopicById(collection.getTopicId()));

            cResponse.setAuthor(userRepository.getUsernameById(collection.getUserId()));

            cResponse.setImageAddress(collection.getImageAddress());

            collectionToMainPageRespons.add(cResponse);
        });
        return collectionToMainPageRespons;
    }


    public void MapAndSaveCollection(CollectionRequest collectionRequest, String jwt, String imageAddress){
        Collection collection = modelMapper.map(collectionRequest, Collection.class);

        collection.setUserId(userRepository.getUserByUsername(jwtUtils.getUserNameFromJwtToken(jwt)).getId());

        collection.setImageAddress(imageAddress);

        collectionRepository.save(collection);
    }

    public ResponseEntity<MessageResponse> addColumnToCollection(CollectionColumnsRequest columnsRequest){
        ResponseEntity<MessageResponse> response = checkCollectionColumnsRequest(columnsRequest);
        if(response != null) {
            return response;
        }
            for(Map.Entry<String, Integer> column : columnsRequest.getColumns().entrySet())
                collectionColumnRepository.save(new CollectionColumn(
                        columnsRequest.getCollectionId(),
                        column.getKey(),
                        column.getValue()
                ));

            return ResponseEntity.ok().body(new MessageResponse("SAVED SUCCESSFULLY"));
        }


    private ResponseEntity<MessageResponse> checkCollectionColumnsRequest(CollectionColumnsRequest columnsRequest) {
        if(collectionColumnRepository.existsByCollectionId(columnsRequest.getCollectionId())){
            return ResponseEntity.status(405).body(new MessageResponse("COLUMNS ALREADY ADDED"));
        }
        if(!collectionRepository.existsById(columnsRequest.getCollectionId())){
            return ResponseEntity.status(405).body(new MessageResponse("COLLECTION NOT FOUND"));
        }
        if(columnsRequest.getColumns().get(null) != null){
            return ResponseEntity.status(405).body(new MessageResponse("ERROR IN THE COLUMN NAMES"));
        }
        for(Integer type : columnsRequest.getColumns().values()){
            if(type == null || type < 0 || type > 5){
                return ResponseEntity.status(405).body(new MessageResponse("FORMAT ERROR"));
            }
        }
        return null;
    }

}


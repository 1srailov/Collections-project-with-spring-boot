package com.itransition.final_task.services;

import com.itransition.final_task.dto.request.CollectionColumnsRequest;
import com.itransition.final_task.dto.response.MessageResponse;
import com.itransition.final_task.models.CollectionColumn;
import com.itransition.final_task.repository.CollectionColumnRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CollectionColumnService {
    private final CollectionColumnRepository collectionColumnRepository;
    private final CollectionService collectionService;

    public Boolean existsByCollectionId(Long collectionId){
        return collectionColumnRepository.existsByCollectionId(collectionId);
    }

    public void save(CollectionColumn collectionColumn){
        collectionColumnRepository.save(collectionColumn);
    }

    public List<CollectionColumn> findAllByCollectionId(Long collectionId){
        return collectionColumnRepository.findAllByCollectionId(collectionId);
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
        if(existsByCollectionId(columnsRequest.getCollectionId())){
            return ResponseEntity.status(405).body(new MessageResponse("COLUMNS ALREADY ADDED"));
        }
        if(!collectionService.existsById(columnsRequest.getCollectionId())){
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

package com.itransition.final_task.services;
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

    public List<CollectionColumn> findAllByCollectionId(Long collectionId){
        return collectionColumnRepository.findAllByCollectionId(collectionId);
    }

    public ResponseEntity<MessageResponse> addColumnToCollection(Map<String, Integer> columns, Long id){
        ResponseEntity<MessageResponse> response = checkCollectionColumnsRequest(columns, id);
        if(response != null) {
            return response;
        }
        for(Map.Entry<String, Integer> column : columns.entrySet())
            collectionColumnRepository.save(new CollectionColumn(
                    id,
                    column.getKey(),
                    column.getValue()
            ));

        return ResponseEntity.ok().body(new MessageResponse("SAVED SUCCESSFULLY"));
    }

    private ResponseEntity<MessageResponse> checkCollectionColumnsRequest(Map<String, Integer> columns, Long id) {
        if(collectionColumnRepository.existsByCollectionId(id)){
            return ResponseEntity.status(405).body(new MessageResponse("COLUMNS ALREADY ADDED"));
        }

        if(columns.get(null) != null){
            return ResponseEntity.status(405).body(new MessageResponse("ERROR IN THE COLUMN NAMES"));
        }
        for(Integer type : columns.values()){
            if(type == null || type < 0 || type > 5){
                return ResponseEntity.status(405).body(new MessageResponse("FORMAT ERROR"));
            }
        }
        return null;
    }
}

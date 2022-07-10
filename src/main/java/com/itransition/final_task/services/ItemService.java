package com.itransition.final_task.services;

import com.itransition.final_task.dto.request.AddItemRequest;

import com.itransition.final_task.dto.response.CommentaryResponse;
import com.itransition.final_task.dto.response.ItemResponse;
import com.itransition.final_task.dto.response.MessageResponse;
import com.itransition.final_task.mapper.CommentaryMapper;
import com.itransition.final_task.mapper.ItemValueMapper;
import com.itransition.final_task.models.*;
import com.itransition.final_task.repository.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final UserService userService;

    @Lazy
    private final CollectionService collectionService;
    private final ItemRepository itemRepository;
    private final CollectionColumnService collectionColumnService;
    private final ItemValueService itemValueService;
    private final HashtagService hashtagService;

    private final ModelMapper modelMapper;

    public ResponseEntity<MessageResponse> deleteItemFromCollection(Long itemId, String jwt){
        System.out.println(itemId);
        Optional<Item> item = itemRepository.findById(itemId);
        if(item.isPresent()){
            Long userId = collectionService.getUserIdByCollectionId(item.get().getCollectionId());
            if(userService.checkIsCanEditData(userId, jwt)){
                itemRepository.deleteById(itemId);
                return ResponseEntity.ok().body(new MessageResponse("DELETED SUCCESSFULLY"));
            }
            return ResponseEntity.status(403).body(new MessageResponse("YOU DON'T HAVE PERMISSION TO DO IT"));
        }
        System.out.println(itemId);
        return ResponseEntity.status(405).body(new MessageResponse("ITEM NOT FOUND"));
    }

    public ResponseEntity<List<MessageResponse>> addItem(AddItemRequest item, String jwt){

        List<MessageResponse> messages = new ArrayList<>();

        Long userId = collectionService.getUserIdByCollectionId(item.getCollectionId());

        MessageResponse error = checkCollectionAndPermission(userId, jwt);

        if(error != null) {
            messages.add(error);
            return ResponseEntity.status(405).body(messages);
        }

        List<CollectionColumn> columns = collectionColumnService.findAllByCollectionId(item.getCollectionId());


        Item item1 = new Item(item.getCollectionId(), item.getName(), hashtagService.toDtos(item.getHashtags()));

        System.out.println(item1);

        itemRepository.save(item1);

        Map<ItemValue, String> values = new HashMap<>();

        columns.forEach(a -> values.put(new ItemValue(item1.getId(), a.getId(), a.getTypeCode()),
                item.getColumnValues().get(a.getId())));

        messages = checkAndInsertValuesToItem(values);

        if(messages.get(0).getMessage().equals("ADDED SUCCESSFULLY")) {
            return ResponseEntity.ok(messages);
        }

        itemRepository.deleteById(item1.getId());

        return ResponseEntity.status(500).body(messages);
    }

    private MessageResponse checkCollectionAndPermission(Long userId, String jwt) {
        if(userId == null){
            return new MessageResponse("COLLECTION NOT FOUND");
        }
        if(!userService.checkIsCanEditData(userId, jwt)){
            return new MessageResponse("YOU DON'T HAVE PERMISSION TO DO IT");
        }
        return null;
    }


    private List<MessageResponse> checkAndInsertValuesToItem(Map<ItemValue, String> values) {
        List<MessageResponse> messages = new ArrayList<>();
        values.forEach( (item,value) -> {
            Integer typeCode = item.getTypeCode();
            if(typeCode == 1)
                item.setString(value);
            else if (typeCode ==2) {
                try {
                    item.setInteger(Integer.parseInt(value));
                }catch(Exception e) {
                    messages.add(new MessageResponse("ERROR IN INTEGER TYPE"));
                }
            }
            else if(typeCode == 3){
                try {
                    item.setDoubleValue(Double.parseDouble(value));
                }catch(Exception e) {
                    messages.add(new MessageResponse("ERROR IN DOUBLE TYPE"));
                }
            }else if (typeCode == 4){
                try {
                    item.setBooleanValue(Boolean.parseBoolean(value));
                }catch(Exception e) {
                    messages.add(new MessageResponse("ERROR IN BOOLEAN TYPE"));
                }
            }
        });
        if(messages.size() == 0){
            values.keySet();
            itemValueService.saveAll(values.keySet());
            messages.add(new MessageResponse("ADDED SUCCESSFULLY"));
        }
        return messages;
    }

    public boolean existsById(Long itemId){
        return itemRepository.existsById(itemId);
    }

    public ResponseEntity<ItemResponse> getItemById(Long id){
        Optional<Item> item = itemRepository.findById(id);
        return item.map(value -> ResponseEntity.ok().body(itemToItemResponse(value))).orElseGet(() -> ResponseEntity.status(404).body(null));
    }

    public ItemResponse itemToItemResponse(Item item){
        ItemResponse itemResponse = ItemValueMapper.toDto(item);
        itemResponse.setHashtags(hashtagService.entitiesToResponses(item.getHashtags()));
        itemResponse.setCommentaries(commentaryEntitiesToResponses(item.getCommentaries()));
        return itemResponse;
    }

    public Set<CommentaryResponse> commentaryEntitiesToResponses(Set<Commentary> commentaries){
        return commentaries != null ? commentaries.stream().map(
                a -> {
                    CommentaryResponse commentaryResponse = modelMapper.map(a, CommentaryResponse.class);

                    commentaryResponse.setUsername(userService.getUsernameById(a.getUserId()));
                    return commentaryResponse;
                }).collect(Collectors.toSet()) : null;
    }
}

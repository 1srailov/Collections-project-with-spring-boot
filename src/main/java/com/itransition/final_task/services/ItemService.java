package com.itransition.final_task.services;

import com.itransition.final_task.dto.request.AddItemRequest;
import com.itransition.final_task.dto.response.ItemResponse;
import com.itransition.final_task.dto.response.MessageResponse;
import com.itransition.final_task.models.*;
import com.itransition.final_task.repository.*;
import com.itransition.final_task.validations.ValueTypeValidation;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final UserService userService;
    private final CollectionService collectionService;
    private final ItemRepository itemRepository;
    private final CollectionColumnService collectionColumnService;
    private final ItemValueService itemValueService;

    private final ModelMapper modelMapper;

    public ResponseEntity<MessageResponse> deleteItemFromCollection(Long itemId, String jwt){
        System.out.println(itemId);
        Optional<Item> item = itemRepository.findById(itemId);
        if(item.isPresent()){
            Long userId = collectionService.getUserIdById(item.get().getCollectionId());
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

        Long userId = collectionService.getUserIdById(item.getCollectionId());

        if(userId == null){
            messages.add(new MessageResponse("COLLECTION NOT FOUND"));
            return ResponseEntity.status(405).body(messages);
        }
        if(!userService.checkIsCanEditData(userId, jwt)){
            messages.add(new MessageResponse("YOU DON'T HAVE PERMISSION TO DO IT"));
            return ResponseEntity.status(405).body(messages);

        }

        List<CollectionColumn> columns = collectionColumnService.findAllByCollectionId(item.getCollectionId());
        Item item1 = new Item(item.getCollectionId(),
                item.getName(), new HashSet<>());
        itemRepository.save(item1);

        Map<ItemValue, String> values = new HashMap<>();
        columns.forEach(a -> values.put(new ItemValue(item1.getId(), a.getId(), a.getTypeCode()),
                item.getColumnValues().get(a.getId())));

        messages = checkAndInsertValuesToItem(values);
        if(messages.get(0).getMessage().equals("ADDED SUCCESSFULLY"))
             return ResponseEntity.ok(messages);

            itemRepository.deleteById(item1.getId());
            return ResponseEntity.status(500).body(messages);
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
}

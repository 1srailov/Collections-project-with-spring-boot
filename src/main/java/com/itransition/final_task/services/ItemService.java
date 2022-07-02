package com.itransition.final_task.services;

import com.itransition.final_task.dto.request.AddItemRequest;
import com.itransition.final_task.dto.response.MessageResponse;
import com.itransition.final_task.models.*;
import com.itransition.final_task.repository.*;
import com.itransition.final_task.validations.ValueTypeValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final UserService userService;
    private final CommentaryRepository commentaryRepository;
    private final CollectionRepository collectionRepository;
    private final ItemRepository itemRepository;
    private final CollectionColumnRepository cColumnRepository;
    private final LikeRepository likeRepository;

    private final ItemValueRepository itemValueRepository;

    public ResponseEntity<MessageResponse> deleteItemFromCollection(Long itemId, String jwt){
        System.out.println(itemId);
        Optional<Item> item = itemRepository.findById(itemId);
        if(item.isPresent()){
            Long userId = collectionRepository.getUserIdById(item.get().getCollectionId());
            if(userService.checkIsCanEditData(userId, jwt)){
                itemRepository.deleteById(itemId);
                return ResponseEntity.ok().body(new MessageResponse("DELETED SUCCESSFULLY"));
            }
            return ResponseEntity.status(403).body(new MessageResponse("YOU DON'T HAVE PERMISSION TO DO IT"));
        }
        System.out.println(itemId);
        return ResponseEntity.status(405).body(new MessageResponse("ITEM NOT FOUND"));
    }


    public ResponseEntity<MessageResponse> addCommentToItem(String text, Long itemId, String jwt) {
        if (itemRepository.existsById(itemId)) {
            commentaryRepository.save(new Commentary(text, itemId, userService.getUserIdFromJwt(jwt)));
            return ResponseEntity.ok().body(new MessageResponse("COMMENTARY SUCCESSFULLY ADDED"));
        }
        return ResponseEntity.status(405).body(new MessageResponse("ITEM NOT FOUND"));
    }

    public void likeOrDislikeItem(Long itemId, String jwt){
        Long userId =  userService.getUserIdFromJwt(jwt);
        if(likeRepository.existsByUserIdAndItemId(itemId, userId)){
            likeRepository.deleteLikeByUserIdAndItemId(itemId, userId);
        }
        likeRepository.save(new Like(userId, itemId));
    }

    public ResponseEntity<List<MessageResponse>> addItem(AddItemRequest item, HttpServletRequest request){
        List<CollectionColumn> columns = cColumnRepository.findAllByCollectionId(item.getCollectionId());
        Item item1 = new Item(item.getCollectionId(),
                item.getName(), new HashSet<>());
        itemRepository.save(item1);
        Map<ItemValue, String> values = new HashMap<>();
        columns.forEach(a -> values.put(new ItemValue(item1.getId(), a.getId(), a.getTypeCode()),
                item.getColumnValues().get(a.getId())));

        List<MessageResponse> messages = checkAndInsertValuesToItem(values);
        return messages.get(0).equals("ADDED SUCCESSFULLY") ?
                ResponseEntity.ok(messages) : ResponseEntity.status(500).body(messages);
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
            itemValueRepository.save(item);
        });
        if(messages.size() == 0){
            messages.add(new MessageResponse("ADDED SUCCESSFULLY"));
        }
        return messages;
    }
}

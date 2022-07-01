package com.itransition.final_task.services;

import com.itransition.final_task.dto.response.MessageResponse;
import com.itransition.final_task.models.Commentary;
import com.itransition.final_task.models.Item;
import com.itransition.final_task.models.Like;
import com.itransition.final_task.repository.CollectionRepository;
import com.itransition.final_task.repository.CommentaryRepository;
import com.itransition.final_task.repository.ItemRepository;
import com.itransition.final_task.repository.LikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final UserService userService;
    private final CommentaryRepository commentaryRepository;
    private final CollectionRepository collectionRepository;
    private final ItemRepository itemRepository;

    private final LikeRepository likeRepository;

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
        if (itemRepository.findById(itemId).isPresent()) {
            commentaryRepository.save(new Commentary(text, itemId, userService.getUserIdFromJwt(jwt)));
            return ResponseEntity.ok().body(new MessageResponse("COMMENTARY SUCCESSFULLY ADDED"));
        }
        return ResponseEntity.status(405).body(new MessageResponse("ITEM NOT FOUND"));
    }

    public void likeOrDislikeItem(Long itemId, String jwt){
        Long userId =  userService.getUserIdFromJwt(jwt);
        if(likeRepository.findByUserIdAndItemId(itemId, userId).isPresent()){
            likeRepository.deleteLikeByUserIdAndItemId(itemId, userId);
        }
        likeRepository.save(new Like(userId, itemId));
    }
}

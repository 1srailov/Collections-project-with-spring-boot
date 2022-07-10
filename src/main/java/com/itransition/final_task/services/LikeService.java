package com.itransition.final_task.services;

import com.itransition.final_task.models.Like;
import com.itransition.final_task.repository.LikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;

    private final UserService userService;

    public void likeOrDislikeItem(Long itemId, String jwt){
        Long userId =  userService.getUserIdFromJwt(jwt);
        if(likeRepository.existsByUserIdAndItemId(userId, itemId)){
            likeRepository.deleteLikeByUserIdAndItemId(userId, itemId);
        }
        else {
            likeRepository.save(new Like(userId, itemId));
        }
    }


}

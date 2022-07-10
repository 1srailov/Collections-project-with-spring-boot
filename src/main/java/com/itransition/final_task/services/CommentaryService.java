package com.itransition.final_task.services;


import com.itransition.final_task.dto.response.CommentaryResponse;
import com.itransition.final_task.dto.response.MessageResponse;
import com.itransition.final_task.models.Commentary;
import com.itransition.final_task.repository.CommentaryRepository;
import io.swagger.models.Model;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentaryService{

    private final CommentaryRepository commentaryRepository;
    @Lazy
    private final ItemService itemService;

    private final ModelMapper modelMapper;

    private UserService userService;

    public ResponseEntity<MessageResponse> addCommentToItem(String text, Long itemId, String jwt) {
        if (itemService.existsById(itemId)) {
            commentaryRepository.save(new Commentary(text, itemId, userService.getUserIdFromJwt(jwt)));
            return ResponseEntity.ok().body(new MessageResponse("COMMENTARY SUCCESSFULLY ADDED"));
        }
        return ResponseEntity.status(405).body(new MessageResponse("ITEM NOT FOUND"));
    }



}

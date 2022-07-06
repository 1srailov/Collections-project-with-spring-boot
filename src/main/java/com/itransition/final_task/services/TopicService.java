package com.itransition.final_task.services;


import com.itransition.final_task.dto.response.TopicResponse;
import com.itransition.final_task.repository.TopicRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TopicService {

    private final TopicRepository topicRepository;

    private final ModelMapper modelMapper;

    public String getTopicNameById(Integer id){
        return topicRepository.getTopicNameById(id);
    }

    public ResponseEntity<List<TopicResponse>> getAll(){
        List<TopicResponse> topics = new ArrayList<>();
        topicRepository.findAll().forEach(a -> topics.add(modelMapper.map(a, TopicResponse.class)));
        return ResponseEntity.ok().body(topics);
    }
}

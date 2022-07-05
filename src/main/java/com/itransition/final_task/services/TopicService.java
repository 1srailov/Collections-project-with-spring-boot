package com.itransition.final_task.services;


import com.itransition.final_task.repository.TopicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TopicService {

    private final TopicRepository topicRepository;


    public String getTopicNameById(Integer id){
        return topicRepository.getTopicNameById(id);
    }
}

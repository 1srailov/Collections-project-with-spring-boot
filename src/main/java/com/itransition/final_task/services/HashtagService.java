package com.itransition.final_task.services;

import com.itransition.final_task.dto.response.CollectionColumnResponse;
import com.itransition.final_task.dto.response.HashtagResponse;
import com.itransition.final_task.models.Hashtag;
import com.itransition.final_task.repository.HashtagRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Service
public class HashtagService {

    @Autowired
    private HashtagRepository hashtagRepository;
    @Autowired
    private ModelMapper modelMapper;

    public ResponseEntity<List<HashtagResponse>> getAllHashtags(){
        List<Hashtag> hashtags = hashtagRepository.findAll();
        List<HashtagResponse> hashtagResponses = modelMapper.map(hashtags, new TypeToken<List<HashtagResponse>>(){}.getType());

        return ResponseEntity.ok().body(hashtagResponses);
    }

    public Set<Hashtag> toDtos(Set<Long> hashtagIds) {
        Set<Hashtag> hashtags = new HashSet<>();
        hashtagIds.forEach(a -> hashtags.add(new Hashtag(a)));
        return hashtags;
    }
}

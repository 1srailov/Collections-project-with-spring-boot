package com.itransition.final_task.services;

import com.itransition.final_task.models.Hashtag;
import com.itransition.final_task.repository.HashtagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;


@Service
public class HashtagService {

    @Autowired
    private HashtagRepository hashtagRepository;

    public List<Hashtag> addHashtags(Set<Long> ids){
       return hashtagRepository.findAllById(ids);
   }
}

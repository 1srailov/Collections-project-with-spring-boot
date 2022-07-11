package com.itransition.final_task.mapper;

import com.itransition.final_task.dto.response.HashtagResponse;
import com.itransition.final_task.models.Hashtag;
import org.mapstruct.Mapper;

import java.util.Set;


@Mapper(componentModel = "spring")
public interface HashTagsMapper {

    Hashtag toEntity(Long id);

    Set<Hashtag> toDtos(Set<Long> hashtagIds);

    HashtagResponse entitiesToResponses(Hashtag hashtags);
    Set<HashtagResponse> entitiesToResponses(Set<Hashtag> hashtags);


}
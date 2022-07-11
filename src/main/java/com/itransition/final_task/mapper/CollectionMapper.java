package com.itransition.final_task.mapper;


import com.itransition.final_task.dto.request.CollectionRequest;
import com.itransition.final_task.dto.response.HashtagResponse;
import com.itransition.final_task.models.Collection;
import com.itransition.final_task.models.Hashtag;
import org.mapstruct.Mapper;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface CollectionMapper {

    Collection toEntity(CollectionRequest collectionRequest);

//    Set<Hashtag> toDtos(Set<Long> hashtagIds);
//
//    HashtagResponse entitiesToResponses(Hashtag hashtags);
//    Set<HashtagResponse> entitiesToResponses(Set<Hashtag> hashtags);

}

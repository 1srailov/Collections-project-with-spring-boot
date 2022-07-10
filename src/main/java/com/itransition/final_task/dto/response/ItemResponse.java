package com.itransition.final_task.dto.response;

import com.itransition.final_task.models.Commentary;
import com.itransition.final_task.models.Hashtag;
import com.itransition.final_task.models.ItemValue;
import com.itransition.final_task.models.Like;
import com.itransition.final_task.repository.ItemValueRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemResponse{
    private Long id;
    private Long collectionId;
    private String name;
    private Set<HashtagResponse> hashtags;
    private Set<CommentaryResponse> commentaries;
    private Integer likeCount;
    private List<ItemValueResponse> values;

    public ItemResponse(Long id, Long collectionId, String name, int size, List<ItemValueResponse> itemValues) {
    this.id = id;
    this.collectionId = collectionId;
    this.name = name;
    this.likeCount = size;
    this.values = itemValues;
    }
}

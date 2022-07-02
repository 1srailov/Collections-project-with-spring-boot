package com.itransition.final_task.dto.request;

import com.itransition.final_task.models.Hashtag;
import jdk.jfr.DataAmount;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddItemRequest {

    private Long collectionId;

    private String name;

    private Set<Integer> hashtags;

    private HashMap<Long, String> columnValues;
}

package com.itransition.final_task.dto.request;

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

    private Set<Long> hashtags;

    private HashMap<Long, String> columnValues;
}

package com.itransition.final_task.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class CollectionColumnsRequest {

    private Long collectionId;

    @NotNull
    private HashMap<String, Integer> columns;
}

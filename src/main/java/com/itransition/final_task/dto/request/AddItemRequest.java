package com.itransition.final_task.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddItemRequest {

    @NotNull
    private Long collectionId;

    @NotNull
    @NotBlank
    private String name;

    @NotNull
    private Set<Long> hashtags;

    @NotNull
    private HashMap<Long, String> columnValues;
}

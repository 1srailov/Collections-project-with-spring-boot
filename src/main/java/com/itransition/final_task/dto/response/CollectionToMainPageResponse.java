package com.itransition.final_task.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CollectionToMainPageResponse {
    private Long id;
    private String author;
    private String topic;
    private String name;
    private String imageAddress;
}

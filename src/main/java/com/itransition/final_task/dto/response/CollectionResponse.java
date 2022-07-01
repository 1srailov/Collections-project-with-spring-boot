package com.itransition.final_task.dto.response;

import com.itransition.final_task.models.Item;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CollectionResponse{
    private Long id;
    private String author;
    private Integer topicId;
    private String topic;
    private String name;
    private String description;
    private String imageAddress;
    private Set<Item> items;
}

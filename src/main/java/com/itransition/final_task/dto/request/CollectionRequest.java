package com.itransition.final_task.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CollectionRequest{
    private Integer topicId;
    private String name;
    private String description;
    private MultipartFile image;
}

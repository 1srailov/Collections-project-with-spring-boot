package com.itransition.final_task.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CollectionRequest{

    @NotNull
    private Integer topicId;

    @NotNull
    private String name;

    @NotNull
    private String description;

    private HashMap<String, Integer> columns;
}

package com.itransition.final_task.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CollectionColumnResponse{
    private Long id;
    private Integer typeCode;
    private String name;
}

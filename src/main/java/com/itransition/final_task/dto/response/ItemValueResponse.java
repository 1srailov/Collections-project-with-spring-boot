package com.itransition.final_task.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ItemValueResponse {
    private Long id;
    private Long columnId;
    private String value;
}

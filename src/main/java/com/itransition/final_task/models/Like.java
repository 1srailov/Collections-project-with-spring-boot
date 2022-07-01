package com.itransition.final_task.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "likes")
public class Like{

    @Id
    @GeneratedValue(generator = "like_seq")
    @SequenceGenerator(name = "like_seq", sequenceName = "like_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "item_id")
    private Long itemId;

    public Like(Long userId, Long itemId){
        this.userId = userId;
        this.itemId = itemId;
    }
}

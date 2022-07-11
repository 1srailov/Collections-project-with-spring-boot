package com.itransition.final_task.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@Entity
@Table(name = "commentaries")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Commentary {
    @Id
    @GeneratedValue(generator = "commentaries_seq")
    @SequenceGenerator(name = "commentaries_seq", sequenceName = "commentaries_id_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "item_id")
    private Long itemId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "text")
    private String text;

    public Commentary(String text, Long itemId, Long userId){
        this.itemId = itemId;
        this.userId = userId;
        this.text = text;
    }
}

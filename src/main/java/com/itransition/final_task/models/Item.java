package com.itransition.final_task.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "items")
public class Item{
    @Id
    @GeneratedValue(generator = "item_seq")
    @SequenceGenerator(name = "item_seq", sequenceName = "collection_item_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "collection_id")
    private Long collectionId;

    @Column(name = "name")
    private String name;

    @ManyToMany
    Set<Hashtag> hashtags;

    @OneToMany(mappedBy="itemId")
    private Set<Commentary> commentaries;

    @OneToMany(mappedBy = "itemId")
    private Set<Like> likes;
}

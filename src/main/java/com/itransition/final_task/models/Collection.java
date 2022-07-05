package com.itransition.final_task.models;

import jdk.jfr.SettingDescriptor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "collections")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Collection {
    @Id
    @GeneratedValue(generator = "collections_seq")
    @SequenceGenerator(name = "collections_seq", sequenceName = "collections_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "topic_id")
    private Integer topicId;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "image_address")
    private String imageAddress;

    @OneToMany(mappedBy="collectionId")
    private Set<Item> items;

    @OneToMany(mappedBy = "collectionId")
    private Set<CollectionColumn> collectionColumns;
}
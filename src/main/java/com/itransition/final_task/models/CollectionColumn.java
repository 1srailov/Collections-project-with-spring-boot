package com.itransition.final_task.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@Entity
@Table(name = "collection_columns")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CollectionColumn{
    @Id
    @GeneratedValue(generator = "collection_rows_seq")
    @SequenceGenerator(name = "collection_rows_seq", sequenceName = "collection_rows_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "collection_id")
    private Long collectionId;

    @Column(name = "name")
    private String columnName;

    @Column(name = "type_code")
    private Integer typeCode;

    public CollectionColumn(Long collectionId, String columnName, Integer typeCode){
        this.collectionId = collectionId;
        this.columnName = columnName;
        this.typeCode = typeCode;
    }

}

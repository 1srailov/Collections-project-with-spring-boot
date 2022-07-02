package com.itransition.final_task.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "item_values")
public class ItemValue {
    @Id
    @GeneratedValue(generator = "item_values_seq")
    @SequenceGenerator(name = "item_values_seq", sequenceName = "item_values_id_seq", allocationSize = 1)
    private Long id;
    @Column(name = "col_column_id")
    private Long collectionColumnId;
    @Column(name = "item_id")
    private Long itemId;

    @Column(name = "type_code")
    private Integer typeCode;

    @Column(name = "string")
    private String string;

    @Column(name = "integer")
    private Integer integer;

    @Column(name = "double")
    private Double doubleValue;

    @Column(name = "boolean")
    private Boolean booleanValue;

    @Column(name = "date")
    private Date date;

    public ItemValue(Long itemId, long cColumdId, Integer typeCode){
        this.itemId = itemId;
        this.collectionColumnId = cColumdId;
        this.typeCode = typeCode;
    }
}

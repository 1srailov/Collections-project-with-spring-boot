package com.itransition.final_task.mapper;

import com.itransition.final_task.dto.response.ItemResponse;
import com.itransition.final_task.dto.response.ItemValueResponse;
import com.itransition.final_task.models.Item;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

public class ItemValueMapper {

    public static ItemResponse toDto(Item item){
        List<ItemValueResponse> itemValues = new ArrayList<>();

        for(int i = 0; i < item.getValues().size(); i++){
            Integer type = item.getValues().get(i).getTypeCode();
            ItemValueResponse itemValueResponse = new ItemValueResponse(item.getValues().get(i).getId(), item.getValues().get(i).getCollectionColumnId(), "");
            if(type == 1){
                itemValueResponse.setValue(item.getValues().get(i).getString());
            } else if (type == 2) {
                itemValueResponse.setValue(item.getValues().get(i).getInteger() + "");
            } else if (type == 3) {
                itemValueResponse.setValue(item.getValues().get(i).getDoubleValue() + "");
            } else if (type == 4) {
               itemValueResponse.setValue(item.getValues().get(i).getBooleanValue() + "");
            }
            itemValues.add(itemValueResponse);
        }
        return new ItemResponse(item.getId(), item.getCollectionId(),
                item.getName(), item.getLikes().size(), itemValues);
    }
}

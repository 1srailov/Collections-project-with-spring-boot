package com.itransition.final_task.services;

import com.itransition.final_task.models.ItemValue;
import com.itransition.final_task.repository.ItemValueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
@Service
@RequiredArgsConstructor
public class ItemValueService {

    private final ItemValueRepository itemValueRepository;
    public void saveAll(Set<ItemValue> keySet){
        itemValueRepository.saveAll(keySet);
    }
}

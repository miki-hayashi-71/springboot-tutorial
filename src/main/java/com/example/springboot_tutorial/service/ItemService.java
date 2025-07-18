package com.example.springboot_tutorial.service;

import com.example.springboot_tutorial.model.Item;
import java.util.List;

public interface ItemService {

    List<Item> findAll();

    Item findById(Long id);

    Item createItem(Item item);

    Item updateItem(Long id, Item newItemData);

    void deleteItem(Long id);
}

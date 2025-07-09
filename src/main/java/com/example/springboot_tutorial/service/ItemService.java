package com.example.springboot_tutorial.service;

import com.example.springboot_tutorial.model.Item; // Itemクラスを使うため
import com.example.springboot_tutorial.repository.ItemRepository;
import org.springframework.stereotype.Service;  // @serviceのアノテーション使用時に必要

import java.util.List;  // コレクションフレームワークのListを使うため

@Service  // ビジネスロジックを担当するServiceであることを明示
public class ItemService {

    private final ItemRepository itemRepository;

    // コンストラクタでItemRepositoryを受け取る（DI:依存性の注入）
    // ItemServiceは自分でnewせず、ItemRepositoryを引数とすることで、ItemRepositoryが受け取ったものを受け取る
    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    // DBから全件取得
    public List<Item> findAll() {
        return itemRepository.findAll();
    }

    // IDを指定して1件取得するメソッド（なければnullを返す）
    public Item findById(Long id) {
        return itemRepository.findById(id).orElse(null);
    }
}

package com.example.springboot_tutorial.service;

import com.example.springboot_tutorial.model.Item; // Itemクラスを使うため
import org.springframework.stereotype.Service;  // @serviceのアノテーション使用時に必要

import java.util.List;  // コレクションフレームワークのListを使うため


@Service  // ビジネスロジックを担当するServiceであることを明示
public class ItemService {

    // 一旦仮でDBの代わりのアイテムリストを作成（findAll:すべてのアイテムを取得する）
    public List<Item> findAll() {

        // アイテムリストを返す
        return List.of(
                new Item(1L, "鉛筆", 100),
                new Item(2L, "消しゴム", 150),
                new Item(3L, "ノート", 200)
        );
    }
}

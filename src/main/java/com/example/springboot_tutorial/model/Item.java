package com.example.springboot_tutorial.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity // このクラスがDBのテーブルに対応することを示す
public class Item {

    @Id // テーブルn主キーであることを明示
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ＠Idとセットで使用、IDを自動採番
    private Long id;
    private String name;
    private Integer price;

    // 引数なしコンストラクタ(JPAのため)
    public Item() {
    }

    // コンストラクタ
    public Item(Long id, String name, Integer price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    // privateなフィールドの値を取得するた目のゲッターメソッド
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getPrice() {
        return price;
    }
}

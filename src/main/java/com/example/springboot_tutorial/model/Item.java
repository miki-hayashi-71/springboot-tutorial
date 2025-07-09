package com.example.springboot_tutorial.model;


public class Item {

    private Long id;
    private String name;
    private Integer price;

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

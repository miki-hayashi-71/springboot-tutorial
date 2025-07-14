package com.example.springboot_tutorial.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // ＠@Getter, @Setter, @ToStringなどをまとめて設定
@Builder
@AllArgsConstructor // 全フィールドを引数に持つコンストラクター
@NoArgsConstructor // 引数なしのコンストラクター(JPAに必要)
@Entity // このクラスがDBのテーブルに対応することを示す
public class Item {

    @Id // テーブルに主キーであることを明示
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ＠Idとセットで使用、IDを自動採番
    private Long id;
    private String name;
    private Integer price;
}

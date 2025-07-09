package com.example.springboot_tutorial.repository;

import com.example.springboot_tutorial.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository //データベースとのやり取り担当
public interface ItemRepository extends JpaRepository<Item, Long> {
    // 何も書かずとも、JpaRepositoryの継承だけで基本的なDB操作をやってくれる。
}

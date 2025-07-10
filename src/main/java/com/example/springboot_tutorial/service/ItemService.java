package com.example.springboot_tutorial.service;

import com.example.springboot_tutorial.model.Item; // Itemクラスを使うため
import com.example.springboot_tutorial.repository.ItemRepository;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
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

    /**
     * DBからアイテム一覧を取得
     * GET /items
     */
    public List<Item> findAll() {
        return itemRepository.findAll();
    }

    /**
     * IDを指定してアイテムを1件取得
     * なけれなnullを返す
     * GET /items/{id}
     */
    public Item findById(Long id) {
        return itemRepository.findById(id).orElse(null);
    }

    /**
     * 新しいアイテムを1件登録
     * @param item 登録するアイテム情報
     * @return データベースに保存されたアイテム情報（ID採番）
     */
    public Item createItem(Item item) {
        return itemRepository.save(item); // saveはIDがなければINSERTを実行する
    }

    /**
     * IDを指定してアイテムを更新する
     * @param id 更新対象のアイテムID
     * @param newItemData 更新後のアイテム情報
     * @return 更新されたアイテム情報
     */
    public Item updateItem(Long id, Item newItemData) {
        // 更新対象のアイテムがあるか確認
        Item existingItem = itemRepository.findById(id).orElse(null);

        if (existingItem != null){
            // 既存のアイテム情報を更新
            existingItem.setName(newItemData.getName());
            existingItem.setPrice(newItemData.getPrice());
            // 更新を実行
            return itemRepository.save(existingItem);
        }
        // アイテムが見つからなかった場合はnull
        return null;
    }

    /**
     * IDを指定してアイテムを削除する
     * @param id 削除対象のアイテムID
     */
    public void deleteItem(Long id) {
        itemRepository.deleteById(id);
    }
}

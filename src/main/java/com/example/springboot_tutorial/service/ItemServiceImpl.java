package com.example.springboot_tutorial.service;

import com.example.springboot_tutorial.exception.ResourceNotFoundException;
import com.example.springboot_tutorial.model.Item; // Itemクラスを使うため
import com.example.springboot_tutorial.repository.ItemRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;  // @serviceのアノテーション使用時に必要

import java.util.List;  // コレクションフレームワークのListを使うため

@Service  // ビジネスロジックを担当するServiceであることを明示
public class ItemServiceImpl implements ItemService{

    private final ItemRepository itemRepository;

    // コンストラクタでItemRepositoryを受け取る（DI:依存性の注入）
    // ItemServiceは自分でnewせず、ItemRepositoryを引数とすることで、ItemRepositoryが受け取ったものを受け取る
    public ItemServiceImpl(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    /**
     * DBからアイテム一覧を取得
     * GET /items
     */
    @Override
    public List<Item> findAll() {
        return itemRepository.findAll();
    }

    /**
     * IDを指定してアイテムを1件取得
     * なければ例外をスロー
     * GET /items/{id}
     */
    @Override
    public Item findById(Long id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found with id: " + id));
    }

    /**
     * 新しいアイテムを1件登録
     * @param item 登録するアイテム情報
     * @return データベースに保存されたアイテム情報（ID採番）
     */
    @Override
    public Item createItem(Item item) {
        if (item.getId() != null) {
            throw new IllegalArgumentException("A new item cannot have an ID");
        }
        try {
            return itemRepository.save(item); // saveはIDがなければINSERTを実行する
        } catch (DataAccessException e) {
            throw new RuntimeException("Database registration failed.", e);
        }
    }

    /**
     * IDを指定してアイテムを更新する
     * @param id 更新対象のアイテムID
     * @param newItemData 更新後のアイテム情報
     * @return 更新されたアイテム情報
     */
    @Override
    public Item updateItem(Long id, Item newItemData) {
        // 存在チェックとオブジェクトの取得
        Item existingItem = findById(id);
        // 情報を上書き
        existingItem.setName(newItemData.getName());
        existingItem.setPrice(newItemData.getPrice());

        try {
            // 更新を実行
            return itemRepository.save(existingItem);
        } catch (DataAccessException e){
            throw new RuntimeException("Database update failed.", e);
        }
    }

    /**
     * IDを指定してアイテムを削除する
     * @param id 削除対象のアイテムID
     */
    @Override
    public void deleteItem(Long id) {
        // 存在チェックとオブジェクトの取得
        Item itemToDelete = findById(id);

        try {
            // 削除を実行
            itemRepository.delete(itemToDelete);
        } catch (DataAccessException e) {
            throw new RuntimeException("Database deletion failed.", e);
        }
    }
}

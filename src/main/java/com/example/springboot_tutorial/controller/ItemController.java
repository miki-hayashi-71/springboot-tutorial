package com.example.springboot_tutorial.controller;

import com.example.springboot_tutorial.model.Item;  // Itemクラス
import com.example.springboot_tutorial.service.ItemService;  // ItemService(newしてる)
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    // コンストラクタでItemServiceを受け取る（DI:依存性の注入）
    // ItemControllerは自分でnewせず、ItemServiceを引数で受け取ることで、ItemServiceがnewしたものを受け取る
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    /**
     * アイテム一覧を取得するAPI
     * GET /items
     */
    @GetMapping
    public List<Item> getItems() {
        // Serviceを呼び出して全アイテムを取得
        return itemService.findAll();
    }

    /**
     * IDを指定してアイテムを1件取得するAPI
     * GET /items/{id}
     */
    @GetMapping("/{id}")
    public Item getItemById(@PathVariable Long id) { // @PathVariableでURLの{id}を受け取る
        return itemService.findById(id);
    }

    /**
     * 新しいアイテムを1件登録するAPI
     * POST /items
     */
    @Operation(summary = "新しいアイテムを登録する")  // OpenAPI
    @PostMapping  // POSTリクエストの処理を行う
    @ResponseStatus(HttpStatus.CREATED)  // 成功した時、201 Created を返す
    // JSONで受け取ったデータをJavaオブジェクトに変換し受け取る。itemServiceを経由してidが採番されたItemを受け取る
    public Item createItem(@RequestBody Item item) {
        return itemService.createItem(item);
    }
}

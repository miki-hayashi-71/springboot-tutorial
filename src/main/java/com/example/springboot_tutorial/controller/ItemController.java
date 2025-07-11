package com.example.springboot_tutorial.controller;

import com.example.springboot_tutorial.model.Item;  // Itemクラス
import com.example.springboot_tutorial.service.ItemService;  // ItemService(newしてる)
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
    @Operation(summary = "アイテム一覧を取得する")
    @GetMapping
    public List<Item> getItems() {
        // Serviceを呼び出して全アイテムを取得
        return itemService.findAll();
    }

    /**
     * IDを指定してアイテムを1件取得するAPI
     * GET /items/{id}
     */
    @Operation(summary = "特定のアイテムを1件取得する")
    @GetMapping("/{id}")
    public Item getItemById(
            @Parameter(description = "取得対象アイテムのID") @PathVariable Long id) { // @PathVariableでURLの{id}を受け取る
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

    /**
     * IDを指定してアイテムを1件更新するAPI
     * PUT /items/{id}
     */
    @Operation(summary = "既存のアイテムを更新する") // OpenAPI
    @PutMapping("/{id}")  // PUTリクエストの処理を行う
    public Item updateItem(
            @Parameter(description = "更新対象アイテムのID") @PathVariable Long id,
            @RequestBody Item item) {
        return itemService.updateItem(id, item);
    }

    /**
     * IDを指定してアイテムを1件削除するAPI
     * DELETE /items/{id}
     */
    @Operation(summary = "既存のアイテムを削除する") // OpeAPI
    @DeleteMapping("/{id}")  // DELETEリクエストの処理を行う
    @ResponseStatus(HttpStatus.NO_CONTENT)  // 成功した時、204 No Contentを返す
    public void deleteItem(
            @Parameter(description = "削除対象アイテムのID") @PathVariable Long id) {
        itemService.deleteItem(id);
    }
}

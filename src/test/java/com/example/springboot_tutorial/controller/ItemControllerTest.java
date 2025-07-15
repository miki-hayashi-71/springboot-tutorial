package com.example.springboot_tutorial.controller;

import com.example.springboot_tutorial.model.Item;
import com.example.springboot_tutorial.service.ItemService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

    // HTTPリクエストを送信するためのクライアント
    @Autowired
    private MockMvc mockMvc;

    // Service層のモック化
    @MockBean
    private ItemService itemService;

    @Autowired
    private ObjectMapper objectMapper;

    private Item item1;
    private Item item2;

    @BeforeEach
    void setUp() {
        item1 = Item.builder()
                .id(1L)
                .name("テストアイテム1")
                .price(1000)
                .build();
        item2 = Item.builder()
                .id(2L)
                .name("テストアイテム2")
                .price(2000)
                .build();
    }

    @Test
    @DisplayName("全アイテムを取得するAPIのテスト")
    void getItems() throws Exception{
        // Arrange: ServiceのfindAllメソッドが呼ばれた際にallItmesリストを返すよう設定
        List<Item> allItems = Arrays.asList(item1, item2);
        when(itemService.findAll()).thenReturn(allItems);

        // Act&Assert
        mockMvc.perform(get("/items")) // APIの呼び出し
                .andExpect(status().isOk()) // ステータスコードが200 OK
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)) // レスポンスがJSON形式
                .andExpect(jsonPath("$", hasSize(2))) // JSONの要素の数が2つ
                .andExpect(jsonPath("$[0].name", is("テストアイテム1"))) // 1番目の要素の名前チェック
                .andExpect(jsonPath("$[1].name", is("テストアイテム2"))); // 2番目の要素の名前チェック
    }

    @Test
    @DisplayName("指定したIDのアイテムを1件取得するAPIのテスト")
    void getItemById_Success() throws Exception{
        // Arrange: id=1Lを指定した時、item1を返すように設定
        when(itemService.findById(1L)).thenReturn(item1);

        // Act&Assert
        mockMvc.perform(get("/items/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1))) //
                .andExpect(jsonPath("$.name", is("テストアイテム1")));
    }

    @Test
    @DisplayName("アイテムを新規作成するAPIのテスト")
    void createItem() throws Exception {
        // Arrange: 更新前後のアイテムをそれぞれ作成。createItemが呼び出された時にsavedItemを返すよう設定
        Item newItem = Item.builder()
                .id(null)
                .name("登録前アイテム")
                .price(3000)
                .build();
        Item savedItem = Item.builder()
                .id(3L)
                .name("登録後アイテム")
                .price(5000)
                .build();
        when(itemService.createItem(any(Item.class))).thenReturn(savedItem);

        // Act&Assert
        mockMvc.perform(post("/items")
                .contentType(MediaType.APPLICATION_JSON) // リクエストの形式をJSONに設定
                .content(objectMapper.writeValueAsString(newItem))) // リクエストボディにnewItemをJSON化した文字列を設定
                .andExpect(status().isCreated()) // ステータスコードが 201 created
                .andExpect(jsonPath("$.id", is(3)))
                .andExpect(jsonPath("$.name", is("登録後アイテム")));
    }

    @Test
    @DisplayName("アイテムを更新するAPIのテスト")
    void updatedItem() throws Exception {
        // Arrange: 更新後アイテムを用意。id=1が任意のItemオブジェクトに渡された時、updateItemを返すよう設定
        Item updatedItem = Item.builder()
                .id(1L)
                .name("更新後アイテム")
                .price(3000)
                .build();
        when(itemService.updateItem(eq(1L), any(Item.class))).thenReturn(updatedItem);

        // Act&Assert
        mockMvc.perform(put("/items/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedItem)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("更新後アイテム")))
                .andExpect(jsonPath("$.price", is(3000)));
    }

    @Test
    @DisplayName("アイテムを削除するAPIのテスト")
    void deleteItem() throws Exception {
        // Arrange: 戻り値がないため、deleteItemが呼ばれたら、何もせずに正常に処理を終えるよう設定
        doNothing().when(itemService).deleteItem(1L);

        // Act&Assert
        mockMvc.perform(delete("/items/{id}", 1L))
                .andExpect(status().isNoContent());  // ステータスコードが204 No Content
    }

}

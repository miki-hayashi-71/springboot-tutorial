package com.example.springboot_tutorial.controller;

import com.example.springboot_tutorial.model.Item;
import com.example.springboot_tutorial.repository.ItemRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ItemControllerIntegrationTest {

    // HTTPリクエストを送信するためのクライアント
    @Autowired
    private TestRestTemplate restTemplate;

    // テスト後のDBの状態を確認するために本物のItemRepositoryを注入
    @Autowired
    private ItemRepository itemRepository;

    @Test
    void createItemでアイテムを登録し取得できる() {
        // アイテムの登録
        // Arrange
        Item newItem = Item.builder()
                .id(null)
                .name("結合テスト用アイテム")
                .price(9999)
                .build();

        // Act POSTリクエストでアイテムを新規登録
        // postForEntity(URLのパス、リクエストに含めるオブジェクト(自動でJSONに変換される)、レスポンスボディをどのクラスに変換するか)
        ResponseEntity<Item> postResponse = restTemplate.postForEntity("/items", newItem, Item.class);

        // Assert POSTリクエストの結果
        assertEquals(HttpStatus.CREATED, postResponse.getStatusCode());
        assertNotNull(postResponse.getBody());
        assertNotNull(postResponse.getBody().getId());
        assertEquals("結合テスト用アイテム", postResponse.getBody().getName());


        // 登録したアイテムの取得
        // Arrange
        Long createdItemId = postResponse.getBody().getId();

        // Act GETリクエストで今登録したアイテムを取得
        ResponseEntity<Item> getResponse = restTemplate.getForEntity("/items/" + createdItemId, Item.class);

        // Assert GETリクエストの結果
        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        assertNotNull(getResponse.getBody());
        assertEquals(createdItemId, getResponse.getBody().getId());
    }
}

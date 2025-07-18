package com.example.springboot_tutorial.Service;

import com.example.springboot_tutorial.exception.ResourceNotFoundException;
import com.example.springboot_tutorial.repository.ItemRepository;
import com.example.springboot_tutorial.model.Item;
import com.example.springboot_tutorial.service.ItemServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ItemServiceImplTest {

    @Mock  // 偽物のItemRepositoryを作成
    private ItemRepository itemRepository;

    @InjectMocks  // モックを注入した本物のItemServiceImplを作成する
    private ItemServiceImpl itemService;

    private Item item1;

    // 各テストの共通処理でアイテム1を準備
    @BeforeEach
    void setUp () {
        item1 = Item.builder()
                .id(1L)
                .name("テストアイテム1")
                .price(2000)
                .build();
    }

    @Test
    @DisplayName("全アイテムを取得するテスト")
    void findAll() {
        // Arrange: アイテム2を作り、itemListを作成。findAll()したらitemListを返すよう設定
        Item item2 = Item.builder()
                .id(2L)
                .name("テストアイテム2")
                .price(3000)
                .build();
        List<Item> itemList = Arrays.asList(item1, item2);
        when(itemRepository.findAll()).thenReturn(itemList);

        // Act
        List<Item> result = itemService.findAll();

        // Assert: リストのサイズが2件であることを確認 && findAllが1回呼ばれたことを確認
        assertThat(result).hasSize(2);
        verify(itemRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("指定したIDのアイテムを1件取得するテスト(成功)")
    void findById_Success() {

        // Arrange: id=1Lで呼び出されたらitem1オブジェクトが入ったOptionalを返すよう設定
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item1));

        // Act
        Item result = itemService.findById(1L);

        // Assert: idとnameをチェック && 1回呼び出しをチェック
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("テストアイテム1");
        verify(itemRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("指定したIDのアイテムを1件取得するテスト(失敗:見つからない)")
    void findByIdNotFound() {

        // Arrange: id=99Lで呼び出されたら中身が空のOptionalを返すよう設定
        when(itemRepository.findById(99L)).thenReturn(Optional.empty());

        // Act&Assert: 第2引数の処理を実行したときに、　第1引数で指定した例外がスローされることをチェック && 1回呼び出しをチェック
        assertThrows(ResourceNotFoundException.class, () -> {
            itemService.findById(99L);
        });
        verify(itemRepository, times(1)).findById(99L);
    }

    @Test
    @DisplayName("アイテムを新規登録するテスト")
    void createItem() {

        // Arrange: id=nullの新規アイテムとid=1Lの保存済を想定した新規アイテムを作成
        // saveメソッドがItemオブジェクトを引数として呼ばれたらsavedItemを返すよう設定
        Item newItem = Item.builder()
                .id(null)
                .name("新規アイテム")
                .price(2000)
                .build();
        Item savedItem = Item.builder()
                .id(1L)
                .name("新規アイテム")
                .price(2000)
                .build();
        when(itemRepository.save(any(Item.class))).thenReturn(savedItem);

        // Act
        Item result = itemService.createItem(newItem);

        //Assert: 返ってきたresultの値がnullでない、nameが等しい
        assertThat(result.getId()).isNotNull();
        assertThat(result.getName()).isEqualTo("新規アイテム");
    }

    @Test
    @DisplayName("アイテムを更新するテスト")
    void updateItem() {
        // Arrange: 更新用のデータを作成 && id=1Lで探したときにitem1が見つかる && saveで更新後データを返すよう設定
        Item updateData = Item.builder()
                .name("更新後アイテム")
                .price(1500)
                .build();
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item1));
        when(itemRepository.save(any(Item.class))).thenReturn(item1);

        // Act
        Item result = itemService.updateItem(1L, updateData);

        // Assert: 返ってきたresultのnameとpriceが更新後情報になっている && 1回呼び出しを確認
        assertThat(result.getName()).isEqualTo("更新後アイテム");
        assertThat(result.getPrice()).isEqualTo(1500);
        verify(itemRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("アイテムを削除するテスト")
    void deleteItem() {

        // Arrange: id=1Lが存在した時にitem1を返すよう設定
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item1));

        // Act
        itemService.deleteItem(1L);

        // Assert: 呼ばれた回数をチェック。削除することではなく呼び出すことがServiceの責務であるから
        verify(itemRepository, times(1)).delete(item1);
    }
}

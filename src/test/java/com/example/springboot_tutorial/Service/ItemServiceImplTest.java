package com.example.springboot_tutorial.Service;

import com.example.springboot_tutorial.exception.ResourceNotFoundException;
import com.example.springboot_tutorial.repository.ItemRepository;
import com.example.springboot_tutorial.model.Item;
import com.example.springboot_tutorial.service.ItemServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ItemServiceImplTest {

    @Mock  // 偽物のItemRepositoryを作成
    private ItemRepository itemRepository;

    @InjectMocks  // モックを注入した本物のItemServiceImplを作成する
    private ItemServiceImpl itemService;

    @Test
    void findByIdでIDが存在する場合アイテムを返す() {

        // Arrange(準備) itemの作成、id=1Lで呼び出されたらitemオブジェクトが入ったOptionalを返すよう設定
        Item item = new Item(1L, "テストアイテム", 1000);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        // Act(実行)
        Item result = itemService.findById(1L);

        // Assert(検証) 2つの引数が等しいことを確認。idとnameをチェック
        Assertions.assertEquals(1L, result.getId());
        Assertions.assertEquals("テストアイテム", result.getName());
    }

    @Test
    void findByIdでIDが存在しない場合の例外をスローする(){

        // Arrange id=99Lで呼び出されたら中身が空のOptionalを返すよう設定
        when(itemRepository.findById(99L)).thenReturn(Optional.empty());

        // Act&Assert 第2引数の処理を実行したときに、　第1引数で指定した例外がスローされることをチェック
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            itemService.findById(99L);
        });
    }

    @Test
    void createItemでアイテムを登録できる(){

        // Arrange id=nullの新規アイテムとid=1Lの保存済を想定した新規アイテムを作成
        // saveメソッドがItemオブジェクトを引数として呼ばれたらsavedItemを返すよう設定
        Item newItem = new Item(null, "新規アイテム", 2000);
        Item savedItem = new Item(1L, "新規アイテム", 2000);
        when(itemRepository.save(any(Item.class))).thenReturn(savedItem);

        // Act
        Item result = itemService.createItem(newItem);

        //Assert 帰ってきたresultの値がnullでない、nameが等しい
        Assertions.assertNotNull(result.getId());
        Assertions.assertEquals("新規アイテム", result.getName());
    }

    @Test
    void deleteItemでアイテムを削除できる() {

        // Arrange オブジェクト本体は不要のメソッドであるため設定するのもitemIdのみでOK
        // itemIdが存在した時にtrueを返すよう設定
        Long itemId = 1L;
        when(itemRepository.existsById(itemId)).thenReturn(true);

        // Act
        itemService.deleteItem(itemId);

        // Assert 呼ばれた回数をチェック。削除することではなく呼び出すことがServiceの責務であるから
        verify(itemRepository, times(1)).deleteById(itemId);
    }
}

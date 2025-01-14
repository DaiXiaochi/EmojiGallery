package com.example.EmojiGallery.dao;

import androidx.room.*;
import com.example.EmojiGallery.entity.Emoji;

import java.util.List;

//数据访问对象
@Dao  // Database access object
public interface EmojiDao {
    @Insert
    void insertEmojis(Emoji... emojis);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertEmojis(List<Emoji> emojis);

    @Update
    void updateEmojis(Emoji... emojis);

    @Delete
    void deleteEmojis(Emoji... emojis);

    @Query("DELETE FROM EMOJI")
    void deleteAllEmojis();

    @Query("SELECT * FROM EMOJI ORDER BY ID DESC")
    List<Emoji> getAllEmojis();
}

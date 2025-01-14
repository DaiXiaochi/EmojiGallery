package com.example.EmojiGallery.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import com.example.EmojiGallery.dao.EmojiDao;
import com.example.EmojiGallery.entity.Emoji;

// 数据库类
// entities表示该数据库有哪些表，version表示数据库的版本号
//exportSchema表示是否导出数据库信息的json串，建议设为false，若设置为true还要设置Scheme文件保存地址
@Database(entities = {Emoji.class} , version =1,exportSchema = true)
public abstract class EmojiDatabase extends RoomDatabase {
    public abstract EmojiDao getEmojiDao();
}

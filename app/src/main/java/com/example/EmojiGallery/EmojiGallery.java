package com.example.EmojiGallery;

import android.app.Application;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.room.Room;
import com.example.EmojiGallery.Utils.FileUtil;
import com.example.EmojiGallery.Utils.SharedUtil;
import com.example.EmojiGallery.dao.EmojiDao;
import com.example.EmojiGallery.database.EmojiDatabase;
import com.example.EmojiGallery.entity.Emoji;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class EmojiGallery extends Application {

    private static EmojiGallery mApp;

    public static EmojiGallery getInstance(){
        return mApp;
    }

    private EmojiDatabase emojiDatabase;

    // 可以定义变量但是不能在这里初始化
    // private AssetManager assets=getAssets();是错误的
    private AssetManager assets;
    //在App启动时调用
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("Dai","MyApplication onCreate");
        mApp=this;
        assets=getAssets();

        // 构建图片数据库的实例
        emojiDatabase = Room.databaseBuilder(this, EmojiDatabase.class,"emoji_database")
                //允许迁移数据库（发生数据库变更时，Room默认删除原数据库再生成新的数据库）
                .addMigrations()
                //在检测到版本不匹配时删除数据库并重新创建。
//                .fallbackToDestructiveMigration()
                // 允许主线程操作数据库  ，一般要放在副线程运行数据库操作，但是这里为了测试强制主线程操作
                .allowMainThreadQueries()
                .build();

        initEmoji();
    }




    private void initEmoji(){
        // 初始化表情图库数据库

        Log.d("Dai","InitEmoji");
        //获取共享参数保存的是否首次打开参数
        boolean isFirst = SharedUtil.getInstance(this).readBoolean("first",true);
        String directory =getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString()+ File.separatorChar;
        if(isFirst){
            Log.d("Dai","Downloading Emojis");
            // 模拟网络下载图片，这里放在类的声明里，从assets目录中下载图片
            List<Emoji> list = Emoji.getDefaultList();
            InputStream inputStream=null;
            for(Emoji emoji : list){
                try {
                    inputStream=assets.open(emoji.getImage_path());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                String path = directory +emoji.getImage_name();

//                Log.d("Dai","文件下载路径:"+path);
                FileUtil.saveImage(path,bitmap);

                bitmap.recycle();
                emoji.setImage_path(path);
                Log.d("Dai",emoji.toString());
            }

            // 将文件写入数据库中

            EmojiDao emojiDao= emojiDatabase.getEmojiDao();
            emojiDao.insertEmojis(list);

            SharedUtil.getInstance(this).writeBoolean("first",false);

        }
    }


    // 在App终止时调用，只有在系统开发时才有这个
    @Override
    public void onTerminate() {
        super.onTerminate();
        if (emojiDatabase != null) {
            emojiDatabase.close();
        }
        Log.d("Dai","onTerminate");
    }

    //在配置改变时调用，例如从横屏变为竖屏
    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.d("Dai","onConfigurationChanged");
    }

    public EmojiDatabase getEmojiDB(){
        return emojiDatabase;
    }
}

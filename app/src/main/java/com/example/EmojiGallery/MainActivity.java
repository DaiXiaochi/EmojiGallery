package com.example.EmojiGallery;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;
import com.example.EmojiGallery.dao.EmojiDao;
import com.example.EmojiGallery.database.EmojiDatabase;
import com.example.EmojiGallery.entity.Emoji;

import java.io.File;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private EmojiDao emojiDao;
    private TextView tv_count;
    private GridLayout gl_channel;
    List<Emoji> list;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Log.d("Dai","MainActivity onCreate");

        TextView tv_title = findViewById(R.id.tv_title);
        tv_title.setText("表情图库");

        tv_count = findViewById(R.id.tv_count);

        gl_channel = findViewById(R.id.gl_channel);
        // 从App实例中获取唯一的表情图库持久化对象
        EmojiDatabase emojiDatabase = EmojiGallery.getInstance().getEmojiDB();
        if (emojiDatabase == null) {
            Log.e("EmojiGallery", "Database is not initialized");
            return;
        }
        emojiDao = emojiDatabase.getEmojiDao();

        updateView();


    }

    void updateView(){
        Log.d("Dai","updateView");
        // 获取屏幕宽度
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int columnCount = gl_channel.getColumnCount(); // 每行 5 列
        int itemSize = screenWidth / columnCount; // 每个子视图的宽度
        list = emojiDao.getAllEmojis();
        gl_channel.removeAllViews();
        for(Emoji emoji:list){
            View view = LayoutInflater.from(this).inflate(R.layout.emoji_item,null);
            // 设置子视图宽高
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = itemSize; // 子视图宽度
            params.height = itemSize; // 子视图高度
            params.setMargins(4, 4, 4, 4); // 设置边距

            view.setLayoutParams(params);

            ImageView iv_emoji=view.findViewById(R.id.iv_emoji);
            iv_emoji.setImageURI(Uri.parse(emoji.getImage_path()));

            gl_channel.addView(view);
        }
    }
}
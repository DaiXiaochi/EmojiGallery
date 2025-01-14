package com.example.EmojiGallery;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.EmojiGallery.dao.EmojiDao;
import com.example.EmojiGallery.database.EmojiDatabase;
import com.example.EmojiGallery.entity.Emoji;

import java.io.File;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private EmojiDao emojiDao;
    private GridLayout gl_channel;
    private int currentPage = 0;//当前加载的页码
    private static final int PAGE_SIZE = 50;
    private List<Emoji> list;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Log.d("Dai","MainActivity onCreate");

        TextView tv_title = findViewById(R.id.tv_title);
        tv_title.setText("表情图库");

        // 从App实例中获取唯一的表情图库持久化对象
        EmojiDatabase emojiDatabase = EmojiGallery.getInstance().getEmojiDB();
        if (emojiDatabase == null) {
            Log.e("EmojiGallery", "Database is not initialized");
            return;
        }
        emojiDao = emojiDatabase.getEmojiDao();
        list = emojiDao.getAllEmojis();

        gl_channel = findViewById(R.id.gl_channel);
        loadImages(currentPage++);
        loadImages(currentPage);

        // 监听滚动事件
        ScrollView scrollView = findViewById(R.id.sv_channel);
        scrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                // 获取ScrollView的可视高度
                int scrollViewHeight = scrollView.getHeight();

                // 获取整个ScrollView内容的高度
                int contentHeight = scrollView.getChildAt(0).getHeight();

                // 判断是否滑动到底部
                if (scrollView.getScrollY() + scrollViewHeight == contentHeight) {
                    // 滑动到底部，加载下一页
                    currentPage++;
                    loadImages(currentPage);
                }
            }
        });



    }

    private void loadImages(int page) {
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int columnCount = gl_channel.getColumnCount(); // 每行 5 列
        int itemSize = screenWidth / columnCount; // 每个子视图的宽度

        int start = page * PAGE_SIZE;
        int end = Math.min(start + PAGE_SIZE, list.size());

        for (int i = start; i < end; i++) {
            Emoji emoji = list.get(i);

            // 创建每个图片项
            View view = LayoutInflater.from(this).inflate(R.layout.emoji_item,null);
            // 设置子视图宽高
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = itemSize; // 子视图宽度
            params.height = itemSize; // 子视图高度
            params.setMargins(4, 4, 4, 4); // 设置边距

            view.setLayoutParams(params);

            ImageView iv_emoji = view.findViewById(R.id.iv_emoji);

            // 使用 Glide 加载图片
            Glide.with(this)
                    .load(Uri.fromFile(new File(emoji.getImage_path())))
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.ic_error)
                    .into(iv_emoji);

            // 设置点击事件，放大图片
            iv_emoji.setOnClickListener(v -> {
                Intent intent = new Intent(this, ImagePreviewActivity.class);
                intent.putExtra("image_path", emoji.getImage_path());
                startActivity(intent);
            });

            // 添加到容器
            gl_channel.addView(view);
        }
    }

}
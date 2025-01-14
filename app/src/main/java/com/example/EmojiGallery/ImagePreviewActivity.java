package com.example.EmojiGallery;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;

public class ImagePreviewActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_preview);

        ImageView ivPreview = findViewById(R.id.iv_preview);
        ImageButton btnClose = findViewById(R.id.btn_close);

        // 从 Intent 获取图片路径
        String imagePath = getIntent().getStringExtra("image_path");
        if (imagePath != null) {
            Glide.with(this)
                    .load(imagePath)
                    .placeholder(R.drawable.placeholder) // 占位图
                    .error(R.drawable.error) // 错误图
                    .into(ivPreview);
        }

        // 点击关闭按钮关闭预览
        btnClose.setOnClickListener(v -> finish());
    }
}


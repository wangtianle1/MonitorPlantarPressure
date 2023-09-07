package com.example.monitorplantarpressure;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

public class GifActivity extends AppCompatActivity {
    private ImageView iv_leftback;
    private ImageView   iv_pgyl,iv_xjyl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gif);
        initview();

    }

    private void initview() {
        iv_leftback = findViewById(R.id.iv_leftback);
        iv_pgyl = findViewById(R.id.iv_pgyl);
        iv_leftback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finish();
            }
        });

        // 使用Glide加载本地GIF图片
        Glide.with(this)
                .asGif()
                .load(R.drawable.pgyl) // 替换成您的本地GIF图片资源
                .apply(RequestOptions.centerInsideTransform())
                .listener(new RequestListener<GifDrawable>() {
                    @Override
                    public boolean onLoadFailed( GlideException e, Object model, Target<GifDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GifDrawable resource, Object model, Target<GifDrawable> target, DataSource dataSource, boolean isFirstResource) {
                        // 在GIF图片加载完成后执行操作（可选）
                        return false;
                    }
                })
                .into(iv_pgyl);
        iv_xjyl = findViewById(R.id.iv_xjyl);
//渲染橡胶压力的gif
        Glide.with(this)
                .asGif()
                .load(R.drawable.xjyl) // 替换成您的本地GIF图片资源
                .apply(RequestOptions.centerInsideTransform())
                .listener(new RequestListener<GifDrawable>() {
                    @Override
                    public boolean onLoadFailed( GlideException e, Object model, Target<GifDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GifDrawable resource, Object model, Target<GifDrawable> target, DataSource dataSource, boolean isFirstResource) {
                        // 在GIF图片加载完成后执行操作（可选）
                        return false;
                    }
                })
                .into(iv_xjyl);

    }
}
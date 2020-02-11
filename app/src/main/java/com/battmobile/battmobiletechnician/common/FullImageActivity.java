package com.battmobile.battmobiletechnician.common;

import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import com.battmobile.battmobiletechnician.R;
import com.veinhorn.scrollgalleryview.MediaInfo;
import com.veinhorn.scrollgalleryview.ScrollGalleryView;
import com.veinhorn.scrollgalleryview.builder.GallerySettings;

import java.util.ArrayList;
import java.util.List;

import static com.veinhorn.scrollgalleryview.loader.picasso.dsl.DSL.images;

public class FullImageActivity extends FragmentActivity {
    private ScrollGalleryView galleryView;
    List<MediaInfo> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);
        list = new ArrayList<>();
        list.addAll(images(getIntent().getStringExtra("image")));
        galleryView = ScrollGalleryView
                .from((ScrollGalleryView) findViewById(R.id.scroll_gallery_view))
                .settings(
                        GallerySettings
                                .from(getSupportFragmentManager())
                                .thumbnailSize(1)
                                .enableZoom(true)
                                .build()
                )
                .onImageClickListener(new ScrollGalleryView.OnImageClickListener() {
                    @Override
                    public void onClick(int position) {
                    }
                })
                .onImageLongClickListener(new ScrollGalleryView.OnImageLongClickListener() {
                    @Override
                    public void onClick(int position) {
                    }
                })
                .onPageChangeListener(new CustomOnPageListener())
                .add(list)
                .build();
    }

    private class CustomOnPageListener extends ViewPager.SimpleOnPageChangeListener {
        @Override
        public void onPageSelected(int position) {
        }
    }
}
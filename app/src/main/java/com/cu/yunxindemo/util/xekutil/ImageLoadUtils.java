package com.cu.yunxindemo.util.xekutil;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.cu.yunxindemo.R;
import com.cu.yunxindemo.util.GlideUtils;

import java.io.IOException;

import sj.keyboard.utils.imageloader.ImageBase;
import sj.keyboard.utils.imageloader.ImageLoader;

public class ImageLoadUtils extends ImageLoader {

    public ImageLoadUtils(Context context) {
        super(context);
    }

    @Override
    protected void displayImageFromFile(String imageUri, ImageView imageView) throws IOException {
        String filePath = Scheme.FILE.crop(imageUri);
        if (GlideUtils.canLoadImage(imageView.getContext())) {
            Glide.with(imageView.getContext())
                    .load(filePath)
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .into(imageView);
        }

    }

    @Override
    protected void displayImageFromAssets(String imageUri, ImageView imageView) throws IOException {
        String uri = Scheme.cropScheme(imageUri);
        ImageBase.Scheme.ofUri(imageUri).crop(imageUri);
        if (GlideUtils.canLoadImage(imageView.getContext())) {
            Glide.with(imageView.getContext())
                    .load(Uri.parse("file:///android_asset/" + uri))
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .into(imageView);
        }

    }
}

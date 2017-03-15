package com.cu.yunxindemo.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.cu.yunxindemo.R;
import com.cu.yunxindemo.util.transformations.BlurTransformation;
import com.cu.yunxindemo.util.transformations.GlideCircleTransform;

/**
 * Description
 * Glide 工具类
 * Company Beijing guokeyuzhou
 * author  youxuan  E-mail:xuanyouwu@163.com
 * date createTime：16/7/11
 * version
 */

public class GlideUtils {


    /**
     * fragment 中 glide是否可以加载图片
     * 否则 glide会引发崩溃
     *
     * @param fragment
     * @return
     */
    public static boolean canLoadImage(Fragment fragment) {
        if (fragment == null) {
            return false;
        }
        FragmentActivity parentActivity = fragment.getActivity();
        return canLoadImage(parentActivity);
    }

    /**
     * context 中 glide是否可以加载图片
     * 否则 glide会引发崩溃
     *
     * @param context
     * @return
     */
    public static boolean canLoadImage(Context context) {
        if (context == null) {
            return false;
        }
        if (!(context instanceof Activity)) {
            return true;
        }
        Activity activity = (Activity) context;
        return canLoadImage(activity);
    }

    /**
     * activity 中 glide是否可以加载图片
     * 否则 glide会引发崩溃
     *
     * @param activity
     * @return
     */
    public static boolean canLoadImage(Activity activity) {
        if (activity == null) {
            return false;
        }
        boolean destroyed = Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 &&
                activity.isDestroyed();
        if (destroyed || activity.isFinishing()) {
            return false;
        }
        return true;
    }


    private GlideUtils() {
    }

    /**
     * 加载用户 头像 等 圆角
     *
     * @param context
     * @param path
     * @param imageView
     */
    public static void loadUser(Context context, String path, ImageView imageView) {
        if (context == null) return;
        if (imageView == null) return;
        if (context instanceof Activity) {
            if (((Activity) context).isFinishing())
                return;
        }
        Glide.with(context)
                .load(path)
                .transform(new GlideCircleTransform(context))
                .placeholder(R.mipmap.default_head)
                .error(R.mipmap.default_head)
                .crossFade()
                .into(imageView);
    }


    /**
     * 加载用户 头像 等 圆角 有边框有 图标
     *
     * @param context
     * @param path
     * @param imageView
     */
    public static void loadUser2(Context context, String path, ImageView imageView, int color, int width, int angle, int img) {
        if (context == null) return;
        if (imageView == null) return;
        if (context instanceof Activity) {
            if (((Activity) context).isFinishing())
                return;
        }
        Glide.with(context)
                .load(path)
                .transform(new GlideHollowCircleTransform(context, color, width, angle, img))
                .placeholder(R.mipmap.default_head)
                .error(R.mipmap.default_head)
                .crossFade()
                .into(imageView);
    }




    /**
     * 为图片着色
     *
     * @param context
     * @param drawableRes
     * @param color
     * @return
     */
    public static Drawable getTintedDrawable(Context context, int drawableRes, int color) {
        Drawable drawable = null;
        if (context != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                drawable = context.getResources().getDrawable(drawableRes, null);
                if (drawable != null) {
                    drawable.setTint(color);
                }
            } else {
                drawable = context.getResources().getDrawable(drawableRes);
                if (drawable != null) {
                    drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
                }
            }
        }
        return drawable;
    }


}


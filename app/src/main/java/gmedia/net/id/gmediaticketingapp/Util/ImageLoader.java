package gmedia.net.id.gmediaticketingapp.Util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;

public class ImageLoader {
    private final static String TAG = "image_loader_log";
    public static void load(Context context, String url, ImageView view){
        Glide.with(context).load(url).transition(DrawableTransitionOptions.withCrossFade()).
                apply(new RequestOptions().placeholder(new ColorDrawable(Color.WHITE)).
                        diskCacheStrategy(DiskCacheStrategy.NONE)).into(view);
    }

    public static void load(Context context, int res_id, ImageView view){
        Glide.with(context).load(res_id).transition(DrawableTransitionOptions.withCrossFade()).
                apply(new RequestOptions().placeholder(new ColorDrawable(Color.WHITE)).
                        diskCacheStrategy(DiskCacheStrategy.NONE)).into(view);
    }

    public static void load(Context context, Bitmap image, ImageView view){
        Glide.with(context).load(image).transition(DrawableTransitionOptions.withCrossFade()).
                apply(new RequestOptions().placeholder(new ColorDrawable(Color.WHITE)).
                        diskCacheStrategy(DiskCacheStrategy.NONE)).into(view);
    }

    public static void load(Context context, String url, ImageView view, int width, int height){
        Glide.with(context).load(url).transition(DrawableTransitionOptions.withCrossFade()).
                apply(new RequestOptions().override(width, height).placeholder(new ColorDrawable(Color.WHITE)).
                        diskCacheStrategy(DiskCacheStrategy.NONE)).into(view);
    }

    public static void load(Context context, String url, ImageView view, ColorDrawable color){
        Glide.with(context).load(url).transition(DrawableTransitionOptions.withCrossFade()).
                apply(new RequestOptions().placeholder(color).
                        diskCacheStrategy(DiskCacheStrategy.NONE)).into(view);
    }

    public static void preload(Context context, String url, final ImageLoadListener listener){
        Glide.with(context)
                .asBitmap()
                .load(url)
                .apply(new RequestOptions().placeholder(new ColorDrawable(Color.WHITE)).
                                diskCacheStrategy(DiskCacheStrategy.ALL))
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        listener.onLoaded(resource, resource.getWidth(), resource.getHeight());
                    }
                });
    }

    public static void preload(Context context, String url, final ImageLoadListener listener, int width, int height){
        Glide.with(context)
                .asBitmap()
                .load(url)
                .apply(new RequestOptions().placeholder(new ColorDrawable(Color.WHITE)).
                        override(width, height).
                        diskCacheStrategy(DiskCacheStrategy.ALL))
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        listener.onLoaded(resource, resource.getWidth(), resource.getHeight());
                    }
                });
    }

    public interface ImageLoadListener{
        void onLoaded(Bitmap image, float width, float height);
    }
}

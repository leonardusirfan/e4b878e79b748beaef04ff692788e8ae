package gmedia.net.id.gmediaticketingapp.Util;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MultipleImageLoader {
    private Context context;
    private List<String> listUrl;
    private int load_counter;

    private List<Bitmap> listLoaded = new ArrayList<>();
    private MultipleImageLoaderListener listener;

    public MultipleImageLoader(Context context, List<String> listUrl, MultipleImageLoaderListener listener){
        this.context = context;
        this.listUrl = listUrl;
        load_counter = 0;
        this.listener = listener;

        loadNextBitmap();
    }

    private void loadNextBitmap(){
        if(load_counter < listUrl.size()){
            ImageLoader.preload(context, listUrl.get(load_counter), new ImageLoader.ImageLoadListener() {
                @Override
                public void onLoaded(Bitmap image, float width, float height) {
                    Log.d("_log", "image loaded " + width + "," + height);
                    listLoaded.add(image);
                    load_counter++;

                    loadNextBitmap();
                }
            });
        }
        else{
            listener.onAllLoaded(listLoaded);
        }
    }

    public interface MultipleImageLoaderListener{
        void onAllLoaded(List<Bitmap> lisLoaded);
    }
}

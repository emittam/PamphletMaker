package net.emittam.pamphletmaker;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by kato-h on 16/11/27.
 */

public class ImageRepository {

    private static Map<String, ImageRepository> instanceMap = new HashMap<>();

    public static synchronized ImageRepository getInstance(@NonNull Context context, @NonNull String key) {
        ImageRepository instance = instanceMap.get(key);
        if (instance == null) {
            instance = new ImageRepository(key);
            instanceMap.put(key, instance);
        }
        instance.load(context);
        return instance;
    }

    private final String mKey;

    private List<String> mImageUrlList;

    private ImageRepository(String key) {
        this.mKey = key;
    }

    private void load(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(mKey, Context.MODE_PRIVATE);
        Set<String> set = prefs.getStringSet(ImageCollectBroadcastReceiver.SAVE_IMAGE_URL_KEY, new HashSet<String>());
        mImageUrlList = new ArrayList<>();
        for (String url : set) {
            mImageUrlList.add(url);
        }
    }

    public LeftPamphletViewerModel getLeftModel(int page) {
        LeftPamphletViewerModel model = new LeftPamphletViewerModel();
        for (int i = page * LeftPamphletViewerFragment.PAGE_VIEW_IMAGE_COUNT; i < (page + 1) * LeftPamphletViewerFragment.PAGE_VIEW_IMAGE_COUNT; i++) {
            if (i < mImageUrlList.size()) {
                model.add(mImageUrlList.get(i));
            }
        }
        return model;
    }

    public int getImageCount() {
        return mImageUrlList.size();
    }
}

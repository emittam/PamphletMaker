package net.emittam.pamphletmaker;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by kato-h on 16/11/26.
 */

public class ImageCollectBroadcastReceiver extends BroadcastReceiver {

    private static final String NEW_PHOTO_ACTION = "android.hardware.action.NEW_PICTURE";

    public static final String SAVE_IMAGE_URL_KEY = "image.url.set";

    public static final String SAVE_FILE_PATH_KEY = "save.filepath.key";

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences defaultPref = PreferenceManager.getDefaultSharedPreferences(context);
        String saveKey = defaultPref.getString(SAVE_FILE_PATH_KEY, null);

        int permissionCheck = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_EXTERNAL_STORAGE
        );

        if (intent.getAction().equals(NEW_PHOTO_ACTION)
                && saveKey != null
                && android.content.pm.PackageManager.PERMISSION_GRANTED == permissionCheck) {
            SharedPreferences prefs = context.getSharedPreferences(saveKey, Context.MODE_PRIVATE);
            Set<String> savedSet = prefs.getStringSet(SAVE_IMAGE_URL_KEY, new HashSet<String>());
            savedSet = new HashSet<>(savedSet);
            String[] CONTENT_PROJECTION = {
                    MediaStore.Images.Media.DATA,
            };

            Cursor c = context.getContentResolver().query(intent.getData(), CONTENT_PROJECTION, null, null, null);
            if (c == null || !c.moveToFirst()) {
                if (c != null) {
                    c.close();
                }
            }
            String localPath = c.getString(c.getColumnIndex(MediaStore.Images.Media.DATA));
            c.close();

            String url = "file://" + localPath;
            if (!savedSet.contains(url)) {
                savedSet.add(url);
            }
            SharedPreferences.Editor editor = prefs.edit();
            editor.putStringSet(SAVE_IMAGE_URL_KEY, savedSet);
            editor.commit();
        }
    }
}

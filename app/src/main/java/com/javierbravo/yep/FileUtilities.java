package com.javierbravo.yep;

import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Enrique on 13/02/2016.
 */
public class FileUtilities {
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    public static final String APP_NAME = "WaveApp";
    private static final String TAG_DIR = "Directory";

    private static File getOutputMediaFile(int type) {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "MyCameraApp");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }
        return null;
    }

    public static Uri getOutputMediaFileUri(int mediaType) {
        File appWave = Environment.getExternalStoragePublicDirectory(APP_NAME);
        File mediaFile = null;
        String fileName ="";
        Date now = new Date();

        if (isExternalStorageAvailable()) {
            File mediaStorageDir = null;

            switch(mediaType) {
                case MEDIA_TYPE_IMAGE:
                    mediaStorageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                    break;
                case MEDIA_TYPE_VIDEO:
                    mediaStorageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);
                    break;
            }
            if (!appWave.exists()) {
                Log.d(TAG_DIR, appWave.getAbsolutePath()+" no exist");
                if(!appWave.mkdirs()) {
                    Log.d(TAG_DIR, "Directory" + appWave.getAbsolutePath()+ " not created");
                    return null;
                }
            }

            String timestap = new SimpleDateFormat("yyyyMMdd_HHmmss", new Locale("es", "ES")).format(now);

            switch(mediaType) {
                case MEDIA_TYPE_IMAGE:
                    fileName = "IMG_" + timestap + ".jpg";
                    break;
                case MEDIA_TYPE_VIDEO:
                    fileName = "VID_" + timestap + ".mp4";
                    break;
            }

            String pathFile = appWave.getAbsolutePath() + File.separator + fileName;
            mediaFile = new File(pathFile);
            Log.d(TAG_DIR, "File : " + mediaFile.getAbsolutePath());
        }

        return Uri.fromFile(mediaFile);
    }

    private static boolean isExternalStorageAvailable() {
        String state= Environment.getExternalStorageState();

        if(state.equals(Environment.MEDIA_MOUNTED))
            return true;
        else return false;
    }
}


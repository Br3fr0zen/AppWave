package com.javierbravo.yep;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by manu on 19/02/2016.
 */
public class FileHelper {
    private static final String TAG = "Error";
    public static final int SHORT_SIDE_TARGET = 1280;


    public static byte[] getByteArrayFromFile(Context context, Uri uri) {
        byte[] fileBytes = null;//arraydebytes
        InputStream inStream = null;//flujodeentrada
        ByteArrayOutputStream outStream = null;//flujodesalida

        //MediaStore(andgeneral)
        if (uri.getScheme().equals("content")) {
            try {
                inStream = context.getContentResolver().openInputStream(uri);
                outStream = new ByteArrayOutputStream();
                byte[] bytesFromFile = new byte[1024 * 1024];//buffersize(1MB)
                int bytesRead = inStream.read(bytesFromFile);
                while (bytesRead != -1) {
                    outStream.write(bytesFromFile, 0, bytesRead);
                    bytesRead = inStream.read(bytesFromFile);
                }
                fileBytes = outStream.toByteArray();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (inStream != null)
                        inStream.close();
                    if (outStream != null)
                        outStream.close();
                } catch (IOException e) {
                }
            }
        }
        //File
        else {
            FileInputStream fileInput = null;

            try {
                File file = new File(uri.getPath());
                fileInput = new FileInputStream(file);
                fileBytes = IOUtils.toByteArray(fileInput);
            } catch (IOException e) {
                if (fileInput != null)
                    try {
                        fileInput.close();
                    } catch (IOException e1) {

                    }
                Log.e(TAG, e.getMessage());
            }


        }


        return new byte[0];
    }

    public static byte[] reduceImageForUpload(byte[] imageData) {
        Bitmap bitmap = ImageResizer.resizeImageMaintainAspectRatio(imageData, SHORT_SIDE_TARGET);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        byte[] reducedData = outputStream.toByteArray();
        try {
            outputStream.close();
        } catch (IOException e) {
            //e.printStackTrace();
        }
        return reducedData;
    }


    public static String getFileName(Context context, Uri uri, String fileType) {
        String fileName = "uploaded_file.";
        if (fileType.equals(ParseConstants.TYPE_IMAGE)) {
            fileName += "png";
        } else {
            //Forvideo,wewanttogettheactualfileextension
            if (uri.getScheme().equals("content")) {
                //doitusingthemimetype
                String mimeType = context.getContentResolver().getType(uri);
                int slashIndex = mimeType.indexOf("/");
                String fileExtension = mimeType.substring(slashIndex + 1);
                fileName += fileExtension;
            } else {
                fileName = uri.getLastPathSegment();
            }
        }
        return fileName;
    }

}

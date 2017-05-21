package com.hw.helpers;

import android.os.Environment;
import android.util.Log;
import java.io.File;
import java.util.ArrayList;

public class ImageHelper {
    public static File getImage() {
        ArrayList<File> fileList = new ArrayList();
        try {
            for (File file : new File(getAlbumPath()).listFiles()) {
                Log.d("ImageHelper", "file name: " + file.getName());
                if (file.isDirectory()) {
                    for (File image : file.listFiles()) {
                        Log.d("ImageHelper", "image name: " + image.getName());
                        if (image.getName().endsWith("jpg")) {
                            fileList.add(image);
                        }
                    }
                } else if (file.getName().endsWith("jpg")) {
                    fileList.add(file);
                }
            }
        } catch (Exception e) {
        }
        Log.d("ImageHelper", "list size: " + fileList.size());
        if (fileList.size() <= 0) {
            return null;
        }
        int index = ((int) (Math.random() * ((double) fileList.size()))) - 1;
        Log.d("ImageHelper", "list index: " + index);
        return (File) fileList.get(index);
    }

    public static String getAlbumPath() {
        return Environment.getExternalStorageDirectory() + File.separator;
    }
}

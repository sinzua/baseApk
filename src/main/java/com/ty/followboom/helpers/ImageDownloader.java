package com.ty.followboom.helpers;

import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;
import com.forwardwin.base.widgets.MD5Encryption;
import com.ty.instaview.R;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import org.codehaus.jackson.util.MinimalPrettyPrinter;

public class ImageDownloader {
    private static final int DEFAULT_QUALITY = 80;
    private static final String TAG = "ImageDownloader";
    private static ImageDownloader sInstance;
    private Context mContext;
    private String mFileName;
    private String mSaveMessage;
    private String mVideoUrl;
    private Handler messageHandler = new Handler() {
        public void handleMessage(Message msg) {
            Log.d(ImageDownloader.TAG, ImageDownloader.this.mSaveMessage);
            Toast.makeText(ImageDownloader.this.mContext, ImageDownloader.this.mSaveMessage, 0).show();
        }
    };

    private class ImageSaverRunnable implements Runnable {
        private String mFileName;
        private String mImageUrl;

        private ImageSaverRunnable(String imageUrl, String fileName) {
            this.mImageUrl = imageUrl;
            this.mFileName = fileName;
        }

        public void run() {
            try {
                if (ImageDownloader.this.saveFile(this.mImageUrl, this.mFileName)) {
                    ImageDownloader.this.mSaveMessage = ImageDownloader.this.mContext.getResources().getString(R.string.download_image_succeed);
                } else {
                    ImageDownloader.this.mSaveMessage = ImageDownloader.this.mContext.getResources().getString(R.string.download_image_exist);
                }
            } catch (IOException e) {
                ImageDownloader.this.mSaveMessage = ImageDownloader.this.mContext.getResources().getString(R.string.download_image_failed);
                e.printStackTrace();
            }
            Log.d(ImageDownloader.TAG, "saveFileRunnable: " + this.mFileName + MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + ImageDownloader.this.mSaveMessage);
            ImageDownloader.this.messageHandler.sendMessage(ImageDownloader.this.messageHandler.obtainMessage());
            ImageDownloader.this.mContext.sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", Uri.parse("file://" + ImageDownloader.this.getAlbumPath() + this.mFileName)));
        }
    }

    private class VideoSaverRunnable implements Runnable {
        private String mFileName;
        private String mVideoUrl;

        private VideoSaverRunnable(String videoUrl, String fileName) {
            this.mVideoUrl = videoUrl;
            this.mFileName = fileName;
        }

        public void run() {
            try {
                if (ImageDownloader.this.downloadVideo(this.mVideoUrl, this.mFileName).booleanValue()) {
                    ImageDownloader.this.mSaveMessage = ImageDownloader.this.mContext.getResources().getString(R.string.download_video_succeed);
                } else {
                    ImageDownloader.this.mSaveMessage = ImageDownloader.this.mContext.getResources().getString(R.string.download_video_exist);
                }
            } catch (Exception e) {
                ImageDownloader.this.mSaveMessage = ImageDownloader.this.mContext.getResources().getString(R.string.download_image_failed);
                e.printStackTrace();
            }
            ImageDownloader.this.messageHandler.sendMessage(ImageDownloader.this.messageHandler.obtainMessage());
            ImageDownloader.this.mContext.sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", Uri.parse("file://" + ImageDownloader.this.getAlbumPath() + this.mFileName)));
        }
    }

    public ImageDownloader(Context context) {
        this.mContext = context;
    }

    public static synchronized ImageDownloader getInstance(Context context) {
        ImageDownloader imageDownloader;
        synchronized (ImageDownloader.class) {
            if (sInstance == null) {
                sInstance = new ImageDownloader(context);
            }
            imageDownloader = sInstance;
        }
        return imageDownloader;
    }

    public String getAlbumPath() {
        return Environment.getExternalStorageDirectory() + File.separator + this.mContext.getString(R.string.album_path) + File.separator;
    }

    private boolean saveFile(String imageUrl, String fileName) throws IOException {
        Log.d(TAG, "saveFile: " + fileName);
        String albumPath = getAlbumPath();
        File dirFile = new File(albumPath);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        if (new File(albumPath + fileName).exists()) {
            return false;
        }
        File file = new File(getAlbumPath() + fileName);
        if (file.exists()) {
            return false;
        }
        DownloadManager downloadManager = (DownloadManager) this.mContext.getSystemService("download");
        Request request = new Request(Uri.parse(imageUrl));
        request.setNotificationVisibility(0);
        request.setDestinationUri(Uri.fromFile(file));
        try {
            downloadManager.enqueue(request);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void saveImage(String imageUrl) {
        if (!TextUtils.isEmpty(imageUrl)) {
            String fileName = generateImageName(imageUrl);
            Log.d(TAG, "saveImage: " + fileName);
            new Thread(new ImageSaverRunnable(imageUrl, fileName)).start();
        }
    }

    public String generateImageName(String imageUrl) {
        return MD5Encryption.getMD5(imageUrl) + ".jpg";
    }

    public void downloadImage(String url) {
        getInstance(this.mContext).saveImage(url);
    }

    public ArrayList<String> getFileList() {
        ArrayList<String> fileList = new ArrayList();
        try {
            for (File image : new File(getAlbumPath()).listFiles()) {
                if (image.getName().endsWith("mp4") || image.getName().endsWith("jpg")) {
                    fileList.add(Uri.fromFile(image).toString());
                }
            }
        } catch (Exception e) {
        }
        return fileList;
    }

    public String generateVideoName(String videoUrl) {
        return MD5Encryption.getMD5(videoUrl) + ".mp4";
    }

    public void saveVideo(String videoUrl) {
        this.mVideoUrl = videoUrl;
        this.mFileName = generateVideoName(this.mVideoUrl);
        new Thread(new VideoSaverRunnable(this.mVideoUrl, this.mFileName)).start();
    }

    private Boolean downloadVideo(String videoUrl, String fileName) {
        File file = new File(getAlbumPath() + fileName);
        if (file.exists()) {
            return Boolean.valueOf(false);
        }
        DownloadManager downloadManager = (DownloadManager) this.mContext.getSystemService("download");
        Request request = new Request(Uri.parse(videoUrl));
        request.setNotificationVisibility(0);
        request.setDestinationUri(Uri.fromFile(file));
        try {
            downloadManager.enqueue(request);
            return Boolean.valueOf(true);
        } catch (Exception e) {
            return Boolean.valueOf(false);
        }
    }
}

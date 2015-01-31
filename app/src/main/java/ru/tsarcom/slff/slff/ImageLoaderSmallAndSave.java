package ru.tsarcom.slff.slff;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ImageLoaderSmallAndSave {

    MemoryCache memoryCache=new MemoryCache();
    FileCache fileCache;
    Context iContext;
    private Map<ImageView, String> imageViews=Collections.synchronizedMap(new WeakHashMap<ImageView, String>());
    ExecutorService executorService;

    public ImageLoaderSmallAndSave(Context context){
        iContext = context;
        fileCache=new FileCache(context);
        executorService=Executors.newFixedThreadPool(5);
    }
 
    int stub_id = R.drawable.ic_launcher;


    //decodes image and scales it to reduce memory consumption
    private Bitmap decodeFile(File f){
        try {
            //decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f),null,o);
 
            //Find the correct scale value. It should be the power of 2.
            final int REQUIRED_SIZE=50;
            int width_tmp=o.outWidth, height_tmp=o.outHeight;
            int scale=1;
            while(true){
                if(width_tmp/2<REQUIRED_SIZE || height_tmp/2<REQUIRED_SIZE)
                    break;
                width_tmp/=2;
                height_tmp/=2;
                scale*=2;
            }
 
            //decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize=scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {}
        return null;
    }

    public void clearCache() {
        memoryCache.clear();
        fileCache.clear();
    }
    // -----------------------------------

    public void DisplayImageAndSave(String url, int loader, ImageView imageView, int imgOrientation,
                                    File dir_path_, int cid_, int pid_, String fname_)
    {
        stub_id = loader;
        imageViews.put(imageView, url);
        Bitmap bitmap=memoryCache.get(url);
//
        if(bitmap!=null) {
//            Toast.makeText(iContext, "DisplayImageAndSave bitmap!=null ",
//                    Toast.LENGTH_SHORT).show();
            imageView.setImageBitmap(bitmap);
        }
        else
        {
//            Toast.makeText(iContext, "DisplayImageAndSave bitmap=====null ",
//                    Toast.LENGTH_SHORT).show();
            queuePhoto2(url, imageView, dir_path_,  cid_,  pid_,  fname_);
            imageView.setImageResource(loader);
        }
    }
    public Bitmap getBitmapWeb(String url, File dpath, String fname)
    {
        fname = "s"+fname;
        File f=fileCache.getFileAndSave(url, dpath, fname);

        //from SD cache
        Bitmap b = decodeFile(f);
        if(b!=null) {
//            Toast.makeText(iContext, "getBitmapWeb from SD cache decodeFile ",
//                    Toast.LENGTH_SHORT).show();
            return b;
        }

        //from web
        try {
//            Toast.makeText(iContext, "getBitmapWeb from WEB  ",
//                    Toast.LENGTH_SHORT).show();
            Bitmap bitmap=null;
            URL imageUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection)imageUrl.openConnection();
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(30000);
            conn.setInstanceFollowRedirects(true);
            InputStream is=conn.getInputStream();
            OutputStream os = new FileOutputStream(f);



            Bitmap bitmap_os;
            //decode image size
//            BitmapFactory.Options o = new BitmapFactory.Options();
//            o.inJustDecodeBounds = true;
//            BitmapFactory.decodeStream(is,null,o);
//
//            //Find the correct scale value. It should be the power of 2.
//            final int REQUIRED_SIZE=50;
//            int width_tmp=o.outWidth, height_tmp=o.outHeight;
//            int scale=10;
//            while(true){
//                if(width_tmp/2<REQUIRED_SIZE || height_tmp/2<REQUIRED_SIZE)
//                    break;
//                width_tmp/=2;
//                height_tmp/=2;
//                scale*=2;
//            }

            //decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            int scale = 1;
            o2.inSampleSize=scale;
            bitmap_os = BitmapFactory.decodeStream(is, null, o2);


            int width = bitmap_os.getWidth();
            int height = bitmap_os.getHeight();
            int width_n;
            int height_n;
            if(width>height){
                 width_n = 150;
                 height_n = 150*height/width;
            }else{
                 width_n = 150*width/height;
                 height_n = 150;
            }
            bitmap_os = Bitmap.createScaledBitmap(bitmap_os, width_n,
                    height_n, false);
            // Writing the bitmap to the output stream
            bitmap_os.compress(Bitmap.CompressFormat.PNG, 100, os);

//            Utils.CopyStream(is, os);
            os.close();
//            bitmap = decodeFile(f);
            bitmap = bitmap_os;


//            Utils.CopyStream(is, os);
//            os.close();
//            bitmap = decodeFile(f);
            if(bitmap!=null) {
//                Toast.makeText(iContext, "getBitmapWeb from WEB bitmap!=null ",
//                        Toast.LENGTH_SHORT).show();
            }
            return bitmap;
        } catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
    }
    private void queuePhoto2(String url, ImageView imageView,File dir_path_, int cid_, int pid_, String fname_)
    {
        PhotoToLoad2 p=new PhotoToLoad2(url, imageView, dir_path_, cid_, pid_,  fname_);
        executorService.submit(new PhotosLoader2(p));
    }
    //Task for the queue
    private class PhotoToLoad2
    {
        public String url;
        public ImageView imageView;
        public File dir_path;
        public int cid;
        public int pid;
        public String fname;
        public PhotoToLoad2(String u, ImageView i,File dir_path_, int cid_, int pid_, String fname_){
            url=u;
            imageView=i;
            dir_path = dir_path_;
            cid = cid_;
            pid = pid_;
            fname = fname_;
        }
    }
    class PhotosLoader2 implements Runnable {
        PhotoToLoad2 photoToLoad2;
        PhotosLoader2(PhotoToLoad2 photoToLoad2){
            this.photoToLoad2=photoToLoad2;
        }

        @Override
        public void run() {
            if(imageViewReused2(photoToLoad2))
                return;
            Bitmap bmp=getBitmapWeb(photoToLoad2.url,photoToLoad2.dir_path,photoToLoad2.fname);
            memoryCache.put(photoToLoad2.url, bmp);
            if(imageViewReused2(photoToLoad2))
                return;
            BitmapDisplayer2 bd=new BitmapDisplayer2(bmp, photoToLoad2);
            Activity a=(Activity)photoToLoad2.imageView.getContext();
            a.runOnUiThread(bd);
        }
    }
 
    boolean imageViewReused2(PhotoToLoad2 photoToLoad2){
        String tag=imageViews.get(photoToLoad2.imageView);
        if(tag==null || !tag.equals(photoToLoad2.url))
            return true;
        return false;
    }
 
    //Used to display bitmap in the UI thread
    class BitmapDisplayer2 implements Runnable
    {
        Bitmap bitmap;
        PhotoToLoad2 photoToLoad2;
        public BitmapDisplayer2(Bitmap b, PhotoToLoad2 p){bitmap=b;photoToLoad2=p;}
        public void run()
        {
            if(imageViewReused2(photoToLoad2))
                return;
            if(bitmap!=null)
                photoToLoad2.imageView.setImageBitmap(bitmap);
            else
                photoToLoad2.imageView.setImageResource(stub_id);
        }
    }

 
}

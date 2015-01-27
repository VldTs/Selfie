package ru.tsarcom.slff.slff;

import java.io.File;
import android.content.Context;
import android.widget.Toast;

public class FileCache {
 
    private File cacheDir;
    Context iContext;
    public FileCache(Context context){
        iContext = context;
        //Find the dir to save cached images
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
            cacheDir=new File(android.os.Environment.getExternalStorageDirectory(),"TempImages");
        else
            cacheDir=context.getCacheDir();
        if(!cacheDir.exists())
            cacheDir.mkdirs();
    }
 
    public File getFile(String url){
        String filename=String.valueOf(url.hashCode());
        File f = new File(cacheDir, filename);
        return f;
 
    }
    public File getFileAndSave(String url, File dpath, String fname){
        String filename=String.valueOf(url.hashCode());
        if (null == dpath){
            dpath = cacheDir;

//            Toast.makeText(iContext, "getFileAndSave null == dpath ",
//                    Toast.LENGTH_SHORT).show();
        }
        if (null == fname){
            fname = filename;

//            Toast.makeText(iContext, "getFileAndSave null == fname ",
//                    Toast.LENGTH_SHORT).show();
        }
        File f = new File(dpath, fname);
        return f;

    }
 
    public void clear(){
        File[] files=cacheDir.listFiles();
        if(files==null)
            return;
        for(File f:files)
            f.delete();
    }
 
}
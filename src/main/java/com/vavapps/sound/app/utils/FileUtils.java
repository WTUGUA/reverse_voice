package com.vavapps.sound.app.utils;

import android.os.Environment;
import com.vavapps.sound.app.SoundApp;

import java.io.*;
import java.nio.channels.FileChannel;

public class FileUtils {

    //默认生成的临时文件路径
    public static final String PcmUrl = SoundApp.getContext().getFilesDir().getAbsolutePath() + "/tts.pcm";
    public static final String CacheMp3Url = SoundApp.getContext().getFilesDir().getAbsolutePath() + "/tts.mp3";
    public static final String ExportMp3Url =Environment.getExternalStorageDirectory() + "/novasound/";
    public static final String AudioHistoryMp3Url =SoundApp.getContext().getFilesDir().getAbsolutePath() + "/novasound/";



    public static long getFileSize(String fileName){
        FileChannel fc= null;
        try {
            File f= new File(fileName);
            if (f.exists() && f.isFile()){
                FileInputStream fis= new FileInputStream(f);
                fc= fis.getChannel();
                long size = fc.size();
                return size;
            }
        } catch (FileNotFoundException e) {
        } catch (IOException ignored) {
        } finally {
            if (null!=fc){
                try{
                    fc.close();
                }catch(IOException e){
                }
            }
        }
        return 0;
    }


    /**
     * 根据文件路径拷贝文件
     * @param src 源文件
     * @return boolean 成功true、失败false
     */
    public static boolean copyFile(File src, String destPath,String filename) {
        boolean result = false;
        if ((src == null) || (destPath== null)) {
            return result;
        }

        File file = new File(destPath);
        if (!file.exists()){
            file.mkdirs();
        }

        File dest= new File(destPath+filename);
        if (dest!= null && dest.exists()) {
            dest.delete(); // delete file
        }
        try {
            dest.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileChannel srcChannel = null;
        FileChannel dstChannel = null;

        try {
            srcChannel = new FileInputStream(src).getChannel();
            dstChannel = new FileOutputStream(dest).getChannel();
            srcChannel.transferTo(0, srcChannel.size(), dstChannel);
            result = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            return result;
        }
        try {
            srcChannel.close();
            dstChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


}

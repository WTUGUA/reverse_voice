package com.vavapps.sound.app.utils;

import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;
import java.util.Date;

public class FilesUtil {

    public static final String ROOT_DIR = Environment.getExternalStorageDirectory().getPath() + "/audioclip";
    public static final String PROJECT_DIR = ROOT_DIR + "/project/";
    public static final String CACHE_DIR = ROOT_DIR + "/cache/";

    /**
     * 删除文件
     *
     * @param filePath
     * @return
     */
    public static boolean deleteFile(String filePath) {
        if (LogUtils.DEBUG) {
            LogUtils.e("deleteFile path " + filePath);
        }

        if (!TextUtils.isEmpty(filePath)) {
            final File file = new File(filePath);
            if (LogUtils.DEBUG) {
                LogUtils.e("deleteFile path exists " + file.exists());
            }
            if (file.exists()) {
                return file.delete();
            }
        }
        return false;
    }

    /**
     * 删除文件夹下所有文件
     *
     * @return
     */
    public static void deleteDirectoryAllFile(String directoryPath) {
        final File file = new File(directoryPath);
        deleteDirectoryAllFile(file);
    }

    public static void deleteDirectoryAllFile(File file) {
        if (!file.exists()) {
            return;
        }

        boolean rslt = true;// 保存中间结果
        if (!(rslt = file.delete())) {// 先尝试直接删除
            // 若文件夹非空。枚举、递归删除里面内容
            final File subs[] = file.listFiles();
            final int size = subs.length - 1;
            for (int i = 0; i <= size; i++) {
                if (subs[i].isDirectory())
                    deleteDirectoryAllFile(subs[i]);// 递归删除子文件夹内容
                rslt = subs[i].delete();// 删除子文件夹本身
            }
            // rslt = file.delete();// 删除此文件夹本身
        }

        if (!rslt) {
            if (LogUtils.DEBUG) {
                LogUtils.w("无法删除:" + file.getName());
            }
            return;
        }
    }

    /**
     * 根据后缀名删除文件
     *
     * @param delPath
     *            path of file
     * @param delEndName
     *            end name of file
     * @return boolean the result
     */
    public static boolean deleteEndFile(String delPath, String delEndName) {
        // param is null
        if (delPath == null || delEndName == null) {
            return false;
        }
        try {
            // create file
            final File file = new File(delPath);
            if (file != null) {
                if (file.isDirectory()) {
                    // file list
                    String[] fileList = file.list();
                    File delFile = null;

                    // digui
                    final int size = fileList.length;
                    for (int i = 0; i < size; i++) {
                        // create new file
                        delFile = new File(delPath + "/" + fileList[i]);
                        if (delFile != null && delFile.isFile()) {// 删除该文件夹下所有文件以delEndName为后缀的文件（不包含子文件夹里的文件）
                            // if (delFile != null) {//
                            // 删除该文件夹下所有文件以delEndName为后缀的文件（包含子文件夹里的文件）
                            deleteEndFile(delFile.toString(), delEndName);
                        } else {
                            // nothing
                        }
                    }
                } else if (file.isFile()) {

                    // check the end name
                    if (file.toString().contains(".")
                            && file.toString()
                            .substring(
                                    (file.toString().lastIndexOf(".") + 1))
                            .equals(delEndName)) {
                        // file delete
                        file.delete();
                    }
                }
            }
        } catch (Exception ex) {
            if (LogUtils.DEBUG) {
                LogUtils.e(ex);
            }
            return false;
        }
        return true;
    }

    /**
     * 删除文件夹内所有文件
     *
     * @param delpath
     *            delpath path of file
     * @return boolean the result
     */
    public static boolean deleteAllFile(String delpath) {
        try {
            // create file
            final File file = new File(delpath);

            if (!file.isDirectory()) {
                file.delete();
            } else if (file.isDirectory()) {

                final String[] filelist = file.list();
                final int size = filelist.length;
                for (int i = 0; i < size; i++) {

                    // create new file
                    final File delfile = new File(delpath + "/" + filelist[i]);
                    if (!delfile.isDirectory()) {
                        delfile.delete();
                    } else if (delfile.isDirectory()) {
                        // digui
                        deleteFile(delpath + "/" + filelist[i]);
                    }
                }
                file.delete();
            }
        } catch (Exception ex) {
            if (LogUtils.DEBUG) {
                LogUtils.e(ex);
            }
            return false;
        }
        return true;
    }

    public static String getSuffix(String path){
        return path.substring(path.lastIndexOf(".") + 1);
    }

    /**
     * 删除目录（文件夹）以及目录下的文件
     *
     * @param sPath
     *            被删除目录的文件路径
     * @return 目录删除成功返回true，否则返回false
     */
    public static boolean deleteDirectory(String sPath) {

        if (TextUtils.isEmpty(sPath)) {
            return false;
        }

        boolean flag;
        // 如果sPath不以文件分隔符结尾，自动添加文件分隔符
        if (!sPath.endsWith(File.separator)) {
            sPath = sPath + File.separator;
        }
        final File dirFile = new File(sPath);
        // 如果dir对应的文件不存在，或者不是一个目录，则退出
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        flag = true;
        // 删除文件夹下的所有文件(包括子目录)
        final File[] files = dirFile.listFiles();
        if (files != null && files.length > 0) {
            for (int i = 0; i < files.length; i++) {
                // 删除子文件
                if (files[i].isFile()) {
                    flag = deleteFile(files[i].getAbsolutePath());
                    if (!flag)
                        break;
                } // 删除子目录
                else {
                    flag = deleteDirectory(files[i].getAbsolutePath());
                    if (!flag)
                        break;
                }
            }
        }
        if (!flag)
            return false;
        // 删除当前目录
        if (dirFile.delete()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取后缀名
     *
     * @param path
     *            全路径
     * @return
     */
    public static String getFileExtName(String path) {
        String ext = "";
        if ((path != null) && (path.length() > 0)) {
            int dot = path.lastIndexOf('.');
            if ((dot > -1) && (dot < (path.length() - 1))) {
                ext = path.substring(dot + 1);
            }
        }
        return ext;
    }

    /**
     * 获取文件的存放文件夹路径
     *
     * @param filePath
     * @return
     */
    public static String getFileDir(String filePath) {
        int index = filePath.lastIndexOf("/");
        if (index == -1) return "";
        return filePath.substring(0, filePath.lastIndexOf("/") + 1);
    }

    /**
     * 获取文件名
     *
     * @param path
     *            全路径
     * @return
     */
    public static String getFileName(String path) {
        if (!TextUtils.isEmpty(path)) {
            return path.substring(path.lastIndexOf(File.separator) + 1);
        }
        return "";
    }


    /**
     * 获取文件名并去后缀
     *
     * @param path
     *            全路径
     * @return
     */
    public static String getFileNameWithoutSuffix(String path) {
        String fileName = path.substring(path.lastIndexOf(File.separator) + 1, path.length());
        int index = fileName.lastIndexOf(".");
        if (index == -1) return "";
        else return fileName.substring(0, index);
    }

    /**
     * 获取文件所在的文件路径
     *
     * @param path
     * @return
     */
    public static String getFilePath(String path) {
        return path.substring(0, path.lastIndexOf(File.separator) + 1);
    }

    /**
     * 复制文件
     *
     * @param srcPath : 源文件全路径
     * @param destPath : 目标文件全路径
     * @return
     */
    public static long copyFile(String srcPath, String destPath) {
        try {
            int position = destPath.lastIndexOf(File.separator);
            String dir = destPath.substring(0, position);
            String newFileName = destPath.substring(position+1);
            final File cacheDir = new File(dir);
            if (!cacheDir.exists()) {
                cacheDir.mkdirs();
            }
            return copyFile(new File(srcPath), new File(dir), newFileName);
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 复制文件(以超快的速度复制文件)
     *
     * @param srcFile
     *            源文件File
     * @param destDir
     *            目标目录File
     * @param newFileName
     *            新文件名
     * @return 实际复制的字节数，如果文件、目录不存在、文件为null或者发生IO异常，返回-1
     */
    @SuppressWarnings("resource")
    public static long copyFile(final File srcFile, final File destDir, String newFileName) {
        long copySizes = 0;
        if (!srcFile.exists()) {
            if (LogUtils.DEBUG) {
                LogUtils.d("源文件不存在");
            }
            copySizes = -1;
        } else if (!destDir.exists()) {
            if (LogUtils.DEBUG) {
                LogUtils.d("目标目录不存在");
            }
            copySizes = -1;
        } else if (newFileName == null) {
            if (LogUtils.DEBUG) {
                LogUtils.d("文件名为null");
            }
            copySizes = -1;
        } else {
            FileChannel fcin = null;
            FileChannel fcout = null;
            try {
                fcin = new FileInputStream(srcFile).getChannel();
                fcout = new FileOutputStream(new File(destDir, newFileName)).getChannel();
                long size = fcin.size();
                fcin.transferTo(0, fcin.size(), fcout);
                copySizes = size;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fcin != null) {
                        fcin.close();
                    }
                    if (fcout != null) {
                        fcout.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return copySizes;
    }

    /**
     * 获取文件夹下所有文件大小
     *
     * @param f
     * @return
     */
    public static long getFileSize(File f) {
        long size = 0;
        if (f.isDirectory()){
            try {
                File flist[] = f.listFiles();
                for (int i = 0; i < flist.length; i++) {
                    final File file = flist[i];
                    if (file == null) {
                        continue;
                    }
                    if (file.isDirectory()) {
                        size = size + getFileSize(file);
                    } else {
                        size = size + file.length();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else return f.length();
        return size;
    }

    /**
     * 调用此方法自动计算指定文件或指定文件夹的大小
     *
     * @param file 文件
     * @return 计算好的带B、KB、MB、GB的字符串
     */
    public static String getAutoFileOrFilesSize(File file) {
        if (file == null) {
            return "0B";
        }

        final long blockSize = getFileSize(file);
        return FormetFileSize(blockSize);
    }
    /**
     *
     * 转换文件大小
     * @param fileS
     * @return
     */
    public static String FormetFileSize(long fileS) {
        final DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        String wrongSize = "0B";
        if (fileS == 0) {
            return wrongSize;
        }
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "GB";
        }
        return fileSizeString;
    }

    public static String transfSize(long size) {
        String resultString = "";
        DecimalFormat df = new DecimalFormat("###.##");
        float f;
        if (size < 1048576L) {
            f = (float)size / 1024.0F;
            resultString = df.format((new Float(f)).doubleValue()) + "KB";
        } else {
            f = (float)size / 1048576.0F;
            resultString = df.format((new Float(f)).doubleValue()) + "MB";
        }

        return resultString;
    }


    /**
     * 获取文件保存路径
     *
     * @return
     */
    public static String getSaveFilePath() {
        File file = new File(FilesUtil.CACHE_DIR);
        if (!file.exists()) {
            file.mkdirs();
        }
        File wavFile = new File(file, new Date().getTime()+".wav");
        return wavFile.getAbsolutePath();
    }

}


package com.anzhuo.video.app.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.text.TextUtils;

import com.orhanobut.logger.Logger;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created with Android Studio.
 * Authour：Eric_chan
 * Date：2016/6/23 11:20
 * Des：
 */
public class FileUtil {

    public static final String ENCODING = "UTF-8";

    /**
     * 从文件路径里获取文件名 ，含文件后缀名
     *
     * @param filePath
     * @return
     */
    public static String getFileName(String filePath) {
        if (TextUtils.isEmpty(filePath))
            return filePath;

        int filePosi = filePath.lastIndexOf(File.separator);
        return (filePosi == -1) ? filePath : filePath.substring(filePosi + 1);
    }
    /**
     * 获得当前版本号
     *
     * @return
     */
    public static String getVersion(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * 获取该文件的所属上级文件夹
     *
     * @param filePath
     * @return
     */
    public static String getFolderName(String filePath) {
        if (TextUtils.isEmpty(filePath))
            return filePath;

        File file = new File(filePath);
        if (file.exists() && file.isDirectory())
            return filePath;

        int filePosi = filePath.lastIndexOf(File.separator);
        return (filePosi == -1) ? "" : filePath.substring(0, filePosi);
    }

    /**
     * 获取该文件路径的文件后缀名
     *
     * @param filePath
     * @return
     */
    public static String getFileExtension(String filePath) {
        if (TextUtils.isEmpty(filePath))
            return filePath;

        int extenPosi = filePath.lastIndexOf(".");
        int filePosi = filePath.lastIndexOf(File.separator);
        if (extenPosi == -1)
            return "";
        return (filePosi >= extenPosi) ? "" : filePath.substring(extenPosi + 1).toLowerCase();
    }

    /**
     * 创建文件夹
     */
    public static boolean makeDirs(String filePath) {
        String folderName = getFolderName(filePath);

        if (TextUtils.isEmpty(folderName))
            return false;

        File folder = new File(folderName);
        if (folder.exists() && folder.isDirectory())
            return true;

        return folder.mkdirs();
    }

    /**
     * 判断制定文件路径的文件或文件夹是否存在
     *
     * @param filePath
     * @return
     */
    public static boolean isFileExist(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return false;
        }

        File file = new File(filePath);
        return file.exists();
    }

    public static boolean isFileExist(File file) {
        return file != null && file.exists() && file.length() > 0;
    }

    /**
     * 重命名
     *
     * @param oldPath 原文件路径
     * @param newPath 新文件路径
     *                *
     */
    public static void renameFile(String oldPath, String newPath) {
        File oldFile = new File(oldPath);
        File newFile = new File(newPath);
        if (oldFile.exists())
            oldFile.renameTo(newFile);
    }

    public static boolean writeFile(String filePath, String content, boolean append) {
        if (TextUtils.isEmpty(content)) {
            return false;
        }

        FileWriter fileWriter = null;

        try {
            makeDirs(filePath);
            fileWriter = new FileWriter(filePath, append);
            fileWriter.write(content);
            fileWriter.close();
            return true;
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (IOException e) {
                    throw new RuntimeException("IOException occurred. ", e);
                }
            }
        }
    }


    /**
     * write file, the string will be written to the begin of the file
     *
     * @param filePath
     * @param content
     * @return
     */
    public static boolean writeFile(String filePath, String content) {
        return writeFile(filePath, content, false);
    }


    /**
     * write file, the bytes will be written to the begin of the file
     *
     * @param filePath
     * @param stream
     * @return
     * @see {@link #writeFile(String, InputStream, boolean)}
     */
//    public static boolean writeFile(String filePath, InputStream stream) {
//        return writeFile(filePath, stream, false);
//    }

    /**
     * write file
     *
     * @param filePath the file to be opened for writing.
     * @param stream   the input stream
     * @param append   if <code>true</code>, then bytes will be written to the end of the file rather than the beginning
     * @return return true
     * @throws RuntimeException if an error occurs while operator FileOutputStream
     */
//    public static boolean writeFile(String filePath, InputStream stream, boolean append) {
//        return writeFile(filePath != null ? new File(filePath) : null, stream, append);
//    }

    /**
     * write file, the bytes will be written to the begin of the file
     *
     * @param file
     * @param stream
     * @return
     * @see {@link #writeFile(File, InputStream, boolean)}
     */
//    public static boolean writeFile(File file, InputStream stream) {
//        return writeFile(file, stream, false);
//    }

    /**
     * write file
     *
     * @param file   the file to be opened for writing.
     * @param stream the input stream
     * @param append if <code>true</code>, then bytes will be written to the end of the file rather than the beginning
     * @return return true
     * @throws RuntimeException if an error occurs while operator FileOutputStream
     */
//    public static boolean writeFile(File file, InputStream stream, boolean append) {
//        OutputStream o = null;
//        try {
//            makeDirs(file.getAbsolutePath());
//            o = new FileOutputStream(file, append);
//            byte data[] = new byte[1024];
//            int length = -1;
//            while ((length = stream.read(data)) != -1) {
//                o.write(data, 0, length);
//            }
//            o.flush();
//            return true;
//        } catch (FileNotFoundException e) {
//            throw new RuntimeException("FileNotFoundException occurred. ", e);
//        } catch (IOException e) {
//            throw new RuntimeException("IOException occurred. ", e);
//        } finally {
//            Utils.closeCloseable(o);
//            Utils.closeCloseable(stream);
//        }
//    }

    /**
     * Indicates if this file represents a directory on the underlying file system.
     *
     * @param directoryPath
     * @return
     */
    public static boolean isFolderExist(String directoryPath) {
        if (TextUtils.isEmpty(directoryPath)) {
            return false;
        }

        File dire = new File(directoryPath);
        return (dire.exists() && dire.isDirectory());
    }

    /**
     * 获取临时文件名
     *
     * @param fileame
     * @return
     */
    public static String getTempFileName(String fileame) {
        return "temp_" + fileame;
    }

    /**
     * 删除文件或文件夹
     *
     * @param path
     * @return
     */
    public static boolean deleteFile(String path) {
        if (TextUtils.isEmpty(path))
            return false;
        File file = new File(path);
        if (!file.exists())
            return true;

        if (file.isFile())
            return deleteFileSafely(file);

        for (File f : file.listFiles()) {
            if (!deleteFile(f.getAbsolutePath()))
                return false;
        }
        return deleteFileSafely(file);
    }

    /**
     * 安全删除文件. 解决文件删除后重新创建导致报错的问题：open failed: EBUSY (Device or resource busy)
     * http://stackoverflow.com/questions/11539657/open-failed-ebusy-device-or-resource-busy
     *
     * @param file
     * @return
     */
    public static boolean deleteFileSafely(File file) {
        if (file == null) return true;
        String tmpPath = file.getAbsolutePath() + System.currentTimeMillis();
        File tmp = new File(tmpPath);
        file.renameTo(tmp);
        return tmp.delete();
    }

    /**
     * 读取文本文件为String
     *
     * @param file
     * @return
     */
    public static String readFileAsString(File file) {
        if (!isFileExist(file))
            return null;

        StringBuilder sb = new StringBuilder();
        BufferedReader br = null;

        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String line;

            while ((line = br.readLine()) != null) {
                sb.append(line).append('\n');
            }

            return sb.toString();
        } catch (Exception e) {
            // do nothing
        } finally {
            if (br != null) {
                try {
                    br.close();

                } catch (IOException e) {
//                    LogManager.e(e);
                }
            }
        }

        return null;
    }

    /**
     * 读取文本文件为String
     *
//     * @param inputStream
     * @return
     */
    /*public static String readFileAsString(InputStream inputStream) {

        StringBuilder sb = new StringBuilder();
        BufferedReader br = null;

        try {
            br = new BufferedReader(new InputStreamReader(inputStream));
            String line;

            while ((line = br.readLine()) != null) {
                sb.append(line).append('\n');
            }

            return sb.toString();
        } catch (Exception e) {
            LogManager.e(e);
        } finally {
            if (br != null) {
                try {
                    br.close();

                } catch (IOException e) {
                    LogManager.e(e);
                }
            }
        }

        return null;
    }*/


   /* public static boolean writeSerializeFile(String filename, Context context, Object obj) {
        FileOutputStream ostream = null;
        try {
            ostream = context.openFileOutput(filename, Context.MODE_PRIVATE);
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
            return false;
        }

        ObjectOutputStream p = null;
        try {
            p = new ObjectOutputStream(ostream);
            p.writeObject(obj);
            p.flush();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            Utils.closeCloseable(p);
            Utils.closeCloseable(ostream);
        }
        return true;
    }*/
    @SuppressWarnings("unchecked")
   /* public static Object readSerializeFile(String filename, Context context) {
        FileInputStream istream = null;
        try {
            istream = context.openFileInput(filename);
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
            return null;
        }
        ObjectInputStream q = null;
        try {
            q = new ObjectInputStream(istream);
            return q.readObject();
        } catch (IOException e) {
            LogManager.e(e);
        } catch (ClassNotFoundException e) {
            LogManager.e(e);
        } finally {
            Utils.closeCloseable(q);
            Utils.closeCloseable(istream);
        }
        return null;
    }*/

    /**
     * get file name from path, not include suffix
     * <p/>
     * <pre>
     *      getFileNameWithoutExtension(null)               =   null
     *      getFileNameWithoutExtension("")                 =   ""
     *      getFileNameWithoutExtension("   ")              =   "   "
     *      getFileNameWithoutExtension("abc")              =   "abc"
     *      getFileNameWithoutExtension("a.mp3")            =   "a"
     *      getFileNameWithoutExtension("a.b.rmvb")         =   "a.b"
     *      getFileNameWithoutExtension("c:\\")              =   ""
     *      getFileNameWithoutExtension("c:\\a")             =   "a"
     *      getFileNameWithoutExtension("c:\\a.b")           =   "a"
     *      getFileNameWithoutExtension("c:a.txt\\a")        =   "a"
     *      getFileNameWithoutExtension("/home/admin")      =   "admin"
     *      getFileNameWithoutExtension("/home/admin/a.txt/b.mp3")  =   "b"
     * </pre>
     *
     * @param filePath
     * @return file name from path, not include suffix
     * @see
     */
    public static String getFileNameWithoutExtension(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return filePath;
        }

        int extenPosi = filePath.lastIndexOf(".");
        int filePosi = filePath.lastIndexOf(File.separator);
        if (filePosi == -1) {
            return (extenPosi == -1 ? filePath : filePath.substring(0, extenPosi));
        }
        if (extenPosi == -1) {
            return filePath.substring(filePosi + 1);
        }
        return (filePosi < extenPosi ? filePath.substring(filePosi + 1, extenPosi) : filePath.substring(filePosi + 1));

    }


    public static void deleteDir(File dest) throws IOException {

        if (dest.exists() == false) {
            throw new FileNotFoundException("Not a directory: " + dest);
        }

        if (dest.isDirectory() == false) {
            throw new IOException("Not a directory: " + dest);
        }

        File[] files = dest.listFiles();
        if (files == null) {
            throw new IOException("Failed to list contents of: " + dest);
        }

        IOException exception = null;
        for (File file : files) {
            try {
                if (file.isDirectory()) {
                    deleteDir(file);
                } else {
                    file.delete();
                }
            } catch (IOException ioex) {
                exception = ioex;
                continue;
            }
        }
        if (exception != null) {
            throw exception;
        }
    }

    public static boolean initDownFile(File file) {
        if (file.exists()) {
            return true;
        }
        return false;
    }


    private static final int BUFFER = 8192;
    public static final String DOWNPATH = "/IntimateWeather/download/";

    public static String FileName = "IntimateWeather";

    // 读取文件
    public static String readTextFile(File file) throws IOException {
        String text = null;
        InputStream is = null;
        try {
            is = new FileInputStream(file);
            text = readTextInputStream(is);
        } finally {
            if (is != null) {
                is.close();
            }
        }
        return text;
    }

    // 从流中读取文件
    public static String readTextInputStream(InputStream is) throws IOException {
        StringBuffer strbuffer = new StringBuffer();
        String line;
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(is));
            while ((line = reader.readLine()) != null) {
                strbuffer.append(line).append("\r\n");
            }
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
        return strbuffer.toString();
    }

    // 将文本内容写入文件
    public static void writeTextFile(File file, String str) throws IOException {
        DataOutputStream out = null;
        try {
            out = new DataOutputStream(new FileOutputStream(file));
            out.write(str.getBytes());
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    // 复制文件
    public static void copyFile(File sourceFile, File targetFile)
            throws IOException {
        BufferedInputStream inBuff = null;
        BufferedOutputStream outBuff = null;
        try {
            inBuff = new BufferedInputStream(new FileInputStream(sourceFile));
            outBuff = new BufferedOutputStream(new FileOutputStream(targetFile));
            byte[] buffer = new byte[BUFFER];
            int length;
            while ((length = inBuff.read(buffer)) != -1) {
                outBuff.write(buffer, 0, length);
            }
            outBuff.flush();
        } finally {
            if (inBuff != null) {
                inBuff.close();
            }
            if (outBuff != null) {
                outBuff.close();
            }
        }
    }

    public static void deleteFile(File file) {
        if (file.exists()) { // 判断文件是否存在
            if (file.isFile()) { // 判断是否是文件
                file.delete(); // delete()方法 你应该知道 是删除的意思;
            } else if (file.isDirectory()) { // 否则如果它是一个目录
                File files[] = file.listFiles(); // 声明目录下所有的文件 files[];
                for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
                    deleteFile(files[i]); // 把每个文件 用这个方法进行迭代
                }
            }
            file.delete();
        } else {
            Logger.e("文件不存在!");
        }
    }

    public static boolean deleteFileDir(String dirName, boolean isRecurse) {
        boolean blret = false;
        try {
            File f = new File(dirName);
            if (f == null || !f.exists()) {
                return false;
            }
            if (f.isFile()) {
                blret = f.delete();
                return blret;
            } else {
                File[] flst = f.listFiles();
                if (flst == null || flst.length <= 0) {
                    f.delete();
                    return true;
                }
                int filenumber = flst.length;
                File[] fchilda = f.listFiles();
                for (int i = 0; i < filenumber; i++) {
                    File fchild = fchilda[i];
                    if (fchild.isFile()) {
                        blret = fchild.delete();
                        if (!blret) {
                            break;
                        }
                    } else if (isRecurse) {
                        blret = deleteFileDir(fchild.getAbsolutePath(), true);
                    }
                }
                // 删除当前文件
                blret = new File(dirName).delete();
            }
        } catch (Exception e) {
            // Log.d(FILE_TAG, e.getMessage());
            blret = false;
        }

        return blret;
    }

    public static File getCacheDir(Context context) {
        File cacheDir = context.getCacheDir();
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            cacheDir = new File(Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + File.separator + FileName);
            if (!cacheDir.exists())
                cacheDir.mkdir();
        }
        return cacheDir;
    }

    public static final String gifWeatherName = "gifWeather";

    public static String getGifWeatherUri(Context context, String gifFileName) {
        return getCacheDir(context) + File.separator + gifWeatherName + File.separator + gifFileName;
    }

    public static boolean isExist(String strUri) {
        File file = new File(strUri);
        return file.exists();
    }
}


package com.ces.config.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * 打zip包和解压缩zip包 ZipInputStream 和 ZipFile 的区别：
 * 1、类ZipInputStream读出ZIP文件序列（简单地说就是读出这个ZIP文件压缩了多少文件），而类ZipFile使用内嵌的随机文件访问机制读出其中的文件内容，所以不必顺序的读出ZIP压缩文件序列；
 * 2、ZipFile使用内嵌的高速缓冲，所以如果ZipFile被重复调用的话，文件只被打开一次。缓冲值在第二次打开进使用。
 * 因为使用ZipFile打开的所有ZIP文件都在内存中存在映射，所以使用ZipFile的性能优于ZipInputStream。然而，如果同一ZIP文件的内容在程序执行期间经常改变，或是重载的话，使用ZipInputStream就成为你的首选了。
 */
public class ZipUtil {

    public static final int BUFFER = 2048;

    /**
     * 压缩file文件（夹）
     * 
     * @param zipOutputStream zip输出流
     * @param step zip中的路径
     * @param file 源文件
     */
    public static void zipFile(ZipOutputStream zipOutputStream, String step, File file) {
        if (StringUtil.isNotEmpty(step) && !step.endsWith("/")) {
            step += "/";
        }
        if (file.isDirectory()) {
            if (file.listFiles().length == 0) {
                try {
                    zipOutputStream.putNextEntry(new ZipEntry(step + file.getName() + "/"));
                    zipOutputStream.closeEntry();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                File[] listFiles = file.listFiles();
                for (File tempFile : listFiles) {
                    zipFile(zipOutputStream, step + file.getName(), tempFile);
                }
            }
        } else {
            try {
                zipOutputStream.putNextEntry(new ZipEntry(step + file.getName()));
                FileInputStream fis = new FileInputStream(file);
                byte[] data = new byte[BUFFER];
                int count;
                while ((count = fis.read(data, 0, BUFFER)) != -1) {
                    zipOutputStream.write(data, 0, count);
                }
                fis.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 压缩file文件（夹）
     * 
     * @param dest 目标文件
     * @param step zip中的路径
     * @param sourse 要压缩的文件
     */
    public static void zip(File dest, String step, File sourse) {
        FileOutputStream fos = null;
        ZipOutputStream zos = null;
        try {
            // 压缩file文件（夹）
            if (!dest.exists()) {
                dest.createNewFile();
            }
            fos = new FileOutputStream(dest);
            zos = new ZipOutputStream(fos);
            if (sourse.isDirectory()) {
                File[] listFiles = sourse.listFiles();
                for (File tempFile : listFiles) {
                    zipFile(zos, step, tempFile);
                }
            } else {
                zipFile(zos, step, sourse);
            }
            zos.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != zos) {
                try {
                    zos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != fos) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 用ZipInputStream方式解压缩file文件（夹）
     * 
     * @param sourse zip包
     * @param dest 目标路径
     * @throws Exception
     */
    public static void unzip(File sourse, String dest) throws Exception {
        if (StringUtil.isNotEmpty(dest) && !dest.endsWith("/")) {
            dest += "/";
            File destDirectory = new File(dest);
            if (!destDirectory.exists()) {
                destDirectory.mkdirs();
            }
        }
        FileInputStream fis = new FileInputStream(sourse);
        ZipInputStream zis = new ZipInputStream(new BufferedInputStream(fis));

        ZipEntry zipEntry;
        File zipEntryFile;
        while ((zipEntry = zis.getNextEntry()) != null) {
            zipEntryFile = mkFile(dest + zipEntry.getName());
            if (zipEntryFile.isDirectory()) {
                continue;
            }
            FileOutputStream fos = new FileOutputStream(zipEntryFile);
            BufferedOutputStream bos = new BufferedOutputStream(fos, BUFFER);
            int count;
            byte[] data = new byte[BUFFER];
            while ((count = zis.read(data, 0, BUFFER)) != -1) {
                bos.write(data, 0, count);
            }
            bos.flush();
            bos.close();
            fos.close();
        }
        zis.close();
        fis.close();
    }

    /**
     * 用ZipFile方式解压缩file文件（夹）
     * 
     * @param sourse zip包
     * @param dest 目标路径
     * @throws Exception
     */
    public static void unzipFile(File sourse, String dest) throws Exception {
        if (StringUtil.isNotEmpty(dest) && !dest.endsWith("/")) {
            dest += "/";
            File destDirectory = new File(dest);
            if (!destDirectory.exists()) {
                destDirectory.mkdirs();
            }
        }
        ZipFile zipFile = new ZipFile(sourse);
        Enumeration<? extends ZipEntry> entries = zipFile.entries();
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        ZipEntry zipEntry;
        File file;
        while (entries.hasMoreElements()) {
            zipEntry = entries.nextElement();
            file = mkFile(dest + zipEntry.getName());
            if (file.isDirectory()) {
                continue;
            }
            bis = new BufferedInputStream(zipFile.getInputStream(zipEntry));
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos, BUFFER);
            int count;
            byte[] data = new byte[BUFFER];
            while (-1 != (count = bis.read(data, 0, BUFFER))) {
                bos.write(data, 0, count);
            }
            bos.flush();
            bos.close();
            fos.close();
            bis.close();
        }
    }

    /**
     * 创建新文件
     * 
     * @param path 文件路径
     * @return File
     */
    public static File mkFile(String path) {
        File file;
        path.replaceAll("\\\\", "/");
        if (path.indexOf(".") != -1) {
            String derictory = path.substring(0, path.lastIndexOf("/"));
            File dest = new File(derictory);
            if (!dest.exists()) {
                dest.mkdirs();
            }
            file = new File(path);
        } else {
            file = new File(path);
            if (!file.exists()) {
                file.mkdirs();
            }
        }
        return file;
    }

}

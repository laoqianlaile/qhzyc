package com.ces.config.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class FileUtil {

    private static Log log = LogFactory.getLog(FileUtil.class);

    /**
     * 复制单个文件
     * 
     * @param oldPath 原文件路径
     * @param newPath 复制后路径
     */
    public static void copyFile(String oldPath, String newPath) {
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            File oldfile = new File(oldPath);
            if (oldfile.exists()) {
                is = new FileInputStream(oldPath);
                fos = new FileOutputStream(newPath);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, length);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 复制单个文件（速度较快，留意buffer的大小，对速度有很大影响）
     * 
     * @param source 原文件
     * @param target 目标文件
     */
    public static void nioBufferCopy(File source, File target) {
        FileChannel in = null;
        FileChannel out = null;
        FileInputStream inStream = null;
        FileOutputStream outStream = null;
        try {
            inStream = new FileInputStream(source);
            outStream = new FileOutputStream(target);
            in = inStream.getChannel();
            out = outStream.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(4096);
            while (in.read(buffer) != -1) {
                buffer.flip();
                out.write(buffer);
                buffer.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inStream != null) {
                try {
                    inStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outStream != null) {
                try {
                    outStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 复制单个文件（速度较快，文件越大该方法越有优势）
     * 
     * @param source 原文件
     * @param target 目标文件
     */
    public static void nioTransferCopy(File source, File target) {
        FileChannel in = null;
        FileChannel out = null;
        FileInputStream inStream = null;
        FileOutputStream outStream = null;
        try {
            inStream = new FileInputStream(source);
            outStream = new FileOutputStream(target);
            in = inStream.getChannel();
            out = outStream.getChannel();
            in.transferTo(0, in.size(), out);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inStream != null) {
                try {
                    inStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outStream != null) {
                try {
                    outStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 复制整个文件夹内容
     * 
     * @param oldPath 原文件路径
     * @param newPath 复制后路径
     */
    public static void copyFolder(String oldPath, String newPath) {
        try {
            File oldFile = new File(oldPath);
            if (!oldFile.exists()) {
                return;
            }
            File newFile = new File(newPath);
            if (!newFile.exists()) {
                newFile.mkdirs();
            }
            String[] fileNames = oldFile.list();
            File temp = null;
            if (fileNames != null && fileNames.length > 0) {
                for (int i = 0; i < fileNames.length; i++) {
                    if (".svn".equals(fileNames[i])) {
                        continue;
                    }
                    if (oldPath.endsWith(File.separator)) {
                        temp = new File(oldPath + fileNames[i]);
                    } else {
                        temp = new File(oldPath + File.separator + fileNames[i]);
                    }
                    if (temp.isFile()) {
                        nioTransferCopy(temp, new File(newPath + "/" + (temp.getName()).toString()));
                    }
                    if (temp.isDirectory()) {
                        copyFolder(oldPath + "/" + fileNames[i], newPath + "/" + fileNames[i]);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除文件或文件夹
     * 
     * @param path 文件或文件夹路径
     */
    public static void deleteFile(String path) {
        File file = new File(path);
        if (file.exists()) {
            if (file.isDirectory()) {
                String[] filePaths = file.list();
                for (String filePath : filePaths) {
                    deleteFile(path + "/" + filePath);
                }
            }
            file.delete();
        }
    }

    /**
     * 删除文件或文件夹
     * 
     * @param path 文件或文件夹路径
     */
    public static void deleteFile(File file) {
        if (file.exists()) {
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                for (File child : files) {
                    deleteFile(child);
                }
            }
            file.delete();
        }
    }

    /**
     * 删除文件夹内部文件，文件夹本身不删除
     * 
     * @param path 文件夹路径
     */
    public static void deleteDir(String path) {
        File file = new File(path);
        if (file.exists() && file.isDirectory()) {
            String[] filePaths = file.list();
            for (String filePath : filePaths) {
                deleteFile(path + "/" + filePath);
            }
        }
    }

    /**
     * 判断文件夹是否是空文件夹
     * 
     * @param path 文件夹路径
     * @return boolean
     */
    public static boolean isEmptyDir(String path) {
        boolean flag = true;
        File file = new File(path);
        if (file.exists()) {
            if (file.isDirectory()) {
                String[] filePaths = file.list();
                for (String filePath : filePaths) {
                    if (".svn".equals(filePath)) {
                        continue;
                    }
                    if (!isEmptyDir(path + "/" + filePath)) {
                        flag = false;
                        break;
                    }
                }
            } else {
                flag = false;
            }
        }
        return flag;
    }

    /**
     * 判断文件夹是否是空文件夹
     * 
     * @param path 文件夹路径
     * @return boolean
     */
    public static boolean isEmptyDir(File file) {
        boolean flag = true;
        if (file.exists()) {
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                for (File tempFile : files) {
                    if (".svn".equals(tempFile.getName())) {
                        continue;
                    }
                    if (!isEmptyDir(tempFile)) {
                        flag = false;
                        break;
                    }
                }
            } else {
                flag = false;
            }
        }
        return flag;
    }

    /**
     * 创建文件夹
     * 
     * @param path 文件夹路径
     * @return boolean
     */
    public static boolean mkDir(String path) {
        File dir = new File(path);
        if (!dir.exists()) {
            return dir.mkdirs();
        } else {
            return true;
        }
    }

    /**
     * 打开生成的xls文件，对内容进行替换
     */
    public static void copyExcel(File sourcefile, File targetfile) throws Exception {
        FileInputStream fin = new FileInputStream(sourcefile);
        FileOutputStream fout = new FileOutputStream(targetfile);
        byte[] bytes = new byte[1024];
        int c;
        while ((c = fin.read(bytes)) != -1)
            fout.write(bytes, 0, c);
        fin.close();
        fout.close();
    }

    /**
     * 保存文件
     */
    public static void saveFileStream(HSSFWorkbook workbook2003, File exportFile) throws Exception {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(exportFile);
            workbook2003.write(fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * qiucs 2014-9-17
     * <p>描述: 写文件</p>
     * 
     * @return void 返回类型
     * @throws
     */
    public static void write(File f, String content) {
        FileWriter fw = null;
        try {
            fw = new FileWriter(f);
            fw.write(content);
            fw.flush();
        } catch (Exception e) {
            log.error("写文件出错", e);
        } finally {
            try {
                if (null != fw)
                    fw.close();
            } catch (IOException e) {
                log.error("关闭文件流出错", e);
            }
        }
    }

    /**
     * qiucs 2014-8-21
     * <p>描述: 获取MIME类型</p>
     * 
     * @param format 文件格式
     * @return String 返回类型
     * @throws
     */
    public static String getMimeType(String format) {
        return mimeMap.get(format);
    }

    private static final Map<String, String> mimeMap = new HashMap<String, String>();
	static {

		mimeMap.put("load", "text/html");
		mimeMap.put("123", "application/vnd.lotus-1-2-3");
		mimeMap.put("3ds", "image/x-3ds");
		mimeMap.put("3g2", "video/3gpp");
		mimeMap.put("3ga", "video/3gpp");
		mimeMap.put("3gp", "video/3gpp");
		mimeMap.put("3gpp", "video/3gpp");
		mimeMap.put("602", "application/x-t602");
		mimeMap.put("669", "audio/x-mod");
		mimeMap.put("7z", "application/x-7z-compressed");
		mimeMap.put("a", "application/x-archive");
		mimeMap.put("aac", "audio/mp4");
		mimeMap.put("abw", "application/x-abiword");
		mimeMap.put("abw.crashed", "application/x-abiword");
		mimeMap.put("abw.gz", "application/x-abiword");
		mimeMap.put("ac3", "audio/ac3");
		mimeMap.put("ace", "application/x-ace");
		mimeMap.put("adb", "text/x-adasrc");
		mimeMap.put("ads", "text/x-adasrc");
		mimeMap.put("afm", "application/x-font-afm");
		mimeMap.put("ag", "image/x-applix-graphics");
		mimeMap.put("ai", "application/illustrator");
		mimeMap.put("aif", "audio/x-aiff");
		mimeMap.put("aifc", "audio/x-aiff");
		mimeMap.put("aiff", "audio/x-aiff");
		mimeMap.put("al", "application/x-perl");
		mimeMap.put("alz", "application/x-alz");
		mimeMap.put("amr", "audio/amr");
		mimeMap.put("ani", "application/x-navi-animation");
		mimeMap.put("anim[1-9j]", "video/x-anim");
		mimeMap.put("anx", "application/annodex");
		mimeMap.put("ape", "audio/x-ape");
		mimeMap.put("arj", "application/x-arj");
		mimeMap.put("arw", "image/x-sony-arw");
		mimeMap.put("as", "application/x-applix-spreadsheet");
		mimeMap.put("asc", "text/plain");
		mimeMap.put("asf", "video/x-ms-asf");
		mimeMap.put("asp", "application/x-asp");
		mimeMap.put("ass", "text/x-ssa");
		mimeMap.put("asx", "audio/x-ms-asx");
		mimeMap.put("atom", "application/atom+xml");
		mimeMap.put("au", "audio/basic");
		mimeMap.put("avi", "video/x-msvideo");
		mimeMap.put("aw", "application/x-applix-word");
		mimeMap.put("awb", "audio/amr-wb");
		mimeMap.put("awk", "application/x-awk");
		mimeMap.put("axa", "audio/annodex");
		mimeMap.put("axv", "video/annodex");
		mimeMap.put("bak", "application/x-trash");
		mimeMap.put("bcpio", "application/x-bcpio");
		mimeMap.put("bdf", "application/x-font-bdf");
		mimeMap.put("bib", "text/x-bibtex");
		mimeMap.put("bin", "application/octet-stream");
		mimeMap.put("blend", "application/x-blender");
		mimeMap.put("blender", "application/x-blender");
		mimeMap.put("bmp", "image/bmp");
		mimeMap.put("bz", "application/x-bzip");
		mimeMap.put("bz2", "application/x-bzip");
		mimeMap.put("c", "text/x-csrc");
		mimeMap.put("c++", "text/x-c++src");
		mimeMap.put("cab", "application/vnd.ms-cab-compressed");
		mimeMap.put("cb7", "application/x-cb7");
		mimeMap.put("cbr", "application/x-cbr");
		mimeMap.put("cbt", "application/x-cbt");
		mimeMap.put("cbz", "application/x-cbz");
		mimeMap.put("cc", "text/x-c++src");
		mimeMap.put("cdf", "application/x-netcdf");
		mimeMap.put("cdr", "application/vnd.corel-draw");
		mimeMap.put("cer", "application/x-x509-ca-cert");
		mimeMap.put("cert", "application/x-x509-ca-cert");
		mimeMap.put("cgm", "image/cgm");
		mimeMap.put("chm", "application/x-chm");
		mimeMap.put("chrt", "application/x-kchart");
		mimeMap.put("class", "application/x-java");
		mimeMap.put("cls", "text/x-tex");
		mimeMap.put("cmake", "text/x-cmake");
		mimeMap.put("cpio", "application/x-cpio");
		mimeMap.put("cpio.gz", "application/x-cpio-compressed");
		mimeMap.put("cpp", "text/x-c++src");
		mimeMap.put("cr2", "image/x-canon-cr2");
		mimeMap.put("crt", "application/x-x509-ca-cert");
		mimeMap.put("crw", "image/x-canon-crw");
		mimeMap.put("cs", "text/x-csharp");
		mimeMap.put("csh", "application/x-csh");
		mimeMap.put("css", "text/css");
		mimeMap.put("cssl", "text/css");
		mimeMap.put("csv", "text/csv");
		mimeMap.put("cue", "application/x-cue");
		mimeMap.put("cur", "image/x-win-bitmap");
		mimeMap.put("cxx", "text/x-c++src");
		mimeMap.put("d", "text/x-dsrc");
		mimeMap.put("dar", "application/x-dar");
		mimeMap.put("dbf", "application/x-dbf");
		mimeMap.put("dc", "application/x-dc-rom");
		mimeMap.put("dcl", "text/x-dcl");
		mimeMap.put("dcm", "application/dicom");
		mimeMap.put("dcr", "image/x-kodak-dcr");
		mimeMap.put("dds", "image/x-dds");
		mimeMap.put("deb", "application/x-deb");
		mimeMap.put("der", "application/x-x509-ca-cert");
		mimeMap.put("desktop", "application/x-desktop");
		mimeMap.put("dia", "application/x-dia-diagram");
		mimeMap.put("diff", "text/x-patch");
		mimeMap.put("divx", "video/x-msvideo");
		mimeMap.put("djv", "image/vnd.djvu");
		mimeMap.put("djvu", "image/vnd.djvu");
		mimeMap.put("dng", "image/x-adobe-dng");
		mimeMap.put("doc", "application/msword");
		mimeMap.put("docbook", "application/docbook+xml");
		mimeMap.put("docm",
				"application/vnd.openxmlformats-officedocument.wordprocessingml.document");
		mimeMap.put("docx",
				"application/vnd.openxmlformats-officedocument.wordprocessingml.document");
		mimeMap.put("dot", "text/vnd.graphviz");
		mimeMap.put("dsl", "text/x-dsl");
		mimeMap.put("dtd", "application/xml-dtd");
		mimeMap.put("dtx", "text/x-tex");
		mimeMap.put("dv", "video/dv");
		mimeMap.put("dvi", "application/x-dvi");
		mimeMap.put("dvi.bz2", "application/x-bzdvi");
		mimeMap.put("dvi.gz", "application/x-gzdvi");
		mimeMap.put("dwg", "image/vnd.dwg");
		mimeMap.put("dxf", "image/vnd.dxf");
		mimeMap.put("e", "text/x-eiffel");
		mimeMap.put("egon", "application/x-egon");
		mimeMap.put("eif", "text/x-eiffel");
		mimeMap.put("el", "text/x-emacs-lisp");
		mimeMap.put("emf", "image/x-emf");
		mimeMap.put("emp", "application/vnd.emusic-emusic_package");
		mimeMap.put("ent", "application/xml-external-parsed-entity");
		mimeMap.put("eps", "image/x-eps");
		mimeMap.put("eps.bz2", "image/x-bzeps");
		mimeMap.put("eps.gz", "image/x-gzeps");
		mimeMap.put("epsf", "image/x-eps");
		mimeMap.put("epsf.bz2", "image/x-bzeps");
		mimeMap.put("epsf.gz", "image/x-gzeps");
		mimeMap.put("epsi", "image/x-eps");
		mimeMap.put("epsi.bz2", "image/x-bzeps");
		mimeMap.put("epsi.gz", "image/x-gzeps");
		mimeMap.put("epub", "application/epub+zip");
		mimeMap.put("erl", "text/x-erlang");
		mimeMap.put("es", "application/ecmascript");
		mimeMap.put("etheme", "application/x-e-theme");
		mimeMap.put("etx", "text/x-setext");
		mimeMap.put("exe", "application/x-ms-dos-executable");
		mimeMap.put("exr", "image/x-exr");
		mimeMap.put("ez", "application/andrew-inset");
		mimeMap.put("f", "text/x-fortran");
		mimeMap.put("f90", "text/x-fortran");
		mimeMap.put("f95", "text/x-fortran");
		mimeMap.put("fb2", "application/x-fictionbook+xml");
		mimeMap.put("fig", "image/x-xfig");
		mimeMap.put("fits", "image/fits");
		mimeMap.put("fl", "application/x-fluid");
		mimeMap.put("flac", "audio/x-flac");
		mimeMap.put("flc", "video/x-flic");
		mimeMap.put("fli", "video/x-flic");
		mimeMap.put("flv", "video/x-flv");
		mimeMap.put("flw", "application/x-kivio");
		mimeMap.put("fo", "text/x-xslfo");
		mimeMap.put("for", "text/x-fortran");
		mimeMap.put("g3", "image/fax-g3");
		mimeMap.put("gb", "application/x-gameboy-rom");
		mimeMap.put("gba", "application/x-gba-rom");
		mimeMap.put("gcrd", "text/directory");
		mimeMap.put("ged", "application/x-gedcom");
		mimeMap.put("gedcom", "application/x-gedcom");
		mimeMap.put("gen", "application/x-genesis-rom");
		mimeMap.put("gf", "application/x-tex-gf");
		mimeMap.put("gg", "application/x-sms-rom");
		mimeMap.put("gif", "image/gif");
		mimeMap.put("glade", "application/x-glade");
		mimeMap.put("gmo", "application/x-gettext-translation");
		mimeMap.put("gnc", "application/x-gnucash");
		mimeMap.put("gnd", "application/gnunet-directory");
		mimeMap.put("gnucash", "application/x-gnucash");
		mimeMap.put("gnumeric", "application/x-gnumeric");
		mimeMap.put("gnuplot", "application/x-gnuplot");
		mimeMap.put("gp", "application/x-gnuplot");
		mimeMap.put("gpg", "application/pgp-encrypted");
		mimeMap.put("gplt", "application/x-gnuplot");
		mimeMap.put("gra", "application/x-graphite");
		mimeMap.put("gsf", "application/x-font-type1");
		mimeMap.put("gsm", "audio/x-gsm");
		mimeMap.put("gtar", "application/x-tar");
		mimeMap.put("gv", "text/vnd.graphviz");
		mimeMap.put("gvp", "text/x-google-video-pointer");
		mimeMap.put("gz", "application/x-gzip");
		mimeMap.put("h", "text/x-chdr");
		mimeMap.put("h++", "text/x-c++hdr");
		mimeMap.put("hdf", "application/x-hdf");
		mimeMap.put("hh", "text/x-c++hdr");
		mimeMap.put("hp", "text/x-c++hdr");
		mimeMap.put("hpgl", "application/vnd.hp-hpgl");
		mimeMap.put("hpp", "text/x-c++hdr");
		mimeMap.put("hs", "text/x-haskell");
		mimeMap.put("htm", "text/html");
		mimeMap.put("html", "text/html");
		mimeMap.put("hwp", "application/x-hwp");
		mimeMap.put("hwt", "application/x-hwt");
		mimeMap.put("hxx", "text/x-c++hdr");
		mimeMap.put("ica", "application/x-ica");
		mimeMap.put("icb", "image/x-tga");
		mimeMap.put("icns", "image/x-icns");
		mimeMap.put("ico", "image/vnd.microsoft.icon");
		mimeMap.put("ics", "text/calendar");
		mimeMap.put("idl", "text/x-idl");
		mimeMap.put("ief", "image/ief");
		mimeMap.put("iff", "image/x-iff");
		mimeMap.put("ilbm", "image/x-ilbm");
		mimeMap.put("ime", "text/x-imelody");
		mimeMap.put("imy", "text/x-imelody");
		mimeMap.put("ins", "text/x-tex");
		mimeMap.put("iptables", "text/x-iptables");
		mimeMap.put("iso", "application/x-cd-image");
		mimeMap.put("iso9660", "application/x-cd-image");
		mimeMap.put("it", "audio/x-it");
		mimeMap.put("j2k", "image/jp2");
		mimeMap.put("jad", "text/vnd.sun.j2me.app-descriptor");
		mimeMap.put("jar", "application/x-java-archive");
		mimeMap.put("java", "text/x-java");
		mimeMap.put("jng", "image/x-jng");
		mimeMap.put("jnlp", "application/x-java-jnlp-file");
		mimeMap.put("jp2", "image/jp2");
		mimeMap.put("jpc", "image/jp2");
		mimeMap.put("jpe", "image/jpeg");
		mimeMap.put("jpeg", "image/jpeg");
		mimeMap.put("jpf", "image/jp2");
		mimeMap.put("jpg", "image/jpeg");
		mimeMap.put("jpr", "application/x-jbuilder-project");
		mimeMap.put("jpx", "image/jp2");
		mimeMap.put("js", "application/javascript");
		mimeMap.put("json", "application/json");
		mimeMap.put("jsonp", "application/jsonp");
		mimeMap.put("k25", "image/x-kodak-k25");
		mimeMap.put("kar", "audio/midi");
		mimeMap.put("karbon", "application/x-karbon");
		mimeMap.put("kdc", "image/x-kodak-kdc");
		mimeMap.put("kdelnk", "application/x-desktop");
		mimeMap.put("kexi", "application/x-kexiproject-sqlite3");
		mimeMap.put("kexic", "application/x-kexi-connectiondata");
		mimeMap.put("kexis", "application/x-kexiproject-shortcut");
		mimeMap.put("kfo", "application/x-kformula");
		mimeMap.put("kil", "application/x-killustrator");
		mimeMap.put("kino", "application/smil");
		mimeMap.put("kml", "application/vnd.google-earth.kml+xml");
		mimeMap.put("kmz", "application/vnd.google-earth.kmz");
		mimeMap.put("kon", "application/x-kontour");
		mimeMap.put("kpm", "application/x-kpovmodeler");
		mimeMap.put("kpr", "application/x-kpresenter");
		mimeMap.put("kpt", "application/x-kpresenter");
		mimeMap.put("kra", "application/x-krita");
		mimeMap.put("ksp", "application/x-kspread");
		mimeMap.put("kud", "application/x-kugar");
		mimeMap.put("kwd", "application/x-kword");
		mimeMap.put("kwt", "application/x-kword");
		mimeMap.put("la", "application/x-shared-library-la");
		mimeMap.put("latex", "text/x-tex");
		mimeMap.put("ldif", "text/x-ldif");
		mimeMap.put("lha", "application/x-lha");
		mimeMap.put("lhs", "text/x-literate-haskell");
		mimeMap.put("lhz", "application/x-lhz");
		mimeMap.put("log", "text/x-log");
		mimeMap.put("ltx", "text/x-tex");
		mimeMap.put("lua", "text/x-lua");
		mimeMap.put("lwo", "image/x-lwo");
		mimeMap.put("lwob", "image/x-lwo");
		mimeMap.put("lws", "image/x-lws");
		mimeMap.put("ly", "text/x-lilypond");
		mimeMap.put("lyx", "application/x-lyx");
		mimeMap.put("lz", "application/x-lzip");
		mimeMap.put("lzh", "application/x-lha");
		mimeMap.put("lzma", "application/x-lzma");
		mimeMap.put("lzo", "application/x-lzop");
		mimeMap.put("m", "text/x-matlab");
		mimeMap.put("m15", "audio/x-mod");
		mimeMap.put("m2t", "video/mpeg");
		mimeMap.put("m3u", "audio/x-mpegurl");
		mimeMap.put("m3u8", "audio/x-mpegurl");
		mimeMap.put("m4", "application/x-m4");
		mimeMap.put("m4a", "audio/mp4");
		mimeMap.put("m4b", "audio/x-m4b");
		mimeMap.put("m4v", "video/mp4");
		mimeMap.put("mab", "application/x-markaby");
		mimeMap.put("man", "application/x-troff-man");
		mimeMap.put("mbox", "application/mbox");
		mimeMap.put("md", "application/x-genesis-rom");
		mimeMap.put("mdb", "application/vnd.ms-access");
		mimeMap.put("mdi", "image/vnd.ms-modi");
		mimeMap.put("me", "text/x-troff-me");
		mimeMap.put("med", "audio/x-mod");
		mimeMap.put("metalink", "application/metalink+xml");
		mimeMap.put("mgp", "application/x-magicpoint");
		mimeMap.put("mid", "audio/midi");
		mimeMap.put("midi", "audio/midi");
		mimeMap.put("mif", "application/x-mif");
		mimeMap.put("minipsf", "audio/x-minipsf");
		mimeMap.put("mka", "audio/x-matroska");
		mimeMap.put("mkv", "video/x-matroska");
		mimeMap.put("ml", "text/x-ocaml");
		mimeMap.put("mli", "text/x-ocaml");
		mimeMap.put("mm", "text/x-troff-mm");
		mimeMap.put("mmf", "application/x-smaf");
		mimeMap.put("mml", "text/mathml");
		mimeMap.put("mng", "video/x-mng");
		mimeMap.put("mo", "application/x-gettext-translation");
		mimeMap.put("mo3", "audio/x-mo3");
		mimeMap.put("moc", "text/x-moc");
		mimeMap.put("mod", "audio/x-mod");
		mimeMap.put("mof", "text/x-mof");
		mimeMap.put("moov", "video/quicktime");
		mimeMap.put("mov", "video/quicktime");
		mimeMap.put("movie", "video/x-sgi-movie");
		mimeMap.put("mp+", "audio/x-musepack");
		mimeMap.put("mp2", "video/mpeg");
		mimeMap.put("mp3", "audio/mpeg");
		mimeMap.put("mp4", "video/mp4");
		mimeMap.put("mpc", "audio/x-musepack");
		mimeMap.put("mpe", "video/mpeg");
		mimeMap.put("mpeg", "video/mpeg");
		mimeMap.put("mpg", "video/mpeg");
		mimeMap.put("mpga", "audio/mpeg");
		mimeMap.put("mpp", "audio/x-musepack");
		mimeMap.put("mrl", "text/x-mrml");
		mimeMap.put("mrml", "text/x-mrml");
		mimeMap.put("mrw", "image/x-minolta-mrw");
		mimeMap.put("ms", "text/x-troff-ms");
		mimeMap.put("msi", "application/x-msi");
		mimeMap.put("msod", "image/x-msod");
		mimeMap.put("msx", "application/x-msx-rom");
		mimeMap.put("mtm", "audio/x-mod");
		mimeMap.put("mup", "text/x-mup");
		mimeMap.put("mxf", "application/mxf");
		mimeMap.put("n64", "application/x-n64-rom");
		mimeMap.put("nb", "application/mathematica");
		mimeMap.put("nc", "application/x-netcdf");
		mimeMap.put("nds", "application/x-nintendo-ds-rom");
		mimeMap.put("nef", "image/x-nikon-nef");
		mimeMap.put("nes", "application/x-nes-rom");
		mimeMap.put("nfo", "text/x-nfo");
		mimeMap.put("not", "text/x-mup");
		mimeMap.put("nsc", "application/x-netshow-channel");
		mimeMap.put("nsv", "video/x-nsv");
		mimeMap.put("o", "application/x-object");
		mimeMap.put("obj", "application/x-tgif");
		mimeMap.put("ocl", "text/x-ocl");
		mimeMap.put("oda", "application/oda");
		mimeMap.put("odb", "application/vnd.oasis.opendocument.database");
		mimeMap.put("odc", "application/vnd.oasis.opendocument.chart");
		mimeMap.put("odf", "application/vnd.oasis.opendocument.formula");
		mimeMap.put("odg", "application/vnd.oasis.opendocument.graphics");
		mimeMap.put("odi", "application/vnd.oasis.opendocument.image");
		mimeMap.put("odm", "application/vnd.oasis.opendocument.text-master");
		mimeMap.put("odp", "application/vnd.oasis.opendocument.presentation");
		mimeMap.put("ods", "application/vnd.oasis.opendocument.spreadsheet");
		mimeMap.put("odt", "application/vnd.oasis.opendocument.text");
		mimeMap.put("oga", "audio/ogg");
		mimeMap.put("ogg", "video/x-theora+ogg");
		mimeMap.put("ogm", "video/x-ogm+ogg");
		mimeMap.put("ogv", "video/ogg");
		mimeMap.put("ogx", "application/ogg");
		mimeMap.put("old", "application/x-trash");
		mimeMap.put("oleo", "application/x-oleo");
		mimeMap.put("opml", "text/x-opml+xml");
		mimeMap.put("ora", "image/openraster");
		mimeMap.put("orf", "image/x-olympus-orf");
		mimeMap.put("otc", "application/vnd.oasis.opendocument.chart-template");
		mimeMap.put("otf", "application/x-font-otf");
		mimeMap.put("otg",
				"application/vnd.oasis.opendocument.graphics-template");
		mimeMap.put("oth", "application/vnd.oasis.opendocument.text-web");
		mimeMap.put("otp",
				"application/vnd.oasis.opendocument.presentation-template");
		mimeMap.put("ots",
				"application/vnd.oasis.opendocument.spreadsheet-template");
		mimeMap.put("ott", "application/vnd.oasis.opendocument.text-template");
		mimeMap.put("owl", "application/rdf+xml");
		mimeMap.put("oxt", "application/vnd.openofficeorg.extension");
		mimeMap.put("p", "text/x-pascal");
		mimeMap.put("p10", "application/pkcs10");
		mimeMap.put("p12", "application/x-pkcs12");
		mimeMap.put("p7b", "application/x-pkcs7-certificates");
		mimeMap.put("p7s", "application/pkcs7-signature");
		mimeMap.put("pack", "application/x-java-pack200");
		mimeMap.put("pak", "application/x-pak");
		mimeMap.put("par2", "application/x-par2");
		mimeMap.put("pas", "text/x-pascal");
		mimeMap.put("patch", "text/x-patch");
		mimeMap.put("pbm", "image/x-portable-bitmap");
		mimeMap.put("pcd", "image/x-photo-cd");
		mimeMap.put("pcf", "application/x-cisco-vpn-settings");
		mimeMap.put("pcf.gz", "application/x-font-pcf");
		mimeMap.put("pcf.z", "application/x-font-pcf");
		mimeMap.put("pcl", "application/vnd.hp-pcl");
		mimeMap.put("pcx", "image/x-pcx");
		mimeMap.put("pdb", "chemical/x-pdb");
		mimeMap.put("pdc", "application/x-aportisdoc");
		mimeMap.put("pdf", "application/pdf");
		mimeMap.put("pdf.bz2", "application/x-bzpdf");
		mimeMap.put("pdf.gz", "application/x-gzpdf");
		mimeMap.put("pef", "image/x-pentax-pef");
		mimeMap.put("pem", "application/x-x509-ca-cert");
		mimeMap.put("perl", "application/x-perl");
		mimeMap.put("pfa", "application/x-font-type1");
		mimeMap.put("pfb", "application/x-font-type1");
		mimeMap.put("pfx", "application/x-pkcs12");
		mimeMap.put("pgm", "image/x-portable-graymap");
		mimeMap.put("pgn", "application/x-chess-pgn");
		mimeMap.put("pgp", "application/pgp-encrypted");
		mimeMap.put("php", "application/x-php");
		mimeMap.put("php3", "application/x-php");
		mimeMap.put("php4", "application/x-php");
		mimeMap.put("pict", "image/x-pict");
		mimeMap.put("pict1", "image/x-pict");
		mimeMap.put("pict2", "image/x-pict");
		mimeMap.put("pickle", "application/python-pickle");
		mimeMap.put("pk", "application/x-tex-pk");
		mimeMap.put("pkipath", "application/pkix-pkipath");
		mimeMap.put("pkr", "application/pgp-keys");
		mimeMap.put("pl", "application/x-perl");
		mimeMap.put("pla", "audio/x-iriver-pla");
		mimeMap.put("pln", "application/x-planperfect");
		mimeMap.put("pls", "audio/x-scpls");
		mimeMap.put("pm", "application/x-perl");
		mimeMap.put("png", "image/png");
		mimeMap.put("pnm", "image/x-portable-anymap");
		mimeMap.put("pntg", "image/x-macpaint");
		mimeMap.put("po", "text/x-gettext-translation");
		mimeMap.put("por", "application/x-spss-por");
		mimeMap.put("pot", "text/x-gettext-translation-template");
		mimeMap.put("ppm", "image/x-portable-pixmap");
		mimeMap.put("pps", "application/vnd.ms-powerpoint");
		mimeMap.put("ppt", "application/vnd.ms-powerpoint");
		mimeMap.put("pptm",
				"application/vnd.openxmlformats-officedocument.presentationml.presentation");
		mimeMap.put("pptx",
				"application/vnd.openxmlformats-officedocument.presentationml.presentation");
		mimeMap.put("ppz", "application/vnd.ms-powerpoint");
		mimeMap.put("prc", "application/x-palm-database");
		mimeMap.put("ps", "application/postscript");
		mimeMap.put("ps.bz2", "application/x-bzpostscript");
		mimeMap.put("ps.gz", "application/x-gzpostscript");
		mimeMap.put("psd", "image/vnd.adobe.photoshop");
		mimeMap.put("psf", "audio/x-psf");
		mimeMap.put("psf.gz", "application/x-gz-font-linux-psf");
		mimeMap.put("psflib", "audio/x-psflib");
		mimeMap.put("psid", "audio/prs.sid");
		mimeMap.put("psw", "application/x-pocket-word");
		mimeMap.put("pw", "application/x-pw");
		mimeMap.put("py", "text/x-python");
		mimeMap.put("pyc", "application/x-python-bytecode");
		mimeMap.put("pyo", "application/x-python-bytecode");
		mimeMap.put("qif", "image/x-quicktime");
		mimeMap.put("qt", "video/quicktime");
		mimeMap.put("qtif", "image/x-quicktime");
		mimeMap.put("qtl", "application/x-quicktime-media-link");
		mimeMap.put("qtvr", "video/quicktime");
		mimeMap.put("ra", "audio/vnd.rn-realaudio");
		mimeMap.put("raf", "image/x-fuji-raf");
		mimeMap.put("ram", "application/ram");
		mimeMap.put("rar", "application/x-rar");
		mimeMap.put("ras", "image/x-cmu-raster");
		mimeMap.put("raw", "image/x-panasonic-raw");
		mimeMap.put("rax", "audio/vnd.rn-realaudio");
		mimeMap.put("rb", "application/x-ruby");
		mimeMap.put("rdf", "application/rdf+xml");
		mimeMap.put("rdfs", "application/rdf+xml");
		mimeMap.put("reg", "text/x-ms-regedit");
		mimeMap.put("rej", "application/x-reject");
		mimeMap.put("rgb", "image/x-rgb");
		mimeMap.put("rle", "image/rle");
		mimeMap.put("rm", "application/vnd.rn-realmedia");
		mimeMap.put("rmj", "application/vnd.rn-realmedia");
		mimeMap.put("rmm", "application/vnd.rn-realmedia");
		mimeMap.put("rms", "application/vnd.rn-realmedia");
		mimeMap.put("rmvb", "application/vnd.rn-realmedia");
		mimeMap.put("rmx", "application/vnd.rn-realmedia");
		mimeMap.put("roff", "text/troff");
		mimeMap.put("rp", "image/vnd.rn-realpix");
		mimeMap.put("rpm", "application/x-rpm");
		mimeMap.put("rss", "application/rss+xml");
		mimeMap.put("rt", "text/vnd.rn-realtext");
		mimeMap.put("rtf", "application/rtf");
		mimeMap.put("rtx", "text/richtext");
		mimeMap.put("rv", "video/vnd.rn-realvideo");
		mimeMap.put("rvx", "video/vnd.rn-realvideo");
		mimeMap.put("s3m", "audio/x-s3m");
		mimeMap.put("sam", "application/x-amipro");
		mimeMap.put("sami", "application/x-sami");
		mimeMap.put("sav", "application/x-spss-sav");
		mimeMap.put("scm", "text/x-scheme");
		mimeMap.put("sda", "application/vnd.stardivision.draw");
		mimeMap.put("sdc", "application/vnd.stardivision.calc");
		mimeMap.put("sdd", "application/vnd.stardivision.impress");
		mimeMap.put("sdp", "application/sdp");
		mimeMap.put("sds", "application/vnd.stardivision.chart");
		mimeMap.put("sdw", "application/vnd.stardivision.writer");
		mimeMap.put("sgf", "application/x-go-sgf");
		mimeMap.put("sgi", "image/x-sgi");
		mimeMap.put("sgl", "application/vnd.stardivision.writer");
		mimeMap.put("sgm", "text/sgml");
		mimeMap.put("sgml", "text/sgml");
		mimeMap.put("sh", "application/x-shellscript");
		mimeMap.put("shar", "application/x-shar");
		mimeMap.put("shn", "application/x-shorten");
		mimeMap.put("siag", "application/x-siag");
		mimeMap.put("sid", "audio/prs.sid");
		mimeMap.put("sik", "application/x-trash");
		mimeMap.put("sis", "application/vnd.symbian.install");
		mimeMap.put("sisx", "x-epoc/x-sisx-app");
		mimeMap.put("sit", "application/x-stuffit");
		mimeMap.put("siv", "application/sieve");
		mimeMap.put("sk", "image/x-skencil");
		mimeMap.put("sk1", "image/x-skencil");
		mimeMap.put("skr", "application/pgp-keys");
		mimeMap.put("slk", "text/spreadsheet");
		mimeMap.put("smaf", "application/x-smaf");
		mimeMap.put("smc", "application/x-snes-rom");
		mimeMap.put("smd", "application/vnd.stardivision.mail");
		mimeMap.put("smf", "application/vnd.stardivision.math");
		mimeMap.put("smi", "application/x-sami");
		mimeMap.put("smil", "application/smil");
		mimeMap.put("sml", "application/smil");
		mimeMap.put("sms", "application/x-sms-rom");
		mimeMap.put("snd", "audio/basic");
		mimeMap.put("so", "application/x-sharedlib");
		mimeMap.put("spc", "application/x-pkcs7-certificates");
		mimeMap.put("spd", "application/x-font-speedo");
		mimeMap.put("spec", "text/x-rpm-spec");
		mimeMap.put("spl", "application/x-shockwave-flash");
		mimeMap.put("spx", "audio/x-speex");
		mimeMap.put("sql", "text/x-sql");
		mimeMap.put("sr2", "image/x-sony-sr2");
		mimeMap.put("src", "application/x-wais-source");
		mimeMap.put("srf", "image/x-sony-srf");
		mimeMap.put("srt", "application/x-subrip");
		mimeMap.put("ssa", "text/x-ssa");
		mimeMap.put("stc", "application/vnd.sun.xml.calc.template");
		mimeMap.put("std", "application/vnd.sun.xml.draw.template");
		mimeMap.put("sti", "application/vnd.sun.xml.impress.template");
		mimeMap.put("stm", "audio/x-stm");
		mimeMap.put("stw", "application/vnd.sun.xml.writer.template");
		mimeMap.put("sty", "text/x-tex");
		mimeMap.put("sub", "text/x-subviewer");
		mimeMap.put("sun", "image/x-sun-raster");
		mimeMap.put("sv4cpio", "application/x-sv4cpio");
		mimeMap.put("sv4crc", "application/x-sv4crc");
		mimeMap.put("svg", "image/svg+xml");
		mimeMap.put("svgz", "image/svg+xml-compressed");
		mimeMap.put("swf", "application/x-shockwave-flash");
		mimeMap.put("sxc", "application/vnd.sun.xml.calc");
		mimeMap.put("sxd", "application/vnd.sun.xml.draw");
		mimeMap.put("sxg", "application/vnd.sun.xml.writer.global");
		mimeMap.put("sxi", "application/vnd.sun.xml.impress");
		mimeMap.put("sxm", "application/vnd.sun.xml.math");
		mimeMap.put("sxw", "application/vnd.sun.xml.writer");
		mimeMap.put("sylk", "text/spreadsheet");
		mimeMap.put("t", "text/troff");
		mimeMap.put("t2t", "text/x-txt2tags");
		mimeMap.put("tar", "application/x-tar");
		mimeMap.put("tar.bz", "application/x-bzip-compressed-tar");
		mimeMap.put("tar.bz2", "application/x-bzip-compressed-tar");
		mimeMap.put("tar.gz", "application/x-compressed-tar");
		mimeMap.put("tar.lzma", "application/x-lzma-compressed-tar");
		mimeMap.put("tar.lzo", "application/x-tzo");
		mimeMap.put("tar.xz", "application/x-xz-compressed-tar");
		mimeMap.put("tar.z", "application/x-tarz");
		mimeMap.put("tbz", "application/x-bzip-compressed-tar");
		mimeMap.put("tbz2", "application/x-bzip-compressed-tar");
		mimeMap.put("tcl", "text/x-tcl");
		mimeMap.put("tex", "text/x-tex");
		mimeMap.put("texi", "text/x-texinfo");
		mimeMap.put("texinfo", "text/x-texinfo");
		mimeMap.put("tga", "image/x-tga");
		mimeMap.put("tgz", "application/x-compressed-tar");
		mimeMap.put("theme", "application/x-theme");
		mimeMap.put("themepack", "application/x-windows-themepack");
		mimeMap.put("tif", "image/tiff");
		mimeMap.put("tiff", "image/tiff");
		mimeMap.put("tk", "text/x-tcl");
		mimeMap.put("tlz", "application/x-lzma-compressed-tar");
		mimeMap.put("tnef", "application/vnd.ms-tnef");
		mimeMap.put("tnf", "application/vnd.ms-tnef");
		mimeMap.put("toc", "application/x-cdrdao-toc");
		mimeMap.put("torrent", "application/x-bittorrent");
		mimeMap.put("tpic", "image/x-tga");
		mimeMap.put("tr", "text/troff");
		mimeMap.put("ts", "application/x-linguist");
		mimeMap.put("tsv", "text/tab-separated-values");
		mimeMap.put("tta", "audio/x-tta");
		mimeMap.put("ttc", "application/x-font-ttf");
		mimeMap.put("ttf", "application/x-font-ttf");
		mimeMap.put("ttx", "application/x-font-ttx");
		mimeMap.put("txt", "text/plain");
		mimeMap.put("txz", "application/x-xz-compressed-tar");
		mimeMap.put("tzo", "application/x-tzo");
		mimeMap.put("ufraw", "application/x-ufraw");
		mimeMap.put("ui", "application/x-designer");
		mimeMap.put("uil", "text/x-uil");
		mimeMap.put("ult", "audio/x-mod");
		mimeMap.put("uni", "audio/x-mod");
		mimeMap.put("uri", "text/x-uri");
		mimeMap.put("url", "text/x-uri");
		mimeMap.put("ustar", "application/x-ustar");
		mimeMap.put("vala", "text/x-vala");
		mimeMap.put("vapi", "text/x-vala");
		mimeMap.put("vcf", "text/directory");
		mimeMap.put("vcs", "text/calendar");
		mimeMap.put("vct", "text/directory");
		mimeMap.put("vda", "image/x-tga");
		mimeMap.put("vhd", "text/x-vhdl");
		mimeMap.put("vhdl", "text/x-vhdl");
		mimeMap.put("viv", "video/vivo");
		mimeMap.put("vivo", "video/vivo");
		mimeMap.put("vlc", "audio/x-mpegurl");
		mimeMap.put("vob", "video/mpeg");
		mimeMap.put("voc", "audio/x-voc");
		mimeMap.put("vor", "application/vnd.stardivision.writer");
		mimeMap.put("vst", "image/x-tga");
		mimeMap.put("wav", "audio/x-wav");
		mimeMap.put("wax", "audio/x-ms-asx");
		mimeMap.put("wb1", "application/x-quattropro");
		mimeMap.put("wb2", "application/x-quattropro");
		mimeMap.put("wb3", "application/x-quattropro");
		mimeMap.put("wbmp", "image/vnd.wap.wbmp");
		mimeMap.put("wcm", "application/vnd.ms-works");
		mimeMap.put("wdb", "application/vnd.ms-works");
		mimeMap.put("webm", "video/webm");
		mimeMap.put("wk1", "application/vnd.lotus-1-2-3");
		mimeMap.put("wk3", "application/vnd.lotus-1-2-3");
		mimeMap.put("wk4", "application/vnd.lotus-1-2-3");
		mimeMap.put("wks", "application/vnd.ms-works");
		mimeMap.put("wma", "audio/x-ms-wma");
		mimeMap.put("wmf", "image/x-wmf");
		mimeMap.put("wml", "text/vnd.wap.wml");
		mimeMap.put("wmls", "text/vnd.wap.wmlscript");
		mimeMap.put("wmv", "video/x-ms-wmv");
		mimeMap.put("wmx", "audio/x-ms-asx");
		mimeMap.put("wp", "application/vnd.wordperfect");
		mimeMap.put("wp4", "application/vnd.wordperfect");
		mimeMap.put("wp5", "application/vnd.wordperfect");
		mimeMap.put("wp6", "application/vnd.wordperfect");
		mimeMap.put("wpd", "application/vnd.wordperfect");
		mimeMap.put("wpg", "application/x-wpg");
		mimeMap.put("wpl", "application/vnd.ms-wpl");
		mimeMap.put("wpp", "application/vnd.wordperfect");
		mimeMap.put("wps", "application/vnd.ms-works");
		mimeMap.put("wri", "application/x-mswrite");
		mimeMap.put("wrl", "model/vrml");
		mimeMap.put("wv", "audio/x-wavpack");
		mimeMap.put("wvc", "audio/x-wavpack-correction");
		mimeMap.put("wvp", "audio/x-wavpack");
		mimeMap.put("wvx", "audio/x-ms-asx");
		mimeMap.put("x3f", "image/x-sigma-x3f");
		mimeMap.put("xac", "application/x-gnucash");
		mimeMap.put("xbel", "application/x-xbel");
		mimeMap.put("xbl", "application/xml");
		mimeMap.put("xbm", "image/x-xbitmap");
		mimeMap.put("xcf", "image/x-xcf");
		mimeMap.put("xcf.bz2", "image/x-compressed-xcf");
		mimeMap.put("xcf.gz", "image/x-compressed-xcf");
		mimeMap.put("xhtml", "application/xhtml+xml");
		mimeMap.put("xi", "audio/x-xi");
		mimeMap.put("xla", "application/vnd.ms-excel");
		mimeMap.put("xlc", "application/vnd.ms-excel");
		mimeMap.put("xld", "application/vnd.ms-excel");
		mimeMap.put("xlf", "application/x-xliff");
		mimeMap.put("xliff", "application/x-xliff");
		mimeMap.put("xll", "application/vnd.ms-excel");
		mimeMap.put("xlm", "application/vnd.ms-excel");
		mimeMap.put("xls", "application/vnd.ms-excel");
		mimeMap.put("xlsm",
				"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		mimeMap.put("xlsx",
				"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		mimeMap.put("xlt", "application/vnd.ms-excel");
		mimeMap.put("xlw", "application/vnd.ms-excel");
		mimeMap.put("xm", "audio/x-xm");
		mimeMap.put("xmf", "audio/x-xmf");
		mimeMap.put("xmi", "text/x-xmi");
		mimeMap.put("xml", "application/xml");
		mimeMap.put("xpm", "image/x-xpixmap");
		mimeMap.put("xps", "application/vnd.ms-xpsdocument");
		mimeMap.put("xsl", "application/xml");
		mimeMap.put("xslfo", "text/x-xslfo");
		mimeMap.put("xslt", "application/xml");
		mimeMap.put("xspf", "application/xspf+xml");
		mimeMap.put("xul", "application/vnd.mozilla.xul+xml");
		mimeMap.put("xwd", "image/x-xwindowdump");
		mimeMap.put("xyz", "chemical/x-pdb");
		mimeMap.put("xz", "application/x-xz");
		mimeMap.put("w2p", "application/w2p");
		mimeMap.put("z", "application/x-compress");
		mimeMap.put("zabw", "application/x-abiword");
		mimeMap.put("zip", "application/zip");
		mimeMap.put("zoo", "application/x-zoo");

	}
}

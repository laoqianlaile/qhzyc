package com.ces.component.farm.utils;

import com.ces.config.utils.StringUtil;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * txt文件操作工具类
 * Created by bdz on 2015/12/8.
 */
public class TxtUtils {

    /**
     * @param filePathAndName
     * @return
     * @throws IOException
     */
    public static String readFileContent(String filePathAndName)
            throws IOException {
        return readFileContent(filePathAndName,null,null,1024);
    }
    public static String readFileContent(String filePathAndName, String encoding)
            throws IOException {
        return readFileContent(filePathAndName,encoding,null,1024);
    }
    public static String readFileContent(String filePathAndName,int bufLen)
            throws IOException {
        return readFileContent(filePathAndName,null,null,bufLen);
    }
    public static String readFileContent(String filePathAndName, String encoding, String sep)
            throws IOException {
        return readFileContent(filePathAndName,encoding,sep,1024);
    }

    /**
     * 读取文本内容 以行的形式读取
     * @param filePathAndName 带有完整绝对路径的文件名
     * @param encoding         文本文件打开的编码方式 例如 GBK,UTF-8
     * @param sep       分隔符 例如：#，默认为\n;
     * @param bufLen    设置缓冲区大小
     * @return String 返回文本文件的内容
     * @throws IOException
     */
    public static String readFileContent(String filePathAndName, String encoding, String sep,int bufLen )
            throws IOException {
        if (filePathAndName == null || filePathAndName.equals("")) {
            return "";
        }
        if(sep==null||sep.equals(""))
        {
            sep="\n";
        }
        if(!new File(filePathAndName).exists())
        {
            return "";
        }
        StringBuffer str = new StringBuffer("");
        FileInputStream fs = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        try {
            fs = new FileInputStream(filePathAndName);
            if (encoding == null||encoding.trim().equals("")) {
                isr = new InputStreamReader(fs);
            } else {
                isr = new InputStreamReader(fs, encoding.trim());
            }
            br = new BufferedReader(isr,bufLen);


            String data = "";
            while ((data = br.readLine()) != null) {
                str.append(data).append(sep);
            }


        } catch (IOException e) {
            throw e;
        } finally {
            try
            {
                if (br != null)
                    br.close();
                if (isr != null)
                    isr.close();
                if (fs != null)
                    fs.close();
            }catch(IOException e)
            {
                throw e;
            }


        }
        return str.toString();
    }

    public static String readFileContent(File file, String encoding, String sep,int bufLen) throws IOException {
        if(sep==null||sep.equals(""))
        {
            sep="\n";
        }
        if(!file.exists())
        {
            return "";
        }
        StringBuffer str = new StringBuffer("");
        FileInputStream fs = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        try {
            fs = new FileInputStream(file);
            if (encoding == null||encoding.trim().equals("")) {
                isr = new InputStreamReader(fs);
            } else {
                isr = new InputStreamReader(fs, encoding.trim());
            }
            br = new BufferedReader(isr,bufLen);


            String data = "";
            while ((data = br.readLine()) != null) {
                str.append(data).append(sep);
            }


        } catch (IOException e) {
            throw e;
        } finally {
            try
            {
                if (br != null)
                    br.close();
                if (isr != null)
                    isr.close();
                if (fs != null)
                    fs.close();
            }catch(IOException e)
            {
                throw e;
            }


        }
        return str.toString();
    }

    /**
     * 创建文件
     * @param fileName
     * @return
     */
    public static boolean createFile(File fileName)throws Exception{
        boolean flag=false;
        try{
            if(!fileName.exists()){
                fileName.createNewFile();
                flag=true;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 写入txt
     * @param content   内容
     * @param fileName  文件名
     * @param encoding  编码
     * @return
     * @throws Exception
     */
    public static boolean writeTxtFile(String content,File fileName,String encoding)throws Exception{
        RandomAccessFile mm=null;
        boolean flag=false;
        FileOutputStream o=null;
        try {
            o = new FileOutputStream(fileName);
            if(StringUtil.isNotEmpty(content)) {
                if (StringUtil.isNotEmpty(encoding)) {
                    o.write(content.getBytes(encoding));
                } else {
                    o.write(content.getBytes());
                }
            }
            o.close();
//   mm=new RandomAccessFile(fileName,"rw");
//   mm.writeBytes(content);
            flag=true;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }finally{
            if(mm!=null){
                mm.close();
            }
        }
        return flag;
    }
}

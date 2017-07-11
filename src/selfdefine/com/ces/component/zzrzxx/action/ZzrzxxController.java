package com.ces.component.zzrzxx.action;

import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.zzrzxx.service.ZzrzxxService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.xarch.core.entity.StringIDEntity;
import org.apache.struts2.ServletActionContext;

import java.io.*;
import java.net.URLDecoder;
import java.util.*;

public class ZzrzxxController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, ZzrzxxService, TraceShowModuleDao> {

    private static final long serialVersionUID = 1L;
    private File imageUpload;
    private String imageUploadFileName;
    private String menuId;

    public String getMenuId() {
        return menuId;
    }
    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }
    public String getImageUploadFileName() {
        return imageUploadFileName;
    }
    public void setImageUploadFileName(String imageUploadFileName) {
        this.imageUploadFileName = imageUploadFileName;
    }
    public File getImageUpload() {
        return imageUpload;
    }
    public void setImageUpload(File imageUpload) {
        this.imageUpload = imageUpload;
    }
    
    /*
     * (非 Javadoc)   
     * <p>标题: initModel</p>   
     * <p>描述: </p>      
     * @see com.ces.xarch.core.web.struts2.BaseController#initModel()
     */
    @Override
    protected void initModel() {
        setModel(new StringIDEntity());
    }
    public Object saveAdd () throws IOException {

        Map map = this.getRequest().getParameterMap();
        Map newMap = new HashMap();
        Set<Map.Entry> allSet = map.entrySet();
        Iterator<Map.Entry> iter = allSet.iterator();
        while(iter.hasNext()){
            Map.Entry entry = iter.next();
            String[] valueArray = (String[])entry.getValue();
            String value= "";
            for(int i = 0;i<valueArray.length;i++){
                value += valueArray[i];
            }
            newMap.put(entry.getKey(), value);
            value = "";
        }

        String result = getService().saveAdd(newMap,imageUpload,imageUploadFileName);
        setReturnData(result);
        return SUCCESS;
//        String CPMC=getParameter("CPMC");
//        String RZMC=getParameter("RZMC");
//        String RZJG=getParameter("RZJG");
//        String RZRQ=getParameter("RZRQ");
//        String ZSBH=getParameter("ZSBH");
//        String YXQ=getParameter("YXQ");
//        //String TP=getParameter("TP");
//        String ID=getParameter("ID");
//        String TP1=getParameter("TP");
//        int num=TP1.lastIndexOf(".");
//        String TP=System.currentTimeMillis()+"."+TP1.substring(num+1);
//        System.out.println(TP);
//        String realpath = ServletActionContext.getServletContext().getRealPath("/spzstpfj");
//        System.out.println(realpath);
//        //File file=new File(TP1);
//        try {
//            int bytesum = 0;
//            int byteread = 0;
//            File oldfile = new File(TP1);
//            if (oldfile.exists()) { //文件存在时
//                InputStream inStream = new FileInputStream(TP1); //读入原文件
//                FileOutputStream fs = new FileOutputStream(realpath);
//                byte[] buffer = new byte[1444];
//                int length;
//                while ( (byteread = inStream.read(buffer)) != -1) {
//                    bytesum += byteread; //字节数 文件大小
//                    System.out.println(bytesum);
//                    fs.write(buffer, 0, byteread);
//                }
//                inStream.close();
//            }else{System.out.println("不存在");}
//        }
//        catch (Exception e) {
//            System.out.println("复制单个文件操作出错");
//            e.printStackTrace();
//
//        }
//
//        Map<String,String> map = new HashMap<String, String>();
//        map.put("CPMC",CPMC);
//
//        map.put("RZMC",RZMC);
//        map.put("RZJG",RZJG);
//        map.put("RZRQ",RZRQ);
//        map.put("ZSBH",ZSBH);
//        map.put("YXQ",YXQ);
//       // map.put("TP",TP);
//        map.put("ID",ID);
//        map.put("TP",TP);
//        if("".equals(ID)){
//            map.put("ID",null);
//        }else {
//            map.put("ID",ID);
//        }
//        setReturnData(getService().save("t_zz_rzxx",map,null));

    }

    public void deleteById(){
        String ids = getParameter("ids");
        setReturnData(getService().deleteById(ids));
    }

    public void getRzxx(){
        setReturnData(getService().getRzxx());
    }

    public void getRzxxById(){
        String id = getParameter("id");
        setReturnData(getService().getRzxxById(id));
    }

    //删除图片
    public Object deleteImage(){
        String tplj = getParameter("tplj");
        try {
            tplj = URLDecoder.decode(tplj, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        setReturnData(getService().deleteImage(tplj));
        return SUCCESS;
    }

}
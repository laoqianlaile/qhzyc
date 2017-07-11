package com.ces.component.zzkhxxgllb.action;

import com.ces.component.trace.utils.DataDictionaryUtil;
import com.ces.component.trace.utils.SerialNumberUtil;
import com.ces.component.trace.dao.TraceShowModuleDao;
import com.ces.component.zzkhxxgllb.service.ZzkhxxgllbService;
import com.ces.component.trace.action.base.TraceShowModuleDefineServiceDaoController;
import com.ces.config.dhtmlx.entity.code.Code;
import com.ces.xarch.core.entity.StringIDEntity;

import java.io.File;
import java.util.*;

public class ZzkhxxgllbController extends TraceShowModuleDefineServiceDaoController<StringIDEntity, ZzkhxxgllbService, TraceShowModuleDao> {

    private static final long serialVersionUID = 1L;
    private File tpFile;
    private String tpFileFileName;
    private File logoTpFile;
    private String logoTpFileFileName;
    private File mdtpFile;
    private String mdtpFileFileName;
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

    public void getKhlx(){
        setReturnData(getComboboxTypeData(DataDictionaryUtil.getInstance().getDictionaryData("KHLX")));
    }
    public void getKhdj(){
        setReturnData(getComboboxTypeData(DataDictionaryUtil.getInstance().getDictionaryData("KHDJ")));
    }
    public void getKhbh(){
        setReturnData(SerialNumberUtil.getInstance().getSerialNumber("ZZ","ZZKHBH",true));
//        setReturnData(getService().getKhbh());
    }

    public List<Map<String,String>> getComboboxTypeData(List<Code> list){
        List<Map<String,String>> dataMap = new ArrayList<Map<String, String>>();
        for(Code code:list){
            Map<String,String>  temMap = new HashMap<String, String>();
            temMap.put("value",code.getValue());
            temMap.put("text",code.getName());
            dataMap.add(temMap);
        }
        return dataMap;
    }
    /**
     * 保存客户信息及图片信息
     * @return
     */
    public Object saveKhxx(){
        Map map = this.getRequest().getParameterMap();
        Map newMap = new HashMap();
        Set<Map.Entry> allSet = map.entrySet();
        Iterator<Map.Entry> iter = allSet.iterator();
        String dentityJson = "";
        while(iter.hasNext()){
            Map.Entry entry = iter.next();
            String[] valueArray = (String[])entry.getValue();
            String value= "";
            for(int i = 0;i<valueArray.length;i++){
                value += valueArray[i];
            }
            if("rowData".equals(entry.getKey())){
                dentityJson = value;
            }else {
                newMap.put(entry.getKey(), value);
            }
            value = "";
        }

        Map<String,String> objMap = (Map<String, String>) getService().saveKhxx(newMap,dentityJson, tpFile, tpFileFileName, logoTpFile, logoTpFileFileName);
        setReturnData(objMap);
        return SUCCESS;
    }

    /**
     * 保存门店信息
     * @return
     */
    public Object saveMdxx(){
        String entityJson = getParameter(E_ENTITY_JSON);
        Map<String,String> objMap = getService().saveMdxx(entityJson,mdtpFile,mdtpFileFileName);
        setReturnData(objMap);
        return SUCCESS;
    }

    /**
     * 保存门店图片信息
     * @return
     */
    public Object saveMdtpxx(){
        Map map = this.getRequest().getParameterMap();
        Map newMap = new HashMap();
        Set<Map.Entry> allSet = map.entrySet();
        Iterator<Map.Entry> iter = allSet.iterator();
        //处理由html5传过来的数据
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
        //进行上传操作
        setReturnData(getService().saveMdtpxx(newMap, mdtpFile, mdtpFileFileName));
        return SUCCESS;
    }

    /**
     * 删除客户门店信息
     * @return
     */
    public Object deleteKhmdxx(){
        String id = getParameter("id");
        getService().deleteKhmdxx(id);
        return SUCCESS;

    }

    /**
     * 获得修改信息的客户基础信息
     */
    public void searchKhxx(){
        String id = getParameter("id");
        setReturnData(getService().getObjKhxx(id));
    }

    /**
     * 获得指定客户的门店信息
     */
    public void searchKhmdxx(){
        String pid = getParameter("pid");
        setReturnData(getService().searchKhmdxx(pid));
    }

    /**
     * 判断用户名是否重复
     */
    public void isExist(){
        String yhm = getParameter("yhm");
        setReturnData(getService().isExistYhm(yhm));

    }
    public File getTpFile() {
        return tpFile;
    }

    public void setTpFile(File tpFile) {
        this.tpFile = tpFile;
    }

    public File getLogoTpFile() {
        return logoTpFile;
    }

    public void setLogoTpFile(File logoTpFile) {
        this.logoTpFile = logoTpFile;
    }

    public String getTpFileFileName() {
        return tpFileFileName;
    }

    public void setTpFileFileName(String tpFileFileName) {
        this.tpFileFileName = tpFileFileName;
    }

    public String getLogoTpFileFileName() {
        return logoTpFileFileName;
    }

    public void setLogoTpFileFileName(String logoTpFileFileName) {
        this.logoTpFileFileName = logoTpFileFileName;
    }

    public File getMdtpFile() {
        return mdtpFile;
    }

    public void setMdtpFile(File mdtpFile) {
        this.mdtpFile = mdtpFile;
    }

    public String getMdtpFileFileName() {
        return mdtpFileFileName;
    }

    public void setMdtpFileFileName(String mdtpFileFileName) {
        this.mdtpFileFileName = mdtpFileFileName;
    }

    /**
     * 查重
     */
    public void validYhm(){
        String yhm = getParameter("yhm");
        setReturnData(getService().validYhm(yhm));
    }

    public Object getMdssdqByKhssdq(){
        String ssdq = getParameter("ssdq");
        setReturnData(getService().getMdssdqByKhssdq(ssdq));
        return SUCCESS;
    }

    /**
     * 获得对应的客户流水号用户名
     */
    public void getKhyhlsh(){
        setReturnData(SerialNumberUtil.getInstance().getSerialNumber("ZZ","hdty","ZZKHYHLSH",true));
    }

    /**
     * 获得对应的门店用户流水号用户名
     */
    public void getMdyhlsh(){
        setReturnData(SerialNumberUtil.getInstance().getSerialNumber("ZZ","hd","ZZKHMDYHLSH",true));
    }
}
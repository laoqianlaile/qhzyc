package com.ces.config.dhtmlx.action.systemupdate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.multipart.MultiPartRequestWrapper;
import org.apache.struts2.rest.DefaultHttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ces.config.dhtmlx.action.base.ConfigDefineServiceDaoController;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.dhtmlx.dao.systemupdate.SystemUpdateDao;
import com.ces.config.dhtmlx.entity.code.BusinessCode;
import com.ces.config.dhtmlx.entity.code.Code;
import com.ces.config.dhtmlx.entity.code.CodeType;
import com.ces.config.dhtmlx.entity.component.CommonComponentRelation;
import com.ces.config.dhtmlx.entity.component.ComponentArea;
import com.ces.config.dhtmlx.entity.component.ComponentButton;
import com.ces.config.dhtmlx.entity.component.ComponentCallback;
import com.ces.config.dhtmlx.entity.component.ComponentCallbackParam;
import com.ces.config.dhtmlx.entity.component.ComponentClass;
import com.ces.config.dhtmlx.entity.component.ComponentColumn;
import com.ces.config.dhtmlx.entity.component.ComponentFunction;
import com.ces.config.dhtmlx.entity.component.ComponentFunctionData;
import com.ces.config.dhtmlx.entity.component.ComponentInputParam;
import com.ces.config.dhtmlx.entity.component.ComponentJar;
import com.ces.config.dhtmlx.entity.component.ComponentOutputParam;
import com.ces.config.dhtmlx.entity.component.ComponentReserveZone;
import com.ces.config.dhtmlx.entity.component.ComponentSelfParam;
import com.ces.config.dhtmlx.entity.component.ComponentSystemParameter;
import com.ces.config.dhtmlx.entity.component.ComponentTable;
import com.ces.config.dhtmlx.entity.component.ComponentTableColumnRelation;
import com.ces.config.dhtmlx.entity.component.ComponentVersion;
import com.ces.config.dhtmlx.entity.construct.Construct;
import com.ces.config.dhtmlx.entity.label.ColumnLabel;
import com.ces.config.dhtmlx.entity.label.ColumnLabelCategory;
import com.ces.config.dhtmlx.entity.label.TypeLabel;
import com.ces.config.dhtmlx.entity.parameter.SystemParameter;
import com.ces.config.dhtmlx.entity.parameter.SystemParameterCategory;
import com.ces.config.dhtmlx.entity.systemupdate.SystemUpdate;
import com.ces.config.dhtmlx.service.code.BusinessCodeService;
import com.ces.config.dhtmlx.service.code.CodeService;
import com.ces.config.dhtmlx.service.code.CodeTypeService;
import com.ces.config.dhtmlx.service.component.CommonComponentRelationService;
import com.ces.config.dhtmlx.service.component.ComponentAreaService;
import com.ces.config.dhtmlx.service.component.ComponentButtonService;
import com.ces.config.dhtmlx.service.component.ComponentCallbackParamService;
import com.ces.config.dhtmlx.service.component.ComponentCallbackService;
import com.ces.config.dhtmlx.service.component.ComponentClassService;
import com.ces.config.dhtmlx.service.component.ComponentColumnService;
import com.ces.config.dhtmlx.service.component.ComponentFunctionDataService;
import com.ces.config.dhtmlx.service.component.ComponentFunctionService;
import com.ces.config.dhtmlx.service.component.ComponentInputParamService;
import com.ces.config.dhtmlx.service.component.ComponentJarService;
import com.ces.config.dhtmlx.service.component.ComponentOutputParamService;
import com.ces.config.dhtmlx.service.component.ComponentReserveZoneService;
import com.ces.config.dhtmlx.service.component.ComponentSelfParamService;
import com.ces.config.dhtmlx.service.component.ComponentSystemParameterRelationService;
import com.ces.config.dhtmlx.service.component.ComponentSystemParameterService;
import com.ces.config.dhtmlx.service.component.ComponentTableColumnRelationService;
import com.ces.config.dhtmlx.service.component.ComponentTableService;
import com.ces.config.dhtmlx.service.component.ComponentVersionService;
import com.ces.config.dhtmlx.service.construct.ConstructService;
import com.ces.config.dhtmlx.service.label.ColumnLabelCategoryService;
import com.ces.config.dhtmlx.service.label.ColumnLabelService;
import com.ces.config.dhtmlx.service.label.TypeLabelService;
import com.ces.config.dhtmlx.service.parameter.SystemParameterCategoryService;
import com.ces.config.dhtmlx.service.parameter.SystemParameterService;
import com.ces.config.dhtmlx.service.systemupdate.SystemUpdateService;
import com.ces.config.utils.ComponentFileUtil;
import com.ces.config.utils.ConstantVar;
import com.ces.config.utils.FileUtil;
import com.ces.config.utils.StringUtil;
import com.ces.config.utils.ZipUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 系统更新Controller
 * 
 * @author wanglei
 * @date 2015-02-12
 */
public class SystemUpdateController extends ConfigDefineServiceDaoController<SystemUpdate, SystemUpdateService, SystemUpdateDao> {

    private static final long serialVersionUID = -7659084919140230484L;

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.web.struts2.BaseController#initModel()
     */
    @Override
    protected void initModel() {
        setModel(new SystemUpdate());
    }

    /*
     * (非 Javadoc) <p>标题: setService</p> <p>描述: 注入自定义服务层SERVICE</p> @param service
     * @see com.ces.config.dhtmlx.action.base.StringIDDhtmlxConfigController#setService(com.ces.xarch.core.service.
     * StringIDService)
     */
    @Autowired
    @Qualifier("systemUpdateService")
    @Override
    protected void setService(SystemUpdateService service) {
        super.setService(service);
    }

    /**
     * 导入更新包
     * 
     * @return Object
     */
    @SuppressWarnings("unchecked")
    public Object uploadHandler() {
        HttpServletRequest request = ServletActionContext.getRequest();
        // 1、上传文件
        // struts2 请求包装
        MultiPartRequestWrapper wrapper = (MultiPartRequestWrapper) request;
        Enumeration<String> names = wrapper.getFileParameterNames();
        String name = "";
        while (names.hasMoreElements()) {
            name = names.nextElement();
        }
        // 获取上传文件名
        String fileName = wrapper.getFileNames(name)[0];
        // 获取上传文件
        File file = wrapper.getFiles(name)[0];
        FileOutputStream fos = null;
        FileInputStream fis = null;
        try {
            // 建立文件输出流
            fos = new FileOutputStream(ComponentFileUtil.getUpdatePackagePath() + fileName);
            // 建立文件上传流
            fis = new FileInputStream(file);
            byte[] buffer = new byte[1024];
            for (int len = 0; (len = fis.read(buffer)) > 0;) {
                fos.write(buffer, 0, len);
            }
        } catch (Exception e) {
            request.getSession().setAttribute("message", "上传失败！");
        } finally {
            if (fis != null) {
                try {
                    fis.close();
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

        // 2、解包
        String tempPath = ComponentFileUtil.getUpdatePackagePath() + fileName.substring(0, fileName.lastIndexOf("."));
        try {
            ZipUtil.unzipFile(new File(ComponentFileUtil.getUpdatePackagePath() + fileName), tempPath);
        } catch (Exception e) {
            uploadError("解析更新包错误！", fileName, tempPath);
            return new DefaultHttpHeaders(SUCCESS).disableCaching();
        }

        // 3、解析system.json文件
        File systemConfigFile = new File(tempPath + "/system.json");
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> systemInfoMap = null;
        try {
            systemInfoMap = objectMapper.readValue(systemConfigFile, Map.class);
        } catch (Exception e) {
            uploadError("解析system.json错误！", fileName, tempPath);
            return new DefaultHttpHeaders(SUCCESS).disableCaching();
        }
        systemInfoMap.put("fileName", fileName);
        systemInfoMap.put("tempPath", tempPath);

        // 4、初步校验更新包
        String message = validateUpdatePackage(systemInfoMap);
        if (StringUtil.isNotEmpty(message)) {
            uploadError(message, fileName, tempPath);
            return new DefaultHttpHeaders(SUCCESS).disableCaching();
        }

        String updatePackageConfigKey = request.getParameter("updatePackageConfigKey");
        request.getSession().setAttribute(updatePackageConfigKey, systemInfoMap);

        // dhtmlxVault上传完成时需要
        String fileId = request.getParameter("sessionId").toString().trim();
        request.getSession().setAttribute("FileUpload.Progress." + fileId, "-1");
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 校验更新包信息
     * 
     * @param systemInfoMap 更新包的系统配置
     * @return String 校验结果
     */
    @SuppressWarnings("unchecked")
    private String validateUpdatePackage(Map<String, String> systemInfoMap) {
        List<Object[]> systemList = DatabaseHandlerDao.getInstance().queryForList("select id,name,version,update_version from t_xtpz_system");
        Map<String, String> system = new HashMap<String, String>();
        if (CollectionUtils.isNotEmpty(systemList)) {
            Object[] objs = systemList.get(0);
            system.put("id", String.valueOf(objs[0]));
            system.put("name", String.valueOf(objs[1]));
            system.put("version", String.valueOf(objs[2]));
            system.put("updateVersion", String.valueOf(objs[3]));
        }
        if (!systemInfoMap.get("systemId").equals(system.get("id"))) {
            return "该更新包不是本系统的更新包，不能更新！";
        }
        if (!systemInfoMap.get("systemVersion").equals(system.get("version"))) {
            return "该更新包的系统版本不是本系统的系统版本，不能更新！";
        }
        if (StringUtil.isNotEmpty(system.get("updateVersion"))) {
            String[] systemVersions = system.get("updateVersion").split("\\.");
            String[] upSystemVersions = systemInfoMap.get("updatePackageVersion").split("\\.");
            if (systemVersions.length > 0 && upSystemVersions.length > 0) {
                if (systemVersions.length >= upSystemVersions.length) {
                    for (int i = 0; i < upSystemVersions.length; i++) {
                        if (Integer.parseInt(systemVersions[i]) > Integer.parseInt(upSystemVersions[i])) {
                            return "该更新包版本低于系统更新的版本，不能更新！";
                        }
                    }
                    return "该更新包版本低于系统更新的版本，不能更新！";
                } else {
                    for (int i = 0; i < systemVersions.length; i++) {
                        if (Integer.parseInt(systemVersions[i]) > Integer.parseInt(upSystemVersions[i])) {
                            return "该更新包版本低于系统更新的版本，不能更新！";
                        }
                    }
                }
            }
        }
        return "";
    }

    /**
     * 上传更新包有问题时的处理
     * 
     * @param message 错误消息
     * @param fileName 更新包文件路径
     * @param tempPath 更新包解包文件目录
     */
    private void uploadError(String message, String fileName, String tempPath) {
        HttpServletRequest request = ServletActionContext.getRequest();
        deleteUpdatePackage(ComponentFileUtil.getTempCompPath() + fileName, tempPath);
        request.getSession().setAttribute("message", message);
        // dhtmlxVault上传完成时需要
        String fileId = request.getParameter("sessionId").toString().trim();
        request.getSession().setAttribute("FileUpload.Progress." + fileId, "-1");
    }

    /**
     * 删除解压的临时文件和上传的文件
     * 
     * @param newFileName 更新包包名次
     * @param tempPath 更新包解压的临时目录
     */
    private void deleteUpdatePackage(String packagePath, String uncompressPath) {
        FileUtil.deleteFile(packagePath);
        FileUtil.deleteFile(uncompressPath);
    }

    /**
     * 获取上传更新包的情况
     * 
     * @return Object
     */
    public Object getUploadMessage() {
        HttpServletRequest request = ServletActionContext.getRequest();
        Object message = request.getSession().getAttribute("message");
        request.getSession().setAttribute("message", "");
        if (StringUtil.isNotEmpty(message)) {
            setReturnData("{'success':false, 'message':'" + message + "'}");
        } else {
            setReturnData("{'success':true}");
        }
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * dhtmlxVault获取ID方法
     * 
     * @return Object
     */
    public Object getIdHandler() {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        String id = request.getSession().getId().toString();
        try {
            PrintWriter writer = response.getWriter();
            writer.println(id);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        request.getSession().setAttribute("FileUpload.Progress." + id, "0");
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * dhtmlxVault获取信息方法
     * 
     * @return Object
     */
    public Object getInfoHandler() {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        try {
            PrintWriter writer = response.getWriter();
            writer.println(request.getSession().getAttribute("FileUpload.Progress." + request.getParameter("sessionId").toString().trim()));
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 执行更新包
     * 
     * @return Object
     */
    @SuppressWarnings("unchecked")
    public Object execUpdatePackage() {
        HttpServletRequest request = ServletActionContext.getRequest();
        String updatePackageConfigKey = request.getParameter("updatePackageConfigKey");
        Map<String, String> systemInfoMap = (Map<String, String>) request.getSession().getAttribute(updatePackageConfigKey);
        if (systemInfoMap == null) {
            setReturnData("{'success':false,'message':'上传失败！'}");
        } else {
            String fileName = systemInfoMap.get("fileName");
            String tempPath = systemInfoMap.get("tempPath");
            String updatePackageVersion = systemInfoMap.get("updatePackageVersion");
            // 1、解析config.json
            File configFile = new File(tempPath + "/config.json");
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> configInfoMap = null;
            try {
                configInfoMap = objectMapper.readValue(configFile, Map.class);
            } catch (Exception e) {
                setReturnData("{'success':false,'message':'解析config.json错误！'}");
                uploadError("解析config.json错误！", fileName, tempPath);
                return new DefaultHttpHeaders(SUCCESS).disableCaching();
            }
            // 2、更新编码、系统参数等信息，如果已经存在的，不更新值
            updateSystemInfo(configInfoMap);
            // 3、更新基础构件，自定义构件如果已经存在 直接忽略，开发的构件如果版本相同则判断打包时间 打包时间新的更新，版本大的则更新
            List<Map<String, Object>> assembleComponentVersionsOfConfig = new ArrayList<Map<String, Object>>();
            List<Map<String, Object>> componentVersionsOfConfig = (List<Map<String, Object>>) configInfoMap.get("ComponentVersions");
            if (CollectionUtils.isNotEmpty(componentVersionsOfConfig)) {
                for (Map<String, Object> componentVersionOfConfig : componentVersionsOfConfig) {
                    Map<String, String> componentOfConfig = (Map<String, String>) componentVersionOfConfig.get("Component");
                    Map<String, String> cvBaseInfoOfConfig = (Map<String, String>) componentVersionOfConfig.get("baseInfo");
                    if (ConstantVar.Component.Type.ASSEMBLY.equals(componentOfConfig.get("type"))) {
                        assembleComponentVersionsOfConfig.add(componentVersionOfConfig);
                        continue;
                    }
                    ComponentVersion cvOfDb = getService(ComponentVersionService.class).getByComponentNameAndVersion(componentOfConfig.get("name"),
                            cvBaseInfoOfConfig.get("version"));
                    if (cvOfDb != null) { // 该构件版本在数据库中存在
                        // 自定义构件已经存在 直接忽略
                        if (ConstantVar.Component.Type.SELF_DEFINE.indexOf(cvOfDb.getComponent().getType()) != -1
                                || ConstantVar.Component.Type.TAB.equals(cvOfDb.getComponent().getType())) {
                            continue;
                        } else {
                            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            if (StringUtil.isNotEmpty(cvOfDb.getPackageTime()) && StringUtil.isNotEmpty(cvBaseInfoOfConfig.get("packageTime"))) {
                                try {
                                    Date packageTimeOfConfig = df.parse(cvBaseInfoOfConfig.get("packageTime"));
                                    Date packageTimeOfDb = df.parse(cvOfDb.getPackageTime());
                                    if (packageTimeOfConfig.getTime() > packageTimeOfDb.getTime()) {
                                        saveComponentVersion(componentVersionOfConfig, cvOfDb);
                                    }
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    } else {
                        saveComponentVersion(componentVersionOfConfig, null);
                    }
                }
            }
            // 4、更新组合构件
            if (CollectionUtils.isNotEmpty(assembleComponentVersionsOfConfig)) {
                for (Map<String, Object> assembleComponentVersionOfConfig : assembleComponentVersionsOfConfig) {
                    Map<String, String> assembleComponentOfConfig = (Map<String, String>) assembleComponentVersionOfConfig.get("Component");
                    Map<String, String> assembleCvBaseInfoOfConfig = (Map<String, String>) assembleComponentVersionOfConfig.get("baseInfo");
                    ComponentVersion assembleCvOfDb = getService(ComponentVersionService.class).getByComponentNameAndVersion(
                            assembleComponentOfConfig.get("name"), assembleCvBaseInfoOfConfig.get("version"));
                    if (assembleCvOfDb != null) { // 该构件版本在数据库中存在
                        // 更新数据库
                        if (!assembleComponentOfConfig.get("alias").equals(assembleCvOfDb.getComponent().getAlias())) {
                            DatabaseHandlerDao.getInstance().executeSql(
                                    "update t_xtpz_component set alias='" + assembleComponentOfConfig.get("alias") + "' where id='"
                                            + assembleComponentOfConfig.get("id") + "'");
                        }
                        DatabaseHandlerDao.getInstance().executeSql(
                                "update t_xtpz_component_version set url='" + StringUtil.null2empty(assembleComponentOfConfig.get("url")) + "',remark='"
                                        + StringUtil.null2empty(assembleComponentOfConfig.get("remark")).replaceAll("'", "''") + "',package_time='"
                                        + assembleComponentOfConfig.get("packageTime") + "' where id='" + assembleComponentOfConfig.get("id") + "'");
                    } else {
                        DatabaseHandlerDao.getInstance().executeSql(
                                "insert into t_xtpz_component(id,code,name,alias,type) values('" + assembleComponentOfConfig.get("id") + "','"
                                        + assembleComponentOfConfig.get("code") + "','" + assembleComponentOfConfig.get("name") + "','"
                                        + assembleComponentOfConfig.get("alias") + "','" + assembleComponentOfConfig.get("type") + "')");
                        DatabaseHandlerDao.getInstance().executeSql(
                                "insert into t_xtpz_component_version(id,component_id,version,url,remark,area_id,path,import_date,views,"
                                        + "system_param_config,is_package,is_system_used,package_time) values ('"
                                        + assembleCvBaseInfoOfConfig.get("id")
                                        + "','"
                                        + assembleComponentOfConfig.get("id")
                                        + "','"
                                        + assembleCvBaseInfoOfConfig.get("version")
                                        + "','"
                                        + assembleCvBaseInfoOfConfig.get("url")
                                        + "','"
                                        + StringUtil.null2empty(assembleCvBaseInfoOfConfig.get("remark")).replaceAll("'", "''")
                                        + "','"
                                        + assembleCvBaseInfoOfConfig.get("areaId")
                                        + "','"
                                        + assembleCvBaseInfoOfConfig.get("path")
                                        + "',"
                                        + new Date()
                                        + ",'"
                                        + assembleCvBaseInfoOfConfig.get("views")
                                        + "','"
                                        + assembleCvBaseInfoOfConfig.get("systemParamConfig")
                                        + "','"
                                        + assembleCvBaseInfoOfConfig.get("isPackage") + "','1','" + assembleCvBaseInfoOfConfig.get("packageTime") + "')");
                    }
                }
            }
            List<Map<String, Object>> constructsOfConfig = (List<Map<String, Object>>) configInfoMap.get("Constructs");
            if (CollectionUtils.isNotEmpty(constructsOfConfig)) {
                List<Construct> constructsOfDb = getService(ConstructService.class).findAll();
                Map<String, Construct> constructMapOfDb = new HashMap<String, Construct>();
                if (CollectionUtils.isNotEmpty(constructsOfDb)) {
                    for (Construct constructOfDb : constructsOfDb) {
                        constructMapOfDb.put(constructOfDb.getId(), constructOfDb);
                    }
                }
                for (Map<String, Object> constructOfConfig : constructsOfConfig) {
                    if (!constructMapOfDb.containsKey(constructOfConfig.get("id"))) {
                        DatabaseHandlerDao.getInstance().executeSql("");
                    }
                }
            }
            // 5、更新菜单

            // 6、删除临时文件
            FileUtil.deleteFile(tempPath);
            setReturnData("{'success':true}");
            // 7、修改系统更新版本
            SystemUpdate systemUpdate = new SystemUpdate();
            systemUpdate.setUpdateVersion(updatePackageVersion);
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            systemUpdate.setUpdateTime(df.format(new Date()));
            getService().save(systemUpdate);
            DatabaseHandlerDao.getInstance().executeSql("update t_xtpz_system set update_version='" + updatePackageVersion + "'");
        }
        request.getSession().removeAttribute(updatePackageConfigKey);
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 更新编码、系统参数等信息，如果已经存在的，不更新值
     * 
     * @param configInfoMap 更新包信息
     */
    @SuppressWarnings("unchecked")
    private void updateSystemInfo(Map<String, Object> configInfoMap) {
        // 系统参数
        List<Map<String, String>> sysParamCategorysOfConfig = (List<Map<String, String>>) configInfoMap.get("SystemParameterCategorys");
        if (CollectionUtils.isNotEmpty(sysParamCategorysOfConfig)) {
            List<SystemParameterCategory> sysParamCategorysOfDb = getService(SystemParameterCategoryService.class).findAll();
            Map<String, SystemParameterCategory> sysParamCategoryMapOfDb = new HashMap<String, SystemParameterCategory>();
            if (CollectionUtils.isNotEmpty(sysParamCategorysOfDb)) {
                for (SystemParameterCategory sysParamCategoryOfDb : sysParamCategorysOfDb) {
                    sysParamCategoryMapOfDb.put(sysParamCategoryOfDb.getId(), sysParamCategoryOfDb);
                }
            }
            for (Map<String, String> sysParamCategoryOfConfig : sysParamCategorysOfConfig) {
                if (!sysParamCategoryMapOfDb.containsKey(sysParamCategoryOfConfig.get("id"))) {
                    DatabaseHandlerDao.getInstance().executeSql(
                            "insert into t_xtpz_system_param_category(id,name,parent_id,has_child) values('" + sysParamCategoryOfConfig.get("id") + "','"
                                    + sysParamCategoryOfConfig.get("name") + "','" + sysParamCategoryOfConfig.get("parentId") + "','"
                                    + StringUtil.null2zero(sysParamCategoryOfConfig.get("hasChild")) + "')");
                }
            }
        }
        List<Map<String, String>> sysParamsOfConfig = (List<Map<String, String>>) configInfoMap.get("SystemParameters");
        if (CollectionUtils.isNotEmpty(sysParamsOfConfig)) {
            List<SystemParameter> sysParamsOfDb = getService(SystemParameterService.class).findAll();
            Map<String, SystemParameter> sysParamMapOfDb = new HashMap<String, SystemParameter>();
            if (CollectionUtils.isNotEmpty(sysParamsOfDb)) {
                for (SystemParameter sysParamOfDb : sysParamsOfDb) {
                    sysParamMapOfDb.put(sysParamOfDb.getId(), sysParamOfDb);
                }
            }
            for (Map<String, String> sysParamOfConfig : sysParamsOfConfig) {
                if (!sysParamMapOfDb.containsKey(sysParamOfConfig.get("id"))) {
                    DatabaseHandlerDao.getInstance().executeSql(
                            "insert into t_xtpz_system_param(id,category_id,name,value,type,remark,show_order) values('" + sysParamOfConfig.get("id") + "','"
                                    + sysParamOfConfig.get("categoryId") + "','" + sysParamOfConfig.get("name") + "','"
                                    + StringUtil.null2empty(sysParamOfConfig.get("value")) + "','" + StringUtil.null2empty(sysParamOfConfig.get("type"))
                                    + "','" + StringUtil.null2empty(sysParamOfConfig.get("remark")).replaceAll("'", "''") + "',"
                                    + sysParamOfConfig.get("showOrder") + ")");
                }
            }
        }
        // 编码
        List<Map<String, String>> codeTypesOfConfig = (List<Map<String, String>>) configInfoMap.get("CodeTypes");
        if (CollectionUtils.isNotEmpty(codeTypesOfConfig)) {
            List<CodeType> codeTypesOfDb = getService(CodeTypeService.class).findAll();
            Map<String, CodeType> codeTypeMapOfDb = new HashMap<String, CodeType>();
            if (CollectionUtils.isNotEmpty(codeTypesOfDb)) {
                for (CodeType codeTypeOfDb : codeTypesOfDb) {
                    codeTypeMapOfDb.put(codeTypeOfDb.getId(), codeTypeOfDb);
                }
            }
            for (Map<String, String> codeTypeOfConfig : codeTypesOfConfig) {
                if (!codeTypeMapOfDb.containsKey(codeTypeOfConfig.get("id"))) {
                    DatabaseHandlerDao.getInstance().executeSql(
                            "insert into t_xtpz_code_type(id,name,code,is_system,is_business) values('" + codeTypeOfConfig.get("id") + "','"
                                    + codeTypeOfConfig.get("name") + "','" + codeTypeOfConfig.get("code") + "','" + codeTypeOfConfig.get("isSystem") + "','"
                                    + StringUtil.null2empty(codeTypeOfConfig.get("isBusiness")) + "')");
                }
            }
        }
        List<Map<String, String>> codesOfConfig = (List<Map<String, String>>) configInfoMap.get("Codes");
        if (CollectionUtils.isNotEmpty(codesOfConfig)) {
            List<Code> codesOfDb = getService(CodeService.class).findAll();
            Map<String, Code> codeMapOfDb = new HashMap<String, Code>();
            if (CollectionUtils.isNotEmpty(codesOfDb)) {
                for (Code codeOfDb : codesOfDb) {
                    codeMapOfDb.put(codeOfDb.getId(), codeOfDb);
                }
            }
            for (Map<String, String> codeOfConfig : codesOfConfig) {
                if (!codeMapOfDb.containsKey(codeOfConfig.get("id"))) {
                    DatabaseHandlerDao.getInstance().executeSql(
                            "insert into t_xtpz_code(id,name,value,show_order,code_type_code,parent_id,remark) values('" + codeOfConfig.get("id") + "','"
                                    + codeOfConfig.get("name") + "','" + codeOfConfig.get("value") + "'," + codeOfConfig.get("showOrder") + ",'"
                                    + codeOfConfig.get("codeTypeCode") + "','" + StringUtil.null2empty(codeOfConfig.get("parentId")) + "','"
                                    + StringUtil.null2empty(codeOfConfig.get("remark")).replaceAll("'", "''") + "')");
                }
            }
        }
        List<Map<String, String>> businessCodesOfConfig = (List<Map<String, String>>) configInfoMap.get("BusinessCodes");
        if (CollectionUtils.isNotEmpty(businessCodesOfConfig)) {
            List<BusinessCode> businessCodesOfDb = getService(BusinessCodeService.class).findAll();
            Map<String, BusinessCode> businessCodeMapOfDb = new HashMap<String, BusinessCode>();
            if (CollectionUtils.isNotEmpty(businessCodesOfDb)) {
                for (BusinessCode businessCodeOfDb : businessCodesOfDb) {
                    businessCodeMapOfDb.put(businessCodeOfDb.getId(), businessCodeOfDb);
                }
            }
            for (Map<String, String> businessCodeOfConfig : businessCodesOfConfig) {
                if (!businessCodeMapOfDb.containsKey(businessCodeOfConfig.get("id"))) {
                    DatabaseHandlerDao.getInstance().executeSql(
                            "insert into t_xtpz_business_code(id,table_name,code_name_field,code_value_field,show_order_field,id_field,parent_id_field,"
                                    + "code_type_code,is_auth,is_timing_update,period,business_code_type,class_name) values('"
                                    + businessCodeOfConfig.get("id")
                                    + "','"
                                    + StringUtil.null2empty(businessCodeOfConfig.get("tableName"))
                                    + "','"
                                    + StringUtil.null2empty(businessCodeOfConfig.get("codeNameField"))
                                    + "','"
                                    + StringUtil.null2empty(businessCodeOfConfig.get("codeValueField"))
                                    + "','"
                                    + StringUtil.null2empty(businessCodeOfConfig.get("showOrderField"))
                                    + "','"
                                    + StringUtil.null2empty(businessCodeOfConfig.get("idField"))
                                    + "','"
                                    + StringUtil.null2empty(businessCodeOfConfig.get("parentIdField"))
                                    + "','"
                                    + StringUtil.null2empty(businessCodeOfConfig.get("codeTypeCode"))
                                    + "','"
                                    + businessCodeOfConfig.get("isAuth")
                                    + "','"
                                    + businessCodeOfConfig.get("isTimingUpdate")
                                    + "','"
                                    + StringUtil.null2empty(businessCodeOfConfig.get("period"))
                                    + "','"
                                    + StringUtil.null2empty(businessCodeOfConfig.get("businessCodeType"))
                                    + "','"
                                    + StringUtil.null2empty(businessCodeOfConfig.get("className")) + "')");
                }
            }
        }
        // 标签
        List<Map<String, String>> columnLabelCategorysOfConfig = (List<Map<String, String>>) configInfoMap.get("ColumnLabelCategorys");
        if (CollectionUtils.isNotEmpty(columnLabelCategorysOfConfig)) {
            List<ColumnLabelCategory> columnLabelCategorysOfDb = getService(ColumnLabelCategoryService.class).findAll();
            Map<String, ColumnLabelCategory> columnLabelCategoryMapOfDb = new HashMap<String, ColumnLabelCategory>();
            if (CollectionUtils.isNotEmpty(columnLabelCategorysOfDb)) {
                for (ColumnLabelCategory columnLabelCategoryOfDb : columnLabelCategorysOfDb) {
                    columnLabelCategoryMapOfDb.put(columnLabelCategoryOfDb.getId(), columnLabelCategoryOfDb);
                }
            }
            for (Map<String, String> columnLabelCategoryOfConfig : columnLabelCategorysOfConfig) {
                if (!columnLabelCategoryMapOfDb.containsKey(columnLabelCategoryOfConfig.get("id"))) {
                    DatabaseHandlerDao.getInstance().executeSql(
                            "insert into t_xtpz_column_label_category(id,name,show_order) values('" + columnLabelCategoryOfConfig.get("id") + "','"
                                    + columnLabelCategoryOfConfig.get("name") + "'," + columnLabelCategoryOfConfig.get("showOrder") + ")");
                }
            }
        }
        List<Map<String, String>> columnLabelsOfConfig = (List<Map<String, String>>) configInfoMap.get("ColumnLabels");
        if (CollectionUtils.isNotEmpty(columnLabelsOfConfig)) {
            List<ColumnLabel> columnLabelsOfDb = getService(ColumnLabelService.class).findAll();
            Map<String, ColumnLabel> columnLabelMapOfDb = new HashMap<String, ColumnLabel>();
            if (CollectionUtils.isNotEmpty(columnLabelsOfDb)) {
                for (ColumnLabel columnLabelOfDb : columnLabelsOfDb) {
                    columnLabelMapOfDb.put(columnLabelOfDb.getId(), columnLabelOfDb);
                }
            }
            for (Map<String, String> columnLabelOfConfig : columnLabelsOfConfig) {
                if (!columnLabelMapOfDb.containsKey(columnLabelOfConfig.get("id"))) {
                    DatabaseHandlerDao.getInstance().executeSql(
                            "insert into t_xtpz_column_label(id,code,name,show_order,category_id) values('" + columnLabelOfConfig.get("id") + "','"
                                    + columnLabelOfConfig.get("code") + "','" + columnLabelOfConfig.get("name") + "',"
                                    + StringUtil.null2empty(columnLabelOfConfig.get("showOrder")) + ",'"
                                    + StringUtil.null2empty(columnLabelOfConfig.get("categoryId")) + "')");
                }
            }
        }
        List<Map<String, String>> typeLabelsOfConfig = (List<Map<String, String>>) configInfoMap.get("TypeLabels");
        if (CollectionUtils.isNotEmpty(typeLabelsOfConfig)) {
            List<TypeLabel> typeLabelsOfDb = getService(TypeLabelService.class).findAll();
            Map<String, TypeLabel> typeLabelMapOfDb = new HashMap<String, TypeLabel>();
            if (CollectionUtils.isNotEmpty(typeLabelsOfDb)) {
                for (TypeLabel typeLabelOfDb : typeLabelsOfDb) {
                    typeLabelMapOfDb.put(typeLabelOfDb.getId(), typeLabelOfDb);
                }
            }
            for (Map<String, String> typeLabelOfConfig : typeLabelsOfConfig) {
                if (!typeLabelMapOfDb.containsKey(typeLabelOfConfig.get("id"))) {
                    DatabaseHandlerDao.getInstance().executeSql(
                            "insert into t_xtpz_type_label(id,code,name,show_order) values('" + typeLabelOfConfig.get("id") + "','"
                                    + typeLabelOfConfig.get("code") + "','" + typeLabelOfConfig.get("name") + "',"
                                    + StringUtil.null2empty(typeLabelOfConfig.get("showOrder")) + ")");
                }
            }
        }
        // 构件分类
        List<Map<String, String>> componentAreasOfConfig = (List<Map<String, String>>) configInfoMap.get("ComponentAreas");
        if (CollectionUtils.isNotEmpty(componentAreasOfConfig)) {
            List<ComponentArea> componentAreasOfDb = getService(ComponentAreaService.class).findAll();
            Map<String, ComponentArea> componentAreaMapOfDb = new HashMap<String, ComponentArea>();
            if (CollectionUtils.isNotEmpty(componentAreasOfDb)) {
                for (ComponentArea componentAreaOfDb : componentAreasOfDb) {
                    componentAreaMapOfDb.put(componentAreaOfDb.getId(), componentAreaOfDb);
                }
            }
            for (Map<String, String> componentAreaOfConfig : componentAreasOfConfig) {
                if (!componentAreaMapOfDb.containsKey(componentAreaOfConfig.get("id"))) {
                    DatabaseHandlerDao.getInstance().executeSql(
                            "insert into t_xtpz_component_area(id,name,show_order,parent_id,has_child) values('" + componentAreaOfConfig.get("id") + "','"
                                    + componentAreaOfConfig.get("name") + "'," + StringUtil.null2zero(componentAreaOfConfig.get("showOrder")) + ",'"
                                    + componentAreaOfConfig.get("parentId") + "','" + componentAreaOfConfig.get("hasChild") + "')");
                }
            }
        }
    }

    /**
     * 保存构件
     * 
     * @param componentVersionOfConfig 构件版本配置
     * @param cvOfDb 数据库中的构件
     */
    @SuppressWarnings("unchecked")
    private void saveComponentVersion(Map<String, Object> componentVersionOfConfig, ComponentVersion cvOfDb) {
        Map<String, String> componentOfConfig = (Map<String, String>) componentVersionOfConfig.get("Component");
        Map<String, String> cvBaseInfoOfConfig = (Map<String, String>) componentVersionOfConfig.get("baseInfo");
        String componentVersionId = null;
        String componentId = null;
        if (cvOfDb != null) {
            componentVersionId = cvOfDb.getId();
            componentId = cvOfDb.getComponent().getId();
            // 更新数据库
            if (!componentOfConfig.get("alias").equals(cvOfDb.getComponent().getAlias())) {
                DatabaseHandlerDao.getInstance().executeSql(
                        "update t_xtpz_component set alias='" + componentOfConfig.get("alias") + "' where id='" + componentId + "'");
            }
            DatabaseHandlerDao.getInstance().executeSql(
                    "update t_xtpz_component_version set url='" + StringUtil.null2empty(componentOfConfig.get("url")) + "',remark='"
                            + StringUtil.null2empty(componentOfConfig.get("remark")).replaceAll("'", "''") + "',package_time='"
                            + componentOfConfig.get("packageTime") + "' where id='" + componentVersionId + "'");
        } else {
            componentVersionId = cvBaseInfoOfConfig.get("id");
            componentId = componentOfConfig.get("id");
            DatabaseHandlerDao.getInstance().executeSql(
                    "insert into t_xtpz_component(id,code,name,alias,type) values('" + componentOfConfig.get("id") + "','" + componentOfConfig.get("code")
                            + "','" + componentOfConfig.get("name") + "','" + componentOfConfig.get("alias") + "','" + componentOfConfig.get("type") + "')");
            DatabaseHandlerDao.getInstance().executeSql(
                    "insert into t_xtpz_component_version(id,component_id,version,url,remark,area_id,path,import_date,views,"
                            + "system_param_config,is_package,is_system_used,package_time) values ('"
                            + componentVersionId
                            + "','"
                            + componentId
                            + "','"
                            + componentOfConfig.get("version")
                            + "','"
                            + componentOfConfig.get("url")
                            + "','"
                            + StringUtil.null2empty(componentOfConfig.get("remark")).replaceAll("'", "''")
                            + "','"
                            + componentOfConfig.get("areaId")
                            + "','"
                            + componentOfConfig.get("path")
                            + "',"
                            + new Date()
                            + ",'"
                            + componentOfConfig.get("views")
                            + "','"
                            + componentOfConfig.get("systemParamConfig")
                            + "','"
                            + componentOfConfig.get("isPackage")
                            + "','1','"
                            + componentOfConfig.get("packageTime") + "')");
        }
        // 更新预留区
        List<Map<String, String>> reserveZonesOfConfig = (List<Map<String, String>>) componentVersionOfConfig.get("ComponentReserveZones");
        if (CollectionUtils.isNotEmpty(reserveZonesOfConfig)) {
            List<ComponentReserveZone> reserveZonesOfDb = getService(ComponentReserveZoneService.class).getByComponentVersionId(componentVersionId);
            Map<String, ComponentReserveZone> reserveZoneMapOfDb = new HashMap<String, ComponentReserveZone>();
            if (CollectionUtils.isNotEmpty(reserveZonesOfDb)) {
                for (ComponentReserveZone reserveZoneOfDb : reserveZonesOfDb) {
                    reserveZoneMapOfDb.put(reserveZoneOfDb.getId(), reserveZoneOfDb);
                }
            }
            for (Map<String, String> reserveZoneOfConfig : reserveZonesOfConfig) {
                if (!reserveZoneMapOfDb.containsKey(reserveZoneOfConfig.get("id"))) {
                    DatabaseHandlerDao.getInstance().executeSql(
                            "insert into t_xtpz_component_reserve_zone(id,component_version_id,name,alias,type,page,is_common,show_order) values('"
                                    + reserveZoneOfConfig.get("id") + "','" + componentVersionId + "','" + reserveZoneOfConfig.get("name") + "','"
                                    + reserveZoneOfConfig.get("alias") + "','" + reserveZoneOfConfig.get("type") + "','" + reserveZoneOfConfig.get("page")
                                    + "','" + reserveZoneOfConfig.get("isCommon") + "'," + reserveZoneOfConfig.get("showOrder") + ")");
                }
            }
        }
        // 更新系统参数
        List<Map<String, String>> systemParametersOfConfig = (List<Map<String, String>>) componentVersionOfConfig.get("ComponentSystemParameters");
        if (CollectionUtils.isNotEmpty(systemParametersOfConfig)) {
            List<ComponentSystemParameter> systemParametersOfDb = getService(ComponentSystemParameterService.class).getByComponentVersionId(componentVersionId);
            Map<String, ComponentSystemParameter> systemParameterMapOfDb = new HashMap<String, ComponentSystemParameter>();
            if (CollectionUtils.isNotEmpty(systemParametersOfDb)) {
                for (ComponentSystemParameter systemParameterOfDb : systemParametersOfDb) {
                    systemParameterMapOfDb.put(systemParameterOfDb.getId(), systemParameterOfDb);
                }
            }
            for (Map<String, String> systemParameterOfConfig : systemParametersOfConfig) {
                if (!systemParameterMapOfDb.containsKey(systemParameterOfConfig.get("id"))) {
                    DatabaseHandlerDao.getInstance().executeSql(
                            "insert into t_xtpz_component_system_param(id,component_version_id,name,remark) values('" + systemParameterOfConfig.get("id")
                                    + "','" + componentVersionId + "','" + systemParameterOfConfig.get("name") + "','"
                                    + systemParameterOfConfig.get("remark").replaceAll("'", "''") + "')");
                }
            }
            List<Map<String, String>> systemParameterRelationsOfConfig = (List<Map<String, String>>) componentVersionOfConfig
                    .get("ComponentSystemParameterRelations");
            if (CollectionUtils.isNotEmpty(systemParameterRelationsOfConfig)) {
                getService(ComponentSystemParameterRelationService.class).deleteByComponentVersionId(componentVersionId);
                for (Map<String, String> systemParameterRelationOfConfig : systemParameterRelationsOfConfig) {
                    DatabaseHandlerDao.getInstance().executeSql(
                            "insert into t_xtpz_comp_sys_param_relation(id,component_system_param_id,system_param_id,component_version_id) values('"
                                    + systemParameterRelationOfConfig.get("id") + "','" + systemParameterRelationOfConfig.get("componentSystemParamId") + "','"
                                    + systemParameterRelationOfConfig.get("systemParamId") + "','" + componentVersionId + "')");
                }
            }
        }
        // 构件自身参数
        List<Map<String, String>> selfParamsOfConfig = (List<Map<String, String>>) componentVersionOfConfig.get("ComponentSelfParams");
        if (CollectionUtils.isNotEmpty(selfParamsOfConfig)) {
            List<ComponentSelfParam> selfParamsOfDb = getService(ComponentSelfParamService.class).getByComponentVersionId(componentVersionId);
            Map<String, ComponentSelfParam> selfParamMapOfDb = new HashMap<String, ComponentSelfParam>();
            if (CollectionUtils.isNotEmpty(selfParamsOfDb)) {
                for (ComponentSelfParam selfParamOfDb : selfParamsOfDb) {
                    selfParamMapOfDb.put(selfParamOfDb.getId(), selfParamOfDb);
                }
            }
            for (Map<String, String> selfParamOfConfig : selfParamsOfConfig) {
                if (!selfParamMapOfDb.containsKey(selfParamOfConfig.get("id"))) {
                    DatabaseHandlerDao.getInstance().executeSql(
                            "insert into t_xtpz_component_self_param(id,component_version_id,name,type,value,remark,options,text) values('"
                                    + selfParamOfConfig.get("id") + "','" + componentVersionId + "','" + selfParamOfConfig.get("name") + "','"
                                    + selfParamOfConfig.get("type") + "','" + StringUtil.null2empty(selfParamOfConfig.get("value")) + "','"
                                    + StringUtil.null2empty(selfParamOfConfig.get("remark")).replaceAll("'", "''") + "','"
                                    + StringUtil.null2empty(selfParamOfConfig.get("options")).replaceAll("'", "''") + "','"
                                    + StringUtil.null2empty(selfParamOfConfig.get("text")) + "')");
                }
            }
        }
        // 构件输入参数
        List<Map<String, String>> inputParamsOfConfig = (List<Map<String, String>>) componentVersionOfConfig.get("ComponentInputParams");
        if (CollectionUtils.isNotEmpty(inputParamsOfConfig)) {
            List<ComponentInputParam> inputParamsOfDb = getService(ComponentInputParamService.class).getByComponentVersionId(componentVersionId);
            Map<String, ComponentInputParam> inputParamMapOfDb = new HashMap<String, ComponentInputParam>();
            if (CollectionUtils.isNotEmpty(inputParamsOfDb)) {
                for (ComponentInputParam inputParamOfDb : inputParamsOfDb) {
                    inputParamMapOfDb.put(inputParamOfDb.getId(), inputParamOfDb);
                }
            }
            for (Map<String, String> inputParamOfConfig : inputParamsOfConfig) {
                if (!inputParamMapOfDb.containsKey(inputParamOfConfig.get("id"))) {
                    DatabaseHandlerDao.getInstance().executeSql(
                            "insert into t_xtpz_component_input_param(id,component_version_id,name,value,remark) values('" + inputParamOfConfig.get("id")
                                    + "','" + componentVersionId + "','" + inputParamOfConfig.get("name") + "','"
                                    + StringUtil.null2empty(inputParamOfConfig.get("value")) + "','"
                                    + StringUtil.null2empty(inputParamOfConfig.get("remark")).replaceAll("'", "''") + "')");
                }
            }
        }
        // 构件输出参数
        List<Map<String, String>> outputParamsOfConfig = (List<Map<String, String>>) componentVersionOfConfig.get("ComponentOutputParams");
        if (CollectionUtils.isNotEmpty(outputParamsOfConfig)) {
            List<ComponentOutputParam> outputParamsOfDb = getService(ComponentOutputParamService.class).getByComponentVersionId(componentVersionId);
            Map<String, ComponentOutputParam> outputParamMapOfDb = new HashMap<String, ComponentOutputParam>();
            if (CollectionUtils.isNotEmpty(outputParamsOfDb)) {
                for (ComponentOutputParam outputParamOfDb : outputParamsOfDb) {
                    outputParamMapOfDb.put(outputParamOfDb.getId(), outputParamOfDb);
                }
            }
            for (Map<String, String> outputParamOfConfig : outputParamsOfConfig) {
                if (!outputParamMapOfDb.containsKey(outputParamOfConfig.get("id"))) {
                    DatabaseHandlerDao.getInstance().executeSql(
                            "insert into t_xtpz_component_output_param(id,component_version_id,name,remark) values('" + outputParamOfConfig.get("id") + "','"
                                    + componentVersionId + "','" + outputParamOfConfig.get("name") + "','"
                                    + StringUtil.null2empty(outputParamOfConfig.get("remark")).replaceAll("'", "''") + "')");
                }
            }
        }
        // 构件方法
        List<Map<String, Object>> functionsOfConfig = (List<Map<String, Object>>) componentVersionOfConfig.get("ComponentFunctions");
        if (CollectionUtils.isNotEmpty(functionsOfConfig)) {
            List<ComponentFunction> functionsOfDb = getService(ComponentFunctionService.class).getByComponentVersionId(componentVersionId);
            Map<String, ComponentFunction> functionMapOfDb = new HashMap<String, ComponentFunction>();
            if (CollectionUtils.isNotEmpty(functionsOfDb)) {
                for (ComponentFunction functionOfDb : functionsOfDb) {
                    functionMapOfDb.put(functionOfDb.getId(), functionOfDb);
                }
            }
            for (Map<String, Object> functionOfConfig : functionsOfConfig) {
                Map<String, String> baseInfo = (Map<String, String>) functionOfConfig.get("baseInfo");
                if (!functionMapOfDb.containsKey(baseInfo.get("id"))) {
                    DatabaseHandlerDao.getInstance().executeSql(
                            "insert into t_xtpz_component_function(id,component_version_id,name,page,remark) values('" + baseInfo.get("id") + "','"
                                    + componentVersionId + "','" + baseInfo.get("name") + "','" + baseInfo.get("page") + "','"
                                    + StringUtil.null2empty(baseInfo.get("remark")).replaceAll("'", "''") + "')");
                }
                List<Map<String, String>> functionDatas = (List<Map<String, String>>) functionOfConfig.get("ComponentFunctionDatas");
                if (CollectionUtils.isNotEmpty(functionDatas)) {
                    List<ComponentFunctionData> functionDatasOfDb = getService(ComponentFunctionDataService.class).getByFunctionId(baseInfo.get("id"));
                    Map<String, ComponentFunctionData> functionDataMapOfDb = new HashMap<String, ComponentFunctionData>();
                    if (CollectionUtils.isNotEmpty(functionDatasOfDb)) {
                        for (ComponentFunctionData functionDataOfDb : functionDatasOfDb) {
                            functionDataMapOfDb.put(functionDataOfDb.getId(), functionDataOfDb);
                        }
                    }
                    for (Map<String, String> functionData : functionDatas) {
                        if (!functionDataMapOfDb.containsKey(functionData.get("id"))) {
                            DatabaseHandlerDao.getInstance().executeSql(
                                    "insert into t_xtpz_component_function_data(id,function_id,name,remark) values('" + functionData.get("id") + "','"
                                            + functionData.get("functionId") + "','" + functionData.get("name") + "','"
                                            + StringUtil.null2empty(functionData.get("remark")).replaceAll("'", "''") + "')");
                        }
                    }
                }
            }
        }
        // 构件回调函数
        List<Map<String, Object>> callbacksOfConfig = (List<Map<String, Object>>) componentVersionOfConfig.get("ComponentCallbacks");
        if (CollectionUtils.isNotEmpty(callbacksOfConfig)) {
            List<ComponentCallback> callbacksOfDb = getService(ComponentCallbackService.class).getByComponentVersionId(componentVersionId);
            Map<String, ComponentCallback> callbackMapOfDb = new HashMap<String, ComponentCallback>();
            if (CollectionUtils.isNotEmpty(callbacksOfDb)) {
                for (ComponentCallback callbackOfDb : callbacksOfDb) {
                    callbackMapOfDb.put(callbackOfDb.getId(), callbackOfDb);
                }
            }
            for (Map<String, Object> callbackOfConfig : callbacksOfConfig) {
                Map<String, String> baseInfo = (Map<String, String>) callbackOfConfig.get("baseInfo");
                if (!callbackMapOfDb.containsKey(baseInfo.get("id"))) {
                    DatabaseHandlerDao.getInstance().executeSql(
                            "insert into t_xtpz_component_callback(id,component_version_id,name,page,remark) values('" + baseInfo.get("id") + "','"
                                    + componentVersionId + "','" + baseInfo.get("name") + "','" + baseInfo.get("page") + "','"
                                    + StringUtil.null2empty(baseInfo.get("remark")).replaceAll("'", "''") + "')");
                }
                List<Map<String, String>> callbackParams = (List<Map<String, String>>) callbackOfConfig.get("ComponentCallbackParams");
                if (CollectionUtils.isNotEmpty(callbackParams)) {
                    List<ComponentCallbackParam> callbackParamsOfDb = getService(ComponentCallbackParamService.class).getByCallbackId(baseInfo.get("id"));
                    Map<String, ComponentCallbackParam> callbackParamMapOfDb = new HashMap<String, ComponentCallbackParam>();
                    if (CollectionUtils.isNotEmpty(callbackParamsOfDb)) {
                        for (ComponentCallbackParam callbackParamOfDb : callbackParamsOfDb) {
                            callbackParamMapOfDb.put(callbackParamOfDb.getId(), callbackParamOfDb);
                        }
                    }
                    for (Map<String, String> callbackParam : callbackParams) {
                        if (!callbackParamMapOfDb.containsKey(callbackParam.get("id"))) {
                            DatabaseHandlerDao.getInstance().executeSql(
                                    "insert into t_xtpz_comp_callback_param(id,callback_id,name,remark) values('" + callbackParam.get("id") + "','"
                                            + callbackParam.get("callbackId") + "','" + callbackParam.get("name") + "','"
                                            + StringUtil.null2empty(callbackParam.get("remark")).replaceAll("'", "''") + "')");
                        }
                    }
                }
            }
        }
        // 构件按钮
        List<Map<String, String>> buttonsOfConfig = (List<Map<String, String>>) componentVersionOfConfig.get("ComponentButtons");
        if (CollectionUtils.isNotEmpty(buttonsOfConfig)) {
            List<ComponentButton> buttonsOfDb = getService(ComponentButtonService.class).getByComponentVersionId(componentVersionId);
            Map<String, ComponentButton> buttonMapOfDb = new HashMap<String, ComponentButton>();
            if (CollectionUtils.isNotEmpty(buttonsOfDb)) {
                for (ComponentButton buttonOfDb : buttonsOfDb) {
                    buttonMapOfDb.put(buttonOfDb.getId(), buttonOfDb);
                }
            }
            for (Map<String, String> buttonOfConfig : buttonsOfConfig) {
                if (!buttonMapOfDb.containsKey(buttonOfConfig.get("id"))) {
                    DatabaseHandlerDao.getInstance().executeSql(
                            "insert into t_xtpz_component_button(id,component_version_id,name,display_name) values('" + buttonOfConfig.get("id") + "','"
                                    + componentVersionId + "','" + buttonOfConfig.get("name") + "','"
                                    + StringUtil.null2empty(buttonOfConfig.get("displayName")) + "')");
                }
            }
        }
        // 构件Class
        List<Map<String, String>> classesOfConfig = (List<Map<String, String>>) componentVersionOfConfig.get("ComponentClasses");
        if (CollectionUtils.isNotEmpty(classesOfConfig)) {
            List<ComponentClass> classesOfDb = getService(ComponentClassService.class).getByComponentVersionId(componentVersionId);
            Map<String, ComponentClass> classMapOfDb = new HashMap<String, ComponentClass>();
            if (CollectionUtils.isNotEmpty(classesOfDb)) {
                for (ComponentClass classOfDb : classesOfDb) {
                    classMapOfDb.put(classOfDb.getId(), classOfDb);
                }
            }
            for (Map<String, String> classOfConfig : classesOfConfig) {
                if (!classMapOfDb.containsKey(classOfConfig.get("id"))) {
                    DatabaseHandlerDao.getInstance().executeSql(
                            "insert into t_xtpz_component_class(id,component_version_id,name) values('" + classOfConfig.get("id") + "','" + componentVersionId
                                    + "','" + classOfConfig.get("name") + "')");
                }
            }
        }
        // 构件Jar
        List<Map<String, String>> jarsOfConfig = (List<Map<String, String>>) componentVersionOfConfig.get("ComponentJars");
        if (CollectionUtils.isNotEmpty(jarsOfConfig)) {
            List<ComponentJar> jarsOfDb = getService(ComponentJarService.class).getByComponentVersionId(componentVersionId);
            Map<String, ComponentJar> jarMapOfDb = new HashMap<String, ComponentJar>();
            if (CollectionUtils.isNotEmpty(jarsOfDb)) {
                for (ComponentJar jarOfDb : jarsOfDb) {
                    jarMapOfDb.put(jarOfDb.getId(), jarOfDb);
                }
            }
            for (Map<String, String> jarOfConfig : jarsOfConfig) {
                if (!jarMapOfDb.containsKey(jarOfConfig.get("id"))) {
                    DatabaseHandlerDao.getInstance().executeSql(
                            "insert into t_xtpz_component_jar(id,component_version_id,name) values('" + jarOfConfig.get("id") + "','" + componentVersionId
                                    + "','" + jarOfConfig.get("name") + "')");
                }
            }
        }
        // 构件表
        List<Map<String, Object>> tablesOfConfig = (List<Map<String, Object>>) componentVersionOfConfig.get("ComponentTables");
        if (CollectionUtils.isNotEmpty(tablesOfConfig)) {
            List<ComponentTable> tablesOfDb = getService(ComponentTableService.class).findAll();
            Map<String, ComponentTable> tableMapOfDb = new HashMap<String, ComponentTable>();
            if (CollectionUtils.isNotEmpty(tablesOfDb)) {
                for (ComponentTable tableOfDb : tablesOfDb) {
                    tableMapOfDb.put(tableOfDb.getId(), tableOfDb);
                }
            }
            for (Map<String, Object> tableOfConfig : tablesOfConfig) {
                Map<String, String> tableBaseInfoOfConfig = (Map<String, String>) tableOfConfig.get("baseInfo");
                List<Map<String, String>> columnsOfConfig = (List<Map<String, String>>) tableOfConfig.get("ComponentColumns");
                if (!tableMapOfDb.containsKey(tableBaseInfoOfConfig.get("id"))) {
                    DatabaseHandlerDao.getInstance().executeSql(
                            "insert into t_xtpz_component_table(id,name,release_with_data) values('" + tableBaseInfoOfConfig.get("id") + "','"
                                    + tableBaseInfoOfConfig.get("name") + "','" + tableBaseInfoOfConfig.get("releaseWithData") + "')");
                    if (CollectionUtils.isNotEmpty(columnsOfConfig)) {
                        for (Map<String, String> columnOfConfig : columnsOfConfig) {
                            DatabaseHandlerDao.getInstance().executeSql(
                                    "insert into insert into t_xtpz_component_column(id,name,type,is_null,default_value,remark,length) values('"
                                            + columnOfConfig.get("id") + "','" + columnOfConfig.get("name") + "','" + columnOfConfig.get("type") + "','"
                                            + columnOfConfig.get("isNull") + "','" + StringUtil.null2empty(columnOfConfig.get("defaultValue")) + "','"
                                            + StringUtil.null2empty(columnOfConfig.get("remark")) + "'," + columnOfConfig.get("length") + ")");
                        }
                    }
                } else {
                    if (!tableBaseInfoOfConfig.get("releaseWithData").equals(tableMapOfDb.get(tableBaseInfoOfConfig.get("id")).getReleaseWithData())) {
                        DatabaseHandlerDao.getInstance().executeSql(
                                "update t_xtpz_component_table set release_with_data='" + tableBaseInfoOfConfig.get("releaseWithData") + "' where id='"
                                        + tableBaseInfoOfConfig.get("id") + "'");
                    }
                    if (CollectionUtils.isNotEmpty(columnsOfConfig)) {
                        List<ComponentColumn> columnsOfDb = getService(ComponentColumnService.class).getByComponentVersionIdAndTableId(componentVersionId,
                                tableBaseInfoOfConfig.get("id"));
                        Map<String, ComponentColumn> columnMapOfDb = new HashMap<String, ComponentColumn>();
                        if (CollectionUtils.isNotEmpty(columnsOfDb)) {
                            for (ComponentColumn columnOfDb : columnsOfDb) {
                                columnMapOfDb.put(columnOfDb.getId(), columnOfDb);
                            }
                        }
                        for (Map<String, String> columnOfConfig : columnsOfConfig) {
                            if (!columnMapOfDb.containsKey(columnOfConfig.get("id"))) {
                                DatabaseHandlerDao.getInstance().executeSql(
                                        "insert into insert into t_xtpz_component_column(id,name,type,is_null,default_value,remark,length) values('"
                                                + columnOfConfig.get("id") + "','" + columnOfConfig.get("name") + "','" + columnOfConfig.get("type") + "','"
                                                + columnOfConfig.get("isNull") + "','" + StringUtil.null2empty(columnOfConfig.get("defaultValue")) + "','"
                                                + StringUtil.null2empty(columnOfConfig.get("remark")) + "'," + columnOfConfig.get("length") + ")");
                            } else {
                                DatabaseHandlerDao.getInstance().executeSql(
                                        "update t_xtpz_component_column set name='" + columnOfConfig.get("name") + "',type='" + columnOfConfig.get("type")
                                                + "',is_null='" + columnOfConfig.get("isNull") + "',default_value='"
                                                + StringUtil.null2empty(columnOfConfig.get("defaultValue")) + "',remark='"
                                                + StringUtil.null2empty(columnOfConfig.get("remark")) + "',length=" + columnOfConfig.get("length")
                                                + " where id='" + columnOfConfig.get("id") + "'");
                            }
                        }
                    }
                }
            }
            // 更新数据库
            ComponentVersion componentVersion = getService(ComponentVersionService.class).getByComponentNameAndVersion(componentOfConfig.get("name"),
                    cvBaseInfoOfConfig.get("version"));
            ComponentFileUtil.createComponentTable(componentVersion);
        }
        // 构件、表、字段的关系
        List<Map<String, String>> compTabColRelationsOfConfig = (List<Map<String, String>>) componentVersionOfConfig.get("ComponentTableColumnRelations");
        if (CollectionUtils.isNotEmpty(compTabColRelationsOfConfig)) {
            List<ComponentTableColumnRelation> compTabColRelationsOfDb = getService(ComponentTableColumnRelationService.class).getByComponentVersionId(
                    componentVersionId);
            Map<String, ComponentTableColumnRelation> compTabColRelationMapOfDb = new HashMap<String, ComponentTableColumnRelation>();
            if (CollectionUtils.isNotEmpty(compTabColRelationsOfDb)) {
                for (ComponentTableColumnRelation compTabColRelationOfDb : compTabColRelationsOfDb) {
                    compTabColRelationMapOfDb.put(compTabColRelationOfDb.getId(), compTabColRelationOfDb);
                }
            }
            for (Map<String, String> compTabColRelationOfConfig : compTabColRelationsOfConfig) {
                if (!compTabColRelationMapOfDb.containsKey(compTabColRelationOfConfig.get("id"))) {
                    DatabaseHandlerDao.getInstance().executeSql(
                            "insert into t_xtpz_component_table_column(id,component_version_id,table_id,column_id) values('"
                                    + compTabColRelationOfConfig.get("id") + "','" + componentVersionId + "','" + compTabColRelationOfConfig.get("tableId")
                                    + "','" + compTabColRelationOfConfig.get("columnId") + "')");
                }
            }
        }
        // 构件与公用构件关联关系
        List<Map<String, String>> commonComponentRelationsOfConfig = (List<Map<String, String>>) componentVersionOfConfig.get("CommonComponentRelations");
        if (CollectionUtils.isNotEmpty(commonComponentRelationsOfConfig)) {
            List<CommonComponentRelation> commonComponentRelationsOfDb = getService(CommonComponentRelationService.class).getByComponentVersionId(
                    componentVersionId);
            Map<String, CommonComponentRelation> commonComponentRelationMapOfDb = new HashMap<String, CommonComponentRelation>();
            if (CollectionUtils.isNotEmpty(commonComponentRelationsOfDb)) {
                for (CommonComponentRelation commonComponentRelationOfDb : commonComponentRelationsOfDb) {
                    commonComponentRelationMapOfDb.put(commonComponentRelationOfDb.getId(), commonComponentRelationOfDb);
                }
            }
            for (Map<String, String> commonComponentRelationOfConfig : commonComponentRelationsOfConfig) {
                if (!commonComponentRelationMapOfDb.containsKey(commonComponentRelationOfConfig.get("id"))) {
                    DatabaseHandlerDao.getInstance().executeSql(
                            "insert into t_xtpz_commom_comp_relation(id,component_version_id,common_comp_verion_id) values('"
                                    + commonComponentRelationOfConfig.get("id") + "','" + componentVersionId + "','"
                                    + commonComponentRelationOfConfig.get("commonComponentVersionId") + "')");
                }
            }
        }
    }
}

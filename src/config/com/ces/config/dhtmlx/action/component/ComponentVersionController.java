package com.ces.config.dhtmlx.action.component;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.multipart.MultiPartRequestWrapper;
import org.apache.struts2.rest.DefaultHttpHeaders;
import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ces.config.dhtmlx.action.base.ConfigDefineServiceDaoController;
import com.ces.config.dhtmlx.dao.component.ComponentVersionDao;
import com.ces.config.dhtmlx.entity.appmanage.WorkflowDefine;
import com.ces.config.dhtmlx.entity.component.Component;
import com.ces.config.dhtmlx.entity.component.ComponentArea;
import com.ces.config.dhtmlx.entity.component.ComponentConfig;
import com.ces.config.dhtmlx.entity.component.ComponentTable;
import com.ces.config.dhtmlx.entity.component.ComponentVersion;
import com.ces.config.dhtmlx.entity.construct.Construct;
import com.ces.config.dhtmlx.entity.construct.ConstructDetail;
import com.ces.config.dhtmlx.entity.menu.Menu;
import com.ces.config.dhtmlx.json.entity.common.DhtmlxTreeNode;
import com.ces.config.dhtmlx.service.component.CommonComponentRelationService;
import com.ces.config.dhtmlx.service.component.ComponentAreaService;
import com.ces.config.dhtmlx.service.component.ComponentService;
import com.ces.config.dhtmlx.service.component.ComponentVersionService;
import com.ces.config.dhtmlx.service.construct.ConstructDetailService;
import com.ces.config.dhtmlx.service.construct.ConstructService;
import com.ces.config.dhtmlx.service.menu.MenuService;
import com.ces.config.utils.CfgCommonUtil;
import com.ces.config.utils.ComponentFileUtil;
import com.ces.config.utils.ComponentInfoUtil;
import com.ces.config.utils.ComponentParamsUtil;
import com.ces.config.utils.ConstantVar;
import com.ces.config.utils.FileUtil;
import com.ces.config.utils.PreviewUtil;
import com.ces.config.utils.StringUtil;
import com.ces.config.utils.ZipUtil;
import com.ces.xarch.core.exception.FatalException;
import com.google.common.collect.Lists;

/**
 * 构件版本Controller
 * 
 * @author wanglei
 * @date 2013-07-22
 */
public class ComponentVersionController extends ConfigDefineServiceDaoController<ComponentVersion, ComponentVersionService, ComponentVersionDao> {

    private static final long serialVersionUID = 6231754177803672231L;

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.web.struts2.BaseController#initModel()
     */
    @Override
    protected void initModel() {
        setModel(new ComponentVersion());
    }

    /*
     * (非 Javadoc) <p>标题: setService</p> <p>描述: 注入自定义服务层SERVICE</p> @param service
     * @see com.ces.config.dhtmlx.action.base.StringIDDhtmlxConfigController#setService(com.ces.xarch.core.service.
     * StringIDService)
     */
    @Autowired
    @Qualifier("componentVersionService")
    @Override
    protected void setService(ComponentVersionService service) {
        super.setService(service);
    }

    /**
     * 导入构件
     * 
     * @return Object
     */
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
            fos = new FileOutputStream(ComponentFileUtil.getTempCompPath() + fileName);
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
        String tempPath = ComponentFileUtil.getTempCompUncompressPath() + fileName.substring(0, fileName.lastIndexOf("."));
        try {
            ZipUtil.unzipFile(new File(ComponentFileUtil.getTempCompPath() + fileName), tempPath);
        } catch (Exception e1) {
            uploadError("解析构件包错误！", fileName, tempPath);
            return new DefaultHttpHeaders(SUCCESS).disableCaching();
        }

        // 3、解析component-config.xml文件
        File configFile = new File(tempPath + "/component-config.xml");
        ComponentConfig componentConfig = null;
        try {
            componentConfig = ComponentFileUtil.parseConfigFile(configFile);
        } catch (DocumentException e) {
            uploadError("解析component-config.xml错误！", fileName, tempPath);
            return new DefaultHttpHeaders(SUCCESS).disableCaching();
        }
        componentConfig.setPackageFileName(fileName);

        // 4、初步校验构件包
        String message = validateComponent(componentConfig);
        if (StringUtil.isNotEmpty(message)) {
            uploadError(message, fileName, tempPath);
            return new DefaultHttpHeaders(SUCCESS).disableCaching();
        }
        if (ConstantVar.Component.Type.ASSEMBLY.equals(componentConfig.getType())) {
            uploadError("组合构件应该在构件组装模块导入！", fileName, tempPath);
            return new DefaultHttpHeaders(SUCCESS).disableCaching();
        }
        // 5、如果导入的是自定义构件，解析配置信息
        if (ConstantVar.Component.Type.SELF_DEFINE.indexOf(componentConfig.getType()) != -1) {
            // ComponentVersion componentVersion = getService().getByComponentNameAndVersion(componentConfig.getName(),
            // componentConfig.getVersion());
            // if (componentVersion != null) {
            // uploadError("该自定义构件已经存在，不用覆盖！", fileName, tempPath);
            // return new DefaultHttpHeaders(SUCCESS).disableCaching();
            // }
            File selfDefineConfigFile = new File(tempPath + "/data/config.json");
            Map<String, Object> selfDefineConfig = null;
            try {
                selfDefineConfig = ComponentFileUtil.parseSelfDefineConfig(selfDefineConfigFile, false);
            } catch (Exception e) {
                e.printStackTrace();
                uploadError("解析自定义构件配置文件config.json错误！", fileName, tempPath);
                return new DefaultHttpHeaders(SUCCESS).disableCaching();
            }
            componentConfig.setSelfDefineConfig(selfDefineConfig);
        }

        String componentConfigKey = request.getParameter("componentConfigKey");
        request.getSession().setAttribute(componentConfigKey, componentConfig);

        // dhtmlxVault上传完成时需要
        String fileId = request.getParameter("sessionId").toString().trim();
        request.getSession().setAttribute("FileUpload.Progress." + fileId, "-1");
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 上传构件有问题时的处理
     * 
     * @param message 错误消息
     * @param newFileName 构件文件路径
     * @param tempPath 构件解包文件目录
     */
    private void uploadError(String message, String newFileName, String tempPath) {
        HttpServletRequest request = ServletActionContext.getRequest();
        deleteComponentFile(ComponentFileUtil.getTempCompPath() + newFileName, tempPath);
        request.getSession().setAttribute("message", message);
        // dhtmlxVault上传完成时需要
        String fileId = request.getParameter("sessionId").toString().trim();
        request.getSession().setAttribute("FileUpload.Progress." + fileId, "-1");
    }

    /**
     * 校验构件信息
     * 
     * @param componentConfig 构件配置
     * @return String 校验结果
     */
    private String validateComponent(ComponentConfig componentConfig) {
        StringBuffer message = new StringBuffer();
        if (StringUtil.isEmpty(componentConfig.getCode())) {
            message.append("code不能为空，");
        }
        if (StringUtil.isEmpty(componentConfig.getName())) {
            message.append("name不能为空，");
        }
        if (StringUtil.isEmpty(componentConfig.getAlias())) {
            message.append("alias不能为空，");
        }
        if (StringUtil.isEmpty(componentConfig.getVersion())) {
            message.append("version不能为空，");
        }
        if (StringUtil.isEmpty(componentConfig.getType())) {
            message.append("type不能为空，");
        }
        if (ConstantVar.Component.Type.PAGE.equals(componentConfig.getType()) && StringUtil.isEmpty(componentConfig.getViews())) {
            message.append("views不能为空，");
        }
        if (!ConstantVar.Component.Type.COMMON.equals(componentConfig.getType())
                && !ConstantVar.Component.Type.TRANSFER_DEVICE.equals(componentConfig.getType()) && StringUtil.isEmpty(componentConfig.getUrl())) {
            message.append("url不能为空，");
        }
        Component component = getService(ComponentService.class).getComponentByName(componentConfig.getName());
        if (component != null) {
            /*
             * if (!component.getAlias().equals(componentConfig.getAlias())) {
             * message.append("构件显示名称错误，");
             * }
             */
            if (!component.getType().equals(componentConfig.getType())) {
                message.append("构件类型错误，");
            }
        }
        if (message.length() > 0) {
            message.deleteCharAt(message.length() - 1);
        }
        return message.toString();
    }

    /**
     * 上传的构件校验失败
     * 
     * @return Object
     */
    public Object validateFailure() {
        HttpServletRequest request = ServletActionContext.getRequest();
        String componentConfigKey = request.getParameter("componentConfigKey");
        ComponentConfig componentConfig = (ComponentConfig) request.getSession().getAttribute(componentConfigKey);

        if (componentConfig == null) {
            setReturnData("上传失败");
        } else {
            String packageName = componentConfig.getPackageFileName().substring(0, componentConfig.getPackageFileName().lastIndexOf("."));
            // 删除临时文件
            deleteComponentFile(ComponentFileUtil.getTempCompPath() + componentConfig.getPackageFileName(), ComponentFileUtil.getTempCompUncompressPath()
                    + packageName);
            request.getSession().removeAttribute(componentConfigKey);
        }
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 保存构件版本
     * 
     * @return Object
     */
    public Object saveComponentVersion() {
        HttpServletRequest request = ServletActionContext.getRequest();
        String componentConfigKey = request.getParameter("componentConfigKey");
        ComponentConfig componentConfig = (ComponentConfig) request.getSession().getAttribute(componentConfigKey);
        if (componentConfig == null) {
            setReturnData("{'success':false,'message':'上传失败！'}");
        } else {
            // 1、保存构件及相关信息
            ComponentVersion componentVersion = null;
            try {
                componentVersion = getService().saveComponentConfig(componentConfig, true);
            } catch (Exception e) {
                e.printStackTrace();
                setReturnData("{'success':false,'message':'保存构件失败！'}");
                request.getSession().removeAttribute(componentConfigKey);
                return new DefaultHttpHeaders(SUCCESS).disableCaching();
            }
            // 2、将构件相关文件拷贝到正式存储目录下
            String packageName = componentConfig.getPackageFileName().substring(0, componentConfig.getPackageFileName().lastIndexOf("."));
            FileUtil.copyFile(ComponentFileUtil.getTempCompPath() + componentConfig.getPackageFileName(),
                    ComponentFileUtil.getCompPath() + componentConfig.getPackageFileName());
            FileUtil.copyFolder(ComponentFileUtil.getTempCompUncompressPath() + packageName, ComponentFileUtil.getCompUncompressPath() + packageName);
            // 3、删除临时文件
            deleteComponentFile(ComponentFileUtil.getTempCompPath() + componentConfig.getPackageFileName(), ComponentFileUtil.getTempCompUncompressPath()
                    + packageName);
            setReturnData("{'success':true,'systemParamConfig':'" + componentVersion.getSystemParamConfig() + "','id':'" + componentVersion.getId()
                    + "','type':'" + componentConfig.getType() + "'}");
            // 4、 如果是自定义构件，将构件文件拷贝到系统中
            if (ConstantVar.Component.Type.SELF_DEFINE.indexOf(componentConfig.getType()) != -1) {
                String projectPath = ComponentFileUtil.getProjectPath();
                PreviewUtil.copyComponentFile(componentVersion, projectPath);
            }
            // 5、创建表
            ComponentFileUtil.createComponentTable(componentVersion);
            ComponentFileUtil.execComponentDataSql(componentVersion);
            // 6、更新打包时间
            componentVersion.setPackageTime(StringUtil.null2empty(componentConfig.getPackageTime()));
            getService().save(componentVersion);
        }
        request.getSession().removeAttribute(componentConfigKey);
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 删除解压的临时文件和上传的文件
     * 
     * @param newFileName 构件包名次
     * @param tempPath 构件解压的临时目录
     */
    private void deleteComponentFile(String packagePath, String uncompressPath) {
        FileUtil.deleteFile(packagePath);
        FileUtil.deleteFile(uncompressPath);
    }

    /**
     * 获取页面构件、自定义构件和组合构件
     * 
     * @return Object
     */
    public Object getPageComponentVersionList() {
        String isSystemUsed = getParameter("isSystemUsed");
        list = getDataModel(getModelTemplate());
        processFilter(list);
        if (ConstantVar.Component.SystemUsed.YES.equals(isSystemUsed)) {
            list.setData(getService().getPageCVInfoList(isSystemUsed));
        } else {
            list.setData(getService().getPageCVInfoList());
        }
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 更改构件分类
     * 
     * @return Object
     */
    public Object changeArea() {
        String componentVersionIds = getParameter("componentVersionIds");
        String areaId = getParameter("areaId");
        String[] versionIds = componentVersionIds.split(",");
        getService().changeArea(areaId, versionIds);
        setReturnData("更改构件分类成功！");
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 更改组合构件分类
     * 
     * @return Object
     */
    public Object changeAssembleArea() {
        String componentVersionIds = getParameter("componentVersionIds");
        String assembleAreaId = getParameter("assembleAreaId");
        String[] versionIds = componentVersionIds.split(",");
        getService().changeAssembleArea(assembleAreaId, versionIds);
        setReturnData("更改构件分类成功！");
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 打包自定义构件版本
     * 
     * @return Object
     */
    public Object packageComponent() {
        try {
            ComponentFileUtil.packageComponent(getParameter("id"), false);
            setReturnData("打包成功！");
        } catch (Exception e) {
            e.printStackTrace();
            setReturnData("打包失败！");
        }
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 下载自定义构件版本
     * 
     * @return Object
     */
    public Object downloadComponent() {
        HttpServletRequest request = ServletActionContext.getRequest();
        String componentVersionId = getParameter("id");
        ComponentVersion componentVersion = getService().getByID(componentVersionId);
        String componentAlias = componentVersion.getComponent().getAlias();
        File componentZip = new File(ComponentFileUtil.getCompPath() + componentVersion.getPath());
        String fileName = "";
        try {
            if (request.getHeader("User-Agent").toLowerCase().indexOf("firefox") > 0 || request.getHeader("User-Agent").toLowerCase().indexOf("chrome") > 0) {
                fileName = new String(componentAlias.getBytes("UTF-8"), "ISO8859-1");
            } else {
                fileName = java.net.URLEncoder.encode(componentAlias, "UTF-8");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        HttpServletResponse response = ServletActionContext.getResponse();
        response.reset();
        response.setContentType("application/x-msdownload");
        response.setContentLength((int) componentZip.length());
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + "_" + componentVersion.getVersion() + ".zip");
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        OutputStream os = null;
        try {
            fis = new FileInputStream(componentZip);
            bis = new BufferedInputStream(fis);
            os = response.getOutputStream();
            byte[] b = new byte[1024];
            int len;
            while ((len = bis.read(b, 0, 1024)) > 0) {
                os.write(b, 0, len);
            }
            os.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return NONE;
    }

    /**
     * 判断该构件版本是否可以删除
     * 
     * @return Object
     */
    public Object deleteValid() {
        String componentVersionId = getParameter("componentVersionId");
        boolean flag = true;
        StringBuffer msg = new StringBuffer();
        msg.append("该构件不能删除，因为该构件");
        // 绑定了菜单，不能删除
        List<Menu> menuList = getService(MenuService.class).getByComponentVersionId(componentVersionId);
        if (CollectionUtils.isNotEmpty(menuList)) {
            flag = false;
            msg.append("绑定了菜单");
            for (Menu menu : menuList) {
                msg.append(menu.getName());
                msg.append("、");
            }
            msg.deleteCharAt(msg.length() - 1);
            msg.append("；");
        }
        // 是某组合构件的基础构件，不能删除
        List<Construct> constructList = getService(ConstructService.class).getByBaseComponentVersionId(componentVersionId);
        if (CollectionUtils.isNotEmpty(constructList)) {
            flag = false;
            msg.append("组装了构件");
            for (Construct construct : constructList) {
                msg.append(construct.getAssembleComponentVersion().getComponent().getName() + "_" + construct.getAssembleComponentVersion().getVersion());
                msg.append("、");
            }
            msg.deleteCharAt(msg.length() - 1);
            msg.append("；");
        }
        // 绑定了某组合构件中的预留区，不能删除
        List<ConstructDetail> constructDetailList = getService(ConstructDetailService.class).getByComponentVersionId(componentVersionId);
        if (CollectionUtils.isNotEmpty(constructDetailList)) {
            flag = false;
            msg.append("绑定了构件");
            for (ConstructDetail ConstructDetail : constructDetailList) {
                Construct construct = getService(ConstructService.class).getByID(ConstructDetail.getConstructId());
                msg.append(construct.getAssembleComponentVersion().getComponent().getName() + "_" + construct.getAssembleComponentVersion().getVersion());
                msg.append("、");
            }
            msg.deleteCharAt(msg.length() - 1);
            msg.append("中的按钮；");
        }
        setReturnData("{'success':" + flag + ", 'message':'" + msg + "'}");
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 删除构件版本
     * 
     * @return Object
     */
    public Object deleteComponentVersion() {
        String componentVersionId = getParameter("componentVersionId");
        getService().deleteComponentVersion(componentVersionId, false);
        setReturnData("删除成功！");
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 自定义构件预览预操作
     * 
     * @return Object
     */
    public Object previewSelfDefine() {
        String componentVersionId = getParameter("componentVersionId");
        HttpServletRequest request = ServletActionContext.getRequest();
        ComponentVersion componentVersion = getService().getByID(componentVersionId);
        // 依赖的公用构件
        Set<ComponentVersion> componentVersionSet = getService(CommonComponentRelationService.class).getAllCommonComponentVersion(componentVersionId);
        if (componentVersionSet.size() > 0) {
            boolean flag = true;
            File componentDir = new File(ComponentFileUtil.getProjectPath() + "cfg-resource/" + componentVersion.getViews() + "/views/selfdefine/"
                    + componentVersion.getComponent().getName().toLowerCase());
            if (!componentDir.exists()) {
                flag = false;
                setReturnData("{'status':false,'message':'该构件的文件在本系统中不存在，请检查！'}");
            }
            if (flag) {
                for (ComponentVersion cv : componentVersionSet) {
                    componentDir = new File(ComponentFileUtil.getCompPath() + cv.getPath());
                    if (!componentDir.exists()) {
                        flag = false;
                        setReturnData("{'status':false,'message':'关联的公用构件的zip包在构件库中不存在，请检查！'}");
                        break;
                    }
                }
            }
            componentVersionSet.add(componentVersion);
            if (flag) {
                PreviewUtil.previewComponents(componentVersionSet);
                boolean reloadStatus = PreviewUtil.restartPreviewProject();
                if (reloadStatus) {
                    // 获取构件的预览路径
                    String previewUrl = ComponentFileUtil.getPreviewUrl() + "cfg-resource/" + componentVersion.getViews() + "/views/"
                            + componentVersion.getUrl();
                    if (previewUrl.indexOf("?") == -1) {
                        previewUrl += "?componentVersionId=" + componentVersionId;
                    } else {
                        previewUrl += "&componentVersionId=" + componentVersionId;
                    }
                    setReturnData("{'status':true,'url':'" + previewUrl + "'}");
                } else {
                    setReturnData("{'status':false,'message':'预览系统未启动或重启失败！'}");
                }
            }
        } else {
            boolean flag = true;
            File componentDir = new File(ComponentFileUtil.getProjectPath() + "cfg-resource/" + componentVersion.getViews() + "/views/selfdefine/"
                    + componentVersion.getComponent().getName().toLowerCase());
            if (!componentDir.exists()) {
                flag = false;
                setReturnData("{'status':false,'message':'该构件的文件在本系统中不存在，请检查！'}");
            }
            if (flag) {
                String previewUrl = request.getContextPath() + "/cfg-resource/" + componentVersion.getViews() + "/views/" + componentVersion.getUrl();
                if (previewUrl.indexOf("?") == -1) {
                    previewUrl += "?componentVersionId=" + componentVersionId;
                } else {
                    previewUrl += "&componentVersionId=" + componentVersionId;
                }
                setReturnData("{'status':true,'url':'" + previewUrl + "'}");
            }
        }
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 页面构件预览预操作
     * 
     * @return Object
     */
    public Object preview() {
        HttpServletRequest request = ServletActionContext.getRequest();
        String componentVersionId = getParameter("componentVersionId");
        ComponentVersion componentVersion = getService().getByID(componentVersionId);
        if (CfgCommonUtil.isReleasedSystem() && StringUtil.isBooleanTrue(componentVersion.getIsSystemUsed())) {
            boolean flag = true;
            File componentDir = new File(ComponentFileUtil.getProjectPath() + "cfg-resource/" + componentVersion.getViews() + "/views/component/"
                    + componentVersion.getComponent().getName().toLowerCase());
            if (!componentDir.exists()) {
                flag = false;
                setReturnData("{'status':false,'message':'该构件的文件在本系统中不存在，请检查！'}");
            }
            if (flag) {
                String previewUrl = request.getContextPath() + "/cfg-resource/" + componentVersion.getViews() + "/views/" + componentVersion.getUrl();
                if (previewUrl.indexOf("?") == -1) {
                    previewUrl += "?componentVersionId=" + componentVersionId;
                } else {
                    previewUrl += "&componentVersionId=" + componentVersionId;
                }
                String paramsOfComponent = ComponentParamsUtil.getParamsOfComponent(null, componentVersion);
                if (StringUtil.isNotEmpty(paramsOfComponent)) {
                    if (previewUrl.indexOf("?") == -1) {
                        previewUrl += "?" + paramsOfComponent.substring(1);
                    } else {
                        previewUrl += paramsOfComponent;
                    }
                }
                setReturnData("{'status':true,'url':'" + previewUrl + "'}");
            }
        } else {
            Set<ComponentVersion> componentVersionSet = new HashSet<ComponentVersion>();
            componentVersionSet.add(componentVersion);
            // 依赖的公用构件
            componentVersionSet.addAll(getService(CommonComponentRelationService.class).getAllCommonComponentVersion(componentVersionId));
            boolean flag = true;
            for (ComponentVersion cv : componentVersionSet) {
                File componentDir = new File(ComponentFileUtil.getCompPath() + cv.getPath());
                if (!componentDir.exists()) {
                    flag = false;
                    setReturnData("{'status':false,'message':'该构件的文件在本系统中不存在，请检查！'}");
                    break;
                }
            }
            if (flag) {
                PreviewUtil.previewComponents(componentVersionSet);
                boolean reloadStatus = PreviewUtil.restartPreviewProject();
                if (reloadStatus) {
                    // 获取构件的预览路径
                    String previewUrl = ComponentFileUtil.getPreviewUrl() + "cfg-resource/" + componentVersion.getViews() + "/views/"
                            + componentVersion.getUrl();
                    if (previewUrl.indexOf("?") == -1) {
                        previewUrl += "?componentVersionId=" + componentVersionId;
                    } else {
                        previewUrl += "&componentVersionId=" + componentVersionId;
                    }
                    String paramsOfComponent = ComponentParamsUtil.getParamsOfComponent(null, componentVersion);
                    if (StringUtil.isNotEmpty(paramsOfComponent)) {
                        if (previewUrl.indexOf("?") == -1) {
                            previewUrl += "?" + paramsOfComponent.substring(1);
                        } else {
                            previewUrl += paramsOfComponent;
                        }
                    }
                    setReturnData("{'status':true,'url':'" + previewUrl + "'}");
                } else {
                    setReturnData("{'status':false,'message':'预览系统未启动或重启失败！'}");
                }
            }
        }
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 页面构件重复预览操作
     * 
     * @return Object
     */
    public Object repeatPreview() {
        String componentVersionId = getParameter("componentVersionId");
        ComponentVersion componentVersion = getService().getByID(componentVersionId);
        boolean flag = true;
        if (ConstantVar.Component.Type.SELF_DEFINE.indexOf(componentVersion.getComponent().getType()) != -1) {
            File componentDir = new File(ComponentFileUtil.getProjectPath() + "cfg-resource/" + componentVersion.getViews() + "/views/selfdefine/"
                    + componentVersion.getComponent().getName().toLowerCase());
            if (!componentDir.exists()) {
                flag = false;
                setReturnData("{'status':false,'message':'该构件的文件在本系统中不存在，请检查！'}");
            }
        } else if (ConstantVar.Component.Type.PAGE.equals(componentVersion.getComponent().getType())) {
            File componentDir = new File(ComponentFileUtil.getProjectPath() + "cfg-resource/" + componentVersion.getViews() + "/views/component/"
                    + componentVersion.getComponent().getName().toLowerCase());
            if (!componentDir.exists()) {
                flag = false;
                setReturnData("{'status':false,'message':'该构件的文件在本系统中不存在，请检查！'}");
            }
        }
        if (flag) {
            // 查找构件，获取构件的预览路径
            String previewUrl = ComponentFileUtil.getPreviewUrl() + "cfg-resource/" + componentVersion.getViews() + "/views/" + componentVersion.getUrl();
            if (previewUrl.indexOf("?") == -1) {
                previewUrl += "?componentVersionId=" + componentVersionId;
            } else {
                previewUrl += "&componentVersionId=" + componentVersionId;
            }
            String paramsOfComponent = ComponentParamsUtil.getParamsOfComponent(null, componentVersion);
            if (StringUtil.isNotEmpty(paramsOfComponent)) {
                if (previewUrl.indexOf("?") == -1) {
                    previewUrl += "?" + paramsOfComponent.substring(1);
                } else {
                    previewUrl += paramsOfComponent;
                }
            }
            setReturnData("{'status':true,'url':'" + previewUrl + "'}");
        }
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 预览配置文件
     * 
     * @return Object
     */
    public Object previewConfigFile() {
        String componentVersionId = getParameter("componentVersionId");
        ComponentVersion componentVersion = getService().getByID(componentVersionId);
        String configFilePath = ComponentFileUtil.getCompUncompressPath()
                + componentVersion.getPath().substring(0, componentVersion.getPath().lastIndexOf(".")) + "/component-config.xml";
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-type", "text/html;charset=UTF-8");
        BufferedReader br = null;
        PrintWriter writer = null;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(configFilePath), "UTF-8"));
            writer = response.getWriter();
            String line;
            while ((line = br.readLine()) != null) {
                writer.write(line.replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\t", "&emsp;") + "<br/>");
            }
            writer.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (writer != null) {
                writer.close();
            }
        }
        return NONE;
    }

    /**
     * 获取菜单树，菜单下使用过该构件的选中
     * 
     * @return Object
     */
    public Object getMenuTree() {
        String componentVersionId = getParameter("componentVersionId");
        ComponentVersion componentVersion = getService().getByID(componentVersionId);
        Set<Menu> menuSet = new HashSet<Menu>();
        if (componentVersion != null) {
            menuSet.addAll(getService(MenuService.class).getByComponentVersionId(componentVersionId));
            Set<ComponentVersion> assembleComponentVersionSet = getService(ConstructService.class).getAllAssembleComponentVersion(componentVersionId);
            if (CollectionUtils.isNotEmpty(assembleComponentVersionSet)) {
                for (ComponentVersion assembleComponentVersion : assembleComponentVersionSet) {
                    menuSet.addAll(getService(MenuService.class).getByComponentVersionId(assembleComponentVersion.getId()));
                }
            }
        }
        String jsonStr = "{id:'0', item:[{id:'1', text:'菜单', open:'1'" + getChildMenuTreeJson("-1", menuSet) + "}]}";
        setReturnData(jsonStr);
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 获取菜单绑定的构件树(菜单模块中使用)
     * 
     * @return Object
     */
    public Object getMenuComponentTree() {
        list = getDataModel(getModelTemplate());
        processFilter(list);
        boolean open = BooleanUtils.toBoolean(getParameter("P_OPEN"));
        String treeNodeId = getId();
        List<DhtmlxTreeNode> treeNodelist = Lists.newArrayList();
        DhtmlxTreeNode treeNode = null;
        if ("-1".equals(treeNodeId)) {
            List<ComponentArea> componentAreaList = getService(ComponentAreaService.class).getComponentAreaListByParentId(treeNodeId);
            if (CollectionUtils.isNotEmpty(componentAreaList)) {
                for (ComponentArea componentArea : componentAreaList) {
                    treeNode = new DhtmlxTreeNode();
                    treeNode.setId("CA_" + componentArea.getId());
                    treeNode.setText(componentArea.getName());
                    treeNode.setIm0(ConstantVar.IconTreeNode.LEAF);
                    treeNode.setIm1(ConstantVar.IconTreeNode.FOLDER_OPEN);
                    treeNode.setIm2(ConstantVar.IconTreeNode.FOLDER_CLOSE);
                    treeNode.setChild("1");
                    treeNode.setOpen(open);
                    treeNodelist.add(treeNode);
                }
            }
        } else {
            treeNodeId = getId().substring(3);
            List<ComponentArea> componentAreaList = getService(ComponentAreaService.class).getComponentAreaListByParentId(treeNodeId);
            if (CollectionUtils.isNotEmpty(componentAreaList)) {
                for (ComponentArea componentArea : componentAreaList) {
                    treeNode = new DhtmlxTreeNode();
                    treeNode.setId("CA_" + componentArea.getId());
                    treeNode.setText(componentArea.getName());
                    treeNode.setIm0(ConstantVar.IconTreeNode.LEAF);
                    treeNode.setIm1(ConstantVar.IconTreeNode.FOLDER_OPEN);
                    treeNode.setIm2(ConstantVar.IconTreeNode.FOLDER_CLOSE);
                    treeNode.setChild("1");
                    treeNode.setOpen(open);
                    treeNodelist.add(treeNode);
                }
            }
            List<ComponentVersion> componentVersionList = getService(ComponentVersionService.class).getComponentVersionListByAreaId(treeNodeId);
            if (CollectionUtils.isNotEmpty(componentVersionList)) {
                for (ComponentVersion tempCV : componentVersionList) {
                    if ((ConstantVar.Component.Type.PAGE.equals(tempCV.getComponent().getType()) || ConstantVar.Component.Type.ASSEMBLY.equals(tempCV
                            .getComponent().getType())) && "1".equals(tempCV.getMenuUse())) {
                        treeNode = new DhtmlxTreeNode();
                        treeNode.setId("CV_" + tempCV.getId());
                        treeNode.setText(tempCV.getComponent().getAlias() + "_" + tempCV.getVersion());
                        treeNode.setIm0(ConstantVar.IconTreeNode.LEAF);
                        treeNode.setIm1(ConstantVar.IconTreeNode.FOLDER_OPEN);
                        treeNode.setIm2(ConstantVar.IconTreeNode.FOLDER_CLOSE);
                        treeNode.setChild("0");
                        treeNode.setOpen(open);
                        treeNodelist.add(treeNode);
                    }
                }
            }
        }
        list.setData(treeNodelist);
        return new DefaultHttpHeaders("success").disableCaching();
    }

    /**
     * 获取菜单json
     * 
     * @param parentMenuId 父菜单ID
     * @param componentMenuSet 使用该构件的菜单
     * @return String
     */
    private String getChildMenuTreeJson(String parentMenuId, Set<Menu> componentMenuSet) {
        List<Menu> menuList = getService(MenuService.class).getMenuByParentId(parentMenuId);
        String jsonStr = "";
        if (CollectionUtils.isNotEmpty(menuList)) {
            jsonStr = ", item:[";
            for (Menu menu : menuList) {
                if (menu.getId().indexOf("sys_") == -1) {
                    jsonStr += "{id:'" + menu.getId() + "', text:'" + menu.getName() + "', open:'1'" + getChildMenuTreeJson(menu.getId(), componentMenuSet);
                    if (CollectionUtils.isNotEmpty(componentMenuSet)) {
                        for (Menu componentMenu : componentMenuSet) {
                            if (menu.getId().equals(componentMenu.getId())) {
                                jsonStr += ", checked:'1'";
                                break;
                            }
                        }
                    }
                    jsonStr += "},";
                }
            }
            jsonStr = jsonStr.substring(0, jsonStr.length() - 1);
            jsonStr += "]";
        }
        return jsonStr;
    }

    /**
     * 发布出的系统中，导入的构件应用到本系统
     * 
     * @return Object
     */
    public Object systemUse() {
        String componentVersionId = getParameter("componentVersionId");
        ComponentVersion componentVersion = getService().getByID(componentVersionId);
        if (StringUtil.isBooleanTrue(componentVersion.getIsSystemUsed())) {
            setReturnData("{'success':true,'message':'操作成功！'}");
        } else {
            String componentName = componentVersion.getComponent().getName();
            String projectPath = ComponentFileUtil.getProjectPath();
            File classDir = new File(projectPath + "WEB-INF/classes/com/ces/component/" + componentName);
            File componentViewDir = new File(projectPath + "cfg-resource/" + componentVersion.getViews() + "/views/component/" + componentName);
            File selfdefineViewDir = new File(projectPath + "cfg-resource/" + componentVersion.getViews() + "/views/selfdefine/" + componentName);
            if (classDir.exists() || componentViewDir.exists() || selfdefineViewDir.exists()) {
                setReturnData("{'success':false,'message':'系统中存在与该构件代码冲突的构件！'}");
            } else {
                // 创建表
                ComponentFileUtil.createComponentTable(componentVersion);
                ComponentFileUtil.execComponentDataSql(componentVersion);
                // 将构件文件拷贝到系统中
                PreviewUtil.copyComponentFile(componentVersion, projectPath);
                componentVersion.setIsSystemUsed(ConstantVar.Component.SystemUsed.YES);
                getService().save(componentVersion);
                setReturnData("{'success':true,'message':'操作成功，需要重启系统！'}");
            }
        }
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 获取上传构件的情况
     * 
     * @return Object
     */
    @SuppressWarnings("unchecked")
    public Object getUploadMessage() {
        HttpServletRequest request = ServletActionContext.getRequest();
        Object message = request.getSession().getAttribute("message");
        request.getSession().setAttribute("message", "");
        if (StringUtil.isNotEmpty(message)) {
            setReturnData("{'success':false, 'message':'" + message + "'}");
        } else {
            String componentConfigKey = request.getParameter("componentConfigKey");
            ComponentConfig componentConfig = (ComponentConfig) request.getSession().getAttribute(componentConfigKey);
            List<ComponentTable> componentTables = componentConfig.getComponentTables();
            // 是否需要校验构件相关表
            boolean validTables = false;
            // 是否是自定义的构件
            boolean isSelfDefineComponent = ConstantVar.Component.Type.SELF_DEFINE.indexOf(componentConfig.getType()) != -1;
            // 是否是公用构件
            boolean isCommonComponent = ConstantVar.Component.Type.COMMON.equals(componentConfig.getType());
            // 是否存在工作流
            boolean validWorkflow = false;
            // 公用构件和自定义构件只有V1.0版本
            boolean validVersion = false;
            if (isCommonComponent && !"V1.0".equals(componentConfig.getVersion())) {
                validVersion = true;
            }
            if (isSelfDefineComponent) {
                if (!"V1.0".equals(componentConfig.getVersion())) {
                    validVersion = true;
                }
                validTables = true;
                // 校验工作流
                Map<String, WorkflowDefine> workflowMap = (Map<String, WorkflowDefine>) componentConfig.getSelfDefineConfig().get("workflows");
                if (MapUtils.isNotEmpty(workflowMap)) {
                    validWorkflow = true;
                }
            } else {
                validTables = CollectionUtils.isNotEmpty(componentTables);
            }
            // 是否存在旧的构件版本
            boolean existOldComponentVersion = false;
            // 该构件是否已经被使用过了
            boolean componentVersionUsed = false;
            Component component = getService(ComponentService.class).getComponentByName(componentConfig.getName());
            if (component != null) {
                ComponentVersion oldComponentVersion = getService().getComponentVersionByComponentIdAndVersion(component.getId(), componentConfig.getVersion());
                if (oldComponentVersion != null) {
                    existOldComponentVersion = true;
                    // 绑定了菜单
                    List<Menu> menuList = getService(MenuService.class).getByComponentVersionId(oldComponentVersion.getId());
                    if (CollectionUtils.isNotEmpty(menuList)) {
                        componentVersionUsed = true;
                    }
                    if (!componentVersionUsed) {
                        // 是某组合构件的基础构件
                        List<Construct> constructList = getService(ConstructService.class).getByBaseComponentVersionId(oldComponentVersion.getId());
                        if (CollectionUtils.isNotEmpty(constructList)) {
                            componentVersionUsed = true;
                        }
                    }
                    if (!componentVersionUsed) {
                        // 绑定了某组合构件中的预留区
                        List<ConstructDetail> constructDetailList = getService(ConstructDetailService.class).getByComponentVersionId(
                                oldComponentVersion.getId());
                        if (CollectionUtils.isNotEmpty(constructDetailList)) {
                            componentVersionUsed = true;
                        }
                    }
                }
            }
            setReturnData("{'success':true, 'validVersion':" + validVersion + ", 'validTables':" + validTables + ", 'existOldComponentVersion':"
                    + existOldComponentVersion + ", 'componentVersionUsed':" + componentVersionUsed + ", 'isSelfDefineComponent':" + isSelfDefineComponent
                    + ", 'validWorkflow':" + validWorkflow + "}");
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
     * 获取构件关联的公用构件列表
     * 
     * @return Object
     */
    public Object getCommonComponentList() {
        String componentVersionId = getParameter("componentVersionId");
        list = getDataModel(getModelTemplate());
        processFilter(list);
        list.setData(getService().getCommonComponentList(componentVersionId));
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 获取组合构件的基础构件下拉框信息
     * 
     * @return Object
     */
    public Object getBaseComponentVersionCombo() {
        String isSystemUsed = getParameter("isSystemUsed");
        if (StringUtil.isEmpty(isSystemUsed)) {
            isSystemUsed = ConstantVar.Component.SystemUsed.NO;
        }
        setReturnData(getService().getBaseComponentVersionCombo(isSystemUsed));
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 获取组合构件中绑定按钮的构件下拉框信息
     * 
     * @return Object
     */
    public Object getBindingComponentVersionCombo() {
        String reserveZoneType = getParameter("reserveZoneType");
        String treeNodeType = getParameter("treeNodeType");
        String isSystemUsed = getParameter("isSystemUsed");
        if (StringUtil.isEmpty(isSystemUsed)) {
            isSystemUsed = ConstantVar.Component.SystemUsed.NO;
        }
        setReturnData(getService().getBindingComponentVersionCombo(reserveZoneType, treeNodeType, isSystemUsed));
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 删除构件关联的公用构件
     * 
     * @return Object
     */
    public Object deleteCommonComponent() {
        String componentVersionId = getParameter("componentVersionId");
        String commonComponentVersionIds = getParameter("commonComponentVersionIds");
        getService(CommonComponentRelationService.class).deleteCommonComponent(componentVersionId, commonComponentVersionIds);
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 校验添加的公用构件和原先构件绑定的公用构件是否有冲突
     * 
     * @return Object
     */
    public Object validateCommonComponentVersion() {
        String componentVersionId = getParameter("componentVersionId");
        String selectedComponentVersionIds = getParameter("commonComponentVersionIds");
        // 原先构件绑定的公用构件
        List<ComponentVersion> oldCommonComponentVersionList = getService().getCommonComponentList(componentVersionId);
        ComponentVersion componentVersion = getService().getByID(componentVersionId);
        // 如果构件本身也是公用构件，绑定的构件也不能与本构件冲突
        if (ConstantVar.Component.Type.COMMON.equals(componentVersion.getComponent().getType())) {
            oldCommonComponentVersionList.add(componentVersion);
        }
        boolean flag = true;
        StringBuilder failureComponentVersionId = new StringBuilder();
        if (CollectionUtils.isNotEmpty(oldCommonComponentVersionList)) {
            String[] selectedComponentVersionIdArray = selectedComponentVersionIds.split(",");
            List<ComponentVersion> selectedComponentVersionList = new ArrayList<ComponentVersion>();
            if (selectedComponentVersionIdArray.length > 0) {
                ComponentVersion selectedComponentVersion = null;
                for (String versionId : selectedComponentVersionIdArray) {
                    selectedComponentVersion = getService(ComponentVersionService.class).getByID(versionId);
                    if (selectedComponentVersion != null) {
                        selectedComponentVersionList.add(selectedComponentVersion);
                    }
                }
            }
            for (ComponentVersion oldCommonComponentVersion : oldCommonComponentVersionList) {
                for (ComponentVersion selectedComponentVersion : selectedComponentVersionList) {
                    if (oldCommonComponentVersion.getComponent().getId().equals(selectedComponentVersion.getComponent().getId())) {
                        flag = false;
                        failureComponentVersionId.append(componentVersion.getId() + ",");
                    }
                }
            }
        }
        if (!flag) {
            failureComponentVersionId.deleteCharAt(failureComponentVersionId.length() - 1);
        }
        setReturnData("{'success':" + flag + ",'failureComponentVersionId':'" + failureComponentVersionId + "'}");
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 添加公用构件
     * 
     * @return Object
     */
    public Object saveCommonComponentVersion() {
        String componentVersionId = getParameter("componentVersionId");
        String commonComponentVersionIds = getParameter("commonComponentVersionIds");
        getService(CommonComponentRelationService.class).saveCommonComponent(componentVersionId, commonComponentVersionIds);
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 扫描更新构件库
     * 
     * @return Object
     */
    public Object scanComponentLib() {
        File componentLib = new File(ComponentFileUtil.getCompPath());
        File[] componentZipFiles = componentLib.listFiles();
        int successCount = 0;
        int failureCount = 0;
        StringBuilder message = new StringBuilder();
        String msg = null;
        if (componentZipFiles != null && componentZipFiles.length > 0) {
            for (File componentZip : componentZipFiles) {
                String componentZipFileName = componentZip.getName();
                if (!".zip".equalsIgnoreCase(componentZipFileName.substring(componentZipFileName.lastIndexOf("."), componentZipFileName.length()))) {
                    continue;
                }
                String upcompressComponentPath = ComponentFileUtil.getCompUncompressPath()
                        + componentZipFileName.substring(0, componentZipFileName.lastIndexOf("."));
                File upcompressComponentFile = new File(upcompressComponentPath);
                if (!upcompressComponentFile.exists()) {
                    // 新增的构件
                    msg = uncompressComponent(componentZip);
                    if (msg != null) {
                        failureCount++;
                        message.append("<br/>" + componentZip.getName() + " " + msg);
                    } else {
                        successCount++;
                    }
                } else {
                    ComponentConfig zipComponentConfig = getComponentConfigFormZip(componentZip);
                    if (zipComponentConfig != null) {
                        // 自定义构件和组合构件不扫描
                        if (!ConstantVar.Component.Type.COMMON.equals(zipComponentConfig.getType())
                                && !ConstantVar.Component.Type.PAGE.equals(zipComponentConfig.getType())
                                && !ConstantVar.Component.Type.LOGIC.equals(zipComponentConfig.getType())) {
                            continue;
                        }
                        ComponentVersion componentVersion = getService().getByComponentNameAndVersion(zipComponentConfig.getName(),
                                zipComponentConfig.getVersion());
                        if (componentVersion != null) {
                            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            if (StringUtil.isNotEmpty(zipComponentConfig.getPackageTime())) {
                                if (StringUtil.isNotEmpty(componentVersion.getPackageTime())) {
                                    try {
                                        Date zipPackageTime = df.parse(zipComponentConfig.getPackageTime());
                                        Date dbPackageTime = df.parse(componentVersion.getPackageTime());
                                        if (zipPackageTime.getTime() > dbPackageTime.getTime()) {
                                            // 更新构件
                                            msg = uncompressComponent(componentZip);
                                            if (msg != null) {
                                                failureCount++;
                                                message.append("<br/>" + componentZip.getName() + " " + msg);
                                            } else {
                                                successCount++;
                                            }
                                        }
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                        failureCount++;
                                        message.append("<br/>" + componentZip.getName() + " " + "打包时间格式错误！");
                                    }
                                } else {
                                    // 数据库中该构件没有打包时间，说明数据库中的构件比较旧
                                    msg = uncompressComponent(componentZip);
                                    if (msg != null) {
                                        failureCount++;
                                        message.append("<br/>" + componentZip.getName() + " " + msg);
                                    } else {
                                        successCount++;
                                    }
                                }
                            }
                        } else {
                            // 数据库中该构件不存在
                            msg = uncompressComponent(componentZip);
                            if (msg != null) {
                                failureCount++;
                                message.append("<br/>" + componentZip.getName() + " " + msg);
                            } else {
                                successCount++;
                            }
                        }
                    } else {
                        failureCount++;
                        message.append("<br/>" + componentZip.getName() + " 解析构件包错误！");
                    }
                }
            }
        }
        setReturnData("更新成功" + successCount + "个构件，更新失败" + failureCount + "个构件！" + message.toString());
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 更新构件
     * 
     * @param componentZip 构件Zip包
     * @return String
     */
    private ComponentConfig getComponentConfigFormZip(File componentZip) {
        ComponentConfig componentConfig = null;
        InputStream is = null;
        try {
            ZipFile zipFile = new ZipFile(componentZip);
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            ZipEntry zipEntry = null;
            while (entries.hasMoreElements()) {
                zipEntry = entries.nextElement();
                if ("component-config.xml".equals(zipEntry.getName())) {
                    is = zipFile.getInputStream(zipEntry);
                    componentConfig = ComponentFileUtil.parseConfigFile(is);
                }
            }
        } catch (ZipException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return componentConfig;
    }

    /**
     * 更新构件
     * 
     * @param componentZip 构件Zip包
     * @return String
     */
    private String uncompressComponent(File componentZip) {
        String componentZipFileName = componentZip.getName();
        // 1、解包
        String upcompressComponentPath = ComponentFileUtil.getCompUncompressPath() + componentZipFileName.substring(0, componentZipFileName.lastIndexOf("."));
        File upcompressComponentFile = new File(upcompressComponentPath);
        if (upcompressComponentFile.exists()) {
            FileUtil.deleteFile(upcompressComponentPath);
        }
        try {
            ZipUtil.unzipFile(new File(ComponentFileUtil.getCompPath() + componentZipFileName), upcompressComponentPath);
        } catch (Exception e) {
            e.printStackTrace();
            return "解析构件包错误！";
        }

        // 2、解析component-config.xml文件
        File configFile = new File(upcompressComponentPath + "/component-config.xml");
        ComponentConfig componentConfig = null;
        try {
            componentConfig = ComponentFileUtil.parseConfigFile(configFile);
        } catch (DocumentException e) {
            e.printStackTrace();
            return "解析component-config.xml错误！";
        }
        componentConfig.setPackageFileName(componentZipFileName);

        // 3、初步校验构件包
        String message = validateComponent(componentConfig);
        if (StringUtil.isNotEmpty(message)) {
            return "component-config.xml配置错误！";
        }

        // 4、如果导入的是自定义构件，解析配置信息
        if ("3".equals(componentConfig.getType())) {
            ComponentVersion componentVersion = getService().getByComponentNameAndVersion(componentConfig.getName(), componentConfig.getVersion());
            if (componentVersion != null) {
                return "该自定义构件已经存在！";
            }
            File selfDefineConfigFile = new File(upcompressComponentPath + "/data/config.json");
            Map<String, Object> selfDefineConfig = null;
            try {
                selfDefineConfig = ComponentFileUtil.parseSelfDefineConfig(selfDefineConfigFile, false);
            } catch (Exception e) {
                e.printStackTrace();
                return "解析自定义构件配置文件config.json错误！";
            }
            componentConfig.setSelfDefineConfig(selfDefineConfig);
        }

        // 5、保存构件及相关信息
        ComponentVersion componentVersion = null;
        try {
            componentVersion = getService().saveComponentConfig(componentConfig, false);
            componentVersion.setIsPackage(ConstantVar.Component.Packaged.YES);
            componentVersion.setPackageTime(StringUtil.null2empty(componentConfig.getPackageTime()));
            getService().save(componentVersion);
        } catch (Exception e) {
            e.printStackTrace();
            return "保存构件失败！";
        }

        // 6、 如果是自定义构件，将构件文件拷贝到系统中
        if ("3".equals(componentConfig.getType())) {
            FileUtil.deleteFile(ComponentFileUtil.getProjectPath() + "cfg-resource/" + componentVersion.getViews() + "/views/selfdefine/"
                    + componentVersion.getComponent().getName().toLowerCase());
            FileUtil.deleteFile(ComponentFileUtil.getSrcPath() + "selfdefine/com/ces/component/" + componentVersion.getComponent().getName().toLowerCase());
            PreviewUtil.copyComponentFile(componentVersion, ComponentFileUtil.getProjectPath());
        }
        return null;
    }

    /**
     * 修改按钮项或菜单项
     * 
     * @return Object
     * @throws FatalException
     */
    public Object updateStatus() throws FatalException {
        boolean success = true;
        String message = "";
        try {
            String columnName = getParameter("columName");
            String value = getParameter("value");
            String id = getParameter("id");
            model = getService().getByID(id);
            if (model != null) {
                if ("buttonUse".equals(columnName)) {
                    if (ConstantVar.Component.Type.COMMON.equals(model.getComponent().getType())) {
                        success = false;
                        message = "公用构件不能作为按钮项！";
                    } else if (ConstantVar.Component.Type.TREE.equals(model.getComponent().getType())) {
                        success = false;
                        message = "树构件不能作为按钮项！";
                    } else if (ConstantVar.Component.Type.PHYSICAL_TABLE.equals(model.getComponent().getType())) {
                        success = false;
                        message = "物理表构件不能作为按钮项！";
                    } else if (ConstantVar.Component.Type.LOGIC_TABLE.equals(model.getComponent().getType())) {
                        success = false;
                        message = "逻辑表构件不能作为按钮项！";
                    } else if (ConstantVar.Component.Type.NO_TABLE.equals(model.getComponent().getType())) {
                        success = false;
                        message = "通用表构件不能作为按钮项！";
                    } else if (ConstantVar.Component.Type.TAB.equals(model.getComponent().getType())) {
                        success = false;
                        message = "标签页构件不能作为按钮项！";
                    } else if (ConstantVar.Component.Type.TRANSFER_DEVICE.equals(model.getComponent().getType())) {
                        success = false;
                        message = "中转器构件不能作为按钮项！";
                    } else {
                        model.setButtonUse(value);
                        getService().save(model);
                        ComponentInfoUtil.getInstance().putComponentVersion(model);
                    }
                } else if ("menuUse".equals(columnName)) {
                    if (ConstantVar.Component.Type.COMMON.equals(model.getComponent().getType())) {
                        success = false;
                        message = "公用构件不能作为菜单项！";
                    } else if (ConstantVar.Component.Type.LOGIC.equals(model.getComponent().getType())) {
                        success = false;
                        message = "逻辑表构件不能作为菜单项！";
                    } else if (ConstantVar.Component.Type.TREE.equals(model.getComponent().getType())) {
                        success = false;
                        message = "树构件不能作为菜单项！";
                    } else if (ConstantVar.Component.Type.PHYSICAL_TABLE.equals(model.getComponent().getType())) {
                        success = false;
                        message = "物理表构件不能作为菜单项！";
                    } else if (ConstantVar.Component.Type.LOGIC_TABLE.equals(model.getComponent().getType())) {
                        success = false;
                        message = "逻辑表构件不能作为菜单项！";
                    } else if (ConstantVar.Component.Type.NO_TABLE.equals(model.getComponent().getType())) {
                        success = false;
                        message = "通用表构件不能作为菜单项！";
                    } else if (ConstantVar.Component.Type.TAB.equals(model.getComponent().getType())) {
                        success = false;
                        message = "标签页构件不能作为菜单项！";
                    } else if (ConstantVar.Component.Type.TRANSFER_DEVICE.equals(model.getComponent().getType())) {
                        success = false;
                        message = "中转器构件不能作为菜单项！";
                    } else {
                        model.setMenuUse(value);
                        getService().save(model);
                        ComponentInfoUtil.getInstance().putComponentVersion(model);
                    }
                }
            }
        } catch (Exception e) {
            success = false;
            message = e.getMessage();
        }
        setReturnData("{'success':" + success + ",'message':'" + message + "'}");
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }
}

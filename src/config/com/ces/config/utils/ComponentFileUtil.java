package com.ces.config.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.dhtmlx.entity.appmanage.AppButton;
import com.ces.config.dhtmlx.entity.appmanage.AppColumn;
import com.ces.config.dhtmlx.entity.appmanage.AppDefine;
import com.ces.config.dhtmlx.entity.appmanage.AppForm;
import com.ces.config.dhtmlx.entity.appmanage.AppFormElement;
import com.ces.config.dhtmlx.entity.appmanage.AppGrid;
import com.ces.config.dhtmlx.entity.appmanage.AppReport;
import com.ces.config.dhtmlx.entity.appmanage.AppSearch;
import com.ces.config.dhtmlx.entity.appmanage.AppSearchPanel;
import com.ces.config.dhtmlx.entity.appmanage.AppSort;
import com.ces.config.dhtmlx.entity.appmanage.ColumnBusiness;
import com.ces.config.dhtmlx.entity.appmanage.ColumnDefine;
import com.ces.config.dhtmlx.entity.appmanage.ColumnRelation;
import com.ces.config.dhtmlx.entity.appmanage.ColumnSplice;
import com.ces.config.dhtmlx.entity.appmanage.ColumnSplit;
import com.ces.config.dhtmlx.entity.appmanage.LogicGroupDefine;
import com.ces.config.dhtmlx.entity.appmanage.LogicGroupRelation;
import com.ces.config.dhtmlx.entity.appmanage.LogicTableDefine;
import com.ces.config.dhtmlx.entity.appmanage.LogicTableRelation;
import com.ces.config.dhtmlx.entity.appmanage.Module;
import com.ces.config.dhtmlx.entity.appmanage.PhysicalGroupDefine;
import com.ces.config.dhtmlx.entity.appmanage.PhysicalGroupRelation;
import com.ces.config.dhtmlx.entity.appmanage.PhysicalTableDefine;
import com.ces.config.dhtmlx.entity.appmanage.TableTree;
import com.ces.config.dhtmlx.entity.appmanage.TreeDefine;
import com.ces.config.dhtmlx.entity.appmanage.WorkflowButtonSetting;
import com.ces.config.dhtmlx.entity.appmanage.WorkflowDefine;
import com.ces.config.dhtmlx.entity.appmanage.WorkflowFormSetting;
import com.ces.config.dhtmlx.entity.appmanage.WorkflowTree;
import com.ces.config.dhtmlx.entity.appmanage.WorkflowVersion;
import com.ces.config.dhtmlx.entity.component.CommonComponentRelation;
import com.ces.config.dhtmlx.entity.component.Component;
import com.ces.config.dhtmlx.entity.component.ComponentArea;
import com.ces.config.dhtmlx.entity.component.ComponentButton;
import com.ces.config.dhtmlx.entity.component.ComponentCallback;
import com.ces.config.dhtmlx.entity.component.ComponentCallbackParam;
import com.ces.config.dhtmlx.entity.component.ComponentClass;
import com.ces.config.dhtmlx.entity.component.ComponentColumn;
import com.ces.config.dhtmlx.entity.component.ComponentConfig;
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
import com.ces.config.dhtmlx.entity.construct.ConstructCallback;
import com.ces.config.dhtmlx.entity.construct.ConstructDetail;
import com.ces.config.dhtmlx.entity.construct.ConstructDetailSelfParam;
import com.ces.config.dhtmlx.entity.construct.ConstructFilter;
import com.ces.config.dhtmlx.entity.construct.ConstructFilterDetail;
import com.ces.config.dhtmlx.entity.construct.ConstructFunction;
import com.ces.config.dhtmlx.entity.construct.ConstructInputParam;
import com.ces.config.dhtmlx.entity.construct.ConstructSelfParam;
import com.ces.config.dhtmlx.service.appmanage.AppButtonService;
import com.ces.config.dhtmlx.service.appmanage.AppColumnService;
import com.ces.config.dhtmlx.service.appmanage.AppDefineService;
import com.ces.config.dhtmlx.service.appmanage.AppFormElementService;
import com.ces.config.dhtmlx.service.appmanage.AppFormService;
import com.ces.config.dhtmlx.service.appmanage.AppGridService;
import com.ces.config.dhtmlx.service.appmanage.AppReportService;
import com.ces.config.dhtmlx.service.appmanage.AppSearchPanelService;
import com.ces.config.dhtmlx.service.appmanage.AppSearchService;
import com.ces.config.dhtmlx.service.appmanage.AppSortService;
import com.ces.config.dhtmlx.service.appmanage.ColumnBusinessService;
import com.ces.config.dhtmlx.service.appmanage.ColumnDefineService;
import com.ces.config.dhtmlx.service.appmanage.ColumnOperationService;
import com.ces.config.dhtmlx.service.appmanage.ColumnRelationService;
import com.ces.config.dhtmlx.service.appmanage.ColumnSpliceService;
import com.ces.config.dhtmlx.service.appmanage.ColumnSplitService;
import com.ces.config.dhtmlx.service.appmanage.LogicGroupDefineService;
import com.ces.config.dhtmlx.service.appmanage.LogicGroupRelationService;
import com.ces.config.dhtmlx.service.appmanage.LogicTableDefineService;
import com.ces.config.dhtmlx.service.appmanage.LogicTableRelationService;
import com.ces.config.dhtmlx.service.appmanage.ModuleService;
import com.ces.config.dhtmlx.service.appmanage.PhysicalGroupDefineService;
import com.ces.config.dhtmlx.service.appmanage.PhysicalGroupRelationService;
import com.ces.config.dhtmlx.service.appmanage.PhysicalTableDefineService;
import com.ces.config.dhtmlx.service.appmanage.TableRelationService;
import com.ces.config.dhtmlx.service.appmanage.TableTreeService;
import com.ces.config.dhtmlx.service.appmanage.TreeDefineService;
import com.ces.config.dhtmlx.service.appmanage.WorkflowButtonSettingService;
import com.ces.config.dhtmlx.service.appmanage.WorkflowDefineService;
import com.ces.config.dhtmlx.service.appmanage.WorkflowFormSettingService;
import com.ces.config.dhtmlx.service.appmanage.WorkflowTreeService;
import com.ces.config.dhtmlx.service.appmanage.WorkflowVersionService;
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
import com.ces.config.dhtmlx.service.component.ComponentSystemParameterService;
import com.ces.config.dhtmlx.service.component.ComponentTableColumnRelationService;
import com.ces.config.dhtmlx.service.component.ComponentTableService;
import com.ces.config.dhtmlx.service.component.ComponentVersionService;
import com.ces.config.dhtmlx.service.construct.ConstructCallbackService;
import com.ces.config.dhtmlx.service.construct.ConstructDetailSelfParamService;
import com.ces.config.dhtmlx.service.construct.ConstructDetailService;
import com.ces.config.dhtmlx.service.construct.ConstructFilterDetailService;
import com.ces.config.dhtmlx.service.construct.ConstructFilterService;
import com.ces.config.dhtmlx.service.construct.ConstructFunctionService;
import com.ces.config.dhtmlx.service.construct.ConstructInputParamService;
import com.ces.config.dhtmlx.service.construct.ConstructSelfParamService;
import com.ces.config.dhtmlx.service.construct.ConstructService;
import com.ces.utils.BeanUtils;
import com.ces.xarch.core.web.listener.XarchListener;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ComponentFileUtil {

    private static Log log = LogFactory.getLog(ComponentFileUtil.class);

    private static Properties props = new Properties();

    /** * 当前工程路径,定位到WebRoot */
    private static String projectPath = "";

    /** * 当前工程源码路径 */
    private static String srcPath = "";

    /** * 预览的工程访问地址的IP */
    private static String previewIp = "";

    /** * 预览的工程访问地址的PORT */
    private static String previewPort = "";

    /** * 预览的工程的名称 */
    private static String previewProjectName = "";

    /**
     * 加载配置文件
     */
    private static void loadProperties() {
        try {
            props.load(new FileInputStream(getConfigPath() + "prop/component_configure.properties"));
            String componentLibPath = getProjectPath() + "components/";
            // 构件包临时文件路径
            props.put("temp_component_lib.path", componentLibPath + "temp_component_lib");
            // 构件包临时文件解压路径
            props.put("temp_component_lib.uncompressPath", componentLibPath + "temp_component_lib_uncompress");
            // 构件包文件路径
            props.put("component_lib.path", componentLibPath + "component_lib");
            // 构件包文件解压路径
            props.put("component_lib.uncompressPath", componentLibPath + "component_lib_uncompress");
        } catch (IOException e) {
            log.error("加载构件配置文件失败！", e);
        }
    }

    /**
     * 初始化构件库目录
     */
    public static void init() {
        loadProperties();
        try {
            File tempComponentLib = new File(props.getProperty("temp_component_lib.path"));
            if (!tempComponentLib.exists()) {
                tempComponentLib.mkdirs();
            }
            File tempComponentLibUncompress = new File(props.getProperty("temp_component_lib.uncompressPath"));
            if (!tempComponentLibUncompress.exists()) {
                tempComponentLibUncompress.mkdirs();
            }
            File componentLib = new File(props.getProperty("component_lib.path"));
            if (!componentLib.exists()) {
                componentLib.mkdirs();
            }
            File componentLibUncompress = new File(props.getProperty("component_lib.uncompressPath"));
            if (!componentLibUncompress.exists()) {
                componentLibUncompress.mkdirs();
            }
            File completeComponentLib = new File(props.getProperty("complete_component_lib.path"));
            if (!completeComponentLib.exists()) {
                completeComponentLib.mkdirs();
            }
            File completeComponentLibUncompress = new File(props.getProperty("complete_component_lib.uncompressPath"));
            if (!completeComponentLibUncompress.exists()) {
                completeComponentLibUncompress.mkdirs();
            }
            File releaseTempPath = new File(props.getProperty("release_temp_path"));
            if (!releaseTempPath.exists()) {
                releaseTempPath.mkdirs();
            }
            File updatePackagePath = new File(props.getProperty("update_package_path"));
            if (!updatePackagePath.exists()) {
                updatePackagePath.mkdirs();
            }
            File compressTempPath = new File(props.getProperty("component_compress_temp_path") + "/batch/input");
            if (!compressTempPath.exists()) {
                compressTempPath.mkdirs();
            }

            String previewUrl = getPreviewUrl();
            previewUrl = previewUrl.replaceAll("http://", "");
            previewIp = previewUrl.substring(0, previewUrl.indexOf(":"));
            previewUrl = previewUrl.substring(previewUrl.indexOf(":") + 1);
            previewPort = previewUrl.substring(0, previewUrl.indexOf("/"));
            previewUrl = previewUrl.substring(previewUrl.indexOf("/") + 1);
            previewProjectName = previewUrl.replaceAll("/", "");
        } catch (Exception e) {
            System.out.println("component_config.properties文件配置错误！");
            e.printStackTrace();
        }
    }

    /**
     * 获取配置文件的路径
     * 
     * @return String
     */
    public static String getConfigPath() {
        String configPath = getProjectPath() + "WEB-INF/conf/";
        return configPath;
    }

    /**
     * 设置工程路径
     * 
     * @return String
     */
    public static void setProjectPath(String path) {
        if (!path.endsWith("/")) {
            path += "/";
        }
        projectPath = path;
    }

    /**
     * 获取工程路径
     * 
     * @return String
     */
    public static String getProjectPath() {
        if (StringUtil.isEmpty(projectPath)) {
            projectPath = ServletActionContext.getServletContext().getRealPath("/").replaceAll("\\\\", "/");
            if (!projectPath.endsWith("/")) {
                projectPath += "/";
            }
        }
        return projectPath;
    }

    /**
     * 获取工程src路径
     * 
     * @return String
     */
    public static String getSrcPath() {
        if (StringUtil.isEmpty(srcPath)) {
            String projectPath = getProjectPath();
            srcPath = projectPath.substring(0, projectPath.length() - 1);
            srcPath = srcPath.substring(0, srcPath.lastIndexOf("/")) + "/src/";
        }
        return srcPath;
    }

    /**
     * 获取配置文件中的属性
     * 
     * @param key 名称
     * @return String 值
     */
    public static String getProperties(String key) {
        return props.getProperty(key);
    }

    /**
     * 获取构件包临时文件路径
     * 
     * @return String
     */
    public static String getTempCompPath() {
        String path = getProperties("temp_component_lib.path");
        path = path.replaceAll("\\\\", "/");
        if (!path.endsWith("/")) {
            path = path + "/";
        }
        return path;
    }

    /**
     * 获取构件包临时文件解压路径
     * 
     * @return String
     */
    public static String getTempCompUncompressPath() {
        String path = getProperties("temp_component_lib.uncompressPath");
        path = path.replaceAll("\\\\", "/");
        if (!path.endsWith("/")) {
            path = path + "/";
        }
        return path;
    }

    /**
     * 获取构件包文件路径
     * 
     * @return String
     */
    public static String getCompPath() {
        String path = getProperties("component_lib.path");
        path = path.replaceAll("\\\\", "/");
        if (!path.endsWith("/")) {
            path = path + "/";
        }
        return path;
    }

    /**
     * 获取构件包文件解压路径
     * 
     * @return String
     */
    public static String getCompUncompressPath() {
        String path = getProperties("component_lib.uncompressPath");
        path = path.replaceAll("\\\\", "/");
        if (!path.endsWith("/")) {
            path = path + "/";
        }
        return path;
    }

    /**
     * 获取构件库文件路径
     * 
     * @return String
     */
    public static String getCompleteCompPath() {
        String path = getProperties("complete_component_lib.path");
        path = path.replaceAll("\\\\", "/");
        if (!path.endsWith("/")) {
            path = path + "/";
        }
        return path;
    }

    /**
     * 获取构件库文件解压路径
     * 
     * @return String
     */
    public static String getCompleteCompUncompressPath() {
        String path = getProperties("complete_component_lib.uncompressPath");
        path = path.replaceAll("\\\\", "/");
        if (!path.endsWith("/")) {
            path = path + "/";
        }
        return path;
    }

    /**
     * 获取预览的工程路径
     * 
     * @return String
     */
    public static String getPreviewProject() {
        String path = getProperties("preview_project");
        path = path.replaceAll("\\\\", "/");
        if (!path.endsWith("/")) {
            path = path + "/";
        }
        return path;
    }

    /**
     * 获取预览的工程访问地址
     * 
     * @return String
     */
    public static String getPreviewUrl() {
        String path = getProperties("preview_url");
        path = path.replaceAll("\\\\", "/");
        if (!path.endsWith("/")) {
            path = path + "/";
        }
        return path;
    }

    /**
     * 获取预览的工程访问地址的IP
     * 
     * @return String
     */
    public static String getPreviewIp() {
        return previewIp;
    }

    /**
     * 获取预览的工程访问地址的PORT
     * 
     * @return String
     */
    public static String getPreviewPort() {
        return previewPort;
    }

    /**
     * 获取预览的工程名称
     * 
     * @return String
     */
    public static String getPreviewProjectName() {
        return previewProjectName;
    }

    /**
     * 获取预览的工程的Tomcat用户名
     * 
     * @return String
     */
    public static String getTomcatUserName() {
        return getProperties("tomcat_user_name");
    }

    /**
     * 获取预览的工程的Tomcat用户密码
     * 
     * @return String
     */
    public static String getTomcatUserPwd() {
        return getProperties("tomcat_user_pwd");
    }

    /**
     * 获取用于发布的临时文件路径
     * 
     * @return String
     */
    public static String getReleaseTempPath() {
        String path = getProperties("release_temp_path");
        path = path.replaceAll("\\\\", "/");
        if (!path.endsWith("/")) {
            path = path + "/";
        }
        return path;
    }

    /**
     * 获取用于更新包的临时文件路径
     * 
     * @return String
     */
    public static String getUpdatePackagePath() {
        String path = getProperties("update_package_path");
        path = path.replaceAll("\\\\", "/");
        if (!path.endsWith("/")) {
            path = path + "/";
        }
        return path;
    }

    /**
     * 用于构件打包的临时文件路径
     * 
     * @return String
     */
    public static String getCompressTempPath() {
        String path = getProperties("component_compress_temp_path");
        path = path.replaceAll("\\\\", "/");
        if (!path.endsWith("/")) {
            path = path + "/";
        }
        return path;
    }

    /**
     * 解析component-config.xml文件
     * 
     * @param configFile 配置文件
     * @return ComponentConfig
     * @throws DocumentException
     */
    public static ComponentConfig parseConfigFile(File configFile) throws DocumentException {
        ComponentConfig componentConfig = null;
        InputStream is = null;
        try {
            is = new FileInputStream(configFile);
            componentConfig = parseConfigFile(is);
        } catch (FileNotFoundException e) {
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
     * 解析component-config.xml文件
     * 
     * @param configFile 配置文件
     * @return ComponentConfig
     * @throws DocumentException
     */
    public static ComponentConfig parseConfigFile(InputStream configFile) throws DocumentException {
        ComponentConfig componentConfig = new ComponentConfig();
        SAXReader reader = new SAXReader();
        Document doc = reader.read(configFile);
        Element root = doc.getRootElement();
        componentConfig.setCode(root.elementTextTrim("code"));
        componentConfig.setName(root.elementTextTrim("name"));
        componentConfig.setAlias(root.elementTextTrim("alias"));
        componentConfig.setVersion(root.elementTextTrim("version"));
        componentConfig.setType(root.elementTextTrim("type"));
        componentConfig.setViews(root.elementTextTrim("views"));
        componentConfig.setUrl(root.elementTextTrim("url"));
        String beforeClickJs = root.elementText("before_click_js");
        if (StringUtil.isNotEmpty(beforeClickJs)) {
            beforeClickJs = beforeClickJs.trim();
        }
        componentConfig.setBeforeClickJs(beforeClickJs);
        // 解析系统参数节点
        parseSystemParametersElement(componentConfig, root);
        // 解析自身参数节点
        parseSelfParamsElement(componentConfig, root);
        // 解析入参节点
        parseInputParametersElement(componentConfig, root);
        // 解析出参节点
        parseOutputParametersElement(componentConfig, root);
        // 解析预留区节点
        parseReserveZonesElement(componentConfig, root);
        // 解析方法节点
        parseFunctionsElement(componentConfig, root);
        // 解析回调函数节点
        parseCallbacksElement(componentConfig, root);
        // 解析表节点
        parseTablesElement(componentConfig, root);
        // 解析构件按钮节点
        parseButtonElement(componentConfig, root);
        componentConfig.setPackageTime(root.elementTextTrim("package_time"));
        componentConfig.setRemark(root.elementTextTrim("remark"));
        return componentConfig;
    }

    /**
     * 解析系统参数节点
     * 
     * @param componentConfig 构件配置
     * @param root 根节点
     */
    @SuppressWarnings("rawtypes")
    private static void parseSystemParametersElement(ComponentConfig componentConfig, Element root) {
        Element systemParameters = root.element("system_parameters");
        if (systemParameters != null) {
            List<ComponentSystemParameter> componentSystemParameterList = new ArrayList<ComponentSystemParameter>();
            ComponentSystemParameter componentSystemParameter = null;
            Element parameter = null;
            for (Iterator parameterIterator = systemParameters.elementIterator("system_parameter"); parameterIterator.hasNext();) {
                parameter = (Element) parameterIterator.next();
                componentSystemParameter = new ComponentSystemParameter();
                componentSystemParameter.setName(parameter.elementTextTrim("name"));
                componentSystemParameter.setRemark(parameter.elementTextTrim("remark"));
                componentSystemParameterList.add(componentSystemParameter);
            }
            componentConfig.setComponentSystemParameters(componentSystemParameterList);
        }
    }

    /**
     * 解析自身参数节点
     * 
     * @param componentConfig 构件配置
     * @param root 根节点
     */
    @SuppressWarnings("rawtypes")
    private static void parseSelfParamsElement(ComponentConfig componentConfig, Element root) {
        Element selfParams = root.element("self_parameters");
        if (selfParams != null) {
            List<ComponentSelfParam> selfParamList = new ArrayList<ComponentSelfParam>();
            ComponentSelfParam componentSelfParam = null;
            Element selfParam = null;
            for (Iterator selfParamIterator = selfParams.elementIterator("self_parameter"); selfParamIterator.hasNext();) {
                selfParam = (Element) selfParamIterator.next();
                componentSelfParam = new ComponentSelfParam();
                componentSelfParam.setName(selfParam.elementTextTrim("name"));
                componentSelfParam.setText(selfParam.elementTextTrim("text"));
                componentSelfParam.setType(selfParam.elementTextTrim("type"));
                componentSelfParam.setValue(selfParam.elementTextTrim("value"));
                componentSelfParam.setRemark(selfParam.elementTextTrim("remark"));
                Element options = selfParam.element("options");
                if (options != null) {
                    StringBuffer optionSb = new StringBuffer();
                    optionSb.append("[{value:'',text:'请选择',selected:true}");
                    Element option = null;
                    for (Iterator optionIterator = options.elementIterator("option"); optionIterator.hasNext();) {
                        option = (Element) optionIterator.next();
                        optionSb.append(",{");
                        optionSb.append("value:'");
                        optionSb.append(option.elementTextTrim("value"));
                        optionSb.append("',text:'");
                        optionSb.append(option.elementTextTrim("text"));
                        optionSb.append("'}");
                    }
                    optionSb.append("]");
                    componentSelfParam.setOptions(optionSb.toString());
                }
                selfParamList.add(componentSelfParam);
            }
            componentConfig.setSelfParams(selfParamList);
        }
    }

    /**
     * 解析入参节点
     * 
     * @param componentConfig 构件配置
     * @param root 根节点
     */
    @SuppressWarnings("rawtypes")
    private static void parseInputParametersElement(ComponentConfig componentConfig, Element root) {
        Element inputParams = root.element("input_parameters");
        if (inputParams != null) {
            List<ComponentInputParam> inputParamList = new ArrayList<ComponentInputParam>();
            ComponentInputParam componentInputParam = null;
            Element parameter = null;
            for (Iterator parameterIterator = inputParams.elementIterator("input_parameter"); parameterIterator.hasNext();) {
                parameter = (Element) parameterIterator.next();
                componentInputParam = new ComponentInputParam();
                componentInputParam.setName(parameter.elementTextTrim("name"));
                componentInputParam.setValue(parameter.elementTextTrim("value"));
                componentInputParam.setRemark(parameter.elementTextTrim("remark"));
                inputParamList.add(componentInputParam);
            }
            componentConfig.setInputParams(inputParamList);
        }
    }

    /**
     * 解析出参节点
     * 
     * @param componentConfig 构件配置
     * @param root 根节点
     */
    @SuppressWarnings("rawtypes")
    private static void parseOutputParametersElement(ComponentConfig componentConfig, Element root) {
        Element outputParams = root.element("output_parameters");
        if (outputParams != null) {
            List<ComponentOutputParam> outputParamList = new ArrayList<ComponentOutputParam>();
            ComponentOutputParam componentOutputParam = null;
            Element parameter = null;
            for (Iterator parameterIterator = outputParams.elementIterator("output_parameter"); parameterIterator.hasNext();) {
                parameter = (Element) parameterIterator.next();
                componentOutputParam = new ComponentOutputParam();
                componentOutputParam.setName(parameter.elementTextTrim("name"));
                componentOutputParam.setRemark(parameter.elementTextTrim("remark"));
                outputParamList.add(componentOutputParam);
            }
            componentConfig.setOutputParams(outputParamList);
        }
    }

    /**
     * 解析预留区节点
     * 
     * @param componentConfig 构件配置
     * @param root 根节点
     */
    @SuppressWarnings("rawtypes")
    private static void parseReserveZonesElement(ComponentConfig componentConfig, Element root) {
        Element reserveZones = root.element("reserve_zones");
        if (reserveZones != null) {
            List<ComponentReserveZone> reserveZoneList = new ArrayList<ComponentReserveZone>();
            ComponentReserveZone componentReserveZone = null;
            Element reserveZone = null;
            for (Iterator reserveZoneIterator = reserveZones.elementIterator("reserve_zone"); reserveZoneIterator.hasNext();) {
                reserveZone = (Element) reserveZoneIterator.next();
                componentReserveZone = new ComponentReserveZone();
                componentReserveZone.setName(reserveZone.elementTextTrim("name"));
                componentReserveZone.setAlias(reserveZone.elementTextTrim("alias"));
                componentReserveZone.setType(reserveZone.elementTextTrim("type"));
                componentReserveZone.setPage(reserveZone.elementTextTrim("page"));
                reserveZoneList.add(componentReserveZone);
            }
            componentConfig.setReserveZones(reserveZoneList);
        }
    }

    /**
     * 解析方法节点
     * 
     * @param componentConfig 构件配置
     * @param root 根节点
     */
    @SuppressWarnings("rawtypes")
    private static void parseFunctionsElement(ComponentConfig componentConfig, Element root) {
        Element functions = root.element("functions");
        if (functions != null) {
            List<ComponentFunction> functionList = new ArrayList<ComponentFunction>();
            ComponentFunction componentFunction = null;
            Element function = null;
            for (Iterator functionIterator = functions.elementIterator("function"); functionIterator.hasNext();) {
                function = (Element) functionIterator.next();
                componentFunction = new ComponentFunction();
                componentFunction.setName(function.elementTextTrim("name"));
                componentFunction.setRemark(function.elementTextTrim("remark"));
                componentFunction.setPage(function.elementTextTrim("page"));
                Element functionDatas = function.element("return_datas");
                if (functionDatas != null) {
                    List<ComponentFunctionData> functionDataList = new ArrayList<ComponentFunctionData>();
                    ComponentFunctionData componentFunctionData = null;
                    Element functionData = null;
                    for (Iterator functionDataIterator = functionDatas.elementIterator("return_data"); functionDataIterator.hasNext();) {
                        functionData = (Element) functionDataIterator.next();
                        componentFunctionData = new ComponentFunctionData();
                        componentFunctionData.setName(functionData.elementTextTrim("name"));
                        componentFunctionData.setRemark(functionData.elementTextTrim("remark"));
                        functionDataList.add(componentFunctionData);
                    }
                    componentFunction.setComponentFunctionDataList(functionDataList);
                }
                functionList.add(componentFunction);
            }
            componentConfig.setFunctions(functionList);
        }
    }

    /**
     * 解析回调函数节点
     * 
     * @param componentConfig 构件配置
     * @param root 根节点
     */
    @SuppressWarnings("rawtypes")
    private static void parseCallbacksElement(ComponentConfig componentConfig, Element root) {
        Element callbacks = root.element("callbacks");
        if (callbacks != null) {
            List<ComponentCallback> callbackList = new ArrayList<ComponentCallback>();
            ComponentCallback componentCallback = null;
            Element callback = null;
            for (Iterator callbackIterator = callbacks.elementIterator("callback"); callbackIterator.hasNext();) {
                callback = (Element) callbackIterator.next();
                componentCallback = new ComponentCallback();
                componentCallback.setName(callback.elementTextTrim("name"));
                componentCallback.setRemark(callback.elementTextTrim("remark"));
                componentCallback.setPage(callback.elementTextTrim("page"));
                Element callbackParams = callback.element("parameters");
                if (callbackParams != null) {
                    List<ComponentCallbackParam> callbackParamList = new ArrayList<ComponentCallbackParam>();
                    ComponentCallbackParam componentCallbackParam = null;
                    Element callbackParam = null;
                    for (Iterator callbackParamIterator = callbackParams.elementIterator("parameter"); callbackParamIterator.hasNext();) {
                        callbackParam = (Element) callbackParamIterator.next();
                        componentCallbackParam = new ComponentCallbackParam();
                        componentCallbackParam.setName(callbackParam.elementTextTrim("name"));
                        componentCallbackParam.setRemark(callbackParam.elementTextTrim("remark"));
                        callbackParamList.add(componentCallbackParam);
                    }
                    componentCallback.setComponentCallbackParamList(callbackParamList);
                }
                callbackList.add(componentCallback);
            }
            componentConfig.setCallbacks(callbackList);
        }
    }

    /**
     * 解析表节点
     * 
     * @param componentConfig 构件配置
     * @param root 根节点
     */
    @SuppressWarnings("rawtypes")
    private static void parseTablesElement(ComponentConfig componentConfig, Element root) {
        Element tables = root.element("tables");
        if (tables != null) {
            List<ComponentTable> componentTableList = new ArrayList<ComponentTable>();
            ComponentTable componentTable = null;
            Element table = null;
            for (Iterator tableIterator = tables.elementIterator("table"); tableIterator.hasNext();) {
                table = (Element) tableIterator.next();
                componentTable = new ComponentTable();
                componentTable.setName(table.elementTextTrim("name").toUpperCase());
                componentTable.setReleaseWithData(StringUtil.null2zero(table.elementTextTrim("release_with_data")));
                Element isSelfdefine = table.element("is_selfdefine");
                if (isSelfdefine != null) {
                    componentTable.setIsSelfdefine(isSelfdefine.getTextTrim());
                } else {
                    componentTable.setIsSelfdefine(ConstantVar.Judgment.NO);
                }
                if (ConstantVar.Judgment.NO.equals(componentTable.getIsSelfdefine())) {
                    Element columns = table.element("columns");
                    if (columns != null) {
                        List<ComponentColumn> componentColumnList = new ArrayList<ComponentColumn>();
                        ComponentColumn componentColumn = null;
                        Element column = null;
                        for (Iterator columnIterator = columns.elementIterator("column"); columnIterator.hasNext();) {
                            column = (Element) columnIterator.next();
                            componentColumn = new ComponentColumn();
                            componentColumn.setName(column.elementTextTrim("name"));
                            componentColumn.setType(column.elementTextTrim("type"));
                            if (null == column.elementTextTrim("length") || "".equals(column.elementTextTrim("length"))) {
                                componentColumn.setLength(null);
                            } else {
                                componentColumn.setLength(Integer.valueOf(column.elementTextTrim("length")));
                            }
                            componentColumn.setIsNull(column.elementTextTrim("is_null"));
                            componentColumn.setDefaultValue(column.elementTextTrim("default_value"));
                            componentColumn.setRemark(column.elementTextTrim("remark"));
                            componentColumnList.add(componentColumn);
                        }
                        componentTable.setComponentColumnList(componentColumnList);
                    }
                }
                componentTableList.add(componentTable);
            }
            componentConfig.setComponentTables(componentTableList);
        }
    }

    /**
     * 解析权限按钮节点
     * 
     * @param componentConfig 构件配置
     * @param root 根节点
     */
    @SuppressWarnings("rawtypes")
    private static void parseButtonElement(ComponentConfig componentConfig, Element root) {
        Element authorityButtons = root.element("authority_buttons");
        if (authorityButtons != null) {
            List<ComponentButton> componentButtonList = new ArrayList<ComponentButton>();
            ComponentButton componentButton = null;
            Element parameter = null;
            for (Iterator parameterIterator = authorityButtons.elementIterator("button"); parameterIterator.hasNext();) {
                parameter = (Element) parameterIterator.next();
                componentButton = new ComponentButton();
                componentButton.setName(parameter.elementTextTrim("name"));
                componentButton.setDisplayName(parameter.elementTextTrim("display_name"));
                componentButtonList.add(componentButton);
            }
            componentConfig.setButtons(componentButtonList);
        }
    }

    /**
     * 解析自定义构件的配置信息
     * 
     * @param selfDefineConfigFile 自定义构件的配置文件
     * @param containSelfDefineInfo 包含自定义信息（树定义、应用定义、工作流定义、报表定义）
     * @return Map<String, Object>
     * @throws IOException
     * @throws JsonMappingException
     * @throws JsonParseException
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> parseSelfDefineConfig(File selfDefineConfigFile, boolean containSelfDefineInfo) throws JsonParseException,
            JsonMappingException, IOException {
        Map<String, Object> configMap = new HashMap<String, Object>();
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> map = objectMapper.readValue(selfDefineConfigFile, Map.class);
        Map<String, Object> componentVersionMap = (Map<String, Object>) map.get("componentVersion");
        // 构件版本
        configMap.put("componentVersion", componentVersionMap);
        // 配置信息
        Map<String, String> moduleMap = (Map<String, String>) map.get("module");
        // 整理自定义配置信息中模块信息
        Module module = new Module();
        module.setId(moduleMap.get("id"));
        module.setType(moduleMap.get("type"));
        module.setLogicTableGroupCode(moduleMap.get("logicTableGroupCode"));
        module.setTemplateType(moduleMap.get("templateType"));
        module.setName(moduleMap.get("name"));
        module.setComponentClassName(StringUtil.null2empty(moduleMap.get("componentClassName")));
        module.setTreeId(StringUtil.null2empty(moduleMap.get("treeId")));
        module.setAreaLayout(StringUtil.null2empty(moduleMap.get("areaLayout")));
        module.setUiType(StringUtil.null2empty(moduleMap.get("uiType")));
        module.setShowOrder(Integer.valueOf(StringUtil.null2zero(moduleMap.get("showOrder"))));
        module.setRemark(StringUtil.null2empty(moduleMap.get("remark")));
        module.setComponentVersionId(StringUtil.null2empty(moduleMap.get("componentVersionId")));
        module.setComponentUrl(StringUtil.null2empty(moduleMap.get("componentUrl")));
        module.setComponentAreaId(StringUtil.null2empty(moduleMap.get("componentAreaId")));
        module.setTable1Id(StringUtil.null2empty(moduleMap.get("table1Id")));
        module.setArea1Id(StringUtil.null2empty(moduleMap.get("area1Id")));
        module.setArea1Size(StringUtil.null2empty(moduleMap.get("area1Size")));
        module.setTable2Id(StringUtil.null2empty(moduleMap.get("table2Id")));
        module.setArea2Id(StringUtil.null2empty(moduleMap.get("area2Id")));
        module.setArea2Size(StringUtil.null2empty(moduleMap.get("area2Size")));
        module.setTable3Id(StringUtil.null2empty(moduleMap.get("table3Id")));
        module.setArea3Id(StringUtil.null2empty(moduleMap.get("area3Id")));
        module.setArea3Size(StringUtil.null2empty(moduleMap.get("area3Size")));
        module.setShowOrder(1);
        if (ConstantVar.Component.Type.TREE.equals(module.getType())) {
            List<Map<String, Object>> treeList = (List<Map<String, Object>>) map.get("tree");
            // 树构件
            // 整理自定义配置信息中树定义信息
            parseSelfDefineTree(configMap, treeList);
            List<Map<String, Object>> logicTableList = (List<Map<String, Object>>) map.get("logicTables");
            List<Map<String, Object>> logicColumnList = (List<Map<String, Object>>) map.get("logicColumns");
            List<Map<String, Object>> logicGroupList = (List<Map<String, Object>>) map.get("logicGroups");
            List<Map<String, Object>> logicGroupRelationList = (List<Map<String, Object>>) map.get("logicGroupRelations");
            List<Map<String, Object>> logicTableRelationList = (List<Map<String, Object>>) map.get("logicTableRelations");
            List<Map<String, Object>> physicalTableList = (List<Map<String, Object>>) map.get("physicalTables");
            Map<String, Object> columnRelationsMap = (Map<String, Object>) map.get("columnRelations");
            List<Map<String, Object>> physicalGroupList = (List<Map<String, Object>>) map.get("physicalGroups");
            List<Map<String, Object>> physicalRelationList = (List<Map<String, Object>>) map.get("physicalGroupRelations");
            // 整理自定义配置信息中逻辑表组信息
            parseLogicGroupMap(configMap, logicGroupList);
            // 整理自定义配置信息中逻辑表信息
            parseLogicTableList(configMap, logicTableList);
            // 整理自定义配置信息中逻辑表中字段信息
            parseLogicColumnList(configMap, logicColumnList);
            // 整理自定义配置信息中逻辑表和逻辑表组关系信息
            parseLogicGroupRelationList(configMap, logicGroupRelationList);
            // 整理自定义配置信息中逻辑表组中逻辑表关联关系信息
            parseLogicTableRelationList(configMap, logicTableRelationList);
            // 整理自定义配置信息中表定义信息
            parsePhysicalTable(configMap, physicalTableList);
            // 整理自定义配置信息中字段关联定义信息
            configMap.put("columnRelations", columnRelationsMap);
            // 整理自定义配置信息中物理表组信息
            parsePhysicalGroupMap(configMap, physicalGroupList);
            // 整理自定义配置信息中物理表和物理表组关系信息
            parsePhysicalGroupRelationList(configMap, physicalRelationList);
            if (containSelfDefineInfo) {
                Map<String, Object> workflowMap = (Map<String, Object>) map.get("workflow");
                Map<String, Object> applicationMap = (Map<String, Object>) map.get("application");
                // 整理工作流信息
                configMap.put("workflow", workflowMap);
                // 整理自定义配置信息中应用定义信息
                configMap.put("application", applicationMap);
            }
        } else if (ConstantVar.Component.Type.PHYSICAL_TABLE.equals(module.getType())) {
            List<Map<String, Object>> logicTableList = (List<Map<String, Object>>) map.get("logicTables");
            List<Map<String, Object>> logicColumnList = (List<Map<String, Object>>) map.get("logicColumns");
            List<Map<String, Object>> physicalTableList = (List<Map<String, Object>>) map.get("physicalTables");
            Map<String, Object> columnRelationsMap = (Map<String, Object>) map.get("columnRelations");
            Map<String, Object> applicationMap = (Map<String, Object>) map.get("application");
            // 整理自定义配置信息中逻辑表信息
            parseLogicTableList(configMap, logicTableList);
            // 整理自定义配置信息中逻辑表中字段信息
            parseLogicColumnList(configMap, logicColumnList);
            // 整理自定义配置信息中表定义信息
            parsePhysicalTable(configMap, physicalTableList);
            // 整理自定义配置信息中字段关联定义信息
            configMap.put("columnRelations", columnRelationsMap);
            if (containSelfDefineInfo) {
                // 整理自定义配置信息中应用定义信息
                configMap.put("application", applicationMap);
            }
        } else if (ConstantVar.Component.Type.LOGIC_TABLE.equals(module.getType())) {
            List<Map<String, Object>> logicGroupList = (List<Map<String, Object>>) map.get("logicGroups");
            List<Map<String, Object>> logicTableList = (List<Map<String, Object>>) map.get("logicTables");
            List<Map<String, Object>> logicColumnList = (List<Map<String, Object>>) map.get("logicColumns");
            List<Map<String, Object>> logicGroupRelationList = (List<Map<String, Object>>) map.get("logicGroupRelations");
            List<Map<String, Object>> logicTableRelationList = (List<Map<String, Object>>) map.get("logicTableRelations");
            // 整理自定义配置信息中逻辑表组信息
            parseLogicGroupMap(configMap, logicGroupList);
            // 整理自定义配置信息中逻辑表信息
            parseLogicTableList(configMap, logicTableList);
            // 整理自定义配置信息中逻辑表中字段信息
            parseLogicColumnList(configMap, logicColumnList);
            // 整理自定义配置信息中逻辑表和逻辑表组关系信息
            parseLogicGroupRelationList(configMap, logicGroupRelationList);
            // 整理自定义配置信息中逻辑表组中逻辑表关联关系信息
            parseLogicTableRelationList(configMap, logicTableRelationList);
        }
        configMap.put("module", module);
        configMap.put("tableTrees", map.get("tableTrees"));
        return configMap;
    }

    /**
     * 整理自定义配置信息中树定义信息
     * 
     * @param configMap 配置信息Map
     * @param treeList 树节点信息
     */
    private static void parseSelfDefineTree(Map<String, Object> configMap, List<Map<String, Object>> treeList) {
        if (CollectionUtils.isNotEmpty(treeList)) {
            Map<String, TreeDefine> treeDefineMap = new HashMap<String, TreeDefine>();
            configMap.put("tree", treeDefineMap);
            TreeDefine treeDefine = null;
            for (Map<String, Object> tree : treeList) {
                treeDefine = new TreeDefine();
                treeDefine.setId(StringUtil.null2empty(tree.get("id")));
                treeDefine.setName(StringUtil.null2empty(tree.get("name")));
                treeDefine.setParentId(StringUtil.null2empty(tree.get("parentId")));
                treeDefine.setType(StringUtil.null2empty(tree.get("type")));
                treeDefine.setShowOrder(Integer.valueOf(StringUtil.null2zero(tree.get("showOrder"))));
                treeDefine.setValue(StringUtil.null2empty(tree.get("value")));
                treeDefine.setDbId(StringUtil.null2empty(tree.get("dbId")));
                treeDefine.setTableId(StringUtil.null2empty(tree.get("tableId")));
                treeDefine.setChild(Boolean.valueOf(StringUtil.null2empty(tree.get("child"))));
                treeDefine.setNodeRule(StringUtil.null2empty(tree.get("nodeRule")));
                treeDefine.setDynamic(StringUtil.null2empty(tree.get("dynamic")));
                treeDefine.setDynamicFromId(StringUtil.null2empty(tree.get("dynamicFromId")));
                treeDefine.setRemark(StringUtil.null2empty(tree.get("remark")));
                treeDefine.setColumnNames(StringUtil.null2empty(tree.get("columnNames")));
                treeDefine.setColumnValues(StringUtil.null2empty(tree.get("columnValues")));
                treeDefine.setParentIds(StringUtil.null2empty(tree.get("parentIds")));
                treeDefine.setDataSource(StringUtil.null2empty(tree.get("dataSource")));
                treeDefine.setShowNodeCount(StringUtil.null2empty(tree.get("showNodeCount")));
                treeDefine.setSortType(StringUtil.null2empty(tree.get("sortType")));
                treeDefine.setRootId(StringUtil.null2empty(tree.get("rootId")));
                treeDefine.setShowRoot(StringUtil.null2empty(tree.get("showRoot")));
                treeDefine.setColumnFilter(StringUtil.null2empty(tree.get("columnFilter")));
                treeDefineMap.put(StringUtil.null2empty(tree.get("id")), treeDefine);
            }
        }
    }

    /**
     * 整理自定义配置信息中逻辑表组信息
     * 
     * @param configMap 配置信息Map
     * @param logicGroupList 逻辑表组信息
     */
    private static void parseLogicGroupMap(Map<String, Object> configMap, List<Map<String, Object>> logicGroupList) {
        if (CollectionUtils.isNotEmpty(logicGroupList)) {
            List<LogicGroupDefine> logicGroupDefineList = new ArrayList<LogicGroupDefine>();
            configMap.put("logicGroups", logicGroupDefineList);
            for (Map<String, Object> logicGroupMap : logicGroupList) {
                LogicGroupDefine logicGroup = new LogicGroupDefine();
                logicGroup.setId(StringUtil.null2empty(logicGroupMap.get("id")));
                logicGroup.setGroupName(StringUtil.null2empty(logicGroupMap.get("groupName")));
                logicGroup.setCode(StringUtil.null2empty(logicGroupMap.get("code")));
                logicGroup.setShowOrder(Integer.parseInt(StringUtil.null2zero(logicGroupMap.get("showOrder"))));
                logicGroup.setRemark(StringUtil.null2empty(logicGroupMap.get("remark")));
                logicGroup.setStatus(StringUtil.null2empty(logicGroupMap.get("status")));
                logicGroupDefineList.add(logicGroup);
            }
        }
    }

    /**
     * 整理自定义配置信息中逻辑表信息
     * 
     * @param configMap 配置信息Map
     * @param logicTableList 逻辑表信息
     */
    private static void parseLogicTableList(Map<String, Object> configMap, List<Map<String, Object>> logicTableList) {
        if (CollectionUtils.isNotEmpty(logicTableList)) {
            List<LogicTableDefine> logicTableDefineList = new ArrayList<LogicTableDefine>();
            configMap.put("logicTables", logicTableDefineList);
            LogicTableDefine logicTableDefine = null;
            for (Map<String, Object> logicTableMap : logicTableList) {
                logicTableDefine = new LogicTableDefine();
                logicTableDefine.setId(StringUtil.null2empty(logicTableMap.get("id")));
                logicTableDefine.setShowName(StringUtil.null2empty(logicTableMap.get("showName")));
                logicTableDefine.setCode(StringUtil.null2empty(logicTableMap.get("code")));
                logicTableDefine.setShowOrder(Integer.parseInt(StringUtil.null2zero(logicTableMap.get("showOrder"))));
                logicTableDefine.setRemark(StringUtil.null2empty(logicTableMap.get("remark")));
                logicTableDefine.setStatus(StringUtil.null2empty(logicTableMap.get("status")));
                logicTableDefine.setClassification(StringUtil.null2empty(logicTableMap.get("classification")));
                logicTableDefine.setTableTreeId(StringUtil.null2empty(logicTableMap.get("tableTreeId")));
                logicTableDefineList.add(logicTableDefine);
            }
        }
    }

    /**
     * 整理自定义配置信息中逻辑表的字段信息
     * 
     * @param configMap 配置信息Map
     * @param logicColumnList 逻辑表的字段信息
     */
    private static void parseLogicColumnList(Map<String, Object> configMap, List<Map<String, Object>> logicColumnList) {
        if (CollectionUtils.isNotEmpty(logicColumnList)) {
            Map<String, Map<String, ColumnDefine>> logicColumnDefineMap = new HashMap<String, Map<String, ColumnDefine>>();
            configMap.put("logicColumns", logicColumnDefineMap);
            Map<String, ColumnDefine> tempLogicColumnDefineMap = null;
            ColumnDefine logicColumn = null;
            String logicTableCode = null;
            for (Map<String, Object> logicColumnMap : logicColumnList) {
                logicTableCode = String.valueOf(logicColumnMap.get("tableId"));
                tempLogicColumnDefineMap = logicColumnDefineMap.get(logicTableCode);
                if (tempLogicColumnDefineMap == null) {
                    tempLogicColumnDefineMap = new HashMap<String, ColumnDefine>();
                    logicColumnDefineMap.put(logicTableCode, tempLogicColumnDefineMap);
                }
                logicColumn = new ColumnDefine();
                logicColumn.setId(StringUtil.null2empty(logicColumnMap.get("id")));
                logicColumn.setTableId(StringUtil.null2empty(logicColumnMap.get("tableId")));
                logicColumn.setShowName(StringUtil.null2empty(logicColumnMap.get("showName")));
                logicColumn.setColumnName(StringUtil.null2empty(logicColumnMap.get("columnName")));
                logicColumn.setDataType(StringUtil.null2empty(logicColumnMap.get("dataType")));
                logicColumn.setDataTypeExtend(StringUtil.null2empty(logicColumnMap.get("dataTypeExtend")));
                logicColumn.setCodeTypeCode(StringUtil.null2empty(logicColumnMap.get("codeTypeCode")));
                logicColumn.setLength(Integer.valueOf(StringUtil.null2zero(logicColumnMap.get("length"))));
                logicColumn.setColumnType(StringUtil.null2empty(logicColumnMap.get("columnType")));
                logicColumn.setInputable(StringUtil.null2empty(logicColumnMap.get("inputable")));
                logicColumn.setUpdateable(StringUtil.null2empty(logicColumnMap.get("updateable")));
                logicColumn.setSearchable(StringUtil.null2empty(logicColumnMap.get("searchable")));
                logicColumn.setListable(StringUtil.null2empty(logicColumnMap.get("listable")));
                logicColumn.setSortable(StringUtil.null2empty(logicColumnMap.get("sortable")));
                logicColumn.setPhraseable(StringUtil.null2empty(logicColumnMap.get("phraseable")));
                logicColumn.setAlign(StringUtil.null2empty(logicColumnMap.get("align")));
                logicColumn.setFilterType(StringUtil.null2empty(logicColumnMap.get("filterType")));
                logicColumn.setWidth(Integer.valueOf(StringUtil.null2zero(logicColumnMap.get("width"))));
                logicColumn.setDefaultValue(StringUtil.null2empty(logicColumnMap.get("defaultValue")));
                logicColumn.setRemark(StringUtil.null2empty(logicColumnMap.get("remark")));
                logicColumn.setCreated(StringUtil.null2empty(logicColumnMap.get("created")));
                logicColumn.setColumnLabel(StringUtil.null2empty(logicColumnMap.get("columnLabel")));
                logicColumn.setShowOrder(Integer.valueOf(StringUtil.null2zero(logicColumnMap.get("showOrder"))));
                logicColumn.setInputType(StringUtil.null2empty(logicColumnMap.get("inputType")));
                logicColumn.setDataTypeExtend(StringUtil.null2empty(logicColumnMap.get("dataTypeExpend")));
                logicColumn.setInputOption(StringUtil.null2empty(logicColumnMap.get("inputOption")));
                tempLogicColumnDefineMap.put(StringUtil.null2empty(logicColumnMap.get("id")), logicColumn);
            }
        }
    }

    /**
     * 整理自定义配置信息中逻辑表和逻辑表组关系信息
     * 
     * @param configMap 配置信息Map
     * @param logicGroupRelationList 逻辑表和逻辑表组关系信息
     */
    private static void parseLogicGroupRelationList(Map<String, Object> configMap, List<Map<String, Object>> logicGroupRelationList) {
        if (CollectionUtils.isNotEmpty(logicGroupRelationList)) {
            List<LogicGroupRelation> groupRelationList = new ArrayList<LogicGroupRelation>();
            configMap.put("logicGroupRelations", groupRelationList);
            LogicGroupRelation logicGroupRelation = null;
            for (Map<String, Object> logicGroupRelationMap : logicGroupRelationList) {
                logicGroupRelation = new LogicGroupRelation();
                logicGroupRelation.setId(StringUtil.null2empty(logicGroupRelationMap.get("id")));
                logicGroupRelation.setGroupCode(StringUtil.null2empty(logicGroupRelationMap.get("groupCode")));
                logicGroupRelation.setTableCode(StringUtil.null2empty(logicGroupRelationMap.get("tableCode")));
                logicGroupRelation.setParentTableCode(StringUtil.null2empty(logicGroupRelationMap.get("parentTableCode")));
                logicGroupRelation.setShowOrder(Integer.parseInt(StringUtil.null2zero(logicGroupRelationMap.get("showOrder"))));
                groupRelationList.add(logicGroupRelation);
            }
        }
    }

    /**
     * 整理自定义配置信息中逻辑表组中逻辑表关联关系信息
     * 
     * @param configMap 配置信息Map
     * @param logicTableRelationList 逻辑表组中逻辑表关联关系信息
     */
    private static void parseLogicTableRelationList(Map<String, Object> configMap, List<Map<String, Object>> logicTableRelationList) {
        if (CollectionUtils.isNotEmpty(logicTableRelationList)) {
            List<LogicTableRelation> tableRelationList = new ArrayList<LogicTableRelation>();
            configMap.put("logicTableRelations", tableRelationList);
            LogicTableRelation logicTableRelation = null;
            for (Map<String, Object> logicTableRelationMap : logicTableRelationList) {
                logicTableRelation = new LogicTableRelation();
                logicTableRelation.setId(StringUtil.null2empty(logicTableRelationMap.get("id")));
                logicTableRelation.setGroupCode(StringUtil.null2empty(logicTableRelationMap.get("groupCode")));
                logicTableRelation.setTableCode(StringUtil.null2empty(logicTableRelationMap.get("tableCode")));
                logicTableRelation.setColumnId(StringUtil.null2empty(logicTableRelationMap.get("columnId")));
                logicTableRelation.setParentTableCode(StringUtil.null2empty(logicTableRelationMap.get("parentTableCode")));
                logicTableRelation.setParentColumnId(StringUtil.null2empty(logicTableRelationMap.get("parentColumnId")));
                tableRelationList.add(logicTableRelation);
            }
        }
    }

    /**
     * 整理自定义配置信息中物理表组信息
     * 
     * @param configMap 配置信息Map
     * @param physicalGroupList 物理表组信息
     */
    private static void parsePhysicalGroupMap(Map<String, Object> configMap, List<Map<String, Object>> physicalGroupList) {
        if (CollectionUtils.isNotEmpty(physicalGroupList)) {
            List<PhysicalGroupDefine> physicalGroupDefineList = new ArrayList<PhysicalGroupDefine>();
            configMap.put("physicalGroups", physicalGroupDefineList);
            for (Map<String, Object> physicalGroupMap : physicalGroupList) {
                PhysicalGroupDefine physicalGroup = new PhysicalGroupDefine();
                physicalGroup.setId(StringUtil.null2empty(physicalGroupMap.get("id")));
                physicalGroup.setGroupName(StringUtil.null2empty(physicalGroupMap.get("groupName")));
                physicalGroup.setCode(StringUtil.null2empty(physicalGroupMap.get("code")));
                physicalGroup.setLogicGroupCode(StringUtil.null2empty(physicalGroupMap.get("logicGroupCode")));
                physicalGroup.setShowOrder(Integer.parseInt(StringUtil.null2zero(physicalGroupMap.get("showOrder"))));
                physicalGroup.setRemark(StringUtil.null2empty(physicalGroupMap.get("remark")));
                physicalGroupDefineList.add(physicalGroup);
            }
        }
    }

    /**
     * 整理自定义配置信息中物理表和物理表组关系信息
     * 
     * @param configMap 配置信息Map
     * @param physicalGroupRelationList 物理表和物理表组关系信息
     */
    private static void parsePhysicalGroupRelationList(Map<String, Object> configMap, List<Map<String, Object>> physicalGroupRelationList) {
        if (CollectionUtils.isNotEmpty(physicalGroupRelationList)) {
            List<PhysicalGroupRelation> groupRelationList = new ArrayList<PhysicalGroupRelation>();
            configMap.put("physicalGroupRelations", groupRelationList);
            PhysicalGroupRelation physicalGroupRelation = null;
            for (Map<String, Object> physicalGroupRelationMap : physicalGroupRelationList) {
                physicalGroupRelation = new PhysicalGroupRelation();
                physicalGroupRelation.setId(StringUtil.null2empty(physicalGroupRelationMap.get("id")));
                physicalGroupRelation.setGroupId(StringUtil.null2empty(physicalGroupRelationMap.get("groupId")));
                physicalGroupRelation.setTableId(StringUtil.null2empty(physicalGroupRelationMap.get("tableId")));
                physicalGroupRelation.setShowOrder(Integer.valueOf(StringUtil.null2zero(physicalGroupRelationMap.get("showOrder"))));
                groupRelationList.add(physicalGroupRelation);
            }
        }
    }

    /**
     * 整理自定义配置信息中表定义信息
     * 
     * @param configMap 配置信息Map
     * @param physicalTableList 自定义构件关联的表
     */
    @SuppressWarnings("unchecked")
    private static void parsePhysicalTable(Map<String, Object> configMap, List<Map<String, Object>> physicalTableList) {
        Map<String, PhysicalTableDefine> physicalTableDefineMap = new HashMap<String, PhysicalTableDefine>();
        Map<String, Map<String, ColumnDefine>> columnDefineMap = new HashMap<String, Map<String, ColumnDefine>>();
        List<Map<String, String>> tableRelationList = new ArrayList<Map<String, String>>();
        configMap.put("physicalTable", physicalTableDefineMap);
        configMap.put("physicalColumn", columnDefineMap);
        configMap.put("tableRelation", tableRelationList);
        if (CollectionUtils.isNotEmpty(physicalTableList)) {
            PhysicalTableDefine physicalTableDefine = null;
            Map<String, ColumnDefine> columnDefineColumnMap = null;
            ColumnDefine columnDefine = null;
            for (Map<String, Object> physicalTable : physicalTableList) {
                physicalTableDefine = new PhysicalTableDefine();
                physicalTableDefine.setId(StringUtil.null2empty(physicalTable.get("id")));
                physicalTableDefine.setShowName(StringUtil.null2empty(physicalTable.get("showName")));
                physicalTableDefine.setTablePrefix(StringUtil.null2empty(physicalTable.get("tablePrefix")));
                physicalTableDefine.setTableCode(StringUtil.null2empty(physicalTable.get("tableCode")));
                physicalTableDefine.setClassification(StringUtil.null2empty(physicalTable.get("classification")));
                physicalTableDefine.setTableType(StringUtil.null2empty(physicalTable.get("tableType")));
                physicalTableDefine.setTableName(StringUtil.null2empty(physicalTable.get("tableName")));
                physicalTableDefine.setLogicTableCode(StringUtil.null2empty(physicalTable.get("logicTableCode")));
                physicalTableDefine.setRemark(StringUtil.null2empty(physicalTable.get("remark")));
                physicalTableDefine.setTableTreeId(StringUtil.null2empty(physicalTable.get("tableTreeId")));
                physicalTableDefine.setShowOrder(Integer.valueOf(StringUtil.null2zero(physicalTable.get("showOrder"))));
                physicalTableDefine.setReleaseWithData(StringUtil.null2empty(physicalTable.get("releaseWithData")));
                physicalTableDefine.setCreateIndex(StringUtil.null2empty(physicalTable.get("createIndex")));
                physicalTableDefineMap.put(StringUtil.null2empty(physicalTable.get("tableId")), physicalTableDefine);
                List<Map<String, Object>> columnList = (List<Map<String, Object>>) physicalTable.get("columns");
                if (CollectionUtils.isNotEmpty(columnList)) {
                    columnDefineColumnMap = new HashMap<String, ColumnDefine>();
                    columnDefineMap.put(StringUtil.null2empty(physicalTable.get("tableId")), columnDefineColumnMap);
                    for (Map<String, Object> columnMap : columnList) {
                        columnDefine = new ColumnDefine();
                        columnDefine.setId(StringUtil.null2empty(columnMap.get("id")));
                        columnDefine.setTableId(StringUtil.null2empty(columnMap.get("tableId")));
                        columnDefine.setShowName(StringUtil.null2empty(columnMap.get("showName")));
                        columnDefine.setColumnName(StringUtil.null2empty(columnMap.get("columnName")));
                        columnDefine.setDataType(StringUtil.null2empty(columnMap.get("dataType")));
                        columnDefine.setDataTypeExtend(StringUtil.null2empty(columnMap.get("dataTypeExtend")));
                        columnDefine.setCodeTypeCode(StringUtil.null2empty(columnMap.get("codeTypeCode")));
                        columnDefine.setLength(Integer.valueOf(StringUtil.null2zero(columnMap.get("length"))));
                        columnDefine.setColumnType(StringUtil.null2empty(columnMap.get("columnType")));
                        columnDefine.setInputable(StringUtil.null2empty(columnMap.get("inputable")));
                        columnDefine.setUpdateable(StringUtil.null2empty(columnMap.get("updateable")));
                        columnDefine.setSearchable(StringUtil.null2empty(columnMap.get("searchable")));
                        columnDefine.setListable(StringUtil.null2empty(columnMap.get("listable")));
                        columnDefine.setSortable(StringUtil.null2empty(columnMap.get("sortable")));
                        columnDefine.setPhraseable(StringUtil.null2empty(columnMap.get("phraseable")));
                        columnDefine.setAlign(StringUtil.null2empty(columnMap.get("align")));
                        columnDefine.setFilterType(StringUtil.null2empty(columnMap.get("filterType")));
                        columnDefine.setWidth(Integer.valueOf(StringUtil.null2zero(columnMap.get("width"))));
                        columnDefine.setDefaultValue(StringUtil.null2empty(columnMap.get("defaultValue")));
                        columnDefine.setRemark(StringUtil.null2empty(columnMap.get("remark")));
                        columnDefine.setCreated(StringUtil.null2empty(columnMap.get("created")));
                        columnDefine.setColumnLabel(StringUtil.null2empty(columnMap.get("columnLabel")));
                        columnDefine.setShowOrder(Integer.valueOf(StringUtil.null2zero(columnMap.get("showOrder"))));
                        columnDefine.setInputType(StringUtil.null2empty(columnMap.get("inputType")));
                        columnDefine.setInputOption(StringUtil.null2empty(columnMap.get("inputOption")));
                        columnDefineColumnMap.put(StringUtil.null2empty(columnMap.get("id")), columnDefine);
                    }
                }
                List<Map<String, String>> tableRelations = (List<Map<String, String>>) physicalTable.get("tableRelations");
                if (CollectionUtils.isNotEmpty(tableRelations)) {
                    tableRelationList.addAll(tableRelations);
                }
            }
        }
    }

    /**
     * 打包自定义构件版本
     * 
     * @param componentVersionId 自定义构件ID
     * @param containSelfDefineInfo 包含自定义信息（树定义、应用定义、工作流定义、报表定义）
     * @return Object
     * @throws Exception
     */
    public static Map<String, Object> packageComponent(String componentVersionId, boolean containSelfDefineInfo) throws Exception {
        ComponentVersion componentVersion = XarchListener.getBean(ComponentVersionService.class).getByID(componentVersionId);
        String componentName = componentVersion.getComponent().getName();
        String componentPackageName = componentName.toLowerCase();
        String componentDirPath = getCompressTempPath() + "selfdefine/" + componentName;
        // 删除旧的构件包
        if (StringUtil.isNotEmpty(componentVersion.getPath())) {
            String oldComponentDirPath = getCompUncompressPath() + componentVersion.getPath().substring(0, componentVersion.getPath().lastIndexOf("."));
            String oldComponentZipPath = getCompPath() + componentVersion.getPath();
            FileUtil.deleteFile(oldComponentDirPath);
            FileUtil.deleteFile(oldComponentZipPath);
        }
        // 1、准备构件打包的目录
        File componentDir = new File(componentDirPath);
        if (!componentDir.exists()) {
            componentDir.mkdirs();
        }
        File classesDir = new File(componentDirPath + "/classes");
        File jarDir = new File(componentDirPath + "/jar");
        File pageDir = new File(componentDirPath + "/page");
        File srcDir = new File(componentDirPath + "/src");
        File dataDir = new File(componentDirPath + "/data");
        if (!classesDir.exists()) {
            classesDir.mkdirs();
        }
        if (!jarDir.exists()) {
            jarDir.mkdirs();
        }
        if (!pageDir.exists()) {
            pageDir.mkdirs();
        }
        if (!srcDir.exists()) {
            srcDir.mkdirs();
        }
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }
        // 生成新的构件包文件名
        String newFileName = componentVersion.getComponent().getAlias() + "_" + componentVersion.getVersion() + ".zip";
        String path = getCompPath() + newFileName;
        String uncompressPath = getCompUncompressPath() + newFileName.substring(0, newFileName.lastIndexOf("."));
        Map<String, Object> map = null;
        try {
            // 2、准备构件包内文件
            String projectPath = getProjectPath();
            // 拷贝页面文件
            FileUtil.copyFolder(projectPath + "cfg-resource/" + componentVersion.getViews() + "/views/selfdefine/" + componentPackageName, componentDirPath
                    + "/page/" + componentPackageName);
            if (!FileUtil.isEmptyDir(getSrcPath() + "selfdefine/com/ces/component/" + componentPackageName)) {
                // 创建构件java文件包
                FileUtil.copyFolder(getSrcPath() + "selfdefine/com/ces/component/" + componentPackageName, componentDirPath + "/src/com/ces/component/"
                        + componentPackageName);
                FileUtil.copyFolder(projectPath + "WEB-INF/classes/com/ces/component/" + componentPackageName, componentDirPath + "/classes/com/ces/component/"
                        + componentPackageName);
            }
            // 3、准备构件配置文件
            createComponentConfigXmlFile(componentVersion, componentDirPath + "/component-config.xml");
            // 4、准备构件配置信息
            map = createConfigJsonFile(componentVersion, containSelfDefineInfo, componentDirPath + "/data/config.json");
            // 5、压缩zip包
            String zipPath = componentDirPath + ".zip";
            File componentZip = new File(zipPath);
            ZipUtil.zip(componentZip, "", componentDir);
            // 6、拷贝到构件包文件路径下
            FileUtil.copyFile(zipPath, path);
            ZipUtil.unzipFile(new File(path), uncompressPath);
            componentVersion.setPath(newFileName);
            componentVersion.setIsPackage(ConstantVar.Component.Packaged.YES);
            XarchListener.getBean(ComponentVersionService.class).save(componentVersion);
        } catch (Exception e) {
            e.printStackTrace();
            deleteComponentFile(path, uncompressPath);
            throw new Exception(e.getMessage());
        } finally {
            FileUtil.deleteDir(getCompressTempPath() + "selfdefine/");
        }
        return map;
    }

    /**
     * 删除解压的临时文件和上传的文件
     * 
     * @param newFileName 构件包名次
     * @param tempPath 构件解压的临时目录
     */
    private static void deleteComponentFile(String packagePath, String uncompressPath) {
        FileUtil.deleteFile(packagePath);
        FileUtil.deleteFile(uncompressPath);
    }

    /**
     * 创建构件配置文件
     * 
     * @param componentVersion 构件版本
     * @param configFilePath 构件文件路径
     */
    @SuppressWarnings("unchecked")
    public static void createComponentConfigXmlFile(ComponentVersion componentVersion, String configFilePath) {
        Document doc = DocumentHelper.createDocument();
        doc.setXMLEncoding("UTF-8");
        Element root = doc.addElement("component");
        root.addElement("code").addText(componentVersion.getComponent().getName());
        root.addElement("name").addText(componentVersion.getComponent().getName());
        root.addElement("alias").addText(componentVersion.getComponent().getAlias());
        root.addElement("version").addText(componentVersion.getVersion());
        root.addElement("type").addText(componentVersion.getComponent().getType());
        root.addElement("views").addText(componentVersion.getViews());
        root.addElement("url").addText(componentVersion.getUrl());
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        root.addElement("package_time").addText(df.format(new Date()));
        List<ComponentSystemParameter> systemParameterList = XarchListener.getBean(ComponentSystemParameterService.class).getByComponentVersionId(
                componentVersion.getId());
        if (CollectionUtils.isNotEmpty(systemParameterList)) {
            Element systemParametersElement = root.addElement("system_parameters");
            for (ComponentSystemParameter systemParameter : systemParameterList) {
                Element systemParameterElement = systemParametersElement.addElement("system_parameter");
                systemParameterElement.addElement("name").addText(systemParameter.getName());
                systemParameterElement.addElement("remark").addText(StringUtil.null2empty(systemParameter.getRemark()));
            }
        }
        List<ComponentSelfParam> selfParamList = XarchListener.getBean(ComponentSelfParamService.class).getByComponentVersionId(componentVersion.getId());
        if (CollectionUtils.isNotEmpty(selfParamList)) {
            Element selfParamsElement = root.addElement("self_parameters");
            for (ComponentSelfParam selfParam : selfParamList) {
                Element selfParamElement = selfParamsElement.addElement("self_parameter");
                selfParamElement.addElement("name").addText(selfParam.getName());
                selfParamElement.addElement("type").addText(selfParam.getType());
                selfParamElement.addElement("text").addText(StringUtil.null2empty(selfParam.getText()));
                selfParamElement.addElement("value").addText(StringUtil.null2empty(selfParam.getValue()));
                String options = selfParam.getOptions().replaceAll("'", "\"").replaceAll("value", "\"value\"").replaceAll("text", "\"text\"")
                        .replaceAll("selected", "\"selected\"");
                if (StringUtil.isNotEmpty(options)) {
                    try {
                        List<Map<String, Object>> list = BeanUtils.toBean(options, List.class);
                        if (CollectionUtils.isNotEmpty(list)) {
                            Element optionsElement = selfParamElement.addElement("options");
                            for (Map<String, Object> map : list) {
                                if (StringUtil.isNotEmpty(map.get("value"))) {
                                    Element optionElement = optionsElement.addElement("option");
                                    optionElement.addElement("text").addText(String.valueOf(map.get("text")));
                                    optionElement.addElement("value").addText(String.valueOf(map.get("value")));
                                }
                            }
                        }
                    } catch (JsonParseException e) {
                        e.printStackTrace();
                    } catch (JsonMappingException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                selfParamElement.addElement("remark").addText(StringUtil.null2empty(selfParam.getRemark()));
            }
        }
        List<ComponentInputParam> inputParamList = XarchListener.getBean(ComponentInputParamService.class).getByComponentVersionId(componentVersion.getId());
        if (CollectionUtils.isNotEmpty(inputParamList)) {
            Element inputParamsElement = root.addElement("input_parameters");
            for (ComponentInputParam inputParam : inputParamList) {
                Element inputParamElement = inputParamsElement.addElement("input_parameter");
                inputParamElement.addElement("name").addText(inputParam.getName());
                inputParamElement.addElement("value").addText(StringUtil.null2empty(inputParam.getValue()));
                inputParamElement.addElement("remark").addText(StringUtil.null2empty(inputParam.getRemark()));
            }
        }
        List<ComponentOutputParam> outputParamList = XarchListener.getBean(ComponentOutputParamService.class).getByComponentVersionId(componentVersion.getId());
        if (CollectionUtils.isNotEmpty(outputParamList)) {
            Element outputParamsElement = root.addElement("output_parameters");
            for (ComponentOutputParam outputParam : outputParamList) {
                Element outputParamElement = outputParamsElement.addElement("output_parameter");
                outputParamElement.addElement("name").addText(outputParam.getName());
                outputParamElement.addElement("remark").addText(StringUtil.null2empty(outputParam.getRemark()));
            }
        }
        List<ComponentReserveZone> reserveZoneList = XarchListener.getBean(ComponentReserveZoneService.class).getByComponentVersionId(componentVersion.getId());
        if (CollectionUtils.isNotEmpty(reserveZoneList)) {
            Element reserveZonesElement = root.addElement("reserve_zones");
            for (ComponentReserveZone reserveZone : reserveZoneList) {
                Element reserveZoneElement = reserveZonesElement.addElement("reserve_zone");
                reserveZoneElement.addElement("name").addText(reserveZone.getName());
                reserveZoneElement.addElement("alias").addText(reserveZone.getAlias());
                reserveZoneElement.addElement("type").addText(reserveZone.getType());
                reserveZoneElement.addElement("page").addText(reserveZone.getPage());
            }
        }
        List<ComponentFunction> functionList = XarchListener.getBean(ComponentFunctionService.class).getByComponentVersionId(componentVersion.getId());
        if (CollectionUtils.isNotEmpty(functionList)) {
            Element functionsElement = root.addElement("functions");
            for (ComponentFunction function : functionList) {
                Element functionElement = functionsElement.addElement("function");
                functionElement.addElement("name").addText(function.getName());
                functionElement.addElement("page").addText(function.getPage());
                List<ComponentFunctionData> functionDataList = XarchListener.getBean(ComponentFunctionDataService.class).getByFunctionId(function.getId());
                if (CollectionUtils.isNotEmpty(functionDataList)) {
                    Element returnDatasElement = functionElement.addElement("return_datas");
                    for (ComponentFunctionData functionData : functionDataList) {
                        Element returnDataElement = returnDatasElement.addElement("return_data");
                        returnDataElement.addElement("name").addText(functionData.getName());
                        returnDataElement.addElement("remark").addText(StringUtil.null2empty(functionData.getRemark()));
                    }
                }
                functionElement.addElement("remark").addText(StringUtil.null2empty(function.getRemark()));
            }
        }
        List<ComponentCallback> callbackList = XarchListener.getBean(ComponentCallbackService.class).getByComponentVersionId(componentVersion.getId());
        if (CollectionUtils.isNotEmpty(callbackList)) {
            Element callbacksElement = root.addElement("callbacks");
            for (ComponentCallback callback : callbackList) {
                Element callbackElement = callbacksElement.addElement("callback");
                callbackElement.addElement("name").addText(callback.getName());
                callbackElement.addElement("page").addText(callback.getPage());
                List<ComponentCallbackParam> callbackParamList = XarchListener.getBean(ComponentCallbackParamService.class).getByCallbackId(callback.getId());
                if (CollectionUtils.isNotEmpty(callbackParamList)) {
                    Element parametersElement = callbackElement.addElement("parameters");
                    for (ComponentCallbackParam callbackParam : callbackParamList) {
                        Element parameterElement = parametersElement.addElement("parameter");
                        parameterElement.addElement("name").addText(callbackParam.getName());
                        parameterElement.addElement("remark").addText(StringUtil.null2empty(callbackParam.getRemark()));
                    }
                }
                callbackElement.addElement("remark").addText(StringUtil.null2empty(callback.getRemark()));
            }
        }
        root.addElement("remark").addText(StringUtil.null2empty(componentVersion.getRemark()));
        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding("UTF-8");
        format.setNewLineAfterDeclaration(false);
        format.setIndent("\t");
        XMLWriter xmlWriter = null;
        try {
            xmlWriter = new XMLWriter(new OutputStreamWriter(new FileOutputStream(configFilePath), "UTF-8"), format);
            xmlWriter.write(doc);
            xmlWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (xmlWriter != null) {
                try {
                    xmlWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 准备构件配置信息
     * 
     * @param componentVersion 构件版本
     * @param initJsonPath 构件配置信息文件路径
     * @param containSelfDefineInfo 包含自定义信息（树定义、应用定义、工作流定义、报表定义）
     * @return map
     */
    @SuppressWarnings("unchecked")
    private static Map<String, Object> createConfigJsonFile(ComponentVersion componentVersion, boolean containSelfDefineInfo, String initJsonPath) {
        Map<String, Object> map = new HashMap<String, Object>();
        // 模块信息
        String moduleId = componentVersion.getComponent().getCode();
        Module module = XarchListener.getBean(ModuleService.class).findByComponentVersionId(componentVersion.getId());
        Map<String, String> moduleMap = new HashMap<String, String>();
        map.put("module", moduleMap);
        moduleMap.put("id", module.getId());
        moduleMap.put("type", module.getType());
        moduleMap.put("logicTableGroupCode", module.getLogicTableGroupCode());
        moduleMap.put("templateType", module.getTemplateType());
        moduleMap.put("name", module.getName());
        moduleMap.put("componentClassName", module.getComponentClassName());
        moduleMap.put("treeId", module.getTreeId());
        moduleMap.put("areaLayout", StringUtil.null2empty(module.getAreaLayout()));
        moduleMap.put("uiType", module.getUiType());
        moduleMap.put("showOrder", StringUtil.null2zero(module.getShowOrder()));
        moduleMap.put("remark", module.getRemark());
        moduleMap.put("componentVersionId", module.getComponentVersionId());
        moduleMap.put("componentUrl", module.getComponentUrl());
        moduleMap.put("componentAreaId", module.getComponentAreaId());
        // 构件信息
        map.put("componentVersion", getComponentVersionJsonData(componentVersion));
        // 存放所有关联到的表
        Map<String, PhysicalTableDefine> physicalTableMap = new HashMap<String, PhysicalTableDefine>();
        if (ConstantVar.Component.Type.TREE.equals(module.getType())) {
            // 树构件
            String treeId = module.getTreeId();
            List<TreeDefine> treeDefineList = new ArrayList<TreeDefine>();
            if (containSelfDefineInfo) {
                treeDefineList.addAll(XarchListener.getBean(TreeDefineService.class).getAllChildren(treeId, null));
            }
            TreeDefine rootNode = XarchListener.getBean(TreeDefineService.class).getByID(treeId);
            treeDefineList.add(rootNode);
            map.put("tree", treeDefineList);
            Set<LogicTableDefine> logicTableDefineSet = new HashSet<LogicTableDefine>();
            Set<LogicGroupDefine> logicGroupDefineSet = new HashSet<LogicGroupDefine>();
            List<LogicGroupRelation> logicGroupRelationList = new ArrayList<LogicGroupRelation>();
            List<LogicTableRelation> logicTableRelationList = new ArrayList<LogicTableRelation>();
            Set<PhysicalTableDefine> physicalTableDefineSet = new HashSet<PhysicalTableDefine>();
            Set<PhysicalGroupDefine> physicalGroupDefineSet = new HashSet<PhysicalGroupDefine>();
            List<PhysicalGroupRelation> physicalGroupRelationList = new ArrayList<PhysicalGroupRelation>();
            Set<WorkflowDefine> workflowDefineSet = new HashSet<WorkflowDefine>();
            if (CollectionUtils.isNotEmpty(treeDefineList)) {
                for (TreeDefine treeDefine : treeDefineList) {
                    if (TreeDefine.T_TABLE.equals(treeDefine.getType())) {
                        PhysicalTableDefine physicalTable = XarchListener.getBean(PhysicalTableDefineService.class).getByID(treeDefine.getTableId());
                        physicalTableMap.put(physicalTable.getId(), physicalTable);
                        if (StringUtil.isNotEmpty(physicalTable.getLogicTableCode())) {
                            if (ConstantVar.TableClassification.VIEW.equals(physicalTable.getClassification())) {
                                String[] logicTableCodes = physicalTable.getLogicTableCode().split("'");
                                for (String logicTableCode : logicTableCodes) {
                                    logicTableDefineSet.add(XarchListener.getBean(LogicTableDefineService.class).getByCode(logicTableCode));
                                }
                            } else {
                                logicTableDefineSet.add(XarchListener.getBean(LogicTableDefineService.class).getByCode(physicalTable.getLogicTableCode()));
                            }
                        }
                        physicalTableDefineSet.add(physicalTable);
                    } else if (TreeDefine.T_GROUP.equals(treeDefine.getType())) {
                        PhysicalGroupDefine physicalGroup = XarchListener.getBean(PhysicalGroupDefineService.class).getByID(treeDefine.getDbId());
                        if (physicalGroup != null) {
                            physicalGroupDefineSet.add(physicalGroup);
                        }
                    } else if (TreeDefine.T_COFLOW.equals(treeDefine.getType())) {
                        WorkflowDefine workflowDefine = XarchListener.getBean(WorkflowDefineService.class).getByID(treeDefine.getDbId());
                        if (workflowDefine != null) {
                            workflowDefineSet.add(workflowDefine);
                        }
                    }
                }
                if (containSelfDefineInfo) {
                    if (CollectionUtils.isNotEmpty(physicalGroupDefineSet)) {
                        for (PhysicalGroupDefine physicalGroup : physicalGroupDefineSet) {
                            logicGroupDefineSet.add(XarchListener.getBean(LogicGroupDefineService.class).getByCode(physicalGroup.getLogicGroupCode()));
                            List<PhysicalGroupRelation> tempPhysicalGroupRelationList = XarchListener.getBean(PhysicalGroupRelationService.class)
                                    .findByGroupId(physicalGroup.getId());
                            if (CollectionUtils.isNotEmpty(tempPhysicalGroupRelationList)) {
                                for (PhysicalGroupRelation physicalGroupRelation : tempPhysicalGroupRelationList) {
                                    PhysicalTableDefine physicalTable = XarchListener.getBean(PhysicalTableDefineService.class).getByID(
                                            physicalGroupRelation.getTableId());
                                    physicalTableMap.put(physicalTable.getId(), physicalTable);
                                    physicalTableDefineSet.add(physicalTable);
                                }
                                physicalGroupRelationList.addAll(tempPhysicalGroupRelationList);
                            }
                        }
                    }
                    if (CollectionUtils.isNotEmpty(logicGroupDefineSet)) {
                        for (LogicGroupDefine logicGroup : logicGroupDefineSet) {
                            List<LogicGroupRelation> tempLogicGroupRelationList = XarchListener.getBean(LogicGroupRelationService.class).getByGroupCode(
                                    logicGroup.getCode());
                            if (CollectionUtils.isNotEmpty(tempLogicGroupRelationList)) {
                                for (LogicGroupRelation logicGroupRelation : tempLogicGroupRelationList) {
                                    logicTableDefineSet.add(XarchListener.getBean(LogicTableDefineService.class).getByCode(logicGroupRelation.getTableCode()));
                                }
                                logicGroupRelationList.addAll(tempLogicGroupRelationList);
                            }
                            List<LogicTableRelation> tempLogicTableRelationList = XarchListener.getBean(LogicTableRelationService.class)
                                    .getTableRelationsByGroupCode(logicGroup.getCode());
                            if (CollectionUtils.isNotEmpty(tempLogicTableRelationList)) {
                                for (LogicTableRelation logicTableRelation : logicTableRelationList) {
                                    logicTableDefineSet.add(XarchListener.getBean(LogicTableDefineService.class).getByCode(logicTableRelation.getTableCode()));
                                }
                                logicTableRelationList.addAll(tempLogicTableRelationList);
                            }
                        }
                    }
                    Set<String> workflowVersionIdSet = new HashSet<String>();
                    if (CollectionUtils.isNotEmpty(workflowDefineSet)) {
                        // 工作流信息
                        Map<String, Object> workflowsMap = getWorkflowsMap(workflowDefineSet, physicalTableMap, physicalTableDefineSet);
                        if (!workflowsMap.isEmpty()) {
                            map.put("workflow", workflowsMap);
                            String dataDir = initJsonPath.replace("config.json", "");
                            createWorkflowFile(dataDir, workflowsMap);
                            List<WorkflowVersion> workflowVersionList = (List<WorkflowVersion>) workflowsMap.get("WorkflowVersion");
                            if (CollectionUtils.isNotEmpty(workflowVersionList)) {
                                for (WorkflowVersion wv : workflowVersionList) {
                                    workflowVersionIdSet.add(wv.getId());
                                }
                            }
                        }
                    }
                    // 表树
                    Map<String, TableTree> tableTreeMap = new HashMap<String, TableTree>();
                    List<LogicTableDefine> logicTableDefineList = new ArrayList<LogicTableDefine>();
                    if (CollectionUtils.isNotEmpty(logicTableDefineSet)) {
                        for (LogicTableDefine logicTableDefine : logicTableDefineSet) {
                            if (logicTableDefine == null) {
                                continue;
                            }
                            logicTableDefineList.add(logicTableDefine);
                            getTableTrees(logicTableDefine.getTableTreeId(), tableTreeMap);
                        }
                    }
                    map.put("logicTables", logicTableDefineList);
                    // 逻辑表中的字段
                    List<ColumnDefine> logicColumnList = new ArrayList<ColumnDefine>();
                    map.put("logicColumns", logicColumnList);
                    if (CollectionUtils.isNotEmpty(logicTableDefineSet)) {
                        for (LogicTableDefine logicTable : logicTableDefineSet) {
                            logicColumnList.addAll(XarchListener.getBean(ColumnDefineService.class).findByTableId(logicTable.getCode()));
                        }
                    }
                    map.put("logicGroups", logicGroupDefineSet);
                    map.put("logicGroupRelations", logicGroupRelationList);
                    map.put("logicTableRelations", logicTableRelationList);
                    // 表信息
                    List<Map<String, Object>> physicalTableList = new ArrayList<Map<String, Object>>();
                    map.put("physicalTables", physicalTableList);
                    if (CollectionUtils.isNotEmpty(physicalTableDefineSet)) {
                        for (PhysicalTableDefine physicalTable : physicalTableDefineSet) {
                            physicalTableList.add(physicalTableDefine2Map(physicalTable));
                            getTableTrees(physicalTable.getTableTreeId(), tableTreeMap);
                        }
                    }
                    map.put("physicalGroups", physicalGroupDefineSet);
                    map.put("physicalGroupRelations", physicalGroupRelationList);
                    if (!physicalTableMap.isEmpty()) {
                        // 字段关联定义信息
                        Map<String, Object> columnRelationsMap = getColumnRelationsMap(physicalTableMap);
                        map.put("columnRelations", columnRelationsMap);
                    }
                    // 应用定义信息
                    Map<String, Object> applicationMap = getApplicationMap(physicalTableMap.values(), moduleId, workflowVersionIdSet);
                    map.put("application", applicationMap);
                    map.put("tableTrees", tableTreeMap.values());
                }
            }
        } else if (ConstantVar.Component.Type.PHYSICAL_TABLE.equals(module.getType())) {
            Set<LogicTableDefine> logicTableDefineSet = new HashSet<LogicTableDefine>();
            // 物理表构件
            if (Module.L_1C.equals(module.getTemplateType())) {
                // 整张页面
                PhysicalTableDefine physicalTable1 = XarchListener.getBean(PhysicalTableDefineService.class).getByID(module.getTable1Id());
                if (StringUtil.isNotEmpty(physicalTable1.getLogicTableCode())) {
                    if (ConstantVar.TableClassification.VIEW.equals(physicalTable1.getClassification())) {
                        String[] logicTableCodes = physicalTable1.getLogicTableCode().split("'");
                        for (String logicTableCode : logicTableCodes) {
                            logicTableDefineSet.add(XarchListener.getBean(LogicTableDefineService.class).getByCode(logicTableCode));
                        }
                    } else {
                        logicTableDefineSet.add(XarchListener.getBean(LogicTableDefineService.class).getByCode(physicalTable1.getLogicTableCode()));
                    }
                }
                physicalTableMap.put(physicalTable1.getId(), physicalTable1);
                moduleMap.put("table1Id", physicalTable1.getTableName());
                moduleMap.put("area1Id", module.getArea1Id());
                moduleMap.put("area1Size", module.getArea1Size());
                // 表信息
                List<Map<String, Object>> physicalTableList = new ArrayList<Map<String, Object>>();
                map.put("physicalTables", physicalTableList);
                physicalTableList.add(physicalTableDefine2Map(physicalTable1));
            } else if (Module.L_2E.equals(module.getTemplateType())) {
                // 上下结构页面
                PhysicalTableDefine physicalTable1 = XarchListener.getBean(PhysicalTableDefineService.class).getByID(module.getTable1Id());
                PhysicalTableDefine physicalTable2 = XarchListener.getBean(PhysicalTableDefineService.class).getByID(module.getTable2Id());
                if (StringUtil.isNotEmpty(physicalTable1.getLogicTableCode())) {
                    if (ConstantVar.TableClassification.VIEW.equals(physicalTable1.getClassification())) {
                        String[] logicTableCodes = physicalTable1.getLogicTableCode().split("'");
                        for (String logicTableCode : logicTableCodes) {
                            logicTableDefineSet.add(XarchListener.getBean(LogicTableDefineService.class).getByCode(logicTableCode));
                        }
                    } else {
                        logicTableDefineSet.add(XarchListener.getBean(LogicTableDefineService.class).getByCode(physicalTable1.getLogicTableCode()));
                    }
                }
                if (StringUtil.isNotEmpty(physicalTable2.getLogicTableCode())) {
                    if (ConstantVar.TableClassification.VIEW.equals(physicalTable2.getClassification())) {
                        String[] logicTableCodes = physicalTable2.getLogicTableCode().split("'");
                        for (String logicTableCode : logicTableCodes) {
                            logicTableDefineSet.add(XarchListener.getBean(LogicTableDefineService.class).getByCode(logicTableCode));
                        }
                    } else {
                        logicTableDefineSet.add(XarchListener.getBean(LogicTableDefineService.class).getByCode(physicalTable2.getLogicTableCode()));
                    }
                }
                physicalTableMap.put(physicalTable1.getId(), physicalTable1);
                physicalTableMap.put(physicalTable2.getId(), physicalTable2);
                moduleMap.put("table1Id", physicalTable1.getTableName());
                moduleMap.put("area1Id", module.getArea1Id());
                moduleMap.put("area1Size", module.getArea1Size());
                moduleMap.put("table2Id", physicalTable2.getTableName());
                moduleMap.put("area2Id", module.getArea2Id());
                moduleMap.put("area2Size", module.getArea2Size());
                // 表信息
                List<Map<String, Object>> physicalTableList = new ArrayList<Map<String, Object>>();
                map.put("physicalTables", physicalTableList);
                physicalTableList.add(physicalTableDefine2Map(physicalTable1));
                physicalTableList.add(physicalTableDefine2Map(physicalTable2));
            } else if (Module.L_3E.equals(module.getTemplateType())) {
                // 上中下结构页面
                PhysicalTableDefine physicalTable1 = XarchListener.getBean(PhysicalTableDefineService.class).getByID(module.getTable1Id());
                PhysicalTableDefine physicalTable2 = XarchListener.getBean(PhysicalTableDefineService.class).getByID(module.getTable2Id());
                PhysicalTableDefine physicalTable3 = XarchListener.getBean(PhysicalTableDefineService.class).getByID(module.getTable3Id());
                if (StringUtil.isNotEmpty(physicalTable1.getLogicTableCode())) {
                    if (ConstantVar.TableClassification.VIEW.equals(physicalTable1.getClassification())) {
                        String[] logicTableCodes = physicalTable1.getLogicTableCode().split("'");
                        for (String logicTableCode : logicTableCodes) {
                            logicTableDefineSet.add(XarchListener.getBean(LogicTableDefineService.class).getByCode(logicTableCode));
                        }
                    } else {
                        logicTableDefineSet.add(XarchListener.getBean(LogicTableDefineService.class).getByCode(physicalTable1.getLogicTableCode()));
                    }
                }
                if (StringUtil.isNotEmpty(physicalTable2.getLogicTableCode())) {
                    if (ConstantVar.TableClassification.VIEW.equals(physicalTable2.getClassification())) {
                        String[] logicTableCodes = physicalTable2.getLogicTableCode().split("'");
                        for (String logicTableCode : logicTableCodes) {
                            logicTableDefineSet.add(XarchListener.getBean(LogicTableDefineService.class).getByCode(logicTableCode));
                        }
                    } else {
                        logicTableDefineSet.add(XarchListener.getBean(LogicTableDefineService.class).getByCode(physicalTable2.getLogicTableCode()));
                    }
                }
                if (StringUtil.isNotEmpty(physicalTable3.getLogicTableCode())) {
                    if (ConstantVar.TableClassification.VIEW.equals(physicalTable3.getClassification())) {
                        String[] logicTableCodes = physicalTable3.getLogicTableCode().split("'");
                        for (String logicTableCode : logicTableCodes) {
                            logicTableDefineSet.add(XarchListener.getBean(LogicTableDefineService.class).getByCode(logicTableCode));
                        }
                    } else {
                        logicTableDefineSet.add(XarchListener.getBean(LogicTableDefineService.class).getByCode(physicalTable3.getLogicTableCode()));
                    }
                }
                physicalTableMap.put(physicalTable1.getId(), physicalTable1);
                physicalTableMap.put(physicalTable2.getId(), physicalTable2);
                physicalTableMap.put(physicalTable3.getId(), physicalTable3);
                moduleMap.put("table1Id", physicalTable1.getTableName());
                moduleMap.put("area1Id", module.getArea1Id());
                moduleMap.put("area1Size", module.getArea1Size());
                moduleMap.put("table2Id", physicalTable2.getTableName());
                moduleMap.put("area2Id", module.getArea2Id());
                moduleMap.put("area2Size", module.getArea2Size());
                moduleMap.put("table3Id", physicalTable3.getTableName());
                moduleMap.put("area3Id", module.getArea3Id());
                moduleMap.put("area3Size", module.getArea3Size());
                // 表信息
                List<Map<String, Object>> physicalTableList = new ArrayList<Map<String, Object>>();
                map.put("physicalTables", physicalTableList);
                physicalTableList.add(physicalTableDefine2Map(physicalTable1));
                physicalTableList.add(physicalTableDefine2Map(physicalTable2));
                physicalTableList.add(physicalTableDefine2Map(physicalTable3));
            }
            if (!physicalTableMap.isEmpty()) {
                // 字段关联定义信息
                Map<String, Object> columnRelationsMap = getColumnRelationsMap(physicalTableMap);
                map.put("columnRelations", columnRelationsMap);
                if (containSelfDefineInfo) {
                    // 应用定义信息
                    Map<String, Object> applicationMap = getApplicationMap(physicalTableMap.values(), moduleId, null);
                    map.put("application", applicationMap);
                }
            }
            // 表树
            Map<String, TableTree> tableTreeMap = new HashMap<String, TableTree>();
            for (PhysicalTableDefine physicalTableDefine : physicalTableMap.values()) {
                getTableTrees(physicalTableDefine.getTableTreeId(), tableTreeMap);
            }
            List<LogicTableDefine> logicTableDefineList = new ArrayList<LogicTableDefine>();
            if (CollectionUtils.isNotEmpty(logicTableDefineSet)) {
                for (LogicTableDefine logicTableDefine : logicTableDefineSet) {
                    if (logicTableDefine == null) {
                        continue;
                    }
                    logicTableDefineList.add(logicTableDefine);
                    getTableTrees(logicTableDefine.getTableTreeId(), tableTreeMap);
                }
            }
            map.put("tableTrees", tableTreeMap.values());
            map.put("logicTables", logicTableDefineList);
            // 逻辑表中的字段
            List<ColumnDefine> logicColumnList = new ArrayList<ColumnDefine>();
            map.put("logicColumns", logicColumnList);
            if (CollectionUtils.isNotEmpty(logicTableDefineList)) {
                for (LogicTableDefine logicTable : logicTableDefineList) {
                    logicColumnList.addAll(XarchListener.getBean(ColumnDefineService.class).findByTableId(logicTable.getCode()));
                }
            }
        } else if (ConstantVar.Component.Type.LOGIC_TABLE.equals(module.getType())) {
            List<LogicGroupDefine> logicGroupDefineList = new ArrayList<LogicGroupDefine>();
            LogicGroupDefine logicGroupDefine = XarchListener.getBean(LogicGroupDefineService.class).getByCode(module.getLogicTableGroupCode());
            logicGroupDefineList.add(logicGroupDefine);
            List<LogicTableDefine> logicTableDefineList = XarchListener.getBean(LogicTableDefineService.class).getByLogicTableGroupCode(
                    module.getLogicTableGroupCode());
            List<LogicGroupRelation> logicGroupRelationList = XarchListener.getBean(LogicGroupRelationService.class).getByGroupCode(
                    module.getLogicTableGroupCode());
            List<LogicTableRelation> logicTableRelationList = XarchListener.getBean(LogicTableRelationService.class).getTableRelationsByGroupCode(
                    module.getLogicTableGroupCode());
            map.put("logicGroups", logicGroupDefineList);
            map.put("logicTables", logicTableDefineList);
            map.put("logicGroupRelations", logicGroupRelationList);
            map.put("logicTableRelations", logicTableRelationList);
            // 表树
            Map<String, TableTree> tableTreeMap = new HashMap<String, TableTree>();
            // 逻辑表中的字段
            List<ColumnDefine> columnDefineList = new ArrayList<ColumnDefine>();
            map.put("logicColumns", columnDefineList);
            if (CollectionUtils.isNotEmpty(logicTableDefineList)) {
                for (LogicTableDefine logicTable : logicTableDefineList) {
                    getTableTrees(logicTable.getTableTreeId(), tableTreeMap);
                    columnDefineList.addAll(XarchListener.getBean(ColumnDefineService.class).findByTableId(logicTable.getCode()));
                }
            }
            map.put("tableTrees", tableTreeMap.values());
            // 逻辑表构件
            if (Module.L_1C.equals(module.getTemplateType())) {
                // 整张页面
                moduleMap.put("table1Id", module.getTable1Id());
                moduleMap.put("area1Id", module.getArea1Id());
                moduleMap.put("area1Size", module.getArea1Size());
            } else if (Module.L_2E.equals(module.getTemplateType())) {
                // 上下结构页面
                moduleMap.put("table1Id", module.getTable1Id());
                moduleMap.put("area1Id", module.getArea1Id());
                moduleMap.put("area1Size", module.getArea1Size());
                moduleMap.put("table2Id", module.getTable2Id());
                moduleMap.put("area2Id", module.getArea2Id());
                moduleMap.put("area2Size", module.getArea2Size());
            } else if (Module.L_3L.equals(module.getTemplateType())) {
                // 左上下结构页面
                moduleMap.put("table1Id", module.getTable1Id());
                moduleMap.put("area1Id", module.getArea1Id());
                moduleMap.put("area1Size", module.getArea1Size());
                moduleMap.put("table2Id", module.getTable2Id());
                moduleMap.put("area2Id", module.getArea2Id());
                moduleMap.put("area2Size", module.getArea2Size());
                moduleMap.put("table3Id", module.getTable3Id());
                moduleMap.put("area3Id", module.getArea3Id());
                moduleMap.put("area3Size", module.getArea3Size());
            }
        } else if (ConstantVar.Component.Type.NO_TABLE.equals(module.getType())) {
            // 通用表构件
            moduleMap.put("table1Id", module.getTable1Id());
            moduleMap.put("area1Id", module.getArea1Id());
            moduleMap.put("area1Size", module.getArea1Size());
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(initJsonPath);
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(fos, map);
            fos.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return map;
    }

    /**
     * 准备工作流相关文件
     * 
     * @param dataDir 导出的工作流文件放置的文件夹
     * @param workflowsMap 工作流信息
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    private static void createWorkflowFile(String dataDir, Map<String, Object> workflowsMap) {
        // 工作流文件
        Set<String> wfXpdlFileNameSet = (Set<String>) workflowsMap.get("WFXpdlFileName");
        Set<String> wfPackIdAndVersionSet = (Set<String>) workflowsMap.get("WFPackIdAndVersion");
        File configDir = new File(ComponentFileUtil.getProjectPath() + "WEB-INF/conf/");
        File[] configDirFiles = configDir.listFiles();
        if (configDirFiles != null && configDirFiles.length > 0) {
            for (File file : configDirFiles) {
                String name = file.getName();
                if (name.endsWith(".xpdl") && wfXpdlFileNameSet.contains(name)) {
                    FileUtil.copyFile(ComponentFileUtil.getProjectPath() + "WEB-INF/conf/" + name, dataDir + name);
                }
            }
        }
        File tempxpdlDir = new File(ComponentFileUtil.getProjectPath() + "WEB-INF/conf/tempxpdl/");
        File[] tempxpdlDirFiles = tempxpdlDir.listFiles();
        if (tempxpdlDirFiles != null && tempxpdlDirFiles.length > 0) {
            File dataXpdlDir = new File(dataDir + "tempxpdl/");
            dataXpdlDir.mkdirs();
            for (File file : tempxpdlDirFiles) {
                String name = file.getName();
                if (name.endsWith(".xpdl") && wfXpdlFileNameSet.contains(name)) {
                    FileUtil.copyFile(ComponentFileUtil.getProjectPath() + "WEB-INF/conf/tempxpdl/" + name, dataDir + "tempxpdl/" + name);
                }
            }
        }
        // 更改Repository.xml文件
        SAXReader reader = new SAXReader();
        XMLWriter xmlWriter = null;
        Writer writer = null;
        try {
            File repositoryFile = new File(ComponentFileUtil.getProjectPath() + "WEB-INF/conf/Repository.xml");
            Document doc = reader.read(repositoryFile);
            Element root = doc.getRootElement();
            Element defineXmlFiles = root.element("DefineXmlFiles");
            if (defineXmlFiles != null) {
                Element defineXmlFile = null;
                for (Iterator parameterIterator = defineXmlFiles.elementIterator("DefineXmlFile"); parameterIterator.hasNext();) {
                    defineXmlFile = (Element) parameterIterator.next();
                    String packageId = defineXmlFile.attributeValue("packageId");
                    String packageVersion = defineXmlFile.attributeValue("packageVersion");
                    if (!wfPackIdAndVersionSet.contains(packageId + "$$" + packageVersion)) {
                        defineXmlFiles.remove(defineXmlFile);
                    }
                }
            }
            OutputFormat format = OutputFormat.createPrettyPrint();
            format.setEncoding("UTF-8");
            format.setNewLineAfterDeclaration(false);
            format.setIndent("\t");
            writer = new OutputStreamWriter(new FileOutputStream(dataDir + "Repository.xml"), "UTF-8");
            xmlWriter = new XMLWriter(writer, format);
            xmlWriter.write(doc);
            writer.flush();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (xmlWriter != null) {
                try {
                    xmlWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 将表定义实体类转换成Map
     * 
     * @param table 表定义实体类
     * @return Map<String, String>
     */
    @SuppressWarnings("unchecked")
    private static Map<String, Object> physicalTableDefine2Map(PhysicalTableDefine table) {
        Map<String, Object> tableMap = new HashMap<String, Object>();
        tableMap.put("id", table.getId());
        tableMap.put("tableId", table.getId());
        tableMap.put("showName", table.getShowName());
        tableMap.put("tablePrefix", table.getTablePrefix());
        tableMap.put("tableCode", table.getTableCode());
        tableMap.put("classification", table.getClassification());
        tableMap.put("tableType", table.getTableType());
        tableMap.put("tableName", table.getTableName());
        tableMap.put("logicTableCode", table.getLogicTableCode());
        tableMap.put("releaseWithData", table.getReleaseWithData());
        tableMap.put("remark", table.getRemark());
        tableMap.put("tableTreeId", table.getTableTreeId());
        tableMap.put("showOrder", table.getShowOrder());
        tableMap.put("createIndex", table.getCreateIndex());
        // 表中的字段
        List<ColumnDefine> columnDefineList = XarchListener.getBean(ColumnDefineService.class).findByTableId(table.getId());
        tableMap.put("columns", columnDefineList);
        // 表关系
        List<Map<String, String>> tableRelationList = new ArrayList<Map<String, String>>();
        tableMap.put("tableRelations", tableRelationList);
        List<Object[]> relationList = (List<Object[]>) XarchListener.getBean(TableRelationService.class).getAllTableRelations(table.getId());
        if (CollectionUtils.isNotEmpty(relationList)) {
            Map<String, String> relationMap = null;
            for (Object[] objArray : relationList) {
                relationMap = new HashMap<String, String>();
                relationMap.put("id", String.valueOf(objArray[0]));
                relationMap.put("sourceTableName", String.valueOf(objArray[2]));
                relationMap.put("sourceColumnName", String.valueOf(objArray[4]));
                relationMap.put("targetTableName", String.valueOf(objArray[6]));
                relationMap.put("targetColumnName", String.valueOf(objArray[8]));
                tableRelationList.add(relationMap);
            }
        }
        return tableMap;
    }

    /**
     * 获取表树
     * 
     * @param tableTreeId 表树ID
     * @param tableTreeMap 表树Map
     * @return String
     */
    public static void getTableTrees(String tableTreeId, Map<String, TableTree> tableTreeMap) {
        if (StringUtil.isNotEmpty(tableTreeId) && tableTreeMap.get(tableTreeId) == null) {
            TableTree tableTree = XarchListener.getBean(TableTreeService.class).getByID(tableTreeId);
            if (tableTree != null) {
                tableTreeMap.put(tableTreeId, tableTree);
                if (tableTree.getParentId() != null && !tableTree.getParentId().startsWith("-")) {
                    getTableTrees(tableTree.getParentId(), tableTreeMap);
                }
            }
        }
    }

    /**
     * 字段关联定义信息
     * 
     * @param tableMap 表
     * @return Map<String, Object>
     */
    private static Map<String, Object> getColumnRelationsMap(Map<String, PhysicalTableDefine> tableMap) {
        Map<String, Object> columnRelationsMap = new HashMap<String, Object>();
        String tableId = null;
        Map<String, Object> tempMap = null;
        List<ColumnRelation> columnRelationList = null;
        List<ColumnSplice> columnSpliceList = null;
        List<ColumnSplit> columnSplitList = null;
        List<ColumnBusiness> columnBusinessList = null;
        List<Map<String, String>> columnOperationList = null;
        for (Iterator<String> iterator = tableMap.keySet().iterator(); iterator.hasNext();) {
            tableId = iterator.next();
            tempMap = new HashMap<String, Object>();
            columnRelationsMap.put(tableId, tempMap);
            columnRelationList = XarchListener.getBean(ColumnRelationService.class).findByTableId(tableId);
            if (CollectionUtils.isNotEmpty(columnRelationList)) {
                tempMap.put("columnRelation", columnRelationList);
            }
            columnSpliceList = XarchListener.getBean(ColumnSpliceService.class).findByTableId(tableId);
            if (CollectionUtils.isNotEmpty(columnSpliceList)) {
                tempMap.put("columnSplice", columnSpliceList);
            }
            columnSplitList = XarchListener.getBean(ColumnSplitService.class).findByTableId(tableId);
            if (CollectionUtils.isNotEmpty(columnSplitList)) {
                tempMap.put("columnSplit", columnSplitList);
            }
            columnBusinessList = XarchListener.getBean(ColumnBusinessService.class).findByTableId(tableId);
            if (CollectionUtils.isNotEmpty(columnBusinessList)) {
                tempMap.put("columnBusiness", columnBusinessList);
            }
            List<Object[]> operationList = XarchListener.getBean(ColumnOperationService.class).findForExport(tableId);
            if (CollectionUtils.isNotEmpty(operationList)) {
                columnOperationList = new ArrayList<Map<String, String>>();
                tempMap.put("columnOperation", columnOperationList);
                Map<String, String> columnOperationMap = null;
                for (Object[] operationValues : operationList) {
                    columnOperationMap = new HashMap<String, String>();
                    columnOperationMap.put("id", StringUtil.null2empty(operationValues[0]));
                    columnOperationMap.put("columnRelationId", StringUtil.null2empty(operationValues[1]));
                    columnOperationMap.put("name", StringUtil.null2empty(operationValues[2]));
                    columnOperationMap.put("type", StringUtil.null2empty(operationValues[3]));
                    columnOperationMap.put("tableId", StringUtil.null2empty(operationValues[4]));
                    columnOperationMap.put("columnId", StringUtil.null2empty(operationValues[5]));
                    columnOperationMap.put("originTableName", StringUtil.null2empty(operationValues[6]));
                    columnOperationMap.put("originColumnName", StringUtil.null2empty(operationValues[7]));
                    columnOperationMap.put("operator", StringUtil.null2empty(operationValues[8]));
                    columnOperationList.add(columnOperationMap);
                }
            }
        }
        return columnRelationsMap;
    }

    /**
     * 工作流信息
     * 
     * @param workflowDefineSet 工作流定义
     * @param tableMap 表
     * @param moduleId 模块ID
     * @return Map<String, Object>
     */
    @SuppressWarnings("unchecked")
    private static Map<String, Object> getWorkflowsMap(Set<WorkflowDefine> workflowDefineSet, Map<String, PhysicalTableDefine> physicalTableMap,
            Set<PhysicalTableDefine> physicalTableDefineSet) {
        Map<String, Object> workflowsMap = new HashMap<String, Object>();
        List<WorkflowDefine> workflowDefineList = new ArrayList<WorkflowDefine>(workflowDefineSet);
        Set<String> workflowTreeIdSet = new HashSet<String>();
        List<WorkflowVersion> workflowVersionList = new ArrayList<WorkflowVersion>();
        List<WorkflowFormSetting> workflowFormSettingList = new ArrayList<WorkflowFormSetting>();
        List<WorkflowButtonSetting> workflowButtonSettingList = new ArrayList<WorkflowButtonSetting>();
        List<String[]> wfRelevantdataList = new ArrayList<String[]>();
        Set<String> wfXpdlFileNameSet = new HashSet<String>();
        Set<String> wfPackIdAndVersionSet = new HashSet<String>();
        if (CollectionUtils.isNotEmpty(workflowDefineList)) {
            List<WorkflowVersion> tempWorkflowVersionList = null;
            for (WorkflowDefine workflowDefine : workflowDefineList) {
                // 获取树节点
                workflowTreeIdSet.add(workflowDefine.getWorkflowTreeId());
                // 获取视图
                String viewName = WorkflowUtil.getViewName(workflowDefine.getWorkflowCode());
                PhysicalTableDefine viewTable = XarchListener.getBean(PhysicalTableDefineService.class).getByTableName(viewName);
                if (viewTable != null) {
                    physicalTableMap.put(viewTable.getId(), viewTable);
                    physicalTableDefineSet.add(viewTable);
                }
                // 获取相关表
                PhysicalTableDefine businessTable = XarchListener.getBean(PhysicalTableDefineService.class).getByID(workflowDefine.getBusinessTableId());
                if (businessTable != null) {
                    physicalTableMap.put(businessTable.getId(), businessTable);
                    physicalTableDefineSet.add(businessTable);
                }
                PhysicalTableDefine businessView = XarchListener.getBean(PhysicalTableDefineService.class).getByID(
                        WorkflowUtil.getViewId(workflowDefine.getId()));
                if (businessView != null) {
                    physicalTableMap.put(businessView.getId(), businessView);
                    physicalTableDefineSet.add(businessView);
                }
                if (ConstantVar.Judgment.YES.equals(workflowDefine.getEnableDocumentTable())) {
                    String documentTableName = WorkflowUtil.getDocumentTableName(workflowDefine.getWorkflowCode());
                    PhysicalTableDefine documentTable = XarchListener.getBean(PhysicalTableDefineService.class).getByTableName(documentTableName);
                    if (documentTable != null) {
                        physicalTableMap.put(documentTable.getId(), documentTable);
                        physicalTableDefineSet.add(documentTable);
                    }
                }
                if (ConstantVar.Judgment.YES.equals(workflowDefine.getEnableConfirmTable())) {
                    String confirmTableName = WorkflowUtil.getConfirmTableName(workflowDefine.getWorkflowCode());
                    PhysicalTableDefine confirmTable = XarchListener.getBean(PhysicalTableDefineService.class).getByTableName(confirmTableName);
                    if (confirmTable != null) {
                        physicalTableMap.put(confirmTable.getId(), confirmTable);
                        physicalTableDefineSet.add(confirmTable);
                    }
                }
                if (ConstantVar.Judgment.YES.equals(workflowDefine.getEnableAssistTable())) {
                    String assistTableName = WorkflowUtil.getAssistTableName(workflowDefine.getWorkflowCode());
                    PhysicalTableDefine assistTable = XarchListener.getBean(PhysicalTableDefineService.class).getByTableName(assistTableName);
                    if (assistTable != null) {
                        physicalTableMap.put(assistTable.getId(), assistTable);
                        physicalTableDefineSet.add(assistTable);
                    }
                }
                String packageId = WorkflowUtil.getPackageIdByCode(workflowDefine.getWorkflowCode());
                String processId = WorkflowUtil.getProcessIdByCode(workflowDefine.getWorkflowCode());
                // 获取工作流版本
                tempWorkflowVersionList = XarchListener.getBean(WorkflowVersionService.class).getByWorkflowId(workflowDefine.getId());
                if (CollectionUtils.isNotEmpty(tempWorkflowVersionList)) {
                    workflowVersionList.addAll(tempWorkflowVersionList);
                    for (WorkflowVersion workflowVersion : workflowVersionList) {
                        wfXpdlFileNameSet.add(WorkflowUtil.getFileName(workflowDefine.getWorkflowCode(), workflowVersion.getVersion()));
                        wfPackIdAndVersionSet.add(packageId + "$$" + workflowVersion.getVersion());
                    }
                }
                // 获取工作流相关数据
                List<String[]> list = DatabaseHandlerDao.getInstance().queryForList(
                        "select t.table_name,t.package_id,t.process_id from t_wf_relevantdata_tablename t where t.package_id='" + packageId
                                + "' and t.process_id='" + processId + "'");
                if (CollectionUtils.isNotEmpty(list)) {
                    wfRelevantdataList.addAll(list);
                }
            }
        }
        // 获取工作流分类树
        Map<String, WorkflowTree> workflowTreeMap = new HashMap<String, WorkflowTree>();
        if (CollectionUtils.isNotEmpty(workflowTreeIdSet)) {
            WorkflowTree workflowTree = null;
            for (String workflowTreeId : workflowTreeIdSet) {
                if (workflowTreeMap.get(workflowTreeId) == null) {
                    workflowTree = XarchListener.getBean(WorkflowTreeService.class).getByID(workflowTreeId);
                    if (workflowTree != null) {
                        workflowTreeMap.put(workflowTree.getId(), workflowTree);
                        // 工作流分类树父节点
                        String parentId = workflowTree.getParentId();
                        while (!"-1".equals(parentId)) {
                            if (workflowTreeMap.containsKey(parentId)) {
                                break;
                            }
                            workflowTree = XarchListener.getBean(WorkflowTreeService.class).getByID(parentId);
                            if (workflowTree != null) {
                                workflowTreeMap.put(workflowTree.getId(), workflowTree);
                                parentId = workflowTree.getParentId();
                            } else {
                                break;
                            }
                        }
                    }
                }
            }
        }
        if (CollectionUtils.isNotEmpty(workflowVersionList)) {
            for (WorkflowVersion workflowVersion : workflowVersionList) {
                workflowFormSettingList.addAll(XarchListener.getBean(WorkflowFormSettingService.class).findByWorkflowVersionId(workflowVersion.getId()));
                workflowButtonSettingList.addAll(XarchListener.getBean(WorkflowButtonSettingService.class).findByWorkflowVersionId(workflowVersion.getId()));
            }
        }
        // WorkflowTree、WorkflowDefine、WorkflowVersion、WorkflowFormSetting、WorkflowButtonSetting
        workflowsMap.put("WorkflowTree", new ArrayList<WorkflowTree>(workflowTreeMap.values()));
        workflowsMap.put("WorkflowDefine", workflowDefineList);
        workflowsMap.put("WorkflowVersion", workflowVersionList);
        workflowsMap.put("WorkflowFormSetting", workflowFormSettingList);
        workflowsMap.put("WorkflowButtonSetting", workflowButtonSettingList);
        workflowsMap.put("WorkflowRelevantdata", wfRelevantdataList);
        workflowsMap.put("WFXpdlFileName", wfXpdlFileNameSet);
        workflowsMap.put("WFPackIdAndVersion", wfPackIdAndVersionSet);
        return workflowsMap;
    }

    /**
     * 将应用定义转换成Map
     * 
     * @param tableDefineList 表集合
     * @param moduleId 模块ID
     * @param workflowVersionIdSet 工作流版本IDs
     * @return Map<String, Object>
     */
    private static Map<String, Object> getApplicationMap(Collection<PhysicalTableDefine> tableDefineList, String moduleId, Set<String> workflowVersionIdSet) {
        Map<String, Object> applicationMap = new HashMap<String, Object>();
        // 应用定义
        List<Map<String, Object>> appDefineList = new ArrayList<Map<String, Object>>();
        applicationMap.put("appDefines", appDefineList);
        // 检索定义
        List<Map<String, Object>> appSearchList = new ArrayList<Map<String, Object>>();
        applicationMap.put("appSearchs", appSearchList);
        // 列表字段定义
        List<Map<String, Object>> appColumnList = new ArrayList<Map<String, Object>>();
        applicationMap.put("appColumns", appColumnList);
        // 列表排序定义
        List<Map<String, Object>> appSortList = new ArrayList<Map<String, Object>>();
        applicationMap.put("appSorts", appSortList);
        // 界面定义
        List<Map<String, Object>> appFormList = new ArrayList<Map<String, Object>>();
        applicationMap.put("appForms", appFormList);
        // 报表定义
        List<Map<String, Object>> appReportList = new ArrayList<Map<String, Object>>();
        applicationMap.put("appReports", appReportList);
        // 列表按钮定义
        List<Map<String, Object>> appGridButtonList = new ArrayList<Map<String, Object>>();
        applicationMap.put("appGridButtons", appGridButtonList);
        // 按钮定义
        List<Map<String, Object>> appFormButtonList = new ArrayList<Map<String, Object>>();
        applicationMap.put("appFormButtons", appFormButtonList);
        Set<String> workflowBoxSet = new HashSet<String>();
        workflowBoxSet.add(AppDefine.DEFAULT_DEFINE_ID);
        workflowBoxSet.add(WorkflowUtil.Box.applyfor);
        workflowBoxSet.add(WorkflowUtil.Box.complete);
        workflowBoxSet.add(WorkflowUtil.Box.hasdone);
        workflowBoxSet.add(WorkflowUtil.Box.hasread);
        workflowBoxSet.add(WorkflowUtil.Box.todo);
        workflowBoxSet.add(WorkflowUtil.Box.toread);
        List<AppDefine> tempAppDefineList = null;
        for (Iterator<PhysicalTableDefine> iterator = tableDefineList.iterator(); iterator.hasNext();) {
            PhysicalTableDefine physicalTableDefine = iterator.next();
            tempAppDefineList = XarchListener.getBean(AppDefineService.class).findByTableIdAndUserId(physicalTableDefine.getId(), CommonUtil.SUPER_ADMIN_ID);
            if (CollectionUtils.isNotEmpty(tempAppDefineList)) {
                for (AppDefine appDefine : tempAppDefineList) {
                    if (appDefine != null) {
                        if ((AppDefine.DEFAULT_DEFINE_ID.equals(appDefine.getMenuId()))
                                || (workflowBoxSet.contains(appDefine.getComponentVersionId()) && CollectionUtils.isNotEmpty(workflowVersionIdSet) && workflowVersionIdSet
                                        .contains(appDefine.getMenuId()))) {
                            Map<String, Object> appDefineMap = new HashMap<String, Object>();
                            appDefineList.add(appDefineMap);
                            appDefineMap.put("tableId", appDefine.getTableId());
                            appDefineMap.put("componentVersionId", appDefine.getComponentVersionId());
                            appDefineMap.put("menuId", appDefine.getMenuId());
                            appDefineMap.put("searched", appDefine.getSearched());
                            appDefineMap.put("columned", appDefine.getColumned());
                            appDefineMap.put("sorted", appDefine.getSorted());
                            appDefineMap.put("formed", appDefine.getFormed());
                            appDefineMap.put("reported", appDefine.getReported());
                            appDefineMap.put("gridButtoned", appDefine.getGridButtoned());
                            appDefineMap.put("formButtoned", appDefine.getFormButtoned());
                            // 检索定义
                            if ("1".equals(appDefine.getSearched())) {
                                Map<String, Object> appSearchMap = new HashMap<String, Object>();
                                appSearchList.add(appSearchMap);
                                appSearchMap.put("tableId", appDefine.getTableId());
                                appSearchMap.put("componentVersionId", appDefine.getComponentVersionId());
                                appSearchMap.put("menuId", appDefine.getMenuId());
                                // 检索框定义
                                AppSearchPanel tempAppSearchPanel = XarchListener.getBean(AppSearchPanelService.class).findByFk(physicalTableDefine.getId(),
                                        appDefine.getComponentVersionId(), appDefine.getMenuId(), CommonUtil.SUPER_ADMIN_ID);
                                if (tempAppSearchPanel != null) {
                                    appSearchMap.put("colspan", tempAppSearchPanel.getColspan());
                                }
                                // 基本检索定义
                                List<AppSearch> tempAppSearchList = XarchListener.getBean(AppSearchService.class).findByFk(physicalTableDefine.getId(),
                                        appDefine.getComponentVersionId(), appDefine.getMenuId(), CommonUtil.SUPER_ADMIN_ID);
                                if (CollectionUtils.isNotEmpty(tempAppSearchList)) {
                                    List<Map<String, String>> baseSearchColumnList = new ArrayList<Map<String, String>>();
                                    appSearchMap.put("baseSearchColumns", baseSearchColumnList);
                                    int showOrder = 0;
                                    for (AppSearch appSearch : tempAppSearchList) {
                                        Map<String, String> searchColumnMap = new HashMap<String, String>();
                                        searchColumnMap.put("columnId", appSearch.getColumnId());
                                        searchColumnMap.put("columnName", appSearch.getColumnName());
                                        searchColumnMap.put("showName", appSearch.getShowName());
                                        searchColumnMap.put("filterType", appSearch.getFilterType());
                                        searchColumnMap.put("showOrder", "" + showOrder);
                                        showOrder++;
                                        baseSearchColumnList.add(searchColumnMap);
                                    }
                                }
                            }
                            // 列表字段定义
                            if ("1".equals(appDefine.getColumned())) {
                                List<AppColumn> tempAppColumnList = XarchListener.getBean(AppColumnService.class).findByFk(physicalTableDefine.getId(),
                                        appDefine.getComponentVersionId(), appDefine.getMenuId(), CommonUtil.SUPER_ADMIN_ID);
                                if (CollectionUtils.isNotEmpty(tempAppColumnList)) {
                                    Map<String, Object> appColumnMap = new HashMap<String, Object>();
                                    appColumnList.add(appColumnMap);
                                    appColumnMap.put("tableId", appDefine.getTableId());
                                    appColumnMap.put("componentVersionId", appDefine.getComponentVersionId());
                                    appColumnMap.put("menuId", appDefine.getMenuId());
                                    AppGrid tempAppGrid = XarchListener.getBean(AppGridService.class).findByFk(physicalTableDefine.getId(),
                                            appDefine.getComponentVersionId(), appDefine.getMenuId(), CommonUtil.SUPER_ADMIN_ID);
                                    appColumnMap.put("hasRowNumber", tempAppGrid.getHasRowNumber());
                                    appColumnMap.put("dblclick", tempAppGrid.getDblclick());
                                    appColumnMap.put("searchType", tempAppGrid.getSearchType());
                                    appColumnMap.put("pageable", tempAppGrid.getPageable());
                                    appColumnMap.put("adaptive", tempAppGrid.getAdaptive());
                                    appColumnMap.put("opeColWidth", tempAppGrid.getOpeColWidth());
                                    appColumnMap.put("opeColName", tempAppGrid.getOpeColName());
                                    appColumnMap.put("opeColWidth", tempAppGrid.getOpeColWidth());
                                    appColumnMap.put("headerSetting", tempAppGrid.getHeaderSetting());
                                    List<Map<String, String>> gridColumnList = new ArrayList<Map<String, String>>();
                                    appColumnMap.put("gridColumns", gridColumnList);
                                    int showOrder = 0;
                                    for (AppColumn appColumn : tempAppColumnList) {
                                        Map<String, String> defineColumnMap = new HashMap<String, String>();
                                        defineColumnMap.put("columnId", appColumn.getColumnId());
                                        defineColumnMap.put("columnName", appColumn.getColumnName());
                                        defineColumnMap.put("columnType", appColumn.getColumnType());
                                        defineColumnMap.put("columnAlias", appColumn.getColumnAlias());
                                        defineColumnMap.put("showName", appColumn.getShowName());
                                        defineColumnMap.put("align", appColumn.getAlign());
                                        defineColumnMap.put("width", String.valueOf(appColumn.getWidth()));
                                        defineColumnMap.put("type", appColumn.getType());
                                        defineColumnMap.put("url", appColumn.getUrl());
                                        defineColumnMap.put("showOrder", "" + showOrder);
                                        showOrder++;
                                        gridColumnList.add(defineColumnMap);
                                    }
                                }
                            }
                            // 列表排序定义
                            if ("1".equals(appDefine.getSorted())) {
                                List<AppSort> tempAppSortList = XarchListener.getBean(AppSortService.class).findByFk(physicalTableDefine.getId(),
                                        appDefine.getComponentVersionId(), appDefine.getMenuId(), CommonUtil.SUPER_ADMIN_ID);
                                if (CollectionUtils.isNotEmpty(tempAppSortList)) {
                                    Map<String, Object> appSortMap = new HashMap<String, Object>();
                                    appSortList.add(appSortMap);
                                    appSortMap.put("tableId", appDefine.getTableId());
                                    appSortMap.put("componentVersionId", appDefine.getComponentVersionId());
                                    appSortMap.put("menuId", appDefine.getMenuId());
                                    List<Map<String, String>> gridSortColumnList = new ArrayList<Map<String, String>>();
                                    appSortMap.put("sortColumns", gridSortColumnList);
                                    int showOrder = 0;
                                    for (AppSort appSort : tempAppSortList) {
                                        Map<String, String> appSortColumnMap = new HashMap<String, String>();
                                        appSortColumnMap.put("columnId", appSort.getColumnId());
                                        appSortColumnMap.put("columnName", appSort.getColumnName());
                                        appSortColumnMap.put("sortType", appSort.getSortType());
                                        appSortColumnMap.put("showOrder", "" + showOrder);
                                        showOrder++;
                                        gridSortColumnList.add(appSortColumnMap);
                                    }
                                }
                            }
                            // 界面定义
                            if ("1".equals(appDefine.getFormed())) {
                                AppForm appForm = XarchListener.getBean(AppFormService.class).findByFk(physicalTableDefine.getId(),
                                        appDefine.getComponentVersionId(), appDefine.getMenuId());
                                List<AppFormElement> defineColumnList = XarchListener.getBean(AppFormElementService.class).findByFk(
                                        physicalTableDefine.getId(), appDefine.getComponentVersionId(), appDefine.getMenuId());
                                if (CollectionUtils.isNotEmpty(defineColumnList)) {
                                    Map<String, Object> appFormMap = new HashMap<String, Object>();
                                    appFormList.add(appFormMap);
                                    appFormMap.put("tableId", appDefine.getTableId());
                                    appFormMap.put("componentVersionId", appDefine.getComponentVersionId());
                                    appFormMap.put("menuId", appDefine.getMenuId());
                                    appFormMap.put("colspan", appForm.getColspan());
                                    appFormMap.put("border", appForm.getBorder());
                                    appFormMap.put("type", appForm.getBorder());
                                    List<Map<String, String>> formElementList = new ArrayList<Map<String, String>>();
                                    appFormMap.put("formElements", formElementList);
                                    int showOrder = 0;
                                    if (CollectionUtils.isNotEmpty(defineColumnList)) {
                                        for (AppFormElement defineColumn : defineColumnList) {
                                            Map<String, String> defineColumnMap = new HashMap<String, String>();
                                            defineColumnMap.put("columnId", defineColumn.getColumnId());
                                            defineColumnMap.put("showName", defineColumn.getShowName());
                                            defineColumnMap.put("colspan", String.valueOf(defineColumn.getColspan()));
                                            defineColumnMap.put("spacePercent", String.valueOf(defineColumn.getSpacePercent()));
                                            defineColumnMap.put("required", defineColumn.getRequired());
                                            defineColumnMap.put("readonly", defineColumn.getReadonly());
                                            defineColumnMap.put("hidden", defineColumn.getHidden());
                                            defineColumnMap.put("defaultValue", defineColumn.getDefaultValue());
                                            defineColumnMap.put("kept", defineColumn.getKept());
                                            defineColumnMap.put("increase", defineColumn.getIncrease());
                                            defineColumnMap.put("inherit", defineColumn.getInherit());
                                            defineColumnMap.put("validation", defineColumn.getValidation());
                                            defineColumnMap.put("tooltip", defineColumn.getTooltip());
                                            defineColumnMap.put("showOrder", "" + showOrder);
                                            showOrder++;
                                            formElementList.add(defineColumnMap);
                                        }
                                    }
                                }
                            }
                            // 报表定义
                            if ("1".equals(appDefine.getReported())) {
                                List<AppReport> tempAppReportList = XarchListener.getBean(AppReportService.class).findByFk(physicalTableDefine.getId(),
                                        appDefine.getComponentVersionId(), appDefine.getMenuId(), CommonUtil.SUPER_ADMIN_ID);
                                if (CollectionUtils.isNotEmpty(tempAppReportList)) {
                                    Map<String, Object> appReportMap = new HashMap<String, Object>();
                                    appReportList.add(appReportMap);
                                    appReportMap.put("tableId", appDefine.getTableId());
                                    appReportMap.put("componentVersionId", appDefine.getComponentVersionId());
                                    appReportMap.put("menuId", appDefine.getMenuId());
                                    List<Map<String, String>> gridReportList = new ArrayList<Map<String, String>>();
                                    appReportMap.put("reports", gridReportList);
                                    int showOrder = 0;
                                    for (AppReport appReport : tempAppReportList) {
                                        Map<String, String> defineColumnMap = new HashMap<String, String>();
                                        defineColumnMap.put("reportId", appReport.getReportId());
                                        defineColumnMap.put("showOrder", "" + showOrder);
                                        showOrder++;
                                        gridReportList.add(defineColumnMap);
                                    }
                                }
                            }
                            // 列表工具条定义
                            if ("1".equals(appDefine.getGridButtoned())) {
                                List<AppButton> tempAppButtonList = XarchListener.getBean(AppButtonService.class).findByFk(physicalTableDefine.getId(),
                                        appDefine.getComponentVersionId(), appDefine.getMenuId(), AppButton.BUTTON_GRID);
                                if (CollectionUtils.isNotEmpty(tempAppButtonList)) {
                                    Map<String, Object> appButtonMap = new HashMap<String, Object>();
                                    appGridButtonList.add(appButtonMap);
                                    appButtonMap.put("tableId", appDefine.getTableId());
                                    appButtonMap.put("componentVersionId", appDefine.getComponentVersionId());
                                    appButtonMap.put("menuId", appDefine.getMenuId());
                                    List<Map<String, String>> buttonList = new ArrayList<Map<String, String>>();
                                    appButtonMap.put("buttons", buttonList);
                                    int showOrder = 0;
                                    for (AppButton appButton : tempAppButtonList) {
                                        Map<String, String> buttonMap = new HashMap<String, String>();
                                        buttonMap.put("buttonCode", appButton.getButtonCode());
                                        buttonMap.put("buttonName", appButton.getButtonName());
                                        buttonMap.put("showName", appButton.getShowName());
                                        buttonMap.put("display", appButton.getDisplay());
                                        buttonMap.put("remark", appButton.getRemark());
                                        buttonMap.put("showOrder", "" + showOrder);
                                        showOrder++;
                                        buttonList.add(buttonMap);
                                    }
                                }
                            }
                            // 表单工具条定义
                            if ("1".equals(appDefine.getFormButtoned())) {
                                List<AppButton> tempAppButtonList = XarchListener.getBean(AppButtonService.class).findByFk(physicalTableDefine.getId(),
                                        appDefine.getComponentVersionId(), appDefine.getMenuId(), AppButton.BUTTON_FORM);
                                if (CollectionUtils.isNotEmpty(tempAppButtonList)) {
                                    Map<String, Object> appButtonMap = new HashMap<String, Object>();
                                    appFormButtonList.add(appButtonMap);
                                    appButtonMap.put("tableId", appDefine.getTableId());
                                    appButtonMap.put("componentVersionId", appDefine.getComponentVersionId());
                                    appButtonMap.put("menuId", appDefine.getMenuId());
                                    List<Map<String, String>> buttonList = new ArrayList<Map<String, String>>();
                                    appButtonMap.put("buttons", buttonList);
                                    int showOrder = 0;
                                    for (AppButton appButton : tempAppButtonList) {
                                        Map<String, String> buttonMap = new HashMap<String, String>();
                                        buttonMap.put("buttonCode", appButton.getButtonCode());
                                        buttonMap.put("buttonName", appButton.getButtonName());
                                        buttonMap.put("showName", appButton.getShowName());
                                        buttonMap.put("display", appButton.getDisplay());
                                        buttonMap.put("remark", appButton.getRemark());
                                        buttonMap.put("showOrder", "" + showOrder);
                                        showOrder++;
                                        buttonList.add(buttonMap);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return applicationMap;
    }

    /**
     * 创建构件版本中的表
     * 
     * @param componentVersion 构件版本
     */
    public static void createComponentTable(ComponentVersion componentVersion) {
        List<ComponentTable> componentTableList = XarchListener.getBean(ComponentTableService.class).getComponentTableList(componentVersion.getId());
        if (CollectionUtils.isNotEmpty(componentTableList)) {
            List<ComponentColumn> componentColumnList = null;
            StringBuffer createTableSql = null;
            StringBuffer alterSql = null;
            String dataType = null;
            for (ComponentTable componentTable : componentTableList) {
                componentColumnList = XarchListener.getBean(ComponentColumnService.class).getByComponentVersionIdAndTableId(componentVersion.getId(),
                        componentTable.getId());
                if (DatabaseHandlerDao.getInstance().tableExists(componentTable.getName())) {
                    for (ComponentColumn column : componentColumnList) {
                        alterSql = new StringBuffer();
                        if (DatabaseHandlerDao.getInstance().columnExists(componentTable.getName(), column.getName())) {
                            if (column.getLength() != null
                                    && column.getLength().intValue() > DatabaseHandlerDao.getInstance().getColumnLength(componentTable.getName(),
                                            column.getName())) {
                                alterSql.append("alter table " + componentTable.getName());
                                if (DatabaseHandlerDao.DB_ORACLE.equals(DatabaseHandlerDao.getDbType())) {
                                    alterSql.append(" modify " + column.getName() + " ");
                                } else if (DatabaseHandlerDao.DB_SQLSERVER.equals(DatabaseHandlerDao.getDbType())) {
                                    alterSql.append(" alter " + column.getName() + " ");
                                } else if (DatabaseHandlerDao.DB_MYSQL.equals(DatabaseHandlerDao.getDbType())) {
                                    alterSql.append(" change " + column.getName() + " " + column.getName() + " ");
                                } else if (DatabaseHandlerDao.DB_DAMING.equals(DatabaseHandlerDao.getDbType())) {
                                    alterSql.append(" modify " + column.getName() + " ");
                                }
                                alterSql.append(PreviewUtil.getDataType(column.getType(), column.getLength()));
                                if (column.getDefaultValue() != null && !"".equals(column.getDefaultValue())) {
                                    if ("数字型".equals(column.getType())) {
                                        alterSql.append(" default " + Integer.valueOf(column.getDefaultValue()));
                                    } else {
                                        alterSql.append(" default '" + column.getDefaultValue() + "'");
                                    }
                                }
                                DatabaseHandlerDao.getInstance().executeSql(alterSql.toString());
                            }
                        } else {
                            alterSql.append("alter table " + componentTable.getName() + " add " + column.getName() + " ");
                            alterSql.append(PreviewUtil.getDataType(column.getType(), column.getLength()));
                            if (column.getDefaultValue() != null && !"".equals(column.getDefaultValue())) {
                                if ("数字型".equals(column.getType())) {
                                    alterSql.append(" default " + Integer.valueOf(column.getDefaultValue()));
                                } else {
                                    alterSql.append(" default '" + column.getDefaultValue() + "'");
                                }
                            }
                            DatabaseHandlerDao.getInstance().executeSql(alterSql.toString());
                        }
                    }
                } else {
                    createTableSql = new StringBuffer();
                    createTableSql.append("create table " + componentTable.getName() + "(");
                    for (ComponentColumn column : componentColumnList) {
                        dataType = PreviewUtil.getDataType(column.getType(), column.getLength());
                        if (dataType != null) {
                            createTableSql.append(column.getName());
                            createTableSql.append(" ");
                            createTableSql.append(dataType);
                            if (column.getDefaultValue() != null && !"".equals(column.getDefaultValue())) {
                                if ("数字型".equals(column.getType())) {
                                    createTableSql.append(" default " + Integer.valueOf(column.getDefaultValue()));
                                } else {
                                    createTableSql.append(" default '" + column.getDefaultValue() + "'");
                                }
                            }
                            if ("1".equals(column.getIsNull())) {
                                createTableSql.append(" not null");
                            }
                            createTableSql.append(",");
                        }
                    }
                    createTableSql.deleteCharAt(createTableSql.length() - 1);
                    createTableSql.append(")");
                    DatabaseHandlerDao.getInstance().executeSql(createTableSql.toString());
                }
            }
        }
    }

    /**
     * 执行构件版本的更新数据脚本
     * 
     * @param componentVersion 构件版本
     */
    public static void execComponentDataSql(ComponentVersion componentVersion) {
        String sqlPath = getCompUncompressPath() + componentVersion.getComponent().getAlias() + "_" + componentVersion.getVersion() + "/sql/";
        if (DatabaseHandlerDao.isOracle()) {
            sqlPath += "oracle/data.sql";
        } else if (DatabaseHandlerDao.isSqlserver()) {
            sqlPath += "sqlserver/data.sql";
        } else {
            return;
        }
        String dataSql = loadSql(sqlPath).replaceAll("\\r\\n", " ").trim();
        if (!"".equals(dataSql)) {
            try {
                DatabaseHandlerDao.getInstance().jdbcExecuteSql(dataSql);
            } catch (Exception e) {
                log.error("更新构件'" + componentVersion.getComponent().getAlias() + "_" + componentVersion.getVersion() + "'数据脚本失败！", e);
            }
        }
    }

    /**
     * 读取oracle和sqlserver脚本
     * 
     * @return String
     */
    public static String loadSql(String sqlFile) {
        InputStream sqlFileIn = null;
        try {
            File file = new File(sqlFile);
            if (!file.exists()) {
                return "";
            }
            sqlFileIn = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(sqlFileIn, "UTF-8"));
            StringBuffer sqlSb = new StringBuffer();
            String line = "";
            while ((line = reader.readLine()) != null) {
                sqlSb.append(line);
            }
            return sqlSb.toString();
        } catch (Exception e) {
            log.error("加载数据库补丁脚本失败！", e);
            throw new RuntimeException(e);
        } finally {
            if (sqlFileIn != null) {
                try {
                    sqlFileIn.close();
                } catch (IOException e) {
                    log.error("加载数据库补丁脚本失败！", e);
                }
            }
        }
    }

    /**
     * 准备构件配置信息
     * 
     * @param assembleComponentVersion 组合构件构件版本
     * @param logicTableDefineSet 组合构件构件涉及到的所有逻辑表
     * @param initJsonPath 构件配置信息文件路径
     */
    public static boolean createAssembleConfigJsonFile(ComponentVersion assembleComponentVersion, Set<LogicTableDefine> logicTableDefineSet, String initJsonPath) {
        boolean flag = true;
        FileOutputStream fos = null;
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            Set<ComponentVersion> baseComponentVersions = XarchListener.getBean(ConstructService.class).getComponentVersionOfConstruct(
                    assembleComponentVersion.getId());
            List<Map<String, Object>> baseComponentVersionMapList = new ArrayList<Map<String, Object>>();
            Map<String, Object> componentVersionJsonData = null;
            Set<String> componentAreaIdSet = new HashSet<String>();
            for (ComponentVersion baseComponentVersion : baseComponentVersions) {
                componentVersionJsonData = getComponentVersionJsonData(baseComponentVersion);
                baseComponentVersionMapList.add(componentVersionJsonData);
                componentAreaIdSet.add(baseComponentVersion.getAreaId());
            }
            Map<String, Object> constructMap = getAssembleComponentJsonData(assembleComponentVersion);
            Set<ComponentVersion> assembleComponentVersionSet = XarchListener.getBean(ConstructService.class).getAssemblesOfConstruct(
                    assembleComponentVersion.getId());
            StringBuilder assembleIds = new StringBuilder();
            assembleIds.append(assembleComponentVersion.getId());
            if (CollectionUtils.isNotEmpty(assembleComponentVersionSet)) {
                for (ComponentVersion assembleCv : assembleComponentVersionSet) {
                    assembleIds.append(",").append(assembleCv.getId());
                }
            }
            List<ConstructFilter> constructFilterList = XarchListener.getBean(ConstructFilterService.class).getByTopComVersionIds(assembleIds.toString());
            List<ConstructFilterDetail> constructFilterDetailList = XarchListener.getBean(ConstructFilterDetailService.class).getByTopComVersionIds(
                    assembleIds.toString());
            constructMap.put("constructFilters", constructFilterList);
            constructMap.put("constructFilterDetails", constructFilterDetailList);
            map.put("BaseComponentVersions", baseComponentVersionMapList);
            map.put("Assemble", constructMap);
            // 获取基础构件分类树
            Map<String, ComponentArea> componentAreaMap = new HashMap<String, ComponentArea>();
            if (CollectionUtils.isNotEmpty(componentAreaIdSet)) {
                ComponentArea componentArea = null;
                for (String componentAreaId : componentAreaIdSet) {
                    if (componentAreaMap.get(componentAreaId) == null) {
                        componentArea = XarchListener.getBean(ComponentAreaService.class).getByID(componentAreaId);
                        if (componentArea != null) {
                            componentAreaMap.put(componentArea.getId(), componentArea);
                            // 构件分类树父节点
                            String parentId = componentArea.getParentId();
                            while (!"-1".equals(parentId)) {
                                if (componentAreaMap.containsKey(parentId)) {
                                    break;
                                }
                                componentArea = XarchListener.getBean(ComponentAreaService.class).getByID(parentId);
                                if (componentArea != null) {
                                    componentAreaMap.put(componentArea.getId(), componentArea);
                                    parentId = componentArea.getParentId();
                                } else {
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            List<ComponentArea> componentAreaList = new ArrayList<ComponentArea>(componentAreaMap.values());
            map.put("BaseComponentAreas", componentAreaList);
            if (CollectionUtils.isNotEmpty(logicTableDefineSet)) {
                List<String> reserveZoneNameList = new ArrayList<String>();
                for (LogicTableDefine logicTableDefine : logicTableDefineSet) {
                    AppDefineUtil.getCommonReserveZones(reserveZoneNameList, logicTableDefine.getCode(), null);
                }
                List<ComponentReserveZone> commonReserveZoneList = XarchListener.getBean(ComponentReserveZoneService.class).getAllCommonReserveZone();
                if (CollectionUtils.isNotEmpty(commonReserveZoneList)) {
                    for (Iterator<ComponentReserveZone> it = commonReserveZoneList.iterator(); it.hasNext();) {
                        if (!reserveZoneNameList.contains(it.next().getName())) {
                            it.remove();
                        }
                    }
                }
                if (CollectionUtils.isNotEmpty(commonReserveZoneList)) {
                    // 获取该组合构件下涉及的所有构件，包括组合构件
                    Set<ComponentVersion> componentVersionOfConstruct = XarchListener.getBean(ConstructService.class).getComponentVersionOfConstruct(
                            assembleComponentVersion.getId(), null);
                    Set<String> componentVersionIdOfConstruct = new HashSet<String>();
                    for (ComponentVersion cv : componentVersionOfConstruct) {
                        componentVersionIdOfConstruct.add(cv.getId());
                    }
                    List<Map<String, Object>> commonConstructDetailMapList = new ArrayList<Map<String, Object>>();
                    Map<String, Object> commonConstructDetailMap = null;
                    List<Map<String, Object>> constructDetailMapList = null;
                    List<ConstructDetail> commonConstructDetailList = null;
                    ConstructDetail commonConstructDetail = null;
                    Map<String, String> commonReserveZoneMap = null;
                    for (ComponentReserveZone commonReserveZone : commonReserveZoneList) {
                        commonConstructDetailMap = new HashMap<String, Object>();
                        commonConstructDetailMap.put("commonReserveZone", commonReserveZone);
                        commonConstructDetailList = XarchListener.getBean(ConstructDetailService.class).getByReserveZoneIdOfCommonBinding(
                                commonReserveZone.getId());
                        if (CollectionUtils.isNotEmpty(commonConstructDetailList)) {
                            for (Iterator<ConstructDetail> it = commonConstructDetailList.iterator(); it.hasNext();) {
                                commonConstructDetail = it.next();
                                if (StringUtil.isNotEmpty(commonConstructDetail.getComponentVersionId())
                                        && !componentVersionIdOfConstruct.contains(commonConstructDetail.getComponentVersionId())) {
                                    it.remove();
                                }
                            }
                            commonReserveZoneMap = new HashMap<String, String>();
                            commonReserveZoneMap.put(commonReserveZone.getId(), commonReserveZone.getName());
                            constructDetailMapList = getConstructDetailJsonData(commonConstructDetailList, commonReserveZoneMap, false);
                            commonConstructDetailMap.put("constructDetails", constructDetailMapList);
                        }
                        commonConstructDetailMapList.add(commonConstructDetailMap);
                    }
                    map.put("CommonConstructDetails", commonConstructDetailMapList);
                }
            }
            fos = new FileOutputStream(initJsonPath);
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(fos, map);
            fos.flush();
        } catch (Exception e) {
            flag = false;
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return flag;
    }

    /**
     * 获取组合构件中的所有信息
     * 
     * @param assembleComponentVersion 组合构件构件版本
     * @return Map<String, Object>
     */
    private static Map<String, Object> getAssembleComponentJsonData(ComponentVersion assembleComponentVersion) {
        Construct construct = XarchListener.getBean(ConstructService.class).getByAssembleComponentVersionId(assembleComponentVersion.getId());
        List<ConstructDetail> constructDetailList = XarchListener.getBean(ConstructDetailService.class).getByConstructId(construct.getId());
        List<ConstructSelfParam> constructSelfParamList = XarchListener.getBean(ConstructSelfParamService.class).getByConstructId(construct.getId());
        List<ConstructInputParam> constructInputParamList = XarchListener.getBean(ConstructInputParamService.class).getByConstructId(construct.getId());
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("assembleComponentVersion", getComponentVersionJsonData(assembleComponentVersion));
        Map<String, String> constructMap = new HashMap<String, String>();
        map.put("construct", constructMap);
        constructMap.put("id", construct.getId());
        constructMap.put("baseComponentVersionId", construct.getBaseComponentVersionId());
        constructMap.put("assembleComponentVersionId", assembleComponentVersion.getId());
        Map<String, String> reserveZoneMap = new HashMap<String, String>();
        List<ComponentReserveZone> reserveZoneList = XarchListener.getBean(ComponentReserveZoneService.class).findAll();
        for (ComponentReserveZone reserveZone : reserveZoneList) {
            reserveZoneMap.put(reserveZone.getId(), reserveZone.getName());
        }
        reserveZoneMap.put("TREE", "TREE");
        List<Map<String, Object>> constructDetailMapList = getConstructDetailJsonData(constructDetailList, reserveZoneMap, true);
        map.put("constructDetails", constructDetailMapList);
        List<Map<String, String>> constructSelfParamMapList = new ArrayList<Map<String, String>>();
        map.put("constructSelfParams", constructSelfParamMapList);
        if (CollectionUtils.isNotEmpty(constructSelfParamList)) {
            Map<String, String> contructSelfParamMap = null;
            for (ConstructSelfParam constructSelfParam : constructSelfParamList) {
                contructSelfParamMap = new HashMap<String, String>();
                contructSelfParamMap.put("id", constructSelfParam.getId());
                contructSelfParamMap.put("componentVersionId", constructSelfParam.getComponentVersionId());
                contructSelfParamMap.put("constructId", constructSelfParam.getConstructId());
                contructSelfParamMap.put("selfParamId", constructSelfParam.getSelfParamId());
                contructSelfParamMap.put("name", constructSelfParam.getName());
                contructSelfParamMap.put("type", constructSelfParam.getType());
                contructSelfParamMap.put("value", StringUtil.null2empty(constructSelfParam.getValue()));
                contructSelfParamMap.put("remark", StringUtil.null2empty(constructSelfParam.getRemark()));
                contructSelfParamMap.put("options", StringUtil.null2empty(constructSelfParam.getOptions()));
                contructSelfParamMap.put("text", StringUtil.null2empty(constructSelfParam.getText()));
                constructSelfParamMapList.add(contructSelfParamMap);
            }
        }
        List<Map<String, String>> constructInputParamMapList = new ArrayList<Map<String, String>>();
        map.put("constructInputParams", constructInputParamMapList);
        if (CollectionUtils.isNotEmpty(constructInputParamList)) {
            Map<String, String> constructInputParamMap = null;
            for (ConstructInputParam constructInputParam : constructInputParamList) {
                constructInputParamMap = new HashMap<String, String>();
                constructInputParamMap.put("id", constructInputParam.getId());
                constructInputParamMap.put("constructId", constructInputParam.getConstructId());
                constructInputParamMap.put("inputParamId", constructInputParam.getInputParamId());
                constructInputParamMap.put("name", constructInputParam.getName());
                constructInputParamMap.put("value", StringUtil.null2empty(constructInputParam.getValue()));
                constructInputParamMapList.add(constructInputParamMap);
            }
        }
        return map;
    }

    /**
     * 获取ConstructDetail的json信息
     * 
     * @param constructDetailList
     * @param reserveZoneMap 预留区ID和预留区名称
     * @param cascade 是否级联获取
     * @return List<Map<String, Object>>
     */
    private static List<Map<String, Object>> getConstructDetailJsonData(List<ConstructDetail> constructDetailList, Map<String, String> reserveZoneMap,
            boolean cascade) {
        List<Map<String, Object>> constructDetailMapList = new ArrayList<Map<String, Object>>();
        if (CollectionUtils.isNotEmpty(constructDetailList)) {
            Map<String, Object> constructDetailMap = null;
            ComponentVersion detailComponentVersion = null;
            Map<String, String> constructDetailBaseInfo = null;
            List<ConstructDetailSelfParam> detailSelfParamList = null;
            List<Map<String, String>> detailSelfParamMapList = null;
            Map<String, String> detailSelfParamMap = null;
            List<ConstructFunction> constructFunctionList = null;
            List<Map<String, String>> constructFunctionMapList = null;
            Map<String, String> constructFunctionMap = null;
            List<ConstructCallback> constructCallbackList = null;
            List<Map<String, String>> constructCallbackMapList = null;
            Map<String, String> constructCallbackMap = null;
            for (ConstructDetail constructDetail : constructDetailList) {
                constructDetailMap = new HashMap<String, Object>();
                constructDetailBaseInfo = new HashMap<String, String>();
                constructDetailMap.put("baseInfo", constructDetailBaseInfo);
                constructDetailBaseInfo.put("id", constructDetail.getId());
                constructDetailBaseInfo.put("constructId", constructDetail.getConstructId());
                constructDetailBaseInfo.put("componentVersionId", StringUtil.null2empty(constructDetail.getComponentVersionId()));
                constructDetailBaseInfo.put("reserveZoneId", constructDetail.getReserveZoneId());
                constructDetailBaseInfo.put("reserveZoneName", StringUtil.null2empty(reserveZoneMap.get(constructDetail.getReserveZoneId())));
                constructDetailBaseInfo.put("isCommonReserveZone", constructDetail.getIsCommonReserveZone());
                constructDetailBaseInfo.put("buttonCode", StringUtil.null2empty(constructDetail.getButtonCode()));
                constructDetailBaseInfo.put("buttonName", StringUtil.null2empty(constructDetail.getButtonName()));
                constructDetailBaseInfo.put("buttonDisplayName", StringUtil.null2empty(constructDetail.getButtonDisplayName()));
                constructDetailBaseInfo.put("buttonType", StringUtil.null2empty(constructDetail.getButtonType()));
                constructDetailBaseInfo.put("parentButtonCode", StringUtil.null2empty(constructDetail.getParentButtonCode()));
                constructDetailBaseInfo.put("buttonCls", StringUtil.null2empty(constructDetail.getButtonCls()));
                constructDetailBaseInfo.put("buttonIcon", StringUtil.null2empty(constructDetail.getButtonIcon()));
                constructDetailBaseInfo.put("buttonSource", StringUtil.null2empty(constructDetail.getButtonSource()));
                constructDetailBaseInfo.put("position", StringUtil.null2empty(constructDetail.getPosition()));
                constructDetailBaseInfo.put("showOrder", StringUtil.null2zero(constructDetail.getShowOrder()));
                constructDetailBaseInfo.put("width", constructDetail.getWidth());
                constructDetailBaseInfo.put("height", constructDetail.getHeight());
                constructDetailBaseInfo.put("treeNodeType", StringUtil.null2empty(constructDetail.getTreeNodeType()));
                constructDetailBaseInfo.put("treeNodeProperty", StringUtil.null2empty(constructDetail.getTreeNodeProperty()));
                constructDetailBaseInfo.put("assembleType", constructDetail.getAssembleType());
                constructDetailBaseInfo.put("beforeClickJs", StringUtil.null2empty(constructDetail.getBeforeClickJs()));
                constructDetailBaseInfo.put("searchComboOptions", StringUtil.null2empty(constructDetail.getSearchComboOptions()));
                detailSelfParamList = XarchListener.getBean(ConstructDetailSelfParamService.class).getByConstructDetailId(constructDetail.getId());
                if (CollectionUtils.isNotEmpty(detailSelfParamList)) {
                    detailSelfParamMapList = new ArrayList<Map<String, String>>();
                    constructDetailMap.put("constructDetailSelfParams", detailSelfParamMapList);
                    for (ConstructDetailSelfParam detailSelfParam : detailSelfParamList) {
                        detailSelfParamMap = new HashMap<String, String>();
                        detailSelfParamMap.put("id", detailSelfParam.getId());
                        detailSelfParamMap.put("componentVersionId", detailSelfParam.getComponentVersionId());
                        detailSelfParamMap.put("constructDetailId", detailSelfParam.getConstructDetailId());
                        detailSelfParamMap.put("selfParamId", detailSelfParam.getSelfParamId());
                        detailSelfParamMap.put("name", detailSelfParam.getName());
                        detailSelfParamMap.put("type", detailSelfParam.getType());
                        detailSelfParamMap.put("value", StringUtil.null2empty(detailSelfParam.getValue()));
                        detailSelfParamMap.put("remark", StringUtil.null2empty(detailSelfParam.getRemark()));
                        detailSelfParamMap.put("options", StringUtil.null2empty(detailSelfParam.getOptions()));
                        detailSelfParamMap.put("text", StringUtil.null2empty(detailSelfParam.getText()));
                        detailSelfParamMapList.add(detailSelfParamMap);
                    }
                }
                constructFunctionList = XarchListener.getBean(ConstructFunctionService.class).getByConstructDetailId(constructDetail.getId());
                if (CollectionUtils.isNotEmpty(constructFunctionList)) {
                    constructFunctionMapList = new ArrayList<Map<String, String>>();
                    constructDetailMap.put("constructFunctions", constructFunctionMapList);
                    for (ConstructFunction constructFunction : constructFunctionList) {
                        constructFunctionMap = new HashMap<String, String>();
                        constructFunctionMap.put("id", constructFunction.getId());
                        constructFunctionMap.put("constructDetailId", constructFunction.getConstructDetailId());
                        constructFunctionMap.put("functionId", constructFunction.getFunctionId());
                        constructFunctionMap.put("outputParamId", constructFunction.getOutputParamId());
                        constructFunctionMap.put("inputParamId", constructFunction.getInputParamId());
                        constructFunctionMapList.add(constructFunctionMap);
                    }
                }
                constructCallbackList = XarchListener.getBean(ConstructCallbackService.class).getByConstructDetailId(constructDetail.getId());
                if (CollectionUtils.isNotEmpty(constructCallbackList)) {
                    constructCallbackMapList = new ArrayList<Map<String, String>>();
                    constructDetailMap.put("constructCallbacks", constructCallbackList);
                    for (ConstructCallback constructCallback : constructCallbackList) {
                        constructCallbackMap = new HashMap<String, String>();
                        constructCallbackMap.put("id", constructCallback.getId());
                        constructCallbackMap.put("constructDetailId", constructCallback.getConstructDetailId());
                        constructCallbackMap.put("callbackId", constructCallback.getCallbackId());
                        constructCallbackMap.put("outputParamId", StringUtil.null2empty(constructCallback.getOutputParamId()));
                        constructCallbackMap.put("inputParamId", StringUtil.null2empty(constructCallback.getInputParamId()));
                        constructCallbackMapList.add(constructCallbackMap);
                    }
                }
                if (cascade && StringUtil.isNotEmpty(constructDetail.getComponentVersionId())) {
                    detailComponentVersion = XarchListener.getBean(ComponentVersionService.class).getByID(constructDetail.getComponentVersionId());
                    if (ConstantVar.Component.Type.ASSEMBLY.equals(detailComponentVersion.getComponent().getType())) {
                        constructDetailMap.put("assembleInfo", getAssembleComponentJsonData(detailComponentVersion));
                    }
                }
                constructDetailMapList.add(constructDetailMap);
            }
        }
        return constructDetailMapList;
    }

    /**
     * 获取构件版本JSON信息
     * 
     * @param componentVersion 构件版本
     * @return Map<String, Object>
     */
    private static Map<String, Object> getComponentVersionJsonData(ComponentVersion componentVersion) {
        Map<String, Object> componentVersionMap = new HashMap<String, Object>();
        Map<String, String> baseInfo = new HashMap<String, String>();
        componentVersionMap.put("baseInfo", baseInfo);
        baseInfo.put("id", componentVersion.getId());
        baseInfo.put("version", componentVersion.getVersion());
        baseInfo.put("views", componentVersion.getViews());
        baseInfo.put("url", componentVersion.getUrl());
        baseInfo.put("remark", StringUtil.null2empty(componentVersion.getRemark()));
        baseInfo.put("areaId", componentVersion.getAreaId());
        baseInfo.put("path", StringUtil.null2empty(componentVersion.getPath()));
        baseInfo.put("isPackage", componentVersion.getIsPackage());
        baseInfo.put("packageTime", componentVersion.getPackageTime());
        baseInfo.put("beforeClickJs", componentVersion.getBeforeClickJs());
        baseInfo.put("systemParamConfig", StringUtil.null2zero(componentVersion.getSystemParamConfig()));
        baseInfo.put("assembleAreaId", StringUtil.null2empty(componentVersion.getAssembleAreaId()));
        baseInfo.put("buttonUse", StringUtil.null2zero(componentVersion.getButtonUse()));
        baseInfo.put("menuUse", StringUtil.null2zero(componentVersion.getMenuUse()));
        Map<String, String> componentMap = new HashMap<String, String>();
        componentVersionMap.put("Component", componentMap);
        componentMap.put("id", componentVersion.getComponent().getId());
        componentMap.put("code", componentVersion.getComponent().getCode());
        componentMap.put("name", componentVersion.getComponent().getName());
        componentMap.put("alias", componentVersion.getComponent().getAlias());
        componentMap.put("type", componentVersion.getComponent().getType());
        List<ComponentReserveZone> reserveZoneList = XarchListener.getBean(ComponentReserveZoneService.class).getByComponentVersionId(componentVersion.getId());
        if (CollectionUtils.isNotEmpty(reserveZoneList)) {
            componentVersionMap.put("ComponentReserveZones", reserveZoneList);
        }
        List<ComponentSystemParameter> systemParameterList = XarchListener.getBean(ComponentSystemParameterService.class).getByComponentVersionId(
                componentVersion.getId());
        if (CollectionUtils.isNotEmpty(systemParameterList)) {
            componentVersionMap.put("ComponentSystemParameters", systemParameterList);
        }
        List<ComponentSelfParam> selfParamList = XarchListener.getBean(ComponentSelfParamService.class).getByComponentVersionId(componentVersion.getId());
        if (CollectionUtils.isNotEmpty(selfParamList)) {
            componentVersionMap.put("ComponentSelfParams", selfParamList);
        }
        List<ComponentInputParam> inputParamList = XarchListener.getBean(ComponentInputParamService.class).getByComponentVersionId(componentVersion.getId());
        if (CollectionUtils.isNotEmpty(inputParamList)) {
            componentVersionMap.put("ComponentInputParams", inputParamList);
        }
        List<ComponentOutputParam> outputParamList = XarchListener.getBean(ComponentOutputParamService.class).getByComponentVersionId(componentVersion.getId());
        if (CollectionUtils.isNotEmpty(outputParamList)) {
            componentVersionMap.put("ComponentOutputParams", outputParamList);
        }
        List<ComponentFunction> functionList = XarchListener.getBean(ComponentFunctionService.class).getByComponentVersionId(componentVersion.getId());
        if (CollectionUtils.isNotEmpty(functionList)) {
            List<Map<String, Object>> functionMapList = new ArrayList<Map<String, Object>>();
            componentVersionMap.put("ComponentFunctions", functionMapList);
            Map<String, Object> functionMap = null;
            Map<String, String> functionBaseInfo = null;
            for (ComponentFunction function : functionList) {
                functionMap = new HashMap<String, Object>();
                functionMapList.add(functionMap);
                functionBaseInfo = new HashMap<String, String>();
                functionMap.put("baseInfo", functionBaseInfo);
                functionBaseInfo.put("id", function.getId());
                functionBaseInfo.put("name", function.getName());
                functionBaseInfo.put("page", function.getPage());
                functionBaseInfo.put("remark", StringUtil.null2empty(function.getRemark()));
                List<ComponentFunctionData> functionDataList = XarchListener.getBean(ComponentFunctionDataService.class).getByFunctionId(function.getId());
                if (CollectionUtils.isNotEmpty(functionDataList)) {
                    functionMap.put("ComponentFunctionDatas", functionDataList);
                }
            }
        }
        List<ComponentCallback> callbackList = XarchListener.getBean(ComponentCallbackService.class).getByComponentVersionId(componentVersion.getId());
        if (CollectionUtils.isNotEmpty(callbackList)) {
            List<Map<String, Object>> callbackMapList = new ArrayList<Map<String, Object>>();
            componentVersionMap.put("ComponentCallbacks", callbackMapList);
            Map<String, Object> callbackMap = null;
            Map<String, String> callbackBaseInfo = null;
            for (ComponentCallback callback : callbackList) {
                callbackMap = new HashMap<String, Object>();
                callbackMapList.add(callbackMap);
                callbackBaseInfo = new HashMap<String, String>();
                callbackMap.put("baseInfo", callbackBaseInfo);
                callbackBaseInfo.put("id", callback.getId());
                callbackBaseInfo.put("name", callback.getName());
                callbackBaseInfo.put("page", callback.getPage());
                callbackBaseInfo.put("remark", StringUtil.null2empty(callback.getRemark()));
                List<ComponentCallbackParam> callbackParamList = XarchListener.getBean(ComponentCallbackParamService.class).getByCallbackId(callback.getId());
                if (CollectionUtils.isNotEmpty(callbackParamList)) {
                    callbackMap.put("ComponentCallbackParams", callbackParamList);
                }
            }
        }
        List<ComponentButton> componentButtonList = XarchListener.getBean(ComponentButtonService.class).getByComponentVersionId(componentVersion.getId());
        if (CollectionUtils.isNotEmpty(componentButtonList)) {
            componentVersionMap.put("ComponentButtons", componentButtonList);
        }
        List<ComponentClass> componentClassList = XarchListener.getBean(ComponentClassService.class).getByComponentVersionId(componentVersion.getId());
        if (CollectionUtils.isNotEmpty(componentClassList)) {
            componentVersionMap.put("ComponentClasses", componentClassList);
        }
        List<ComponentJar> componentJarList = XarchListener.getBean(ComponentJarService.class).getByComponentVersionId(componentVersion.getId());
        if (CollectionUtils.isNotEmpty(componentJarList)) {
            componentVersionMap.put("ComponentJars", componentJarList);
        }
        List<ComponentTableColumnRelation> tableColumnRelationList = XarchListener.getBean(ComponentTableColumnRelationService.class).getByComponentVersionId(
                componentVersion.getId());
        if (CollectionUtils.isNotEmpty(tableColumnRelationList)) {
            List<Map<String, String>> tableColumnRelationMapList = new ArrayList<Map<String, String>>();
            componentVersionMap.put("ComponentTableColumnRelations", tableColumnRelationMapList);
            Map<String, String> tableColumnRelationMap = null;
            Set<String> tableIdSet = new HashSet<String>();
            for (ComponentTableColumnRelation tableColumnRelation : tableColumnRelationList) {
                tableColumnRelationMap = new HashMap<String, String>();
                tableColumnRelationMap.put("id", tableColumnRelation.getId());
                tableColumnRelationMap.put("tableId", tableColumnRelation.getTableId());
                tableColumnRelationMap.put("columnId", tableColumnRelation.getColumnId());
                tableColumnRelationMapList.add(tableColumnRelationMap);
                tableIdSet.add(tableColumnRelation.getTableId());
            }
            List<Map<String, Object>> tableMapList = new ArrayList<Map<String, Object>>();
            componentVersionMap.put("ComponentTables", tableMapList);
            Map<String, Object> tableMap = null;
            Map<String, String> tableBaseInfo = null;
            ComponentTable table = null;
            for (String tableId : tableIdSet) {
                table = XarchListener.getBean(ComponentTableService.class).getByID(tableId);
                if (table != null) {
                    tableMap = new HashMap<String, Object>();
                    tableBaseInfo = new HashMap<String, String>();
                    tableMap.put("baseInfo", tableBaseInfo);
                    tableBaseInfo.put("id", table.getId());
                    tableBaseInfo.put("name", table.getName());
                    tableBaseInfo.put("releaseWithData", table.getReleaseWithData());
                    tableBaseInfo.put("isSelfdefine", table.getIsSelfdefine());
                    List<ComponentColumn> componentColumnList = XarchListener.getBean(ComponentColumnService.class).getByComponentVersionIdAndTableId(
                            componentVersion.getId(), tableId);
                    if (CollectionUtils.isNotEmpty(componentColumnList)) {
                        tableMap.put("ComponentColumns", componentColumnList);
                    }
                }
            }
        }
        if (!ConstantVar.Component.Type.COMMON.equals(componentVersion.getComponent().getType())) {
            List<CommonComponentRelation> componentRelationList = XarchListener.getBean(CommonComponentRelationService.class).getByComponentVersionId(
                    componentVersion.getId());
            if (CollectionUtils.isNotEmpty(componentRelationList)) {
                componentVersionMap.put("CommonComponentRelations", componentRelationList);
            }
        }
        return componentVersionMap;
    }

    /**
     * 解析组合构件的配置信息
     * 
     * @param assembleConfigFile 组合构件的配置文件
     * @return Map<String, Object>
     * @throws IOException
     * @throws JsonMappingException
     * @throws JsonParseException
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> parseAssembleConfig(File assembleConfigFile) throws JsonParseException, JsonMappingException, IOException {
        Map<String, Object> configMap = new HashMap<String, Object>();
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> map = objectMapper.readValue(assembleConfigFile, Map.class);
        List<ComponentVersion> baseComponentVersionList = new ArrayList<ComponentVersion>();
        List<Map<String, Object>> baseComponentVersionMapList = (List<Map<String, Object>>) map.get("BaseComponentVersions");
        for (Map<String, Object> baseComponentVersionMap : baseComponentVersionMapList) {
            baseComponentVersionList.add(parseBaseComponentVersion(baseComponentVersionMap));
        }
        configMap.put("Assemble", map.get("Assemble"));
        configMap.put("BaseComponentAreas", map.get("BaseComponentAreas"));
        configMap.put("CommonConstructDetails", map.get("CommonConstructDetails"));
        configMap.put("BaseComponentVersions", baseComponentVersionList);
        return configMap;
    }

    /**
     * 解析基础构件版本
     * 
     * @param baseComponentVersionMap 基础构件版本信息
     * @return ComponentVersion
     */
    @SuppressWarnings("unchecked")
    private static ComponentVersion parseBaseComponentVersion(Map<String, Object> baseComponentVersionMap) {
        ComponentVersion baseComponentVersion = new ComponentVersion();
        Component component = new Component();
        Map<String, String> componentMap = (Map<String, String>) baseComponentVersionMap.get("Component");
        component.setId(componentMap.get("id"));
        component.setCode(componentMap.get("code"));
        component.setName(componentMap.get("name"));
        component.setAlias(componentMap.get("alias"));
        component.setType(componentMap.get("type"));
        baseComponentVersion.setComponent(component);
        Map<String, String> baseInfo = (Map<String, String>) baseComponentVersionMap.get("baseInfo");
        baseComponentVersion.setId(baseInfo.get("id"));
        baseComponentVersion.setVersion(baseInfo.get("version"));
        baseComponentVersion.setViews(baseInfo.get("views"));
        baseComponentVersion.setUrl(StringUtil.null2empty(baseInfo.get("url")));
        baseComponentVersion.setRemark(StringUtil.null2empty(baseInfo.get("remark")));
        baseComponentVersion.setAreaId(baseInfo.get("areaId"));
        baseComponentVersion.setPath(StringUtil.null2empty(baseInfo.get("path")));
        baseComponentVersion.setImportDate(new Date());
        baseComponentVersion.setSystemParamConfig(StringUtil.null2zero(baseInfo.get("systemParamConfig")));
        baseComponentVersion.setIsPackage("1");
        baseComponentVersion.setPackageTime(baseInfo.get("packageTime"));
        if (CfgCommonUtil.isReleasedSystem()) {
            baseComponentVersion.setIsSystemUsed(ConstantVar.Component.SystemUsed.YES);
        } else {
            baseComponentVersion.setIsSystemUsed(ConstantVar.Component.SystemUsed.NO);
        }
        baseComponentVersion.setBeforeClickJs(baseInfo.get("beforeClickJs"));
        List<Map<String, Object>> reserveZoneMapList = (List<Map<String, Object>>) baseComponentVersionMap.get("ComponentReserveZones");
        if (CollectionUtils.isNotEmpty(reserveZoneMapList)) {
            baseComponentVersion.setReserveZoneMapList(reserveZoneMapList);
        }
        List<Map<String, String>> systemParameterMapList = (List<Map<String, String>>) baseComponentVersionMap.get("ComponentSystemParameters");
        if (CollectionUtils.isNotEmpty(systemParameterMapList)) {
            baseComponentVersion.setSystemParamMapList(systemParameterMapList);
        }
        List<Map<String, String>> selfParamMapList = (List<Map<String, String>>) baseComponentVersionMap.get("ComponentSelfParams");
        if (CollectionUtils.isNotEmpty(selfParamMapList)) {
            baseComponentVersion.setSelfParamMapList(selfParamMapList);
        }
        List<Map<String, String>> inputParamMapList = (List<Map<String, String>>) baseComponentVersionMap.get("ComponentInputParams");
        if (CollectionUtils.isNotEmpty(inputParamMapList)) {
            baseComponentVersion.setInputParamMapList(inputParamMapList);
        }
        List<Map<String, String>> outputParamMapList = (List<Map<String, String>>) baseComponentVersionMap.get("ComponentOutputParams");
        if (CollectionUtils.isNotEmpty(outputParamMapList)) {
            baseComponentVersion.setOutputParamMapList(outputParamMapList);
        }
        List<Map<String, Object>> functionMapList = (List<Map<String, Object>>) baseComponentVersionMap.get("ComponentFunctions");
        if (CollectionUtils.isNotEmpty(functionMapList)) {
            baseComponentVersion.setFunctionMapList(functionMapList);
        }
        List<Map<String, Object>> callbackMapList = (List<Map<String, Object>>) baseComponentVersionMap.get("ComponentCallbacks");
        if (CollectionUtils.isNotEmpty(callbackMapList)) {
            baseComponentVersion.setCallbackMapList(callbackMapList);
        }
        List<Map<String, String>> buttonMapList = (List<Map<String, String>>) baseComponentVersionMap.get("ComponentButtons");
        if (CollectionUtils.isNotEmpty(buttonMapList)) {
            baseComponentVersion.setButtonMapList(buttonMapList);
        }
        List<Map<String, String>> classMapList = (List<Map<String, String>>) baseComponentVersionMap.get("ComponentClasses");
        if (CollectionUtils.isNotEmpty(classMapList)) {
            baseComponentVersion.setClassMapList(classMapList);
        }
        List<Map<String, String>> jarMapList = (List<Map<String, String>>) baseComponentVersionMap.get("ComponentJars");
        if (CollectionUtils.isNotEmpty(jarMapList)) {
            baseComponentVersion.setJarMapList(jarMapList);
        }
        List<Map<String, String>> tableColumnRelationMapList = (List<Map<String, String>>) baseComponentVersionMap.get("ComponentTableColumnRelations");
        if (CollectionUtils.isNotEmpty(tableColumnRelationMapList)) {
            baseComponentVersion.setComponentTableColumnRelationMapList(tableColumnRelationMapList);
        }
        List<Map<String, Object>> tableMapList = (List<Map<String, Object>>) baseComponentVersionMap.get("ComponentTables");
        if (CollectionUtils.isNotEmpty(tableMapList)) {
            baseComponentVersion.setTableMapList(tableMapList);
        }
        List<Map<String, String>> relationMapList = (List<Map<String, String>>) baseComponentVersionMap.get("CommonComponentRelations");
        if (CollectionUtils.isNotEmpty(relationMapList)) {
            baseComponentVersion.setCommonComponentRelationMapList(relationMapList);
        }
        return baseComponentVersion;
    }
}

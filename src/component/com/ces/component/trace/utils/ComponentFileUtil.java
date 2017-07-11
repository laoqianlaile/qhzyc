package com.ces.component.trace.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
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
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;



import com.ces.utils.BeanUtils;
import com.ces.xarch.core.web.listener.XarchListener;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ComponentFileUtil {

    private static Logger log = Logger.getLogger(ComponentFileUtil.class);

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
            // 开发构件配置文件路径
            props.put("component_config_lib.dev", componentLibPath + "component_config_lib/dev");
            // 组合构件配置文件路径
            props.put("component_config_lib.assemble", componentLibPath + "component_config_lib/assemble");
            // 自定义构件配置文件
            props.put("component_config_lib", componentLibPath + "component_config_lib");
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
            File releaseTempPath = new File(props.getProperty("release_temp_path"));
            if (!releaseTempPath.exists()) {
                releaseTempPath.mkdirs();
            }
            File updatePackagePath = new File(props.getProperty("update_package_path"));
            if (!updatePackagePath.exists()) {
                updatePackagePath.mkdirs();
            }
            File componentConfigLibDev = new File(props.getProperty("component_config_lib.dev"));
            if (componentConfigLibDev.exists()) {
                componentConfigLibDev.mkdirs();
            }
            File componentConfigLibAssemble = new File(props.getProperty("component_config_lib.assemble"));
            if (componentConfigLibAssemble.exists()) {
                componentConfigLibAssemble.mkdirs();
            }
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
    public static String getProjectRoot() {
    	String projectRoot = getProjectPath();
    	if (projectRoot.endsWith("/")) {
    		projectRoot = projectRoot.substring(0, projectRoot.length() - 1);
        }
    	projectRoot = projectRoot.substring(0, projectRoot.lastIndexOf("/"));
    	if (!projectRoot.endsWith("/")) {
    		projectRoot += "/";
        }
        return projectRoot;
    }

    /**
     * 获取工程路径
     * 
     * @return String
     */
    public static String getProjectPath() {
        if (StringUtil.isEmpty(projectPath)) {
            projectPath = XarchListener.appAbsolutepath.replaceAll("\\\\", "/");
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
     * 用于开发构件配置文件路径
     * 
     * @return String
     */
    public static String getComponentConfigLibDev() {
        String path = getProperties("component_config_lib.dev");
        path = path.replaceAll("\\\\", "/");
        if (!path.endsWith("/")) {
            path = path + "/";
        }
        return path;
    }

    /**
     * 用于组合构件配置文件路径
     * 
     * @return String
     */
    public static String getComponentConfigLibAssemble() {
        String path = getProperties("component_config_lib.assemble");
        path = path.replaceAll("\\\\", "/");
        if (!path.endsWith("/")) {
            path = path + "/";
        }
        return path;
    }

    /**
     * 用于构件配置文件路径
     * 
     * @return String
     */
    public static String getComponentLib() {
        String path = getProperties("component_config_lib");
        path = path.replaceAll("\\\\", "/");
        if (!path.endsWith("/")) {
            path = path + "/";
        }
        return path;
    }

    
}

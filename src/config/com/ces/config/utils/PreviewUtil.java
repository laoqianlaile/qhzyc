package com.ces.config.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;

import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.dhtmlx.entity.component.ComponentColumn;
import com.ces.config.dhtmlx.entity.component.ComponentVersion;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

public class PreviewUtil {

    public static synchronized String netConnect(String hostname, int port, String method, String url, String parameter, String cookie, boolean isCheck,
            int timeOut, String charSet, String authorization) {
        boolean flag = false;
        StringBuffer ret = new StringBuffer();
        do {
            try {
                InetAddress addr = InetAddress.getByName(hostname);
                Socket socket = new Socket(addr, port);
                socket.setSoTimeout(timeOut);
                BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), charSet));
                StringBuffer sb = new StringBuffer();
                sb.append(method
                        + " "
                        + url
                        + " HTTP/1.0\r\nAccept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\r\nAccept-Charset: GBK,utf-8;q=0.7,*;q=0.3\r\nAccept-Encoding: gzip,deflate,sdch\r\nAccept-Language: zh-CN,zh;q=0.8\r\nAuthorization: "
                        + authorization + "\r\nConnection: keep-alive\r\nReferer: http://" + hostname + "/\r\nHost: " + hostname
                        + "\r\nCache-Control: no-cache\r\n");
                if (parameter != null && !parameter.equals(""))
                    sb.append("Content-Length: " + parameter.length() + "\r\n");
                if (cookie != null && !cookie.equals(""))
                    sb.append("Cookie: " + cookie + "\r\n");
                sb.append("\r\n");
                sb.append(parameter);
                wr.write(sb.toString());
                wr.flush();
                BufferedReader rd = new BufferedReader(new InputStreamReader(socket.getInputStream(), charSet));
                String line;
                while ((line = rd.readLine()) != null) {
                    ret.append(line + "\n");
                }
                wr.close();
                rd.close();
            } catch (Exception e) {
                if (isCheck) {
                    flag = true;
                    ret = new StringBuffer();
                    continue;
                }
            }
            flag = false;
        } while (flag);
        return ret.toString();
    }

    public static boolean restartProject(String hostname, int port, String sysName, String tomcatUserName, String tomcatPassword) {
        String sign = Base64.encode((tomcatUserName + ":" + tomcatPassword).getBytes());
        String[] ret = netConnect(hostname, port, "GET", "/manager/html", "", "", false, 20000, "utf-8", "Basic " + sign).split("\n");
        StringBuffer cookies = new StringBuffer();
        for (int i = 0; i < ret.length; i++) {
            if (ret[i].startsWith("Set-Cookie")) {
                cookies.append(ret[i].substring(ret[i].indexOf("Set-Cookie: ") + 12, ret[i].length()) + ";");
            }
        }
        for (int i = 0; i < ret.length; i++) {
            if (ret[i].indexOf("\">/" + sysName + "</a></small></td>") != -1) {
                String url = "";
                if (ret[i + 6].indexOf("&nbsp;<small>Start</small>&nbsp;") != -1) {
                    System.out.println("重启项目: " + sysName);
                    url = ret[i + 8].substring(ret[i + 8].indexOf("action=\"") + 8, ret[i + 8].indexOf("\">")).replaceAll("&amp;", "&");
                } else {
                    System.out.println("启动项目: " + sysName);
                    url = ret[i + 6].substring(ret[i + 6].indexOf("action=\"") + 8, ret[i + 6].indexOf("\">")).replaceAll("&amp;", "&");
                }
                String[] rett = netConnect(hostname, port, "POST", url, "", cookies.toString(), false, 200000, "utf-8", "Basic " + sign).split("\n");
                for (int j = 0; j < rett.length; j++) {
                    if (rett[j].indexOf("\">/" + sysName + "</a></small></td>") != -1) {
                        if (rett[j + 6].indexOf("&nbsp;<small>Start</small>&nbsp;") != -1) {
                            return true;
                        }
                        return false;
                    }
                }
                return false;
            }
        }
        return false;
    }

    /**
     * 重新加载预览工程
     * 
     * @return boolean
     */
    public static boolean restartPreviewProject() {
        return restartProject(ComponentFileUtil.getPreviewIp(), Integer.parseInt(ComponentFileUtil.getPreviewPort()),
                ComponentFileUtil.getPreviewProjectName(), ComponentFileUtil.getTomcatUserName(), ComponentFileUtil.getTomcatUserPwd());
    }

    /**
     * 初始化用于预览的工程
     */
    public static void initPreviewProject() {
        String previewProjectPath = ComponentFileUtil.getPreviewProject() + "WebRoot/";
        String previewSrcProjectPath = ComponentFileUtil.getPreviewProject() + "src/";
        // 删除源文件
        FileUtil.deleteDir(previewSrcProjectPath + "component/com/ces/component/");
        FileUtil.deleteDir(previewSrcProjectPath + "selfdefine/com/ces/component/");
        // 删除page
        FileUtil.deleteDir(previewProjectPath + "cfg-resource/dhtmlx/views/selfdefine/");
        FileUtil.deleteDir(previewProjectPath + "cfg-resource/dhtmlx/views/component/");
        FileUtil.deleteDir(previewProjectPath + "cfg-resource/coral40/views/selfdefine/");
        FileUtil.deleteDir(previewProjectPath + "cfg-resource/coral40/views/component/");
        // 删除jar
        File componentLib = new File(previewProjectPath + "WEB-INF/component_lib/");
        if (componentLib.exists()) {
            File[] jarFiles = componentLib.listFiles();
            if (jarFiles != null && jarFiles.length > 0) {
                File jar = null;
                for (File jarFile : jarFiles) {
                    jar = new File(previewProjectPath + "WEB-INF/lib/" + jarFile.getName());
                    jar.delete();
                }
            }
            FileUtil.deleteDir(previewProjectPath + "WEB-INF/component_lib/");
        }
        // 删除class
        FileUtil.deleteDir(previewProjectPath + "WEB-INF/classes/com/ces/component/");
    }

    /**
     * 清除数据库中的构件表
     */
    @SuppressWarnings("rawtypes")
    public static void clearComponentTables() {
        List tableList = DatabaseHandlerDao.getInstance().queryForList("select table_name from t_xtpz_preview_table");
        if (CollectionUtils.isNotEmpty(tableList)) {
            for (Object tableName : tableList) {
                DatabaseHandlerDao.getInstance().executeSql("drop table " + tableName);
            }
            DatabaseHandlerDao.getInstance().executeSql("delete from t_xtpz_preview_table");
        }
    }

    /**
     * 将构件发布
     * 
     * @param componentVersionSet
     */
    public static void previewComponents(Set<ComponentVersion> componentVersionSet) {
        String project = ComponentFileUtil.getProjectPath();
        String previewProjectPath = ComponentFileUtil.getPreviewProject() + "WebRoot/";
        String previewSrcProjectPath = ComponentFileUtil.getPreviewProject() + "src/";
        // 1、初始化用于预览的工程
        PreviewUtil.initPreviewProject();
        // 2、清除数据库中的构件表
        PreviewUtil.clearComponentTables();
        // 3、将关联的表创建到数据库中
        createComponentTables(componentVersionSet);
        // 4、将构件信息拷贝到预览工程
        for (ComponentVersion componentVersion : componentVersionSet) {
            if (ConstantVar.Component.Type.SELF_DEFINE.indexOf(componentVersion.getComponent().getType()) != -1) {
                String packageName = componentVersion.getComponent().getName().toLowerCase();
                FileUtil.copyFolder(ComponentFileUtil.getSrcPath() + "selfdefine/com/ces/component/" + packageName, previewSrcProjectPath
                        + "selfdefine/com/ces/component/" + packageName);
                FileUtil.copyFolder(project + "cfg-resource/" + componentVersion.getViews() + "/views/selfdefine/" + packageName, previewProjectPath
                        + "cfg-resource/" + componentVersion.getViews() + "/views/selfdefine/" + packageName);
                FileUtil.copyFolder(project + "WEB-INF/classes/com/ces/component/" + packageName, previewProjectPath + "WEB-INF/classes/com/ces/component/"
                        + packageName);
            } else if (ConstantVar.Component.Type.PAGE.equals(componentVersion.getComponent().getType())
                    || ConstantVar.Component.Type.LOGIC.equals(componentVersion.getComponent().getType())
                    || ConstantVar.Component.Type.COMMON.equals(componentVersion.getComponent().getType())) {
                PreviewUtil.copyComponentFile(componentVersion, previewProjectPath);
            }
        }
        // 5、更新预览系统中的报表文件
        FileUtil.deleteDir(previewProjectPath + "cfg-resource/dhtmlx/views/config/appmanage/report/cll/");
        FileUtil.copyFolder(project + "cfg-resource/dhtmlx/views/config/appmanage/report/cll/", previewProjectPath
                + "cfg-resource/dhtmlx/views/config/appmanage/report/cll/");
    }

    /**
     * 将构件版本关联的表创建到数据库中
     * 
     * @param componentVersionSet 构件版本Set
     */
    @SuppressWarnings("unchecked")
    public static void createComponentTables(Set<ComponentVersion> componentVersionSet) {
        StringBuffer componentVersionIds = new StringBuffer();
        if (CollectionUtils.isNotEmpty(componentVersionSet)) {
            for (ComponentVersion componentVersion : componentVersionSet) {
                componentVersionIds.append("'");
                componentVersionIds.append(componentVersion.getId());
                componentVersionIds.append("',");
            }
            componentVersionIds.deleteCharAt(componentVersionIds.length() - 1);
        }
        if (componentVersionIds.length() > 0) {
            String sql = "select ct.name as tableName,cc.id,cc.name as columnName,cc.type,cc.length,cc.is_null,cc.default_value from t_xtpz_component_column cc, t_xtpz_component_table ct, t_xtpz_component_table_column ctc"
                    + " where cc.id=ctc.column_id and ct.id=ctc.table_id" + " and ctc.component_version_id in (" + componentVersionIds.toString() + ")";
            List<Object[]> tables = DatabaseHandlerDao.getInstance().queryForList(sql);
            if (CollectionUtils.isNotEmpty(tables)) {
                // 整理tables
                Map<String, List<ComponentColumn>> tableMap = new HashMap<String, List<ComponentColumn>>();
                ComponentColumn componentColumn = null;
                List<ComponentColumn> componentColumnList = null;
                for (Object[] objs : tables) {
                    componentColumnList = tableMap.get(objs[0]);
                    if (componentColumnList == null) {
                        componentColumnList = new ArrayList<ComponentColumn>();
                        tableMap.put(String.valueOf(objs[0]), componentColumnList);
                    }
                    componentColumn = new ComponentColumn();
                    componentColumn.setId(String.valueOf(objs[1]));
                    componentColumn.setName(String.valueOf(objs[2]));
                    componentColumn.setType(String.valueOf(objs[3]));
                    componentColumn.setLength(objs[4] == null ? null : Integer.valueOf(String.valueOf(objs[4])));
                    componentColumn.setIsNull(objs[5] == null ? "0" : String.valueOf(objs[5]));
                    componentColumn.setDefaultValue(objs[6] == null ? null : String.valueOf(objs[6]));
                    componentColumnList.add(componentColumn);
                }
                // 创建tables
                String tableName = null;
                StringBuffer createTableSql = null;
                String dataType = null;
                for (Iterator<String> keyIterator = tableMap.keySet().iterator(); keyIterator.hasNext();) {
                    tableName = keyIterator.next();
                    createTableSql = new StringBuffer();
                    createTableSql.append("create table " + tableName + "(");
                    componentColumnList = tableMap.get(tableName);
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
                    DatabaseHandlerDao.getInstance().executeSql("insert into t_xtpz_preview_table values('" + tableName + "')");
                }
            }
        }
    }

    /**
     * 获取对应数据库的数据类型
     * 
     * @param type 字段类型
     * @param length 长度
     * @return String 返回类型
     */
    public static String getDataType(String type, Integer length) {
        String dataType = null;
        if (DatabaseHandlerDao.DB_ORACLE.equals(DatabaseHandlerDao.getDbType())) {
            if ("数字型".equals(type)) {
                dataType = "number(" + (null == length ? "18" : length) + ")";
            } else if ("字符型".equals(type)) {
                dataType = "varchar2(" + (null == length ? "50" : length) + ")";
            } else if ("日期型".equals(type)) {
                dataType = "date";
            } else {
                dataType = type;
            }
        } else if (DatabaseHandlerDao.DB_SQLSERVER.equals(DatabaseHandlerDao.getDbType())) {
            if ("数字型".equals(type)) {
                dataType = "int";
            } else if ("字符型".equals(type)) {
                dataType = "nvarchar(" + (null == length ? "50" : length) + ")";
            } else if ("日期型".equals(type)) {
                dataType = "datetime";
            } else {
                dataType = type;
            }
        } else if (DatabaseHandlerDao.DB_MYSQL.equals(DatabaseHandlerDao.getDbType())) {

        } else if (DatabaseHandlerDao.DB_DAMING.equals(DatabaseHandlerDao.getDbType())) {

        }
        return dataType;
    }

    /**
     * 获取对应数据库的数据类型
     * 
     * @param databaseType 数据库类型
     * @param type 字段类型
     * @param length 长度
     * @return String 返回类型
     */
    public static String getDataType(String databaseType, String type, Integer length) {
        String dataType = null;
        if (DatabaseHandlerDao.DB_ORACLE.equals(databaseType)) {
            if ("数字型".equals(type)) {
                dataType = "number(" + (null == length ? "18" : length) + ")";
            } else if ("字符型".equals(type)) {
                dataType = "varchar2(" + (null == length ? "50" : length) + ")";
            } else if ("日期型".equals(type)) {
                dataType = "date";
            } else {
                dataType = type;
            }
        } else if (DatabaseHandlerDao.DB_SQLSERVER.equals(databaseType)) {
            if ("数字型".equals(type)) {
                dataType = "int";
            } else if ("字符型".equals(type)) {
                dataType = "nvarchar(" + (null == length ? "50" : length) + ")";
            } else if ("日期型".equals(type)) {
                dataType = "datetime";
            } else {
                dataType = type;
            }
        } else if (DatabaseHandlerDao.DB_MYSQL.equals(databaseType)) {

        } else if (DatabaseHandlerDao.DB_DAMING.equals(databaseType)) {

        }
        return dataType;
    }

    /**
     * 将构件文件拷贝到相关的文件夹下
     * 
     * @param componentConfig 构件配置
     * @param targetPath 用于预览的工程的路径
     */
    public static void copyComponentFile(ComponentVersion componentVersion, String targetPath) {
        String path = componentVersion.getPath();
        if (path != null && !"".equals(path)) {
            targetPath = targetPath.replaceAll("\\\\", "/");
            if (!targetPath.endsWith("/")) {
                targetPath += "/";
            }
            String packageName = path.substring(0, componentVersion.getPath().lastIndexOf("."));
            String tempPath = ComponentFileUtil.getCompUncompressPath() + packageName + "/";
            File packageUncompressFile = new File(tempPath);
            if (!packageUncompressFile.exists()) {
                File packageFile = new File(ComponentFileUtil.getCompPath() + componentVersion.getPath());
                if (packageFile.exists()) {
                    try {
                        ZipUtil.unzipFile(packageFile, tempPath);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            String targetSrcPath = targetPath.substring(0, targetPath.length() - 1);
            targetSrcPath = targetSrcPath.substring(0, targetSrcPath.lastIndexOf("/")) + "/src/";
            if (ConstantVar.Component.Type.SELF_DEFINE.indexOf(componentVersion.getComponent().getType()) != -1) {
                FileUtil.copyFolder(tempPath + "src/", targetSrcPath + "selfdefine/");
                FileUtil.copyFolder(tempPath + "page/", targetPath + "cfg-resource/" + componentVersion.getViews() + "/views/selfdefine/");
            } else if (ConstantVar.Component.Type.PAGE.equals(componentVersion.getComponent().getType())
                    || ConstantVar.Component.Type.LOGIC.equals(componentVersion.getComponent().getType())
                    || ConstantVar.Component.Type.COMMON.equals(componentVersion.getComponent().getType())) {
                FileUtil.copyFolder(tempPath + "src/", targetSrcPath + "component/");
                FileUtil.copyFolder(tempPath + "page/", targetPath + "cfg-resource/" + componentVersion.getViews() + "/views/component/");
            }
            copyComponentJar(tempPath + "jar/", targetPath);
            FileUtil.copyFolder(tempPath + "classes", targetPath + "WEB-INF/classes/");
        }
    }

    /**
     * 将构件相关的JAR包拷贝到相关的文件夹下
     * 
     * @param jarPath 构件jar存放位置
     * @param targetPath 用于预览的工程的路径
     */
    private static void copyComponentJar(String jarPath, String targetPath) {
        File sourceJarDir = new File(jarPath);
        File[] sourceJars = sourceJarDir.listFiles();
        if (sourceJars != null && sourceJars.length > 0) {
            File componentLib = new File(targetPath + "WEB-INF/component_lib/");
            if (!componentLib.exists()) {
                componentLib.mkdir();
            }
            File targetJar = null;
            for (File sourceJar : sourceJars) {
                targetJar = new File(targetPath + "WEB-INF/lib/" + sourceJar.getName());
                // 构件关联的jar在系统lib下是否存在，如果存在，不用拷贝；如果不存在，拷贝到lib和component_lib文件夹下
                if (!targetJar.exists()) {
                    FileUtil.copyFile(sourceJar.getPath(), targetPath + "WEB-INF/component_lib/" + sourceJar.getName());
                    FileUtil.copyFile(sourceJar.getPath(), targetPath + "WEB-INF/lib/" + sourceJar.getName());
                }
            }
        }
    }
}

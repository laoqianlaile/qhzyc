package com.ces.config.dhtmlx.action.componentconfig;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.MapUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.multipart.MultiPartRequestWrapper;
import org.apache.struts2.rest.DefaultHttpHeaders;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.ces.config.dhtmlx.action.base.ConfigController;
import com.ces.config.dhtmlx.entity.component.ComponentButton;
import com.ces.config.dhtmlx.entity.component.ComponentCallback;
import com.ces.config.dhtmlx.entity.component.ComponentConfig;
import com.ces.config.dhtmlx.entity.component.ComponentFunction;
import com.ces.config.dhtmlx.entity.component.ComponentInputParam;
import com.ces.config.dhtmlx.entity.component.ComponentOutputParam;
import com.ces.config.dhtmlx.entity.component.ComponentReserveZone;
import com.ces.config.dhtmlx.entity.component.ComponentSelfParam;
import com.ces.config.dhtmlx.entity.component.ComponentTable;
import com.ces.config.dhtmlx.entity.parameter.SystemParameter;
import com.ces.config.utils.ComponentFileUtil;
import com.ces.config.utils.FileUtil;
import com.ces.config.utils.ZipUtil;
import com.ces.xarch.core.entity.StringIDEntity;

/**
 * 构件配置文件Controller
 * 
 * @author wanglei
 * @date 2013-12-02
 */
public class ComponentConfigController extends ConfigController<StringIDEntity> {

    private static final long serialVersionUID = -132561819688171083L;

    private Map<String, SystemParameter> systemParameterMap = new HashMap<String, SystemParameter>();

    private Map<String, ComponentSelfParam> componentSelfParamMap = new HashMap<String, ComponentSelfParam>();

    private Map<String, Map<String, String[]>> componentSelfParamOptionMap = new HashMap<String, Map<String, String[]>>();

    private Map<String, ComponentInputParam> inputParameterMap = new HashMap<String, ComponentInputParam>();

    private Map<String, ComponentOutputParam> outputParameterMap = new HashMap<String, ComponentOutputParam>();

    private Map<String, ComponentReserveZone> reserveZoneMap = new HashMap<String, ComponentReserveZone>();

    private Map<String, ComponentFunction> functionMap = new HashMap<String, ComponentFunction>();

    private Map<String, Map<String, String[]>> functionReturnDataMap = new HashMap<String, Map<String, String[]>>();

    private Map<String, ComponentCallback> callbackMap = new HashMap<String, ComponentCallback>();

    private Map<String, Map<String, String[]>> callbackParameterMap = new HashMap<String, Map<String, String[]>>();

    private Map<String, ComponentButton> componentButtonMap = new HashMap<String, ComponentButton>();

    private Map<String, ComponentTable> tableMap = new HashMap<String, ComponentTable>();

    private Map<String, Map<String, String[]>> tableColumnMap = new HashMap<String, Map<String, String[]>>();

    /*
     * (non-Javadoc)
     * @see com.ces.xarch.core.web.struts2.BaseController#initModel()
     */
    @Override
    protected void initModel() {
        setModel(new StringIDEntity());
    }

    /**
     * 获取开发的构件名称
     * 
     * @return Object
     */
    public Object getComponentNames() {
        Set<String> nameSet = new HashSet<String>();
        File srcDir = new File(ComponentFileUtil.getSrcPath() + "component/com/ces/component/");
        File dhtmlxViewDir = new File(ComponentFileUtil.getProjectPath() + "cfg-resource/dhtmlx/views/component/");
        File coral40ViewDir = new File(ComponentFileUtil.getProjectPath() + "cfg-resource/coral40/views/component/");
        if (srcDir.exists()) {
            String[] srcNames = srcDir.list();
            if (srcNames != null && srcNames.length > 0) {
                for (String srcName : srcNames) {
                    if (!".svn".equals(srcName)) {
                        nameSet.add(srcName);
                    }
                }
            }
        }
        String[] dhtmlxViewNames = dhtmlxViewDir.list();
        if (dhtmlxViewNames != null && dhtmlxViewNames.length > 0) {
            for (String viewName : dhtmlxViewNames) {
                if (!".svn".equals(viewName)) {
                    nameSet.add(viewName);
                }
            }
        }
        String[] coral40ViewNames = coral40ViewDir.list();
        if (coral40ViewNames != null && coral40ViewNames.length > 0) {
            for (String viewName : coral40ViewNames) {
                if (!".svn".equals(viewName)) {
                    nameSet.add(viewName);
                }
            }
        }
        String names = "";
        if (!nameSet.isEmpty()) {
            for (String name : nameSet) {
                names += name + ",";
            }
        }
        if (names.length() > 0) {
            names = names.substring(0, names.length() - 1);
        }
        setReturnData(names);
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 校验开发的构件是否存在
     * 
     * @return Object
     */
    public Object existComponent() {
        String name = getParameter("name");
        boolean exist = false;
        File javaDir = new File(ComponentFileUtil.getSrcPath() + "component/com/ces/component/" + name);
        File dhtmlxViewDir = new File(ComponentFileUtil.getProjectPath() + "cfg-resource/dhtmlx/views/component/" + name);
        File coral40ViewDir = new File(ComponentFileUtil.getProjectPath() + "cfg-resource/coral40/views/component/" + name);
        if (javaDir.exists() || dhtmlxViewDir.exists() || coral40ViewDir.exists()) {
            exist = true;
        }
        setReturnData("{'exist':" + exist + "}");
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 获取配置文件信息
     * 
     * @return Object
     */
    public Object getComponentConfig() {
        File configFile = new File(ComponentFileUtil.getCompressTempPath() + "component/" + getParameter("name") + "/component-config.xml");
        try {
            ComponentConfig componentConfig = ComponentFileUtil.parseConfigFile(configFile);
            setReturnData(componentConfig);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 创建配置文件
     * 
     * @return Object
     */
    @SuppressWarnings("rawtypes")
    public Object createConfigFile() {
        HttpServletRequest request = ServletActionContext.getRequest();
        Map parameterMap = request.getParameterMap();
        initParams(parameterMap);

        Document doc = DocumentHelper.createDocument();
        doc.setXMLEncoding("UTF-8");
        Element root = doc.addElement("component");
        root.addElement("code").addText(request.getParameter("code"));
        root.addElement("name").addText(request.getParameter("name"));
        root.addElement("alias").addText(request.getParameter("alias"));
        root.addElement("version").addText(request.getParameter("version"));
        root.addElement("type").addText(request.getParameter("type"));
        root.addElement("views").addText(request.getParameter("views"));
        root.addElement("url").addText(request.getParameter("url"));
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        root.addElement("package_time").addText(df.format(new Date()));

        if (!systemParameterMap.isEmpty()) {
            Element systemParametersElement = root.addElement("system_parameters");
            SystemParameter systemParameter = null;
            for (Iterator<SystemParameter> iterator = systemParameterMap.values().iterator(); iterator.hasNext();) {
                systemParameter = iterator.next();
                Element systemParameterElement = systemParametersElement.addElement("system_parameter");
                systemParameterElement.addElement("name").addText(systemParameter.getName());
                systemParameterElement.addElement("remark").addText(systemParameter.getRemark());
            }
        }
        if (!componentSelfParamMap.isEmpty()) {
            Element componentSelfParamsElement = root.addElement("self_parameters");
            Entry<String, ComponentSelfParam> entry = null;
            ComponentSelfParam componentSelfParam = null;
            Map<String, String[]> options = null;
            String[] option = null;
            for (Iterator<Entry<String, ComponentSelfParam>> iterator = componentSelfParamMap.entrySet().iterator(); iterator.hasNext();) {
                entry = iterator.next();
                Element componentSelfParamElement = componentSelfParamsElement.addElement("self_parameter");
                componentSelfParam = entry.getValue();
                componentSelfParamElement.addElement("name").addText(componentSelfParam.getName());
                componentSelfParamElement.addElement("type").addText(componentSelfParam.getType());
                componentSelfParamElement.addElement("text").addText(componentSelfParam.getText());
                componentSelfParamElement.addElement("value").addText(componentSelfParam.getValue());
                if ("1".equals(componentSelfParam.getType()) && !componentSelfParamOptionMap.isEmpty()) {
                    options = componentSelfParamOptionMap.get(entry.getKey());
                    if (MapUtils.isNotEmpty(options)) {
                        Element optionsElement = componentSelfParamElement.addElement("options");
                        for (Iterator<String[]> i = options.values().iterator(); i.hasNext();) {
                            option = i.next();
                            Element optionElement = optionsElement.addElement("option");
                            optionElement.addElement("text").addText(option[0]);
                            optionElement.addElement("value").addText(option[1]);
                        }
                    }
                }
                componentSelfParamElement.addElement("remark").addText(componentSelfParam.getRemark());
            }
        }
        if (!inputParameterMap.isEmpty()) {
            Element inputParametersElement = root.addElement("input_parameters");
            ComponentInputParam inputParameter = null;
            for (Iterator<ComponentInputParam> iterator = inputParameterMap.values().iterator(); iterator.hasNext();) {
                inputParameter = iterator.next();
                Element inputParameterElement = inputParametersElement.addElement("input_parameter");
                inputParameterElement.addElement("name").addText(inputParameter.getName());
                inputParameterElement.addElement("value").addText(inputParameter.getValue());
                inputParameterElement.addElement("remark").addText(inputParameter.getRemark());
            }
        }
        if (!outputParameterMap.isEmpty()) {
            Element outputParametersElement = root.addElement("output_parameters");
            ComponentOutputParam outputParameter = null;
            for (Iterator<ComponentOutputParam> iterator = outputParameterMap.values().iterator(); iterator.hasNext();) {
                outputParameter = iterator.next();
                Element outputParameterElement = outputParametersElement.addElement("output_parameter");
                outputParameterElement.addElement("name").addText(outputParameter.getName());
                outputParameterElement.addElement("remark").addText(outputParameter.getRemark());
            }
        }
        if (!reserveZoneMap.isEmpty()) {
            Element reserveZonesElement = root.addElement("reserve_zones");
            ComponentReserveZone reserveZone = null;
            for (Iterator<ComponentReserveZone> iterator = reserveZoneMap.values().iterator(); iterator.hasNext();) {
                reserveZone = iterator.next();
                Element reserveZoneElement = reserveZonesElement.addElement("reserve_zone");
                reserveZoneElement.addElement("name").addText(reserveZone.getName());
                reserveZoneElement.addElement("alias").addText(reserveZone.getAlias());
                reserveZoneElement.addElement("type").addText(reserveZone.getType());
                reserveZoneElement.addElement("page").addText(reserveZone.getPage());
            }
        }
        if (!functionMap.isEmpty()) {
            Element functionsElement = root.addElement("functions");
            Entry<String, ComponentFunction> entry = null;
            ComponentFunction function = null;
            Map<String, String[]> returnDatas = null;
            String[] returnData = null;
            for (Iterator<Entry<String, ComponentFunction>> iterator = functionMap.entrySet().iterator(); iterator.hasNext();) {
                entry = iterator.next();
                Element functionElement = functionsElement.addElement("function");
                function = entry.getValue();
                functionElement.addElement("name").addText(function.getName());
                functionElement.addElement("page").addText(function.getPage());
                if (!functionReturnDataMap.isEmpty()) {
                    returnDatas = functionReturnDataMap.get(entry.getKey());
                    if (MapUtils.isNotEmpty(returnDatas)) {
                        Element returnDatasElement = functionElement.addElement("return_datas");
                        for (Iterator<String[]> i = returnDatas.values().iterator(); i.hasNext();) {
                            returnData = i.next();
                            Element returnDataElement = returnDatasElement.addElement("return_data");
                            returnDataElement.addElement("name").addText(returnData[0]);
                            returnDataElement.addElement("remark").addText(returnData[1]);
                        }
                    }
                }
                functionElement.addElement("remark").addText(function.getRemark());
            }
        }
        if (!callbackMap.isEmpty()) {
            Element callbacksElement = root.addElement("callbacks");
            Entry<String, ComponentCallback> entry = null;
            ComponentCallback callback = null;
            Map<String, String[]> parameters = null;
            String[] parameter = null;
            for (Iterator<Entry<String, ComponentCallback>> iterator = callbackMap.entrySet().iterator(); iterator.hasNext();) {
                entry = iterator.next();
                Element callbackElement = callbacksElement.addElement("callback");
                callback = entry.getValue();
                callbackElement.addElement("name").addText(callback.getName());
                callbackElement.addElement("page").addText(callback.getPage());
                if (!callbackParameterMap.isEmpty()) {
                    parameters = callbackParameterMap.get(entry.getKey());
                    if (MapUtils.isNotEmpty(parameters)) {
                        Element parametersElement = callbackElement.addElement("parameters");
                        for (Iterator<String[]> i = parameters.values().iterator(); i.hasNext();) {
                            parameter = i.next();
                            Element parameterElement = parametersElement.addElement("parameter");
                            parameterElement.addElement("name").addText(parameter[0]);
                            parameterElement.addElement("remark").addText(parameter[1]);
                        }
                    }
                }
                callbackElement.addElement("remark").addText(callback.getRemark());
            }
        }
        if (!componentButtonMap.isEmpty()) {
            Element componentButtonsElement = root.addElement("authority_buttons");
            ComponentButton componentButton = null;
            for (Iterator<ComponentButton> iterator = componentButtonMap.values().iterator(); iterator.hasNext();) {
                componentButton = iterator.next();
                Element componentButtonElement = componentButtonsElement.addElement("button");
                componentButtonElement.addElement("name").addText(componentButton.getName());
                componentButtonElement.addElement("display_name").addText(componentButton.getDisplayName());
            }
        }
        if (!tableMap.isEmpty()) {
            Element tablesElement = root.addElement("tables");
            Entry<String, ComponentTable> entry = null;
            ComponentTable table = null;
            Map<String, String[]> columns = null;
            String[] column = null;
            for (Iterator<Entry<String, ComponentTable>> iterator = tableMap.entrySet().iterator(); iterator.hasNext();) {
                entry = iterator.next();
                Element tableElement = tablesElement.addElement("table");
                table = entry.getValue();
                tableElement.addElement("name").addText(table.getName());
                tableElement.addElement("is_selfdefine").addText(table.getIsSelfdefine() == null ? "0" : table.getIsSelfdefine());
                tableElement.addElement("release_with_data").addText(table.getReleaseWithData() == null ? "0" : table.getReleaseWithData());
                if (!tableColumnMap.isEmpty()) {
                    columns = tableColumnMap.get(entry.getKey());
                    if (MapUtils.isNotEmpty(columns)) {
                        Element columnsElement = tableElement.addElement("columns");
                        for (Iterator<String[]> i = columns.values().iterator(); i.hasNext();) {
                            column = i.next();
                            Element columnElement = columnsElement.addElement("column");
                            columnElement.addElement("name").addText(column[0]);
                            columnElement.addElement("type").addText(column[1]);
                            columnElement.addElement("length").addText(column[2]);
                            columnElement.addElement("is_null").addText(column[3]);
                            columnElement.addElement("default_value").addText(column[4]);
                            columnElement.addElement("remark").addText(column[5]);
                        }
                    }
                }
            }
        }
        root.addElement("remark").addText(request.getParameter("remark") == null ? "" : request.getParameter("remark"));

        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding("UTF-8");
        format.setNewLineAfterDeclaration(false);
        format.setIndent("\t");
        XMLWriter xmlWriter = null;

        String componentDirPath = ComponentFileUtil.getCompressTempPath() + "component/" + request.getParameter("name");
        File componentDir = new File(componentDirPath);
        if (!componentDir.exists()) {
            componentDir.mkdirs();
        }
        String componentConfigFilePath = componentDirPath + "/component-config.xml";
        Writer writer = null;
        boolean result = false;
        try {
            writer = new OutputStreamWriter(new FileOutputStream(componentConfigFilePath), "UTF-8");
            xmlWriter = new XMLWriter(writer, format);
            xmlWriter.write(doc);
            writer.flush();
            result = true;
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
            setReturnData("{'success':" + result + "}");
        }
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void initParams(Map parameterMap) {
        for (Iterator<Entry<String, String[]>> iterator = parameterMap.entrySet().iterator(); iterator.hasNext();) {
            Entry<String, String[]> entry = iterator.next();
            String val = entry.getValue()[0];
            if (val == null) {
                val = "";
            }
            String key = null;
            String[] strs = null;
            if (entry.getKey().indexOf("system_parameter_") != -1) {
                key = entry.getKey().replaceAll("system_parameter_", "");
                strs = key.split("_");
                SystemParameter systemParameter = systemParameterMap.get(strs[1]);
                if (systemParameter == null) {
                    systemParameter = new SystemParameter();
                    systemParameterMap.put(strs[1], systemParameter);
                }
                if ("name".equalsIgnoreCase(strs[0])) {
                    systemParameter.setName(val);
                } else if ("remark".equalsIgnoreCase(strs[0])) {
                    systemParameter.setRemark(val);
                }
            } else if (entry.getKey().indexOf("self_parameter_") != -1) {
                key = entry.getKey().replaceAll("self_parameter_", "");
                strs = key.split("_");
                if (strs.length == 2) {
                    ComponentSelfParam componentSelfParam = componentSelfParamMap.get(strs[1]);
                    if (componentSelfParam == null) {
                        componentSelfParam = new ComponentSelfParam();
                        componentSelfParamMap.put(strs[1], componentSelfParam);
                    }
                    if ("name".equalsIgnoreCase(strs[0])) {
                        componentSelfParam.setName(val);
                    } else if ("type".equalsIgnoreCase(strs[0])) {
                        componentSelfParam.setType(val);
                    } else if ("text".equalsIgnoreCase(strs[0])) {
                        componentSelfParam.setText(val);
                    } else if ("value".equalsIgnoreCase(strs[0])) {
                        componentSelfParam.setValue(val);
                    } else if ("remark".equalsIgnoreCase(strs[0])) {
                        componentSelfParam.setRemark(val);
                    }
                } else {
                    Map<String, String[]> componentSelfParamOption = componentSelfParamOptionMap.get(strs[1]);
                    if (componentSelfParamOption == null) {
                        componentSelfParamOption = new HashMap<String, String[]>();
                        componentSelfParamOptionMap.put(strs[1], componentSelfParamOption);
                    }
                    String[] option = componentSelfParamOption.get(strs[3]);
                    if (option == null) {
                        option = new String[2];
                        componentSelfParamOption.put(strs[3], option);
                    }
                    if ("text".equalsIgnoreCase(strs[2])) {
                        option[0] = val;
                    } else if ("value".equalsIgnoreCase(strs[2])) {
                        option[1] = val;
                    }
                }
            } else if (entry.getKey().indexOf("input_parameter_") != -1) {
                key = entry.getKey().replaceAll("input_parameter_", "");
                strs = key.split("_");
                ComponentInputParam componentInputParam = inputParameterMap.get(strs[1]);
                if (componentInputParam == null) {
                    componentInputParam = new ComponentInputParam();
                    inputParameterMap.put(strs[1], componentInputParam);
                }
                if ("name".equalsIgnoreCase(strs[0])) {
                    componentInputParam.setName(val);
                } else if ("value".equalsIgnoreCase(strs[0])) {
                    componentInputParam.setValue(val);
                } else if ("remark".equalsIgnoreCase(strs[0])) {
                    componentInputParam.setRemark(val);
                }
            } else if (entry.getKey().indexOf("output_parameter_") != -1) {
                key = entry.getKey().replaceAll("output_parameter_", "");
                strs = key.split("_");
                ComponentOutputParam componentOutputParam = outputParameterMap.get(strs[1]);
                if (componentOutputParam == null) {
                    componentOutputParam = new ComponentOutputParam();
                    outputParameterMap.put(strs[1], componentOutputParam);
                }
                if ("name".equalsIgnoreCase(strs[0])) {
                    componentOutputParam.setName(val);
                } else if ("remark".equalsIgnoreCase(strs[0])) {
                    componentOutputParam.setRemark(val);
                }
            } else if (entry.getKey().indexOf("reserve_zone_") != -1) {
                key = entry.getKey().replaceAll("reserve_zone_", "");
                strs = key.split("_");
                ComponentReserveZone componentReserveZone = reserveZoneMap.get(strs[1]);
                if (componentReserveZone == null) {
                    componentReserveZone = new ComponentReserveZone();
                    reserveZoneMap.put(strs[1], componentReserveZone);
                }
                if ("name".equalsIgnoreCase(strs[0])) {
                    componentReserveZone.setName(val);
                } else if ("alias".equalsIgnoreCase(strs[0])) {
                    componentReserveZone.setAlias(val);
                } else if ("type".equalsIgnoreCase(strs[0])) {
                    componentReserveZone.setType(val);
                } else if ("page".equalsIgnoreCase(strs[0])) {
                    componentReserveZone.setPage(val);
                }
            } else if (entry.getKey().indexOf("function_") != -1) {
                key = entry.getKey().replaceAll("function_", "");
                strs = key.split("_");
                if (strs.length == 2) {
                    ComponentFunction componentFunction = functionMap.get(strs[1]);
                    if (componentFunction == null) {
                        componentFunction = new ComponentFunction();
                        functionMap.put(strs[1], componentFunction);
                    }
                    if ("name".equalsIgnoreCase(strs[0])) {
                        componentFunction.setName(val);
                    } else if ("page".equalsIgnoreCase(strs[0])) {
                        componentFunction.setPage(val);
                    } else if ("remark".equalsIgnoreCase(strs[0])) {
                        componentFunction.setRemark(val);
                    }
                } else {
                    Map<String, String[]> functionReturnData = functionReturnDataMap.get(strs[2]);
                    if (functionReturnData == null) {
                        functionReturnData = new HashMap<String, String[]>();
                        functionReturnDataMap.put(strs[2], functionReturnData);
                    }
                    String[] returnData = functionReturnData.get(strs[4]);
                    if (returnData == null) {
                        returnData = new String[2];
                        functionReturnData.put(strs[4], returnData);
                    }
                    if ("name".equalsIgnoreCase(strs[3])) {
                        returnData[0] = val;
                    } else if ("remark".equalsIgnoreCase(strs[3])) {
                        returnData[1] = val;
                    }
                }
            } else if (entry.getKey().indexOf("callback_") != -1) {
                key = entry.getKey().replaceAll("callback_", "");
                strs = key.split("_");
                if (strs.length == 2) {
                    ComponentCallback componentCallback = callbackMap.get(strs[1]);
                    if (componentCallback == null) {
                        componentCallback = new ComponentCallback();
                        callbackMap.put(strs[1], componentCallback);
                    }
                    if ("name".equalsIgnoreCase(strs[0])) {
                        componentCallback.setName(val);
                    } else if ("page".equalsIgnoreCase(strs[0])) {
                        componentCallback.setPage(val);
                    } else if ("remark".equalsIgnoreCase(strs[0])) {
                        componentCallback.setRemark(val);
                    }
                } else {
                    Map<String, String[]> callbackParameter = callbackParameterMap.get(strs[1]);
                    if (callbackParameter == null) {
                        callbackParameter = new HashMap<String, String[]>();
                        callbackParameterMap.put(strs[1], callbackParameter);
                    }
                    String[] parameter = callbackParameter.get(strs[3]);
                    if (parameter == null) {
                        parameter = new String[2];
                        callbackParameter.put(strs[3], parameter);
                    }
                    if ("name".equalsIgnoreCase(strs[2])) {
                        parameter[0] = val;
                    } else if ("remark".equalsIgnoreCase(strs[2])) {
                        parameter[1] = val;
                    }
                }
            } else if (entry.getKey().indexOf("authority_button_") != -1) {
                key = entry.getKey().replaceAll("authority_button_", "");
                strs = key.split("_");
                if (strs.length == 2) {
                    ComponentButton componentButton = componentButtonMap.get(strs[1]);
                    if (componentButton == null) {
                        componentButton = new ComponentButton();
                        componentButtonMap.put(strs[1], componentButton);
                    }
                    if ("name".equalsIgnoreCase(strs[0])) {
                        componentButton.setName(val);
                    }
                } else if (strs.length == 3) {
                    ComponentButton componentButton = componentButtonMap.get(strs[2]);
                    if (componentButton == null) {
                        componentButton = new ComponentButton();
                        componentButtonMap.put(strs[2], componentButton);
                    }
                    if ("display".equalsIgnoreCase(strs[0])) {
                        componentButton.setDisplayName(val);
                    }
                }
            } else if (entry.getKey().indexOf("table_") != -1) {
                key = entry.getKey().replaceAll("table_", "");
                strs = key.split("_");
                if (strs.length == 2) {
                    ComponentTable componentTable = tableMap.get(strs[1]);
                    if (componentTable == null) {
                        componentTable = new ComponentTable();
                        tableMap.put(strs[1], componentTable);
                    }
                    if ("name".equalsIgnoreCase(strs[0])) {
                        componentTable.setName(val);
                    }
                } else if ("release".equals(strs[0])) {
                    ComponentTable componentTable = tableMap.get(strs[3]);
                    if (componentTable == null) {
                        componentTable = new ComponentTable();
                        tableMap.put(strs[3], componentTable);
                    }
                    componentTable.setReleaseWithData(val);
                } else if ("selfdefine".equals(strs[1])) {
                    ComponentTable componentTable = tableMap.get(strs[2]);
                    if (componentTable == null) {
                        componentTable = new ComponentTable();
                        tableMap.put(strs[2], componentTable);
                    }
                    componentTable.setIsSelfdefine(val);
                } else {
                    Map<String, String[]> tableColumn = tableColumnMap.get(strs[1]);
                    if (tableColumn == null) {
                        tableColumn = new HashMap<String, String[]>();
                        tableColumnMap.put(strs[1], tableColumn);
                    }
                    String[] column = tableColumn.get(strs[3]);
                    if (column == null) {
                        column = new String[6];
                        tableColumn.put(strs[3], column);
                    }
                    if ("name".equalsIgnoreCase(strs[2])) {
                        column[0] = val;
                    } else if ("type".equalsIgnoreCase(strs[2])) {
                        column[1] = val;
                    } else if ("length".equalsIgnoreCase(strs[2])) {
                        column[2] = val;
                    } else if ("isNull".equalsIgnoreCase(strs[2])) {
                        column[3] = val;
                    } else if ("defaultValue".equalsIgnoreCase(strs[2])) {
                        column[4] = val;
                    } else if ("remark".equalsIgnoreCase(strs[2])) {
                        column[5] = val;
                    }
                }
            }
        }
    }

    /**
     * 校验构件的配置文件是否存在
     * 
     * @return Object
     */
    public Object existConfigFile() {
        boolean exist = false;
        File configFile = new File(ComponentFileUtil.getCompressTempPath() + "component/" + getParameter("name") + "/component-config.xml");
        if (configFile.exists()) {
            exist = true;
        }
        setReturnData("{'exist':" + exist + "}");
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 预览配置文件
     * 
     * @return Object
     */
    public Object previewConfigFile() {
        String configFilePath = ComponentFileUtil.getCompressTempPath() + "component/" + getParameter("name") + "/component-config.xml";
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
     * 列出构件的jar文件
     * 
     * @return Object
     */
    public Object listJars() {
        String name = getParameter("name");
        String jarDirPath = ComponentFileUtil.getCompressTempPath() + "component/" + name + "/jar";
        File jarDir = new File(jarDirPath);
        String result = "";
        if (jarDir.exists() && !FileUtil.isEmptyDir(jarDirPath)) {
            String[] fileNames = jarDir.list();
            for (String fileName : fileNames) {
                result += fileName + ",";
            }
        }
        if (result.length() > 0) {
            result = result.substring(0, result.length() - 1);
        }
        setReturnData(result);
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 删除构件的jar文件
     * 
     * @return Object
     */
    public Object removeJar() {
        String name = getParameter("name");
        String jarName = getParameter("jarName");
        String jarPath = ComponentFileUtil.getCompressTempPath() + "component/" + name + "/jar/" + jarName;
        File jar = new File(jarPath);
        String result = "";
        if (jar.exists()) {
            if (jar.delete()) {
                result = "删除成功！";
            } else {
                result = "删除失败！";
            }
        } else {
            result = "该文件不存在！";
        }
        setReturnData(result);
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 打包构件
     * 
     * @return Object
     */
    public Object packageComponent() {
        String name = getParameter("name");
        String views = getParameter("views");
        String projectPath = ComponentFileUtil.getProjectPath();
        String componentDirPath = ComponentFileUtil.getCompressTempPath() + "component/" + name;
        String javaDirPath = ComponentFileUtil.getSrcPath() + "component/com/ces/component/" + name;
        String classDirPath = projectPath + "WEB-INF/classes/com/ces/component/" + name;
        String viewDirPath = projectPath + "cfg-resource/" + views + "/views/component/" + name;
        File javaDir = new File(javaDirPath);
        File viewDir = new File(viewDirPath);
        // 1、准备构件打包的目录
        File componentDir = new File(componentDirPath);
        if (!componentDir.exists()) {
            componentDir.mkdirs();
        }
        File srcDir = new File(componentDirPath + "/src");
        File classesDir = new File(componentDirPath + "/classes");
        File pageDir = new File(componentDirPath + "/page");
        if (!srcDir.exists()) {
            srcDir.mkdirs();
        } else {
            FileUtil.deleteDir(componentDirPath + "/src");
        }
        if (!classesDir.exists()) {
            classesDir.mkdirs();
        } else {
            FileUtil.deleteDir(componentDirPath + "/classes");
        }
        if (!pageDir.exists()) {
            pageDir.mkdirs();
        } else {
            FileUtil.deleteDir(componentDirPath + "/page");
        }
        // 2、准备构件包内文件
        if (javaDir.exists() && !FileUtil.isEmptyDir(javaDirPath)) {
            // 创建构件java文件包
            FileUtil.copyFolder(javaDirPath, componentDirPath + "/src/com/ces/component/" + name);
            FileUtil.copyFolder(classDirPath, componentDirPath + "/classes/com/ces/component/" + name);
        }
        if (viewDir.exists() && !FileUtil.isEmptyDir(viewDir)) {
            // 拷贝页面文件
            FileUtil.copyFolder(viewDirPath, componentDirPath + "/page/" + name);
        }
        // 3、更新配置文件中构件打包时间
        updatePackageTime(componentDirPath + "/component-config.xml");
        // 4、压缩zip包
        File componentZip = new File(ComponentFileUtil.getCompressTempPath() + "component/" + name + ".zip");
        if (componentZip.exists()) {
            FileUtil.deleteFile(componentZip);
        }
        ZipUtil.zip(componentZip, "", componentDir);
        setReturnData("打包成功！");
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 更新配置文件中构件打包时间
     * 
     * @param configFile 配置文件路径
     */
    private void updatePackageTime(String configFile) {
        SAXReader reader = new SAXReader();
        Document doc = null;
        try {
            doc = reader.read(new File(configFile));
            Element root = doc.getRootElement();
            Element packageTime = root.element("package_time");
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (packageTime == null) {
                root.addElement("package_time").addText(df.format(new Date()));
            } else {
                packageTime.setText(df.format(new Date()));
            }
        } catch (DocumentException e1) {
            e1.printStackTrace();
        }
        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding("UTF-8");
        format.setNewLineAfterDeclaration(false);
        format.setIndent("\t");
        XMLWriter xmlWriter = null;

        Writer writer = null;
        try {
            writer = new OutputStreamWriter(new FileOutputStream(configFile), "UTF-8");
            xmlWriter = new XMLWriter(writer, format);
            xmlWriter.write(doc);
            writer.flush();
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
     * 校验构件包是否存在
     * 
     * @return Object
     */
    public Object existComponentPackage() {
        boolean exist = false;
        File componentPackage = new File(ComponentFileUtil.getCompressTempPath() + "component/" + getParameter("name") + ".zip");
        if (componentPackage.exists()) {
            exist = true;
        }
        setReturnData("{'exist':" + exist + "}");
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 下载构件
     * 
     * @return Object
     */
    public Object downloadComponent() {
        HttpServletRequest request = ServletActionContext.getRequest();
        String name = getParameter("name");
        String alias = getParameter("alias");
        String version = getParameter("version");
        File componentZip = new File(ComponentFileUtil.getCompressTempPath() + "component/" + name + ".zip");
        String fileName = "";
        try {
            if (request.getHeader("User-Agent").toLowerCase().indexOf("firefox") > 0) {
                fileName = new String((alias + "_" + version).getBytes("UTF-8"), "ISO8859-1");
            } else {
                fileName = java.net.URLEncoder.encode(alias + "_" + version, "UTF-8");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        HttpServletResponse response = ServletActionContext.getResponse();
        response.reset();
        response.setContentType("application/x-msdownload");
        response.setContentLength((int) componentZip.length());
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".zip");
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
     * 上传JAR
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

        String jarDirPath = ComponentFileUtil.getCompressTempPath() + "component/" + getParameter("name") + "/jar";
        File jarDir = new File(jarDirPath);
        if (!jarDir.exists()) {
            jarDir.mkdirs();
        }
        FileOutputStream fos = null;
        FileInputStream fis = null;
        try {
            // 建立文件输出流
            fos = new FileOutputStream(jarDirPath + "/" + fileName);
            // 建立文件上传流
            fis = new FileInputStream(file);
            byte[] buffer = new byte[1024];
            for (int len = 0; (len = fis.read(buffer)) > 0;) {
                fos.write(buffer, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
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

        // dhtmlxVault上传完成时需要
        String fileId = request.getParameter("sessionId").toString().trim();
        request.getSession().setAttribute("FileUpload.Progress." + fileId, "-1");
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
     * 加载批量打包的配置文件
     * 
     * @return Object
     */
    public Object loadBatchPackageGrid() {
        File batchInputDir = new File(ComponentFileUtil.getCompressTempPath() + "batch/input/");
        if (!batchInputDir.exists()) {
            batchInputDir.mkdirs();
        }
        String[] componentConfigFileNames = batchInputDir.list();
        List<Object[]> list = new ArrayList<Object[]>();
        if (componentConfigFileNames != null && componentConfigFileNames.length > 0) {
            Object[] obj = null;
            for (String componentConfigFileName : componentConfigFileNames) {
                if (componentConfigFileName.endsWith(".xml") || componentConfigFileName.endsWith(".XML")) {
                    obj = new Object[2];
                    obj[0] = componentConfigFileName;
                    obj[1] = componentConfigFileName;
                    list.add(obj);
                }
            }
        }
        setReturnData(list);
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 批量打包
     * 
     * @return Object
     */
    public Object batchPackage() {
        String componentConfigFileNames = getParameter("componentConfigFileNames");
        String[] configFileNames = componentConfigFileNames.split(",");
        File batchInputDir = new File(ComponentFileUtil.getCompressTempPath() + "batch/input/");
        if (!batchInputDir.exists()) {
            batchInputDir.mkdirs();
        }
        File batchOutputDir = new File(ComponentFileUtil.getCompressTempPath() + "batch/output/");
        if (!batchOutputDir.exists()) {
            batchOutputDir.mkdirs();
        } else {
            FileUtil.deleteDir(ComponentFileUtil.getCompressTempPath() + "batch/output/");
        }
        int successCount = 0;
        int failureCount = 0;
        StringBuilder message = new StringBuilder();
        String msg;
        for (String configFileName : configFileNames) {
            msg = packageComponent(configFileName);
            if (msg != null) {
                failureCount++;
                message.append("\r\n" + msg);
            } else {
                successCount++;
            }
        }
        setReturnData("打包成功" + successCount + "个构件，打包失败" + failureCount + "个构件！" + message.toString());
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 根据配置文件打包构件
     * 
     * @param configFileName 配置文件名称
     * @return String
     */
    private String packageComponent(String configFileName) {
        // 解析构件配置文件
        File configFile = new File(ComponentFileUtil.getCompressTempPath() + "batch/input/" + configFileName);
        ComponentConfig componentConfig = null;
        try {
            componentConfig = ComponentFileUtil.parseConfigFile(configFile);
        } catch (DocumentException e) {
            return configFileName + "解析component-config.xml错误！";
        }
        String name = componentConfig.getName();
        String views = componentConfig.getViews();
        String alias = componentConfig.getAlias();
        String version = componentConfig.getVersion();
        String projectPath = ComponentFileUtil.getProjectPath();
        String componentDirPath = ComponentFileUtil.getCompressTempPath() + "batch/output/" + alias + "_" + version;
        String javaDirPath = ComponentFileUtil.getSrcPath() + "component/com/ces/component/" + name;
        String classDirPath = projectPath + "WEB-INF/classes/com/ces/component/" + name;
        String viewDirPath = projectPath + "cfg-resource/" + views + "/views/component/" + name;
        File javaDir = new File(javaDirPath);
        File viewDir = new File(viewDirPath);
        // 1、准备构件打包的目录
        File componentDir = new File(componentDirPath);
        if (!componentDir.exists()) {
            componentDir.mkdirs();
        }
        try {
            boolean existFile = false;
            // 2、准备构件包内文件
            FileUtil.copyFile(ComponentFileUtil.getCompressTempPath() + "batch/input/" + configFileName, componentDirPath + "/component-config.xml");
            if (javaDir.exists() && !FileUtil.isEmptyDir(javaDirPath)) {
                File srcDir = new File(componentDirPath + "/src");
                File classesDir = new File(componentDirPath + "/classes");
                if (!srcDir.exists()) {
                    srcDir.mkdirs();
                }
                if (!classesDir.exists()) {
                    classesDir.mkdirs();
                }
                File componentSrcDir = new File(javaDirPath);
                if (!componentSrcDir.exists()) {
                    if ("2".equals(componentConfig.getType())) {
                        return configFileName + "没有对应的java文件！";
                    }
                } else {
                    existFile = true;
                }
                // 创建构件java文件包
                FileUtil.copyFolder(javaDirPath, componentDirPath + "/src/com/ces/component/" + name);
                FileUtil.copyFolder(classDirPath, componentDirPath + "/classes/com/ces/component/" + name);
            }
            if (viewDir.exists() && !FileUtil.isEmptyDir(viewDir)) {
                File pageDir = new File(componentDirPath + "/page");
                if (!pageDir.exists()) {
                    pageDir.mkdirs();
                }
                File componentPageDir = new File(viewDirPath);
                if (!componentPageDir.exists()) {
                    if ("1".equals(componentConfig.getType())) {
                        return configFileName + "没有对应的页面文件！";
                    }
                } else {
                    existFile = true;
                }
                // 拷贝页面文件
                FileUtil.copyFolder(viewDirPath, componentDirPath + "/page/" + name);
            }
            if (!existFile) {
                return configFileName + "没有对应的文件！";
            }
            // 更新配置文件中构件打包时间
            updatePackageTime(componentDirPath + "/component-config.xml");
            // 3、压缩zip包
            File componentZip = new File(ComponentFileUtil.getCompressTempPath() + "batch/output/" + alias + "_" + version + ".zip");
            ZipUtil.zip(componentZip, "", componentDir);
        } catch (Exception e) {
            return configFileName + "打包失败！";
        } finally {
            FileUtil.deleteFile(componentDir);
        }
        return null;
    }

    /**
     * 校验批量打包的构件是否存在
     * 
     * @return Object
     */
    public Object existBatchComponent() {
        boolean exist = false;
        File outputDir = new File(ComponentFileUtil.getCompressTempPath() + "batch/output/");
        if (outputDir.exists()) {
            String[] componentZips = outputDir.list();
            if (componentZips != null && componentZips.length > 0) {
                exist = true;
            }
        }
        setReturnData("{'exist':" + exist + "}");
        return new DefaultHttpHeaders(SUCCESS).disableCaching();
    }

    /**
     * 下载批量打包的构件
     * 
     * @return Object
     */
    public Object downloadBatchComponent() {
        HttpServletRequest request = ServletActionContext.getRequest();
        File outputDir = new File(ComponentFileUtil.getCompressTempPath() + "batch/output/");
        if (outputDir.exists()) {
            String[] componentZips = outputDir.list();
            if (componentZips != null && componentZips.length > 0) {
                File componentZip = new File(ComponentFileUtil.getCompressTempPath() + "batch/构件包.zip");
                ZipUtil.zip(componentZip, "", outputDir);
                String fileName = "";
                try {
                    if (request.getHeader("User-Agent").toLowerCase().indexOf("firefox") > 0) {
                        fileName = new String("构件包".getBytes("UTF-8"), "ISO8859-1");
                    } else {
                        fileName = java.net.URLEncoder.encode("构件包", "UTF-8");
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                HttpServletResponse response = ServletActionContext.getResponse();
                response.reset();
                response.setContentType("application/x-msdownload");
                response.setContentLength((int) componentZip.length());
                response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".zip");
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
            }
        }
        return NONE;
    }
}

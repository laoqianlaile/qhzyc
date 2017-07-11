package com.ces.config.dhtmlx.service.appmanage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ces.sdk.system.bean.OrgInfo;
import ces.sdk.system.bean.UserInfo;
import ces.workflow.core.define.DefineXmlParserImpl;
import ces.workflow.wapi.Coflow;
import ces.workflow.wapi.define.Activity;
import ces.workflow.wapi.define.DefineXmlFile;
import ces.workflow.wapi.define.ErrorReport;
import ces.workflow.wapi.define.Transition;
import ces.workflow.wapi.define.WFDefineException;
import ces.workflow.wapi.define.WFPackage;
import ces.workflow.wapi.define.WorkflowProcess;
import ces.workflow.wapi.service.util.DBField;
import ces.workflow.wapi.service.util.DBTable;

import com.ces.coflow.web.update.CoflowClient;
import com.ces.coflow.web.util.Const;
import com.ces.coflow.web.util.WebCoflowUitl;
import com.ces.config.datamodel.message.MessageModel;
import com.ces.config.dhtmlx.dao.appmanage.WorkflowVersionDao;
import com.ces.config.dhtmlx.dao.common.DatabaseHandlerDao;
import com.ces.config.dhtmlx.entity.appmanage.ColumnDefine;
import com.ces.config.dhtmlx.entity.appmanage.WorkflowDefine;
import com.ces.config.dhtmlx.entity.appmanage.WorkflowVersion;
import com.ces.config.dhtmlx.service.base.ConfigDefineDaoService;
import com.ces.config.utils.ConstantVar;
import com.ces.config.utils.StringUtil;
import com.ces.config.utils.WorkflowUtil;
import com.ces.utils.BeanUtils;
import com.ces.xarch.plugins.authsystem.utils.FacadeUtil;

@Component
public class WorkflowVersionService extends ConfigDefineDaoService<WorkflowVersion, WorkflowVersionDao> {
	
	private static Log log = LogFactory.getLog(WorkflowVersionService.class);
	
	/*
	 * (non-Javadoc)
	 * @see com.ces.xarch.core.service.AbstractService#save(com.ces.xarch.core.entity.BaseEntity)
	 */
	@Override
	@Transactional
	public WorkflowVersion save(WorkflowVersion entity) {
		String runningVersionId = null;
		if (StringUtil.isEmpty(entity.getId())) {
			Integer showOrder = getDao().getMaxShowOrder(entity.getWorkflowId());
			if (null == showOrder) showOrder = 0;
			entity.setShowOrder(++showOrder);
			if (showOrder > 1) {
				// 获取当前正在运行的流程版本ID
				runningVersionId = getRunningVersionId(entity.getWorkflowId());
			}
		}
		//
		createProcessFile(entity);
		// 向缓存添加数据
		WorkflowUtil.addWorkflowVersion(entity);
		entity = super.save(entity);
		// 向当前运行版本的复制配置信息
		if (null != runningVersionId) {
			copyConfiguration(entity.getWorkflowId(), runningVersionId, entity.getId());
		}
		return (entity);
	}
	
	/**
	 * @date 2014-12-22 下午3:35:41
	 * <p>描述: 创建流程文件 </p>
	 * @return void
	 */
	private File createProcessFile(WorkflowVersion entity) {
		File file = null;
		try {
			WorkflowDefine flowDefine = WorkflowUtil.getWorkflowEntity(entity.getWorkflowId());
			String version = entity.getVersion();// 版本号
			String desc = "";// 描述
			String packageName = flowDefine.getWorkflowName();// 包名称
			String processName = flowDefine.getWorkflowName();// 流程名称
			String fileName = WorkflowUtil.getFileName(flowDefine.getWorkflowCode(), version);// 文件名称
			String packageId = WorkflowUtil.getPackageIdByCode(flowDefine.getWorkflowCode());// 包id
			String processId = WorkflowUtil.getProcessIdByCode(flowDefine.getWorkflowCode());// 流程id
			String scriptType = "JAVA";// 脚本类型:JAVASCRIPT,JAVA
			String vendor = "";
			String author = "";
			String filePath = WorkflowUtil.getLocalXpdlFilePath(fileName);
			file = new File(filePath);
			if (!file.exists()) {
				file.createNewFile();
				createBaseDocument(file, packageId, packageName, processId,
						processName, vendor, author, scriptType, desc);
			}
			//entity.setStatus(Const.LOCAL);
		} catch (Exception e) {
			log.error("创建流程文件出错", e);
		}
		return file;
	}
	
	/*
	 * qiucs 2015-4-29 下午11:18:31
	 * (non-Javadoc)
	 * @see com.ces.config.service.base.StringIDConfigDefineDaoService#delete(java.lang.String)
	 */
	@Transactional
	public void delete(String id) {
		WorkflowVersion entity = WorkflowUtil.getWorkflowVersion(id);
		String workflowCode = WorkflowUtil.getWorkflowCode(entity.getWorkflowId());
		String packageId = WorkflowUtil.getPackageIdByCode(workflowCode),
		       packageVersion = entity.getVersion(),
		       processId = WorkflowUtil.getProcessIdByCode(workflowCode);
		//
		getService(WorkflowActivityService.class).deleteByFk(packageId, packageVersion, processId);
		//
		getService(WorkflowButtonSettingService.class).deleteByWorkflowVersionId(id);
		//
		getService(WorkflowFormSettingService.class).deleteByWorkflowVersionId(id);
		//
		getService(AppDefineService.class).deleteByMenuId(id);
		//
		super.delete(id);
		//
		WorkflowUtil.removeWorkflowVersion(id);
		//
		String fileName = WorkflowUtil.getFileName(workflowCode, packageVersion);
		String tempFilePath = WorkflowUtil.getLocalXpdlFilePath(fileName);
		String serverFilePath = WorkflowUtil.getServerXpdlFilePath(fileName);
		File file = new File(tempFilePath);
		if (file.exists()) file.delete(); // 删除服务端临时流程文件
		file = new File(serverFilePath);
		if (file.exists()) file.delete(); // 删除服务端正式流程文件
	}
	
	/**
	 * qiucs 2014-12-18 下午7:20:02
	 * <p>描述: 删除校验检查 </p>
	 * @return MessageModel
	 */
	public MessageModel checkDelete(String id) {
		WorkflowVersion entity = WorkflowUtil.getWorkflowVersion(id);
		if (Const.RUNNING.equals(entity.getStatus())) {
			return MessageModel.falseInstance("当前版本的流程正在运行，不可删除！");
		}
		if (Const.UPDATED.equals(entity.getStatus())) {
			return MessageModel.falseInstance("当前版本的流程还流程实例未办理完成，不可删除！");
		}
		return MessageModel.trueInstance("OK");
	}

	/**
	 * qiucs 2014-12-17 上午9:30:54
	 * <p>描述: 工作流定义时，自动生成一个版本1.0 </p>
	 * @return void
	 */
	@Transactional
	public void addDefaultVersion(String workflowId) {
		String filters = "EQ_workflowId=" + workflowId;
		long cnt = count(filters);
		if (cnt > 0) return;
		WorkflowVersion entity = new WorkflowVersion();
		entity.setWorkflowId(workflowId);
		entity.setVersion(WorkflowVersion.DEFAULT_VERSION);
		entity.setShowOrder(1);
		entity.setStatus(Const.UNDEFINED);
		entity.setRemark("由工作流定义自动生成");
		getDao().save(entity);
	}
	
	/**
	 * qiucs 2014-12-17 下午8:30:59
	 * <p>描述: 封装为树节点json </p>
	 * @return List<Map<String,Object>>
	 */
    public List<Map<String, Object>> getTreeNode(String workflowId, String idPre) {
    	String filters = "EQ_workflowId=" + workflowId; // 多个用英文";"分隔
        List<WorkflowVersion> list = find(filters, new Sort("showOrder"));
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        idPre = StringUtil.null2empty(idPre);
        for (WorkflowVersion entity : list) {
            data.add(beanToTreeNode(entity, idPre));
        }
        return data;
    }
    
    /**
     * qiucs 2014-12-17 下午8:32:19
     * <p>描述: 实体bean转为树节点</p>
     * @return Map<String,Object>    返回类型   
     */
    private Map<String, Object> beanToTreeNode(WorkflowVersion entity, String idPre) {
        Map<String, Object> data = new HashMap<String, Object>();
        List<Map<String, String>> userdata = new ArrayList<Map<String, String>>();
        Map<String, String> item = new HashMap<String, String>();
        item.put("name", "type");
        item.put("content", "2");
        userdata.add(item);
        item = new HashMap<String, String>();
        item.put("name", "status");
        item.put("content", entity.getStatus());
        userdata.add(item);
        
        data.put("id", idPre.concat(entity.getId()));
        data.put("text", entity.getVersion() + "（" + WorkflowUtil.getStatusText(entity.getStatus()) + "）");
        data.put("child", Boolean.FALSE);
        data.put("userdata", userdata);
        return data;
    }

    /**
     * @date 2014-12-22 下午3:38:30
     * <p>描述: 流程文件 </p>
     * @return void
     */
	protected void createBaseDocument(File file, String packageId,
			String packageName, String processId, String processName,
			String vendor, String author, String scriptType, String desc) {
		Document document = DocumentHelper.createDocument();
		document.setXMLEncoding("utf-8");
		Element root = document.addElement("Package");// package
		root.addAttribute("id", packageId);
		root.addAttribute("name", packageName);
		Element packageHeader = root.addElement("PackageHeader");// packageHeader
		Element created = packageHeader.addElement("Created");// created
		created.setText(new Date().toString());
		packageHeader.addElement("Vendor").setText(vendor);
		packageHeader.addElement("Author").setText(author);
		packageHeader.addElement("Description").setText(desc);
		root.addElement("Script").addAttribute("type", scriptType);// Script
		root.addElement("Participants");
		root.addElement("Applications");
		Element workflowProcesses = root.addElement("WorkflowProcesses");
		Element workflowProcess = workflowProcesses.addElement("WorkflowProcess");
		workflowProcess.addAttribute("id", processId).addAttribute("name", processName);
		workflowProcess.addElement("FormalParameters");
		workflowProcess.addElement("DataFields");
		workflowProcess.addElement("Activities");
		workflowProcess.addElement("Transitions");
		workflowProcess.addElement("ExtendedAttributes");

		root.addElement("ExtendedAttributes");
		OutputFormat outputFormat = OutputFormat.createPrettyPrint();
		outputFormat.setEncoding("utf-8");
		XMLWriter xmlWriter = null;
		try {
			xmlWriter = new XMLWriter(new OutputStreamWriter(new FileOutputStream(file), "utf-8"), outputFormat);
			xmlWriter.write(document);
			xmlWriter.flush();
		} catch (IOException e) {
			log.error("写流程文件出错", e);
		} finally {
			if (xmlWriter != null) {
				try {
					xmlWriter.close();
				} catch (IOException e) {
					log.error("关闭写流程文件出错", e);
				}
			}
		}
	}
	
	/**
	 * @date 2014-12-22 下午5:25:25
	 * <p>描述: TODO(这里用一句话描述这个方法的作用) </p>
	 * @return MessageModel
	 */
	@Transactional
	public MessageModel saveProcessFile(String data) {
		String[] arr = data.split("々");
		//Long processId = Long.valueOf(arr[0]);
		String xmlData = arr[1];
		WorkflowVersion entity = WorkflowUtil.getWorkflowVersion(arr[0]);
		try {
			Document document = DocumentHelper.parseText(xmlData);
			if(null == entity) return MessageModel.falseInstance("");
			WorkflowDefine flowDefine = WorkflowUtil.getWorkflowEntity(entity.getWorkflowId());
			String fileName = WorkflowUtil.getFileName(flowDefine.getWorkflowCode(), entity.getVersion());
			String filePath = WorkflowUtil.getLocalXpdlFilePath(fileName);
			File file = new File(filePath);
			if(!file.exists()){
				try {
					file.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			OutputFormat outputFormat = OutputFormat.createPrettyPrint();
			outputFormat.setEncoding("utf-8");
			XMLWriter xmlWriter = null;
			try {
				xmlWriter = new XMLWriter(new OutputStreamWriter(new FileOutputStream(new File(filePath)), "utf-8"),outputFormat);
				xmlWriter.write(document);
				xmlWriter.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				if(xmlWriter != null){
					try {
						xmlWriter.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			if(Const.UNDEFINED.equals(entity.getStatus())){
				entity.setStatus(Const.LOCAL);
			}else if(Const.STOPPED.equals(entity.getStatus())){
				entity.setStatus(Const.MODIFIED_STOPPED);
			}else if(Const.UNREGISTERED.equals(entity.getStatus())){
				entity.setStatus(Const.MODIFIED_UNREGISTERED);
			}else if(Const.UPDATED.equals(entity.getStatus())){
				entity.setStatus(Const.MODIFIED_UPDATED);
			}
			save(entity);
			
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		
		return MessageModel.trueInstance(entity);
	}
	
	/**
	 * @date 2014-12-22 下午5:34:13
	 * <p>描述: 获取流程文件 </p>
	 * @return Object
	 */
	public Object getProcessFile(String id) {
		Map<String, Object> data = new HashMap<String, Object>();
		WorkflowVersion entity = WorkflowUtil.getWorkflowVersion(id);
		if (null == entity)
			return MessageModel.falseInstance("OK");
		WorkflowDefine flDefine = WorkflowUtil.getWorkflowEntity(entity.getWorkflowId());

		String fileName = WorkflowUtil.getFileName(flDefine.getWorkflowCode(), entity.getVersion());
		String filePath = WorkflowUtil.getLocalXpdlFilePath(fileName);
		StringBuffer sb = new StringBuffer();
		File file = new File(filePath);
		if (!file.exists()) {
			file = createProcessFile(entity);
		}
		BufferedReader br = null;
		InputStreamReader in = null;
		try {
			in = new InputStreamReader(new FileInputStream(file), "utf-8");
			br = new BufferedReader(in);
			String str = null;
			while ((str = br.readLine()) != null) {
				sb.append(str);
			}
			//System.out.println("coflow file content: " + sb);
			data.put("xmlDoc", sb);
			data.put("status", entity.getStatus());
			data.put("version", entity.getVersion());

		} catch (Exception e) {
			log.error("获取流程文件出错", e);
		} finally {
			try {
				if (br != null) {
					br.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				log.error("关闭IO流出错", e);
			}
		}
		return data;
	}

	/**
	 * @date 2014-12-22 下午8:52:56
	 * <p>描述: 流程文件校验 </p>
	 * @return Object
	 */
	public Object checkProcessFile(String data) {
		String[] arr = data.split("々");
		String xmlData = arr[1];
		WorkflowVersion entity = WorkflowUtil.getWorkflowVersion(arr[0]);
		if (null == entity) return MessageModel.falseInstance("OK");
		WorkflowDefine flDefine = WorkflowUtil.getWorkflowEntity(entity.getWorkflowId());
		String fileName = WorkflowUtil.getFileName(flDefine.getWorkflowCode(), entity.getVersion());
		
		File file = null;
		try {
			Document document = DocumentHelper.parseText(xmlData);
			String filePath = WorkflowUtil.getLocalXpdlFilePath(UUID.randomUUID() + ".xpdl");
			file = new File(filePath);//生成一个随机文件,
			if (file.exists()) {
				file.delete();
			}
			try {
				if (!file.exists()) file.createNewFile();
			} catch (IOException e) {
				log.error("File.createNewFile出错", e);
			}
			OutputFormat outputFormat = OutputFormat.createPrettyPrint();
			outputFormat.setEncoding("utf-8");
			XMLWriter xmlWriter = null;
			try {
				OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file), "utf-8");
				xmlWriter = new XMLWriter(writer,outputFormat);
				xmlWriter.write(document);
				xmlWriter.flush();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if(xmlWriter != null){
					try {
						xmlWriter.close();
					} catch (IOException e) {
						log.error("关闭XMLWriter出错", e);
					}
				}
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		DefineXmlParserImpl parse = new DefineXmlParserImpl();
		if (file != null) {
			try {
				WFPackage wfPackage = parse.parse(file);
				List list = wfPackage.validate();
				if (list.isEmpty()) return MessageModel.trueInstance("OK");
				Iterator it = list.iterator();
				StringBuffer sb = new StringBuffer();
				sb.append(fileName + " 流程定义文件有错：" + "\n");
		        while (it.hasNext()) {
		            ErrorReport error = (ErrorReport) it.next();
		            sb.append(error.getMessage()+"\n");
		        }
		        return MessageModel.falseInstance(sb.toString());
			} catch (Throwable e) {
				log.error("流程定义文件校验出错", e);
			} finally {
				file.delete();
			}
		}
		return MessageModel.trueInstance("OK");
	}

	/**
	 * qiucs 2014-12-23 下午3:21:48
	 * <p>描述: 获取流程节点 </p>
	 * @return Object
	 * @throws WFDefineException 
	 * @throws DocumentException 
	 */
	public Object getActivities(String id) throws WFDefineException, DocumentException {
		WorkflowVersion entity = WorkflowUtil.getWorkflowVersion(id);
		WorkflowDefine flowDefine = WorkflowUtil.getWorkflowEntity(entity.getWorkflowId());
		String processId = WorkflowUtil.getProcessIdByCode(flowDefine.getWorkflowCode());
		String version = entity.getVersion();
		String fileName = WorkflowUtil.getFileName(
				flowDefine.getWorkflowCode(), version);
		String filePath = WorkflowUtil.getLocalXpdlFilePath(fileName);
		File file = new File(filePath);
		return getActivities(file, processId);
	}
	
	/**
	 * qiucs 2015-4-29 下午10:19:13
	 * <p>描述: 获取流程节点 </p>
	 * @return List<Map<String,String>>
	 */
	private List<Map<String, String>> getActivities(File file, String processId) {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		if (!file.exists()) return list;
		BufferedReader br = null;
		InputStreamReader in = null;
		StringBuilder sb = new StringBuilder();
		try {
			in = new InputStreamReader(new FileInputStream(file), "utf-8");
			br = new BufferedReader(in);
			String str = null;
			while ((str = br.readLine()) != null) {
				sb.append(str);
			}
			List<Element> returnElements = new ArrayList<Element>();
			Document document = DocumentHelper.parseText(sb.toString());
			Element process = (Element) document.selectSingleNode("//WorkflowProcess[@id='" + processId + "']");
			Element startElement = (Element) process.selectSingleNode("./Activities/Activity[@type='START']");
			if (startElement != null) {
				returnElements.add(startElement);
			}
			List<Element> transtions = process.selectNodes("./Transitions/Transition");
			sortActivity(process, startElement, transtions, returnElements);
			Map<String, String> item = null;
			Set<String> activitySet = new HashSet<String>();
			String id = null, name = null;
			for (Element element : returnElements) {
				item = new HashMap<String, String>();
				id = element.attributeValue("id");
				name = element.attributeValue("name");
				if (activitySet.contains(id)) {
					log.warn("流程ID（" + processId + "）中节点名称（" + name + "）重复获取！");
					continue;
				}
				activitySet.add(id);
				item.put("id", id);
				item.put("name", name);
				item.put("type", element.attributeValue("type"));
				list.add(item);
			}
			activitySet.clear(); activitySet = null;
		} catch (Exception e) {
			log.error("读取流程文件获取节点出错", e);
		} finally {
			try {
				if (br != null) {
					br.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				log.error("获取节点时关闭IO流出错", e);
			}
		}
		
		return list;
	}
	
	/**
	 * qiucs 2014-12-29 下午10:00:48
	 * <p>描述: 流程节点排序 </p>
	 * @return void
	 */
	private void sortActivity(Element process, Element node, List<Element> transtions, List<Element> returnElements) {
		if (node == null) return;
		String fromId = node.attributeValue("id");
		List<Element> ts = process.selectNodes("./Transitions/Transition[@from='" + fromId + "']");
		List<Element> toActivitiesElements = new ArrayList<Element>();
		for (Element t : ts) {
			String toId = t.attributeValue("to");
			Element activity = (Element) process.selectSingleNode("./Activities/Activity[@id='" + toId + "']");
			if (activity != null) {
				if ("FINISH".equalsIgnoreCase(activity.attributeValue("type"))) {
					// 结束节点
				}
				if (!returnElements.contains(activity)) returnElements.add(activity);
				
				toActivitiesElements.add(activity);
			}
		}
		for (Element tElement : toActivitiesElements) {
			sortActivity(process, tElement, transtions, returnElements);
		}
	}
	
	
	/**
	 * @date 2014-12-24 下午9:40:26
	 * <p>描述: 同步服务器 </p>
	 * @return Object
	 * @throws Exception 
	 */
	public Object syncServer(String id, String serverIp) throws Exception {
		WorkflowVersion entity = WorkflowUtil.getWorkflowVersion(id);
		if (null == entity)	return MessageModel.falseInstance("OK");
		
		WorkflowDefine flDefine = WorkflowUtil.getWorkflowEntity(entity.getWorkflowId());
		List<WorkflowVersion> versions = find("EQ_workflowId=" + flDefine.getId(), new Sort("showOrder"));
		String fileName = WorkflowUtil.getFileName(flDefine.getWorkflowCode(), entity.getVersion());
		String filePath = WorkflowUtil.getLocalXpdlFilePath(fileName);
		File file = new File(filePath);
		List defineXmlFiles = CoflowClient.updateProject(file, serverIp);
		DefineXmlFile xmlfile = null;
		List<String> xmlFileNames = new ArrayList<String>();
		for (int i = 0; i < defineXmlFiles.size(); i++) {
			xmlfile = (DefineXmlFile) defineXmlFiles.get(i);
			xmlFileNames.add(xmlfile.getFileName());
			/*
			 * processerFile =
			 * WebCoflowUitl.getProcessFileByFileNameAndVersion(flowDefine
			 * ,xmlfile.getFileName(),xmlfile.getPackageVersion());
			 * if(processerFile != null){
			 * processerFile.setVersion(xmlfile.getPackageVersion());
			 * processerFile.setStatus(xmlfile.getStatus().toLowerCase());
			 * this.processerFileService.save(processerFile); }
			 */
		}
		for (WorkflowVersion version : versions) {
			fileName = WorkflowUtil.getFileName(flDefine.getWorkflowCode(), version.getVersion());
			if (!xmlFileNames.contains(fileName)) {
				if (StringUtil.isNotEmpty(version.getStatus()) && !Const.LOCAL.equals(version.getStatus())) {
					version.setStatus(Const.ERROR);
					getDao().save(version);
				}
			}
		}

		return MessageModel.trueInstance("OK");
	}

	/**
	 * qiucs 2014-12-24 下午10:37:23
	 * <p>描述: 删除流程 </p>
	 * @return Object
	 * @throws Exception 
	 */
	@Transactional
	public Object deleteProcess(String id, String serverIp) {
		WorkflowVersion entity = WorkflowUtil.getWorkflowVersion(id);
		if (null == entity) return MessageModel.falseInstance("OK");
		WorkflowDefine flDefine = WorkflowUtil.getWorkflowEntity(entity.getWorkflowId());
		String fileName = WorkflowUtil.getFileName(flDefine.getWorkflowCode(), entity.getVersion());
		String filePath = WorkflowUtil.getServerXpdlFilePath(fileName);
		File file = new File(filePath);
		try {
			CoflowClient.deleteProcess(file, serverIp);
			if (file.exists()) file.delete();
		} catch (Exception e) {
			log.error("删除流程出错", e);
			return MessageModel.falseInstance(e.getMessage());
		}
		entity.setStatus(Const.LOCAL);
		save(entity);
		return MessageModel.trueInstance(entity);
	}

	/**
	 * qiucs 2014-12-24 下午10:46:09
	 * <p>描述: 注册流程 </p>
	 * @return Object
	 * @throws DocumentException 
	 * @throws Exception 
	 */
	@Transactional
	public Object registerProcess(String id, String serverIp) throws DocumentException {
		WorkflowVersion entity = WorkflowUtil.getWorkflowVersion(id);
		if (null == entity)
			return MessageModel.falseInstance("OK");
		WorkflowDefine flDefine = WorkflowUtil.getWorkflowEntity(entity.getWorkflowId());
		String fileName = WorkflowUtil.getFileName(flDefine.getWorkflowCode(), entity.getVersion());
		String filePath = WorkflowUtil.getServerXpdlFilePath(fileName);
		System.out.println(filePath);
		File file = new File(filePath);
		if (!file.exists()) return MessageModel.falseInstance("文件不存在！");
		
		Document document = null;
		String fileText = WebCoflowUitl.readFile(file, "utf-8");
		document = DocumentHelper.parseText(fileText);
		Element root = document.getRootElement();
		String packageId = root.attributeValue("id");
		try {
			CoflowClient.registerProcess(file, serverIp);
		} catch (Exception e) {
			log.error("注册流程出错", e);
			return MessageModel.falseInstance(e.getMessage());
		}
		entity.setStatus(Const.RUNNING);
		save(entity);
		List<WorkflowVersion> pfs = find("EQ_workflowId=" + flDefine.getId(), new Sort("showOrder"));
		//String tempPackageId = "";
		WorkflowVersion pf = null;
		for (int i = 0; i < pfs.size(); i++) {
			pf = pfs.get(i);
			if (!id.equals(pf.getId())) {
				/*fileName = WorkflowUtil.getFileName(flDefine.getWorkflowCode(), entity.getVersion());
				filePath = WorkflowUtil.getServerXpdlFilePath(fileName);
				System.out.println(filePath);
				file = new File(filePath);
				fileText = WebCoflowUitl.readFile(file);
				document = DocumentHelper.parseText(fileText);
				root = document.getRootElement();
				tempPackageId = root.attributeValue("id");
				if (packageId.equals(tempPackageId)) {*/
					if (Const.RUNNING.equals(pf.getStatus())) {
						pf.setStatus(Const.UPDATED);
						getDao().save(pf);
					} else if (Const.MODIFIED_RUNNING.equals(pf.getStatus())) {
						pf.setStatus(Const.MODIFIED_UPDATED);
						getDao().save(pf);
					}
				/*}*/
			}
		}
		// 同步相关数据
		syncDataFieldTable(id, serverIp);
		// 
		
		return MessageModel.trueInstance(pfs);
	}

	/**
	 * qiucs 2014-12-24 下午10:57:13
	 * <p>描述: 启动流程 </p>
	 * @return Object
	 * @throws DocumentException 
	 * @throws Exception 
	 */
	@Transactional
	public Object startProcess(String id, String serverIp) throws DocumentException {
		WorkflowVersion entity = WorkflowUtil.getWorkflowVersion(id);
		if (null == entity)
			return MessageModel.falseInstance("OK");
		WorkflowDefine flDefine = WorkflowUtil.getWorkflowEntity(entity.getWorkflowId());
		String fileName = WorkflowUtil.getFileName(flDefine.getWorkflowCode(), entity.getVersion());
		String filePath = WorkflowUtil.getServerXpdlFilePath(fileName);
		File file = new File(filePath);
		if (!file.exists())
			return MessageModel.falseInstance("流程文件不存在！");

		Document document = null;
		String fileText = WebCoflowUitl.readFile(file, "utf-8");
		document = DocumentHelper.parseText(fileText);
		Element root = document.getRootElement();
		String packageId = root.attributeValue("id");
		try {
			CoflowClient.startProcess(file, serverIp);
		} catch (Exception e) {
			log.error("启动流程出错", e);
			return MessageModel.falseInstance(e.getMessage());
		}
		entity.setStatus(Const.RUNNING);
		save(entity);
		List<WorkflowVersion> pfs = find("EQ_workflowId=" + flDefine.getId(), new Sort("showOrder"));
		String tempPackageId = "";
		WorkflowVersion pf = null;
		for (int i = 0; i < pfs.size(); i++) {
			pf = pfs.get(i);
			if (!id.equals(pf.getId())) {
				/*fileName = WorkflowUtil.getFileName(flDefine.getWorkflowCode(), entity.getVersion());
				filePath = WorkflowUtil.getFilePath(fileName);
				file = new File(filePath);
				fileText = WebCoflowUitl.readFile(file);
				document = DocumentHelper.parseText(fileText);
				root = document.getRootElement();
				tempPackageId = root.attributeValue("id");
				if (packageId.equals(tempPackageId)) {*/
					if (Const.RUNNING.equals(pf.getStatus())) {
						pf.setStatus(Const.UPDATED);
						getDao().save(pf);
					} else if (Const.MODIFIED_RUNNING.equals(pf.getStatus())) {
						pf.setStatus(Const.MODIFIED_UPDATED);
						getDao().save(pf);
					}
				/*}*/
			}
		}
		return MessageModel.trueInstance(pfs);
	}

	/**
	 * qiucs 2014-12-24 下午11:03:02
	 * <p>描述: 停止流程  </p>
	 * @return Object
	 * @throws Exception 
	 */
	@Transactional
	public Object stopProcess(String id, String serverIp) {
		WorkflowVersion entity = WorkflowUtil.getWorkflowVersion(id);
		if (null == entity)
			return MessageModel.falseInstance("OK");
		WorkflowDefine flDefine = WorkflowUtil.getWorkflowEntity(entity.getWorkflowId());
		String fileName = WorkflowUtil.getFileName(flDefine.getWorkflowCode(), entity.getVersion());
		String filePath = WorkflowUtil.getServerXpdlFilePath(fileName);
		File file = new File(filePath);
		if (!file.exists())
			return MessageModel.falseInstance("流程文件不存在！");
		
		try {
			CoflowClient.stopProcess(file, serverIp);
		} catch (Exception e) {
			log.error("停止流程出错", e);
			return MessageModel.falseInstance(e.getMessage());
		}
		entity.setStatus(Const.STOPPED);
		getDao().save(entity);
		
		return MessageModel.trueInstance(entity);
	}

	/**
	 * qiucs 2014-12-24 下午11:07:01
	 * <p>描述: 上传流程 </p>
	 * @return Object
	 * @throws DocumentException 
	 * @throws Exception 
	 */
	@Transactional
	public Object uploadProcess(String id, String serverIp) throws DocumentException {
		return uploadProcessFile(id, serverIp, true);
	}


	/**
	 * qiucs 2015-3-26 上午11:48:25
	 * <p>描述: 刷新流程 </p>
	 * @return Object
	 */
	@Transactional
	public Object refreshProcess(String id, String serverIp) throws DocumentException {
		return uploadProcessFile(id, serverIp, false);
	}
	
	/**
	 * qiucs 2015-3-26 上午11:51:30
	 * <p>描述: 上传流程文件 </p>
	 * @return MessageModel
	 */
	@Transactional
	private MessageModel uploadProcessFile(String id, String serverIp, boolean changeStatus) throws DocumentException {
		WorkflowVersion entity = WorkflowUtil.getWorkflowVersion(id);
		if (null == entity)
			return MessageModel.falseInstance("OK");
		WorkflowDefine flDefine = WorkflowUtil.getWorkflowEntity(entity
				.getWorkflowId());
		String fileName = WorkflowUtil.getFileName(flDefine.getWorkflowCode(),
				entity.getVersion());
		String filePath = WorkflowUtil.getLocalXpdlFilePath(fileName);
		File file = new File(filePath);
		if (!file.exists())
			return MessageModel.falseInstance("流程文件不存在！");

		String version = "";
		String author = "";
		version = entity.getVersion();
		Document document = null;
		String fileText = WebCoflowUitl.readFile(file, "utf-8");
		document = DocumentHelper.parseText(fileText);
		Element root = document.getRootElement();
		Element packageHeaderNode = root.element("PackageHeader");
		Element authorNode = packageHeaderNode.element("Author");
		if (authorNode != null) {
			author = authorNode.getText();
		}
		if (author == null) {
			author = "";
		}
		if (version == null || "".equals(version)) {
			version = "1.0";
		}
		try {
			CoflowClient.checkInFile(serverIp, file, version, author);
		} catch (Exception e) {
			log.error("上传流程出错", e);
			return MessageModel.falseInstance(e.getMessage());
		}
		
		if (changeStatus) {
			entity.setStatus(Const.UNREGISTERED);
			save(entity);
		}
		// 同步流程节点信息到数据库中
		String packageId = WorkflowUtil.getPackageIdByCode(flDefine.getWorkflowCode());
		String processId = WorkflowUtil.getProcessIdByCode(flDefine.getWorkflowCode());
		syncActivies(file, packageId, version, processId);
		
		return MessageModel.trueInstance(entity);
	}
	
	/**
	 * qiucs 2015-4-29 下午10:33:21
	 * <p>描述: 同步流程节点信息到数据库中 </p>
	 * @return void
	 */
	@Transactional
	private void syncActivies(File file, String packageId, String packageVersion, String processId) {
		List<Map<String, String>> list = getActivities(file, processId);
		getService(WorkflowActivityService.class).save(packageId, packageVersion, processId, list);
	}

	/**
	 * qiucs 2014-12-24 下午11:12:16
	 * <p>描述: 同步相关数据表 </p>
	 * @return Object
	 */
	public Object syncDataFieldTable(String id, String serverIp) {
		WorkflowVersion entity = WorkflowUtil.getWorkflowVersion(id);
		if (null == entity)
			return MessageModel.falseInstance("OK");
		WorkflowDefine flDefine = WorkflowUtil.getWorkflowEntity(entity.getWorkflowId());
		String fileName = WorkflowUtil.getFileName(flDefine.getWorkflowCode(), entity.getVersion());
		String filePath = WorkflowUtil.getServerXpdlFilePath(fileName);
		File file = new File(filePath);
		if (!file.exists())
			return MessageModel.falseInstance("流程文件不存在，请检查！");

		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf-8"));
			String line = null, tableName = null;
			StringBuffer sb = new StringBuffer();
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			Document document = DocumentHelper.parseText(sb.toString());
			Element root = document.getRootElement();
			List<Element> wps = root.elements("WorkflowProcesses");
			for (Element e : wps) {
				List<Element> wpList = e.elements("WorkflowProcess");
				if (CollectionUtils.isEmpty(wpList)) continue;
				for (Element wp : wpList) {
					Element dataFieldsNode = wp.element("DataFields");
					if (null == dataFieldsNode) continue;
					List<Element> dfs = dataFieldsNode.elements("DataField");
					DBTable table = new DBTable();
					table.setPackageId(root.attributeValue("id"));
					table.setProcessId(wp.attributeValue("id"));
					for (Element df : dfs) {
						Element dTypeNode = df.element("DataType");
						Element basicType = dTypeNode.element("BasicType");
						DBField field = new DBField(
								df.attributeValue("id"),
								df.attributeValue("name"),
								basicType.attributeValue("type"));
						table.addField(field);
					}
					CoflowClient.synchronizeDataFieldTable(serverIp, table);
				}
			}
		} catch (Throwable e) {
			log.error("同步相关数据表出错", e);
			return MessageModel.falseInstance("同步相关数据表出错");
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					log.error("同步相关数据表（关闭IO流）出错", e);
					return MessageModel.falseInstance("同步相关数据表出错");
				}
			}
		}
		return MessageModel.trueInstance("OK");
	}

	/**
	 * qiucs 2014-12-25 上午11:43:42
	 * <p>描述: 部门 </p>
	 * @return Object
	 * @throws Exception 
	 */
	public Object parseOrganization(String id) throws Exception {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		if (StringUtil.isEmpty(id)) {
			OrgInfo rootOrg = FacadeUtil.getOrgInfoFacade().findByID("-1");
			Map<String, Object> item = new HashMap<String, Object>();
			item.put("id", rootOrg.getId() + "_department");
			item.put("name", rootOrg.getName());
			item.put("pId", "");
			item.put("type", "DEPARTMENT");
			item.put("isParent", Boolean.TRUE);
			list.add(item);
		} else {
			String organizeId = id.replace("_department", "");
			List<OrgInfo> orgs = FacadeUtil.getOrgInfoFacade().findChildsByParentId(organizeId);
			if(orgs != null && !orgs.isEmpty()) {
				for(OrgInfo org : orgs) {
					Map<String, Object> item = new HashMap<String, Object>();
					item.put("id", org.getId() + "_department");
					item.put("name", org.getName());
					item.put("pId", organizeId + "_department");
					item.put("type", "DEPARTMENT");
					item.put("isParent", CollectionUtils.isNotEmpty(FacadeUtil.getOrgInfoFacade().findChildsByParentId(org.getId())));
					list.add(item);
				}
			}
		}
		return list;
	}

	/**
	 * qiucs 2014-12-25 上午11:59:32
	 * <p>描述: 用户 </p>
	 * @return Object
	 * @throws Exception 
	 */
	public Object parseUser(String id) throws Exception {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		if (StringUtil.isEmpty(id)) {
			OrgInfo rootOrg = FacadeUtil.getOrgInfoFacade().findByID("-1");
			Map<String, Object> item = new HashMap<String, Object>();
			item.put("id", rootOrg.getId() + "_department");
			item.put("name", rootOrg.getName());
			item.put("pId", "");
			item.put("type", "DEPARTMENT");
			item.put("isParent", Boolean.TRUE);
			list.add(item);
		} else {
			String organizeId = id.replace("_department", "");
			List<OrgInfo> orgs = FacadeUtil.getOrgInfoFacade().findChildsByParentId(organizeId);
			List<UserInfo> users = null;
			if(orgs != null && !orgs.isEmpty()) {
				for(OrgInfo org : orgs) {
					boolean isParent = false;
					Map<String, Object> item = new HashMap<String, Object>();
					isParent = CollectionUtils.isNotEmpty(FacadeUtil.getOrgInfoFacade().findChildsByParentId(org.getId()));
					item.put("id", org.getId() + "_department");
					item.put("name", org.getName());
					item.put("pId", organizeId + "_department");
					item.put("type", "DEPARTMENT");
					
					users = FacadeUtil.getUserInfoFacade().findUsersByOrgId(organizeId);
					
					item.put("isParent", isParent || CollectionUtils.isNotEmpty(users));
					list.add(item);
				}
			} else {
				users = FacadeUtil.getUserInfoFacade().findUsersByOrgId(organizeId);
				if(users != null && !users.isEmpty()){//存在用户，则返回用户
					for(UserInfo user : users) {
						Map<String, Object> item = new HashMap<String, Object>();
						item.put("id", user.getId());
						item.put("name", user.getName());
						item.put("pId", organizeId + "_department");
						item.put("type", "HUMAN");
						item.put("isParent", false);						
						list.add(item);
					}
				}
			}
		}
		return list;
	}

	/**
	 * qiucs 2014-12-25 下午1:08:41
	 * <p>描述: 自定义公式 </p>
	 * @return Object
	 * @throws DocumentException 
	 */
	public Object customFormula() throws DocumentException {
		String filePath = WorkflowUtil.getFormulaFilePath();
		File formulaFile = new File(filePath);
		if (!formulaFile.exists()) return MessageModel.falseInstance("Formula.xml文件不存在！");
		
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		
		String fileContents = WebCoflowUitl.readFile(formulaFile,"utf-8");
		Document document = DocumentHelper.parseText(fileContents);
		Element root = document.getRootElement();
		if(root!=null){
			Iterator<Element> formuals = root.elementIterator();
			while(formuals.hasNext()){
				Element formula = formuals.next();
				if(formula.getName().equals("Formula")){
					Iterator<Element> props = formula.elementIterator();
					Map<String, Object> item = new HashMap<String, Object>();
					while(props.hasNext()){
						Element prop = props.next();
						if(prop.getName().equals("name")){
							item.put("name", prop.getText().trim());
						}else if(prop.getName().equals("value")){
							item.put("value", prop.getText().trim());
						}
					}
					if (!item.isEmpty()) list.add(item);
				}
			}
		}
		return MessageModel.trueInstance(list);
	}

	/**
	 * qiucs 2014-12-25 下午1:25:48
	 * <p>描述: 导入相关数据 </p>
	 * @return Object
	 */
	public Object importDataField(String id) {
		
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		WorkflowVersion entity = WorkflowUtil.getWorkflowVersion(id);
		if (null == entity)
			return MessageModel.falseInstance("当前版本数据已被删除，请刷新！");
		WorkflowDefine flDefine = WorkflowUtil.getWorkflowEntity(entity.getWorkflowId());
		List<ColumnDefine> clist = getService(ColumnDefineService.class).findByTableId(flDefine.getBusinessTableId());
		for (ColumnDefine col : clist) {
			Map<String, Object> item = new HashMap<String, Object>();
			item.put("id", col.getColumnName().toLowerCase());
			item.put("name", col.getShowName());
			item.put("dataType", getDbType(col.getDataType()));
			item.put("initValue", StringUtil.null2empty(col.getDefaultValue()));
			item.put("desc", col.getShowName());
			list.add(item);
		}
		return list;
	}
	
	/**
	 * qiucs 2014-12-25 下午9:20:51
	 * <p>描述: 数据库类型转换 </p>
	 * @return String
	 */
	private String getDbType(String type) {
		if (ConstantVar.DataType.NUMBER.equals(type)) {
			return "整型";
		} else if (ConstantVar.DataType.DATE.equals(type)) {
			return "日期型";
		}
		return "字符型";
	}

	/**
	 * qiucs 2014-12-25 下午1:44:57
	 * <p>描述: 添加本地副本 </p>
	 * @return Object
	 * @throws IOException 
	 */
	@Transactional
	public Object addLocalCopy(String id, String version) throws IOException {
		WorkflowVersion entity = WorkflowUtil.getWorkflowVersion(id);
		if (null == entity)
			return MessageModel.falseInstance("当前版本数据已被删除，请刷新！");
		
		MessageModel msg = checkUnique(buildSpecification("EQ_version=" + version + ";EQ_workflowId=" + entity.getWorkflowId()), "");
		if (!msg.getSuccess()) {
			msg.setMessage("版本号重复，请修改！");
			return msg;
		}
		
		WorkflowDefine flDefine = WorkflowUtil.getWorkflowEntity(entity.getWorkflowId());
		String fileName = WorkflowUtil.getFileName(flDefine.getWorkflowCode(), entity.getVersion());
		String filePath = WorkflowUtil.getLocalXpdlFilePath(fileName);
		File file = new File(filePath);
		if (!file.exists())
			return MessageModel.falseInstance("当前版本流程文件不存在，请刷新！");
		
		WorkflowVersion destEntity = new WorkflowVersion();
		Integer showOrder = getDao().getMaxShowOrder(flDefine.getId());
		BeanUtils.copy(entity, destEntity);
		destEntity.setId(null);
		destEntity.setVersion(version);
		destEntity.setStatus(Const.LOCAL);
		destEntity.setShowOrder(++showOrder);
		destEntity.setRemark("复制来源：版本(" + entity.getVersion() + ")");
		String destFileName = WorkflowUtil.getFileName(flDefine.getWorkflowCode(), version);
		String destFilePath = WorkflowUtil.getLocalXpdlFilePath(destFileName);
		File destFile = new File(destFilePath);
		FileUtils.copyFile(file, destFile);
		
		getDao().save(destEntity);
		
		copyConfiguration(flDefine.getId(), id, destEntity.getId());
		
		return MessageModel.trueInstance(destEntity);
	}
	
	/**
	 * qiucs 2015-4-28 上午10:37:29
	 * <p>描述: 拷贝上一版本的配置信息 </p>
	 * @return void
	 */
	@Transactional
	private void copyConfiguration(String workflowId, String fromVersionId, String toVersionId) {
		String tableId = WorkflowUtil.getBusinessTableId(workflowId),
				viewId = WorkflowUtil.getViewId(workflowId);
		// 应用定义
		getService(AppSearchPanelService.class).copyWorkflow(viewId, fromVersionId, toVersionId);
		getService(AppGridService.class).copyWorkflow(viewId, fromVersionId, toVersionId);
		getService(AppSortService.class).copyWorkflow(viewId, fromVersionId, toVersionId);
		getService(AppButtonService.class).copyWorkflow(viewId, fromVersionId, toVersionId);
		getService(AppFormService.class).copyWorkflow(tableId, fromVersionId, toVersionId);
		// 个性设置
		getService(WorkflowFormSettingService.class).copyWorkflow(fromVersionId, toVersionId);
		getService(WorkflowButtonSettingService.class).copyWorkflow(fromVersionId, toVersionId);
		getService(WorkflowAssistOpinionService.class).copyWorkflow(fromVersionId, toVersionId);
	}

	/**
	 * qiucs 2014-12-25 下午5:27:51
	 * <p>描述: 格式化流程文件 </p>
	 * @return Object
	 * @throws DocumentException 
	 */
	public Object formatShow(String data) throws DocumentException {
		Document document;
		document = DocumentHelper.parseText(data);
		StringWriter out = null;
		XMLWriter xmlWriter = null;
		try {
			OutputFormat format = OutputFormat.createPrettyPrint();
			format.setEncoding("utf-8");
			out = new StringWriter();
			xmlWriter = new XMLWriter(out, format);
			xmlWriter.write(document);
			xmlWriter.flush();
		} catch (IOException e) {
			log.error("格式化流程文件（写流文件）出错", e);
		} finally {
			try {
				if (xmlWriter != null)
					xmlWriter.close();
				if (out != null)
					out.close();
			} catch (IOException e) {
				log.error("格式化流程文件（关闭IO流）出错", e);
			}
		}
		return out.toString();
	}
	
	/**
	 * qiucs 2014-12-29 下午2:05:58
	 * <p>描述: 获取正在运行中的流程版本 </p>
	 * @return String
	 */
	public String getRunningVersionId(String workflowId) {
		String filters = "EQ_workflowId=" + workflowId + ";EQ_status=" + Const.RUNNING;
		List<WorkflowVersion> list = find(filters);
		if (CollectionUtils.isEmpty(list)) return null;
		if (list.size() > 1) {
			log.warn("工作流程（" + WorkflowUtil.getWorkflowEntity(workflowId).getWorkflowName() + "）同时存在多个状态为“运行中”的版本，请检查！");
		}
		return list.get(0).getId();
	}
	
	/**
	 * qiucs 2014-12-29 下午2:31:08
	 * <p>描述: 获取表单对应的流程版本ID </p>
	 * @return String
	 */
	public String getFormVersionId(String workflowId, String processInstanceId) {
		String version = getVersionByProcessInstanceId(processInstanceId);
		String filters = "EQ_workflowId=" + workflowId + ";EQ_version=" + version;
		List<WorkflowVersion> list = find(filters);
		if (CollectionUtils.isEmpty(list)) return null;
		if (list.size() > 1) {
			log.warn("工作流程（" + WorkflowUtil.getWorkflowEntity(workflowId).getWorkflowName() + "）同时存在多个相同版本号（" + version + "），请检查！");
		}
		return list.get(0).getId();
	}
	
	/**
	 * qiucs 2014-12-29 下午2:31:55
	 * <p>描述: 根据流程实例ID，从工作流引擎中获取对应的版本号 </p>
	 * @return String
	 */
	private String getVersionByProcessInstanceId(String processInstanceId) {
		String sql = "select t.package_version from t_wf_process_instance t where t.id=" + processInstanceId;
		Object version = DatabaseHandlerDao.getInstance().queryForObject(sql);
		if (null == version) return "1.0";
		return String.valueOf(version);
	}
	
	/**
	 * qiucs 2014-12-29 下午8:00:37
	 * <p>描述: 获取工作流流程开始节点 </p>
	 * @return String
	 */
	public String getStartActivityId(String id) {
		WorkflowVersion entity = WorkflowUtil.getWorkflowVersion(id);
		if (null == entity)
			return "";
		WorkflowDefine flowDefine = WorkflowUtil.getWorkflowEntity(entity.getWorkflowId());
		String code = flowDefine.getWorkflowCode();
		WorkflowProcess wfp = null;
		try {
			wfp = Coflow.getDefineService().getProcess(
					WorkflowUtil.getPackageIdByCode(code),
					entity.getVersion(),
					WorkflowUtil.getProcessIdByCode(code));
		} catch (WFDefineException e) {
			log.error("获取工作流流程出错", e);
		}
		if (null != wfp) {
			@SuppressWarnings("unchecked")
			List<Activity> activities = wfp.getActivities();
			for (Activity a : activities) {
				if (a.isStartActivity()) return a.getId();
			}
		} else {
			log.error(flowDefine.getWorkflowName() + "中版本号为" + entity.getVersion() + "流程不存在，请联系管理员！");
		}
		return "";
	}
	
	/**
	 * qiucs 2015-4-13 下午7:23:06
	 * <p>描述: 获取流程中所有节点 </p>
	 * @return List<Activity>
	 */
	public List<Activity> getActivities(String packageId, String packageVersion, String processId) {
		WorkflowProcess wfp = null;
		try {
			wfp = Coflow.getDefineService().getProcess(
					packageId,
					packageVersion,
					processId);
		} catch (WFDefineException e) {
			log.error("获取工作流流程出错", e);
		}
		if (null != wfp) {
			@SuppressWarnings("unchecked")
			List<Activity> activities = wfp.getActivities();
			return activities;
		} else {
			log.error("流程包ID（" + packageId + "）中版本号为" + packageVersion + "流程不存在，请联系管理员！");
		}
		return null;
	}
	
	/**
	 * qiucs 2015-3-24 下午12:45:10
	 * <p>描述: 获取流程中的第一个节点（即开始节点下一个节点） </p>
	 * @return String
	 */
	@SuppressWarnings("unchecked")
	public String getFirstActivityId(String id) {
		WorkflowVersion entity = WorkflowUtil.getWorkflowVersion(id);
		if (null == entity)
			return StringUtil.EMPTY;
		WorkflowDefine flowDefine = WorkflowUtil.getWorkflowEntity(entity.getWorkflowId());
		String code = flowDefine.getWorkflowCode();
		WorkflowProcess wfp = null;
		try {
			wfp = Coflow.getDefineService().getProcess(
					WorkflowUtil.getPackageIdByCode(code),
					entity.getVersion(),
					WorkflowUtil.getProcessIdByCode(code));
		} catch (WFDefineException e) {
			log.error("获取工作流流程出错", e);
		}
		String firstActivityId = StringUtil.EMPTY;
		if (null != wfp) {
			List<Activity> activities = wfp.getActivities();
			for (Activity a : activities) {
				if (a.isStartActivity()) {
					List<Transition> list = a.getSplitTransitions();
					if (CollectionUtils.isNotEmpty(list)) {
						firstActivityId = list.get(0).getTo();
					}
					break;
				}
			}
		} else {
			log.error(flowDefine.getWorkflowName() + "中版本号为" + entity.getVersion() + "流程不存在，请联系管理员！");
		}
		return firstActivityId;
	}
	
	/**
     * 获取工作流版本
     * 
     * @param workflowId 工作流定义ID
     * @return List<WorkflowVersion>
     */
    public List<WorkflowVersion> getByWorkflowId(String workflowId) {
        return getDao().getByWorkflowId(workflowId);
    }
    
    /**
     * qiucs 2015-4-1 下午8:45:24
     * <p>描述: 检查是否有正在运行的流程版本 </p>
     * @return boolean
     */
    public boolean checkRunning(String workflowId) {
    	List<WorkflowVersion> list = find("EQ_workflowId=" + workflowId + ";EQ_status=" + Const.RUNNING);
    	return CollectionUtils.isNotEmpty(list);
    }
}

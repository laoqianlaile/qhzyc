package com.ces.coflow.web.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import ces.workflow.wapi.org.Department;
import ces.workflow.wapi.org.Organization;
import ces.workflow.wapi.org.User;

public class WebCoflowUitl {

	/**
	 * 读取文件内容
	 * @param file
	 * @return
	 */
	public static String readFile(File file,String... charsets){
		String charset = null;
		 for(int i=0;i<charsets.length;i++){
			 charset = charsets[0];
			 break;
		}
		BufferedReader br = null;
		StringBuffer sb = new StringBuffer();
		try {
			if(charset!=null)
				br = new BufferedReader(new InputStreamReader(new FileInputStream(file), charset));
			else
				br = new BufferedReader(new FileReader(file));
			String line = null;
			while((line = br.readLine())!=null){
				sb.append(line+"\r\n");
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(br != null){
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return sb.toString();
	}
	
	public static Organization parserOrganization(File file){
		Organization organization = null;
		try {
			String fileContents = WebCoflowUitl.readFile(file,"utf-8");
			Document document = DocumentHelper.parseText(fileContents);
			Element root = document.getRootElement();
			if(root != null){
				organization = new Organization();
				Iterator<Element> iterator = root.elementIterator();
				while(iterator.hasNext()){
					Element element = iterator.next();
					if(element.getName().equals("Departments")){
						Iterator<Element> departmentsEle = element.elementIterator();
						while(departmentsEle.hasNext()){
							Element departmentEle = departmentsEle.next();
							if(departmentEle.getName().equals("Department")){
								Department department = new Department();
								department.setId(departmentEle.attributeValue("id"));
								department.setParentId(departmentEle.attributeValue("parentId"));
								department.setName(departmentEle.attributeValue("name"));
								organization.addDepartment(department);
							}
						}
					}else if(element.getName().equals("Users")){
						Iterator<Element> usersEle = element.elementIterator();
						while(usersEle.hasNext()){
							Element userEle = usersEle.next();
							if(userEle.getName().equals("User")){
								User user = new User();
								user.setId(userEle.attributeValue("id"));
								user.setName(userEle.attributeValue("name"));
								user.setDescription(userEle.attributeValue("desc"));
								Iterator<Element> ownDeptsEle = userEle.elementIterator();
								while(ownDeptsEle.hasNext()){
									Element ownDeptEle = ownDeptsEle.next();
									if(ownDeptEle.getName().equals("OwnDept")){
										user.addOwnDept(ownDeptEle.attributeValue("id"));
									}else if(ownDeptEle.getName().equals("OwnRole")){
										user.addOwnRole(ownDeptEle.attributeValue("id"));
									}
								}
								organization.addUser(user);
							}
						}
					}else if(element.getName().equals("Roles")){
						
					}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return organization;
	}
	
	public static String transStatus2Chinese(String status) {
		if (Const.UNDEFINED.equals(status)) {
			return "未定义";
		} else if (Const.LOCAL.equals(status)) {
			return "本地";
		} else if (Const.RUNNING.equals(status)) {
			return "运行中";
		} else if (Const.UPDATED.equals(status)) {
			return "已更新";
		} else if (Const.STOPPED.equals(status)) {
			return "已停止";
		} else if (Const.UNREGISTERED.equals(status)) {
			return "未注册";
		} else if (Const.UNKNOWN.equals(status)) {
			return "未知";
		} else if (Const.ERROR.equals(status)) {
			return "错误";
		} else if (Const.MODIFIED.equals(status)) {
			return "已修改";
		} else if (Const.MODIFIED_RUNNING.equals(status)) {
			return "已修改运行中";
		} else if (Const.MODIFIED_UPDATED.equals(status)) {
			return "已修改已更新";
		} else if (Const.MODIFIED_STOPPED.equals(status)) {
			return "已修改已停止";
		} else if (Const.MODIFIED_UNREGISTERED.equals(status)) {
			return "已修改未注册";
		} else {
			return status;
		}
	}
	
	public static String getConfigPath() {
        String configPath = WebCoflowUitl.class.getResource("/").getPath();
        try {
            configPath = java.net.URLDecoder.decode(configPath, "UTF-8");
            configPath = configPath.substring(0, configPath.length() - "classes/".length());
            configPath += "config/";
        } catch (UnsupportedEncodingException e) {
        	e.printStackTrace();
        }
        return configPath;
    }
	
	
}

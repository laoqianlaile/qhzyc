<%@page import="com.ces.config.dhtmlx.entity.document.Document"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%
	Document document = (Document)request.getAttribute("doc");

	//System.out.println("======file name======" + document.getFileName());
	String id = document.getId();//request.getParameter("P_id");
	String tableId = document.getTableId();//request.getParameter("P_tableId");
	String fileName = document.getFileName();
	String format   = document.getFileFormat();
	

	/*String id = "4028ff8146d8b5b10146d8b892910061";//document.getId();//request.getParameter("P_id");
	String tableId = "4028ff8146d8b5b10146d8b892910006";//document.getTableId();//request.getParameter("P_tableId");
	String fileName = "test.pdf";//document.getFileName();
	String format   = "pdf";//document.getFileFormat();//*/
	
	String useType = "";
	boolean printed = false;
	boolean saved   = false;
	StringBuffer serverFullFilePath = new StringBuffer(request.getScheme());
	serverFullFilePath
		.append("://").append(request.getServerName())
		.append(":").append(request.getServerPort())
		.append(request.getContextPath())
		.append("/document/document-handle!getDocument?P_id=")
		.append(id)
		.append("&P_tableId=")
		.append(tableId)
		.append("&sessionid=")
		.append(session.getId())
		.append("&P_timestamp=")
		.append(System.currentTimeMillis());
	//serverFullFilePath = new StringBuffer("D:/temp/test.pdf");
	//useType = "local";
	System.out.println("serverPath: " + serverFullFilePath);
	
	//StringBuffer printURL = new StringBuffer(request.getScheme());
	//printURL
	//	.append("://").append(request.getServerName())
	//	.append(":").append(request.getServerPort())
	//	.append(request.getContextPath())
	//	.append("/gdda/logDocPrint@UserLogAction.action?sessionid=" + sessionid + "&id="+id+"&module="+module+"&map.borrowId="+borrowId);
	//System.out.println("printURL: " + printURL);
	
	//StringBuffer saveAsURL = new StringBuffer(request.getScheme());
	//saveAsURL
	//	.append("://").append(request.getServerName())
	//	.append(":").append(request.getServerPort())
	//	.append(request.getContextPath())
	//	.append("/gdda/logDocSaveAs@UserLogAction.action?sessionid=" + sessionid + "&id="+id+"&module="+module+"&map.borrowId="+borrowId);
	//System.out.println("saveAsURL: " + saveAsURL);
	//System.out.println("id: " + id);
	//System.out.println("module: " + module);
	//System.out.println("title: " + title);
	//String value= App.getSetting("waterMarkImage");
	//JSONObject jsonObj= JSONObject.fromObject(value);
	//String imageUrl=jsonObj.get("imageUrl").toString();
	//String tmp=imageUrl.lastIndexOf('.')!=-1?imageUrl.substring(imageUrl.lastIndexOf('.')):"";
	//String view=jsonObj.getString("view");
	//String saveAs=jsonObj.getString("saveAs");
	//String print=jsonObj.getString("print");
	
	/*StringBuffer waterMarkImagePath=new StringBuffer(request.getScheme());
	waterMarkImagePath
		.append("://").append(request.getServerName())
		.append(":").append(request.getServerPort())
		.append(request.getContextPath())
		.append("/gdda/getWaterMarkImage@DocumentAction.action?tmp="+tmp);//*/
	
	int waterMarkUseType=-1;
	/*if(!"1".equals(view)&&!"1".equals(saveAs)&&!"1".equals(print)){//打印另存浏览都不加水印
		waterMarkUseType=-1;
	}else if("1".equals(view)&&"1".equals(saveAs)&&"1".equals(print)){//全部都加
		waterMarkUseType=0;
	}else{
		int temp=0;
		if("1".equals(view)){//1:浏览加水印
			temp+=1;
		}
		if("1".equals(saveAs)){//4：另存加水印
			temp+=4;
		}
		if("1".equals(print)){//2：打印加水印
			temp+=2;
		}
		waterMarkUseType=temp;
	}
	//水印图片没有时默认成不加水印
	if(Util.isNull(imageUrl)){
		waterMarkUseType=-1;
	}//*/
%>
<html>
<head>
<title><%="" %></title>
</head>
<body leftmargin="0" topmargin="0" scroll="no">
<SCRIPT LANGUAGE="JavaScript">

if((navigator.userAgent.indexOf("MSIE") >= 0)) {
	document.body.innerHTML = 
			"<OBJECT"
			+ " id=\"CesBrowser\""
			+ " classid=\"clsid:343DD251-FBB0-49FF-AEEF-47C5DEB9D7B1\""
			+ " title=\"通用浏览器\""
			+ " width=\"100%\""
			+ " height=\"100%\""
		+ ">"
		+ "<param name=\"UseType\" value=\"<%=useType %>\" />"
		//+ "<param name=\"WaterMarkImagePath\" value=\"<%="" %>\" />"
		+ "<param name=\"WaterMarkUseType\" value=\"<%=waterMarkUseType %>\"/>"
		+ "<param name=\"DownLoadFilePath\" value=\"<%=serverFullFilePath.toString() %>\" />"
		+ "<param name=\"EnablePrint\" value=\"<%=printed %>\" />"
		+ "<param name=\"SaveAs\" value=\"<%=saved %>\" />"
		//+ "<param name=\"PrintURL\" value=\"<%="" %>\" />"
		//+ "<param name=\"SaveAsURL\" value=\"<%="" %>\" />"
		+ "<param name=\"ID\" value=\"<%=id %>\" />"
		//+ "<param name=\"Module\" value=\"<%="" %>\" />"
		+ "<param name=\"FileName\" value=\"<%=fileName %>\" />"
		+ "<param name=\"ExtName\" value=\"<%=format %>\"/>"
		+ "</OBJECT>";
} else {
	document.body.innerHTML = 
		document.body.innerHTML = 
			"<OBJECT"
				+ " type=\"application/x-itst-activex\""
				+ " id=\"CesBrowser\""
				+ " clsid=\"{343DD251-FBB0-49FF-AEEF-47C5DEB9D7B1}\""
				+ " title=\"通用浏览器\""
				+ " width=\"100%\""
				+ " height=\"100%\""
				//+ " param_UseType=\"\""
				//+ " param_WaterMarkImagePath=\"<%="" %>\""
				//+ " param_WaterMarkUseType=\"<%=waterMarkUseType %>\""
				+ " param_DownLoadFilePath=\"<%=serverFullFilePath.toString() %>\""
				+ " param_EnablePrint=\"<%=printed %>\""
				+ " param_SaveAs=\"<%=saved %>\""
				//+ " param_PrintURL=\"<%="" %>\""
				//+ " param_SaveAsURL=\"<%="" %>\""
				+ " param_ID=\"<%=id %>\""
				//+ " param_Module=\"<%="" %>\""
				+ " param_FileName=\"<%=fileName %>\""
				+ " param_ExtName=\"<%=format %>\""
			+ ">"
			+ "</OBJECT>";

}
</SCRIPT>
</BODY>
</HTML>
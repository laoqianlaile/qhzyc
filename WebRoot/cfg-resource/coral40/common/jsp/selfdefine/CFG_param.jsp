<%@page import="com.ces.config.utils.JsonUtil"%>
<%@page language="java" import="java.util.*,com.ces.config.utils.StringUtil" pageEncoding="UTF-8"%>
<%

	Map<String, String[]> reqMap = request.getParameterMap();
	final String PARAM_PRE = "P_";
	Iterator<String> it = reqMap.keySet().iterator();
	int beginIdx = PARAM_PRE.length();
	String[] valArr = null;
	Map<String, Object> pMap = new HashMap<String, Object>();
	String key = null;
	Object val = null;
	while (it.hasNext()) {
		key    = it.next();
		valArr = reqMap.get(key);
		if (null != valArr && 1 == valArr.length) {
			val = valArr[0];
		} else {
			val = valArr;
		}
		if (key.startsWith(PARAM_PRE))	key = key.substring(beginIdx);
		pMap.put(key, val);
	}

	String menuId = StringUtil.null2empty(request.getParameter("menuId"));
	if (StringUtil.isEmpty(menuId)) menuId = "-1";
    request.setAttribute("menuId", menuId); // 菜单ID
	request.setAttribute("componentVersionId", StringUtil.null2empty(request.getParameter("componentVersionId")));// 自定义构件在构件生产库中的对应的ID
    request.setAttribute("moduleId", StringUtil.null2empty(request.getParameter("P_moduleId"))); // 自定义构件自身的ID
    request.setAttribute("tableId", StringUtil.null2empty(request.getParameter("P_tableId")));   // 表ID传递给整张页面构件用的
    request.setAttribute("groupId", StringUtil.null2empty(request.getParameter("P_groupId")));   // 物理表组ID传递给逻辑表组构件使用的
    request.setAttribute("columns", StringUtil.null2empty(request.getParameter("P_columns")));   // 主列表的过滤条件格式，如：EQ_C_NAME≡XXX;LIKE_C_TITLE≡XXX;...多个用英文分号(;)分隔
    request.setAttribute("dataId", StringUtil.null2empty(request.getParameter("P_dataId")));     // 
    request.setAttribute("masterTableId", StringUtil.null2empty(request.getParameter("P_masterTableId")));
    request.setAttribute("masterDataId", StringUtil.null2empty(request.getParameter("P_masterDataId")));
    request.setAttribute("masterCgridDivId", StringUtil.null2empty(request.getParameter("P_masterCgridDivId")));
    request.setAttribute("paramMap", JsonUtil.bean2json(pMap));
    request.setAttribute("timestamp", System.currentTimeMillis());
%>
<%@ include file="/portal/jsp/include.jsp" %>
<%@ tag language="java" pageEncoding="UTF-8" body-content="scriptless"%>

<% //-- 元素基本属性设置 %>
<%@ attribute name="id" 	type="java.lang.String" description="元素id"%>
<%@ attribute name="cls" 	type="java.lang.String" description="元素class"%>
<%@ attribute name="icon"   type="java.lang.String" description="图标"%>
<%@ attribute name="title"  type="java.lang.String" description="标题"%>
<%@ attribute name="width" 	type="java.lang.String" description="宽度(需要添加单位)"%>
<%@ attribute name="height" type="java.lang.String" description="高度(需要添加单位)"%>
<%@ attribute name="align" 	type="java.lang.String" description="水平对齐(left|center|right)"%>
<%@ attribute name="valign" type="java.lang.Boolean" description="垂直对齐(false|true)"%>
<%@ attribute name="nostyle" type="java.lang.Boolean" description="是否启用元素内样式(false|true)"%>

<% //-- 元素显示方式设置 %>
<%@ attribute name="floats" 		type="java.lang.String" description="浮动(left|right)"%>
<%@ attribute name="display" 		type="java.lang.String" description="显示方式(参见CSS属性)"%>
<%@ attribute name="overflow" 		type="java.lang.String" description="元素溢出方式(visible|hidden|scroll|auto)"%>
<%@ attribute name="position" 		type="java.lang.String" description="元素定位方式(absolute|relative)"%>
<%@ attribute name="positionTop" 	type="java.lang.String" description="上边距"%>
<%@ attribute name="positionLeft" 	type="java.lang.String" description="左边距"%>
<%@ attribute name="positionRight" 	type="java.lang.String" description="右边距"%>
<%@ attribute name="positionBottom" type="java.lang.String" description="下边距"%>
<%@ attribute name="positionZIndex" type="java.lang.String" description="元素层叠"%>

<% //-- 元素内外间距设置 %>
<%@ attribute name="marginTop" 		type="java.lang.String" description="上外补丁"%>
<%@ attribute name="marginLeft" 	type="java.lang.String" description="左外补丁"%>
<%@ attribute name="marginRight" 	type="java.lang.String" description="右外补丁"%>
<%@ attribute name="marginBottom" 	type="java.lang.String" description="下外补丁"%>
<%@ attribute name="paddingTop" 	type="java.lang.String" description="上内补丁"%>
<%@ attribute name="paddingLeft" 	type="java.lang.String" description="左内补丁"%>
<%@ attribute name="paddingRight" 	type="java.lang.String" description="右内补丁"%>
<%@ attribute name="paddingBottom" 	type="java.lang.String" description="下内补丁"%>

<% //-- 元素边框设置 %>
<%@ attribute name="border" 	  type="java.lang.String" description="边框(width style color)"%>
<%@ attribute name="borderTop"    type="java.lang.String" description="上边框样式(width style color)"%>
<%@ attribute name="borderLeft"   type="java.lang.String" description="左边框样式(width style color)"%>
<%@ attribute name="borderRight"  type="java.lang.String" description="右边框样式(width style color)"%>
<%@ attribute name="borderBottom" type="java.lang.String" description="下边框样式(width style color)"%>

<% //-- 元素背景设置 %>
<%@ attribute name="bgcolor" 	type="java.lang.String" description="背景颜色(transparent|color)"%>
<%@ attribute name="bgimage" 	type="java.lang.String" description="背景图片"%>
<%@ attribute name="bgrepeat" 	type="java.lang.String" description="背景图片重复(repeat|repeat-x|repeat-y|no-repeat)"%>
<%@ attribute name="bgposition" type="java.lang.String" description="背景图片位置(水平 垂直)"%>

<%@ attribute name="fixself" 	type="java.lang.String" description="自身补正（宽,高），用于设置元素边框时"%>
<%@ attribute name="fixwidth" 	type="java.lang.Integer" description="宽度补正（相对于整个文档）"%>
<%@ attribute name="fixheight" 	type="java.lang.Integer" description="高度补正（相对于整个文档）"%>

<% //-- 加载标签体 %>
<jsp:doBody var="pageHTML"/>

<% //-- 定义元素内样式 %>
<c:if test="${!nostyle}">
	<c:set var="innerStyle" scope="page">
		${empty width?"width:100%;":fn:replace("width:_width;", "_width", width)}
		${empty height?"height:100%;":fn:replace("height:_height;", "_height", height)}
		${empty align?"":fn:replace("text-align:_align;","_align",align)}
		${valign?fn:replace("line-height:_height;","_height",height):""}
	
		${empty floats?"":fn:replace("float:_float;", "_float", floats)}
		${empty display?"":fn:replace("display:_display;", "_display", display)}
		${empty overflow?"hidden;":fn:replace("overflow:_overflow;","_overflow", overflow)}
		${empty position?"":fn:replace("position:_position; ","_position", position)}
		${empty positionTop?"":fn:replace("top:_top;", "_top", positionTop)}
		${empty positionLeft?"":fn:replace("left:_left;", "_left", positionLeft)}
		${empty positionRight?"":fn:replace("right:_right;", "_right", positionRight)}
		${empty positionBottom?"":fn:replace("bottom:_bottom;", "_bottom", positionBottom)}
		${empty positionZIndex?"":fn:replace("z-index:_zindex;", "_zindex", positionZIndex)}
	
		${empty marginTop?"":fn:replace("margin-top:_mtop;", "_mtop", marginTop)}
		${empty marginLeft?"":fn:replace("margin-left:_mleft;", "_mleft", marginLeft)}
		${empty marginRight?"":fn:replace("margin-right:_mright;", "_mright", marginRight)}
		${empty marginBottom?"":fn:replace("margin-bottom:_mbottom;", "_mbottom", marginBottom)}
		${empty paddingTop?"":fn:replace("padding-top:_ptop;", "_ptop", paddingTop)}
		${empty paddingLeft?"":fn:replace("padding-left:_pleft;", "_pleft", paddingLeft)}
		${empty paddingRight?"":fn:replace("padding-right:_pright;", "_pright", paddingRight)}
		${empty paddingBottom?"":fn:replace("padding-bottom:_pbottom;", "_pbottom", paddingBottom)}
		
		${empty border?"":fn:replace("border:_border;", "_border", border)}
		${empty borderTop?"":fn:replace("border-top:_borderTop;", "_borderTop", borderTop)}
		${empty borderLeft?"":fn:replace("border-left:_borderLeft;", "_borderLeft", borderLeft)}
		${empty borderRight?"":fn:replace("border-right:_borderRight;", "_borderRight", borderRight)}
		${empty borderBottom?"":fn:replace("border-bottom:_borderBottom;", "_borderBottom", borderBottom)}
		
		${empty bgcolor?"":fn:replace("background-color:_bgcolor;", "_bgcolor", bgcolor)}
		${empty bgimage?"":fn:replace("background-image:url(_bgimage);", "_bgimage", bgimage)}
		${empty bgrepeat?"":fn:replace("background-repeat:_bgrepeat;", "_bgrepeat", bgrepeat)}
		${empty bgposition?"":fn:replace("background-position:_bgposition;", "_bgposition", bgposition)}
	</c:set>
</c:if>

<% //-- 创建DIV元素 %>
<div style='${innerStyle}'
	${empty id?"":fn:replace("id='_id'", "_id", id)} 
	${empty cls?"":fn:replace("class='cls'", "cls", cls)} 
	${empty icon?"":fn:replace("iconCls='_icon'", "_icon", icon)} 
	${empty title?"":fn:replace("title='_title'", "_title", title)} 
	${empty fixself?"":fn:replace("fixself='_fixself'", "_fixself", fixself)} 
	${empty fixwidth?"":fn:replace("fixwidth='_fixwidth'", "_fixwidth", fixwidth)} 
	${empty fixheight?"":fn:replace("fixheight='_fixheight'", "_fixheight", fixheight)} 
	
> ${pageHTML} </div>

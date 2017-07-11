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

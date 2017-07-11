<%@ page import="com.ces.config.utils.CommonUtil" %>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib prefix="ces" tagdir="/WEB-INF/tags" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
    request.setAttribute("idSuffix", CommonUtil.generateUIId(""));
    request.setAttribute("basePath", basePath);
%>
<style>
    .coral-toolbar-border {
        display: block;
        /*width: auto;
        height: auto;*/
        /* overflow */
        /*overflow: hidden;*/
        position: relative;
        float: left;
        left: 0;
        top: 0;
        height: 48px;
    }
</style>
<div id="maxDiv${idSuffix}" class="fill">
    <form id="trplyForm${idSuffix}" method="post" class="coralui-form">

            <div class="fillwidth colspan2 clearfix" style="margin-top: 10px">
            <div class="app-inputdiv6">
                <label class="app-input-label">生产方案编号:</label>
                <input  id="schemeId${idSuffix}" name="schemeId" class="coralui-textbox" />
            </div>
            <div class="app-inputdiv6">
                <label class="app-input-label">饮片名称:</label>
                <input  id="piecesName${idSuffix}" name="piecesName" class="coralui-textbox" />
            </div>
            <div class="app-inputdiv6">
                <label class="app-input-label">原材料:</label>
                <input  id="rawMaterial${idSuffix}" name="rawMaterial" class="coralui-textbox" />
            </div>
            <div class="app-inputdiv6">
                <label class="app-input-label">原材料比例:</label>
                <input  id="rawMaterialPropertion${idSuffix}" name="rawMaterialrawMaterialPropertion" class="coralui-textbox" />
            </div>
            <div class="app-inputdiv6">
                <label class="app-input-label" style="width:20%;">加工工艺:</label>
                <div id="processTechnique${idSuffix}" class="coralui-radiolist" name="processTechnique"
                         data-options="value:'1',column:6,data:[{value:'1',text:'浸润'},{value:'2',text:'研磨'},{value:'3',text:'烘干'},{value:'4',text:'萃取'},
                         {value:'5',text:'速冻'},{value:'6',text:'搅拌'}]"></div>
            </div>
            </div>
        </form>
            <div class="app-bsearch-tbar">
                    <%--<div style="margin-top: 10px;height:60px">--%>
                <ces:toolbar onClick="$.ns('namespaceId${idSuffix}').toolbarClick1"
                             data="['->',{'label': '查询', 'id':'search','cls':'app-search-btn save', 'type': 'button'},{'label': '重置', 'id':'reset','cls':'app-reset-btn cancel', 'type': 'button'},'->']">
                </ces:toolbar>
                    <%--</div>--%>
            </div>

        <ces:layoutRegion region="center" split="true">
            <ces:toolbar onClick="$.ns('namespaceId${idSuffix}').toolbarClick2"
                         data="['->',{'label': '修改', 'id':'judge','cls':'', 'type': 'button'}]">
            </ces:toolbar>
            <div class='homeSpan' style="margin-top: -23px;"><div><div style='margin-left:20px;width: 250px;' id="nva${idSuffix}"> - 数据维护 </div></div></div>
            <div id="nsxgzGridDiv${idSuffix}" style="height:632px">
                <ces:grid rowNum="10" multiselect='true' id="nsxgzGrid${idSuffix}" height="auto"
                          url="${basePath}/zznsxgz!nsxgzSearch.json" rownumbers="true" fitStyle="fill">
                    <ces:gridCols>
                        <ces:gridCol name="qydm" width="10" hidden="true">企业代码</ces:gridCol>
                        <ces:gridCol name="scfabh" width="10" hidden="true">生产方案编号</ces:gridCol>
                        <ces:gridCol name="FLMC" width="120" formatter="combobox">辅料品名</ces:gridCol>
                        <ces:gridCol name="FLCD" width="80" formatter="combobox">辅料产地</ces:gridCol>
                        <ces:gridCol name="FLSYBL" width="80" formatter="combobox">辅料使用比例</ces:gridCol>
                        <ces:gridCol name="FLJYB" width="80" formatter="combobox">辅料净药比</ces:gridCol>
                        <ces:gridCol name="CZ" formatter="toolbar"
                                     formatoptions="onClick:$.ns('namespaceId${idSuffix}').toolbarClick1,
						             data:[{'label': '添加', 'id':'addBtn', 'disabled': 'false','type': 'button'},{'label': '删除', 'id':'delBtn', 'disabled': 'false','type': 'button'}]" width="120">操作</ces:gridCol>
                    </ces:gridCols>
                    <ces:gridPager gridId="nsxgzGrid${idSuffix}"></ces:gridPager>
                </ces:grid>
            </div>
        </ces:layoutRegion>

</div>
<script type="text/javascript">
   var _colModel =[
       {name:'qydm',hidden:true,width:10},
       {name:'scfabh',hidden:true,width:10},
       {name:'FLMC',edittype:'combobox',width:100},
       {name:'FLCD',edittype:'combobox',width:100},
       {name:'FLSYBL',edittype:'combobox',width:100},
       {name:'FLJYB',edittype:'combobox',width:100}];
   var _colName=["企业代码","生产方案编号","辅料品名","辅料使用比例","辅料净药比"];
   var _setting = {
       width : 'auto',
       height : 'auto' ,
       shrinkToFit : "true",
       forceFit : "true" ,
       fitStyle : "fill" ,
       datatype : "local"  ,
       colModel : _colModel,
       colNames : _colNames
   };
   //第一个列表工具条事件
   toolbarClick1 : function(event, button) {
       var $grid = $("#pGridId${idSuffix}");
       var $cgrid = $("#gridId${idSuffix}");
       //进行操作的行
       var rowId = event.data.rowId;
       pGridRowId = rowId;
       var gData = $grid.grid("getRowData",rowId);
       //添加事件
       if (button.id == "addBtn") {
           if(isUpdate){
               $("#trplyForm${idSuffix}").form().submit();
               $cgrid.grid("clearGridData");
           }
           isUpdate = true;
       }
       //删除事件
       if (button.id == "delBtn") {
           var gDataId = gData.ID;
           //判断是否在数据库中存在
           if(gDataId == "" || isEmpty(gDataId)){//如果是删除编辑状态的数据并且是没有进行保存的数据则执行grid的remove
               $grid.grid( 'clearEdited', rowId );
               $grid.grid("delRowData",rowId);
               CFG_message("删除成功！", "success");
           }else{//走数据库删除
               $.ajax({
                   url : $.contextPath + "/zztrplylb!deleteScdalyxx.json?id="+gDataId,
                   type : "post",
                   dataType : "json",
                   success : function ( res ){
                       CFG_message("操作成功!","success");
                       $grid.grid("delRowData",rowId);
                   },error : function ( res ){
                       CFG_message("操作失败!","error");
                   }
               })
           }
       }
   }
</script>

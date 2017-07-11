<%@ page import="com.ces.config.utils.CommonUtil"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="ces" tagdir="/WEB-INF/tags"%>
<%
  String path = request.getContextPath();
  String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
  request.setAttribute("gurl","");
  request.setAttribute("turl","");
  request.setAttribute("idSuffix", CommonUtil.generateUIId(""));
%>
<style type="text/css">
  .app-input-label {
    float: left;
  }
</style>
<div id="max${idSuffix}" class="fill">
  <div class="fill">
    <div class="toolbarsnav clearfix">
      <ces:toolbar id="toolbarId${idSuffix}" onClick="$.ns('namespaceId${idSuffix}').toolbarClick"
                   data="[{'label': '保存', 'id':'save', 'disabled': 'false','type': 'button'}]">
      </ces:toolbar>
    </div>
    <form id="csForm${idSuffix}" action="zzcsgl!saveCsxx.json"
          enctype="multipart/form-data" method="post" class="coralui-form">
      <div class="app-inputdiv4" style ="height:32px;display: none">
        <%--<input id="ID${idSuffix}" class="coralui-textbox" name="ID"/>--%>
      </div>

      <div class="fillwidth colspan3 clearfix">
        <!------------------ 第一排开始---------------->
        <div class="app-inputdiv4">
          <label class="app-input-label" >出场流水号：</label>
          <input id="CSLSH${idSuffix}" name="CSLSH" readonly="readonly" class="coralui-textbox"/>
        </div>
        <div class="app-inputdiv4">
          <label class="app-input-label" >客户名称：</label>
          <input id="QYBH${idSuffix}" name="QYBH"  data-options="required:true"/>

        </div>
        <div class="app-inputdiv4">
          <label class="app-input-label" >销售订单号：</label>
          <input id="DKMC${idSuffix}" name="DKMC"  data-options="required:true"/>

        </div>
        <!------------------ 第一排结束---------------->

        <!------------------ 第二排开始---------------->
        <div class="app-inputdiv4">
          <label class="app-input-label" >总重量：</label>
          <input id="ZZDYMC${idSuffix}" name="ZZDYMC" data-options="required:true" />

        </div>
        <div class="app-inputdiv4">
          <label class="app-input-label" >配送方式：</label>
          <input id="SCDABH${idSuffix}" name="SCDABH" data-options="required:true" />
        </div>
        <div class="app-inputdiv4">
          <label class="app-input-label" >车牌号：</label>
          <input id="PL${idSuffix}" name="PL" readonly="readonly" class="coralui-textbox" data-options="required:true"/>
        </div>
        <!------------------ 第二排结束---------------->

        <!------------------ 第三排开始---------------->
        <div class="app-inputdiv4">
          <label class="app-input-label" >配送责任人：</label>
          <input id="PZ${idSuffix}" name="PZ" readonly="readonly" data-options="required:true" class="coralui-textbox"/>
        </div>

        <div class="app-inputdiv4">
          <label class="app-input-label" >出场时间：</label>
          <input id="ZLHJ${idSuffix}" name="ZLHJ"  class="coralui-textbox"/>
        </div>
        <div  class="app-inputdiv4">
          <label class="app-input-label" >备注：</label>
          <ces:datepicker id="CSSJ${idSuffix}" name="CSSJ" dateFormat="yyyy-MM-dd HH:mm:ss" required="true"></ces:datepicker>
          <%--<input id="CSSJ${idSuffix}" name="CSSJ" class="coralui-textbox"/>--%>
          <%--<input id="TP${idSuffix}" name="TP" class="coralui-textbox" type="hidden"/>--%>
        </div>
        <div  class="app-inputdiv4" style="display:none">
          <input id="ID${idSuffix}" name="ID"  class="coralui-textbox" />
          <input id="QYBM${idSuffix}" name="QYBM"  class="coralui-textbox" />
          <input id="KCZL${idSuffix}" name="KCZL"  class="coralui-textbox" />
          <input id="YCCZL${idSuffix}" name="YCCZL"  class="coralui-textbox" />
          <input id="ZZDYBH${idSuffix}"  name="ZZDYBH"  class="coralui-textbox"/>
          <input id="DKBH${idSuffix}" name="DKBH"  class="coralui-textbox"/>
          <input id="QVMC${idSuffix}" name="QVMC"   class="coralui-textbox"/>
          <input id="PZBH${idSuffix}" name="PZBH"   class="coralui-textbox"/>
          <input id="PLBH${idSuffix}" name="PLBH"   class="coralui-textbox"/>

        </div>


      </div>
    </form>

    <div class="toolbarsnav clearfix">

    </div>
    <ces:grid id="gridId${idSuffix}" afterInlineSaveRow="afterInlineSaveRow"
              shrinkToFit="true" forceFit="true" fitStyle="fill" rownumbers="true" datatype="local" editableRows="true"  >
      <ces:gridCols>
        <ces:gridCol name="ID" editable="false" hidden="true"  width="100">ID</ces:gridCol>
        <ces:gridCol name="PID" editable="false"  hidden="true"  width="80">PID</ces:gridCol>
        <%--<ces:gridCol name="XH" editable="false" edittype="text" editoptions="required: true" width="180">序号</ces:gridCol>--%>
        <ces:gridCol name="ZLDJ" edittype="combobox" editable="false" revertCode="true"  width="180">质量等级</ces:gridCol>
        <ces:gridCol name="PCH"  editable="false" edittype="text" editoptions="required: false" width="180">批次号</ces:gridCol>
        <ces:gridCol name="ZL" editable="true" edittype="text" editoptions="required: false" width="180">重量(KG)</ces:gridCol>
        <ces:gridCol name="DYZS" editable="true" edittype="text" editoptions="required: false" width="80">打印张数</ces:gridCol>
        <ces:gridCol name="CZ" editable="false" formatter="toolbar"
                     formatoptions="onClick:$.ns('namespaceId${idSuffix}').toolbarClick3,
						             data:[{'label': '编辑', 'id':'bjBtn', 'disabled': 'false','type': 'button'}]" width="220">操作</ces:gridCol>
      </ces:gridCols>
    </ces:grid>
    <div id="jqUC" style="display: none"></div>
  </div>
</div>

<%--
  Created by IntelliJ IDEA.
  User: WILL
  Date: 15/8/18
  Time: 下午4:56
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.ces.config.utils.CommonUtil" %>
<%@ taglib prefix="ces" tagdir="/WEB-INF/tags" %>
<%
    String id=request.getParameter("ID");
    String type=request.getParameter("LX");
    if("".equals(id)){
        request.setAttribute("id","");
        request.setAttribute("lx","");
    }else{
        request.setAttribute("id",id);
        request.setAttribute("lx",type);
    }
//    request.setAttribute("id",id);
//    request.setAttribute("type",type);
    System.out.println(id);
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://"
            + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
    request.setAttribute("idSuffix", CommonUtil.generateUIId(""));
%>
<div id="maxDiv${idSuffix}" class="fill" style="height:780px;">
    <ces:layout id="layoutId${idSuffix}" name="" style="width:800px;height:700px;" fit="true">
        <ces:layoutRegion region="north" split="true" style="height:325px;" title="生产方案">
            <ces:toolbar onClick="$.ns('namespaceId${idSuffix}').toolbarClick"
                         data="['->',{'label': '关闭', 'id':'return', 'cls':'return_tb','disabled': 'false','type': 'button'}]">
            </ces:toolbar>
            <div style="margin-top: -25px;position: absolute;"><div class='homeSpan' style="height:23px"><div style='margin-left:25px;width: 150px;font-size:16px' id="nva${idSuffix}"> - 种植任务 - 详情 </div></div></div>
            <div id="enterFormDiv${idSuffix}" style="margin-left: 20px;height: 265px; ">
                <form id="enterForm${idSuffix}" enctype="multipart/form-data" method="post" class="coralui-form">
                    <div class="fillwidth colspan3 clearfix">
                        <div class="app-inputdiv4" style="display: none">
                            <label class="app-input-label" style="width:36%;">种植批次号:</label>
                            <div class="app-inputdiv6" style="height:32px;display:none">
                                <input id="ID${idSuffix}" class="coralui-textbox" name="ID"/>
                                <input id="zzpch${idSuffix}" class="coralui-textbox" name="zzpch"
                                       data-options="valueField:'ZZPCH',textField:'ZZPCH',required:true">
                            </div>
                        </div>
                        <div class="app-inputdiv4">
                            <label class="app-input-label" style="width:36%;">种植方案:</label>
                            <input id="zzfa${idSuffix}" class="coralui-combobox" name="zzfa"
                                   data-options="valueField:'ZZFABH',textField:'ZZFAMC',required:true">
                        </div>
                        <div class="app-inputdiv4">
                            <label class="app-input-label" style="width:36%;">药材名称:</label>
                            <input id="ycmc${idSuffix}" class="coralui-textbox" name="ycmc"
                                   data-options="readonly:true">
                        </div>
                        <input id="ycdm${idSuffix}" name="ycdm" type="hidden"  class="coralui-textbox">
                        <div class="app-inputdiv4">
                            <label class="app-input-label" style="width:36%;">种苗(种子):</label>
                            <input id="zmzzmc${idSuffix}" class="coralui-textbox" name="zmzzmc"
                                   data-options="readonly:true">
                        </div>
                        <div class="app-inputdiv4">
                            <label class="app-input-label" style="width:36%;">种子来源:</label>
                            <input id="zzly${idSuffix}" class="coralui-combobox" name="zzly" data-options="readonly:true">
                        </div>
                        <div class="app-inputdiv4">
                            <label class="app-input-label" style="width:36%;">种子重量:</label>
                            <ces:input id="zzzl${idSuffix}"  name="zzzl" readonly="true" validType="naturalnumber"></ces:input>

                        </div>
                        <input id="zmzzbh${idSuffix}" name="zmzzbh" type="hidden"  class="coralui-textbox">
                        <div class="app-inputdiv4">
                            <label class="app-input-label" style="width:36%;">种植基地:</label>
                            <input id="jdmc${idSuffix}" class="coralui-combobox" name="jdmc"
                                   data-options="readonly:true,required:true,valueField:'JDBH',textField:'JDMC'">
                        </div>
                        <div class="app-inputdiv4">
                            <label class="app-input-label" style="width:36%;">种植地块:</label>
                            <input id="dkmc${idSuffix}" class="coralui-combobox" name="dkbh"
                                   data-options="readonly:true,required:true,valueField:'DKBH',textField:'DKMC'">
                        </div>
                        <div class="app-inputdiv4">
                            <label class="app-input-label" style="width:36%;">种植负责人:</label>
                            <input id="zzfzr${idSuffix}" class="coralui-combobox" name="zzfzr"
                                   data-options="readonly:true,required:true,valueField:'GZRYBH',textField:'XM'">
                        </div>

                        <div class="app-inputdiv4">
                            <label class="app-input-label" style="width:36%;">种植面积:</label>
                            <input id="dkmj${idSuffix}" class="coralui-textbox" name="dkmj"
                                   data-options="readonly:true">
                        </div>

                        <div class="app-inputdiv4">
                            <label class="app-input-label" style="width:36%;">种植时间:</label>
                            <input id="zzsj${idSuffix}" class="coralui-datepicker" name="zzsj"
                                   data-options="readonly:true,required:true">
                        </div>

                    </div>
                </form>
            </div>
        </ces:layoutRegion>
        <ces:layoutRegion region="center" split="true" style="height:295px;" cls="small-grid">
            <%--<div><input type="button" onclick="test133()" value="收起"/></div>--%>
            <div id="tabs${idSuffix}" class="coralui-tabs" data-options="heightStyle:'fill'" style="display: none;">
                <ul>
                    <li><a href="#scbz${idSuffix}">播种方案</a></li>
                    <li><a href="#scgg${idSuffix}">灌溉方案</a></li>
                    <li><a href="#scsf${idSuffix}">施肥方案</a></li>
                    <li><a href="#scyy${idSuffix}">用药方案</a></li>
                    <%--<li><a href="#scjcjl${idSuffix}">检测方案</a></li>--%>
                    <li><a href="#sccs${idSuffix}">采收</a></li>
                    <li><a href="#sccc${idSuffix}">锄草</a></li>
                    <li><a href="#scqt${idSuffix}">其它</a></li>
                    <%--<li><a href="#xsqx${idSuffix}">销售去向</a></li>--%>
                </ul>
                <!-- 播种 -->
                <div id="scbz${idSuffix}" name="tabBorderDiv">
                    <ces:toolbar id="scbzToolbarId${idSuffix}" onClick="$.ns('namespaceId${idSuffix}').toolbarClick"
                                 data="[{'type': 'html','content':'<span>起始农事项时间:</span>'},{'type': 'textbox','id': 'qsnsxsjlabel','readonly': 'true'}]">
                    </ces:toolbar>
                    <div id="scbzGridDiv${idSuffix}" style="height: 200px;">
                        <ces:grid id="scbzGrid${idSuffix}" height="auto" rownumbers="true" fitStyle="fill"
                                  cellEdit="true">
                            <ces:gridCols>
                                <ces:gridCol name="ID" hidden="true"></ces:gridCol>
                                <ces:gridCol name="QSNSX" hidden="true"></ces:gridCol>
                                <ces:gridCol name="NSZYXBH" formatter="text"
                                             formatoptions="readonly:true,required: true"
                                             width="80">农事作业项编号</ces:gridCol>
                                <ces:gridCol name="NSZYXMC" formatter="text"
                                             formatoptions="readonly:true,required: true"
                                             width="80">农事作业项名称</ces:gridCol>
                                <ces:gridCol name="ZPFS" formatter="combobox"
                                             formatoptions="readonly:true,required: true" revertCode="true"
                                             width="80">栽培方式</ces:gridCol>
                                <ces:gridCol name="NSXJGSJ" formatter="text"
                                             formatoptions="readonly:true,required: true,validType: 'integer'"
                                             width="80">农事项间隔时间</ces:gridCol>
                                <ces:gridCol name="CZFDSJ" formatter="text"
                                             formatoptions="readonly:true,required: true,validType: 'naturalnumber'"
                                             sorttype="naturalnumber" width="80">操作浮动时间</ces:gridCol>
                                <ces:gridCol name="OP" fixed="true" width="100" formatter="toolbar"
                                             formatoptions="isOverflow:false,onClick:$.ns('namespaceId${idSuffix}').gridClick,data:[{'label': '操作详情', 'id':'lookInfo', 'disabled': 'false','type': 'button'}]">操作</ces:gridCol>
                            </ces:gridCols>
                        </ces:grid>
                    </div>
                    <ces:toolbar onClick="$.ns('namespaceId${idSuffix}').toolbarClick"
                                 data="[]">
                    </ces:toolbar>
                    <div id="scbzTrpGridDiv${idSuffix}" style="height: 200px;">
                        <ces:grid id="scbzTrpGrid${idSuffix}" height="auto" rownumbers="true" fitStyle="fill">
                            <ces:gridCols>
                                <ces:gridCol name="ID" hidden="true"></ces:gridCol>
                                <ces:gridCol name="NSZYXBH" formatter="combobox"
                                             formatoptions="readonly:true,required: true" revertCode="true"
                                             width="80">农事作业项编号</ces:gridCol>
                                <ces:gridCol name="TRPLX" formatter="combobox"
                                             formatoptions="readonly:true,required: true" width="80">投入品类型</ces:gridCol>
                                <ces:gridCol name="TRPTYM" formatter="combobox"
                                             formatoptions="readonly:true,required: true"
                                             width="80">投入品通用名</ces:gridCol>
                                <ces:gridCol name="TRPMC" formatter="combobox"
                                             formatoptions="readonly:true,required: true" width="80">投入品名称</ces:gridCol>
                                <ces:gridCol name="YT" width="80">用途</ces:gridCol>
                                <%--<ces:gridCol name="GG" width="80">规格</ces:gridCol>--%>
                                <ces:gridCol name="YL" formatter="text" formatoptions="readonly:true,required: true"
                                             width="80">用量（千克/每亩）</ces:gridCol>
                                <%--<ces:gridCol name="OP" fixed="true" width="100" formatter="toolbar"--%>
                                <%--formatoptions="onClick:$.ns('namespaceId${idSuffix}').gridClick,data:[{'label': '删除', 'id':'delDetailGridData', 'disabled': 'false','type': 'button'}]">操作</ces:gridCol>--%>
                            </ces:gridCols>
                        </ces:grid>
                    </div>
                </div>
                <!-- 灌溉 -->
                <div id="scgg${idSuffix}" name="tabBorderDiv">
                    <ces:toolbar onClick="$.ns('namespaceId${idSuffix}').toolbarClick"
                                 data="[{'type': 'html','content':'<span>起始农事项时间:</span>'},{'type': 'textbox','id': 'qsnsxsjlabel','readonly': 'true'}]">
                    </ces:toolbar>
                    <div id="scggGridDiv${idSuffix}" style="height: 200px;">
                        <ces:grid id="scggGrid${idSuffix}" fitStyle="fill" height="auto" rownumbers="true">
                            <ces:gridCols>
                                <ces:gridCol name="ID" hidden="true"></ces:gridCol>
                                <ces:gridCol name="QSNSX" hidden="true"></ces:gridCol>
                                <ces:gridCol name="NSZYXBH" formatter="text"
                                             formatoptions="readonly:true,required: true"
                                             width="80">农事作业项编号</ces:gridCol>
                                <ces:gridCol name="NSZYXMC" formatter="text"
                                             formatoptions="readonly:true,required: true"
                                             width="80">农事作业项名称</ces:gridCol>
                                <ces:gridCol name="GGFS" revertCode="true" sortable="true" width="100"
                                             formatter="combobox"
                                             formatoptions="readonly:true,required: true">灌溉方式</ces:gridCol>
                                <ces:gridCol name="SYLX" revertCode="true" width="80" formatter="combobox"
                                             formatoptions="readonly:true,required: true">水源类型</ces:gridCol>
                                <ces:gridCol name="SYDJ" revertCode="true" width="80" formatter="combobox"
                                             formatoptions="readonly:true,required: true">水源等级</ces:gridCol>
                                <ces:gridCol name="NSXJGSJ" formatter="text"
                                             formatoptions="readonly:true,required: true,validType: 'integer'"
                                             width="80">农事项间隔时间</ces:gridCol>
                                <ces:gridCol name="CZFDSJ" formatter="text"
                                             formatoptions="readonly:true,required: true,validType: 'naturalnumber'"
                                             sorttype="naturalnumber" width="80">操作浮动时间</ces:gridCol>
                                <ces:gridCol name="OP" fixed="true" width="100" formatter="toolbar"
                                             formatoptions="isOverflow:false,onClick:$.ns('namespaceId${idSuffix}').gridClick,data:[{'label': '操作详情', 'id':'lookInfo', 'disabled': 'false','type': 'button'},{'label': '删除', 'id':'delGridData', 'disabled': 'false','type': 'button'}]">操作</ces:gridCol>
                            </ces:gridCols>
                        </ces:grid>
                    </div>
                    <ces:toolbar onClick="$.ns('namespaceId${idSuffix}').toolbarClick"
                                 data="[]">
                    </ces:toolbar>
                    <div id="scggTrpGridDiv${idSuffix}" style="height: 200px;">
                        <ces:grid id="scggTrpGrid${idSuffix}" height="auto" rownumbers="true" fitStyle="fill">
                            <ces:gridCols>
                                <ces:gridCol name="ID" hidden="true"></ces:gridCol>
                                <ces:gridCol name="NSZYXBH" formatter="combobox"
                                             formatoptions="readonly:true,required: true" revertCode="true"
                                             width="80">农事作业项编号</ces:gridCol>
                                <ces:gridCol name="TRPLX" formatter="combobox"
                                             formatoptions="readonly:true,required: true" width="80">投入品类型</ces:gridCol>
                                <ces:gridCol name="TRPTYM" formatter="combobox"
                                             formatoptions="readonly:true,required: true"
                                             width="80">投入品通用名</ces:gridCol>
                                <ces:gridCol name="TRPMC" formatter="combobox"
                                             formatoptions="readonly:true,required: true" width="80">投入品名称</ces:gridCol>
                                <ces:gridCol name="YT" width="80">用途</ces:gridCol>
                                <%--<ces:gridCol name="GG" width="80">规格</ces:gridCol>--%>
                                <ces:gridCol name="YL" formatter="text" formatoptions="readonly:true,required: true"
                                             width="80">用量（千克/每亩）</ces:gridCol>
                                <%--<ces:gridCol name="OP" fixed="true" width="100" formatter="toolbar"--%>
                                <%--formatoptions="onClick:$.ns('namespaceId${idSuffix}').gridClick,data:[{'label': '删除', 'id':'delDetailGridData', 'disabled': 'false','type': 'button'}]">操作</ces:gridCol>--%>
                            </ces:gridCols>
                        </ces:grid>
                    </div>
                </div>
                <!-- 施肥 -->
                <div id="scsf${idSuffix}" name="tabBorderDiv">
                    <ces:toolbar onClick="$.ns('namespaceId${idSuffix}').toolbarClick"
                                 data="[{'type': 'html','content':'<span>起始农事项时间:</span>'},{'type': 'textbox','id': 'qsnsxsjlabel','readonly': 'true'}]">
                    </ces:toolbar>
                    <div id="scsfGridDiv${idSuffix}" style="height: 200px;">
                        <ces:grid id="scsfGrid${idSuffix}" fitStyle="fill" height="auto" rownumbers="true">
                            <ces:gridCols>
                                <ces:gridCol name="ID" hidden="true"></ces:gridCol>
                                <ces:gridCol name="QSNSX" hidden="true"></ces:gridCol>
                                <ces:gridCol name="NSZYXBH" formatter="text"
                                             formatoptions="readonly:true,required: true"
                                             width="80">农事作业项编号</ces:gridCol>
                                <ces:gridCol name="NSZYXMC" formatter="text"
                                             formatoptions="readonly:true,required: true"
                                             width="80">农事作业项名称</ces:gridCol>
                                <ces:gridCol name="SFSD" revertCode="true" sortable="true" width="100"
                                             formatter="combobox"
                                             formatoptions="readonly:true,required: true">施肥时段</ces:gridCol>
                                <ces:gridCol name="NSXJGSJ" formatter="text"
                                             formatoptions="readonly:true,required: true,validType: 'integer'"
                                             width="80">农事项间隔时间</ces:gridCol>
                                <ces:gridCol name="CZFDSJ" formatter="text"
                                             formatoptions="readonly:true,required: true,validType: 'naturalnumber'"
                                             sorttype="naturalnumber" width="80">操作浮动时间</ces:gridCol>
                                <ces:gridCol name="OP" fixed="true" width="100" formatter="toolbar"
                                             formatoptions="isOverflow:false,onClick:$.ns('namespaceId${idSuffix}').gridClick,data:[{'label': '操作详情', 'id':'lookInfo', 'disabled': 'false','type': 'button'}]">操作</ces:gridCol>
                            </ces:gridCols>
                        </ces:grid>
                    </div>
                    <ces:toolbar onClick="$.ns('namespaceId${idSuffix}').toolbarClick"
                                 data="[]">
                    </ces:toolbar>
                    <div id="scsfTrpGridDiv${idSuffix}" style="height: 200px;">
                        <ces:grid id="scsfTrpGrid${idSuffix}" height="auto" rownumbers="true" fitStyle="fill">
                            <ces:gridCols>
                                <ces:gridCol name="ID" hidden="true"></ces:gridCol>
                                <ces:gridCol name="NSZYXBH" formatter="combobox"
                                             formatoptions="readonly:true,required: true" revertCode="true"
                                             width="80">农事作业项编号</ces:gridCol>
                                <ces:gridCol name="TRPLX" formatter="combobox"
                                             formatoptions="readonly:true,required: true" width="80">投入品类型</ces:gridCol>
                                <ces:gridCol name="TRPTYM" formatter="combobox"
                                             formatoptions="readonly:true,required: true"
                                             width="80">投入品通用名</ces:gridCol>
                                <ces:gridCol name="TRPMC" formatter="combobox"
                                             formatoptions="readonly:true,required: true" width="80">投入品名称</ces:gridCol>
                                <ces:gridCol name="YT" width="80">用途</ces:gridCol>
                                <%--<ces:gridCol name="GG" width="80">规格</ces:gridCol>--%>
                                <ces:gridCol name="YL" formatter="text" formatoptions="readonly:true,required: true"
                                             width="80">用量（千克/每亩）</ces:gridCol>
                                <%--<ces:gridCol name="OP" fixed="true" width="100" formatter="toolbar"--%>
                                <%--formatoptions="onClick:$.ns('namespaceId${idSuffix}').gridClick,data:[{'label': '删除', 'id':'delDetailGridData', 'disabled': 'false','type': 'button'}]">操作</ces:gridCol>--%>
                            </ces:gridCols>
                        </ces:grid>
                    </div>
                </div>
                <!-- 用药 -->
                <div id="scyy${idSuffix}" name="tabBorderDiv">
                    <ces:toolbar onClick="$.ns('namespaceId${idSuffix}').toolbarClick"
                                 data="[{'type': 'html','content':'<span>起始农事项时间:</span>'},{'type': 'textbox','id': 'qsnsxsjlabel','readonly': 'true'}]">
                    </ces:toolbar>
                    <div id="scyyGridDiv${idSuffix}" style="height: 200px;">
                        <ces:grid id="scyyGrid${idSuffix}" fitStyle="fill" height="auto" rownumbers="true">
                            <ces:gridCols>
                                <ces:gridCol name="ID" hidden="true"></ces:gridCol>
                                <ces:gridCol name="QSNSX" hidden="true"></ces:gridCol>
                                <ces:gridCol name="NSZYXBH" formatter="text"
                                             formatoptions="readonly:true,required: true"
                                             width="80">农事作业项编号</ces:gridCol>
                                <ces:gridCol name="NSZYXMC" formatter="text"
                                             formatoptions="readonly:true,required: true"
                                             width="80">农事作业项名称</ces:gridCol>
                                <ces:gridCol name="YYSD" formatter="combobox"
                                             formatoptions="readonly:true,required: true" revertCode="true"
                                             width="80">用药时段</ces:gridCol>
                                <ces:gridCol name="NSXJGSJ" formatter="text"
                                             formatoptions="readonly:true,required: true,validType: 'integer'"
                                             width="80">农事项间隔时间</ces:gridCol>
                                <ces:gridCol name="CZFDSJ" formatter="text"
                                             formatoptions="readonly:true,required: true,validType: 'naturalnumber'"
                                             sorttype="naturalnumber" width="80">操作浮动时间</ces:gridCol>
                                <ces:gridCol name="OP" fixed="true" width="100" formatter="toolbar"
                                             formatoptions="isOverflow:false,onClick:$.ns('namespaceId${idSuffix}').gridClick,data:[{'label': '操作详情', 'id':'lookInfo', 'disabled': 'false','type': 'button'}]">操作</ces:gridCol>
                            </ces:gridCols>
                        </ces:grid>
                    </div>
                    <ces:toolbar onClick="$.ns('namespaceId${idSuffix}').toolbarClick"
                                 data="[]">
                    </ces:toolbar>
                    <div id="scyyTrpGridDiv${idSuffix}" style="height: 200px;">
                        <ces:grid id="scyyTrpGrid${idSuffix}" height="auto" rownumbers="true" fitStyle="fill">
                            <ces:gridCols>
                                <ces:gridCol name="ID" hidden="true"></ces:gridCol>
                                <ces:gridCol name="NSZYXBH" formatter="combobox"
                                             formatoptions="readonly:true,required: true" revertCode="true"
                                             width="80">农事作业项编号</ces:gridCol>
                                <ces:gridCol name="TRPLX" formatter="combobox"
                                             formatoptions="readonly:true,required: true" width="80">投入品类型</ces:gridCol>
                                <ces:gridCol name="TRPTYM" formatter="combobox"
                                             formatoptions="readonly:true,required: true"
                                             width="80">投入品通用名</ces:gridCol>
                                <ces:gridCol name="TRPMC" formatter="combobox"
                                             formatoptions="readonly:true,required: true" width="80">投入品名称</ces:gridCol>
                                <ces:gridCol name="YT" width="80">用途</ces:gridCol>
                                <%--<ces:gridCol name="GG" width="80">规格</ces:gridCol>--%>
                                <ces:gridCol name="YL" formatter="text" formatoptions="readonly:true,required: true"
                                             width="80">用量（千克/每亩）</ces:gridCol>
                                <%--<ces:gridCol name="OP" fixed="true" width="100" formatter="toolbar"--%>
                                <%--formatoptions="onClick:$.ns('namespaceId${idSuffix}').gridClick,data:[{'label': '删除', 'id':'delDetailGridData', 'disabled': 'false','type': 'button'}]">操作</ces:gridCol>--%>
                            </ces:gridCols>
                        </ces:grid>
                    </div>
                </div>
                <!-- 检测记录 -->
                <%--<div id="scjcjl${idSuffix}" name="tabBorderDiv">--%>
                    <%--<div id="scjcjlGridDiv${idSuffix}" style="height: 400px;">--%>
                        <%--<ces:grid id="scjcjlGrid${idSuffix}" fitStyle="fill" height="auto" rownumbers="true">--%>
                            <%--<ces:gridCols>--%>
                                <%--<ces:gridCol name="ID" hidden="true"></ces:gridCol>--%>
                                <%--<ces:gridCol name="JCR" formatter="text" hidden="true"></ces:gridCol>--%>
                                <%--<ces:gridCol name="CSLSH" formatter="text" formatoptions="required: true,readonly: true"--%>
                                             <%--width="80">采收流水号</ces:gridCol>--%>
                                <%--<ces:gridCol name="JCXM" formatter="text" formatoptions="required: true,readonly: true"--%>
                                             <%--width="80">检测项目</ces:gridCol>--%>
                                <%--<ces:gridCol name="JCFF" formatter="combobox" formatoptions="required: true,readonly: true"--%>
                                             <%--revertCode="true" width="100">检测方法</ces:gridCol>--%>
                                <%--<ces:gridCol name="CYFF" width="80" formatter="text"--%>
                                             <%--formatoptions="required: true,readonly: true">采样方法</ces:gridCol>--%>
                                <%--<ces:gridCol name="JCDW" width="80" formatter="text"--%>
                                             <%--formatoptions="required: true,readonly: true">检测单位</ces:gridCol>--%>
                                <%--&lt;%&ndash;<ces:gridCol name="JCDD" width="80" formatter="text"&ndash;%&gt;--%>
                                             <%--&lt;%&ndash;formatoptions="required: true,readonly: true">检测地点</ces:gridCol>&ndash;%&gt;--%>
                                <%--<ces:gridCol name="JCSJ" formatter="datepicker"--%>
                                             <%--formatoptions="required: true,readonly: true"--%>
                                             <%--width="80">检测时间</ces:gridCol>--%>
                                <%--<ces:gridCol name="JCRBH" formatter="combobox" formatoptions="required: true,readonly: true"--%>
                                             <%--revertCode="true" width="100">检测人</ces:gridCol>--%>
                                <%--<ces:gridCol name="JCJG" width="80" formatter="text"--%>
                                             <%--formatoptions="required: true,readonly: true">检测结果</ces:gridCol>--%>
                                <%--<ces:gridCol name="OP" fixed="true" width="190" formatter="toolbar"--%>
                                             <%--formatoptions="isOverflow:false,onClick:$.ns('namespaceId${idSuffix}').showImgClick,data:[{'label': '查看图片', 'id':'showImg', 'disabled': 'false','type': 'button'}]">操作</ces:gridCol>--%>
                            <%--</ces:gridCols>--%>
                        <%--</ces:grid>--%>
                    <%--</div>--%>
                <%--</div>--%>
                <!-- 采收 -->
                <div id="sccs${idSuffix}" name="tabBorderDiv">
                    <ces:toolbar onClick="$.ns('namespaceId${idSuffix}').toolbarClick"
                                 data="[{'type': 'html','content':'<span>起始农事项时间:</span>'},{'type': 'textbox','id': 'qsnsxsjlabel','readonly': 'true'}]">
                    </ces:toolbar>
                    <div id="sccsGridDiv${idSuffix}" style="height: 200px;">
                        <ces:grid id="sccsGrid${idSuffix}" fitStyle="fill" height="auto" rownumbers="true">
                            <ces:gridCols>
                                <ces:gridCol name="ID" hidden="true"></ces:gridCol>
                                <ces:gridCol name="QSNSX" hidden="true"></ces:gridCol>
                                <ces:gridCol name="NSZYXBH" formatter="text"
                                             formatoptions="readonly:true,required: true"
                                             width="80">农事作业项编号</ces:gridCol>
                                <ces:gridCol name="NSZYXMC" formatter="text"
                                             formatoptions="readonly:true,required: true"
                                             width="80">农事作业项名称</ces:gridCol>
                                <ces:gridCol name="CSFS" formatter="combobox"
                                             formatoptions="readonly:true,required: true" revertCode="true"
                                             width="100">采收方式</ces:gridCol>
                                <ces:gridCol name="NSXJGSJ" formatter="text"
                                             formatoptions="readonly:true,required: true,validType: 'integer'"
                                             width="80">农事项间隔时间</ces:gridCol>
                                <ces:gridCol name="CZFDSJ" formatter="text"
                                             formatoptions="readonly:true,required: true,validType: 'naturalnumber'"
                                             sorttype="naturalnumber" width="80">操作浮动时间</ces:gridCol>
                                <ces:gridCol name="OP" fixed="true" width="100" formatter="toolbar"
                                             formatoptions="isOverflow:false,onClick:$.ns('namespaceId${idSuffix}').gridClick,data:[{'label': '操作详情', 'id':'lookInfo', 'disabled': 'false','type': 'button'}]">操作</ces:gridCol>
                            </ces:gridCols>
                        </ces:grid>
                    </div>
                    <ces:toolbar onClick="$.ns('namespaceId${idSuffix}').toolbarClick"
                                 data="[]">
                    </ces:toolbar>
                    <div id="sccsTrpGridDiv${idSuffix}" style="height: 200px;">
                        <ces:grid id="sccsTrpGrid${idSuffix}" height="auto" rownumbers="true" fitStyle="fill">
                            <ces:gridCols>
                                <ces:gridCol name="ID" hidden="true"></ces:gridCol>
                                <ces:gridCol name="NSZYXBH" formatter="combobox"
                                             formatoptions="readonly:true,required: true" revertCode="true"
                                             width="80">农事作业项编号</ces:gridCol>
                                <ces:gridCol name="TRPLX" formatter="combobox"
                                             formatoptions="readonly:true,required: true" width="80">投入品类型</ces:gridCol>
                                <ces:gridCol name="TRPTYM" formatter="combobox"
                                             formatoptions="readonly:true,required: true"
                                             width="80">投入品通用名</ces:gridCol>
                                <ces:gridCol name="TRPMC" formatter="combobox"
                                             formatoptions="readonly:true,required: true" width="80">投入品名称</ces:gridCol>
                                <ces:gridCol name="YT" width="80">用途</ces:gridCol>
                                <%--<ces:gridCol name="GG" width="80">规格</ces:gridCol>--%>
                                <ces:gridCol name="YL" formatter="text" formatoptions="readonly:true,required: true"
                                             width="80">用量（千克/每亩）</ces:gridCol>
                                <%--<ces:gridCol name="OP" fixed="true" width="100" formatter="toolbar"--%>
                                <%--formatoptions="onClick:$.ns('namespaceId${idSuffix}').gridClick,data:[{'label': '删除', 'id':'delDetailGridData', 'disabled': 'false','type': 'button'}]">操作</ces:gridCol>--%>
                            </ces:gridCols>
                        </ces:grid>
                    </div>
                </div>
                <!-- 锄草 -->
                <div id="sccc${idSuffix}" name="tabBorderDiv">
                    <ces:toolbar onClick="$.ns('namespaceId${idSuffix}').toolbarClick"
                                 data="[{'type': 'html','content':'<span>起始农事项时间:</span>'},{'type': 'textbox','id': 'qsnsxsjlabel','readonly': 'true'}]">
                    </ces:toolbar>
                    <div id="scccGridDiv${idSuffix}" style="height: 200px;">
                        <ces:grid id="scccGrid${idSuffix}" fitStyle="fill" height="auto" rownumbers="true">
                            <ces:gridCols>
                                <ces:gridCol name="ID" hidden="true"></ces:gridCol>
                                <ces:gridCol name="QSNSX" hidden="true"></ces:gridCol>
                                <ces:gridCol name="NSZYXBH" formatter="text"
                                             formatoptions="readonly:true,required: true"
                                             width="80">农事作业项编号</ces:gridCol>
                                <ces:gridCol name="NSZYXMC" formatter="text"
                                             formatoptions="readonly:true,required: true"
                                             width="80">农事作业项名称</ces:gridCol>
                                <ces:gridCol name="CCFS" formatter="combobox"
                                             formatoptions="readonly:true,required: true" revertCode="true"
                                             width="100">锄草方式</ces:gridCol>
                                <ces:gridCol name="NSXJGSJ" formatter="text"
                                             formatoptions="readonly:true,required: true,validType: 'integer'"
                                             width="80">农事项间隔时间</ces:gridCol>
                                <ces:gridCol name="CZFDSJ" formatter="text"
                                             formatoptions="readonly:true,required: true,validType: 'naturalnumber'"
                                             sorttype="naturalnumber" width="80">操作浮动时间</ces:gridCol>
                                <ces:gridCol name="OP" fixed="true" width="100" formatter="toolbar"
                                             formatoptions="isOverflow:false,onClick:$.ns('namespaceId${idSuffix}').gridClick,data:[{'label': '操作详情', 'id':'lookInfo', 'disabled': 'false','type': 'button'}]">操作</ces:gridCol>
                            </ces:gridCols>
                        </ces:grid>
                    </div>
                    <ces:toolbar onClick="$.ns('namespaceId${idSuffix}').toolbarClick"
                                 data="[]">
                    </ces:toolbar>
                    <div id="scccTrpGridDiv${idSuffix}" style="height: 200px;">
                        <ces:grid id="scccTrpGrid${idSuffix}" height="auto" rownumbers="true" fitStyle="fill">
                            <ces:gridCols>
                                <ces:gridCol name="ID" hidden="true"></ces:gridCol>
                                <ces:gridCol name="NSZYXBH" formatter="combobox"
                                             formatoptions="readonly:true,required: true" revertCode="true"
                                             width="80">农事作业项编号</ces:gridCol>
                                <ces:gridCol name="TRPLX" formatter="combobox"
                                             formatoptions="readonly:true,required: true" width="80">投入品类型</ces:gridCol>
                                <ces:gridCol name="TRPTYM" formatter="combobox"
                                             formatoptions="readonly:true,required: true"
                                             width="80">投入品通用名</ces:gridCol>
                                <ces:gridCol name="TRPMC" formatter="combobox"
                                             formatoptions="readonly:true,required: true" width="80">投入品名称</ces:gridCol>
                                <ces:gridCol name="YT" width="80">用途</ces:gridCol>
                                <%--<ces:gridCol name="GG" width="80">规格</ces:gridCol>--%>
                                <ces:gridCol name="YL" formatter="text" formatoptions="readonly:true,required: true"
                                             width="80">用量（千克/每亩）</ces:gridCol>
                                <%--<ces:gridCol name="OP" fixed="true" width="100" formatter="toolbar"--%>
                                <%--formatoptions="onClick:$.ns('namespaceId${idSuffix}').gridClick,data:[{'label': '删除', 'id':'delDetailGridData', 'disabled': 'false','type': 'button'}]">操作</ces:gridCol>--%>
                            </ces:gridCols>
                        </ces:grid>
                    </div>
                </div>
                <!-- 其它 -->
                <div id="scqt${idSuffix}" name="tabBorderDiv">
                    <ces:toolbar onClick="$.ns('namespaceId${idSuffix}').toolbarClick"
                                 data="[{'type': 'html','content':'<span>起始农事项时间:</span>'},{'type': 'textbox','id': 'qsnsxsjlabel','readonly': 'true'}]">
                    </ces:toolbar>
                    <div id="scqtGridDiv${idSuffix}" style="height: 200px;">
                        <ces:grid id="scqtGrid${idSuffix}" fitStyle="fill" height="auto" rownumbers="true">
                            <ces:gridCols>
                                <ces:gridCol name="ID" hidden="true"></ces:gridCol>
                                <ces:gridCol name="QSNSX" hidden="true"></ces:gridCol>
                                <ces:gridCol name="NSZYXBH" formatter="text"
                                             formatoptions="readonly:true,required: true"
                                             width="80">农事作业项编号</ces:gridCol>
                                <ces:gridCol name="NSZYXMC" formatter="text"
                                             formatoptions="readonly:true,required: true"
                                             width="80">农事作业项名称</ces:gridCol>
                                <ces:gridCol name="NSXJGSJ" formatter="text"
                                             formatoptions="readonly:true,required: true,validType: 'integer'"
                                             width="80">农事项间隔时间</ces:gridCol>
                                <ces:gridCol name="CZFDSJ" formatter="text"
                                             formatoptions="readonly:true,required: true,validType: 'naturalnumber'"
                                             sorttype="naturalnumber" width="80">操作浮动时间</ces:gridCol>
                                <ces:gridCol name="OP" fixed="true" width="100" formatter="toolbar"
                                             formatoptions="isOverflow:false,onClick:$.ns('namespaceId${idSuffix}').gridClick,data:[{'label': '操作详情', 'id':'lookInfo', 'disabled': 'false','type': 'button'}]">操作</ces:gridCol>
                            </ces:gridCols>
                        </ces:grid>
                    </div>
                    <ces:toolbar onClick="$.ns('namespaceId${idSuffix}').toolbarClick"
                                 data="[]">
                    </ces:toolbar>
                    <div id="scqtTrpGridDiv${idSuffix}" style="height: 200px;">
                        <ces:grid id="scqtTrpGrid${idSuffix}" height="auto" rownumbers="true" fitStyle="fill">
                            <ces:gridCols>
                                <ces:gridCol name="ID" hidden="true"></ces:gridCol>
                                <ces:gridCol name="NSZYXBH" formatter="combobox"
                                             formatoptions="readonly:true,required: true" revertCode="true"
                                             width="80">农事作业项编号</ces:gridCol>
                                <ces:gridCol name="TRPLX" formatter="combobox"
                                             formatoptions="readonly:true,required: true" width="80">投入品类型</ces:gridCol>
                                <ces:gridCol name="TRPTYM" formatter="combobox"
                                             formatoptions="readonly:true,required: true"
                                             width="80">投入品通用名</ces:gridCol>
                                <ces:gridCol name="TRPMC" formatter="combobox"
                                             formatoptions="readonly:true,required: true" width="80">投入品名称</ces:gridCol>
                                <ces:gridCol name="YT" width="80">用途</ces:gridCol>
                                <%--<ces:gridCol name="GG" width="80">规格</ces:gridCol>--%>
                                <ces:gridCol name="YL" formatter="text" formatoptions="readonly:true,required: true"
                                             width="80">用量（千克/每亩）</ces:gridCol>
                                <%--<ces:gridCol name="OP" fixed="true" width="100" formatter="toolbar"--%>
                                <%--formatoptions="onClick:$.ns('namespaceId${idSuffix}').gridClick,data:[{'label': '删除', 'id':'delDetailGridData', 'disabled': 'false','type': 'button'}]">操作</ces:gridCol>--%>
                            </ces:gridCols>
                        </ces:grid>
                    </div>
                </div>
                <!-- 销售去向 -->
                <%--<div id="xsqx${idSuffix}" name="tabBorderDiv">--%>
                    <%--<div id="xsqxGridDiv${idSuffix}" style="height: 400px;">--%>
                        <%--<ces:grid id="xsqxGrid${idSuffix}" fitStyle="fill" height="auto" rownumbers="true">--%>
                            <%--<ces:gridCols>--%>
                                <%--<ces:gridCol name="ID" hidden="true"></ces:gridCol>--%>
                                <%--<ces:gridCol name="CSLSH" formatter="text" formatoptions="required: true,readonly: true"--%>
                                             <%--width="80">采收流水号</ces:gridCol>--%>
                                <%--<ces:gridCol name="KHMC" formatter="text" formatoptions="required: true,readonly: true"--%>
                                             <%--revertCode="true" width="100">客户名称</ces:gridCol>--%>
                                <%--<ces:gridCol name="MDMC" width="80" formatter="text"--%>
                                             <%--formatoptions="required: true,readonly: true">门店名称</ces:gridCol>--%>
                                <%--<ces:gridCol name="XSDDH" width="80" formatter="text"--%>
                                             <%--formatoptions="required: true,readonly: true">销售订单号</ces:gridCol>--%>
                                <%--<ces:gridCol name="CCZL" formatter="text"--%>
                                             <%--formatoptions="required: true,readonly: true"--%>
                                             <%--width="80">出场重量</ces:gridCol>--%>
                                <%--<ces:gridCol name="CCSJ" formatter="text" formatoptions="required: true,readonly: true"--%>
                                             <%--revertCode="true" width="100">出场时间</ces:gridCol>--%>
                            <%--</ces:gridCols>--%>
                        <%--</ces:grid>--%>
                    <%--</div>--%>
                <%--</div>--%>
            </div>
        </ces:layoutRegion>
    </ces:layout>
</div>
<script>
    var tzid="${id}";
    var qqlx="${lx}";

//    var id = CFG_getInputParamValue(configInfo, 'paramIn1');
    <%--if(tzid!=""){$.ns('namespaceId${idSuffix}').initform(tzid);--%>

    <%--}--%>

    var _uploadId = "";

    /**************************************修改时加载数据 end**************************************/
    function test133() {
        $("#enterFormDiv${idSuffix}").animate({height: 'toggle', opacity: 'toggle'}, "slow");
    }
    var gridOptions = [];
    $.extend($.ns("namespaceId${idSuffix}"), {
        fapzs: null,
        qyxxs: null,
        dkxxs: null,
        zzdyxxs: null,
        initform: function (id) {//初始化表单信息

            //种植时间
            <%--$("#zzsj${idSuffix}").datepicker("setDate", new Date());--%>
            //区域信息
            <%--$("#ssqybh${idSuffix}").combobox("option", "onChange", function (e, ui) {--%>
            <%--var qybh = ui.value;--%>
            <%--for (var i in $.ns("namespaceId${idSuffix}").qyxxs) {--%>
            <%--var qyxx = $.ns("namespaceId${idSuffix}").qyxxs[i];--%>
            <%--if (qyxx.QYBH == qybh) {--%>
            <%--$("#qygly${idSuffix}").textbox("setValue", qyxx.FZR);--%>
            <%--$("#qyglybh${idSuffix}").val(qyxx.FZRBH);--%>
            <%--break;--%>
            <%--}--%>
            <%--}--%>
            <%--var dks = $.loadJson($.contextPath + "/sczzscda!getDkxxByQybh.json?qybh=" + qybh);--%>
            <%--$.ns("namespaceId${idSuffix}").dkxxs = dks;--%>
            <%--$("#dk${idSuffix}").combobox("reload", dks);--%>
            <%--//清空地块信息和种植单元信息--%>
            <%--$("#dk${idSuffix}").combobox("setValue", "");--%>
            <%--$("#zzdy${idSuffix}").combobox("setValue", "");--%>
            <%--$.ns("namespaceId${idSuffix}").zzdyxxs = null;--%>
            <%--});--%>
            //地块信息
            $("#dk${idSuffix}").combobox("option", "onChange", function (e, ui) {
                var dkbh = ui.value;
                var zzdys = $.loadJson($.contextPath + "/sczzscda!getZzdyBydk.json?dkbh=" + dkbh);
                $.ns("namespaceId${idSuffix}").zzdyxxs = zzdys;
                $("#zzdy${idSuffix}").combobox("reload", zzdys);
                for (var i in $.ns("namespaceId${idSuffix}").dkxxs) {
                    var dkxx = $.ns("namespaceId${idSuffix}").dkxxs[i];
                    if (dkxx.DKBH == dkbh) {
                        $("#dkmj${idSuffix}").textbox("setValue", dkxx.MJ);
                        break;
                    }
                }
                //清空种植单元信息
                $("#zzdy${idSuffix}").combobox("setValue", "");
            });
            //种植单元信息
            $("#zzdy${idSuffix}").combobox("option", "onChange", function (e, ui) {
                for (var i in $.ns("namespaceId${idSuffix}").zzdyxxs) {
                    var zzdyxx = $.ns("namespaceId${idSuffix}").zzdyxxs[i];
                    if (zzdyxx.ZZDYBH == ui.value) {
                        $("#zzdymj${idSuffix}").textbox("setValue", zzdyxx.ZZDYMJ);
                        $("#zzdygly${idSuffix}").textbox("setValue", zzdyxx.GLY);
                        $("#zzdyglybh${idSuffix}").val(zzdyxx.GLYBH);
                        break;
                    }
                }
            });
            var qys = $.loadJson($.contextPath + "/sczzscda!getQybhList.json");
            $.ns("namespaceId${idSuffix}").qyxxs = qys;
            $("#ssqybh${idSuffix}").combobox("reload", qys);
            //播种方案栽培方式
            var zpfs = $.loadJson($.contextPath + "/trace!getDataDictionary.json?lxbm=ZZZPFS");
            $("#scbzGrid${idSuffix}").grid("setColProp", "ZPFS", {
                "formatoptions": {
                    readonly: true,
                    required: true,
                    "data": zpfs
                }
            });
            //灌溉方案灌溉方式
            var ggfs = $.loadJson($.contextPath + "/trace!getDataDictionary.json?lxbm=GGFS");
            $("#scggGrid${idSuffix}").grid("setColProp", "GGFS", {
                "formatoptions": {
                    readonly: true,
                    required: true,
                    "data": ggfs
                }
            });
            //灌溉方案水源类型
            var sylx = $.loadJson($.contextPath + "/trace!getDataDictionary.json?lxbm=SYLX");
            $("#scggGrid${idSuffix}").grid("setColProp", "SYLX", {
                "formatoptions": {
                    readonly: true,
                    required: true,
                    "data": sylx
                }
            });
            //灌溉方案水源等级
            var sydj = $.loadJson($.contextPath + "/trace!getDataDictionary.json?lxbm=SYDJ");
            $("#scggGrid${idSuffix}").grid("setColProp", "SYDJ", {
                "formatoptions": {
                    readonly: true,
                    required: true,
                    "data": sydj
                }
            });
            //施肥方案施肥时段
            var sfsd = $.loadJson($.contextPath + "/trace!getDataDictionary.json?lxbm=ZZSFSD");
            $("#scsfGrid${idSuffix}").grid("setColProp", "SFSD", {
                "formatoptions": {
                    readonly: true,
                    required: true,
                    "data": sfsd
                }
            });
            //用药时段
            var yysd = $.loadJson($.contextPath + "/trace!getDataDictionary.json?lxbm=ZZYYSD");
            $("#scyyGrid${idSuffix}").grid("setColProp", "YYSD", {
                "formatoptions": {
                    readonly: true,
                    required: true,
                    "data": yysd
                }
            });
            //检测检测方法
            var jcff = $.loadJson($.contextPath + "/trace!getDataDictionary.json?lxbm=ZZJCFF");
            $("#scjcjlGrid${idSuffix}").grid("setColProp", "JCFF", {"formatoptions": {readonly: true,required: true, "data": jcff}});
            //采收方式
            var csfs = $.loadJson($.contextPath + "/trace!getDataDictionary.json?lxbm=ZZSCCSFS");
            $("#sccsGrid${idSuffix}").grid("setColProp", "CSFS", {
                "formatoptions": {
                    readonly: true,
                    required: true,
                    "data": csfs
                }
            });
            //锄草方式
            var ccfs = $.loadJson($.contextPath + "/trace!getDataDictionary.json?lxbm=ZZSCCCFS");
            $("#scccGrid${idSuffix}").grid("setColProp", "CCFS", {
                "formatoptions": {
                    readonly: true,
                    required: true,
                    "data": ccfs
                }
            });
            //负责人
            var fzr = $.loadJson($.contextPath + "/sczzscda!getFzr.json");
            $("#scjcjlGrid${idSuffix}").grid("setColProp", "JCRBH", {"formatoptions": {readonly:true,required: true, "data": fzr, onChange: function(e,data){
                var $fzrTextbox = $("#" + e.data.gridId).grid("getCellComponent", e.data.rowId, "JCR");
                $fzrTextbox.textbox("setValue",data.text);
            }}});
            //投入品类型、通用名、名称
            var allTrplx = $.loadJson($.contextPath + "/trace!getDataDictionary.json?lxbm=ZZTRPLX");
            var allTrptym = $.loadJson($.contextPath + "/sczzscda!getAllTrptym.json");
            var allTrpmc = $.loadJson($.contextPath + "/sczzscda!getAllTrpxx.json");
            var $detailGrids = $("#scbzTrpGrid${idSuffix},#scggTrpGrid${idSuffix},#scsfTrpGrid${idSuffix},#scyyTrpGrid${idSuffix},#sccsTrpGrid${idSuffix},#scccTrpGrid${idSuffix},#scqtTrpGrid${idSuffix}");
            $detailGrids.grid("setColProp", "TRPLX", {
                "formatoptions": {
                    readonly: true, required: true, "data": allTrplx, onChange: function (e, data) {
                        var $trptymCombobox = $("#" + e.data.gridId).grid("getCellComponent", e.data.rowId, "TRPTYM");
                        var trptym = $.loadJson($.contextPath + "/sczzscda!getTrptymByLx.json?lx=" + data.value);
                        $trptymCombobox.combobox("reload", trptym);
                        $trptymCombobox.combobox("setValue", "");
                        $("#" + e.data.gridId).grid("getCellComponent", e.data.rowId, "TRPMC").combobox("setValue", "");
                    }
                }
            });
            $detailGrids.grid("setColProp", "TRPTYM", {
                "formatoptions": {
                    valueField: 'VALUE',
                    textField: 'TEXT',
                    readonly: true,
                    required: true,
              //      "data": allTrptym,
                    onChange: function (e, data) {
                        var $trpmcCombobox = $("#" + e.data.gridId).grid("getCellComponent", e.data.rowId, "TRPMC");
                        var rowData =$("#" + e.data.gridId).grid("getRowData", e.data.rowId);
                        var trpmc = $.loadJson($.contextPath + "/sczzscda!getTrpxxByTym.json?lx=" + rowData.TRPLX+"&tym="+data.newText);
                        $trpmcCombobox.combobox("reload", trpmc);
                        $trpmcCombobox.combobox("setValue", "");
                    }
                }
            });
            $detailGrids.grid("setColProp", "TRPMC", {
                "formatoptions": {
                    valueField: 'VALUE',
                    textField: 'TEXT',
                    readonly: true,
                    required: true,
             //       "data": allTrpmc,
                    onChange: function (e, data) {
                        for (var i in allTrpmc) {
                            if (allTrpmc[i].TRPBH == data.value) {
                                $("#" + e.data.gridId).grid("setRowData", e.data.rowId, {
                                    YT: allTrpmc[i].YT,
                                    GG: allTrpmc[i].BZGG
                                });
                                break;
                            }
                        }
                    }
                }
            });
            //种植方案信息
            this.fapzs = $.loadJson($.contextPath + "/sczzscda!getZzfapzxx.json");
            $("#zzfa${idSuffix}").combobox("reload", this.fapzs.fapzs);
            //方案变化的同时，初始化列表的信息
            <%--$("#zzfa${idSuffix}").combobox("option", "onChange", function (e, ui) {--%>
            <%--$("#tabs${idSuffix}").show();--%>
            <%--//清空所有列表的数据，刷新列表--%>
            <%--var $allGrids = $("#scbzGrid${idSuffix},#scbzTrpGrid${idSuffix},#scggGrid${idSuffix},#scggTrpGrid${idSuffix},#scsfGrid${idSuffix},#scsfTrpGrid${idSuffix},#scyyGrid${idSuffix},#scyyTrpGrid${idSuffix}");--%>
            <%--$allGrids.grid("clearGridData");--%>
            <%--$allGrids.grid("refresh");--%>
            <%--$.ns("namespaceId${idSuffix}").initData(ui.value);--%>
            <%--});--%>
            /*************************************修改时加载数据 begin*************************************/
            var updateData = $.loadJson($.contextPath + "/sczzscda!getScdaxxById.json?rowId=" + id);
            var gzryData = $.loadJson($.contextPath + "/sczzscda!getZzfzrxxByZzfzr.json?zzfzr="+updateData.scda.ZZFZR);//根据种植负责人查找工作人员编号
            /*****************加载生产档案数据 begin**************************/
            $("#scdaid${idSuffix}").textbox("setValue", id);
            $("#zzpch${idSuffix}").textbox("setValue", updateData.scda.ZZPCH);
            $("#dkmc${idSuffix}").combobox("setValue", updateData.scda.DKBH);
            $("#jdmc${idSuffix}").combobox("setValue", updateData.scda.JDBH);
            $("#zzfzr${idSuffix}").combobox("setValue", gzryData[0].GZRYBH);
            $("#zzfa${idSuffix}").combobox("setValue", updateData.scda.ZZFA);
            $("#ycmc${idSuffix}").textbox("setValue", updateData.scda.YCMC);
            $("#ycdm${idSuffix}").textbox("setValue", updateData.scda.YCDM);
            $("#zmzzmc${idSuffix}").textbox("setValue", updateData.scda.ZMZZMC);
            $("#zmzzbh${idSuffix}").textbox("setValue", updateData.scda.ZMZZBH);
            $("#zzly${idSuffix}").combobox("setValue", updateData.scda.ZZLY);
            $("#zzzl${idSuffix}").textbox("setValue", updateData.scda.ZZZL);
            $("#zzsj${idSuffix}").datepicker("setValue", updateData.scda.ZZSJ);
            $("#zzjssj${idSuffix}").datepicker("setValue", updateData.scda.ZZJSSJ);
            $("#trzb${idSuffix}").textbox("setValue", updateData.scda.TRZB);
            $("#dkmj${idSuffix}").textbox("setValue",updateData.scda.DKMJ)
            $("#zzsj${idSuffix}").datepicker("option", "readonly", "true");
            $("#zzjssj${idSuffix}").datepicker("option", "readonly", "true");


            $("#scdaid${idSuffix},#qygly${idSuffix},#dkmj${idSuffix},#zzdymj${idSuffix},#zzdygly${idSuffix},#pl${idSuffix},#pz${idSuffix},#trzb${idSuffix}").textbox({
                readonly: true
            });
            $("#ssqybh${idSuffix},#dk${idSuffix},#zzdy${idSuffix},#zzfa${idSuffix},#zt${idSuffix}").combobox({
                readonly: true
            });
            $("#zzsj${idSuffix}").datepicker("option", "readonly", "true");
            $("#zzjssj${idSuffix}").datepicker("option", "readonly", "true");

            $("#tabs${idSuffix}").show();
            //清空所有列表的数据，刷新列表
            var $allGrids = $("#scbzGrid${idSuffix},#scbzTrpGrid${idSuffix},#scggGrid${idSuffix},#scggTrpGrid${idSuffix},#scsfGrid${idSuffix},#scsfTrpGrid${idSuffix},#scyyGrid${idSuffix},#scyyTrpGrid${idSuffix},#sccsTrpGrid${idSuffix},#scccTrpGrid${idSuffix},#scqtTrpGrid${idSuffix}");
            $allGrids.grid("clearGridData");
            $allGrids.grid("refresh");
            $.ns("namespaceId${idSuffix}").initData($("#zzfa${idSuffix}").combobox("getValue"));

            /*****************加载生产播种 begin**************************/
            $.each(updateData.scbz, function (e, data) {
                $("#scbzGrid${idSuffix}").grid("addRowData", data.ID, data);
                if (data.QSNSX == 1) {
                    $("#scbzGrid${idSuffix}").grid("setRowData", data.ID, null, {background: '#68C561'});
                    var gridIds = ["scbzGrid${idSuffix}","scggGrid${idSuffix}","scsfGrid${idSuffix}","scyyGrid${idSuffix}","scccGrid${idSuffix}","sccsGrid${idSuffix}","scqtGrid${idSuffix}"];
                    $.each(gridIds, function(e, data){
                        $.ns('namespaceId${idSuffix}').getToolBarQsnsxsjComponent(data).textbox("setValue", updateData.qsnsxsj);
                    });
                }
                $.ns('namespaceId${idSuffix}').updateDGridBh("scbzGrid${idSuffix}");
            });
            $.each(updateData.scbztrp, function (e, data) {
                $("#scbzTrpGrid${idSuffix}").grid("addRowData", data.ID, data);
                var trptymCombobox = $("#scbzTrpGrid${idSuffix}").grid("getCellComponent",  data.ID, "TRPTYM");
                var trpmcCombobox = $("#scbzTrpGrid${idSuffix}").grid("getCellComponent",  data.ID, "TRPMC");
                var trptym = $.loadJson($.contextPath + "/sczzscda!getTrptymByLx.json?lx=" + data.TRPLX);
                trptymCombobox.combobox("reload", trptym);
                trptymCombobox.combobox("setValue", data.TRPTYM);
                trpmcCombobox.combobox("reload", trptym);
                trpmcCombobox.combobox("setValue", data.TRPTYM);
            });
            /*****************加载生产灌溉 begin**************************/
            $.each(updateData.scgg, function (e, data) {
                $("#scggGrid${idSuffix}").grid("addRowData", data.ID, data);
                if (data.QSNSX == 1) {
                    $("#scggGrid${idSuffix}").grid("setRowData", data.ID, null, {background: '#68C561'});
                    var gridIds = ["scbzGrid${idSuffix}","scggGrid${idSuffix}","scsfGrid${idSuffix}","scyyGrid${idSuffix}","scccGrid${idSuffix}","sccsGrid${idSuffix}","scqtGrid${idSuffix}"];
                    $.each(gridIds, function(e, data){
                        $.ns('namespaceId${idSuffix}').getToolBarQsnsxsjComponent(data).textbox("setValue", updateData.qsnsxsj);
                    });
                }
                $.ns('namespaceId${idSuffix}').updateDGridBh("scggGrid${idSuffix}");
            });
            $.each(updateData.scggtrp, function (e, data) {
                $("#scggTrpGrid${idSuffix}").grid("addRowData", data.ID, data);
                var trptymCombobox = $("#scggTrpGrid${idSuffix}").grid("getCellComponent",  data.ID, "TRPTYM");
                var trpmcCombobox = $("#scggTrpGrid${idSuffix}").grid("getCellComponent",  data.ID, "TRPMC");
                var trptym = $.loadJson($.contextPath + "/sczzscda!getTrptymByLx.json?lx=" + data.TRPLX);
                var trpmc = $.loadJson($.contextPath + "/sczzscda!getTrpxxByTym.json?lx=" + data.TRPLX+"&tym="+data.TRPTYM);
                trptymCombobox.combobox("reload", trptym);
                trptymCombobox.combobox("setValue", data.TRPTYM);
                trpmcCombobox.combobox("reload", trptym);
                trpmcCombobox.combobox("setValue", data.TRPTYM);
            });
            /*****************加载生产施肥 begin**************************/
            $.each(updateData.scsf, function (e, data) {
                $("#scsfGrid${idSuffix}").grid("addRowData", data.ID, data);
                if (data.QSNSX == 1) {
                    $("#scsfGrid${idSuffix}").grid("setRowData", data.ID, null, {background: '#68C561'});
                    var gridIds = ["scbzGrid${idSuffix}","scggGrid${idSuffix}","scsfGrid${idSuffix}","scyyGrid${idSuffix}","scccGrid${idSuffix}","sccsGrid${idSuffix}","scqtGrid${idSuffix}"];
                    $.each(gridIds, function(e, data){
                        $.ns('namespaceId${idSuffix}').getToolBarQsnsxsjComponent(data).textbox("setValue", updateData.qsnsxsj);
                    });
                }
                $.ns('namespaceId${idSuffix}').updateDGridBh("scsfGrid${idSuffix}");
            });
            $.each(updateData.scsftrp, function (e, data) {
                $("#scsfTrpGrid${idSuffix}").grid("addRowData", data.ID, data);
                var trptymCombobox = $("#scsfTrpGrid${idSuffix}").grid("getCellComponent",  data.ID, "TRPTYM");
                var trpmcCombobox = $("#scsfTrpGrid${idSuffix}").grid("getCellComponent",  data.ID, "TRPMC");
                var trptym = $.loadJson($.contextPath + "/sczzscda!getTrptymByLx.json?lx=" + data.TRPLX);
                var trpmc = $.loadJson($.contextPath + "/sczzscda!getTrpxxByTym.json?lx=" + data.TRPLX+"&tym="+data.TRPTYM);
                trptymCombobox.combobox("reload", trptym);
                trptymCombobox.combobox("setValue", data.TRPTYM);
                trpmcCombobox.combobox("reload", trpmc);
                trpmcCombobox.combobox("setValue", data.TRPMC);
            });
            /*****************加载生产用药 begin**************************/
            $.each(updateData.scyy, function (e, data) {
                $("#scyyGrid${idSuffix}").grid("addRowData", data.ID, data);
                if (data.QSNSX == 1) {
                    $("#scyyGrid${idSuffix}").grid("setRowData", data.ID, null, {background: '#68C561'});
                    var gridIds = ["scbzGrid${idSuffix}","scggGrid${idSuffix}","scsfGrid${idSuffix}","scyyGrid${idSuffix}","scccGrid${idSuffix}","sccsGrid${idSuffix}","scqtGrid${idSuffix}"];
                    $.each(gridIds, function(e, data){
                        $.ns('namespaceId${idSuffix}').getToolBarQsnsxsjComponent(data).textbox("setValue", updateData.qsnsxsj);
                    });
                }
                $.ns('namespaceId${idSuffix}').updateDGridBh("scyyGrid${idSuffix}");
            });
            $.each(updateData.scyytrp, function (e, data) {
                $("#scyyTrpGrid${idSuffix}").grid("addRowData", data.ID, data);
                var trptymCombobox = $("#scyyTrpGrid${idSuffix}").grid("getCellComponent",  data.ID, "TRPTYM");
                var trpmcCombobox = $("#scyyTrpGrid${idSuffix}").grid("getCellComponent",  data.ID, "TRPMC");
                var trptym = $.loadJson($.contextPath + "/sczzscda!getTrptymByLx.json?lx=" + data.TRPLX);
                var trpmc = $.loadJson($.contextPath + "/sczzscda!getTrpxxByTym.json?lx=" + data.TRPLX+"&tym="+data.TRPTYM);
                trptymCombobox.combobox("reload", trptym);
                trptymCombobox.combobox("setValue", data.TRPTYM);
                trpmcCombobox.combobox("reload", trpmc);
                trpmcCombobox.combobox("setValue", data.TRPMC);
            });
            /*****************加载生产采收 begin**************************/
            $.each(updateData.sccs, function (e, data) {
                $("#sccsGrid${idSuffix}").grid("addRowData", data.ID, data);
                if (data.QSNSX == 1) {
                    $("#sccsGrid${idSuffix}").grid("setRowData", data.ID, null, {background: '#68C561'});
                    var gridIds = ["scbzGrid${idSuffix}","scggGrid${idSuffix}","scsfGrid${idSuffix}","scyyGrid${idSuffix}","scccGrid${idSuffix}","sccsGrid${idSuffix}","scqtGrid${idSuffix}"];
                    $.each(gridIds, function(e, data){
                        $.ns('namespaceId${idSuffix}').getToolBarQsnsxsjComponent(data).textbox("setValue", updateData.qsnsxsj);
                    });
                }
                $.ns('namespaceId${idSuffix}').updateDGridBh("sccsGrid${idSuffix}");
            });
            $.each(updateData.sccstrp, function (e, data) {
                $("#sccsTrpGrid${idSuffix}").grid("addRowData", data.ID, data);
                var trptymCombobox = $("#sccsTrpGrid${idSuffix}").grid("getCellComponent",  data.ID, "TRPTYM");
                var trpmcCombobox = $("#sccsTrpGrid${idSuffix}").grid("getCellComponent",  data.ID, "TRPMC");
                var trptym = $.loadJson($.contextPath + "/sczzscda!getTrptymByLx.json?lx=" + data.TRPLX);
                var trpmc = $.loadJson($.contextPath + "/sczzscda!getTrpxxByTym.json?lx=" + data.TRPLX+"&tym="+data.TRPTYM);
                trptymCombobox.combobox("reload", trptym);
                trptymCombobox.combobox("setValue", data.TRPTYM);
                trpmcCombobox.combobox("reload", trptym);
                trpmcCombobox.combobox("setValue", data.TRPTYM);
            });
            /*****************加载生产锄草 begin**************************/
            $.each(updateData.sccc, function (e, data) {
                $("#scccGrid${idSuffix}").grid("addRowData", data.ID, data);
                if (data.QSNSX == 1) {
                    $("#scccGrid${idSuffix}").grid("setRowData", data.ID, null, {background: '#68C561'});
                    var gridIds = ["scbzGrid${idSuffix}","scggGrid${idSuffix}","scsfGrid${idSuffix}","scyyGrid${idSuffix}","scccGrid${idSuffix}","sccsGrid${idSuffix}","scqtGrid${idSuffix}"];
                    $.each(gridIds, function(e, data){
                        $.ns('namespaceId${idSuffix}').getToolBarQsnsxsjComponent(data).textbox("setValue", updateData.qsnsxsj);
                    });
                }
                $.ns('namespaceId${idSuffix}').updateDGridBh("scccGrid${idSuffix}");
            });
            $.each(updateData.sccctrp, function (e, data) {
                $("#scccTrpGrid${idSuffix}").grid("addRowData", data.ID, data);
                var trptymCombobox = $("#scccTrpGrid${idSuffix}").grid("getCellComponent",  data.ID, "TRPTYM");
                var trpmcCombobox = $("#scccTrpGrid${idSuffix}").grid("getCellComponent",  data.ID, "TRPMC");
                var trptym = $.loadJson($.contextPath + "/sczzscda!getTrptymByLx.json?lx=" + data.TRPLX);
                var trpmc = $.loadJson($.contextPath + "/sczzscda!getTrpxxByTym.json?lx=" + data.TRPLX+"&tym="+data.TRPTYM);
                trptymCombobox.combobox("reload", trptym);
                trptymCombobox.combobox("setValue", data.TRPTYM);
                trpmcCombobox.combobox("reload", trpmc);
                trpmcCombobox.combobox("setValue", data.TRPMC);
            });
            /*****************加载生产其它 begin**************************/
            $.each(updateData.scqt, function (e, data) {
                $("#scqtGrid${idSuffix}").grid("addRowData", data.ID, data);
                if (data.QSNSX == 1) {
                    $("#scqtGrid${idSuffix}").grid("setRowData", data.ID, null, {background: '#68C561'});
                    var gridIds = ["scbzGrid${idSuffix}","scggGrid${idSuffix}","scsfGrid${idSuffix}","scyyGrid${idSuffix}","scccGrid${idSuffix}","sccsGrid${idSuffix}","scqtGrid${idSuffix}"];
                    $.each(gridIds, function(e, data){
                        $.ns('namespaceId${idSuffix}').getToolBarQsnsxsjComponent(data).textbox("setValue", updateData.qsnsxsj);
                    });
                }
                $.ns('namespaceId${idSuffix}').updateDGridBh("scqtGrid${idSuffix}");
            });
            $.each(updateData.scqttrp, function (e, data) {
                $("#scqtTrpGrid${idSuffix}").grid("addRowData", data.ID, data);
                var trptymCombobox = $("#scqtTrpGrid${idSuffix}").grid("getCellComponent",  data.ID, "TRPTYM");
                var trpmcCombobox = $("#scqtTrpGrid${idSuffix}").grid("getCellComponent",  data.ID, "TRPMC");
                var trptym = $.loadJson($.contextPath + "/sczzscda!getTrptymByLx.json?lx=" + data.TRPLX);
                var trpmc = $.loadJson($.contextPath + "/sczzscda!getTrpxxByTym.json?lx=" + data.TRPLX+"&tym="+data.TRPTYM);
                trptymCombobox.combobox("reload", trptym);
                trptymCombobox.combobox("setValue", data.TRPTYM);
                trpmcCombobox.combobox("reload", trpmc);
                trpmcCombobox.combobox("setValue", data.TRPMC);
            });

            $.each(updateData.scjcjl, function (e, data) {
                $("#scjcjlGrid${idSuffix}").grid("addRowData", data.ID, data);
            });
            var xsqxList = $.loadJson($.contextPath + "/sczzscda!getXsqx.json?id="+id);
            $.each(xsqxList, function (e, data) {
                $("#xsqxGrid${idSuffix}").grid("addRowData", data.ID, data);
            });


            /**************************************修改时加载数据 end**************************************/
        },
        initData: function (fabh) {
            //初始化所有的列表信息
            //品类，品种
            for (var i in $.ns("namespaceId${idSuffix}").fapzs.fapzs) {
                var originFapz = $.ns("namespaceId${idSuffix}").fapzs.fapzs[i];
                if (originFapz.ZZFABH == fabh) {
                    $("#pl${idSuffix}").textbox("setValue", originFapz.PL);
                    $("#plbh${idSuffix}").val(originFapz.PLBH);
                    $("#pz${idSuffix}").textbox("setValue", originFapz.PZ);
                    $("#pzbh${idSuffix}").val(originFapz.PZBH);
                }
            }
            /*
             //播种
             var bzs = $.ns("namespaceId
            ${idSuffix}").fapzs.bz[fapz];
             for (var i in  bzs) {
             var bz = bzs[i];
             $("#scbzGrid
            ${idSuffix}").grid("addRowData", bz.ID, {
             scbzGridPl : bz.PL,
             scbzGridPz : bz.PZ,
             scbzGridPzbh : bz.PZBH,
             scbzGridZpfs : bz.ZPFS,
             scbzGridYl : bz.YL,
             fabh : fapz
             }, "last");
             }
             //灌溉
             var ggs = $.ns("namespaceId
            ${idSuffix}").fapzs.gg[fapz];
             for (var i in  ggs) {
             var gg = ggs[i];
             $("#scggGrid
            ${idSuffix}").grid("addRowData", gg.ID, {
             scggGridGgfs : gg.GGFS,
             scggGridSylx : gg.SYLX,
             scggGridSydj : gg.SYDJ,
             fabh : fapz
             }, "last");
             }
             //施肥
             var sfs = $.ns("namespaceId
            ${idSuffix}").fapzs.sf[fapz];
             for (var i in  sfs) {
             var sf = sfs[i];
             $("#scsfGrid
            ${idSuffix}").grid("addRowData", sf.ID, {
             scsfGridSfsd : sf.SFSD,
             scsfGridFlmc : sf.FLMC,
             scsfGridFllx : sf.FLLX,
             scsfGridYl : sf.YL,
             fabh : fapz
             }, "last");
             var $scsfGridFlmcCombobox = $("#scsfGrid
            ${idSuffix}").grid("getCellComponent", sf.ID, "scsfGridFlmc");
             var flmc = $.loadJson($.contextPath + "/sczzscda!getTrpflxx.json?fllx="+sf.FLLX);
             $scsfGridFlmcCombobox.combobox("reload", flmc);
             }
             //用药
             var yys = $.ns("namespaceId
            ${idSuffix}").fapzs.yy[fapz];
             for (var i in  yys) {
             var yy = yys[i];
             $("#scyyGrid
            ${idSuffix}").grid("addRowData", yy.ID, {
             scyyGridYymc : yy.YYMC,
             scyyGridFzdx : yy.GG,
             scyyGridGg : yy.FZDX,
             scyyGridYl : yy.YL,
             fabh : fapz
             }, "last");
             }
             //检测
             var jcs = $.ns("namespaceId
            ${idSuffix}").fapzs.jc[fapz];
             for (var i in  jcs) {
             var jc = jcs[i];
             $("#scjcGrid
            ${idSuffix}").grid("addRowData", jc.ID, {
             scjcGridJcff : jc.JCFF,
             scjcGridCyff : jc.CYFF,
             scjcGridJcdw : jc.JCDW,
             fabh : fapz
             }, "last");
             }
             */
        },
        validGrid: function (gridId, err1, err2) {//主表验证
            var $grid = $("#" + gridId);
            var gridIds = $grid.grid("getDataIDs");
            if ($.isEmptyObject(gridIds)) {
                CFG_message(err1, "warning");
                return false;
            }
            if (!$grid.grid("valid")) {
                CFG_message(err2, "warning");
                return false;
            }
            return true;
        },
        toolbarClick: function (e, ui) {
            if (ui.id == "return") {//关闭
                var configInfo = $("#maxDiv${idSuffix}").data('configInfo');
                CFG_clickCloseButton(configInfo);
            }
        },
        valid: function () {//保存前验证

        },
        updateDGridBh: function (gridId) {//更新子表编号
            var $grid = $("#" + gridId);
            var $dGrid = $.ns('namespaceId${idSuffix}').getDetailGrid(gridId);
            var gridIds = $grid.grid("getDataIDs");
            var dGridIds = $dGrid.grid("getDataIDs");
            var zxybhs = [];
            var zxybhsArray = [];
            for (var i in gridIds) {
                var gridData = $grid.grid("getRowData", gridIds[i]);
                zxybhs.push({
                    value: gridData.NSZYXBH,
                    text: gridData.NSZYXBH
                });
                zxybhsArray.push(gridData.NSZYXBH);
            }
            $dGrid.grid("setColProp", "NSZYXBH", {"formatoptions": {readonly: true, required: true, "data": zxybhs}});
            for (var i in dGridIds) {
                var dGridData = $dGrid.grid("getRowData", dGridIds[i]);
                if ($.inArray(dGridData.NSZYXBH, zxybhsArray) == -1) {
                    $dGrid.grid("delRowData", dGridIds[i]);
                } else {
                    $dGrid.grid("getCellComponent", dGridIds[i], "NSZYXBH").combobox("reload", zxybhs);
                }
            }
        },
        getDetailGrid: function (gridId) {//获取子表
            return $("#" + gridId).parents("div[name='tabBorderDiv']").find(".ctrl-init-grid:last");
        },
        getToolBarQsnsxsjComponent: function (gridId) {//获取关联的工具条
            return $("#" + gridId).parents("div[name='tabBorderDiv']").find(".ctrl-init-toolbar:first").toolbar("getSubCoral", "qsnsxsjlabel").$el;
        },
        getNextNszyxbh: function (gridId) {//获取新的农事项编号
            var $grid = $("#" + gridId);
            var gridIds = $grid.grid("getDataIDs");
            if ($.isEmptyObject(gridIds)) {
                return 1;
            }
            var gridData = $grid.grid("getRowData", gridIds[gridIds.length - 1]);
            return parseInt(gridData.NSZYXBH) + 1;
        },
        gridClick: function (e, ui) {
            var gridId = e.data.gridId.split("_")[0];
            var url = "";
            var id = $("#" + e.data.gridId).grid("getRowData",e.data.rowId).ID;
            if (gridId == "scbzGrid") {
                url = $.contextPath + "/sczzscda!getCkxq.json?tableName=t_zz_scbz&id=" + id;
            } else if (gridId == "scggGrid") {
                url = $.contextPath + "/sczzscda!getCkxq.json?tableName=t_zz_scgg&id=" + id;
            } else if (gridId == "scsfGrid") {
                url = $.contextPath + "/sczzscda!getCkxq.json?tableName=t_zz_scsf&id=" + id;
            } else if (gridId == "scyyGrid") {
                url = $.contextPath + "/sczzscda!getCkxq.json?tableName=t_zz_scyy&id=" + id;
            } else if (gridId == "scjcGrid") {
                url = $.contextPath + "/sczzscda!getCkxq.json?tableName=t_zz_scjc&id=" + id;
            } else if (gridId == "sccsGrid") {
                url = $.contextPath + "/sczzscda!getCkxq.json?tableName=t_zz_sccs&id=" + id;
            } else if (gridId == "scccGrid") {
                url = $.contextPath + "/sczzscda!getCkxq.json?tableName=t_zz_sccc&id=" + id;
            } else if (gridId == "scqtGrid") {
                url = $.contextPath + "/sczzscda!getCkxq.json?tableName=t_zz_scqt&id=" + id;
            }
            $("#czxq${idSuffix}").remove();
            $("body").append("<div id='czxq${idSuffix}'><div id='scxqGrid${idSuffix}' height='auto' rownumbers='true' fitStyle='fill'></div></div>");
            var $grid = $("#scxqGrid${idSuffix}"),
                    _colModel = [
                        {name: "CZR", sortable: true, width: 200,align : "center"},
                        {name: "KSSJ", sortable: true, sorttype: "date", formatter: "date", width: 200, fixed: true,align : "center"},
                        {name: "JSSJ", sortable: true, sorttype: "date", formatter: "date", width: 200, fixed: true,align : "center"}
                    ],
                    _colNames = ["操作人", "操作开始时间","操作结束时间"],
                    _setting = {
                        width: "550",
                        height: "auto",
                        colModel: _colModel,
                        colNames: _colNames,
                        url: url
                    };
            $grid.grid(_setting);

            $("#czxq${idSuffix}").dialog({
                modal: true,
                width: 550,
                height: "auto",
                title: "操作详情",
                minHeight:400,
                onOpen: function () {
                    $("#qsnsxdialogDate${idSuffix}").datepicker({
                        required: true
                    });
                    $("#dialogConfirm${idSuffix}").button({
                        label: "确定",
                        width: 80,
                        onClick: function () {
                            if (!$("#qsnsxdialogDate${idSuffix}").datepicker("valid")) {
                                CFG_message("请先填写时间", "warning");
                                return;
                            }
                            $.ns('namespaceId${idSuffix}').clearFirst();
                            $("#" + gridId).grid("setRowData", rowId, {
                                QSNSX: "1"
                            });
                            //TODO 高亮行
                            var $grids = $("#scbzGrid${idSuffix},#scggGrid${idSuffix},#scsfGrid${idSuffix},#scyyGrid${idSuffix},#sccsGrid${idSuffix},#scccGrid${idSuffix},#scqtGrid${idSuffix}");
                            $grids.grid("option", "rowattr", $.ns('namespaceId${idSuffix}').rowAttr);
                            //设置toolbar文本框
                            $.ns('namespaceId${idSuffix}').getToolBarQsnsxsjComponent(gridId).textbox("setValue", $("#qsnsxdialogDate${idSuffix}").datepicker("getValue"));
                            $("#czxq${idSuffix}").remove();
                        }
                    });
                    $("#dialogCancel${idSuffix}").button({
                        label: "取消",
                        width: 80,
                        onClick: function () {
                            $("#czxq${idSuffix}").remove();
                        }
                    });
                },
                onClose: function () {
                    $("#czxq${idSuffix}").remove();
                }
            });
            $("#czxq${idSuffix}").dialog("open");
        },
        rowAttr: function (o) {//表格行属性
            if (o.rowData.QSNSX == "1") {
                return {"style": "background:#F0EFB5"};
            }
        },
        showImgClick: function(e,data){
            _uploadId = e.data.rowId;
            var data = $('#maxDiv${idSuffix}').data('configInfo');

            CFG_clickButtonOrTreeNode(data, "cktp", "图片查看", 2, $.ns("namespaceId${idSuffix}"));
        },
        setTptableIdColumns: function(o){
            o={
                status:true,
                P_tableId : "402894815065addb015065b59f960013",
                P_columns : "EQ_C_ZBID≡"+_uploadId
            };
            return o;
        }
    });

    $(function () {
        var configInfo = CFG_initConfigInfo({
            /** 页面名称 */
            'page': 'sczzscdaxx.jsp',
            /** 页面中的最大元素 */
            'maxEleInPage': $('#maxDiv${idSuffix}'),
            /** 获取构件嵌入的区域 */
            'getEmbeddedZone': function () {
            },
            /** 初始化预留区 */
            'initReserveZones': function (configInfo) {
                <%--CFG_addToolbarButtons(configInfo, $('#toolbarId${idSuffix}'), 'toolBarReserve', 0);--%>
            },
            /** 获取返回按钮添加的位置 */
            'setReturnButton': function (configInfo) {
                <%--CFG_setReturnButton(configInfo, $('#toolbarId${idSuffix}'));--%>
            },
            /** 页面初始化的方法 */
            'bodyOnLoad': function (configInfo) {
                //种植地块
                var dkObj = $.loadJson($.contextPath + "/sczzscda!getDkxx.json");
                $('#dkmc${idSuffix}').combobox('reload',dkObj);
                //种植负责人
                var zzfzrObj = $.loadJson($.contextPath + "/sczzscda!getZzfzr.json");
                $('#zzfzr${idSuffix}').combobox('reload',zzfzrObj);
                //种植基地
                var jdObj = $.loadJson($.contextPath + "/sczzscda!getJdxx.json");
                $('#jdmc${idSuffix}').combobox('reload',jdObj);

                if(tzid !=""){$.ns('namespaceId${idSuffix}').initform(tzid);
                    //如果tzid!=""的话，说明页面是从工作台跳转过来的，那么移除对话框里面的关闭按钮。
                    if(qqlx!='GJZZ'){$('#return').css("visibility","hidden");}

                    configInfo.CFG_urlParams = {"menuId": "402881fc4f3ffb80014f4031d280013e", constructId:'402894815065addb0150668da1a90058'};

                    if(qqlx=='scbz'){$("#tabs${idSuffix}").tabs("option", "active", 0);}
                    else if(qqlx=='scgg'){$("#tabs${idSuffix}").tabs("option", "active", 1);}
                    else if(qqlx=='scsf'){$("#tabs${idSuffix}").tabs("option", "active", 2);}
                    else if(qqlx=='scyy'){$("#tabs${idSuffix}").tabs("option", "active", 3);}
                    else if(qqlx=='scjcjl'){$("#tabs${idSuffix}").tabs("option", "active", 4);}
                    else if(qqlx=='sccs'){$("#tabs${idSuffix}").tabs("option", "active", 5);}
                    else if(qqlx=='sccc'){$("#tabs${idSuffix}").tabs("option", "active", 6);}
                    else if(qqlx=='scqt'){$("#tabs${idSuffix}").tabs("option", "active", 7);}



                }else
//                if(tzid =="")
                {
                    var id = CFG_getInputParamValue(configInfo, 'paramIn1');
                    $.ns('namespaceId${idSuffix}').initform(id);}
//                else if(tzid!=''&&qqlx==''){
//
//                }
                if(tzid!=''){
                    $('#return').bind('click',function(){
                        $('#return').css("visibility","visible");
//                        menuClick('高级追踪','/cfg-resource/coral40/views/component/zlzzgjzz/zlzzgjzz.jsp');
                        //http://localhost:8092/config1.0/cfg-resource/coral40/views/component/zlzzgjzz/zlzzgjzz.jsp?constructId=402881f24fc9c50f014fcac405110004&bindingType=menu&menuId=408a962b50ef08420150f014b4ea0074&componentVersionId=402881f24fc9c50f014fcac404db0003&menuCode=ZLZZGJZZ&topComVersionId=402881f24fc9c50f014fcac404db0003&useNavigation=1&___t=1448417845795
    ///Users/wngyu/Documents/config1.0/WebRoot/cfg-resource/coral40/views/selfdefine/gjzzlb/js/JQ_override.jsp
                        ///Users/wngyu/Documents/config1.0/WebRoot/cfg-resource/coral40/views/component/zlzzgjzz/zlzzgjzz.jsp
                        menuClick('高级追踪','/cfg-resource/coral40/views/component/zlzzgjzz/zlzzgjzz.jsp?constructId=402881f24fc9c50f014fcac405110004&bindingType=menu&menuId=408a962b50ef08420150f014b4ea0074&componentVersionId=402881f24fc9c50f014fcac404db0003&menuCode=ZLZZGJZZ&topComVersionId=402881f24fc9c50f014fcac404db0003&useNavigation=1&___t=1448417845795');
                        var configInfo = $("#maxDiv${idSuffix}").data('configInfo');
                        CFG_clickCloseButton(configInfo);
                    });
                }

                $("#scbzToolbarId${idSuffix}").toolbar("refresh");
                $('#return').bind('click',function(){

                        var configInfo = $("#maxDiv${idSuffix}").data('configInfo');
                        CFG_clickCloseButton(configInfo);
                });


                //样式调整
                $("input[id=qsnsxsjlabel]").parent().css("border","0");
                $("input[id=qsnsxsjlabel]").parent().parent().unbind();
                $("input[id=qsnsxsjlabel]").parent().parent().css("margin-top","3px");
            }
        });
        if (configInfo) {

            configInfo.parentConfigInfo.jqChild = $("#maxDiv${idSuffix}");
            //alert("系统参数：\t" + "关联的系统参数=" + CFG_getSystemParamValue(configInfo, 'systemParam1')
            //		+ "\n构件自身参数：\t" + "selfParam1=" + CFG_getSelfParamValue(configInfo, 'selfParam1')
            //		+ "\n构件入参：\t" + "inputParamName_1=" + CFG_getInputParamValue(configInfo, 'inputParamName_1'));
        }
        // 设置输出参数
        // configInfo.CFG_outputParams.xxx = '';


    }

    );



</script>
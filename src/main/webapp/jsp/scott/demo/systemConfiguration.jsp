<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <%@include file="/jsp/resource.jsp" %>
    <script type="text/javascript" src="<%=basePath%>/js/jquery-easyui-1.3.2/jquery.mask.js"></script>
    <link href="../css/systemConfig.css" rel="stylesheet" type="text/css"/>
</head>
<body class="easyui-layout" style="overflow: hidden;">

<div region="center" border="false" style="overflow: hidden;">
    <div class="easyui-tabs" region="center" border="false" data-options="tools:'#tab-tools'" style="overflow: hidden;">
        <div title="站点管理" data-options="closable:false" style="padding:10px;">
            <table style="font: 11px;">
                <tr>
                    <input type="hidden" id="txtStationId" name="hiddenField"/>
                </tr>
                <tr>
                    <td style="height: 25px">
                        站点:
                    </td>
                    <td style="height: 25px">
                        <input id='txtStation' width='285px' style="width:285px;"/>
                    </td>
                </tr>
                <tr>
                    <td style="height: 25px">
                        单位:
                    </td>
                    <td style="height: 25px">
                        <input id='txtUnitName' style="width:285px;"/>
                    </td>
                </tr>
                <tr>
                    <td>
                        地址:
                    </td>
                    <td style="height: 25px">
                        <input id='txtUnitAddress' style="width:285px;"/>
                    </td>
                </tr>
                <tr>
                    <td valign="top">
                        &nbsp;&nbsp;
                        <!-- a id="btn_Station"  href="#" class="easyui-linkbutton">修改</a -->
                        <input type="button" id="btn_Station" value="修改"/>
                    </td>
                </tr>
            </table>
        </div>
        <div title="区域管理" data-options="closable:false" style="padding:10px;">
            <div id="div_space">
                <table id="tab_space">
                </table>
            </div>
            <div id="space-window" title="space添加修改" style="width:400px;height:300px;font-size: 16">
                <div data-options="region:'north',split:true" style="height:185px;padding:10px">
                    <form id="spaceForm" class="ui-form" method="post">
                        <table id="tab_space_Form">
                            <tr>
                                <td>区域ID</td>
                                <td><input id="txt_spaceID"/></td>
                                <td><label id="msg_spaceID"></label></td>
                            </tr>
                            <tr>
                                <td>区域名</td>
                                <td><input id="txt_spaceName"/></td>
                                <td><label id="msg_spaceName"></label>
                                </td>
                            </tr>
                            <tr>
                                <td>电压等级</td>
                                <td>
                                    <select id="txt_objectVoltage_A" class="easyui-combobox" style="width:100px;"
                                            panelHeight="50">
                                        <option value="AC">AC</option>
                                        <option value="DC">DC</option>
                                    </select>
                                    <select id="txt_objectVoltage_O" class="easyui-combobox" style="width:100px;"
                                            panelHeight="100">
                                        <option value="500KV">500kV</option>
                                        <option value="800KV">800kV</option>
                                    </select>
                                </td>
                                <td>
                                    <label id="msg_objectVoltage_A"> </label>
                                    <label id="msg_objectVoltage_O"> </label>
                                </td>
                            </tr>
                            <tr>
                                <td>区域标签</td>
                                <td><input id="txt_spaceTag"/></td>
                                <td><label id="msg_spaceTag"> </label></td>
                            </tr>
                            <tr>
                                <td></td>
                                <td><input type="button" id="bt_space" value="提交"/></td>
                                <td></td>
                            </tr>
                        </table>
                    </form>
                </div>
            </div>
            <div id="UploadMap-window" title="区域地图上传" style="width:400px;height:150px;font-size: 16">
                <div data-options="region:'north',split:true" style="padding:10px">
                    <form id="MapForm" action="" enctype="multipart/form-data" method="post">
                        <p>请选择需要上传的.xml文件和.swf文件</p>
                        <input id="File_xml" accept=".xml" type="file" style="width: 160px;" name="uploadFile"/>
                        <input id="File_swf" accept=".swf" type="file" style="width: 160px;" name="uploadFile"/>
                        <input id="uploadBtn" type="button" value="上传" class="btn" style="margin-top: 0px;"/>
                    </form>
                </div>
            </div>
        </div>
        <div title="IED接入配置" data-options="closable:false" style="padding:10px;">
            <div id="div_ldein">
                <table id="tab_ldein">
                </table>
            </div>
            <div id="editLED-window" title="配置修改" style="width:400px;height:300px;font-size: 16">
                <div data-options="region:'north',split:true" style="height:185px;padding:10px">
                    <form id="editForm3" class="ui-form" method="post">
                        <table id="tab_led">
                            <tr>
                                <td>IED节点名称</td>
                                <td><input id="txt_AR_Name1" disabled="true"/></td>
                                <td>
                                    <div id="txt_AR_Name2"></div>
                                </td>
                            </tr>
                            <tr>
                                <td>Ip地址</td>
                                <td><input id="txt_netAddr1"/></td>
                                <td></td>
                            </tr>
                            <tr>
                                <td colspan="3"><br/><br/><br/></td>
                            </tr>
                            <tr>
                                <td></td>
                                <td><input type="button" id="ied_sub" value="提交"/></td>
                                <td></td>
                            </tr>
                        </table>
                    </form>
                </div>
            </div>
            <div id="icdUpload-window" title="icd上传" style="width:400px;height:150px;font-size: 16">
                <div data-options="region:'north',split:true" style="padding:10px">
                    <form id="pdfForm" action="" enctype="multipart/form-data" method="post">
                        <input id="uploadFileBtn" type="button" value="上传" class="btn" style="margin-top: 0px;"/>
                        <input id="fileUrl" type="file" accept=".icd" style="width: 150px;" name="uploadFile"
                               multiple="multiple"/>
                    </form>
                </div>
            </div>
        </div>
        <div title="逻辑设备管理" data-options="closable:false" style="padding:10px;">
            <div id="list1">
                <table id="equipmentList"></table>
            </div>
            <div id="list2">
                <table id="device_data_list"></table>
            </div>
            <!-- 主设备弹出窗口：添加数据 -->
            <div id="addData-window" title="添加设备或修改" style="width:800px;height:500px;font-size: 16">
                <div data-options="region:'north',split:true" style="height:185px;padding:10px">
                    <form id="editForm" class="ui-form" method="post">
                        <table>
                            <tr>
                                <td><label>编码:</label>
                                    <input id="equipmentID" type="text" data-options="required:true">
                                </td>
                                <td><label>名称:</label>
                                    <input id="equipmentName" type="text" data-options="required:true">
                                </td>
                                <%--					    	 				<td><label>制造厂家:</label>--%>
                                <%--		               							<input id="manufactoryName" type="text"  data-options="required:true">--%>
                                <%--		               						</td>--%>
                                <td><label>IEC61850LDevice编码:</label>
                                    <select id="IEC61850LD" class="easyui-combobox" style="width:200px;"
                                            panelHeight="150">
                                    </select>
                                </td>
                            </tr>
                            <tr>
                                <%--					    	 				<td><label>类型:</label>--%>
                                <%--		               							<select id="deviceType" class="easyui-combobox" style="width:145px;" panelHeight="150">--%>
                                <%--                    							</select>--%>
                                <%--		               						</td>--%>

<%--                                <td><label>相别:</label>--%>
<%--                                    <select id="phase" class="easyui-combobox" style="width:145px;" panelHeight="150">--%>
<%--                                    </select>--%>
<%--                                </td>--%>
                                <td><label>区域位置:</label>
                                    <select id="spaceId" class="easyui-combobox" style="width:145px;" panelHeight="150">
                                    </select>
                                </td>
                                <td><label>备注:</label>
                                    <input id="Remark" type="text" data-options="required:true">
                                </td>
                            </tr>
                            <%--					    	 			<tr>--%>
                            <%--					    	 				--%>
                            <%--					    	 			</tr>--%>
                            <tr>
                                <td>
                                    <input id="equipment_submit" type="button" value="确定"/>
                                    <input id="equipment_close" type="button" value="关闭"/>
                                </td>
                            </tr>
                        </table>
                    </form>
                </div>
            </div>
<%--            <!-- Excel上传 -->--%>
<%--            <div id="logicalDevice-window" title="Excel上传" style="width:400px;height:150px;font-size: 16">--%>
<%--                <div data-options="region:'north',split:true" style="padding:10px">--%>
<%--                    导入设备类型:<select id="pubdevice_type" class="easyui-combobox"--%>
<%--                                   style="width: 200px;" panelHeight="100">--%>
<%--                    <!-- option value="0" >普通设备</option -->--%>
<%--                    <option value="1">红外测温</option>--%>
<%--                </select>--%>
<%--                    <input id='DownLoadXls' type='button' value='下载模板'/>--%>
<%--                    <form id="LD_ExcelForm" action="" enctype="multipart/form-data" method="post">--%>
<%--                        <p>请选择需要导入的Excel文件</p>--%>
<%--                        <input id="LD_fileUrl" accept=".xls" type="file" style="width: 160px;" name="uploadFile"/>--%>
<%--                        <input id="LD_uploadFileBtn" type="button" value="上传" class="btn" style="margin-top: 0px;"/>--%>
<%--                    </form>--%>
<%--                </div>--%>
<%--            </div>--%>
            <!-- 设备弹出窗口：添加数据 -->
            <div id="device-window" title="添加设备或修改" style="width:600px;height:400px;font-size: 16">
                <div data-options="region:'north',split:true" style="height:185px;padding:10px">
                    <form id="editForm2" class="ui-form" method="post">
                        <table>
                            <tr>
                                <td><label>编码:</label>
                                    <input id="txtDeviceID" type="text" data-options="required:true">
                                </td>
                                <td><label>装置名称:</label>
                                    <input id="txtDeviceName" type="text" data-options="required:true">
                                </td>
                            </tr>
                            <tr>
                                <td><label>IEC61850LD_LN:</label>
                                    <!--<input id="IEC61850LD_LN" type="text"  data-options="required:true">
                                    -->
                                    <select id="IEC61850LD_LN" class="easyui-combobox" style="width:240px;"
                                            panelHeight="150">
                                    </select>
                                </td>
                                <td><label>装置类型:</label>
                                    <select id="ddlDeviceType" class="easyui-combobox" style="width:100px;"
                                            panelHeight="150" disabled="disabled"></select>
                                </td>

                            </tr>
                            <tr>
                                <td><label>投运日期:</label>
                                    <input id="txtRunTime" type="text" data-options="required:true">
                                </td>
                                <%--					    	 				<td><label>所属区域:</label>--%>
                                <%--		               							<select id="tSpace" class="easyui-combobox" style="width:100px;" panelHeight="150">--%>
                                <%--                    							</select>--%>
                                <%--		               						</td>--%>
<%--                                <td><label>相别:</label>--%>
<%--                                    <input id="tPhase" type="text">--%>
<%--                                </td>--%>
                                <td><label>相别:</label>
                                    <select id="phase" class="easyui-combobox" style="width:145px;" panelHeight="150">
                                    </select>
                                </td>
                            </tr>
                            <tr>
                                <td><label>监测设备:</label>
                                    <input id="textMDev" type="text" readonly>
                                </td>
                                <td><label>出厂序号:</label>
                                    <input id="txtcode" type="text" data-options="required:true">
                                </td>

                            </tr>
                            <tr>
                                <td><label>制造厂家:</label>
                                    <input id="txtFactory" type="text" data-options="required:true">
                                </td>
                                <td><label>IP地址:</label>
                                    <input id="txtIPAddress" type="text" data-options="required:true">
                                </td>

                            </tr>
                            <%--					    	 			<tr>--%>
                            <%--					    	 				--%>

                            <%--					    	 			</tr>--%>
                            <tr>
                                <td><label>安装地址:</label>
                                    <input id="txtInstallAddress" type="text" data-options="required:true">
                                </td>
                                <td><label>运行设置:</label>
                                    <input id="cbStopSoundAlarm" type='checkbox'>关闭告警音
                                    <input id="cbStopUse" type='checkbox'>停用
                                </td>

                            </tr>
                            <tr>
                                <td><label>地图坐标:</label>
                                    <input id="tPosition" type="text">
                                </td>
                                <td colspan="1"><label>备注:</label>
                                    <textarea id="txtRemark"></textarea>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <input id="device_submit" type="button" value="确定"/>
                                    <input id="device_close" type="button" value="关闭"/>
                                </td>
                            </tr>
                        </table>
                    </form>
                </div>
            </div>
            <!-- 告警设置 -->
            <div id="deviceMonitor-window" title="告警设置" style="width:800px;height:500px;font-size: 16">
                <div data-options="region:'north',split:true" style="height:185px;padding:10px">
                    <div id="monitor">
                        <table id="monitor_data_list"></table>
                    </div>
                    <div id="select_div">
                        应用设备选择:<br/>
                        <input id="select_all" type='checkbox'>全选
                        <div id="select"></div>
                    </div>
                </div>
            </div>
        </div>
        <div data-options="closable:false"></div>
    <%--        <div title="远传映射配置" data-options="closable:false" style="padding:10px;">--%>
<%--            <div id="list3">--%>
<%--                <table id="i2List"></table>--%>
<%--            </div>--%>
<%--            <div id="editI2-window" title="I2添加修改" style="width:800px;height:500px;font-size: 16">--%>
<%--                <div data-options="region:'north',split:true" style="height:185px;padding:10px">--%>
<%--                    <form id="editForm4" class="ui-form" method="post">--%>
<%--                        <table id="tab_I1ToI2">--%>
<%--                            <tr>--%>
<%--                                <td>远传点号</td>--%>
<%--                                <td><input id="txt_I2_ID"/></td>--%>
<%--                                <td><label id="msg_I2_ID">*</label></td>--%>
<%--                            </tr>--%>
<%--                            <tr>--%>
<%--                                <td>设备</td>--%>
<%--                                <td><select id="sel_dname" class="easyui-combobox" style="width:100px;"--%>
<%--                                            panelHeight="150">--%>
<%--                                </select></td>--%>
<%--                                <td><label id="msg_dname"> </label></td>--%>
<%--                            </tr>--%>
<%--                            <tr>--%>
<%--                                <td>类型</td>--%>
<%--                                <td><select id="sel_type" class="easyui-combobox" style="width:100px;"--%>
<%--                                            panelHeight="150">--%>
<%--                                </select></td>--%>
<%--                                <td><label id="msg_type"> </label></td>--%>
<%--                            </tr>--%>
<%--                            <tr>--%>
<%--                                <td>名称</td>--%>
<%--                                <td><select id="sel_instName" class="easyui-combobox" style="width:100px;"--%>
<%--                                            panelHeight="150">--%>
<%--                                </select></td>--%>
<%--                                <td><label id="msg_instName"></label></td>--%>
<%--                            </tr>--%>
<%--                            <tr>--%>
<%--                                <td>备注</td>--%>
<%--                                <td><input id="txt_desc"/></td>--%>
<%--                                <td><label id="msg_desc"> </label></td>--%>
<%--                            </tr>--%>
<%--                            <tr>--%>
<%--                                <td></td>--%>
<%--                                <td><input type="button" id="bt_sub" value="提交"/></td>--%>
<%--                                <td></td>--%>
<%--                            </tr>--%>
<%--                        </table>--%>
<%--                    </form>--%>
<%--                </div>--%>
<%--            </div>--%>
<%--            <div id="ExcelUpload-window" title="Excel上传" style="width:400px;height:150px;font-size: 16">--%>
<%--                <div data-options="region:'north',split:true" style="padding:10px">--%>
<%--                    <form id="ExcelpdfForm" action="" enctype="multipart/form-data" method="post">--%>
<%--                        <p>请选择需要导入的Excel文件</p>--%>
<%--                        <input id="ExcelfileUrl" accept=".xlsx" type="file" style="width: 160px;" name="uploadFile"/>--%>
<%--                        <input id="ExceluploadFileBtn" type="button" value="上传" class="btn" style="margin-top: 0px;"/>--%>
<%--                    </form>--%>
<%--                </div>--%>
<%--            </div>--%>
<%--        </div>--%>
        <div data-options="closable:false"></div>
<%--        <div title="浙江103远传映射配置" data-options="closable:false" style="padding:10px;">--%>
<%--            <div>--%>
<%--                <span>CommAddress起始序号:</span><input id="sel_zh_103" value="0"/>--%>
<%--                <input id="bt_dxh_103" type="button" value="一键生成"/>--%>
<%--                </td>--%>
<%--            </div>--%>
<%--            <div id="list_103">--%>
<%--                <table id="i2List_103"></table>--%>
<%--            </div>--%>
<%--        </div>--%>
        <div title="测量量映射配置" data-options="closable:false" style="padding:10px;">
            <table>
                <tr>
                    <td>
                        <div>
                            <fieldset id="ied_field">
                                <legend>IED</legend>
                                <table>
                                    <tr>
                                        <td>iedName</td>
                                        <td><input id="sel_iedName"/>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>apName</td>
                                        <td><input id="sel_apName"/></td>
                                    </tr>
                                    <%--						    	<tr>--%>
                                    <%--							    	<td>ldInst</td>--%>
                                    <%--									<td><input id="sel_ldInst" /></td>--%>
                                    <%--						    	</tr>--%>
                                </table>
                            </fieldset>
                        </div>
                    </td>
                    <td>
                        <div>
                            <fieldset id="data_field">
                                <legend>数据来源</legend>
                                <table>
                                    <tr>
                                        <td>逻辑设备</td>
                                        <td colspan="5"><select id="sel_LD0" class="easyui-combobox"
                                                                style="width:200px;" panelHeight="150">
                                        </select></td>
                                    </tr>
                                    <tr>
                                        <td>逻辑节点</td>
                                        <td colspan="5"><select id="sel_LN0" class="easyui-combobox"
                                                                style="width:200px;" panelHeight="150">
                                        </select></td>
                                    </tr>
                                    <tr>
                                        <td>fc</td>
                                        <td colspan="1"><select id="sel_fc" class="easyui-combobox" style="width:100px;"
                                                                panelHeight="150">
                                        </select></td>
                                        <td><input id="bt_ysSGCB" type="button" value="映射SGCB"></td>
                                        <td></td>
                                        <td></td>
                                    </tr>
                                    <%--						    	<tr>--%>
                                    <%--		                   			<td>mms起始组号</td>--%>
                                    <%--									<td><input id="sel_zh"  value="0" />--%>
                                    <%--									</td>--%>
                                    <%--		                   			<td>mms起始点序号</td>--%>
                                    <%--									<td><input id="sel_dxh" value="0" />--%>
                                    <%--									</td>--%>
                                    <%--									<td><input id="bt_dxh"  type="button" value="一键生成" />--%>
                                    <%--									</td>--%>
                                    <%--						    	</tr>--%>
                                </table>
                            </fieldset>
                        </div>
                    </td>
                </tr>
            </table>
            <div id="loading">
                <div id="cllys_div">
                    <table id="cllys_tab" style="overflow: auto;height:660px;border: 1px solid red"></table>
                </div>
            </div>
        </div>
        <div title="节点名称配置" data-options="closable:false" style="padding:10px;">
            <div id="loading">
                <div id="jdName_div" style="overflow: auto;height:400px;border: 1px solid red">
                    <table id="jdName_tab"></table>
                </div>
            </div>
        </div>
        <div title="测量量名称" data-options="closable:false" style="padding:10px;">
            <div id="refname_desc_div">
                <table id="refname_desc_tb"></table>
            </div>
            <div id="refname_desc_window" title="测量量名称添加修改" style="width:400px;height:300px;font-size: 16">
                <div data-options="region:'north',split:true" style="height:185px;padding:10px">
                    <form id="refname_descForm" class="ui-form" method="post">
                        <table id="tab_refname_desc_Form">
                            <tr>
                                <td>测量量</td>
                                <td><input id="txt_refname_desc_Refname"/></td>
                                <td><label id="msg_refname_desc_Refname"></label></td>
                            </tr>
                            <tr>
                                <td>描述</td>
                                <td><input id="txt_refname_desc_Desc"/></td>
                                <td><label id="msg_refname_desc_Desc"></label>
                                </td>
                            </tr>
                            <tr>
                                <td></td>
                                <td><input type="button" id="bt_refname_desc_downld" value="提交"/></td>
                                <td></td>
                            </tr>
                        </table>
                    </form>
                </div>
            </div>
            <div id="UploadRefname_window" title="测量量名称上传" style="width:400px;height:150px;font-size: 16">
                <div data-options="region:'north',split:true" style="padding:10px">
                    <form id="RefnameForm" action="" enctype="multipart/form-data" method="post">
                        <p>请选择需要上传的.xlsx文件</p>
                        <input id="File_xls" accept=".xlsx" type="file" style="width: 160px;" name="uploadFile"/>
                        <input id="bt_refname_desc_updt" type="button" value="上传" class="btn" style="margin-top: 0px;"/>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript" src="<%=basePath%>/js/commons/tableExport.js"></script>
<script type="text/javascript" src="<%=basePath%>/js/Innerjs/systemConfiguration.js"></script>
<%--<script type="text/javascript" src="<%=basePath%>/js/Innerjs/i1toI2.js"></script>--%>
<%--<script type="text/javascript" src="<%=basePath%>/js/Innerjs/i1toI2_103.js"></script>--%>
<script type="text/javascript" src="<%=basePath%>/js/Innerjs/LEDConfiguration.js"></script>
<script type="text/javascript" src="<%=basePath%>/js/Innerjs/SpaceConfiguration.js"></script>
<script type="text/javascript" src="<%=basePath%>/js/Innerjs/cllysConfig.js"></script>
<script type="text/javascript" src="<%=basePath%>/js/Innerjs/jdNameConfig.js"></script>
<script type="text/javascript" src="<%=basePath%>/js/Innerjs/refnameConfig.js"></script>
<%--        <script type="text/javascript" src="<%=basePath%>/js/commons/swfobject.js"></script>--%>
</body>
</html>


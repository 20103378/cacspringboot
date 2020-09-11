<!-- 事件记录页面 -->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
 <!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
    <head>
        <%@include file="/jsp/resource.jsp" %>
		<style type="text/css">
           #AlarmRecord_div{
           		height:95%;
				margin-riht: 0px;
                margin-bottom: 0px;
           }
           /*#OperaterRecord_div{*/
           /*		height:95%;*/
			/*	margin-riht: 0px;*/
           /*     margin-bottom: 0px;*/
           /*}*/
        </style>
    </head>
    <!--事件记录页面  -->
    <body class="easyui-layout">
        <div  id="showdata" region="center" border="false" data-options="tools:'#tab-tools'" style="width:700px;height:250px;padding:10px;">
			<div>
				<table>
					<tr>
						<td>事件类型选择&nbsp;:&nbsp;
						<select id="ddlRecords" class="easyui-combobox" style="width:100px;">
							<option value="0">告警事件</option>    
						</select></td>
						<td>&nbsp;&nbsp;监测设备选择&nbsp;:&nbsp;
						<select id="ddlDevType" class="easyui-combobox" style="width:100px;">
							<option value="1">油色谱及微水</option>
							<option value="2">SF6气体压力</option>
							<option value="3">避雷器泄漏电流及动作次数</option>
							<option value="4">铁芯泄漏电流</option>
						</select></td>
						<td>&nbsp;&nbsp;告警类型选择&nbsp;:&nbsp;
						<select id="ddlAlarmType" class="easyui-combobox" style="width:100px;">
							<option value="0">全部</option>
							<option value="1">告警</option> 
							<option value="2">故障</option> 
						</select></td>
						<td>&nbsp;&nbsp;选择时间&nbsp;:&nbsp; <input id="tbStart" value="" class="easyui-datebox" style="width:100px"></td>
						<td>至&nbsp;:&nbsp; <input id="tbEnd" value="" class="easyui-datebox" style="width:100px"  ></td>
						<td><a id="search" href="#" class="easyui-linkbutton" iconCls="icon-ok">查询</a></td>
					</tr>
				</table>
				<div id="AlarmRecord_div">
					<table id="AlarmRecordR_list" title="告警事件"></table>
				</div>
<%--				<div id="OperaterRecord_div">--%>
<%--					<table id="OperaterRecord_list" title="操作事件"></table>--%>
<%--				</div>--%>
			</div>
        </div>
    </body>
    <script type="text/javascript" src="<%=basePath%>/js/Innerjs/eventRecord.js"></script>
</html>

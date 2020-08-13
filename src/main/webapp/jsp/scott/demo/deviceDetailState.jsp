<!-- 跳转设备页 -->
<%@ page language="java" contentType="text/html; charset=UTF-8" import="java.util.*" pageEncoding="UTF-8" %>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
 <!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
    <head>
    <meta http-equiv="refresh" content="600">
        <%@include file="/jsp/resource.jsp" %>
        <script type="text/javascript" src="<%=basePath%>/js/commons/datagrid-transposedview.js"></script>
        <style type="text/css">
			.tt-inner img {
                border: 0;
                margin-right: 30px;
                margin-bottom: 30px;
            }#history_div{
				height:97%;
				margin-riht: 0px;
                margin-bottom: 0px;
            }#chartdiv{
           		float:left;
            }#dataSpan{
	            width: 100%;
	        }#tab_inf td{
	        	border:1px solid #9CF;
	        	margin:0;
	        	padding:0;
			}
	         #tab_inf tr{
	        	margin:0;
	        	padding:0;
	        }
			.datagrid-btable tbody tr{
	        	height:30px;
	        }
        </style>
    </head>
	<body class="easyui-layout">
		<!--显示设备健康状态信息-->
		<div class="easyui-tabs" region="center" border="false"
			data-options="tools:'#tab-tools'" style="width: 700px; height: 250px">
			<div title="实时数据" data-options="closable:false" style="padding: 10px;">
				<div id="table_cac" style="width: 100%; height: 100%"><table id="yx_data-list" title="实时数据"></table></div>

				<div id="table_amc" style="width: 100%; height: 100%"><table id="yx_data-list_amc" title="实时数据"></table></div>
				<table id="Infrared_data" title="实时数据"></table>
			</div>

			<div title="遥测历史数据" data-options="closable:false"
				style="padding: 10px; height: 50%; display: none" id="d1">
				<div>开始时间: <input id="startTime" value="" class="easyui-datebox"
						data-options="formatter:myformatter,parser:myparser"
						style="width: 100px"> 结束时间: <input id="endTime" value=""
						class="easyui-datebox"
						data-options="formatter:myformatter,parser:myparser"
						style="width: 100px"> &nbsp;
					<div style="display: none">
						设备状态:<select id="state" class="easyui-combobox"
							style="width: 100px;">
							<option value="0">全部</option>
							<option value="1">正常</option>
							<option value="2">故障</option>
							<option value="3">告警</option>
							<option value="4">停用</option>
						</select>
					</div>
					<a id="searchHistory" href="#" class="easyui-linkbutton"
						iconCls="icon-search">查询</a> 
					<a id="export" href="#"
						class="easyui-linkbutton" iconCls="icon-ok">导出成EXCEL</a>
				</div>
				<div id="history_div">
					<table id="history"></table>
				</div>
			</div>
			<!-- <div title="遥信历史数据" data-options="closable:false"
				style="padding: 10px; height: 95%; display: none" id="d4">
				<div>
					开始时间: <input id="yxStartTime" value="" class="easyui-datebox"
						data-options="formatter:myformatter,parser:myparser"
						style="width: 100px"> 结束时间: <input id="yxEndTime" value=""
						class="easyui-datebox"
						data-options="formatter:myformatter,parser:myparser"
						style="width: 100px"> &nbsp;参数名:<select id="yxName"
						class="easyui-combobox" style="width: 100px;">
					</select> <a id="searchYXHistory" href="#" class="easyui-linkbutton"
						iconCls="icon-search">查询</a>
				</div>
				<div id="YXhistory_div">
					<table id="YXhistory"></table>
				</div>
			</div> -->
			<div title="趋势分析数据" data-options="closable:false"
				style="width: 80%;padding: 10px; display: none" id="d2">
				<div>
					<!--  监测量选择:<select id="value_select" class="easyui-combobox"
						style="width: 100px;" panelHeight="150"> -->
					</select>&nbsp;&nbsp; 开始时间: <input id="_startTime" value=""
						class="easyui-datebox"
						data-options="formatter:myformatter,parser:myparser"
						style="width: 100px"> 结束时间: <input id="_endTime" value=""
						class="easyui-datebox"
						data-options="formatter:myformatter,parser:myparser"
						style="width: 100px"> <a id="showChat" href="#"
						class="easyui-linkbutton" iconCls="icon-search">查询</a>
				</div>
				<div style="float:left;width:80%">
				<div id="chartdiv" align="center"></div>
				<table id="dataSpan">
					<tr>
						<td id="tb0">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
						<td id="tb1"></td>
						<td id="tb2"></td>
						<td id="tb3"></td>
						<td id="tb4"></td>
						<td id="tb5"></td>
						<td id="tb6"></td>
						<td id="tb7"></td>
						<td id="tb8"></td>
						<td id="tb9"></td>
						<td id="tb10"></td>
					</tr>
				</table>
				</div>
				<div style="float:right;width: 18%;height: 90%;">
					<input type="checkbox" id="DTorCL"/>单设备对比<br>
					同类设备：
					<div id="TypeDevice_div" style="height: 45%;border:1px solid;overflow:scroll;">
						<table id="TypeDevice" style="height:100%;width: 90%;">
						</table>
					</div>
					测量量：
					<div id="cll_div" style="height: 45%;border:1px solid;overflow:scroll;">
						<table id="cll" style="height:100%;width: 90%;">
						</table>
					</div>
				</div>
			</div>
			<!--  div title="inf文件" data-options="closable:false" style="padding: 10px;"
				id="d3">
				<  <table id="inf-list" title="LED装置"></table>
	                <br/>
	                <table id="sensor-list" title="传感器装置"></table> >
				<div id="div_inf">
					<table id="tab_inf">
					</table>
				</div>
			</div -->
		</div>
		<div data-options="region:'south'"
			style="text-align: center; height: 20px; display: none">
			<img src="<%=basePath%>/images/state/Green.png">正常</img> <img
				src="<%=basePath%>/images/state/Yellow.png">故障</img> <img
<%--				src="<%=basePath%>/images/state/Red.png">告警</img> <img--%>
<%--				src="<%=basePath%>/images/state/Gray.png">停用</img>--%>
		</div>
		<input type="hidden" id="txtID" name="hiddenField"
			value=<%=(String)session.getAttribute("DeviceID")%> />
		<input type="hidden" id="txtType" name="hiddenField"
			value=<%=(String)session.getAttribute("DeviceType")%> />
		<input type="hidden" id="txtName" name="hiddenField"
			value=<%=(String)session.getAttribute("DeviceName")%> />
		<script type="text/javascript" src="<%=basePath%>/js/Innerjs/deviceDetailState.js"></script>
		<script type="text/javascript" src="<%=basePath%>/js/Innerjs/yx.js"></script>
		<script type="text/javascript" src="<%=basePath%>/js/Innerjs/history.js"></script>
		<script type="text/javascript" src="<%=basePath%>/js/fusioncharts/FusionCharts.js"></script>
<%--		<script type="text/javascript" src="<%=basePath%>/js/fusioncharts/fusioncharts.js"></script>--%>
		<script type="text/javascript" src="<%=basePath%>/js/Innerjs/chart.js"></script>
		<!-- script type="text/javascript" src="<%=basePath%>/jsp/com.com.scott/demo/inf.js"></script -->
		<script type="text/javascript" src="<%=basePath%>/js/commons/tableExport.js"></script>
		<script type="text/javascript" src="<%=basePath%>/js/Innerjs/yxHistory.js"></script>
	</body>
</html>

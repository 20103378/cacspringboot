<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
 <!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
    <head>
        <%@include file="/jsp/resource.jsp" %>
        <script type="text/javascript" src="<%=basePath%>/js/fusioncharts/FusionCharts.js"></script>
<%--		<script type="text/javascript" src="<%=basePath%>/js/fusioncharts/fusioncharts.js"></script>--%>
		<style type="text/css">
         	#currentState,#tt {
         	    width:90%;
				font-family: verdana,arial,sans-serif;
				font-size:11px;
				color:#333333;
				border-width: 1px;
				border-color: #666666;
				border-collapse: collapse;
			}
			.col {
				border-width: 1px;
				padding: 8px;
				border-style: solid;
				border-color: #666666;
				background-color: #dedede;
			}
			#currentState th{
				text-align:left;
			}
			#currentState td {
				border-width: 1px;
				padding: 8px;
				border-style: solid;
				border-color: #666666;
				background-color: #ffffff;
			}
			.stateDiv{
				border-width: 1px;
			}
			.stateTable td{
				width:8px;
				height:8px;
				border:1px solid white;
			}
			.divcol{
				float:left;
				width:30px;
			}
         </style>
    </head>
    <body class="easyui-layout">
        <div id="tree_divice_health_state" data-options="region:'west',split:true" title="通讯单元" style="width:260px;">
            <ul id="ZoneEmuList">
            </ul>
        </div>
        <div region="center" border="false">
            <div class="easyui-tabs" region="center" border="false" data-options="tools:'#tab-tools'" >
                <div title="通讯状态"  data-options="closable:false" style="padding:10px;">
                <div id="conn_status_all"></div>
                <div id="divState" >
	                <table id="currentState" border="1">
						<tr>
							<th id="txtHeader" colspan="3"></th>
						</tr>
						<tr>
							<td class="col">装置类型</td>
							<td colspan="2">SCAC</td>
						</tr>
						<tr>
							<td class="col">规约类型</td>
							<td colspan="2">IEC61850</td>
						</tr>
						<tr>
							<td class="col">状态时标</td>
							<td id="txtDevicpTime" colspan="2"></td>
						</tr>
						<tr>
							<td class="col">IP地址</td>
							<td id="IPADD" colspan="2"></td>
						</tr>
						<tr>
							<td class="col">通信状态</td>
							<td id="txtIedstate"></td>
						</tr>
					</table>
               	</div>
              		<div style="height:200px">
					<table id="AMC104ConnState" title="I2连接状态"></table>
					</div>
                </div>

				<div title="历史记录" data-options="closable:false"
				style="padding: 10px;">
				选择时间: <input id="ck_Time" value="" class="easyui-datebox"
					style="width: 100px"> &nbsp;&nbsp;&nbsp;&nbsp; <input
					id="btnTime" type="button" value="查询历史状态" />
				<table>
					<tr>
						<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span
							style="font-size: 20px; color: #336699">61850通讯</span>
							<br><div id="timexy_1">时间： </div><br />
							<div class="divcol">
								<table>
									<tr>
										<td>24</td>
									</tr>
									<tr>
										<td><br />20</td>
									</tr>
									<tr>
										<td><br />15</td>
									</tr>
									<tr>
										<td><br />10</td>
									</tr>
									<tr>
										<td><br />5</td>
									</tr>
								</table>
							</div>
							<div id="div_datastate" class="stateDiv">
								<table id="table_datastate" class='stateTable' cellspacing="0" cellpadding="0">
								</table>
							</div>
							</td>
						</tr>
						<tr>
						<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span
							style="font-size: 20px; color: #336699">连接状态</span>
							<br><div id="timexy_2">时间：</div> <br />
							<div class="divcol">
								<table>
									<tr>
										<td>24</td>
									</tr>
									<tr>
										<td><br />20</td>
									</tr>
									<tr>
										<td><br />15</td>
									</tr>
									<tr>
										<td><br />10</td>
									</tr>
									<tr>
										<td><br />5</td>
									</tr>

								</table>
							</div>
							<div id="div_datastate">
								<table id="table_iedstate" class='stateTable' cellspacing="0" cellpadding="0">

								</table>
							</div>
							</td>
						</tr>
					</table>
				</div>
           		<div title="程序运行状态" data-options="closable:false"
					style="width: 100%;height: 100%">
					<table id="ProgramState" data-options="fit:true" class="easyui-datagrid"></table>
				</div>
				<div title="cac与各装置通讯日志" data-options="closable:false"
					style="width: 100%;height: 100%">
					<table id="CACState" data-options="fit:true" class="easyui-datagrid"></table>
				</div>
				<div title="cac与cag通讯日志" data-options="closable:false"
					style="width: 100%;height: 100%">
					<table id="CAGState" data-options="fit:true" class="easyui-datagrid"></table>
				</div>
            </div>
        </div>
        <script type="text/javascript" src="<%=basePath%>/js/commons/datagrid-transposedview.js"></script>
		<script type="text/javascript" src="<%=basePath%>/js/commons/tableExport.js"></script>
        <script type="text/javascript" src="<%=basePath%>/js/Innerjs/connState.js"></script>
    </body>
</html>


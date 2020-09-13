	<!-- 设备报表页 -->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
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
            }
	        .Detailtb td{
	        	margin:0;
	        	padding:0;
	        	font-size:16px;
	        	font-family:Microsoft YaHei;
	        	font-size:15;
	        	text-align:center;
	        	color:#ffffff
	        }
    	    .Detailtb tr{
	        	font-size:16px;
	        	background:#566071;
	        }
	        .Detailtb th{
	        	font-size:16px
	        }
	        #TypeDevice td{
	        	border:1px solid #9CF;
	        	margin:0;
	        	padding:0;
	        }
	        #cll td{
	        	border:1px solid #9CF;
	        	margin:0;
	        	padding:0;

	        }#tab_inf tr{
	        	margin:0;
	        	padding:0;
	        }.datagrid-btable tbody tr{
	        	height:30px;
	        }
        </style>
    </head>
	<body class="easyui-layout">
		<!--显示设备健康状态信息-->
		<div class="easyui-tabs" region="center" border="false"
			data-options="tools:'#tab-tools'" style="width: 700px; height: 250px">
        	<div title="类型设备实时信息"  data-options="closable:false" style="padding:10px;">
        		<table>
<%--					<td>设备区域：</td>--%>
					<td>
<%--						<select id="DeviceArea" class="easyui-combobox" style="width:200px;height:30px" panelHeight="200px"></select>--%>
						查询日期: <input id="startTime" value="" class="easyui-datebox"
						data-options="formatter:myformatter,parser:myparser"
						style="width: 100px"> 查询时间:
						<select id="findTime" class="easyui-combobox" name="select" >

							<option value="1">01</option>
							<option value="2">02</option>
							<option value="3">03</option>
							<option value="4">04</option>
							<option value="5">05</option>
							<option value="6">06</option>
							<option value="7">07</option>
							<option value="8">08</option>
							<option value="9">09</option>
							<option value="10">10</option>
							<option value="11">11</option>
							<option value="12">12</option>
							<option value="13">13</option>
							<option value="14">14</option>
							<option value="15">15</option>
							<option value="16">16</option>
							<option value="17">17</option>
							<option value="18">18</option>
							<option value="19">19</option>
							<option value="20">20</option>
							<option value="21">21</option>
							<option value="22">22</option>
							<option value="23">23</option>
							<option value="24">24</option>
					    </select>&nbsp;
						<a id="search_but" href="#" class="easyui-linkbutton" iconCls="icon-search">查询</a>&nbsp;
						<a id="reset_but" href="#" class="easyui-linkbutton" iconCls="icon-search">重置</a>&nbsp;
	<!--input type="button" value="导出" onclick="$('#data_list_0_tb').tableExport({type:'pdf',escape:'false'})"-->

						<a id="Detail_but" href="#" class="easyui-linkbutton" iconCls="icon-sum">导出</a>
					</td>

					<!--  <td>设备选择：</td>
					<td>
						<select id="Device" class="easyui-combobox" style="width:200px;height:30px" panelHeight="200px" "></select>
					</td>
					-->
				</table>
        		<table id="showdata_div" style="height:95%;">
        		</table>
		 	</div>
		</div>
		<div data-options="region:'south'"
			style="text-align: center; height: 20px; display: none">
			<img src="<%=basePath%>/images/state/Green.png">正常</img> <img
				src="<%=basePath%>/images/state/Yellow.png">故障</img> <img
				src="<%=basePath%>/images/state/Red.png">告警</img> <img
				src="<%=basePath%>/images/state/Gray.png">停用</img>
		</div>
		<input type="hidden" id="txtType" name="hiddenField"
			value=<%=(String)session.getAttribute("DeviceType")%> />
		<input type="hidden" id="txtName" name="hiddenField"
			value=<%=(String)session.getAttribute("DeviceName")%> />
		<script type="text/javascript" src="<%=basePath%>/js/Innerjs/deviceDetailState.js"></script>
		<script type="text/javascript" src="<%=basePath%>/js/Innerjs/spaceDetailState.js"></script>
<%--		<script type="text/javascript" src="<%=basePath%>/js/tableExport/jquery.base64.js"></script>--%>
<%--		<script type="text/javascript" src="<%=basePath%>/js/tableExport/tableExport.js"></script>--%>
<%--		<script type="text/javascript" src="<%=basePath%>/js/tableExport/jspdf/libs/sprintf.js"></script>--%>
<%--		<script type="text/javascript" src="<%=basePath%>/js/tableExport/jspdf/jspdf.js"></script>--%>
<%--		<script type="text/javascript" src="<%=basePath%>/js/tableExport/jspdf/libs/base64.js"></script>--%>
<%--		<script type="text/javascript" src="<%=basePath%>/js/tableExport/html2canvas.js"></script>--%>
		<script type="text/javascript" src="<%=basePath%>/js/commons/tableExport.js"></script>
		<script type="text/javascript" src="<%=basePath%>/js/commons/tableExport_test.js"></script>

		<!-- script type="text/javascript" src="<%=basePath%>/jsp/com.scott/demo/yxHistory.js"></script -->
	</body>
</html>

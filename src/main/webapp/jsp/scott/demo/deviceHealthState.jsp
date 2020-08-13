<!-- 健康设备状态页 -->
<%@ page language="java" contentType="text/html; charset=UTF-8" import="java.util.*" pageEncoding="UTF-8" %>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <%@include file="/jsp/resource.jsp" %>
    <script type="text/javascript" src="<%=basePath%>/js/fusioncharts/FusionCharts.js"></script>
    <style type="text/css">
        .tt-inner img {
            border: 0;
            margin-right: 30px;
            margin-bottom: 30px;
        }
    </style>
</head>
<body class="easyui-layout">
<!--显示设备健康状态信息-->
<div class="easyui-tabs" id="eTab" region="center" border="false" data-options="tools:'#tab-tools'"
     style="width:700px;height:200px;padding:10px;">
    <div title="设备健康状态信息" data-options="closable:false" style="padding:10px;">
        <table>
            <%--					<td>设备区域：</td>--%>
            <%--					<td>--%>
            <%--						<select id="DeviceArea" class="easyui-combobox" style="width:200px;height:30px" panelHeight="200px"></select>--%>
            <%--					</td>--%>
            <td>设备类型：</td>
            <td>
                <select id="DeviceType" class="easyui-combobox" style="width:200px;height:30px"
                        panelHeight="200px"></select>
            </td>

        </table>
        <div id="showdata" style="height:95%">
        </div>
        <div id="ShowDevice-window" title="实时数据" style="width:400px;height:150px;font-size: 16"
             data-options="region:'north',split:true">
            <table id="yx_data-list" title="实时数据" style="padding:10px"></table>
        </div>
    </div>

    <%--        <input type="hidden" id="txtID" name="hiddenField" value=--%>
    <%--        <%=(String)session.getAttribute("DeviceID")%>--%>
    <%--		/>--%>
    <%--		<input type="hidden" id="txtMap" name="hiddenField" value=--%>
    <%--        <%=(String)session.getAttribute("map")%>--%>
    <%--		/>--%>
    <div data-options="region:'south'" style=" text-align:center;height:20px;">
        <img src="<%=basePath%>/images/state/Green.png">正常</img>
        <img src="<%=basePath%>/images/state/Yellow.png">故障</img>
        <img src="<%=basePath%>/images/state/Red.png">告警</img>
        <img src="<%=basePath%>/images/state/Gray.png">停用</img>
    </div>
    <script type="text/javascript" src="<%=basePath%>/js/Innerjs/deviceHealthState.js"></script>
</div>
</body>
</html>

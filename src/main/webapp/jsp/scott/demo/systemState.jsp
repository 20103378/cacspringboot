<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
 <!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
    <head>
        <%@include file="/jsp/resource.jsp" %>
        <script type="text/javascript" src="<%=basePath%>/js/fusioncharts/FusionCharts.js"></script>
		<style type="text/css">
         	#cpuchartdiv{
         		float:left;
         	}
         	#memchartdiv{
         		float:left;
         	}
         </style>
    </head>
    <body class="easyui-layout">

        <div region="center" border="false">
	                <div id="chartdiv">
	                <table width="1000px" border="1" cellspacing="1" cellpadding="2"
	                style='font-family:Microsoft YaHei;font-size:20;font-weight:bold;margin-left:20px'>
	                	<tr>
						    <td valign="top" class="text" align="left">
							<p>磁盘总容量:</p>
							</td>
							<td valign="top" class="text" align="left">
							<div class="capdiv" id="cpcapall">未查询到数据</div>
							</td>
						    <td valign="top" class="text" align="left">
						    <p>内存总容量:</p>
							</td>
							<td valign="top" class="text" align="left">
							<div class="capdiv" id="nccapall">未查询到数据</div>
							</td>
							<td valign="top" class="text" align="left">
						    <p>CAC103serverCPU使用率:</p>
							</td>
							<td valign="top" class="text" align="left">
							<div class="capdiv" id="I1cpu">未查询到数据</div>
							</td>
							<td valign="top" class="text" align="left">
						    <p>IEC61850CPU使用率:</p>
							</td>
							<td valign="top" class="text" align="left">
							<div class="capdiv" id="I2cpu">未查询到数据</div>
							</td>
							<td valign="top" class="text" align="left">
						    <p>用户CPU使用率:</p>
							</td>
							<td valign="top" class="text" align="left">
							<div class="capdiv" id="usercpu">未查询到数据</div>
							</td>
						</tr>
						<tr>
							<td valign="top" class="text" align="left">
							<p>磁盘剩余容量:</p>
							</td>
							<td valign="top" class="text" align="left">
							<div class="capdiv" id="cpcap">未查询到数据</div>
							</td>
							<td valign="top" class="text" align="left">
							<p>内存剩余容量:</p>
							</td>
							<td valign="top" class="text" align="left">
							<div class="capdiv" id="nccap">未查询到数据</div>
							</td>
							<td valign="top" class="text" align="left">
							<p>CAC103server内存使用率:</p>
							</td>
							<td valign="top" class="text" align="left">
							<div class="capdiv" id="I1men">未查询到数据</div>
							</td>
							<td valign="top" class="text" align="left">
							<p>IEC61850内存使用率:</p>
							</td>
							<td valign="top" class="text" align="left">
							<div class="capdiv" id="I2men">未查询到数据</div>
							</td>
							<td valign="top" class="text" align="left">
						    <p>系统CPU使用率:</p>
							</td>
							<td valign="top" class="text" align="left">
							<div class="capdiv" id="syscpu">未查询到数据</div>
							</td>
						</tr>
	                </table>
	                </div>
	                <table width="98%" border="0" cellspacing="0" cellpadding="3" align="center">
						  <tr>
						    <td valign="top" class="text" align="left">
						    	<div id="cpuchartdiv" align="left"></div>
						     </td>
						     <td valign="top" class="text" align="left">
						    	<div id="memchartdiv" align="left"></div>
						     </td>
						  </tr>
					</table>
                </div>
        <script type="text/javascript" src="<%=basePath%>/js/Innerjs/currentState.js"></script>
    </body>
</html>


<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"  %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"  %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>SCAC-3000 在线监控系统</title>
    <%@include file="/jsp/resource.jsp" %>
    <link rel="stylesheet" type="text/css" href="<%=basePath%>/css/BreakingNews.css"/>
    <link rel="stylesheet" type="text/css" href="<%=basePath%>/css/main.css">
    <script src="<%=basePath%>/js/ux/main/BreakingNews.js"></script>
    <script type="text/javascript" src="<%=basePath%>/js/ux/main/main.js"></script>
  </head>
  <body class="easyui-layout">
 	<div class="ui-header" data-options="region:'north',split:true,border:false" style="height:85px;overflow: hidden;">
 		<div id="logo" style="height:120px">
	 		<div id="sh_logo" style="width:400px"></div>

		 	<div  class="ui-login">
		 		<div class="ui-login-info">
		 		<br><br>
			 		温度:<span id ='Temperature'></span>℃
			 		湿度:<span id ='Humidity'></span>%
			 		风向:<span id ='WindDirection'></span>°
			 		风速:<span id ='WindSpeed'></span>m/s
			 		欢迎<span class="orange">${user.nickName}</span> 第[<span class="orange">${user.loginCount}</span>]次登录
			 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			 		<a class="modify-pwd-btn"  href="javascript:void(0);">修改密码</a> |
		 			<a class="logout-btn" href="<%=basePath%>/logout">退出</a>
		 		</div>
		 	</div>
		 	<br>
		</div>
		<!-- 加入导航区 -->
		<!-- div style="height:40px;">
			  <div class="BreakingNewsController easing" id="breakingnews" style="height:33px;">
        		<div class="bn-title"></div>
		            <ul id="message">
		            </ul>
            	<div class="bn-arrows"><span class="bn-arrows-left"></span><span class="bn-arrows-right"></span></div>
       		 </div>
   		 </div>
   		-->
   <!-- 加入删选区域 -->

		</div>
 	</div>
	<!-- 树形菜单 -->
	<div data-options="region:'west',split:true,title:'功能导航'" style="width:200px;">
		<div id="tree-box" class="easyui-accordion" data-options="fit:true,border:false">
			<c:forEach var="item" items="${menuList}">
			<div title="${item.text} ">
				<c:choose>
   					 <c:when test="${item.text == '业务功能'}">
					 		<ul id="ZoneEmuList">
           					 </ul>
           					 <div style="display:none">
           					 	
           					 	<a id="DeviceDetail" class="menu-item" href="">设备详细信息</a>
           					 </div>
					 </c:when>
					<c:otherwise>
						<c:forEach var="node" items="${item.children}">
						<a class="menu-item" href="<%=basePath%>${node.url}">${node.text}</a>
						</c:forEach>
					</c:otherwise>

				 </c:choose>
			</div>
			</c:forEach>
		</div>
	</div>
	<div data-options="region:'south',split:true,border:false" style="height: 30px;overflow:hidden;">
		<div class="panel-header" style="border: none;text-align: center;" >CopyRight &copy; 2015 杭州申昊科技有限公司   版权所有. &nbsp;&nbsp;官方网址: </div>
	</div>
	<!-- 中间内容页面 -->
	<div data-options="region:'center'" >
		<div class="easyui-tabs" id="tab-box" data-options="fit:true,border:false">
			<!--
			<div title="Welcome" style="padding:20px;overflow:hidden;">

			</div>
			-->
		</div>
	</div>
	<!--  modify password start -->
	<div id="modify-pwd-win"  class="easyui-dialog" buttons="#editPwdbtn" title="修改用户密码" data-options="closed:true,iconCls:'icon-save',modal:true" style="width:350px;height:200px;">
		<form id="pwdForm" action="modifyPwd" class="ui-form" method="post">
     		 <input class="hidden" name="id">
     		 <input class="hidden" name="email">
     		 <div class="ui-edit">
	           <div class="fitem">
	              <label>旧密码:</label>
	              <input id="oldPwd" name="oldPwd" type="password" class="easyui-validatebox"  data-options="required:true"/>
	           </div>
	            <div class="fitem">
	               <label>新密码:</label>
	               <input id="newPwd" name="newPwd" type="password" class="easyui-validatebox" data-options="required:true" />
	           </div>
	           <div class="fitem">
	               <label>重复密码:</label>
	              <input id="rpwd" name="rpwd" type="password" class="easyui-validatebox"   required="required" validType="equals['#newPwd']" />
	           </div>
	         </div>
     	 </form>
     	 <!-- 修改密码窗体 -->
     	 <div id="editPwdbtn" class="dialog-button" >
            <a href="javascript:void(0)" class="easyui-linkbutton" id="btn-pwd-submit">确定</a>
            <a href="javascript:void(0)" class="easyui-linkbutton" id="btn-pwd-close">取消</a>
         </div>
	</div>
	<script type="text/javascript" src="<%=basePath%>/js/Innerjs/deviceTree.js"></script>
	<!-- modify password end  -->
  </body>
</html>

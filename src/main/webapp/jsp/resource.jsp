<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%-- <%
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
%> --%>

<%
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
	String ctxPath=(String)request.getSession().getServletContext().getAttribute("ctxPath");
	if(ctxPath == null){
		request.getSession().getServletContext().setAttribute("ctxPath",request.getContextPath());
	}	
%>
<script type="text/javascript">
var ctxPath='${ctxPath}';
var basePath='<%=basePath%>';
//alert(ctxPath);
//alert(basePath);
</script>


<!-- 公共资源CSS,JS  -->
<!--Css -->
<link rel="stylesheet" type="text/css" href="<%=basePath%>/js/jquery-easyui-1.3.2/themes/bootstrap/easyui.css">
<link rel="stylesheet" type="text/css" href="<%=basePath%>/js/jquery-easyui-1.3.2/themes/icon.css">
<link rel="stylesheet" type="text/css" href="<%=basePath%>/css/base.css">
<!-- ** Javascript ** -->
<script type="text/javascript" src="<%=basePath%>/js/commons/jquery.min.js"></script>
<script type="text/javascript" src="<%=basePath%>/js/commons/jquery.form.js"></script>
<script type="text/javascript" src="<%=basePath%>/js/commons/package.js"></script>
<script type="text/javascript" src="<%=basePath%>/js/jquery-easyui-1.3.2/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=basePath%>/js/commons/urls.js?v=11"></script>
<script type="text/javascript" src="<%=basePath%>/js/commons/base.js?v=11"></script>
<script type="text/javascript" src="<%=basePath%>/js/commons/YDataGrid.js"></script>
<script type="text/javascript" src="<%=basePath%>/js/jquery-easyui-1.3.2/locale/easyui-lang-zh_CN.js"></script>
<!-- miniui 
<script  type="text/javascript" src="<%=basePath%>/js/miniui/boot.js"></script>
-->
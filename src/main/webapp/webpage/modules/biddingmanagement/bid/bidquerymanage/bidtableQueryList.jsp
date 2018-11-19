<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>投标管理</title>
	<meta http-equiv="Content-type" content="text/html; charset=utf-8">
	<meta name="decorator" content="ani"/>
	<%@ include file="/webpage/include/bootstraptable.jsp"%>
	<%@include file="/webpage/include/treeview.jsp" %>
	<%@include file="bidtableQueryList.js" %>
</head>
<body>
	<div class="wrapper wrapper-content">
	<div class="panel panel-primary">
	<div class="panel-heading">
		<h3 class="panel-title">投标查询管理列表</h3>
	</div>
	<div class="panel-body">
		<sys:message content="${message}"/>
	<!-- 搜索 (伪类：clear属性用于标记重置后可清除value的输入框)-->
		<div class="accordion-inner">
			<form:form id="searchForm" modelAttribute="bidtableQuery" class="form form-horizontal well clearfix">
			<div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="招标单位名称：">招标单位名称：</label>
				<form:input path="company.companyName" htmlEscape="false" maxlength="64"  class=" form-control clear"/>
			</div>
			<div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="参投单位名称：">参投单位名称：</label>
				<form:input path="bidCompanyManage.company.companyName" htmlEscape="false" maxlength="64"  class=" form-control clear"/>
			</div>
			<div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="项目工程id：">项目工程名称：</label>
				<form:input path="program.programName" htmlEscape="false" maxlength="64"  class=" form-control clear"/>
			</div>
<!-- 			<div class="col-xs-12 col-sm-6 col-md-4"> -->
<!-- 				<label class="label-item single-overflow pull-left" title="项目工程id：">人员名称：</label> -->
<%-- 				<form:input path="program.programName" htmlEscape="false" maxlength="64"  class=" form-control clear"/> --%>
<!-- 			</div> -->
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="项目状态：">项目状态：</label>
				<div class="col-xs-12">
					<form:checkboxes class="i-checks" path="program.status"  items="${fns:getDictList('programstatus')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</div>
			</div>
		 <div class="col-xs-12 col-sm-6 col-md-4">
			<div style="margin-top:26px">
			  <a  id="search" class="btn btn-primary btn-rounded  btn-bordered btn-sm"><i class="fa fa-search"></i> 查询</a>
			  <a  id="reset" class="btn btn-primary btn-rounded  btn-bordered btn-sm" ><i class="fa fa-refresh"></i> 重置</a>
			 </div>
	    </div>	
	</form:form>
	</div>
	<!-- 工具栏 -->
	<div id="toolbar">
			<%-- <shiro:hasPermission name="bidmanage:bidtable:add"> --%>
<!-- 				<a id="add" class="btn btn-primary" href="#" title="投标管理" onclick="add()"><i class="glyphicon glyphicon-plus"></i> 新建</a> -->
			<%-- </shiro:hasPermission>--%>
<%-- 			<shiro:hasPermission name="bidmanage:bidtable:edit">  --%>
			    <button id="exportBidTab" class="btn btn-success" disabled onclick="exportBidTab()">
	            	<i class="glyphicon glyphicon-edit"></i> 导出投标情况表
	        	</button>
<%-- 			</shiro:hasPermission> --%>
    </div>
	<!-- 表格 -->
	<table id="bidtableTable"   data-toolbar="#toolbar"></table>
	</div>
	</div>
	</div>
</body>
</html>
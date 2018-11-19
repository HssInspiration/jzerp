<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>项目工程管理</title>
	<meta http-equiv="Content-type" content="text/html; charset=utf-8">
	<meta name="decorator" content="ani"/>
	<%@ include file="/webpage/include/bootstraptable.jsp"%>
	<%@include file="/webpage/include/treeview.jsp" %>
	<%@include file="programList.js" %>
</head>
<body>
	<div class="wrapper wrapper-content">
	<div class="panel panel-primary">
	<div class="panel-heading">
		<h3 class="panel-title">
		投标项目管理列表</h3>
	</div>
	<div class="panel-body">
		<sys:message content="${message}"/>
	
	<!-- 搜索 -->
	<div class="accordion-group">
	<div id="collapseTwo" class="accordion-body collapse">
		<div class="accordion-inner">
			<form:form id="searchForm" modelAttribute="program" class="form form-horizontal well clearfix">
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="项目工程编号：">项目工程编号：</label>
				<form:input path="programNum" htmlEscape="false" maxlength="64"  class=" form-control"/>
			 </div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="项目工程名称：">项目工程名称：</label>
				<form:input path="programName" htmlEscape="false" maxlength="64"  class=" form-control"/>
			 </div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="发标单位：">发标单位：</label>
				<form:input path="company.companyName" htmlEscape="false" maxlength="64"  class=" form-control"/>
			 </div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="项目状态：">项目状态：</label>
				<form:select path="status"  class="form-control m-b">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('programstatus')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			 </div>
		 <div class="col-xs-12 col-sm-6 col-md-4">
			<div style="margin-top:26px">
			  <a  id="search" class="btn btn-primary btn-rounded  btn-bordered btn-sm"><i class="fa fa-search"></i> 查询</a>
			  <a  id="reset" class="btn btn-primary btn-rounded  btn-bordered btn-sm" ><i class="fa fa-refresh"></i> 重置</a>
			</div>
	     </div>	
	</form:form>
	</div>
	</div>
	</div>
	
	<!-- 工具栏 -->
	<div id="toolbar">
			<shiro:hasPermission name="programmanage:program:addForJyb">
				<a id="add" class="btn btn-primary" onclick="add()"><i class="glyphicon glyphicon-plus"></i> 新建</a>
			</shiro:hasPermission>
			<shiro:hasPermission name="programmanage:program:editForJyb">
			    <button id="edit" class="btn btn-success" disabled onclick="edit()">
	            	<i class="glyphicon glyphicon-edit"></i> 修改
	        	</button>
			</shiro:hasPermission>
			<shiro:hasPermission name="programmanage:program:delForJyb">
				<button id="remove" class="btn btn-danger" disabled onclick="deleteAll()">
	            	<i class="glyphicon glyphicon-remove"></i> 删除
	        	</button>
        	</shiro:hasPermission>
			<shiro:hasPermission name="programmanage:program:completedForJyb">
				<button id="completed" class="btn btn-info" disabled onclick="completed()">
	            	<i class="glyphicon glyphicon-ok-sign"></i> 竣工
	        	</button>
        	</shiro:hasPermission>
			<shiro:hasPermission name="programmanage:program:shutdownForJyb">
				<button id="shutdown" class="btn btn-warning" disabled onclick="shutdown()">
	            	<i class="glyphicon glyphicon-ban-circle"></i> 停工
	        	</button>
        	</shiro:hasPermission>
			<shiro:hasPermission name="programmanage:program:closecaseForJyb">
				<button id="closecase" class="btn btn-success" disabled onclick="closecase()">
	            	<i class="glyphicon glyphicon-ban-check"></i> 结案
	        	</button>
        	</shiro:hasPermission>
	        	<a class="accordion-toggle btn btn-default" data-toggle="collapse" data-parent="#accordion2" href="#collapseTwo">
					<i class="fa fa-search"></i> 检索
				</a>
		    </div>
		
	<!-- 表格 -->
	<table id="table"
			   data-toolbar="#toolbar"
	           data-id-field="id">
	</table>

	</div>
	</div>
	</div>
</body>
</html>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>人员在建项目管理</title>
	<meta http-equiv="Content-type" content="text/html; charset=utf-8">
	<meta name="decorator" content="ani"/>
	<%@ include file="/webpage/include/bootstraptable.jsp"%>
	<%@include file="/webpage/include/treeview.jsp" %>
	<%@include file="procontractList.js" %>	
</head>
<body>
	<div class="wrapper wrapper-content">
	<div class="panel panel-primary">
	<div class="panel-heading">
		<h3 class="panel-title">总包合同管理列表</h3>
	</div>
	<div class="panel-body">
		<sys:message content="${message}"/>
	
	<!-- 搜索 -->
	<div class="accordion-group">
	<div id="collapseTwo" class="accordion-body collapse">
		<div class="accordion-inner">
			<form:form id="searchForm" modelAttribute="proContract" class="form form-horizontal well clearfix">
				<div class="col-xs-12 col-sm-6 col-md-4">
					<label class="label-item single-overflow pull-left" title="合同名称：">合同名称：</label>
					<form:input path="contractName" htmlEscape="false" maxlength="64"  class=" form-control"/>
				</div>
				<div class="col-xs-12 col-sm-6 col-md-4">
					<label class="label-item single-overflow pull-left" title="项目名称：">项目名称：</label>
					<form:input path="program.programName" htmlEscape="false" maxlength="64"  class=" form-control"/>
				</div>
				<div class="col-xs-12 col-sm-6 col-md-4">
					<label class="label-item single-overflow pull-left" title="人员名称：">拟草人：</label>
					<form:input path="user.name" htmlEscape="false" maxlength="64"  class=" form-control"/>
				</div>
				<div class="col-xs-12 col-sm-6 col-md-4">
					<label class="label-item single-overflow pull-left" title="承包单位：">承包单位：</label>
					<form:input path="program.office.name" htmlEscape="false" maxlength="64"  class=" form-control"/>
				</div>
				<div class="col-xs-12 col-sm-6 col-md-4">
				 <div class="form-group">
					<label class="label-item single-overflow pull-left" title="签订日期：">签订日期：</label>
					<div class="col-xs-12">
						   <div class="col-xs-12 col-sm-5">
					        	  <div class='input-group date' id='beginContractDate' style="left: -10px;" >
					                   <input type='text'  name="beginContractDate" id="beginDate"  class="form-control"  />
					                   <span class="input-group-addon">
					                       <span class="glyphicon glyphicon-calendar"></span>
					                   </span>
					             </div>	
					        </div>
					        <div class="col-xs-12 col-sm-1">
					        		~
					       	</div>
					        <div class="col-xs-12 col-sm-5">
					          	<div class='input-group date' id='endContractDate' style="left: -10px;" >
					                   <input type='text'  name="endContractDate" id="endDate"  class="form-control" />
					                   <span class="input-group-addon">
					                       <span class="glyphicon glyphicon-calendar"></span>
					                   </span>
					           	</div>	
					        </div>
					</div>
				</div>	
			</div>
				<div class="col-xs-12 col-sm-6 col-md-4">
					<label class="label-item single-overflow pull-left" title="审批状态：">审批状态：</label>
					<form:select path="approvalStatus"  class="form-control m-b">
						<form:option value="" label="--请选择审批状态--"/>
						<form:options items="${fns:getDictList('procontract_approval')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
					</form:select>
				</div>
				<div class="col-xs-12 col-sm-6 col-md-4">
					<label class="label-item single-overflow pull-left" title="合同状态：">合同状态：</label>
					<form:select path="contractStatus"  class="form-control m-b">
						<form:option value="" label="--请选择合同状态--"/>
						<form:options items="${fns:getDictList('contract_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
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
<%-- 			<shiro:hasPermission name="subpackage:building:add"> --%>
				<a id="add" class="btn btn-primary" onclick="add()"><i class="glyphicon glyphicon-plus"></i> 新建</a>
<%-- 			</shiro:hasPermission> --%>
<%-- 			<shiro:hasPermission name="subpackage:building:edit"> --%>
			    <button id="edit" class="btn btn-success" disabled onclick="edit()">
	            	<i class="glyphicon glyphicon-edit"></i> 修改
	        	</button>
<%-- 			</shiro:hasPermission> --%>
<%-- 			<shiro:hasPermission name="subpackage:building:del"> --%>
				<button id="remove" class="btn btn-danger" disabled onclick="deleteAll()">
	            	<i class="glyphicon glyphicon-remove"></i> 删除
	        	</button>
<%-- 			</shiro:hasPermission> --%>
				<button id="startApproval" class="btn btn-primary" disabled onclick="startApproval()">
	            	<i class="glyphicon glyphicon-open"></i> 启动审批
	        	</button>
				<button id="stamp" class="btn btn-info" disabled onclick="stamp()">
	            	<i class="glyphicon glyphicon-star"></i> 用印
	        	</button>
				<button id="confirmValid" class="btn btn-success" disabled onclick="confirmValid()">
	            	<i class="glyphicon glyphicon-hand-up"></i> 确认生效
	        	</button>
	        	<a class="accordion-toggle btn btn-default" data-toggle="collapse" data-parent="#accordion2" href="#collapseTwo">
					<i class="fa fa-search"></i> 检索
				</a>
		    </div>
		
	<!-- 表格 -->
	<table id="table"   data-toolbar="#toolbar"></table>
	</div>
	</div>
	</div>
</body>
</html>
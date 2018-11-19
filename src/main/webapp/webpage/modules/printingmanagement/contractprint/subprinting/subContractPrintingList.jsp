<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>分包合同用章管理</title>
	<meta http-equiv="Content-type" content="text/html; charset=utf-8">
	<meta name="decorator" content="ani"/>
	<%@ include file="/webpage/include/bootstraptable.jsp"%>
	<%@include file="/webpage/include/treeview.jsp" %>
	<%@include file="subContractPrintingList.js" %>	
</head>
<body>
	<div class="wrapper wrapper-content">
	<div class="panel panel-primary">
	<div class="panel-heading">
		<h3 class="panel-title">分包合同用章管理列表</h3>
	</div>
	<div class="panel-body">
		<sys:message content="${message}"/>
	
	<!-- 搜索 -->
	<div class="accordion-group">
	<div id="collapseTwo" class="accordion-body collapse">
		<div class="accordion-inner">
			<form:form id="searchForm" modelAttribute="subContractPrinting" class="form form-horizontal well clearfix">
				<div class="col-xs-12 col-sm-6 col-md-4">
					<label class="label-item single-overflow pull-left" title="合同名称：">合同名称：</label>
					<form:input path="subProContract.subProContractName" htmlEscape="false" maxlength="64"  class=" form-control"/>
				</div>
				<div class="col-xs-12 col-sm-6 col-md-4">
					<label class="label-item single-overflow pull-left" title="人员名称：">用章人：</label>
					<form:input path="subProContract.user.name" htmlEscape="false" maxlength="64"  class=" form-control"/>
				</div>
				<div class="col-xs-12 col-sm-6 col-md-4">
				 <div class="form-group">
					<label class="label-item single-overflow pull-left" title="用章日期：">用章日期：</label>
					<div class="col-xs-12">
						   <div class="col-xs-12 col-sm-5">
					        	  <div class='input-group date' id='beginPrintDate' style="left: -10px;" >
					                   <input type='text'  name="beginPrintDate" id="beginDate"  class="form-control"  />
					                   <span class="input-group-addon">
					                       <span class="glyphicon glyphicon-calendar"></span>
					                   </span>
					             </div>	
					        </div>
					        <div class="col-xs-12 col-sm-1">
					        		~
					       	</div>
					        <div class="col-xs-12 col-sm-5">
					          	<div class='input-group date' id='endPrintDate' style="left: -10px;" >
					                   <input type='text'  name="endPrintDate" id="endDate"  class="form-control" />
					                   <span class="input-group-addon">
					                       <span class="glyphicon glyphicon-calendar"></span>
					                   </span>
					           	</div>	
					        </div>
					</div>
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
	</div>
	</div>
	
	<!-- 工具栏 -->
	<div id="toolbar">
		<shiro:hasPermission name="contractprint:subprinting:print">
			<button id="print" class="btn btn-info" disabled onclick="print()">
	           	<i class="glyphicon glyphicon-star"></i> 用章
	       	</button>
       	</shiro:hasPermission>
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
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>招标信息管理管理</title>
	<meta http-equiv="Content-type" content="text/html; charset=utf-8">
	<meta name="decorator" content="ani"/>
	<%@ include file="/webpage/include/bootstraptable.jsp"%>
	<%@include file="/webpage/include/treeview.jsp" %>
	<%@include file="tenderList.js" %>
</head>
<body>
	<div class="wrapper wrapper-content">
	<div class="panel panel-primary">
	<div class="panel-heading">
		<h3 class="panel-title">招标信息管理列表</h3>
	</div>
	<div class="panel-body">
		<sys:message content="${message}"/>
	
	<!-- 搜索 -->
<!-- 	<div class="accordion-group"> -->
<!-- 	<div id="collapseTwo" class="accordion-body collapse"> -->
		<div class="accordion-inner">
			<form:form id="searchForm" modelAttribute="tender" class="form form-horizontal well clearfix">
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="招标工程名称：">招标工程名称：</label>
				<form:input path="subpackageProgram.subpackageProgramName" htmlEscape="false" maxlength="64"  class=" form-control"/>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				 <div class="form-group">
					<label class="label-item single-overflow pull-left" title="开标时间：">&nbsp;开标时间：</label>
					<div class="col-xs-12">
						   <div class="col-xs-12 col-sm-5">
					        	  <div class='input-group date' id='beginOpenBidDate' style="left: -10px;" >
					                   <input type='text'  name="beginOpenBidDate" class="form-control"  />
					                   <span class="input-group-addon">
					                       <span class="glyphicon glyphicon-calendar"></span>
					                   </span>
					             </div>	
					        </div>
					        <div class="col-xs-12 col-sm-1">
					        		~
					       	</div>
					        <div class="col-xs-12 col-sm-5">
					          	<div class='input-group date' id='endOpenBidDate' style="left: -10px;" >
					                   <input type='text'  name="endOpenBidDate" class="form-control" />
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
<!-- 	</div> -->
<!-- 	</div> -->
	
	<!-- 工具栏 -->
	<div id="toolbar">
		<shiro:hasPermission name="tendermanage:tender:add">
			<a id="add" class="btn btn-primary" onclick="add()"><i class="glyphicon glyphicon-plus"></i> 新建</a>
		</shiro:hasPermission>
		<shiro:hasPermission name="tendermanage:tender:edit">
		    <button id="edit" class="btn btn-success" disabled onclick="edit()">
            	<i class="glyphicon glyphicon-edit"></i> 修改
        	</button>
		</shiro:hasPermission>
		<shiro:hasPermission name="tendermanage:tender:del">
			<button id="remove" class="btn btn-danger" disabled onclick="deleteAll()">
            	<i class="glyphicon glyphicon-remove"></i> 删除
        	</button>
		</shiro:hasPermission>
    </div>
		
	<!-- 表格 -->
	<table id="table"   data-toolbar="#toolbar"></table>
	</div>
	</div>
	</div>
</body>
</html>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>保证金审批管理</title>
	<meta http-equiv="Content-type" content="text/html; charset=utf-8">
	<meta name="decorator" content="ani"/>
	<%@ include file="/webpage/include/bootstraptable.jsp"%>
	<%@include file="/webpage/include/treeview.jsp" %>
	<%@include file="depositApprovalList.js" %>
</head>
<body>
	<div class="wrapper wrapper-content">
	<div class="panel panel-primary">
	<div class="panel-heading">
		<h3 class="panel-title">保证金审批列表</h3>
	</div>
	<div class="panel-body">
		<sys:message content="${message}"/>
	<!-- 搜索 -->
	<div class="accordion-group">
	<div id="collapseTwo" class="accordion-body collapse">
		<div class="accordion-inner">
			<form:form id="searchForm" modelAttribute="depositApproval" class="form form-horizontal well clearfix">
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="保证金名称：">保证金名称：</label>
				<form:input path="deposit.depositName" htmlEscape="false" maxlength="64"  class=" form-control"/>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="审批状态：">审批状态：</label>
				<form:select path="checkStatus"  class="form-control m-b">
					<form:option value="" label="--请选择审批状态--"/>
					<form:options items="${fns:getDictList('deposit_checkstatus')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="缴纳方式：">缴纳方式：</label>
				<form:select path="payWay"  class="form-control m-b">
					<form:option value="" label="--请选择缴纳方式--"/>
					<form:options items="${fns:getDictList('pay_way')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				 <div class="form-group">
					<label class="label-item single-overflow pull-left" title="出账日期：">&nbsp;出账日期：</label>
					<div class="col-xs-12">
						   <div class="col-xs-12 col-sm-5">
					        	  <div class='input-group date' id='beginStatementDate' style="left: -10px;" >
					                   <input type='text'  name="beginStatementDate" class="form-control"  />
					                   <span class="input-group-addon">
					                       <span class="glyphicon glyphicon-calendar"></span>
					                   </span>
					             </div>	
					        </div>
					        <div class="col-xs-12 col-sm-1">
					        		~
					       	</div>
					        <div class="col-xs-12 col-sm-5">
					          	<div class='input-group date' id='endStatementDate' style="left: -10px;" >
					                   <input type='text'  name="endStatementDate" class="form-control" />
					                   <span class="input-group-addon">
					                       <span class="glyphicon glyphicon-calendar"></span>
					                   </span>
					           	</div>	
					        </div>
					</div>
				</div>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				 <div class="form-group">
					<label class="label-item single-overflow pull-left" title="退还日期：">&nbsp;退还日期：</label>
					<div class="col-xs-12">
						   <div class="col-xs-12 col-sm-5">
					        	  <div class='input-group date' id='beginRefundDate' style="left: -10px;" >
					                   <input type='text'  name="beginRefundDate" class="form-control"  />
					                   <span class="input-group-addon">
					                       <span class="glyphicon glyphicon-calendar"></span>
					                   </span>
					             </div>	
					        </div>
					        <div class="col-xs-12 col-sm-1">
					        		~
					       	</div>
					        <div class="col-xs-12 col-sm-5">
					          	<div class='input-group date' id='endRefundDate' style="left: -10px;" >
					                   <input type='text'  name="endRefundDate" class="form-control" />
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
<%-- 			<shiro:hasPermission name="depositapproval:depositApproval:add"> --%>
				<a id="add" class="btn btn-primary" onclick="add()"><i class="glyphicon glyphicon-plus"></i> 新建</a>
<%-- 			</shiro:hasPermission> --%>
<%-- 			<shiro:hasPermission name="depositapproval:depositApproval:edit"> --%>
			    <button id="edit" class="btn btn-success" disabled onclick="edit()">
	            	<i class="glyphicon glyphicon-edit"></i> 修改
	        	</button>
<%-- 			</shiro:hasPermission> --%>
<%-- 				<shiro:hasPermission name="depositapproval:del"> --%>
					<button id="remove" class="btn btn-danger" disabled onclick="deleteAll()">
		            	<i class="glyphicon glyphicon-remove"></i> 删除
		        	</button>
<%-- 	        	</shiro:hasPermission> --%>
<%-- 	        	<shiro:hasPermission name="depositapproval:check"> --%>
		        	<button id="check" class="btn btn-info" disabled onclick="check()">
		            	<i class="glyphicon glyphicon-check"></i> 审批
		        	</button>
<%-- 	        	</shiro:hasPermission> --%>
	        	<button id="statement" class="btn btn-info" disabled onclick="statement()">
	            	<i class="glyphicon glyphicon-hand-up"></i> 确认出账
	        	</button>
<%-- 			</shiro:hasPermission> --%>
	        	<a class="accordion-toggle btn btn-default" data-toggle="collapse" data-parent="#accordion2" href="#collapseTwo">
					<i class="fa fa-search"></i> 检索
				</a>
		    </div>
		
	<!-- 表格 -->
	<table id="table"   data-toolbar="#toolbar"></table>

    <!-- context menu -->
<!--     <ul id="context-menu" class="dropdown-menu"> -->
<%--     	<shiro:hasPermission name="depositapproval:depositApproval:edit"> --%>
<!--         <li data-item="edit"><a>编辑</a></li> -->
<%--         </shiro:hasPermission> --%>
<%--         <shiro:hasPermission name="depositapproval:depositApproval:del"> --%>
<!--         <li data-item="delete"><a>删除</a></li> -->
<%--         </shiro:hasPermission> --%>
<!--         <li data-item="action1"><a>取消</a></li> -->
<!--     </ul>   -->
	</div>
	</div>
	</div>
</body>
</html>
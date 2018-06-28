<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>参投单位管理管理</title>
	<meta http-equiv="Content-type" content="text/html; charset=utf-8">
	<meta name="decorator" content="ani"/>
	<%@ include file="/webpage/include/bootstraptable.jsp"%>
	<%@include file="/webpage/include/treeview.jsp" %>
	<%@include file="bidcompanyList.js" %>
</head>
<body>
	<div class="wrapper wrapper-content">
	<div class="panel panel-primary">
	<div class="panel-heading">
		<h3 class="panel-title">参投单位管理列表</h3>
	</div>
	<div class="panel-body">
		<sys:message content="${message}"/>
	<!-- 搜索 -->
		 <div class="accordion-inner">
			<form:form id="searchForm" modelAttribute="bidcompany" class="form form-horizontal well clearfix">
			<div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="项目工程名称：">项目工程名称：</label>
				<form:input path="program.programName" htmlEscape="false" maxlength="64"  class=" form-control"/>
			</div>
			<div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="参投单位名称：">参投单位名称：</label>
				<form:input path="company.companyName" htmlEscape="false" maxlength="64" value="金卓" class=" form-control"/>
			</div>
			 <div class="col-xs-12 col-sm-6 col-md-4">
				 <div class="form-group">
					<label class="label-item single-overflow pull-left" title="投标时间：">&nbsp;投标时间：</label>
					<div class="col-xs-12">
						   <div class="col-xs-12 col-sm-5">
					        	  <div class='input-group date' id='beginBidDate' style="left: -10px;" >
					                   <input type='text'  name="beginBidDate" id="beginDate"  class="form-control"  />
<%-- 					                   <input type='text'  name="beginBidDate" id="beginDate" value="${bidcompany.queryBeginDate}" class="form-control"  /> --%>
					                   <span class="input-group-addon">
					                       <span class="glyphicon glyphicon-calendar"></span>
					                   </span>
					             </div>	
					        </div>
					        <div class="col-xs-12 col-sm-1">
					        		~
					       	</div>
					        <div class="col-xs-12 col-sm-5">
					          	<div class='input-group date' id='endBidDate' style="left: -10px;" >
					                   <input type='text'  name="endBidDate" id="endDate"  class="form-control" />
<%-- 					                   <input type='text'  name="endBidDate" id="endDate" value="${queryEndDate}" class="form-control" /> --%>
					                   <span class="input-group-addon">
					                       <span class="glyphicon glyphicon-calendar"></span>
					                   </span>
					           	</div>	
					        </div>
					</div>
				</div>	
			</div>
			<div class="col-xs-12 col-sm-6 col-md-4">
				<label class="label-item single-overflow pull-left" title="是否中标：">是否中标：</label>
				<form:select path="isBid"  class="form-control m-b">
					<form:option value="" label="----请选择是否中标----"/>
					<form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
			
		 <div class="col-xs-12 col-sm-6 col-md-4">
			<div style="margin-top:26px">
			  <a  id="search" class="btn btn-primary btn-rounded  btn-bordered btn-sm"><i class="fa fa-search"></i> 查询</a>
			  <a  id="reset" class="btn btn-primary btn-rounded  btn-bordered btn-sm" ><i class="fa fa-refresh"></i> 重置</a>
			  <a  id="openStatistics" class="btn btn-primary btn-rounded  btn-bordered btn-sm" ><i class="fa  fa-area-chart "></i> 数据统计 </a>
			 </div>
	    </div>	
	</form:form>
	</div>
	
	<!-- 工具栏 -->
	<div id="toolbar">
			<%-- <shiro:hasPermission name="companymanage:bidcompany:add"> --%>
				<a id="add" class="btn btn-primary" href="#" title="参投单位管理" onclick="add()"><i class="glyphicon glyphicon-plus"></i> 新建</a>
			<%-- </shiro:hasPermission>
			<shiro:hasPermission name="companymanage:bidcompany:edit"> --%>
			    <button id="edit" class="btn btn-success" disabled onclick="edit()">
	            	<i class="glyphicon glyphicon-edit"></i> 修改
	        	</button>
			<%-- </shiro:hasPermission>
			<shiro:hasPermission name="companymanage:bidcompany:del"> --%>
				<button id="remove" class="btn btn-danger" disabled onclick="deleteAll()">
	            	<i class="glyphicon glyphicon-remove"></i> 删除
	        	</button>
			<%-- </shiro:hasPermission>
			<shiro:hasPermission name="companymanage:bidcompany:import"> --%>
<!-- 				<button id="btnImport" class="btn btn-info"><i class="fa fa-folder-open-o"></i> 导入</button> -->
<!-- 				<div id="importBox" class="hide"> -->
<%-- 						<form id="importForm" action="${ctx}/companymanage/bidcompany/import" method="post" enctype="multipart/form-data" --%>
<!-- 							 style="padding-left:20px;text-align:center;" ><br/> -->
<!-- 							<input id="uploadFile" name="file" type="file" style="width:330px"/>导入文件不能超过5M，仅允许导入“xls”或“xlsx”格式文件！<br/>　　 -->
<!-- 						</form> -->
<!-- 				</div> -->
		    </div>
		
	<!-- 表格 -->
	<table id="table"   data-toolbar="#toolbar">
		
	</table>

    <!-- context menu -->
    <%-- <ul id="context-menu" class="dropdown-menu">
    	<shiro:hasPermission name="companymanage:bidcompany:edit">
        <li data-item="edit"><a>编辑</a></li>
        </shiro:hasPermission>
        <shiro:hasPermission name="companymanage:bidcompany:del">
        <li data-item="delete"><a>删除</a></li>
        </shiro:hasPermission>
        <li data-item="action1"><a>取消</a></li>
    </ul>  --%> 
	</div>
	</div>
	</div>
</body>
</html>
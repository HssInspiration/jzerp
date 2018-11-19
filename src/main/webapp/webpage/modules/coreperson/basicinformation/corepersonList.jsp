<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>人员管理</title>
	<meta http-equiv="Content-type" content="text/html; charset=utf-8">
	<meta name="decorator" content="ani"/>
	<%@ include file="/webpage/include/bootstraptable.jsp"%>
	<%@include file="/webpage/include/treeview.jsp" %>
	<%@ include file="corepersonList.js"%>
	<style>
		#left {
			 -webkit-transition: width 0.5s;
                transition: width 0.5s;      
		}
	</style>
</head>
<body>
	<div class="wrapper wrapper-content">
		<div class="panel panel-primary">
			<div class="panel-heading">
				<h3 class="panel-title">人员管理列表</h3>
			</div>
			<div class="panel-body">
				<sys:message content="${message}" />
				<div class="row">
					<div id="left" class="col-sm-12">
						<!-- 搜索 -->
						<div class="accordion-group">
							<div id="collapseTwo" class="accordion-body collapse">
								<div class="accordion-inner">
									<form:form id="searchForm" modelAttribute="corePerson"
										class="form form-horizontal well clearfix">
										<div class="col-xs-12 col-sm-6 col-md-4">
											<label class="label-item single-overflow pull-left"
												title="人员名称：">人员名称：</label> <input id="type"
												name="user.name" class="form-control m-b" />
										</div>
										<div class="col-xs-12 col-sm-6 col-md-4">
											<label class="label-item single-overflow pull-left"
												title="证书类型名称：">证书类型名称：</label>
											<form:select path="personCertificate.certificateName"
												class="form-control required">
												<form:option value="" label="--请选择证书类型名称--" />
												<form:options items="${fns:getDictList('certificate_type')}"
													itemLabel="label" itemValue="value" htmlEscape="false" />
											</form:select>
										</div>
										<div class="col-xs-12 col-sm-6 col-md-4">
											<div style="margin-top: 26px">
												<a id="search"
													class="btn btn-primary btn-rounded  btn-bordered btn-sm"><i
													class="fa fa-search"></i> 查询</a> <a id="reset"
													class="btn btn-primary btn-rounded  btn-bordered btn-sm"><i
													class="fa fa-refresh"></i> 重置</a>
											</div>
										</div>
									</form:form>
								</div>
							</div>
						</div>

						<!-- 工具栏 -->
						<div id="toolbar">
				    		<shiro:hasPermission name="basicinformation:add">
								<a id="add" class="btn btn-primary" onclick="add()"><i
									class="glyphicon glyphicon-plus"></i> 新建</a>
							</shiro:hasPermission>
							<shiro:hasPermission name="basicinformation:edit">
								<button id="edit" class="btn btn-success" disabled
									onclick="edit()">
									<i class="glyphicon glyphicon-edit"></i> 修改
								</button>
							</shiro:hasPermission>
							<shiro:hasPermission name="basicinformation:del">
								<button id="remove" class="btn btn-danger" disabled
									onclick="del()">
									<i class="glyphicon glyphicon-remove"></i> 删除
								</button>
							</shiro:hasPermission>
								<a class="accordion-toggle btn btn-default "
									data-toggle="collapse" data-parent="#accordion2"
									href="#collapseTwo"> <i class="fa fa-search"></i> 检索
								</a>
						</div>
						<!-- 工具栏结束 -->

						<!-- 表格 -->
						<table id="table" data-toolbar="#toolbar" data-id-field="id">
						</table>

						<!-- context menu -->
						<ul id="context-menu" class="dropdown-menu">
							<li data-item="edit"><a>编辑</a></li>
							<li data-item="delete"><a>删除</a></li>
							<li data-item="action1"><a>取消</a></li>
						</ul>
					</div>

					<div id="right" class="panel panel-default col-sm-6"
						style="display: none">
						<div class="panel-heading">
							<h3 class="panel-title">
								<label>证书列表,所属人员: </label><font id="corePersonLabel"></font><input
									type="hidden" id="corePersonId" />
							</h3>
						</div>
						<div class="panel-body">
							<div id="personCertificateToolbar">
								<button id="personCertificateButton"
									class="btn btn-outline btn-primary" title="添加证书">
									<i class="fa fa-plus-circle"></i> 添加证书
								</button>
							</div>
							<!-- 表格 -->
							<table id="personCertificateTable"
								data-toolbar="#personCertificateToolbar" data-id-field="id">
							</table>

						</div>
					</div>

				</div>
			</div>
		</div>
	</div>
</body>
</html>
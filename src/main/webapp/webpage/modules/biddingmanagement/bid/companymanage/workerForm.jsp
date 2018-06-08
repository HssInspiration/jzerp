<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>人员信息管理</title>
	<meta name="decorator" content="ani"/>
	<script type="text/javascript">
		var validateForm;
		var $table;// 父页面table表格id
		var $topIndex;//弹出窗口的 index
		function doSubmit(table, index){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
		  if(validateForm.form()){
			  $table = table;
			  $topIndex = index;
			  $("#inputForm").submit();
			  return true;
		  }
		  return false;
		} 

		$(document).ready(function() {
			validateForm =  $("#inputForm").validate({
				submitHandler : function(form) {
					jp.loading();
					jp.post("${ctx}/programmanage/enclosure/save",$('#inputForm').serialize(),function(data){
		                    if(data.success){
		                    	$table.bootstrapTable('refresh');//刷新表格
		                    	jp.success(data.msg);
		                    	jp.close($topIndex);//关闭dialog
		                    }else{
		        	  			jp.error(data.msg);
		                    }
		            });
					
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				}
			});
			
		});
		function returnManage(){
			
        	jp.close();//先关闭弹出层
        	
        	window.location = "${ctx}/bidmanage/bidtable/list";
        }
	</script>
</head>
<body>
<div class="wrapper wrapper-content">				
<div class="row">
	<div class="col-md-12">
	<div class="panel panel-primary">
		<div class="panel-heading">
			<h3 class="panel-title"> 
				<a class="panelButton" href="#" onclick="returnManage()"><i class="ti-angle-left"></i> 返回</a>
			</h3>
		</div>
		<div class="panel-body">
		<form:form id="inputForm" modelAttribute="worker" action="${ctx}/companymanage/worker/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>人员编号：</label>
					<div class="col-sm-10">
						<form:input path="workerNum" htmlEscape="false"    class="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>人员名称：</label>
					<div class="col-sm-10">
						<form:input path="workerName" htmlEscape="false"    class="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>人员职位：</label>
					<div class="col-sm-10">
						<form:select path="workerPosition" class="form-control required">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('position')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</div>
				</div>
		<c:if test="${fns:hasPermission('companymanage:worker:edit') || isAdd}">
				<div class="col-lg-3"></div>
		        <div class="col-lg-6">
		             <div class="form-group text-center">
		                 <div>
		                     <button class="btn btn-primary btn-block btn-lg btn-parsley" data-loading-text="正在提交...">提 交</button>
		                 </div>
		             </div>
		        </div>
		</c:if>
		</form:form>
		</div>				
	</div>
	</div>
</div>
</div>
</body>
</html>
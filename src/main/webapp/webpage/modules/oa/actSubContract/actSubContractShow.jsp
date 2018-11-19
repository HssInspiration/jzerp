<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>查看当前合同审批信息</title>
	<meta name="decorator" content="ani"/>
	<script type="text/javascript">
		var validateForm;
		function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
		  if(validateForm.form()){
			  jp.loading();
			  $("#inputForm").submit();
			  return true;
		  }
		  return false;
		}
		$(document).ready(function() {
			$("#name").focus();
			validateForm = $("#inputForm").validate({
				submitHandler: function(form){
// 					form.submit();
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
	</script>
</head>
<body>
<div class="wrapper wrapper-content">
<div class="row">
	<div class="col-md-12">
		<div class="panel panel-primary">
			<div class="panel-body">
				<div class="form-group text-center">
				 <h3>查看当前合同审批信息</h3>
				</div>
			<form:form id="inputForm" modelAttribute="actSubContract" action="${ctx}/oa/actSubContract/show" method="post" class="form-horizontal">
				<sys:message content="${message}"/>
					<div class="form-group">
						<label class="col-sm-2 control-label">标题：</label>
						<div class="col-sm-10">
							<form:input path="remarks" readonly="true" class="form-control" rows="5" maxlength="100"/>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label">合同名称：</label>
						<div class="col-sm-10">
							<input type="text" class="form-control" readonly="readonly" id="test_data" value = "${actSubContract.subProContract.subProContractName}">
						</div>
					</div>
					<act:flowChart procInsId="${actSubContract.act.procInsId}"/>
					<act:histoicFlow procInsId="${actSubContract.act.procInsId}"/>
			</form:form>
	</div>
</div>
</div>
</div>
</div>
</body>
</html>


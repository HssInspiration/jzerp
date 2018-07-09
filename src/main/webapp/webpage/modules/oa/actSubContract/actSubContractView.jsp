<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>总包合同（市场投标）审批</title>
	<meta name="decorator" content="ani"/>
	<link id="bscss" href="${ctxStatic}/common/css/bootstrap.min.css" rel="stylesheet">
	<script type="text/javascript">
		var validateForm;
		function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
		  if(validateForm.form()){
			  $("#inputForm").submit();
			  return true;
		  }
		  return false;
		}
		$(document).ready(function() {
			//从url请求项目集合，参数：项目名称-contractName
			$("#test_data").bsSuggest({
				  idField : "id\" style=\"display:none\"",
				  keyField : "word",
				  getDataMethod : "url",
				  url : "${ctx}/oa/attachContract/getAppointProContractByName?contractName=",
				  processData : function(json) {
				      var i, len, data = {
				          value : []
				      };
				      if (json.length == 0) {
				          return false
				      } 
				      len = json.length;
				      for (i = 0; i < len; i++) {
				          data.value.push({
				        	  "id\" style=\"display:none\"":json[i].id,
				              "word":json[i].contractName
			              })
			          }
			              return data
				  }
			});
			$("#name").focus();
			validateForm = $("#inputForm").validate({
				submitHandler: function(form){
					loading('正在提交，请稍等...');
					form.submit();
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
	<script src="${ctxStatic}/plugin/js/bootstrap-suggest.min.js"></script>
</head>
<body>
<div class="wrapper wrapper-content">
<div class="row">
	<div class="col-md-12">
		<div class="panel panel-primary">
			<div class="panel-heading">
				<h3 class="panel-title">
					<a class="panelButton" href="#"  onclick="history.go(-1)"><i class="ti-angle-left"></i> 返回</a>
				</h3>
			</div>
			<div class="panel-body">
				<div class="form-group text-center">
				 <h3>总包合同（市场投标）审批</h3>
				</div>
			<form:form id="inputForm" modelAttribute="actSubContract" action="${ctx}/oa/actSubContract/save" method="post" class="form-horizontal">
				<form:hidden path="id"/>
				<form:hidden path="act.taskId"/>
				<form:hidden path="act.taskName"/>
				<form:hidden path="act.taskDefKey"/>
				<form:hidden path="act.procInsId"/>
				<form:hidden path="act.procDefId"/>
				<form:hidden id="flag" path="act.flag"/>
				<sys:message content="${message}"/>
					<div class="form-group">
						<label class="col-sm-2 control-label">合同名称：</label>
						<div class="col-sm-10">
							<input type="text" class="form-control" readonly="readonly" id="test_data" value = "${actSubContract.subProContract.subProContractName}">
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label">备注：</label>
						<div class="col-sm-10">
							<form:textarea path="remarks" readonly="true" class="form-control" rows="5" maxlength="20"/>
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


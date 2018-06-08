<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>总包合同审批</title>
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
			
			
// 			$("#name").focus();
			validateForm = $("#inputForm").validate({
				submitHandler: function(form){
					jp.loading('正在提交，请稍等...');
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
				<form:form id="inputForm" modelAttribute="leave" action="${ctx}/oa/leave/save" method="post" class="form-horizontal">
					<form:hidden path="id"/>
					<sys:message content="${message}"/>
					<div class="form-group">
						<label class="col-sm-2 control-label">合同名称：</label>
						<div class="col-sm-10">
							
						</div>
					</div>
<!-- 					<div class="form-group"> -->
<!-- 						<label class="col-sm-2 control-label"><font color="red">*</font>开始时间：</label> -->
<!-- 						<div class="col-sm-10"> -->
<!-- 							<p class="input-group"> -->
<!-- 								<div class='input-group form_datetime' id='startTime'> -->
<!-- 									<input type='text'  name="startTime" class="form-control required"  pattern="yyyy-MM-dd HH:mm:ss"/> -->
<!-- 									<span class="input-group-addon"> -->
<!-- 										<span class="glyphicon glyphicon-calendar"></span> -->
<!-- 									</span> -->
<!-- 								</div> -->
<!-- 							</p> -->
<!-- 						</div> -->
<!-- 					</div> -->
<!-- 					<div class="form-group"> -->
<!-- 						<label class="col-sm-2 control-label"><font color="red">*</font>结束时间：</label> -->
<!-- 						<div class="col-sm-10"> -->
<!-- 							<p class="input-group"> -->
<!-- 								<div class='input-group form_datetime' id='endTime'> -->
<!-- 									<input type='text'  name="endTime" class="form-control required" pattern="yyyy-MM-dd HH:mm:ss"/> -->
<!-- 									<span class="input-group-addon"> -->
<!-- 										<span class="glyphicon glyphicon-calendar"></span> -->
<!-- 									</span> -->
<!-- 								</div> -->
<!-- 							</p> -->
<!-- 						</div> -->
<!-- 					</div> -->
					<div class="form-group">
						<label class="col-sm-2 control-label"><font color="red">*</font>备注信息：</label>
						<div class="col-sm-10">
							<form:textarea path="remarks" class="form-control required" rows="5" maxlength="20"/>
						</div>
					</div>
					<div class="col-sm-3"></div>
					<div class="col-sm-6">
						<div class="form-group text-center">
							<label></label>

							<div>
								<button type="submit" class="btn btn-primary btn-block btn-lg btn-parsley" data-loading-text="正在提交...">提 交</button>
							</div>
						</div>
					</div>
				</form:form>
				</div>
			</div>
		</div>
	</div>
</div>
</body>
</html>


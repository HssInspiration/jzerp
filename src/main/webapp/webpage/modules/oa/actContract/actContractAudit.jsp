<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>总包合同（市场投标）审批申请</title>
	<meta name="decorator" content="ani"/>
	<link id="bscss" href="${ctxStatic}/common/css/bootstrap.min.css" rel="stylesheet">
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
			//文档加载完毕验证附件是否为空，若不为空，显示对应附件
			var enclosureCont = $("#enclosureCont").val();
			if(enclosureCont != null){//附件内容值非空
				console.log("附件内容值："+enclosureCont);
				var array = enclosureCont.split("|");
				var valueArray = array.slice(1);//浅克隆去除数组第一个元素
				console.log(valueArray);
	        	var labelArray = [];
	        	for(var i = 0 ; i<valueArray.length; i++){
	        		if(!/\.(gif|jpg|jpeg|png|GIF|JPG|PNG)$/.test(valueArray[i])){
	        			labelArray[i] = "<a href=\""+valueArray[i]+"\" url=\""+valueArray[i]+"\" target=\"_blank\">"+decodeURIComponent(valueArray[i].substring(valueArray[i].lastIndexOf("/")+1))+"</a>"
	        		}else{
	        			labelArray[i] = '<img   onclick="jp.showPic(\''+valueArray[i]+'\')"'+' height="50px" src="'+valueArray[i]+'">';
	        		}
	        	}
	        	labelArray.join(" ");
	        	console.log("labelArray:"+labelArray);
	        	if(labelArray.length>0){
					var str = "";
					for(var i=0;i<labelArray.length;i++){
						str += "&nbsp;&nbsp;&nbsp;"+labelArray[i];
					}
					$("#enclosure").append(str);
				}
			}
			
			$("#name").focus();
			validateForm = $("#inputForm").validate({
				submitHandler: function(form){
// 					jp.loading('正在提交，请稍等...');
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
				 <h3>总包合同（市场投标）审批申请</h3>
				</div>
			<form:form id="inputForm" modelAttribute="actContract" action="${ctx}/oa/actContract/saveAudit" method="post" class="form-horizontal">
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
							<input type="text" class="form-control" readonly="readonly" id="test_data" value = "${actContract.proContract.contractName}">
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label">备注：</label>
						<div class="col-sm-10">
							<form:textarea path="remarks" readonly="true" class="form-control" rows="5" maxlength="20"/>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label">相关附件：</label>
						<div class="col-sm-10" id="enclosure">
							<form:hidden path="enclosureCont"  class="form-control" rows="5" maxlength="10000"/>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label"><font color="red">*</font>审批意见：</label>
						<div class="col-sm-10">
							<form:textarea path="act.comment" class="form-control required" rows="5" maxlength="20"/>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label"></label>
						<div class="col-sm-10">
							<c:if test="${actContract.act.taskDefKey ne 'apply_end'}">
								<input id="btnSubmit" class="btn btn-primary" type="submit" value="同 意" onclick="$('#flag').val('yes')"/>&nbsp;
								<input id="btnSubmit" class="btn btn-inverse" type="submit" value="驳 回" onclick="$('#flag').val('no')"/>&nbsp;
							</c:if>
						</div>
					</div>
		
					<act:flowChart procInsId="${actContract.act.procInsId}"/>
					<act:histoicFlow procInsId="${actContract.act.procInsId}"/>
			</form:form>
	</div>
</div>
</div>
</div>
</div>
</body>
</html>


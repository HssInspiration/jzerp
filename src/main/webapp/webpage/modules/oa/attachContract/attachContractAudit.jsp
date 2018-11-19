<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>总包合同（业主指定）审批申请</title>
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
			var enclosureCont = $("#enclosureCont").val();
			var contractTextCont = $("#contractTextCont").val();
			if(enclosureCont != null){//附件内容值非空
				console.log("附件内容值："+enclosureCont);
				var array = enclosureCont.split("|");
				console.log("array:"+array);
				var valueArray;
				if(array[0] == null){
					valueArray = array.slice(1);
				}else{
					valueArray = array;
				}
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
			if(contractTextCont != null){//合同正文内容值非空
				console.log("合同正文内容值："+contractTextCont);
				var contractText = ""; 
        		if(!/\.(gif|jpg|jpeg|png|GIF|JPG|PNG)$/.test(contractTextCont)){
        			contractText = "<a href=\""+contractTextCont+"\" url=\""+contractTextCont+"\" target=\"_blank\">"+decodeURIComponent(contractTextCont.substring(contractTextCont.lastIndexOf("/")+1))+"</a>"
        		}else{
        			contractText = '<img  onclick="jp.showPic(\''+contractTextCont+'\')"'+' height="50px" src="'+contractTextCont+'">';
        		}
	        	labelArray.join(" ");
				$("#text").append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+contractText);
			}
			
			$("#viewPdfText").bind("click",function(){
				console.log("事件绑定成功！");
				//发送ajax请求获取当前id对应的合同正文PDF版本
				var id = $("#id").val();
				console.log("id为："+id);
				var jsonData = JSON.stringify({"id":id});
				$.ajax({
					url:"${ctx}/oa/attachContract/exchangeWordToPdf",
					type:"post",
					data:jsonData,
					dataType:"json",
					contentType:"application/json;charset=utf-8",
					success:function(data){
						console.log("获取成功1:"+data);
						console.log("获取成功2:"+data.contractContToPdf);
						var urlStr = data.contractContToPdf.replace("D:\\jzerp_files\\","/ccpt/");
						window.open(urlStr);
					},
					error:function(){
						console.log("回调失败！");
					}
				});
			})
			
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
				<h3>总包合同（业主指定）审批申请</h3>
				</div>
			<form:form id="inputForm" modelAttribute="attachContract" action="${ctx}/oa/attachContract/saveAudit" method="post" class="form-horizontal">
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
							<input type="text" class="form-control" readonly="readonly" id="test_data" value = "${attachContract.proContract.contractName}">
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label">标题：</label>
						<div class="col-sm-10">
							<form:input path="remarks" readonly="true" class="form-control" rows="5" maxlength="100"/>
						</div>
					</div>
<!-- 					<div class="form-group"> -->
<!-- 						<label class="col-sm-2 control-label">合同正文：</label> -->
<!-- 						<div class="col-sm-10" id="text"> -->
<%-- 							<form:hidden path="contractTextCont"  class="form-control" rows="5" maxlength="10000"/> --%>
<!-- 						</div> -->
<!-- 					</div> -->
					<div class="form-group">
						<label class="col-sm-2 control-label">合同正文：</label>
						<div class="col-sm-3" id="text">
							<form:hidden path="contractTextCont"  class="form-control" rows="5" maxlength="10000"/>
						</div>
						<div class="col-sm-7">
							<input type="button" id="viewPdfText" value="在线查看合同正文"/>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label">相关附件：</label>
						<div class="col-sm-10" id="enclosure">
							<form:hidden path="enclosureCont"  class="form-control" rows="5" maxlength="10000"/>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label">审批意见：</label>
						<div class="col-sm-10">
							<form:textarea path="act.comment" class="form-control" rows="5" maxlength="20"/>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label"></label>
						<div class="col-sm-10">
							<c:if test="${attachContract.act.taskDefKey ne 'apply_end'}">
								<input id="btnSubmit" class="btn btn-primary" type="submit" value="同 意" onclick="$('#flag').val('yes')"/>&nbsp;
								<input id="btnSubmit" class="btn btn-inverse" type="submit" value="驳 回" onclick="$('#flag').val('no')"/>&nbsp;
							</c:if>
						</div>
					</div>
		
					<act:flowChart procInsId="${attachContract.act.procInsId}"/>
					<act:histoicFlow procInsId="${attachContract.act.procInsId}"/>
			</form:form>
	</div>
</div>
</div>
</div>
</div>
</body>
</html>


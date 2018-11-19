<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>合同正文管理</title>
	<meta name="decorator" content="ani"/>
	<script type="text/javascript">
		var validateForm;
		var $table; // 父页面table表格id
		var $topIndex;//弹出窗口的 index
		function doSubmit(table, index){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
		  if(validateForm.form()){
			  $table = table;
			  $topIndex = index;
			  jp.loading();
			  $("#inputForm").submit();
			  return true;
		  }
		  return false;
		}

		$(document).ready(function() {
			var contractTextCont = $("#contractTextCont").val();
			if(contractTextCont != null){//合同正文内容值非空
				//合同正文非空，且审批通过，打印带水印的文档	
				console.log("合同正文内容值："+contractTextCont);
				var contractText = ""; 
        		if(!/\.(gif|jpg|jpeg|png|GIF|JPG|PNG)$/.test(contractTextCont)){
        			contractText = "<a href=\""+contractTextCont+"\" url=\""+contractTextCont+"\" target=\"_blank\">"+decodeURIComponent(contractTextCont.substring(contractTextCont.lastIndexOf("/")+1))+"</a>"
        		}else{
        			contractText = '<img  onclick="jp.showPic(\''+contractTextCont+'\')"'+' height="50px" src="'+contractTextCont+'">';
        		}
				$("#text").append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+contractText);
			}
		});
	</script>
</head>
<body class="bg-white">
		<form:form id="inputForm" modelAttribute="contractText" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>
		<table class="table table-bordered">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right">合同正文：</label></td>
					<td class="width-35">
						<form:hidden  path="contractTextCont" htmlEscape="false" maxlength="64" class="form-control"/>
						<c:if test="${approvalStatus ne 2 }">
							<sys:ckfinder input="contractTextCont" readonly="true" type="files" uploadPath="/contractTextManage" selectMultiple="false"/>
						</c:if>
						<c:if test="${approvalStatus eq 2 }">
							<div class="col-sm-3" id="text">
								
							</div>
						</c:if>
					</td>
				</tr>
		 	</tbody>
		</table>
	</form:form>
</body>
</html>
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
			//文档加载时将状态设为有效
// 			$("#contractTextStatus1").parent().attr("class","iradio_square-blue checked")
			validateForm = $("#inputForm").validate({
				submitHandler: function(form){
					$("[disabled]").each(function() {//移除disable,后台方可取值  
				        if (parseInt($(this).val()) != -1) {  
				            $(this).attr("disabled", false);  
				        }  
				    });
					var contractTextStatus = $("input[type='radio']:checked").val();//获取选中按钮的值
					console.log("contractTextStatus："+contractTextStatus);
					if(contractTextStatus == 1 || contractTextStatus == undefined){
						jp.confirm('是否确认设置当前合同正文为有效合同正文？', function(){
							jp.loading();  	
							jp.post("${ctx}/contractTextManage/save",$('#inputForm').serialize(),function(data){
								if(data.success){
			                    	$table.bootstrapTable('refresh');
			                    	jp.success(data.msg);
			                    	jp.close($topIndex);//关闭dialog
		
			                    }else{
		            	  			jp.error(data.msg);
			                    }
							})
						})
					}else {
						jp.loading();  	
						jp.post("${ctx}/contractTextManage/save",$('#inputForm').serialize(),function(data){
							if(data.success){
		                    	$table.bootstrapTable('refresh');
		                    	jp.success(data.msg);
		                    	jp.close($topIndex);//关闭dialog
		                    }else{
	            	  			jp.error(data.msg);
		                    }
						})
					}
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
<body class="bg-white">
		<form:form id="inputForm" modelAttribute="contractText" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>
		<table class="table table-bordered">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>合同正文编号：</label></td>
					<td class="width-35">
						<form:input path="contractTextNum" readOnly="true" htmlEscape="false"  class="form-control required"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>合同正文名称：</label></td>
					<td class="width-35">
						<c:if test="${isAdd }">
							<form:input path="contractTextName" htmlEscape="false"  class="form-control required"/>
						</c:if>
						<c:if test="${edit }">
							<form:input path="contractTextName" readOnly="true" htmlEscape="false"  class="form-control required"/>
						</c:if>
					</td>
				</tr>
				<c:if test="${isAdd }">
					<form:hidden path="contractTextStatus" value="1" htmlEscape="false"  class="form-control required"/>
				</c:if>
				<c:if test="${edit}">
					<tr>
						<td class="width-15 active"><label class="pull-right"><font color="red">*</font>合同正文状态：</label></td>
						<td class="width-35">
							<form:radiobuttons path="contractTextStatus"  items="${fns:getDictList('contractText_status')}" itemLabel="label" itemValue="value" htmlEscape="false" class="i-checks required"/>
						</td>
					</tr>	
				</c:if>	
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>合同正文内容：</label></td>
					<td class="width-35">
						<form:hidden  path="contractTextCont" htmlEscape="false" maxlength="64" class="form-control required"/>
						<c:if test = "${isAdd}" >
							<sys:ckfinder input="contractTextCont" type="files" uploadPath="/contractTextManage" selectMultiple="false"/>
						</c:if>
						<c:if test = "${edit}" >
							<sys:ckfinder input="contractTextCont" readonly="true" type="files" uploadPath="/contractTextManage" selectMultiple="false"/>
						</c:if>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">备注信息：</label></td>
					<td class="width-35">		
						<form:textarea path="remarks"  htmlEscape="false"  rows="4" class="form-control "/>
						<c:if test = "${isAdd}">
							<c:if test="${not empty proContract.id}">
								<form:input path="contractId" type="hidden" value="${proContract.id}" class="form-control"/>
							</c:if>
							<c:if test="${not empty subProContract.id}">
								<form:input path="contractId" type="hidden" value="${subProContract.id}" class="form-control"/>
							</c:if>
						</c:if>
						
						<c:if test = "${edit}">
							<form:input path="contractId" type="hidden" class="form-control"/>
						</c:if>
					</td>
				</tr>
		 	</tbody>
		</table>
	</form:form>
</body>
</html>
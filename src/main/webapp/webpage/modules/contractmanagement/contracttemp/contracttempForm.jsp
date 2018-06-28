<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>合同模板管理</title>
	<meta name="decorator" content="ani"/>
	<link id="bscss" href="${ctxStatic}/common/css/bootstrap.min.css" rel="stylesheet">
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
			
			validateForm = $("#inputForm").validate({
				submitHandler: function(form){
					jp.post("${ctx}/contracttemp/save",$('#inputForm').serialize(),function(data){
						if(data.success){
	                    	$table.bootstrapTable('refresh');
	                    	jp.success(data.msg);
	                    	jp.close($topIndex);//关闭dialog

	                    }else{
            	  			jp.error(data.msg);
	                    }
					})
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
<body class="bg-white">
		<form:form id="inputForm" modelAttribute="contractTemp" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>
		<table class="table table-bordered">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>模板编号：</label></td>
					<td class="width-35">
						<form:input path="tempNum" htmlEscape="false" readOnly="true" value="${contractTemp.tempNum }"  class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>模板名称：</label></td>
					<td class="width-35">
						<form:input path="tempName" htmlEscape="false"    class="form-control "/>		
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>模板类型：</label></td>
					<td class="width-35">
						<c:if test="${isAdd}">
							<form:select path="tempType" class="form-control required">
								<form:option value="" label="--请选择模板类型--"/>
								<form:options items="${fns:getDictList('contract_temp_type')}"  itemLabel="label" itemValue="value"  htmlEscape="false"/>
							</form:select>	
						</c:if>
						<c:if test="${edit}">
							<form:select path="tempType" class="form-control required" disabled="true">
								<form:options items="${fns:getDictList('contract_temp_type')}"  itemLabel="label" itemValue="value"  htmlEscape="false"/>
							</form:select>	
						</c:if>
						
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">备注信息：</label></td>
					<td class="width-35">		
						<form:textarea path="remarks"  htmlEscape="false"  rows="4" class="form-control "/>
					</td>
				</tr>
		 	</tbody>
		</table>
	</form:form>
</body>
</html>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>附件信息管理管理</title>
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
			validateForm = $("#inputForm").validate({
				rules: {
					enclosureNum: {
						remote: "${ctx}/enclosuremanage/enclosuretab/checkEnclosureNum?enclosureOldNum=" + encodeURIComponent("${enclosuretab.enclosureNum}")},//设置了远程验证，在初始化时必须预先调用一次。
				},
				messages: {
					enclosureNum: {remote: "项目编号已存在"},
				},
				submitHandler: function(form){
					jp.post("${ctx}/enclosuremanage/enclosuretab/save",$('#inputForm').serialize(),function(data){
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
</head>
<body class="bg-white">
		<form:form id="inputForm" modelAttribute="enclosuretab" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
		<table class="table table-bordered">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>附件编号：</label></td>
					<td class="width-35">
						<input id="enclosureOldNum" name="enclosureOldNum" type="hidden" value="${enclosuretab.enclosureNum}">
						<form:input path="enclosureNum" htmlEscape="false"    class="form-control required"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>附件类型：</label></td>
					<td class="width-35">
						<form:select path="enclosureType" class="form-control required">
							<form:option value="" label="--请选择附件类型--"/>
							<form:options items="${fns:getDictList('enclosuretype')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">附件内容：</label></td>
					<td class="width-35">
						<form:hidden  path="enclosureCont" htmlEscape="false" maxlength="64" class="form-control"/>
						<sys:ckfinder input="enclosureCont" type="files" uploadPath="/enclosuremanage/enclosuretab" selectMultiple="true"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">备注信息：</label></td>
					<td class="width-35">
					<c:if test = "${isAdd}">
						<c:if test="${not empty program.id}">
							<form:input path="foreginId" type="hidden" class="form-control" value = "${program.id}"/>
						</c:if>
						
						<c:if test="${not empty bidtable.id}">
							<form:input path="foreginId" type="hidden" class="form-control" value = "${bidtable.id}"/>
						</c:if>
						
						<c:if test="${not empty bidCompany.id}">
							<form:input path="foreginId" type="hidden" class="form-control" value = "${bidCompany.id}"/>
						</c:if>
						
						<c:if test="${not empty subpackageProgram.id}">
							<form:input path="foreginId" type="hidden" class="form-control" value = "${subpackageProgram.id}"/>
						</c:if>
						
						<c:if test="${not empty tender.id}">
							<form:input path="foreginId" type="hidden" class="form-control" value = "${tender.id}"/>
						</c:if>
						
						<c:if test="${not empty subBidCompany.id}">
							<form:input path="foreginId" type="hidden" class="form-control" value = "${subBidCompany.id}"/>
						</c:if>
						
						<c:if test="${not empty clearEvaluate.id}">
							<form:input path="foreginId" type="hidden" class="form-control" value = "${clearEvaluate.id}"/>
						</c:if>
						
						<c:if test="${not empty proContract.id}">
							<form:input path="foreginId" type="hidden" class="form-control" value = "${proContract.id}"/>
						</c:if>
						
						<c:if test="${not empty subProContract.id}">
							<form:input path="foreginId" type="hidden" class="form-control" value = "${subProContract.id}"/>
						</c:if>
						
						<c:if test="${not empty proContractApproval.id}">
							<form:input path="foreginId" type="hidden" class="form-control" value = "${proContractApproval.id}"/>
						</c:if>
						
						<c:if test="${not empty personCertificate.id}">
							<form:input path="foreginId" type="hidden" class="form-control" value = "${personCertificate.id}"/>
						</c:if>
						
						<c:if test="${not empty contractTemp.id}">
							<form:input path="foreginId" type="hidden" class="form-control" value = "${contractTemp.id}"/>
						</c:if>
					</c:if>
					<c:if test = "${edit}">
						<form:input path="foreginId" type="hidden" class="form-control" value = "${enclosuretab.foreginId}"/>
					</c:if>
						<form:textarea path="remarks" htmlEscape="false" rows="4"    class="form-control "/>
					</td>
				</tr>
		 	</tbody>
		</table>
	</form:form>
</body>
</html>
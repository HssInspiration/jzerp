<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>总包合同管理</title>
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
			//从url请求项目集合，参数：合同名称-contractName
			validateForm = $("#inputForm").validate({
				submitHandler: function(form){
				 	$("[disabled]").each(function() {//移除disable,后台方可取值  
				        if (parseInt($(this).val()) != -1) {  
				            $(this).attr("disabled", false);  
				        }  
				    }); 
					jp.post("${ctx}/contractprint/subprinting/save",$('#inputForm').serialize(),function(data){
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
			$('#printDate').datetimepicker({
				 format: "YYYY-MM-DD HH:mm:ss"
		    });
		});
	</script>
</head>
<body class="bg-white">
		<form:form id="inputForm" modelAttribute="subContractPrinting" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>
		<table class="table table-bordered">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>用章编号：</label></td>
					<td class="width-35">
						<form:input path="printNum" htmlEscape="false" readOnly="true" value="${contractPrinting.printNum }"  class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>类&nbsp;型：</label></td>
					<td class="width-35">
						<form:select path="printType" class="form-control required" disabled="true" >
							<form:option value="" label="--请选择类型--"/>
							<form:options items="${fns:getDictList('print_type')}" itemLabel="label" itemValue="value"  htmlEscape="false"/>
						</form:select>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>合同名称：</label></td>
					<td class="width-35">
						<input type="hidden" class="form-control" name= "subProContract" value = "${subContractPrinting.subProContract.id}">
						<form:input path="subProContract.subProContractName" htmlEscape="false" readOnly="true" class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>用章类别：</label></td>
					<td class="width-35">
						<sys:checkbox id="printDetailType" name="printDetailType"  items="${fns:getDictList('print_detail_type')}" values="${contractPrinting.printDetailType}" cssClass="i-checks required"/>
					</td>
<!-- 					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>用章日期：</label></td> -->
<!-- 					<td class="width-35"> -->
<!-- 						<div class='input-group form_datetime' id='printDate'> -->
<%-- 		                    <input type='text'  name="printDate" class="form-control required"  value="<fmt:formatDate value="${subContractPrinting.printDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/> --%>
<!-- 		                    <span class="input-group-addon"> -->
<!-- 		                        <span class="glyphicon glyphicon-calendar"></span> -->
<!-- 		                    </span> -->
<!-- 		                </div>	 -->
<!-- 					</td> -->
				</tr>
		 	</tbody>
		</table>
	</form:form>
</body>
</html>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>合同统计管理</title>
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
</head>
<body class="bg-white">
		<form:form id="inputForm" modelAttribute="proContractStatistics" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>
		<table class="table table-bordered">
		   <tbody>
		   		<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>总包合同数量(个)：</label></td>
					<td class="width-35">	
						<form:input path="proContractCount" htmlEscape="false" placeholder="单位：个" class="form-control "/>			
					</td>
				</tr>
				
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>总包合同总价(万元)：</label></td>
					<td class="width-35">
						<form:input path="totalProContractPrice" htmlEscape="false" placeholder="单位：万元" class="form-control "/>
					</td>
				</tr>
				
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>市场投标总价(万元)：</label></td>
					<td class="width-35">
						<form:input path="marketProContractPrice" htmlEscape="false" placeholder="单位：万元" class="form-control "/>		
					</td>
				</tr>
				
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>业主指定总价(万元)：</label></td>
					<td class="width-35">
						<form:input path="appointProContractPrice" htmlEscape="false" placeholder="单位：万元" class="form-control "/>		
					</td>
				</tr>
				
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>生效总包合同数量(个)：</label></td>
					<td class="width-35">	
						<form:input path="effectProContractCount" htmlEscape="false" placeholder="单位：个" class="form-control "/>			
					</td>
				</tr>
				
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>生效总包合同总价(万元)：</label></td>
					<td class="width-35">	
						<form:input path="effectProContractPrice" htmlEscape="false" placeholder="单位：万元" class="form-control "/>			
					</td>
				</tr>
				
<!-- 				<tr> -->
<!-- 					<td class="width-15 active"><label class="pull-right">未生效总包合同数量：</label></td> -->
<!-- 					<td class="width-35">	 -->
<%-- 						<form:input path="notEffectProContractCount" htmlEscape="false"  class="form-control "/>			 --%>
<!-- 					</td> -->
<!-- 				</tr> -->
<!-- 				<tr> -->
<!-- 					<td class="width-15 active"><label class="pull-right">未生效总包合同总价：</label></td> -->
<!-- 					<td class="width-35">	 -->
<%-- 						<form:input path="notEffectProContractPrice" htmlEscape="false"  class="form-control "/>			 --%>
<!-- 					</td> -->
<!-- 				</tr> -->
				
<!-- 				<tr> -->
<!-- 					<td class="width-15 active"><label class="pull-right">市场招标且生效总价：</label></td> -->
<!-- 					<td class="width-35">	 -->
<%-- 						<form:input path="marketAndEffectedPrice" htmlEscape="false" class="form-control "/>			 --%>
<!-- 					</td> -->
<!-- 				</tr> -->
<!-- 				<tr> -->
<!-- 					<td class="width-15 active"><label class="pull-right">业主指定且生效总价：</label></td> -->
<!-- 					<td class="width-35">	 -->
<%-- 						<form:input path="marketAndEffectedPrice" htmlEscape="false"    class="form-control "/>			 --%>
<!-- 					</td> -->
<!-- 				</tr> -->
		 	</tbody>
		</table>
	</form:form>
</body>
</html>
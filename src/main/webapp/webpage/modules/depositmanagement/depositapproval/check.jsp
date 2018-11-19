<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>保证金审批管理</title>
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
					jp.post("${ctx}/depositApproval/save",$('#inputForm').serialize(),function(data){
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
		<form:form id="inputForm" modelAttribute="depositApproval" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
		<table class="table table-bordered">
		   <tbody>
	   		    <c:if test="${depositApproval.checkClass eq 1 
					   		   || depositApproval.checkClass eq 2
					   		   || depositApproval.checkClass eq 3
					   		   || depositApproval.checkClass eq 4
					   		   || depositApproval.checkClass eq 5 }">
					<tr>
						<td class="width-15 active"><label class="pull-right">经办人(审批)：</label></td>
						<td class="width-35">
<%-- 							<form:radiobuttons path="operator"  --%>
<%-- 								items="${fns:getDictList('check_deposit')}"  --%>
<%-- 								itemLabel="label" itemValue="value" --%>
<%-- 								htmlEscape="false" class="i-checks required"/> --%>

 							<c:if test="${num1 ne 1}"> 
 								<form:radiobuttons path="operator"  
 								items="${fns:getDictList('check_deposit')}"   
 								itemLabel="label" itemValue="value"  
  								disabled="true"  
  								htmlEscape="false" class="i-checks required"/>  
  							</c:if>   
 							<c:if test="${num1 eq 1}"> 
 								<form:radiobuttons path="operator"  
 								items="${fns:getDictList('check_deposit')}"  
 								itemLabel="label" itemValue="value" 
								htmlEscape="false" class="i-checks required"/>
 							</c:if>  
						</td>
					</tr>
				</c:if>
				
				<c:if test="${depositApproval.checkClass eq 2 
					           || depositApproval.checkClass eq 3
		   		               || depositApproval.checkClass eq 4
		   		    		   || depositApproval.checkClass eq 5}">
		   		    <tr>
						<td class="width-15 active"><label class="pull-right">分管负责人(审批)：</label></td>
						<td class="width-35">
							
							<c:if test="${num2 ne 2 || empty depositApproval.operator}">
	 								<form:radiobuttons path="managingDirector"  
	 								items="${fns:getDictList('check_deposit')}"
	 								itemLabel="label" itemValue="value" disabled="true"
									htmlEscape="false" class="i-checks"/>
							</c:if>
							<c:if test="${num2 eq 2 && not empty depositApproval.operator}">
 								<form:radiobuttons path="managingDirector"  
 								items="${fns:getDictList('check_deposit')}"  
 								itemLabel="label" itemValue="value"  
								htmlEscape="false" class="i-checks "/>
							</c:if>
						</td>
					</tr>
				</c:if>
				
				<c:if test="${depositApproval.checkClass eq 3  
							   || depositApproval.checkClass eq 4
		   		    		   || depositApproval.checkClass eq 5}">
 		    		<tr>
						<td class="width-15 active"><label class="pull-right">总经理(审批)：</label></td>
						<td class="width-35">
							<c:if test="${num3 ne 3 || empty depositApproval.operator || empty depositApproval.managingDirector }">
								<form:radiobuttons path="topManager"  
								items="${fns:getDictList('check_deposit')}"  
								itemLabel="label" itemValue="value"  disabled="true"
								htmlEscape="false" class="i-checks "/> 
							</c:if>
							<c:if test="${num3 eq 3 && not empty depositApproval.operator 
													&& not empty depositApproval.managingDirector}">
 								<form:radiobuttons path="topManager"  
 								items="${fns:getDictList('check_deposit')}"  
 								itemLabel="label" itemValue="value"  
 								htmlEscape="false" class="i-checks "/> 
							</c:if>
						</td>
					</tr>
				</c:if>
				<c:if test="${depositApproval.checkClass eq 4 || depositApproval.checkClass eq 5}">
					<tr>
						<td class="width-15 active"><label class="pull-right">董事长(审批)：</label></td>
						<td class="width-35">
 							<c:if test="${num4 ne 4 || empty depositApproval.operator
													|| empty depositApproval.managingDirector
													|| empty depositApproval.topManager}"> 
	 								<form:radiobuttons path="chairman"  
	 								items="${fns:getDictList('check_deposit')}"  
	 								itemLabel="label" itemValue="value" disabled="true"
	 								htmlEscape="false" class="i-checks "/>
							</c:if>
 							<c:if test="${num4 eq 4 && not empty depositApproval.operator
													&& not empty depositApproval.managingDirector
													&& not empty depositApproval.topManager}"> 
 								<form:radiobuttons path="chairman"  
 								items="${fns:getDictList('check_deposit')}"  
 								itemLabel="label" itemValue="value"  
 								htmlEscape="false" class="i-checks "/> 
							</c:if>
						</td>
					</tr>
				</c:if>
				
				<c:if test="${depositApproval.checkClass eq 5 }">
					<tr>
						<td class="width-15 active"><label class="pull-right">集团董事长(审批)：</label></td>
						<td class="width-35">
 							<c:if test="${num5 ne 5 || empty depositApproval.operator
													|| empty depositApproval.managingDirector
													|| empty depositApproval.topManager
													|| empty depositApproval.chairman}"> 
 								<form:radiobuttons path="groupChairman" 
								items="${fns:getDictList('check_deposit')}"  
 								itemLabel="label" itemValue="value" disabled="true"
 								htmlEscape="false" class="i-checks "/> 
 							</c:if> 
 							<c:if test="${num5 eq 5 && not empty depositApproval.operator
													&& not empty depositApproval.managingDirector
													&& not empty depositApproval.topManager
													&& not empty depositApproval.chairman}"> 
 								<form:radiobuttons path="groupChairman" 
								items="${fns:getDictList('check_deposit')}"  
 								itemLabel="label" itemValue="value"   
 								htmlEscape="false" class="i-checks "/> 
 							</c:if> 
						</td>
					</tr>
				</c:if>
		   </tbody>
		</table>
	</form:form>
</body>
</html>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>保证金出账记录管理</title>
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
			
			//从url请求项目集合，参数：项目名称-programName
			$("#test_data").bsSuggest({
				  idField : "id\" style=\"display:none\"",
				  keyField : "word",
				  getDataMethod : "url",
				  url : "${ctx}/depositStatement/getDepositApprovalList?depositName=",
				  processData : function(json) {
					  console.log(json);
				      var i, len, data = {
				          value : []
				      };
				      if (json.length == 0) {
				          return false
				      } 
				      len = json.length;
				      for (i = 0; i < len; i++) {
				          data.value.push({
				        	  "id\" style=\"display:none\"":json[i].id,
				              "word":json[i].deposit.depositName
				              })
				          }
				          return data
				  }
			  });
			
			validateForm = $("#inputForm").validate({
				submitHandler: function(form){
					var depositApprovalId = $("#test_data").attr("data-id");
	 				if(depositApprovalId != null && depositApprovalId != ""){
	 					$("#depositApprovalId").val(depositApprovalId);
	 				}
					jp.post("${ctx}/depositStatement/save",$('#inputForm').serialize(),function(data){
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
			
	        $('#statementDate').datetimepicker({
				 format: "YYYY-MM-DD HH:mm:ss"
		    });
		});
	</script>
	<script src="${ctxStatic}/plugin/js/bootstrap-suggest.min.js"></script>
</head>
<body class="bg-white">
		<form:form id="inputForm" modelAttribute="depositStatement" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
		<table class="table table-bordered">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right">出账编号：</label></td>
					<td class="width-35">
						<form:input path="statementNum" htmlEscape="false"  readOnly="true"  class="form-control "/>
					</td>
					
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>保证金名称：</label></td>
					<td class="width-35">
						<c:if test="${isAdd}">
							<input type="hidden" class="form-control" name= "depositApproval.id" id ="depositApprovalId" value = "${depositStatement.depositApproval.id}">
							<div class="row">
				                <div class="col-lg-2">
				                    <div class="input-group">
				                        <input type="text" class="form-control requied" id="test_data"  value = "${depositStatement.depositApproval.deposit.depositName}">
				                        <div class="input-group-btn">
				                            <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
				                                <span class="caret"></span>
				                            </button>
				                            <ul class="dropdown-menu dropdown-menu-right" role="menu">
				                            </ul>
				                        </div>
				                    </div>
				                </div>
				            </div>
			            </c:if>
			            <c:if test="${edit}">
			            	<input type="hidden" class="form-control" name= "depositApproval.id" id ="depositApprovalId" value = "${depositStatement.depositApproval.id}">
			            	<input type="text" class="form-control" readOnly="true" value = "${depositStatement.depositApproval.deposit.depositName}">
			            </c:if>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>领票人：</label></td>
					<td class="width-35">
						<form:input path="ticketHolder" htmlEscape="false"    class="form-control required"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>汇票时间：</label></td>
					<td class="width-35">
						<div class='input-group form_datetime' id='statementDate'>
		                    <input type='text'  name="statementDate" class="form-control required"  value="<fmt:formatDate value="${depositStatement.statementDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
		                    <span class="input-group-addon">
		                        <span class="glyphicon glyphicon-calendar"></span>
		                    </span>
		                </div>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">备注信息：</label></td>
					<td class="width-35">
						<form:textarea path="remarks" htmlEscape="false" rows="4"    class="form-control "/>
					</td>
				</tr>
		 	</tbody>
		</table>
	</form:form>
</body>
</html>
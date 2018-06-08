<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>保证金催退管理</title>
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
// 				  showBtn: false,     //不显示下拉按钮
// 			      delayUntilKeyup: true, //获取数据的方式为 firstByUrl 时，延迟到有输入/获取到焦点时才请求数据
				  idField : "id\" style=\"display:none\"",
				  keyField : "word",
				  getDataMethod : "url",
				  url : "${ctx}/depositReturn/getDepositStatementList?depositName=",
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
				              "word":json[i].depositApproval.deposit.depositName
				              })
				          }
				          return data
				  }
			  });
			
			$("#test_data").on('onSetSelectValue', function (e, keyword) {
				console.log('onSetSelectValue: ', keyword);
				//思路：
				//1.下拉变化时获取id;
				var id = keyword.id;
				console.log(id);
				var jsonData = JSON.stringify({"id":id});
				//2.获取完id传入后台获取对应的数据;
				$.ajax({
					url:"${ctx}/depositReturn/getDepositStatementById",
// 					async:false,//设置为同步获取数据
					data:jsonData,
					type:"post",
					contentType:"application/json;charset=utf-8",
					dataType:"json",
					success:function(data){
						console.log("获取成功"+data);
						if(data==true){
							$("#repeatObj").html("×当前保证金已催退，请重新选择！");
							$("#repeatObj").css("color","red");
						}else if(data==false){
							$("#repeatObj").html("√OK!");
							$("#repeatObj").css("color","green");
						}
					},
					error:function(){
						console.log("获取失败！");
					}
				});
			});//下拉框值选中时调用
			
			validateForm = $("#inputForm").validate({
				submitHandler: function(form){
					var depositStatementId = $("#test_data").attr("data-id");
	 				if(depositStatementId != null && depositStatementId != ""){
	 					$("#depositStatementId").val(depositStatementId);
	 				}
					jp.post("${ctx}/depositReturn/save",$('#inputForm').serialize(),function(data){
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
			
	        $('#returnDate').datetimepicker({
				 format: "YYYY-MM-DD HH:mm:ss"
		    });
		});
	</script>
	<script src="${ctxStatic}/plugin/js/bootstrap-suggest.min.js"></script>
</head>
<body class="bg-white">
		<form:form id="inputForm" modelAttribute="depositReturn" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
		<table class="table table-bordered">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>催退编号：</label></td>
					<td class="width-35">
						<form:input path="returnNum" htmlEscape="false"  readOnly="true"   class="form-control required"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>保证金名称：</label></td>
					<td class="width-35">
						<c:if test="${isAdd }">
						<input type="hidden" class="form-control" name= "depositStatement.id" id ="depositStatementId" value = "${depositReturn.depositStatement.id}">
						<div class="row">
			                <div class="col-lg-2">
			                    <div class="input-group">
			                        <input type="text" class="form-control required" id="test_data"   value = "${depositReturn.depositStatement.depositApproval.deposit.depositName}">
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
			            <c:if test="${edit }">
			            	<input type="hidden" class="form-control" name= "depositStatement.id" id ="depositStatementId" value = "${depositReturn.depositStatement.id}">
	                        <input type="text" class="form-control required" id="test_data" readOnly="true" value = "${depositReturn.depositStatement.depositApproval.deposit.depositName}">
			            </c:if>
			            <span id="repeatObj" style = "font-weight:bold;"></span>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>催退人名称：</label></td>
					<td class="width-35">
						<form:input path="returnName" htmlEscape="false"    class="form-control required"/>
					</td>
				</tr>
				
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>催退人号码：</label></td>
					<td class="width-35">
						<form:input path="returnNumber" htmlEscape="false"    class="form-control required"/>
					</td>
				</tr>
				
				<tr>
<!-- 					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>用途(类型)：</label></td> -->
<!-- 					<td class="width-35"> -->
<%-- 						<c:if test="${isAdd}"> --%>
<%-- 							<form:select path="depositStatement.depositApproval.deposit.depositType" class="form-control required" id="depositType"> --%>
<%-- 								<form:option value="" label="--请选择用途(类型)--"/> --%>
<%-- 							</form:select> --%>
<%-- 						</c:if> --%>
<%-- 						<c:if test="${edit}"> --%>
<%-- 							<form:select path="depositStatement.depositApproval.deposit.depositType" disabled="true" class="form-control required" id="depositType"> --%>
<%-- 								<form:options items="${fns:getDictList('deposit_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/> --%>
<%-- 							</form:select> --%>
<%-- 						</c:if> --%>
<!-- 					</td> -->
					<td  class="width-15 active">	<label class="pull-right"><font color="red">*</font>是否退回:</label></td>
			        <c:if test="${isAdd}">
			          <td  class="width-35" >
			            <form:radiobuttons path="isReturn"  items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false" class="i-checks required"/>
			          </td>
			        </c:if>
		            <c:if test="${edit}">
			          <td  class="width-35" >
			            <form:radiobuttons path="isReturn" disabled="true" items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false" class="i-checks required"/>
			          </td>
			        </c:if>
				</tr>
				<tr>
<!-- 					<td class="width-15 active"><label class="pull-right">催退详情描述：</label></td> -->
<!-- 					<td class="width-35"> -->
<%-- 						<form:textarea path="returnCont" htmlEscape="false" rows="4"    class="form-control "/> --%>
<!-- 					</td> -->
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
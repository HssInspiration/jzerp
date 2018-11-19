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
			//从url请求项目集合，参数：项目名称-programName
			$("#test_data").bsSuggest({
				  idField : "id\" style=\"display:none\"",
				  keyField : "word",
				  getDataMethod : "url",
				  url : "${ctx}/depositApproval/getDepositList?depositName=",
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
				              "word":json[i].depositName
				              })
				          }
				          return data
				  }
			  });
			
// 			$("#depositType").bind("change",function(){//选择改变后先做验证，是否已经登记
// 				var depositId = $("#test_data").attr("data-id");//所属项目id
// 				var value = $("#depositType").val();//类型
// 				console.log(value);
// 				var str,newNum;
// 				var jsonData = JSON.stringify({"id":depositId});//保证金id
// 				//发送验证：
// 				$.ajax({
// 					url:"${ctx}/depositApproval/getTypeByDepositId",
// 					data:jsonData,
// 					type:"post",
// 					contentType:"application/json;charset=utf-8",
// 					dataType:"json",
// 					success:function(data){
// 						console.log(data);
// 						console.log(data.length);
// 						for(var i=0;i<=data.length-1;i++){
// 							console.log("00000:"+data[i]);
// 							if(data[i]==value){
// 								console.log("类别重复！");
// 								$("#typeAlreadyExsist").html("×当前用途(类型)已存在，请重新选择！");
// 								$("#typeAlreadyExsist").css("color","red");
// 								break;
// 							}else{
// 								$("#typeAlreadyExsist").html("√OK!");
// 								$("#typeAlreadyExsist").css("color","green");
// 							}
// 						}
// 					},
// 					error:function(){
// 						console.log("失败！");
// 					}
// 				});
// 			});
			
			
		    $("#test_data").on('onSetSelectValue', function (e, keyword) {
				console.log('onSetSelectValue: ', keyword);
				//思路：
				//1.下拉变化时获取id;
				var id = keyword.id;
				console.log(id);
				var jsonData = JSON.stringify({"id":id});
				//2.获取完id传入后台获取对应的数据;
				$.ajax({
					url:"${ctx}/depositApproval/getDepositById",
// 					async:false,//设置为同步获取数据
					data:jsonData,
					type:"post",
					contentType:"application/json;charset=utf-8",
					dataType:"json",
					success:function(data){
						console.log("获取成功"+data);
						if(data==true){
							$("#repeatObj").html("×当前保证金已登记，请重新选择！");
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
				//催退时间处理
			 });//下拉框值选中时调用
			
			validateForm = $("#inputForm").validate({
				messages: {
					confirmReceiverAccount: {equalTo: "输入与上面相同的账号！"}
				},
				submitHandler: function(form){
	 				var depositId = $("#test_data").attr("data-id");
	 				if(depositId != null && depositId != ""){
	 					$("#depositId").val(depositId);
	 				}
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
			
	        $('#statementDate').datetimepicker({
				 format: "YYYY-MM-DD HH:mm:ss"
		    });
	        
	        $('#refundDate').datetimepicker({
				 format: "YYYY-MM-DD HH:mm:ss"
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
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>审批编号：</label></td>
					<td class="width-35">
						<form:input path="approvalNum" htmlEscape="false"  readOnly="true"  class="form-control required"/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>缴纳方式：</label></td>
					<td class="width-35">
						<form:select path="payWay" class="form-control required">
							<form:option value="" label="--请选择缴纳方式--"/>
							<form:options items="${fns:getDictList('pay_way')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>保证金名称：</label></td>
					<td class="width-35">
						<c:if test="${isAdd }">
						<input type="hidden" class="form-control" name= "deposit.id" id ="depositId" value = "${depositApproval.deposit.id}">
						<div class="row">
			                <div class="col-lg-2">
			                    <div class="input-group">
			                        <input type="text" class="form-control required" id="test_data"   value = "${depositApproval.deposit.depositName}">
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
			           		<input type="hidden" class="form-control" name= "deposit.id" id ="depositId" value = "${depositApproval.deposit.id}">
			            	<input type="text" class="form-control" readOnly="true"  value = "${depositApproval.deposit.depositName}">
			            </c:if>
			            <span id="repeatObj" style = "font-weight:bold;"></span>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>收款人：</label></td>
					<td class="width-35">
						<form:input path="receiver" htmlEscape="false"    class="form-control required"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>汇款账户：</label></td>
					<td class="width-35">
						<input id="remittanceAccount" name="remittanceAccount" type="text" value="${depositApproval.remittanceAccount }" maxlength="30" minlength="10" class="form-control required number"/>
<%-- 						<form:input path="remittanceAccount" htmlEscape="false"  maxlength="30" minlength="10"  class="form-control required"/> --%>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>汇款银行：</label></td>
					<td class="width-35">
						<form:input path="remittanceBank" htmlEscape="false"    class="form-control required"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>收款账户：</label></td>
					<td class="width-35">
						<input id="receiverAccount" name="receiverAccount" type="text" value="${depositApproval.receiverAccount }" maxlength="30" minlength="10" class="form-control required number"/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>收款银行：</label></td>
					<td class="width-35">
						<form:input path="receiverBank" htmlEscape="false"  class="form-control required"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>确认收款账户：</label></td>
					<td class="width-35">
						<input id="confirmReceiverAccount" onpaste="return false" name="confirmReceiverAccount" type="text" value="" maxlength="30" minlength="10" class="form-control required number" equalTo="#receiverAccount"/>
					</td>
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
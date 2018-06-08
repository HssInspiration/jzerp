<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>总包合同管理</title>
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
				  url : "${ctx}/subcontractapproval/getSubProContractList?subProContractName=",
				  processData : function(json) {
				      var i, len, data = {
				          value : []
				      };
				      if (json.length == 0) {
				          return false
				      } 
				      len = json.length;
				      for (i = 0; i < len; i++) {
				    	  console.log(json[i].id+"+"+json[i].subProContractName);
				          data.value.push({
				        	  "id\" style=\"display:none\"":json[i].id,
				              "word":json[i].subProContractName
			              })
			          }
			              return data
				  }
			  });
			
// 			$("#test_data").on('onSetSelectValue', function (e, keyword) {//下拉框值选中时调用
// 				console.log('onSetSelectValue: ', keyword);
// 				//思路：
// 				//1.下拉变化时获取项目id;
// 				var id = keyword.id;
// 				console.log("id:"+id);
// 				var jsonData = JSON.stringify({"id":id});
// 				//2.获取完id传入后台获取对应主项目信息;
// 				$.ajax({
// 					url:"${ctx}/subcontractapproval/getsubcontractapprovalByProgramId",
// 					data:jsonData,
// 					type:"post",
// 					contentType:"application/json;charset=utf-8",
// 					dataType:"json",
// 					success:function(data){
// 						console.log("获取成功！"+data);
// 						if(data==true){//代表该主项目对应的合同已经登记
// 							$("#alreadyExsist").html("×当前项目已登记，请重新选择！");
// 							$("#alreadyExsist").css("color","red");
// 						}else{//代表该主项目对应的合同未登记
// 							$("#alreadyExsist").html("OK!");
// 							$("#alreadyExsist").css("color","green");
// 						}
// 					},
// 					error:function(){
// 						console.log("获取失败！");
// 					}
// 				});
// 			});
			
			validateForm = $("#inputForm").validate({
				submitHandler: function(form){
					var subProContractId = $("#test_data").attr("data-id");
					if(subProContractId != null && subProContractId != ""){
						$("#subProContractId").val(subProContractId);
 					}
					
					jp.post("${ctx}/subcontractapproval/save",$('#inputForm').serialize(),function(data){
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
		<form:form id="inputForm" modelAttribute="subContractApproval" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>
		<table class="table table-bordered">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>分合同名称：</label></td>
					<td class="width-35">
						<input type="hidden" class="form-control" name= "subProContract.id" id = "subProContractId" value = "${subContractApproval.subProContract.id}">
						<c:if test="${isAdd}">
							<div class="row">
				                <div class="col-lg-2">
				                    <div class="input-group">
				                        <input type="text" class="form-control" <c:if test="${edit}" >readOnly="true"</c:if>  id="test_data" value = "${subcontractapproval.subProContract.subProContractName}">
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
				            <span id="alreadyExsist" style = "font-weight:bold;"></span>
			            </c:if>
			            <c:if test="${edit }">
			            	<input type="text" class="form-control" readOnly="true" value = "${subContractApproval.subProContract.subProContractName}">
			            </c:if>		
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>合同状态：</label></td>
					<td class="width-35">
						<form:select path="subContractStatus" class="form-control" >
							<form:option value="" label="--请选择合同状态--"/>
							<form:options items="${fns:getDictList('subcontract_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>审核状态：</label></td>
					<td class="width-35">
						<form:select path="approvalStatus" class="form-control" >
							<form:option value="" label="--请选择审核状态--"/>
							<form:options items="${fns:getDictList('procontract_approval')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
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
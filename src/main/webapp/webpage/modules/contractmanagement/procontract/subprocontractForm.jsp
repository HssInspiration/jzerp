<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>分包合同管理</title>
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
			
// 			从url请求项目集合，参数：项目名称-programName
			$("#test_data").bsSuggest({
				  idField : "id\" style=\"display:none\"",
				  keyField : "word",
				  getDataMethod : "url",
				  url : "${ctx}/subprocontract/getSubpackageProgramListByName?subpackageProgramName=",
				  processData : function(json) {
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
				              "word":json[i].subpackageProgramName
				              })
				          }
				          return data
				  }
			  });
			  
// 			从url请求项目集合，参数：项目名称-proContractName
			$("#test_data1").bsSuggest({
				  idField : "id\" style=\"display:none\"",
				  keyField : "word",
				  getDataMethod : "url",
				  url : "${ctx}/subprocontract/getProContractList?proContractName=",
				  processData : function(json) {
					  console.log("remark1:"+json);
				      var i, len, data = {
				          value : []
				      };
				      if (json.length == 0) {
				          return false
				      } 
				      len = json.length;
				      for (i = 0; i < len; i++) {
				    	  console.log("remark2:"+json[i].contractName);
				          data.value.push({
				        	  "id\" style=\"display:none\"":json[i].id,
				              "word":json[i].contractName
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
				//2.获取完id传入后台获取对应的类型集合;
				$.ajax({
					url:"${ctx}/subprocontract/getSubProContractBySubProId",
					data:jsonData,
					type:"post",
					contentType:"application/json;charset=utf-8",
					dataType:"json",
					success:function(data){
						console.log("获取成功！"+data);
						if(data==true){//代表该分包项目对应的合同已经登记
							$("#alreadyExsist").html("×当前子项目已登记，请重新选择！");
							$("#alreadyExsist").css("color","red");
						}else{//代表该分包项目对应的合同未登记
							$("#alreadyExsist").html("OK!");
							$("#alreadyExsist").css("color","green");
						}
					},
					error:function(){
						console.log("获取失败！");
					}
				});
			});//下拉框值选中时调用
			
			validateForm = $("#inputForm").validate({
				submitHandler: function(form){
					var subpackageProgramId = $("#test_data").attr("data-id");
					if(subpackageProgramId != null && subpackageProgramId != ""){
						$("#subpackageProgramId").val(subpackageProgramId);
 					}
					jp.post("${ctx}/subprocontract/save",$('#inputForm').serialize(),function(data){
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
			$('#startDate').datetimepicker({
				 format: "YYYY-MM-DD HH:mm:ss"
		    });
			$('#completeDate').datetimepicker({
				 format: "YYYY-MM-DD HH:mm:ss"
		    });
			$('#subProContractDate').datetimepicker({
				 format: "YYYY-MM-DD HH:mm:ss"
		    });
		});
	</script>
	<script src="${ctxStatic}/plugin/js/bootstrap-suggest.min.js"></script>
</head>
<body class="bg-white">
		<form:form id="inputForm" modelAttribute="subProContract" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>
		<table class="table table-bordered">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>分包合同编号：</label></td>
					<td class="width-35">
						<input type="hidden" class="form-control" name= "subProContract.subpackageProgram.proContract.id" value = "${subProContract.subpackageProgram.proContract.id}">
						<form:input path="subProContractNum" htmlEscape="false"  readOnly="true"  class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>分包合同名称：</label></td>
					<td class="width-35">
						<input type="hidden" class="form-control" name= "subProContract.id" id = "subProContractId" value = "${subProContract.id}">
						<form:input path="subProContractName" htmlEscape="false"  class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>分包项目名称：</label></td>
					<td class="width-35">
						<input type="hidden" class="form-control" name= "subpackageProgram.id" id = "subpackageProgramId" value = "${subProContract.subpackageProgram.id}">
						<c:if test="${isAdd}">
							<div class="row">
				                <div class="col-lg-2">
				                    <div class="input-group">
				                        <input type="text" class="form-control required"  id="test_data" value = "${subProContract.subpackageProgram.subpackageProgramName}">
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
			            	<input type="text" class="form-control" readOnly="true" value = "${subProContract.subpackageProgram.subpackageProgramName}">
			            </c:if>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>合同总价(万元)：</label></td>
					<td class="width-35">
						<form:input path="subProTotalPrice" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>工程联系人：</label></td>
					<td class="width-35">
						<form:input path="connector" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>联系人号码：</label></td>
					<td class="width-35">
						<form:input path="phoneNum" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
<!-- 				<tr> -->
<!-- 					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>总包合同名称：</label></td> -->
<!-- 					<td class="width-35"> -->
<%-- 						<input type="hidden" class="form-control" name= "proContract.id" id = "proContractId" value = "${subProContract.proContract.id}"> --%>
<!-- 						<div class="row"> -->
<!-- 			                <div class="col-lg-2"> -->
<!-- 			                    <div class="input-group"> -->
<%-- 			                        <input type="text" class="form-control required"  id="test_data1" value = "${subProContract.proContract.contractName}"> --%>
<!-- 			                        <div class="input-group-btn"> -->
<!-- 			                            <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown"> -->
<!-- 			                                <span class="caret"></span> -->
<!-- 			                            </button> -->
<!-- 			                            <ul class="dropdown-menu dropdown-menu-right" role="menu"> -->
<!-- 			                            </ul> -->
<!-- 			                        </div> -->
<!-- 			                    </div> -->
<!-- 			                </div> -->
<!-- 			            </div> -->
<!-- 					</td> -->
<!-- 					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>工程地址：</label></td> -->
<!-- 					<td class="width-35"> -->
<%-- 						<form:input path="subProAddr" htmlEscape="false"    class="form-control "/> --%>
<!-- 					</td> -->
<!-- 					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>工程联系人：</label></td> -->
<!-- 					<td class="width-35"> -->
<%-- 						<form:input path="connector" htmlEscape="false"    class="form-control "/> --%>
<!-- 					</td> -->
<!-- 				</tr> -->
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>开工日期：</label></td>
					<td class="width-35">
						<div class='input-group form_datetime' id='startDate'>
		                    <input type='text'  name="startDate" class="form-control required"  value="<fmt:formatDate value="${subProContract.startDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
		                    <span class="input-group-addon">
		                        <span class="glyphicon glyphicon-calendar"></span>
		                    </span>
		                </div>	
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>合同拟草人：</label></td>
					<td class="width-35">
						<input type="hidden" class="form-control" name= "user.id" id = "userId" value = "${subProContract.user.id}">
						<form:input path="user.name" htmlEscape="false"  readOnly="true"  class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>竣工日期：</label></td>
					<td class="width-35">
						<div class='input-group form_datetime' id='completeDate'>
		                    <input type='text'  name="completeDate" class="form-control required"  value="<fmt:formatDate value="${subProContract.completeDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
		                    <span class="input-group-addon">
		                        <span class="glyphicon glyphicon-calendar"></span>
		                    </span>
		                </div>
					</td>
					
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>分包公司：</label></td>
					<td class="width-35">
						<form:input path="employer" htmlEscape="false" readOnly="true" value="江苏金卓建设工程有限公司" class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>签订日期：</label></td>
					<td class="width-35">
						<div class='input-group form_datetime' id='subProContractDate'>
		                    <input type='text'  name="subProContractDate" class="form-control required"  value="<fmt:formatDate value="${subProContract.subProContractDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
		                    <span class="input-group-addon">
		                        <span class="glyphicon glyphicon-calendar"></span>
		                    </span>
		                </div>
					</td>
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
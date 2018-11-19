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
				  url : "${ctx}/procontract/getProgramByName?programName=",
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
				              "word":json[i].programName
			              })
			          }
			              return data
				  }
			  });
			
			$("#test_data").on('onSetSelectValue', function (e, keyword) {//下拉框值选中时调用
				console.log('onSetSelectValue: ', keyword);
				//思路：
				//1.下拉变化时获取项目id;
				var id = keyword.id;
				console.log("id:"+id);
				var jsonData = JSON.stringify({"id":id});
				var str= JSON.stringify(id);
			   	console.log("id:"+id+"jsonData:"+jsonData+typeof(id)+"6666:"+str);
				//2.获取完id传入后台获取对应主项目信息;
				$.ajax({
					url:"${ctx}/procontract/getProContractByProgramId",
					data:jsonData,
					type:"post",
					contentType:"application/json;charset=utf-8",
					dataType:"json",
					success:function(data){
						console.log("获取成功！"+data);
						if(data==true){//代表该主项目对应的合同已经登记
							$("#alreadyExsist").html("×当前项目已登记，请重新选择！");
							$("#alreadyExsist").css("color","red");
						}else{//代表该主项目对应的合同未登记
							$("#alreadyExsist").html("OK!");
							$("#alreadyExsist").css("color","green");
						}
					},
					error:function(){
						console.log("获取失败！");
					}
				});
				//3若为市场投标项目则代入参投模块的投标价
				$.ajax({
					url:"${ctx}/procontract/getBidPriceByProId",
					data:jsonData,
					type:"post",
					contentType:"application/json;charset=utf-8",
					dataType:"json",
					success:function(data){
						console.log("getBidPriceByProId获取成功0！"+data);
						console.log("getBidPriceByProId获取成功2！"+data.bidPrice);
						if(data.bidPrice != null){
							$("#contractTotalPrice").val(data.bidPrice);
						}
					},
					error:function(){
						console.log("获取失败！");
					}
				});
			});
			
			validateForm = $("#inputForm").validate({
				submitHandler: function(form){
					var programId = $("#test_data").attr("data-id");
					if(programId != null && programId != ""){
						$("#programId").val(programId);
 					}
					
					jp.post("${ctx}/procontract/save",$('#inputForm').serialize(),function(data){
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
			$('#contractDate').datetimepicker({
				 format: "YYYY-MM-DD HH:mm:ss"
		    });
		});
	</script>
	<script src="${ctxStatic}/plugin/js/bootstrap-suggest.min.js"></script>
</head>
<body class="bg-white">
		<form:form id="inputForm" modelAttribute="proContract" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>
		<table class="table table-bordered">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>合同编号：</label></td>
					<td class="width-35">
						<form:input path="contractNum" htmlEscape="false" readOnly="true" value="${proContract.contractNum }"  class="form-control required"/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>合同名称：</label></td>
					<td class="width-35">
					<c:if test = "${proContract.approvalStatus eq 1 || proContract.approvalStatus eq 2 }"> 
						<form:input path="contractName" htmlEscape="false"  readOnly="true"  class="form-control required"/>	
					</c:if>	
					<c:if test = "${proContract.approvalStatus eq 0 || isAdd}"> 
						<form:input path="contractName" htmlEscape="false"   class="form-control required"/>	
					</c:if>	
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>项目名称：</label></td>
					<td class="width-35">
						<input type="hidden" class="form-control required" name= "program.id" id = "programId" value = "${proContract.program.id}">
						<c:if test="${isAdd}">
							<div class="row">
				                <div class="col-lg-2">
				                    <div class="input-group">
				                        <input type="text" class="form-control" <c:if test="${edit}" >readOnly="true"</c:if>  id="test_data" value = "${proContract.program.programName}">
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
			            	<input type="text" class="form-control" readOnly="true" value = "${proContract.program.programName}">
			            </c:if>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>合同拟草人：</label></td>
					<td class="width-35">
						<input type="hidden" class="form-control" name="user.id" value = "${proContract.user.id}">
						<form:input path="user.name" htmlEscape="false" value="${proContract.user.name}" readOnly="true"  class="form-control required"/>
					</td>
					
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>工程联系人：</label></td>
					<td class="width-35">
						<form:input path="programConnector" htmlEscape="false"    class="form-control required"/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>联系人号码：</label></td>
					<td class="width-35">
						<form:input path="phoneNum" htmlEscape="false"    class="form-control required"/>
					</td>
				</tr>
				<tr>
					
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>开工日期：</label></td>
					<td class="width-35">
						<div class='input-group form_datetime' id='startDate'>
		                    <input type='text' <c:if test = "${proContract.approvalStatus eq 1 ||proContract.approvalStatus eq 2 }">readOnly="true" </c:if> name="startDate" class="form-control required"  value="<fmt:formatDate value="${proContract.startDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
		                    <span class="input-group-addon">
		                        <span class="glyphicon glyphicon-calendar"></span>
		                    </span>
		                </div>	
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>竣工日期：</label></td>
					<td class="width-35">
						<div class='input-group form_datetime' id='completeDate'>
		                     <input type='text' name="completeDate"  <c:if test = "${proContract.approvalStatus eq 1 ||proContract.approvalStatus eq 2 }">readOnly="true" </c:if> class="form-control required"  value="<fmt:formatDate value="${proContract.completeDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
			                    <span class="input-group-addon">
			                        <span class="glyphicon glyphicon-calendar"></span>
			                    </span>
		                </div>
					</td>
				</tr>
<!-- 				<tr> -->
<!-- 					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>工程地址：</label></td> -->
<!-- 					<td class="width-35"> -->
<%-- 						<form:input path="programAddr" htmlEscape="false"    class="form-control "/> --%>
<!-- 					</td> -->
<!-- 				</tr> -->
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>合同总价(万元)：</label></td>
					<td class="width-35">
						<form:input path="contractTotalPrice" htmlEscape="false" class="form-control required isFloatGteZero"/>
					</td>
<!-- 					<td class="width-15 active"><label class="pull-right">生效签订日期：</label></td> -->
<!-- 					<td class="width-35"> -->
<!-- 						<div class='input-group form_datetime' id='contractDate'> -->
<%-- 		                    <input type='text'  name="contractDate" class="form-control"  value="<fmt:formatDate value="${proContract.contractDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/> --%>
<!-- 		                    <span class="input-group-addon"> -->
<!-- 		                        <span class="glyphicon glyphicon-calendar"></span> -->
<!-- 		                    </span> -->
<!-- 		                </div> -->
<!-- 					</td> -->
					<td class="width-15 active"><label class="pull-right">备注信息：</label></td>
					<td class="width-35">		
						<form:textarea path="remarks"  htmlEscape="false"  rows="4" class="form-control"/>
					</td>
				</tr>
		 	</tbody>
		</table>
	</form:form>
</body>
</html>
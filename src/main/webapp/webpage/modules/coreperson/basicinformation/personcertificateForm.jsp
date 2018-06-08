<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>字典管理</title>
	<meta name="decorator" content="ani"/>
	<script type="text/javascript">
		var validateForm;
		var $table; // 父页面table表格id
		var $topIndex;//弹出窗口的 index
		function doSubmit(table, index){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
		  if(validateForm.form()){
			  $table = table;
			  $topIndex = index;
			  $("#inputForm").submit();
			  return true;
		  }
	
		  return false;
		}
		$(document).ready(function() {
			
			var certificateName = $("#certificateName").val();
			var jsonData = JSON.stringify({"value":certificateName});
			var url="${ctx}/basicinformation/getDictValueListById";
			var setRegisNum = {//验证是否需要填写注册证号
				method:function(certificateName){
					if(certificateName!=null){
						if(certificateName=="1"){//建造师（注册证号不可为空）
							$("#addClass2").html("<font color='red'> *</font>注册证号:");
							$("#registrationNum").addClass("required");
						}else{
							$("#addClass2").html("注册证号:");
							$("#registrationNum").removeClass("required");
						}
					}
				}
			}
			//若修改时，加载数据，初始化数据;发送ajax请求传回等级字典值集合
// 			checkCertificateName(url,jsonData);//src="${ctxStatic}/plugin/js/data.js"
			setRegisNum.method(certificateName);
			
			//证书名称下拉框改变时赋值证书等级
			$("#certificateName").bind("change",function(){
				certificateName = $("#certificateName").val();
				jsonData = JSON.stringify({"value":certificateName});
				console.log("certificateName:"+certificateName);
				//发送ajax请求传回等级字典值集合
				checkCertificateName(url,jsonData);//src="${ctxStatic}/plugin/js/data.js"
				setRegisNum.method(certificateName);
			});
			
			 validateForm = $("#inputForm").validate({
				submitHandler: function(form){
					$("#certificateName").each(function() {//移除disable,后台方可取值  
					    if (parseInt($(this).val()) != -1) {  
					        $(this).attr("disabled", false);  
					    }  
					});
					$("#certificateClass").each(function() {//移除disable,后台方可取值  
					    if (parseInt($(this).val()) != -1) {  
					        $(this).attr("disabled", false);  
					    }  
					});
					jp.loading();
					jp.post("${ctx}/basicinformation/savePersonCertificate",$('#inputForm').serialize(),function(data){
		                    if(data.success){
		                    	$table.bootstrapTable('refresh');
		                    	jp.success(data.msg);
		                    	jp.close($topIndex);//关闭dialog
		                    }else{
	            	  			jp.error(data.msg);
		                    }
		                    
		            });
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
			$('#regisDate').datetimepicker({
				 format: "YYYY-MM-DD HH:mm:ss"
		    });
			$('#invalidDate').datetimepicker({
				 format: "YYYY-MM-DD HH:mm:ss"
		    });
// 			$('#isInvalid2').parent().addClass("checked");
		});
	</script>
	<script src="${ctxStatic}/plugin/js/data.js"></script>
</head>
<body class="bg-white">
	<form:form id="inputForm" modelAttribute="personCertificate"  method="post" class="form-horizontal">
		<input type="hidden" name="personCertificateId" value="${personCertificate.id }"/>
		<form:hidden path="corePerson.id"/>
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
		      <tr>
		         <td  class="width-15 active">	<label class="pull-right"><font color="red">*</font>证书名称:</label></td>
		         <td class="width-35" >
			         <c:if test="${isAdd }">
						<form:select path="certificateName"  class="form-control required ">
							<form:option value="" label="--请选择证书名称--"/>
							<form:options items="${fns:getDictList('certificate_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					 </c:if>
			         <c:if test="${edit }">
						<form:select path="certificateName"  class="form-control required " disabled="true">
							<form:option value="" label="--请选择证书名称--"/>
							<form:options items="${fns:getDictList('certificate_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					 </c:if>
		         </td>
		      </tr>
		      <tr>
		         <td  class="width-15 active">	<label class="pull-right" id="addClass1">证书级别:</label></td>
		         <td class="width-35" >
		         	<c:if test="${isAdd}">
						<form:select path="certificateClass"  class="form-control ">
							<form:option value="" label="--请选择证书级别--"/>
						</form:select>
					</c:if>
					<c:if test="${edit}">
						<form:select path="certificateClass"  class="form-control " disabled="true">
							<c:if test="${personCertificate.certificateName eq 1 }">
								<form:options items="${fns:getDictList('constructor_class')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
							</c:if>
							<c:if test="${personCertificate.certificateName eq 2 }">
								<form:options items="${fns:getDictList('position_class')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
							</c:if>
							<c:if test="${personCertificate.certificateName eq 11 }">
								<form:options items="${fns:getDictList('costmember_calss')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
							</c:if>
						</form:select>
					</c:if>
		         </td>
		      </tr>
		      <tr>
		         <td  class="width-15 active">	<label class="pull-right"><font color="red">*</font>对应专业:</label></td>
		         <td class="width-35" >
<%-- 					<form:checkboxes class="i-checks" path="certificateMajor"  items="${fns:getDictList('certificate_major')}" itemLabel="label" itemValue="value" htmlEscape="false"/> --%>
		         	 <sys:checkbox id="certificateMajor" name="certificateMajor"  items="${fns:getDictList('certificate_major')}" values="${personCertificate.certificateMajor}" cssClass="i-checks required"/>
		         </td>
		      </tr>
		      <tr>
		         <td  class="width-15 active">	
			   		<label class="pull-right"><font color="red">*</font>注册日期:</label>
			   	  </td>
		          <td  class="width-35" >
					<div class='input-group form_datetime' id='regisDate'>
	                    <input type='text'  name="regisDate" class="form-control required"  value="<fmt:formatDate value="${personCertificate.regisDate}" pattern="yyyy-MM-dd HH:mm"/>"/>
	                    <span class="input-group-addon">
	                        <span class="glyphicon glyphicon-calendar"></span>
	                    </span>
	                </div>						            
			      </td>
		      </tr>
		      <tr>
		         <td  class="width-15 active">	
			   		<label class="pull-right"><font color="red">*</font>失效日期:</label>
			   	  </td>
		          <td  class="width-35" >
					<div class='input-group form_datetime' id='invalidDate'>
	                    <input type='text'  name="invalidDate" class="form-control required"  value="<fmt:formatDate value="${personCertificate.invalidDate}" pattern="yyyy-MM-dd HH:mm"/>"/>
	                    <span class="input-group-addon">
	                        <span class="glyphicon glyphicon-calendar"></span>
	                    </span>
	                </div>						            
			      </td>
		      </tr>
<!-- 		      <tr> -->
<!-- 		          <td  class="width-15 active">	<label class="pull-right"><font color="red">*</font>是否失效:</label></td> -->
<!-- 		          <td  class="width-35" > -->
<%-- 		          	 <form:radiobuttons path="isInvalid"  items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false" class="i-checks required"/> --%>
<!-- 		          </td> -->
<!-- 		      </tr> -->
			
		      <tr>
		          <td  class="width-15 active">	<label class="pull-right">失效原因:</label></td>
		          <td  class="width-35" >
		          	<c:if test="${personCertificate.isInvalid eq 1 }">
		          		<form:input path="invalidReason" htmlEscape="false" maxlength="50"  class="form-control"/>
					</c:if> 
		          	<c:if test="${personCertificate.isInvalid eq 0 }">
		          		<form:input path="invalidReason" htmlEscape="false" maxlength="50" disabled="true" class="form-control"/>
					</c:if> 
		          </td>
		      </tr>
		   
		      <tr>
		          <td  class="width-15 active">	<label class="pull-right"><font color="red">*</font>证书编号1:</label></td>
		          <td  class="width-35" ><form:input path="certificateFirstNum" htmlEscape="false" maxlength="50" class="form-control required"/></td>
		      </tr>
		      <tr>
		          <td  class="width-15 active">	<label class="pull-right">证书编号2:</label></td>
		          <td  class="width-35" ><form:input path="certificateSecondNum" htmlEscape="false" maxlength="50" class="form-control "/></td>
		      </tr>
		      <tr>
		          <td  class="width-15 active">	<label class="pull-right">证书编号3:</label></td>
		          <td  class="width-35" ><form:input path="certificateThirdNum" htmlEscape="false" maxlength="50" class="form-control "/></td>
		      </tr>
		      <tr>
		          <td  class="width-15 active">	<label class="pull-right" id="addClass2">注册证号:</label></td>
		          <td  class="width-35" ><form:input path="registrationNum" htmlEscape="false" maxlength="50" placeholder="若为建造师必填" class="form-control "/></td>
		      </tr>
		   </tbody>
		  </table>   
	</form:form>
</body>
</html>
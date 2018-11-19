<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>项目工程管理管理</title>
	<meta name="decorator" content="ani"/>
	<link id="bscss" href="${ctxStatic}/common/css/bootstrap.min.css" rel="stylesheet">
	<script type="text/javascript">
		var validateForm;
		var $table;// 父页面table表格id
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
			$("#programNum").focus();
			//发标单位模糊匹配下拉框
			$("#test_data1").bsSuggest({
				  idField : "id\" style=\"display:none\"",
				  keyField : "word",
				  getDataMethod : "url",
				  url : "${ctx}/programmanage/program/getCompanyList?companyName=",
				  processData : function(json) {
					//console.log(json);
				      var i, len, data = {
				          value : []
				      };
				      if (json.length == 0) {
				          return false
				      } 
				      len = json.length;
				      for (i = 0; i < len; i++) {
				    	//console.log(json[i].id);
				          data.value.push({
				        	  "id\" style=\"display:none\"":json[i].id,
				              "word":json[i].companyName
				              })
				          }
				          return data
				  }
			  });
			//分公司模糊匹配下拉框
			$("#test_data2").bsSuggest({
				  idField : "id\" style=\"display:none\"",
				  keyField : "word",
				  getDataMethod : "url",
				  url : "${ctx}/programmanage/program/getOfficeList?name=",
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
				              "word":json[i].name
				              })
				          }
				          return data
				  }
			  });
			
			validateForm = $("#inputForm").validate({
				rules: {
					programNum: {
						remote: "${ctx}/programmanage/program/checkProgramNum?programOldNum=" + encodeURIComponent("${program.programNum}")},//设置了远程验证，在初始化时必须预先调用一次。
				},
				messages: {
					programNum: {remote: "项目编号已存在"},
				}, 
				
				submitHandler : function(form) {
					$("[disabled]").each(function() {//移除disable,后台方可取值  
				        if (parseInt($(this).val()) != -1) {  
				            $(this).attr("disabled", false);  
				        }  
				    }); 
					var companyId = $("#test_data1").attr("data-id");
					console.log("companyId为："+companyId);
 					if(companyId != null && companyId != ""){
						$("#companyId").val(companyId);
 					}
					var officeId = $("#test_data2").attr("data-id");
					console.log("officeId为："+officeId);
					if(officeId != null && officeId != ""){
						$("#officeId").val(officeId);
 					}
					jp.loading();
					jp.post("${ctx}/programmanage/program/save",$('#inputForm').serialize(),function(data){
		                    if(data.success){
		                    	$table.bootstrapTable('refresh');//刷新表格
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
			
	        $('#callBidDate').datetimepicker({
				 format: "YYYY-MM-DD HH:mm:ss"
		    });

	        $('#planToStart').datetimepicker({
				 format: "YYYY-MM-DD HH:mm:ss"
		    });
			$('#planToEnd').datetimepicker({
				 format: "YYYY-MM-DD HH:mm:ss"
		    });
	        
 		});
		
		function returnManage(){
			
        	jp.close();//先关闭弹出层
        	
        	window.location = "${ctx}/programmanage/program/list";
        	
        }
		
		function companyManage(){
        	jp.openDialogView('单位管理', '${ctx}/programmanage/company/list','1000px', '600px');
        }
		
	</script>
	<script src="${ctxStatic}/plugin/js/bootstrap-suggest.min.js"></script>
</head>
<body class="bg-white">
	<form:form id="inputForm" modelAttribute="program" autocomplete="off" action="${ctx}/programmanage/program/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
		     <c:if test = "${isAdd}">
		       <tr>
		         <td  class="width-15 active"><label class="pull-right"><font color="red">*</font>项目工程编号:</label></td>
		         <td class="width-35" >
		         	<input id="programOldNum" name="programOldNum" type="hidden" value="${program.programNum}">
		         	<form:input path="programNum" htmlEscape="false" maxlength="50" class="form-control required" id="pnum"/>
		         </td>
		       	 <td  class="width-15 active">	
		       	 	<label class="pull-right"><font color="red">*</font>项目工程名称:</label>
		       	 </td>
		         <td  class="width-35" >
		         	<form:input path="programName" htmlEscape="false" maxlength="50" class="form-control required"/>
		         </td>
		       </tr>
		     </c:if>
		   	 <c:if test = "${edit}">
		       <tr>
		         <td  class="width-15 active">
		         <label class="pull-right"><font color="red">*</font>项目工程编号:</label>
		         </td>
		         <td class="width-35" >
			         <input id="programOldNum" name="programOldNum" type="hidden" value="${program.programNum}">
			         <form:input path="programNum" htmlEscape="false" maxlength="50" class="form-control required" readOnly = "true"/>
		         </td>
		         <td  class="width-15 active">	
		         	<label class="pull-right"><font color="red">*</font>项目工程名称:</label></td>
		         <td  class="width-35" >
		         	<form:input path="programName" htmlEscape="false" readOnly="true" maxlength="50" class="form-control required"/>
		         </td>
		       </tr>
		     </c:if>
		       <tr>
			     <td  class="width-15 active">	<label class="pull-right"><font color="red">*</font>项目工程类别:</label></td>
			     <td  class="width-35" >
			            <sys:checkbox id="programType" name="programType"  items="${fns:getDictList('programtype')}" values="${program.programType}" cssClass="i-checks required"/>
			     </td>
		          <td  class="width-15 active">	<label class="pull-right">项目详细描述:</label></td>
		          <td  class="width-35" >
					<form:input path="proDescription" htmlEscape="false"  class="form-control "/>	
				  </td>
		      </tr> 
		      <tr>
		          <td  class="width-15 active">	<label class="pull-right"><font color="red">*</font>发标单位:</label></td>
		          <td  class="width-35" >
<%-- 			          <form:select path="company.id" class="form-control required" id="s2">  --%>
<%-- 								<form:option value="" label="--请选择单位，若无可选，请点击维护--"/> --%>
<%-- 								<form:options items="${companyList}" itemLabel="companyName" itemValue="id" htmlEscape="false"/> --%>
<%-- 					  </form:select> --%>
					<input type="hidden" class="form-control required" name= "company.id" id = "companyId" value = "${program.company.id}">
					<div class="row">
		                <div class="col-lg-2">
		                    <div class="input-group">
		                        <input type="text" class="form-control required" id="test_data1" placeholder="请选择单位，若无可选，请点击维护" value = "${program.company.companyName}">
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
					  <a class="btn btn-primary"   onclick ="companyManage()" style="display:block;margin-top:15px;width:160px;"href="#" title="发标单位管理">
					<i class="glyphicon glyphicon-edit"></i> 维护单位信息</a>
		          </td>
		          
		          <td class="width-15 active"><label class="pull-right"><font color="red">*</font>承接方式：</label></td>
				  <td class="width-35">
							<form:select path="getMethod" class="form-control reqired" disabled="true">
								<form:options items="${fns:getDictList('get_method')}"  itemLabel="label" itemValue="value"  htmlEscape="false"/>
							</form:select>
						<span id="typeAlreadyExsist" style = "font-weight:bold;"></span>
				  </td>
		      </tr>
		      <tr>
		          <td  class="width-15 active">	<label class="pull-right"><font color="red">*</font>承接分公司:</label></td>
		          <td  class="width-35" >
					<input type="hidden" class="form-control required" name= "office.id" id = "officeId" value = "${program.office.id}">
					<div class="row">
		                <div class="col-lg-2">
		                    <div class="input-group">
		                        <input type="text" class="form-control required" id="test_data2" value = "${program.office.name}">
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
		          </td>
		          
		          <td  class="width-15 active">	
			   		<label class="pull-right"><font color="red">*</font>计划开标日期:</label>
			   	  </td>
		          <td  class="width-35" >
					<div class='input-group form_datetime' id='planToStart'>
	                    <input type='text'  name="planToStart" class="form-control required"  value="<fmt:formatDate value="${program.planToStart}" pattern="yyyy-MM-dd HH:mm"/>"/>
	                    <span class="input-group-addon">
	                        <span class="glyphicon glyphicon-calendar"></span>
	                    </span>
	                </div>						            
			      </td>
		      </tr>
		      <tr>
		      	 <td  class="width-15 active">	
		         	<label class="pull-right"><font color="red">*</font>工程联系人:</label></td>
		         <td  class="width-35" >
		         	<form:input path="programConnector" htmlEscape="false"  maxlength="50" class="form-control required"/>
		         </td>
		         <td  class="width-15 active">	
		         	<label class="pull-right"><font color="red">*</font>联系人号码:</label></td>
		         <td  class="width-35" >
		         	<form:input path="connectorPhone" htmlEscape="false" maxlength="50" class="form-control required"/>
		         </td>
		      </tr>
		      <tr>
		      	<td  class="width-15 active">	
		         	<label class="pull-right"><font color="red">*</font>工程地址:</label></td>
		        <td  class="width-35" >
		         	<form:input path="programAddr" htmlEscape="false"  maxlength="50" class="form-control required"/>
		        </td>
		        <td  class="width-15 active">	<label class="pull-right">备注信息:</label></td>
	          	<td  class="width-35" >
	           	 	<form:textarea path="remarks" htmlEscape="false" rows="4"    class="form-control "/>
	         	</td>
		      </tr>
		   </tbody>
		   </table>   
	</form:form>
</body>

<%-- <body>
<div class="wrapper wrapper-content">				
<div class="row">
	<div class="col-md-12">
	<div class="panel panel-primary">
		<div class="panel-heading">
			<h3 class="panel-title"> 
				<a class="panelButton"  onclick="returnManage()"><i class="ti-angle-left"></i> 返回</a>
			</h3>
		</div>
		<div class="panel-body">
		<form:form id="inputForm" modelAttribute="program" action="${ctx}/programmanage/program/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>项目工程编号：</label>
					<div class="col-sm-10">
						<form:input path="programNum" htmlEscape="false"    class="form-control required" id="programnum"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>项目工程名称：</label>
					<div class="col-sm-10">
						<form:input path="programName" htmlEscape="false"    class="form-control required"/>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>发标单位：</label>
					<div class="col-sm-10">
						<form:select path="company.id" class="form-control required" id="s3"> 
							<form:option value="" label="--请选择发标单位，若无可选单位，请点击维护--"/>
							<form:options items="${companyList}" itemLabel="companyName" itemValue="id" htmlEscape="false"/>
						</form:select>
						<form:input  path="company.companyName"  type="button" id="showSel" value="显示select" onclick="javascript:$('#s3').show();" />
						<a class="btn btn-primary"   onclick ="companyManage()" style="display:block;margin-top:15px;width:160px;"href="#" title="发标单位管理">
						<i class="glyphicon glyphicon-edit"></i> 维护单位信息</a>
					</div>
				</div>
				<script type="text/javascript" src="${ctxStatic}/plugin/js/jquery162.js"></script>
				<script type="text/javascript" src="${ctxStatic}/plugin/js/jquery-ui.min.js" ></script>
				<script type="text/javascript" src="${ctxStatic}/plugin/js/jquery-ui-i18n.js" ></script>
				<script type="text/javascript" src="${ctxStatic}/plugin/js/jquery-combobox-min_prop.js"></script>
				<script type="text/javascript">
					$(document).ready(function(){
						$('#s3').combobox({size: 70});
						//$('#s3').combobox({'title': '显示所有'});
					});
				</script>
				
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>项目工程类别：</label>
					<div class="col-sm-10">
						<sys:checkbox id="programType" name="programType" items="${fns:getDictList('programtype')}" values="${program.programType}" cssClass="i-checks required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">项目详细描述：</label>
					<div class="col-sm-10">
						<form:input path="proDescription" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">备注信息：</label>
					<div class="col-sm-10">
						<form:textarea path="remarks" htmlEscape="false" rows="4"    class="form-control "/>
					</div>
				</div>
				<c:if test="${edit || isAdd}">
						<div class="col-lg-3"></div>
				        <div class="col-lg-6">
				             <div class="form-group text-center" id="submitProgram">
				                 <div>
				                     <button class="btn btn-primary btn-block btn-lg btn-parsley" data-loading-text="正在提交...">提 交</button>
				                 </div>
				             </div>
				        </div>
				</c:if>
		</form:form>
		<!-- <script type="text/javascript">
			$().ready(function(){
				$("#submitProgram").bind("click",function(){
					jp.close();//关闭弹出层
					//$programTable.bootstrapTable('refresh');//局部刷新父页面列表：
				});
			});
		</script> -->
		</div>				
	</div>
	</div>
</div>
</div>
</body> --%>
</html>
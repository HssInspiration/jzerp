<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>单位管理管理</title>
	<meta name="decorator" content="ani"/>
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
			validateForm =  $("#inputForm").validate({
				
				submitHandler : function(form) {
					jp.post("${ctx}/programmanage/company/save",$('#inputForm').serialize(),function(data){
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
		});
	</script>
</head>
<body class="bg-white">
	<form:form id="inputForm" modelAttribute="company" action="${ctx}/programmanage/company/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
		       <tr>
		         <td  class="width-15 active"><label class="pull-right"><font color="red">*</font>公司编号:</label></td>
		         <td class="width-35" ><form:input path="companyNum" htmlEscape="false"  readOnly="true"  class="form-control required"/></td>
		      </tr>
		      <tr>
		          <td  class="width-15 active">	<label class="pull-right"><font color="red">*</font>公司名称:</label></td>
		          <td  class="width-35" >
					<form:input path="companyName" htmlEscape="false"    class="form-control required"/>								          </td>
		      </tr> 
		       <tr>
		          <td  class="width-15 active">	<label class="pull-right">公司详情:</label></td>
		          <td  class="width-35" >
		            <form:input path="companyCont" htmlEscape="false"    class="form-control "/>
		          </td>
		      </tr> 
		       <tr>
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
				<a class="panelButton" href="#" onclick="returnManage()"><i class="ti-angle-left"></i> 返回</a>
			</h3>
		</div>
		<div class="panel-body">
		<form:form id="inputForm" modelAttribute="company" action="${ctx}/programmanage/company/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>公司编号：</label>
					<div class="col-sm-10">
						<form:input path="companyNum" htmlEscape="false"    class="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>公司名称：</label>
					<div class="col-sm-10">
						<form:input path="companyName" htmlEscape="false"    class="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">公司详情：</label>
					<div class="col-sm-10">
						<form:input path="companyCont" htmlEscape="false"    class="form-control "/>
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
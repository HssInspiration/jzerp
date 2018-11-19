<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>投标管理管理</title>
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
			$("#bidNum").focus();
			//从url请求项目集合，参数：项目名称-programName
			$("#test_data").bsSuggest({
				  idField : "id\" style=\"display:none\"",
				  keyField : "word",
				  getDataMethod : "url",
				  url : "${ctx}/bidmanage/bidtable/getProgramList?programName=",
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
				              "word":json[i].programName
				              })
				          }
				          return data
				  }
			  });
		  validateForm = $("#inputForm").validate({
				rules: {
					bidNum: {remote: "${ctx}/bidmanage/bidtable/checkBidNum?bidOldNum=" + encodeURIComponent("${bidtable.bidNum}")},//设置了远程验证，在初始化时必须预先调用一次。
					
				},
				messages: {
					bidNum: {remote: "投标编号已存在"},
				},
				submitHandler : function(form) {
					var programId = $("#test_data").attr("data-id");
					if(programId != null && programId != ""){
						$("#programId").val(programId);
 					}
					jp.loading();
					jp.post("${ctx}/bidmanage/bidtable/save",$('#inputForm').serialize(),function(data){
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
			$('#openBidDate').datetimepicker({
				 format: "YYYY-MM-DD HH:mm:ss"
		    });
			var deposit = parseFloat($('#deposit').val()).toString();
			if(deposit != "NaN"){
				$('#deposit').val(deposit);//转换保证金
			}
			var ctrlPrice = parseFloat($('#ctrlPrice').val()).toString();
			if(ctrlPrice != "NaN"){
				$('#ctrlPrice').val(ctrlPrice);//转换控制价
			}
			var floorPrice = parseFloat($('#floorPrice').val()).toString();
			if(floorPrice != "NaN"){
				$('#floorPrice').val(floorPrice);//转换标底价
			}
			var provisionPrice = parseFloat($('#provisionPrice').val()).toString();
			if(provisionPrice != "NaN"){
				$('#provisionPrice').val(provisionPrice);//转换控制价
			}
		});
	</script>
	<script src="${ctxStatic}/plugin/js/bootstrap-suggest.min.js"></script>
</head>
<!-- dialog风格 -->
 <body class="bg-white">
	<form:form id="inputForm" modelAttribute="bidtable" action="${ctx}/bidmanage/bidtable/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
		     <c:if test ="${isAdd}">
			   <tr>
			      <td  class="width-15 active"><label class="pull-right"><font color="red">*</font>投标编号:</label></td>
			      <td class="width-35" ><input id="bidOldNum" name="bidOldNum" type="hidden" value="${bidtable.bidNum}">
			      <form:input path="bidNum" readOnly="true" htmlEscape="false" class="form-control required"/></td>
			   	  <td  class="width-15 active">	<label class="pull-right"><font color="red">*</font>投标日期:</label></td>
		          <td  class="width-35" >
						<div class='input-group form_datetime' id='openBidDate'>
		                    <input type='text'  name="openBidDate" class="form-control"  value="<fmt:formatDate value="${bidtable.program.planToStart}" pattern="yyyy-MM-dd HH:mm"/>"/>
		                    <span class="input-group-addon">
		                        <span class="glyphicon glyphicon-calendar"></span>
		                    </span>
		                </div>						            
			      </td>
			   </tr>
			 </c:if>
			 <c:if test ="${edit}">
			   <tr>
			      <td  class="width-15 active">
			      	<label class="pull-right"><font color="red">*</font>投标编号:</label>
			      </td>
			      <td class="width-35" >
			      	<input id="bidOldNum" name="bidOldNum" type="hidden" value="${bidtable.bidNum}">
			        <form:input path="bidNum"  htmlEscape="false" class="form-control required" readOnly = "true"/>
			      </td>
			   	  <td  class="width-15 active">	
			   		<label class="pull-right"><font color="red">*</font>投标日期:</label>
			   	  </td>
		          <td  class="width-35" >
					<div class='input-group form_datetime' id='openBidDate'>
	                    <input type='text'  name="openBidDate" class="form-control "  value="<fmt:formatDate value="${bidtable.program.planToStart}" pattern="yyyy-MM-dd HH:mm"/>"/>
	                    <span class="input-group-addon">
	                        <span class="glyphicon glyphicon-calendar"></span>
	                    </span>
	                </div>						            
			      </td>
			   </tr>
			 </c:if>
		      <tr>
		          <td  class="width-15 active">	<label class="pull-right"><font color="red">*</font>项目工程名称:</label></td>
		          <td  class="width-35" >
				    <input type="hidden" class="form-control" name= "program.id" id = "programId" value = "${bidtable.program.id}">
					<c:if test="${isAdd }">
						<div class="row">
			                <div class="col-lg-2">
			                    <div class="input-group">
			                        <input type="text" class="form-control" <c:if test="${edit}" >readOnly="true"</c:if>  id="test_data" value = "${bidtable.program.programName}">
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
	                     <input type="text" class="form-control required" readOnly="true" id="test_data" value = "${bidtable.program.programName}">
		            </c:if>
		          </td>
		          <td  class="width-15 active">	<label class="pull-right">开标地点:</label></td>
		          <td  class="width-35" >
		            <form:input path="openBidAddr" htmlEscape="false"    class="form-control "/>
		          </td>
		      </tr>
		      <tr>
		          <td  class="width-15 active">	<label class="pull-right"><font color="red">*</font>投标保证金(万元):</label></td>
		          <td  class="width-35" >
					<form:input path="deposit" htmlEscape="false"  placeholder = "单位：万元"  class="form-control required isFloatGteZero"/>
				  </td>							
				  <td  class="width-15 active">	<label class="pull-right">控制价(万元):</label></td>
		          <td  class="width-35" >
		            <form:input path="ctrlPrice" htmlEscape="false"  placeholder = "单位：万元"  class="form-control isFloatGteZero"/>
		          </td>	         
		      </tr> 
		      <tr>
		          <td  class="width-15 active">	<label class="pull-right">标底价(万元):</label></td>
		          <td  class="width-35" >
		            <form:input path="floorPrice" htmlEscape="false"  placeholder = "单位：万元"  class="form-control isFloatGteZero"/>
		          </td>
		           <td  class="width-15 active">	<label class="pull-right">暂列金额(万元):</label></td>
		          <td  class="width-35" >
		           <form:input path="provisionPrice" htmlEscape="false"  placeholder = "单位：万元"  class="form-control isFloatGteZero"/>
		          </td>
		      </tr>
		      <tr>
		          <td  class="width-15 active">	<label class="pull-right">开标记录人员:</label></td>
		          <td  class="width-35" >
		           <form:input path="recordWorker" htmlEscape="false"    class="form-control "/>
		          </td>
		          <td  class="width-15 active">	<label class="pull-right">所需材料:</label></td>
		          <td  class="width-35" >
<%-- 		           <sys:checkbox id="provideMeterial" name="provideMeterial" items="${fns:getDictList('providemeterial')}" values="${bidtable.provideMeterial}" cssClass="i-checks "/> --%>
		           <form:textarea path="provideMeterial" htmlEscape="false" rows="4"    class="form-control "/>
		          </td>
		      </tr>
		      <tr>
		      	  <td  class="width-15 active">	<label class="pull-right">评标方法:</label></td>
		          <td  class="width-35" >
		            <form:textarea path="evaluateMethod" htmlEscape="false" rows="4" class="form-control "/>
		          </td>
		          <td  class="width-15 active">	<label class="pull-right">备注信息:</label></td>
		          <td  class="width-35" >
		            <form:textarea path="remarks" htmlEscape="false" rows="4" class="form-control "/>
		          </td>
		      </tr>
		   </tbody>
		   </table>   
	</form:form>
</body>

<!-- form风格 -->
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
		<form:form id="inputForm" modelAttribute="bidtable" action="${ctx}/bidmanage/bidtable/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>投标编号：</label>
					<div class="col-sm-10">
						<form:input path="bidNum" htmlEscape="false" class="form-control required"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>计划开标时间：</label>
					<div class="col-sm-10">
						<p class="input-group">
							<div class='input-group form_datetime' id='openBidDate'>
			                    <input type='text'  name="openBidDate" class="form-control"  value="<fmt:formatDate value="${bidtable.openBidDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
			                    <span class="input-group-addon">
			                        <span class="glyphicon glyphicon-calendar"></span>
			                    </span>
			                </div>						            
			            </p>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label"><font color="red">*</font>项目工程名称：</label>
					<div class="col-sm-10">
						<form:select path="program.id" class="form-control required" id="s3">
							<form:option value="" label="--请选择项目工程--"/>
							<form:options items="${programList}" itemLabel="programName" itemValue="id" htmlEscape="false"/>
						</form:select>
					</div>
				</div>
				<script type="text/javascript" src="${ctxStatic}/plugin/js/jquery162.js"></script>
				<script type="text/javascript" src="${ctxStatic}/plugin/js/jquery-ui.min.js" ></script>
				<script type="text/javascript" src="${ctxStatic}/plugin/js/jquery-ui-i18n.js" ></script>
				<script type="text/javascript" src="${ctxStatic}/plugin/js/jquery-combobox-min_prop.js"></script>
				 <script type="text/javascript">
					$(document).ready(function(){
						//$('#s3').combobox({size: 25});
						$('#s3').combobox({'title': '显示所有'});
					});
				</script> 
				<div class="form-group">
					<label class="col-sm-2 control-label">开标地点：</label>
					<div class="col-sm-10">
						<form:input path="openBidAddr" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">保证金：</label>
					<div class="col-sm-10">
						<form:input path="deposit" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">控制价：</label>
					<div class="col-sm-10">
						<form:input path="ctrlPrice" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">标底价：</label>
					<div class="col-sm-10">
						<form:input path="floorPrice" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">暂列金额：</label>
					<div class="col-sm-10">
						<form:input path="provisionPrice" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">开标记录人员：</label>
					<div class="col-sm-10">
						<form:input path="recordWorker" htmlEscape="false"    class="form-control "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">所需材料：</label>
					<div class="col-sm-10">
						<sys:checkbox id="provideMeterial" name="provideMeterial" items="${fns:getDictList('providemeterial')}" values="${bidtable.provideMeterial}" cssClass="i-checks "/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">备注信息：</label>
					<div class="col-sm-10">
						<form:textarea path="remarks" htmlEscape="false" rows="4"    class="form-control "/>
					</div>
				</div>
			<c:if test="${edit|| isAdd}">
					<div class="col-lg-3"></div>
			        <div class="col-lg-6">
			             <div class="form-group text-center">
			                 <div>
			                     <button class="btn btn-primary btn-block btn-lg btn-parsley" data-loading-text="正在提交...">提 交</button>
			                 </div>
			             </div>
			        </div>
			</c:if>
		</form:form>
		
		</div>				
	</div>
	</div>
</div>
</div>
</body> --%>

<!-- 测试带输入功能的下拉框 -->
<!-- <body>  
    <div class="container">  
        <form class="form-horizontal" role="form">  
            <div class="form-group">  
                <label for="bs3Select" class="col-lg-2 control-label">Test bootstrap 3 form</label>  
                <div class="col-lg-10">  
                    <select id="bs3Select" class="selectpicker show-tick form-control" multiple data-live-search="true">  
                        <option>cow</option>  
                        <option>bull</option>  
                        <option class="get-class" disabled>ox</option>  
                        <optgroup label="test" data-subtext="another test" data-icon="icon-ok">  
                            <option>ASD</option>  
                            <option selected>Bla</option>  
                            <option>Ble</option>  
                        </optgroup>  
                    </select>  
                </div>  
              </div>  
        </form>  
    </div>  
  
</body>  
 -->
</html>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>子项目工程管理管理</title>
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
				  url : "${ctx}/buildingpro/getBidcompanyByProName?programName=",
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
				              "word":json[i].program.programName
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
					url:"${ctx}/buildingpro/getUserListById",
					async:false,
					data:jsonData,
					type:"post",
					contentType:"application/json;charset=utf-8",
					dataType:"json",
					success:function(data){
						console.log("获取成功！"+data);
						$("#coreStaffId").empty();  //每次获取成功后清空下拉列表()
						$("#coreStaffId").append("<option value=''>--请选择人员--</option>");
						if(data != null && data.length > 0){
							for(var i=0;i<data.length;i++){
								console.log(data[i].user.name);
								var userId = data[i].id;
	                            var userName = data[i].user.name;  
	                            $("#coreStaffId").append("<option value = \"" + userId + "\">"+ userName +"</option>");  
							}
						}
					},
					error:function(){
						console.log("获取失败！");
					}
				});
				//附加：选取之后下次再进行调用只显示没有选过的类型。
			});//下拉框值选中时调用
			
			//从url请求人员集合，参数：人员名称-name
// 			$("#test_data1").bsSuggest({
// 				  idField : "id\" style=\"display:none\"",
// 				  keyField : "word",
// 				  getDataMethod : "url",
// 				  url : "${ctx}/buildingpro/getCoreStaffByName?name=",
// 				  processData : function(json) {
// 				      var i, len, data = {
// 				          value : []
// 				      };
// 				      if (json.length == 0) {
// 				          return false
// 				      } 
// 				      len = json.length;
// 				      for (i = 0; i < len; i++) {
// 				          data.value.push({
// 				        	  "id\" style=\"display:none\"":json[i].id,
// 				              "word":json[i].user.name
// 				              })
// 				          }
// 				          return data
// 				  }
// 			  });
// 			$("#test_data1").on('onSetSelectValue', function (e, keyword) {
// 				console.log('onSetSelectValue: ', keyword);
			$("#coreStaffId").bind("change",function(){
				//思路：
				//1.下拉变化时获取id;
				var id = $("#coreStaffId").val();
				console.log(id);
				var jsonData = JSON.stringify({"id":id});
				//2.获取完id传入后台获取对应的数据;
				$.ajax({
					url:"${ctx}/buildingpro/getCertificateListById",
					data:jsonData,
					type:"post",
					contentType:"application/json;charset=utf-8",
					dataType:"json",
					success:function(data){
						console.log("获取成功"+data);
						$("#staffCertificateId").empty();  //每次获取成功后清空下拉列表()
						$("#staffCertificateId").append("<option value=''>--请选择证书类型--</option>");
						//3.将获得的证书类型集合遍历成下拉列表;
						if(data != null && data.length > 0){  
	                        for(var i = 0;i < data.length;i++){  
	                        	var certificateValue = data[i].certificateName;
	                            var certificateName = data[i].dictValueName.label;  
	                            $("#staffCertificateId").append("<option value = \"" + certificateValue + "\">"+ certificateName +"</option>");  
	                       }  
						}
					},
					error:function(){
						console.log("获取失败！");
					}
				});
			})
				
// 			});//下拉框值选中时调用
			
			validateForm = $("#inputForm").validate({
				submitHandler: function(form){
					var bidcompanyId = $("#test_data").attr("data-id");
					if(bidcompanyId != null && bidcompanyId != ""){
						$("#bidcompanyId").val(bidcompanyId);
 					}
					
					var coreStaffId = $("#test_data1").attr("data-id");
					if(coreStaffId != null && coreStaffId != ""){
						$("#coreStaffId").val(coreStaffId);
 					}
					
					jp.post("${ctx}/buildingpro/save",$('#inputForm').serialize(),function(data){
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
		<form:form id="inputForm" modelAttribute="building" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>
		<table class="table table-bordered">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>在建项目名称：</label></td>
					<td class="width-35">
					<input type="hidden" class="form-control" name= "bidcompany.id" id = "bidcompanyId" value = "${building.bidcompany.id}">
					<c:if test="${isAdd}">
					<div class="row">
		                <div class="col-lg-12">
		                    <div class="input-group">
		                        <input type="text"  class="form-control required" id="test_data" value = "${building.bidcompany.program.programName}">
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
		            	 <input type="text"  class="form-control required" readOnly="true"  value = "${building.bidcompany.program.programName}">
		            </c:if>
					</td>
				</tr>
				<tr>
<!-- 					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>人员名称：</label></td> -->
<!-- 					<td class="width-35"> -->
<%-- 						<input type="hidden" class="form-control" name= "coreStaff.id" id = "coreStaffId" value = "${building.coreStaff.id}"> --%>
<%-- 						<c:if test="${isAdd}"> --%>
<!-- 						<div class="row"> -->
<!-- 			                <div class="col-lg-2"> -->
<!-- 			                    <div class="input-group"> -->
<%-- 			                        <input type="text"  class="form-control required" id="test_data1" value = "${building.coreStaff.user.name}"> --%>
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
<%-- 			            </c:if> --%>
<%-- 			            <c:if test="${edit }"> --%>
<%-- 			            	 <input type="text"  class="form-control required" readOnly="true"  value = "${building.coreStaff.user.name}"> --%>
<%-- 			            </c:if> --%>
<!-- 					</td> -->
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>人员名称：</label></td>
					<td class="width-35">
<%-- 			          <form:select path="director.id" class="form-control " id="direct"> --%>
<%-- 							<form:option value="" label="----请选择对应工作人员----"/> --%>
<%-- 							<form:options items="${userList}" itemLabel="name" itemValue="id" htmlEscape="false"/> --%>
<%-- 					  </form:select> --%>
					<c:if test="${isAdd}">
						<form:select path="coreStaff.id" class="form-control required" id="coreStaffId">
							<form:option value="" label="----请选择人员名称----"/>
						</form:select>
					</c:if>
					<c:if test="${edit}">
						<form:select path="coreStaff.id" class="form-control" disabled="true" id="coreStaffId">
							<form:options items="${userList}" itemLabel="name" itemValue="id" htmlEscape="false"/>
						</form:select>
					</c:if>
					<span id="typeAlreadyExsist" style = "font-weight:bold;"></span>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>证件名称：</label></td>
					<td class="width-35">
					<c:if test="${isAdd}">
						<form:select path="staffCertificate.id" class="form-control required" id="staffCertificateId">
							<form:option value="" label="----请选择证书类型----"/>
						</form:select>
					</c:if>
					<c:if test="${edit}">
						<form:select path="staffCertificate.id" class="form-control" disabled="true" id="staffCertificateId">
							<form:options items="${fns:getDictList('certificate_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</c:if>
					<span id="typeAlreadyExsist" style = "font-weight:bold;"></span>
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
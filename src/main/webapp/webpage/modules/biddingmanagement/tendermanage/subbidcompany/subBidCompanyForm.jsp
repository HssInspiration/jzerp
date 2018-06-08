<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>子项目参投单位管理管理</title>
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
			
			//从url请求项目集合，参数：子项目名称-subpackageProgramName
			$("#test_data").bsSuggest({
				  idField : "id\" style=\"display:none\"",
				  keyField : "word",
				  getDataMethod : "url",
				  url : "${ctx}/tendermanage/subBidCompany/getTenderList?subpackageProgramName=",
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
				              "word":json[i].subpackageProgram.subpackageProgramName
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
// 				$.ajax({
// 					url:"${ctx}/tendermanage/subBidCompany/getCompanyNameByTenderId",
// 					async:false,
// 					data:jsonData,
// 					type:"post",
// 					contentType:"application/json;charset=utf-8",
// 					dataType:"json",
// 					success:function(data){
// 						console.log("获取成功！"+data);
// 					},
// 					error:function(){
// 						console.log("获取失败！");
// 					}
// 				});
				
				//参投单位名称模糊匹配框
				$("#test_data1").bsSuggest({
					  idField : "id\" style=\"display:none\"",
					  keyField : "word",
					  getDataMethod : "url",
					  url : "${ctx}/tendermanage/subBidCompany/getAllCompanyList?companyName=&tenderId="+id,
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
					              "word":json[i].companyName
					              })
					          }
					          return data
					  }
				  });
			});//下拉框值选中时调用
			
			
			validateForm = $("#inputForm").validate({
				
				submitHandler: function(form){
					
					var companyId = $("#test_data1").attr("data-id");
					if(companyId != null && companyId != ""){
						$("#companyId").val(companyId);
					}
					
					var tenderId = $("#test_data").attr("data-id");
					if(tenderId != null && tenderId != ""){
						$("#tenderId").val(tenderId);
					}
					
					jp.post("${ctx}/tendermanage/subBidCompany/save",$('#inputForm').serialize(),function(data){
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
			
	        $('#subBidDate').datetimepicker({
				 format: "YYYY-MM-DD HH:mm:ss"
		    });
		});
		function companyManage(){
			
			jp.openDialogView('单位管理', '${ctx}/programmanage/company/list','1000px', '600px');
	    }
	</script>
	<script src="${ctxStatic}/plugin/js/bootstrap-suggest.min.js"></script>
</head>
<body class="bg-white">
		<form:form id="inputForm" modelAttribute="subBidCompany" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
		<table class="table table-bordered">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>子项目名称：</label></td>
						<td class="width-35">
						<input type="hidden" class="form-control" name= "tender.id" id = "tenderId" value = "${subBidCompany.tender.id}">
						<div class="row">
			                <div class="col-lg-2">
			                    <div class="input-group">
			                        <input type="text" class="form-control required" id="test_data" <c:if test="${edit}">readOnly="true"</c:if>
			                         value = "${subBidCompany.tender.subpackageProgram.subpackageProgramName}">
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
				 
<!-- 				</tr> -->
<!-- 				<tr> -->
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>投标日期：</label></td>
					<td class="width-35">
							<div class='input-group form_datetime' id='subBidDate'>
			                    <input type='text'  name="subBidDate" class="form-control required"  value="<fmt:formatDate value="${subBidCompany.subBidDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
			                    <span class="input-group-addon">
			                        <span class="glyphicon glyphicon-calendar"></span>
			                    </span>
			                </div>
					</td>
				</tr>
				<tr>
	  			  <td  class="width-15 active">	<label class="pull-right"> <font color="red">*</font>投标单位名称:</label></td>
		          <td  class="width-35" >
					<input type="hidden" class="form-control " name= "company.id" id = "companyId" value = "${subBidCompany.company.id}">
					<div class="row">
		                <div class="col-lg-2">
		                    <div class="input-group">
		                        <input type="text" class="form-control required" <c:if test="${edit}">readOnly="true"</c:if>  id="test_data1" placeholder="若无可选，请点击维护"
		                        value = "${subBidCompany.company.companyName}">
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
		            <a class="btn btn-primary"   onclick ="companyManage()" style="display:block;margin-top:15px;width:160px;"href="#" title="参投单位管理">
					<i class="glyphicon glyphicon-edit"></i> 维护单位信息</a>
				 </td>
		  			<td class="width-15 active"><label class="pull-right"><font color="red">*</font>投标价：</label></td>
<!-- 					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>投标价（万元）：</label></td> -->
					<td class="width-35">
							<form:textarea path="subBidPrice" htmlEscape="false" rows="4"    class="form-control "/>
<%-- 						<form:input path="subBidPrice" htmlEscape="false"  placeholder="单位：万元"  class="form-control required"/> --%>
					</td>
		  		</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>现场负责人：</label></td>
					<td class="width-35">
						<form:input path="presentDireId" htmlEscape="false"    class="form-control required"/>
					</td>
					<td class="width-15 active"><label class="pull-right">现场负责人号码：</label></td>
					<td class="width-35">
						<form:input path="presentDireTel" htmlEscape="false"  placeholder="请输入11位手机号码"  class="form-control  isTel"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>项目负责人：</label></td>
					<td class="width-35">
						<form:input path="programDireId" htmlEscape="false"    class="form-control required"/>
					</td>
					<td class="width-15 active"><label class="pull-right">项目负责人号码：</label></td>
					<td class="width-35">
						<form:input path="programDireTel" htmlEscape="false"   placeholder="请输入11位手机号码"   class="form-control  isTel"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>安全员：</label></td>
					<td class="width-35">
						<form:input path="saverId" htmlEscape="false"    class="form-control required"/>
					</td>
					<td class="width-15 active"><label class="pull-right">安全员号码：</label></td>
					<td class="width-35">
						<form:input path="saverTel" htmlEscape="false"   placeholder="请输入11位手机号码"   class="form-control  isTel"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>递交人：</label></td>
					<td class="width-35">
						<form:input path="submiter" htmlEscape="false"    class="form-control required"/>
					</td>
					<td class="width-15 active"><label class="pull-right">递交人号码：</label></td>
					<td class="width-35">
						<form:input path="submitTel" htmlEscape="false"   placeholder="请输入11位手机号码"   class="form-control  isTel"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">现场所需材料：</label></td>
					<td class="width-35">
						<form:textarea path="meterials" htmlEscape="false" rows="4"    class="form-control "/>
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
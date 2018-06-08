<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>招标信息管理管理</title>
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
			//从url请求项目集合，参数：项目名称-subpackageProgramName
			$("#test_data").bsSuggest({
				  idField : "id\" style=\"display:none\"",
				  keyField : "word",
				  getDataMethod : "url",
				  url : "${ctx}/tendermanage/tender/getSubpackageProgramList?subpackageProgramName=",
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
				    	  console.log(json[i]);
				          data.value.push({
				        	  "id\" style=\"display:none\"":json[i].id,
				              "word":json[i].subpackageProgramName
				              })
				          }
				          return data
				  }
			  });
			
			validateForm = $("#inputForm").validate({
				
				submitHandler: function(form){
					var subpackageProgramId = $("#test_data").attr("data-id");
					if(subpackageProgramId != null && subpackageProgramId != ""){
						$("#subpackageProgramId").val(subpackageProgramId);
 					}
					jp.post("${ctx}/tendermanage/tender/save",$('#inputForm').serialize(),function(data){
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
			
	        $('#openBidDate').datetimepicker({
				 format: "YYYY-MM-DD HH:mm:ss"
		    });
		});
	</script>
	<script src="${ctxStatic}/plugin/js/bootstrap-suggest.min.js"></script>
</head>
<body class="bg-white">
		<form:form id="inputForm" modelAttribute="tender" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
		<table class="table table-bordered">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>招标工程名称：</label></td>
					<td class="width-35">
						<input type="hidden" class="form-control" name= "subpackageProgram.id" id = "subpackageProgramId" value = "${tender.subpackageProgram.id}">
						<div class="row">
			                <div class="col-lg-2">
			                    <div class="input-group">
			                        <input type="text" class="form-control required"  readOnly="true" id="test_data" value = "${tender.subpackageProgram.subpackageProgramName}">
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
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>保证金金额：</label></td>
					<td class="width-35">
						<form:input path="deposit" htmlEscape="false"  placeholder="单位：万元"  class=" form-control isFloatGteZero required"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">开标地点：</label></td>
					<td class="width-35">
						<form:input path="openBidAddr" htmlEscape="false"  class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">质量要求：</label></td>
					<td class="width-35">
						<form:input path="quality" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>投标截止时间：</label></td>
					<td class="width-35">
<!-- 						<p class="input-group"> -->
							<div class='input-group form_datetime' id='openBidDate'>
			                    <input type='text'  name="openBidDate" class="form-control required"  value="<fmt:formatDate value="${tender.openBidDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
			                    <span class="input-group-addon">
			                        <span class="glyphicon glyphicon-calendar"></span>
			                    </span>
			                </div>
<!-- 			            </p> -->
					</td>
					<td class="width-15 active"><label class="pull-right">工期要求：</label></td>
					<td class="width-35">
						<form:input path="buildDate" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>招标负责人：</label></td>
					<td class="width-35">
						<form:input path="tenderDirector" htmlEscape="false"    class="form-control required"/>
					</td>
					<td class="width-15 active"></td>
					<td class="width-35">
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">招标控制价：</label></td>
					<td class="width-35">
						<form:textarea path="tenderCtrlPrice" htmlEscape="false" rows="4"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">招标范围：</label></td>
					<td class="width-35">
						<form:textarea path="arrange" htmlEscape="false" rows="4"    class="form-control "/>
					</td>
					
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">评标办法：</label></td>
					<td class="width-35">
						<form:textarea path="evaluateMethod" htmlEscape="false" rows="4"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">备注：</label></td>
					<td class="width-35">
						<form:textarea path="remarks" htmlEscape="false" rows="4"    class="form-control "/>
					</td>
		  		</tr>
		  		<tr>
					<td class="width-15 active"><label class="pull-right">工程概况：</label></td>
					<td class="width-35">
						<form:textarea path="projectProfile" htmlEscape="false" rows="4"    class="form-control "/>
					</td>
					<td class="width-15 active"></td>
					<td class="width-35">
					</td>
		  		</tr>
		 	</tbody>
		</table>
	</form:form>
</body>
</html>
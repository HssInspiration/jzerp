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
// 				  showBtn: false,     //不显示下拉按钮
// 			      delayUntilKeyup: true, //获取数据的方式为 firstByUrl 时，延迟到有输入/获取到焦点时才请求数据
				  idField : "id\" style=\"display:none\"",
				  keyField : "word",
				  getDataMethod : "url",
				  url : "${ctx}/tendermanage/subpackageProgram/getProgramList?programName=",
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
			$("#test_data").on('onSetSelectValue', function (e, keyword) {
				console.log('onSetSelectValue: ', keyword);
				//思路：
				//1.下拉变化时获取id;
				var id = keyword.id;
				console.log(id);
				var jsonData = JSON.stringify({"id":id});
				//2.获取完id传入后台获取对应的类型集合;
				$.ajax({
					url:"${ctx}/tendermanage/subpackageProgram/getProgramTypeById",
					async:false,
					data:jsonData,
					type:"post",
					contentType:"application/json;charset=utf-8",
					dataType:"json",
					success:function(data){
						console.log("获取成功！"+data);
						$("#subProgramType").empty();  //每次获取成功后清空下拉列表()
						//$("#subProgramType").attr("class","form-control required error");
						$("#subProgramType").append("<option value=''>--与主项目下类别一致--</option>");
						//3.将获得的类型集合与表单中的子项目类别做匹配(点击下拉时可直接显示对应中标项目的下属子类别);
						if(data != null && data.length > 0){  
	                        for(var i = 0;i < data.length;i++){  
	                            var proType = data[i];  
	                            var programType;
	                            switch(proType){
	                            case '1':
	                            	programType="穿越"
	                            	break;
	                            case '2':
	                            	programType="埋管"
	                            	break;
	                            case '3':
	                            	programType="土方"
	                            	break;
	                            case '4':
	                            	programType="焊接"
	                            	break;
	                            case '5':
	                            	programType="顶管"
	                            	break;
	                            case '6':
	                            	programType="探伤"
	                            	break;
	                            case '7':
	                            	programType="防腐"
	                            	break;
	                            };
	                            $("#subProgramType").append("<option value = \"" + proType + "\">"+ programType +"</option>");  
	                        }  
	                    }else{  
	                        $("#subProgramType").append("<option> </option>");  
	                    }  
					},
					error:function(){
						console.log("获取失败！");
					}
				});
				
				//附加：选取之后下次再进行调用只显示没有选过的类型。
			});//下拉框值选中时调用
			
			validateForm = $("#inputForm").validate({
				submitHandler: function(form){
					var programId = $("#test_data").attr("data-id");
					if(programId != null && programId != ""){
						$("#programId").val(programId);
 					}
					jp.post("${ctx}/tendermanage/subpackageProgram/save",$('#inputForm').serialize(),function(data){
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
			
			$('#planToStart').datetimepicker({
				 format: "YYYY-MM-DD HH:mm:ss"
		    });
			$('#planToEnd').datetimepicker({
				 format: "YYYY-MM-DD HH:mm:ss"
		    });
			
		});
		
	</script>
	<script src="${ctxStatic}/plugin/js/bootstrap-suggest.min.js"></script>
</head>
<body class="bg-white">
		<form:form id="inputForm" modelAttribute="subpackageProgram" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>
		<table class="table table-bordered">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>招标编号：</label></td>
					<td class="width-35">
						<form:input path="subpackageProgramNum" htmlEscape="false" readOnly="true" value="${subpackageProgramNum}"  class="form-control required"/>
					</td>
					<td class="width-15 active"><label class="pull-right">招标单位名称：</label></td>
					<td class="width-35">
						<form:input path="tenderCompany" htmlEscape="false" readOnly="true" value = "江苏金卓建设工程有限公司" class="form-control"/>
					</td>
				</tr>
				
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>所属项目名称：</label></td>
					<td class="width-35">
						 <input type="hidden" class="form-control" name= "program.id" id = "programId" value = "${subpackageProgram.program.id}">
					
					<c:if test="${isAdd}">
					<div class="row">
		                <div class="col-lg-6">
		                    <div class="input-group">
		                        <input type="text"  class="form-control required" id="test_data" value = "${subpackageProgram.program.programName}">
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
		            	 <input type="text"  class="form-control required" readOnly="true" id="test_data" value = "${subpackageProgram.program.programName}">
		            </c:if>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>子项目名称：</label></td>
					<td class="width-35">
						<form:input path="subpackageProgramName" htmlEscape="false"  class="form-control required"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>招标工程类别：</label></td>
					<td class="width-35">
						<c:if test="${isAdd}">
							<form:select path="subProgramType" class="form-control required">
								<form:option value="" label="--与主项目下类别一致--"/>
							</form:select>
						</c:if>
						<c:if test="${edit}">
							<form:select path="subProgramType" class="form-control" disabled="true">
								<form:options items="${fns:getDictList('programtype')}" disabled="true" itemLabel="label" itemValue="value" id = "programtype" htmlEscape="false"/>
							</form:select>
						</c:if>
						<span id="typeAlreadyExsist" style = "font-weight:bold;"></span>
					</td>
					<td  class="width-15 active">	
			   		<label class="pull-right"><font color="red">*</font>计划开始时间:</label>
			   	    </td>
		            <td  class="width-35" >
						<div class='input-group form_datetime' id='planToStart'>
		                    <input type='text'  name="planToStart" class="form-control required"  value="<fmt:formatDate value="${subpackageProgram.planToStart}" pattern="yyyy-MM-dd HH:mm"/>"/>
		                    <span class="input-group-addon">
		                        <span class="glyphicon glyphicon-calendar"></span>
		                    </span>
		                </div>						            
			        </td>
				</tr>
				<script>
					$().ready(function(){
						$("#subProgramType").bind("change",function(){//选择改变后先做验证，是否已经登记
							var programId = $("#test_data").attr("data-id");//所属项目id
							var value = $("#subProgramType").val();//类型
							console.log(value);
							var subProgramNum = $("#subpackageProgramNum").val();//编号
							if(subProgramNum.length>14){//若为修改将编号后面两位去掉
								subProgramNum = subProgramNum.substring(0,subProgramNum.length-2);
							}
							console.log(subProgramNum);
							var str,newNum;
							var jsonData = JSON.stringify({"id":programId});
							//发送验证：
							$.ajax({
								url:"${ctx}/tendermanage/subpackageProgram/getTypeByParentId",
								data:jsonData,
								type:"post",
								contentType:"application/json;charset=utf-8",
								dataType:"json",
								success:function(data){
									console.log(data);
									console.log(data.length);
									for(var i=0;i<=data.length-1;i++){
										console.log(data[i]);
										if(data[i]==value){
											console.log("类别重复！");
											$("#typeAlreadyExsist").html("×当前类别已存在，请重新选择");
											$("#typeAlreadyExsist").css("color","red");
											break;
										}else{
											$("#typeAlreadyExsist").html("OK!");
											$("#typeAlreadyExsist").css("color","green");
										}
									}
								},
								error:function(){
									console.log("失败！");
								}
							});
							switch (value){
							case '1':
								str = "CY";//穿越
								newNum = subProgramNum+str;
								$("#subpackageProgramNum").val(newNum);
								break;
							case '2':
								str = "MG";//埋管
								newNum = subProgramNum+str;
								$("#subpackageProgramNum").val(newNum);
								break;
							case '3':
								str = "TF";//土方
								newNum = subProgramNum+str;
								$("#subpackageProgramNum").val(newNum);
								break;
							case '4':
								str = "HJ";//焊接
								newNum = subProgramNum+str;
								$("#subpackageProgramNum").val(newNum);
								break;
							case '5':
								str = "DG";//顶管
								newNum = subProgramNum+str;
								$("#subpackageProgramNum").val(newNum);
								break;
							case '6':
								str = "TS";//探伤
								newNum = subProgramNum+str;
								$("#subpackageProgramNum").val(newNum);
								break;
							case '7':
								str = "FF";//防腐
								newNum = subProgramNum+str;
								$("#subpackageProgramNum").val(newNum);
								break;
						}
						});
					});
				</script>
				<tr>
					<td  class="width-15 active">	
			   		<label class="pull-right"><font color="red">*</font>计划结束时间:</label>
			   	    </td>
		            <td  class="width-35" >
						<div class='input-group form_datetime' id='planToEnd'>
		                    <input type='text'  name="planToEnd" class="form-control required"  value="<fmt:formatDate value="${subpackageProgram.planToEnd}" pattern="yyyy-MM-dd HH:mm"/>"/>
		                    <span class="input-group-addon">
		                        <span class="glyphicon glyphicon-calendar"></span>
		                    </span>
		                </div>						            
			        </td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">子项目内容：</label></td>
					<td class="width-35">
						<form:textarea path="subpackageProgramCont" htmlEscape="false" rows="4"    class="form-control "/>
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
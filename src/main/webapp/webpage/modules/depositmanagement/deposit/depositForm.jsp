<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>保证金信息管理管理</title>
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
// 			var url = "${ctx}/deposit/getDepositTypeListById";//用来验证对应类型是否重复的url
			//从url请求项目集合，参数：项目名称-programName
			$("#test_data").bsSuggest({
				  idField : "id\" style=\"display:none\"",
				  keyField : "word",
				  getDataMethod : "url",
				  url : "${ctx}/deposit/getProgramList?programName=",
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
			
			//封装回调函数:用来验证对应类型是否重复
			var checkDepositType = function(jsonData,value){
					$.ajax({
						url:"${ctx}/deposit/getDepositTypeListById",
						data:jsonData,
						type:"post",
						contentType:"application/json;charset=utf-8",
						dataType:"json",
						success:function(data){
							console.log(data);
							console.log("data.length:"+data.length);
							if(data.length>0){
								for(var i=0;i<=data.length-1;i++){
									if(data[i]==value){
										console.log("类别重复！");
										$("#typeAlreadyExsist").html("×当前用途(类型)已存在，请重新选择！");
										$("#typeAlreadyExsist").css("color","red");
										break;
									}else{
										$("#typeAlreadyExsist").html("√OK!");
										$("#typeAlreadyExsist").css("color","green");
										break;
									}
								}
							}else{//数组长度为0，保证金类别不重复可添加
								$("#typeAlreadyExsist").html("√OK!");
								$("#typeAlreadyExsist").css("color","green");
								if(value==1){
									var url1 = "${ctx}/deposit/getDepositByProId";
									console.log("6666699999");
									getDepositByProId(url1,jsonData);
								}
								
							}
						},
						error:function(){
							console.log("失败！");
						}
					});
				}
			
			//验证当前此项目的中标状态（若中标可继续申请；若不中标默认为投标保证金其他不可选）
			$("#test_data").on('onSetSelectValue', function (e, keyword) {
				console.log('onSetSelectValue: ', keyword);
				//思路：
				//1.下拉变化时获取id;
				var id = keyword.id;
				var name = keyword.key;
				
				var jsonData = JSON.stringify({"id":id});
// 				2.获取完id传入后台获取对应的数据;
				$.ajax({
					url:"${ctx}/deposit/getProgramById",
					async:false,
					data:jsonData,
					type:"post",
					contentType:"application/json;charset=utf-8",
					dataType:"json",
					success:function(data){
						console.log("获取成功:"+data);
						var status = data.status;
						switch(status){//依据状态给出提示或默认操作
							case 0:
								console.log("当前项目为自由状态");
								//给出提示并将保证金的类型选择按钮设置为不可用
								$("#choosePro").html("×当前项目自由，不可申请保证金！");
								$("#choosePro").css("color","red");
								$("#depositType").val(0);//设置下拉框的值选中
								$(".readOnly").attr({ readonly: 'true' });//设置输入框失效
								$("#depositType").attr({disabled: 'true'});//设置输入框失效
								break;
							case 1:
								console.log("当前项目为招标状态");
								//给出提示并将保证金的类型选择按钮设置为不可用
								$("#choosePro").html("项目招标中，只可登记投标保证金！");
								$("#choosePro").css("color","red");
								$("#depositType").val(1);//设置下拉框的值选中
								var value = $("#depositType").val();//类型
								checkDepositType(jsonData,value);//调用函数判断保证金书类别是否重复
								$("#depositType").attr({disabled: 'true'});//设置输入框失效
								$(".readOnly").removeAttr("readOnly");//设置输入框生效
								break;
							case 2:
								console.log("当前项目为施工状态");
								$("#choosePro").html("项目施工中，请选择保证金！");
								$("#choosePro").css("color","green");
								$('#depositType').removeAttr("disabled"); //设置下拉框生效
								$(".readOnly").removeAttr("readOnly");//设置输入框生效
								break;
							case 3:
								console.log("当前项目为竣工状态");
								$("#choosePro").html("×当前项目已竣工，不可申请保证金！");
								$("#choosePro").css("color","red");
								$("#depositType").val(0);//设置下拉框的值选中
								$(".readOnly").attr({ readonly: 'true' });//设置输入框失效
								$("#depositType").attr({disabled: 'true'});//设置输入框失效
								break;
							case 4:
								console.log("当前项目为停工状态");
								$("#choosePro").html("×当前项目已停工，不可申请保证金！");
								$("#choosePro").css("color","red");
								$("#depositType").val(0);//设置下拉框的值选中
								$(".readOnly").attr({ readonly: 'true' });//设置输入框失效
								$("#depositType").attr({disabled: 'true'});//设置输入框失效
								break;
							case 5:
								console.log("当前项目为结案状态");
								//给出提示并将保证金的类型选择按钮设置为不可用
								$("#choosePro").html("×当前项目已结案，不可申请保证金！");
								$("#choosePro").css("color","red");
								$("#depositType").val(0);//设置下拉框的值选中
								$(".readOnly").attr({ readonly: 'true' });//设置输入框失效
								$("#depositType").attr({disabled: 'true'});//设置输入框失效
								break;
							case 6:
								console.log("当前项目为未中标状态");
								//给出提示并将保证金的类型选择按钮设置为不可用
								$("#choosePro").html("项目未中标，只可登记投标保证金！");
								$("#choosePro").css("color","red");
// 								$("#depositType option[value='1']").attr("selected", true);//设置下拉框的值选中
								$("#depositType").val(1);
								var value = $("#depositType").val();//类型
								checkDepositType(jsonData,value);//下拉选中后调用
								$("#depositType").attr({disabled: 'true'});//设置输入框失效
								$(".readOnly").removeAttr("readOnly");//设置输入框生效
								break;
							default:
								console.log("当前项目为未知状态");
								//给出提示并将保证金的类型选择按钮设置为不可用
								$("#choosePro").html("×当前项目状态未知，不可申请保证金！");
								$("#choosePro").css("color","red");
								$(".readOnly").attr({ readonly: 'true' });//设置输入框失效
								$("#depositType").attr({disabled: 'true'});//设置输入框失效
								break;
						
						}
						
					},
					error:function(){
						console.log("获取失败！");
					}
				});
				//项目已选将保证金名称设为“项目名称+保证金”
				$("#depositName").val(name+"保证金");
				
// 				$("#choosePro").remove();//移除未选择时提示
			});//下拉框值选中时调用
			
			$("#depositType").bind("change",function(){//选择改变后先做验证，是否已经登记
				var programId = $("#test_data").attr("data-id");//所属项目id
				var value = $("#depositType").val();//类型
				var proId = $("#programId").val();
				
				if(proId==null){
					//若项目为空给出提示
					$("#choosePro").html("×请先选择项目名称！");
					$("#choosePro").css("color","red");
				}else{
					var jsonData = JSON.stringify({"id":programId});//项目id
					console.log("programId:"+programId);
					//发送验证：
					checkDepositType(jsonData,value);
				}
			});
			
			//文档加载时根据项目id判断
			var proId = $("#programId").val();
			console.log("proId:"+proId);
			if(proId != null && proId.length != 0){//非空--代表修改
				var jsonData = JSON.stringify({"id":proId});//项目id
				var value = $("#depositType").val();//类型
				//进行验证
				$.ajax({
					url:"${ctx}/deposit/getProgramById",
// 					async:false,
					data:jsonData,
					type:"post",
					contentType:"application/json;charset=utf-8",
					dataType:"json",
					success:function(data){
						console.log("获取成功:"+data);
						var status = data.status;
						console.log(status);
						if(status!=2){
							console.log("非施工项目！");
							$("#depositType").attr({disabled: 'true'});//设置下拉框失效
						}
					},
					error:function(){
						console.log("获取失败！");
					}
				});
			}
			
			validateForm = $("#inputForm").validate({
				submitHandler: function(form){
					var programId = $("#test_data").attr("data-id");
					if(programId != null && programId != ""){
						$("#programId").val(programId);
 					}
				    $("[disabled]").each(function() {//移除disable,后台方可取值  
				        if (parseInt($(this).val()) != -1) {  
				            $(this).attr("disabled", false);  
				        }  
				    });  
					console.log("programId3:"+$("#programId").val());
					jp.post("${ctx}/deposit/save",$('#inputForm').serialize(),function(data){
						if(data.success){
	                    	$table.bootstrapTable('refresh');
	                    	jp.success(data.msg);
	                    	jp.close($topIndex);//关闭dialog

	                    }else{
            	  			jp.error(data.msg);
            	  			$("#depositType").attr({disabled: 'true'});//设置类型输入框失效
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
			$('#applyDate').datetimepicker({
				 format: "YYYY-MM-DD HH:mm:ss"
		    });
		});
	</script>
	<script src="${ctxStatic}/plugin/js/bootstrap-suggest.min.js"></script>
	<script src="${ctxStatic}/plugin/js/data.js"></script>
</head>
<body class="bg-white">
		<form:form id="inputForm" modelAttribute="deposit" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
		<table class="table table-bordered">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>保证金编号：</label></td>
					<td class="width-35">
						<form:input path="depositNum" htmlEscape="false" readOnly = "true" class="form-control required"/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>保证金名称：</label></td>
					<td class="width-35">
						<form:input path="depositName" htmlEscape="false" class="form-control required readOnly"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>项目名称：</label></td>
					<td class="width-35">
					<input type="hidden" class="form-control" name= "program.id" id ="programId" value = "${deposit.program.id}">
					<c:if test="${isAdd }">
					<div class="row">
		                <div class="col-lg-2">
		                    <div class="input-group">
		                        <input type="text" class="form-control required" id="test_data" value = "${deposit.program.programName}">
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
		            	<input type="text" class="form-control required" readOnly="true" value = "${deposit.program.programName}">
		            </c:if>
		            <span id="choosePro" style = "font-weight:bold;"></span>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>申请人：</label></td>
					<td class="width-35">
						<form:input path="applyer" htmlEscape="false"  class="form-control required readOnly"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>用途(类型)：</label></td>
					<td class="width-35">
<%-- 						<c:if test="${isAdd}"> --%>
							<form:select path="depositType"  class="form-control required ">
								<form:option value="0" label="--请选择用途（类型）--"/>
								<form:options items="${fns:getDictList('deposit_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
							</form:select>
<%-- 						</c:if> --%>
<%-- 						<c:if test="${edit}"> --%>
<%-- 							<form:select path="depositType"  class="form-control required " disabled="true"> --%>
<%-- 								<form:option value="0" label="--请选择用途（类型）--"/> --%>
<%-- 								<form:options items="${fns:getDictList('deposit_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/> --%>
<%-- 							</form:select> --%>
<%-- 						</c:if> --%>
						
						<span id="typeAlreadyExsist" style = "font-weight:bold;"></span>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>申请时间：</label></td>
					<td class="width-35">
						<div class='input-group form_datetime' id='applyDate'>
		                    <input type='text'  name="applyDate" class="form-control required readOnly"  value="<fmt:formatDate value="${deposit.applyDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
		                    <span class="input-group-addon">
		                        <span class="glyphicon glyphicon-calendar"></span>
		                    </span>
			            </div>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>缴纳金额(万元)：</label></td>
					<td class="width-35">
						<c:if test="${isAdd }">
							<form:input path="payCount" htmlEscape="false"  placeholder="单位：万元"  class="form-control required readOnly"/>
						</c:if>
						
						<c:if test="${edit}">
							<c:if test="${deposit.depositType eq 1}">
								<form:input path="payCount" htmlEscape="false" readOnly="true"  placeholder="单位：万元"  class="form-control required readOnly"/>
							</c:if>
							<c:if test="${deposit.depositType ne 1}">
								<form:input path="payCount" htmlEscape="false"  placeholder="单位：万元"  class="form-control required readOnly"/>
							</c:if>
						</c:if>
					</td>
					<td class="width-15 active"><label class="pull-right">备注信息：</label></td>
					<td class="width-35">
						<form:textarea path="remarks" htmlEscape="false" rows="4"    class="form-control readOnly "/>
 					</td>
				</tr>
		 	</tbody>
		</table>
	</form:form>
</body>
</html>
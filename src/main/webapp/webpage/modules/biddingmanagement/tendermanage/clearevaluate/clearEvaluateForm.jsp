<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>清评标管理管理</title>
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
			//已参投的单位名称模糊匹配框
			$("#test_data1").bsSuggest({
				  idField : "id\" style=\"display:none\"",
				  keyField : "word",
				  getDataMethod : "url",
				  url : "${ctx}/tendermanage/clearevaluate/getTenderList?subpackageProgramName=",
				  contentType: "application/json;charset = utf-8",
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
				    	  console.log(json[i].id);
				          data.value.push({
				        	  "id\" style=\"display:none\"":json[i].id,
				              "word":json[i].subpackageProgram.subpackageProgramName
				              })
				          }
				          return data
				  }
			  });
				
			$("#test_data1").on('onSetSelectValue', function (e, keyword) {//下拉框值选中时调用
		        console.log('onSetSelectValue: ', keyword);
		        var id = keyword.id;
		        var word = keyword.key;
		        console.log("id:"+id+"word:"+word);
		        var jsonData = JSON.stringify({"subpackageProgramName":word});
		        //当子项目选择之后，招标单位下拉框选项才会生效
		       $.ajax({
					url:"${ctx}/tendermanage/clearevaluate/getBidCompanyList",
					data:jsonData,
					type:"post",
					dataType:"json",
					contentType:"application/json;charset=utf-8",
					success:function(data){
						//添加提示信息:
						console.log(data);
						$("#bidComp").empty();  //每次获取成功后清空下拉列表()
						for(var i = 0;i<=data.length-1;i++){
							var companyName = data[i].company.companyName;
							var subBidCompanyId = data[i].id;
							console.log(companyName+"   "+subBidCompanyId);
							 $("#bidComp").append("<option value = \"" +subBidCompanyId+ "\">"+ companyName +"</option>");  
						}
					},
					error:function(){
						console.log("回调失败！");
					}
				});
			});
			validateForm = $("#inputForm").validate({
				submitHandler: function(form){
					var subBidCompanyId = $("#test_data2").attr("data-id");
					if(subBidCompanyId != null && subBidCompanyId != ""){
						$("#subBidCompanyId").val(subBidCompanyId);
						console.log($("#tenderId").val());
					}
					
					var tenderId = $("#test_data1").attr("data-id");
					if(tenderId != null && tenderId != ""){
						$("#tenderId").val(tenderId);
						console.log($("#tenderId").val());
					}
					jp.post("${ctx}/tendermanage/clearevaluate/save",$('#inputForm').serialize(),function(data){
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
			
		  $('#accessDate').datetimepicker({
				 format: "YYYY-MM-DD HH:mm:ss"
		  });
		});
		
		function addRow(list, idx, tpl, row){
			$(list).append(Mustache.render(tpl, {
				idx: idx, delBtn: true, row: row
			}));
			$(list+idx).find("select").each(function(){
				$(this).val($(this).attr("data-value"));
			});
			$(list+idx).find("input[type='checkbox'], input[type='radio']").each(function(){
				var ss = $(this).attr("data-value").split(',');
				for (var i=0; i<ss.length; i++){
					if($(this).val() == ss[i]){
						$(this).attr("checked","checked");
					}
				}
			});
			$(list+idx).find(".form_datetime").each(function(){
				 $(this).datetimepicker({
					 format: "YYYY-MM-DD HH:mm:ss"
			    });
			});
		}
		function delRow(obj, prefix){
			var id = $(prefix+"_id");
			var delFlag = $(prefix+"_delFlag");
			if (id.val() == ""){
				$(obj).parent().parent().remove();
			}else if(delFlag.val() == "0"){
				delFlag.val("1");
				$(obj).html("&divide;").attr("title", "撤销删除");
				$(obj).parent().parent().addClass("error");
			}else if(delFlag.val() == "1"){
				delFlag.val("0");
				$(obj).html("&times;").attr("title", "删除");
				$(obj).parent().parent().removeClass("error");
			}
		}
	</script>
	<script src="${ctxStatic}/plugin/js/bootstrap-suggest.min.js"></script>
</head>
<body class="bg-white">
		<form:form id="inputForm" modelAttribute="clearEvaluate"  class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>
		<table class="table table-bordered">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>子项目名称：</label></td> 
					<td class="width-35">
						<input type="hidden" class="form-control" name= "tender.id" id = "tenderId" value = "${clearEvaluate.subBidCompany.tender.id}">
						<div class="row">
			                <div class="col-lg-12">
			                    <div class="input-group">
			                        <input type="text" class="form-control required" id="test_data1" <c:if test="${edit }">readOnly = "true"</c:if> value = "${clearEvaluate.subBidCompany.tender.subpackageProgram.subpackageProgramName}">
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
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>业绩：</label></td>
					<td class="width-35">
						<form:input path="performance" htmlEscape="false"    class="form-control required"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>投标单位：</label></td> 
					<td class="width-35">
					<c:if test="${isAdd}">
						<form:select path="subBidCompany.id" class="form-control required" id="bidComp"> 
							<form:option value="" label="----请选择投标单位----"/>
						</form:select>
					</c:if>
					<c:if test="${edit}">
						<form:select path="subBidCompany.id" readOnly="true" class="form-control " id="bidComp"> 
							<form:option value="${clearEvaluate.subBidCompany.id}" label="${clearEvaluate.subBidCompany.company.companyName}"/>
						</form:select>
					</c:if>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>投标书密封情况：</label></td>
					<td class="width-35">
						<form:input path="secretCircumstances" htmlEscape="false"    class="form-control required"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>工期：</label></td>
					<td class="width-35">
						<form:input path="buildDate" htmlEscape="false"    class="form-control required"/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>施工组织设计：</label></td>
					<td class="width-35">
						<form:input path="design" htmlEscape="false"    class="form-control required"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>投标书填写情况：</label></td>
					<td class="width-35">
						<form:input path="writeCircumstances" htmlEscape="false"    class="form-control required"/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>施工企业资质证书：</label></td>
					<td class="width-35">
						<form:input path="certificate" htmlEscape="false"    class="form-control required"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">备注信息：</label></td>
					<td class="width-35">
						<form:textarea path="remarks" htmlEscape="false" rows="4"    class="form-control "/>
					</td>
					<td class="width-15 active"></td>
					<td class="width-35">
					</td>
				</tr>
		 	</tbody>
		</table>
		<div class="tabs-container">
            <ul class="nav nav-tabs">
				<li class="active"><a data-toggle="tab" href="#tab-1" aria-expanded="true"><font color="red">*</font>评标人员：</a>
                </li>
            </ul>
            <div class="tab-content">
				<div id="tab-1" class="tab-pane fade in  active">
			<a class="btn btn-white btn-sm" onclick="addRow('#evaluateUserList', evaluateUserRowIdx, evaluateUserTpl);evaluateUserRowIdx = evaluateUserRowIdx + 1;" title="新增"><i class="fa fa-plus"></i> 新增</a>
			<table class="table table-striped table-bordered table-condensed">
				<thead>
					<tr>
						<th class="hide"></th>
						<th><font color="red">*</font>评标人员</th>
						<th>备注信息</th>
						<th width="10">&nbsp;</th>
					</tr>
				</thead>
				<tbody id="evaluateUserList">
				</tbody>
			</table>
			<script type="text/template" id="evaluateUserTpl">//<!--
				<tr id="evaluateUserList{{idx}}">
					<td class="hide">
						<input id="evaluateUserList{{idx}}_id" name="evaluateUserList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
						<input id="evaluateUserList{{idx}}_delFlag" name="evaluateUserList[{{idx}}].delFlag" type="hidden" value="0"/>
					</td>

					<td>
						<select id="evaluateUserList{{idx}}_user" name="evaluateUserList[{{idx}}].user.id" data-value="{{row.user.id}}" class="form-control  required">
							<option value=""></option>
							<c:forEach items="${userList}" var="user">
								<option value="${user.id}">${user.name}</option>
							</c:forEach>
						</select>
					</td>

					<td>
						<input id="evaluateUserList{{idx}}_remarks" name="evaluateUserList[{{idx}}].remarks" rows="4"  value="{{row.remarks}}"  class="form-control">
					</td>
					
					<td class="text-center" width="10">
						{{#delBtn}}<span class="close" onclick="delRow(this, '#evaluateUserList{{idx}}')" title="删除">&times;</span>{{/delBtn}}
					</td>
				</tr>//-->
			</script>
			<script type="text/javascript">
				var evaluateUserRowIdx = 0, evaluateUserTpl = $("#evaluateUserTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
				$(document).ready(function() {
					var data = ${fns:toJson(clearEvaluate.evaluateUserList)};
					for (var i=0; i<data.length; i++){
						addRow('#evaluateUserList', evaluateUserRowIdx, evaluateUserTpl, data[i]);
						evaluateUserRowIdx = evaluateUserRowIdx + 1;
					}
				});
			</script>
			</div>
		</div>
		</div>
		</form:form>
</body>
</html>
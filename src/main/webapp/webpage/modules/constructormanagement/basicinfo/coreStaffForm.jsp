<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>投标管理管理</title>
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
			//人员名称模糊匹配框
			$("#test_data").bsSuggest({
				  idField : "id\" style=\"display:none\"",
				  keyField : "word",
				  getDataMethod : "url",
				  url : "${ctx}/basicinfo/getAllUserList?name=",
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
			$("#test_data").on('onSetSelectValue', function (e, keyword) {
				console.log('onSetSelectValue: ', keyword);
				//思路：
				//1.下拉变化时获取id;
				var id = keyword.id;
				console.log(id);
				var jsonData = JSON.stringify({"id":id});
				//2.获取完id传入后台获取对应的类型集合;
				$.ajax({
					url:"${ctx}/basicinfo/getCoreStaffByUserId",
					async:false,
					data:jsonData,
					type:"post",
					contentType:"application/json;charset=utf-8",
					dataType:"json",
					success:function(data){
						console.log("获取成功！"+data);
						if(data==true){
							$("#repeatObj").html("×当前人员已登记，请重新选择！");
							$("#repeatObj").css("color","red");
						}else if(data==false){
							$("#repeatObj").html("√OK!");
							$("#repeatObj").css("color","green");
						}
					},
					error:function(){
						console.log("获取失败！");
					}
				});
			});//下拉框值选中时调用
			
			
			validateForm = $("#inputForm").validate({
				submitHandler: function(form){
					var userId = $("#test_data").attr("data-id");
					console.log("00000:"+userId)
					if(userId != null && userId != ""){
						$("#userId").val(userId);
					}
					
					jp.post("${ctx}/basicinfo/save",$('#inputForm').serialize(),function(data){
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
		<form:form id="inputForm" modelAttribute="coreStaff" action="${ctx}/basicinfo/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>
		<table class="table table-bordered">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>人员姓名：</label></td>
					<td class="width-35">
						<input type="hidden" class="form-control" name= "user.id" id = "userId" value = "${coreStaff.user.id}">
						<div class="row">
			                <div class="col-lg-12">
			                    <div class="input-group">
			                        <input type="text" class="form-control required" id="test_data" 
			                         value = "${coreStaff.user.name}">
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
			            <span id="repeatObj" style = "font-weight:bold;"></span>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>身份证号码：</label></td>
					<td class="width-35">
						<form:input path="identityNum" htmlEscape="false"  class="form-control required"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>手机号码：</label></td>
					<td class="width-35">
						<form:input path="phoneNum" htmlEscape="false"  class="form-control required"/>
					</td>
				</tr>
<!-- 				<tr> -->
<!-- 					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>所属机构：</label></td> -->
<!-- 					<td class="width-35"> -->
<%-- 						<form:input path="user.office.name" htmlEscape="false"  class="form-control required"/> --%>
<!-- 					</td> -->
<!-- 				</tr> -->
				<tr>
					<td class="width-15 active"><label class="pull-right">备注信息：</label></td>
					<td class="width-35">
<!-- 					不可删，mustache模板 -->
<!-- 					<td> -->
<!-- 						<input id="staffCertificateList{{idx}}_certificateSecondNum" name="staffCertificateList[{{idx}}].certificateSecondNum" type="hidden" value="{{row.certificateSecondNum}}"    class="form-control "/> -->
<!-- 					</td> -->
					
<!-- 					<td> -->
<!-- 						<input id="staffCertificateList{{idx}}_certificateThirdNum" name="staffCertificateList[{{idx}}].certificateThirdNum" type="hidden" value="{{row.certificateThirdNum}}"    class="form-control "/> -->
<!-- 					</td> -->
<!-- 						<input id="staffCertificateList{{idx}}_regisDate" name="staffCertificateList[{{idx}}].regisDate" type="text" value="{{row.regisDate}}"    class="form-control required "/> -->
<!-- 						<input id="staffCertificateList{{idx}}_invalidDate" name="staffCertificateList[{{idx}}].invalidDate" type="text" value="{{row.invalidDate}}"    class="form-control required "/> -->
<!-- 						<input id="staffCertificateList{{idx}}_certificateMajor" name="staffCertificateList[{{idx}}].certificateMajor" type="text" value="{{row.certificateMajor}}"    class="form-control required "/> -->
<!-- 						<input id="staffCertificateList{{idx}}_certificateClass" name="staffCertificateList[{{idx}}].certificateClass" type="text" value="{{row.certificateClass}}"    class="form-control required"/> -->
<!-- 						<input id="staffCertificateList{{idx}}_certificateName" name="staffCertificateList[{{idx}}].certificateName" type="text" value="{{row.certificateName}}"    class="form-control required"/> -->
						<form:textarea path="remarks" htmlEscape="false" rows="4"    class="form-control "/>
					</td>
		  		</tr>
		 	</tbody>
		</table>
		<div class="tabs-container">
            <ul class="nav nav-tabs">
				<li class="active"><a data-toggle="tab" href="#tab-1" aria-expanded="true">人员证书表：</a>
                </li>
            </ul>
            <div class="tab-content">
				<div id="tab-1" class="tab-pane fade in  active">
			<a class="btn btn-white btn-sm" onclick="addRow('#staffCertificateList', staffCertificateRowIdx, staffCertificateTpl);staffCertificateRowIdx = staffCertificateRowIdx + 1;" title="新增"><i class="fa fa-plus"></i> 新增</a>
			<table class="table table-striped table-bordered table-condensed">
				<thead>
					<tr>
						<th class="hide"></th>
						<th><font color="red">*</font>证书名称</th>
						<th><font color="red">*</font>证书等级</th>
						<th><font color="red">*</font>对应专业</th>
						<th><font color="red">*</font>注册时间</th>
						<th><font color="red">*</font>失效时间</th>
						<th><font color="red">*</font>证书编号1</th>
<!-- 						<th>证书编号2</th> -->
<!-- 						<th>证书编号3</th> -->
						<th>注册证号</th>
						<th width="10">&nbsp;</th>
					</tr>
				</thead>
				<tbody id="staffCertificateList">
				</tbody>	
			</table>
			<script type="text/template" id="staffCertificateTpl">//<!--
				<tr id="staffCertificateList{{idx}}">
					<td class="hide">
						<input id="staffCertificateList{{idx}}_id" name="staffCertificateList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
						<input id="staffCertificateList{{idx}}_delFlag" name="staffCertificateList[{{idx}}].delFlag" type="hidden" value="0"/>
					</td>
					<td >
						<select id="staffCertificateList{{idx}}_certificateName" name="staffCertificateList[{{idx}}].certificateName" data-value="{{row.certificateName}}" class="form-control required m-b  ">
							<option value=""></option>
							<c:forEach items="${fns:getDictList('certificate_type')}" var="dict">
								<option value="${dict.value}">${dict.label}</option>
							</c:forEach>
						</select>
					</td>
					<td >
						<select id="staffCertificateList{{idx}}_certificateClass" name="staffCertificateList[{{idx}}].certificateClass" data-value="{{row.certificateClass}}" class="form-control  m-b  ">
							<option value=""></option>
							<c:forEach items="${fns:getDictList('normal_certificateclass')}" var="dict">
								<option value="${dict.value}">${dict.label}</option>
							</c:forEach>
						</select>
					</td>
					<td >
						<select id="staffCertificateList{{idx}}_certificateMajor" name="staffCertificateList[{{idx}}].certificateMajor" data-value="{{row.certificateMajor}}" class="form-control  m-b  ">
							<option value=""></option>
							<c:forEach items="${fns:getDictList('certificate_major')}" var="dict">
								<option value="${dict.value}">${dict.label}</option>
							</c:forEach>
						</select>
					</td>
					<td>
						<div class='input-group form_datetime' id="staffCertificateList{{idx}}_regisDate">
		                    <input type='text'  name="staffCertificateList[{{idx}}].regisDate" class="form-control required"  value="{{row.regisDate}}"/>
		                    <span class="input-group-addon">
		                        <span class="glyphicon glyphicon-calendar"></span>
		                    </span>
		                </div>		
					</td>
					<td>
						<div class='input-group form_datetime' id="staffCertificateList{{idx}}_invalidDate">
		                    <input type='text'  name="staffCertificateList[{{idx}}].invalidDate" class="form-control required"  value="{{row.invalidDate}}"/>
		                    <span class="input-group-addon">
		                        <span class="glyphicon glyphicon-calendar"></span>
		                    </span>
		                </div>	
					</td>
					<td>
						<input id="staffCertificateList{{idx}}_certificateFirstNum" name="staffCertificateList[{{idx}}].certificateFirstNum" type="text" value="{{row.certificateFirstNum}}"    class="form-control required"/>
					</td>
					<td>
						<input id="staffCertificateList{{idx}}_registrationNum" name="staffCertificateList[{{idx}}].registrationNum" type="text" value="{{row.registrationNum}}"    class="form-control "/>
					</td>
					<td class="text-center" width="10">
						{{#delBtn}}<span class="close" onclick="delRow(this, '#staffCertificateList{{idx}}')" title="删除">&times;</span>{{/delBtn}}
					</td>
				</tr>
//-->
			</script>
			<script type="text/javascript">
				var staffCertificateRowIdx = 0, staffCertificateTpl = $("#staffCertificateTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
				$(document).ready(function() {
					var data = ${fns:toJson(coreStaff.staffCertificateList)};
					for (var i=0; i<data.length; i++){
						addRow('#staffCertificateList', staffCertificateRowIdx, staffCertificateTpl, data[i]);
						staffCertificateRowIdx = staffCertificateRowIdx + 1;
					}
					
					//下拉级联处理：
					//1.若为建造师，显示建造师级别（一级、二级）
// 					$("#staffCertificateList{{idx}}_certificateName").bind("change",function(){
// 						console.log("2018-05-23:可编辑")
// 					});
					//2.若为职称证，显示工程师级别（工程师、助理工程师）
					//3.若为造价员，显示造价员级别（初级、中级、高级、造价师）
					
				});
			</script>
			</div>
		</div>
		</div>
<!-- 		<tr id="staffCertificateList{{idx}}"> -->
<!-- 					<td class="hide"> -->
<!-- 						<input id="staffCertificateList{{idx}}_id" name="staffCertificateList[{{idx}}].id" type="hidden" value="{{row.id}}"/> -->
<!-- 						<input id="staffCertificateList{{idx}}_delFlag" name="staffCertificateList[{{idx}}].delFlag" type="hidden" value="0"/> -->
<!-- 					</td> -->
<!-- 				</tr> -->
<!-- 				<tr> -->
<!-- 					<td class="width-15 active"><label class="pull-right">证书名称：</label></td> -->
<!-- 					<td class="width-35"> -->
<!-- 						<select id="staffCertificateList{{idx}}_certificateName" name="staffCertificateList[{{idx}}].certificateName" data-value="{{row.certificateName}}" class="form-control required m-b  "> -->
<!-- 							<option value=""></option> -->
<%-- 							<c:forEach items="${fns:getDictList('certificate_type')}" var="dict"> --%>
<%-- 								<option value="${dict.value}">${dict.label}</option> --%>
<%-- 							</c:forEach> --%>
<!-- 						</select> -->
<!-- 					</td> -->
<!-- 					<td class="width-15 active"><label class="pull-right">证书等级：</label></td> -->
<!-- 					<td class="width-35"> -->
<!-- 						<select id="staffCertificateList{{idx}}_certificateClass" name="staffCertificateList[{{idx}}].certificateClass" data-value="{{row.certificateClass}}" class="form-control required m-b  "> -->
<!-- 							<option value=""></option> -->
<%-- 							<c:forEach items="${fns:getDictList('normal_certificateclass')}" var="dict"> --%>
<%-- 								<option value="${dict.value}">${dict.label}</option> --%>
<%-- 							</c:forEach> --%>
<!-- 						</select> -->
<!-- 					</td> -->
<!-- 					<td class="text-center" width="10"> -->
<!-- 						{{#delBtn}}<span class="close" onclick="delRow(this, '#staffCertificateList{{idx}}')" title="删除">&times;</span>{{/delBtn}} -->
<!-- 					</td> -->
<!-- 				</tr> -->
<!-- 				<tr> -->
<!-- 					<td class="width-15 active"><label class="pull-right">对应专业：</label></td> -->
<!-- 					<td class="width-35"> -->
<!-- 						<select id="staffCertificateList{{idx}}_certificateMajor" name="staffCertificateList[{{idx}}].certificateMajor" data-value="{{row.certificateMajor}}" class="form-control required m-b  "> -->
<!-- 							<option value=""></option> -->
<%-- 							<c:forEach items="${fns:getDictList('certificate_major')}" var="dict"> --%>
<%-- 								<option value="${dict.value}">${dict.label}</option> --%>
<%-- 							</c:forEach> --%>
<!-- 						</select> -->
<!-- 					</td> -->
<!-- 					<td class="width-15 active"><label class="pull-right">注册时间：</label></td> -->
<!-- 					<td class="width-35"> -->
<!-- 						<div class='input-group form_datetime' id="staffCertificateList{{idx}}_regisDate"> -->
<!-- 		                    <input type='text'  name="staffCertificateList[{{idx}}].regisDate" class="form-control required"  value="{{row.regisDate}}"/> -->
<!-- 		                    <span class="input-group-addon"> -->
<!-- 		                        <span class="glyphicon glyphicon-calendar"></span> -->
<!-- 		                    </span> -->
<!-- 		                </div>		 -->
<!-- 					</td> -->
<!-- 					<td class="text-center" width="10"> -->
<!-- 						{{#delBtn}}<span class="close" onclick="delRow(this, '#staffCertificateList{{idx}}')" title="删除">&times;</span>{{/delBtn}} -->
<!-- 					</td> -->
<!-- 				</tr> -->
<!-- 				<tr> -->
<!-- 					<td class="width-15 active"><label class="pull-right">失效时间：</label></td> -->
<!-- 					<td class="width-35"> -->
<!-- 						<div class='input-group form_datetime' id="staffCertificateList{{idx}}_invalidDate"> -->
<!-- 		                    <input type='text'  name="staffCertificateList[{{idx}}].invalidDate" class="form-control required"  value="{{row.invalidDate}}"/> -->
<!-- 		                    <span class="input-group-addon"> -->
<!-- 		                        <span class="glyphicon glyphicon-calendar"></span> -->
<!-- 		                    </span> -->
<!-- 		                </div>	 -->
<!-- 					</td> -->
<!-- 					<td class="width-15 active"><label class="pull-right">证书编号：</label></td> -->
<!-- 					<td class="width-35"> -->
<!-- 						<input id="staffCertificateList{{idx}}_certificateFirstNum" name="staffCertificateList[{{idx}}].certificateFirstNum" type="text" value="{{row.certificateFirstNum}}"    class="form-control required"/> -->
<!-- 					</td> -->
<!-- 					<td class="text-center" width="10"> -->
<!-- 						{{#delBtn}}<span class="close" onclick="delRow(this, '#staffCertificateList{{idx}}')" title="删除">&times;</span>{{/delBtn}} -->
<!-- 					</td> -->
<!-- 				</tr> -->
<!-- 				<tr> -->
<!-- 					<td class="width-15 active"><label class="pull-right">注册证号：</label></td> -->
<!-- 					<td class="width-35"> -->
<!-- 						<input id="staffCertificateList{{idx}}_registrationNum" name="staffCertificateList[{{idx}}].registrationNum" type="text" value="{{row.registrationNum}}"    class="form-control "/> -->
<!-- 					</td> -->
<!-- 					<td class="width-15 active"></td> -->
<!-- 					<td class="width-35"> -->
<!-- 					</td> -->
<!-- 					<td class="text-center" width="10"> -->
<!-- 						{{#delBtn}}<span class="close" onclick="delRow(this, '#staffCertificateList{{idx}}')" title="删除">&times;</span>{{/delBtn}} -->
<!-- 					</td> -->
<!-- 				</tr> -->
		</form:form>
</body>
</html>
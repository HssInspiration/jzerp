<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>投标管理管理</title>
	<meta name="decorator" content="ani"/>
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
			validateForm = $("#inputForm").validate({
				submitHandler: function(form){
					jp.post("${ctx}/bidquerymanage/bidtablequery/save",$('#inputForm').serialize(),function(data){
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
</head>
<body class="bg-white">
		<form:form id="inputForm" modelAttribute="bidtableQuery" action="${ctx}/bidquerymanage/bidtablequery/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>
		<table class="table table-bordered">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>投标编号：</label></td>
					<td class="width-35">
						<form:input path="bidNum" htmlEscape="false"    class="form-control required"/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>项目工程id：</label></td>
					<td class="width-35">
						<form:select path="programId" class="form-control required">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>状态：</label></td>
					<td class="width-35">
						<form:select path="status" class="form-control required">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('bidstatus')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
					<td class="width-15 active"><label class="pull-right">投标附件：</label></td>
					<td class="width-35">
						<form:hidden id="bidEnclosure" path="bidEnclosure" htmlEscape="false" class="form-control"/>
						<sys:ckfinder input="bidEnclosure" type="files" uploadPath="/bidquerymanage/bidtablequery" selectMultiple="true"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">备注信息：</label></td>
					<td class="width-35">
						<form:textarea path="remarks" htmlEscape="false" rows="4"    class="form-control "/>
					</td>
					<td class="width-15 active"></td>
		   			<td class="width-35" ></td>
		  		</tr>
		 	</tbody>
		</table>
		<div class="tabs-container">
            <ul class="nav nav-tabs">
				<li class="active"><a data-toggle="tab" href="#tab-1" aria-expanded="true">参投单位表：</a>
                </li>
            </ul>
            <div class="tab-content">
				<div id="tab-1" class="tab-pane fade in  active">
			<a class="btn btn-white btn-sm" onclick="addRow('#bidCompanyManageList', bidCompanyManageRowIdx, bidCompanyManageTpl);bidCompanyManageRowIdx = bidCompanyManageRowIdx + 1;" title="新增"><i class="fa fa-plus"></i> 新增</a>
			<table class="table table-striped table-bordered table-condensed">
				<thead>
					<tr>
						<th class="hide"></th>
						<th><font color="red">*</font>单位id</th>
						<th><font color="red">*</font>项目id</th>
						<th><font color="red">*</font>投标价</th>
						<th><font color="red">*</font>投标时间</th>
						<th><font color="red">*</font>劳务费</th>
						<th>材料费</th>
						<th>技术标</th>
						<th>商务标</th>
						<th>经济标</th>
						<th><font color="red">*</font>建造师</th>
						<th><font color="red">*</font>技术负责人</th>
						<th><font color="red">*</font>安全员</th>
						<th><font color="red">*</font>施工员</th>
						<th><font color="red">*</font>质检员</th>
						<th><font color="red">*</font>材料员</th>
						<th><font color="red">*</font>造价员</th>
						<th><font color="red">*</font>是否中标</th>
						<th><font color="red">*</font>递交保证金金额</th>
						<th><font color="red">*</font>相关附件</th>
						<th width="10">&nbsp;</th>
					</tr>
				</thead>
				<tbody id="bidCompanyManageList">
				</tbody>
			</table>
			<script type="text/template" id="bidCompanyManageTpl">//<!--
				<tr id="bidCompanyManageList{{idx}}">
					<td class="hide">
						<input id="bidCompanyManageList{{idx}}_id" name="bidCompanyManageList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
						<input id="bidCompanyManageList{{idx}}_delFlag" name="bidCompanyManageList[{{idx}}].delFlag" type="hidden" value="0"/>
					</td>
					
					<td>
						<input id="bidCompanyManageList{{idx}}_companyId" name="bidCompanyManageList[{{idx}}].companyId" type="text" value="{{row.companyId}}"    class="form-control required"/>
					</td>
					
					
					<td>
						<input id="bidCompanyManageList{{idx}}_programId" name="bidCompanyManageList[{{idx}}].programId" type="text" value="{{row.programId}}"    class="form-control required"/>
					</td>
					
					
					<td>
						<input id="bidCompanyManageList{{idx}}_bidPrice" name="bidCompanyManageList[{{idx}}].bidPrice" type="text" value="{{row.bidPrice}}"    class="form-control required isFloatGteZero"/>
					</td>
					
					
					<td>
						<div class='input-group form_datetime' id="bidCompanyManageList{{idx}}_bidDate">
		                    <input type='text'  name="bidCompanyManageList[{{idx}}].bidDate" class="form-control required"  value="{{row.bidDate}}"/>
		                    <span class="input-group-addon">
		                        <span class="glyphicon glyphicon-calendar"></span>
		                    </span>
		                </div>						            
					</td>
					
					
					<td>
						<input id="bidCompanyManageList{{idx}}_laborCost" name="bidCompanyManageList[{{idx}}].laborCost" type="text" value="{{row.laborCost}}"    class="form-control required isFloatGteZero"/>
					</td>
					
					
					<td>
						<input id="bidCompanyManageList{{idx}}_meterialExpense" name="bidCompanyManageList[{{idx}}].meterialExpense" type="text" value="{{row.meterialExpense}}"    class="form-control  isFloatGteZero"/>
					</td>
					
					
					<td>
						<select id="bidCompanyManageList{{idx}}_tecBid" name="bidCompanyManageList[{{idx}}].tecBid" data-value="{{row.tecBid}}" class="form-control m-b  ">
							<option value=""></option>
							<c:forEach items="${fns:getDictList('')}" var="dict">
								<option value="${dict.value}">${dict.label}</option>
							</c:forEach>
						</select>
					</td>
					
					
					<td>
						<select id="bidCompanyManageList{{idx}}_comBid" name="bidCompanyManageList[{{idx}}].comBid" data-value="{{row.comBid}}" class="form-control m-b  ">
							<option value=""></option>
							<c:forEach items="${fns:getDictList('')}" var="dict">
								<option value="${dict.value}">${dict.label}</option>
							</c:forEach>
						</select>
					</td>
					
					
					<td>
						<select id="bidCompanyManageList{{idx}}_ecoBid" name="bidCompanyManageList[{{idx}}].ecoBid" data-value="{{row.ecoBid}}" class="form-control m-b  ">
							<option value=""></option>
							<c:forEach items="${fns:getDictList('')}" var="dict">
								<option value="${dict.value}">${dict.label}</option>
							</c:forEach>
						</select>
					</td>
					
					
					<td>
						<select id="bidCompanyManageList{{idx}}_constructorId" name="bidCompanyManageList[{{idx}}].constructorId" data-value="{{row.constructorId}}" class="form-control m-b  required">
							<option value=""></option>
							<c:forEach items="${fns:getDictList('')}" var="dict">
								<option value="${dict.value}">${dict.label}</option>
							</c:forEach>
						</select>
					</td>
					
					
					<td>
						<select id="bidCompanyManageList{{idx}}_directorId" name="bidCompanyManageList[{{idx}}].directorId" data-value="{{row.directorId}}" class="form-control m-b  required">
							<option value=""></option>
							<c:forEach items="${fns:getDictList('')}" var="dict">
								<option value="${dict.value}">${dict.label}</option>
							</c:forEach>
						</select>
					</td>
					
					
					<td>
						<select id="bidCompanyManageList{{idx}}_saverId" name="bidCompanyManageList[{{idx}}].saverId" data-value="{{row.saverId}}" class="form-control m-b  required">
							<option value=""></option>
							<c:forEach items="${fns:getDictList('')}" var="dict">
								<option value="${dict.value}">${dict.label}</option>
							</c:forEach>
						</select>
					</td>
					
					
					<td>
						<select id="bidCompanyManageList{{idx}}_constrworkerId" name="bidCompanyManageList[{{idx}}].constrworkerId" data-value="{{row.constrworkerId}}" class="form-control m-b  required">
							<option value=""></option>
							<c:forEach items="${fns:getDictList('')}" var="dict">
								<option value="${dict.value}">${dict.label}</option>
							</c:forEach>
						</select>
					</td>
					
					
					<td>
						<select id="bidCompanyManageList{{idx}}_inspectorId" name="bidCompanyManageList[{{idx}}].inspectorId" data-value="{{row.inspectorId}}" class="form-control m-b  required">
							<option value=""></option>
							<c:forEach items="${fns:getDictList('')}" var="dict">
								<option value="${dict.value}">${dict.label}</option>
							</c:forEach>
						</select>
					</td>
					
					
					<td>
						<select id="bidCompanyManageList{{idx}}_meterialerId" name="bidCompanyManageList[{{idx}}].meterialerId" data-value="{{row.meterialerId}}" class="form-control m-b  required">
							<option value=""></option>
							<c:forEach items="${fns:getDictList('')}" var="dict">
								<option value="${dict.value}">${dict.label}</option>
							</c:forEach>
						</select>
					</td>
					
					
					<td>
						<select id="bidCompanyManageList{{idx}}_costerId" name="bidCompanyManageList[{{idx}}].costerId" data-value="{{row.costerId}}" class="form-control m-b  required">
							<option value=""></option>
							<c:forEach items="${fns:getDictList('')}" var="dict">
								<option value="${dict.value}">${dict.label}</option>
							</c:forEach>
						</select>
					</td>
					
					
					<td>
						<c:forEach items="${fns:getDictList('isBid')}" var="dict" varStatus="dictStatus">
							<span>
								<input id="bidCompanyManageList{{idx}}_isBid${dictStatus.index}" 
									 name="bidCompanyManageList[{{idx}}].isBid" 
									 type="radio" class="i-checks" 
									value="${dict.value}" 
							   data-value="{{row.isBid}}">
								<label for="bidCompanyManageList{{idx}}_isBid${dictStatus.index}">
									${dict.label}
								</label>
							</span>
						</c:forEach>
					</td>
					
					<td>
						<input id="bidCompanyManageList{{idx}}_deposit" name="bidCompanyManageList[{{idx}}].deposit" type="text" value="{{row.deposit}}"    class="form-control required"/>
					</td>
					
					
					<td>
						<input id="bidCompanyManageList{{idx}}_depositEnclosure" name="bidCompanyManageList[{{idx}}].depositEnclosure" type="hidden" value="{{row.depositEnclosure}}" class="form-control"/>
						<sys:ckfinder input="bidCompanyManageList{{idx}}_depositEnclosure" type="files" uploadPath="/bidquerymanage/bidtable" selectMultiple="true"/>
					</td>
					
					<td class="text-center" width="10">
						{{#delBtn}}<span class="close" onclick="delRow(this, '#bidCompanyManageList{{idx}}')" title="删除">&times;</span>{{/delBtn}}
					</td>
				</tr>//-->
			</script>
			<script type="text/javascript">
				var bidCompanyManageRowIdx = 0, bidCompanyManageTpl = $("#bidCompanyManageTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
				$(document).ready(function() {
					var data = ${fns:toJson(bidtable.bidCompanyManageList)};
					for (var i=0; i<data.length; i++){
						addRow('#bidCompanyManageList', bidCompanyManageRowIdx, bidCompanyManageTpl, data[i]);
						bidCompanyManageRowIdx = bidCompanyManageRowIdx + 1;
					}
				});
			</script>
			</div>
		</div>
		</div>
		</form:form>
</body>
</html>
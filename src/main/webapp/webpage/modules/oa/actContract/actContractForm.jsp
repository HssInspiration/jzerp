<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>总包合同（市场投标）审批申请</title>
	<meta name="decorator" content="ani"/>
	<link id="bscss" href="${ctxStatic}/common/css/bootstrap.min.css" rel="stylesheet">
	<script type="text/javascript">
		var validateForm;
		function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
		  if(validateForm.form()){
			  jp.loading();
			  $("#inputForm").submit();
			  return true;
		  }
		  return false;
		}
		$(document).ready(function() {
			//合同修改时打开页面：
			var contractTextCont = $("#contractTextCont").val();
			var enclosureCont = $("#enclosureCont").val();
			console.log("*********"+contractTextCont);
			console.log("#########"+enclosureCont);
			if(contractTextCont != null){
				if(!/\.(gif|jpg|jpeg|png|GIF|JPG|PNG)$/.test(contractTextCont)){
        			contractText = "<a href=\""+contractTextCont+"\" url=\""+contractTextCont+"\" target=\"_blank\">"+decodeURIComponent(contractTextCont.substring(contractTextCont.lastIndexOf("/")+1))+"</a>"
        		}else{
        			contractText = '<img  onclick="jp.showPic(\''+contractTextCont.substring(1)+'\')"'+' height="50px" src="'+contractTextCont.substring(1)+'">';
        		}
				//将对应附件放在合同正文中
				$("#text").append(contractText);
			}
			if(enclosureCont != null){
				var array = enclosureCont.split("|");
				console.log("array:"+array);
				var valueArray;
				if(array[0] == null){
					valueArray = array.slice(1);
				}else{
					valueArray = array;
				}
				var labelArray = [];
	        	for(var i =1 ; i<valueArray.length; i++){
	        		if(!/\.(gif|jpg|jpeg|png|GIF|JPG|PNG)$/.test(valueArray[i])){
	        			labelArray[i] = "<a href=\""+valueArray[i]+"\" url=\""+valueArray[i]+"\" target=\"_blank\">"+decodeURIComponent(valueArray[i].substring(valueArray[i].lastIndexOf("/")+1))+"</a>"
	        		}else{
	        			labelArray[i] = '<img   onclick="jp.showPic(\''+valueArray[i]+'\')"'+' height="50px" src="'+valueArray[i]+'">';
	        		}
	        		labelArray.join(" ");
	        	}
	        	if(labelArray.length>0){
					var str = "";
					for(var i=1;i<labelArray.length;i++){
						str += "&nbsp;&nbsp;&nbsp;"+labelArray[i];
						console.log("str:"+str);
					}
					$("#enclosureContent").append(str);
				}
			}
			
			var getUrl,actProId = $("#id").val();
			console.log("getUrl1:"+getUrl);
			console.log("actProId1:"+actProId);
			if(actProId != null){
				getUrl = "${ctx}/oa/actContract/getProContractByName?contractName="
			}else{
				getUrl = "${ctx}/oa/actContract/getMarketProContractByName?contractName="
			}
			console.log("getUrl2:"+getUrl);
			//从url请求项目集合，参数：项目名称-contractName
			$("#test_data").bsSuggest({
				  idField : "id\" style=\"display:none\"",
				  keyField : "word",
				  getDataMethod : "url",
				  url : getUrl,
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
				              "word":json[i].contractName
			              })
			          }
			              return data
				  }
			});
			
			$("#test_data").on('onSetSelectValue', function (e, keyword) {//下拉框值选中时调用
				console.log('onSetSelectValue: ', keyword);
				//思路：
				//1.下拉变化时获取项目id;
				var id = keyword.id;
				console.log("id:"+id);
				var jsonData = JSON.stringify({"id":id});
				//每次选中后要对之前的做一个清除
				$("#enclosureContent").empty();
				$("#text").empty();
				//2.获取完id传入后台获取对应合同正文信息;
				$.ajax({
					url:"${ctx}/oa/actContract/getContractTextByContractId",
					data:jsonData,
					type:"post",
					contentType:"application/json;charset=utf-8",
					dataType:"json",
					success:function(data){
						console.log("获取成功！"+data);
						console.log("获取成功！"+data.length);
						if(data != null){
							var contractText = ""; 
							//设置合同正文隐藏域的相关值
							var contractTextStr = data.contractTextCont;
							console.log("获取成功:contractTextStr0为:"+data.contractTextCont);
							console.log("获取成功:contractTextStr1为:"+contractTextStr);
			        		if(!/\.(gif|jpg|jpeg|png|GIF|JPG|PNG)$/.test(data.contractTextCont)){
			        			contractText = "<a href=\""+data.contractTextCont+"\" url=\""+data.contractTextCont+"\" target=\"_blank\">"+decodeURIComponent(data.contractTextCont.substring(data.contractTextCont.lastIndexOf("/")+1))+"</a>"
			        		}else{
			        			contractText = '<img  onclick="jp.showPic(\''+data.contractTextCont+'\')"'+' height="50px" src="'+data.contractTextCont+'">';
			        		}
							$("#contractText").val(contractTextStr);
							console.log("获取成功:contractTextStr2为:"+$("#contractText").val());
							//将对应附件放在合同正文中
							$("#text").append(contractText);
						}
					},
					error:function(){
						console.log("获取失败！");
					}
				});
				
				$.ajax({
					url:"${ctx}/oa/actContract/getEnclosureContByForeginId",
					data:jsonData,
					type:"post",
					contentType:"application/json;charset=utf-8",
					dataType:"json",
					success:function(data){
						console.log("获取成功！"+data);
						console.log("获取成功！"+data.length);
						if(data!=null && data.length>0){
							var arr = [];
							var labelArray = [];
							var valueArray = [];
							var enclosureStr = ""; 
							for(var i=0;i<data.length;i++){
								//设置附件隐藏域的相关值
								enclosureStr += data[i].enclosureCont;
								console.log("enclosure1为:"+enclosureStr);//获得对应附件
								arr = enclosureStr.substring(1).split("|");
								if(arr[0] != null && arr[0].length>0){//第一个元素为空就删除掉
									valueArray = arr;
								}else{
									valueArray = arr.slice(1);
								}
// 								console.log("valueArray:"+valueArray);
// 								console.log("valueArray:"+valueArray.length);
								for(var j=0;j<valueArray.length;j++){
									if(!/\.(gif|jpg|jpeg|png|GIF|JPG|PNG)$/.test(valueArray[j])){
					        			labelArray[j] = "<a href=\""+valueArray[j]+"\" url=\""+valueArray[j]+"\" target=\"_blank\">"+decodeURIComponent(valueArray[j].substring(valueArray[j].lastIndexOf("/")+1))+"</a>"
					        		}else{
					        			labelArray[j] = '<img   onclick="jp.showPic(\''+valueArray[j]+'\')"'+' height="50px" src="'+valueArray[j]+'">';
					        		}
								}
				        		labelArray.join(" ");
							}	
							console.log("enclosure2为:"+enclosureStr);//获得对应附件
							$("#enclosure").val(enclosureStr);
							console.log("labelArray为:"+labelArray);//获得对应附件
							console.log("enclosure3为:"+$("#enclosure").val());//获得对应附件
							//将对应附件放在相关附件中
							if(labelArray.length>0){
								var str = "";
								for(var i=0;i<labelArray.length;i++){
									str += labelArray[i]+"&nbsp;&nbsp;&nbsp;";
								}
								$("#enclosureContent").append(str);
							}
						}
					},
					error:function(){
						console.log("获取失败！");
					}
				});
			});
			
			$("#name").focus();
			validateForm = $("#inputForm").validate({
				submitHandler: function(form){
					var proContractId = $("#test_data").attr("data-id");
					if(proContractId != null && proContractId != ""){
						$("#proContractId").val(proContractId);
 					}
// 					jp.loading('正在提交，请稍等...');
					form.submit();
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
<body>
<div class="wrapper wrapper-content">
<div class="row">
	<div class="col-md-12">
		<div class="panel panel-primary">
			<div class="panel-heading">
				<h3 class="panel-title">
					<a class="panelButton" href="#"  onclick="history.go(-1)"><i class="ti-angle-left"></i> 返回</a>
				</h3>
			</div>
			<div class="panel-body">
				<div class="form-group text-center">
				 	<h3>总包合同（市场投标）审批申请</h3>
				</div>
			<form:form id="inputForm" modelAttribute="actContract" action="${ctx}/oa/actContract/save" method="post" class="form-horizontal">
				<form:hidden path="id"/>
				<form:hidden path="act.taskId"/>
				<form:hidden path="act.taskName"/>
				<form:hidden path="act.taskDefKey"/>
				<form:hidden path="act.procInsId"/>
				<form:hidden path="act.procDefId"/>
				<form:hidden id="flag" path="act.flag"/>
				<sys:message content="${message}"/>
					<div class="form-group">
						<label class="col-sm-2 control-label">合同名称：</label>
<%-- 						<c:if test="${empty actContract.id}"> --%>
							<div class="col-sm-10">
								<input type="hidden" class="form-control" name= "proContract.id" id = "proContractId" value = "${actContract.proContract.id}">
				                    <div class="input-group">
				                        <input type="text" class="form-control"  id="test_data" value = "${actContract.proContract.contractName}">
				                        <div class="input-group-btn">
				                            <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
				                                <span class="caret"></span>
				                            </button>
				                            <ul class="dropdown-menu dropdown-menu-right" role="menu">
				                            </ul>
				                        </div>
				                    </div>
							</div>
<%-- 						</c:if> --%>
<%-- 						<c:if test="${not empty actContract.id}"> --%>
<!-- 							<div class="col-sm-10"> -->
<%-- 								<input type="hidden" class="form-control " name= "proContract.id"  value = "${actContract.proContract.id}"> --%>
<%-- 								<form:input path="proContract.contractName" readOnly="true" class="form-control required"/> --%>
<!-- 							</div> -->
<%-- 						</c:if> --%>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label">标题：</label>
						<div class="col-sm-10">
							<form:input path="remarks" class="form-control" rows="5" maxlength="100"/>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label">合同正文：</label>
							<input id="contractText" name="contractTextCont" maxlength="64" class="form-control" type="hidden">	
						<div class="col-sm-10" id="text">
							<c:if test="${not empty actContract.id}">
								<form:hidden path="contractTextCont" class="form-control required" rows="5" maxlength="20"/>
							</c:if>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label">相关附件：</label>
						<input id="enclosure" name="enclosureCont" maxlength="64" class="form-control" type="hidden">	
						<div class="col-sm-10" id="enclosureContent">
							<c:if test="${not empty actContract.id}">
								<form:hidden path="enclosureCont" class="form-control required" rows="5" maxlength="20"/>
							</c:if>
						</div>
					</div>
					<c:if test="${actContract.act.taskDefKey eq 'contract_modify'}">
						<div class="form-group">
							<label class="col-sm-2 control-label">修改说明：</label>
							<div class="col-sm-10">
								<form:textarea path="act.comment" class="form-control" rows="5" maxlength="50"/>
							</div>
						</div>
					</c:if>
				<div class="form-group">
				   <div class="col-sm-3"></div>
					 <div class="col-sm-6">
						<div class="form-group text-center">
							<label></label>
							<div>
								<input id="btnSubmit" class="btn  btn-primary btn-lg btn-parsley" type="submit" value="提交申请" onclick="$('#flag').val('yes')"/>&nbsp;
								<c:if test="${not empty actContract.id}">
									<input id="btnSubmit2" class="btn  btn-danger btn-lg btn-parsley" type="submit" value="销毁申请" onclick="$('#flag').val('no')"/>&nbsp;
									<input id="actContractIdVal"  type="hidden" value="${actContract.id}"/>
								</c:if>
							</div>
						</div>
					</div>
				</div>
					<c:if test="${not empty actContract.id}">
						<act:flowChart procInsId="${actContract.act.procInsId}"/>
						<act:histoicFlow procInsId="${actContract.act.procInsId}" />
					</c:if>
			</form:form>
	</div>
</div>
</div>
</div>
</div>
</body>
</html>


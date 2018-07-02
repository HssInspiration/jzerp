<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>分包合同审批申请</title>
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
			//从url请求项目集合，参数：项目名称-contractName
			$("#test_data").bsSuggest({
				  idField : "id\" style=\"display:none\"",
				  keyField : "word",
				  getDataMethod : "url",
				  url : "${ctx}/oa/actSubContract/getAppointSubProContractByName?subProContractName=",
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
				              "word":json[i].subProContractName
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
				//2.获取完id传入后台获取对应主项目信息;
				$.ajax({
					url:"${ctx}/oa/actSubContract/getEnclosureContByForeginId",
					data:jsonData,
					type:"post",
					contentType:"application/json;charset=utf-8",
					dataType:"json",
					success:function(data){
						console.log("获取成功！"+data);
						console.log("获取成功！"+data.length);
						if(data!=null && data.length>0){
							var labelArray = [];
							var enclosureStr = ""; 
							for(var i=0;i<data.length;i++){
								console.log("1附件内容为："+data[i].enclosureCont);
								console.log("2附件内容为："+data[i].enclosureCont.substring(1));
								//设置附件隐藏域的相关值
								enclosureStr += data[i].enclosureCont;
				        		if(!/\.(gif|jpg|jpeg|png|GIF|JPG|PNG)$/.test(data[i].enclosureCont.substring(1))){
				        			labelArray[i] = "<a href=\""+data[i].enclosureCont.substring(1)+"\" url=\""+data[i].enclosureCont.substring(1)+"\" target=\"_blank\">"+decodeURIComponent(data[i].enclosureCont.substring(1).substring(data[i].enclosureCont.substring(1).lastIndexOf("/")+1))+"</a>"
				        		}else{
				        			labelArray[i] = '<img   onclick="jp.showPic(\''+data[i].enclosureCont.substring(1)+'\')"'+' height="50px" src="'+data[i].enclosureCont.substring(1)+'">';
				        		}
				        		labelArray.join(" ");
							}	
							$("#enclosure").val(enclosureStr);
							console.log("labelArray为:"+labelArray);//获得对应附件
							console.log("enclosure为:"+$("#enclosure").val());//获得对应附件
							//将对应附件放在相关附件中
							if(labelArray.length>0){
								var str = "";
								for(var i=0;i<labelArray.length;i++){
									str += "&nbsp;&nbsp;&nbsp;"+labelArray[i];
								}
								$("#enclosureCont").append(str);
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
					var subProContractId = $("#test_data").attr("data-id");
					if(subProContractId != null && subProContractId != ""){
						$("#subProContractId").val(subProContractId);
 					}
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
				 <h3>分包合同审批申请</h3>
				</div>
			<form:form id="inputForm" modelAttribute="actSubContract" action="${ctx}/oa/actSubContract/save" method="post" class="form-horizontal">
				<form:hidden path="id"/>
				<form:hidden path="act.taskId"/>
				<form:hidden path="act.taskName"/>
				<form:hidden path="act.taskDefKey"/>
				<form:hidden path="act.procInsId"/>
				<form:hidden path="act.procDefId"/>
				<form:hidden id="flag" path="act.flag"/>
				<sys:message content="${message}"/>
					<div class="form-group">
						<label class="col-sm-2 control-label">分包合同名称：</label>
						<div class="col-sm-10">
							<input type="hidden" class="form-control" name= "subProContract.id" id = "subProContractId" value = "${actSubContract.subProContract.id}">
			                    <div class="input-group">
			                        <input type="text" class="form-control"  id="test_data" value = "${actSubContract.subProContract.subProContractName}">
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
					<div class="form-group">
						<label class="col-sm-2 control-label">备注：</label>
						<div class="col-sm-10">
							<form:textarea path="remarks" class="form-control" rows="5" maxlength="20"/>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label">相关附件：</label>
						<div class="col-sm-10" id="enclosureCont">
							<input id="enclosure" name="enclosureCont" maxlength="64" class="form-control" type="hidden">	
						</div>
					</div>
					<div class="col-sm-3"></div>
					<div class="col-sm-6">
						<div class="form-group text-center">
							<label></label>
							<div>
								<input id="btnSubmit" class="btn  btn-primary btn-lg btn-parsley" type="submit" value="提交申请" onclick="$('#flag').val('yes')"/>&nbsp;
								<c:if test="${not empty actSubContract.id}">
									<input id="btnSubmit2" class="btn  btn-danger btn-lg btn-parsley" type="submit" value="销毁申请" onclick="$('#flag').val('no')"/>&nbsp;
								</c:if>
							</div>
						</div>
					</div>
			</form:form>
	</div>
</div>
</div>
</div>
</div>
</body>
</html>


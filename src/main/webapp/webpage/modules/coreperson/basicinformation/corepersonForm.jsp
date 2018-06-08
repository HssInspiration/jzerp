<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>人员管理</title>
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
			
			
// 			$("#value").focus();
			 validateForm = $("#inputForm").validate({
				 rules: {
				 	identityNum: {remote: "${ctx}/basicinformation/checkIdentityNum?identityOldNum=" + encodeURIComponent("${corePerson.identityNum}")},//设置了远程验证，在初始化时必须预先调用一次。
				 },
			  messages: {
					identityNum: {remote: "证件号码已存在"},
				 },
				submitHandler: function(form){
					var userId = $("#test_data").attr("data-id");
					console.log("00000:"+userId)
					if(userId != null && userId != ""){
						$("#userId").val(userId);
					}
					jp.loading();
					$.post("${ctx}/basicinformation/save",$('#inputForm').serialize(),function(data){
		                    if(data.success){
		                    	$table.bootstrapTable('refresh');
		                    	jp.success(data.msg);
		                    	jp.close($topIndex);//关闭dialog
	            	  			
		                    }else{
	            	  			jp.error(data.msg);
		                    }
		                    
		            });
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
	<form:form id="inputForm" modelAttribute="corePerson" action="${ctx}/basicinformation/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
		   	   <tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>人员姓名：</label></td>
					<td class="width-35">
						<input type="hidden" class="form-control" name= "user.id" id = "userId" value = "${corePerson.user.id}">
						<c:if test="${isAdd }">
							<div class="row">
				                <div class="col-lg-12">
				                    <div class="input-group">
				                        <input type="text" class="form-control required" id="test_data" 
				                         value = "${corePerson.user.name}">
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
			            	<input type="text" class="form-control required" readOnly="true" value = "${corePerson.user.name}">
			            </c:if>
			            <span id="repeatObj" style = "font-weight:bold;"></span>
					</td>
			   </tr>
		       <tr>
		         <td  class="width-15 active"><label class="pull-right"><font color="red">*</font>证件号码:</label></td>
		         <td class="width-35" >
			         <input id="identityOldNum" name="identityOldNum" type="hidden" value="${corePerson.identityNum}">
			         <form:input path="identityNum" htmlEscape="false" maxlength="50" class="form-control required abc"/>
			     </td>
		      </tr>
<!-- 		       <tr> -->
<!-- 		          <td  class="width-15 active">	<label class="pull-right"><font color="red">*</font>手机号码:</label></td> -->
<%-- 		          <td  class="width-35" ><form:input path="phoneNum" htmlEscape="false" maxlength="50" class="form-control required"/></td> --%>
<!-- 		      </tr> -->
		      <td class="width-15 active"><label class="pull-right">备注信息：</label></td>
					<td class="width-35">
						<form:textarea path="remarks" htmlEscape="false" rows="4"    class="form-control "/>
					</td>
		   </tbody>
		   </table>   
	</form:form>
</body>
</html>
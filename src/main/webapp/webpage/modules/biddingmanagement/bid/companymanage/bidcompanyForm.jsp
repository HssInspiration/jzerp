<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>参投单位管理</title>
	<meta name="decorator" content="ani"/>
	<link id="bscss" href="${ctxStatic}/common/css/bootstrap.min.css" rel="stylesheet">
	<script type="text/javascript">
		var validateForm;
		var $table;// 父页面table表格id
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
			//参投单位名称模糊匹配框
			$("#test_data1").bsSuggest({
				  idField : "id\" style=\"display:none\"",
				  keyField : "word",
				  getDataMethod : "url",
				  url : "${ctx}/companymanage/bidcompany/getAllCompanyList?companyName=",
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
			//投标项目名称模糊匹配框
			$("#test_data2").bsSuggest({
				  idField : "id\" style=\"display:none\"",
				  keyField : "word",
				  getDataMethod : "url",
				  url : "${ctx}/companymanage/bidcompany/getAllBidtableList?programName=",
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
				              "word":json[i].program.programName
				              })
				          }
				          return data
				  }
			  });
			
			$("#test_data2").on('onSetSelectValue', function (e, keyword) {//下拉框值选中时调用
		        console.log('onSetSelectValue: ', keyword);
		        var bidtableId = keyword.id;
		        console.log("bidtableId:"+bidtableId);
		        $.ajax({//获取投标时间
		        	url:"${ctx}/companymanage/bidcompany/getBidtableByProId",
					data:JSON.stringify({"id":bidtableId}),
					type:"post",
					dataType:"json",
					contentType:"application/json;charset=utf-8",
					success:function(data){
						console.log(data);
						console.log("remarks:"+data.remarks);
						if(data.remarks=="true"){//说明当前项目存在中标的单位，将是否中标设为否
							//设置为否	
							$('.iCheck-helper').trigger("click");//设置事件被点击执行 
							$("#isBid2").parent().attr("class","iradio_square-blue checked")
							console.log($("#isBid2").attr("class"));	
							console.log($("#isBid2").parent().attr("class"));
						}
						var date = data.openBidDate;
						console.log(date);
						console.log(data.deposit);//保证金
						$("#deposit").val(data.deposit);
						//设置参投的投标时间为date
						$("#bidDateValidate").val(date);								
					},
					error:function(){
						console.log("回调失败！");
					}
		        });
			})
			
			//绑定输入框文本变化事件（）：
			var requestUrl1 = "${ctx}/companymanage/bidcompany/getAllCorePersonListByName?name=";
			var requestUrl = "${ctx}/companymanage/bidcompany/getAppointCorePersonListByName?name=&certificateName="
			//建造师模糊匹配
			autoFillData("constructoor",requestUrl,1);
			//技术负责人模糊匹配
			autoFillData("director",requestUrl,2);
			//施工员模糊匹配
			autoFillData("constrworker",requestUrl,4);
			//安全员模糊匹配
			autoFillData("saver",requestUrl,6);
			//质检员模糊匹配
			autoFillData("inspector",requestUrl,3);
			//技术标模糊匹配
			autoFillData1("tecBidName",requestUrl1);
			//商务标模糊匹配
			autoFillData1("comBidName",requestUrl1);
			//材料员模糊匹配
			autoFillData("meterialer",requestUrl,5);
			//造价员模糊匹配
			autoFillData("coster",requestUrl,11);
			
			//定义下拉选中时的变量（人员id）判断在本项目中不能重复
			var constId;//建造师
			var direcId;//技术负责人
			var saveId; //安全员
			var incepId; //质检员
			var constrworkerId; //施工员
			
			//若修改时为不为金卓将下拉人员屏蔽，否则启用
			var companyOldId = $("#companyId").val(); 
			console.log("companyId:"+companyOldId)
        	if(companyOldId != "03ae459404284f17bbd25e78a13397a6"){
        		//禁用插件
	        	$("input#constructoor").bsSuggest("disable");
	        	$("input#director").bsSuggest("disable");
	        	$("input#tecBidName").bsSuggest("disable");
	        	$("input#constrworker").bsSuggest("disable");
	        	$("input#comBidName").bsSuggest("disable");
	        	$("input#saver").bsSuggest("disable");
	        	$("input#meterialer").bsSuggest("disable");
	        	$("input#inspector").bsSuggest("disable");
	        	$("input#coster").bsSuggest("disable");
        	}else{
        		//启用插件
	        	$("input#constructoor").bsSuggest("enable");
	        	$("input#director").bsSuggest("enable");
	        	$("input#tecBidName").bsSuggest("enable");
	        	$("input#constrworker").bsSuggest("enable");
	        	$("input#comBidName").bsSuggest("enable");
	        	$("input#saver").bsSuggest("enable");
	        	$("input#meterialer").bsSuggest("enable");
	        	$("input#inspector").bsSuggest("enable");
	        	$("input#coster").bsSuggest("enable");
	        	//建造师模糊匹配
				$("#constructoor").on('onSetSelectValue', function (e, keyword) {//下拉框值选中时调用
			        console.log('onSetSelectValue: ', keyword);
			        var workerid = keyword.id;
			        console.log(workerid);
			        $.ajax({//验证建造师是否重复
// 			        	url:"${ctx}/companymanage/bidcompany/validateOneWorker",
			        	url:"${ctx}/companymanage/bidcompany/validateWorker",
						data:JSON.stringify({"constructorId":workerid}),
						type:"post",
						dataType:"json",
						contentType:"application/json;charset=utf-8",
						success:function(data){
							console.log(typeof(data));
							console.log(data.program);
							//添加提示信息:
							if(data!=null && data.length>0){
								for(var i=0;i<data.length;i++){
									var status = data[i].program.status;
									console.log("项目状态"+status);
// 									if(status!=null){
										console.log("000:"+data[i].program.programName);
										$("#constructoorMessage").html("已在项目\""+data[i].program.programName+"\"中存在请谨慎添加！");
										$("#constructoorMessage").css("color","red");	
// 									}else {
// 										$("#constructoorMessage").html("");
// 									}
// 									if(status == 2){
// 										$("#constructoorMessage").html("已在施工项目\""+data[i].program.programName+"\"中存在不可重复添加！");
// 										$("#constructoorMessage").css("color","red");
// 										break;
// 									}else if(status == 3){
// 										$("#constructoorMessage").html("已在停工项目\""+data[i].program.programName+"\"中存在不可重复添加！");
// 										$("#constructoorMessage").css("color","red");
// 										break;
// 									}else {
// 										$("#constructoorMessage").html("");
// 										break;
// 									}
								}
							}else {
								$("#constructoorMessage").html("");
							}
// 							if(typeof(data.program) != "undefined" ){
// 								$("#constructoorMessage").html("已在项目\""+data.program.programName+"\"中存在请谨慎添加！");
// 								$("#constructoorMessage").css("color","red");
// 							}else {
// 								$("#constructoorMessage").html("");
// 							}
						},
						error:function(){
							console.log("回调失败！");
						}
			        });
				});
				//技术负责人模糊匹配
				$("#director").on('onSetSelectValue', function (e, keyword) {//下拉框值选中时调用
			        console.log('onSetSelectValue: ', keyword);
			        var workerid = keyword.id;
			        console.log(workerid);
			        $.ajax({//验证技术负责人是否重复
			        	url:"${ctx}/companymanage/bidcompany/validateWorker",
						data:JSON.stringify({"directorId":workerid}),
						type:"post",
						dataType:"json",
						contentType:"application/json;charset=utf-8",
						success:function(data){
							console.log(data);
							//添加提示信息:
							if(data!=null && data.length>0){
								for(var i=0;i<data.length;i++){
									var status = data[i].program.status;
									console.log("项目状态"+status);
									if(status!=null){
										$("#directorMessage").html("已在项目\""+data[i].program.programName+"\"中存在请谨慎添加！");
										$("#directorMessage").css("color","red");	
									}else {
										$("#directorMessage").html("");
										break;
									}
// 									if(status == 2){
// 										$("#directorMessage").html("已在施工项目\""+data[i].program.programName+"\"中存在不可重复添加！");
// 										$("#directorMessage").css("color","red");
// 										break;
// 									}else if(status == 3){
// 										$("#directorMessage").html("已在停工项目\""+data[i].program.programName+"\"中存在不可重复添加！");
// 										$("#directorMessage").css("color","red");
// 										break;
// 									}else {
// 										$("#directorMessage").html("");
// 										break;
// 									}
								}
							}else {
								$("#directorMessage").html("");
							}
						},
						error:function(){
							console.log("回调失败！");
						}
			        });
				});
				//施工员模糊匹配
				$("#constrworker").on('onSetSelectValue', function (e, keyword) {//下拉框值选中时调用
			        console.log('onSetSelectValue: ', keyword);
			        var workerid = keyword.id;
			        console.log(workerid);
			        $.ajax({//验证施工员是否重复
			        	url:"${ctx}/companymanage/bidcompany/validateWorker",
						data:JSON.stringify({"constrworkerId":workerid}),
						type:"post",
						dataType:"json",
						contentType:"application/json;charset=utf-8",
						success:function(data){
							console.log(data);
							//添加提示信息:
							if(data!=null && data.length>0){
								for(var i=0;i<data.length;i++){
									var status = data[i].program.status;
									console.log("项目状态"+status);
									if(status!=null){
										$("#constrworkerMessage").html("已在项目\""+data[i].program.programName+"\"中存在请谨慎添加！");
										$("#constrworkerMessage").css("color","red");	
									}else {
										$("#constrworkerMessage").html("");
										break;
									}
// 									if(status == 2){
// 										$("#constrworkerMessage").html("已在施工项目\""+data[i].program.programName+"\"中存在不可重复添加！");
// 										$("#constrworkerMessage").css("color","red");
// 										break;
// 									}else if(status == 3){
// 										$("#constrworkerMessage").html("已在停工项目\""+data[i].program.programName+"\"中存在不可重复添加！");
// 										$("#constrworkerMessage").css("color","red");
// 										break;
// 									}else {
// 										$("#constrworkerMessage").html("");
// 										break;
// 									}
								}
							}else {
								$("#constrworkerMessage").html("");
							}
						},
						error:function(){
							console.log("回调失败！");
						}
			        });
				});
				//安全员模糊匹配
				$("#saver").on('onSetSelectValue', function (e, keyword) {//下拉框值选中时调用
			        console.log('onSetSelectValue: ', keyword);
			        var workerid = keyword.id;
			        console.log(workerid);
			        $.ajax({//验证安全员是否重复
			        	url:"${ctx}/companymanage/bidcompany/validateWorker",
						data:JSON.stringify({"saverId":workerid}),
						type:"post",
						dataType:"json",
						contentType:"application/json;charset=utf-8",
						success:function(data){
							console.log(data);
							//添加提示信息:
							if(data!=null && data.length>0){
								for(var i=0;i<data.length;i++){
									var status = data[i].program.status;
									console.log("项目状态"+status);
									if(status!=null){
										$("#saverMessage").html("已在项目\""+data[i].program.programName+"\"中存在请谨慎添加！");
										$("#saverMessage").css("color","red");	
									}else {
										$("#saverMessage").html("");
										break;
									}
// 									if(status == 2){
// 										$("#saverMessage").html("已在施工项目\""+data[i].program.programName+"\"中存在不可重复添加！");
// 										$("#saverMessage").css("color","red");
// 										break;
// 									}else if(status == 3){
// 										$("#saverMessage").html("已在停工项目\""+data[i].program.programName+"\"中存在不可重复添加！");
// 										$("#saverMessage").css("color","red");
// 										break;
// 									}else {
// 										$("#saverMessage").html("");
// 										break;
// 									}
								}
							}else {
								$("#saverMessage").html("");
							}
						},
						error:function(){
							console.log("回调失败！");
						}
			        });
				});
				
				//质检员模糊匹配
				$("#inspector").on('onSetSelectValue', function (e, keyword) {//下拉框值选中时调用
			        console.log('onSetSelectValue: ', keyword);
			        var workerid = keyword.id;
			        console.log(workerid);
			        $.ajax({//验证质检员是否重复
			        	url:"${ctx}/companymanage/bidcompany/validateWorker",
						data:JSON.stringify({"inspectorId":workerid}),
						type:"post",
						dataType:"json",
						contentType:"application/json;charset=utf-8",
						success:function(data){
							console.log("data:"+data);
							//添加提示信息:
							if(data!=null && data.length>0){
								for(var i=0;i<data.length;i++){
									var status = data[i].program.status;
									console.log("项目状态"+status);
									if(status!=null){
										$("#inspectorMessage").html("已在项目\""+data[i].program.programName+"\"中存在请谨慎添加！");
										$("#inspectorMessage").css("color","red");	
									}else {
										$("#inspectorMessage").html("");
										break;
									}
// 									if(status == 2){
// 										$("#inspectorMessage").html("已在施工项目\""+data[i].program.programName+"\"中存在不可重复添加！");
// 										$("#inspectorMessage").css("color","red");
// 										break;
// 									}else if(status == 3){
// 										$("#inspectorMessage").html("已在停工项目\""+data[i].program.programName+"\"中存在不可重复添加！");
// 										$("#inspectorMessage").css("color","red");
// 										break;
// 									}else {
// 										$("#inspectorMessage").html("");
// 										break;
// 									}
								}
							}else {
								$("#inspectorMessage").html("");
							}
						},
						error:function(){
							console.log("回调失败！");
						}
			        });
				});
        	}
			//当从下拉菜单选取值时触发，并传回设置的数据到第二个参数：
			$("#test_data1").on('onSetSelectValue', function (e, keyword) {
		        console.log('onSetSelectValue: ', keyword);
		        var compid = keyword.id;
		        console.log(compid);
		        if(compid == "03ae459404284f17bbd25e78a13397a6"){
		        	//启用插件
		        	$("input#constructoor").bsSuggest("enable");
		        	$("input#director").bsSuggest("enable");
		        	$("input#tecBidName").bsSuggest("enable");
		        	$("input#constrworker").bsSuggest("enable");
		        	$("input#comBidName").bsSuggest("enable");
		        	$("input#saver").bsSuggest("enable");
		        	$("input#meterialer").bsSuggest("enable");
		        	$("input#inspector").bsSuggest("enable");
		        	$("input#coster").bsSuggest("enable");
					
					$("#test_data2").on('onSetSelectValue', function (e, keyword) {//下拉框值选中时调用
				        console.log('onSetSelectValue: ', keyword);
				        var bidtableId = keyword.id;
				        console.log("bidtableId:"+bidtableId);
				        $.ajax({//获取投标时间
				        	url:"${ctx}/companymanage/bidcompany/getBidtableByProId",
							data:JSON.stringify({"id":bidtableId}),
							type:"post",
							dataType:"json",
							contentType:"application/json;charset=utf-8",
							success:function(data){
								console.log(data);
								console.log(data.status);
								if(data.status=="true"){//说明当前项目存在中标的单位，将是否中标设为否
									console.log("可设置单选钮状态！")
								}
								var date = data.openBidDate;
								console.log(date);
								console.log(data.deposit);//保证金
								$("#deposit").val(data.deposit);
								//设置参投的投标时间为date
								$("#bidDateValidate").val(date);								
							},
							error:function(){
								console.log("回调失败！");
							}
				        });
					});
					//建造师模糊匹配
					$("#constructoor").on('onSetSelectValue', function (e, keyword) {//下拉框值选中时调用
				        console.log('onSetSelectValue: ', keyword);
				        var workerid = keyword.id;
				        console.log(workerid);
				        $.ajax({//验证建造师是否重复
				        	url:"${ctx}/companymanage/bidcompany/validateWorker",
							data:JSON.stringify({"constructorId":workerid}),
							type:"post",
							dataType:"json",
							contentType:"application/json;charset=utf-8",
							success:function(data){
								console.log(data);
								//添加提示信息:
								if(data!=null && data.length>0){
									for(var i=0;i<data.length;i++){
										var status = data[i].program.status;
										console.log("项目状态"+status);
										console.log("000:"+data[i].program.programName);
										if(status!=null){
											$("#constructoorMessage").html("已在项目\""+data[i].program.programName+"\"中存在请谨慎添加！");
											$("#constructoorMessage").css("color","red");	
										}else {
											$("#constructoorMessage").html("");
											break;
										}
// 										if(status == 2){
// 											$("#constructoorMessage").html("已在施工项目\""+data[i].program.programName+"\"中存在不可重复添加！");
// 											$("#constructoorMessage").css("color","red");
// 											break;
// 										}else if(status == 3){
// 											$("#constructoorMessage").html("已在停工项目\""+data[i].program.programName+"\"中存在不可重复添加！");
// 											$("#constructoorMessage").css("color","red");
// 											break;
// 										}else {
// 											$("#constructoorMessage").html("");
// 											break;
// 										}
									}
								}else {
									$("#constructoorMessage").html("");
								}
							},
							error:function(){
								console.log("回调失败！");
							}
				        });
					});
					
					//技术负责人模糊匹配
					$("#director").on('onSetSelectValue', function (e, keyword) {//下拉框值选中时调用
				        console.log('onSetSelectValue: ', keyword);
				        var workerid = keyword.id;
				        
				        console.log(workerid);
				        $.ajax({//验证技术负责人是否重复
				        	url:"${ctx}/companymanage/bidcompany/validateWorker",
							data:JSON.stringify({"directorId":workerid}),
							type:"post",
							dataType:"json",
							contentType:"application/json;charset=utf-8",
							success:function(data){
								console.log(data);
								//添加提示信息:
								if(data!=null && data.length>0){
									for(var i=0;i<data.length;i++){
										var status = data[i].program.status;
										console.log("项目状态"+status);
										if(status!=null){
											$("#directorMessage").html("已在项目\""+data[i].program.programName+"\"中存在请谨慎添加！");
											$("#directorMessage").css("color","red");	
										}else {
											$("#directorMessage").html("");
											break;
										}
// 										if(status == 2){
// 											$("#directorMessage").html("已在施工项目\""+data[i].program.programName+"\"中存在不可重复添加！");
// 											$("#directorMessage").css("color","red");
// 											break;
// 										}else if(status == 3){
// 											$("#directorMessage").html("已在停工项目\""+data[i].program.programName+"\"中存在不可重复添加！");
// 											$("#directorMessage").css("color","red");
// 											break;
// 										}else {
// 											$("#directorMessage").html("");
// 											break;
// 										}
									}
								}else {
									$("#directorMessage").html("");
								}
							},
							error:function(){
								console.log("回调失败！");
							}
				        });
					});
					
					//施工员模糊匹配
					$("#constrworker").on('onSetSelectValue', function (e, keyword) {//下拉框值选中时调用
				        console.log('onSetSelectValue: ', keyword);
				        var workerid = keyword.id;
				        console.log(workerid);
				        $.ajax({//验证施工员是否重复
				        	url:"${ctx}/companymanage/bidcompany/validateWorker",
							data:JSON.stringify({"constrworkerId":workerid}),
							type:"post",
							dataType:"json",
							contentType:"application/json;charset=utf-8",
							success:function(data){
								console.log(data);
								//添加提示信息:
								if(data!=null && data.length>0){
									for(var i=0;i<data.length;i++){
										var status = data[i].program.status;
										console.log("项目状态"+status);
										
// 										if(status == 2){
// 											$("#constrworkerMessage").html("已在施工项目\""+data[i].program.programName+"\"中存在谨慎添加！");
// 											$("#constrworkerMessage").css("color","red");
// 											break;
// 										}else if(status == 3){
// 											$("#constrworkerMessage").html("已在停工项目\""+data[i].program.programName+"\"中存在不可重复添加！");
// 											$("#constrworkerMessage").css("color","red");
// 											break;
// 										}else {
// 											$("#constrworkerMessage").html("");
// 											break;
// 										}
										if(status!=null){
											$("#constrworkerMessage").html("已在项目\""+data[i].program.programName+"\"中存在请谨慎添加！");
											$("#constrworkerMessage").css("color","red");	
										}else {
											$("#constrworkerMessage").html("");
											break;
										}
									}
								}else {
									$("#constrworkerMessage").html("");
								}
							},
							error:function(){
								console.log("回调失败！");
							}
				        });
				        
					});
					//安全员模糊匹配
					$("#saver").on('onSetSelectValue', function (e, keyword) {//下拉框值选中时调用
				        console.log('onSetSelectValue: ', keyword);
				        var workerid = keyword.id;
				        console.log(workerid);
				       //	获取建造师
				        $.ajax({//验证安全员是否重复
				        	url:"${ctx}/companymanage/bidcompany/validateWorker",
							data:JSON.stringify({"saverId":workerid}),
							type:"post",
							dataType:"json",
							contentType:"application/json;charset=utf-8",
							success:function(data){
								console.log(data);
								//添加提示信息:
								if(data!=null && data.length>0){
									for(var i=0;i<data.length;i++){
										var status = data[i].program.status;
										console.log("项目状态"+status);
										if(status!=null){
											$("#saverMessage").html("已在项目\""+data[i].program.programName+"\"中存在请谨慎添加！");
											$("#saverMessage").css("color","red");	
										}else {
											$("#saverMessage").html("");
											break;
										}
// 										if(status == 2){
// 											$("#saverMessage").html("已在施工项目\""+data[i].program.programName+"\"中存在不可重复添加！");
// 											$("#saverMessage").css("color","red");
// 											break;
// 										}else if(status == 3){
// 											$("#saverMessage").html("已在停工项目\""+data[i].program.programName+"\"中存在不可重复添加！");
// 											$("#saverMessage").css("color","red");
// 											break;
// 										}else {
// 											$("#saverMessage").html("");
// 											break;
// 										}
									}
								}else {
									$("#saverMessage").html("");
								}
							},
							error:function(){
								console.log("回调失败！");
							}
				        });
				        
					});
					
					//质检员模糊匹配
					$("#inspector").on('onSetSelectValue', function (e, keyword) {//下拉框值选中时调用
				        console.log('onSetSelectValue: ', keyword);
				        var workerid = keyword.id;
				        console.log(workerid);
				        $.ajax({//验证质检员是否重复
				        	url:"${ctx}/companymanage/bidcompany/validateWorker",
							data:JSON.stringify({"inspectorId":workerid}),
							type:"post",
							dataType:"json",
							contentType:"application/json;charset=utf-8",
							success:function(data){
								console.log(data);
								//添加提示信息:
								if(data!=null && data.length>0){
									for(var i=0;i<data.length;i++){
										var status = data[i].program.status;
										console.log("项目状态"+status);
										if(status!=null){
											$("#inspectorMessage").html("已在项目\""+data[i].program.programName+"\"中存在请谨慎添加！");
											$("#inspectorMessage").css("color","red");	
										}else {
											$("#inspectorMessage").html("");
											break;
										}
// 										if(status == 2){
// 											$("#inspectorMessage").html("已在施工项目\""+data[i].program.programName+"\"中存在不可重复添加！");
// 											$("#inspectorMessage").css("color","red");
// 											break;
// 										}else if(status == 3){
// 											$("#inspectorMessage").html("已在停工项目\""+data[i].program.programName+"\"中存在不可重复添加！");
// 											$("#inspectorMessage").css("color","red");
// 											break;
// 										}else {
// 											$("#inspectorMessage").html("");
// 											break;
// 										}
									}
								}else {
									$("#inspectorMessage").html("");
								}
							},
							error:function(){
								console.log("回调失败！");
							}
				        });
					});
		        }else{
		        	$("#test_data2").on('onSetSelectValue', function (e, keyword) {//下拉框值选中时调用
				        console.log('onSetSelectValue: ', keyword);
				        var bidtableId = keyword.id;
				        console.log("bidtableId:"+bidtableId);
				        $.ajax({//获取投标时间
				        	url:"${ctx}/companymanage/bidcompany/getBidtableByProId",
							data:JSON.stringify({"id":bidtableId}),
							type:"post",
							dataType:"json",
							contentType:"application/json;charset=utf-8",
							success:function(data){
								console.log(data);
								if(data.remarks=="true"){//说明当前项目存在中标的单位，将是否中标设为否
									//设置为否	
									$('.iCheck-helper').trigger("click");//设置事件被点击执行 
									$("#isBid2").parent().attr("class","iradio_square-blue checked")
									console.log($("#isBid2").attr("class"));	
									console.log($("#isBid2").parent().attr("class"));
								}
								var date = data.openBidDate;
								console.log(date);
								console.log(data.deposit);//保证金
								$("#deposit").val(data.deposit);
							},
							error:function(){
								console.log("回调失败！");
							}
				        });
					});
		        	//禁用插件
		        	$("input#constructoor").bsSuggest("disable");
		        	$("input#director").bsSuggest("disable");
		        	$("input#tecBidName").bsSuggest("disable");
		        	$("input#constrworker").bsSuggest("disable");
		        	$("input#comBidName").bsSuggest("disable");
		        	$("input#saver").bsSuggest("disable");
		        	$("input#meterialer").bsSuggest("disable");
		        	$("input#inspector").bsSuggest("disable");
		        	$("input#coster").bsSuggest("disable");
		        }
		    });
			
			//当保证金一栏改变的时候进行验证是否和后台中的数据一致
		//绑定保证金改变事件
			$("#deposit").bind("change",function(){
				//先获取参投单位的id如果是金卓再向后台获取投标中对应的保证金数据
				var bidcompId = $("#companyId").val();
				if(bidcompId != null && bidcompId != ""){
					console.log("bidcompId:"+bidcompId);
				}else{//不是金卓
					var bidcompanyId =  $("#test_data1").attr("data-id");
					if(bidcompanyId == "03ae459404284f17bbd25e78a13397a6"){
						var depositval = $("#deposit").val();
						console.log(depositval);
						var bidtableId = $("#test_data2").attr("data-id");
						console.log(bidtableId);
						var jsonData = JSON.stringify({"id":bidtableId});
						//从后台获取投标中对应项目的保证金
						$.ajax({
							url:"${ctx}/companymanage/bidcompany/getDeposit",
							data:jsonData,
							type:"post",
							contentType:"application/json;charset=utf-8",
							dataType:"json",
							success:function(data){
								console.log("data为:"+data);
								var deposit = data.deposit;
								console.log(deposit);
								if(deposit != depositval ){
									$("#showdeposit").html("×保证金填写有误（投标模块金额为："+deposit+"万元）");
									$("#showdeposit").css("color","red");
								}else{
									$("#showdeposit").html("√保证金填写正确");
									$("#showdeposit").css("color","green");
								}
							},
							error:function(){
								console.log("获取保证金失败！");
							}
						});	
					}
				}
			});
			
			validateForm = $("#inputForm").validate({
				submitHandler : function(form) {
					var companyId = $("#test_data1").attr("data-id");
					if(companyId != null && companyId != ""){
						$("#companyId").val(companyId);
 					}
					var bidtableId = $("#test_data2").attr("data-id");
					if(bidtableId != null && bidtableId != ""){
						$("#bidtableId").val(bidtableId);
 					}
					//建造师更改时id赋值：
					var constructoorId = $("#constructoor").attr("data-id");
					if(constructoorId != null && constructoorId != ""){
						$("#constructoorId").val(constructoorId);
 					}
					console.log("constructoorId为:"+constructoorId);
					//技术负责人更改时id赋值：
					var directorId = $("#director").attr("data-id");
					if(directorId != null && directorId != ""){
						$("#directorId").val(directorId);
 					}
					//技术标更改时id赋值：
					var tecBidNameId = $("#tecBidName").attr("data-id");
					if(tecBidNameId != null && tecBidNameId != ""){
						$("#tecBidNameId").val(tecBidNameId);
 					}
					//商务标更改时id赋值：
					var comBidNameId = $("#comBidName").attr("data-id");
					if(comBidNameId != null && comBidNameId != ""){
						$("#comBidNameId").val(comBidNameId);
 					}
					//施工员更改时id赋值：
					var constrworkerId = $("#constrworker").attr("data-id");
					if(constrworkerId != null && constrworkerId != ""){
						$("#constrworkerId").val(constrworkerId);
 					}
					//安全员更改时id赋值：
					var saverId = $("#saver").attr("data-id");
					if(saverId != null && saverId != ""){
						$("#saverId").val(saverId);
 					}
					//材料员更改时id赋值：
					var meterialerId = $("#meterialer").attr("data-id");
					if(meterialerId != null && meterialerId != ""){
						$("#meterialerId").val(meterialerId);
 					}
					//质检员更改时id赋值：
					var inspectorId = $("#inspector").attr("data-id");
					if(inspectorId != null && inspectorId != ""){
						$("#inspectorId").val(inspectorId);
 					}
					//造价员更改时id赋值：
					var costerId = $("#coster").attr("data-id");
					if(costerId != null && costerId != ""){
						$("#costerId").val(costerId);
 					}
					//判断参投单位是不是金卓若是正常新增，若不是判断五大员与其他人员是否有输入，若无，正常新增，若有提示不能添加本单位人员至其他公司
					var companyNewId = $("#companyId").val();
					if(companyNewId=="03ae459404284f17bbd25e78a13397a6"){
						console.log("这是金卓公司");
					}else{
						console.log("这不是金卓！");
					}
					
					jp.post("${ctx}/companymanage/bidcompany/save",$('#inputForm').serialize(),function(data){
		                    if(data.success){
		                    	$table.bootstrapTable('refresh');//刷新表格
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
			
			$('#bidDate').datetimepicker({
				 format: "YYYY-MM-DD HH:mm:ss"
		    });
			
			var bidPrice = parseFloat($('#bidPrice').val()).toString();
			if(bidPrice != "NaN"){
				$('#bidPrice').val(bidPrice);//转换投标价
			}
			var meterialExpense = parseFloat($('#meterialExpense').val()).toString();
			if(meterialExpense != "NaN"){
				$('#meterialExpense').val(meterialExpense);//转换劳务费
			}
			var laborCost = parseFloat($('#laborCost').val()).toString();
			if(laborCost != "NaN"){
				$('#laborCost').val(laborCost);//转换材料费
			}
			var deposit = parseFloat($('#deposit').val()).toString();
			if(deposit != "NaN"){
				$('#deposit').val(deposit);//转换递交保证金
			}	
			
			//技术标五大员重复验证:
// 			$("#tec").bind("change",function(){
// 				//change后发送ajax回调到后台
// 				var tecBid = $("#tec").val();
// 				var jsonData = JSON.stringify({"tecBid":tecBid});
// 				$.ajax({
// 					url:"${ctx}/companymanage/bidcompany/validateWorker",
// 					data:jsonData,
// 					type:"post",
// 					dataType:"json",
// 					contentType:"application/json;charset=utf-8",
// 					success:function(data){
// 						//添加提示信息:
// 						if(data!=null){
// 							$("#tecBidMessage").html("五大员重复,请谨慎添加！");
// 							$("#tecBidMessage").css("color","red");
// 						}
// 					},
// 					error:function(){
// 						console.log("回调失败！");
// 					}
// 				});
// 			});
		});
		function companyManage(){
        	
        	jp.openDialogView('单位管理', '${ctx}/programmanage/company/list','1000px', '600px');
			        	
	    }
		
		function workerManage(){
        	
        	jp.openDialog('人员管理', '${ctx}/basicinfo/list','1000px', '600px');
			        	
	    }
	</script>
	<script src="${ctxStatic}/plugin/js/bootstrap-suggest.min.js"></script>
	<script src="${ctxStatic}/plugin/js/data.js"></script>
</head>
<!-- dialog风格页面 -->
<body class="bg-white">
<form:form id="inputForm" modelAttribute="bidcompany" action="${ctx}/companymanage/bidcompany/save" method="post" class="form-horizontal">		<form:hidden path="id"/>
		<sys:message content="${message}"/>
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
		      <tr>
		         <td  class="width-15 active"><label class="pull-right"><font color="red">*</font>参投单位:</label></td>
		         <td class="width-35" >
			  <input type="hidden" class="form-control " name= "company.id" id = "companyId" value = "${bidcompany.company.id}">
					<div class="row">
		                <div class="col-lg-2">
		                    <div class="input-group">
		                        <input type="text" class="form-control required" id="test_data1" <c:if test="${edit}">readOnly="true"</c:if> placeholder="请选择单位，若无可选，请点击维护"
		                        value = "${bidcompany.company.companyName}">
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
				 <td  class="width-15 active">	<label class="pull-right"><font color="red">*</font>投标价(万元):</label></td>
		         <td  class="width-35" >
					<form:input path="bidPrice" htmlEscape="false" placeholder = "单位：万元" class="form-control required isFloatGteZero"/>								          
				 </td>
		      </tr>
		      <tr>
		        <td  class="width-15 active">	<label class="pull-right"> <font color="red">*</font>投标项目名称:</label></td>
		          <td  class="width-35" >
					<input type="hidden" class="form-control " name= "bidtable.id" id = "bidtableId" value ="${bidcompany.bidtable.id}">
<%-- 					<input type="hidden" class="form-control " name= "program.id" id = "programId" value ="${bidcompany.bidtable.program.id}"> --%>
						<div class="row">
			                <div class="col-lg-2">
			                    <div class="input-group">
			                        <input type="text" class="form-control required" id="test_data2" <c:if test="${edit}">readOnly="true"</c:if>
			                        value = "${bidcompany.program.programName}"/>
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
		         <td  class="width-15 active">	<label class="pull-right">让利幅度(%):</label></td>
		         <td  class="width-35" >
					<form:input path="discountRate" htmlEscape="false" placeholder = "单位：%" class="form-control isFloatGteZero "/>								          
				 </td>
		      </tr>
		      <tr>
		          <td  class="width-15 active">	<label class="pull-right"><font color="red">*</font>投标日期:</label></td>
		          <td  class="width-35" >
					<div class='input-group form_datetime' id='bidDate'>
<%-- 						<input id="openBidDate" name="openBidDate" type="hidden" value="${bidcompany.bidtable.openBidDate}"> --%>
	                    <input type='text'  name="bidDate" class="form-control required" id ="bidDateValidate" value="<fmt:formatDate value="${bidcompany.bidDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
	                    <span class="input-group-addon">
	                        <span class="glyphicon glyphicon-calendar"></span>
	                    </span>
	                </div>						            
			      </td>
		          <td  class="width-15 active">	<label class="pull-right">劳务费(万元):</label></td>
		          <td  class="width-35" >
		            <form:input path="laborCost" htmlEscape="false" value="0" placeholder = "单位：万元"  class="form-control required isFloatGteZero"/>
		          </td>
		      </tr> 
		      <tr>
		         <td  class="width-15 active">	<label class="pull-right"><font color="red">*</font>是否中标:</label></td>
		          <c:if test="${isAdd}">
			          <td  class="width-35" >
			            <form:radiobuttons path="isBid"  items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false" class="i-checks required"/>
			          </td>
		          </c:if>
		          <c:if test="${edit}">
			          <td  class="width-35" >
			            <form:radiobuttons path="isBid" disabled="true" items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false" class="i-checks required"/>
			          </td>
			      </c:if>
		         <td  class="width-15 active">	<label class="pull-right"><font color="red">*</font>投标保证金(万元):</label></td>
		          <td  class="width-35" >
		            <form:input path="deposit" htmlEscape="false"  placeholder = "单位：万元(请参照投标模块谨慎填写！)"  class="form-control required isFloatGteZero"/>
		          	<span id = "showdeposit" style="font-weight:bold"></span>
		          </td>
		      </tr>
		      <tr>
		          <td  class="width-15 active">	<label class="pull-right">建造师:</label></td>
		          <td  class="width-35" >
<%-- 			          <form:select path="constructoor.id" class="form-control " id="constr"> --%>
<%-- 							<form:option value="" label="----请选择对应工作人员----"/> --%>
<%-- 							<form:options items="${workerList}" itemLabel="workerName" itemValue="id" htmlEscape="false"/> --%>
<%-- 					  </form:select> --%>
					<input type="hidden" class="form-control " name="constructoor.id"  id="constructoorId" value="${bidcompany.constructoor.id}">
					 <div class="row">
		                <div class="col-lg-6">
		                    <div class="input-group">
		                        <input type="text" class="form-control " id="constructoor" 
		                        value = "${bidcompany.constructoor.user.name}">
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
	           		  <span id = "constructoorMessage" style = "font-weight:bold;"></span>
		          
		          <td  class="width-15 active">	<label class="pull-right">材料费(万元):</label></td>
		          <td  class="width-35" >
		            <form:input path="meterialExpense" htmlEscape="false"  value="0"  class="form-control isFloatGteZero" placeholder = "单位：万元"/>
		          </td>
		      </tr>
		      <tr>
		         <td  class="width-15 active">	<label class="pull-right">技术负责人:</label></td>
		          <td  class="width-35" >
<%-- 			          <form:select path="director.id" class="form-control " id="direct"> --%>
<%-- 							<form:option value="" label="----请选择对应工作人员----"/> --%>
<%-- 							<form:options items="${workerList}" itemLabel="workerName" itemValue="id" htmlEscape="false"/> --%>
<%-- 					  </form:select> --%>
					 <input type="hidden" class="form-control " name= "director.id" id = "directorId" value = "${bidcompany.director.id}">
	 				 <div class="row">
		                <div class="col-lg-6">
		                    <div class="input-group">
		                        <input type="text" class="form-control" id="director" value = "${bidcompany.director.user.name}">
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
	           		 <span id = "directorMessage" style = "font-weight:bold;"></span>
		          </td>
		          <td  class="width-15 active">	<label class="pull-right">技术标:</label></td>
		          <td  class="width-35" >
<%-- 			          <form:select path="tecBidName.id" class="form-control " id="tec"> --%>
<%-- 							<form:option value="" label="----请选择对应工作人员----"/> --%>
<%-- 							<form:options items="${workerList}" itemLabel="workerName" itemValue="id" htmlEscape="false" /> --%>
<%-- 					  </form:select> --%>
					<input type="hidden" class="form-control " name= "tecBidName.id" id = "tecBidNameId" value = "${bidcompany.tecBidName.id}">
					<div class="row">
		                <div class="col-lg-6">
		                    <div class="input-group">
		                        <input type="text" class="form-control" id="tecBidName" value = "${bidcompany.tecBidName.user.name}">
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
					 <span id = "tecBidNameMessage" style = "font-weight:bold;"></span>
		          </td>
		      </tr>
		      <tr>
		           <td  class="width-15 active">	<label class="pull-right">施工员:</label></td>
		          <td  class="width-35" >
<%-- 			          <form:select path="constrworker.id" class="form-control " id="constrworker"> --%>
<%-- 							<form:option value="" label="----请选择对应工作人员----"/> --%>
<%-- 							<form:options items="${workerList}" itemLabel="user.name" itemValue="id" htmlEscape="false"/> --%>
<%-- 						</form:select> --%>
						<input type="hidden" class="form-control " name= "constrworker.id" id = "constrworkerId" value = "${bidcompany.constrworker.id}">
					  <div class="row">
			                <div class="col-lg-6">
			                    <div class="input-group">
			                        <input type="text" class="form-control" id="constrworker" value = "${bidcompany.constrworker.user.name}">
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
	           		   <span id = "constrworkerMessage" style = "font-weight:bold;"></span>
		          </td>
		          <td  class="width-15 active">	<label class="pull-right">商务标:</label></td>
		          <td  class="width-35" >
<%-- 			          <form:select path="comBidName.id" class="form-control " id="com"> --%>
<%-- 							<form:option value="" label="----请选择对应工作人员----"/> --%>
<%-- 							<form:options items="${workerList}" itemLabel="workerName" itemValue="id" htmlEscape="false"/> --%>
<%-- 					  </form:select> --%>
					<input type="hidden" class="form-control " name= "comBidName.id" id = "comBidNameId" value = "${bidcompany.comBidName.id}">
					<div class="row">
		                <div class="col-lg-6">
		                    <div class="input-group">
		                        <input type="text" class="form-control" id="comBidName" value = "${bidcompany.comBidName.user.name}">
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
	           		 <span id = "comBidNameMessage" style = "font-weight:bold;"></span>
		          </td>
		      </tr>
		      <tr>
		      <td  class="width-15 active">	<label class="pull-right">安全员:</label></td>
		          <td  class="width-35" >
<%-- 			          <form:select path="saver.id" class="form-control " id="saver"> --%>
<%-- 							<form:option value="" label="----请选择对应工作人员----"/> --%>
<%-- 							<form:options items="${workerList}" itemLabel="workerName" itemValue="id" htmlEscape="false"/> --%>
<%-- 					  </form:select> --%>
					<input type="hidden" class="form-control " name= "saver.id" id = "saverId" value = "${bidcompany.saver.id}">
					<div class="row">
		                <div class="col-lg-6">
		                    <div class="input-group">
		                        <input type="text" class="form-control" id="saver" value = "${bidcompany.saver.user.name}">
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
	           		 <span id = "saverMessage" style = "font-weight:bold;"></span>
		          </td>
		          
		          <td  class="width-15 active">	<label class="pull-right">材料员:</label></td>
		          <td  class="width-35" >
<%-- 			          <form:select path="meterialer.id" class="form-control " id="meteri"> --%>
<%-- 							<form:option value="" label="----请选择对应工作人员----"/> --%>
<%-- 							<form:options items="${workerList}" itemLabel="workerName" itemValue="id" htmlEscape="false"/> --%>
<%-- 						</form:select> --%>
					<input type="hidden" class="form-control " name= "meterialer.id" id = "meterialerId" value = "${bidcompany.meterialer.id}">
					<div class="row">
		                <div class="col-lg-6">
		                    <div class="input-group">
		                        <input type="text" class="form-control" id="meterialer" value = "${bidcompany.meterialer.user.name}">
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
	           		 <span id = "meterialerMessage" style = "font-weight:bold;"></span>
		         </td>
		      </tr>
		      <tr>
		      <td  class="width-15 active">	<label class="pull-right">质检员:</label></td>
		          <td  class="width-35" >
<%-- 			          <form:select path="inspector.id" class="form-control " id="intece"> --%>
<%-- 							<form:option value="" label="----请选择对应工作人员----"/> --%>
<%-- 							<form:options items="${workerList}" itemLabel="workerName" itemValue="id" htmlEscape="false"/> --%>
<%-- 						</form:select> --%>
					<input type="hidden" class="form-control " name= "inspector.id" id = "inspectorId" value = "${bidcompany.inspector.id}">
					<div class="row">
		                <div class="col-lg-6">
		                    <div class="input-group">
		                        <input type="text" class="form-control" id="inspector" value = "${bidcompany.inspector.user.name}">
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
	           		  <span id = "inspectorMessage" style = "font-weight:bold;"></span>
		          </td>
		          <td  class="width-15 active">	<label class="pull-right">造价员:</label></td>
		          <td  class="width-35" >
<%-- 			          <form:select path="coster.id" class="form-control " id="cost"> --%>
<%-- 							<form:option value="" label="----请选择对应工作人员----"/> --%>
<%-- 							<form:options items="${workerList}" itemLabel="workerName" itemValue="id" htmlEscape="false"/> --%>
<%-- 					  </form:select> --%>
				<input type="hidden" class="form-control " name= "coster.id" id = "costerId" value = "${bidcompany.coster.id}">
					<div class="row">
		                <div class="col-lg-6">
		                    <div class="input-group">
		                        <input type="text" class="form-control" id="coster" value = "${bidcompany.coster.user.name}">
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
	           		  <span id = "costerMessage" style = "font-weight:bold;"></span>
		        </td>
		      </tr>
		      <tr>
		        <td  class="width-15 active">	<label class="pull-right">其他人员:</label></td>
		          <td  class="width-35" >
<%-- 						  <form:input path="otherWorkers.id" htmlEscape="false"  placeholder = "请填写其他人员，如：标准员:李三"  class="form-control"/> --%>
		          			<form:textarea path="otherWorkers.id" htmlEscpe="false" rows="4" placeholder="可填写其他人员，如：标准员：李三" class="form-control"/>
		          </td>
		          <td  class="width-15 active">	<label class="pull-right"></label></td>
		          <td  class="width-35" >
		          </td>
		      </tr>
		   </tbody>
		   </table>   
	</form:form>
</body>
</html>
<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function() {
	$('#table').bootstrapTable({
		  	   //请求方法
               method: 'get',
               //类型json
               dataType: "json",
               //显示刷新按钮
               showRefresh: true,
               //显示切换手机视图按钮
               showToggle: true,
               //显示 内容列下拉框
    	       showColumns: true,
    	       //显示导出按钮
    	       showExport: true,
    	       //显示切换分页按钮
    	       showPaginationSwitch: true,
    	       //最低显示2行
    	       minimumCountColumns: 2,
               //是否显示行间隔色
               striped: true,
               //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）     
               cache: false,    
               //是否显示分页（*）  
               pagination: true,   
                //排序方式 
               sortOrder: "asc",  
               //初始化加载第一页，默认第一页
               pageNumber:1,   
               //每页的记录行数（*）   
               pageSize: 10,  
               //可供选择的每页的行数（*）    
               pageList: [10, 20, 50, 100],
               //这个接口需要处理bootstrap table传递的固定参数,并返回特定格式的json数据  
               url: "${ctx}/procontract/data",
               //默认值为 'limit',传给服务端的参数为：limit, offset, search, sort, order Else
               //queryParamsType:'',   
               ////查询参数,每次调用是会带上这个参数，可自定义                         
               queryParams : function(params) {
               	var searchParam = $("#searchForm").serializeJSON();
               	searchParam.pageNo = params.limit === undefined? "1" :params.offset/params.limit+1;
               	searchParam.pageSize = params.limit === undefined? -1 : params.limit;
               	searchParam.orderBy = params.sort === undefined? "" : params.sort+ " "+  params.order;
                   return searchParam;
               },
               //分页方式：client客户端分页，server服务端分页（*）
               sidePagination: "server",
               contextMenuTrigger:"right",//pc端 按右键弹出菜单
               contextMenuTriggerMobile:"press",//手机端 弹出菜单，click：单击， press：长按。
               contextMenu: '#context-menu',
               onContextMenuItem: function(row, $el){
                   if($el.data("item") == "edit"){
                   	edit(row.id);
                   } else if($el.data("item") == "delete"){
                        jp.confirm('确认要删除该记录吗？', function(){
                       	jp.loading();
                       	jp.get("${ctx}/procontract/delete?id="+row.id, function(data){
                   	  		if(data.success){
                   	  			$('#table').bootstrapTable('refresh');
                   	  			jp.success(data.msg);
                   	  		}else{
                   	  			jp.error(data.msg);
                   	  		}
                   	  	})
                   	   
                   	});
                   } 
               },
              
               onClickRow: function(row, $el){
               },
               columns: [{
		        checkbox: true
		    }
			,{
		        field: 'contractNum',
		        title: '总包合同编号'
		        ,formatter:function(value, row , index){
		        	var approvalStatus = row.approvalStatus;
		        	var contractStatus = row.contractStatus;
		        	if(approvalStatus == 0 || contractStatus == 3){
		        		return '<a  href="#" onclick="jp.openDialog(\'编辑信息\', \'${ctx}/procontract/form?id='+row.id+'\',\'1000px\', \'600px\')">'+value+'</a>';
		        	}else{
		        		return '<a  href="#" onclick="jp.openDialogView(\'查看信息\', \'${ctx}/procontract/form?id='+row.id+'\',\'1000px\', \'600px\')">'+value+'</a>';
		        	}
//	        		return "<a href='javascript:edit(\""+row.id+"\")'>"+value+"</a>";
		         }
		    }
			,{
		        field: 'contractName',
		        title: '合同名称'
		       
		    }
			,{
				field: 'program.getMethod',
				title: '承接方式',
		        formatter:function(value, row , index){
		        	return jp.getDictLabel(${fns:toJson(fns:getDictList('get_method'))}, value, "-");
		        }
			}
			,{
		        field: 'program.programName',
		        title: '项目名称'
		       
		    }
//			,{
//		        field: 'program.programType',
//		        title: '项目类别',
//		        formatter:function(value, row , index){
//		        	var valueArray = value.split(",");
//		        	var labelArray = [];
//		        	for(var i =0 ; i<valueArray.length-1; i++){
//		        		labelArray[i] = jp.getDictLabel(${fns:toJson(fns:getDictList('programtype'))}, valueArray[i], "-");
//		        	}
//		        	return labelArray.join(",");
//		        }
//		       
//		    }
			,{
		        field: 'program.company.companyName',
		        title: '业主单位'
		    }
			,{
		        field: 'program.office.name',
		        title: '承包单位'
		       
		    }
			,{
		        field: 'program.programAddr',
		        title: '工程地址'
		    }
			,{
		        field: 'programConnector',
		        title: '工程联系人'
		    }
			,{
		        field: 'phoneNum',
		        title: '联系电话'
		    }
			,{
		        field: 'startDate',
		        title: '开工日期'
		    }
			,{
		        field: 'completeDate',
		        title: '竣工日期'
		    }
			,{
		        field: 'contractTotalPrice',
		        title: '合同总价(万元)',
		        sortable: true
		    }
//			,{
//		        field: 'contractDate',
//		        title: '合同签订日期',
//		        sortable: true
//		    }
			,{
		        field: 'buildDate',
		        title: '工期'
		    }
			,{
		        field: 'user.name',
		        title: '合同拟草人'
		    }
			,{
		        field: 'createDate',
		        title: '拟草时间'
		    }
			,{
		        field: 'approvalStatus',
		        title: '审核状态',
		        formatter:function(value, row , index){
		        	return jp.getDictLabel(${fns:toJson(fns:getDictList('procontract_approval'))}, value, "-");
		        }
		    }
			,{
		        field: 'contractStatus',
		        title: '合同状态',
		        formatter:function(value, row , index){
		        	return jp.getDictLabel(${fns:toJson(fns:getDictList('contract_status'))}, value, "-");
		        }
		    }
			,{
		        field: 'remarks',
		        title: '备注信息'
		       
		    }
			,{
                field: 'operate',
                title: '操作',
                align: 'center',
                events: {
    		        'click .view': function (e, value, row, index) {
    		        	jp.openTab("${ctx}/enclosuremanage/enclosuretab/list?contractId="+row.id,"总包合同附件",false);
    		        },
    		        'click .enclosureedit': function (e, value, row, index) {
    		        	jp.openDialog('编辑附件', '${ctx}/enclosuremanage/enclosuretab/form?contractId='+row.id,'1000px', '600px');
    		        },
    		        'click .contractedit': function (e, value, row, index) {
    		        	if(row.approvalStatus==0 || row.approvalStatus == 3){
    		        		jp.openDialog('编辑合同正文', '${ctx}/contractTextManage/form?contractId='+row.id,'1000px', '600px');
    		        	}else if(row.approvalStatus==1){
    		        		jp.info("审批中不可操作！");
    		        	}else if(row.approvalStatus==2){
    		        		jp.info("审批通过不可操作！");
    		        	}
    		        },
    		        'click .contractview': function (e, value, row, index) {
    		        	if(row.approvalStatus==0 || row.approvalStatus == 3){
    		        		if(row.contractStatus == 0 || row.contractStatus == 3){
    		        			jp.openDialogView('查看合同正文', '${ctx}/contractTextManage/list?contractId='+row.id,'1000px', '600px');
    		        		}
    		        	}else{
    		        		jp.openDialogView('查看合同正文', '${ctx}/contractTextManage/show?contractId='+row.id,'600px', '200px');
    		        	}
    		        }
    		    },
                formatter: function operateFormatter(value, row, index) {
                	var foreginId = row.id;//获取当前行id
                	var count;
                	$.ajax({
                		url:"${ctx}/programmanage/program/getEnclosureCount",
                		type:"post",
                		async : false,//此处设置异步请求为false，将异步改成同步即可为全局变量count赋值。
                		data:JSON.stringify({"id":foreginId}),
                		contentType:"application/json;charset=utf-8",
                		dataType:"json",
                		success:function(data){
                			console.log(data);
                			count = data;
                		},
                		error:function(){
                			console.log("获取附件数量失败！")
                		}
                	});
                	return [
							'<a href="#" class="view" title="点击查看附件" >',
                	        '<span style="color:green;font-weight:bold;">',
							count,
							'</span>',
							'</a> ',
							'<a href="#" class="enclosureedit"  title="点击编辑附件" >',
							'<i class="fa fa-paperclip"></i>',
							'</a> ',
							'<a href="#" class="contractview" title="查看合同正文" >',
							'<i class="fa fa-file-text-o"></i>',
							'</a> ',
							'<a href="#" class="contractedit" style="color:green;font-weight:bold;" title="编辑合同正文" >',
							'<i class="fa fa-edit"></i>',
							'</a> '
						].join('');
                }
		    }
		     ]
		});
		  
	  if(navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i)){//如果是移动端

		  $('#table').bootstrapTable("toggleView");
		}
	  
	  $('#table').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
                'check-all.bs.table uncheck-all.bs.table', function () {
            $('#remove').prop('disabled', ! $('#table').bootstrapTable('getSelections').length);
            $('#edit').prop('disabled', $('#table').bootstrapTable('getSelections').length!=1);
            $('#startApproval').prop('disabled', $('#table').bootstrapTable('getSelections').length!=1);
            $('#stampApply').prop('disabled', $('#table').bootstrapTable('getSelections').length!=1);
            $('#confirmValid').prop('disabled', $('#table').bootstrapTable('getSelections').length!=1);
            $('#shutdown').prop('disabled', $('#table').bootstrapTable('getSelections').length!=1);
            $('#closeCase').prop('disabled', $('#table').bootstrapTable('getSelections').length!=1);
            $('#viewProcess').prop('disabled', $('#table').bootstrapTable('getSelections').length!=1);
        });
		  
		$("#btnImport").click(function(){
			jp.open({
			    type: 1, 
			    area: [500, 300],
			    title:"导入数据",
			    content:$("#importBox").html() ,
			    btn: ['下载模板','确定', '关闭'],
				    btn1: function(index, layero){
					  window.location='${ctx}/procontract/import/template';
				},
			    btn2: function(index, layero){
				        var inputForm =top.$("#importForm");
				        var top_iframe = top.getActiveTab().attr("name");//获取当前active的tab的iframe 
				        inputForm.attr("target",top_iframe);//表单提交成功后，从服务器返回的url在当前tab中展示
				        inputForm.onsubmit = function(){
				        	jp.loading('  正在导入，请稍等...');
				        }
				        inputForm.submit();
					    jp.close(index);
				  },
				 
				  btn3: function(index){ 
					  jp.close(index);
	    	       }
			}); 
		});
		    
	  $("#search").click("click", function() {// 绑定查询按扭
		  $('#table').bootstrapTable('refresh');
	  });
	 
	 $("#reset").click("click", function() {// 绑定查询按扭
		  $("#searchForm  input").val("");
		  $("#searchForm  select").val("");
		  $("#searchForm  .select-item").html("");
		  $('#table').bootstrapTable('refresh');
		});
	 
		 $('#beginContractDate').datetimepicker({
			 format: "YYYY-MM-DD HH:mm:ss"
		 });
	
		$('#endContractDate').datetimepicker({
			 format: "YYYY-MM-DD HH:mm:ss"
		});
  });
		
  function getContractStatus() {
    return $.map($("#table").bootstrapTable('getSelections'), function (row) {
        return row.contractStatus
    });
  }
  
  function getIsKnocked() {
      return $.map($("#table").bootstrapTable('getSelections'), function (row) {
          return row.isKnocked
      });
  }
  
  function getApprovalStatus() {
      return $.map($("#table").bootstrapTable('getSelections'), function (row) {
          return row.approvalStatus
      });
  }
  
  function getProgramGetMethod() {
	  return $.map($("#table").bootstrapTable('getSelections'), function (row) {
		  return row.program.getMethod
	  });
  }
  
  function getIdSelections() {
      return $.map($("#table").bootstrapTable('getSelections'), function (row) {
          return row.id
      });
  }
  
  function deleteAll(){
	jp.confirm('确认要删除该记录吗？', function(){
		jp.loading();  	
		jp.get("${ctx}/procontract/deleteAll?ids=" + getIdSelections(), function(data){
 	  		if(data.success){
 	  			$('#table').bootstrapTable('refresh');
 	  			jp.success(data.msg);
 	  		}else{
 	  			jp.error(data.msg);
 	  		}
     	})
	})
  }
  
  function startApproval(contractStatus, getMethod, approvalStatus){//启动审批
	  	//合同状态为未生效，用章状态为未申请，审批状态为待审批方可进行启动操作
	  if(contractStatus == undefined){
		  contractStatus = getContractStatus();
	  }
	  if(approvalStatus == undefined){
		  approvalStatus = getApprovalStatus();
	  }
	  
	  if(getMethod == undefined){
		  getMethod = getProgramGetMethod();
	  }
	  
	  if(contractStatus==0 && approvalStatus==0 && getMethod==1){
		  console.log("已进入审批状态！");
		  jp.openTab("${ctx}/act/task/process?id=" + getIdSelections(),"发起流程",false);//新开单位信息管理tab
	   }else if(contractStatus==0 && approvalStatus==0 && getMethod==0){
			  console.log("已进入审批状态！");
			  jp.openTab("${ctx}/act/task/process?id=" + getIdSelections(),"发起流程",false);//新开单位信息管理tab
	   }else{
		  jp.info("当前合同非待审批状态，请检查！");
	   }
   }
  
  function add(){
	  jp.openDialog('新增管理', "${ctx}/procontract/form",'1000px', '600px', $('#table'));
  }
  
  function edit(id, approvalStatus, contractStatus){//没有权限时，不显示确定按钮
  	  if(id == undefined){
		 id = getIdSelections();
	  }
  	 if(contractStatus == undefined){
		  contractStatus = getContractStatus();
	  }
	  if(approvalStatus == undefined){
		  approvalStatus = getApprovalStatus();
	  }
	  if(approvalStatus==0 || contractStatus==3){//未审批、合同终止
		  jp.openDialog('编辑管理', "${ctx}/procontract/form?id=" + id,'1000px', '600px', $('#table')); 
	  }else{
		  jp.openDialogView('查看', "${ctx}/procontract/form?id=" + id,'1000px', '600px', $('#table')); 
	  }
  }
  
  function stampApply(id,approvalStatus){//用印
  	  if(id == undefined){
		 id = getIdSelections();
	  }
  	  console.log("type:"+typeof(id));
  	  if(approvalStatus == undefined){
		  approvalStatus = getApprovalStatus();
	  }
  	  if(approvalStatus == 2){//审批通过
		console.log("审批通过，可用印！");
		jp.openDialog('编辑管理', "${ctx}/contractprint/proprinting/form?proContractId=" + id,'1000px', '600px', $('#table'));
	  }else{
		console.log("不可用印！"); 
		jp.info("非审批通过状态，不可用印！")
	  }
  }
  
  function confirmValid(id,approvalStatus){//确认生效
  	  if(id == undefined){
		 id = getIdSelections();
	  }
  	  if(approvalStatus == undefined){
		  approvalStatus = getApprovalStatus();
	  }
  	  if(approvalStatus == 2){//审批通过
  		console.log("审批通过，可确认生效！");
		var str= id.toString();
    	var jsonData = JSON.stringify({"id":str});
    	//发送验证--用章中是否有记录
    	jp.confirm('您当前正在进行合同生效操作，是否继续？', function(){
    	   jp.openDialog('合同生效确认', "${ctx}/procontract/confirmValid?id=" + id,'600px', '200px', $('#table'));
		})
//		$.ajax({
//			url:"${ctx}/contractprint/proprinting/getContractPrinting",
//    		type:"post",
//    		data:jsonData,
//    		contentType:"application/json;charset=utf-8",
//    		dataType:"json",
//    		success:function(data){
//    			console.log(data);
//    			if(data.length>0){
//    				for(var i=0; i<data.length; i++){
//    					if(data[i].isStamp == '1'){
//    						jp.confirm('您当前正在进行合同生效操作，是否继续？', function(){
//    	    			    	  jp.openDialog('合同生效确认', "${ctx}/procontract/confirmValid?id=" + id,'600px', '200px', $('#table'));
//    						})
//    					}else{
//    	    				jp.info("合同未用印，请先用印！");
//    	    			}
//    				}
////    				jp.openDialogView('查看', "${ctx}/contractprint/proprinting/form?proContractId=" + id,'1000px', '600px', $('#table'));
//    			}else{
//    				jp.info("用印申请无记录！");
////    				jp.openDialog('编辑管理', "${ctx}/contractprint/proprinting/form?proContractId=" + id,'1000px', '600px', $('#table'));
//    			}
//    		},
//			error:function(){
//				console.log("回调失败！")
//			}
//		})
  	  }else{
  		console.log("不可确认生效！"); 
  		jp.info("非审批通过状态，不可确认生效！")
  	  }
  }
  
function shutdown(approvalStatus,getMethod){//终止
	  
	  if(approvalStatus == undefined){
		  approvalStatus = getApprovalStatus();
	  }
	  if(getMethod == undefined){
		  getMethod = getProgramGetMethod();
	  }
	  if(approvalStatus==0){
		  jp.info("当前合同未审批不允许终止!");
	  }else if(approvalStatus==1){//审批中可切换为终止
		  jp.confirm('确认终止当前合同么？', function(){
			  jp.prompt("终止原因",function (text) {
				jp.loading();  	
				jp.get("${ctx}/procontract/shutdown?id=" + getIdSelections()+"&getMethod="+getMethod+"&reason="+encodeURIComponent(text), function(data){
		 	  		if(data.success){
		 	  			$('#table').bootstrapTable('refresh');
		 	  			jp.success(data.msg);
		 	  		}else{
		 	  			jp.error(data.msg);
		 	  		}
				})
			  })
			})
	  }else if(approvalStatus==2){
		  jp.info("当前合同已审批通过不允许终止!");
	  }else{
		  jp.info("状态有误，请检查!"+approvalStatus);
	  }
	
    }
function closeCase(approvalStatus,contractStatus){//终止
	  
	  if(approvalStatus == undefined){
		  approvalStatus = getApprovalStatus();
	  }
	  if(contractStatus == undefined){
		  contractStatus = getContractStatus();
	  }
	  if(approvalStatus==0){
		  jp.info("当前合同未审批不允许结案!");
	  }else if(approvalStatus==1){//审批中可切换为终止
		  jp.info("当前合同审批中不允许结案!");
	  }else if(approvalStatus==2){
		  if(contractStatus == 0){
			  console.log("contractStatus1："+contractStatus);
			  console.log("contractStatus2："+typeof(contractStatus));
			  jp.info("当前合同未生效不允许结案!");
		  }else if(contractStatus == 1){
			  jp.confirm('确认结案当前合同？', function(){
					jp.loading();  	
					jp.get("${ctx}/procontract/closeCase?id=" + getIdSelections(), function(data){
			 	  		if(data.success){
			 	  			$('#table').bootstrapTable('refresh');
			 	  			jp.success(data.msg);
			 	  		}else{
			 	  			jp.error(data.msg);
			 	  		}
					})
			})
		  }else if(contractStatus == 2){
			  jp.info("当前合同已结案!");
		  }else if(contractStatus == 3){
			  jp.info("当前合同已终止!");
		  }else{
			  jp.info("状态有误，请检查!"+contractStatus);
		  }
	  }else{
		  jp.info("状态有误，请检查!"+approvalStatus);
	  }
    }
	function viewProcess(id, approvalStatus, contractStatus, getMethod){//查询审批流程  arg:id
		 if(id == undefined){
			 id = getIdSelections();
		  }
		if(approvalStatus == undefined){
			approvalStatus = getApprovalStatus();
		}
		if(contractStatus == undefined){
			contractStatus = getContractStatus();
		}
		if(getMethod == undefined){
			getMethod = getProgramGetMethod();
		}
		if(approvalStatus==0){
			jp.info("当前合同未审批!");
//		}else if(approvalStatus==1){//
		}else{//
			if(contractStatus == 3){
				jp.info("当前合同已终止!");
			}else{
//				jp.info("0");
				if(getMethod == 1){
//					jp.info("市场投标");
					jp.openDialogView('查看流程信息', "${ctx}/oa/actContract/showActProcess?proContractId=" + id,'1000px', '800px', $('#table'));
				}else if(getMethod == 0){
					jp.openDialogView('查看流程信息', "${ctx}/oa/attachContract/showAttachProcess?proContractId=" + id,'1000px', '800px', $('#table'));
				}
			}
		}
//		}else if(approvalStatus==2){
//			jp.info("当前合同已审批通过!");
//		}else{
//			jp.info("状态有误，请检查!状态码："+approvalStatus);
//		}
	}
</script>
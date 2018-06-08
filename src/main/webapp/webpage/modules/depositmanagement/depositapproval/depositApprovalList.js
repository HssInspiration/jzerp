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
               //显示切换手机试图按钮
               showToggle: true,
               //显示 内容列下拉框
    	       showColumns: true,
    	       //显示到处按钮
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
               pageList: [10, 25, 50, 100],
               //这个接口需要处理bootstrap table传递的固定参数,并返回特定格式的json数据  
               url: "${ctx}/depositApproval/data",
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
                        jp.confirm('确认要删除该保证金审批记录吗？', function(){
                       	jp.loading();
                       	jp.get("${ctx}/depositApproval/delete?id="+row.id, function(data){
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
		        field: 'approvalNum',
		        title: '审批编号',
		        sortable: true,
		        formatter:function(value, row , index){
		        	var status = row.deposit.checkStatus;
		        	if(status == "0" || status == "3"){
		        		return '<a  href="#" onclick="jp.openDialog(\'编辑项目信息\', \'${ctx}/depositApproval/form?id='+row.id+'\',\'1000px\', \'600px\')">'+value+'</a>';
		        	}else{
		        		return '<a  href="#" onclick="jp.openDialogView(\'查看项目信息\', \'${ctx}/depositApproval/form?id='+row.id+'\',\'1000px\', \'600px\')">'+value+'</a>';
		        	}
		        }
		    }
			,{
		        field: 'deposit.depositName',
		        title: '保证金名称',
		        sortable: true
		       
		    }
//			,{
//		        field: 'deposit.program.programName',
//		        title: '所属项目名称',
//		        sortable: true
//		    }
			,{
		        field: 'deposit.depositType',
		        title: '用途(类型)',
		        sortable: true,
		        formatter:function(value, row , index){
		        	return jp.getDictLabel(${fns:toJson(fns:getDictList('deposit_type'))}, value, "-");
		        }
		    }
			,{
		        field: 'deposit.payCount',
		        title: '缴纳金额(万元)',
		        sortable: true
		    }
			,{
		        field: 'payWay',
		        title: '缴纳方式',
		        sortable: true,
		        formatter:function(value, row , index){
		        	return jp.getDictLabel(${fns:toJson(fns:getDictList('pay_way'))}, value, "-");
		        }
		       
		    }
			,{
		        field: 'deposit.checkStatus',
		        title: '审批状态',
		        sortable: true,
		        align:"center",
		        formatter:function(value, row , index){
		        	if(value == '0'){
		        		return '<font color="blue">'+jp.getDictLabel(${fns:toJson(fns:getDictList('deposit_checkstatus'))}, value, "-")+'</font>';
		        	}else if(value == '1'){
		        		return '<font color="orange">'+jp.getDictLabel(${fns:toJson(fns:getDictList('deposit_checkstatus'))}, value, "-")+'</font>';
		        	}else if(value == '2'){
		        		return '<font color="green">'+jp.getDictLabel(${fns:toJson(fns:getDictList('deposit_checkstatus'))}, value, "-")+'</font>';
		        	}else if(value == '3'){
		        		return '<font color="red">'+jp.getDictLabel(${fns:toJson(fns:getDictList('deposit_checkstatus'))}, value, "-")+'</font>';
		        	}
		        }
		    }
			,{
		        field: 'receiver',
		        title: '收款人',
		        sortable: true
		       
		    }
			,{
		        field: 'receiverAccount',
		        title: '收款账户',
		        sortable: true
		       
		    }
			,{
		        field: 'receiverBank',
		        title: '收款银行',
		        sortable: true
		       
		    }
			,{
		        field: 'remittanceBank',
		        title: '汇款银行',
		        sortable: true
		       
		    }
			,{
		        field: 'remittanceAccount',
		        title: '汇款账户',
		        sortable: true
		       
		    }
			,{
		        field: 'checkClass',
		        title: '审批等级',
		        sortable: true,
		        formatter:function(value, row , index){
		        	return jp.getDictLabel(${fns:toJson(fns:getDictList('check_class'))}, value, "-");
		        }
		    }
			,{
		        field: 'operator',
		        title: '经办人(审批)',
		        sortable: true,
		        align:"center",
		        formatter:function(value, row , index){
		        	if(value == '0'){
		        		return '<font color="red">'+jp.getDictLabel(${fns:toJson(fns:getDictList('check_deposit'))}, value, "-")+'</font>';
		        	}else{
		        		return '<font color="green">'+jp.getDictLabel(${fns:toJson(fns:getDictList('check_deposit'))}, value, "-")+'</font>';
		        	}
		        }
		    }
			,{
		        field: 'managingDirector',
		        title: '分管负责人(审批)',
		        sortable: true,
		        align:"center",
		        formatter:function(value, row , index){
		        	if(value == '0'){
		        		return '<font color="red">'+jp.getDictLabel(${fns:toJson(fns:getDictList('check_deposit'))}, value, "-")+'</font>';
		        	}else{
		        		return '<font color="green">'+jp.getDictLabel(${fns:toJson(fns:getDictList('check_deposit'))}, value, "-")+'</font>';
		        	}
		        }
		    }
			,{
		        field: 'topManager',
		        title: '总经理(审批)',
		        sortable: true,
		        align:"center",
		        formatter:function(value, row , index){
		        	if(value == '0'){
		        		return '<font color="red">'+jp.getDictLabel(${fns:toJson(fns:getDictList('check_deposit'))}, value, "-")+'</font>';
		        	}else{
		        		return '<font color="green">'+jp.getDictLabel(${fns:toJson(fns:getDictList('check_deposit'))}, value, "-")+'</font>';
		        	}
		        }
		       
		    }
			,{
		        field: 'chairman',
		        title: '董事长(审批)',
		        sortable: true,
		        align:"center",
		        formatter:function(value, row , index){
		        	if(value == '0'){
		        		return '<font color="red">'+jp.getDictLabel(${fns:toJson(fns:getDictList('check_deposit'))}, value, "-")+'</font>';
		        	}else{
		        		return '<font color="green">'+jp.getDictLabel(${fns:toJson(fns:getDictList('check_deposit'))}, value, "-")+'</font>';
		        	}
		        }
		       
		    }
			,{
		        field: 'groupChairman',
		        title: '集团董事长(审批)',
		        sortable: true,
		        align:"center",
		        formatter:function(value, row , index){
		        	if(value == '0'){
		        		return '<font color="red">'+jp.getDictLabel(${fns:toJson(fns:getDictList('check_deposit'))}, value, "-")+'</font>';
		        	}else{
		        		return '<font color="green">'+jp.getDictLabel(${fns:toJson(fns:getDictList('check_deposit'))}, value, "-")+'</font>';
		        	}
		        }
		       
		    }
			,{
		        field: 'statementDate',
		        title: '出账日期',
		        sortable: true
		       
		    }
			,{
		        field: 'refundDate',
		        title: '退还日期',
		        sortable: true
		       
		    }
			,{
		        field: 'remarks',
		        title: '备注信息',
		        sortable: true
		       
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
            $('#check').prop('disabled', $('#table').bootstrapTable('getSelections').length!=1);
            $('#statement').prop('disabled', getCheckStatusSelection()!=2);//当审批状态为通过时设置确认出账按钮才有效
        });
		  
		$("#btnImport").click(function(){
			jp.open({
			    type: 1, 
			    area: [500, 300],
			    title:"导入数据",
			    content:$("#importBox").html() ,
			    btn: ['下载模板','确定', '关闭'],
				    btn1: function(index, layero){
					  window.location='${ctx}/depositApproval/import/template';
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
		
		$('#beginStatementDate').datetimepicker({
			 format: "YYYY-MM-DD HH:mm:ss"
		});
		$('#endStatementDate').datetimepicker({
			 format: "YYYY-MM-DD HH:mm:ss"
		});
		$('#beginRefundDate').datetimepicker({
			 format: "YYYY-MM-DD HH:mm:ss"
		});
		$('#endRefundDate').datetimepicker({
			 format: "YYYY-MM-DD HH:mm:ss"
		});
		
	});
		
  function getIdSelections() {
        return $.map($("#table").bootstrapTable('getSelections'), function (row) {
            return row.id
        });
  }
  
  function getCheckClassSelection() {
      return $.map($("#table").bootstrapTable('getSelections'), function (row) {
          return row.checkClass
      });
  }
  
  function getCheckStatusSelection() {
      return $.map($("#table").bootstrapTable('getSelections'), function (row) {
    	  var status = row.deposit.checkStatus; 
          return status;
      });
  }
  
//  经办
  function getOperatorSelection() {
      return $.map($("#table").bootstrapTable('getSelections'), function (row) {
          return row.operator
      });
  }
//  分管负责人
  function getManagingDirectorSelection() {
      return $.map($("#table").bootstrapTable('getSelections'), function (row) {
          return row.managingDirector
      });
  }
//  总经理
  function getTopManagerSelection() {
      return $.map($("#table").bootstrapTable('getSelections'), function (row) {
          return row.topManager
      });
  }
//  董事长
  function getChairManSelection() {
      return $.map($("#table").bootstrapTable('getSelections'), function (row) {
          return row.chairman
      });
  }
//  集团董事长
  function getGroupChairManSelection() {
      return $.map($("#table").bootstrapTable('getSelections'), function (row) {
          return row.groupChairman
      });
  }
  
  function deleteAll(){

		jp.confirm('确认要删除该保证金审批记录吗？', function(){
			jp.loading();  	
			jp.get("${ctx}/depositApproval/deleteAll?ids=" + getIdSelections(), function(data){
     	  		if(data.success){
     	  			$('#table').bootstrapTable('refresh');
     	  			jp.success(data.msg);
     	  		}else{
     	  			jp.error(data.msg);
     	  		}
         	})
		})
  }
  
  function add(){
	  jp.openDialog('新增保证金审批', "${ctx}/depositApproval/form",'1000px', '600px', $('#table'));
  }
  
  function edit(id,status){//没有权限时，不显示确定按钮
	 if(id == undefined){
		id = getIdSelections();
	 }
	 
	 if(status == undefined){
		 status = getCheckStatusSelection();
	 }
	 
	 console.log("222:"+status)
	  if(status == "0" || status == "3"){
		  jp.openDialog('编辑保证金审批', "${ctx}/depositApproval/form?id=" + id,'1000px', '600px', $('#table'));
	  }else{
		  jp.openDialogView('查看保证金审批', "${ctx}/depositApproval/form?id=" + id,'1000px', '600px', $('#table'));
	  }
//	   <shiro:hasPermission name="depositapproval:depositApproval:edit">
//	  jp.openDialog('编辑保证金审批', "${ctx}/depositApproval/form?id=" + id,'800px', '500px', $('#table'));
//	   </shiro:hasPermission>
//	  <shiro:lacksPermission name="depositapproval:depositApproval:edit">
//	  jp.openDialogView('查看保证金审批', "${ctx}/depositApproval/form?id=" + id,'800px', '500px', $('#table'));
//	  </shiro:lacksPermission>
  }
  
  function statement(id,checkClass,operator,managingDirector,topManager,chairman,groupChairman){//确认出账
	  console.log("This is:"+getCheckStatusSelection());
	  if(id == undefined){
		 id = getIdSelections();
	  }
	  if(checkClass == undefined){
		 checkClass = getCheckClassSelection();//当前审批等级
	  }
//	  经办
	  if(operator == undefined){
		  operator = getOperatorSelection();
	  }
//	  分管负责人
	  if(managingDirector == undefined){
		  managingDirector = getManagingDirectorSelection();
	  }
//	  总经理
	  if(topManager == undefined){
		  topManager = getTopManagerSelection();
	  }
//	  董事长
	  if(chairman == undefined){
		  chairman = getChairManSelection();
	  }
//	  集团董事长
	  if(groupChairman == undefined){
		  groupChairman = getGroupChairManSelection();
	  }
	  console.log("checkClass:"+checkClass+
			  	  "operator:"+operator+
			  	  "managingDirector:"+managingDirector+
			  	  "topManager:"+topManager+
			  	  "chairman:"+chairman+
			  	  "groupChairman"+groupChairman);
	  //1.当前审批全部通过方可确认出账
	  //2.依据不同的通过情况给出提示
	  if(checkClass=='1'){
		if(operator!=null && operator=="1"){//当前为五级审批，经办非空且审核通过-->设置状态为审核中
			//执行出账操作
			//先设置出账日期为当前日期:
			jp.openDialog('编辑保证金出账管理', "${ctx}/depositStatement/form?approId=" + id,'1000px', '800px', $('#table'));
//			jp.post("${ctx}/depositApproval/changeDate?id=" + id,function(data){
//				if(data.success){
//                	$table.bootstrapTable('refresh');
//                	jp.success(data.msg);
//                }else{
//    	  			jp.error(data.msg);
//                }
//			})
		}else if(operator!=null && operator=="0"){//经办非空且审核不通过
			jp.alert("审核不通过，出账失败！请检查！")
		}
	  }else if(checkClass=='2'){
		if(operator!=null && operator=="1"){//当前为五级审批，经办非空且审核通过-->设置状态为审核中
			if(managingDirector!=null && managingDirector=="1"){//分管领导非空且审核通过
				//执行出账操作
				jp.openDialog('编辑保证金出账管理', "${ctx}/depositStatement/form?approId=" + id,'1000px', '800px', $('#table'));
			}else if(managingDirector!=null && managingDirector=="0"){//分管领导非空且审核不通过
				jp.alert("审核不通过，出账失败！请检查！")
			}
		}else if(operator!=null && operator=="0"){//经办非空且审核不通过
			jp.alert("审核不通过，出账失败！请检查！")
		}
	  }else if(checkClass=='3'){
		  if(operator!=null && operator=="1"){//当前为五级审批，经办非空且审核通过-->设置状态为审核中
				if(managingDirector!=null && managingDirector=="1"){//分管领导非空且审核通过
					if(topManager!=null && topManager=="1"){//总经理非空且审核通过
						//执行出账操作
						jp.openDialog('编辑保证金出账管理', "${ctx}/depositStatement/form?approId=" + id,'1000px', '800px', $('#table'));
					}else if(topManager!=null && topManager=="0"){//总经理非空且审核不通过
						jp.alert("审核不通过，出账失败！请检查！")
					}
				}else if(managingDirector!=null && managingDirector=="0"){//分管领导非空且审核不通过
					jp.alert("审核不通过，出账失败！请检查！")
				}
			}else if(operator!=null && operator=="0"){//经办非空且审核不通过
				jp.alert("审核不通过，出账失败！请检查！")
			}
	  }else if(checkClass=='4'){
		  if(operator!=null && operator=="1"){//当前为五级审批，经办非空且审核通过-->设置状态为审核中
				if(managingDirector!=null && managingDirector=="1"){//分管领导非空且审核通过
					if(topManager!=null && topManager=="1"){//总经理非空且审核通过
						if(chairman!=null && chairman=="1"){//董事长非空且审核通过
							//执行出账操作
							jp.openDialog('编辑保证金出账管理', "${ctx}/depositStatement/form?approId=" + id,'1000px', '800px', $('#table'));
						}else if(chairman!=null && chairman=="0"){//董事长非空且审核不通过
							jp.alert("审核不通过，出账失败！请检查！")
						}
					}else if(topManager!=null && topManager=="0"){//总经理非空且审核不通过
						jp.alert("审核不通过，出账失败！请检查！")
					}
				}else if(managingDirector!=null && managingDirector=="0"){//分管领导非空且审核不通过
					jp.alert("审核不通过，出账失败！请检查！")
				}
			}else if(operator!=null && operator=="0"){//经办非空且审核不通过
				jp.alert("审核不通过，出账失败！请检查！")
			}
	  }else if(checkClass=='5'){
		  if(operator!=null && operator=="1"){//当前为五级审批，经办非空且审核通过-->设置状态为审核中
				if(managingDirector!=null && managingDirector=="1"){//分管领导非空且审核通过
					if(topManager!=null && topManager=="1"){//总经理非空且审核通过
						if(chairman!=null && chairman=="1"){//董事长非空且审核通过
							if(groupChairman!=null && groupChairman=="1"){//集团董事长非空且审核通过-->设置状态为审核通过
								//执行出账操作
								jp.openDialog('编辑保证金出账管理', "${ctx}/depositStatement/form?approId=" + id,'1000px', '800px', $('#table'));
							}else if(groupChairman!=null && groupChairman=="0"){//集团董事长非空且审核不通过
								jp.alert("审核不通过，出账失败！请检查！")
							}
						}else if(chairman!=null && chairman=="0"){//董事长非空且审核不通过
							jp.alert("审核不通过，出账失败！请检查！")
						}
					}else if(topManager!=null && topManager=="0"){//总经理非空且审核不通过
						jp.alert("审核不通过，出账失败！请检查！")
					}
				}else if(managingDirector!=null && managingDirector=="0"){//分管领导非空且审核不通过
					jp.alert("审核不通过，出账失败！请检查！")
				}
			}else if(operator!=null && operator=="0"){//经办非空且审核不通过
				jp.alert("审核不通过，出账失败！请检查！")
			}
	  }else {
		  jp.alert("审核不通过，出账失败！请检查！");
	  }
  }
  
  function check(id){//审批
	  if(id == undefined){
		 id = getIdSelections();
	  }
	  jp.openDialog('编辑保证金审批', "${ctx}/depositApproval/check?id=" + id,'800px', '500px', $('#table'));
  }
</script>
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
               pageList: [10, 25, 50, 100],
               //这个接口需要处理bootstrap table传递的固定参数,并返回特定格式的json数据  
               url: "${ctx}/depositStatement/data",
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
                        jp.confirm('确认要删除该保证金出账记录记录吗？', function(){
                       	jp.loading();
                       	jp.get("${ctx}/depositStatement/delete?id="+row.id, function(data){
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
		        field: 'statementNum',
		        title: '出账编号',
		        sortable: true
		        ,formatter:function(value, row , index){
		        	return "<a href='javascript:edit(\""+row.id+"\")'>"+value+"</a>";
		         }
		       
		    }
			,{
		        field: 'depositApproval.deposit.depositName',
		        title: '保证金名称',
		        sortable: true
		       
		    }
			,{
		        field: 'depositApproval.deposit.depositType',
		        title: '用途(类型)',
		        sortable: true,
		        formatter:function(value, row , index){
		        	return jp.getDictLabel(${fns:toJson(fns:getDictList('deposit_type'))}, value, "-");
		        }
		       
		    }
			,{
		        field: 'depositApproval.payWay',
		        title: '缴纳方式',
		        sortable: true,
		        formatter:function(value, row , index){
		        	return jp.getDictLabel(${fns:toJson(fns:getDictList('pay_way'))}, value, "-");
		        }
		       
		    }
			,{
		        field: 'depositApproval.deposit.payCount',
		        title: '缴纳金额',
		        sortable: true
		       
		    }
			,{
		        field: 'depositApproval.receiver',
		        title: '收款人名称',
		        sortable: true
		       
		    }
			,{
		        field: 'depositApproval.receiverAccount',
		        title: '收款人账号',
		        sortable: true
		       
		    }
			,{
		        field: 'depositApproval.receiverBank',
		        title: '收款银行',
		        sortable: true
		       
		    }
			,{
		        field: 'depositApproval.remittanceBank',
		        title: '汇款银行',
		        sortable: true
		       
		    }
			,{
		        field: 'statementDate',
		        title: '汇票时间',
		        sortable: true
		       
		    }
			,{
		        field: 'ticketHolder',
		        title: '领票人',
		        sortable: true
		       
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
    		        	jp.openTab("${ctx}/enclosuremanage/enclosuretab/list?statementId="+row.id,"出账凭证",false);
    		        },
    		        'click .enclosureedit': function (e, value, row, index) {
    		        	jp.openDialog('编辑出账凭证', '${ctx}/enclosuremanage/enclosuretab/form?statementId='+row.id,'1000px', '600px');
    		        },
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
							'<a href="#" class="view" title="查看出账凭证" >',
                	        '<span style="color:green;font-weight:bold;">',
							count,
							'</span>',
							'</a> ',
							'<a href="#" class="enclosureedit" title="编辑出账凭证" >',
							'<i class="fa fa-paperclip"></i>',
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
        });
		  
		$("#btnImport").click(function(){
			jp.open({
			    type: 1, 
			    area: [500, 300],
			    title:"导入数据",
			    content:$("#importBox").html() ,
			    btn: ['下载模板','确定', '关闭'],
				    btn1: function(index, layero){
					  window.location='${ctx}/depositStatement/import/template';
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
		
	});
		
  function getIdSelections() {
    return $.map($("#table").bootstrapTable('getSelections'), function (row) {
        return row.id
    });
  }
  
  function deleteAll(){
	jp.confirm('确认要删除该保证金出账记录记录吗？', function(){
		jp.loading();  	
		jp.get("${ctx}/depositStatement/deleteAll?ids=" + getIdSelections(), function(data){
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
	  jp.openDialog('新增保证金出账记录', "${ctx}/depositStatement/form",'800px', '500px', $('#table'));
  }
  function edit(id){//没有权限时，不显示确定按钮
//  	  if(id == undefined){
			id = getIdSelections();
//		}
  	 jp.openDialog('编辑保证金出账记录', "${ctx}/depositStatement/form?id=" + id,'800px', '500px', $('#table'));
//	   <shiro:hasPermission name="depositstatement:depositStatement:edit">
//	  jp.openDialog('编辑保证金出账记录', "${ctx}/depositStatement/form?id=" + id,'800px', '500px', $('#table'));
//	   </shiro:hasPermission>
//	  <shiro:lacksPermission name="depositstatement:depositStatement:edit">
//	  jp.openDialogView('查看保证金出账记录', "${ctx}/depositStatement/form?id=" + id,'800px', '500px', $('#table'));
//	  </shiro:lacksPermission>
  }

</script>
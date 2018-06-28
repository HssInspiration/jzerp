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
               pageSize: 20,  
               //可供选择的每页的行数（*）    
               pageList: [20, 25, 50, 100],
               //这个接口需要处理bootstrap table传递的固定参数,并返回特定格式的json数据  
               url: "${ctx}/contractprint/proprinting/data",
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
                       	jp.get("${ctx}/contractprint/proprinting/delete?id="+row.id, function(data){
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
		        field: 'printNum',
		        title: '用章编号'
		    }
           ,{
   		        field: 'printType',
   		        title: '用章类别',
   		        formatter:function(value, row , index){
   		        	return jp.getDictLabel(${fns:toJson(fns:getDictList('print_type'))}, value, "-");
   		        }
   		    }
           ,{
				field: 'printDate',
				title: '用章日期',
				sortable: true
			}
			,{
		        field: 'proContract.contractName',
		        title: '合同名称'
		       
		    }
			,{
		        field: 'proContract.contractTotalPrice',
		        title: '合同总价(万元)'
		    }
			,{
		        field: 'proContract.contractDate',
		        title: '合同签订日期'
		    }
			,{
		        field: 'proContract.user.name',
		        title: '用章申请人'
		       
		    }
			,{
		        field: 'times',
		        title: '用章次数',
		        align: 'center',
		        formatter: function operateFormatter(value, row, index) {
                	return [
            	        '<span style="color:green;font-weight:bold;">',
						row.times,
						'</span>'
					].join('');
                }
		    }
			,{
				field: 'remarks',
				title: '备注信息'
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
            $('#stamp').prop('disabled', $('#table').bootstrapTable('getSelections').length!=1);
            $('#confirmValid').prop('disabled', $('#table').bootstrapTable('getSelections').length!=1);
            
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
	 
		 $('#beginPrintDate').datetimepicker({
			 format: "YYYY-MM-DD HH:mm:ss"
		 });
	
		$('#endPrintDate').datetimepicker({
			 format: "YYYY-MM-DD HH:mm:ss"
		});
  });
		
  function getContractStatus() {
        return $.map($("#table").bootstrapTable('getSelections'), function (row) {
            return row.contratStatus
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
//		  jp.openTab("${ctx}/act/task/form?procDefId=act_contract:2:8f671a80bc3b4039a3e797f2ed0785d2&proContractId="+getIdSelections(),"总包合同（市场投标）审批流程",false);//新开单位信息管理tab
	   }else if(contractStatus==0 && approvalStatus==0 && getMethod==0){
			  console.log("已进入审批状态！");
			  jp.openTab("${ctx}/act/task/process?id=" + getIdSelections(),"发起流程",false);//新开单位信息管理tab
//			  jp.openTab("${ctx}/act/task/form?procDefId=attach_contract_approval:1:65ac57aa9f824134aaa879bcc87204d8&attachContractId="+getIdSelections(),"总包合同（业主指定）审批流程",false);//新开单位信息管理tab
	   }else{
		  jp.info("当前合同非待审批状态，请检查！");
	   }
   }
  
  function add(){
	  jp.openDialog('新增管理', "${ctx}/procontract/form",'1000px', '600px', $('#table'));
  }
  
  function edit(id){//没有权限时，不显示确定按钮
  	  if(id == undefined){
		 id = getIdSelections();
	  }
	  jp.openDialog('编辑管理', "${ctx}/procontract/form?id=" + id,'1000px', '600px', $('#table'));
  }
  
  function stamp(id,approvalStatus){//用印
  	  if(id == undefined){
		 id = getIdSelections();
	  }
  	  if(approvalStatus == undefined){
		  approvalStatus = getApprovalStatus();
	  }
  	  if(approvalStatus == 2){//审批通过
		console.log("审批通过，可用印！");
//		jp.openDialog('编辑管理', "${ctx}/procontract/form?id=" + id,'1000px', '600px', $('#table'));
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
  		jp.confirm('您当前正在进行合同生效操作，是否继续？', function(){
    	  jp.openDialog('合同生效确认', "${ctx}/procontract/confirmValid?id=" + id,'600px', '200px', $('#table'));
		})
  	  }else{
  		console.log("不可确认生效！"); 
  		jp.info("非审批通过状态，不可确认生效！")
  	  }
  }

</script>
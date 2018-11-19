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
   		        checkbox: true
   		    },
   		    {
		        field: 'printNum',
		        title: '用章编号'
		    }
           ,{
   		        field: 'printType',
   		        title: '类型',
   		        formatter:function(value, row , index){
   		        	return jp.getDictLabel(${fns:toJson(fns:getDictList('print_type'))}, value, "-");
   		        }
   		    }
           ,{
		        field: 'printDetailType',
		        title: '用章类别',
		        formatter:function(value, row , index){
		        	var valueArray = value.split(",");
		        	var labelArray = [];
		        	for(var i =0 ; i<valueArray.length-1; i++){
		        		labelArray[i] = jp.getDictLabel(${fns:toJson(fns:getDictList('print_detail_type'))}, valueArray[i], "-");
		        	}
		        	return labelArray.join(",");
		        }
		    }
           ,{
 				field: 'createDate',
 				title: '用章申请日期',
 				sortable: true
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
		        field: 'isStamp',
		        title: '是否用章',
   		        formatter:function(value, row , index){
   		        	return jp.getDictLabel(${fns:toJson(fns:getDictList('yes_no'))}, value, "-");
   		        }
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
            $('#print').prop('disabled', $('#table').bootstrapTable('getSelections').length!=1||getStampStatus() == 1);
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
		
  function getStampStatus() {
        return $.map($("#table").bootstrapTable('getSelections'), function (row) {
            return row.isStamp
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
  
  function add(){
	  jp.openDialog('新增管理', "${ctx}/procontract/form",'1000px', '600px', $('#table'));
  }
  
  function edit(id){//没有权限时，不显示确定按钮
  	  if(id == undefined){
		 id = getIdSelections();
	  }
	  jp.openDialog('编辑管理', "${ctx}/procontract/form?id=" + id,'1000px', '600px', $('#table'));
  }
  
  function print(id){//用印
  	  if(id == undefined){
		 id = getIdSelections();
	  }
	jp.openDialog('用章', "${ctx}/contractprint/proprinting/printForm?id=" + id,'600px', '200px', $('#table'));
  }
  
</script>
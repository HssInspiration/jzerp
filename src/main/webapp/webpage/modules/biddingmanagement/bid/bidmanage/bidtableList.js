<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
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
    	       /*//显示详情按钮
    	       detailView: true,
   	       	   //显示详细内容函数
	           detailFormatter: "detailFormatter",*/
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
               url: "${ctx}/bidmanage/bidtable/data",
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
                   	window.location = "${ctx}/bidmanage/bidtable/form?id=" + row.id;
                   } else if($el.data("item") == "delete"){
                        jp.confirm('确认要删除该投标管理记录吗？', function(){
                       	jp.loading();
                       	jp.get("${ctx}/bidmanage/bidtable/delete?id="+row.id, function(data){
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
		        field: 'bidNum',
		        title: '投标编号',
		        sortable: true,
		        width:30
		        ,formatter:function(value, row , index){
		        	return '<a  href="#" onclick="jp.openDialog(\'编辑投标信息\', \'${ctx}/bidmanage/bidtable/form?id='+row.id+'\',\'1000px\', \'600px\')">'+value+'</a>';
		         }
		    }
			,{
		        field: 'company.companyName',
		        title: '招标单位',
	        	width:20
		    }
			,{
		        field: 'program.programName',
		        title: '项目名称',
		        width:20
		    }
//			,{
//		        field: 'openBidDate',
//		        title: '计划开标时间',
//		        sortable: true
//		       
//		    }
//			,{
//		        field: 'openBidDate',
//		        title: '投标时间',
//		        sortable: true
//		       
//		    }
			,{
				field: 'program.planToStart',
				title: '投标时间',
				width:150
				
			}
			,{
		        field: 'openBidAddr',
		        title: '开标地点',
	        	width:15
		    }
			,{
		        field: 'deposit',
		        title: '投标保证金（万元）',
		        width:15
		    }
			,{
		        field: 'ctrlPrice',
		        title: '控制价（万元）',
	        	width:15
		       
		    }
			,{
		        field: 'floorPrice',
		        title: '标底价（万元）',
	        	width:15
		       
		    }
			,{
		        field: 'provisionPrice',
		        title: '暂列金额（万元）',
	        	width:15
		       
		    }
			,{
				field: 'evaluateMethod',
				title: '评标办法',
				width:15
			}
			,{
		        field: 'recordWorker',
		        title: '开标记录人员',
	        	width:15
		    }
			,{
		        field: 'remarks',
		        title: '备注信息'
		    }
			,{
		        field: 'provideMeterial',
		        title: '所需材料'
		    }
			,{
                field: 'operate',
                title: '操作',
                align: 'center',
                events: {
    		        'click .view': function (e, value, row, index) {
    		        	jp.openTab("${ctx}/enclosuremanage/enclosuretab/list?bidId="+row.id,"投标附件",false);
//    		        	jp.openTab("${ctx}/programmanage/enclosure/list?bidId="+row.id,"附件管理",false);
    		        },
    		        'click .enclosureedit': function (e, value, row, index) {
    		        	jp.openDialog('编辑投标附件', '${ctx}/enclosuremanage/enclosuretab/form?bidId='+row.id,'1000px', '600px');
//    		        	jp.openDialog('编辑附件', '${ctx}/programmanage/enclosure/form?bidId='+row.id,'1000px', '600px');
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
							'<a href="#" class="view" title="点击查看附件" >',
                	        '<span style="color:green;font-weight:bold;">',
							count,
							'</span>',
							'</a> ',
							'<a href="#" class="enclosureedit" title="点击编辑附件" >',
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
					  window.location='${ctx}/bidmanage/bidtable/import/template';
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
	  
	  $('#beginOpenBidDate').datetimepicker({
		  format: "YYYY-MM-DD HH:mm:ss"
	  });
		 
	  $('#endOpenBidDate').datetimepicker({
		  format: "YYYY-MM-DD HH:mm:ss"
	  });
	});
		
  function getIdSelections() {
      return $.map($("#table").bootstrapTable('getSelections'), function (row) {
          return row.id
      });
  }
  
  function deleteAll(){
		jp.confirm('确认要删除该投标管理记录吗？', function(){
			jp.loading();  	
			jp.get("${ctx}/bidmanage/bidtable/deleteAll?ids=" + getIdSelections(), function(data){
     	  		if(data.success){
     	  			$('#table').bootstrapTable('refresh');
     	  			jp.success(data.msg);
     	  		}else{
     	  			jp.error(data.msg);
     	  		}
         	 })
		})
  }
  
  function edit(id){
	  if(id == undefined){
		 id = getIdSelections();
	  }
	  	jp.openDialog('编辑投标信息', '${ctx}/bidmanage/bidtable/form?id=' + getIdSelections(),'1000px', '600px');
  }
  
  function add(){
		jp.openDialog('新建投标信息', '${ctx}/bidmanage/bidtable/form?id=','1000px', '600px');
  }
</script>
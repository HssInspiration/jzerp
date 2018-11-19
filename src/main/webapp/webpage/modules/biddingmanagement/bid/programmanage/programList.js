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
               url: "${ctx}/programmanage/program/dataForJyb",
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
                   	window.location = "${ctx}/programmanage/program/form?id=" + row.id;
                   } else if($el.data("item") == "delete"){
                        jp.confirm('确认要删除该项目工程管理记录吗？', function(){
                       	jp.loading();
                       	jp.get("${ctx}/programmanage/program/delete?id="+row.id, function(data){
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
		        field: 'programNum',
		        title: '项目工程编号',
		        sortable: true
		        ,formatter:function(value, row , index){
		        	if(row.status==0){
		        		return '<a  href="#" onclick="jp.openDialog(\'编辑项目信息\', \'${ctx}/programmanage/program/form?id='+row.id+'\',\'1000px\', \'600px\')">'+value+'</a>';return '<a  href="#" onclick="jp.openDialog(\'编辑项目信息\', \'${ctx}/programmanage/program/form?id='+row.id+'\',\'1000px\', \'600px\')">'+value+'</a>';
		        	}else{
		        		return '<a  href="#" onclick="jp.openDialogView(\'编辑项目信息\', \'${ctx}/programmanage/program/form?id='+row.id+'\',\'1000px\', \'600px\')">'+value+'</a>';return '<a  href="#" onclick="jp.openDialog(\'编辑项目信息\', \'${ctx}/programmanage/program/form?id='+row.id+'\',\'1000px\', \'600px\')">'+value+'</a>';
		        	}
		        	
		         }
		    }
			
			,{
		        field: 'programName',
		        title: '项目工程名称',
		        sortable: true
		    }
			,{
		        field: 'company.companyName',
		        title: '发标单位'
		    }
			,{
		        field: 'getMethod',
		        title: '承接方式',
		        formatter:function(value, row , index){
		        	return jp.getDictLabel(${fns:toJson(fns:getDictList('get_method'))}, value, "-");
		        }
		    }
			,{
		        field: 'office.name',
		        title: '承接分公司'
		    }
			,{
		        field: 'programType',
		        title: '项目工程类别',
		        formatter:function(value, row , index){
		        	var valueArray = value.split(",");
		        	var labelArray = [];
		        	for(var i =0 ; i<valueArray.length-1; i++){
		        		labelArray[i] = jp.getDictLabel(${fns:toJson(fns:getDictList('programtype'))}, valueArray[i], "-");
		        	}
		        	return labelArray.join(",");
		        }
		    }
			,{
		        field: 'proDescription',
		        title: '项目详细描述'
		    }
			,{
				field: 'programConnector',
				title: '项目联系人'
			}
			,{
				field: 'connectorPhone',
				title: '联系号码'
			}
			,{
				field: 'programAddr',
				title: '工程地址'
			}
			,{
		        field: 'status',
		        title: '项目状态',
		        sortable: true,
		        formatter:function(value, row , index){
		        	return jp.getDictLabel(${fns:toJson(fns:getDictList('programstatus'))}, value, "-");
		        }
		       
		    }
			,{
		        field: 'planToStart',
		        title: '计划开标日期'
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
    		        	jp.openTab("${ctx}/enclosuremanage/enclosuretab/list?proid="+row.id,"项目附件",false);
//    		        	jp.openTab("${ctx}/programmanage/enclosure/list?proid="+row.id,"附件管理",false);
    		        },
    		        'click .enclosureedit': function (e, value, row, index) {
    		        	jp.openDialog('编辑项目附件', '${ctx}/enclosuremanage/enclosuretab/form?proid='+row.id,'1000px', '600px');
//    		        	jp.openDialog('编辑附件', '${ctx}/programmanage/enclosure/form?proid='+row.id,'1000px', '600px');
    		        },
					'click .bidmanage': function (e, value, row, index) {
						jp.openDialog('编辑投标管理', '${ctx}/bidmanage/bidtable/newform?proid='+row.id,'1000px', '600px');
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
							'</a> ',
							<shiro:hasPermission name="programmanage:program:addbid">
							'<a href="#" class="bidmanage" title="点击编辑投标管理" >',
							'<i class="fa fa-link"></i>',
							'</a> '
							</shiro:hasPermission>
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
            $('#completed').prop('disabled', $('#table').bootstrapTable('getSelections').length!=1);
            $('#shutdown').prop('disabled', $('#table').bootstrapTable('getSelections').length!=1);
            $('#closecase').prop('disabled', $('#table').bootstrapTable('getSelections').length!=1);
        });
		  
		$("#btnImport").click(function(){
			jp.open({
			    type: 1, 
			    area: [500, 300],
			    title:"导入数据",
			    content:$("#importBox").html() ,
			    btn: ['下载模板','确定', '关闭'],
				    btn1: function(index, layero){
					  window.location='${ctx}/programmanage/program/import/template';
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
		
		$('#beginCallBidDate').datetimepicker({
			 format: "YYYY-MM-DD HH:mm:ss"
		});
		$('#endCallBidDate').datetimepicker({
			 format: "YYYY-MM-DD HH:mm:ss"
		});
		
	});
		
  function getIdSelections() {
        return $.map($("#table").bootstrapTable('getSelections'), function (row) {
            return row.id
        });
  }
  
  function getNameSelection(){
	  return $.map($("#table").bootstrapTable('getSelections'), function (row) {
      	console.log(row.programName);
          return row.programName
      }); 
  }
  
  function getStatusSelection(){
	  return $.map($("#table").bootstrapTable('getSelections'), function (row) {
      	console.log(row.status);
          return row.status
      }); 
  }
  
  function deleteAll(){
		jp.confirm('确认要删除该项目工程管理记录吗？', function(){
			jp.loading();  	
			jp.get("${ctx}/programmanage/program/deleteAll?ids=" + getIdSelections(), function(data){
     	  		if(data.success){
     	  			$('#table').bootstrapTable('refresh');
     	  			jp.success(data.msg);
     	  		}else{
     	  			jp.error(data.msg);
     	  		}
     	  	})
		})
  }
  
  function edit(id,status){
	  if(id == undefined){
		id = getIdSelections();
	  }
	  if(status == undefined){
		  status = getStatusSelection();
	  }
	  if(status==0){
		  jp.openDialog('编辑项目信息', '${ctx}/programmanage/program/formForJyb?id='+id,'1000px', '600px');  
	  }else{
		  jp.openDialogView('查看项目信息', '${ctx}/programmanage/program/formForJyb?id='+id,'1000px', '600px');
	  }
	  
  }
  
  function add(){
	  jp.openDialog('新建项目信息', '${ctx}/programmanage/program/formForJyb','1000px', '600px');
  }
  
  function completed(status){
	  if(status == undefined){
		  status = getStatusSelection();
	  }
	  if(status==0){//自由
		  jp.info("项目自由不允许切换到竣工!");
	  }else if(status==1){//招标
		  jp.info("项目招标中不允许切换到竣工!");
	  }else if(status==2){//施工
		  jp.confirm('确认切换\"'+getNameSelection()+'\"项目状态为竣工么？', function(){
				jp.loading();  	
				jp.get("${ctx}/programmanage/program/completed?id=" + getIdSelections(), function(data){
		 	  		if(data.success){
		 	  			$('#table').bootstrapTable('refresh');
		 	  			jp.success(data.msg);
		 	  		}else{
		 	  			jp.error(data.msg);
		 	  		}
				})
		  })
		  
	  }else if(status==3){//竣工
		  jp.info("当前项目已竣工!");
	  }else if(status==4){//停工
		  jp.info("项目已停工不允许切换到竣工");
	  }else if(status==5){//结案
		  jp.info("项目已结案不允许切换到竣工");
	  }else if(status==6){//未中标
		  jp.info("项目未中标不允许切换到竣工");
	  }else{
		  jp.info("项目状态不正确，请检查!"+status);
	  }
  }
  
  function shutdown(status){
	  
	  if(status == undefined){
		  status = getStatusSelection();
	  }
	  if(status==0){
		  jp.info("项目自由不允许切换到停工!");
	  }else if(status==1){
		  jp.info("项目招标中不允许切换到停工!");
	  }else if(status==2){//施工时才可切换到停工
		  jp.confirm('确认切换\"'+getNameSelection()+'\"项目状态为停工么？', function(){
				jp.loading();  	
				jp.get("${ctx}/programmanage/program/shutdown?id=" + getIdSelections(), function(data){
		 	  		if(data.success){
		 	  			$('#table').bootstrapTable('refresh');
		 	  			jp.success(data.msg);
		 	  		}else{
		 	  			jp.error(data.msg);
		 	  		}
				})
			})
	  }else if(status==3){
		  jp.info("当前项目已竣工不允许切换到停工!");
	  }else if(status==4){
		  jp.info("当前项目已停工!");
	  }else if(status==5){//结案
		  jp.info("项目已结案不允许切换到停 工");
	  }else if(status==6){//未中标
		  jp.info("项目未中标不允许切换到停工");
	  }else{
		  jp.info("项目状态不正确，请检查!"+status);
	  }
	
    }
   
   function closecase(status){
	   
	  if(status == undefined){
		 status = getStatusSelection();
	  }
	  if(status==0){
		  jp.info("项目自由不允许结案!");
	  }else if(status==1){
		  jp.info("项目招标中不允许结案!");
	  }else if(status==2){
		  jp.info("项目施工中不允许结案!");
	  }else if(status==3){//竣工
		  jp.confirm('确认切换\"'+getNameSelection()+'\"项目状态为结案么？', function(){
				jp.loading();  	
				jp.get("${ctx}/programmanage/program/closeCase?id=" + getIdSelections(), function(data){
		 	  		if(data.success){
		 	  			$('#table').bootstrapTable('refresh');
		 	  			jp.success(data.msg);
		 	  		}else{
		 	  			jp.error(data.msg);
		 	  		}
				})
		   }) 
	  }else if(status==4){//停工
		  jp.info("项目已停工不允许结案!");
	  }else if(status==5){//结案
		  jp.info("当前项目已结案!");
	  }else if(status==6){//未中标
		  jp.info("项目未中标不允许切换到结案");
	  }else{
		  jp.info("项目状态不正确，请检查!"+status);
	  }
	  
    }
</script>
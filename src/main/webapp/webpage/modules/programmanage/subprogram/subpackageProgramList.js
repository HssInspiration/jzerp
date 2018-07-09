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
               url: "${ctx}/programmanage/subpackageProgram/data",
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
                        jp.confirm('确认要删除该子项目工程管理记录吗？', function(){
                       	jp.loading();
                       	jp.get("${ctx}/programmanage/subpackageProgram/delete?id="+row.id, function(data){
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
		        field: 'subpackageProgramNum',
		        title: '分包项目编号',
		        width:30
		        ,formatter:function(value, row , index){
		        	return "<a href='javascript:edit(\""+row.id+"\")'>"+value+"</a>";
		         }
		    }
			,{
		        field: 'subpackageProgramName',
		        title: '分包项目名称',
		        sortable: true,
		        width:15
		       
		    }
			//待修改
			,{
		        field: 'program.programName',
		        title: '所属项目名称',
		        width:20
		    }
			,{
		        field: 'subpackageProgramCont',
		        title: '分包项目内容',
		        width:20
		       
		    }
			,{
		        field: 'tenderCompany',
		        title: '招标单位名称',
		        width:20
		    }
			,{
		        field: 'program.office.name',
		        title: '承接分公司'
		    }
			,{
				field: 'program.getMethod',
				title: '承接方式',
		        formatter:function(value, row , index){
		        	return jp.getDictLabel(${fns:toJson(fns:getDictList('get_method'))}, value, "-");
		        }
			}
			,{
		        field: 'subProgramType',
		        title: '分包项目工程类别',
		        width:5,
		        formatter:function(value, row , index){
		        	return jp.getDictLabel(${fns:toJson(fns:getDictList('programtype'))}, value, "-");
		        }
		    }
			,{
		        field: 'planToStart',
		        title: '计划开始时间'
		    }
			
			,{
		        field: 'planToEnd',
		        title: '计划结束时间'
		    }
			,{
		        field: 'subproAddr',
		        title: '分包工程地址'
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
    		        	jp.openTab("${ctx}/enclosuremanage/enclosuretab/list?subproid="+row.id,"子项目附件",false);
    		        },
    		        'click .enclosureedit': function (e, value, row, index) {
    		        	jp.openDialog('编辑子项目附件', '${ctx}/enclosuremanage/enclosuretab/form?subproid='+row.id,'1000px', '600px');
    		        }
//    		        ,'click .subtendermanage': function (e, value, row, index) {
//						jp.openDialog('编辑招标管理', '${ctx}/tendermanage/tender/newform?subproid='+row.id,'1000px', '600px');
//					}
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
							'</a> '
//							,'<a href="#" class="subtendermanage" title="点击编辑招标管理" >',
//							'<i class="fa fa-link"></i>',
//							'</a> '
						].join('');
                }
		    }]
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
					  window.location='${ctx}/programmanage/subpackageProgram/import/template';
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
		
		
	});
		
  function getIdSelections() {
        return $.map($("#table").bootstrapTable('getSelections'), function (row) {
            return row.id
        });
    }
  
  function deleteAll(){

		jp.confirm('确认要删除该子项目工程管理记录吗？', function(){
			jp.loading();  	
			jp.get("${ctx}/programmanage/subpackageProgram/deleteAll?ids=" + getIdSelections(), function(data){
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
	  jp.openDialog('新增子项目工程管理', "${ctx}/programmanage/subpackageProgram/form",'1000px', '800px', $('#table'));
  }
  function edit(id){//没有权限时，不显示确定按钮
  	  if(id == undefined){
			id = getIdSelections();
		}
	  jp.openDialog('编辑子项目工程管理', "${ctx}/programmanage/subpackageProgram/form?id=" + id,'1000px', '800px', $('#table'));
//	   <shiro:hasPermission name="subpackage:subpackageProgram:edit">
//	  jp.openDialog('编辑子项目工程管理', "${ctx}/tendermanage/subpackageProgram/form?id=" + id,'800px', '500px', $('#table'));
//	   </shiro:hasPermission>
//	  <shiro:lacksPermission name="subpackage:subpackageProgram:edit">
//	  jp.openDialogView('查看子项目工程管理', "${ctx}/tendermanage/subpackageProgram/form?id=" + id,'800px', '500px', $('#table'));
//	  </shiro:lacksPermission>
  }

</script>
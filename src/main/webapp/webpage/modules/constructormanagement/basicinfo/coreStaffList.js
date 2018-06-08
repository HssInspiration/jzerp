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
    	       //显示详情按钮
    	       detailView: true,
    	       	//显示详细内容函数
	           detailFormatter: "detailFormatter",
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
               url: "${ctx}/basicinfo/data",
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
                       	jp.get("${ctx}/basicinfo/delete?id="+row.id, function(data){
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
		        field: 'user.name',
		        title: '人员姓名',
		        sortable: true,
		        formatter:function(value, row , index){
		        	var isBuild = row.isBuild;
		        	if(isBuild == "0"){
		        		return '<a  href="#" onclick="jp.openDialog(\'编辑人员信息\', \'${ctx}/basicinfo/form?id='+row.id+'\',\'1000px\', \'600px\')">'+value+'</a>';
		        	}else{
		        		return '<a  href="#" onclick="jp.openDialogView(\'查看人员信息\', \'${ctx}/basicinfo/form?id='+row.id+'\',\'1000px\', \'600px\')">'+value+'</a>';
		        	}
		        }
//		        ,formatter:function(value, row, index){
//   		        	return "<a href='javascript:edit(\""+row.id+"\")'>"+value+"</a>";
//   		         }
		    }
			,{
		        field: 'identityNum',
		        title: '身份证号码',
		        sortable: true
		        
		    }
			,{
		        field: 'phoneNum',
		        title: '手机号码',
		        sortable: true
		    }
			,{
		        field: 'user.office.name',
		        title: '所属机构',
		        sortable: true
		    }
			,{
		        field: 'isBuild',
		        title: '是否有在建项目',
		        sortable: true,
		        align:"center",
		        formatter:function(value, row , index){
		        	if(value == '0'){
		        		return '<font color="red">'+jp.getDictLabel(${fns:toJson(fns:getDictList('yes_no'))}, value, "-")+'</font>';
		        	}else{
		        		return '<font color="green">'+jp.getDictLabel(${fns:toJson(fns:getDictList('yes_no'))}, value, "-")+'</font>';
		        	}
		        }
		    }
			,{
		        field: 'remarks',
		        title: '备注',
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
      });
		  
		$("#btnImport").click(function(){
			jp.open({
			    type: 1, 
			    area: [500, 300],
			    title:"导入数据",
			    content:$("#importBox").html() ,
			    btn: ['下载模板','确定', '关闭'],
				    btn1: function(index, layero){
					  window.location='${ctx}/basicinfo/import/template';
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
	 
	 $("#reset").click("click", function() {// 绑定重置按扭
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
    
    function getIsBuildSelection(){
    	return $.map($("#table").bootstrapTable('getSelections'), function (row) {
            return row.isBuild
        });
    }
  
  function deleteAll(){
	jp.confirm('确认要删除该记录吗？', function(){
		jp.loading();  	
		jp.get("${ctx}/basicinfo/deleteAll?ids=" + getIdSelections(), function(data){
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
	  jp.openDialog('新增人员信息', "${ctx}/basicinfo/form",'1200px', '800px', $('#table'));
  }
  
  function edit(id,isBuild){//没有权限时，不显示确定按钮
  	  if(id == undefined){
			id = getIdSelections();
  	  }
  	  if(isBuild == undefined){
  		isBuild = getIsBuildSelection();
  	  }
  	  if(isBuild=="0"){
  		jp.openDialog('编辑人员信息', "${ctx}/basicinfo/form?id=" + id,'1200px', '800px', $('#table'));
  	  }else{
  		jp.openDialogView('查看人员信息', "${ctx}/basicinfo/form?id=" + id,'1200px', '800px', $('#table'));
  	  }
	  
  }
  
  function detailFormatter(index, row) {
	  var htmltpl =  $("#coreStaffChildrenTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
	  var html = Mustache.render(htmltpl, {
			idx:row.id
	  });
	  $.get("${ctx}/basicinfo/detail?id="+row.id, function(coreStaff){
    	var coreStaffChild1RowIdx = 0, coreStaffChild1Tpl = $("#coreStaffChild1Tpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
		var data1 =  coreStaff.staffCertificateList;
		for (var i=0; i<data1.length; i++){
			addRow('#coreStaffChild-'+row.id+'-1-List', coreStaffChild1RowIdx, coreStaffChild1Tpl, data1[i]);
			coreStaffChild1RowIdx = coreStaffChild1RowIdx + 1;
		}
      })
        return html;
    }
  
	function addRow(list, idx, tpl, row){
		$(list).append(Mustache.render(tpl, {
			idx: idx, delBtn: true, row: row
		}));
	}
			
</script>
<script type="text/template" id="coreStaffChildrenTpl">//<!--
	<div class="tabs-container">
		<ul class="nav nav-tabs">
			<li class="active"><a data-toggle="tab" href="#tab-{{idx}}-1" aria-expanded="true">人员证件表</a></li>
		</ul>
		<div class="tab-content">
				 <div id="tab-{{idx}}-1" class="tab-pane fade in active">
						<table class="ani table">
						<thead>
							<tr>
							<th>证书名称</th>
							<th>证书级别</th>
							<th>对应专业</th>
							<th>注册时间</th>
							<th>失效时间</th>
							<th>证书编号</th>
							<th>注册证号</th>
							</tr>
						</thead>
						<tbody id="coreStaffChild-{{idx}}-1-List">
						</tbody>
					</table>
				</div>
		</div>//-->
	</script>
	<script type="text/template" id="coreStaffChild1Tpl">//<!--
				<tr>
					<td>
						{{row.dictValueName.label}}
					</td>
					
					<td>
						{{row.dictValueClass.label}}
					</td>
					
					<td>
						{{row.dictValueMajor.label}}
					</td>
					
					<td>
						{{row.regisDate}}
					</td>
					
					<td>
						{{row.invalidDate}}
					</td>
					
					<td>
						{{row.certificateFirstNum}}
					</td>
					
					<td>
						{{row.registrationNum}}
					</td>
				</tr>//-->
	</script>

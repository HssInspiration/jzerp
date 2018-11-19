<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function() {
	$('#bidtableTable').bootstrapTable({
		 
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
               url: "${ctx}/bidquerymanage/bidtablequery/data",
               //默认值为 'limit',传给服务端的参数为：limit, offset, search, sort, order Else
               //queryParamsType:'',   
               ////查询参数,每次调用是会带上这个参数，可自定义                         
               queryParams : function(params) {
               	var searchParam = $("#searchForm").serializeJSON();
               	searchParam.pageNo = params.limit === undefined? "1" :params.offset/params.limit+1;
               	searchParam.pageSize = params.limit === undefined? -1 : params.limit;
               	searchParam.orderBy = params.sort === undefined? "" : params.sort+ " "+  params.order;
               	console.log("params:"+params);
               		
	               	var box = document.getElementsByName('program.status');
		       		console.log("box:"+box);
		       		var objArray = box.length;
		       		var statusStr = "";
		       		console.log("objArray:"+objArray);
	       			for(var i=0;i<objArray;i++){
		       			if(box[i].checked == true){
		       				statusStr += box[i].value+","; 
		       			}
		       		}
		       		statusStr = statusStr.substring(0,statusStr.length-1);
		       		console.log("statusStr:"+statusStr);
		       		searchParam.programStatus = statusStr;
		       		console.log("11:"+searchParam.programStatus);
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
                        jp.confirm('确认要删除该投标管理记录吗？', function(){
                       	jp.loading();
                       	jp.get("${ctx}/bidquerymanage/bidtablequery/delete?id="+row.id, function(data){
                   	  		if(data.success){
                   	  			$('#bidtableTable').bootstrapTable('refresh');
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
		        sortable: true
		    }
			,{
		        field: 'company.companyName',
		        title: '招标单位',
		        sortable: true
		    }
			,{
		        field: 'program.programName',
		        title: '项目名称',
		        sortable: true
		    }
			,{
		        field: 'program.status',
		        title: '项目状态',
		        sortable: true,
		        formatter:function(value, row , index){
		        	return jp.getDictLabel(${fns:toJson(fns:getDictList('programstatus'))}, value, "-");
		        }
		    }
			,{
		        field: 'openBidDate',
		        title: '计划开标时间',
		        sortable: true
		       
		    }
			,{
		        field: 'openBidAddr',
		        title: '开标地点',
		        sortable: true
		       
		    }
			,{
		        field: 'deposit',
		        title: '保证金',
		        sortable: true
		       
		    }
			,{
		        field: 'ctrlPrice',
		        title: '控制价',
		        sortable: true
		       
		    }
			,{
		        field: 'floorPrice',
		        title: '标底价',
		        sortable: true
		       
		    }
			,{
		        field: 'provisionPrice',
		        title: '暂列金额',
		        sortable: true
		       
		    }
			,{
				field: 'evaluateMethod',
				title: '评标办法',
				width:15
				
			}
//			,{
//				field: 'coefficient',
//				title: '系数',
//				width:15
//				
//			}
			,{
		        field: 'recordWorker',
		        title: '开标记录人员',
		        sortable: true
		       
		    }
			,{
		        field: 'remarks',
		        title: '备注信息',
		        sortable: true
		       
		    }
			,{
		        field: 'provideMeterial',
		        title: '所需材料',
		        sortable: true,
		    }

		     ]
		
		});
		
		  
	  if(navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i)){//如果是移动端
		  $('#bidtableTable').bootstrapTable("toggleView");
		}
	  
	  $('#bidtableTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
                'check-all.bs.table uncheck-all.bs.table', function () {
            $('#remove').prop('disabled', ! $('#bidtableTable').bootstrapTable('getSelections').length);
            $('#edit').prop('disabled', $('#bidtableTable').bootstrapTable('getSelections').length!=1);
            $('#exportBidTab').prop('disabled', $('#bidtableTable').bootstrapTable('getSelections').length!=1);
        });
		  
		$("#btnImport").click(function(){
			jp.open({
			    type: 1, 
			    area: [500, 300],
			    title:"导入数据",
			    content:$("#importBox").html() ,
			    btn: ['下载模板','确定', '关闭'],
				    btn1: function(index, layero){
					  window.location='${ctx}/bidquerymanage/bidtablequery/import/template';
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
		  $('#bidtableTable').bootstrapTable('refresh');
	 });
	 
	 $("#reset").click("click", function() {// 绑定重置按扭
		  $("#searchForm  .clear").val("");
		  $("#searchForm  select").val("");
		  $("#searchForm  .select-item").html("");
		  $("#searchForm .icheckbox_square-blue").removeClass("checked");//去除选中式样
		  $("#searchForm input[type='checkbox']").prop({"checked":false});//将选中状态去除
		  $('#bidtableTable').bootstrapTable('refresh');
		});
	 
	 $('#beginOpenBidDate').datetimepicker({
		 format: "YYYY-MM-DD HH:mm:ss"
	 });
	 
	 $('#endOpenBidDate').datetimepicker({
		 format: "YYYY-MM-DD HH:mm:ss"
	 });
	 
	});
		
  function getIdSelections() {
        return $.map($("#bidtableTable").bootstrapTable('getSelections'), function (row) {
            return row.id
        });
    }
  
  function deleteAll(){

		jp.confirm('确认要删除该投标管理记录吗？', function(){
			jp.loading();  	
			jp.get("${ctx}/bidquerymanage/bidtablequery/deleteAll?ids=" + getIdSelections(), function(data){
         	  		if(data.success){
         	  			$('#bidtableTable').bootstrapTable('refresh');
         	  			jp.success(data.msg);
         	  		}else{
         	  			jp.error(data.msg);
         	  		}
         	  	})
          	   
		})
  }
  /**
   * 将前台的复选框勾选的值传入到后台
   */
  function checkBoxRemote(){
	  var box = $("#program.status").val();
	  var objArray = box.length;
	  var statusStr = "";
	  
	  for(var i=0;i<objArray;i++){
		  if(box[i].checked == true){
			  statusStr += box[i].value+","; 
		  }
	  }
	  
//	  if(status == "" || status == 0){
//		  jp.info("请勾选需要查询的状态！");
//		  return;
//	  }
	  
	  statusStr = statusStr.substring(0,statusStr.length-1);
  }
  
  
  function add(){
	  jp.openDialog('新增投标管理', "${ctx}/bidquerymanage/bidtablequery/form",'800px', '500px', $('#bidtableTable'));
  }
  
  function edit(id){//没有权限时，不显示确定按钮
  	  if(id == undefined){
			id = getIdSelections();
		}
	  jp.openDialogView('查看投标管理', "${ctx}/bidquerymanage/bidtablequery/form?id=" + id,'800px', '500px', $('#bidtableTable'));
  }
  
  function exportBidTab(id){
	  if(id == undefined){
		  id = getIdSelections();
	  }
	  jp.confirm('确认导出投标项目开标情况表？', function(){
		  window.location.href="${ctx}/bidquerymanage/bidtablequery/exportBidTab?bidId=" + id;
	  })
  }
  
  function detailFormatter(index, row) {
	  var htmltpl =  $("#bidtableChildrenTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
	  var html = Mustache.render(htmltpl, {
			idx:row.id
			
		});
	  $.get("${ctx}/bidquerymanage/bidtablequery/detail?id="+row.id, function(bidtable){
    	var bidtableChild1RowIdx = 0, bidtableChild1Tpl = $("#bidtableChild1Tpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
		var data1 =  bidtable.bidCompanyManageList;
		for (var i=0; i<data1.length; i++){
			addRow('#bidtableChild-'+row.id+'-1-List', bidtableChild1RowIdx, bidtableChild1Tpl, data1[i]);
			bidtableChild1RowIdx = bidtableChild1RowIdx + 1;
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
<script type="text/template" id="bidtableChildrenTpl">//<!--
	<div class="tabs-container">
		<ul class="nav nav-tabs">
			<li class="active"><a data-toggle="tab" href="#tab-{{idx}}-1" aria-expanded="true">参投单位表</a></li>
		</ul>
		<div class="tab-content">
				 <div id="tab-{{idx}}-1" class="tab-pane fade in active">
						<table class="ani table">
						<thead>
							<tr>
								<th>参投单位名称</th>
								<th>投标时间</th>
								<th>投标价(万元)</th>
								<th>让利幅度(%)</th>
								<th>劳务费(万元)</th>
								<th>材料费(万元)</th>
								<th>建造师</th>
								<th>技术负责人</th>
								<th>安全员</th>
								<th>施工员</th>
								<th>质检员</th>
								<th>技术标</th>
								<th>商务标</th>
								<th>材料员</th>
								<th>造价员</th>
								<th>工期</th>
								<th>质量</th>
								<th>是否中标</th>
								<th>投标保证金</th>
							</tr>
						</thead>
						<tbody id="bidtableChild-{{idx}}-1-List">
						</tbody>
					</table>
				</div>
		</div>//-->
	</script>
	<script type="text/template" id="bidtableChild1Tpl">//<!--
				<tr>
					<td>
						{{row.company.companyName}}
					</td>
					<td>
						{{row.bidDate}}
					</td>
					<td>
						{{row.bidPrice}}
					</td>
					<td>
						{{row.discountRate}}
					</td>
					<td>
						{{row.laborCost}}
					</td>
					<td>
						{{row.meterialExpense}}
					</td>
					<td>
						{{row.constructoor.user.name}}
					</td>
					<td>
						{{row.director.user.name}}
					</td>
					<td>
						{{row.saver.user.name}}
					</td>
					<td>
						{{row.constrworker.user.name}}
					</td>
					<td>
						{{row.inspector.user.name}}
					</td>
					<td>
					    {{row.tecBidName.user.name}}
					</td>
					<td>
						{{row.comBidName.user.name}}
					</td>
					<td>
						{{row.meterialer.user.name}}
					</td>
					<td>
						{{row.coster.user.name}}
					</td>
					<td>
						{{row.buildDate}}
					</td>
					<td>
						{{row.quality}}
					</td>
					<td>
						{{row.dictValue.label}}
					</td>
					<td>
						{{row.deposit}}
					</td>
				</tr>//-->
	</script>

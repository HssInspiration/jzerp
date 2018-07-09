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
               pageSize: 10,  
               //可供选择的每页的行数（*）    
               pageList: [10, 25, 50, 100],
               //这个接口需要处理bootstrap table传递的固定参数,并返回特定格式的json数据  
               url: "${ctx}/tendermanage/clearevaluate/data",
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
                        jp.confirm('确认要删除该清评标管理记录吗？', function(){
                       	jp.loading();
                       	jp.get("${ctx}/tendermanage/clearevaluate/delete?id="+row.id, function(data){
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
   		        field: 'subBidCompany.tender.subpackageProgram.subpackageProgramNum',
   		        title: '清评编号',
   		        sortable: true
   		        ,formatter:function(value, row , index){
   		        	return "<a href='javascript:edit(\""+row.id+"\")'>"+value+"</a>";
   		         }
   		    }
   			,{
   		        field: 'subBidCompany.tender.subpackageProgram.subpackageProgramName',
   		        title: '子项目名称',
   		        sortable: true
   		       
   		    }
   			
   			,{
   		        field: 'subBidCompany.company.companyName',
   		        title: '投标单位名称',
   		        sortable: true
   		    }
   			,{
   		        field: 'subBidCompany.subBidPrice',
   		        title: '投标报价',
   		        sortable: true
   		       
   		    }
   			,{
   		        field: 'buildDate',
   		        title: '工期',
   		        align:'center',
   		        sortable: true
   		       
   		    }
   			,{
   		        field: 'performance',
   		        title: '业绩',
   		        align:'center',
   		        sortable: true
   		       
   		    }
   			,{
   		        field: 'isBid',
   		        title: '是否中标',
   		        sortable: true,
   		        align:'center',
   		        formatter:function(value, row , index){
   		        	if(value == '0'){
   		        		return '<font color="red">'+jp.getDictLabel(${fns:toJson(fns:getDictList('yes_no'))}, value, "-")+'</font>';
   		        	}else if(value == '1'){
   		        		return '<font color="green">'+jp.getDictLabel(${fns:toJson(fns:getDictList('yes_no'))}, value, "-")+'</font>';
   		        	}else{
   		        		return '<font color="blue">'+jp.getDictLabel(${fns:toJson(fns:getDictList('yes_no'))}, value, "-")+'</font>';
   		        	}
   		        }
   		    }
   			,{
   		        field: 'certificate',
   		        title: '施工企业资质证书',
   		        sortable: true
   		       
   		    }
   			,{
   		        field: 'writeCircumstances',
   		        title: '投标书填写情况',
   		        sortable: true
   		       
   		    }
   			,{
   		        field: 'secretCircumstances',
   		        title: '投标书密封情况',
   		        sortable: true
   		       
   		    }
   			,{
   		        field: 'subBidCompany.subBidDate',
   		        title: '标书送达时间',
   		        sortable: true
   		        
   		    }
   			,{
   		        field: 'design',
   		        title: '施工组织设计',
   		        sortable: true
   		        
   		    }
   			,{
   		        field: 'remarks',
   		        title: '备注信息',
   		        sortable: true
   		        
   		    }
   			,{
                   field: 'operate',
                   title: '操作',
                   align: 'center',
                   events: {
       		        'click .view': function (e, value, row, index) {
       		        	jp.openTab("${ctx}/enclosuremanage/enclosuretab/list?clearid="+row.id,"开评标附件管理",false);
       		        },
       		        'click .enclosureedit': function (e, value, row, index) {
       		        	jp.openDialog('编辑开评标附件', '${ctx}/enclosuremanage/enclosuretab/form?clearid='+row.id,'1000px', '600px');
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
            $('#isBid').prop('disabled', $('#table').bootstrapTable('getSelections').length!=1);
            $('#exportTemp').prop('disabled', $('#table').bootstrapTable('getSelections').length!=1);
	  });
		  
		$("#btnImport").click(function(){
			jp.open({
			    type: 1, 
			    area: [500, 300],
			    title:"导入数据",
			    content:$("#importBox").html() ,
			    btn: ['下载模板','确定', '关闭'],
				    btn1: function(index, layero){
					  window.location='${ctx}/tendermanage/clearevaluate/import/template';
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
	 
	 $('#accessDate').datetimepicker({
		 format: "YYYY-MM-DD HH:mm:ss"
	});
	 
	});
		
  function getIdSelections() {
        return $.map($("#table").bootstrapTable('getSelections'), function (row) {
            return row.id
        });
  }
  
  function getIsBidSelection() {
      return $.map($("#table").bootstrapTable('getSelections'), function (row) {
          return row.isBid
      });
  }
  
  function getSubProIdSelection(){
	  return $.map($("#table").bootstrapTable('getSelections'), function (row) {
      	console.log(row.subBidCompany.tender.subpackageProgram.id);
          return row.subBidCompany.tender.subpackageProgram.id
      }); 
  }
  
  function getNameSelection(){
	  return $.map($("#table").bootstrapTable('getSelections'), function (row) {
      	console.log(row.subBidCompany.company.companyName);
          return row.subBidCompany.company.companyName
      }); 
  }
  
  function deleteAll(){
		jp.confirm('确认要删除该清评标管理记录吗？', function(){
			jp.loading();  	
			jp.get("${ctx}/tendermanage/clearevaluate/deleteAll?ids=" + getIdSelections(), function(data){
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
	  jp.openDialog('新增清评标管理', "${ctx}/tendermanage/clearevaluate/form",'1000px', '800px', $('#table'));
  }
  
  function edit(id){//没有权限时，不显示确定按钮
  	  if(id == undefined){
			id = getIdSelections();
		}
  	  jp.openDialog('编辑清评标管理', "${ctx}/tendermanage/clearevaluate/form?id=" + id,'1000px', '800px', $('#table'));
  }
  
  function isBid(bidStatus,id){
	    if(bidStatus == undefined){
		   bidStatus = getIsBidSelection();
	    }	
	    
		if(id == undefined){
			id = getIdSelections();
		}
		if(bidStatus[0]==0){//状态为否
			jp.openDialog('请选择中标状态', "${ctx}/tendermanage/clearevaluate/bidStatus?id=" + id,'600px', '200px', $('#table'));
		}else if(bidStatus[0]==1){//状态为是
			jp.openDialog('请选择中标状态', "${ctx}/tendermanage/clearevaluate/bidStatus?id=" + id,'600px', '180px', $('#table'));
		}else{//状态为空
			jp.openDialog('请选择中标状态', "${ctx}/tendermanage/clearevaluate/bidStatus?id=" + id,'600px', '200px', $('#table'));
		}
  }
  
  function exportTemp(proId){
  	  if(proId == undefined){
  		proId = getSubProIdSelection();
	  }
	  jp.confirm('确认导出分包报告？', function(){
		  window.location.href="${ctx}/tendermanage/clearevaluate/exportFbmb?proid=" + proId;
	  })
   }
		   
  function detailFormatter(index, row) {
	  var htmltpl =  $("#clearEvaluateChildrenTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
	  var html = Mustache.render(htmltpl, {
			idx:row.id
		});
	  $.get("${ctx}/tendermanage/clearevaluate/detail?id="+row.id, function(clearEvaluate){
    	var clearEvaluateChild1RowIdx = 0, clearEvaluateChild1Tpl = $("#clearEvaluateChild1Tpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
		var data1 =  clearEvaluate.evaluateUserList;
		for (var i=0; i<data1.length; i++){
			addRow('#clearEvaluateChild-'+row.id+'-1-List', clearEvaluateChild1RowIdx, clearEvaluateChild1Tpl, data1[i]);
			clearEvaluateChild1RowIdx = clearEvaluateChild1RowIdx + 1;
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
<script type="text/template" id="clearEvaluateChildrenTpl">//<!--
	<div class="tabs-container">
		<ul class="nav nav-tabs">
				<li class="active"><a data-toggle="tab" href="#tab-{{idx}}-1" aria-expanded="true">评标人员表</a></li>
		</ul>
		<div class="tab-content">
				 <div id="tab-{{idx}}-1" class="tab-pane fade in active">
						<table class="ani table">
						<thead>
							<tr>
								<th>评标人员名称</th>
								<th>评标人员职务</th>
								<th>备注信息</th>
							</tr>
						</thead>
						<tbody id="clearEvaluateChild-{{idx}}-1-List">
						</tbody>
					</table>
				</div>
		</div>//-->
	</script>
	<script type="text/template" id="clearEvaluateChild1Tpl">//<!--
				<tr>
					<td>
						{{row.user.name}}
					</td>
					<td>
						{{row.user.role.name}}
					</td>
					<td>
						{{row.remarks}}
					</td>
				</tr>//-->
	</script>

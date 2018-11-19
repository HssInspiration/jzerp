<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
<title>用户选择</title>
<meta name="decorator" content="ani" />
<%@ include file="/webpage/include/anihead.jsp"%>
<%@ include file="/webpage/include/bootstraptable.jsp"%>
<%@include file="/webpage/include/treeview.jsp"%>
<script type="text/javascript">
		$(document).ready(function() {
			//bootstrap treeview初始化
			$('#jstree').jstree({
				'core' : {
					"multiple" : false,
					"animation" : 0,
					"themes" : { "variant" : "large", "icons":true , "stripes":true},
					'data' : {
						"url" : "${ctx}/sys/office/treeData",
						"dataType" : "json" // needed only if you do not supply JSON headers
					}
				},
				"conditionalselect" : function (node, event) {
				      return false;
				 },
				'plugins' : ['types', 'wholerow'],
				"types":{ 
					'default' : { 'icon' : 'fa fa-file-text-o' }, 
			        '1' : {'icon' : 'fa fa-home'},
					'2' : {'icon' : 'fa fa-umbrella' },
				    '3' : { 'icon' : 'fa fa-group'},
					'4' : { 'icon' : 'fa fa-file-text-o' }
				} 

			}).bind("activate_node.jstree", function (obj, e) {
			    // 处理代码
			    // 获取当前节点
			    var treeNode = e.node;
			    var id = treeNode.id == '0' ? '' :treeNode.id;
				if(treeNode.level == 0){//level=0 代表公司
					$("#companyId").val(id);
					$("#officeId").val("");
				}else{
					$("#companyId").val("");
					$("#officeId").val(id);
				}
				
				$('#table').bootstrapTable('refresh');
			}).on('loaded.jstree', function() {
				$("#jstree").jstree('open_all');
			});
			var $table;
		    var selectionIds = [];	// 保存选中ids
			//初始化表格
		    $table = $('#table').bootstrapTable({
				  //请求方法
	                method: 'get',
	                dataType: "json",
	                 //是否显示行间隔色
	                striped: true,
	                //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）     
	                cache: false,    
	                //是否显示分页（*）  
	                pagination: true, 
	                showPaginationSwitch: true,//展示页数的选择？
	             	// 是否选中
// 	        		clickToSelect:true,	
// 	        		maintainSelected:true,
	                //排序方式 
	                sortOrder: "asc",    
	            	// 初始化加载第一页，默认第一页
	                pageNumber:1,   
	                // 每页的记录行数（*）
	                pageSize: 85,  
	                // 可供选择的每页的行数（*）
	                pageList: [10, 25, 50,"ALL"],
	                //这个接口需要处理bootstrap table传递的固定参数,并返回特定格式的json数据  
	                url: "${ctx}/sys/user/list",
	                // 在渲染页面数据之前执行的方法，此配置很重要!!!!!!!
// 	                responseHandler:responseHandler,
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
	                onClickRow: function(row, $el){
	                },
	                columns: [{
	                	field:'checkStatus',
	                	<c:if test="${isMultiSelect}">
	                		checkbox: true
	                	</c:if>
	                	<c:if test="${!isMultiSelect}">
	                		radio: true
	                	</c:if>
				    }, {
				        field: 'loginName',
				        title: '登录名',
				        sortable: true
				       
				    }, {
				        field: 'name',
				        title: '姓名',
				        sortable: true,
				    }, {
				        field: 'phone',
				        title: '电话',
				        sortable: true
				    }, {
				        field: 'mobile',
				        title: '手机',
				        sortable: true
				    }, {
				        field: 'company.name',
				        title: '归属公司'
				    }, {
				        field: 'office.name',
				        title: '归属部门'
				    }]
				
				});
			  
			  if(navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i)){//如果是移动端，默认关闭tab
				  $('#table').bootstrapTable("toggleView");
				}
			  
			  $('#table').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
		                'check-all.bs.table uncheck-all.bs.table', function () {
		            $('#remove').prop('disabled', ! $('#table').bootstrapTable('getSelections').length);
		            $('#edit').prop('disabled', $('#table').bootstrapTable('getSelections').length!=1);
		        });
	
			  
				    
			  $("#search").click("click", function() {// 绑定查询按扭
				  $('#table').bootstrapTable('refresh');
				});
			  $("#reset").click("click", function() {// 绑定查询按扭
				  $("#searchForm  input").val("");
				  $("#searchForm  select").val("");
				  zTreeObj.cancelSelectedNode();
				  $('#table').bootstrapTable('refresh');
				});
				// 选中事件操作数组
// 				var union = function(array,ids){
// 					$.each(ids, function (i, id) {
// 						if($.inArray(id,array)==-1){
// 							array[array.length] = id;
// 						}
// 				    	 });
// 				    	return array;
// 				};
// 				// 取消选中事件操作数组
// 			    var difference = function(array,ids){
// 				    	$.each(ids, function (i, id) {
// 				    		 var index = $.inArray(id,array);
// 				    		 if(index!=-1){
// 				    			 array.splice(index, 1);
// 				    		 }
// 				    	 });
// 				    	return array;
// 				};
// 				var _ = {"union":union,"difference":difference};
// 				// 绑定选中事件、取消事件、全部选中、全部取消
// 				$table.on('check.bs.table check-all.bs.table uncheck.bs.table uncheck-all.bs.table', function (e, rows) {
// 				        var ids = $.map(!$.isArray(rows) ? [rows] : rows, function (row) {
// 				                 return row.id;
// 				        });
// 			            func = $.inArray(e.type, ['check', 'check-all']) > -1 ? 'union' : 'difference';
// 			            selectionIds = _[func](selectionIds, ids);	
// 			     });
// 				// 表格分页之前处理多选框数据
// 				function responseHandler(res) {
// 				      $.each(res.rows, function (i, row) {
// 				          row.checkStatus = $.inArray(row.id, selectionIds) != -1;	// 判断当前行的数据id是否存在与选中的数组，存在则将多选框状态变为true
// 				      });
// 				      return res;
// 				}	
			  
		});
		
		  function getIdSelections() {
		        return $.map($("#table").bootstrapTable('getSelections'), function (row) {
		            return row.id
		        });
		    }
		  
		  function getNameSelections() {
		        return $.map($("#table").bootstrapTable('getSelections'), function (row) {
		            return row.name
		        });
		    }
		  
		  function getSelections() {
		        return $.map($("#table").bootstrapTable('getSelections'), function (row) {
		            return row
		        });
		    }
		
	</script>
</head>
<body class="bg-white">
	<div class="wrapper wrapper-content">
		<div class="row">
			<div class="col-sm-6 col-md-4">
				<div id="jstree"></div>
			</div>
			<div class="col-sm-6 col-md-8 animated fadeInRight">
				<!-- 搜索框-->
				<div class="accordion-group">
					<div id="collapseTwo" class="accordion-body">
						<div class="accordion-inner">
							<form id="searchForm" class="form form-horizontal well clearfix">
								<input type="hidden" id="companyId" name="company.id" /> <input
									type="hidden" id="officeId" name="office.id" />
								<div class="col-sm-4">
									<label class="label-item single-overflow pull-left"
										title="登录名：">登录名：</label> <input type="text" name="loginName"
										maxlength="100" class=" form-control" />
								</div>
								<div class="col-sm-4">
									<label class="label-item single-overflow pull-left" title="姓名：">姓名：</label>
									<input type="text" name="name" maxlength="100"
										class=" form-control" />
								</div>
								<div class="col-sm-4">
									<div style="margin-top: 26px">
										<a id="search"
											class="btn btn-primary btn-rounded  btn-bordered btn-sm"><i
											class="fa fa-search"></i> 查询</a> <a id="reset"
											class="btn btn-primary btn-rounded  btn-bordered btn-sm"><i
											class="fa fa-refresh"></i> 重置</a>
									</div>
								</div>
							</form>
						</div>
					</div>
				</div>
				<!-- 搜索框结束 -->
				<!-- 表格 -->
				<table id="table" data-toolbar="#toolbar"
					data-minimum-count-columns="2" data-id-field="id">
				</table>
			</div>
		</div>
	</div>
</body>
</html>
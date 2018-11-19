<%@ page contentType="text/html;charset=UTF-8" %>
<script>
//格式化日期
//Date.prototype.format = function(fmt) { 
//    var o = { 
//       "M+" : this.getMonth()+1,                 //月份 
//       "d+" : this.getDate(),                    //日 
//       "h+" : this.getHours(),                   //小时 
//       "m+" : this.getMinutes(),                 //分 
//       "s+" : this.getSeconds(),                 //秒 
//       "q+" : Math.floor((this.getMonth()+3)/3), //季度 
//       "S"  : this.getMilliseconds()             //毫秒 
//   }; 
//   if(/(y+)/.test(fmt)) {
//           fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length)); 
//   }
//    for(var k in o) {
//       if(new RegExp("("+ k +")").test(fmt)){
//            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
//        }
//    }
//   return fmt; 
//}
//投标时间往前推三个月
//var endDate = new Date().format("yyyy-MM-dd");//当前时间格式化
//var beginDate = new Date().setMonth(new Date().getMonth()-3);//将当前时间往前推三月，得到的时间戳
//var date=new Date(beginDate).format("yyyy-MM-dd");//格式化beginDate
//console.log(endDate);//当前时间
//console.log(date);//往前三月

$(document).ready(function() {
	//加载前将两个日期数据传入后台，findlist中的条件参数不为空便可查询到指定的数据
//	$("#beginDate").val(date);
//	$("#endDate").val(endDate);
		 
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
               url: "${ctx}/companymanage/bidcompany/data?corePersonId=${corePersonId}&bidcompany=${bidcompany}",
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
                   	window.location = "${ctx}/companymanage/bidcompany/form?id=" + row.id;
                   } else if($el.data("item") == "delete"){
                        jp.confirm('确认要删除该参投单位管理记录吗？', function(){
                       	jp.loading();
                       	jp.get("${ctx}/companymanage/bidcompany/delete?id="+row.id, function(data){
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
		        field: 'program.programName',
		        title: '投标项目名称',
		        sortable: true,
		        formatter:function(value, row , index){
		        	return '<a  href="#" onclick="jp.openDialog(\'编辑参投单位信息\', \'${ctx}/companymanage/bidcompany/form?id='+row.id+'\',\'1000px\', \'800px\')">'+value+'</a>';
		        }
		    }
			,{
		        field: 'company.companyName',
		        title: '参投单位',
		        sortable: true
		        
		    }
			,{
		        field: 'bidDate',
		        title: '投标时间',
		        sortable: true
		       
		    }
			,{
		        field: 'bidPrice',
		        title: '投标价（万元）',
		        sortable: true
		       
		    }
			
			,{
		        field: 'discountRate',
		        title: '让利幅度（%）',
		        sortable: true
		       
		    }
			,{
		        field: 'laborCost',
		        title: '劳务费（万元）',
		        sortable: true
		       
		    }
			,{
		        field: 'meterialExpense',
		        title: '材料费（万元）',
		        sortable: true
		       
		    }
			,{
		        field: 'deposit',
		        title: '投标保证金（万元）',
		        sortable: true
		       
		    }
			,{
		        field: 'constructoor.user.name',
		        title: '建造师',
		        sortable: true,
		       
		    }
			,{
		        field: 'director.user.name',
		        title: '技术负责人',
		        sortable: true,
		    }
			,{
		        field: 'saver.user.name',
		        title: '安全员',
		        sortable: true,
		    }
			,{
		        field: 'constrworker.user.name',
		        title: '施工员',
		        sortable: true,
		    }
			,{
		        field: 'inspector.user.name',
		        title: '质检员',
		        sortable: true,
		    }
			,{
		        field: 'tecBidName.user.name',
		        title: '技术标',
		        sortable: true,
		       
		    }
			,{
		        field: 'comBidName.user.name',
		        title: '商务标',
		        sortable: true,
		       
		    }
			,{
		        field: 'meterialer.user.name',
		        title: '材料员',
		        sortable: true,
		    }
			,{
		        field: 'coster.user.name',
		        title: '造价员',
		        sortable: true,
		    }
			,{
		        field: 'otherWorkers.id',
		        title: '其他'
		    }
			,{
				field: 'buildDate',
				title: '工期'
			}
			,{
				field: 'quality',
				title: '质量'
			}
			,{
		        field: 'isBid',
		        title: '是否中标',
		        sortable: true,
		        formatter:function(value, row , index){
		        	if(value == '0'){
		        		return '<font color="red">'+jp.getDictLabel(${fns:toJson(fns:getDictList('yes_no'))}, value, "-")+'</font>';
		        	}else{
		        		return '<font color="green">'+jp.getDictLabel(${fns:toJson(fns:getDictList('yes_no'))}, value, "-")+'</font>';
		        	}
		        }
		    }
			,{
                field: 'operate',
                title: '操作',
                align: 'center',
                events: {
    		        'click .view': function (e, value, row, index) {
    		        	jp.openTab("${ctx}/enclosuremanage/enclosuretab/list?bidCompId="+row.id,"参投附件",false);
    		        	//jp.openDialogView('附件管理', '${ctx}/programmanage/enclosure/list?bidCompId='+row.id,'1000px', '600px');
//    		        	jp.openTab("${ctx}/programmanage/enclosure/list?bidCompId="+row.id,"附件管理",false);
    		        },
    		        'click .enclosureedit': function (e, value, row, index) {
    		        	jp.openDialog('编辑参投附件', '${ctx}/enclosuremanage/enclosuretab/form?bidCompId='+row.id,'1000px', '600px');
    		        	//jp.openTab("${ctx}/programmanage/enclosure/form?bidCompId="+row.id,"附件管理",false);
//    		        	jp.openDialog('编辑附件', '${ctx}/programmanage/enclosure/form?bidCompId='+row.id,'1000px', '600px');
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
					  window.location='${ctx}/companymanage/bidcompany/import/template';
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
		
	 $("#openStatistics").click("click", function() {// 绑定数据统计按扭
		 var proName = $('#searchForm input:eq(0)').val();  // 项目名称
		 var compName = $('#searchForm input:eq(1)').val(); // 单位名称
		 var beginDate = $('#searchForm input:eq(2)').val();// 开始日期
		 var endDate = $('#searchForm input:eq(3)').val();  // 结束日期
		 var isBid = $('#searchForm select').val();         // 是否中标
		 
		 console.log("1:"+proName+"2:"+compName+"3:"
				 	+isBid+"5:"+beginDate+"6:"
				 	+endDate);
		 jp.openDialogView('数据统计', '${ctx}/companymanage/bidcompany/statisticsForm?programName='+proName
									+'&companyName='+compName
									+'&beginBidDate='+beginDate
									+'&endBidDate='+endDate
									+'&isBid='+isBid,'800px', '600px');
	 });
	 
	 $('#beginBidDate').datetimepicker({
		 format: "YYYY-MM-DD HH:mm:ss"
	});
	$('#endBidDate').datetimepicker({
		 format: "YYYY-MM-DD HH:mm:ss"
	});
		
	});
  function getIdSelections() {
        return $.map($("#table").bootstrapTable('getSelections'), function (row) {
            return row.id
        });
    }
  
  function deleteAll(){
		jp.confirm('确认要删除该参投单位管理记录吗？', function(){
			jp.loading();  	
			jp.get("${ctx}/companymanage/bidcompany/deleteAll?ids=" + getIdSelections(), function(data){
         	  		if(data.success){
         	  			$('#table').bootstrapTable('refresh');
         	  			jp.success(data.msg);
         	  		}else{
         	  			jp.error(data.msg);
         	  		}
     	  	})
		})
  }
  
  function edit(){
  	jp.openDialog('编辑参投单位信息', '${ctx}/companymanage/bidcompany/form?id=' + getIdSelections(),'1000px', '800px');
  }
  
  function add(){
	jp.openDialog('新建参投单位信息', '${ctx}/companymanage/bidcompany/form','1000px', '800px');
  }
</script>
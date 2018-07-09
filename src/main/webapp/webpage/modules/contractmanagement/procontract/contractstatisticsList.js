<%@ page contentType="text/html;charset=UTF-8" %>
<script type="text/javascript">
$(document).ready(function() {
	$('#table').bootstrapTable({
		    //请求方法
            method: 'get',
            dataType: "json",
            //是否显示行间隔色
            striped: true,
            //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）     
            cache: false,    
            //是否显示分页（*）  
            pagination: true, 
            
            pageList: [20, 35, 50, 100],
            //这个接口需要处理bootstrap table传递的固定参数,并返回特定格式的json数据  
            url: "${ctx}/contractstatistics/data",
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
                    del(row.id);
                   
                } 
            },
            
            columns: [{
		        checkbox: true
    		    },
            	{
		        field: 'contractNum',
		        title: '总包合同编号',
		        sortable: true
		    }
			,{
		        field: 'contractName',
		        title: '合同名称',
		        sortable: true
		       
		    }
			,{
				field: 'program.getMethod',
				title: '承接方式',
				sortable: true,
		        formatter:function(value, row , index){
		        	return jp.getDictLabel(${fns:toJson(fns:getDictList('get_method'))}, value, "-");
		        }
			}
			,{
		        field: 'program.programName',
		        title: '项目名称',
		        sortable: true
		       
		    }
			,{
		        field: 'program.programType',
		        title: '项目类别',
		        sortable: true,
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
		        field: 'program.company.companyName',
		        title: '业主单位',
		        sortable: true
		    }
			,{
		        field: 'program.office.name',
		        title: '承包单位',
		        sortable: true
		       
		    }
			,{
		        field: 'program.programAddr',
		        title: '工程地址',
		        sortable: true
		    }
			,{
		        field: 'programConnector',
		        title: '工程联系人',
		        sortable: true
		    }
			,{
		        field: 'phoneNum',
		        title: '联系电话',
		        sortable: true
		    }
			,{
		        field: 'startDate',
		        title: '开工日期',
		        sortable: true
		    }
			,{
		        field: 'completeDate',
		        title: '竣工日期',
		        sortable: true
		    }
			,{
		        field: 'contractTotalPrice',
		        title: '合同总价(万元)',
		        sortable: true
		    }
			,{
		        field: 'contractDate',
		        title: '合同签订日期',
		        sortable: true
		    }
			,{
		        field: 'buildDate',
		        title: '工期',
		        sortable: true
		    }
			,{
		        field: 'user.name',
		        title: '合同拟草人',
		        sortable: true
		    }
			,{
		        field: 'approvalStatus',
		        title: '审核状态',
		        sortable: true,
		        formatter:function(value, row , index){
		        	return jp.getDictLabel(${fns:toJson(fns:getDictList('procontract_approval'))}, value, "-");
		        }
		    }
			,{
		        field: 'contractStatus',
		        title: '合同状态',
		        sortable: true,
		        formatter:function(value, row , index){
		        	return jp.getDictLabel(${fns:toJson(fns:getDictList('contract_status'))}, value, "-");
		        }
		    }
			,{
		        field: 'remarks',
		        title: '备注信息',
		        sortable: true
		       
		    }
		    , {
                field: 'operate',
                title: '操作',
                align: 'center',
                events: {
                	'click .showView': function (e, value, row, index) {
    		        	jp.openTab("${ctx}/enclosuremanage/enclosuretab/list?contractId="+row.id,"总包合同附件",false);
    		        },
    		        'click .showSubProContract': function (e, value, row, index) {
    					$("#left").attr("class", "col-sm-6");
    					setTimeout(function(){
    						$("#right").fadeIn(500);
    					},500)
    					$("#proContractLabel").html(row.contractName);
    					$("#proContractId").val(row.id);
    					$('#subProContractTable').bootstrapTable("refresh",{query:{proContractId:row.id}})
    		        }
    		    },
                formatter:  function operateFormatter(value, row, index) {
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
	    		        	'<a href="#" class="showView" title="查看附件" >',
							count,
							'</a> ',
							'<a href="#" class="showSubProContract"  title="查看对应分包合同"><i class="glyphicon glyphicon-eye-open"></i></a>',
    		        ].join('');
    		    }
            }]
			
		});
		
		  if(navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i)){//如果是移动端，默认关闭tab
			  $('#table').bootstrapTable("toggleView");
		  }
		  
		  $('#table').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
	                'check-all.bs.table uncheck-all.bs.table', function () {
	            $('#remove').prop('disabled', ! $('#table').bootstrapTable('getSelections').length);
	            $('#edit').prop('disabled', $('#table').bootstrapTable('getSelections').length!=1);
	            $('#subStatistics').prop('disabled', $('#table').bootstrapTable('getSelections').length!=1);
	        });

		  $("#search").click("click", function() {// 绑定查询按扭
			  $('#table').bootstrapTable('refresh');
			});
		  $("#reset").click("click", function() {// 绑定查询按扭
			  $("#searchForm  input").val("");
			  $("#searchForm  select").val("");
			  $('#table').bootstrapTable('refresh');
			});
		  
		  $("#openStatistics").click("click", function() {// 绑定数据统计按扭
			  console.log("可统计数据！");
				 var contractName = $('#searchForm input:eq(0)').val();  // 合同名称
				 var proName = $('#searchForm input:eq(1)').val(); // 项目名称
				 var userName = $('#searchForm input:eq(2)').val(); // 拟草人名称
				 var officeName = $('#searchForm input:eq(3)').val(); //承包单位
				 var beginDate = $('#searchForm input:eq(4)').val();// 开始日期
				 var endDate = $('#searchForm input:eq(5)').val();  // 结束日期
				 var approvalStatus = $('#searchForm #approvalStatus').val();   // 审批状态
				 var contractStatus = $('#searchForm #contractStatus').val();   // 合同状态
				 console.log("1:"+contractName+"2:"+proName+"3:"
						 	+userName+"4:"+officeName+"5:"+beginDate+"6:"
						 	+endDate+"7:"+approvalStatus+"8:"+contractStatus);
//				 jp.openDialogView('数据统计', '${ctx}/contractstatistics/proStatisticsForm','1000px', '600px');
				 jp.openDialogView('数据统计', '${ctx}/contractstatistics/proStatisticsForm?contractName='+contractName//合同名称
					 	 +'&program.programName='+proName//项目名称
					 	 +'&user.name='+userName//拟草人名称
					 	 +'&program.office.name='+officeName//单位名称
						 +'&beginContractDate='+beginDate//开始时间
						 +'&endContractDate='+endDate//结束时间
						 +'&approvalStatus='+approvalStatus//审批状态
						 +'&contractStatus='+contractStatus,'1000px', '600px');
		  });
		  
		  $('#beginContractDate').datetimepicker({
			 format: "YYYY-MM-DD HH:mm:ss"
		  });
			
		  $('#endContractDate').datetimepicker({
			 format: "YYYY-MM-DD HH:mm:ss"
		  });
	});

    function getIdSelections() {
        return $.map($("#table").bootstrapTable('getSelections'), function (row) {
            return row.id
        });
    }
  
    function subStatistics(id) {//查看分包数据统计
    	if(id == undefined){
   		  id = getIdSelections();
   	    }
    	//此处id为一般的对象，直接用JSON.stringify({"id":id})转化的话会导致解析出的参数带有[];传入后台会报异常，故先转成String 
    	var str= id.toString();
    	var jsonData = JSON.stringify({"id":str});
    	console.log(jsonData);
//    	jp.info("可查询分包数据！");
    	//1.查询当前id下是否有对应的分包数据，若无，给出提示
    	$.ajax({//通过
			url:"${ctx}/contractstatistics/getSubProContractById",
			data:jsonData,
			type:"post",
			contentType:"application/json;charset=utf-8",
			dataType:"json",
			success:function(data){
				console.log("获取成功！"+data);
				if(data != null && data.length>0){//2.如果当期id下有分包合同数据，返回分包合同统计表单
					for(var i=0 ;i<data.length;i++){
						console.log(data[i].subProContractName);
					}
				 jp.openDialogView('分包数据统计', '${ctx}/contractstatistics/subStatisticsForm?id='+str,'1000px', '600px');//合同名称
				}else{
					jp.info("当前总包合同分包数据为空！");
				}
			},
			error:function(){
				console.log("获取失败！");
			}
		});
    }
$(document).ready(function() {
	var $subProContractTable =	$('#subProContractTable').bootstrapTable({
		    //请求方法
            method: 'get',
            dataType: "json",
             //是否显示行间隔色
            striped: true,
            //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）     
            cache: false,    
            //是否显示分页（*）  
            pagination: false,   
            //这个接口需要处理bootstrap table传递的固定参数,并返回特定格式的json数据  
            url: "${ctx}/contractstatistics/getSubProContract",
            //默认值为 'limit',传给服务端的参数为：limit, offset, search, sort, order Else
            //queryParamsType:'',   
            ////查询参数,每次调用是会带上这个参数，可自定义                         
            queryParams : function(params) {
                return {proContractId:$("#proContractId").val()};
            },
            //分页方式：client客户端分页，server服务端分页（*）
            sidePagination: "server",
            columns: [{
		        field: 'subProContractNum',
		        title: '分包合同编号',
		        sortable: true
		    }
		    ,{
		        field: 'proContract.contractName',
		        title: '总包合同名称',
		        sortable: true
		       
		    }
			,{
		        field: 'subProContractName',
		        title: '分包合同名称',
		        sortable: true
		       
		    }
			,{
		        field: 'subpackageProgram.subpackageProgramName',
		        title: '分包项目名称',
		        sortable: true
		       
		    }
			,{
		        field: 'subpackageProgram.subProgramType',
		        title: '分包项目类别',
		        sortable: true,
		        formatter:function(value, row , index){
		        	return jp.getDictLabel(${fns:toJson(fns:getDictList('programtype'))}, value, "-");
		        }
		    }
			,{
		        field: 'employer',
		        title: '发包单位',
		        sortable: true
		    }
			,{
		        field: 'subBidCompany.company.companyName',
		        title: '承包单位',
		        sortable: true
		    }
			,{
		        field: 'subProAddr',
		        title: '工程地址',
		        sortable: true
		    }
			,{
		        field: 'connector',
		        title: '工程联系人',
		        sortable: true
		    }
			,{
		        field: 'phoneNum',
		        title: '联系人号码',
		        sortable: true
		    }
			,{
		        field: 'startDate',
		        title: '开工日期',
		        sortable: true
		    }
			,{
		        field: 'completeDate',
		        title: '竣工日期',
		        sortable: true
		    }
			,{
		        field: 'buildDate',
		        title: '工期',
		        sortable: true
		    }
			,{
		        field: 'subProTotalPrice',
		        title: '合同总价(万元)',
		        sortable: true
		    }
			,{
		        field: 'approvalStatus',
		        title: '审核状态',
		        sortable: true,
		        formatter:function(value, row , index){
		        	return jp.getDictLabel(${fns:toJson(fns:getDictList('procontract_approval'))}, value, "-");
		        }
		    }
			,{
		        field: 'contractStatus',
		        title: '合同状态',
		        sortable: true,
		        formatter:function(value, row , index){
		        	return jp.getDictLabel(${fns:toJson(fns:getDictList('contract_status'))}, value, "-");
		        }
		    }
			,{
		        field: 'user.name',
		        title: '合同拟草人',
		        sortable: true
		    }
			,{
		        field: 'subProContractDate',
		        title: '合同签订日期',
		        sortable: true
		    }
			,{
		        field: 'remarks',
		        title: '备注信息',
		        sortable: true
		       
		    }
		    , {
                field: 'operate',
                title: '操作',
		        sortable:true,
                align: 'center',
                events: {
                	'click .showView': function (e, value, row, index) {
                		console.log("foreginId1:"+row.id);
    		        	jp.openTab("${ctx}/enclosuremanage/enclosuretab/list?subContractId="+row.id,"分包合同附件",false);
    		        }
    		    },
                formatter:  function operateFormatter(value, row, index) {
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
                		'<a href="#" class="showView" title="查看附件" >',
						count,
						'</a> ',
					].join('');
    		    }
            }]
		});
	  if(navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i)){//如果是移动端
		  $('#subProContractTable').bootstrapTable("toggleView");
	  }
	});
</script>
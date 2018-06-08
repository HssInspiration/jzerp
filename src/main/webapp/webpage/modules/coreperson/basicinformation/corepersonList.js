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
                url: "${ctx}/basicinformation/data",
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
			       
			    }, {
			        field: 'user.name',
			        title: '人员姓名',
			        sortable:true,
//			        align:"center",
			        formatter:function(value, row , index){
			        	var isBuild = row.isBuild;
			        	if(isBuild == "0"){
			        		return '<a  href="#" onclick="jp.openDialog(\'编辑人员信息\', \'${ctx}/basicinformation/form?id='+row.id+'\',\'1000px\', \'600px\')">'+value+'</a>';
			        	}else{
			        		return '<a  href="#" onclick="jp.openDialogView(\'查看人员信息\', \'${ctx}/basicinformation/form?id='+row.id+'\',\'1000px\', \'600px\')">'+value+'</a>';
			        	}
			        }
			    }, {
			        field: 'identityNum',
			        title: '证件号码',
			        sortable:true
			    }
			    , {
			        field: 'user.mobile',
			        title: '手机号码',
			        sortable:true
			    }
			    , {
			        field: 'isBuild',
			        title: '是否有在建项目',
			        sortable:true,
			        align:"center",
			        formatter:function(value, row, index){
			        	if(value == '0'){
			        		return '<font color="red">'+jp.getDictLabel(${fns:toJson(fns:getDictList('yes_no'))}, value, "-")+'</font>';
			        	}else{
			        		return '<font color="green">'+jp.getDictLabel(${fns:toJson(fns:getDictList('yes_no'))}, value, "-")+'</font>';
			        	}
			        }
			    }
			    , {
			    	field: 'remarks',
			    	title: '备注',
			    	sortable:true
			    }
			    , {
                    field: 'operate',
                    title: '操作',
                    align: 'center',
                    events: {
//        		        'click .view': function (e, value, row, index) {
//        		        	jp.openDialogView('查看', '${ctx}/basicinformation/form?id=' + row.id,'800px', '500px');
//        		        },
//        		        'click .edit': function (e, value, row, index) {
//        		        	jp.openDialog('编辑', '${ctx}/basicinformation/form?id=' + row.id,'800px', '500px');
//        		        },
//        		        'click .del': function (e, value, row, index) {
//        		        	del(row.id);
//        		        },
        		        'click .setPersonCertificate': function (e, value, row, index) {
        		        	
        					$("#left").attr("class", "col-sm-6");
        					setTimeout(function(){
        						$("#right").fadeIn(500);
        					},500)
        					$("#corePersonLabel").html(row.user.name);
        					$("#corePersonId").val(row.id);
        					$('#personCertificateTable').bootstrapTable("refresh",{query:{corePersonId:row.id}})
        		        }
        		    },
                    formatter:  function operateFormatter(value, row, index) {
        		        return [
    							'<a href="#" class="setPersonCertificate"  title="管理证书"><i class="glyphicon glyphicon-book"></i>管理证书</a>'
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
	        });

		  $("#search").click("click", function() {// 绑定查询按扭
			  $('#table').bootstrapTable('refresh');
			});
		  $("#reset").click("click", function() {// 绑定查询按扭
			  $("#searchForm  input").val("");
			  $("#searchForm  select").val("");
			  $('#table').bootstrapTable('refresh');
			});
		  
	});

  function getIdSelections() {
        return $.map($("#table").bootstrapTable('getSelections'), function (row) {
            return row.id
        });
    }
  function getIsBuild() {
	  return $.map($("#table").bootstrapTable('getSelections'), function (row) {
		  return row.isBuild
	  });
  }
  
  function add(){
	  jp.openDialog('新建', '${ctx}/basicinformation/form','1000px', '600px')
  }
  
  function edit(id,isBuild){
	  if(!id){
		  id = getIdSelections();
	  }
	  if(!isBuild){
		  isBuild = getIsBuild();
	  }
	  if(isBuild==0){
		  jp.openDialog('编辑', "${ctx}/basicinformation/form?id=" + id,'1000px', '600px')  
	  }else if(isBuild==1){
		  jp.openDialogView('编辑', "${ctx}/basicinformation/form?id=" + id,'1000px', '600px') 
	  }
	  
	  
  }
  function del(ids){
		if(!ids){
			ids = getIdSelections();
			//alert(ids);
		}
		
		jp.loading();
		jp.confirm('确认要删除选中的值吗？',  function(){
			//alert(getIdSelections());
     	  	$.get("${ctx}/basicinformation/deleteAll?ids=" + ids, function(data){
     	  		if(data.success){
     	  			$('#table').bootstrapTable('refresh');
    	  			jp.success(data.msg);
    	  		}else{
    	  			jp.error(data.msg);
    	  		}
     	  	})
		})
  }
  
  
$(document).ready(function() {
	var $personCertificateTable =	$('#personCertificateTable').bootstrapTable({
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
                url: "${ctx}/basicinformation/getPersonCertificate",
                //默认值为 'limit',传给服务端的参数为：limit, offset, search, sort, order Else
                //queryParamsType:'',   
                ////查询参数,每次调用是会带上这个参数，可自定义                         
                queryParams : function(params) {
                    return {corePersonId:$("#corePersonId").val()};
                },
                //分页方式：client客户端分页，server服务端分页（*）
                sidePagination: "server",
                columns: [{
			        field: 'certificateName',
			        title: '证书名称',
			        sortable:true,
			        formatter:function(value, row , index){
			        	return jp.getDictLabel(${fns:toJson(fns:getDictList('certificate_type'))}, value, "-");
			        }
			    }, {
			        field: 'certificateClass',
			        title: '证书级别',
			        sortable:true,
			        formatter:function(value, row , index){
			        	var certificateName = row.certificateName;
			        	if(certificateName != null){
			        		if(certificateName=="1"){//建造师
			        			return jp.getDictLabel(${fns:toJson(fns:getDictList('constructor_class'))}, value, "-");
			        		}
			        		if(certificateName=="2"){//职称证
			        			return jp.getDictLabel(${fns:toJson(fns:getDictList('position_class'))}, value, "-");
			        		}
			        		if(certificateName=="11"){//造价员
			        			return jp.getDictLabel(${fns:toJson(fns:getDictList('costmember_calss'))}, value, "-");
			        		}
			        	}
			        }
			    }
			    ,{
			        field: 'certificateMajor',
			        title: '专业分类',
			        sortable: true,
			        formatter:function(value, row , index){
			        	var valueArray = value.split(",");
			        	var labelArray = [];
			        	for(var i =0 ; i<valueArray.length-1; i++){
			        		labelArray[i] = jp.getDictLabel(${fns:toJson(fns:getDictList('certificate_major'))}, valueArray[i], "-");
			        	}
			        	return labelArray.join(",");
			        }
			    }
			    ,{
			    	field: 'regisDate',
			        title: '注册时间',
			        sortable:true,
			       
			    }
			    ,{
			    	field: 'invalidDate',
			        title: '失效时间',
			        sortable:true,
			       
			    }
			    , {
			    	field: 'isInvalid',
			    	title: '是否失效',
			    	sortable:true,
			        align:"center",
			        events: {
			        	'click .change': function (e, value, row, index) {
				        	jp.confirm('确认要切换证书状态吗？', function(){//
				    			jp.loading();  	
					    		jp.get("${ctx}/basicinformation/change?personCertificateId=" + row.id, function(data){
				           	  		if(data.success){
				           	  			$('#table').bootstrapTable('refresh');
				           	  			$('#personCertificateTable').bootstrapTable("refresh");
				           	  			jp.success(data.msg);
				           	  		}else{
				           	  			jp.error(data.msg);
				           	  		}
					           	})
				    		})
			        	}
			        },
			        formatter:function(value, row , index){
			        	if(value == '0'){
			        		return '<a href="#" class="change" title="点击切换状态" ><font color="red">'+jp.getDictLabel(${fns:toJson(fns:getDictList('yes_no'))}, value, "-")+'</font></a>';
			        	}else if(value == '1'){
			        		return '<a href="#" class="change" title="点击切换状态" ><font color="green">'+jp.getDictLabel(${fns:toJson(fns:getDictList('yes_no'))}, value, "-")+'</font></a>';
			        	}
			        }
			    }
			    , {
			    	field: 'invalidReason',
			    	title: '失效原因',
			    	sortable:true
			    }
			    ,{
			    	field: 'certificateFirstNum',
			        title: '证书编号1',
			        sortable:true,
			       
			    }
			    ,{
			    	field: 'certificateSecondNum',
			        title: '证书编号2',
			        sortable:true,
			       
			    }
			    ,{
			    	field: 'certificateThirdNum',
			        title: '证书编号3',
			        sortable:true,
			       
			    }
			    ,{
			    	field: 'registrationNum',
			        title: '注册证号',
			        sortable:true,
			       
			    }
			    
			    , {
                    field: 'operate',
                    title: '操作',
			        sortable:true,
                    align: 'center',
                    events: {
                    	 'click .edit': function (e, value, row, index) {
            		        	
                    		 jp.openDialog('编辑', '${ctx}/basicinformation/personCertificateForm?corePersonId=' + $("#corePersonId").val()+"&personCertificateId="+row.id,'800px', '500px', $personCertificateTable);
            		        },
        		        'click .del': function (e, value, row, index) {
        		        	
        		        	jp.confirm('确认要删除该证书吗？',function(){
        		        		jp.loading();
        		        		$.get('${ctx}/basicinformation/deletePersonCertificate?personCertificateId='+row.id+'&corePersonId=' + $("#corePersonId").val(), function(data){
  	                    	  		if(data.success){
  	                    	  			$('#personCertificateTable').bootstrapTable("refresh");
  	                    	  			jp.success(data.msg);
  	                    	  		}else{
  	                    	  			jp.error(data.msg);
  	                    	  		}
  	                    	  	})
        		        	});
        		        }
        		    },
                    formatter:  function operateFormatter(value, row, index) {
        		        return [
//        		        	<shiro:hasPermission name="sys:dict:edit">
    						'<a href="#" class="edit" title="编辑" ><i class="glyphicon glyphicon-edit"></i> </a>',
//    						</shiro:hasPermission>
//        		        	<shiro:hasPermission name="sys:dict:edit">
    						'<a href="#" class="del" title="删除" ><i class="glyphicon glyphicon-remove"> </a>'
//    						</shiro:hasPermission>
        		        ].join('');
        		    }
                }]
			
			});
		
		  if(navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i)){//如果是移动端
			  $('#personCertificateTable').bootstrapTable("toggleView");
		  }
		  
		  $("#personCertificateButton").click(function(){
				jp.openDialog('添加证书', '${ctx}/basicinformation/personCertificateForm?corePersonId=' + $("#corePersonId").val(),'1000px', '600px', $personCertificateTable);
			});
		  });
		
	</script>
//(function() {
	/** * 请求json格式的数据  {value: [key:value,key2:value2]}; */

	function autoFillData(partXId,reuqestUrl,certificateName){
	     $("#"+partXId).bsSuggest({
	    	url: reuqestUrl+certificateName,
	        getDataMethod : "url",
	        showBtn: false,     //不显示下拉按钮
	        delayUntilKeyup: true, //获取数据的方式为 firstByUrl 时，延迟到有输入/获取到焦点时才请求数据
	        idField: "id\" style=\"display:none\"",
	        keyField: "word",
	        clearable: true,
	        processData : function(json) {
//				  console.log("回调json为:"+json);
			      var i, len, data = {
			          value : []
			      };
			      if (json.length == 0) {
			          return false
			      } 
			      len = json.length;
			      for (i = 0; i < len; i++) {
			          data.value.push({
			        	  "id\" style=\"display:none\"":json[i].id,
			              "word":json[i].user.name
			              })
			          }
			          return data
			  }
	     });
	 }

	function autoFillData1(partXId,reuqestUrl1){
	     $("#"+partXId).bsSuggest({
	    	url: reuqestUrl1,
	        getDataMethod : "url",
	        showBtn: false,     //不显示下拉按钮
	        delayUntilKeyup: true, //获取数据的方式为 firstByUrl 时，延迟到有输入/获取到焦点时才请求数据
	        idField: "id\" style=\"display:none\"",
	        keyField: "word",
	        clearable: true,
	        processData : function(json) {
			      var i, len, data = {
			          value : []
			      };
			      if (json.length == 0) {
			          return false
			      } 
			      len = json.length;
			      for (i = 0; i < len; i++) {
			          data.value.push({
			        	  "id\" style=\"display:none\"":json[i].id,
			              "word":json[i].user.name
			              })
			          }
			          return data
			  }
	     });
	 }
	//发送ajax请求传回等级字典值集合
	function checkCertificateName(url,jsonData){
		$.ajax({
			url:url,
//			async:false,//异步
			data:jsonData,
			type:"post",
			contentType:"application/json;charset=utf-8",
			dataType:"json",
			success:function(data){
				console.log("length:"+data.length);
				$("#certificateClass").empty();  //每次获取成功后清空下拉列表()
				$("#certificateClass").append("<option value=''>--请选择证书级别--</option>");
				if(data!=null && data.length>0){
					$("#certificateClass").removeAttr("disabled");//设置下拉框生效
					for(var i=0;i<data.length;i++){
						var val=data[i].value;
						var label = data[i].label;
						$("#addClass1").html("<font color='red'> *</font>证书级别:");
						$("#certificateClass").addClass("required");
						$("#certificateClass").append("<option value = \"" + val + "\">"+ label +"</option>");  
					}
				}else{  
					$("#addClass1").html("证书级别:");
					$("#certificateClass").removeClass("required");
					$("#certificateClass").attr({disabled: 'true'});//设置下拉框失效
                }
			},
			error:function(){
				console.log("回调失败！")
			}
		});
	}

	function getDepositByProId(url1,jsonData){
		$.ajax({
			url:url1,
			data:jsonData,
			type:"post",
			contentType:"application/json;charset=utf-8",
			dataType:"json",
			success:function(data){
				console.log("保证金为"+data.deposit)
				//设置保证金的值：
				$("#payCount").val(data.deposit);
			},
			error:function(){
				console.log("回调失败！")
			}
		});
	}
	
//	   function suggest(domID,reuqestUrl) {   
//		 $("#" + domID).bsSuggest({   
//			url: reuqestUrl,
//			indexId: 2,  //data.value 的第几个数据，作为input输入框的内容
//		    indexKey: 1, //data.value 的第几个数据，作为input输入框的内容
//			idField : "id\" style=\"display:none\"",
//			keyField : "word",
//			getDataMethod: "url",
//			processData:function(json){
//	         // url 获取数据时，对数据的处理，作为 getData 的回调函数              
//	             var i, len, data = {value: []};            
//	             if(!json || json.length == 0) {
//	                 return false;
//	             }
//	             len = json.length;            
//	             for (i = 0; i < len; i++) {
//	                 data.value.push({
//	                	 "id\" style=\"display:none\"":json[i].id,
//	                    "word":json[i].progranName
//	                 });
//	              }            
//	              return data;            
//			}
//		 }).on('onDataRequestSuccess', function(e, result) {   
//			 console.log('从 json参数中获取，不会触发 onDataRequestSuccess 事件', result);   
//		 }).on('onSetSelectValue', function(e, keyword, data) {    
//			 console.log('onSetSelectValue: ', keyword, data);   
//		 }).on('onUnsetSelectValue', function() {    
//			 console.log("onUnsetSelectValue");   
//		 });    
//	 } 	
//	function init(url,dataList) {
//		$.ajax({
//			url : url,
//			type : "post",
//			contentType:"application/json;charset=utf-8",
//			dataType:"json",
//			success : function(data) {
//				console.log(data);
//				for (var i = 0; i < data.length; i++) {
//					var obj = {};
//					obj.id = data[i].id;
//					if(data[i].companyName!=null){
//					obj.word = data[i].companyName;
//					}else if(data[i].progranName!=null){
//						obj.word = data[i].progranName;
//					}
//					dataList.value.push(
////						"id\" style=\"display:none\"": data[i].id;
////						"word":	data[i].progranName;
//						obj
//					);
//				} 	
//				console.log(dataList);
//				suggest("test_data", dataList);
//			},
//			error : function(){
//				console.log("回调失败");
//			}
//		})
//		return dataList;
//	}
//	
//	 function suggest(domID, dataList) {   
//		 $("#" + domID).bsSuggest({    
//			indexId: 2,  //data.value 的第几个数据，作为input输入框的内容
//		    indexKey: 1, //data.value 的第几个数据，作为input输入框的内容
//			idField : "id\" style=\"display:none\"",
//			keyField : "word",
//			data : dataList 
//		 }).on('onDataRequestSuccess', function(e, result) {   
//			 console.log('从 json参数中获取，不会触发 onDataRequestSuccess 事件', result);   
//		 }).on('onSetSelectValue', function(e, keyword, data) {    
//			 console.log('onSetSelectValue: ', keyword, data);   
//		 }).on('onUnsetSelectValue', function() {    
//			 console.log("onUnsetSelectValue");   
//		 });    
//	 } 	
//	 
//	init(url,dataList);
//	suggest("test_data", dataList); 
	
//}());
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>数据统计</title>
	<meta name="decorator" content="ani"/>
 	<script type="text/javascript"> 
 		$().ready(function(){
 			var bidPrice = parseFloat($('#totalBidPrice').val()).toString();
			if(bidPrice != "NaN"){
				$('#totalBidPrice').val(bidPrice);//转换投标价
			}
			var meterialExpense = parseFloat($('#totalBeterialPrice').val()).toString();
			if(meterialExpense != "NaN"){
				$('#totalBeterialPrice').val(meterialExpense);//转换劳务费
			}
			var laborCost = parseFloat($('#totalBidLaborCost').val()).toString();
			if(laborCost != "NaN"){
				$('#totalBidLaborCost').val(laborCost);//转换材料费
			}
			var deposit = parseFloat($('#totalDeposit').val()).toString();
			if(deposit != "NaN"){
				$('#totalDeposit').val(deposit);//转换递交保证金
			}	 			
 		});
	</script>
</head>
<!-- dialog风格 -->
 <body class="bg-white">
	<form:form id="inputForm" modelAttribute="bidStatistics" action="${ctx}/companymanage/bidcompany/save" method="post" class="form-horizontal">
		<sys:message content="${message}"/>
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
			   <tr>
			   		<td  class="width-15 active">	<label class="pull-right">投标价总计（万元）:</label></td>
			        <td  class="width-35" >
			           <form:input path="totalBidPrice" htmlEscape="false"    class="form-control "/>
			        </td>
			   </tr>
			   <tr>
			   		<td  class="width-15 active">	<label class="pull-right">劳务费总计（万元）:</label></td>
			        <td  class="width-35" >
			           <form:input path="totalBidLaborCost" htmlEscape="false"    class="form-control "/>
			        </td>
			   </tr>
			   <tr>
			   		<td  class="width-15 active">	<label class="pull-right">材料费总计（万元）:</label></td>
			        <td  class="width-35" >
			           <form:input path="totalBeterialPrice" htmlEscape="false"    class="form-control "/>
			        </td>
			   </tr>
			   <tr>
			   		<td  class="width-15 active">	<label class="pull-right">保证金总计（万元）:</label></td>
			        <td  class="width-35" >
			           <form:input path="totalDeposit" htmlEscape="false"  class="form-control "/>
			        </td>
			   </tr>
		   </tbody>
		   </table>   
	</form:form>
	<div style="color:red;font-weight:bold;">
		&nbsp;&nbsp;若查询条件为空，默认显示本单位所有数据汇总
	</div>
</body>
</html>
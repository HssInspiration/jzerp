<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<% response.setHeader("refresh","300"); %>
<html>
<head>
	<title>首页</title>
	<meta name="decorator" content="ani"/>
<!-- 	<META HTTP-EQUIV="Refresh" content="10"> -->
	<style>
		#body-container {
			margin-left: 0px !important;
			/**padding: 10px;*/
			margin-top: 0px !important;
			overflow-x: hidden!important;
			transition: all 0.2s ease-in-out !important;
			height: 100% !important;
		}
	</style>
</head>
<body class="">
<div id="body-container" class="wrapper wrapper-content">
		<div class="conter-wrapper home-container">
			<div class="row home-row">
				<div class="col-sm-3" id="forwardTotalBid">
					<a href="#" class="stat hvr-wobble-horizontal">
						<div class=" stat-icon">
							<i class="fa fa-flag fa-4x text-danger "></i>
						</div>
						<div class=" stat-label">
							<h5>投标工程数量</h5>
							<h1 class="no-margins">${totalBid}&nbsp;个</h1>
						</div>
					</a>
				</div>
				<div class="col-sm-3">
					<a href="#" class="stat hvr-wobble-horizontal">
						<div class=" stat-icon">
							<i class="fa fa-diamond fa-4x text-success "></i>
						</div>
						<div class=" stat-label" id="forwardTotalIsBid1">
							<h5>中标工程数量</h5>
							<h1 class="no-margins">${totalIsBid}&nbsp;个</h1>
						</div>
					</a>
				</div>
				<div class="col-sm-3">
					<a href="#" class="stat hvr-wobble-horizontal">
						<div class=" stat-icon">
							<i class="fa fa-cny fa-4x text-warning "></i>
						</div>
						<div class=" stat-label">
							<h5>中标工程投标价</h5>
							<h1 class="no-margins">${totalBidPrice}&nbsp;万元</h1>
						</div>
					</a>
				</div>
				<div class="col-sm-3">
					<a href="#" class="stat hvr-wobble-horizontal">
						<div class=" stat-icon">
							<i class="fa fa-cny fa-4x text-warning "></i>
						</div>
						<div class=" stat-label">
							<h5>中标劳务费</h5>
							<h1 class="no-margins">${totalLaborCost}&nbsp;万元</h1>
						</div>
					</a>
				</div>
				<div class="col-sm-3" id="forwardTotalIsBid2">
					<a href="#" class="stat hvr-wobble-horizontal">
						<div class=" stat-icon">
							<i class="fa fa-line-chart fa-4x text-info "></i>
						</div>
						<div class=" stat-label">
							<h5>中标率</h5>
							<h2>
								${bidRate}&nbsp;%
							</h2>
							<div class="progress-sm progress ng-isolate-scope" value="progressValue" type="info">
								<div class="progress-bar progress-bar-success" role="progressbar"
									aria-valuenow="${bidRate}" aria-valuemin="0" aria-valuemax="100" style="width: ${bidRate}%;">
								</div>
							</div>
						</div>
					</a>
				</div>
				<div class="col-sm-3">
					<a href="#" class="stat hvr-wobble-horizontal">
						<div class=" stat-icon">
							<i class="fa fa-cny fa-4x text-danger "></i>
						</div>
						<div class=" stat-label" id="notReturnDeposit">
							<h5 class="text-danger">未完成催退保证金:共${totalIsNotReturn }笔</h5>
							<h1 class="no-margins">${totalDepositPrice}&nbsp;万元</h1>
						</div>
					</a>
				</div>
				
			</div>
		</div>
		<script>
			$().ready(
					function() {
						//投标页面跳转便于查看所有投标信息
						$("#forwardTotalBid").bind(
								"click",
								function() {
									jp.openTab(
											"${ctx}/bidmanage/bidtable/list",
											"投标管理", false)//新开投标信息管理tab
								});
						//查看所有已中标信息
						$("#forwardTotalIsBid1").bind(
								"click",
								function() {
									var name = "金卓";
									jp.openTab(
											"${ctx}/companymanage/bidcompany/list?company.companyName="
													+ name + "&isBid=1",
											"参投管理", false)//新开投标信息管理tab
								});
						//查看所有已中标信息
						$("#forwardTotalIsBid2").bind(
								"click",
								function() {
									var name = "金卓";
									jp.openTab(
											"${ctx}/companymanage/bidcompany/list?company.companyName="
													+ name + "&isBid=1",
											"参投管理", false)//新开投标信息管理tab
								});
						//查看未处理的催退
						$("#notReturnDeposit").bind(
								"click",
								function() {
									//新开保证金催退管理tab
									jp.openTab("${ctx}/depositReturn/list?isReturn=0","未完成催退管理", false)//新开投标信息管理tab
								});
					});
		</script>
	</div>
<script src="vendor/ckeditor/ckeditor.js" type="text/javascript"></script>
<script src="js/vendor.js"></script>

<script>
	$(function() {
		$('#calendar2').fullCalendar({
			eventClick : function(calEvent, jsEvent, view) {
				alert('Event: ' + calEvent.title);
				alert('Coordinates: ' + jsEvent.pageX + ',' + jsEvent.pageY);
				alert('View: ' + view.name);
			}
		});

		$('#rtlswitch').click(function() {
			console.log('hello');
			$('body').toggleClass('rtl');

			var hasClass = $('body').hasClass('rtl');

			$.get('/api/set-rtl?rtl=' + (hasClass ? 'rtl' : ''));

		});
		$('.theme-picker').click(function() {
			changeTheme($(this).attr('data-theme'));
		});
		$('#showMenu').click(function() {
			$('body').toggleClass('push-right');
		});

	});
	function changeTheme(the) {
		$("#current-theme").remove();
		$('<link>').appendTo('head').attr('id', 'current-theme').attr({
			type : 'text/css',
			rel : 'stylesheet'
		}).attr('href', '/css/app-' + the + '.css');
	}
</script>

<script>
    $(function(){
        setTimeout(function() {
            var chart = c3.generate({
                bindto: '#lineChart',
                data: {
                    columns: [
                        ['搜索引擎', 30, 200, 100, 400, 150, 250],
                        ['自主访问', 50, 120, 210, 140, 115, 425],
                        ['友情链接', 40, 150, 98, 300, 175, 100]
                    ]
                },
                color: {
                    pattern: ['#3CA2E0','#5CB85C','#F1B35B']
                },
                axis: {
                    x: {
                        show: false
                    },
                    y: {
                        show: false
                    },
                }
            });
        }, 275);
        setTimeout(function() {
            $('#world-map').vectorMap({
                backgroundColor: '#FFF',
                regionStyle: {
                    initial: {
                        fill: 'black',
                        "fill-opacity": 1,
                        stroke: 'none',
                        "stroke-width": 0,
                        "stroke-opacity": 1
                    },
                    hover: {
                        "fill-opacity": 0.8,
                        cursor: 'pointer',
                    },
                    selected: {
                        fill: 'red'
                    },
                    selectedHover: {
                    }
                }
            });
        }, 275);
        setTimeout(function() {
            var chart2 = c3.generate({
                bindto: '#cbar',
                data: {
                    columns: [
                        [10,40,20,90,35,70,10,50,20,80,60,10,20,40,70]
                    ],
                    type:'bar'
                },
                bar: {
                    width: {
                        ratio: 0.5 // this makes bar width 50% of length between ticks
                    }
                },
                color: {
                    pattern: ['#DB5B57']
                },
                labels: true,
                legend: {
                    show: 0
                },
                axis: {
                    x: {
                        show: false
                    },
                    y: {
                        show: false
                    },
                }
            });

        }, 275);
        setTimeout(function() {
            var chart = c3.generate({
                bindto: '#pie',
                data: {
                    // iris data from R
                    columns: [
                        ['data1', 11],
                        ['data2', 23],
                        ['data3', 66]
                    ],
                    type : 'pie',
                },
                color: {
                    pattern: ['#5CB85C','#F0AD4E','#3CA2E0']
                },
                legend: {
                    show: 0
                },
            });

        }, 275);
    });
</script>

</body>
</html>
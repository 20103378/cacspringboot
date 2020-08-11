$package('jeecg.connState');
var img_path1 = "";
var nodetype = "";
var _id = "";
var flag=0;
jeecg.connState = function() {
	var _this = {
		setTime : function() {
			var ck_Time = new Date();
			$("#ck_Time").datebox("setValue", myformatter(ck_Time));
			$("#ck_Time_104conn").datebox("setValue", myformatter(ck_Time));
		},
		treeInit : function() {
			$('#ZoneEmuList').tree({
				url : ctxPath + '/connState/getEmuListData.do',
				checkbox : false,
				cascadeCheck : false,
				animate : true,
				lines : true,
				onLoadSuccess : function(node, data) {// 加载成功后调用事件
					var icons;
					var l = data.length;
					var date_time = new Date();
					for ( var n = 0; n < l; n++) {
						var date2 = data[n].systime;
						date2 = date2.replace(/-/g, "/");
						var oDate1 = new Date(date2);
						var dill = date_time - oDate1;
						icons = $("#ZoneEmuList").find("span.tree-icon");
						if (data[n].iedstate != "1" || dill > (5 * 60 * 1000)) {
							$(icons[n]).addClass("icon-yellow");
							flag=1;
						} else {
							$(icons[n]).addClass("icon-green");
						}
					}
					_id = data[0].id;
					_this.ShowLineData();
				}
			});
			$('#ZoneEmuList').click(_this.ShowLineData);
		},
		addSelect : function() {
			var ck_Time_104 = $('#ck_Time_104conn').datebox('getValue');
			var formData = {};
			if ($('#value_select').combobox('getValue') == null) {
				alert("选择一个连接");
			} else {
				formData['id'] = $('#value_select').combobox('getValue');
			}
			formData['time'] = ck_Time_104;
			$.ajax({
						async : false,
						cache : false,
						type : 'POST',
						url : ctxPath + '/connState/get104connHistoryData.do', //刷新显示历史连接信息
						data : formData,
						error : function() {// 请求失败处理函数
						},
						success : function(data) {
							_this.conndatastateInit();
							_this.conniedstateInit();
							for ( var ii = 0; ii < data.length; ii++) {
								var day = data[ii].systime.substring(14, 16) - 0;
								var hour = data[ii].systime.substring(11, 13) - 0;
								$("#cdr" + hour + "cdc" + day).css({
									'background-color' : 'green'
								});

								$("#cir" + hour + "cic" + day).css({
									'background-color' : 'green'
								});
								if(ii!=0){
									$("#cdr" + hour + "cdc" + (day-1)).css({
										'background-color' : 'green'
									});
									$("#cir" + hour + "cic" + (day-1)).css({
										'background-color' : 'green'
									});
								}
							}
							for ( var i = 0; i < data.length; i++) {
								if (data[i].iedstate == "0") {
									var day = data[i].systime.substring(14, 16) - 0;
									var hour = data[i].systime
											.substring(11, 13) - 0;
									var state = data[i].datastate;
									$("#cir" + hour + "cic" + day).css({
										'background-color' : 'red'
									});
									$("#cdr" + hour + "cdc" + day).css({
										'background-color' : 'red'
									});
								} else {
									if (data[i].datastate == "0") {
										var day = data[i].systime.substring(14,
												16) - 0;
										var hour = data[i].systime.substring(
												11, 13) - 0;
										var state = data[i].datastate;
										$("#cdr" + hour + "cdc" + day).css({
											'background-color' : 'red'
										});
									}
								}
							}
						}
					});

		},
		addTable : function() {
			var ck_Time = $('#ck_Time').datebox('getValue');
			var formData = {};
			if ($('#ZoneEmuList').tree('getSelected') == null) {
				formData['id'] = _id;
			} else {
				formData['id'] = $('#ZoneEmuList').tree('getSelected').id;//值为add或者edit，用于区别是新增还是修改
			}
			formData['time'] = ck_Time;
			$.ajax({
						async : false,
						cache : false,
						type : 'POST',
						url : ctxPath + '/connState/getHistoryData.do', //刷新显示历史连接信息
						data : formData,
						error : function() {// 请求失败处理函数
						},
						success : function(data) {
							_this.datastateInit();
							_this.iedstateInit();
							for ( var ii = 0; ii < data.length; ii++) {
								var day = data[ii].systime.substring(14, 16) - 0;
								var hour = data[ii].systime.substring(11, 13) - 0;
								$("#dr" + hour + "dc" + day).css({
									'background-color' : 'green'
								});

								$("#ir" + hour + "ic" + day).css({
									'background-color' : 'green'
								});
								if(ii!=0){
									$("#dr" + hour + "dc" + (day-1)).css({
										'background-color' : 'green'
									});
									$("#ir" + hour + "ic" + (day-1)).css({
										'background-color' : 'green'
									});
								}
							}
							for ( var i = 0; i < data.length; i++) {
								if (data[i].iedstate == "0") {
									var day = data[i].systime.substring(14, 16) - 0;
									var hour = data[i].systime.substring(11, 13) - 0;
									var state = data[i].datastate;
									$("#ir" + hour + "ic" + day).css({
										'background-color' : 'red'
									});
									$("#dr" + hour + "dc" + day).css({
										'background-color' : 'red'
									});
								} else {
									if (data[i].datastate == "0") {
										var day = data[i].systime.substring(14,
												16) - 0;
										var hour = data[i].systime.substring(
												11, 13) - 0;
										var state = data[i].datastate;
										$("#dr" + hour + "dc" + day).css({
											'background-color' : 'red'
										});
									}
								}
							}
						}
					});
		},
		datastateInit : function() {
			var rowCount = 24;
			var cellCount = 60;
			var color = "gray";
			$("#table_datastate").html("");
			for ( var i = 23; i > -1; i--) {
				var tr = $("<tr></tr>");
				tr.appendTo($("#table_datastate"));
				var div = "conn";
				for ( var j = 0; j < cellCount; j++) {
					var td = $("<td id='dr" + i + "dc" + j+"' style='background:" + color + "' onmouseover='timexy("+i+","+j+",1)'></td>");
					td.appendTo(tr);
				}
			}
			TimeAndMin().appendTo($("#table_datastate"));
		},
		AMCstateInit : function() {
			var table_name = "#AMCState";
			_box = null;
			_this.AMCState.dataGrid.url = "getAMCListData.do";
			_box = new YDataGrid(_this.AMCState, table_name, false, false,
					false, false);
			_box.init();
		},
		AMCState : {
			dataGrid : {
				columns : [ [ {
					field : 'name',
					title : '名称',
					align : 'center',
					sortable : true,
					align : 'left',
					width : fixWidth(0.1),
					formatter : function(value, row, index) {
						return row.text;
					}
				}, {
					field : 'state',
					title : '状态',
					align : 'center',
					sortable : true,
					width : fixWidth(0.2),
					formatter : function(value, row, index) {
						return row.iedstate;
					}
				}, {
					field : 'systime',
					title : '启动时间',
					align : 'center',
					sortable : true,
					align : 'left',
					width : fixWidth(0.17),
					formatter : function(value, row, index) {
						return row.systime;

					}
				}, {
					field : 'runtime',
					title : '运行时间',
					align : 'center',
					sortable : true,
					align : 'left',
					width : fixWidth(0.1),
					formatter : function(value, row, index) {
						if (row.updatetime != null) {
							var date_time = new Date();//获取当前时间

							var date2 = row.systime;//获取查询时间
							date2 = date2.replace(/-/g, "/");//日期格式
							var oDate1 = new Date(date2);//使用日期格式
							var dill = date_time - oDate1;//日期对减
							var h = dill / (3600 * 1000);
							if (h >= (3 * 24)) {
								h = parseInt(h / 24) + "(天)";
							} else if(h>1){
								h = parseInt(h) + "(小时)";
							}else{
								h= parseInt(dill / 1000 /60);
								if(h>1){
									h = h +"(分钟)";
								}else{
									h= parseInt(dill / 1000)+"秒";
								}
							}
							return h;
						}
					}
				}, {
					field : 'runnum',
					title : '重启次数',
					align : 'center',
					sortable : true,
					align : 'left',
					width : fixWidth(0.1),
					formatter : function(value, row, index) {
						return row.deviceip;
					}
				} ] ]
			}
		},
		
		iec104conn : function() {
			var table_name = "#AMC104ConnState";
			_box = null;
			_this.iec104connState.dataGrid.url = "get104connData.do";
			_box = new YDataGrid(_this.iec104connState, table_name, false,
					false, false, false);
			_box.init();
			var chart = [];
			$.ajax({
				async : false,
				cache : false,
				type : 'POST',
				url : "get104connSelect.do",
				error : function() {// 请求失败处理函数
				},
				success : function(data) {
		            for(var j=0;j<data.length;j++){
		            	chart.push({"text":data[j],"value":data[j]});//将chart组合为columns
		            }
		         }
		     });
		     $("#value_select").combobox("loadData", chart);
		     if(chart[0]!=null){				//如果refname为空，且chartt不为空（有返回数据）选择下拉框第一条数据
		    	 $("#value_select").combobox('select',chart[0].value);
		     }
		},
		iec104connState : {
			dataGrid : {
				columns : [ [ {
					field : 'name',
					title : '名称',
					align : 'center',
					sortable : true,
					align : 'left',
					width : fixWidth(0.1),
					formatter : function(value, row, index) {
						return row.text;
					}
				}, {
					field : 'ip',
					title : 'IP',
					align : 'center',
					sortable : true,
					width : fixWidth(0.2),
					formatter : function(value, row, index) {
						return row.deviceip;
					}
				}, {
					field : 'client_state',
					title : '状态',
					align : 'center',
					sortable : true,
					align : 'left',
					width : fixWidth(0.17),
					formatter : function(value, row, index) {
						var date_time = new Date();//获取当前时间
						var date2 = row.updatetime;//获取查询时间
						date2 = date2.replace(/-/g, "/");//日期格式
						var oDate1 = new Date(date2);//使用日期格式
						var dill = date_time - oDate1;//日期对减
						if(row.iedstate == 0){
							$("#conn_status_all").empty();
							$("#conn_status_all").append("<p><font style='font-family:Microsoft YaHei;font-size:20;color:red;font-weight:bold'>SCAC设备运行状态：异常</font></p>");
							return "异常";
						}else if (dill>(300*1000)){
							$("#conn_status_all").empty();
							$("#conn_status_all").append("<p><font style='font-family:Microsoft YaHei;font-size:20;color:red;font-weight:bold'>SCAC设备运行状态：异常</font></p>");
							return "异常";
						}else if(flag == 1){
							$("#conn_status_all").empty();
							$("#conn_status_all").append("<p><font style='font-family:Microsoft YaHei;font-size:20;color:red;font-weight:bold'>SCAC设备运行状态：异常</font></p>");
							return "正常";
						}
						return "正常";

					}
				}, {
					field : 'systime',
					title : '连接时间',
					align : 'center',
					sortable : true,
					align : 'left',
					width : fixWidth(0.17),
					formatter : function(value, row, index) {
						return row.systime;

					}
				} ] ]
			}
		},
		conndatastateInit : function() {
			var rowCount = 24;
			var cellCount = 60;
			var color = "gray";
			$("#table_datastate_104conn").html("");
			for ( var i = 23; i > -1; i--) {
				var tr = $("<tr></tr>");
				tr.appendTo($("#table_datastate_104conn"));
				for ( var j = 0; j < cellCount; j++) {
					var td = $("<td onmouseover='timexy("+i+","+j+",1041)' id='cdr" + i + "cdc" + j
							+ "' style='background:" + color + "'></td>");
					td.appendTo(tr);
				}
			}
			TimeAndMin().appendTo($("#table_datastate_104conn"));
		},
		conniedstateInit : function() {
			var rowCount = 24;
			var cellCount = 60;
			var color = "gray";
			$("#table_iedstate_104conn").html("");
			for ( var i = 23; i > -1; i--) {
				var tr = $("<tr></tr>");
				tr.appendTo($("#table_iedstate_104conn"));
				for ( var j = 0; j < cellCount; j++) {
					var td = $("<td onmouseover='timexy("+i+","+j+",1042)' id='cir" + i + "cic" + j
							+ "' style='background:" + color + "'></td>");
					td.appendTo(tr);
				}
			}
			TimeAndMin().appendTo($("#table_iedstate_104conn"));
		},
		iedstateInit : function() {
			var rowCount = 24;
			var cellCount = 60;
			//	   			 var color="#008000";
			var color = "gray";
			$("#table_iedstate").html("");
			for ( var i = 23; i > -1; i--) {
				var tr = $("<tr></tr>");
				tr.appendTo($("#table_iedstate"));
				for ( var j = 0; j < cellCount; j++) {
					var td = $("<td onmouseover='timexy("+i+","+j+",2)' id='ir" + i + "ic" + j
							+ "' style='background:" + color + "'></td>");
					td.appendTo(tr);
				}

			}
			TimeAndMin().appendTo($("#table_iedstate"));
		},
		ShowLineData : function() {
			var node = $('#ZoneEmuList').tree('getSelected');
			var formData = {};
			if (node == null) {
				formData['id'] = _id;
			} else {
				formData['id'] = node.id;
			}
			$.ajax({
						async : false,
						cache : false,
						type : 'POST',
						url : ctxPath + '/connState/getDataByID.do',
						data : formData,
						error : function() {
							jeecg.alert('错误信息', '数据请求失败', 'error');
						},
						success : function(data) {
							var date_time = new Date();
							var date2 = data[0].systime;
							date2 = date2.replace(/-/g, "/");
							var oDate1 = new Date(date2);
							var dill = date_time - oDate1;
							$("#txtHeader").text(
									"通信单元名称:" + data[0].text + " 监测时间:"
											+ data[0].systime);
							$("#IPADD").text(data[0].deviceip);
							if (data[0].updatetime == ''
									|| data[0].updatetime == null) {
								$("#txtDevicpTime").text("无数据");
							} else {
								$("#txtDevicpTime").text(data[0].updatetime);
							}
							if (data[0].iedstate == "1"
									&& dill < (3 * 60 * 1000)) {
								$("#txtIedstate").text("SCAC与IED连接正常");
							} else {
								$("#txtIedstate").text("SCAC与IED连接断开");
							}
							//	                    	$("#divState").css('display','block');
						}
					});
			//	                _this.addTable();
			$("#btnTime").click(function() {
				_this.addTable();
			});
			$("#btnTime_104conn").click(function() {
				_this.addSelect();
			});
		}
	};
	return _this;
}();

$(function() {
	jeecg.connState.setTime();
	jeecg.connState.treeInit();
	jeecg.connState.AMCstateInit();
	jeecg.connState.iec104conn();
	jeecg.connState.datastateInit();
	jeecg.connState.iedstateInit();
	jeecg.connState.conndatastateInit();
	jeecg.connState.conniedstateInit();
	getAllconnStatus();
});
function myformatter(date) {
	var y = date.getFullYear();
	var m = date.getMonth() + 1;
	var d = date.getDate();
	return y + '-' + (m < 10 ? ('0' + m) : m) + '-' + (d < 10 ? ('0' + d) : d);
}
function getAllconnStatus() {
	var n = 0;
	var p ;
	if($("#txtIedstate").val().indexOf("正常")<-1){
		n=1;
	}
	if( n == 0){
		p = "<p><font style='font-family:Microsoft YaHei;font-size:20;font-weight:bold'>CAC设备运行状态：正常</font></p>";

	}else{
		p = "<p><font style='font-family:Microsoft YaHei;font-size:20;color:red;font-weight:bold'>CAC设备运行状态：异常</font></p>";
	}
	$("#conn_status_all").append(p);
}
function myparser(s) {
	if (!s)
		return new Date();
	var ss = (s.split('-'));
	var y = parseInt(ss[0], 10);
	var m = parseInt(ss[1], 10);
	var d = parseInt(ss[2], 10);
	if (!isNaN(y) && !isNaN(m) && !isNaN(d)) {
		return new Date(y, m - 1, d);
	} else {
		return new Date();
	}
}
function timexy(i,j,n){
	$("#timexy_"+n).empty();
	if(i<10){
		i="0"+i;
	}
	if(j<10){
		j="0"+j;
	}
	$("#timexy_"+n).append("时间："+i+":"+j);
}
function TimeAndMin() {
	var cellCount = 60;
	var tr = $("<tr></tr>");
	for ( var j = 1; j < cellCount; j++) {
		if (j % 5 == 0) {
			var td = $("<td>" + j + "</td>");
		} else {
			var td = $("<td></td>");
		}
		td.appendTo(tr);
	}
	return tr;
}

$(function(){
	$('#ProgramState').datagrid({		
		columns:[[       
			{field:'exeName',title:'程序名称',width:300},
			{field:'exeState',title:'程序状态',width:200,formatter:function(value,row,index){
				if(value == 0){
					return "未运行";
				}else if(value == 1){
					now = parseInt(new Date().getTime());   			
					//得到采集时间戳
					date = row.updateStateTimestamp.replace(/-/g,'/'); 
					time = new Date(date).getTime();
					if(now - time < 5*60*1000){
						return "已运行";
					}else{
						return "未运行";
					}	
				}
			}},
			{field:'exeStateTimestamp',title:'程序开始运行时间',width:300},
			{field:'runNum',title:'运行次数',width:100},
			{field:'updateStateTimestamp',title:'更新时间',width:100,hidden:true},
	    ]]
	});
	//cac与各装置通讯日志
	$('#CACState').datagrid({		
		columns:[[       
			{field:'state_time',title:'时间',width:300},
			{field:'ied_name',title:'装置名称',width:200},
			{field:'ied_ip',title:'ip',width:300},
			{field:'conn_state',title:'连接状态',width:100}
	    ]]
	});
	//cac与cag通讯日志
	$('#CAGState').datagrid({		
		columns:[[       
			{field:'state_time',title:'时间',width:300},
			{field:'client_name',title:'装置名称',width:200},
			{field:'client_ip',title:'ip',width:300},
			{field:'conn_state',title:'连接状态',width:100}
	    ]]
	});
	getCACStateListData();
	getCAGStateListData();
	getProgramListData();
	window.setInterval(update, 2*60*1000);
	
});
/*//cac与各装置通讯日志
$(function(){
	
	
});
//cac与cag通讯日志
$(function(){
	
	window.setInterval(getCACStateListData, 2*60*1000);
	window.setInterval(getCAGStateListData, 2*60*1000);
	
});*/
function update(){
	getCACStateListData();
	getCAGStateListData();
	//getProgramListData();
}
function getProgramListData(){
	$.ajax({
		type:'post',
		dataType:'json',
		url:'getProgramListData.do',
		success:function(data){
			$("#ProgramState").datagrid('loadData', { total: data.total,rows:data.rows });
		}
	});
}

function getCACStateListData(){
	$.ajax({
		type:'post',
		dataType:'json',
		url:'getCACStateListData.do',
		success:function(data){
			$("#CACState").datagrid('loadData', { total: data.total,rows:data.rows });
		}
	});
}

function getCAGStateListData(){
	$.ajax({
		type:'post',
		dataType:'json',
		url:'getCAGStateListData.do',
		success:function(data){
			$("#CAGState").datagrid('loadData', { total: data.total,rows:data.rows });
		}
	});
}

$package('jeecg.yxHistory');
$(function () {
	//设置初始时间
	$(".tabs li:eq(2)").click(function(){
		$("#d4").show();
		jeecg.yxHistory.setTime();
		SerachHistory();
		$('#searchYXHistory').click(SerachHistory);
		jeecg.yxHistory.ShowHistoryData();
	});
});
jeecg.yxHistory = function(){
	var _box = null;
	var _this = {
			//初始化时间
			setTime:function(){
				var curr_time = new Date();
				var stat_time=new Date(curr_time.getTime()-1000*60*60*24*7);
				if(_st==null){
					_st=myformatter(stat_time);
				}
				if(_et==null){
					_et=myformatter(curr_time);
				}
				$("#yxStartTime").datebox("setValue",_st);
				$("#yxEndTime").datebox("setValue",_et);
				/*switch($("#txtType").val()){
				case "1":
					var yxName_data,json;
					yxName_data = [];
					yxName_data.push({"text":"全部","value":"0"},{"text":"监测设备运行异常","value":"SupDevRun"});
					yxName_data.push({"text":"载气欠压告警","value":"GasUnPresAlm"},{"text":"载气低压告警","value":"GasLowPresAlm"});
					yxName_data.push({"text":"异常气瓶号","value":"GasBot"},{"text":"实际气瓶供气状态异常","value":"ActCyGasSta"});
					yxName_data.push({"text":"健康状态（故障代码）","value":"Health"},{"text":"IED与监测设备通讯异常","value":"MoDevConf"});
					$("#yxName").combobox("loadData", yxName_data);
					$("#yxName").combobox('select',yxName_data[0].value);
					break;
				case "2":
					var yxName_data,json;
					yxName_data = [];
					yxName_data.push({"text":"全部","value":"0"},{"text":"IED与监测设备通讯异常","value":"MoDevConf"});
					yxName_data.push({"text":"健康状态（故障代码）","value":"Health"},{"text":"绝缘气体密度告警","value":"DenAlm"});
					yxName_data.push({"text":"监测设备运行异常","value":"SupDevRun"});
					$("#yxName").combobox("loadData", yxName_data);
					$("#yxName").combobox('select',yxName_data[0].value);
					break;
				case "3":
					var yxName_data,json;
					yxName_data = [];
					yxName_data.push({"text":"全部","value":"0"},{"text":"IED与监测设备通讯异常","value":"MoDevConf"});
					yxName_data.push({"text":"健康状态（故障代码）","value":"Health"},{"text":"监测设备运行异常","value":"SupDevRun"});
					$("#yxName").combobox("loadData", yxName_data);
					$("#yxName").combobox('select',yxName_data[0].value);
					break;
				case "4":
					var yxName_data,json;
					yxName_data = [];
					yxName_data.push({"text":"全部","value":"0"},{"text":"IED与监测设备通讯异常","value":"MoDevConf"});
					yxName_data.push({"text":"铁心接地告警","value":"CGAlm"},{"text":"监测设备运行异常","value":"SupDevRun"});
					$("#yxName").combobox("loadData", yxName_data);
					$("#yxName").combobox('select',yxName_data[0].value);
					break;
				case "19":
					var yxName_data,json;
					yxName_data = [];
					yxName_data.push({"text":"全部","value":"0"},{"text":"IED与监测设备通讯异常","value":"MoDevConf"});
					yxName_data.push({"text":"健康状态（故障代码）","value":"Health"},{"text":"监测设备运行异常","value":"SupDevRun"});
					yxName_data.push({"text":"局放告警","value":"PaDschAlm"});
					$("#yxName").combobox("loadData", yxName_data);
					$("#yxName").combobox('select',yxName_data[0].value);
					break;
				}*/
			},
			//数据展示
			ShowHistoryData:function(){
				var node = $('#ZoneEmuList').tree('getSelected');
				var table_name="#YXhistory";
				var DeviceType=$("#txtType").val();
				id=$("#txtID").val();
				startTime = $('#yxStartTime').datebox('getValue')+" 00-00-00";
				endTime = $('#yxEndTime').datebox('getValue')+" 24-00-00";
				state = $('#yxName').combobox('getValue');
				
				switch(DeviceType)
				{
				case "1":
					_box = null;
					_this.showYSPData.dataGrid.url="getstomYXHistoryData?id="+id+"&startTime="+startTime+"&endTime="+endTime+"&state="+state;
					_box = new YDataGrid(_this.showYSPData,table_name,false,true,true,true);
					_box.init();
					break;
				case "2":
					_box = null;
					_this.showSF6Data.dataGrid.url="getSf6YXHistoryData?id="+id+"&startTime="+startTime+"&endTime="+endTime+"&state="+state;
					_box = new YDataGrid(_this.showSF6Data,table_name,false,true,true,true);
					_box.init();
					break;
				case "3":
					_box = null;
					_this.showSF6Data.dataGrid.url="getSmoamYXHistory?id="+id+"&startTime="+startTime+"&endTime="+endTime+"&state="+state;
					_box = new YDataGrid(_this.showSF6Data,table_name,false,true,true,true);
					_box.init();
					break;
				case "4":
					_box = null;
					_this.showSF6Data.dataGrid.url="getScomYXHistoryData?id="+id+"&startTime="+startTime+"&endTime="+endTime+"&state="+state;
					_box = new YDataGrid(_this.showSF6Data,table_name,false,true,true,true);
					_box.init();
					break;
				case "19":
					_box = null;
					_this.showSF6Data.dataGrid.url="getSpdmYXHistoryData?id="+id+"&startTime="+startTime+"&endTime="+endTime+"&state="+state;
					_box = new YDataGrid(_this.showSF6Data,table_name,false,true,true,true);
					_box.init();
					break;
				}
				$(".datagrid-toolbar").remove();
			},
			//油色谱配置
			showYSPData:{
				dataGrid:{
					title: $("#txtName").val(),
					//url:'getstomYXHistoryData',
					columns:  [[{
		                field: 'deviceID',
		                title: '设备ID',
		                align: 'center',
		                sortable: true,
		                width: fixWidth(0.05),
		                formatter: function(value, row, index){
		                    return row.deviceID;
		                }
		            }, {
		            	field: 'sampleTime',
		                title: '采集时间',
		                width: fixWidth(0.12),
		                align: 'center',
		                sortable: true,
		                formatter: function(value, row, index){
		                    return row.sampleTime;
		                }
		            }, {
		            	field: 'phyHealth',
		                title: '物理状态',
		                width: fixWidth(0.08),
		                align: 'center',
		                sortable: true,
		                formatter: function(value, row, index){
		                    return row.phyHealth;
		                }
		            }, {
		            	field: 'supDevRun',
		                title: '运行异常告警',
		                width: fixWidth(0.08),
		                align: 'center',
		                sortable: true,
		                formatter: function(value, row, index){
		                    return row.supDevRun;
		                }
		            }, {
		            	field: 'moDevConf',
		                title: '通讯异常告警',
		                width: fixWidth(0.08),
		                align: 'center',
		                sortable: true,
		                formatter: function(value, row, index){
		                    return row.moDevConf;
		                }
		            }, {
		            	field: 'health',
		                title: '设备状态',
		                width: fixWidth(0.05),
		                align: 'center',
		                sortable: true,
		                formatter: function(value, row, index){
		                    return row.health;
		                }
		            }, {
		            	field: 'gasUnPresAlm',
		                title: '载气欠压告警',
		                width: fixWidth(0.08),
		                align: 'center',
		                sortable: true,
		                formatter: function(value, row, index){
		                    return row.gasUnPresAlm;
		                }
		            }, {
		            	field: 'gasLowPresAlm',
		                title: '载气低压告警',
		                width: fixWidth(0.08),
		                align: 'center',
		                sortable: true,
		                formatter: function(value, row, index){
		                    return row.gasLowPresAlm;
		                }
		            }, {
		            	field: 'actCyGasSta',
		                title: '实际气瓶供气状态',
		                width: fixWidth(0.08),
		                align: 'center',
		                sortable: true,
		                formatter: function(value, row, index){
		                    return row.actCyGasSta;
		                }
		            }, {
		            	field: 'gasBot',
		                title: '气瓶状态',
		                width: fixWidth(0.08),
		                align: 'center',
		                sortable: true,
		                formatter: function(value, row, index){
		                    return row.gasBot;
		                }
		            }, {
		            	field: 'gasLowPresAlm2',
		                title: '载气2低压告警',
		                width: fixWidth(0.08),
		                align: 'center',
		                sortable: true,
		                formatter: function(value, row, index){
		                    return row.gasLowPresAlm2;
		                }
		            }, {
		            	field: 'gasUnPresAlm2',
		                title: '载气2欠压告警',
		                width: fixWidth(0.08),
		                align: 'center',
		                sortable: true,
		                formatter: function(value, row, index){
		                    return row.gasUnPresAlm2;
		                }
		            }/*{
		                field: 'reportDate',
		                title: '采集时间',
		                width: fixWidth(0.12),
		                align: 'center',
		                sortable: true,
		                formatter: function(value, row, index){
		                    return row.reportDate;
		                }
		            },
		            {
		                field: 'constMember',
		                title: '参数名',
		                width: fixWidth(0.12),
		                align: 'center',
		                sortable: true,
		                formatter: function(value, row, index){
		                	if(row.constMember=="MoDevConf"){
		                		return "IED与监测设备通讯异常";
		                	}
		                	if(row.constMember=="SupDevRun"){
		                		return "监测设备运行异常";
		                	}
		                	if(row.constMember=="载气欠压告警"){
		                		return "IED与监测设备通讯异常";
		                	}
		                	if(row.constMember=="GasLowPresAlm"){
		                		return "载气低压告警";
		                	}
		                	if(row.constMember=="GasBot"){
		                		return "异常气瓶号";
		                	}
		                	if(row.constMember=="ActCyGasSta"){
		                		return "实际气瓶供气状态异常";
		                	}
		                	if(row.constMember=="Health"){
		                		return "健康状态（故障代码）";
		                	}
		                    return row.constMember;
		                }
		            },{
		                field: 'value',
		                title: '参数值',
		                width: fixWidth(0.12),
		                align: 'center',
		                sortable: true,
		                formatter: function(value, row, index){
		                	if(row.value=="0"){
		                		return "正常";
		                	}else{
		                		return "异常"
		                	}
		                }
		            }*/
		            ]]
				}
		},
		//六氟化硫配置
			showSF6Data:{
				dataGrid:{
					title: $("#txtName").val(),
					columns:  [[{
	                field: 'deviceID',
	                title: '设备ID',
	                align: 'center',
	                sortable: true,
	                width: fixWidth(0.12),
	                formatter: function(value, row, index){
	                    return row.deviceID;
	                }
	            },{
	                field: 'sampleTime',
	                title: '采集时间',
	                width: fixWidth(0.12),
	                align: 'center',
	                sortable: true,
	                formatter: function(value, row, index){
	                    return row.sampleTime;
	                }
	            },{
	                field: 'phyHealth',
	                title: 'phyHealth',
	                width: fixWidth(0.12),
	                align: 'center',
	                sortable: true,
	                formatter: function(value, row, index){
	                    return row.phyHealth;
	                }
	            },{
	                field: 'supDevRun',
	                title: '运行异常告警',
	                width: fixWidth(0.12),
	                align: 'center',
	                sortable: true,
	                formatter: function(value, row, index){
	                    return row.supDevRun;
	                }
	            },{
	                field: 'moDevConf',
	                title: '通讯异常告警',
	                width: fixWidth(0.12),
	                align: 'center',
	                sortable: true,
	                formatter: function(value, row, index){
	                    return row.moDevConf;
	                }
	            },{
	                field: 'health',
	                title: 'health',
	                width: fixWidth(0.12),
	                align: 'center',
	                sortable: true,
	                formatter: function(value, row, index){
	                    return row.health;
	                }
	            } /*{
	                field: 'reportDate',
	                title: '采集时间',
	                width: fixWidth(0.12),
	                align: 'center',
	                sortable: true,
	                formatter: function(value, row, index){
	                    return row.reportDate;
	                }
	            },
	            {
	                field: 'constMember',
	                title: '参数名',
	                width: fixWidth(0.12),
	                align: 'center',
	                sortable: true,
	                formatter: function(value, row, index){
	                	if(row.constMember=="MoDevConf"){
	                		return "IED与监测设备通讯异常";
	                	}
	                	if(row.constMember=="SupDevRun"){
	                		return "监测设备运行异常";
	                	}
	                	if(row.constMember=="Health"){
	                		return "健康状态（故障代码）";
	                	}
	                	if(row.constMember=="DenAlm"){
	                		return "绝缘气体密度告警";
	                	}
	                    return row.constMember;
	                }
	            },{
	                field: 'value',
	                title: '参数值',
	                width: fixWidth(0.12),
	                align: 'center',
	                sortable: true,
	                formatter: function(value, row, index){
	                	if(row.value=="0"){
	                		return "正常";
	                	}else{
	                		return "异常"
	                	}
	                }
	            }*/
	            ]]
				}
		},
		//避雷器配置
			showSMOAMData:{
				dataGrid:{
					title: $("#txtName").val(),
					columns:  [[{
		                field: 'deviceID',
		                title: '设备ID',
		                align: 'center',
		                sortable: true,
		                width: fixWidth(0.12),
		                formatter: function(value, row, index){
		                    return row.deviceID;
		                }
		            }, {
		                field: 'reportDate',
		                title: '采集时间',
		                width: fixWidth(0.12),
		                align: 'center',
		                sortable: true,
		                formatter: function(value, row, index){
		                    return row.reportDate;
		                }
		            },
		            {
		                field: 'constMember',
		                title: '参数名',
		                width: fixWidth(0.12),
		                align: 'center',
		                sortable: true,
		                formatter: function(value, row, index){
		                	if(row.constMember=="MoDevConf"){
		                		return "IED与监测设备通讯异常";
		                	}
		                	if(row.constMember=="SupDevRun"){
		                		return "监测设备运行异常";
		                	}
		                	if(row.constMember=="Health"){
		                		return "健康状态（故障代码）";
		                	}
		                    return row.constMember;
		                }
		            },{
		                field: 'value',
		                title: '参数值',
		                width: fixWidth(0.12),
		                align: 'center',
		                sortable: true,
		                formatter: function(value, row, index){
		                	if(row.value=="0"){
		                		return "正常";
		                	}else{
		                		return "异常"
		                	}
		                }
		            }
		            ]]
				}
	},
	//铁芯配置
			showSCOMData:{
				dataGrid:{
					title: $("#txtName").val(),
					columns:  [[{
		                field: 'deviceID',
		                title: '设备ID',
		                align: 'center',
		                sortable: true,
		                width: fixWidth(0.12),
		                formatter: function(value, row, index){
		                    return row.deviceID;
		                }
		            }, {
		                field: 'reportDate',
		                title: '采集时间',
		                width: fixWidth(0.12),
		                align: 'center',
		                sortable: true,
		                formatter: function(value, row, index){
		                    return row.reportDate;
		                }
		            },
		            {
		                field: 'constMember',
		                title: '参数名',
		                width: fixWidth(0.12),
		                align: 'center',
		                sortable: true,
		                formatter: function(value, row, index){
		                	if(row.constMember=="MoDevConf"){
		                		return "IED与监测设备通讯异常";
		                	}
		                	if(row.constMember=="SupDevRun"){
		                		return "监测设备运行异常";
		                	}
		                	if(row.constMember=="CGAlm"){
		                		return "铁心接地告警";
		                	}
		                    return row.constMember;
		                }
		            },{
		                field: 'value',
		                title: '参数值',
		                width: fixWidth(0.12),
		                align: 'center',
		                sortable: true,
		                formatter: function(value, row, index){
		                	if(row.value=="0"){
		                		return "正常";
		                	}else{
		                		return "异常"
		                	}
		                }
		            }
		            ]]
				}
		},showSPDMData:{
			dataGrid:{
				title: $("#txtName").val(),
				columns:  [[{
	                field: 'deviceID',
	                title: '设备ID',
	                align: 'center',
	                sortable: true,
	                width: fixWidth(0.12),
	                formatter: function(value, row, index){
	                    return row.deviceID;
	                }
	            }, {
	                field: 'reportDate',
	                title: '采集时间',
	                width: fixWidth(0.12),
	                align: 'center',
	                sortable: true,
	                formatter: function(value, row, index){
	                    return row.reportDate;
	                }
	            },
	            {
	                field: 'constMember',
	                title: '参数名',
	                width: fixWidth(0.12),
	                align: 'center',
	                sortable: true,
	                formatter: function(value, row, index){
	                	if(row.constMember=="MoDevConf"){
	                		return "IED与监测设备通讯异常";
	                	}
	                	if(row.constMember=="SupDevRun"){
	                		return "监测设备运行异常";
	                	}
	                	if(row.constMember=="Health"){
	                		return "健康状态（故障代码）";
	                	}
	                	if(row.constMember=="PaDschAlm"){
	                		return "局放告警";
	                	}
	                    return row.constMember;
	                }
	            },{
	                field: 'value',
	                title: '参数值',
	                width: fixWidth(0.12),
	                align: 'center',
	                sortable: true,
	                formatter: function(value, row, index){
	                	if(row.value=="0"){
	                		return "正常";
	                	}else{
	                		return "异常"
	                	}
	                }
	            }
	            ]]
			}
	}
	}
	return _this;
}();
function SerachHistory() {
	_st = $('#yxStartTime').datebox('getValue');
	_et = $('#yxEndTime').datebox('getValue');
	jeecg.yxHistory.ShowHistoryData();
}

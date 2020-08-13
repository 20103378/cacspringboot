$package('jeecg.systemConfiguration');
var addDataWin = null;
$(function() {
	// 初始化站点信息
	jeecg.systemConfiguration.initStation();
	jeecg.systemConfiguration.upload();
	// $("#serverRestart").unbind('click').click(function() {
	// 	if (window.confirm('确定重启服务器？')) {
	// 		jeecg.systemConfiguration.restart();
	// 	}
	// });
	// $('#btn_Station').click(updateStation);
	$("#btn_Station").unbind('click').click(function() {
		updateStation();
	});
	$("#DownLoadXls").unbind('click').click(function() {
		var xlsUrl = "..\\view\\com.scott\\Infrared.xls";
		window.open(xlsUrl);
	});
	// 下载cac台账表
	$("#bt_download_cacparametertable")
			.click(
					function() {
						$("#download_form")
								.attr(
										"action",
										ctxPath
												+ '/systemConfiguration/exportCacParameterTable.do');
						$("#download_form").submit();
					});
	$("#ddlDeviceType").combobox({
		onSelect : function() {
			jeecg.systemConfiguration.selectdevType();
		},
	});
	// 隐藏测量映射匹配值
	$(".tabs li:eq(6)").hide();
	$(".tabs li:eq(7)").hide();
	$(".tabs li:eq(3)").click(function() {
		// 加载主设备添加删除窗口
		addDataWin = $('#addData-window').window({
			href : '',
			title : '添加数据',
			left : '100px',
			top : '70px',
			closed : true,
			modal : false,
			cache : false,
			minimizable : false,
			maximizable : false,
			collapsible : false,
			shadow : false
		});
		// 加载设备添加删除窗口
		deviceWin = $('#device-window').window({
			href : '',
			title : '添加数据',
			left : '100px',
			top : '70px',
			closed : true,
			modal : false,
			cache : false,
			minimizable : false,
			maximizable : false,
			collapsible : false,
			shadow : false
		});
		deviceMonitorWin = $('#deviceMonitor-window').window({
			href : '',
			left : '100px',
			top : '70px',
			closed : true,
			modal : false,
			cache : false,
			minimizable : false,
			maximizable : true,
			collapsible : false,
			shadow : false
		});
		// 设备提交
		uploadWin = $('#logicalDevice-window').window({
			href : '',
			title : '设备Excel文件上传',
			left : '200px',
			top : '150px',
			closed : true,
			modal : false,
			cache : false,
			minimizable : false,
			maximizable : false,
			collapsible : false,
			shadow : false
		});
		// 加载主设备列表
		jeecg.systemConfiguration.initEquipmentList();
	});
});
jeecg.systemConfiguration = function() {
	var DevTypeDesc = [ "主变", "油色谱及微水", "SF6气体压力", "避雷器及动作次数", "铁芯泄漏电流",
			"换流变运行工况", "SF6气体泄漏", "套管", "断路器", "分压器", "套管局放", "GIS", "开关柜",
			"CT", "环境", "CVT", "电抗器", "PT", "微水", "局放", "主变局放", "GIS局放",
			"耦合电容器", "油温", "母线", "母线CVT", "绝缘子", "线路CVT/OY", "发电机", "线路", "刀闸",
			"油位" ];
	var equipment_select = "";
	var Ld_Ln = [];
	var Ld_Ln_dev = [];
	var _this = {
		upload : function() {
			$("#LD_uploadFileBtn").click(
					function() {
						var fileUrl = $("#LD_fileUrl").val();
						if (fileUrl) {
							if (fileUrl.indexOf("/") == -1
									|| fileUrl.indexOf("\\") == -1) {
								fileUrl = fileUrl.substring(fileUrl
										.lastIndexOf("\\") + 1);
							}
							jeecg.progress('正在导入', '请稍后...');
							$('#LD_ExcelForm').form({
								async : false,
								cache : false,
								onSubmit : function() {
								},
								success : function(data) {
									jeecg.closeProgress();
									if (data.indexOf(1) < 0) {
										alert("导入文件失败！");
									} else if (data.indexOf(2) < 0) {
										alert("导入文件成功，导入数据库失败");
									} else {
										alert("导入成功!");
									}
									;
									uploadWin.window("close");
									$('#device_data_list').datagrid('reload');
								}
							});
							var pubdevice_type = $("#pubdevice_type").combobox(
									'getValue');
							if (pubdevice_type == 0) {
								// $('#LD_ExcelForm').attr("action",
								// "upload_LD_Excel.do").submit();
								alert("普通设备暂时无法导入");
							} else if (pubdevice_type == 1) {
								$('#LD_ExcelForm').attr("action",
										"upload_Infrared_Excel.do").submit();
							} else {
								alert("导入失败");
							}
							// 提交 form

						} else {
							alert("上传的文件不可为空");
						}
					});
		},
		initStation : function() {
			$.ajax({
				async : false,
				cache : false,
				type : 'POST',
				url : ctxPath + "/systemConfiguration/getStation",
				error : function() {
				},
				success : function(data) {
					if (data != '') {
						$("#txtStation").val(data[0].station);
						$("#txtUnitName").val(data[0].name);
						$("#txtUnitAddress").val(data[0].address);
					}
				}
			});

		},
		// 加载主设备列表
		initEquipmentList : function() {
			var _box = null;
			var tablename = "#equipmentList";
			_box = new YDataGrid(_this.equipment_config, tablename, true,
					false, true, true);
			// alert("1");
			_box.grid.datagrid.defaults.onDblClickRow = _this.update;
			_box.init();
			// alert("2");
		},
		update : function() {
			_this.edit_equipment();
		},
		update2 : function() {
			_this.edit_device();
		},
		equipment_config : {
			dataGrid : {
				pageSize : 10,
				title : '',
				url : ctxPath + "/systemConfiguration/getEquipmentList.do",
				singleSelect : true,
				columns : [ [ /*
								 * { field : 'id', checkbox : true },
								 */
						{
							field : 'equipmentID',
							title : '设备编码',
							align : 'center',
							sortable : true,
							width : 150,
							formatter : function(value, row, index) {
								return row.equipmentID;
							}
						},
						{
							field : 'equipmentName',
							title : '设备名称',
							align : 'center',
							sortable : true,
							width : 240,
							formatter : function(value, row, index) {
								return row.equipmentName;
							}
						},
						{
							field : 'phase',
							title : '设备相别',
							align : 'center',
							sortable : true,
							width : 100,
							formatter : function(value, row, index) {
								return row.phase;
							}
						},
						{
							field : 'deviceType',
							title : '设备类型',
							align : 'center',
							sortable : true,
							width : 180,
							formatter : function(value, row, index) {
								return DevTypeDesc[row.deviceType];
							}
						},
						{
							field : 'manufactoryName',
							title : '制造厂家',
							align : 'center',
							sortable : true,
							width : 180,
							formatter : function(value, row, index) {
								return row.manufactoryName;
							}
						},

						{
							field : 'edit',
							title : '修改',
							align : 'center',
							sortable : true,
							width : 180,
							formatter : function(value, row, index) {
								return "<center><div class='dit' style='width: 12px; height: 12px;' ><a href='javascript:void(0);' onclick='jeecg.systemConfiguration.edit_equipment("
										+ index + ")'>修改</a></div></center>";

							}
						},
						{
							field : 'del',
							title : '删除',
							align : 'center',
							sortable : true,
							width : 180,
							formatter : function(value, row, index) {
								return "<center><div class='dit' style='width: 12px; height: 12px;' ><a href='javascript:void(0);'  onclick='jeecg.systemConfiguration.delete_equipment("
										+ index + ")'>删除</a></div></center>";

							}
						}, ] ],
				toolbar : [
						"-",
						{
							id : '',
							text : '添加',
							iconCls : '',
							handler : function() {
								// alert("添加");
								_this.equipment_add();
							}
						},
						"-",
						{
							id : '',
							text : '导出台账',
							iconCls : '',
							handler : function() {
								var _url = ctxPath
										+ "/systemConfiguration/getExportList.do";
								$
										.ajax({
											async : false,
											cache : false,
											type : 'POST',
											url : _url,
											error : function() {// 请求失败处理函数
												alert("连接数据库失败");
											},
											success : function(data) {
												for ( var i = 0; i < data.dataList.length; i++) {
													data.dataList[i].deviceType = DevTypeDesc[data.dataList[i].deviceType];
												}
												var head_data, json;
												head_data = [];
												head_data.push({
													"title" : "设备ID",
													"value" : "deviceID"
												}, {
													"title" : "设备名称",
													"value" : "deviceName"
												}, {
													"title" : "设备类型",
													"value" : "deviceType"
												}, {
													"title" : "IEC61850LD_LN",
													"value" : "IEC61850LD_LN"
												}, {
													"title" : "设备区域",
													"value" : "space"
												}, {
													"title" : "IP地址",
													"value" : "IED_IP"
												}, {
													"title" : "生产厂家",
													"value" : "manufactoryName"
												}, {
													"title" : "采样周期",
													"value" : "smperiod"
												});
												tableExport(data, head_data,
														"excel", "台账表");
											}
										});
							}
						}, "-"/*
								 * , { id: '', text: '重启', iconCls: '', handler:
								 * function () { // alert("添加");
								 * if(window.confirm('确定重启服务器？')){
								 * _this.restart(); } } }, "-"
								 */
				],

				onSelect : function(rowIndex, rowData) {
					var _box2 = null;
					equipment_select = "";
					equipment_manufactoryName = "";

					var tablename = "#device_data_list";
					equipment_select = rowData.equipmentName;
					equipment_manufactoryName = rowData.manufactoryName;
					_this.device_config.dataGrid.url = ctxPath
							+ "/systemConfiguration/getDeviceList.do?EquipmentID="
							+ rowData.equipmentID;
					_box2 = new YDataGrid(_this.device_config, tablename, true,
							false, true, true);
					_box2.grid.datagrid.defaults.onDblClickRow = _this.update2;
					$.ajax({
						async : false,
						cache : false,
						type : 'POST',
						url : ctxPath + "/systemConfiguration/getLd_Ln.do",
						error : function() {// 请求失败处理函数
							// alert("false");
						},
						success : function(data) {
							for ( var i = 0; i < data.length; i++) {
								Ld_Ln.push({
									"text" : data[i].ld_inst_name.replace(';',
											'/'),
									"value" : i
								});
							}
						}
					});
					_box2.init();
				}
			}
		},

		// restart : function() {
		// 	$.ajax({
		// 		async : false,
		// 		cache : false,
		// 		type : 'POST',
		// 		url : ctxPath + "/systemConfiguration/restart.do",
		// 		error : function() {// 请求失败处理函数
		// 			alert("false");
		// 		},
		// 		success : function(data) {
		// 			alert("命令已下发");
		// 		}
		// 	});
		// },
		// 主设备列表添加按钮事件
		equipment_add : function() {
			var deviceType_data, json;
			deviceType_data = [];
			var phase_data, json;
			phase_data = [];
			var objectVoltage_data, json;
			objectVoltage_data = [];
			// 查出
			var nextID = _this.getNextID();
			$("#equipmentID").val(nextID);
			$("#equipmentID").attr("disabled", true);
			$("#equipmentName").val("");
			$("#manufactoryName").val("");
			$("#Remark").val("");
			for ( var i = 0; i < DevTypeDesc.length; i++) {
				deviceType_data.push({
					"text" : DevTypeDesc[i],
					"value" : i
				});
			}
			phase_data.push({
				"text" : "A",
				"value" : "A"
			}, {
				"text" : "B",
				"value" : "B"
			}, {
				"text" : "C",
				"value" : "C"
			});
			objectVoltage_data.push({
				"text" : "AC-500kV"
			}, {
				"text" : "DC-800kV"
			});
			$("#deviceType").combobox("loadData", deviceType_data);
			$("#deviceType").combobox('select', deviceType_data[0].value);
			$("#phase").combobox("loadData", phase_data);
			$("#phase").combobox('select', phase_data[0].value);
			$("#objectVoltage").combobox("loadData", objectVoltage_data);
			$("#objectVoltage").combobox('select', objectVoltage_data[0].value);
			$('#equipment_submit').click(_this.addEquipment_submit);
			$('#equipment_close').unbind('click').click(function() {
				addDataWin.window('close');
			});
			addDataWin.window('open');

		},
		// 主设备修改添加提交事件
		addEquipment_submit : function() {
			var formData = {};
			var url = ctxPath + "/systemConfiguration/add_equipment.do", EquipmentID, SpaceId, DeviceType, DeviceName, Phase, ManufactoryName, Remark;
			formData['EquipmentID'] = $("#equipmentID").val();
			formData['DeviceType'] = $('#deviceType').combobox('getValue');
			formData['EquipmentName'] = $("#equipmentName").val();
			formData['Phase'] = $('#phase').combobox('getValue');
			formData['ManufactoryName'] = $("#manufactoryName").val();
			formData['Remark'] = $("#Remark").val();
			$.ajax({
				async : false,
				cache : false,
				type : 'POST',
				url : url,
				data : formData,
				success : function(data) {
					addDataWin.window('close');
					$('#equipmentList').datagrid('reload');
				},
				error : function() {// 请求失败处理函数
					// alert("false");
				}
			});
		},
		getNextID : function() {
			var str = "";
			$.ajax({
				async : false,
				cache : false,
				type : 'POST',
				url : ctxPath + "/systemConfiguration/getNextEquipmentID.do",
				error : function() {
					alert("数据库连接异常");
				},
				success : function(data) {
					if (data == "" || data == null) {
						str = "M000";
					} else {
						str = data.toString();
					}

				}
			});
			str = parseInt(str.substr(1)) + 1;
			if (str < 10) {
				str = "M00" + str;
			} else if (str < 100) {
				str = "M0" + str;
			} else {
				str = "M" + str;
			}
			;
			return str;
		},
		// 主设备修改按钮点击事件
		edit_equipment : function(index) {
			var deviceType_data, json;
			var phase_data, json;
			deviceType_data = [];
			phase_data = [];
			var rows = $("#equipmentList").datagrid('getRows');
			var select_data = rows[index];
			$("#equipmentID").val(select_data.equipmentID);
			$("#equipmentName").val(select_data.equipmentName);
			for ( var i = 0; i < DevTypeDesc.length; i++) {
				deviceType_data.push({
					"text" : DevTypeDesc[i],
					"value" : i
				});
			}
			phase_data.push({
				"text" : "A",
				"value" : "A"
			}, {
				"text" : "B",
				"value" : "B"
			}, {
				"text" : "C",
				"value" : "C"
			});
			// deviceType_data.push({ "text":
			// DevTypeDesc[select_data.deviceType], "value":
			// select_data.deviceType});
			// phase_data.push({ "text": select_data.phase, "value":
			// select_data.phase});
			$("#deviceType").combobox("loadData", deviceType_data);
			$("#deviceType").combobox('select', select_data.deviceType);
			$("#phase").combobox("loadData", phase_data);
			$("#phase").combobox('select', select_data.phase);
			$("#manufactoryName").val(select_data.manufactoryName);
			$("#Remark").val(select_data.remark);
			$('#equipment_submit').click(_this.equipment_submit);
			$('#equipment_close').unbind('click').click(function() {
				addDataWin.window('close');
			});
			addDataWin.window('open');
		}

		,
		// 修改界面提交事件
		equipment_submit : function() {
			var formData = {};
			var url = ctxPath + "/systemConfiguration/update_equipment.do", EquipmentID, SpaceId, DeviceType, DeviceName, Phase, ManufactoryName, Remark
			formData['EquipmentID'] = $("#equipmentID").val();
			formData['DeviceType'] = $('#deviceType').combobox('getValue');
			formData['EquipmentName'] = $("#equipmentName").val();
			formData['Phase'] = $('#phase').combobox('getValue');
			formData['ManufactoryName'] = $("#manufactoryName").val();
			formData['Remark'] = $("#Remark").val();
			$.ajax({
				async : false,
				cache : false,
				type : 'POST',
				url : url,
				data : formData,
				error : function() {// 请求失败处理函数
					alert("false");
				},
				success : function(data) {
					addDataWin.window('close');
					$('#equipmentList').datagrid('reload');
				}
			});

		},
		// 设备添加修改界面
		device_config : {
			dataGrid : {
				title : '',
				url : ctxPath + "/systemConfiguration/getDeviceList.do",
				singleSelect : true,
				columns : [ [ /*
								 * { field : 'id', checkbox : true },
								 */
						{
							field : 'deviceID',
							title : '设备编码',
							align : 'center',
							sortable : true,
							width : 150,
							formatter : function(value, row, index) {
								return row.deviceID;
							}
						},
						{
							field : 'deviceName',
							title : '设备名称',
							align : 'center',
							sortable : true,
							width : 240,
							formatter : function(value, row, index) {
								return row.deviceName;
							}
						},
						{
							field : 'deviceType',
							title : '设备类型',
							align : 'center',
							sortable : true,
							width : 220,
							formatter : function(value, row, index) {
								return DevTypeDesc[row.deviceType];
							}
						},
						{
							field : 'equipmentID',
							title : '监测主设备',
							align : 'center',
							sortable : true,
							width : 240,
							formatter : function() {
								return equipment_select;
							}
						},
						{
							field : 'manufactoryName',
							title : '制造厂家',
							align : 'center',
							sortable : true,
							width : 140,
							formatter : function() {
								return equipment_manufactoryName;
							}
						},
						{
							field : 'deviceProductID',
							title : '出厂序号',
							align : 'center',
							sortable : true,
							width : 140,
							formatter : function(value, row, index) {
								return row.deviceProductID;
							}
						},
						{
							field : 'startOperateTime',
							title : '投运时间',
							align : 'center',
							sortable : true,
							width : 140,
							formatter : function(value, row, index) {
								return row.startOperateTime;
							}
						},

						{
							field : 'detail',
							title : '详细',
							align : 'center',
							sortable : true,
							width : 120,
							formatter : function(value, row, index) {
								return "<center><div class='dit' style='width: 12px; height: 12px;' ><a	href='javascript:void(0);'  onclick='jeecg.systemConfiguration.edit_device("
										+ index + ")'>详细</a></div></center>";
							}
						},
						{
							field : 'edit',
							title : '修改',
							align : 'center',
							sortable : true,
							width : 120,
							formatter : function(value, row, index) {
								return "<center><div  class='dit' style='width: 12px; height: 12px;' ><a  href='javascript:void(0);'  onclick='jeecg.systemConfiguration.edit_device("
										+ index + ")'>修改</a></div></center>";
							}
						},
						{
							field : 'del',
							title : '删除',
							align : 'center',
							sortable : true,
							width : 120,
							formatter : function(value, row, index) {
								return "<center><div class='dit' style='width: 12px; height: 12px;' ><a  href='javascript:void(0);' onclick='jeecg.systemConfiguration.delete_device("
										+ index + ")'>删除</a></div></center>";
							}
						}, ] ],
				toolbar : [ "-", {
					id : '',
					text : '添加',
					iconCls : '',
					handler : function() {
						_this.device_add();
					}
				}, "-", {
					id : '',
					text : '导入台账',
					iconCls : '',
					handler : function() {
						uploadWin.window('open');
					}
				}, "-", {
					id : '',
					text : '告警管理',
					iconCls : '',
					handler : function() {
						_this.DeviceMonitorPara();
					}
				}, "-" ],

				onSelect : function(rowIndex, rowData) {

				}

			}
		},
		// 告警界面
		monitor_config : {
			dataGrid : {
				title : '',
				columns : [ [
						{
							field : 'strName',
							title : '告警量名称',
							align : 'center',
							sortable : true,
							width : 100,
							formatter : function(value, row, index) {
								return row.strName;
							}
						},
						{
							field : 'strValue',
							title : '告警值',
							align : 'center',
							sortable : true,
							width : 100,
							formatter : function(value, row, index) {
								return "<div class='dit' style='width: 12px; height: 12px;' ><input disabled id='gj_txt"
										+ index
										+ "' value='"
										+ row.strValue
										+ "'  /></div>";
							}
						},
						{
							field : 'edit',
							title : '编辑',
							align : 'center',
							sortable : true,
							width : 100,
							formatter : function(value, row, index) {
								return "<div class='dit' style='width: 12px; height: 12px;' ><a href='javascript:void(0);' onclick='jeecg.systemConfiguration.monitorUpdate("
										+ index + ")'>编辑</a></div>";
							}
						},
						{
							field : 'del',
							title : '更新',
							align : 'center',
							sortable : true,
							width : 100,
							formatter : function(value, row, index) {
								return "<div class='dit' style='width: 12px; height: 12px;' ><a href='javascript:void(0);' onclick='jeecg.systemConfiguration.apply()'>更新</a></div>";
							}
						}, ] ],
				toolbar : [ "-", {
					id : '',
					text : '应用至同类设备',
					iconCls : '',
					handler : function() {
						_this.apply();
					}
				}, "-", {
					id : '',
					text : '返回',
					iconCls : '',
					handler : function() {
						deviceMonitorWin.window('close');
					}
				}, "-" ],

				onSelect : function(rowIndex, rowData) {
				}

			}
		},
		monitorUpdate : function(index) {
			$("#gj_txt" + index).attr("disabled", false);

		},
		// 应用至同类设备按钮和更新按钮的点击事件
		apply : function() {
			var arr = [];
			var deviceType = $("#device_data_list").datagrid("getSelected").deviceType;
			var url_arr = new Array("", "getStomMonitorID", "getSf6MonitorID",
					"getSmoamMonitorID", "getScomMonitorID",
					"getSconditionMonitorID", "getSF6concentrationMonitorID");
			url = ctxPath + "/systemConfiguration/" + url_arr[deviceType]
					+ ".do";
			// 将告警表中的设备ID存储到数组中
			$.ajax({
				async : false,
				cache : false,
				type : 'POST',
				url : url,
				error : function() {// 请求失败处理函数
					alert("数据获取失败");
				},
				success : function(data) {
					for ( var i = 0; i < data.length; i++) {
						arr.push(data[i].deviceID);
					}
				}
			});
			var updateData = []; // 需要修改的告警数组
			var insertData = []; // 需要插入的告警数组
			// 修改和插入url的配置
			var url_insert = new Array("", "insertStomMonitor",
					"insertSf6Monitor", "insertSmoamMonitor",
					"insertScomMonitor", "insertSconditionMonitor",
					"insertSF6concentrationMonitor");
			var url_update = new Array("", "updateStomMonitor",
					"updateSf6Monitor", "updateSmoamMonitor",
					"updateScomMonitor", "updateSconditionMonitor",
					"updateSF6concentrationMonitor");
			// 遍历选中复选框,将值根据是否在告警表中,加入插入或修改数组
			var successFlag = true;// 判断是否应用成功
			$("input[id='checkbox']")
					.each(
							function() {
								var FormData = {};
								if (this.checked == true) {
									var id = $(this).val();
									var datagrid = $("#monitor_data_list");
									var flag = arr.indexOf(id) > -1;
									var _url = "";
									switch (deviceType) {
									case ("1"):
										FormData["DeviceID"] = id;
										FormData["H2ThresHold"] = $("#gj_txt0")
												.val();
										FormData["C2H2ThresHold"] = $(
												"#gj_txt1").val();
										FormData["THThresHold"] = $("#gj_txt2")
												.val();
										FormData["H2Change"] = $("#gj_txt3")
												.val();
										FormData["C2H2Change"] = $("#gj_txt4")
												.val();
										FormData["THChange"] = $("#gj_txt5")
												.val();
										FormData["MstThresHold"] = $("#gj_txt6")
												.val();
										break;
									case ("2"):
										FormData["DeviceID"] = id;
										FormData["PressureThreshold"] = $(
												"#gj_txt0").val();
										break;
									case ("3"):
										FormData["DeviceID"] = id;
										FormData["TotalCurrentThresHold"] = $(
												"#gj_txt0").val();
										break;
									case ("4"):
										FormData["DeviceID"] = id;
										FormData["TotalCurrentThresHold"] = $(
												"#gj_txt0").val();
										break;
									case ("5"):
										FormData["DeviceID"] = id;
										FormData["OilTempThresHold"] = $(
												"#gj_txt0").val();
										break;
									}
									if (flag) {
										_url = ctxPath
												+ "/systemConfiguration/"
												+ url_update[deviceType]
												+ ".do";
									} else {
										_url = ctxPath
												+ "/systemConfiguration/"
												+ url_insert[deviceType]
												+ ".do";
									}
									$.ajax({
										async : false,
										cache : false,
										type : 'POST',
										data : FormData,
										url : _url,
										error : function() {// 请求失败处理函数
											successFlag = false;
										},
										success : function(data) {
										}
									});
								}
							});
			if (successFlag) {
				alert("告警应用成功");
			} else {
				alert("告警应用失败");
			}
		},
		// 告警管理按钮点击事件
		DeviceMonitorPara : function() {
			deviceMonitorWin = $('#deviceMonitor-window').window({
				href : '',
				left : '100px',
				top : '70px',
				closed : false,
				modal : false,
				cache : false,
				minimizable : false,
				maximizable : true,
				collapsible : false,
				shadow : false
			});
			// 加载告警表
			var _box3 = null;
			var tablename = "#monitor_data_list";
			var select_data = $("#device_data_list").datagrid("getSelected");
			var url_arr = new Array("", "getStomMonitor", "getSf6Monitor",
					"getSmoamMonitor", "getScomMonitor",
					"getSconditionMonitor", "getSF6concentrationMonitor");
			if (select_data != null) {
				var deviceType = select_data.deviceType;
				_this.monitor_config.dataGrid.url = ctxPath
						+ "/systemConfiguration/" + url_arr[deviceType]
						+ ".do?DeviceID=" + select_data.deviceID;
				_box3 = new YDataGrid(_this.monitor_config, tablename, true,
						false, true, false);
				_box3.init();

				// 显示窗口
				deviceMonitorWin.window('open');
			} else {
				window.alert("请选择设备");
			}
			// 加载复选框
			var formData = {};
			var url = ctxPath + "/systemConfiguration/getCheckBox.do";
			formData['DeviceID'] = $("#device_data_list").datagrid(
					"getSelected").deviceID;
			formData['DeviceType'] = $("#device_data_list").datagrid(
					"getSelected").deviceType;
			formData['EquipmentID'] = $("#device_data_list").datagrid(
					"getSelected").equipmentID;
			$
					.ajax({
						async : false,
						cache : false,
						type : 'POST',
						url : url,
						data : formData,
						error : function() {// 请求失败处理函数
							alert("数据获取失败");
						},
						success : function(data) {
							var DeviceID = $("#device_data_list").datagrid(
									"getSelected").deviceID;
							var DeviceName = $("#device_data_list").datagrid(
									"getSelected").deviceName;
							var html = "";
							$("#select").empty();
							$("#select")
									.append(
											"<input id='checkbox' name='checkbox_' checked disabled type='checkbox' value='"
													+ DeviceID
													+ "'>"
													+ DeviceName + "<br/>");
							for ( var i = 0; i < data.length; i++) {
								html = "<input id='checkbox' name='checkbox' type='checkbox'  value='"
										+ data[i].deviceID
										+ "' />"
										+ data[i].deviceName + "</br>";
								$("#select").append(html);
							}
						}
					});
			// 全选框点击事件
			$('#select_all').unbind('click').click(function() {
				$("input [name='checkbox']").each(function() {
					$(this).attr("checked", !this.checked);
				});
			});

		},
		// 删除主设备
		delete_equipment : function(index) {
			if (window.confirm('确定要删除？')) {
				var rows = $("#equipmentList").datagrid('getRows');
				var select_data = rows[index];
				var formData = {};
				var url = ctxPath + "/systemConfiguration/delete_equipment.do", EquipmentID, SpaceId, DeviceType, DeviceName, Phase, ManufactoryName, Remark
				formData['EquipmentID'] = select_data.equipmentID;
				$.ajax({
					async : false,
					cache : false,
					type : 'POST',
					url : url,
					data : formData,
					error : function() {// 请求失败处理函数
						alert("false");
					},
					success : function(data) {
						addDataWin.window('close');
						$('#equipmentList').datagrid('reload');
					}
				});
			}
		},
		// 删除设备
		delete_device : function(index) {
			if (window.confirm('确定要删除？')) {
				var rows = $("#device_data_list").datagrid('getRows');
				var select_data = rows[index];
				var formData = {};
				var url = ctxPath + "/systemConfiguration/delete_device.do";
				// EquipmentID,SpaceId,DeviceType,DeviceName,Phase,ManufactoryName,Remark
				formData['DeviceID'] = select_data.deviceID;
				$.ajax({
					async : false,
					cache : false,
					type : 'POST',
					url : url,
					data : formData,
					error : function() {// 请求失败处理函数
						alert("false");
					},
					success : function(data) {
						addDataWin.window('close');
						$('#device_data_list').datagrid('reload');
					}
				});
			}
		},
		// 设备修改点击事件
		edit_device : function(index) {

			deviceWin.window('open');
			var equipment_data = $("#equipmentList").datagrid("getSelected");
			$("#textMDev").val(equipment_data.equipmentName);
			var tSpace_data, json;
			tSpace_data = [];
			var deviceType_data, json;
			deviceType_data = [];
			var rows = $("#device_data_list").datagrid('getRows');
			var select_data = rows[index];
			$("#IEC61850LD_LN").combobox("loadData", Ld_Ln_dev);
			$("#IEC61850LD_LN").combobox('select', select_data.IEC61850LD_LN);
			$("#txtDeviceID").val(select_data.deviceID);
			$('#txtDeviceID').attr("disabled", true);
			$("#txtDeviceName").val(select_data.deviceName);
			$("#tPhase").val(select_data.phase);
			$("#ddlDeviceType").combobox("loadData", deviceType_data);
			$("#ddlDeviceType").combobox('select', select_data.deviceType);
			$("#tSpace").combobox('select', select_data.space);
			$("#tPhase").val(equipment_data.phase);
			$("#txtcode").val(select_data.deviceProductID);
			$("#txtRunTime").val(select_data.startOperateTime);
			$("#txtFactory").val(equipment_data.manufactoryName);
			$("#txtIPAddress").val(select_data.IED_IP);
			$("#txtInstallAddress").val(select_data.deviceLocation);
			var px = select_data.posX == null ? '' : select_data.posX;
			var py = select_data.posY == null ? '' : select_data.posY;
			$("#tPosition").val(px + "," + py);

			// $("#IEC61850LD_LN").val(select_data.IEC61850LD_LN);
			$("#txtRemark").val(select_data.remark);
			if (select_data.stopSoundAlarm == '1') {
				$('#cbStopSoundAlarm').attr("checked", true);
			}
			if (select_data.stopUse == '1') {
				$('#cbStopUse').attr("checked", true);
			}

			$('#device_close').unbind('click').click(function() {
				deviceWin.window('close');
			});
			$('#device_submit').unbind("click");
			$('#device_submit').bind('click', _this.device_update);
			tSpace_data.push({
				"text" : "低端换流变",
				"value" : "低端换流变"
			}, {
				"text" : "高端换流变",
				"value" : "高端换流变"
			}, {
				"text" : "站用变",
				"value" : "站用变"
			}, {
				"text" : "直流场",
				"value" : "直流场"
			}, {
				"text" : "微气象",
				"value" : "微气象"
			}, {
				"text" : "GIS区域",
				"value" : "GIS区域"
			}, {
				"text" : "交流滤波场",
				"value" : "交流滤波场"
			});
			$("#tSpace").combobox("loadData", tSpace_data);

			for ( var i = 0; i < DevTypeDesc.length; i++) {
				deviceType_data.push({
					"text" : DevTypeDesc[i],
					"value" : i
				});
			}
			// deviceType_data.push({ "text":
			// DevTypeDesc[select_data.deviceType], "value":
			// select_data.deviceType});

		},
		// 设备修改提交事件
		device_update : function() {
			var formData = {};
			var select_data = $("#device_data_list").datagrid("getSelected");
			var equipment_data = $("#equipmentList").datagrid("getSelected");
			var url = ctxPath + "/systemConfiguration/update_device.do";
			formData['DeviceID'] = $("#txtDeviceID").val();
			formData['EquipmentID'] = equipment_data.equipmentID;
			formData['DeviceProductID'] = $('#txtcode').val();
			formData['DeviceType'] = $('#ddlDeviceType').combobox('getValue');
			formData['IED_IP'] = $('#txtIPAddress').val();
			formData['DeviceName'] = $("#txtDeviceName").val();
			formData['DeviceLocation'] = $("#txtInstallAddress").val();
			formData['StartOperateTime'] = $("#txtRunTime").val();
			formData['ManufactoryName'] = $('#txtFactory').val();
			formData['Remark'] = $("#txtRemark").val();
			var a = $('#tPosition').val().split(",");
			formData['PosX'] = a[0];
			formData['PosY'] = a[1];
			formData['IEC61850LD_LN'] = $("#IEC61850LD_LN").combobox('getText');
			formData['Space'] = $("#tSpace").combobox('getValue');
			if ($("#cbStopSoundAlarm").attr("checked")) {
				formData['StopSoundAlarm'] = "1";
			} else {
				formData['StopSoundAlarm'] = "0";
			}
			if ($("#cbStopUse").attr("checked")) {
				formData['StopUse'] = "1";
			} else {
				formData['StopUse'] = "0";
			}
			formData['Phase'] = $("#tPhase").val();
			$.ajax({
				async : false,
				cache : false,
				type : 'POST',
				url : url,
				data : formData,
				error : function() {// 请求失败处理函数
					alert("数据库连接异常");
				},
				success : function(data) {
					alert("修改成功");
					deviceWin.window('close');
					$('#device_data_list').datagrid('reload');
				}
			});
		},
		getNextDeviceID : function() {
			var str = "";
			$.ajax({
				async : false,
				cache : false,
				type : 'POST',
				url : ctxPath + "/systemConfiguration/getNextDeviceID.do",
				error : function() {
					alert("数据库连接异常");
				},
				success : function(data) {
					if (data == "" || data == null) {
						str = "D0000";
					} else {
						str = data.toString();
					}

				}
			});
			str = parseInt(str.substr(1)) + 1;
			if (str < 10) {
				str = "D000" + str;
			} else if (str < 100) {
				str = "D00" + str;
			} else if (str < 1000) {
				str = "D0" + str;
			} else {
				str = "D" + str;
			}
			;
			return str;
		},
		// 设备添加点击事件
		device_add : function() {
			var equipment_data = $("#equipmentList").datagrid("getSelected");
			var deviceType_data, json;
			deviceType_data = [];
			var tSpace_data, json;
			tSpace_data = [];

			deviceWin.window('open');
			$("#textMDev").val(equipment_data.equipmentName);
			$("#tPhase").val(equipment_data.phase);
			tSpace_data.push({
				"text" : "低端换流变",
				"value" : "低端换流变"
			}, {
				"text" : "高端换流变",
				"value" : "高端换流变"
			}, {
				"text" : "站用变",
				"value" : "站用变"
			}, {
				"text" : "直流场",
				"value" : "直流场"
			}, {
				"text" : "微气象",
				"value" : "微气象"
			}, {
				"text" : "GIS区域",
				"value" : "GIS区域"
			}, {
				"text" : "交流滤波场",
				"value" : "交流滤波场"
			});
			$("#tSpace").combobox("loadData", tSpace_data);
			$("#tSpace").combobox('select', tSpace_data[0].value);
			var nextDeviceID = _this.getNextDeviceID();
			$("#txtDeviceID").val(nextDeviceID);
			$('#txtDeviceID').attr("disabled", true);
			for ( var i = 0; i < DevTypeDesc.length; i++) {
				deviceType_data.push({
					"text" : DevTypeDesc[i],
					"value" : i
				});
			}
			$("#ddlDeviceType").combobox("loadData", deviceType_data);
			$("#ddlDeviceType").combobox('select', deviceType_data[0].value);
			$("#txtcode").val("");
			$("#txtRunTime").val("");
			$("#txtFactory").val("");
			$("#txtIPAddress").val("");
			$("#txtInstallAddress").val("");
			$("#tPosition").val("");
			$("#txtDeviceName").val("");
			// $("#IEC61850LD_LN").val("");

			$("#IEC61850LD_LN").combobox("loadData", Ld_Ln_dev);
			$("#IEC61850LD_LN").combobox('select', Ld_Ln_dev[0].value);
			$("#txtRemark").val("");
			$('#cbStopSoundAlarm').attr("checked", false);
			$('#cbStopUse').attr("checked", false);

			$('#device_close').unbind('click').click(function() {
				deviceWin.window('close');
			});
			$('#device_submit').unbind("click");
			$('#device_submit').bind('click', _this.device_insert);
		},
		// 设备添加提交事件
		device_insert : function() {
			var formData = {};
			var select_data = $("#device_data_list").datagrid("getSelected");
			var equipment_data = $("#equipmentList").datagrid("getSelected");
			var url = ctxPath + "/systemConfiguration/insert_device.do";
			// formData['DeviceID'] = $("#txtID").val();
			formData['EquipmentID'] = equipment_data.equipmentID;
			formData['DeviceProductID'] = $('#txtcode').val();
			formData['DeviceType'] = $('#ddlDeviceType').combobox('getValue');
			formData['IED_IP'] = $('#txtIPAddress').val();
			formData['DeviceName'] = $("#txtDeviceName").val();
			formData['DeviceLocation'] = $("#txtInstallAddress").val();
			formData['StartOperateTime'] = $("#txtRunTime").val();
			formData['ManufactoryName'] = $('#txtFactory').val();
			formData['Remark'] = $("#txtRemark").val();
			var a = $('#tPosition').val().split(",");
			formData['PosX'] = a[0];
			formData['PosY'] = a[1];
			formData['IEC61850LD_LN'] = $("#IEC61850LD_LN").combobox('getText');
			formData['Space'] = $('#tSpace').combobox('getValue');
			if ($("#cbStopSoundAlarm").attr("checked")) {
				formData['StopSoundAlarm'] = "1";
			} else {
				formData['StopSoundAlarm'] = "0";
			}
			if ($("#cbStopUse").attr("checked")) {
				formData['StopUse'] = "1";
			} else {
				formData['StopUse'] = "0";
			}
			$.ajax({
				async : false,
				cache : false,
				type : 'POST',
				url : url,
				data : formData,
				error : function() {// 请求失败处理函数
					alert("数据库连接异常");
				},
				success : function(data) {
					alert("添加成功");
					deviceWin.window('close');
					$('#device_data_list').datagrid('reload');
				}
			});
		},
		selectdevType : function() {
			Ld_Ln_dev = [];
			var ddlDeviceType = $("#ddlDeviceType").combobox("getValue");
			// $("#IEC61850LD_LN").combobox("loadData", []);
			switch (ddlDeviceType) {
			case '1':
				for ( var i = 0; i < Ld_Ln.length; i++) {
					var dev = Ld_Ln[i];
					if (dev.text.indexOf("SIML") != -1) {
						Ld_Ln_dev.push({
							"text" : dev.text,
							"value" : dev.value
						});
					}
				}
				break;
			case '2':
				for ( var i = 0; i < Ld_Ln.length; i++) {
					var dev = Ld_Ln[i];
					if (dev.text.indexOf("SIMG") != -1) {
						Ld_Ln_dev.push({
							"text" : dev.text,
							"value" : dev.value
						});
					}
				}
				break;
			case '3':
				for ( var i = 0; i < Ld_Ln.length; i++) {
					var dev = Ld_Ln[i];
					if (dev.text.indexOf("SSAR") != -1
							|| dev.text.indexOf("ZSAR") != -1) {
						Ld_Ln_dev.push({
							"text" : dev.text,
							"value" : dev.value
						});
					}
				}
				break;
			case '4':
				for ( var i = 0; i < Ld_Ln.length; i++) {
					var dev = Ld_Ln[i];
					if (dev.text.indexOf("SPTR") != -1) {
						Ld_Ln_dev.push({
							"text" : dev.text,
							"value" : dev.value
						});
					}
				}
				break;
			case '14':
				for ( var i = 0; i < Ld_Ln.length; i++) {
					var dev = Ld_Ln[i];
					if (dev.text.indexOf("MMET") != -1) {
						Ld_Ln_dev.push({
							"text" : dev.text,
							"value" : dev.value
						});
					}
				}
				break;
			default: {
				Ld_Ln_dev = Ld_Ln;
			}
			}
			$("#IEC61850LD_LN").combobox("loadData", Ld_Ln_dev);
			$("#IEC61850LD_LN").combobox('select', Ld_Ln_dev[0].value);
		}
	};
	return _this;
}();
// 更新站点信息
function updateStation() {
	var formData = {};
	formData['station'] = $("#txtStation").val();
	formData['name'] = $("#txtUnitName").val();
	formData['address'] = $("#txtUnitAddress").val();

	$.ajax({
		async : false,
		cache : false,
		type : 'POST',
		data : formData,
		url : ctxPath + "/systemConfiguration/updateStation",
		// error : function() {
		// 	alert("修改失败")
		// },
		success : function(data) {
			if(data){
				alert("修改成功");
				$(".tabs li:eq(1)").show();
				$(".tabs li:eq(2)").show();
				$(".tabs li:eq(3)").show();
				$(".tabs li:eq(4)").show();
				$(".tabs li:eq(5)").show();
				jeecg.systemConfiguration.initStation();
			}else {
				alert("修改失败")

			}

		}
	});
	// $.ajax({
	// 	async : false,
	// 	cache : false,
	// 	type : 'POST',
	// 	data : formData,
	// 	url : "updateStation",
	// 	success : function(data) {
	// 		alert("修改成功");
	// 		$(".tabs li:eq(1)").show();
	// 		$(".tabs li:eq(2)").show();
	// 		$(".tabs li:eq(3)").show();
	// 		$(".tabs li:eq(4)").show();
	// 		$(".tabs li:eq(5)").show();
	// 		jeecg.systemConfiguration.initStation();
	// 	}
	// });
};

$(function() {
	$('#programVersion').datagrid({
		fit : true,
		columns : [ [ {
			field : 'stTime',
			title : '程序升级时间',
			width : 200
		}, {
			field : 'programName',
			title : '程序名',
			width : 200
		}, {
			field : 'md5Num',
			title : 'MD5码',
			width : 300
		}, {
			field : 'editionNum',
			title : '版本号',
			width : 100
		} ] ]
	});
	/*
	 * programVersionSearch();
	 */
	// 添加版本信息
	$("#confirm_add").click(function() {
		if ($("#addProgramForm").form('validate')) {
			$("#addProgramForm").attr("action", 'addProgram.do');
			$("#addProgramForm").ajaxSubmit(function(message) {
				/*
				 * programVersionSearch();
				 */$("#addProgram_win").window('close');
			});

		}
	});
	// 取消
	$("#cancel_add").click(function() {
		$("#addProgram_win").window('close');
	});

	// 修改版本信息
	$("#confirm_edit").click(function() {
		if ($("#editProgramForm").form('validate')) {
			$("#editProgramForm").attr("action", 'editProgram.do');
			$("#editProgramForm").ajaxSubmit(function(message) {
				/*
				 * programVersionSearch();
				 */$("#editProgram_win").window('close');
			});

		}
	});
	// 取消
	$("#cancel_edit").click(function() {
		$("#editProgram_win").window('close');
	});
});

/*
 * function programVersionSearch() { $.ajax({ type : 'post', dataType : 'json',
 * url : 'ProgramVersion_search.do', success : function(data) {
 * $("#programVersion").datagrid('loadData', { total : data.total, rows :
 * data.rows }); } }); };
 */

function add() {
	$("#addProgram_win").window('open');
}
function edit() {
	var sys = $('#programVersion').datagrid('getSelections');
	if (sys.length == 0) {
		$.messager.alert('警告', '未选中要修改的信息！', 'warning');
	} else if (sys.length > 1) {
		$.messager.alert('警告', '不能同时修改多个信息！', 'warning');
	} else if (sys.length == 1) {
		$('#stTime').val(sys[0].stTime);
		$('#programName').val(sys[0].programName);
		$('#md5Num').val(sys[0].md5Num);
		$('#editionNum').val(sys[0].editionNum);
		$('#editProgram_win').window('open'); // open a window
	}
}

function dele() {
	var sys = $('#programVersion').datagrid('getSelections');
	if (sys.length == 0) {
		$.messager.alert('警告', '未选中要修改的信息！', 'warning');
	} else if (sys.length > 1) {
		$.messager.alert('警告', '不能同时修改多个信息！', 'warning');
	} else if (sys.length == 1) {
		var programName = sys[0].programName;
		$.ajax({
			type : 'post',
			url : 'delProgram.do?programName=' + programName,
			success : function() {
				/*
				 * programVersionSearch();
				 */
				$.messager.alert('提示', '删除成功！', 'info');
			}
		});
	}
}
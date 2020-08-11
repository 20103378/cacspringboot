$package('jeecg.LEDConfiguration');
// 定义窗口
$(function() {
	$(".tabs li:eq(2)").click(function() {
		// 初始化弹框
		jeecg.LEDConfiguration.setEditWin();
		// 加载led设备列表
		jeecg.LEDConfiguration.addTable();
		// 加载数据提交按钮事件
		jeecg.LEDConfiguration.commit();
		// icd文件上传提交事件
		jeecg.LEDConfiguration.upload();
	});
});
jeecg.LEDConfiguration = function() {
	var _this = {
		upload : function() {
			$("#uploadFileBtn").unbind('click').click(function() {
				var fileUrl = $("#fileUrl").val();
				if (fileUrl) {
					$('#pdfForm').form({
						async : false,
						cache : false,
						onSubmit : function() {
						},
						success : function(data) {
							// alert(data);
							alert(data);
							uploadWin.window("close");
							$('#tab_ldein').datagrid('reload');
						}
					});
					// 提交 form
					$('#pdfForm').attr("action", "uploadIcd.do").submit();
				} else {
					alert("上传的文件不可为空");
				}
			});
		},
		addTable : function() {
			var _box = null;
			_box = new YDataGrid(_this.config, "#tab_ldein", true, false, true,
					false);
			_box.grid.datagrid.defaults.onDblClickRow = _this.update;
			_box.init();
		},
		setEditWin : function() {
			ledWin = $('#editLED-window').window({
				href : '',
				title : '添加/修改',
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
			uploadWin = $('#icdUpload-window').window({
				href : '',
				title : 'icd文件上传',
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
		},
		update : function(rowIndex, rowData) {
			$("#txt_AR_Name1").val(rowData.AR_Name);
			$("#txt_AR_Name2").html(rowData.AR_Name);
			$("#txt_netAddr1").val(rowData.netAddr);
			$("#txt_AR_Name2").hide();
			ledWin.window('open');
		},
		commit : function() {
			$("#ied_sub").unbind('click').click(function() {
				var formData = {};
				formData['NetAddr'] = $("#txt_netAddr1").val();
				if (_this.ipvalidate($("#txt_netAddr1").val()) == false) {
					alert("ip地址格式不正确")
					return;
				}
				formData['AR_Name'] = $("#txt_AR_Name1").val();
				if ($("#txt_AR_Name2").html() == "") {
					formData['AR_Name_old'] = $("#txt_AR_Name1").val();
				} else {
					formData['AR_Name_old'] = $("#txt_AR_Name2").html();
				}
				$.ajax({
					async : false,
					cache : false,
					type : 'post',
					data : formData,
					url : ctxPath + "/LEDConfiguration/commitled.do",
					success : function(data) {
						ledWin.window('close');
						$('#tab_ldein').datagrid('reload');
					}
				});
			});
		},
		add : function() {
			$("#txt_AR_Name1").val('');
			$("#txt_AR_Name2").html('');
			$("#txt_netAddr1").val('');
			$("#txt_AR_Name2").hide();
			ledWin.window('open');
		},
		Del : function() {
			var select_data = $("#tab_ldein").datagrid("getSelected");
			if (window.confirm('确定要删除测量量名称'+select_data.AR_Name)) {
				var formData = {};
				var url = ctxPath + "/LEDConfiguration/del.do";
				formData['AR_Name'] = select_data.AR_Name;
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
						$('#tab_ldein').datagrid('reload');
					}
				});
			}
		},
		DelSQl : function() {
			var select_data = $("#tab_ldein").datagrid("getSelected");
			if (window.confirm('确定要删除关于'+select_data.AR_Name+"的所有数据")) {
				var select_data = $("#tab_ldein").datagrid("getSelected");
				var formData = {};
				var url = ctxPath + "/LEDConfiguration/delsql.do";
				formData['AR_Name'] = select_data.AR_Name;
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
						$('#tab_ldein').datagrid('reload');
					}
				});
			}
		},
		showcllysWin : function() {
			if ($("#tab_ldein").datagrid("getSelected") == null) {
				alert("请先选择接入设备!");
				return;
			}
			;
			// 监测文件目录是否存在,不存在则要求上传icd文件
			var iedName = $("#tab_ldein").datagrid("getSelected").AR_Name;
			// 通过ajax请求数据
			var formData = {};
			formData['dIRName'] = iedName;
			$.ajax({
				async : false,
				cache : false,
				type : 'post',
				data : formData,
				url : "getIcdExistFlag.do",
				success : function(data) {
					if (data.message == "false") {
						alert("icd文件!不存在,请先导入");
					} else {
						$(".tabs li:eq(6)").show();
						$(".tabs li:eq(6)").click();
					}
				}
			});
		},
		showJdNameWin : function() {
			if ($("#tab_ldein").datagrid("getSelected") == null) {
				alert("请先选择接入设备!");
				return;
			}
			;
			// 监测文件目录是否存在,不存在则要求上传icd文件
			var iedName = $("#tab_ldein").datagrid("getSelected").AR_Name;
			// 通过ajax请求数据
			var formData = {};
			formData['dIRName'] = iedName;
			$.ajax({
				async : false,
				cache : false,
				type : 'post',
				data : formData,
				url : "getIcdExistFlag.do",
				success : function(data) {
					if (data.message == "false") {
						alert("icd文件!不存在,请先导入");
					} else {
						$(".tabs li:eq(7)").show();
						$(".tabs li:eq(7)").click();
					}
				}
			});
		},
		addAllDevice : function() {
			var ld_ln = _this.getLd_Ln();
			var deviceType;
			for ( var i = 0; i < ld_ln.length; i++) {
				var ld_ln_name = ld_ln[i].ld_inst_name;
				if (ld_ln_name.indexOf("SIML") != -1) {
					deviceType = "1";
				} else if (ld_ln_name.indexOf("SIMG") != -1) {
					deviceType = "2";
				} else if (ld_ln_name.indexOf("SSAR") != -1
						|| ld_ln_name.indexOf("ZSAR") != -1) {
					deviceType = "3";
				} else if (ld_ln_name.indexOf("SPTR") != -1) {
					deviceType = "4";
				} else if (ld_ln_name.indexOf("MMET") != -1) {
					deviceType = "14";
				}
				var formData = {};
				var url = ctxPath + "/systemConfiguration/insert_device.do";
				// formData['DeviceID'] = $("#txtID").val();
				formData['EquipmentID'] = "M001";
				formData['DeviceProductID'] = "";
				formData['DeviceType'] = deviceType;
				formData['IED_IP'] = $('#txtIPAddress').val();
				formData['DeviceName'] = $("#txtDeviceName").val();
				formData['DeviceLocation'] = $("#txtInstallAddress").val();
				formData['StartOperateTime'] = $("#txtRunTime").val();
				// formData['ManufactoryName'] = $('#txtFactory').val();
				// formData['Remark'] = $("#txtRemark").val();
				// var a=$('#tPosition').val().split(",");
				// formData['PosX'] = a[0];
				// formData['PosY'] = a[1];
				formData['IEC61850LD_LN'] = ld_ln_name.replace(';', '/');
				// formData['Space'] = $('#tSpace').combobox('getValue');
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
						// alert("添加成功");
						// deviceWin.window('close');
						// $('#device_data_list').datagrid('reload');
					}
				});
			}
			alert("添加成功");
		},
		getLd_Ln : function() {
			var Ld_Ln_data = "";
			$.ajax({
				async : false,
				cache : false,
				type : 'POST',
				url : ctxPath + "/systemConfiguration/getLd_Ln.do",
				error : function() {// 请求失败处理函数
					// alert("false");
				},
				success : function(data) {
					Ld_Ln_data = data;
				}
			});
			return Ld_Ln_data;
		},
		ipvalidate : function(ip) {
			var val = /([0-9]{1,3}\.{1}){3}[0-9]{1,3}/;
			var vald = val.exec(ip);
			if (vald == null) {
				// alert('注意IP有效性');
				return false;
			}
			if (vald != '') {
				if (vald[0] != ip) {
					// alert('注意IP有效性');
					return false;
				}
			}
			return true;
		},
		config : {
			dataGrid : {
				title : $("#txtName").val(),
				url : ctxPath + '/LEDConfiguration/getOsicfgXml.do',
				columns : [ [ {
					field : 'AR_Name',
					title : 'AR_Name',
					width : fixWidth(0.12),
					align : 'center',
					sortable : true,
					formatter : function(value, row, index) {
						return row.AR_Name;
					}
				}, {
					field : 'netAddr',
					title : 'Ip地址',
					width : fixWidth(0.12),
					align : 'center',
					sortable : true,
					formatter : function(value, row, index) {
						return row.netAddr;
					}
				}, ] ],
				toolbar : [ "-", {
					id : '',
					text : '添加',
					iconCls : '',
					handler : function() {
						uploadWin.window('open');
					}
				}, "-", {
					id : '',
					text : '删除',
					iconCls : '',
					handler : function() {
						_this.Del();
					}
				}, "-", {
					id : '',
					text : '测量量映射配置',
					iconCls : '',
					handler : function() {
						_this.showcllysWin();
					}
				}, "-", {
					id : '',
					text : '节点名称配置',
					iconCls : '',
					handler : function() {
						_this.showJdNameWin();
					}
				}, "-", {
					id : '',
					text : '添加所有逻辑设备',
					iconCls : '',
					handler : function() {
						_this.addAllDevice();
					}
				}, "-", {
					id : '',
					text : '删除测量量数据库',
					iconCls : '',
					handler : function() {
						_this.DelSQl();
					}
				}, ]
			}
		}
	};
	return _this;
}();
function fixWidth(percent) {
	return document.body.clientWidth * percent;
}

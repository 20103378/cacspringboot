$package('jeecg.systemConfiguration');
var addDataWin = null;
$(function () {
    // 初始化站点信息
    jeecg.systemConfiguration.initStation();
    // jeecg.systemConfiguration.upload();
    // $("#serverRestart").unbind('click').click(function() {
    // 	if (window.confirm('确定重启服务器？')) {
    // 		jeecg.systemConfiguration.restart();
    // 	}
    // });
    // $('#btn_Station').click(updateStation);
    $("#btn_Station").unbind('click').click(function () {
        // 更新站点信息
        updateStation();
    });
    $("#DownLoadXls").unbind('click').click(function () {
        var xlsUrl = "..\\jsp\\scott\\Infrared.xls";
        window.open(xlsUrl);
    });
    // 下载cac台账表
    $("#bt_download_cacparametertable")
        .click(
            function () {
                $("#download_form")
                    .attr(
                        "action",
                        ctxPath
                        + '/systemConfiguration/exportCacParameterTable');
                $("#download_form").submit();
            });
    $("#IEC61850LD_LN").combobox({
        onSelect: function () {
            jeecg.systemConfiguration.selectdevType();
            // formData['IEC61850LD_LN']
            // ddlDeviceType
        },
    });
    // 隐藏测量映射匹配值
    $(".tabs li:eq(6)").hide();
    $(".tabs li:eq(7)").hide();
    $(".tabs li:eq(3)").click(function () {
        // 加载主设备添加删除窗口
        addDataWin = $('#addData-window').window({
            href: '',
            title: '添加数据',
            left: '100px',
            top: '70px',
            closed: true,
            modal: false,
            cache: false,
            minimizable: false,
            maximizable: false,
            collapsible: false,
            shadow: false
        });
        // 加载设备添加删除窗口
        deviceWin = $('#device-window').window({
            href: '',
            title: '添加数据',
            left: '100px',
            top: '70px',
            closed: true,
            modal: false,
            cache: false,
            minimizable: false,
            maximizable: false,
            collapsible: false,
            shadow: false
        });
        deviceMonitorWin = $('#deviceMonitor-window').window({
            href: '',
            left: '100px',
            top: '70px',
            closed: true,
            modal: false,
            cache: false,
            minimizable: false,
            maximizable: true,
            collapsible: false,
            shadow: false
        });
        // // 设备提交
        // uploadWin = $('#logicalDevice-window').window({
        //     href: '',
        //     title: '设备Excel文件上传',
        //     left: '200px',
        //     top: '150px',
        //     closed: true,
        //     modal: false,
        //     cache: false,
        //     minimizable: false,
        //     maximizable: false,
        //     collapsible: false,
        //     shadow: false
        // });
        // 加载主设备列表
        jeecg.systemConfiguration.initEquipmentList();
    });
});
jeecg.systemConfiguration = function () {
    var DevTypeDesc = ["主变", "油色谱及微水", "SF6气体压力", "避雷器及动作次数", "铁芯泄漏电流",
        "换流变运行工况", "SF6气体泄漏", "套管", "微气象"];
    var equipment_select = "";
    var iec61850LD_LNs = [];
    var spaceIds = [];//区域位置选择
    var phase_selects = [];//相位选择
    var ldDevice_selects = [];//主设备编码
    var deviceTypes = [];//主设备类型
    var LDDevices = [];
    var lDeviceMap = {};
    var Ld_Ln = [];
    var _this = {
        // upload: function () {
        //     $("#LD_uploadFileBtn").click(
        //         function () {
        //             var fileUrl = $("#LD_fileUrl").val();
        //             if (fileUrl) {
        //                 if (fileUrl.indexOf("/") == -1
        //                     || fileUrl.indexOf("\\") == -1) {
        //                     fileUrl = fileUrl.substring(fileUrl
        //                         .lastIndexOf("\\") + 1);
        //                 }
        //                 jeecg.progress('正在导入', '请稍后...');
        //                 $('#LD_ExcelForm').form({
        //                     async: false,
        //                     cache: false,
        //                     onSubmit: function () {
        //                     },
        //                     success: function (data) {
        //                         jeecg.closeProgress();
        //                         if (data.indexOf(1) < 0) {
        //                             alert("导入文件失败！");
        //                         } else if (data.indexOf(2) < 0) {
        //                             alert("导入文件成功，导入数据库失败");
        //                         } else {
        //                             alert("导入成功!");
        //                         }
        //                         ;
        //                         uploadWin.window("close");
        //                         $('#device_data_list').datagrid('reload');
        //                     }
        //                 });
        //                 var pubdevice_type = $("#pubdevice_type").combobox(
        //                     'getValue');
        //                 if (pubdevice_type == 0) {
        //                     // $('#LD_ExcelForm').attr("action",
        //                     // "upload_LD_Excel").submit();
        //                     alert("普通设备暂时无法导入");
        //                 } else if (pubdevice_type == 1) {
        //                     $('#LD_ExcelForm').attr("action",
        //                         "upload_Infrared_Excel").submit();
        //                 } else {
        //                     alert("导入失败");
        //                 }
        //                 // 提交 form
        //
        //             } else {
        //                 alert("上传的文件不可为空");
        //             }
        //         });
        // },
        initStation: function () {
            $.ajax({
                async: false,
                cache: false,
                type: 'POST',
                url: ctxPath + "/systemConfiguration/getStation",
                error: function () {
                },
                success: function (data) {
                    if (data == null) {
                        alert("请输入站点")
                    } else {

                        $("#txtStationId").val(data.id);
                        $("#txtStation").val(data.station);
                        $("#txtUnitName").val(data.name);
                        $("#txtUnitAddress").val(data.address);
                    }
                }
            });

        },
        // 加载主设备类型、相别、区域位置、主设备LD编码枚举
        // 主设备列表
        initEquipmentList: function () {
            // 获取区域位置
            $.ajax({
                async: false,
                cache: false,
                type: 'POST',
                url: ctxPath + "/systemConfiguration/getAllSpace",
                error: function () {// 请求失败处理函数
                    // alert("false");
                },
                success: function (map) {
                    // dele()
                    console.log();
                    var space = map.space;
                    var phase = map.phase;
                    LDDevices = map.LDDevices;
                    deviceTypes = map.deviceTypes;
                    lDeviceMap = map.lDeviceMap
                    // PubDeviceTypeEnum = map.PubDeviceTypeEnum;


                    for (var i = 0; i < space.length; i++) {
                        spaceIds.push({
                            "text": space[i].spaceName,
                            "value": space[i].spaceId
                        });
                    }
                    for (var i = 0; i < phase.length; i++) {
                        phase_selects.push({
                            "text": phase[i],
                            "value": phase[i]
                        });
                    }
                }
            });


            var _box = null;
            var tablename = "#equipmentList";
            _box = new YDataGrid(_this.equipment_config, tablename, true,
                false, true, true);
            // alert("1");
            _box.grid.datagrid.defaults.onDblClickRow = _this.update;
            _box.init();
            // alert("2");
        },
        update: function () {
            _this.edit_equipment();
        },
        update2: function () {
            _this.edit_device();
        },
        equipment_config: {
            dataGrid: {
                pageSize: 10,
                title: '',
                url: ctxPath + "/systemConfiguration/getEquipmentList",
                singleSelect: true,
                columns: [[ /*
								 * { field : 'id', checkbox : true },
								 */
                    // {
                    //     field: 'equipmentID',
                    //     title: '设备编码',
                    //     align: 'center',
                    //     sortable: true,
                    //     width: 150,
                    //     formatter: function (value, row, index) {
                    //         return row.equipmentID;
                    //     }
                    // },
                    {
                        field: 'equipmentName',
                        title: '设备名称',
                        align: 'center',
                        sortable: true,
                        width: 240,
                        formatter: function (value, row, index) {
                            return row.equipmentName;
                        }
                    },
                    {
                        field: 'IEC61850LD',
                        title: '主设备编码',
                        align: 'center',
                        sortable: true,
                        width: 240,
                        formatter: function (value, row, index) {
                            return row.iec61850LD;
                        }
                    },
                    {
                        field: 'phase',
                        title: '设备相别',
                        align: 'center',
                        sortable: true,
                        width: 180,
                        formatter: function (value, row, index) {
                            return row.phase;
                        }
                    },
                    {
                        field: 'SpaceId',
                        title: '区域位置',
                        align: 'center',
                        sortable: true,
                        width: 240,
                        formatter: function (value, row, index) {
                            return row.spaceId;
                        }
                    },
                    // {
                    //     field: 'manufactoryName',
                    //     title: '制造厂家',
                    //     align: 'center',
                    //     sortable: true,
                    //     width: 140,
                    //     formatter: function (value, row, index) {
                    //         return row.manufactoryName;
                    //     }
                    // },

                    {
                        field: 'edit',
                        title: '修改',
                        align: 'center',
                        sortable: true,
                        width: 160,
                        formatter: function (value, row, index) {
                            return "<center><div class='dit' style='width: 12px; height: 12px;' ><a href='javascript:void(0);' onclick='jeecg.systemConfiguration.edit_equipment("
                                + index + ")'>修改</a></div></center>";

                        }
                    },
                    {
                        field: 'del',
                        title: '删除',
                        align: 'center',
                        sortable: true,
                        width: 160,
                        formatter: function (value, row, index) {
                            return "<center><div class='dit' style='width: 12px; height: 12px;' ><a href='javascript:void(0);'  onclick='jeecg.systemConfiguration.delete_equipment("
                                + index + ")'>删除</a></div></center>";

                        }
                    },]],
                toolbar: [
                    "-",
                    {
                        id: '',
                        text: '添加',
                        iconCls: '',
                        handler: function () {
                            // alert("添加");
                            jeecg.systemConfiguration.edit_equipment(-1);
                        }
                    },
                    "-",
                    {
                        id: '',
                        text: '导出台账',
                        iconCls: '',
                        handler: function () {
                            var _url = ctxPath
                                + "/systemConfiguration/getExportList";
                            $.ajax({
                                async: false,
                                cache: false,
                                type: 'POST',
                                url: _url,
                                error: function () {// 请求失败处理函数
                                    alert("连接数据库失败");
                                },
                                success: function (data) {

                                    for (var i = 0; i < data.dataList.length; i++) {
                                        data.dataList[i].deviceType = DevTypeDesc[data.dataList[i].deviceType];
                                    }
                                    var head_data, json;
                                    head_data = [];
                                    head_data.push({
                                        "title": "设备ID",
                                        "value": "deviceID"
                                    }, {
                                        "title": "设备名称",
                                        "value": "deviceName"
                                    }, {
                                        "title": "设备类型",
                                        "value": "deviceType"
                                    }, {
                                        "title": "IEC61850LD_LN",
                                        "value": "iec61850LD_LN"
                                    }, {
                                        "title": "设备区域",
                                        "value": "space"
                                    }, {
                                        "title": "IP地址",
                                        "value": "ied_IP"
                                    }, {
                                        "title": "生产厂家",
                                        "value": "manufactoryName"
                                    }, {
                                        "title": "采样周期",
                                        "value": "smpPeriod"
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

                onSelect: function (rowIndex, rowData) {
                    var _box2 = null;
                    equipment_select = "";
                    equipment_manufactoryName = "";

                    var tablename = "#device_data_list";
                    equipment_select = rowData.equipmentName;
                    // equipment_manufactoryName = rowData.manufactoryName;
                    _this.device_config.dataGrid.url = ctxPath + "/systemConfiguration/getDeviceList?EquipmentID=" + rowData.equipmentID;
                    _box2 = new YDataGrid(_this.device_config, tablename, true,
                        false, true, true);
                    _box2.grid.datagrid.defaults.onDblClickRow = _this.update2;
                    ;
                    console.log(lDeviceMap)
                    console.log(rowData.iec61850LD)
                    iec61850LD_LNs = lDeviceMap[rowData.iec61850LD];


                    // $.ajax({
                    //     async: false,
                    //     cache: false,
                    //     type: 'POST',
                    //     url: ctxPath + "/systemConfiguration/getLd_Ln",
                    //     error: function () {// 请求失败处理函数
                    //         // alert("false");
                    //     },
                    //     success: function (data) {
                    //         for (var i = 0; i < data.length; i++) {
                    //             Ld_Ln.push({
                    //                 "text": data[i].replace(';', '/'),
                    //                 "value": i
                    //             });
                    //         }
                    //     }
                    // });
                    _box2.init();
                }
            }
        },

        // restart : function() {
        // 	$.ajax({
        // 		async : false,
        // 		cache : false,
        // 		type : 'POST',
        // 		url : ctxPath + "/systemConfiguration/restart",
        // 		error : function() {// 请求失败处理函数
        // 			alert("false");
        // 		},
        // 		success : function(data) {
        // 			alert("命令已下发");
        // 		}
        // 	});
        // },
        // 主设备修改添加提交事件
        getNextID: function () {
            var str = "";
            $.ajax({
                async: false,
                cache: false,
                type: 'POST',
                url: ctxPath + "/systemConfiguration/getNextEquipmentID",
                dataType: json,
                error: function () {
                    alert("数据库连接异常");
                },
                success: function (data) {

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
        getIED61850LDs: function () {
            $.ajax({
                async: false,
                cache: false,
                type: 'POST',
                url: ctxPath + "/systemConfiguration/getEquipmentIED61850LDsList?LDDevices=" + LDDevices,
                // data: formData,
                dataType: json,
                error: function () {
                    alert("数据库连接异常");
                },
                success: function (data) {
                    ldDevice_selects = [];
                    for (var i = 0; i < data.length; i++) {
                        ldDevice_selects.push({
                            "text": data[i],
                            "value": data[i]
                        });
                    }
                }

            });
        },
        // 主设备修改按钮点击事件
        edit_equipment: function (index) {
            var equipmentID, equipmentName, IEC61850LD, phase, spaceId, manufactoryName, Remark
            if (index < 0) {
                var nextID = _this.getNextID();
                $("#equipmentID").attr("disabled", true);
                equipmentID = nextID;
                equipmentName = "";
                _this.getIED61850LDs();
                if (ldDevice_selects != null && ldDevice_selects.length > 0) {
                    IEC61850LD = ldDevice_selects[0].value;
                } else {
                    alert("主设备添加完成")
                    return;
                }
                phase = phase_selects[0].value;
                spaceId = spaceIds[0].value;
                manufactoryName = "";
                Remark = "";
            } else {
                var rows = $("#equipmentList").datagrid('getRows');
                var select_data = rows[index];
                equipmentID = select_data.equipmentID;
                equipmentName = select_data.equipmentName;
                IEC61850LD = select_data.iec61850LD;
                phase = select_data.phase;
                spaceId = select_data.spaceId;
                manufactoryName = select_data.manufactoryName;
                Remark = select_data.remark;
            }
            $("#equipmentID").val(equipmentID);
            $("#equipmentName").val(equipmentName);

            $("#IEC61850LD").combobox("loadData", ldDevice_selects);
            $("#IEC61850LD").combobox('select', IEC61850LD);
            $("#phase").combobox("loadData", phase_selects);
            $("#phase").combobox('select', phase);
            $("#spaceId").combobox("loadData", spaceIds);
            $("#spaceId").combobox('select', spaceId);
            $("#manufactoryName").val(manufactoryName);
            $("#Remark").val(Remark);

            // $('#equipment_submit').click(_this.equipment_submit);
            $('#equipment_submit').unbind('click').click(function () {
                _this.equipment_submit();
            });
            $('#equipment_close').unbind('click').click(function () {
                addDataWin.window('close');
            });
            addDataWin.window('open');
        },
        // 修改界面提交事件
        equipment_submit: function () {
            var formData = {};
            var url = ctxPath + "/systemConfiguration/update_equipment";
            formData['EquipmentID'] = $("#equipmentID").val();
            formData['EquipmentName'] = $("#equipmentName").val();
            formData['Phase'] = $('#phase').combobox('getValue');
            formData['SpaceId'] = $('#spaceId').combobox('getValue');
            formData['iec61850LD'] = $('#IEC61850LD').combobox('getValue');
            formData['ManufactoryName'] = $("#manufactoryName").val();
            formData['Remark'] = $("#Remark").val();
            $.ajax({
                async: false,
                cache: false,
                type: 'POST',
                url: url,
                data: formData,
                dataType: json,
                error: function () {// 请求失败处理函数
                    alert("false");
                },
                success: function () {
                    addDataWin.window('close');
                    $('#equipmentList').datagrid('reload');
                }
            });

        },
        // 设备添加修改界面
        device_config: {
            dataGrid: {
                title: '',
                url: ctxPath + "/systemConfiguration/getDeviceList",
                singleSelect: true,
                columns: [[ /*
								 * { field : 'id', checkbox : true },
					// 			 */
                    // {
                    //     field: 'deviceID',
                    //     title: '设备编码',
                    //     align: 'center',
                    //     sortable: true,
                    //     width: 150,
                    //     formatter: function (value, row, index) {
                    //         return row.deviceID;
                    //     }
                    // },
                    {
                        field: 'deviceName',
                        title: '设备名称',
                        align: 'center',
                        sortable: true,
                        width: 200,
                        formatter: function (value, row, index) {
                            return row.deviceName;
                        }
                    },
                    {
                        field: 'IEC61850LD_LN',
                        title: 'IEC61850LD_LN编码',
                        align: 'center',
                        sortable: true,
                        width: 240,
                        formatter: function (value, row, index) {
                            return row.iec61850LD_LN;
                        }
                    },

                    {
                        field: 'deviceType',
                        title: '设备类型',
                        align: 'center',
                        sortable: true,
                        width: 200,
                        formatter: function (value, row, index) {
                            return DevTypeDesc[row.deviceType];
                        }
                    },
                    {
                        field: 'equipmentID',
                        title: '监测主设备',
                        align: 'center',
                        sortable: true,
                        width: 140,
                        formatter: function () {
                            return equipment_select;
                        }
                    },
                    {
                        field: 'manufactoryName',
                        title: '制造厂家',
                        align: 'center',
                        sortable: true,
                        width: 140,
                        formatter: function (value, row, index) {
                            return row.manufactoryName;
                        }
                    },
                    {
                        field: 'deviceProductID',
                        title: '出厂序号',
                        align: 'center',
                        sortable: true,
                        width: 140,
                        formatter: function (value, row, index) {
                            return row.deviceProductID;
                        }
                    },
                    {
                        field: 'startOperateTime',
                        title: '投运时间',
                        align: 'center',
                        sortable: true,
                        width: 140,
                        formatter: function (value, row, index) {
                            return row.startOperateTime;
                        }
                    },

                    {
                        field: 'detail',
                        title: '详细',
                        align: 'center',
                        sortable: true,
                        width: 120,
                        formatter: function (value, row, index) {
                            return "<center><div class='dit' style='width: 12px; height: 12px;' ><a	href='javascript:void(0);'  onclick='jeecg.systemConfiguration.edit_device("
                                + index + ")'>详细</a></div></center>";
                        }
                    },
                    {
                        field: 'edit',
                        title: '修改',
                        align: 'center',
                        sortable: true,
                        width: 120,
                        formatter: function (value, row, index) {
                            return "<center><div  class='dit' style='width: 12px; height: 12px;' ><a  href='javascript:void(0);'  onclick='jeecg.systemConfiguration.edit_device("
                                + index + ")'>修改</a></div></center>";
                        }
                    },
                    {
                        field: 'del',
                        title: '删除',
                        align: 'center',
                        sortable: true,
                        width: 120,
                        formatter: function (value, row, index) {
                            return "<center><div class='dit' style='width: 12px; height: 12px;' ><a  href='javascript:void(0);' onclick='jeecg.systemConfiguration.delete_device("
                                + index + ")'>删除</a></div></center>";
                        }
                    },]],
                toolbar: ["-", {
                    id: '',
                    text: '添加',
                    iconCls: '',
                    handler: function () {
                        _this.device_add();
                    }
                },
                    //     "-", {
                    //     id: '',
                    //     text: '导入台账',
                    //     iconCls: '',
                    //     handler: function () {
                    //         uploadWin.window('open');
                    //     }
                    // },
                    "-", {
                        id: '',
                        text: '告警管理',
                        iconCls: '',
                        handler: function () {
                            var select_data = $("#device_data_list").datagrid("getSelected");
                            if (select_data != null) {
                                var deviceType = select_data.deviceType;
                                if (deviceType > 4 || deviceType < 1) {
                                    alert("不用设置告警信息")
                                } else {
                                    _this.DeviceMonitorPara();
                                }
                            }
                        }
                    }, "-"],

                onSelect: function (rowIndex, rowData) {

                }

            }
        },
        // 告警界面
        monitor_config: {
            dataGrid: {
                title: '',
                columns: [[
                    {
                        field: 'strName',
                        title: '告警量名称',
                        align: 'center',
                        sortable: true,
                        width: 100,
                        formatter: function (value, row, index) {
                            return row.strName;
                        }
                    },
                    {
                        field: 'strValue',
                        title: '告警值',
                        align: 'center',
                        sortable: true,
                        width: 100,
                        formatter: function (value, row, index) {

                            var strValue = row.strValue;
                            if (strValue == null) {
                                strValue = '';
                            }
                            return "<div class='dit' style='width: 12px; height: 12px;' ><input disabled id='gj_txt" + index + "' value='" + strValue + "' /></div>";
                        }
                    },
                    {
                        field: 'edit',
                        title: '编辑',
                        align: 'center',
                        sortable: true,
                        width: 100,
                        formatter: function (value, row, index) {
                            return "<div class='dit' style='width: 12px; height: 12px;' ><a href='javascript:void(0);' onclick='jeecg.systemConfiguration.monitorUpdate("
                                + index + ")'>编辑</a></div>";
                        }
                    },
                    {
                        field: 'del',
                        title: '更新',
                        align: 'center',
                        sortable: true,
                        width: 100,
                        formatter: function (value, row, index) {
                            return "<div class='dit' style='width: 12px; height: 12px;' ><a href='javascript:void(0);' onclick='jeecg.systemConfiguration.apply()'>更新</a></div>";
                        }
                    },]],
                toolbar: ["-", {
                    id: '',
                    text: '应用至同类设备',
                    iconCls: '',
                    handler: function () {
                        _this.apply();
                    }
                }, "-", {
                    id: '',
                    text: '返回',
                    iconCls: '',
                    handler: function () {
                        deviceMonitorWin.window('close');
                    }
                }, "-"],

                onSelect: function (rowIndex, rowData) {
                }

            }
        },
        monitorUpdate: function (index) {
            $("#gj_txt" + index).attr("disabled", false);

        },
        // 应用至同类设备按钮和更新按钮的点击事件
        apply: function () {

            var arr = [];
            var deviceType = $("#device_data_list").datagrid("getSelected").deviceType;
            var url_arr = new Array("", "getStomMonitorID", "getSf6MonitorID",
                "getSmoamMonitorID", "getScomMonitorID",
                "getSconditionMonitorID");

            url = ctxPath + "/systemConfiguration/" + url_arr[deviceType]
                + "";
            // 将告警表中的设备ID存储到数组中
            $.ajax({
                async: false,
                cache: false,
                type: 'POST',
                url: url,
                error: function () {// 请求失败处理函数
                    alert("数据获取失败");
                },
                success: function (data) {
                    for (var i = 0; i < data.length; i++) {
                        arr.push(data[i].deviceID);
                    }
                }
            });
            // 修改和插入url的配置
            var url_insert = new Array("", "insertStomMonitor",
                "insertSf6Monitor", "insertSmoamMonitor",
                "insertScomMonitor");
            var url_update = new Array("", "updateStomMonitor",
                "updateSf6Monitor", "updateSmoamMonitor",
                "updateScomMonitor");
            // 遍历选中复选框,将值根据是否在告警表中,加入插入或修改数组
            var successFlag = true;// 判断是否应用成功

            $("input[id='checkbox']")
                .each(
                    function () {
                        var FormData = {};
                        if (this.checked == true) {
                            var id = $(this).val();
                            var datagrid = $("#monitor_data_list");
                            var flag = arr.indexOf(id) > -1;
                            var _url = "";
                            switch (deviceType) {
                                case (1):
                                    FormData["deviceID"] = id;

                                    FormData["h2ThresHold"] = $("#gj_txt0").val();
                                    FormData["c2h2ThresHold"] = $("#gj_txt1").val();
                                    FormData["thThresHold"] = $("#gj_txt2").val();

                                    FormData["ch4ThresHold"] = $("#gj_txt3").val();
                                    FormData["coThresHold"] = $("#gj_txt4").val();
                                    FormData["co2ThresHold"] = $("#gj_txt5").val();
                                    FormData["o2ThresHold"] = $("#gj_txt7").val();
                                    FormData["c2h4ThresHold"] = $("#gj_txt8").val();
                                    FormData["c2h6ThresHold"] = $("#gj_txt9").val();
                                    FormData["mstThresHold"] = $("#gj_txt6").val();

                                    // FormData["H2Change"] = $("#gj_txt3").val();
                                    // FormData["C2H2Change"] = $("#gj_txt4").val();
                                    // FormData["THChange"] = $("#gj_txt5").val();

                                    break;
                                case (2):
                                    FormData["deviceID"] = id;
                                    FormData["pressureThreshold"] = $("#gj_txt0").val();
                                    FormData["pressChgRateThreshold"] = $("#gj_txt1").val();
                                    FormData["weekChgRateThreshold"] = $("#gj_txt2").val();

                                    break;
                                case (3):
                                    FormData["deviceID"] = id;
                                    FormData["totAThresHold"] = $("#gj_txt0").val();
                                    break;
                                case (4):
                                    FormData["deviceID"] = id;
                                    FormData["preOilUpThresHold"] = $("#gj_txt0").val();
                                    FormData["preOilDownThresHold"] = $("#gj_txt1").val();
                                    FormData["pre1ThresHold"] = $("#gj_txt2").val();
                                    FormData["pre2ThresHold"] = $("#gj_txt3").val();
                                    FormData["tmp1ThresHold"] = $("#gj_txt4").val();
                                    FormData["tmp3ThresHold"] = $("#gj_txt5").val();
                                    FormData["tmp4ThresHold"] = $("#gj_txt6").val();
                                    FormData["mainOilUpThresHold"] = $("#gj_txt7").val();
                                    FormData["mainOilDownThresHold"] = $("#gj_txt8").val();
                                    FormData["sltcOilUpThresHold"] = $("#gj_txt9").val();
                                    FormData["sltcOilDownThresHold"] = $("#gj_txt10").val();
                                    break;
                                // case (5):
                                //     FormData["deviceID"] = id;
                                //     FormData["OilTempThresHold"] = $("#gj_txt0").val();
                                //     break;
                            }
                            if (flag) {
                                _url = ctxPath
                                    + "/systemConfiguration/"
                                    + url_update[deviceType]
                                    + "";
                            } else {
                                _url = ctxPath
                                    + "/systemConfiguration/"
                                    + url_insert[deviceType]
                                    + "";
                            }
                            $.ajax({
                                async: false,
                                cache: false,
                                type: 'POST',
                                data: FormData,
                                url: _url,
                                dataType: json,
                                error: function () {// 请求失败处理函数
                                    successFlag = false;
                                },
                                success: function (data) {
                                    if (data) {
                                        successFlag = true;
                                    } else {
                                        successFlag = false;
                                    }
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
        DeviceMonitorPara: function () {
            deviceMonitorWin = $('#deviceMonitor-window').window({
                href: '',
                left: '100px',
                top: '70px',
                closed: false,
                modal: false,
                cache: false,
                minimizable: false,
                maximizable: true,
                collapsible: false,
                shadow: false
            });
            // 加载告警表
            var _box3 = null;
            var tablename = "#monitor_data_list";
            var select_data = $("#device_data_list").datagrid("getSelected");
            var url_arr = new Array("", "getStomMonitor", "getSf6Monitor",
                "getSmoamMonitor", "getScomMonitor");


            if (select_data != null) {
                var deviceType = select_data.deviceType;
                _this.monitor_config.dataGrid.url = ctxPath
                    + "/systemConfiguration/" + url_arr[deviceType]
                    + "?deviceID=" + select_data.deviceID;
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
            var url = ctxPath + "/systemConfiguration/getCheckBox";
            formData['deviceID'] = $("#device_data_list").datagrid(
                "getSelected").deviceID;
            formData['deviceType'] = $("#device_data_list").datagrid(
                "getSelected").deviceType;
            formData['equipmentID'] = $("#device_data_list").datagrid(
                "getSelected").equipmentID;
            $
                .ajax({
                    async: false,
                    cache: false,
                    type: 'POST',
                    url: url,
                    data: formData,
                    error: function () {// 请求失败处理函数
                        alert("数据获取失败");
                    },
                    success: function (data) {

                        var DeviceID = $("#device_data_list").datagrid(
                            "getSelected").deviceID;
                        var DeviceName = $("#device_data_list").datagrid(
                            "getSelected").deviceName;
                        var html = "";
                        $("#select").empty();
                        $("#select").append("<input id='checkbox' name='checkbox_' checked disabled type='checkbox' value='" + DeviceID + "'>" + DeviceName + "<br/>");
                        for (var i = 0; i < data.length; i++) {
                            html = "<input id='checkbox' name='checkbox' type='checkbox'  value='" + data[i].deviceID + "' />" + data[i].deviceName + "</br>";
                            $("#select").append(html);
                        }
                    }
                });



               $("#select_all").unbind('click').click(function() {
                    $('input[name="checkbox"]').attr("checked",this.checked);
                });
                var $subBox = $("input[name='checkbox']");
                $subBox.unbind('click').click(function(){
                    $("#select_all").attr("checked",$subBox.length == $("input[name='checkbox']:checked").length ? true : false);
                });
                $("#select_all").attr("checked",$subBox.length == $("input[name='checkbox']:checked").length ? true : false);

            // // 全选框点击事件
            // $('#select_all').unbind('click').click(function () {
            //     debugger
            //     var loves = document.getElementsByName("checkbox");
            //     for (var i = 0; i <  loves.length; i++) {
            //         loves[i].checked = !this.checked;
            //     }
            //     // $("input [name='checkbox']").each(function () {
            //     //     debugger
            //     //     $(this).attr("checked", !this.checked);
            //     // });
            // });

        },
        // 删除主设备
        delete_equipment: function (index) {
            if (window.confirm('确定要删除？')) {
                var rows = $("#equipmentList").datagrid('getRows');
                var select_data = rows[index];
                var formData = {};
                var url = ctxPath + "/systemConfiguration/delete_equipment", EquipmentID, SpaceId, DeviceType,
                    DeviceName, Phase, ManufactoryName, Remark
                formData['EquipmentID'] = select_data.equipmentID;
                $.ajax({
                    async: false,
                    cache: false,
                    type: 'POST',
                    url: url,
                    data: formData,
                    dataType: json,
                    error: function () {// 请求失败处理函数
                        alert("false");
                    },
                    success: function (data) {
                        alert(data)
                        addDataWin.window('close');
                        $('#equipmentList').datagrid('reload');
                    }
                });
            }
        },
        // 删除设备
        delete_device: function (index) {
            if (window.confirm('确定要删除？')) {
                var rows = $("#device_data_list").datagrid('getRows');
                var select_data = rows[index];
                var formData = {};
                var url = ctxPath + "/systemConfiguration/delete_device";
                // EquipmentID,SpaceId,DeviceType,DeviceName,Phase,ManufactoryName,Remark
                formData['deviceID'] = select_data.deviceID;
                $.ajax({
                    async: false,
                    cache: false,
                    type: 'POST',
                    url: url,
                    data: formData,
                    dataType: json,
                    error: function () {// 请求失败处理函数
                        alert("false");
                    },
                    success: function (data) {
                        addDataWin.window('close');
                        $('#device_data_list').datagrid('reload');
                    }
                });
            }
        },
        // 设备修改点击事件
        edit_device: function (index) {

            deviceWin.window('open');
            var equipment_data = $("#equipmentList").datagrid("getSelected");
            $("#textMDev").val(equipment_data.equipmentName);
            var rows = $("#device_data_list").datagrid('getRows');

            var select_data = rows[index];
            // $("#IEC61850LD_LN").combobox("loadData", Ld_Ln);
            $("#IEC61850LD_LN").combobox('select', select_data.iec61850LD_LN);
            $("#txtDeviceID").val(select_data.deviceID);
            $('#txtDeviceID').attr("disabled", true);
            $("#txtDeviceName").val(select_data.deviceName);
            $("#ddlDeviceType").combobox("loadData", deviceTypes);
            $("#ddlDeviceType").combobox('select', select_data.deviceType);
            $("#tPhase").val(equipment_data.phase);
            $("#txtcode").val(select_data.deviceProductID);
            $("#txtRunTime").val(select_data.startOperateTime);
            $("#txtFactory").val(select_data.manufactoryName);
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

            $('#device_close').unbind('click').click(function () {
                deviceWin.window('close');
            });
            $('#device_submit').unbind("click");
            $('#device_submit').bind('click', _this.device_update);

            // deviceType_data.push({ "text":
            // DevTypeDesc[select_data.deviceType], "value":
            // select_data.deviceType});

        },
        // 设备修改提交事件
        device_update: function () {
            var formData = {};
            var select_data = $("#device_data_list").datagrid("getSelected");
            var equipment_data = $("#equipmentList").datagrid("getSelected");
            var url = ctxPath + "/systemConfiguration/update_device";
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
            formData['IEC61850LD_LN'] = $("#IEC61850LD_LN").combobox('getValue');
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
                async: false,
                cache: false,
                type: 'POST',
                url: url,
                data: formData,
                dataType: json,
                error: function () {// 请求失败处理函数
                    alert("数据库连接异常");
                },
                success: function (data) {
                    alert("修改成功");
                    deviceWin.window('close');
                    $('#device_data_list').datagrid('reload');
                }
            });
        },
        getIED61850LD_LNs: function (equipmentID) {
            $.ajax({
                async: false,
                cache: false,
                type: 'POST',
                url: ctxPath + "/systemConfiguration/getDeviceIED61850LD_LNsList?equipmentID=" + equipmentID + "&iec61850LD_LNs=" + iec61850LD_LNs,
                // data: formData,
                dataType: json,
                error: function () {
                    alert("数据库连接异常");
                },
                success: function (data) {
                    Ld_Ln = [];
                    for (var i = 0; i < data.length; i++) {
                        Ld_Ln.push({
                            "text": data[i],
                            "value": data[i],
                        });
                    }
                }

            });
        },
        getNextDeviceID: function () {
            var str = "";
            $.ajax({
                async: false,
                cache: false,
                type: 'POST',
                url: ctxPath + "/systemConfiguration/getNextDeviceID",
                error: function () {
                    alert("数据库连接异常");
                },
                success: function (data) {
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
        device_add: function () {

            var equipment_data = $("#equipmentList").datagrid("getSelected");

            $("#textMDev").val(equipment_data.equipmentName);
            $("#tPhase").val(equipment_data.phase);
            var nextDeviceID = _this.getNextDeviceID();
            $("#txtDeviceID").val(nextDeviceID);
            $('#txtDeviceID').attr("disabled", true);


            _this.getIED61850LD_LNs(equipment_data.equipmentID);
            $("#ddlDeviceType").combobox("loadData", deviceTypes);

            $("#IEC61850LD_LN").val("");
            if (Ld_Ln != null && Ld_Ln.length > 0) {
                $("#IEC61850LD_LN").combobox("loadData", Ld_Ln);
                $("#IEC61850LD_LN").combobox('select', Ld_Ln[0].value);
            } else {
                alert("添加完成")
                return;
            }
            deviceWin.window('open');
            // $("#ddlDeviceType").combobox("loadData", deviceTypes);
            // $("#ddlDeviceType").combobox('select', deviceTypes[0].value);
            $("#txtcode").val("");
            $("#txtRunTime").val("");
            $("#txtFactory").val("");
            $("#txtIPAddress").val("");
            $("#txtInstallAddress").val("");
            $("#tPosition").val("");
            $("#txtDeviceName").val("");


            $("#txtRemark").val("");
            $('#cbStopSoundAlarm').attr("checked", false);
            $('#cbStopUse').attr("checked", false);

            $('#device_close').unbind('click').click(function () {
                deviceWin.window('close');
            });
            $('#device_submit').unbind("click");
            $('#device_submit').bind('click', _this.device_insert);
        },
        // 设备添加提交事件
        device_insert: function () {
            var formData = {};
            // var select_data = $("#device_data_list").datagrid("getSelected");
            var equipment_data = $("#equipmentList").datagrid("getSelected");
            var url = ctxPath + "/systemConfiguration/insert_device";
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
            formData['IEC61850LD_LN'] = $("#IEC61850LD_LN").combobox('getValue');

            // formData['IEC61850LD_LN'] = $("#IEC61850LD_LN").combobox('getValue');
            //
            // if(formData['IEC61850LD_LN']==""||formData['IEC61850LD_LN']==null||formData['IEC61850LD_LN'].length<3){
            //     alert("IEC61850LD_LN不能为空！");
            //     return;
            // }
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
                async: false,
                cache: false,
                type: 'POST',
                url: url,
                data: formData,
                dataType: json,
                error: function () {// 请求失败处理函数
                    alert("数据库连接异常");
                },
                success: function (data) {
                    alert("添加成功");
                    deviceWin.window('close');
                    $('#device_data_list').datagrid('reload');
                }
            });
        },
        selectdevType: function () {

            var IEC61850LD_LN = $("#IEC61850LD_LN").combobox("getValue");
            if (IEC61850LD_LN.indexOf("SIML") != -1) {
                $("#ddlDeviceType").combobox('select', 1);
            } else if (IEC61850LD_LN.indexOf("SIMG") != -1) {
                $("#ddlDeviceType").combobox('select', 2);
            } else if (IEC61850LD_LN.indexOf("SSAR") != -1
                || IEC61850LD_LN.indexOf("ZSAR") != -1 ||
                IEC61850LD_LN.indexOf("SLAR") != -1) {
                $("#ddlDeviceType").combobox('select', 3);
            } else if (IEC61850LD_LN.indexOf("SPTR") != -1) {
                $("#ddlDeviceType").combobox('select', 4);
            } else if (IEC61850LD_LN.indexOf("SINS") != -1) {
                $("#ddlDeviceType").combobox('select', 7);
            } else if (IEC61850LD_LN.indexOf("MMET") != -1) {
                $("#ddlDeviceType").combobox('select', 8);
            } else {
                $("#ddlDeviceType").combobox('select', -1);
            }
            // // $("#IEC61850LD_LN").combobox("loadData", []);
            // switch (ddlDeviceType) {
            //     case '1':
            //         for (var i = 0; i < Ld_Ln.length; i++) {
            //             var dev = Ld_Ln[i];
            //             if (dev.text.indexOf("SIML") != -1) {
            //                 Ld_Ln_dev.push({
            //                     "text": dev.text,
            //                     "value": dev.value
            //                 });
            //             }
            //         }
            //         break;
            //     case '2':
            //         for (var i = 0; i < Ld_Ln.length; i++) {
            //             var dev = Ld_Ln[i];
            //             if (dev.text.indexOf("SIMG") != -1) {
            //                 Ld_Ln_dev.push({
            //                     "text": dev.text,
            //                     "value": dev.value
            //                 });
            //             }
            //         }
            //         break;
            //     case '3':
            //         for (var i = 0; i < Ld_Ln.length; i++) {
            //             var dev = Ld_Ln[i];
            //             if (dev.text.indexOf("SSAR") != -1
            //                 || dev.text.indexOf("ZSAR") != -1 ||
            //                 dev.text.indexOf("SLAR") != -1) {
            //                 Ld_Ln_dev.push({
            //                     "text": dev.text,
            //                     "value": dev.value
            //                 });
            //             }
            //         }
            //         break;
            //     case '4':
            //         for (var i = 0; i < Ld_Ln.length; i++) {
            //             var dev = Ld_Ln[i];
            //             if (dev.text.indexOf("SPTR") != -1) {
            //                 Ld_Ln_dev.push({
            //                     "text": dev.text,
            //                     "value": dev.value
            //                 });
            //             }
            //         }
            //         break;
            //     case '7':
            //         for (var i = 0; i < Ld_Ln.length; i++) {
            //             var dev = Ld_Ln[i];
            //             if (dev.text.indexOf("SINS") != -1) {
            //                 Ld_Ln_dev.push({
            //                     "text": dev.text,
            //                     "value": dev.value
            //                 });
            //             }
            //         }
            //         break;
            //     case '8':
            //         for (var i = 0; i < Ld_Ln.length; i++) {
            //             var dev = Ld_Ln[i];
            //             if (dev.text.indexOf("MMET") != -1) {
            //                 Ld_Ln_dev.push({
            //                     "text": dev.text,
            //                     "value": dev.value
            //                 });
            //             }
            //         }
            //         break;
            //     default: {
            //         Ld_Ln_dev = Ld_Ln;
            //     }
            // }
            // $("#IEC61850LD_LN").combobox("loadData", Ld_Ln_dev);
            // if (Ld_Ln_dev == null || Ld_Ln_dev.length == 0) {
            //
            // } else {
            //     $("#IEC61850LD_LN").combobox('select', Ld_Ln_dev[0].value);
            // }
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
    formData['id'] = $("#txtStationId").val();

    $.ajax({
        async: false,
        cache: false,
        type: 'POST',
        data: formData,
        url: ctxPath + "/systemConfiguration/updateStation",
        // error : function() {
        // 	alert("修改失败")
        // },
        success: function (data) {
            if (data) {
                alert("修改成功");
                $(".tabs li:eq(1)").show();
                $(".tabs li:eq(2)").show();
                $(".tabs li:eq(3)").show();
                $(".tabs li:eq(4)").show();
                $(".tabs li:eq(5)").show();
                jeecg.systemConfiguration.initStation();
            } else {
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

$(function () {
    $('#programVersion').datagrid({
        fit: true,
        columns: [[{
            field: 'stTime',
            title: '程序升级时间',
            width: 200
        }, {
            field: 'programName',
            title: '程序名',
            width: 200
        }, {
            field: 'md5Num',
            title: 'MD5码',
            width: 300
        }, {
            field: 'editionNum',
            title: '版本号',
            width: 100
        }]]
    });
    /*
     * programVersionSearch();
     */
    // 添加版本信息
    $("#confirm_add").click(function () {
        if ($("#addProgramForm").form('validate')) {
            $("#addProgramForm").attr("action", 'addProgram');
            $("#addProgramForm").ajaxSubmit(function (message) {
                /*
                 * programVersionSearch();
                 */
                $("#addProgram_win").window('close');
            });

        }
    });
    // 取消
    $("#cancel_add").click(function () {
        $("#addProgram_win").window('close');
    });

    // 修改版本信息
    $("#confirm_edit").click(function () {
        if ($("#editProgramForm").form('validate')) {
            $("#editProgramForm").attr("action", 'editProgram');
            $("#editProgramForm").ajaxSubmit(function (message) {
                /*
                 * programVersionSearch();
                 */
                $("#editProgram_win").window('close');
            });

        }
    });
    // 取消
    $("#cancel_edit").click(function () {
        $("#editProgram_win").window('close');
    });
});

/*
 * function programVersionSearch() { $.ajax({ type : 'post', dataType : 'json',
 * url : 'ProgramVersion_search', success : function(data) {
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
            type: 'post',
            url: 'delProgram?programName=' + programName,
            success: function () {
                /*
                 * programVersionSearch();
                 */
                $.messager.alert('提示', '删除成功！', 'info');
            }
        });
    }
}
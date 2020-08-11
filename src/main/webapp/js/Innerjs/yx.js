$package('jeecg.yx');
$(function () {
    jeecg.yx.SetYxData();
    jeecg.yx.ShowYxData();
    refresh(600);
});
jeecg.yx = function () {
    //alert($("#txtName").val());
    var _this = {
        // 事件
        ShowYxData: function () {
            $('#yx_data-list').datagrid({
                showHeader: true,
                view: transposedview,
                headerWidth: 180,
                itemWidth: 200,
                title: $("#txtName").val(),
                singleSelect: true,
                nowrap: false
            });
            $('#yx_data-list_amc').datagrid({
                showHeader: true,
                headerWidth: 180,
                itemWidth: 200,
                title: $("#txtName").val(),
                singleSelect: true,
                nowrap: false
            });
        },
        SetYxData: function () {
            debugger
            var DeviceType = $("#txtType").val();
            if (DeviceType == "19") {
                // document.getElementById("table_amc").style.display='none';
                document.getElementById("table_cac").style.display = 'none';
                _this.SetSpdmYxData();
            }
            if (DeviceType == "amc") {
                // document.getElementById("table_cac").style.display = 'none';
                // _this.SetYxData_amc();
            }
            if (DeviceType == "hwcw") {
                document.getElementById("table_cac").style.display = 'none';
                document.getElementById("table_amc").style.display = 'none';
                // $(".tabs li:eq(3)").hide();
                // $(".tabs li:eq(4)").hide();
                _this.SetInfraredYxData();
            } else {
                // document.getElementById("table_amc").style.display='none';
                document.getElementById("table_cac").style.display = 'none';
                _this.SetSbushYxData();
            }
        },
        SetInfraredYxData: function () {
            $('#Infrared_data').datagrid.defaults.columns = [[{
                field: 'title',
                title: '测量值',
                align: 'left'
            }, {
                field: 'tmp',
                title: '温度',
                align: 'left'
            },]];
            var id = $("#txtID").val();
            var _url = "getInfraredYx";
            var formdata = {};
            // if(id<10){
            // id='A000'+id;
            // }else if(id<100){
            // id='A00'+id;
            // }else if(id<1000){
            // id='A0'+id;
            // }else if(id<10000){
            // id='A'+id;
            // }
            formdata['DeviceID'] = id;
            $.ajax({
                async: false,
                cache: false,
                url: _url,
                type: "POST",
                data: formdata,
                error: function () {
                },
                success: function (data) {
                    data[0]["title"] = "采集时间";
                    data[1]["title"] = "参数值";
                    $('#yx_data-list').datagrid.defaults.data = {
                        "total": 1,
                        "rows": data
                    };
                }
            });
            $('#Infrared_data').datagrid({
                showHeader: true,
                view: transposedview,
                headerWidth: 180,
                itemWidth: 200,
                title: $("#txtName").val(),
                singleSelect: true,
                nowrap: false
            });
        },
        // 套管实时数据
        SetSbushYxData: function () {
            // var DeviceType = $("#txtType").val();
            var ln_inst_name = $('#txtName').val();
            ln_inst_name = ln_inst_name.replace("#", "@");
            // alert(ln_inst_name);
            var id = $("#txtID").val();
            var _box = null;
            var table_name = '#yx_data-list_amc';
            _this.showData.dataGrid.url = "getYXData?id=" + id;
            debugger
            _box = new YDataGrid(_this.showData, table_name, false, true, true,
                false);
            _box.init();
        },
        SetSpdmYxData: function () {
            $('#yx_data-list').datagrid.defaults.columns = [[{
                field: 'title',
                title: '测量值',
                align: 'left'
            }, {
                field: 'acuPaDsch',
                title: '局放声学水平',
                align: 'left'
            }, {
                field: 'uhfPaDsch',
                title: '局放UHF水平',
                align: 'left'
            }, {
                field: 'phase',
                title: '放电相位',
                align: 'left'
            }, {
                field: 'plsNum',
                title: '脉冲次数',
                align: 'left'
            },

            ]];
            var id = $("#txtID").val();
            var _url = "getSpdmYx";
            var formdata = {};
            formdata['DeviceID'] = id;
            $.ajax({
                async: false,
                cache: false,
                url: _url,
                type: "POST",
                data: formdata,
                error: function () {
                },
                success: function (data) {
                    data[0]["title"] = "采集时间";
                    data[1]["title"] = "参数值";
                    $('#yx_data-list').datagrid.defaults.data = {
                        "total": 1,
                        "rows": data
                    };
                }
            });
        },
        showData: {
            dataGrid: {
                // title: $("#txtName").val(),
                columns: [[{
                    field: 'refname',
                    title: '测量值',
                    align: 'center',
                    sortable: true,
                    width: fixWidth(0.1),
                    formatter: function (value, row, index) {
                        return row.refname;
                    }
                }, {
                    field: 'time',
                    title: '采集时间',
                    width: fixWidth(0.2),
                    align: 'center',
                    sortable: true,
                    formatter: function (value, row, index) {
                        return row.time;
                    }
                }, {
                    field: 'quality',
                    title: '品质',
                    width: fixWidth(0.2),
                    align: 'center',
                    sortable: true,
                    formatter: function (value, row, index) {
                        return row.quality;
                    }
                }, {
                    field: 'value',
                    title: '参数值',
                    width: fixWidth(0.1),
                    align: 'center',
                    sortable: true,
                    formatter: function (value, row, index) {
                        var value = row.value;
                        // if(value=="0"){
                        // value="正常";
                        // }else if(value=="1"){
                        // value="异常";
                        // }
                        return value;
                    }
                }]]
            }
        },
        // SetYxData_amc: function () {
        //     $(".tabs li:eq(2)").hide();
        //     var DeviceType = $("#txtType").val();
        //     var ln_inst_name = $('#txtName').val();
        //     ln_inst_name = ln_inst_name.replace("#", "@");
        //     var amc_id = $("#txtID").val();
        //     var _box = null;
        //     if (DeviceType == "amc") {
        //         //alert("123");
        //         var table_name = '#yx_data-list_amc';
        //         _this.showData.dataGrid.url = "getYCData?ln_inst_name="
        //             + ln_inst_name + "&id=" + amc_id;
        //         _box = new YDataGrid(_this.showData, table_name, false, true,
        //             true, false);
        //         _box.init();
        //     }
        //     // if(DeviceType=="yx"){
        //     // var table_name='#yx_data-list';
        //     // _this.showData.dataGrid.url="getYXData?ln_inst_name="+ln_inst_name+"&ld_inst_name="+ld_inst_name;
        //     // _box = new
        //     // YDataGrid(_this.showData,table_name,false,true,true,true);
        //     // _box.init();
        //     // }
        // }
    };
    return _this;
}();

function refresh(seconds) {
    setTimeout("self.location.reload()", seconds * 1000);
}


// // alert(DeviceType);
// if (DeviceType == "1") {
// 	// 油色谱及微水
// 	// document.getElementById("table_amc").style.display='none';
// 	document.getElementById("table_cac").style.display = 'none';
// 	_this.SetStomYxData();
// }
// if (DeviceType == "2") {
// 	// SF6气体压力
// 	// document.getElementById("table_amc").style.display='none';
// 	document.getElementById("table_cac").style.display = 'none';
// 	_this.SetSf6YxData();
// }
// if (DeviceType == "3") {
// 	// document.getElementById("table_amc").style.display='none';
// 	document.getElementById("table_cac").style.display = 'none';
// 	// _this.SetSmoamYxData();
// 	_this.SetSbushYxData();
// }
// if (DeviceType == "4") {
// 	// document.getElementById("table_amc").style.display='none';
// 	document.getElementById("table_cac").style.display = 'none';
// 	// /_this.SetScomYxData();
// 	_this.SetSbushYxData();
// }
// if (DeviceType == "5") {
// 	// document.getElementById("table_amc").style.display='none';
// 	document.getElementById("table_cac").style.display = 'none';
// 	// _this.SetScomYxData();
// 	_this.SetSbushYxData();
// }
// // GIS 室SF6气体泄露在线监测
// if (DeviceType == "6") {
// 	// document.getElementById("table_amc").style.display='none';
// 	document.getElementById("table_cac").style.display = 'none';
// 	// _this.SetScomYxData();
// 	_this.SetSbushYxData();
// }
// //
// if (DeviceType == "14") {
// 	// document.getElementById("table_amc").style.display='none';
// 	document.getElementById("table_cac").style.display = 'none';
// 	// _this.SetScomYxData();
// 	_this.SetSbushYxData();
// }
// if (DeviceType == "20") {
// 	document.getElementById("table_cac").style.display = 'none';
// 	_this.SetSbushYxData();
// }
// if (DeviceType == "28" || DeviceType == "7") {
// 	//气象
// 	// document.getElementById("table_amc").style.display='none';
// 	document.getElementById("table_cac").style.display = 'none';
// 	_this.SetSbushYxData();
// }
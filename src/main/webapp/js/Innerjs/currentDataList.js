$package('cac.currentDataList');
cac.currentDataList = function(){
    var _this = {
        sf6_data_config: {
            title: '六氟化硫当前数据列表',
            url: '',
            idField: 'DeviceID',
            columns: [[{
                field: 'DeviceID',
                checkbox: true,
                hidden: true
            }, {
                field: 'SampleTime',
                title: '采集时间',
                align: 'center',
                sortable: true,
                width: 80,
                formatter: function(value, row, index){
                    return row.SampleTime;
                }
            }, {
                field: 'Temperature',
                title: '温度',
                align: 'center',
                sortable: true,
                width: 80,
                formatter: function(value, row, index){
                    return row.line_pname;
                }
            }, {
                field: 'Pressure',
                title: '压力',
                align: 'center',
                sortable: true,
                width: 80,
                formatter: function(value, row, index){
                    return row.line_index;
                }
            }, {
                field: 'Density',
                title: '密度',
                align: 'center',
                sortable: true,
                width: 80,
                formatter: function(value, row, index){
                    return row.line_index;
                }
            }, {
                field: 'Remark',
                title: '状态',
                align: 'center',
                sortable: true,
                width: 80,
                formatter: function(value, row, index){
                    return row.line_index;
                }
            }]]
        }
    }
    return _this;
}();

/*********************/
function searchLine(DeviceID){
    if (DeviceID == "") {
        jeecg.alert('警告', '未选中任何终端设备，请先选择终端设备.', 'warning');
        return;
    }
    var param = {
        DeviceID: DeviceID
    };
    $.post(ctxPath + '/treeDevice/getSf6Data', param, function(data){
        $('#Current_data-list').datagrid('loadData', data);
    }, 'json');
}

/*********************/

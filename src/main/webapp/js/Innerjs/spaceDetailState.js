$package('jeecg.spaceDetailState');
var _st = null;
var _et = null;
var _off = false;
var _time = null;
$(function () {
    jeecg.spaceDetailState.setTime();
    jeecg.spaceDetailState.showSelect();
//	jeecg.spaceDetailState.showDevice();
    jeecg.spaceDetailState.showDataList();
    $('#DeviceArea').combobox({
        onChange: function () {
            $("#showdata_div").empty();
            var space = $("#DeviceArea").combo("getValue");
            if (space == "0") {
                jeecg.spaceDetailState.showDataList();
            } else {
                getDeviceBySpace(space);
            }
        }
    });
    $("#Detail_but").unbind('click').click(function () {
        export_Word();
    });
    $("#search_but").unbind('click').click(function () {
        $("#showdata_div").empty();

        _off = true;
        jeecg.spaceDetailState.showDataList();
    });
});
jeecg.spaceDetailState = function () {
    var d = 0;
    var _this = {
        setTime: function () {
            var curr_time = new Date();
            if (_st == null) {
                _st = myformatter(curr_time);
            }
            $("#startTime").datebox("setValue", _st);
        },
        showSelect: function () {
            var chart = [];
            var Type = $("#txtType").val();
            $.ajax({
                async: false,
                cache: false,
                type: 'POST',
                url: ctxPath + '/treeDevice/getSpaceNameByType.do?Type=' + Type,
                success: function (data) {
                    var Sel;
                    Sel = data;//提取data为rows
                    //循环用于Sel.refname去重复||nn[]ajax内共享
                    var nn = [];
                    for (var j = 0; j < Sel.length; j++) {
                        nn.push(Sel[j].text);
                    }
                    //循环用于向chart插入refname，为动态下拉框提供元素
                    chart.push({"text": "全部", "value": "0"});
                    for (var j = 0; j < nn.length; j++) {
                        chart.push({"text": nn[j], "value": nn[j]});//将chart组合为columns
                    }
                    $("#DeviceArea").combobox("loadData", chart);
                    $("#DeviceArea").combobox('select', chart[0].value);
                }
            });
        },
//         showDevice: function () {
//             var type = $("#txtType").val();
//             $.ajax({
//                 async: false,
//                 cache: false,
//                 type: 'POST',
//                 url: ctxPath + '/deviceHealthState/getDetailListDevice.do?Type=' + type,
//                 error: function () {// 请求失败处理函数
//                     jeecg.alert('错误信息', '数据请求失败', 'error');
//                 },
//                 success: function (data) {
//                     var chart = [];
//                     for (var j = 0; j < data.length; j++) {
//                         chart.push({"text": data.deviceName, "value": data.deviceID});//将chart组合为columns
//                     }
//                     $("#Device").combobox("loadData", chart);
//                     $("#Device").combobox('select', chart[0].value);
// //        		showdata("showdata", data ,otherData);
//                 }
//             });
//         },
        showDataList: function (type) {
            var type = $("#txtType").val();
            if (_off == true) {
                var ft = $("#findTime").combobox('getValue');
                if (ft < 10) {
                    ft = "0" + ft + ":00:00";
                } else {
                    ft = ft + ":00:00";
                }
                _time = $('#startTime').datebox('getValue') + " " + ft;
            }
            if (type == "1") {
                $.ajax({
                    async: false,
                    cache: false,
                    type: 'POST',
                    url: ctxPath + '/deviceHealthState/getStomDetailList.do?_off=' + _off + "&_time=" + _time,
                    error: function () {// 请求失败处理函数
                        jeecg.alert('错误信息', '数据请求失败', 'error');
                    },
                    success: function (data) {
                        var tableids = [];
                        var myDate = new Date();
                        myDate = myDate.toLocaleDateString();
                        myDate = (new Date(myDate)).getTime();

                        for (var n = 0; n < data.length; n++) {
                            if ((n % 3 == 0) || n == 0) {
                                var html1 = "";
                                var list = [];
                                list.push(data[n]);
                                list.push(data[n + 1]);
                                list.push(data[n + 2]);
                                var tableid = "data_list_" + n;
                                tableids.push(tableid);
                                if (list[0].CH4ppm != null) {
                                    var starttime = list[0].CH4ppm.split(" ")[0];
                                    var ret = returntime(starttime, myDate);
                                } else {
                                    var ret = "<td style='color:#ea5f5f'>无实时数据</td>";
                                }
                                var Almret;
                                if (list[2].c2H2Alm == 0) {
                                    Almret = "<td style='color:#ea5f5f'>乙炔阈值超出</td>";
                                } else if (list[2].c2H2Alm == 1) {
                                    Almret = "<td style='color:#c0e76f'>正常</td>";
                                } else if (list[2].c2H2Alm == null) {
                                    Almret = "<td style='color:#ebdc5b'>阈值未设置</td>";
                                }
                                var H2Almret;
                                if (list[2].h2Alm == 0) {
                                    H2Almret = "<td style='color:#ea5f5f'>氢气阈值超出</td>";
                                } else if (list[2].h2Alm == 1) {
                                    Almret = "<td style='color:#c0e76f'>正常</td>";
                                } else if (list[2].h2Alm == null) {
                                    Almret = "<td style='color:#ebdc5b'>阈值未设置</td>";
                                }
                                var TAlmret;
                                if (list[2].totHydcAlm == 0) {
                                    TAlmret = "<td style='color:#ea5f5f'>总烃阈值超出</td>";
                                } else if (list[2].totHydcAlm == 1) {
                                    Almret = "<td style='color:#c0e76f'>正常</td>";
                                } else if (list[2].totHydcAlm == null) {
                                    Almret = "<td style='color:#ebdc5b'>阈值未设置</td>";
                                }
                                html1 += "<div id='" + tableid + "' style='width:550px; float:left; margin:10px; padding:10px; border: 1px solid #ccc;background:#f8f8f8;'>";
                                html1 += "<div style='font-family:Microsoft YaHei;font-size:15;float:left;background:#9CF;width:510px;padding:20px;'>" + list[0].deviceName + "</div>";
                                html1 += "<table class='Detailtb' style='margin:auto;font-size:16px;width:100%;'>";
                                html1 += "<tr style='background-color:transparent'><th>监测量名称</th><th>检测量值</th><th> 状    态 </th><th>采集时间</th></tr>";
                                html1 += "<tr><td>甲烷(ppm)</td><td style='color:#c0e76f;'>" + list[1].CH4ppm + "</td><td>--</td><td>" + list[0].CH4ppm + "</td></tr>";
//            	    		html1 +="<tr><th>监测量名称</th><th>检测量值</th><th>采集时间</th></tr>";
//            	    		html1 +="<tr><td>甲烷(ppm)</td><td>"+list[1].CH4ppm+"</td><td>"+list[0].CH4ppm+"</td></tr>";
                                html1 += "<tr><td>乙烯(ppm)</td><td style='color:#c0e76f;'>" + list[1].c2H4ppm + "</td><td>--</td><td>" + list[0].c2H4ppm + "</td></tr>";
                                html1 += "<tr><td>乙炔(ppm)</td><td style='color:#c0e76f;'>" + list[1].c2H2ppm + "</td>" + Almret + "<td>" + list[0].c2H2ppm + "</td></tr>";
                                html1 += "<tr><td>氢气(ppm)</td><td style='color:#c0e76f;'>" + list[1].h2ppm + "</td><td>" + H2Almret + "</td><td>" + list[0].h2ppm + "</td></tr>";
                                html1 += "<tr><td>一氧化碳(ppm)</td><td style='color:#c0e76f;'>" + list[1].COppm + "</td><td>--</td><td>" + list[0].COppm + "</td></tr>";
                                html1 += "<tr><td>二氧化碳(ppm)</td><td style='color:#c0e76f;'>" + list[1].CO2ppm + "</td><td>--</td><td>" + list[0].CO2ppm + "</td></tr>";
                                html1 += "<tr><td>总烃(ppm)</td><td style='color:#c0e76f;'>" + list[1].totHyd + "</td><td>" + TAlmret + "</td><td>" + list[0].totHyd + "</td></tr>";
                                html1 += "<tr><td>油位(ppm)</td><td style='color:#c0e76f;'>" + list[1].oilTmp + "</td><td>--</td><td>" + list[0].oilTmp + "</td></tr>";
                                html1 += "<tr><td>油色谱微水(ppm)</td><td style='color:#c0e76f;'>" + list[1].mst + "</td><td>--</td><td>" + list[0].mst + "</td></tr>";
                                html1 += "<tr><td>状态</td>" + ret + "<td></td><td></td></tr>";
                                html1 += "</table>";
                                html1 += "</div>";
                                $("#showdata_div").append(html1);
                            }
                        }
                    }
                });
            }
            if (type == "2") {
                $.ajax({
                    async: false,
                    cache: false,
                    type: 'POST',
                    url: ctxPath + '/deviceHealthState/getSf6DetailList.do?_off=' + _off + "&_time=" + _time,
                    error: function () {// 请求失败处理函数
                        jeecg.alert('错误信息', '数据请求失败', 'error');
                    },
                    success: function (data) {
                        var tableids = [];
                        var myDate = new Date();
                        myDate = myDate.toLocaleDateString();
                        myDate = (new Date(myDate)).getTime();
                        for (var n = 0; n < data.length; n++) {
                            if ((n % 3 == 0) || n == 0) {
                                var html1 = "";
                                var list = [];
                                list.push(data[n]);
                                list.push(data[n + 1]);
                                list.push(data[n + 2]);
                                var tableid = "data_list_" + n;
                                tableids.push(tableid);
                                if (list[0].tmp != null) {
                                    var starttime = list[0].tmp.split(" ")[0];
                                    var ret = returntime(starttime, myDate);
                                } else {
                                    var ret = "<td style='color:#ea5f5f'>无实时数据</td>";
                                }
                                var PAlmret;
                                if (list[2].pres == 0) {
                                    PAlmret = "压力阈值超出";
                                } else if (list[2].pres == 1) {
                                    PAlmret = "正常";
                                } else if (list[2].pres == null) {
                                    PAlmret = "阈值未设置";
                                }
                                html1 += "<div id='" + tableid + "' style='width:550px; float:left; margin:10px; padding:10px; border: 1px solid #ccc;background:#f8f8f8;'>";
                                html1 += "<div style='font-family:Microsoft YaHei;font-size:15;float:left;background:#9CF;width:510px;padding:20px;'>" + list[0].deviceName + "</div>";
                                html1 += "<table class='Detailtb' style='margin:auto;font-size:16px;width:100%;'>";
                                html1 += "<tr style='background-color:transparent'><th>监测量名称</th><th>检测量值</th><th> 状    态 </th><th>采集时间</th></tr>";
                                html1 += "<tr><td>温度</td><td style='color:#c0e76f;'>" + list[1].tmp + "</td><td>--</td><td>" + list[0].tmp + "</td></tr>";
                                html1 += "<tr><td>压力</td><td style='color:#c0e76f;'>" + list[1].pres + "</td><td>" + PAlmret + "</td><td>" + list[0].pres + "</td></tr>";
                                html1 += "<tr><td>湿度</td><td style='color:#c0e76f;'>" + list[1].hum + "</td><td>--</td><td>" + list[0].hum + "</td></tr>";
                                html1 += "<tr><td>状态</td>" + ret + "<td></td><td></td></tr>";
                                html1 += "</table>";
                                html1 += "</div>";
                                $("#showdata_div").append(html1);
                            }
                        }
                    }
                });
            }
            if (type == "3") {
                $.ajax({
                    async: false,
                    cache: false,
                    type: 'POST',
                    url: ctxPath + '/deviceHealthState/getSmoamDetailList.do?_off=' + _off + "&_time=" + _time,
                    error: function () {// 请求失败处理函数
                        jeecg.alert('错误信息', '数据请求失败', 'error');
                    },
                    success: function (data) {
                        var tableids = [];
                        var myDate = new Date();
                        myDate = myDate.toLocaleDateString();
                        myDate = (new Date(myDate)).getTime();
                        for (var n = 0; n < data.length; n++) {
                            if ((n % 3 == 0) || n == 0) {
                                var html1 = "";
                                var list = [];
                                list.push(data[n]);
                                list.push(data[n + 1]);
                                list.push(data[n + 2]);
                                var tableid = "data_list_" + n;
                                tableids.push(tableid);
                                if (list[0].totA != null) {
                                    var starttime = list[0].totA.split(" ")[0];
                                    var ret = returntime(starttime, myDate);
                                } else {
                                    var ret = "<td style='color:#ea5f5f'>无实时数据</td>";
                                }
                                var TOAlmret;
                                if (list[2].totA == 0) {
                                    TOAlmret = "全电流阈值超出";
                                } else if (list[2].totA == 1) {
                                    TOAlmret = "正常";
                                } else if (list[2].totA == null) {
                                    TOAlmret = "阈值未设置";
                                }
                                html1 += "<div id='" + tableid + "' style='width:550px; float:left; margin:10px; padding:10px; border: 1px solid #ccc;background:#f8f8f8;'>";
                                html1 += "<div style='font-family:Microsoft YaHei;font-size:15;float:left;background:#9CF;width:510px;padding:20px;'>" + list[0].deviceName + "</div>";
                                html1 += "<table class='Detailtb' style='margin:auto;font-size:16px;width:100%;'>";
                                html1 += "<tr style='background-color:transparent'><th>监测量名称</th><th>检测量值</th><th> 状    态 </th><th>采集时间</th></tr>";
                                html1 += "<tr><td>全电流</td><td style='color:#c0e76f;'>" + list[1].totA + "</td><td>" + TOAlmret + "</td><td>" + list[0].totA + "</td></tr>";
                                html1 += "<tr><td>阻性电流</td><td style='color:#c0e76f;'>" + list[1].risA + "</td><td>--</td><td>" + list[0].risA + "</td></tr>";
                                html1 += "<tr><td>阻容比</td><td style='color:#c0e76f;'>" + list[1].risCaRte + "</td><td>--</td><td>" + list[0].risCaRte + "</td></tr>";
                                html1 += "<tr><td>最近落雷时间</td><td style='color:#c0e76f;'>" + list[1].lastLigTm + "</td><td>--</td><td>" + list[0].lastLigTm + "</td></tr>";
                                html1 += "<tr><td>落雷次数</td><td style='color:#c0e76f;'>" + list[1].ligCnt + "</td><td>--</td><td>" + list[0].ligCnt + "</td></tr>";
                                html1 += "<tr><td>状态</td>" + ret + "<td></td><td></td></tr>";
                                html1 += "</table>";
                                html1 += "</div>";
                                $("#showdata_div").append(html1);
                            }
                        }
                    }
                });
            }
            if (type == "4") {
                $.ajax({
                    async: false,
                    cache: false,
                    type: 'POST',
                    url: ctxPath + '/deviceHealthState/getScomDetailList.do?_off=' + _off + "&_time=" + _time,
                    error: function () {// 请求失败处理函数
                        jeecg.alert('错误信息', '数据请求失败', 'error');
                    },
                    success: function (data) {
                        var tableids = [];
                        var myDate = new Date();
                        myDate = myDate.toLocaleDateString();
                        myDate = (new Date(myDate)).getTime();
                        for (var n = 0; n < data.length; n++) {
                            if ((n % 3 == 0) || n == 0) {
                                var html1 = "";
                                var list = [];
                                list.push(data[n]);
                                list.push(data[n + 1]);
                                list.push(data[n + 2]);
                                var tableid = "data_list_" + n;
                                tableids.push(tableid);

                                if (list[0].CGAmp != null) {
                                    var starttime = list[0].CGAmp.split(" ")[0];
                                    var ret = returntime(starttime, myDate);
                                } else {
                                    var ret = "<td style='color:#ea5f5f'>无实时数据</td>";
                                }
                                var CAlmret;
                                if (list[2].CGAmp == 0) {
                                    CAlmret = "泄露电流阈值超出";
                                } else if (list[2].CGAmp == 1) {
                                    CAlmret = "正常";
                                } else if (list[2].CGAmp == null) {
                                    CAlmret = "阈值未设置";
                                }
                                html1 += "<div id='" + tableid + "' style='width:550px; float:left; margin:10px; padding:10px; border: 1px solid #ccc;background:#f8f8f8;'>";
                                html1 += "<div style='font-family:Microsoft YaHei;font-size:15;float:left;background:#9CF;width:510px;padding:20px;'>" + list[0].deviceName + "</div>";
                                html1 += "<table class='Detailtb' style='margin:auto;font-size:16px;width:100%;'>";
                                html1 += "<tr style='background-color:transparent'><th>监测量名称</th><th>检测量值</th><th>采集时间</th></tr>";
                                html1 += "<tr><td>泄露电流</td><td style='color:#c0e76f;'>" + list[1].CGAmp + "</td><td>" + CAlmret + "</td><td>" + list[0].CGAmp + "</td></tr>";
                                html1 += "<tr><td>夹件电流</td><td style='color:#c0e76f;'>" + list[1].amp1 + "</td><td>--</td><td>" + list[0].amp1 + "</td></tr>";
                                html1 += "<tr><td>本体主油箱磁力式油位</td><td style='color:#c0e76f;'>" + list[1].mainOil + "</td><td>--</td><td>" + list[0].mainOil + "</td></tr>";
                                html1 += "<tr><td>本体主油箱压力式油位</td><td style='color:#c0e76f;'>" + list[1].preOil + "</td><td>--</td><td>" + list[0].preOil + "</td></tr>";
                                html1 += "<tr><td>有载开关油位</td><td style='color:#c0e76f;'>" + list[1].sltcOil + "</td><td>--</td><td>" + list[0].sltcOil + "</td></tr>";
                                html1 += "<tr><td>网侧绕组温度</td><td style='color:#c0e76f;'>" + list[1].tmp1 + "</td><td>--</td><td>" + list[0].tmp1 + "</td></tr>";
                                html1 += "<tr><td>油面温度</td><td style='color:#c0e76f;'>" + list[1].tmp2 + "</td><td>--</td><td>" + list[0].tmp2 + "</td></tr>";
                                html1 += "<tr><td>顶层油温1</td><td style='color:#c0e76f;'>" + list[1].tmp4 + "</td><td>--</td><td>" + list[0].tmp4 + "</td></tr>";
                                html1 += "<tr><td>顶层油温2</td><td style='color:#c0e76f;'>" + list[1].tmp5 + "</td><td>--</td><td>" + list[0].tmp5 + "</td></tr>";
                                html1 += "<tr><td>底层油温1</td><td style='color:#c0e76f;'>" + list[1].tmp6 + "</td><td>--</td><td>" + list[0].tmp6 + "</td></tr>";
                                html1 += "<tr><td>底层油温2</td><td style='color:#c0e76f;'>" + list[1].tmp7 + "</td><td>--</td><td>" + list[0].tmp7 + "</td></tr>";
                                html1 += "<tr><td>环境温度</td><td style='color:#c0e76f;'>" + list[1].Tmp8 + "</td><td>--</td><td>" + list[0].tmp8 + "</td></tr>";
                                html1 += "<tr><td>阀侧首端套管SF6压力</td><td style='color:#c0e76f;'>" + list[1].pre1 + "</td><td>--</td><td>" + list[0].pre1 + "</td></tr>";
                                html1 += "<tr><td>阀侧末端套管SF6压力</td><td style='color:#c0e76f;'>" + list[1].pre2 + "</td><td>--</td><td>" + list[0].pre2 + "</td></tr>";
                                html1 += "<tr><td>状态</td>" + ret + "<td></td><td></td></tr>";
                                html1 += "</table>";
                                html1 += "</div>";
                                $("#showdata_div").append(html1);
                            }
                        }
                    }
                });
            }
            if (type == "19") {
                $.ajax({
                    async: false,
                    cache: false,
                    type: 'POST',
                    url: ctxPath + '/deviceHealthState/getSpdmDetailList.do?_off=' + _off + "&_time=" + _time,
                    error: function () {// 请求失败处理函数
                        jeecg.alert('错误信息', '数据请求失败', 'error');
                    },
                    success: function (data) {
                        alert(data);
                        var tableids = [];
                        var myDate = new Date();
                        myDate = myDate.toLocaleDateString();
                        myDate = (new Date(myDate)).getTime();
                        for (var n = 0; n < data.length; n++) {
                            if ((n % 3 == 0) || n == 0) {
                                var html1 = "";
                                var list = [];
                                list.push(data[n]);
                                list.push(data[n + 1]);
                                list.push(data[n + 2]);
                                var tableid = "data_list_" + n;
                                tableids.push(tableid);

                                if (list[0].acuPaDsch != null) {
                                    var starttime = list[0].acuPaDsch.split(" ")[0];
                                    var ret = returntime(starttime, myDate);
                                } else {
                                    var ret = "<td style='color:#ea5f5f'>无实时数据</td>";
                                }
                                var PadAlmret;
                                if (list[2].acuPaDsch == 0) {
                                    PadAlmret = "局放声学水平阈值超出";
                                } else if (list[2].acuPaDsch == 1) {
                                    PadAlmret = "正常";
                                } else if (list[2].acuPaDsch == null) {
                                    PadAlmret = "阈值未设置";
                                }
                                html1 += "<div id='" + tableid + "' style='width:550px; float:left; margin:10px; padding:10px; border: 1px solid #ccc;background:#f8f8f8;'>";
                                html1 += "<div style='font-family:Microsoft YaHei;font-size:15;float:left;background:#9CF;width:510px;padding:20px;'>" + list[0].deviceName + "</div>";
                                html1 += "<table class='Detailtb' style='margin:auto;font-size:16px;width:100%;'>";
                                html1 += "<tr style='background-color:transparent'><th>监测量名称</th><th>检测量值</th><th>采集时间</th></tr>";
                                html1 += "<tr><td>局放声学水平</td><td style='color:#c0e76f;'>" + list[1].acuPaDsch + "</td><td>" + PadAlmret + "</td><td>" + list[0].acuPaDsch + "</td></tr>";
                                html1 += "<tr><td>局放UHF水平</td><td style='color:#c0e76f;'>" + list[1].uhfPaDsch + "</td><td>--</td><td>" + list[0].uhfPaDsch + "</td></tr>";
                                html1 += "<tr><td>放电相位</td><td style='color:#c0e76f;'>" + list[1].phase + "</td><td>--</td><td>" + list[0].phase + "</td></tr>";
                                html1 += "<tr><td>脉冲次数</td><td style='color:#c0e76f;'>" + list[1].plsNum + "</td><td>--</td><td>" + list[0].plsNum + "</td></tr>";
                                html1 += "<tr><td>状态</td>" + ret + "<td></td><td></td></tr>";
                                html1 += "</table>";
                                html1 += "</div>";
                                $("#showdata_div").append(html1);
                            }
                        }
                    }
                });
            }
            if (type == "hwcw") {
                $.ajax({
                    async: false,
                    cache: false,
                    type: 'POST',
                    url: ctxPath + '/deviceHealthState/getSf6DetailList.do',
                    error: function () {// 请求失败处理函数
                        jeecg.alert('错误信息', '数据请求失败', 'error');
                    },
                    success: function (data) {
                        var tableids = [];
                        var myDate = new Date();
                        myDate = myDate.toLocaleDateString();
                        myDate = (new Date(myDate)).getTime();
                        for (var n = 0; n < data.length; n++) {
                            if ((n % 2 == 0) || n == 0) {
                                var html1 = "";
                                var list = [];
                                list.push(data[n]);
                                list.push(data[n + 1]);
                                var tableid = "data_list_" + n;
                                tableids.push(tableid);
                                if (list[0].tmp != null) {
                                    var starttime = list[0].tmp.split(" ")[0];
                                    var ret = returntime(starttime, myDate);
                                } else {
                                    var ret = "<td style='color:#ea5f5f'>无实时数据</td>";
                                }
                                html1 += "<div id='" + tableid + "' style='width:550px; float:left; margin:10px;padding:10px; border: 1px solid #ccc;background:#f8f8f8;'>";
                                html1 += "<div style='font-family:Microsoft YaHei;font-size:15;float:left;background:#9CF;width:510px;padding:20px;'>" + list[0].deviceName + "</div>";
                                html1 += "<table class='Detailtb' style='margin:auto;font-size:16px;width:100%;'>";
                                html1 += "<tr style='background-color:transparent'><th>监测量名称</th><th>检测量值</th><th>采集时间</th></tr>";
                                html1 += "<tr><td>温度</td><td style='color:#c0e76f;'>" + list[1].tmp + "</td><td>" + list[0].tmp + "</td></tr>";
                                html1 += "<tr><td>状态</td>" + ret + "<td></td><td></td></tr>";
                                html1 += "</table>";
                                html1 += "</div>";
                                $("#showdata_div").append(html1);
                            }
                        }
                    }
                });
            }
        }
    };
    return _this;
}();

//监测是否超时
function returntime(starttime, myDate) {

    starttime = starttime.replace(new RegExp("-", "gm"), "/");
    starttime = (new Date(starttime)).getTime(); //得到毫秒数
    if (starttime < myDate) {
        return "<td style='color:#ea5f5f'>超时</td>";
    } else {
        return "<td style='color:#c0e76f'>正常</td>";
    }
}

function getDeviceBySpace(space) {
    var type = $("#txtType").val();
    if (type == "1") {
        $.ajax({
            async: false,
            cache: false,
            type: 'POST',
            url: ctxPath + '/deviceHealthState/getStomDetailListBySpace.do?space=' + space,
            error: function () {// 请求失败处理函数
                jeecg.alert('错误信息', '数据请求失败', 'error');
            },
            success: function (data) {
                var tableids = [];
                var myDate = new Date();
                myDate = myDate.toLocaleDateString();
                myDate = (new Date(myDate)).getTime();
                for (var n = 0; n < data.length; n++) {
                    if ((n % 3 == 0) || n == 0) {
                        var html1 = "";

                        var list = [];
                        list.push(data[n]);
                        list.push(data[n + 1]);
                        list.push(data[n + 2]);
                        var tableid = "data_list_" + n;
                        tableids.push(tableid);
                        if (list[0].CH4ppm != null) {
                            var starttime = list[0].CH4ppm.split(" ")[0];
                            var ret = returntime(starttime, myDate);
                        } else {
                            var ret = "<td style='color:#ea5f5f'>无实时数据</td>";
                        }
                        var Almret;
                        if (list[2].c2H2Alm == 0) {
                            Almret = "乙炔阈值超出";
                        } else if (list[2].c2H2Alm == 1) {
                            Almret = "正常";
                        } else if (list[2].c2H2Alm == null) {
                            Almret = "阈值未设置";
                        }
                        var H2Almret;
                        if (list[2].h2Alm == 0) {
                            H2Almret = "氢气阈值超出";
                        } else if (list[2].h2Alm == 1) {
                            H2Almret = "正常";
                        } else if (list[2].h2Alm == null) {
                            H2Almret = "阈值未设置";
                        }
                        var TAlmret;
                        if (list[2].totHydcAlm == 0) {
                            TAlmret = "总烃阈值超出";
                        } else if (list[2].totHydcAlm == 1) {
                            TAlmret = "正常";
                        } else if (list[2].totHydcAlm == null) {
                            TAlmret = "阈值未设置";
                        }
                        html1 += "<div id='" + tableid + "' style='width:550px; float:left; margin:10px; padding:10px; border: 1px solid #ccc;background:#f8f8f8;'>";
                        html1 += "<div style='font-family:Microsoft YaHei;font-size:15;float:left;background:#9CF;width:510px;padding:20px;'>" + list[0].deviceName + "</div>";
                        html1 += "<table class='Detailtb' style='margin:auto;font-size:16px;width:100%;'>";
                        html1 += "<tr style='background-color:transparent'><th>监测量名称</th><th>检测量值</th><th> 状    态 </th><th>采集时间</th></tr>";
                        html1 += "<tr><td>甲烷(ppm)</td><td style='color:#c0e76f;'>" + list[1].CH4ppm + "</td><td>--</td><td>" + list[0].CH4ppm + "</td></tr>";
                        html1 += "<tr><td>乙烯(ppm)</td><td style='color:#c0e76f;'>" + list[1].c2H4ppm + "</td><td>--</td><td>" + list[0].c2H4ppm + "</td></tr>";
                        html1 += "<tr><td>乙炔(ppm)</td><td style='color:#c0e76f;'>" + list[1].c2H2ppm + "</td><td>" + Almret + "</td><td>" + list[0].c2H2ppm + "</td></tr>";
                        html1 += "<tr><td>氢气(ppm)</td><td style='color:#c0e76f;'>" + list[1].h2ppm + "</td><td>" + H2Almret + "</td><td>" + list[0].h2ppm + "</td></tr>";
                        html1 += "<tr><td>一氧化碳(ppm)</td><td style='color:#c0e76f;'>" + list[1].COppm + "</td><td>--</td><td>" + list[0].COppm + "</td></tr>";
                        html1 += "<tr><td>二氧化碳(ppm)</td><td style='color:#c0e76f;'>" + list[1].CO2ppm + "</td><td>--</td><td>" + list[0].CO2ppm + "</td></tr>";
                        html1 += "<tr><td>总烃(ppm)</td><td style='color:#c0e76f;'>" + list[1].totHyd + "</td><td>" + TAlmret + "</td><td>" + list[0].totHyd + "</td></tr>";
                        html1 += "<tr><td>油位(ppm)</td><td style='color:#c0e76f;'>" + list[1].oilTmp + "</td><td>--</td><td>" + list[0].oilTmp + "</td></tr>";
                        html1 += "<tr><td>油色谱微水(ppm)</td><td style='color:#c0e76f;'>" + list[1].mst + "</td><td>--</td><td>" + list[0].mst + "</td></tr>";
                        html1 += "<tr><td>状态</td>" + ret + "<td></td><td></td></tr>";
                        html1 += "</table>";
                        html1 += "</div>";
                        $("#showdata_div").append(html1);
                    }
                }
            }
        });
    }
    if (type == "2") {
        $.ajax({
            async: false,
            cache: false,
            type: 'POST',
            url: ctxPath + '/deviceHealthState/getSf6DetailListBySpace.do?space=' + space,
            error: function () {// 请求失败处理函数
                jeecg.alert('错误信息', '数据请求失败', 'error');
            },
            success: function (data) {
                var tableids = [];
                var myDate = new Date();
                myDate = myDate.toLocaleDateString();
                myDate = (new Date(myDate)).getTime();
                for (var n = 0; n < data.length; n++) {
                    if ((n % 3 == 0) || n == 0) {
                        var html1 = "";
                        var list = [];
                        list.push(data[n]);
                        list.push(data[n + 1]);
                        list.push(data[n + 2]);
                        var tableid = "data_list_" + n;
                        tableids.push(tableid);
                        if (list[0].tmp != null) {
                            var starttime = list[0].tmp.split(" ")[0];
                            var ret = returntime(starttime, myDate);
                        } else {
                            var ret = "<td style='color:#ea5f5f'>无实时数据</td>";
                        }
                        var PAlmret;
                        if (list[2].pres == 0) {
                            PAlmret = "压力阈值超出";
                        } else if (list[2].pres == 1) {
                            PAlmret = "正常";
                        } else if (list[2].pres == null) {
                            PAlmret = "阈值未设置";
                        }
                        html1 += "<div id='" + tableid + "' style='width:550px; float:left; margin:10px; padding:10px; border: 1px solid #ccc;background:#f8f8f8;'>";
                        html1 += "<div style='font-family:Microsoft YaHei;font-size:15;float:left;background:#9CF;width:510px;padding:20px;'>" + list[0].deviceName + "</div>";
                        html1 += "<table class='Detailtb' style='margin:auto;font-size:16px;width:100%;'>";
                        html1 += "<tr style='background-color:transparent'><th>监测量名称</th><th>检测量值</th><th> 状    态 </th><th>采集时间</th></tr>";
                        html1 += "<tr><td>温度</td><td style='color:#c0e76f;'>" + list[1].tmp + "</td><td>--</td><td>" + list[0].tmp + "</td></tr>";
                        html1 += "<tr><td>压力</td><td style='color:#c0e76f;'>" + list[1].pres + "</td><td>" + PAlmret + "</td><td>" + list[0].pres + "</td></tr>";
                        html1 += "<tr><td>湿度</td><td style='color:#c0e76f;'>" + list[1].hum + "</td><td>--</td><td>" + list[0].hum + "</td></tr>";
                        html1 += "<tr><td>状态</td>" + ret + "<td></td><td></td></tr>";
                        html1 += "</table>";
                        html1 += "</div>";
                        $("#showdata_div").append(html1);
                    }
                }
            }
        });
    }
    if (type == "3") {
        $.ajax({
            async: false,
            cache: false,
            type: 'POST',
            url: ctxPath + '/deviceHealthState/getSmoamDetailListBySpace.do?space=' + space,
            error: function () {// 请求失败处理函数
                jeecg.alert('错误信息', '数据请求失败', 'error');
            },
            success: function (data) {
                var tableids = [];
                var myDate = new Date();
                myDate = myDate.toLocaleDateString();
                myDate = (new Date(myDate)).getTime();
                for (var n = 0; n < data.length; n++) {
                    if ((n % 3 == 0) || n == 0) {
                        var html1 = "";
                        var list = [];
                        list.push(data[n]);
                        list.push(data[n + 1]);
                        list.push(data[n + 2]);
                        var tableid = "data_list_" + n;
                        tableids.push(tableid);
                        if (list[0].totA != null) {
                            var starttime = list[0].totA.split(" ")[0];
                            var ret = returntime(starttime, myDate);
                        } else {
                            var ret = "<td style='color:#ea5f5f'>无实时数据</td>";
                        }
                        var TOAlmret;
                        if (list[2].totA == 0) {
                            TOAlmret = "全电流阈值超出";
                        } else if (list[2].totA == 1) {
                            TOAlmret = "正常";
                        } else if (list[2].totA == null) {
                            TOAlmret = "阈值未设置";
                        }
                        html1 += "<div id='" + tableid + "' style='width:550px; float:left; margin:10px; padding:10px; border: 1px solid #ccc;background:#f8f8f8;'>";
                        html1 += "<div style='font-family:Microsoft YaHei;font-size:15;float:left;background:#9CF;width:510px;padding:20px;'>" + list[0].deviceName + "</div>";
                        html1 += "<table class='Detailtb' style='margin:auto;font-size:16px;width:100%;'>";
                        html1 += "<tr style='background-color:transparent'><th>监测量名称</th><th>检测量值</th><th> 状    态 </th><th>采集时间</th></tr>";
                        html1 += "<tr><td>全电流</td><td style='color:#c0e76f;'>" + list[1].totA + "</td><td>" + TOAlmret + "</td><td>" + list[0].totA + "</td></tr>";
                        html1 += "<tr><td>阻性电流</td><td style='color:#c0e76f;'>" + list[1].risA + "</td><td>--</td><td>" + list[0].risA + "</td></tr>";
                        html1 += "<tr><td>阻容比</td><td style='color:#c0e76f;'>" + list[1].risCaRte + "</td><td>--</td><td>" + list[0].risCaRte + "</td></tr>";
                        html1 += "<tr><td>最近落雷时间</td><td style='color:#c0e76f;'>" + list[1].lastLigTm + "</td><td>--</td><td>" + list[0].lastLigTm + "</td></tr>";
                        html1 += "<tr><td>落雷次数</td><td style='color:#c0e76f;'>" + list[1].ligCnt + "</td><td>--</td><td>" + list[0].ligCnt + "</td></tr>";
                        html1 += "<tr><td>状态</td>" + ret + "<td></td><td></td></tr>";
                        html1 += "</table>";
                        html1 += "</div>";
                        $("#showdata_div").append(html1);
                    }
                }
            }
        });
    }
    if (type == "4") {
        $.ajax({
            async: false,
            cache: false,
            type: 'POST',
            url: ctxPath + '/deviceHealthState/getScomDetailListBySpace.do?space=' + space,
            error: function () {// 请求失败处理函数
                jeecg.alert('错误信息', '数据请求失败', 'error');
            },
            success: function (data) {
                var tableids = [];
                var myDate = new Date();
                myDate = myDate.toLocaleDateString();
                myDate = (new Date(myDate)).getTime();
                for (var n = 0; n < data.length; n++) {
                    if ((n % 3 == 0) || n == 0) {
                        var html1 = "";
                        var list = [];
                        list.push(data[n]);
                        list.push(data[n + 1]);
                        list.push(data[n + 2]);
                        var tableid = "data_list_" + n;
                        tableids.push(tableid);
                        if (list[0].CGAmp != null) {
                            var starttime = list[0].CGAmp.split(" ")[0];
                            var ret = returntime(starttime, myDate);
                        } else {
                            var ret = "<td style='color:#ea5f5f'>无实时数据</td>";
                        }
                        var CAlmret;
                        if (list[2].CGAmp == 0) {
                            CAlmret = "泄露电流阈值超出";
                        } else if (list[2].CGAmp == 1) {
                            CAlmret = "正常";
                        } else if (list[2].CGAmp == null) {
                            CAlmret = "阈值未设置";
                        }
                        html1 += "<div id='" + tableid + "' style='width:550px; float:left; margin:10px; padding:10px; border: 1px solid #ccc;background:#f8f8f8;'>";
                        html1 += "<div style='font-family:Microsoft YaHei;font-size:15;float:left;background:#9CF;width:510px;padding:20px;'>" + list[0].deviceName + "</div>";
                        html1 += "<table class='Detailtb' style='margin:auto;font-size:16px;width:100%;'>";
                        html1 += "<tr style='background-color:transparent'><th>监测量名称</th><th>检测量值</th><th>采集时间</th></tr>";
                        html1 += "<tr><td>泄露电流</td><td style='color:#c0e76f;'>" + list[1].CGAmp + "</td><td>" + CAlmret + "</td><td>" + list[0].CGAmp + "</td></tr>";
                        html1 += "<tr><td>状态</td>" + ret + "<td></td><td></td></tr>";
                        html1 += "</table>";
                        html1 += "</div>";
                        $("#showdata_div").append(html1);
                    }
                }
            }
        });
    }
    if (type == "19") {
        $.ajax({
            async: false,
            cache: false,
            type: 'POST',
            url: ctxPath + '/deviceHealthState/getSpdmDetailListBySpace.do?space=' + space,
            error: function () {// 请求失败处理函数
                jeecg.alert('错误信息', '数据请求失败', 'error');
            },
            success: function (data) {
                var tableids = [];
                var myDate = new Date();
                myDate = myDate.toLocaleDateString();
                myDate = (new Date(myDate)).getTime();
                for (var n = 0; n < data.length; n++) {
                    if ((n % 3 == 0) || n == 0) {
                        var html1 = "";
                        var list = [];
                        list.push(data[n]);
                        list.push(data[n + 1]);
                        list.push(data[n + 2]);
                        var tableid = "data_list_" + n;
                        tableids.push(tableid);
                        if (list[0].acuPaDsch != null) {
                            var starttime = list[0].acuPaDsch.split(" ")[0];
                            var ret = returntime(starttime, myDate);
                        } else {
                            var ret = "<td style='color:#ea5f5f'>无实时数据</td>";
                        }
                        var PadAlmret;
                        if (list[2].acuPaDsch == 0) {
                            PadAlmret = "局放声学水平阈值超出";
                        } else if (list[2].acuPaDsch == 1) {
                            PadAlmret = "正常";
                        } else if (list[2].acuPaDsch == null) {
                            PadAlmret = "阈值未设置";
                        }
                        html1 += "<div id='" + tableid + "' style='width:550px; float:left; margin:10px; padding:10px; border: 1px solid #ccc;background:#f8f8f8;'>";
                        html1 += "<div style='font-family:Microsoft YaHei;font-size:15;float:left;background:#9CF;width:510px;padding:20px;'>" + list[0].deviceName + "</div>";
                        html1 += "<table class='Detailtb' style='margin:auto;font-size:16px;width:100%;'>";
                        html1 += "<tr style='background-color:transparent'><th>监测量名称</th><th>检测量值</th><th>采集时间</th></tr>";
                        html1 += "<tr><td>局放声学水平</td><td style='color:#c0e76f;'>" + list[1].acuPaDsch + "</td><td>" + PadAlmret + "</td><td>" + list[0].acuPaDsch + "</td></tr>";
                        html1 += "<tr><td>局放UHF水平</td><td style='color:#c0e76f;'>" + list[1].uhfPaDsch + "</td><td>--</td><td>" + list[0].uhfPaDsch + "</td></tr>";
                        html1 += "<tr><td>放电相位</td><td style='color:#c0e76f;'>" + list[1].phase + "</td><td>--</td><td>" + list[0].phase + "</td></tr>";
                        html1 += "<tr><td>脉冲次数</td><td>" + list[1].plsNum + "</td><td>--</td><td>" + list[0].plsNum + "</td></tr>";
                        html1 += "<tr><td>状态</td>" + ret + "<td></td><td></td></tr>";
                        html1 += "</table>";
                        html1 += "</div>";
                        $("#showdata_div").append(html1);
                    }
                }
            }
        });
    }
}

function export_Word() {
    var type = $("#txtType").val();
    if (type == "1") {
        $.ajax({
            async: false,
            cache: false,
            type: 'POST',
            url: ctxPath + '/deviceHealthState/getStomDetailListExportWord.do',
            success: function (data) {
                var head_data, json;
                head_data = [];
                head_data.push({"title": "甲烷", "value": "CH4ppm"},
                    {"title": "乙烯", "value": "c2H4ppm"}, {"title": "乙炔", "value": "c2H2ppm"},
                    {"title": "乙炔阈值状态", "value": "c2H2Alm"}, {"title": "氢气", "value": "h2ppm"}, {
                        "title": "氢气阈值状态",
                        "value": "h2Alm"
                    },
                    {"title": "一氧化碳", "value": "COppm"}, {"title": "二氧化碳", "value": "CO2ppm"}, {
                        "title": "总烃",
                        "value": "totHyd"
                    },
                    {"title": "总烃阈值状态", "value": "totHydcAlm"}, {"title": "油位", "value": "oilTmp"}, {
                        "title": "油色谱微水",
                        "value": "mst"
                    },
                    {"title": "状态", "value": "type"});
                tableExport_test(data, head_data, "doc", "油色谱及微水设备报表");

            }
        });
    }
    if (type == "2") {
        $.ajax({
            async: false,
            cache: false,
            type: 'POST',
            url: ctxPath + '/deviceHealthState/getSf6DetailListExportWord.do',
            success: function (data) {
                var head_data, json;
                head_data = [];
                head_data.push(
                    {"title": "温度", "value": "tmp"}, {"title": "压力", "value": "pres"},
                    {"title": "压力阈值状态", "value": "denAlm"}, {"title": "湿度", "value": "hum"},
                    {"title": "状态", "value": "type"});
                tableExport_test(data, head_data, "doc", "SF6气体压力设备报表");
            }
        });
    }
    if (type == "3") {
        $.ajax({
            async: false,
            cache: false,
            type: 'POST',
            url: ctxPath + '/deviceHealthState/getSmoamDetailListExportWord.do',
            success: function (data) {
                var head_data, json;
                head_data = [];
                head_data.push(
                    {"title": "全电流", "value": "totA"}, {"title": "全电流阈值状态", "value": "totAAlm"}, {
                        "title": "阻性电流",
                        "value": "risA"
                    },
                    {"title": "阻容比", "value": "risCaRte"}, {"title": "最近落雷时间", "value": "lastLigTm"}, {
                        "title": "落雷次数",
                        "value": "ligCnt"
                    },
                    {"title": "状态", "value": "type"});
                tableExport(data, head_data, "doc", "避雷器及动作次数设备报表");
            }
        });
    }
    if (type == "4") {
        $.ajax({
            async: false,
            cache: false,
            type: 'POST',
            url: ctxPath + '/deviceHealthState/getScomDetailListExportWord.do',
            success: function (data) {
                var head_data, json;
                head_data = [];
                head_data.push(
                    {"title": "泄露电流", "value": "CGAmp"},
                    {"title": "泄露电流阈值状态", "value": "CGAlm"},
                    {"title": "状态", "value": "type"});
                tableExport(data, head_data, "doc", "避雷器及动作次数设备报表");
            }
        });
    }
    if (type == "19") {
        $.ajax({
            async: false,
            cache: false,
            type: 'POST',
            url: ctxPath + '/deviceHealthState/getSpdmDetailListExportWord.do',
            success: function (data) {
                var head_data, json;
                head_data = [];
                head_data.push(
                    {"title": "局放声学水平", "value": "acuPaDsch"}, {
                        "title": "局放声学水平阈值状态",
                        "value": "paDschAlm"
                    }, {"title": "局放UHF水平", "value": "uhfPaDsch"},
                    {"title": "放电相位", "value": "phase"}, {"title": "脉冲次数", "value": "plsNum"},
                    {"title": "状态", "value": "type"});
                tableExport(data, head_data, "doc", "铁芯泄露电流设备报表");
            }
        });
    }
}


//日历初始化
function myformatter(date) {
    var y = date.getFullYear();
    var m = date.getMonth() + 1;
    var d = date.getDate();
    return y + '-' + (m < 10 ? ('0' + m) : m) + '-' + (d < 10 ? ('0' + d) : d);
}

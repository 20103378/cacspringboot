$package('jeecg.deviceHealthState');
var cbint = 0;
$(function () {
    jeecg.deviceHealthState.setEditWin();//框体加载
    jeecg.deviceHealthState.showSelect();//下拉框加载
    // jeecg.deviceHealthState.showDevice();//设备展示
    // setInterval("jeecg.deviceHealthState.showDevice()", 5 * 60 * 1000);
    jeecg.deviceHealthState.comboxint();//设备类型

});
jeecg.deviceHealthState = function () {
    var _this = {
        setEditWin: function () {
            deviceWin = $('#ShowDevice-window').window({
                href: '',
                title: '实时数据',
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
        },
        comboxint: function () {
            if (cbint == 0) {
                cbint++;
            } else {
                $('#DeviceArea').combobox({
                    onChange: function () {
                        getSomeDevice();
                    }
                });
                $('#DeviceType').combobox({
                    onChange: function () {
                        getSomeDevice();
                    }
                });
            }
        },
        showSelect: function () {
            $.ajax({
                async: false,
                cache: false,
                type: 'POST',
                url: ctxPath + '/treeDevice/getSpaceName',
                success: function (data) {
                    console.log(data)
                    data.space.push({"text": "全部", "value": "0"});
                    $("#DeviceArea").combobox("loadData", data.space);
                    $("#DeviceArea").combobox('select', data.space[0].value);

                    data.deviceType.push({"text": "全部", "value": "0"});
                    $("#DeviceType").combobox("loadData", data.deviceType);
                    $("#DeviceType").combobox('select', data.deviceType[data.deviceType.length - 1].value);
                    getSomeDevice();
                    setInterval("getSomeDevice()", 5 * 60 * 1000);
                }
            });
        },
        showDevice: function () {
            $.ajax({
                async: false,
                cache: false,
                type: 'POST',
                url: ctxPath + '/treeDevice/getOtherImgList',//data
                success: function (data) {
                    otherData = data.entityList;
                }
            });
            $.ajax({
                async: false,
                cache: false,
                type: 'POST',
                url: ctxPath + '/treeDevice/getImgList',
                error: function () {// 请求失败处理函数
                    jeecg.alert('错误信息', '数据请求失败', 'error'); //device
                },
                success: function (data) {
                    showdata("showdata", data, otherData);
                }
            });
        }
    };
    return _this;
}();

// function linkToData(devId, name, type) {
//     var url = encodeURI(ctxPath + '/deviceHealthState/DeviceDetail?DeviceID=' + devId + '&DeviceType=' + type + '&DeviceName=' + name);
//     parent.jeecg.main.addTab(name, url);
// }


function showdata(id, data, otherData) {
    var ee = document.getElementById(id);
    var html1 = "";
    var ln = null;
    var group;
    var trClass = 0;
    var n = 0;//每一排的计数工具

    html1 += "<div class='ims' style='width:100%; height:100%; overflow:auto;'>";
    for (var i = 0, l = data.length; i < l; i++) {
        //alert(otherData[i].id);
        var ln_name = data[i].nodetype;
        if (ln == null) {
            ln = ln_name;
            if (ln == "1") {
                group = "油色谱及微水";
            } else if (ln == "2") {
                group = "SF6气体压力";
            } else if (ln == "3") {
                group = "避雷器及动作次数";
            } else if (ln == "4") {
                group = "铁芯泄露电流";
            } else if (ln == "5") {
                group = "换流变运行工况";
            } else if (ln == "6") {
                group = "SF6气体泄漏";
            } else if (ln == "7") {
                group = "套管";
            } else if (ln == "8") {
                group = "气象信息";
            } else if (ln == "19") {
                group = "局放";
            } else if (ln == "20") {
                group = "主变局放";
            } else if (ln == "hwcw") {
                group = "红外测温";
            } else if (ln == "amc") {
                group = "辅助设备";
            } else {
                group = "未定义";
            }
            trClass++;
            html1 = toHtml();
            n++;
        } else if (ln != ln_name) {
            ln = ln_name;
            if (ln == "1") {
                group = "油色谱及微水";
            } else if (ln == "2") {
                group = "SF6气体压力";
            } else if (ln == "3") {
                group = "避雷器及动作次数";
            } else if (ln == "4") {
                group = "铁芯泄露电流";
            } else if (ln == "5") {
                group = "换流变运行工况";
            } else if (ln == "6") {
                group = "SF6气体泄漏";
            } else if (ln == "7") {
                group = "套管";
            } else if (ln == "8") {
                group = "气象信息";
            } else if (ln == "19") {
                group = "局放";
            } else if (ln == "20") {
                group = "主变局放";
            } else if (ln == "hwcw") {
                group = "红外测温";
            } else if (ln == "amc") {
                group = "辅助设备";
            } else {
                group = "未定义";
            }
            trClass++;
            if (trClass % 2 == 1) {
                html1 += "</div>";
                html1 = toHtml();
            } else {
                html1 += "</div>";
                html1 = toHtml();
            }
            n++;
        }

        function toHtml() {
            html1 += "<div style='width:100%;overflow:hidden; background:#f8f8f8;'>";
            html1 += "<div style='float:left;width: 300px; text-align:left;margin-left:20px;overflow:hidden;'>";
            html1 += "<p style='float:left;font-family:Microsoft YaHei;font-size:20;font-weight:bold'>" + group + "</p>";
            if (group != "红外测温" && group != "辅助设备") {
                html1 += "<a href='javascript:void(0);' style='display: block; width: 70px; margin: 25px 180px;' class='easyui-linkbutton l-btn' iconcls='icon-search' onclick='toSpaceData(" + ln + ")' value='设备报表'>";
                html1 += "<span class='l-btn-left'><span class='l-btn-text icon-search l-btn-icon-left'>报表</span></span></a>";
            }
            html1 += "</div>";
            html1 += "<div style='float:right; text-align:left;margin-left:20px;overflow:hidden;'>";
            html1 += "<input id='input" + n + "' type = 'image' style='margin:20px' onClick='show(" + n + ");' src='../js/jquery-easyui-1.3.2/themes/default/images/combo_arrow.png'/>";
            html1 += "</div>";
            html1 += "</div>";
            return html1 += "<div id='div" + n + "' style='width:100%; overflow:hidden; background:#f8f8f8;'>";
        }


        var name = otherData[i].text;
        var name_title = Trim(name);
        var data_Current = JSON.stringify(otherData[i]);

        html1 += "<div id='" + otherData[i].id + "' class='tt-inner' style='margin:1px;background:#8acff0;float:left;width: 500px; height: 50px; text-align:center; overflow:hidden; border:1px' onclick='toCurrentData(" + data_Current + ")' >" +
            "<span style='width: 430px;float:left;font-family:Microsoft YaHei;font-size:15;font-weight:bold'>" + name + "</span>" +
            "<img id='tt' src=" + otherData[i].imgPath + " title=" + name_title + " style='float:right;'/>" +
            "</img>" +
            "</div>";
    }
    ee.innerHTML = html1;
    //移动鼠标变色操作
    $(".tt-inner").hover(function () {
        $(this).attr("bColor", $(this).css("background-color")).css("background", "#E0E0E0").css("cursor", "pointer");
    }, function () {
        $(this).css("background", $(this).attr("bColor"));
    });
}

var display_1 = 0;
var display_2 = 0;
var display_3 = 0;
var display_4 = 0;
var display_5 = 0;
var display_6 = 0;

function show(n) {
    if (n == 0) {
        if (display_1 == 0) {
            document.getElementById("div" + n).style.display = "none";
            document.getElementById("input" + n).src = "../js/jquery-easyui-1.3.2/themes/default/images/combo_arrow_up.png";
            display_1 = 1;
        } else if (display_1 == 1) {
            document.getElementById("div" + n).style.display = "";
            document.getElementById("input" + n).src = "../js/jquery-easyui-1.3.2/themes/default/images/combo_arrow.png";
            display_1 = 0;
        }
    }
    if (n == 1) {
        if (display_2 == 0) {
            document.getElementById("div" + n).style.display = "none";
            document.getElementById("input" + n).src = "../js/jquery-easyui-1.3.2/themes/default/images/combo_arrow_up.png";
            display_2 = 1;
        } else if (display_2 == 1) {
            document.getElementById("div" + n).style.display = "";
            document.getElementById("input" + n).src = "../js/jquery-easyui-1.3.2/themes/default/images/combo_arrow.png";
            display_2 = 0;
        }
    }
    if (n == 2) {
        if (display_3 == 0) {
            document.getElementById("div" + n).style.display = "none";
            document.getElementById("input" + n).src = "../js/jquery-easyui-1.3.2/themes/default/images/combo_arrow_up.png";
            display_3 = 1;
        } else if (display_3 == 1) {
            document.getElementById("div" + n).style.display = "";
            document.getElementById("input" + n).src = "../js/jquery-easyui-1.3.2/themes/default/images/combo_arrow.png";
            display_3 = 0;
        }
    }
    if (n == 3) {
        if (display_4 == 0) {
            document.getElementById("div" + n).style.display = "none";
            document.getElementById("input" + n).src = "../js/jquery-easyui-1.3.2/themes/default/images/combo_arrow_up.png";
            display_4 = 1;
        } else if (display_4 == 1) {
            document.getElementById("div" + n).style.display = "";
            document.getElementById("input" + n).src = "../js/jquery-easyui-1.3.2/themes/default/images/combo_arrow.png";
            display_4 = 0;
        }
    }
    if (n == 4) {
        if (display_5 == 0) {
            document.getElementById("div" + n).style.display = "none";
            document.getElementById("input" + n).src = "../js/jquery-easyui-1.3.2/themes/default/images/combo_arrow_up.png";
            display_5 = 1;
        } else if (display_5 == 1) {
            document.getElementById("div" + n).style.display = "";
            document.getElementById("input" + n).src = "../js/jquery-easyui-1.3.2/themes/default/images/combo_arrow.png";
            display_5 = 0;
        }
    }
    if (n == 5) {
        if (display_6 == 0) {
            document.getElementById("div" + n).style.display = "none";
            document.getElementById("input" + n).src = "../js/jquery-easyui-1.3.2/themes/default/images/combo_arrow_up.png";
            display_6 = 1;
        } else if (display_6 == 1) {
            document.getElementById("div" + n).style.display = "";
            document.getElementById("input" + n).src = "../js/jquery-easyui-1.3.2/themes/default/images/combo_arrow.png";
            display_6 = 0;
        }
    }
}

function getSomeDevice() {
    var DeviceArea = $("#DeviceArea").combo("getValue");
    var DeviceType = $("#DeviceType").combo("getValue");
    if (DeviceArea == '') {
        DeviceArea = "0";
    }
    if (DeviceType == '') {
        DeviceType = "0";
    }
//		var DeviceState=$("#DeviceState").combo("getValue");
    $.ajax({
        async: false,
        cache: false,
        type: 'POST',
        //通过选择获取其他信息(getOtherImgListBySelect)
        url: ctxPath + "/treeDevice/getOtherImgListBySelect?space=" + DeviceArea + "&deviceType=" + DeviceType,
        success: function (data) {
            otherData = data;
        }
    });
    $.ajax({
        async: false,
        cache: false,
        type: 'POST',
        //按选择获取即时消息(getImgListBySelect);
        url: ctxPath + "/treeDevice/getImgListBySelect?space=" + DeviceArea + "&deviceType=" + DeviceType,
        error: function () {// 请求失败处理函数
            jeecg.alert('错误信息', '数据请求失败', 'error');
        },
        success: function (data) {
            showdata("showdata", data, otherData);
        }
    });
}

function toCurrentData(data) {
    var nodetype = data.nodetype;
    var id = data.id;
    var nodetxt = data.text;
    if (id.indexOf('C') > -1) {
        id = id.substring(2);
        var nodetxt_DeviceDetail = nodetxt.replace("#", "@");
        var url = encodeURI(ctxPath + '/deviceHealthState/DeviceDetail?DeviceID=' + id + '&DeviceType=' + nodetype + '&DeviceName=' + nodetxt_DeviceDetail);

    } else {
        var url = encodeURI(ctxPath + '/deviceHealthState/DeviceDetail?DeviceID=' + id + '&DeviceType=' + nodetype + '&DeviceName=' + nodetxt);
    }
    parent.jeecg.main.addTab(nodetxt, url);
}

function toSpaceData(data) {
    if (data == "1") {
        nodetxt = "油色谱及微水";
    } else if (data == "2") {
        nodetxt = "SF6气体压力";
    } else if (data == "3") {
        nodetxt = "避雷器及动作次数";
    } else if (data == "4") {
        nodetxt = "铁芯泄露电流";
    } else if (data == "5") {
        nodetxt = "换流变运行工况	";
    } else if (data == "8") {
        nodetxt = "气象信息";
    } else if (data == "19") {
        nodetxt = "局放";
    } else if (data == "hwcw") {
        nodetxt = "红外测温";
    } else {
        nodetxt = "未定义";
    }
    var url = encodeURI(ctxPath + '/deviceHealthState/SpaceDetail?DeviceType=' + data + '&DeviceName=' + nodetxt);
    parent.jeecg.main.addTab(nodetxt, url);
}

//去除所有空格
function Trim(str) {
    return str.replace(/\s+/g, "");
}


// imgvalue = "0";
//alert(imgvalue);

// getDeviceState();

// function getDeviceState() {
//     debugger
//     var now = new Date();
//     var runTime;
//     var type = otherData[i].nodetype;
//     if (type == "1") {
//         runTime = 24 * 100 * 100;
//     } else {
//         runTime = 2 * 100 * 100;
//     }
//     var time = otherData[i].value;
//
//     var nowHour = dateToString(now);
//     var timeHour = time.replace(/-/g, '').replace(/:/g, '').replace(' ', '').split(".")[0];
//     var intTime = parseInt(nowHour) - parseInt(timeHour);
//     //alert(intTime);
//     if (intTime > runTime) {
//         imgvalue = "1";
//     } else {
//         imgvalue = "0";
//     }
//     if (imgvalue == "1") {
//         img_path = basePath + "/images/state/Yellow_big.gif";
//     } else if (imgvalue == "2") {
//         img_path = basePath + "/images/state/Red_big.gif";
//     } else {
//         img_path = basePath + "/images/state/Green_big.gif";
//     }
//
//
// }

// function dateToString(now) {
//     var year = now.getFullYear();
//     var month = (now.getMonth() + 1).toString();
//     var day = (now.getDate()).toString();
//     var hour = (now.getHours()).toString();
//     var minute = (now.getMinutes()).toString();
//     var second = (now.getSeconds()).toString();
//     if (month.length == 1) {
//         month = "0" + month;
//     }
//     if (day.length == 1) {
//         day = "0" + day;
//     }
//     if (hour.length == 1) {
//         hour = "0" + hour;
//     }
//     if (minute.length == 1) {
//         minute = "0" + minute;
//     }
//     if (second.length == 1) {
//         second = "0" + second;
//     }
//     var dateTime = year + month + day + hour + minute + second;
//     //   	   	var dateTime1 = year + "-" + month + "-" + day +" "+ hour +":"+minute+":"+second;
//     return dateTime;
// }
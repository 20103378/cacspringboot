$package('jeecg.DeviceTree');
var nodetype = "";
var n = 0;
var nodeID = "null";
$(function () {
    jeecg.DeviceTree.weather();
    jeecg.DeviceTree.init();
});
jeecg.DeviceTree = function () {
    var _this = {
        weather: function () {
            $.ajax({
                async: false,
                cache: false,
                url: ctxPath + '/Weather/getCurrentWeather',
                type: "POST",
                error: function () {
                },
                success: function (data) {
                    $('#Temperature').append(data.temperature);
                    $('#Humidity').append(data.humidity);
                    $('#WindDirection').append(data.windDirection);
                    $('#WindSpeed').append(data.windSpeed);
                }
            });
        },
        init: function () {
            $('#ZoneEmuList').tree({
                url: ctxPath + '/treeDevice/getZoneEmuList',
                checkbox: false,
                cascadeCheck: false,
                animate: true,
                lines: true,
                state: closed,
                onLoadSuccess: function (node, data) {//加载成功后调用事件
                    // console.log(node, data)
                    if (data != '') {
                        var nodetxt = "设备健康状态";
                        var url = ctxPath + "/deviceHealthState/list";
                        jeecg.main.addTab_closable(nodetxt, url);
                    } else {
                        alert("请设置站点");
                    }
                }
            });
            $('#ZoneEmuList').click(_this.ShowLineData);
        },
        ShowLineData: function () {
            debugger
            var node = $('#ZoneEmuList').tree('getSelected');
            if (node) {
                var id = node.id;
                if (id.indexOf("D") > -1) {
                    if (id.indexOf(nodeID) < 0) {
                        var nodetxt = node.text;
                        nodetxt = nodetxt.replace("#", "@");
                        var url = encodeURI(ctxPath + '/deviceHealthState/deviceDetail?deviceID=' + id);
                        jeecg.main.addTab(nodetxt, url);
                        nodeID = id;
                    }
                }
            }
        },
        // getID: function () {
        //     var node = $('#ZoneEmuList').tree('getSelected');
        //     var id = node.id;
        //     return id;
        // },
        // getRemark: function (deviceType, remark) {
        //     if (deviceType == 1) {
        //
        //         return YSP_trans(remark);
        //
        //     } else if (deviceType == 2) {
        //
        //         return SF6_trans(remark);
        //
        //     } else if (deviceType == 3) {
        //
        //         return Smoam_trans(remark);
        //
        //     } else if (deviceType == 4) {
        //
        //         return Scom_trans(remark);
        //
        //     } else if (deviceType == 5) {
        //
        //         return Scondition_trans(remark);
        //
        //     } else {
        //         return "告警";
        //     }
        // }
    };
    return _this;
}();

// $(document).ready(function () {
//     jeecg.DeviceTree.addMessage();
//     $("#breakingnews").BreakingNews({
//         background: '#f2f2f2',
//         title: '系统消息:',
//         titlecolor: 'black',
//         titlebgcolor: '#f2f2f2',
//         linkcolor: '#333',
//         linkhovercolor: '#099',
//         fonttextsize: 16,
//         isbold: false,
//         border: 'solid 1px #00f2f2',
//         width: '100%',
//         timer: 4000,
//         autoplay: true,
//         effect: 'slide'
//     });
//
//     $('#apply').click(function (e) {
//         addValues();
//     });
//     addValues();
// });
//
// function addValues() {
//     var params = 'Add : jQuery.js\n' +
//         'Add : BreakingNews.js\n' +
//         'Add : BreakingNews.css\n\n\n' +
//         '$("#breakingnews").BreakingNews({\n\n' +
//         '	background		:"' + $("#background").val() + '",\n' +
//         '	title			:"' + $("#title").val() + '",\n' +
//         '	titlecolor		:"' + $("#titlecolor").val() + '",\n' +
//         '	titlebgcolor		:"' + $("#titlebgcolor").val() + '",\n' +
//         '	linkcolor		:"' + $("#linkcolor").val() + '",\n' +
//         '	linkhovercolor		:"' + $("#linkhovercolor").val() + '",\n' +
//         '	fonttextsize		:' + parseInt($("#fonttextsize").val()) + ',\n' +
//         '	isbold			:' + ($("#isbold").val() == "true" ? true : false) + ',\n' +
//         '	border			:"' + $("#border").val() + '",\n' +
//         '	width			:"' + $("#width").val() + "%" + '",\n' +
//         '	timer			:' + parseInt($("#timer").val()) + ',\n' +
//         '	autoplay		:' + ($("#autoplay").val() == "true" ? true : false) + ',\n' +
//         '	effect			:"' + $("#effect").val() + '",\n\n' +
//         '});\n';
//
//     $('.content pre code').html(params);
// }
//
// //工况告警翻译
// function Scondition_trans(a) {
//     var str = "";
//     var remark = parseInt(a);
//
//     if ((remark & 0x08000000) == 0x08000000) {
//         if ((remark & 0x08000800) == 0x08000800) {
//             str += "套管1三相增量差值告警(日)";
//         }
//         if ((remark & 0x08002000) == 0x08002000) {
//             str += "套管1三相增量差值告警(周)";
//         }
//         if ((remark & 0x08008000) == 0x08008000) {
//             str += "套管1三相增量差值告警(月)";
//         }
//         if ((remark & 0x08001000) == 0x08001000) {
//             str += "套管2三相增量差值告警(日)";
//         }
//         if ((remark & 0x08004000) == 0x08004000) {
//             str += "套管2三相增量差值告警(周)";
//         }
//         if ((remark & 0x08010000) == 0x08010000) {
//             str += "套管2三相增量差值告警(月)";
//         }
//
//     }
//     if ((remark & 0x04000000) == 0x04000000) {
//         if ((remark & 0x04000200) == 0x04000200) {
//             str += "套管sf6压力1日变化率告警";
//         }
//         if ((remark & 0x04000400) == 0x04000400) {
//             str += "套管sf6压力2日变化率告警";
//         }
//     }
//     if ((remark & 0x02000000) == 0x02000000) {
//         if ((remark & 0x02000004) == 0x02000004) {
//             str += "顶层油温阈值告警";
//         }
//         if ((remark & 0x02000004) == 0x02000008) {
//             str += "网侧绕组温度阈值告警";
//         }
//         if ((remark & 0x02000010) == 0x02000010) {
//             str += "阀侧绕组温度阈值告警";
//         }
//         if ((remark & 0x02000020) == 0x02000020) {
//             str += "套管sf6压力1阈值告警";
//         }
//         if ((remark & 0x02000040) == 0x02000040) {
//             str += "套管sf6压力2阈值告警";
//         }
//         if ((remark & 0x02000080) == 0x02000080) {
//             str += "有载开关油位阈值告警";
//         }
//         if ((remark & 0x02000100) == 0x02000100) {
//             str += "主油箱油位阈值告警";
//         }
//     }
//     if ((remark & 0x01000000) == 0x01000000) {
//         if ((remark & 0x01000001) == 0x01000001) {
//             str += "设备连接异常";
//         }
//         if ((remark & 0x01000002) == 0x01000002) {
//             str += "设备采集异常";
//         }
//     }
//     return str;
// }
//
// /// 铁芯告警翻译
// function Scom_trans(rek) {
//     var str = "";
//     var remark = parseInt(rek);
//     if ((remark & 0x02000000) == 0x02000000) {
//         if ((remark & 0x02000004) == 0x02000004) {
//             str += "接地电流阈值告警";
//         }
//     }
//     if ((remark & 0x01000000) == 0x01000000) {
//         if ((remark & 0x01000001) == 0x01000001) {
//             str += "设备连接异常";
//         }
//         if ((remark & 0x01000002) == 0x01000002) {
//             str += "设备采集异常";
//         }
//     }
//     return str;
// }
//
// /// 避雷器告警翻译
// function Smoam_trans(rek) {
//     var str = "";
//     var remark = parseInt(rek);
//     if ((remark & 0x02000000) == 0x02000000) {
//         if ((remark & 0x02000004) == 0x02000004) {
//             str += "接地电流阈值告警";
//         }
//     }
//     if ((remark & 0x01000000) == 0x01000000) {
//         if ((remark & 0x01000001) == 0x01000001) {
//             str += "设备连接异常";
//         }
//         if ((remark & 0x01000002) == 0x01000002) {
//             str += "设备采集异常";
//         }
//     }
//     return str;
// }
//
// /// 油色谱告警翻译
// function YSP_trans(rek) {
//     var str = "";
//     var remark = parseInt(rek);
//
//     if ((remark & 0x08000000) == 0x08000000) {
//         if ((remark & 0x08001000) == 0x08001000) {
//             str += "H2三相增量对比告警";
//         }
//         if ((remark & 0x08002000) == 0x08002000) {
//             str += "C2H2三相增量对比告警";
//         }
//         if ((remark & 0x08004000) == 0x08004000) {
//             str += "TH三相增量对比告警";
//         }
//
//     }
//     if ((remark & 0x04000000) == 0x04000000) {
//         if ((remark & 0x04000008) == 0x04000008) {
//             str += "H2变化率告警";
//         }
//         if ((remark & 0x04000010) == 0x04000010) {
//             str += "C2H2变化率告警";
//         }
//         if ((remark & 0x04000020) == 0x04000020) {
//             str += "TH变化率告警";
//         }
//         if ((remark & 0x04000040) == 0x04000040) {
//             str += "微水变化率告警";
//         }
//     }
//     if ((remark & 0x02000000) == 0x02000000) {
//         if ((remark & 0x02000100) == 0x02000100) {
//             str += "H2阈值告警";
//         }
//         if ((remark & 0x02000200) == 0x02000200) {
//             str += "C2H2阈值告警";
//         }
//         if ((remark & 0x02000400) == 0x02000400) {
//             str += "TH阈值告警";
//         }
//         if ((remark & 0x02000800) == 0x02000800) {
//             str += "微水阈值告警";
//         }
//     }
//     if ((remark & 0x01000000) == 0x01000000) {
//         if ((remark & 0x01000001) == 0x01000001) {
//             str += "设备连接异常";
//         }
//         if ((remark & 0x01000002) == 0x01000002) {
//             str += "设备采集异常";
//         }
//         if ((remark & 0x01000004) == 0x01000004) {
//             str += "载气欠压";
//         }
//     }
//     return str;
// }
//
// /// SF6告警翻译
// function SF6_trans(rek) {
//     var str = "";
//     var remark = parseInt(rek);
//
//     if ((remark & 0x08000000) == 0x08000000) {
//         if ((remark & 0x08004000) == 0x08000100) {
//             str += "SF6月增量告警";
//         }
//         if ((remark & 0x08002000) == 0x08000080) {
//             str += "SF6周增量告警";
//         }
//         if ((remark & 0x08001000) == 0x08000040) {
//             str += "SF6日增量告警";
//         }
//
//     }
//     if ((remark & 0x04000000) == 0x04000000) {
//         if ((remark & 0x04000008) == 0x04000008) {
//             str += "Sf6日变化率告警";
//         }
//         if ((remark & 0x04000010) == 0x04000010) {
//             str += "Sf6周变化率告警";
//         }
//         if ((remark & 0x04000020) == 0x04000020) {
//             str += "Sf6月变化率告警";
//         }
//     }
//     if ((remark & 0x02000000) == 0x02000000) {
//         if ((remark & 0x02000004) == 0x02000004) {
//             str += "Sf6压力阈值告警";
//         }
//     }
//     if ((remark & 0x01000000) == 0x01000000) {
//         if ((remark & 0x01000001) == 0x01000001) {
//             str += "设备连接异常";
//         }
//         if ((remark & 0x01000002) == 0x01000002) {
//             str += "设备采集异常";
//         }
//     }
//     return str;
// }
//
// function linkDetail(url) {
//     $('#DeviceDetail').attr('href', url);
//     $('#DeviceDetail').click();
//
//}
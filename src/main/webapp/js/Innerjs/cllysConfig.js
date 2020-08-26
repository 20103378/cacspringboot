//测量量映射配置
$package('jeecg.cllysConfig');
var icd_data = [];
var table_data, json;
var sgcb_data, json;
var loadingDiv;
//定义窗口
$(function () {
    $(".tabs li:eq(6)").click(function () {
        //开始初始化下拉框
        jeecg.cllysConfig.setSel();
        //根据级联 开始加载表
        jeecg.cllysConfig.setTable();
        //按钮点击事件
        jeecg.cllysConfig.setBtn();
//		$("#bt_dxh").click();
        $("#bt_ysSGCB").unbind('click').click(function () {
            jeecg.cllysConfig.setysSGCB();
        });

    });
});
jeecg.cllysConfig = function () {
    var _this = {
        setysSGCB: function () {
            var _fileName = $("#sel_iedName").val() + "MONT";
            sgcb_data = [{"iedName": _fileName, "type": "LLN0$SP$SGCB$NumOfSG", "dh": "", "dxh": ""}
                , {"iedName": _fileName, "type": "LLN0$SP$SGCB$ActSG", "dh": "", "dxh": ""}
                , {"iedName": _fileName, "type": "LLN0$SP$SGCB$EditSG", "dh": "", "dxh": ""}
                , {"iedName": _fileName, "type": "LLN0$SP$SGCB$CnfEdit", "dh": "", "dxh": ""}
                , {"iedName": _fileName, "type": "LLN0$SP$SGCB$LActTm", "dh": "", "dxh": ""}];
            $('#cllys_tab').datagrid('loadData', sgcb_data);
            $('#cllys_tab').datagrid('reload');
        },
        //s设置点序号
        setBtn: function () {
            $("#bt_dxh").click(function () {
                var str_zh = $("#sel_zh").val();
                var num_zh = parseInt(str_zh);
                var str_dxh = $("#sel_dxh").val();
                var num_dxh = parseInt(str_dxh);
                //获取所有选中行
                var selects = $('#cllys_tab').datagrid('getSelections');
                //获取选中行行号列表
                var indexs = new Array();
                for (var i = 0; i < selects.length; i++) {
                    indexs.push($('#cllys_tab').datagrid('getRowIndex', selects[i]));
                }
                //定值组数据来源特殊处理
                var _flag = $('#cllys_tab').datagrid("getRows")[0].type.indexOf("SGCB");
                //SG SE特殊处理
                var _flag2 = $('#cllys_tab').datagrid("getRows")[0].type.indexOf("SG");
                //CO特殊处理
                var _flag3 = $('#cllys_tab').datagrid("getRows")[0].type.indexOf("CO");
                if (_flag > -1 && (num_dxh >= 0 && num_zh >= 0)) {
                    for (var i = 0; i < indexs.length; num_dxh++) {
                        sgcb_data[indexs[i]].dh = num_zh;
                        sgcb_data[indexs[i]].dxh = num_dxh;
                        i++;
                    }
                    $('#cllys_tab').datagrid('loadData', sgcb_data);
//					$('#cllys_tab').datagrid('reload');
                } else if (_flag2 > -1 && (num_dxh >= 0 && num_zh >= 0)) {
                    for (var i = 0; i < indexs.length; num_dxh++) {
                        table_data[indexs[i]].dh = num_zh;
                        table_data[indexs[i]].dxh = num_dxh;
                        i++;
                    }
                    $('#cllys_tab').datagrid('loadData', table_data);
//					$('#cllys_tab').datagrid('reload');
                } else if (_flag3 > -1 && (num_dxh >= 0 && num_zh >= 0)) {
                    for (var i = 0; i < indexs.length; num_dxh++) {
                        table_data[indexs[i]].dh = num_zh;
                        table_data[indexs[i]].dxh = num_dxh;
                        i++;
                    }
                    $('#cllys_tab').datagrid('loadData', table_data);
//					$('#cllys_tab').datagrid('reload');
                }
                if (_flag < 0 && _flag2 < 0 && _flag3 < 0 && (num_dxh >= 0 && num_zh >= 0)) {
                    for (var i = 0; i < indexs.length;) {
                        if (i == 0) {
                        } else if (_this.getSplitArr(table_data[indexs[i]].type)[2] != _this.getSplitArr(table_data[indexs[i - 1]].type)[2]) {
                            num_dxh++;
                        } else if (_this.getSplitArr(table_data[indexs[i]].type)[0] != _this.getSplitArr(table_data[indexs[i - 1]].type)[0]) {
                            num_dxh++;
                        } else {
//							num_dxh++;
                        }
                        table_data[indexs[i]].dh = num_zh;
                        table_data[indexs[i]].dxh = num_dxh;
                        i++;
                    }
                    $('#cllys_tab').datagrid('loadData', table_data);
//					$('#cllys_tab').datagrid('reload');
                }
                //保持选中状态
                for (var i = 0; i < indexs.length; i++) {
                    $("#cllys_div  tr")[indexs[i] + 2].attributes[0].nodeValue = "datagrid-row datagrid-row-selected";
//					attr("class","datagrid-row datagrid-row-selected");
                }
            });
        },
        //将字符串转化为数组并返回
        getSplitArr: function (arr) {
            var arr_td = new Array();
            arr_td = arr.split("$");
            return arr_td;
        },
        setTable: function () {




            table_data = [];
            //根据下拉框的选择获取table需要的数据


            var str = $('#sel_LN0').combobox('getValue');
            var fc = $('#sel_fc').combobox('getValue');
            if (str == "全部") {
                debugger
                for (var i = 0; i < icd_data.length; i++) {
                    if (icd_data[i].fc == $('#sel_fc').combobox('getValue')) {
                        if (icd_data[i].datypeName == null) {
                            var _type = icd_data[i].lnClass + icd_data[i].lninst + "$" + icd_data[i].fc + "$" + icd_data[i].doName + "$" + icd_data[i].typeName;
                            table_data.push({
                                "iedName": icd_data[i].iedName + icd_data[i].ldinst,
                                "type": _type,
                                "dh": "",
                                "dxh": "",
                                "desc": icd_data[i].desc
                            });
                            if (icd_data[i].fc == "SG") {
                                _type = icd_data[i].lnClass + icd_data[i].lninst + "$SE$" + icd_data[i].doName + "$" + icd_data[i].typeName;
                                table_data.push({
                                    "iedName": icd_data[i].iedName + icd_data[i].ldinst,
                                    "type": _type,
                                    "dh": "",
                                    "dxh": "",
                                    "desc": icd_data[i].desc
                                });
                            }
                        } else {
                            if (icd_data[i].dbaType == null) {
                                var _type = icd_data[i].lnClass + icd_data[i].lninst + "$" + icd_data[i].fc + "$" + icd_data[i].doName + "$" + icd_data[i].typeName + "$" + icd_data[i].datypeName;
                                table_data.push({
                                    "iedName": icd_data[i].iedName + icd_data[i].ldinst,
                                    "type": _type,
                                    "dh": "",
                                    "dxh": "",
                                    "desc": icd_data[i].desc
                                });
                                if (icd_data[i].fc == "SG") {
                                    _type = icd_data[i].lnClass + icd_data[i].lninst + "$SE$" + icd_data[i].doName + "$" + icd_data[i].typeName;
                                    table_data.push({
                                        "iedName": icd_data[i].iedName + icd_data[i].ldinst,
                                        "type": _type,
                                        "dh": "",
                                        "dxh": "",
                                        "desc": icd_data[i].desc
                                    });
                                }
                            } else {
                                var _type = icd_data[i].lnClass + icd_data[i].lninst + "$" + icd_data[i].fc + "$" + icd_data[i].doName + "$" + icd_data[i].typeName + "$" + icd_data[i].datypeName + "$" + icd_data[i].dbaType;
                                table_data.push({
                                    "iedName": icd_data[i].iedName + icd_data[i].ldinst,
                                    "type": _type,
                                    "dh": "",
                                    "dxh": "",
                                    "desc": icd_data[i].desc
                                });
                                if (icd_data[i].fc == "SG") {
                                    _type = icd_data[i].lnClass + icd_data[i].lninst + "$SE$" + icd_data[i].doName + "$" + icd_data[i].typeName;
                                    table_data.push({
                                        "iedName": icd_data[i].iedName + icd_data[i].ldinst,
                                        "type": _type,
                                        "dh": "",
                                        "dxh": "",
                                        "desc": icd_data[i].desc
                                    });
                                }
                            }

                        }
                    }
                }
                _this.data_config();
                $('#cllys_tab').datagrid('loadData', table_data);
                $('#cllys_tab').datagrid('reload');
                return;
            }
            debugger
            var lnType = str.substring(0, str.indexOf("("));
            var lnClass = str.substring(str.indexOf("(") + 1, str.indexOf(","));
            var inst = str.substring(str.indexOf(",") + 1, str.indexOf(")"));
            //根据上述变量从数组中删选出对应的fc放在table数组中
            for (var i = 0; i < icd_data.length; i++) {
                if (icd_data[i].lnType == lnType && icd_data[i].lninst == inst && icd_data[i].lnClass == lnClass && icd_data[i].fc == $('#sel_fc').combobox('getValue')) {
                    if (icd_data[i].datypeName == null) {
                        var _type = icd_data[i].lnClass + icd_data[i].lninst + "$" + icd_data[i].fc + "$" + icd_data[i].doName + "$" + icd_data[i].typeName;
                        table_data.push({
                            "iedName": icd_data[i].iedName + icd_data[i].ldinst,
                            "type": _type,
                            "dh": "",
                            "dxh": "",
                            "desc": icd_data[i].desc
                        });
                        if (icd_data[i].fc == "SG") {
                            _type = icd_data[i].lnClass + icd_data[i].lninst + "$SE$" + icd_data[i].doName + "$" + icd_data[i].typeName;
                            table_data.push({
                                "iedName": icd_data[i].iedName + icd_data[i].ldinst,
                                "type": _type,
                                "dh": "",
                                "dxh": "",
                                "desc": icd_data[i].desc
                            });
                        }
                    } else {
                        if (icd_data[i].dbaType == null) {
                            var _type = icd_data[i].lnClass + icd_data[i].lninst + "$" + icd_data[i].fc + "$" + icd_data[i].doName + "$" + icd_data[i].typeName + "$" + icd_data[i].datypeName;
                            table_data.push({
                                "iedName": icd_data[i].iedName + icd_data[i].ldinst,
                                "type": _type,
                                "dh": "",
                                "dxh": "",
                                "desc": icd_data[i].desc
                            });
                            if (icd_data[i].fc == "SG") {
                                _type = icd_data[i].lnClass + icd_data[i].lninst + "$SE$" + icd_data[i].doName + "$" + icd_data[i].typeName;
                                table_data.push({
                                    "iedName": icd_data[i].iedName + icd_data[i].ldinst,
                                    "type": _type,
                                    "dh": "",
                                    "dxh": "",
                                    "desc": icd_data[i].desc
                                });
                            }
                        } else {
                            var _type = icd_data[i].lnClass + icd_data[i].lninst + "$" + icd_data[i].fc + "$" + icd_data[i].doName + "$" + icd_data[i].typeName + "$" + icd_data[i].datypeName + "$" + icd_data[i].dbaType;
                            table_data.push({
                                "iedName": icd_data[i].iedName + icd_data[i].ldinst,
                                "type": _type,
                                "dh": "",
                                "dxh": "",
                                "desc": icd_data[i].desc
                            });
                            if (icd_data[i].fc == "SG") {
                                _type = icd_data[i].lnClass + icd_data[i].lninst + "$SE$" + icd_data[i].doName + "$" + icd_data[i].typeName;
                                table_data.push({
                                    "iedName": icd_data[i].iedName + icd_data[i].ldinst,
                                    "type": _type,
                                    "dh": "",
                                    "dxh": "",
                                    "desc": icd_data[i].desc
                                });
                            }
                        }
                    }
                }
            }
            _this.data_config();
            $('#cllys_tab').datagrid('loadData', table_data);
            $('#cllys_tab').datagrid('reload');
        },
        setSel: function () {
            //初始化iedName下拉框
            var iedName = $("#tab_ldein").datagrid("getSelected").arName;
            $("#sel_iedName").val(iedName);
            //通过ajax请求数据
            var formData = {};
            formData['fileName'] = iedName;
            $.ajax({
                async: false,
                cache: false,
                type: 'post',
                data: formData,
                url: "getSelXml",
                success: function (data) {
                    icd_data = data.icd_data;
                    debugger
                    //初始化apName
                    $("#sel_apName").val(data.ap_name);
                    // //初始化ldinst
                    // $("#sel_ldInst").val(data.ld_inst);
                    //
                    // console.log(data.ldInstMap)
                    // console.log(data.ldInstMap["BJST6_ZT5001"])

                    //开始初始化级联
                    //初始化逻辑节点
                    var ld_data = [];
                    ld_data.push({"text": "全部", "value": "全部"});
                    for (var i = 0; i < data.ld_data.length; i++) {
                        ld_data.push({"text": data.ld_data[i], "value": data.ld_data[i]});
                    }
                    $('#sel_LD0').combobox({
                        onChange: function (n, o) {
                            debugger
                            var ld = $('#sel_LD0').combobox('getValue');
                            if(ld=='全部'){
                                icd_data=data.icd_data;
                            }else {
                                icd_data = data.ldInstMap[ld];
                            }
                            _this.setTable();
                        }
                    });

                    $("#sel_LD0").combobox("loadData", ld_data);
                    $("#sel_LD0").combobox('select', ld_data[0].value);


                    //开始初始化级联
                    //初始化逻辑节点
                    var ln_data = [];
                    ln_data.push({"text": "全部", "value": "全部"});
                    for (var i = 0; i < data.ln_data.length; i++) {
                        ln_data.push({"text": data.ln_data[i], "value": data.ln_data[i]});
                    }
                    $('#sel_LN0').combobox({
                        onChange: function (n, o) {
                            _this.setTable();
                        }
                    });
                    $("#sel_LN0").combobox("loadData", ln_data);
                    $("#sel_LN0").combobox('select', ln_data[0].value);


                    var fc_data = [];
                    for (var i = 0; i < data.fc_data.length; i++) {
                        if (data.fc_data[i] == "SG") {
                            fc_data.push({"text": "SG/SE", "value": data.fc_data[i]});
                        } else {
                            fc_data.push({"text": data.fc_data[i], "value": data.fc_data[i]});
                        }
                    }
                    $('#sel_fc').combobox({
                        onChange: function (n, o) {
                            _this.setTable();
                        }
                    });
                    $("#sel_fc").combobox("loadData", fc_data);
                    $("#sel_fc").combobox('select', fc_data[0].value);


                }
            });
        },
        data_config: function () {
            $('#cllys_tab').datagrid({
                onDblClickRow: function () {
                    //预留双击事件
                },
                columns: [[
                    {
                        field: 'cy',
                        checkbox: true,
                        align: 'center'
                    },
                    {
                        field: 'iedName',
                        title: '测点名称',
                        width: 250,
                        align: 'center',
                        formatter: function (value, row, index) {
                            return row.iedName;
                        }
                    },
                    {
                        field: 'type',
                        title: '测点类型',
                        width: 340,
                        align: 'center',
                        formatter: function (value, row, index) {
                            return row.type;
                        }
                    },
                    // 	{
                    //     field: 'dh',
                    //     title: '组号',
                    //     width:180,
                    //     align: 'center',
                    //     sortable: true,
                    //     formatter: function(value, row, index){
                    //         return row.dh;
                    //     }
                    // },{
                    //     field: 'dxh',
                    //     title: '点序号',
                    //     width:180,
                    //     align: 'center',
                    //     formatter: function(value, row, index){
                    //         return row.dxh;
                    //     }
                    // },
                    {
                        field: 'desc',
                        title: '测量点名',
                        width: 500,
                        align: 'center',
                        formatter: function (value, row, index) {
                            return row.desc;
                        }
                    }]],
                toolbar: ["-", {
                    id: '',
                    text: '反选',
                    iconCls: '',
                    handler: function () {
                        $("#cllys_div  tr").each(function (idx) {
                            if (idx > 1) {
                                var cls = $(this).attr("class");
                                if (cls == "datagrid-row datagrid-row-selected") {
                                    $(this).attr("class", "datagrid-row");
                                } else if (cls == "datagrid-row") {
                                    $(this).attr("class", "datagrid-row datagrid-row-selected");
                                }
                            }
                        })
                    }
                },

                    // "-",{
                    //     id: '',
                    //     text: '数据库点号传入',
                    //     iconCls: '',
                    //     handler: function () {
                    //     	var _fileName=$("#sel_iedName").val()+"MONT";
                    //     	var _fc=$('#sel_fc').combobox('getValue');
                    //     	var formdata={};
                    //     	formdata["fc"]=_fc;
                    //     	formdata["ldinst"]=_fileName;
                    //     	$.ajax({
                    //     		async:false,
                    // 		cache:false,
                    // 		type:'post',
                    // 		url:"getDxhFromDB",
                    // 		data:formdata,
                    // 		success:function(data){
                    // 			for(var i=0; i<table_data.length;i++){
                    // 				for(var j=0;j<data.length;j++){
                    // 					// if(table_data[i].type.indexOf(data[j].yc_refname)>-1&&table_data[i].type.indexOf(data[j].ln_inst_name)>-1){
                    // 					if(table_data[i].type.indexOf(data[j].ied_type_id)>-1){
                    // 						var _num=data[j].yc_id;
                    // 						var _zh=parseInt(_num/2048);
                    // 						var _dxh=_num%2048;
                    // 						table_data[i].dh=_zh;
                    // 						table_data[i].dxh=_dxh;
                    // 						break;
                    // 					}
                    // 				}
                    // 			}
                    // 				$('#cllys_tab').datagrid('loadData',table_data);
                    // 			$('#cllys_tab').datagrid('reload');
                    // 		},
                    // 		error:function(){
                    // 		}
                    //     	});
                    //     }
                    // },

                    // "-",{
                    //     id: '',
                    //     text: '配置文件点号传入',
                    //     iconCls: '',
                    //     handler: function () {
                    //     	var _fileName=$("#sel_iedName").val()+"MONT";
                    //     	var _fc=$('#sel_fc').combobox('getValue');
                    //     	var formdata={};
                    //     	formdata["fc"]=_fc;
                    //     	formdata["ldinst"]=_fileName;
                    //     	$.ajax({
                    //     		async:false,
                    // 		cache:false,
                    // 		type:'post',
                    // 		url:"getDxhFromCFG",
                    // 		data:formdata,
                    // 		success:function(data){
                    //     			debugger
                    // 			for(var i=0; i<table_data.length;i++){
                    // 				for(var j=0;j<data.length;j++){
                    // 							// if(table_data[i].type.indexOf(data[j].yc_refname)>-1&&table_data[i].type.indexOf(data[j].ln_inst_name)>-1){
                    // 							if(table_data[i].type.indexOf(data[j].ied_type_id)>-1){
                    // 							var _num=data[j].yc_id;
                    // 								var _zh=parseInt(_num/2048);
                    // 								var _dxh=_num%2048;
                    // 								table_data[i].dh=_zh;
                    // 								table_data[i].dxh=_dxh;
                    // 								break;
                    // 							}
                    // 				}
                    // 			}
                    // 				$('#cllys_tab').datagrid('loadData',table_data);
                    // 			$('#cllys_tab').datagrid('reload');
                    // 		},
                    // 		error:function(){
                    // 		}
                    //     	});
                    //     }
                    // },
                    "-", {
                        id: '',
                        text: '同步至数据库',
                        iconCls: '',
                        handler: function () {
                            var select_datas = $("#cllys_tab").datagrid("getSelections");
                            if (select_datas == null) {
                                alert("未选择任何行");
                                return;
                            }
                            // else if(select_datas[0].dh===""||select_datas[0].dxh===""){
                            // 	alert("序号不能为空!");
                            // 	return;
                            // }
                            loadingDiv = $("#loading");
                            loadingDiv.openMask('正在同步至数据库。。。');
                            $.ajax({
                                async: false,
                                cache: false,
                                traditional: true,
                                type: 'post',
                                url: "updataDB",
                                data: {"list": JSON.stringify(select_datas)},
                                dataType: json,
                                success: function (data) {
                                    if (data) {
                                        // loadingDiv.closeMask("正在同步至数据库。。。");
                                        // alert("同步失败!");
                                        // }else{
                                        loadingDiv.closeMask("正在同步至数据库。。。");
                                        alert("数据库同步成功!");
                                    }
                                },
                                error: function () {
                                    loadingDiv.closeMask("正在同步至数据库。。。");
                                    alert("同步失败!");
                                }
                            });

                        }
                    },
                    "-",
                    {
                        id: '',
                        text: '注入配置文件',
                        iconCls: '',
                        handler: function () {
                            var select = $("#cllys_tab").datagrid("getSelected");
                            if (select == null) {
                                alert("未选择任何行");
                                return;
                            }
                            // else if(select.dh===""||select.dxh===""){
                            // 	alert("序号不能为空!");
                            // 	return;
                            // }
                            _this.updataXml();
                        }
                    },
                    "-", {
                        id: '',
                        text: '导出cfg文件',
                        iconCls: '',
                        handler: function () {
                            var appWindow = window.open("exportCfg?dirName=" + $("#sel_iedName").val());
                            appWindow.focus();
                        }
                    }, "-"]
            });
//			$("#cllys_tab").datagrid('hideColumn', 'index');
        },
        updataXml: function () {
            var select_datas = $("#cllys_tab").datagrid("getSelections");
            var iedName_updataXml = $("#sel_iedName").val();
            $.ajax({
                async: false,
                cache: false,
                traditional: true,
                type: 'post',
                url: "updataXml",
                data: {"iedName": iedName_updataXml, "jsonList": JSON.stringify(select_datas)},
                dataType: json,
                success: function (data) {
                    if (data == "") {
                        alert("iedName地址错误！");
                    } else {
                        alert("注入cfg文件成功!");
                    }
                },
                error: function () {
                    alert("注入cfg文件失败!");
                }
            });
        },
        openMask: function () {
            loadingDiv.openMask("数据加载中。。。。");
        },
        closeMask: function () {
            loadingDiv.closeMask("数据加载中。。。。");
        }
    };
    return _this;
}();

function fixWidth(percent) {
    return document.body.clientWidth * percent;
}

Array.prototype.serializeObject = function (lName) {
    var o = {};
    $t = lName;

    for (var i = 0; i < $t.length; i++) {
        for (var item in $t[i]) {
            o[lName + '[' + i + '].' + item.toString()] = $t[i][item].toString();
        }
    }
    return o;
};
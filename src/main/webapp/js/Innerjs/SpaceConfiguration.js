$package('jeecg.SpaceConfiguration');
//定义窗口
$(function () {
	$(".tabs li:eq(1)").click(function(){
		//加载区域列表
		jeecg.SpaceConfiguration.addTable();
		//初始化space添加修改窗口
		jeecg.SpaceConfiguration.setEditWin();
	  	$("#bt_space").unbind('click').click(function(){
	  		jeecg.SpaceConfiguration.space_sub();
	  	});
		jeecg.SpaceConfiguration.upload();
	});
});
jeecg.SpaceConfiguration = function(){
	var _this = {
			upload:function(){
				$("#uploadBtn").unbind('click').click(function() {
					var File_xml =  $("#File_xml").val();
					var File_swf = $("#File_swf").val();
					var select_name = $("#tab_space").datagrid("getSelected").spaceName;
    				if(select_name=="GIS区域"){
    					select_name="GIS";
    				}else if(select_name=="站用变"||select_name=="低端换流变"||select_name=="低端换流变"){
    					select_name="FT";
    				}else if(select_name=="直流场"){
    					select_name="ZLC";
    				}else if(select_name=="交流滤波场"){
    					select_name="JLC";
    				}
				        if(File_xml && File_swf){
							 if(File_xml.indexOf("/")==-1 || File_xml.indexOf("\\")==-1){
								 File_xml=File_xml.substring(File_xml.lastIndexOf("\\")+1);
							 }
							 if(File_swf.indexOf("/")==-1 || File_swf.indexOf("\\")==-1){
								 File_swf=File_swf.substring(File_swf.lastIndexOf("\\")+1);
							 }
							 jeecg.progress('正在上传','请稍后...^3^');
							 $('#MapForm').form({
				            	async:false,
				      			cache:false,
				                onSubmit: function(){
				                },
				                success:function(data){
				                	jeecg.closeProgress();
				                	if(data){
				                		alert("上传成功!");
				                	};
				                	uploadWin.window("close");
//				                	$('#i2List').datagrid('reload');
				                }
				        	});
				            // 提交 form
				            $('#MapForm').attr("action", "getSpaceMap?select_name="+select_name).submit();
				         }else if(!File_xml){
				         	alert("选择你要上传的xml文件");
				         }else if(!File_swf){
				        	alert("选择你要上传的swf文件");
				         }
				});
			 },
			space_sub:function(){
			 var spaceid=$("#txt_spaceID").val();
			 var spaceName=$("#txt_spaceName").val();
			 var objectVoltage=$("#txt_objectVoltage_A").combobox('getValue')+"-"+$("#txt_objectVoltage_O").combobox('getValue');
			 var spaceTag=$("#txt_spaceTag").val();
			 if(spaceid==""){
				 $("#msg_spaceID").html("区域ID不能为空!");
				 if(spaceName==""){
					 $("#msg_spaceName").html("区域名不能为空!");
					 if(spaceTag==""){
						 $("#msg_spaceTag").html("区域标签不能为空!");
						 return;
					 }
					 return;
				 }
				 return;
			 }
			 var formdata={};
			 formdata['spaceId'] = spaceid;
			 formdata['spaceName'] = spaceName;
			 formdata['objectVoltage'] = objectVoltage;
			 formdata['spaceTag'] = spaceTag;
			 $.ajax({
				 async:false,
				 cache:false,
				 type:'post',
				 url:"insertSpace",
				 data:formdata,
				 dataType: "json",
				 success:function(){
					 deviceWin.window('close');
		        	 $('#tab_space').datagrid('reload');
					 alert("插入成功!")
					 return
				 },
				 error:function(){
					 alert("插入失败!")
				 }
			 });


		 },
		reTextSpace:function(){
			document.getElementById("txt_spaceID").disabled="";
	       	$("#txt_spaceID").val('');
 			$("#txt_spaceName").val('');
 			$("#txt_spaceTag").val('');
		},
		addTable:function(){
			 var _box = null;
			_box = new YDataGrid(_this.config,"#tab_space",true,false,true,false);
			_box.grid.datagrid.defaults.onDblClickRow=_this.update;
			_box.init();
		},
		update:function(rowIndex, rowData){
			$("#txt_spaceID").val(rowData.spaceId);
			$('#txt_spaceID').attr('disabled',true);
			$("#txt_spaceName").val(rowData.spaceName);
			var objectVoltage_A=rowData.objectVoltage.substring(0,2);
			var objectVoltage_O=rowData.objectVoltage.substring(3);
			$("#txt_objectVoltage_A").combobox('select',objectVoltage_A);
			$("#txt_objectVoltage_O").combobox('select',objectVoltage_O);
			$("#txt_spaceTag").val(rowData.spaceTag);
			deviceWin.window('open');
		 },
		 setEditWin:function(){
			 deviceWin = $('#space-window').window({
		        href:'',
			    title:'添加/修改',
			    left:'100px',
			    top:'70px',
			    closed: true,
			    modal: false,
			    cache: false,
			    minimizable:false,
			    maximizable:false,
			    collapsible:false,
			    shadow: false
			 });
			 uploadWin = $('#UploadMap-window').window({
		        href:'',
			    title:'Excel文件上传',
			    left:'200px',
			    top:'150px',
			    closed: true,
			    modal: false,
			    cache: false,
			    minimizable:false,
			    maximizable:false,
			    collapsible:false,
			    shadow: false
			 });
		},
		MakeXml:function(){//生成XML文件。选择设备，通过区域名查询PUBDEVICE表的SPACE获取设备返回ID、deviceName、DeviceType
			if($("#tab_space").datagrid("getSelected")){
				var select_name = encodeURIComponent($("#tab_space").datagrid("getSelected").spaceName);
				var path=ctxPath+"/jsp/com.scott/Graphs/";
				 $.ajax({
					 async:false,
					 cache:false,
					 type:'post',
					 url:"getDeviceBySpace?select_name="+select_name+"&path="+path,
					 success:function(data){
						 alert("生成XML文件成功!");
			        	 return
					 },
					 error:function(){
						 alert("插入失败!")
					 }
				 });
			}else{
				alert("请选择需要生成XML文件的设备！");
			}
		},
		ModXml:function(){//修改。选择设备，通过区域名查询PUBDEVICE表的SPACE获取设备返回ID、deviceName、DeviceType
			if($("#tab_space").datagrid("getSelected")){
				var select_name = encodeURIComponent($("#tab_space").datagrid("getSelected").spaceName);
				var path=ctxPath+"/jsp/scott/Graphs/";
				 $.ajax({
					 async:false,
					 cache:false,
					 type:'post',
					 url:"ModDeviceBySpace?select_name="+select_name+"&path="+path,
					 success:function(data){
						 alert("修改XML文件成功!");
			        	 return
					 },
					 error:function(){
						 alert("插入失败!")
					 }
				 });
			}else{
				alert("请选择需要生成XML文件的设备！");
			}
		},
		MapDeviceSet:function(){
			if($("#tab_space").datagrid("getSelected")){
				var select_name = encodeURIComponent($("#tab_space").datagrid("getSelected").spaceName);
				$.ajax({
					async:false,
					cache:false,
					type:'post',
					url:"getchangeXmlSwitchTo0?select_name="+select_name,
					success:function(data){
						alert("请到首页设备页拖动设备");
					},
					error:function(){
						alert("设置失败!");
					}
				});
			}else{
				alert("请选择区域");
			}
		},
		MapDeviceFinish:function(){
			var select_name = encodeURIComponent($("#tab_space").datagrid("getSelected").spaceName);
			$.ajax({
				async:false,
				cache:false,
				type:'post',
				url:"getchangeXmlSwitchTo1?select_name="+select_name,
				success:function(data){
					alert("设置完成");
				},
				error:function(){
					alert("设置失败!");
				}
			});
		},
		DownlodeMap:function(){
			if($("#tab_space").datagrid("getSelected")){
			var select_name = $("#tab_space").datagrid("getSelected").spaceName;
				if(select_name=="GIS区域"){
					select_name="GIS";
				}else if(select_name=="站用变"||select_name=="低端换流变"||select_name=="低端换流变"){
					select_name="FT";
				}else if(select_name=="直流场"){
					select_name="ZLC";
				}else if(select_name=="交流滤波场"){
					select_name="JLC";
				}
				 $.ajax({
					 async:false,
					 cache:false,
					 type:'post',
					 url:"getSpaceZip?select_name="+select_name,
					 success:function(data){
						 deviceWin.window('close');
			        	 $('#tab_space').datagrid('reload');
			        	 return
					 },
					 error:function(){
						 alert("插入失败!");
					 }
				 });
//				var MapUrl= ctxPath+"/jsp/com.scott/Graphs/data_gis.xml";用不上
				var MapUrl="..\\view\\com.scott\\Graphs\\MapZIP.zip";
				window.open(MapUrl);
			}else{
				alert("请选择需要下载地图的区域！");
			}

		},
		UpdateMap:function(){
			if($("#tab_space").datagrid("getSelected")){
				uploadWin.window('open');
			}else{
				alert("请选择需要上传地图的区域！");
			}
		},
		Del:function(){
			var select_data = $("#tab_space").datagrid("getSelected");
			var delId= select_data.spaceId;
			var delname= select_data.spaceName;
			if(window.confirm("确定要删除"+delname+"["+delId+"]？")){
				var formData = {};
			    var url="deleteSpace";
			    formData['spaceId'] = delId;
				$.ajax({
			         async: false,
			         cache: false,
			         type: 'POST',
			         url: url,
			         data: formData,
					 dataType: "json",
					error: function(){// 请求失败处理函数
			        	 alert("删除失败！");
			         },
			         success: function(){
						 alert("删除成功！");
						 $('#tab_space').datagrid('reload');
			         }
			     });
             }
		},
		config:{
			dataGrid:{
//				title: $("#txtName").val(),
				url:"getEquipmentSpace",
				columns:  [[
	            {
	                field: 'SpaceId',
	                title: '区域ID',
	                width: fixWidth(0.12),
	                align: 'center',
	                sortable: true,
	                formatter: function(value, row, index){
	                    return row.spaceId;
	                }
	            },
	            {
	                field: 'SpaceName',
	                title: '区域名',
	                width: fixWidth(0.12),
	                align: 'center',
	                sortable: true,
	                formatter: function(value, row, index){
	                    return row.spaceName;
	                }
	            },
	            {
	                field: 'ObjectVoltage',
	                title: '电压等级',
	                width: fixWidth(0.12),
	                align: 'center',
	                sortable: true,
	                formatter: function(value, row, index){
	                    return row.objectVoltage;
	                }
	            },
	            {
	                field: 'SpaceTag',
	                title: '区域标签',
	                width: fixWidth(0.12),
	                align: 'center',
	                sortable: true,
	                formatter: function(value, row, index){
	                    return row.spaceTag;
	                }
	            }]],
	            toolbar: ["-", {
	  	            id: '',
	  	            text: '添加',
	  	            iconCls: '',
	  	            handler: function () {
	  	            	_this.reTextSpace();
	  	            	deviceWin.window('open');
	  	            }
	  	        }, "-", {
	  	            id: '',
	  	            text: '删除',
	  	            iconCls: '',
	  	            handler: function () {
	  	            	_this.Del();
	  	            }
	  	        },"-",{
	  	            id: '',
	  	            text: '生成xml',
	  	            iconCls: '',
	  	            handler: function () {
	  	            	_this.MakeXml();
	  	            }
	  	        },"-",{
	  	            id: '',
	  	            text: '修改xml',
	  	            iconCls: '',
	  	            handler: function () {
	  	            	_this.ModXml();
	  	            }
	  	        },"-",{
	  	            id: '',
	  	            text: '地图设备点设置',
	  	            iconCls: '',
	  	            handler: function () {
	  	            	_this.MapDeviceSet();
	  	            }
	  	        },"-",{
	  	            id: 'MapPointFinish',
	  	            text: '地图设备点设置完成',
	  	            iconCls: '',
	  	            handler: function () {
	  	            	_this.MapDeviceFinish();
	  	            }
	  	        },"-",{
	  	            id: '',
	  	            text: '下载地图',
	  	            iconCls: '',
	  	            handler: function () {
	  	            	_this.DownlodeMap();
	  	            }
	  	        },"-",{
	  	            id: '',
	  	            text: '上传地图',
	  	            iconCls: '',
	  	            handler: function () {
	  	            	_this.UpdateMap();
	  	            }
	  	        },"-"]
			}
		}
	};
	return _this;
}();
function fixWidth(percent)
{
    return document.body.clientWidth * percent ;
}

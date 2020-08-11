$package('jeecg.i1toI2');
$(function () {
	$(".tabs li:eq(4)").click(function(){
		//加载I2列表
		jeecg.i1toI2.setI2Table();
		//初始化I2添加修改窗口
		jeecg.i1toI2.setEditWin();
		//初始化EXCEL导入窗口
		jeecg.i1toI2.upload();
		//按钮提交 事件监听
	  	$("#bt_sub").unbind('click').click(function(){
	  		jeecg.i1toI2.i1ToI2_sub();
	  	});
	});
});
jeecg.i1toI2 = function(){
	var _this = {
			upload:function(){
				$("#ExceluploadFileBtn").unbind('click').click(function() {
					var Uploader= $("#ExcelUploader").val();
					if(Uploader){
					var fileUrl =  $("#ExcelfileUrl").val();
				        if(fileUrl){
							 if(fileUrl.indexOf("/")==-1 || fileUrl.indexOf("\\")==-1){
							 fileUrl=fileUrl.substring(fileUrl.lastIndexOf("\\")+1);
							 }
							 jeecg.progress('正在导入','请稍后...^3^');
							 $('#ExcelpdfForm').form({
				            	async:false,
				      			cache:false,
				                onSubmit: function(){
				                },
				                success:function(data){
				                	jeecg.closeProgress();
				                	if(data.indexOf(1)<0){
				                		alert("导入文件失败！");
				                	}else if(data.indexOf(2)<0){
				                		alert("导入文件成功，导入数据库失败");
				                	}else{
				                		alert("导入成功!");
				                	};
				                	uploadWin.window("close");
				                	$('#i2List').datagrid('reload');
				                }
				        	});
				            // 提交 form
				            $('#ExcelpdfForm').attr("action", "uploadExcel.do").submit();;
				         }else{
				         	alert("上传的文件不可为空");
				         }
					}else{
						alert("必须填写提交人");
					}
				});
			 },
		 setDeviceName:function(){
			 //通过ajax获取设备列表
			 $.ajax({
		         async: false,
		         cache: false,
		         type: 'POST',
		         url: "getAllDevice.do",
		         success:function(data){
		        	 var device_data,json;
		        	 device_data=[];
		        	 for(var i=0;i<data.dataList.length;i++){
		        		 device_data.push({"text":data.dataList[i].deviceName,"value":data.dataList[i].IEC61850LD_LN});
		        	 };
		        	 $("#sel_dname").combobox("loadData", device_data);
		 			 $("#sel_dname").combobox('select',device_data[0].value);
		 			 //加载类型下拉框
	  	             _this.setType();
		 			 //加载名称
		 			 $('#sel_dname').combobox({
		 		         onChange:function(n,o){
		 		            _this.setName();
		 		         }
		 			 });
		         },
		         error:function(){
		        	 alert("error");
		         }
			 });
		 },
		 setType:function(){
			 var type_data,json;
			 type_data=[];
			 type_data.push({"text":"遥测","value":"1"},{"text":"遥信","value":"2"},{"text":"遥控","value":"3"});
        	 $("#sel_type").combobox("loadData", type_data);
 			 $("#sel_type").combobox('select',type_data[0].value);
 			 _this.setName();
			 $('#sel_type').combobox({
		         onChange:function(n,o){
		            _this.setName();
		         }
			 });
			 $("#sel_type").combobox('select',type_data[0].value);
		 },
		 setName:function(){
			 var type=$("#sel_type").combobox('getValue');
			 var name=$("#sel_dname").combobox('getValue');
			 var ld_name=name.substring(0,name.indexOf("/"));
			 var ln_name=name.substring((name.indexOf("/")+1));
			 var formData = {};
			 formData['ld_inst_name'] = ld_name;
			 formData['ln_inst_name'] = ln_name;
			 var _url="";
			 if(type=="1")
			 _url="getycNameList.do";
			 if(type=="2")
			 _url="getyxNameList.do";
			 if(type=="3")
			 _url="getykNameList.do";
			 $.ajax({
				async:false,
				cache:false,
				type:'post',
				data:formData,
				url:_url,
				success:function(data){
					 var name_data,json;
		        	 name_data=[];
		        	 if(data.rows.length==0){
		        		 $("#sel_instName").combobox("loadData", {"text":"无数据","value":"无数据"});
		        		 $("#sel_instName").combobox('select',"无数据");
		        	 }
		        	 for(var i=0;i<data.rows.length;i++)
		        	 {
		        		 var temp_name=data.rows[i].yc_refname;
		        		 temp_name=temp_name.substring(temp_name.lastIndexOf("$")+1);
		        		 name_data.push({"text":temp_name,"value":data.rows[i].yc_id});
		        	 }
		        	 if(data.rows.length>0)
		        	 {
		        		 $("#sel_instName").combobox("loadData", name_data);
			 			 $("#sel_instName").combobox('select',name_data[0].value);
		        	 }
				},
				error:function(){

				}
			 });

		 },
		 setEditWin:function(){
			 deviceWin = $('#editI2-window').window({
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
				uploadWin = $('#ExcelUpload-window').window({
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
		 setI2Table:function(){
			 var _box = null;
			 _box = new YDataGrid(_this.showI2Data,"#i2List",true,false,true,true);
			 _box.grid.datagrid.defaults.onDblClickRow=_this.update_I2;
			 _box.init();
		 },
		 update_I2:function(rowIndex, rowData){
			 //设置窗口值
			 _this.setDeviceName();
			 //根据i1id和yc类型获取IEC61850LD_LN
			 var formData = {};
			 formData['i1type'] = rowData.i1type;
			 formData['i1id'] = rowData.i1id;
			 $.ajax({
					async:false,
					cache:false,
					type:'post',
					data:formData,
					url:"getIEC61850LD_LN.do",
					success:function(data){
						$("#sel_dname").combobox('select',data.rows[0].ld_inst_name+"/"+data.rows[0].ln_inst_name);
					},
					error:function(){
						}
			 });
			 $("#sel_type").combobox('select',rowData.i1type);
			 $("#sel_instName").combobox('select',rowData.i1id);
			 $("#txt_I2_ID").val(rowData.i2id);
			 $("#txt_desc").val(rowData.i2_desc);

			 //打开窗口
			 deviceWin.window('open');


		 },
		 i1ToI2_sub:function(){
			 var i2id=$("#txt_I2_ID").val();
			 if(i2id==""){
				 $("#msg_I2_ID").html("ID不能为空!");
				 return;
			 }
			 var i1type=$("#sel_type").combobox('getValue');
			 var i1id=$("#sel_instName").combobox('getValue');
			 var i2_refname=$("#sel_instName").combobox('getText');
			 if(i2_refname=="无数据"){
				 alert("没有设备!");
				 return;
			 }
			 var i2_desc=$("#txt_desc").val();
			 var formdata={};
			 formdata['i2id'] = i2id;
			 formdata['i1type'] = i1type;
			 formdata['i1id'] = i1id;
			 formdata['i2_refname'] = i2_refname;
			 formdata['i2_desc'] = i2_desc;
			 $.ajax({
				 async:false,
				 cache:false,
				 type:'post',
				 url:"i2TableCommmit.do",
				 data:formdata,
				 success:function(data){
					 deviceWin.window('close');
		        	 $('#i2List').datagrid('reload');
		        	 return
				 },
				 error:function(){
					 alert("插入失败,请检查远传点号是否重复!")
				 }
			 });


		 },
		 export_Exl:function(){
			 var myDate = new Date();
			 var Day=myDate.toLocaleDateString();
			 var mytime=myDate.toLocaleTimeString();
			 alert("注意：导出的Excel文件为只读文件,修改后请另存为 .xls 文件");
				$.ajax({
			        async: false,
			        cache: false,
			        type: 'POST',
			        url: "getI2Data_export.do",
			        error: function(){// 请求失败处理函数
			        	alert("连接数据库失败");
			        },
			        success: function(data){
				        	var head_data,json;
				        	head_data = [];
				        	head_data.push({"title":"远传点号","value":"i2id"},{"title":"上传类型","value":"i1type"},
				        			{"title":"I1编号","value":"i1id"},{"title":"名称","value":"i2_refname"},{"title":"备注","value":"i2_desc"});
				        	tableExport(data,head_data,"excel","CAC远传映射配置表"+Day+mytime);
			        }
				});
			},
		 Import_Exl:function(){
				uploadWin.window('open');
		 },
		 i2Del:function(){
			    var select_data = $("#i2List").datagrid("getSelected");
			    if(select_data==null){
			    	alert("请选择设备");
			    	return;
			    }
				var formData = {};
			    var url="delete_I2.do";
			    formData['i2id'] = select_data.i2id;
				$.ajax({
			         async: false,
			         cache: false,
			         type: 'POST',
			         url: url,
			         data: formData,
			         error: function(){// 请求失败处理函数
			        	 alert("false");
			         },
			         success: function(data){
			        	 $('#i2List').datagrid('reload');
			         }
			     });
		 },
		 showI2Data:{
				dataGrid:{
					 //title: $("#txtName").val(),
					 url:'getI2Data.do',
					 columns: [[{
			                field: 'i2id',
			                title: '远传点号',
			                align: 'center',
			                sortable: true,
			                width: fixWidth(0.12),
			                formatter: function(value, row, index){
			                    return row.i2id;
			                }
			            }, {
			                field: 'i1type',
			                title: '上传类型',
			                align: 'center',
			                sortable: true,
			                width: fixWidth(0.12),
			                formatter: function(value, row, index){
			                	if(row.i1type=="1"){
			                		return "遥测";
			                	}
			                	if(row.i1type=="2"){
			                		return "遥信";
			                	}
			                	if(row.i1type=="3"){
			                		return "遥控";
			                	}
			                    return row.i1type;
			                }
			            }, {
			                field: 'i1id',
			                title: 'I1编号',
			                align: 'center',
			                sortable: true,
			                width: fixWidth(0.12),
			                formatter: function(value, row, index){
			                    return row.i1id;
			                }
			            }, {
			                field: 'i2_refname',
			                title: '名称',
			                align: 'center',
			                sortable: true,
			                width: fixWidth(0.12),
			                formatter: function(value, row, index){
			                    return row.i2_refname;
			                }
			            }, {
			                field: 'i2_desc',
			                title: '备注',
			                align: 'center',
			                sortable: true,
			                width: fixWidth(0.12),
			                formatter: function(value, row, index){
			                    return row.i2_desc;
			                }
			            }]],
			  			toolbar: ["-", {
			  	            id: '',
			  	            text: '添加',
			  	            iconCls: '',
			  	            handler: function () {
			  	            	deviceWin.window('open');
			  	            //加载设备名列表
			  	            	_this.setDeviceName();

			  	            }
			  	        }, "-", {
			  	            id: '',
			  	            text: '删除',
			  	            iconCls: '',
			  	            handler: function () {
			  	            	_this.i2Del();
			  	            }
			  	        }, "-", {
			  	            id: '',
			  	            text: '导出',
			  	            iconCls: '',
			  	            handler: function () {
			  	            	_this.export_Exl();
			  	            }
			  	        }, "-", {
			  	            id: '',
			  	            text: '导入',
			  	            iconCls: '',
			  	            handler: function () {
			  	            	_this.Import_Exl();
			  	            }
			  	        }, "-"]
					}
		 	}
	};
	return _this;

}();
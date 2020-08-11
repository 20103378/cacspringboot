$package('jeecg.i1toI2_103');

$(function () {
	$(".tabs li:eq(5)").click(function(){
		//加载I2列表
		jeecg.i1toI2_103.setI2Table();
		//监听导入
		jeecg.i1toI2_103.setBtn();
		//初始化I2添加修改窗口
//		jeecg.i1toI2.setEditWin();
		//初始化EXCEL导入窗口
//		jeecg.i1toI2.upload();
		//按钮提交 事件监听
//	  	$("#bt_sub").click(function(){
//	  		jeecg.i1toI2.i1ToI2_sub();
//	  	});
	});
});
jeecg.i1toI2_103 = function(){
	var _this = {
		setBtn:function(){
			$("#bt_dxh_103").unbind('click').click(function(){
				var str_dxh = $("#sel_zh_103").val();
				var num_dxh = parseInt(str_dxh);
				//获取所有选中行
				var selected = $('#i2List_103').datagrid('getSelections');
				//获取选中行行号列表
				var indexs = new Array();
				for(var i = 0 ; i<selected.length ;i++){
					indexs.push($('#i2List_103').datagrid('getRowIndex',selected[i]));
				}
				if(num_dxh>=0&&indexs!=''){
						for(var i=0;i<selected.length;i++,num_dxh++){
							var comm=selected[i].deviceID;
							$("#CommAddress_"+comm).val(num_dxh);
						}
				}else{
					alert("请选择设备");
					return;
				}
			});
		},
		setEditWin:function(){
		},
		setI2Table:function(){
			$.ajax({
				async:false,
				cache:false,
				type:'post',
				url:" getI2Data_103",
				success:function(data){
					_this.show103Data();
					$('#i2List_103').datagrid('loadData',data);
					$('#i2List_103').datagrid('reload');
				}
			});
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
					url:"getIEC61850LD_LN",
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
				 url:"i2TableCommmit",
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
		 ZJ103Del:function(){
			var selected = $('#i2List_103').datagrid('getSelections');
		    //获取选中行行号列表
			var indexs = new Array();
			for(var i = 0 ; i<selected.length ;i++){
				indexs.push($('#i2List_103').datagrid('getRowIndex',selected[i]));
			}
			if(indexs!=''){
				var D_ID = new Array();
				for(var i=0;i<selected.length;i++){
					D_ID.push(selected[i].deviceID);
				}

				$.ajax({
			         async: false,
			         cache: false,
			         type: 'POST',
			         url: "delete_103?DeviceIDs="+D_ID,
			         error: function(){// 请求失败处理函数
			        	 alert("false");
			         },
			         success: function(data){
			        	 alert("已成功删除:"+data.aa+" 条数据。");
						 $('#i2List_103').datagrid('loadData',data);
			        	 $('#i2List_103').datagrid('reload');
			         }
			     });
			}else{
				alert("请选择设备");
				return;
			}
		 },
		 submit:function(){
			    var selected = $('#i2List_103').datagrid('getSelections');
			    if(selected==null){
			    	alert("请选择设备");
			    	return;
			    }
			    //获取选中行行号列表
				var indexs = new Array();
				for(var i = 0 ; i<selected.length ;i++){
					indexs.push($('#i2List_103').datagrid('getRowIndex',selected[i]));
				}
				if(indexs!=''){
					var D_ID = new Array();
					var M_Com= new Array();
					var M_Dp = new Array();
					for(var i=0;i<selected.length;i++){
						D_ID.push(selected[i].deviceID);
						var comm=selected[i].deviceID;
						var CommAddress_val=$("#CommAddress_"+selected[i].deviceID+"").val();
						M_Com.push(CommAddress_val);
						var DevPhase_val=$("#DevPhase_"+selected[i].deviceID+"").val();
						M_Dp.push(DevPhase_val);
					}
				$.ajax({
					async:false,
					cache:false,
					type:'post',
					url:"submit_103_devPhase?DeviceIDs="+D_ID+"&devPhases="+M_Dp+"&CommAds="+M_Com,
					success:function(data){
						var sgcb_data=data;
						alert("提交成功");
						$('#i2List_103').datagrid('loadData',sgcb_data);
						$('#i2List_103').datagrid('reload');
					}
				});
			}else{
				alert("请选择设备");
				return;
			}
		 },
		 show103Data:function(){
			 $('#i2List_103').datagrid({
			 // title: $("#txtName").val(),
			// url:'getI2Data_103',
				fit:true,
//				pagination:true,
				border:false,
				nowrap: true,
				autoRowHeight: false,
				striped: false,
				collapsible:false,
				remoteSort: false,
				rownumbers:true,
				checkOnSelect:false,
				selectOnCheck:false,
				columns: [[{
			        field: 'DeviceID',
					title: 'DeviceID',
					align: 'center',
				    sortable: true,
				    width: fixWidth(0.12),
				    formatter: function(value, row, index){
				        return row.deviceID;
				    }
				}, {
				    field: 'DeviceName',
					title: 'DeviceName',
					align: 'center',
				    sortable: true,
				    width: fixWidth(0.12),
				    formatter: function(value, row, index){
				        return row.deviceName;
				    }
				}, {
				    field: 'DeviceType',
					title: 'DeviceType',
					align: 'center',
				    sortable: true,
				    width: fixWidth(0.12),
				    formatter: function(value, row, index){
				        return row.deviceType;
				    }
				}, {
				    field: 'IEC61850LD_LN',
					title: 'IEC61850LD_LN',
					align: 'center',
				    sortable: true,
				    width: fixWidth(0.12),
				    formatter: function(value, row, index){
				        return row.IEC61850LD_LN;
				    }
				}, {
				    field: 'CommAddress',
					title: 'CommAddress',
					align: 'center',
					sortable: true,
					width: fixWidth(0.12),
					formatter: function(value, row, index){
						return "<input id='CommAddress_"+row.deviceID+"' value='"+row.commAddress+"'class='easyui-textbox'/>";
				    }
				}, {
				    field: 'DevPhase',
					title: 'DevPhase(0代表无相别，1代表A相，2代表B相，3代表C相)',
					align: 'center',
					sortable: true,
					width: fixWidth(0.2),
					formatter: function(value, row, index){
						return "<input type='text' data-options='required:true,validType:'integer'' id='DevPhase_"+row.deviceID+"' value='"+row.devPhase+" 'class='easyui-validatebox'/>";
				    }
				}]],
				toolbar: [ "-", {
					id: '',
					text: '反选',
					iconCls: '',
					handler: function () {
						$("#list_103  tr").each(function(idx){
							if(idx>1){
								var cls = $(this).attr("class");
								if(cls=="datagrid-row datagrid-row-selected"){
									$(this).attr("class","datagrid-row");
								}else if(cls=="datagrid-row"){
									$(this).attr("class","datagrid-row datagrid-row-selected");
							    }
							}
					    })
					}
				},"-", {
				id: '',
				text: '删除',
				iconCls: '',
				    handler: function () {
				    	_this.ZJ103Del();
				    }
				}, "-", {
					id: '',
					text: '提交',
					iconCls: '',
					    handler: function () {
					    	_this.submit();
					    }
					}, "-"]
			});
		}
//		 show103Data:{
//
//		 	}
	};
	return _this;

}();

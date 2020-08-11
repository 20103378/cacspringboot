$package('jeecg.SpaceConfiguration');
//定义窗口
$(function () {
	$(".tabs li:eq(8)").click(function(){
	//加载备份文件夹下的文件
	jeecg.backup.showFile();
	jeecg.backup.restoreWin();
	});
  	$("#enter").click(function(){
  		jeecg.backup.restore();
  	});
  	$("#exit").click(function(){
  		restoreWin.window("close");
  	});

});
jeecg.backup = function(){
	var _this = {
		backup_all:function(){
//			jeecg.progress('正在备份','请稍后...');
			 $.ajax({
				 async:false,
				 cache:false,
				 type:'post',
				 url:"backup_all",
				 success:function(data){
//		 			 jeecg.closeProgress();
					 alert("备份已完成");
				 }
			 });
			 _this.showFile();
		},
		restore:function(){
			var backup_data = $("#backup_tab").datagrid("getSelected");
			if(backup_data==null||backup_data==''){
				alert("请选择需要还原的版本");
			}else{
				var time=backup_data.time;
				var recars=new Array();
				if(backup_data.l1DataMgr){
					recars[0]="1";
				}else{
					recars[0]="0";
				}
				if(backup_data.iec61850){
					recars[1]="1";
				}else{
					recars[1]="0";
				}
				if(backup_data.ZJCAC103Server){
					recars[2]="1";
				}else{
					recars[2]="0";
				}
				restoreWin.window("close");
//				jeecg.progress('正在还原','请稍后...');
				 $.ajax({
					 async:false,
					 cache:false,
					 type:'post',
					 url:"restore?time="+time+"&recars="+recars,
					 success:function(data){
//						 jeecg.closeProgress();
						 alert("还原已完成");
					 }
				 });
			}
		},
		backup_button:function(){
			jeecg.progress('正在备份','请稍后...');
			 $.ajax({
				 async:false,
				 cache:false,
				 type:'post',
				 url:"backup_button",
				 success:function(data){
					 jeecg.closeProgress();
					 $('#backup_tab').datagrid('reload');
//					 alert("备份已完成");
				 }
			 });
			 _this.showFile();
		},
		restoreWin : function() {
			restoreWin = $('#backupWin').window({
				href : '',
				title : '添加/修改',
				left : '100px',
				top : '70px',
				closed : true,
				modal : false,
				cache : false,
				minimizable : false,
				maximizable : false,
				collapsible : false,
				shadow : false
			});

		},
		showFile:function(){
			 $.ajax({
				 async:false,
				 cache:false,
				 type:'post',
				 url:"backup",
				 success:function(data){
					 _this.data_config();
					$('#backup_tab').datagrid('loadData',data);
				 }
			 });
		},
		data_config:function(){
			$('#backup_tab').datagrid({
				pagination:false,
				singleSelect:true,
			onDblClickRow:function(){
				//预留双击事件
			},columns:  [[{
	                field: 'time',
	                title: '备份时间',
	                align: 'center',
	                sortable: true,
	                width: fixWidth(0.15),
	                formatter: function(value, row, index){
	                    return row.time;
	                }
	            },{
	                field: 'l1DataMgr',
	                title: 'l1DataMgr',
	                align: 'center',
	                sortable: true,
	                width: fixWidth(0.2),
	                formatter: function(value, row, index){
	                    return row.l1DataMgr;
	                }
	            },{
	                field: 'iec61850',
	                title: 'iec61850',
	                width: fixWidth(0.2),
	                align: 'center',
	                sortable: true,
	                formatter: function(value, row, index){
	                    return row.iec61850;
	                }
	            },{
	                field: 'ZJCAC103Server',
	                title: 'ZJCAC103Server',
	                width: fixWidth(0.21),
	                align: 'center',
	                sortable: true,
	                formatter: function(value, row, index){
	                    return row.ZJCAC103Server;
	                }
	            },{
	                field: 'sqlbase',
	                title: '数据库备份',
	                width: fixWidth(0.21),
	                align: 'center',
	                sortable: true,
	                formatter: function(value, row, index){
	                    return row.sqlbase;
	                }
	            }
	            ]],
				toolbar: [ "-", {
					id: '',
					text: '版本备份',
					iconCls: '',
					handler: function () {
						jeecg.backup.backup_all();
					}
				},"-", {
				id: '',
				text: '版本还原',
				iconCls: '',
				    handler: function () {
//				    	jeecg.backup.restore();
				    	restoreWin.window('open');
				    }
				}, "-", {
					id: '',
					text: '备份数据库',
					iconCls: '',
					    handler: function () {
					    	jeecg.backup.backup_button();
					    }
					}, "-"]
			});
		}
	};
	return _this;
}();

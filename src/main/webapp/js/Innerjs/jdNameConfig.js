//测量量映射配置
$package('jeecg.jdNameConfig');
//定义窗口
$(function () {
	$(".tabs li:eq(7)").click(function(){
		//开始初始化下拉框
		jeecg.jdNameConfig.setSel();
	});
});
jeecg.jdNameConfig = function(){
	var _this = {
		setSel:function(){
			 //初始化iedName下拉框
			 var iedName = $("#tab_ldein").datagrid("getSelected").arName;
			 $("#sel_iedName").val(iedName);
			 //通过ajax请求数据
			 var formData = {};
			 formData['fileName'] = iedName;
			 $.ajax({
					async:false,
					cache:false,
					type:'post',
					data:formData,
					url:"getJdList",
					success:function(data){
						_this.data_config();
						$('#jdName_div').datagrid('loadData',data);
						$('#jdName_div').datagrid('reload');
					}
			 });
		},
		data_config:function(){
			$('#jdName_div').datagrid({
			onDblClickRow:function(){
				//预留双击事件
			},
			columns:  [[{
                field: 'ied_name',
                title: 'ied_name',
                width:140,
                align: 'center',
                formatter: function(value, row, index){
                    return row.ied_name;
                }
            },
            {
                field: 'ied_desc',
                title: 'ied描述',
                width:280,
                align: 'center',
                formatter: function(value, row, index){
                    return row.ied_desc;
                }
            },{
                field: 'ld_inst_name',
                title: 'ld_inst_name',
                width:180,
                align: 'center',
                sortable: true,
                formatter: function(value, row, index){
                    return row.ld_inst_name;
                }
            },{
                field: 'ld_inst_desc',
                title: 'ld描述',
                width:180,
                align: 'center',
                formatter: function(value, row, index){
                    return row.ld_inst_desc;
                }
            },{
                field: 'ln_inst_name',
                title: 'ln_inst_name',
                width:157,
                align: 'center',
                formatter: function(value, row, index){
                    return row.ln_inst_name;
                }
            },{
                field: 'ln_inst_desc',
                title: 'ln描述',
                width:280,
                align: 'center',
                formatter: function(value, row, index){
                    return row.ln_inst_desc;
                }
            }]],
            toolbar: [ "-", {
  	            id: '',
  	            text: '同步至数据库',
  	            iconCls: '',
  	            handler: function () {
  	            	var _data = $("#jdName_div").datagrid("getRows");
  	            	loadingDiv=$("#loading");
  	            	loadingDiv.openMask('正在同步至数据库。。。');
  	            	$.ajax({
	  	  				async:false,
	  	  				cache:false,
	  	  				traditional:true,
	  	  				type:'post',
	  	  				url:"JdListTODB",
	  	  				data:{"list":JSON.stringify(_data)},
                        dataType:json,
	  	  				success:function(){
	  	  					loadingDiv.closeMask("正在同步至数据库。。。");
	  	  					alert("数据库同步成功!");
	  	  				},
	  	  				error:function(){
		  	  				loadingDiv.closeMask("正在同步至数据库。。。");
	  	  					alert("同步失败");
	  	  				}
	  	  			});

  	            }
  	        }, "-"]
			});
		}
	};
	return _this;
}();
function fixWidth(percent)
{
    return document.body.clientWidth * percent ;
}
Array.prototype.serializeObject = function (lName) {
    var o = {};
    $t = lName;

    for (var i = 0; i < $t.length; i++) {
        for (var item in $t[i]) {
            o[lName+'[' + i + '].' + item.toString()] = $t[i][item].toString();
        }
    }
    return o;
};
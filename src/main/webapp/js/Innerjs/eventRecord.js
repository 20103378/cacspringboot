$package('jeecg.eventRecord');
jeecg.eventRecord = function(){
	var _this = {
			setTime:function(){
				var curr_time = new Date();
				var stat_time=new Date(curr_time.getTime()-1000*60*60*24*7);
				$("#tbStart").datebox("setValue",myformatter(stat_time));
				$("#tbEnd").datebox("setValue",myformatter(curr_time));
			},
			ShowEventRecord:function(){
				var txtRecords=$('#ddlRecords').datebox('getValue');
				var txtDevType=$('#ddlDevType').datebox('getValue');
				var txtAlarmType=$('#ddlAlarmType').datebox('getValue');
				var txtStartTime=$('#tbStart').datebox('getValue');
				var txtEndTime=$('#tbEnd').datebox('getValue');
				if(txtRecords==0){
					// $("#OperaterRecord_div").hide();
					$("#AlarmRecord_div").show();
					_box = null;
					_this.alarmRecord_config.dataGrid.url="getAlarmRecordList?DeviceType="+txtDevType+"&Start="+txtStartTime+"&End="+txtEndTime+"&RecordType="+txtAlarmType;
					_box = new YDataGrid(_this.alarmRecord_config,'#AlarmRecordR_list',false,true,true,true);
					_box.init();

				}
			},
			alarmRecord_config:{
			    dataGrid:{
				title:'告警事件',
		            columns:  [[{
		                field: 'deviceName',
		                title: '设备名称',
		                width:180,
		                align: 'center',
		                sortable: true,
		                formatter: function(value, row, index){
		                    return row.deviceName;
		                }
		            },
		            {
		                field: 'phase',
		                title: '设备相别',
		                width:120,
		                align: 'center',
		                sortable: true,
		                formatter: function(value, row, index){
		                    return row.phase;
		                }
		            },{
		                field: 'trigger_Date',
		                title: '事件时间',
		                width:180,
		                align: 'center',
		                sortable: true,
		                formatter: function(value, row, index){
		                    return row.trigger_Date;
		                }
		            },{
		                field: 'recordType',
		                title: '事件内容',
		                width:220,
		                align: 'center',
		                sortable: true,
		                formatter: function(value, row, index){
		                	if(row.recordType==0){
		                		return "设备恢复正常";
		                	}
		                	else if(row.deviceType=="1"){
		                		if(row.recordType & 0x01000000){
		                			return '<div class="tt-inner" style="padding:0;margin:0;color:red" >'+YSP_trans(row.recordType)+'</div>';
		                		}else{
		                			return '<div class="tt-inner" style="padding:0;margin:0;color:yellow" >'+YSP_trans(row.recordType)+'</div>';
		                		}
		                	}
		                	else if(row.deviceType=="2"){
		                		if(row.recordType & 0x01000000){
		                			return '<div class="tt-inner" style="padding:0;margin:0;color:red" >'+SF6_trans(row.recordType)+'</div>';
		                		}else{
		                			return '<div class="tt-inner" style="padding:0;margin:0;color:yellow" >'+SF6_trans(row.recordType)+'</div>';
		                		}
		                	}
		                	else if(row.deviceType=="3"){
		                		if(row.recordType & 0x01000000){
		                			return '<div class="tt-inner" style="padding:0;margin:0;color:red" >'+Smoam_trans(row.recordType)+'</div>';
		                		}else{
		                			return '<div class="tt-inner" style="padding:0;margin:0;color:yellow" >'+Smoam_trans(row.recordType)+'</div>';
		                		}
		                	}
		                	else if(row.deviceType=="4"){
		                		if(row.recordType & 0x01000000){
		                			return '<div class="tt-inner" style="padding:0;margin:0;color:red" >'+Scom_trans(row.recordType)+'</div>';
		                		}else{
		                			return '<div class="tt-inner" style="padding:0;margin:0;color:yellow" >'+Scom_trans(row.recordType)+'</div>';
		                		}
		                	}
		                	else if(row.deviceType=="5"){
		                		if(row.recordType & 0x01000000){
		                			return '<div class="tt-inner" style="padding:0;margin:0;color:red" >'+Scondition_trans(row.recordType)+'</div>';
		                		}else{
		                			return '<div class="tt-inner" style="padding:0;margin:0;color:yellow" >'+Scondition_trans(row.recordType)+'</div>';
		                		}
		                	}

		                }
		            }]]
			    }
				}
	};
	return _this;
}();
$(function () {
	jeecg.eventRecord.setTime();
	jeecg.eventRecord.ShowEventRecord();
	$('#ddlRecords').combobox({
		 onChange: function () {
			 jeecg.eventRecord.ShowEventRecord();
		 }
	});
	$('#ddlDevType').combobox({
		 onChange: function () {
			 jeecg.eventRecord.ShowEventRecord();
		 }
	});
	$('#ddlAlarmType').combobox({
		 onChange: function () {
			 jeecg.eventRecord.ShowEventRecord();
		 }
	});
	$('#search').unbind('click').click(function(){
		jeecg.eventRecord.ShowEventRecord();
	});
});


//工况告警翻译
function Scondition_trans(a)
{
    var str = "";
    var remark=parseInt(a);

    if ((remark & 0x08000000) == 0x08000000)
    {
        if ((remark & 0x08000800) == 0x08000800)
        {
            str += "套管1三相增量差值告警(日)";
        }
        if ((remark & 0x08002000) == 0x08002000)
        {
            str += "套管1三相增量差值告警(周)";
        }
        if ((remark & 0x08008000) == 0x08008000)
        {
            str += "套管1三相增量差值告警(月)";
        }
        if ((remark & 0x08001000) == 0x08001000)
        {
            str += "套管2三相增量差值告警(日)";
        }
        if ((remark & 0x08004000) == 0x08004000)
        {
            str += "套管2三相增量差值告警(周)";
        }
        if ((remark & 0x08010000) == 0x08010000)
        {
            str += "套管2三相增量差值告警(月)";
        }

    }
    if ((remark & 0x04000000) == 0x04000000)
    {
        if ((remark & 0x04000200) == 0x04000200)
        {
            str += "套管sf6压力1日变化率告警";
        }
        if ((remark & 0x04000400) == 0x04000400)
        {
            str += "套管sf6压力2日变化率告警";
        }
    }
    if ((remark & 0x02000000) == 0x02000000)
    {
    	if ((remark & 0x02000004) == 0x02000004)
        {
            str += "顶层油温阈值告警";
        }
    	if ((remark & 0x02000004) == 0x02000008)
        {
            str += "网侧绕组温度阈值告警";
        }
    	if ((remark & 0x02000010) == 0x02000010)
        {
            str += "阀侧绕组温度阈值告警";
        }
    	if ((remark & 0x02000020) == 0x02000020)
        {
            str += "套管sf6压力1阈值告警";
        }
    	if ((remark & 0x02000040) == 0x02000040)
        {
            str += "套管sf6压力2阈值告警";
        }
    	if ((remark & 0x02000080) == 0x02000080)
        {
            str += "有载开关油位阈值告警";
        }
        if ((remark & 0x02000100) == 0x02000100)
        {
            str += "主油箱油位阈值告警";
        }
    }
    if ((remark & 0x01000000) == 0x01000000)
    {
    	if ((remark & 0x01000001) == 0x01000001)
        {
            str += "设备连接异常";
        }
    	if ((remark & 0x01000002) == 0x01000002)
        {
            str += "设备采集异常";
        }
    }
    return str;
}

/// 铁芯告警翻译
function Scom_trans(rek)
{
	var str = "";
    var remark=parseInt(rek);
    if ((remark & 0x02000000) == 0x02000000)
    {
    	if ((remark & 0x02000004) == 0x02000004)
        {
            str += "接地电流阈值告警";
        }
    }
    if ((remark & 0x01000000) == 0x01000000)
    {
    	if ((remark & 0x01000001) == 0x01000001)
        {
            str += "设备连接异常";
        }
    	if ((remark & 0x01000002) == 0x01000002)
        {
            str += "设备采集异常";
        }
    }
    return str;
}

/// 避雷器告警翻译
function Smoam_trans(rek)
{
	var str = "";
    var remark=parseInt(rek);
    if ((remark & 0x02000000) == 0x02000000)
    {
    	if ((remark & 0x02000004) == 0x02000004)
        {
            str += "接地电流阈值告警";
        }
    }
    if ((remark & 0x01000000) == 0x01000000)
    {
    	if ((remark & 0x01000001) == 0x01000001)
        {
            str += "设备连接异常";
        }
    	if ((remark & 0x01000002) == 0x01000002)
        {
            str += "设备采集异常";
        }
    }
    return str;
}
/// 油色谱告警翻译
  function YSP_trans(rek)
  {
	  var str = "";
	  var remark=parseInt(rek);

	  if ((remark & 0x08000000) == 0x08000000)
      {
		  if ((remark & 0x08001000) == 0x08001000)
          {
              str += "H2三相增量对比告警";
          }
		  if ((remark & 0x08002000) == 0x08002000)
          {
              str += "C2H2三相增量对比告警";
          }
		  if ((remark & 0x08004000) == 0x08004000)
          {
              str += "TH三相增量对比告警";
          }

      }
	  if ((remark & 0x04000000) == 0x04000000)
      {
    	  if ((remark & 0x04000008) == 0x04000008)
          {
              str += "H2变化率告警";
          }
    	  if ((remark & 0x04000010) == 0x04000010)
          {
              str += "C2H2变化率告警";
          }
    	  if ((remark & 0x04000020) == 0x04000020)
          {
              str += "TH变化率告警";
          }
    	  if ((remark & 0x04000040) == 0x04000040)
          {
              str += "微水变化率告警";
          }
      }
      if ((remark & 0x02000000) == 0x02000000)
      {
    	  if ((remark & 0x02000100) == 0x02000100)
          {
              str += "H2阈值告警";
          }
    	  if ((remark & 0x02000200) == 0x02000200)
          {
              str += "C2H2阈值告警";
          }
    	  if ((remark & 0x02000400) == 0x02000400)
          {
              str += "TH阈值告警";
          }
    	  if ((remark & 0x02000800) == 0x02000800)
          {
              str += "微水阈值告警";
          }
      }
      if ((remark & 0x01000000) == 0x01000000)
      {
      	if ((remark & 0x01000001) == 0x01000001)
          {
              str += "设备连接异常";
          }
      	if ((remark & 0x01000002) == 0x01000002)
          {
              str += "设备采集异常";
          }
      	if ((remark & 0x01000004) == 0x01000004)
        {
      		 str += "载气欠压";
        }
      }
      return str;
  }

  /// SF6告警翻译
  function SF6_trans(rek)
  {
	  var str = "";
	  var remark=parseInt(rek);

	  if ((remark & 0x08000000) == 0x08000000)
      {
      	  if ((remark & 0x08004000) == 0x08000100)
          {
              str += "SF6月增量告警";
          }
      	  if ((remark & 0x08002000) == 0x08000080)
          {
              str += "SF6周增量告警";
          }
          if ((remark & 0x08001000) == 0x08000040)
          {
              str += "SF6日增量告警";
          }

      }
      if ((remark & 0x04000000) == 0x04000000)
      {
      	  if ((remark & 0x04000008) == 0x04000008)
          {
              str += "Sf6日变化率告警";
          }
      	  if ((remark & 0x04000010) == 0x04000010)
          {
              str += "Sf6周变化率告警";
          }
          if ((remark & 0x04000020) == 0x04000020)
          {
              str += "Sf6月变化率告警";
          }
      }
      if ((remark & 0x02000000) == 0x02000000)
      {
      	if ((remark & 0x02000004) == 0x02000004)
          {
              str += "Sf6压力阈值告警";
          }
      }
      if ((remark & 0x01000000) == 0x01000000)
      {
      	if ((remark & 0x01000001) == 0x01000001)
          {
              str += "设备连接异常";
          }
      	if ((remark & 0x01000002) == 0x01000002)
          {
              str += "设备采集异常";
          }
      }
      return str;
  }

  function Operater_trans(rek){
	  var str = "";
	  var remark=parseInt(rek);
	   if(remark == 1){
		   str+="新增用户!";
	  }else if(remark == 2){
		  str+="删除用户!";
	  }else if(remark == 3){
		  str+="修改用户!";
	  }else if(remark == 4){
		  str+="新增区域!";
	  }else if(remark == 5){
		  str+="删除区域!";
	  }else if(remark == 6){
		  str+="修改区域!";
	  }else if(remark == 7){
		  str+="新增设备!";
	  }else if(remark == 8){
		  str+="删除设备!";
	  }else if(remark == 9){
		  str+="修改设备!";
	  }else{
		  str+="未知操作!";
	  }
	 return str;
  }

  function myformatter(date){
	    var y = date.getFullYear();
	    var m = date.getMonth()+1;
	    var d = date.getDate();
	    return y+'-'+(m<10?('0'+m):m)+'-'+(d<10?('0'+d):d);
	}

	function myparser(s){
	    if (!s) return new Date();
	    var ss = (s.split('-'));
	    var y = parseInt(ss[0],10);
	    var m = parseInt(ss[1],10);
	    var d = parseInt(ss[2],10);
	    if (!isNaN(y) && !isNaN(m) && !isNaN(d)){
	        return new Date(y,m-1,d);
	    } else {
	        return new Date();
	    }
	}


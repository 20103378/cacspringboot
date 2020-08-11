$package('jeecg.chart');
var id=null;
var startTime=null;
var endTime=null;
var text_select=null;
var DeviceType=null;
var _startTime=null;
var _endTime=null;
var xml="";
var _DeviceName=null;
var CLdata,json;
CLdata = [];

var DeviceTypes = '';
var Clldata = [];
$(function () {
	//设置初始时间
	$(".tabs li:eq(2)").click(function(){
		$("#d2").show();
		jeecg.chart.setTime();
		//初始化下拉框值
		jeecg.chart.getSelectValue();
//		$('#showChat').click(showChat);
		$('#showChat').unbind('click').click(function(){
			showChat();
		});
	});
	$('#DTorCL').unbind('click').click(function(){
		if($('#DTorCL').attr('checked')){
			  $('.TD').attr('disabled',true);
			  $('.CL').removeAttr('disabled');
			}else{
			   $('.TD').removeAttr('disabled');
			   $('.CL').attr('disabled',true);
			}
		});

});
jeecg.chart = function(){
	var _box = null;
	var _this = {
			ShowHistoryData:function(){
			},
			setTime:function(){
				var curr_time = new Date();
				var stat_time=new Date(curr_time.getTime()-1000*60*60*24*7);
				if(_st==null){
					_st=myformatter(stat_time);
				}
				if(_et==null){
					_et=myformatter(curr_time);
				}
				$("#_startTime").datebox("setValue",_st);
				$("#_endTime").datebox("setValue",_et);
			},
			getSelectValue:function(){
				_DeviceName=$("#txtName").val();
				DeviceType=$("#txtType").val();
				id=$("#txtID").val();
				CLdata = [];
	        	switch(DeviceType){
	        	case "1":CLdata.push({ "text": "H2", "value": "H2" },{ "text": "C2H2", "value": "C2H2" },{ "text": "C2H4", "value": "C2H4" }
	        			 ,{ "text": "C2H6", "value": "C2H6" },{ "text": "CH4", "value": "CH4" },{ "text": "CO", "value": "CO" },{ "text": "CO2", "value": "CO2" });
	        			 break;
	        	case "2":CLdata.push({ "text": "压力", "value": "Pres" },{ "text": "温度", "value": "Tem" },{ "text": "湿度", "value": "Hum" });
   			 			 break;
	        	case "3":CLdata.push({ "text": "全电流", "value": "TotA" });
	        			break;
	        	case "4":CLdata.push({ "text": "铁芯泄漏电流", "value": "CGAmp" });
		 			 	break;
	        	case "19":CLdata.push({ "text": "放电次数", "value": "plsNum" });
		 			 	break;
	        	case "hwcw":CLdata.push({ "text": "温度", "value": "Tem" });
	        	}
//	        	$("#value_select").combobox("loadData", CLdata);
//	        	$("#value_select").combobox('select',CLdata[0].value);
	        	//找到所有的type设备
	        	$("#TypeDevice").empty();
	        	$("#cll").empty();
	        	$.ajax({
	                async: false,
	                cache: false,
	                type: 'POST',
	                url: "getDeviceByType?DeviceType="+DeviceType,
	                error: function(){// 请求失败处理函数
	               	 alert("false");
	                },
	                success: function(data){
	        			for(var nn=0;nn<data.length;nn++){
	        				$("#TypeDevice").append('<tr><td><input type="checkbox" class="TD" id="TypeDevice_'+data[nn].deviceID+'"/></td><td>'+data[nn].deviceName+'</td></tr>');
	        			}
	        			var TDID="TypeDevice_"+id;
	        			document.getElementById(TDID).checked=true;
	        			for(var ii=0;ii<CLdata.length;ii++){
//	        				alert(CLdata[ii]);
	        				$("#cll").append('<tr><td><input type="checkbox" class="CL" id="cll_'+CLdata[ii].value+'"/></td><td>'+CLdata[ii].text+'</td></tr>');
	        			}
//	        			alert("cll_"+CLdata[0].value);
	        			document.getElementById("cll_"+CLdata[0].value).checked=true;
	                }
	            });
	        	showChat();
	        	$('.CL').attr('disabled',true);
			}
	}
	return _this;
}();

function showChat() {
	//找到所有的type设备
	DeviceTypes="";
	Clldata=[];


//	 var _select=$('#value_select').combobox('getValue');
	 var formData = {};
     var url=null;
     formData['DeviceID'] = id;
     _st=$('#_startTime').datebox('getValue');
     _et=$('#_endTime').datebox('getValue');
     formData['_startTime']= _st+" 00-00-00";;
     formData['_endTime']= _et+" 24-00-00";;

     if($("#id").attr("checked")==true);
//     $("#TypeDevice input[type=checkbox]").prop("checked","checked");

     $("#TypeDevice").find(":checkbox:checked").each(function(){
         var val = $(this).attr('id');
         DeviceTypes =DeviceTypes + ","+val;
      });
//     alert("DeviceTypes="+DeviceTypes);
     formData['DeviceID'] = DeviceTypes;

     $("#cll").find(":checkbox:checked").each(function(){
         var val = $(this).parent().next().text();
         Clldata.push(val);
      });
//     alert("Clldata="+Clldata);
     switch(DeviceType)
	{
		case "1":
			url="getStomChartValue";
			break;
		case "2":
			url="getSF6ChartValue";
			break;
		case "3":
			url="getSmoamChartValue";
			break;
		case "4":
			url="getScomChartValue";
			break;
		case "19":
			url="getSpdmChart_Value";
			break;
		case "hwcw":
			url="getInfraredChart_Value";
	}
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
	        	 var datas = [];
	        	 for(var i = 1 ; i <= Object.keys(data).length ; i++){
	        		 var aa="dataList"+i+"";
	        		 if(eval('data.'+aa)==null||eval('data.'+aa)==''){
	        			 continue;
	        		 }
//	        		 datas = Object.values(data)[0];
	        		 datas.push(eval('data.'+aa)) ;
	        	 };
//        		 data=data.dataList;
	        	 if(datas!=null&&datas.length>0){
	        		 if($('#DTorCL').attr('checked')){
	        			 var xml=getXMLbyCLL(datas[0]);
//	        			 alert("勾选的单设备："+xml);
	        		 }else{
	        			 var xml=getXML(datas);
//	        			 alert("没勾选的多设备："+xml);
	        		 }
//	        		 alert("datas="+JSON.stringify(datas));

		        	 var chart = new FusionCharts(ctxPath+"/jsp/scott/Charts/FCF_MSLine.swf", "ChartId", fixWidth(0.78), fixHeight(0.85));
		        	 chart.setDataXML(xml);
	  		   		 chart.render("chartdiv");
	  		   		 showDataSpan(datas[0]);
	        	 }
//  		   		 if(datas!=null&&data.length>0){};
	         }
	     });
}

function showDataSpan(data){
	if(data.length<10){
		for(var i=0;i<data.length;i++){
			$("#tb"+(i+1)+"").text(data[i].sampleTime);
		}
	}else{
		var s1 =  data[0].sampleTime;
		var s2 =  data[data.length-1].sampleTime;
		s1 = new Date(s1.replace(/-/g, "/"));
		s2 = new Date(s2.replace(/-/g, "/"));
		var st =  s1.getTime();
		var timeSpan = (s2.getTime() - st)/10;
		for(var i=0;i<10;i++){
			$("#tb"+(i+1)+"").text(s1.getFullYear()+'/'+(s1.getMonth()+1)+'/'+s1.getDate()+' '+s1.getHours()+'时');
			st=st+timeSpan;
			s1 = new Date(st);
		}
	}


}
function getXMLbyCLL(data){
    var cls = [];

	$("#cll").find(":checkbox:checked").each(function(){
        var val = $(this).parent().next().text();
        cls.push(val);
     });
	xml="";

	switch(DeviceType){
	case "1":
		var yAxisMinValue = 2;
//		for(var i = 0; i <cls.length; i++){
//			if("H2"==cls[i]){
//				var aa = 2;
//				if(aa>yAxisMinValue){
//					yAxisMinValue = aa;
//				}
//			}else if("CO"==cls[i]){
//				var aa = 300;
//				if(aa>yAxisMinValue){
//					yAxisMinValue = aa;
//				}
//			}else if("CO2"==cls[i]){
//				var aa = 640;
//				if(aa>yAxisMinValue){
//					yAxisMinValue = aa;
//				}
//			}else if("CH4"==cls[i]){
//				var aa = 8;
//				if(aa>yAxisMinValue){
//					yAxisMinValue = aa;
//				}
//			}
//		}
		xml +="<graph animation='0' formatNumberScale='0.1' decimalPrecision='1' baseFontSize='15' " +
		"yAxisName='ppm' decimalPrecision='0' formatNumberScale='0' showNames='0' showvalues='0'>";
		xml += "<categories >";
		if(data!=null&&data!=''){
			for (var ii = 0; ii < data.length; ii++)
			{xml += "<category name='" + data[ii].sampleTime + "' />";}
		}
		xml += "</categories>";
		for(var i = 0;i<cls.length;i++){
			xml += "<dataset seriesName='" +data[0].deviceName +cls[i]+ "' color='"+x_16+"' anchorBorderColor='"+x_16+"' anchorBgColor='"+x_16+"'>";
			for (var ii = 0; ii < data.length; ii++){
				var x = Math.random()*9000000+1000000;
				var x_16 = x.toString(16);
				if("H2"==cls[i]){
					xml += "<set value='" + data[ii].h2ppm + "' />";
	    	    }
				if("CO"==cls[i]){
					xml += "<set value='" + data[ii].COppm + "' />";
				}
				if("CO2"==cls[i]){
					xml += "<set value='" + data[ii].CO2ppm + "' />";
				}
				if("CH4"==cls[i]){
					xml += "<set value='" + data[ii].CH4ppm + "' />";
				}
				if("C2H6"==cls[i]){
					xml += "<set value='" + data[ii].c2H6ppm + "' />";
				}
				if("C2H2"==cls[i]){
					xml += "<set value='" + data[ii].c2H2ppm + "' />";
				}
				if("C2H4"==cls[i]){
					xml += "<set value='" + data[ii].c2H4ppm + "' />";
				}
			}
			xml += "</dataset>";
		}
		break;
		case "2":
			var yAxisName = "";
			for(var i = 0; i <cls.length; i++){
				if("温度"==cls[i]){
					yAxisName+="温度：℃";
				}else if("压力"==cls[i]){
					yAxisName+="压力：Mpa";
				}
			}
//			xml +="<graph animation='0' formatNumberScale='0.1' decimalPrecision='1' baseFontSize='15' yAxisMinValue='"+yAxisMinValue+"' " +
//			"yAxisName='ppm' decimalPrecision='0' formatNumberScale='0' showNames='0' showvalues='0'>";
			xml +="<graph animation='0' formatNumberScale='0.1' decimalPrecision='2' baseFontSize='15' " +
			"yAxisName='"+yAxisName+"' decimalPrecision='0' formatNumberScale='0' showNames='0' showvalues='0'>";
			xml += "<categories >";
			if(data!=null&&data!=''){
				for (var ii = 0; ii < data.length; ii++)
				{xml += "<category name='" + data[ii].sampleTime + "' />";}
			}
			xml += "</categories>";
			for(var i = 0;i<cls.length;i++){
				xml += "<dataset seriesName='" +data[0].deviceName +cls[i]+ "' color='"+x_16+"' anchorBorderColor='"+x_16+"' anchorBgColor='"+x_16+"'>";
				for (var ii = 0; ii < data.length; ii++){
					var x = Math.random()*9000000+1000000;
					var x_16 = x.toString(16);
					if("温度"==cls[i]){
						xml += "<set value='" + data[ii].tmp + "' />";
		    	    }
					if("压力"==cls[i]){
						xml += "<set value='" + data[ii].pres + "' />";
					}
					if("湿度"==cls[i]){
						xml += "<set value='" + data[ii].hum + "' />";
					}
				}
				xml += "</dataset>";
			}
		break;
		case "3":
			xml="<graph animation='0' formatNumberScale='0.1' decimalPrecision='2' baseFontSize='15' yAxisMaxValue='0.1' yAxisMinValue='0' " +
			"yAxisName='Mpa' decimalPrecision='0' formatNumberScale='0' showNames='0' showvalues='0'>";
			xml += "<categories >";
			if(data!=null&&data!=''){
				for (var ii = 0; ii < data.length; ii++)
				{xml += "<category name='" + data[ii].sampleTime + "' />";}
			}
			xml += "</categories>";
			for(var i = 0;i<cls.length;i++){
				xml += "<dataset seriesName='" +data[0].deviceName +cls[i]+ "' color='"+x_16+"' anchorBorderColor='"+x_16+"' anchorBgColor='"+x_16+"'>";
				for (var ii = 0; ii < data.length; ii++){
					var x = Math.random()*9000000+1000000;
					var x_16 = x.toString(16);
					if("全电流"==cls[i]){
						xml += "<set value='" + data[ii].totA + "' />";
		    	    }
				}
				xml += "</dataset>";
			}
		break;
		case "4":
			xml="<graph animation='0' formatNumberScale='0.1' decimalPrecision='2' baseFontSize='15' yAxisMaxValue='0.1' yAxisMinValue='0' " +
			"yAxisName='Mpa' decimalPrecision='0' formatNumberScale='0' showNames='0' showvalues='0'>";
			xml += "<categories >";
			if(data!=null&&data!=''){
				for (var ii = 0; ii < data.length; ii++)
				{xml += "<category name='" + data[ii].sampleTime + "' />";}
			}
			xml += "</categories>";
			for(var i = 0;i<cls.length;i++){
				xml += "<dataset seriesName='" +data[0].deviceName +cls[i]+ "' color='"+x_16+"' anchorBorderColor='"+x_16+"' anchorBgColor='"+x_16+"'>";
				for (var ii = 0; ii < data.length; ii++){
					var x = Math.random()*9000000+1000000;
					var x_16 = x.toString(16);
					if("铁芯泄漏电流"==cls[i]){
						xml += "<set value='" + data[ii].CGAmp + "' />";
		    	    }
				}
				xml += "</dataset>";
			}
		break;
		case "19":
			xml="<graph animation='0' formatNumberScale='0.1' decimalPrecision='2' baseFontSize='15' yAxisMaxValue='0.1' yAxisMinValue='0' " +
			"yAxisName='次' decimalPrecision='0' formatNumberScale='0' showNames='0' showvalues='0'>";
			xml += "<categories >";
			if(data!=null&&data!=''){
				for (var ii = 0; ii < data.length; ii++)
				{xml += "<category name='" + data[ii].sampleTime + "' />";}
			}
			xml += "</categories>";
			for(var i = 0;i<cls.length;i++){
				xml += "<dataset seriesName='" +data[0].deviceName +cls[i]+ "' color='"+x_16+"' anchorBorderColor='"+x_16+"' anchorBgColor='"+x_16+"'>";
				for (var ii = 0; ii < data.length; ii++){
					var x = Math.random()*9000000+1000000;
					var x_16 = x.toString(16);
					if("放电次数"==cls[i]){
						xml += "<set value='" + data[ii].plsNum + "' />";
		    	    }
				}
				xml += "</dataset>";
			}
		break;
		case "hwcw":
//			xml +="<graph animation='0' formatNumberScale='0.1' decimalPrecision='1' baseFontSize='15' yAxisMinValue='"+yAxisMinValue+"' " +
//			"yAxisName='ppm' decimalPrecision='0' formatNumberScale='0' showNames='0' showvalues='0'>";
			xml +="<graph animation='0' formatNumberScale='0.1' decimalPrecision='2' baseFontSize='15' " +
			"yAxisName='温度：℃' decimalPrecision='0' formatNumberScale='0' showNames='0' showvalues='0'>";
			xml += "<categories >";
			if(data!=null&&data!=''){
				for (var ii = 0; ii < data.length; ii++)
				{xml += "<category name='" + data[ii].sampleTime + "' />";}
			}
			xml += "</categories>";
			for(var i = 0;i<cls.length;i++){
				xml += "<dataset seriesName='" +data[0].deviceName +cls[i]+ "' color='"+x_16+"' anchorBorderColor='"+x_16+"' anchorBgColor='"+x_16+"'>";
				for (var ii = 0; ii < data.length; ii++){
					var x = Math.random()*9000000+1000000;
					var x_16 = x.toString(16);
					if("温度"==cls[i]){
						xml += "<set value='" + data[ii].tmp + "' />";
		    	    }
				}
				xml += "</dataset>";
			}
		break;
		}
		xml+= "</graph>";
		return xml;
}
function getXML(data){
    $("#cll").find(":checkbox:checked").each(function(){
        text_select = $(this).parent().next().text();
     });
//	text_select=$('#value_select').combobox('getText');
//    alert("data_sampleTime="+data[0][0].sampleTime);
	xml="";1
	var dName = [];
	switch(DeviceType){
	case "1":
		if("H2"==text_select){
		xml +="<graph animation='0' formatNumberScale='0.1' decimalPrecision='1' baseFontSize='15' yAxisMinValue='2'  " +
			"yAxisName='ppm' decimalPrecision='0' formatNumberScale='0' showNames='0' showvalues='0'>";
		xml += "<categories >";
//	       for (var i = 0; i < data.length; i++)
//	       {
	    	   if(data[0]!=null&&data[0]!=''){
		    	   for (var ii = 0; ii < data[0].length; ii++)
			       {
		    		   xml += "<category name='" + data[0][ii].sampleTime + "' />";
			       }
	    	   }

//	       }
	       xml += "</categories>";
	       {
		           for (var i = 0; i < data.length; i++)
		           {
		        	   if(data[i]!=null&&data[i]!=''){
			        	   for (var ii = 0; ii < data[i].length; ii++)
			    	       {
				        	   var onoff=0;
				        	   for(var iii = 0;iii<dName.length;iii++){
				        		   if(data[i][ii].deviceName == dName[iii]){
					        		  onoff=1;
					        	   }
				        	   }
				        	   if(onoff==0){
				        		   var x = Math.random()*9000000+1000000;
					    		   var x_16 = x.toString(16);
	//			        		   alert("data="+data[i].deviceName +"||dName="+ dName[ii]);
				        		   xml += "<dataset seriesName='" +data[i][ii].deviceName +text_select+ "' color='"+x_16+"' anchorBorderColor='"+x_16+"' anchorBgColor='"+x_16+"'>";
				        		   dName.push(data[i][ii].deviceName);
				        	   }
				               xml += "<set value='" + data[i][ii].h2ppm + "' />";
		//		               alert(dName);
			    	       }
		        	   }
		        	   xml += "</dataset>";
		           }
	       }
		}
//		alert(xml);
		if("CO"==text_select){
			xml +="<graph animation='0' formatNumberScale='0.1' decimalPrecision='1' baseFontSize='15' yAxisMinValue='300' " +
				"yAxisName='ppm' decimalPrecision='0' formatNumberScale='0' showNames='0' showvalues='0'>";
			xml += "<categories >";
//		       for (var i = 0; i < data.length; i++)
//		       {
		    	   if(data[0]!=null&&data[0]!=''){
			    	   for (var ii = 0; ii < data[0].length; ii++)
				       {
			    		   xml += "<category name='" + data[0][ii].sampleTime + "' />";
				       }
		    	   }

//		       }
		       xml += "</categories>";
		       {
			           for (var i = 0; i < data.length; i++)
			           {
			        	   if(data[i]!=null&&data[i]!=''){
				        	   for (var ii = 0; ii < data[i].length; ii++)
				    	       {
					        	   var onoff=0;
					        	   for(var iii = 0;iii<dName.length;iii++){
					        		   if(data[i][ii].deviceName == dName[iii]){
						        		  onoff=1;
						        	   }
					        	   }
					        	   if(onoff==0){
					        		   var x = Math.random()*9000000+1000000;
						    		   var x_16 = x.toString(16);
		//			        		   alert("data="+data[i].deviceName +"||dName="+ dName[ii]);
					        		   xml += "<dataset seriesName='" +data[i][ii].deviceName +text_select+ "' color='"+x_16+"' anchorBorderColor='"+x_16+"' anchorBgColor='"+x_16+"'>";
					        		   dName.push(data[i][ii].deviceName);
					        	   }
					        	   xml += "<set value='" + data[i][ii].COppm + "' />";
			//		               alert(dName);
				    	       }
			        	   }
			        	   xml += "</dataset>";
			           }
		       }
//		       for (var i = 0; i < data.length; i++)
//		       {
//		    	   xml += "<category name='" + data[i].sampleTime + "' />";
//		       }
//		       xml += "</categories>";
//		       {
//		    	   xml += "<dataset seriesName='" + _DeviceName +text_select+ "' color='1D8BD1' anchorBorderColor='1D8BD1' anchorBgColor='1D8BD1'>";
//		           for (var i = 0; i < data.length; i++)
//		           {
//		               xml += "<set value='" + data[i].COppm + "' />";
//		           }
//		           xml += "</dataset>";
//		       }
			}
		if("CO2"==text_select){
			xml +="<graph animation='0' formatNumberScale='0.1' decimalPrecision='1' baseFontSize='15' yAxisMinValue='640' " +
				"yAxisName='ppm' decimalPrecision='0' formatNumberScale='0' showNames='0' showvalues='0'>";
			xml += "<categories >";
//		       for (var i = 0; i < data.length; i++)
//		       {
		    	   if(data[0]!=null&&data[0]!=''){
			    	   for (var ii = 0; ii < data[0].length; ii++)
				       {
			    		   xml += "<category name='" + data[0][ii].sampleTime + "' />";
				       }
		    	   }

//		       }
		       xml += "</categories>";
		       {
			           for (var i = 0; i < data.length; i++)
			           {
			        	   if(data[i]!=null&&data[i]!=''){
				        	   for (var ii = 0; ii < data[i].length; ii++)
				    	       {
					        	   var onoff=0;
					        	   for(var iii = 0;iii<dName.length;iii++){
					        		   if(data[i][ii].deviceName == dName[iii]){
						        		  onoff=1;
						        	   }
					        	   }
					        	   if(onoff==0){
					        		   var x = Math.random()*9000000+1000000;
						    		   var x_16 = x.toString(16);
					        		   xml += "<dataset seriesName='" +data[i][ii].deviceName +text_select+ "' color='"+x_16+"' anchorBorderColor='"+x_16+"' anchorBgColor='"+x_16+"'>";
					        		   dName.push(data[i][ii].deviceName);
					        	   }
					        	   xml += "<set value='" + data[i][ii].CO2ppm + "' />";
				    	       }
			        	   }
			        	   xml += "</dataset>";
			           }
		       }
//			for (var i = 0; i < data.length; i++)
//		       {
//		    	   xml += "<category name='" + data[i].sampleTime + "' />";
//		       }
//		       xml += "</categories>";
//		       {
//		    	   xml += "<dataset seriesName='" + _DeviceName +text_select+ "' color='1D8BD1' anchorBorderColor='1D8BD1' anchorBgColor='1D8BD1'>";
//		           for (var i = 0; i < data.length; i++)
//		           {
//		               xml += "<set value='" + data[i].CO2ppm + "' />";
//		           }
//		           xml += "</dataset>";
//		       }
			}
		if("CH4"==text_select){
			xml +="<graph animation='0' formatNumberScale='0.1' decimalPrecision='1' baseFontSize='15' yAxisMinValue='0' yAxisMaxValue='8'  " +
				"yAxisName='ppm' decimalPrecision='0' formatNumberScale='0' showNames='0' showvalues='0'>";
			xml += "<categories >";
//		       for (var i = 0; i < data.length; i++)
//		       {
		    	   if(data[0]!=null&&data[0]!=''){
			    	   for (var ii = 0; ii < data[0].length; ii++)
				       {
			    		   xml += "<category name='" + data[0][ii].sampleTime + "' />";
				       }
		    	   }

//		       }
		       xml += "</categories>";
		       {
			           for (var i = 0; i < data.length; i++)
			           {
			        	   if(data[i]!=null&&data[i]!=''){
				        	   for (var ii = 0; ii < data[i].length; ii++)
				    	       {
					        	   var onoff=0;
					        	   for(var iii = 0;iii<dName.length;iii++){
					        		   if(data[i][ii].deviceName == dName[iii]){
						        		  onoff=1;
						        	   }
					        	   }
					        	   if(onoff==0){
					        		   var x = Math.random()*9000000+1000000;
						    		   var x_16 = x.toString(16);
					        		   xml += "<dataset seriesName='" +data[i][ii].deviceName +text_select+ "' color='"+x_16+"' anchorBorderColor='"+x_16+"' anchorBgColor='"+x_16+"'>";
					        		   dName.push(data[i][ii].deviceName);
					        	   }
					        	   xml += "<set value='" + data[i][ii].CH4ppm + "' />";
				    	       }
			        	   }
			        	   xml += "</dataset>";
			           }
		       }
//		       for (var i = 0; i < data.length; i++)
//		       {
//		    	   xml += "<category name='" + data[i].sampleTime + "' />";
//		       }
//		       xml += "</categories>";
//		       {
//		    	   xml += "<dataset seriesName='" + _DeviceName +text_select+ "' color='1D8BD1' anchorBorderColor='1D8BD1' anchorBgColor='1D8BD1'>";
//		           for (var i = 0; i < data.length; i++)
//		           {
//		               xml += "<set value='" + data[i].CH4ppm + "' />";
//		           }
//		           xml += "</dataset>";
//		       }
			}
		if("C2H6"==text_select){
			xml +="<graph animation='0' formatNumberScale='0.1' decimalPrecision='1' baseFontSize='15'   " +
				"yAxisName='ppm' decimalPrecision='0' formatNumberScale='0' showNames='0' showvalues='0'>";
			xml += "<categories >";
//		       for (var i = 0; i < data.length; i++)
//		       {
		    	   if(data[0]!=null&&data[0]!=''){
			    	   for (var ii = 0; ii < data[0].length; ii++)
				       {
			    		   xml += "<category name='" + data[0][ii].sampleTime + "' />";
				       }
		    	   }

//		       }
		       xml += "</categories>";
		       {
			           for (var i = 0; i < data.length; i++)
			           {
			        	   if(data[i]!=null&&data[i]!=''){
				        	   for (var ii = 0; ii < data[i].length; ii++)
				    	       {
					        	   var onoff=0;
					        	   for(var iii = 0;iii<dName.length;iii++){
					        		   if(data[i][ii].deviceName == dName[iii]){
						        		  onoff=1;
						        	   }
					        	   }
					        	   if(onoff==0){
					        		   var x = Math.random()*9000000+1000000;
						    		   var x_16 = x.toString(16);
					        		   xml += "<dataset seriesName='" +data[i][ii].deviceName +text_select+ "' color='"+x_16+"' anchorBorderColor='"+x_16+"' anchorBgColor='"+x_16+"'>";
					        		   dName.push(data[i][ii].deviceName);
					        	   }
					        	   xml += "<set value='" + data[i][ii].c2H6ppm + "' />";
				    	       }
			        	   }
			        	   xml += "</dataset>";
			           }
		       }
//		       for (var i = 0; i < data.length; i++)
//		       {
//		    	   xml += "<category name='" + data[i].sampleTime + "' />";
//		       }
//		       xml += "</categories>";
//		       {
//		    	   xml += "<dataset seriesName='" + _DeviceName +text_select+ "' color='1D8BD1' anchorBorderColor='1D8BD1' anchorBgColor='1D8BD1'>";
//		           for (var i = 0; i < data.length; i++)
//		           {
//		               xml += "<set value='" + data[i].c2H6ppm + "' />";
//		           }
//		           xml += "</dataset>";
//		       }
			}
		if("C2H2"==text_select){
			xml +="<graph animation='0' formatNumberScale='0.1' decimalPrecision='1' baseFontSize='15'  " +
				"yAxisName='ppm' decimalPrecision='0' formatNumberScale='0' showNames='0' showvalues='0'>";
			xml += "<categories >";
//		       for (var i = 0; i < data.length; i++)
//		       {
		    	   if(data[0]!=null&&data[0]!=''){
			    	   for (var ii = 0; ii < data[0].length; ii++)
				       {
			    		   xml += "<category name='" + data[0][ii].sampleTime + "' />";
				       }
		    	   }

//		       }
		       xml += "</categories>";
		       {
			           for (var i = 0; i < data.length; i++)
			           {
			        	   if(data[i]!=null&&data[i]!=''){
				        	   for (var ii = 0; ii < data[i].length; ii++)
				    	       {
					        	   var onoff=0;
					        	   for(var iii = 0;iii<dName.length;iii++){
					        		   if(data[i][ii].deviceName == dName[iii]){
						        		  onoff=1;
						        	   }
					        	   }
					        	   if(onoff==0){
					        		   var x = Math.random()*9000000+1000000;
						    		   var x_16 = x.toString(16);
					        		   xml += "<dataset seriesName='" +data[i][ii].deviceName +text_select+ "' color='"+x_16+"' anchorBorderColor='"+x_16+"' anchorBgColor='"+x_16+"'>";
					        		   dName.push(data[i][ii].deviceName);
					        	   }
					        	   xml += "<set value='" + data[i][ii].c2H2ppm + "' />";
				    	       }
			        	   }
			        	   xml += "</dataset>";
			           }
		       }
//		       for (var i = 0; i < data.length; i++)
//		       {
//		    	   xml += "<category name='" + data[i].sampleTime + "' />";
//		       }
//		       xml += "</categories>";
//		       {
//		    	   xml += "<dataset seriesName='" + _DeviceName +text_select+ "' color='1D8BD1' anchorBorderColor='1D8BD1' anchorBgColor='1D8BD1'>";
//		           for (var i = 0; i < data.length; i++)
//		           {
//		               xml += "<set value='" + data[i].c2H2ppm + "' />";
//		           }
//		           xml += "</dataset>";
//		       }
			}
		if("C2H4"==text_select){
			xml +="<graph animation='0' formatNumberScale='0.1' decimalPrecision='1' baseFontSize='15'  " +
				"yAxisName='ppm' decimalPrecision='0' formatNumberScale='0' showNames='0' showvalues='0'>";
			xml += "<categories >";
//		       for (var i = 0; i < data.length; i++)
//		       {
		    	   if(data[0]!=null&&data[0]!=''){
			    	   for (var ii = 0; ii < data[0].length; ii++)
				       {
			    		   xml += "<category name='" + data[0][ii].sampleTime + "' />";
				       }
		    	   }

//		       }
		       xml += "</categories>";
		       {
			           for (var i = 0; i < data.length; i++)
			           {
			        	   if(data[i]!=null&&data[i]!=''){
				        	   for (var ii = 0; ii < data[i].length; ii++)
				    	       {
					        	   var onoff=0;
					        	   for(var iii = 0;iii<dName.length;iii++){
					        		   if(data[i][ii].deviceName == dName[iii]){
						        		  onoff=1;
						        	   }
					        	   }
					        	   if(onoff==0){
					        		   var x = Math.random()*9000000+1000000;
						    		   var x_16 = x.toString(16);
					        		   xml += "<dataset seriesName='" +data[i][ii].deviceName +text_select+ "' color='"+x_16+"' anchorBorderColor='"+x_16+"' anchorBgColor='"+x_16+"'>";
					        		   dName.push(data[i][ii].deviceName);
					        	   }
					        	   xml += "<set value='" + data[i][ii].c2H4ppm + "' />";
				    	       }
			        	   }
			        	   xml += "</dataset>";
			           }
		       }
//		       for (var i = 0; i < data.length; i++)
//		       {
//		    	   xml += "<category name='" + data[i].sampleTime + "' />";
//		       }
//		       xml += "</categories>";
//		       {
//		    	   xml += "<dataset seriesName='" + _DeviceName +text_select+ "' color='1D8BD1' anchorBorderColor='1D8BD1' anchorBgColor='1D8BD1'>";
//		           for (var i = 0; i < data.length; i++)
//		           {
//		               xml += "<set value='" + data[i].c2H4ppm + "' />";
//		           }
//		           xml += "</dataset>";
//		       }
			}
		break;
	case "2":
		if("温度"==text_select){
			xml +="<graph animation='0' formatNumberScale='0.1' decimalPrecision='1' baseFontSize='15' yAxisMinValue='25' yAxisMaxValue='45'  " +
				"yAxisName='℃' decimalPrecision='0' formatNumberScale='0' showNames='0' showvalues='0'>";
			xml += "<categories >";

//		       for (var i = 0; i < data.length; i++)
//		       {
		    	   if(data[0]!=null&&data[0]!=''){
			    	   for (var ii = 0; ii < data[0].length; ii++)
				       {
			    		   xml += "<category name='" + data[0][ii].sampleTime + "' />";
				       }
		    	   }

//		       }
		       xml += "</categories>";
		       {
			           for (var i = 0; i < data.length; i++)
			           {
			        	   if(data[i]!=null&&data[i]!=''){
				        	   for (var ii = 0; ii < data[i].length; ii++)
				    	       {
					        	   var onoff=0;
					        	   for(var iii = 0;iii<dName.length;iii++){
					        		   if(data[i][ii].deviceName == dName[iii]){
						        		  onoff=1;
						        	   }
					        	   }
					        	   if(onoff==0){
					        		   var x = Math.random()*9000000+1000000;
						    		   var x_16 = x.toString(16);
					        		   xml += "<dataset seriesName='" +data[i][ii].deviceName +text_select+ "' color='"+x_16+"' anchorBorderColor='"+x_16+"' anchorBgColor='"+x_16+"'>";
					        		   dName.push(data[i][ii].deviceName);
					        	   }
					        	   xml += "<set value='" + data[i][ii].tmp + "' />";
				    	       }
			        	   }
			        	   xml += "</dataset>";
			           }
		       }
//		       for (var i = 0; i < data.length; i++)
//		       {
//		    	   xml += "<category name='" + data[i].sampleTime + "' />";
//		       }
//		       xml += "</categories>";
//		       {
//		    	   xml += "<dataset seriesName='" + _DeviceName +text_select+ "' color='1D8BD1' anchorBorderColor='1D8BD1' anchorBgColor='1D8BD1'>";
//		           for (var i = 0; i < data.length; i++)
//		           {
//		               xml += "<set value='" + data[i].tmp + "' />";
//		           }
//		           xml += "</dataset>";
//		       }
		}
		if("压力"==text_select){
			xml="<graph animation='0' formatNumberScale='0.1' decimalPrecision='2' baseFontSize='15' yAxisMinValue='0.4' yAxisMaxValue='0.8'  " +
				"yAxisName='Mpa' decimalPrecision='0' formatNumberScale='0' showNames='0' showvalues='0'>";
			xml += "<categories >";

//		       for (var i = 0; i < data.length; i++)
//		       {
		    	   if(data[0]!=null&&data[0]!=''){
			    	   for (var ii = 0; ii < data[0].length; ii++)
				       {
			    		   xml += "<category name='" + data[0][ii].sampleTime + "' />";
				       }
		    	   }

//		       }
		       xml += "</categories>";
		       {
			           for (var i = 0; i < data.length; i++)
			           {
			        	   if(data[i]!=null&&data[i]!=''){
				        	   for (var ii = 0; ii < data[i].length; ii++)
				    	       {
					        	   var onoff=0;
					        	   for(var iii = 0;iii<dName.length;iii++){
					        		   if(data[i][ii].deviceName == dName[iii]){
						        		  onoff=1;
						        	   }
					        	   }
					        	   if(onoff==0){
					        		   var x = Math.random()*9000000+1000000;
						    		   var x_16 = x.toString(16);
					        		   xml += "<dataset seriesName='" +data[i][ii].deviceName +text_select+ "' color='"+x_16+"' anchorBorderColor='"+x_16+"' anchorBgColor='"+x_16+"'>";
					        		   dName.push(data[i][ii].deviceName);
					        	   }
					        	   xml += "<set value='" + data[i][ii].pres + "' />";
				    	       }
			        	   }
			        	   xml += "</dataset>";
			           }
		       }
//		       for (var i = 0; i < data.length; i++)
//		       {
//		    	   xml += "<category name='" + data[i].sampleTime + "' />";
//		       }
//		       xml += "</categories>";
//		       {
//		    	   xml += "<dataset seriesName='" + _DeviceName +text_select+ "' color='1D8BD1' anchorBorderColor='1D8BD1' anchorBgColor='1D8BD1'>";
//		           for (var i = 0; i < data.length; i++)
//		           {
//		               xml += "<set value='" + data[i].pres + "' />";
//		           }
//		           xml += "</dataset>";
//		       }
		}
		if("湿度"==text_select){
			xml="<graph animation='0' formatNumberScale='0.1' decimalPrecision='2' baseFontSize='15' yAxisMinValue='0' yAxisMaxValue='1'  " +
				"yAxisName='' decimalPrecision='0' formatNumberScale='0' showNames='0' showvalues='0'>";
			xml += "<categories >";
//		       for (var i = 0; i < data.length; i++)
//		       {
		    	   if(data[0]!=null&&data[0]!=''){
			    	   for (var ii = 0; ii < data[0].length; ii++)
				       {
			    		   xml += "<category name='" + data[0][ii].sampleTime + "' />";
				       }
		    	   }

//		       }
		       xml += "</categories>";
		       {
			           for (var i = 0; i < data.length; i++)
			           {
			        	   if(data[i]!=null&&data[i]!=''){
				        	   for (var ii = 0; ii < data[i].length; ii++)
				    	       {
					        	   var onoff=0;
					        	   for(var iii = 0;iii<dName.length;iii++){
					        		   if(data[i][ii].deviceName == dName[iii]){
						        		  onoff=1;
						        	   }
					        	   }
					        	   if(onoff==0){
					        		   var x = Math.random()*9000000+1000000;
						    		   var x_16 = x.toString(16);
					        		   xml += "<dataset seriesName='" +data[i][ii].deviceName +text_select+ "' color='"+x_16+"' anchorBorderColor='"+x_16+"' anchorBgColor='"+x_16+"'>";
					        		   dName.push(data[i][ii].deviceName);
					        	   }
					        	   xml += "<set value='" + data[i][ii].hum + "' />";
				    	       }
			        	   }
			        	   xml += "</dataset>";
			           }
		       }
//	       for (var i = 0; i < data.length; i++)
//	       {
//	    	   xml += "<category name='" + data[i].sampleTime + "' />";
//	       }
//	       xml += "</categories>";
//	       {
//	    	   xml += "<dataset seriesName='" + _DeviceName +text_select+ "' color='1D8BD1' anchorBorderColor='1D8BD1' anchorBgColor='1D8BD1'>";
//	           for (var i = 0; i < data.length; i++)
//	           {
//	               xml += "<set value='" + data[i].hum + "' />";
//	           }
//	           xml += "</dataset>";
//	       }
		}break;
	case "3":
		if("全电流"==text_select){
			xml="<graph animation='0' formatNumberScale='0.1' decimalPrecision='2' baseFontSize='15' yAxisMaxValue='0.1' yAxisMinValue='0' " +
				"yAxisName='Mpa' decimalPrecision='0' formatNumberScale='0' showNames='0' showvalues='0'>";
			xml += "<categories >";
//		       for (var i = 0; i < data.length; i++)
//		       {
		    	   if(data[0]!=null&&data[0]!=''){
			    	   for (var ii = 0; ii < data[0].length; ii++)
				       {
			    		   xml += "<category name='" + data[0][ii].sampleTime + "' />";
				       }
		    	   }

//		       }
		       xml += "</categories>";
		       {
			           for (var i = 0; i < data.length; i++)
			           {
			        	   if(data[i]!=null&&data[i]!=''){
				        	   for (var ii = 0; ii < data[i].length; ii++)
				    	       {
					        	   var onoff=0;
					        	   for(var iii = 0;iii<dName.length;iii++){
					        		   if(data[i][ii].deviceName == dName[iii]){
						        		  onoff=1;
						        	   }
					        	   }
					        	   if(onoff==0){
					        		   var x = Math.random()*9000000+1000000;
						    		   var x_16 = x.toString(16);
					        		   xml += "<dataset seriesName='" +data[i][ii].deviceName +text_select+ "' color='"+x_16+"' anchorBorderColor='"+x_16+"' anchorBgColor='"+x_16+"'>";
					        		   dName.push(data[i][ii].deviceName);
					        	   }
					        	   xml += "<set value='" + data[i][ii].totA + "' />";
				    	       }
			        	   }
			        	   xml += "</dataset>";
			           }
		       }
//	       for (var i = 0; i < data.length; i++)
//	       {
//	    	   xml += "<category name='" + data[i].sampleTime + "' />";
//	       }
//	       xml += "</categories>";
//	       {
//	    	   xml += "<dataset seriesName='" + _DeviceName +text_select+ "' color='1D8BD1' anchorBorderColor='1D8BD1' anchorBgColor='1D8BD1'>";
//	           for (var i = 0; i < data.length; i++)
//	           {
//	               xml += "<set value='" + data[i].totA + "' />";
//	           }
//	           xml += "</dataset>";
//	       }
		}break;
	case "4":
		if("铁芯泄漏电流"==text_select){
			xml="<graph animation='0' formatNumberScale='0.1' decimalPrecision='2' baseFontSize='15' yAxisMinValue='0' " +
				"yAxisName='Mpa' decimalPrecision='0' formatNumberScale='0' showNames='0' showvalues='0'>";
			xml += "<categories >";
//		       for (var i = 0; i < data.length; i++)
//		       {
		    	   if(data[0]!=null&&data[0]!=''){
			    	   for (var ii = 0; ii < data[0].length; ii++)
				       {
			    		   xml += "<category name='" + data[0][ii].sampleTime + "' />";
				       }
		    	   }

//		       }
		       xml += "</categories>";
		       {
			           for (var i = 0; i < data.length; i++)
			           {
			        	   if(data[i]!=null&&data[i]!=''){
				        	   for (var ii = 0; ii < data[i].length; ii++)
				    	       {
					        	   var onoff=0;
					        	   for(var iii = 0;iii<dName.length;iii++){
					        		   if(data[i][ii].deviceName == dName[iii]){
						        		  onoff=1;
						        	   }
					        	   }
					        	   if(onoff==0){
					        		   var x = Math.random()*9000000+1000000;
						    		   var x_16 = x.toString(16);
					        		   xml += "<dataset seriesName='" +data[i][ii].deviceName +text_select+ "' color='"+x_16+"' anchorBorderColor='"+x_16+"' anchorBgColor='"+x_16+"'>";
					        		   dName.push(data[i][ii].deviceName);
					        	   }
					        	   xml += "<set value='" + data[i][ii].CGAmp + "' />";
				    	       }
			        	   }
			        	   xml += "</dataset>";
			           }
		       }
//	       for (var i = 0; i < data.length; i++)
//	       {
//	    	   xml += "<category name='" + data[i].sampleTime + "' />";
//	       }
//	       xml += "</categories>";
//	       {
//	    	   xml += "<dataset seriesName='" + _DeviceName +text_select+ "' color='1D8BD1' anchorBorderColor='1D8BD1' anchorBgColor='1D8BD1'>";
//	           for (var i = 0; i < data.length; i++)
//	           {
//	               xml += "<set value='" + data[i].CGAmp + "' />";
//	           }
//	           xml += "</dataset>";
//	       }
		}break;
	case "19":
		if("放电次数"==text_select){
			xml="<graph animation='0' formatNumberScale='0.1' decimalPrecision='2' baseFontSize='15' yAxisMaxValue='0.1' yAxisMinValue='0' " +
				"yAxisName='次' decimalPrecision='0' formatNumberScale='0' showNames='0' showvalues='0'>";
			xml += "<categories >";
//		       for (var i = 0; i < data.length; i++)
//		       {
		    	   if(data[0]!=null&&data[0]!=''){
			    	   for (var ii = 0; ii < data[0].length; ii++)
				       {
			    		   xml += "<category name='" + data[0][ii].sampleTime + "' />";
				       }
		    	   }

//		       }
		       xml += "</categories>";
		       {
			           for (var i = 0; i < data.length; i++)
			           {
			        	   if(data[i]!=null&&data[i]!=''){
				        	   for (var ii = 0; ii < data[i].length; ii++)
				    	       {
					        	   var onoff=0;
					        	   for(var iii = 0;iii<dName.length;iii++){
					        		   if(data[i][ii].deviceName == dName[iii]){
						        		  onoff=1;
						        	   }
					        	   }
					        	   if(onoff==0){
					        		   var x = Math.random()*9000000+1000000;
						    		   var x_16 = x.toString(16);
					        		   xml += "<dataset seriesName='" +data[i][ii].deviceName +text_select+ "' color='"+x_16+"' anchorBorderColor='"+x_16+"' anchorBgColor='"+x_16+"'>";
					        		   dName.push(data[i][ii].deviceName);
					        	   }
					        	   xml += "<set value='" + data[i][ii].plsNum + "' />";
				    	       }
			        	   }
			        	   xml += "</dataset>";
			           }
		       }
//	       for (var i = 0; i < data.length; i++)
//	       {
//	    	   xml += "<category name='" + data[i].sampleTime + "' />";
//	       }
//	       xml += "</categories>";
//	       {
//	    	   xml += "<dataset seriesName='" + _DeviceName +text_select+ "' color='1D8BD1' anchorBorderColor='1D8BD1' anchorBgColor='1D8BD1'>";
//	           for (var i = 0; i < data.length; i++)
//	           {
//	               xml += "<set value='" + data[i].plsNum + "' />";
//	           }
//	           xml += "</dataset>";
//	       }
		}break;
	case "hwcw":
		if("温度"==text_select){
			xml +="<graph animation='0' formatNumberScale='0.1' decimalPrecision='1' baseFontSize='15' yAxisMinValue='25' yAxisMaxValue='45'  " +
				"yAxisName='℃' decimalPrecision='0' formatNumberScale='0' showNames='0' showvalues='0'>";
			xml += "<categories >";
		    	   if(data[0]!=null&&data[0]!=''){
			    	   for (var ii = 0; ii < data[0].length; ii++)
				       {
			    		   xml += "<category name='" + data[0][ii].sampleTime + "' />";
				       }
		    	   }
		       xml += "</categories>";
		       {
			           for (var i = 0; i < data.length; i++)
			           {
			        	   if(data[i]!=null&&data[i]!=''){
				        	   for (var ii = 0; ii < data[i].length; ii++)
				    	       {
					        	   var onoff=0;
					        	   for(var iii = 0;iii<dName.length;iii++){
					        		   if(data[i][ii].deviceName == dName[iii]){
						        		  onoff=1;
						        	   }
					        	   }
					        	   if(onoff==0){
					        		   var x = Math.random()*9000000+1000000;
						    		   var x_16 = x.toString(16);
					        		   xml += "<dataset seriesName='" +data[i][ii].deviceName +text_select+ "' color='"+x_16+"' anchorBorderColor='"+x_16+"' anchorBgColor='"+x_16+"'>";
					        		   dName.push(data[i][ii].deviceName);
					        	   }
					        	   xml += "<set value='" + data[i][ii].tmp + "' />";
				    	       }
			        	   }
			        	   xml += "</dataset>";
			           }
		       }
		}break;
	}
	xml+= "</graph>";
	return xml;
}

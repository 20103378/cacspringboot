$package('jeecg.currentState');
$(function () {
	jeecg.currentState.ShowData();
});
jeecg.currentState = function(){
	var _this = {
			//请求数据
				ShowData:function(){
					var url=ctxPath +"/systemState/getData";
					 $.ajax({
				         async: false,
				         cache: false,
				         type: 'POST',
				         url: url,
				         error: function(){// 请求失败处理函数
				        	 alert("false");
				         },
				         success: function(data){

							 var CpList = data.CpList;
							 for(var i=0;i<CpList.length;i++){
								 if(CpList[i].pid == "1000"){
									 $("#nccapall").html(CpList[i].cpu);
								 }
								 if(CpList[i].pid == "1001"){
									 $("#nccap").html(CpList[i].cpu);
								 }
								 if(CpList[i].pid == "1002"){
									 $("#cpcapall").html(CpList[i].cpu);
								 }
								 if(CpList[i].pid == "1003"){
									 $("#cpcap").html(CpList[i].cpu);
								 }
								 if(CpList[i].pid == "1004"){
									 $("#usercpu").html(CpList[i].cpu);
								 }
								 if(CpList[i].pid == "1005"){
									 $("#syscpu").html(CpList[i].cpu);
								 }
								 if(CpList[i].pid == "1006"){
									 $("#I1cpu").html(CpList[i].cpu);
								 }
								 if(CpList[i].pid == "1007"){									 
									 $("#I1men").html(CpList[i].cpu);
								 }
								 if(CpList[i].pid == "1008"){
									 $("#I2cpu").html(CpList[i].cpu);
								 }
								 if(CpList[i].pid == "1009"){
									 $("#I2men").html(CpList[i].cpu);
								 }
							 }

							 var CPUxml=_this.getCPUXML(data.dataList);
				        	 var CPUchart = new FusionCharts(ctxPath+"/jsp/scott/Charts/Pie2D.swf", "ChartId", "600", "400");
				        	 CPUchart.setDataXML(CPUxml);
				        	 CPUchart.render("cpuchartdiv");

				        	 var MEMxml=_this.getMEMXML(data.OsList);
				        	 var MEMchart = new FusionCharts(ctxPath+"/jsp/scott/Charts/Pie2D.swf", "ChartId", "600", "400");
				        	 MEMchart.setDataXML(MEMxml);
				        	 MEMchart.render("memchartdiv");

				         }
					 });
				},
				//获取CPU xml字符串
				getCPUXML:function(data){
					xml="";
					xml="<graph forceDecimals='0' decimals='2' showNames='1'  decimalPrecision='0' caption='CPU使用率(%)' baseFontSize='15'>";
					for(var i=0;i<data.length;i++)
						xml += "<set name='"+data[i].pname+"' value='"+data[i].cpu+"' /> ";
					xml += "</graph> ";
					return xml;
				},
				//获取内存 xml字符串
				getMEMXML:function(data){
					xml="";
					xml="<graph forceDecimals='0' decimals='2' showNames='1'  decimalPrecision='0' caption='内存使用率(%)' baseFontSize='15'>";
					for(var i=0;i<data.length;i++)
						xml += "<set name='"+data[i].pname+"' value='"+data[i].mem+"' /> ";
					xml += "</graph> ";
					return xml;
				}
			};
	return _this;
}();


$package('jeecg.inf');
var colTitle_json=[];//用于存储xml中基本信息中的属性名
var colValue_json=[];//用于存储xml中基本信息中的属性值
var yyname_json=[]; //用于存储应用下的属性名
var yyvalue_json=[];//用于存储应用下的属性值
var ledyyTitle_json=[];//用于存储xml中led应用名
var senyyTitle_json=[];//用于存储xml中传感器应用名
jeecg.inf = function(){
    var _this = {
    	getXMLS:function(){
    		var ledjbinfo_num=0;
    		var ledgkinfo_num=0;
    		var senjbinfo_num=0;
    		var sengkinfo_num=0;
    		var formdata={};
    		formdata["DeviceID"]=$("#txtID").val();
    		formdata["Type"]=$("#txtType").val();
    		 $.ajax({
    			 	async: false,
    			 	cache: false,
    			 	url:"getInfXml",
    			 	data:formdata,
    		        dataType: 'xml',
    		        type: 'GET',
    		        error: function(data)
    		        {
//    		            alert("加载XML 文件出错！");
    		        },
    		        success: function(data)
    		        {
    		        	$(data).find("IED").each(function(i){
    		        		$(this).find("BASIC_INFO").each(function(i){
    		        			ledjbinfo_num=$(this).children().length;
    		        			colTitle_json.push($(this).children("NAME").attr('DESC'));
    		        			colTitle_json.push($(this).children("MODEL").attr('DESC'));
    		        			colTitle_json.push($(this).children("HW_VER").attr('DESC'));
    		        			colTitle_json.push($(this).children("SW_VER").attr('DESC'));
    		        			colTitle_json.push($(this).children("FACTORY").attr('DESC'));
    		        			colTitle_json.push($(this).children("PROD_TIME").attr('DESC'));
    		        			colTitle_json.push($(this).children("USED_TIME").attr('DESC'));
    		        			colTitle_json.push($(this).children("CODE").attr('DESC'));
    		        			colValue_json.push($(this).children("NAME").text());
    		        			colValue_json.push($(this).children("MODEL").text());
    		        			colValue_json.push($(this).children("HW_VER").text());
    		        			colValue_json.push($(this).children("SW_VER").text());
    		        			colValue_json.push($(this).children("FACTORY").text());
    		        			colValue_json.push($(this).children("PROD_TIME").text());
    		        			colValue_json.push($(this).children("USED_TIME").text());
    		        			colValue_json.push($(this).children("CODE").text());

    		        		});
    		        		$(this).find("CONDITION_INFO").each(function(i){
    		        			ledgkinfo_num=$(this).children().length;
    		        			var i=1;
    		        			$(this).children().each(function(i){
    		        				ledyyTitle_json.push( $(this).attr('DESC'));
    		        				yyname_json.push($(this).children("MD5").attr('DESC'));
    		        				yyname_json.push($(this).children("NAME").attr('DESC'));
    		        				yyvalue_json.push($(this).children("MD5").text());
    		        				yyvalue_json.push($(this).children("NAME").text());
        		        			i++;
    		        			});
    		        		});
    		        	});
    		        	$(data).find("SENSOR").each(function(i){
    		        		$(this).find("BASIC_INFO").each(function(i){
    		        			senjbinfo_num=$(this).children().length;
    		        			colTitle_json.push($(this).children("NAME").attr('DESC'));
    		        			colTitle_json.push($(this).children("MODEL").attr('DESC'));
    		        			colTitle_json.push($(this).children("HW_VER").attr('DESC'));
    		        			colTitle_json.push($(this).children("SW_VER").attr('DESC'));
    		        			colTitle_json.push($(this).children("FACTORY").attr('DESC'));
    		        			colTitle_json.push($(this).children("PROD_TIME").attr('DESC'));
    		        			colTitle_json.push($(this).children("USED_TIME").attr('DESC'));
    		        			colTitle_json.push($(this).children("CODE").attr('DESC'));
    		        			colValue_json.push($(this).children("NAME").text());
    		        			colValue_json.push($(this).children("MODEL").text());
    		        			colValue_json.push($(this).children("HW_VER").text());
    		        			colValue_json.push($(this).children("SW_VER").text());
    		        			colValue_json.push($(this).children("FACTORY").text());
    		        			colValue_json.push($(this).children("PROD_TIME").text());
    		        			colValue_json.push($(this).children("USED_TIME").text());
    		        			colValue_json.push($(this).children("CODE").text());

    		        		});
    		        		$(this).find("CONDITION_INFO").each(function(i){
    		        			sengkinfo_num=$(this).children().length;
    		        			var i=1;
    		        			$(this).children().each(function(i){
    		        				senyyTitle_json.push( $(this).attr('DESC'));
    		        				yyname_json.push($(this).children("MD5").attr('DESC'));
    		        				yyname_json.push($(this).children("NAME").attr('DESC'));
    		        				yyvalue_json.push($(this).children("NAME").text());
    		        				yyvalue_json.push($(this).children("MD5").text());
        		        			i++;
    		        			});
    		        		});
    		        	});
    		        }
    		    });
    		 showTable(ledjbinfo_num,ledgkinfo_num,senjbinfo_num,sengkinfo_num);

    	}
    };
    return _this;
}();

$(function(){
	jeecg.inf.getXMLS();
	$(".tabs li:eq(4)").click(function(){
		$("#d3").show();
	});
});
function showTable(ledjbinfo_num,ledgkinfo_num,senjbinfo_num,sengkinfo_num){
	var total_num=ledjbinfo_num+ledgkinfo_num*2+senjbinfo_num+sengkinfo_num*2;
	for(var i=0 ; i<total_num ;i++){
		$("#tab_inf").append('<tr id="'+("tr"+i)+'"></tr>');
			for(var j=0 ;j<5;j++){
				if(j==0){
					if(i==0){
						$("#tr"+i).append('<td rowspan='+(ledjbinfo_num+ledgkinfo_num*2)+' id="'+("td"+i+j)+'">LED装置</td>');
					}else if((i==(ledjbinfo_num+ledgkinfo_num*2))){
						$("#tr"+i).append('<td rowspan='+(senjbinfo_num+sengkinfo_num*2)+' id="'+("td"+i+j)+'">传感器装置</td>');
					}
				}else if(j==1){
					if(i==0){
						$("#tr"+i).append('<td rowspan='+(ledjbinfo_num)+' colspan=2 id="'+("td"+i+j)+'">基本信息</td>');
					}
					if(i==ledjbinfo_num){
						$("#tr"+i).append('<td rowspan='+(ledgkinfo_num*2)+' id="'+("td"+i+j)+'">工况信息</td>');
					}
					if(i==(ledjbinfo_num+ledgkinfo_num*2)){
						$("#tr"+i).append('<td rowspan='+(senjbinfo_num)+' colspan=2 id="'+("td"+i+j)+'">基本信息</td>');
					}
					if(i==(ledjbinfo_num+ledgkinfo_num*2+senjbinfo_num)){
						$("#tr"+i).append('<td rowspan='+(sengkinfo_num*2)+' id="'+("td"+i+j)+'">工况信息</td>');
					}
				}else if(j==2){
					if(i<ledjbinfo_num){
						$("#tr"+i).append('<td  id="'+("td"+i+j)+'">'+colTitle_json.shift().toString()+'</td>');
					}else if(i<(ledjbinfo_num+ledgkinfo_num*2)){
						if(i%2==0){
							$("#tr"+i).append('<td rowspan=2  id="'+("td"+i+j)+'">'+ledyyTitle_json.shift().toString()+'</td>');
						}
					}else if(i<(ledjbinfo_num+ledgkinfo_num*2+senjbinfo_num)){
						$("#tr"+i).append('<td  id="'+("td"+i+j)+'">'+colTitle_json.shift().toString()+'</td>');
					}
					else{
						if(i%2==0){
							$("#tr"+i).append('<td rowspan=2  id="'+("td"+i)+'">'+senyyTitle_json.shift().toString()+'</td>');
						}
					}
				}else if(j==3){
					if(i<ledjbinfo_num||((i>=ledjbinfo_num+ledgkinfo_num*2)&&(i<ledjbinfo_num+ledgkinfo_num*2+senjbinfo_num))){
						$("#tr"+i).append('<td  id="'+("td"+i+j)+'">'+colValue_json.shift().toString()+'</td>');
					}else{
						$("#tr"+i).append('<td  id="'+("td"+i+j)+'">'+yyname_json.shift().toString()+'</td>');
					}
				}else {
					if(i<ledjbinfo_num||((i>=ledjbinfo_num+ledgkinfo_num*2)&&(i<ledjbinfo_num+ledgkinfo_num*2+senjbinfo_num))){
					}else
					$("#tr"+i).append('<td  id="'+("td"+i+j)+'">'+yyvalue_json.shift().toString()+'</td>');
				}
			}
	}
}







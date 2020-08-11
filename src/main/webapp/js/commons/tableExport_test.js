//data:导出数据行
//head_data:表头json数组
//type:"excel"或者doc
//name:表名
var tableExport_test=function(data,head_data,type,name) {
      var defaults = {
        csvSeparator: ',',
        csvEnclosure: '"',
        consoleLog: false,
        displayTableName: true,
        escape: false,
        excelstyles: [ 'border-bottom', 'border-top', 'border-left', 'border-right' ],
        fileName: 'tableExport',
        htmlContent: false,
        ignoreColumn: [],
        jspdf: { orientation: 'p',
                 unit:'pt',
                 format:'a4',
                 margins: { left: 20, right: 10, top: 10, bottom: 10 },
                 autotable: { padding: 2,
                              lineHeight: 12,
                              fontSize: 8,
                              tableExport: { onAfterAutotable: null,
                                             onBeforeAutotable: null,
                                             onTable: null
                                           }
                            }
               },
        onCellData: null,
        outputMode: 'file',  // file|string|base64
        tbodySelector: 'tr',
        theadSelector: 'tr',
        tableName: 'myTableName',
        type: 'csv',
        worksheetName: 'xlsWorksheetName'
      };

      var el = this;
      var DownloadEvt = null;
      var rowspans = [];


        var rowIndex = 0;
        var excel='';
        var a=[];
        _weekcurr=$("#weekcurr").val();
		_yearcurr=$("#yearcurr").val();
        for (var i=0;i<7;i++)
		{
		a[i]=getBeginDateOfWeek(_yearcurr,_weekcurr,i);
		}
        // Header
        for(var i=0;i<data.dataList.length;i++){
        	if(defaults.displayTableName)
    		excel +="<span>";
        	excel +="<table style='border-color:#CCCCCC;border-width:1px;border-style:solid;border-collapse:collapse;'>";
    		excel +="<tr><th class='tdHeader' style='vertical-align: bottom;'>" + data.dataList[i].deviceName +  "</th></tr><tr><td><div>";

	        excel +="<table cellspacing='0' cellpadding='3'  rules='all' style='border-color:#CCCCCC;border-width:1px;border-style:solid;border-collapse:collapse;'>";
	        excel += "<tr style='font-weight:bold'>";

	        excel += "<td style='width:30%;font-size:14px;text-align: center;border:1px solid #000'>测量量名称</td>";
	        excel += "<td style='width:20%;font-size:14px;text-align: center;border:1px solid #000'>测量量值</td>";
	        excel += "<td style='width:20%;font-size:14px;text-align: center;border:1px solid #000'>状态</td>";
	        excel += "<td style='width:30%;font-size:14px;text-align: center;border:1px solid #000'>采集时间</td>";

//	        for(var m=0;m<head_data.length;m++){
//	        	excel += "<td style='width:100px;height:80px;font-size:14px;text-align: center;border:1px solid #000'>"+head_data[m].title+"</td>";
//	    	}

			excel += '</tr>';

	        // Row Vs Column
	        var rowCount=1;
	        for(var m=0;m<head_data.length;m++){
	            if(head_data[m].title.indexOf("阈值状态")<=0){
	            	excel += "<tr>";
		            excel += "<td style='width:30%;border:1px solid #000'>"+head_data[m].title+"</td>";
		            excel += "<td style='width:20%;border:1px solid #000'>"+(data.dataList[i][head_data[m].value]==null?"":data.dataList[i][head_data[m].value])+"</td>";
//		            if(head_data[m].value!=type){
		            	if((m+1)<head_data.length && head_data[(m+1)].title.indexOf("阈值状态")>0){
			            	excel += "<td style='width:20%;border:1px solid #000'>"+(data.dataList[i][head_data[(m+1)].value]==null?"阈值未设置":data.dataList[i][head_data[(m+1)].value])+"</td>";
			            }else{
			            	excel += "<td style='width:20%;border:1px solid #000'> -- </td>";
			            }
			            excel += "<td style='width:30%;border:1px solid #000'>"+(data.dataList[i].sampleTime==null?"":data.dataList[i].sampleTime)+"</td>";
//		            }
		            excel += '</tr>';
	            }
	      	}
	        excel += '</table>';
	        excel += '</div>';
	        excel += '</table>';
	        excel += '</span>';
        }
        if(defaults.consoleLog === true)
          console.log(excel);

        var excelFile = "<span>";
//        excelFile += '<meta http-equiv="content-type" content="application/vnd.ms-'+defaults.type+'; charset=UTF-8">';
//        excelFile += '<meta http-equiv="content-type" content="application/';
//        excelFile += (defaults.type == 'excel')? 'vnd.ms-excel' : 'msword';
//        excelFile += '; charset=UTF-8">';
//        excelFile += "<head>";
//        if (defaults.type == 'excel') {
//          excelFile += "<!--[if gte mso 9]>";
//          excelFile += "<xml>";
//          excelFile += "<x:ExcelWorkbook>";
//          excelFile += "<x:ExcelWorksheets>";
//          excelFile += "<x:ExcelWorksheet>";
//          excelFile += "<x:Name>";
//          excelFile += "sheet1";
//          excelFile += "</x:Name>";
//          excelFile += "<x:WorksheetOptions>";
//          excelFile += "<x:DisplayGridlines/>";
//          excelFile += "</x:WorksheetOptions>";
//          excelFile += "</x:ExcelWorksheet>";
//          excelFile += "</x:ExcelWorksheets>";
//          excelFile += "</x:ExcelWorkbook>";
//          excelFile += "</xml>";
//          excelFile += "<![endif]-->";
//        }
//        excelFile += "</head>";
//        excelFile += "<body>";
        excelFile += excel;
//        excelFile += "</body>";
        excelFile += "</span>";

        if(defaults.outputMode == 'string')
          return excelFile;

        var base64data = base64encode(excelFile);

        if(defaults.outputMode == 'base64')
          return base64data;

        var extension = (type == 'excel')? 'xls' : 'doc';
        try {
          var blob = new Blob([excelFile], {type: 'application/vnd.ms-'+type});
          saveAs (blob, name+'.'+extension);
        }
        catch (e) {
          downloadFile(name+'.'+extension, 'data:application/vnd.ms-'+type+';base64,' + base64data);
        }

      function ForEachVisibleCell(tableRow, selector, rowIndex, cellcallback) {

        $(tableRow).filter(':visible').find(selector).each(function(colIndex) {
          if ($(this).css('display') != 'none' &&
              $(this).data("tableexport-display") != 'none') {
            if(defaults.ignoreColumn.indexOf(colIndex) == -1) {
              if (typeof(cellcallback) === "function") {
                var cs = 0; // colspan value

                // handle previously detected rowspans
                if (typeof rowspans[rowIndex] != 'undefined' && rowspans[rowIndex].length > 0) {
                  for (c = 0; c <= colIndex; c++) {
                    if (typeof rowspans[rowIndex][c] != 'undefined') {
                      cellcallback(null, rowIndex, c);
                      delete rowspans[rowIndex][c];
                      colIndex++;
                    }
                  }
                }

                // output content of current cell
                cellcallback(this, rowIndex, colIndex);

                // handle colspan of current cell
                if ($(this).is("[colspan]")) {
                  cs = $(this).attr('colspan');
                  for (c = 0; c < cs - 1; c++)
                    cellcallback(null, rowIndex, colIndex + c);
                }

                // store rowspan for following rows
                if ($(this).is("[rowspan]")) {
                  var rs = parseInt($(this).attr('rowspan'));

                  for (r = 1; r < rs; r++) {
                    if (typeof rowspans[rowIndex + r] == 'undefined')
                      rowspans[rowIndex + r] = [];
                    rowspans[rowIndex + r][colIndex] = "";

                    for (c = 1; c < cs; c++)
                      rowspans[rowIndex + r][colIndex + c] = "";
                  }
                }

              }
            }
          }
        });
      }

      function jsPdfOutput(doc){
        if(defaults.consoleLog === true)
          console.log(doc.output());

        if(defaults.outputMode == 'string')
          return doc.output();

        if(defaults.outputMode == 'base64')
          return base64encode(doc.output());

        try {
          var blob = doc.output('blob');
          saveAs (blob, defaults.fileName + '.pdf');
        }
        catch (e) {
          downloadFile(defaults.fileName + '.pdf', 'data:application/pdf;base64,' + base64encode(doc.output()));
        }
      }

      function escapeRegExp(string){
        return string.replace(/([.*+?^=!:${}()|\[\]\/\\])/g, "\\$1");
      }

      function replaceAll(string, find, replace){
        return string.replace(new RegExp(escapeRegExp(find), 'g'), replace);
      }

      function csvString(cell, rowIndex, colIndex){
        var result = '';

        if (cell != null){
          var dataString = parseString(cell, rowIndex, colIndex);

          var csvValue = (dataString === null || dataString == '')? '' : dataString.toString();

          if ( dataString instanceof Date )
            result = defaults.csvEnclosure + dataString.toLocaleString() + defaults.csvEnclosure;
          else{
            result = replaceAll (csvValue, defaults.csvEnclosure, defaults.csvEnclosure + defaults.csvEnclosure);

            if ( result.indexOf (defaults.csvSeparator) >= 0 || /[\r\n ]/g.test(result) )
              result = defaults.csvEnclosure + result + defaults.csvEnclosure;
          }
        }

        return result;
      }

      function parseString(cell, rowIndex, colIndex){
        var result = '';

        if (cell != null){
          var $cell = $(cell);

          if(defaults.htmlContent === true){
            result = $cell.html().trim();
          }else{
            result = $cell.text().trim().replace(/\u00AD/g, ""); // remove soft hyphens
          }

          if(defaults.escape === true){
            result = escape(result);
          }

          if (typeof defaults.onCellData === 'function'){
            result = defaults.onCellData($cell, rowIndex, colIndex, result);
          }
        }

        return result;
      }

      function hyphenate (a, b, c){
        return b + "-" + c.toLowerCase();
      }

      // get computed style property
      function getStyle (target, prop){
        if(window.getComputedStyle){ // gecko and webkit
          prop = prop.replace(/([a-z])([A-Z])/, hyphenate);  // requires hyphenated, not camel
          return window.getComputedStyle(target, null).getPropertyValue(prop);
        }
        if(target.currentStyle){ // ie
          return target.currentStyle[prop];
        }
        return target.style[prop];
      }

      function getPropertyUnitValue (target, prop, unit){
        var baseline = 100;  // any number serves

        var value = getStyle(target, prop);  // get the computed style value

        var numeric = value.match(/\d+/);  // get the numeric component
        if(numeric !== null) {
          numeric = numeric[0];  // get the string

          var temp = document.createElement("div");  // create temporary element
          temp.style.overflow = "hidden";  // in case baseline is set too low
          temp.style.visibility = "hidden";  // no need to show it

          target.parentElement.appendChild(temp); // insert it into the parent for em, ex and %

          temp.style.width = baseline + unit;
          var factor = baseline / temp.offsetWidth;

          target.parentElement.removeChild(temp);  // clean up

          return (numeric * factor);
        }
        return 0;
      }

      function downloadFile(filename, data){
        var DownloadLink = document.createElement('a');

        if ( DownloadLink ){
          document.body.appendChild(DownloadLink);
          DownloadLink.style = 'display: none';
          DownloadLink.download = filename;
          DownloadLink.href = data;

          if ( document.createEvent ){
            if ( DownloadEvt == null )
              DownloadEvt = document.createEvent('MouseEvents');

            DownloadEvt.initEvent('click', true, false);
            DownloadLink.dispatchEvent(DownloadEvt);
          }
          else if ( document.createEventObject )
            DownloadLink.fireEvent('onclick');
          else if (typeof DownloadLink.onclick == 'function' )
            DownloadLink.onclick();

          document.body.removeChild(DownloadLink);
        }
      }

      function utf8Encode(string) {
        string = string.replace(/\x0d\x0a/g, "\x0a");
        var utftext = "";
        for (var n = 0; n < string.length; n++) {
          var c = string.charCodeAt(n);
          if (c < 128) {
            utftext += String.fromCharCode(c);
          }
          else if((c > 127) && (c < 2048)) {
            utftext += String.fromCharCode((c >> 6) | 192);
            utftext += String.fromCharCode((c & 63) | 128);
          }
          else {
            utftext += String.fromCharCode((c >> 12) | 224);
            utftext += String.fromCharCode(((c >> 6) & 63) | 128);
            utftext += String.fromCharCode((c & 63) | 128);
          }
        }
        return utftext;
      }

      function base64encode(input) {
        var keyStr = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";
        var output = "";
        var chr1, chr2, chr3, enc1, enc2, enc3, enc4;
        var i = 0;
        input = utf8Encode(input);
        while (i < input.length) {
          chr1 = input.charCodeAt(i++);
          chr2 = input.charCodeAt(i++);
          chr3 = input.charCodeAt(i++);
          enc1 = chr1 >> 2;
          enc2 = ((chr1 & 3) << 4) | (chr2 >> 4);
          enc3 = ((chr2 & 15) << 2) | (chr3 >> 6);
          enc4 = chr3 & 63;
          if (isNaN(chr2)) {
            enc3 = enc4 = 64;
          } else if (isNaN(chr3)) {
            enc4 = 64;
          }
          output = output +
            keyStr.charAt(enc1) + keyStr.charAt(enc2) +
            keyStr.charAt(enc3) + keyStr.charAt(enc4);
        }
        return output;
      }

      return this;
    }


function getBeginDateOfWeek(Year, week,i){

    var firstDay = GetFirstWeekBegDay(Year);
    //7*24*3600000 是一星期的时间毫秒数,(JS中的日期精确到毫秒)
    var time=(week-2)*7*24*3600000+i*24*3600000;
    var beginDay = firstDay;
    //为日期对象 date 重新设置成时间 time
    beginDay.setTime(firstDay.valueOf()+time);
    return formatDate(beginDay);
}
//获取某年的第一天
function GetFirstWeekBegDay(year) {
    var tempdate = new Date(year, 0, 1);
    var temp = tempdate.getDay();
    if (temp == 1){
       return tempdate;
    }
    temp = temp == 0 ? 7 : temp;
    tempdate = tempdate.setDate(tempdate.getDate() + (8 - temp));
    return new Date(tempdate);
}
function fixWidth(percent)
{
    return document.body.clientWidth * percent ;
}
//格式化日期：yyyy-MM-dd
function formatDate(date) {
    var myyear = date.getFullYear();
    var mymonth = date.getMonth()+1;
    var myweekday = date.getDate();

    if(mymonth < 10){
        mymonth = "0" + mymonth;
    }
    if(myweekday < 10){
        myweekday = "0" + myweekday;
    }
    return (myyear+"-"+mymonth + "-" + myweekday);
}


package com.scott.controller;

import com.base.entity.PubDeviceTypeEnum;
import com.base.util.GetXml;
import com.base.util.HtmlUtil;
import com.base.util.UrlUtil;
import com.base.util.edit.DateUtil;
import com.base.web.BaseAction;
import com.scott.entity.*;
import com.scott.page.HistoryPage;
import com.scott.service.DeviceHealthStateService;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.jdom.xpath.XPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <br>
 * <b>功能：</b>JeecgPersonController<br>
 * <b>作者：</b>www.jeecg.org<br>
 * <b>日期：</b> Feb 2, 2013 <br>
 * <b>版权所有：<b>版权所有(C) 2013，www.jeecg.org<br>
 */
@Controller
@RequestMapping("/deviceHealthState")
public class DeviceHealthStateController extends BaseAction {

    @Autowired(required = false)
    // 自动注入，不需要生成set方法了，required=false表示没有实现类，也不会报错。
    private DeviceHealthStateService<DeviceHealthStateEntity> deviceHealthStateService;

    /**
     * 跳转健康信息页
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("/list")
    public String list() {
        return "scott/demo/deviceHealthState";
    }

    /**
     * 跳转设备页
     *
     * @return
     * @throws Exception
     */

    @RequestMapping("/deviceDetail")
    public String DeviceDetail(String deviceID, HttpServletRequest request) {
        DeviceEntity deviceEntity = deviceHealthStateService.getDeviceByDeviceId(deviceID);
        //获取传过来的条件值并存到session中
        HttpSession session = request.getSession();
        session.removeAttribute("DeviceID");
        session.setAttribute("DeviceID", deviceID);
        session.removeAttribute("DeviceType");
        session.setAttribute("DeviceType", String.valueOf(deviceEntity.getDeviceType()));
        session.removeAttribute("DeviceName");
        session.setAttribute("DeviceName", deviceEntity.getDeviceName());
        //跳转到设备健康页面
        return "scott/demo/deviceDetailState";
    }

    /**
     * 跳转红外测温设备页
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("/spaceDetail")
    public String spaceDetail(String deviceType, HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.removeAttribute("DeviceType");
        session.setAttribute("DeviceType", deviceType);
        session.removeAttribute("DeviceName");
        session.setAttribute("DeviceName", PubDeviceTypeEnum.getValueEnum(deviceType).getText());
        return "scott/demo/spaceDetailState";
    }

    /**
     * type = 1 油色谱类型数据实时信息
     *
     * @param _off
     * @param _time
     * @return
     * @throws Exception
     */
    @RequestMapping("/getStomDetailList")
    @ResponseBody
    public List getStomDetailList(boolean _off, String _time) {
        List<StomYxEntity> dataList;
        if (_off == false) {
            dataList = deviceHealthStateService.getStomDetail();
        } else {
            dataList = deviceHealthStateService.getStomDetailByDate(_time);
        }
        setStomYxAlarmData(dataList);
        return dataList;
    }

    /**
     * type = 2 SF6气体压力类型数据实时信息
     *
     * @param _off
     * @param _time
     * @return
     */
    @RequestMapping("/getSf6DetailList")
    @ResponseBody
    public List getSf6DetailList(boolean _off, String _time) {
        List<Sf6YxEntity> dataList;
        if (_off == false) {
            dataList = deviceHealthStateService.getSf6Detail();
        } else {
            dataList = deviceHealthStateService.getSf6DetailByDate(_time);
        }
        setSf6AlarmData(dataList);
        return dataList;
    }

    /**
     * type = 3 避雷器及动作次数
     *
     * @param _off
     * @param _time
     * @return
     */
    @RequestMapping("/getSmoamDetailList")
    @ResponseBody
    public List getSmoamDetailList(boolean _off, String _time) {
        List<SmoamYxEntity> dataList;
        if (_off == false) {
            dataList = deviceHealthStateService.getSmoamDetail();
        } else {
            dataList = deviceHealthStateService.getSmoamDetailByDate(_time);
        }
        getSmoamAlarmData(dataList);
        return dataList;
    }

    /**
     * type = 4 铁芯泄露电流
     *
     * @param _off
     * @param _time
     * @return
     */
    @RequestMapping("/getScomDetailList")
    @ResponseBody
    public List getScomDetailList(boolean _off, String _time) {
        List<ScomYxEntity> dataList;
        if (_off == false) {
            dataList = deviceHealthStateService.getScomDetail();
        } else {
            dataList = deviceHealthStateService.getScomDetailByDate(_time);
        }
        getScomAlarmData(dataList);
        return dataList;
    }


    @RequestMapping("/getSpdmDetailListExportWord")
    @ResponseBody
    public Map<String, Object> getSpdmDetailListExportWord() {
        List<SpdmYxEntity> dataList = deviceHealthStateService.getSpdmDetail();
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        getSpdmAlarmData(dataList);
        jsonMap.put("dataList", dataList);
        return jsonMap;
    }

    /**
     * type = 19 局方
     *
     * @param _off
     * @param _time
     */
    @RequestMapping("/getSpdmDetailList")
    @ResponseBody
    public List getSpdmDetailList(boolean _off, String _time) {
        List<SpdmYxEntity> dataList;
        if (_off == false) {
            dataList = deviceHealthStateService.getSpdmDetail();
        } else {
            dataList = deviceHealthStateService.getSpdmDetailByDate(_time);
        }
        getSpdmAlarmData(dataList);
        return dataList;
    }



    /**
     * 获取油色谱历史数据
     *
     * @param url
     * @param classifyId
     * @return
     * @
     */
    @RequestMapping("/getstomHistoryData")
    public void getstomHistoryData(HistoryPage page,
                                   HttpServletResponse response) {
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        List<stom_dataEntity> dataList = deviceHealthStateService.getYSPHistory(page);
        jsonMap.put("total", page.getPager().getRowCount());
        jsonMap.put("rows", dataList);
        HtmlUtil.writerJson(response, jsonMap);
    }

    /**
     * 获取套管历史数据
     *
     * @param url
     * @param classifyId
     * @return
     * @
     */
    //套管
    @RequestMapping("/getSbushHistoryData")
    public void getSbushHistoryData(HistoryPage page,
                                    HttpServletResponse response) {
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        List<SbushDataEntity> dataList = deviceHealthStateService.getSbushHistoryData(page);
        jsonMap.put("total", page.getPager().getRowCount());
        jsonMap.put("rows", dataList);
        HtmlUtil.writerJson(response, jsonMap);
    }

    //局放
    @RequestMapping("/getSpdcHistoryData")
    public void getSpdcHistoryData(HistoryPage page,
                                   HttpServletResponse response) {
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        List<SbushDataEntity> dataList = deviceHealthStateService.getSpdcHistoryData(page);
        jsonMap.put("total", page.getPager().getRowCount());
        jsonMap.put("rows", dataList);
        HtmlUtil.writerJson(response, jsonMap);
    }

    /**
     * 导出油色谱历史数据
     *
     * @param url
     * @param classifyId
     * @return
     * @
     */
    @RequestMapping("/exportstomHistoryData")
    public void exportstomexportData(HistoryPage page,
                                     HttpServletResponse response) {
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        List<stom_dataEntity> dataList = deviceHealthStateService.exportYSPHistory(page);
        String id = page.getId();
        String Dname = deviceHealthStateService.getDeviceName(id);
        dataList.get(0).setDeviceName(Dname);
        jsonMap.put("total", page.getPager().getRowCount());
        jsonMap.put("dataList", dataList);
        HtmlUtil.writerJson(response, jsonMap);
    }

    /**
     * 获取红外测温历史数据
     *
     * @param url
     * @param classifyId
     * @return
     * @
     */
    @RequestMapping("/getInfraredHistoryData")
    public void getInfraredHistoryData(HistoryPage page,
                                       HttpServletResponse response) {
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        List<Sf6_dataEntity> dataList = deviceHealthStateService.getInfraredHistory(page);
        jsonMap.put("total", page.getPager().getRowCount());
        jsonMap.put("rows", dataList);
        HtmlUtil.writerJson(response, jsonMap);
    }

    /**
     * 获取sf6历史数据
     *
     * @param url
     * @param classifyId
     * @return
     * @
     */
    @RequestMapping("/getSf6HistoryData")
    public void getSf6HistoryData(HistoryPage page,
                                  HttpServletResponse response) {
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        List<Sf6_dataEntity> dataList = deviceHealthStateService.getSF6History(page);
        jsonMap.put("total", page.getPager().getRowCount());
        jsonMap.put("rows", dataList);
        HtmlUtil.writerJson(response, jsonMap);
    }

    /**
     * 导出sf6历史数据
     *
     * @param url
     * @param classifyId
     * @return
     * @
     */
    @RequestMapping("/exportSf6HistoryData")
    public void exportSf6HistoryData(HistoryPage page,
                                     HttpServletResponse response) {
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        List<Sf6_dataEntity> dataList = deviceHealthStateService.exportSF6History(page);
        jsonMap.put("total", page.getPager().getRowCount());
        jsonMap.put("dataList", dataList);
        HtmlUtil.writerJson(response, jsonMap);
    }

    /**
     * 获取避雷器历史数据
     *
     * @param url
     * @param classifyId
     * @return
     * @
     */
    @RequestMapping("/getSMOAMHistoryData")
    public void getSMOAMHistoryData(HistoryPage page,
                                    HttpServletResponse response) {
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        List<SMOAM_dataEntity> dataList = deviceHealthStateService.getSMOAMHistory(page);
        jsonMap.put("total", page.getPager().getRowCount());
        jsonMap.put("rows", dataList);
        HtmlUtil.writerJson(response, jsonMap);
    }

    /**
     * 导出避雷器历史数据
     *
     * @param url
     * @param classifyId
     * @return
     * @
     */
    @RequestMapping("/exportSMOAMHistoryData")
    public void exportSMOAMHistoryData(HistoryPage page,
                                       HttpServletResponse response) {
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        List<SMOAM_dataEntity> dataList = deviceHealthStateService.exportSMOAMHistory(page);
        jsonMap.put("total", page.getPager().getRowCount());
        jsonMap.put("dataList", dataList);
        HtmlUtil.writerJson(response, jsonMap);
    }

    /**
     * 获取铁芯历史数据
     *
     * @param url
     * @param classifyId
     * @return
     * @
     */
    @RequestMapping("/getSCOMHistoryData")
    public void getSCOMHistoryData(HistoryPage page,
                                   HttpServletResponse response) {
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        List<SCOM_dataEntity> dataList = deviceHealthStateService.getSCOMHistory(page);
        jsonMap.put("total", page.getPager().getRowCount());
        jsonMap.put("rows", dataList);
        HtmlUtil.writerJson(response, jsonMap);
    }

    /**
     * 导出铁芯历史数据
     *
     * @param url
     * @param classifyId
     * @return
     * @
     */
    @RequestMapping("/exportSCOMHistoryData")
    public void exportSCOMHistoryData(HistoryPage page,
                                      HttpServletResponse response) {
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        List<SCOM_dataEntity> dataList = deviceHealthStateService.exportSCOMHistory(page);
        jsonMap.put("total", page.getPager().getRowCount());
        jsonMap.put(" dataList", dataList);
        HtmlUtil.writerJson(response, jsonMap);
    }

    /**
     * 获取工况历史数据
     *
     * @param url
     * @param classifyId
     * @return
     * @
     */
    @RequestMapping("/getSPDMHistoryData")
    public void getSconditionHistoryData(HistoryPage page,
                                         HttpServletResponse response) {
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        List<Spdm_dataEntity> dataList = deviceHealthStateService.getSpdmHistoryData(page);
        jsonMap.put("total", page.getPager().getRowCount());
        jsonMap.put("rows", dataList);
        HtmlUtil.writerJson(response, jsonMap);

    }

    @RequestMapping("/exportSPDMHistoryData")
    public void exportSconditionHistoryData(HistoryPage page,
                                            HttpServletResponse response) {
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        List<Spdm_dataEntity> dataList = deviceHealthStateService.exportSpdmHistoryData(page);
        jsonMap.put("total", page.getPager().getRowCount());
        jsonMap.put("dataList", dataList);
        HtmlUtil.writerJson(response, jsonMap);
    }

    /**
     * 获取图表中同类设备
     *
     * @param DeviceType
     * @param response
     * @param request
     * @
     */
    @RequestMapping("/getDeviceByType")
    public void getDeviceByType(String DeviceType,
                                HttpServletResponse response) {
        List<DeviceEntity> dataList = new ArrayList<DeviceEntity>();
        if ("hwcw".equals(DeviceType)) {
            dataList = deviceHealthStateService.getInfraredByType();
        } else {
            dataList = deviceHealthStateService.getDeviceByType(DeviceType);
        }

        HtmlUtil.writerJson(response, dataList);
    }

    /**
     * 获取红外测温图表中需要的数据
     *
     * @param url
     * @param classifyId
     * @return
     * @
     */
    @RequestMapping("/getInfraredChart_Value")
    public void getInfraredChart_Value(
            HttpServletResponse response, HttpServletRequest request) {
        Map<String, Object> param = new HashMap<String, Object>();
        List<List<Sf6_dataEntity>> dataLists = new ArrayList<List<Sf6_dataEntity>>();
        Map<String, List<Sf6_dataEntity>> maps = new HashMap<String, List<Sf6_dataEntity>>();
        String startTime = request.getParameter("_startTime");
        String endTime = request.getParameter("_endTime");

        if (startTime == null || startTime == "") {
            startTime = getStatetime();
        }
        param.put("startTime", startTime);
        param.put("endTime", endTime);
        String id = request.getParameter("DeviceID");
        String[] ids = id.split(",");
        for (int nn = 1; nn < ids.length; nn++) {
            id = ids[nn].split("_")[1];
            param.put("id", id);
            List<Sf6_dataEntity> dataList = new ArrayList<Sf6_dataEntity>();
            dataList = deviceHealthStateService.getInfraredChart_history(param);
            maps.put("dataList" + nn, dataList);
        }
        HtmlUtil.writerJson(response, maps);
    }

    /**
     * 获取SF6图表中需要的数据
     *
     * @param url
     * @param classifyId
     * @return
     * @
     */
    @RequestMapping("/getSF6ChartValue")
    public void getSF6ChartValue(
            HttpServletResponse response, HttpServletRequest request) {
        Map<String, Object> param = new HashMap<String, Object>();
        Map<String, List<Sf6_dataEntity>> maps = new HashMap<String, List<Sf6_dataEntity>>();
        String startTime = request.getParameter("_startTime");
        String endTime = request.getParameter("_endTime");

        if (startTime == null || startTime == "") {
            startTime = getStatetime();
        }
        param.put("startTime", startTime);
        param.put("endTime", endTime);
        String id = request.getParameter("DeviceID");
        String[] ids = id.split(",");
        for (int nn = 1; nn < ids.length; nn++) {
            id = ids[nn].split("_")[1];
            param.put("id", id);
            List<Sf6_dataEntity> dataList = null;
            dataList = deviceHealthStateService.getSF6Chart_history(param);
            if (dataList.size() > 0) {
                maps.put("dataList" + nn, dataList);
            }
        }
        HtmlUtil.writerJson(response, maps);
    }

    /**
     * 获取油色谱图表中需要的数据
     *
     * @param url
     * @param classifyId
     * @return
     * @
     */
    @RequestMapping("/getStomChartValue")
    public void getStomChartValue(
            HttpServletResponse response, HttpServletRequest request) {
        Map<String, Object> param = new HashMap<String, Object>();
        Map<String, List<stom_dataEntity>> maps = new HashMap<String, List<stom_dataEntity>>();
        String startTime = request.getParameter("_startTime");
        String endTime = request.getParameter("_endTime");
        if (startTime == null || startTime == "") {
            startTime = getStatetime();
        }
        param.put("startTime", startTime);
        param.put("endTime", endTime);
        String id = request.getParameter("DeviceID");
        String[] ids = id.split(",");
        for (int nn = 1; nn < ids.length; nn++) {
            id = ids[nn].split("_")[1];
            param.put("id", id);
            List<stom_dataEntity> dataList = null;
            dataList = deviceHealthStateService.getStomChart_history(param);
            if (dataList.size() > 0) {
                maps.put("dataList" + nn, dataList);
            }
        }

        HtmlUtil.writerJson(response, maps);
    }

    /**
     * 获取避雷器图表中需要的数据
     *
     * @param url
     * @param classifyId
     * @return
     * @
     */
    @RequestMapping("/getSmoamChartValue")
    public void getSmoamChartValue(
            HttpServletResponse response, HttpServletRequest request) {
        Map<String, Object> param = new HashMap<String, Object>();
        Map<String, List<SMOAM_dataEntity>> maps = new HashMap<String, List<SMOAM_dataEntity>>();
        String startTime = request.getParameter("_startTime");
        String endTime = request.getParameter("_endTime");
        if (startTime == null || startTime == "") {
            startTime = getStatetime();
        }
        param.put("startTime", startTime);
        param.put("endTime", endTime);
        String id = request.getParameter("DeviceID");
        String[] ids = id.split(",");
        for (int nn = 1; nn < ids.length; nn++) {
            id = ids[nn].split("_")[1];
            param.put("id", id);
            List<SMOAM_dataEntity> dataList = null;
            dataList = deviceHealthStateService.getSmoamChart_history(param);
            if (dataList.size() > 0) {
                maps.put("dataList" + nn, dataList);
            }
        }
        HtmlUtil.writerJson(response, maps);
    }

    /**
     * 获取铁芯图表中需要的数据
     *
     * @param url
     * @param classifyId
     * @return
     * @
     */
    @RequestMapping("/getScomChartValue")
    public void getScomChartValue(
            HttpServletResponse response, HttpServletRequest request) {
        Map<String, Object> param = new HashMap<String, Object>();
        Map<String, List<SCOM_dataEntity>> maps = new HashMap<String, List<SCOM_dataEntity>>();
        String startTime = request.getParameter("_startTime");
        String endTime = request.getParameter("_endTime");
        if (startTime == null || startTime == "") {
            startTime = getStatetime();
        }
        param.put("startTime", startTime);
        param.put("endTime", endTime);
        String id = request.getParameter("DeviceID");
        String[] ids = id.split(",");
        for (int nn = 1; nn < ids.length; nn++) {
            id = ids[nn].split("_")[1];
            param.put("id", id);
            List<SCOM_dataEntity> dataList = null;
            dataList = deviceHealthStateService.getScomChart_history(param);
            if (dataList.size() > 0) {
                maps.put("dataList" + nn, dataList);
            }
        }
        HtmlUtil.writerJson(response, maps);
    }

    /**
     * 获取局放图表中需要的数据
     *
     * @param url
     * @param classifyId
     * @return
     * @
     */
    @RequestMapping("/getSpdmChart_Value")
    public void getSpdmChart_history(
            HttpServletResponse response, HttpServletRequest request) {
        Map<String, Object> param = new HashMap<String, Object>();
        Map<String, List<Spdm_dataEntity>> maps = new HashMap<String, List<Spdm_dataEntity>>();
        String startTime = request.getParameter("_startTime");
        String endTime = request.getParameter("_endTime");
        if (startTime == null || startTime == "") {
            startTime = getStatetime();
        }
        param.put("startTime", startTime);
        param.put("endTime", endTime);
        String id = request.getParameter("DeviceID");
        String[] ids = id.split(",");
        for (int nn = 1; nn < ids.length; nn++) {
            id = ids[nn].split("_")[1];
            param.put("id", id);
            List<Spdm_dataEntity> dataList = null;
            dataList = deviceHealthStateService.getSpdmChart_history(param);
            if (dataList.size() > 0) {
                maps.put("dataList" + nn, dataList);
            }
        }
        HtmlUtil.writerJson(response, maps);
    }

    /**
     * 获取infxml文件
     *
     * @param url
     * @param classifyId
     * @return
     * @
     */
    @RequestMapping("/getInfXml")
    public void getInfXml(
            HttpServletResponse response, HttpServletRequest request) throws Exception {
        String strXml = null;
        //获取根据id获取device表中的LN
        String id = request.getParameter("DeviceID");//获取DeviceID
        String type = request.getParameter("Type");//获取Type
        String ln = deviceHealthStateService.getDeviceLN(id);//获取设备
        String file_path = "";
        if ("1".equals(type)) {
            file_path = UrlUtil.getUrlUtil().getYspinf();
        }
        if ("2".equals(type)) {
            file_path = UrlUtil.getUrlUtil().getSf6inf();
        }
        if ("3".equals(type)) {
            file_path = UrlUtil.getUrlUtil().getBlqinf();
        }
        if ("19".equals(type)) {
            file_path = UrlUtil.getUrlUtil().getJfinf();
        }
        if ("4".equals(type)) {
            file_path = UrlUtil.getUrlUtil().getScominf();
        }
        ln = ln.substring(ln.indexOf("/") + 1);
        String xmlName = "";
        File f = new File(file_path);//组成文件类，问题：\\CAC\\iec61850\\fileserv\\HZSH_SF6121地址指向的是哪里
        String[] files = f.list();//文件名，问题：获取到的files为null
        for (int i = 0; i < files.length; i++) {
            if (files[i].indexOf(ln) >= 0) {
                xmlName = files[i];
                break;
            }
        }
        GetXml xml = new GetXml(file_path + xmlName);
        strXml = xml.XmlToString();
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.print(strXml);
        out.flush();
    }


    public static String getStatetime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -7);
        Date monday = c.getTime();
        String preMonday = sdf.format(monday);
        return preMonday;
    }

    @RequestMapping("/getYXData")
    public void getYXData(HistoryPage page,
                          HttpServletResponse response, HttpServletRequest request) throws Exception {
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        //获取ln和ld
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        String id = request.getParameter("id");
        List<DataEntity> dataList = new ArrayList<DataEntity>();
        String IEC61850LD_LN = deviceHealthStateService.getyxLDLN(id);
        IEC61850LD_LN = IEC61850LD_LN + "$";
        List<DataEntity> dataLists = deviceHealthStateService.getYXData(IEC61850LD_LN);
        for (int i = 0; i < dataLists.size(); i++) {
            if ("1".equals(dataLists.get(i).getIed_type_id())) {
                String refname = dataLists.get(i).getRefname();
                String[] refnames = refname.split("\\$");
                refname = refnames[(refnames.length - 1)];
                String ref = deviceHealthStateService.getyxDesc(refname);
                if ("".equals(ref) || ref == null) {
                    dataLists.get(i).setRefname(refname);
                } else {
                    dataLists.get(i).setRefname(ref);
                }
                dataList.add(dataLists.get(i));
            } else if ("0".equals(dataLists.get(i).getIed_type_id())) {
                if (true
//                        dataLists.get(i).getRefname().indexOf("$ST$GasUnPresAlm") > -1 ||
//                        dataLists.get(i).getRefname().indexOf("$ST$MoDevConf") > -1 ||
//                        dataLists.get(i).getRefname().indexOf("$ST$MoDevConF") > -1 ||
//                        dataLists.get(i).getRefname().indexOf("$ST$SupDevRun") > -1 ||
//                        dataLists.get(i).getRefname().indexOf("$ST$DschCnt") > -1
                ) {
                    String refname = dataLists.get(i).getRefname();
                    String[] refnames = refname.split("\\$");
                    refname = refnames[(refnames.length - 1)];
                    String ref = deviceHealthStateService.getyxDesc(refname);
                    if ("".equals(ref) || ref == null) {
                        dataLists.get(i).setRefname(refname);
                    } else {
                        dataLists.get(i).setRefname(ref);
                    }
                    if (!(dataLists.get(i).getRefname().indexOf("放电频次") > -1)) {
                        if ("0".equals(dataLists.get(i).getValue())) {
                            dataLists.get(i).setValue("正常");
                        } else {
                            dataLists.get(i).setValue("异常");
                        }
                    }
                    dataList.add(dataLists.get(i));
                }
            }
        }
         /*List<DataEntity> dataLists = deviceHealthStateService.getYXData(id);
         for(int i=0 ; i<dataLists.size() ; i++){
        	 if(!("".equals(dataLists.get(i)))&&dataLists.get(i)!=null){
            	dataList.add(dataLists.get(i));
        	 }
         }*/
        jsonMap.put("total", page.getPager().getRowCount());
        jsonMap.put("rows", dataList);
        HtmlUtil.writerJson(response, jsonMap);
    }

    /**
     * 获取遥信历史数据开始
     *
     * @param page
     * @param response
     * @param request
     * @
     */


    @RequestMapping("/getstomYXHistoryData")
    public void getstomYXHistoryData(HistoryPage page,
                                     HttpServletResponse response) {
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        List<YXhistroyData> dataList = deviceHealthStateService.getstomYXHistoryData(page);
        jsonMap.put("total", page.getPager().getRowCount());
        jsonMap.put("rows", dataList);
        HtmlUtil.writerJson(response, jsonMap);
    }

    @RequestMapping("/getSf6YXHistoryData")
    public void getSf6YXHistoryData(HistoryPage page,
                                    HttpServletResponse response) {
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        List<YXhistroyData> dataList = deviceHealthStateService.getSf6YXHistoryData(page);
        jsonMap.put("total", page.getPager().getRowCount());
        jsonMap.put("rows", dataList);
        HtmlUtil.writerJson(response, jsonMap);
    }

    @RequestMapping("/getSmoamYXHistory")
    public void getSmoamYXHistory(HistoryPage page,
                                  HttpServletResponse response) {
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        List<YXhistroyData> dataList = deviceHealthStateService.getSmoamYXHistoryData(page);
        jsonMap.put("total", page.getPager().getRowCount());
        jsonMap.put("rows", dataList);
        HtmlUtil.writerJson(response, jsonMap);
    }

    @RequestMapping("/getScomYXHistoryData")
    public void getScomYXHistoryData(HistoryPage page,
                                     HttpServletResponse response) {
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        List<YXhistroyData> dataList = deviceHealthStateService.getScomYXHistoryData(page);
        jsonMap.put("total", page.getPager().getRowCount());
        jsonMap.put("rows", dataList);
        HtmlUtil.writerJson(response, jsonMap);
    }

    @RequestMapping("/getSpdmYXHistoryData")
    public void getSpdmYXHistoryData(HistoryPage page,
                                     HttpServletResponse response) {
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        List<YXhistroyData> dataList = deviceHealthStateService.getSpdmYXHistoryData(page);
        jsonMap.put("total", page.getPager().getRowCount());
        jsonMap.put("rows", dataList);
        HtmlUtil.writerJson(response, jsonMap);
    }

    @RequestMapping("/UpdateMap")
    public void UpdateMap(HttpServletResponse response, HttpServletRequest request) throws Exception {
        //开始解析参数
        String info = request.getParameter("info");
        String[] data = info.split(",");
        String id = data[0];
        String y = data[1];
        String x = data[2];
        String index = data[3];
        //根据index选择不同xml文件
        String rootPath = request.getSession().getServletContext().getRealPath("");
        new UrlUtil();
        rootPath = rootPath + UrlUtil.getUrlUtil().getMapPath();
        if (index.equals("1")) {
            rootPath += "data_gis.xml";
        } else if (index.equals("2")) {
            rootPath += "data_ft.xml";
        } else if (index.equals("3")) {
            rootPath += "data_zlc.xml";
        } else if (index.equals("4")) {
            rootPath += "data_jlc.xml";
        } else if (index.equals("5")) {
            rootPath += "data_zyb.xml";
        }
        SAXBuilder bulider = new SAXBuilder();
        InputStream inSt = new FileInputStream(rootPath);
        Document document = bulider.build(inSt);
        XPath path = XPath.newInstance("/map/state[devid='" + id + "']/loc");
        List<Element> list = path.selectNodes(document);
        Element item = list.get(0);
        String s = item.getText();
        item.setText(x + "," + y);
        String t = item.getText();
        inSt.close();
        // 将doc对象输出到文件
        try {
            // 创建xml文件输出流
            XMLOutputter xmlopt = new XMLOutputter();

            // 创建文件输出流
            FileOutputStream writer = new FileOutputStream(rootPath);

            // 指定文档格式
            Format fm = Format.getPrettyFormat();
            fm.setEncoding("UTF-8");
            xmlopt.setFormat(fm);

            // 将doc写入到指定的文件中
            xmlopt.output(document, writer);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void setStomYxAlarmData(List<StomYxEntity> dataList) {
        Long time = Calendar.getInstance().getTime().getTime();
        for (int ii = 0; ii < dataList.size(); ii++) {
            StomYxEntity alarmData = dataList.get(ii);
            if (null != alarmData.getSampleTime()) {
                Long time2 = DateUtil.computCuurentTimeDataPlus1(alarmData.getSampleTime());
                if (time.compareTo(time2) > 0) {
                    alarmData.setType("超时");
                } else {
                    alarmData.setType("正常");
                }
            } else {
                alarmData.setType("无实时数据");
            }
        }
    }

    private void setSf6AlarmData(List<Sf6YxEntity> dataList) {
        Long time = Calendar.getInstance().getTime().getTime();
        for (int ii = 0; ii < dataList.size(); ii++) {
            Sf6YxEntity alarmData = dataList.get(ii);
            if (null != alarmData.getSampleTime()) {
                Long time2 = DateUtil.computCuurentTimeDataPlus1(alarmData.getSampleTime());
                if (time.compareTo(time2) > 0) {
                    alarmData.setType("超时");
                } else {
                    alarmData.setType("正常");
                }
            } else {
                alarmData.setType("无实时数据");
            }
        }
    }

    private void getSmoamAlarmData(List<SmoamYxEntity> dataList) {
        Long time = Calendar.getInstance().getTime().getTime();
        for (int ii = 0; ii < dataList.size(); ii++) {
            SmoamYxEntity alarmData = dataList.get(ii);
            if (null != alarmData.getSampleTime()) {
                Long time2 = DateUtil.computCuurentTimeDataPlus1(alarmData.getSampleTime());
                if (time.compareTo(time2) > 0) {
                    alarmData.setType("超时");
                } else {
                    alarmData.setType("正常");
                }
            } else {
                alarmData.setType("无实时数据");
            }
        }
    }

    private void getScomAlarmData(List<ScomYxEntity> dataList) {
        Long time = Calendar.getInstance().getTime().getTime();
        for (int ii = 0; ii < dataList.size(); ii++) {
            ScomYxEntity alarmData = dataList.get(ii);
            if (null != alarmData.getSampleTime()) {
                Long time2 = DateUtil.computCuurentTimeDataPlus1(alarmData.getSampleTime());
                if (time.compareTo(time2) > 0) {
                    alarmData.setType("超时");
                } else {
                    alarmData.setType("正常");
                }
            } else {
                alarmData.setType("无实时数据");
            }
        }
    }

    private void getSpdmAlarmData(List<SpdmYxEntity> dataList) {
        Long time = Calendar.getInstance().getTime().getTime();
        for (int ii = 0; ii < dataList.size(); ii++) {
            SpdmYxEntity alarmData = dataList.get(ii);
            if (null != alarmData.getSampleTime()) {
                Long time2 = DateUtil.computCuurentTimeDataPlus1(alarmData.getSampleTime());
                if (time.compareTo(time2) > 0) {
                    alarmData.setType("超时");
                } else {
                    alarmData.setType("正常");
                }
            } else {
                alarmData.setType("无实时数据");
            }
        }
    }
}

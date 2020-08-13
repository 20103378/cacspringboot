package com.scott.controller;

import com.base.entity.PubDeviceTypeEnum;
import com.base.util.GetXml;
import com.base.util.HtmlUtil;
import com.base.util.UrlUtil;
import com.base.web.BaseAction;
import com.scott.entity.*;
import com.scott.page.HistoryPage;
import com.scott.service.DeviceHealthStateService;
import com.scott.service.TreeDeviceService;
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
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.text.ParseException;
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
        session.setAttribute("DeviceType", deviceEntity.getDeviceType());
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


    @RequestMapping("/getDetailListDevice")
    public void getDetailListDevice(String Type,
                                    HttpServletResponse response) {
        List<DeviceEntity> date = deviceHealthStateService.getDetailListDevice(Type);
        HtmlUtil.writerJson(response, date);
    }

    @RequestMapping("/getStomDetailList")
    public void getStomDetailList(boolean _off, String _time,
                                  HttpServletResponse response) throws Exception {
        List<StomYxEntity> dataList = new ArrayList<StomYxEntity>();
        if (_off == false) {
            List<StomYxEntity> date = deviceHealthStateService.getStomDetailDate();
            List<StomYxEntity> data = deviceHealthStateService.getStomDetail();
//    		List<StomAlarmEntity> Alarm = deviceHealthStateService.getStomAlarm();
            dataList = new ArrayList<StomYxEntity>();
            for (int ii = 0; ii < data.size(); ii++) {
                dataList.add(date.get(ii));
                dataList.add(data.get(ii));
                if (null == data.get(ii).getH2ppm()) {
                    StomYxEntity AlarmData = new StomYxEntity(1);
                    dataList.add(AlarmData);
                    continue;
                }
            }
        } else {
            List<StomYxEntity> date = deviceHealthStateService.getStomDetailDateByDate(_time);
            List<StomYxEntity> data = deviceHealthStateService.getStomDetailByDate(_time);
//    		List<StomAlarmEntity> Alarm = deviceHealthStateService.getStomAlarm();
            dataList = new ArrayList<StomYxEntity>();
            for (int ii = 0; ii < data.size(); ii++) {
                dataList.add(date.get(ii));
                dataList.add(data.get(ii));
                if (null == data.get(ii).getH2ppm()) {
                    StomYxEntity AlarmData = new StomYxEntity(1);
                    dataList.add(AlarmData);
                    continue;
                }
            }
        }
        HtmlUtil.writerJson(response, dataList);
    }

    @RequestMapping("/getStomDetailListExportWord")
    public void getStomDetailListExportWord(
            HttpServletResponse response) throws Exception {

        Map<String, Object> jsonMap = new HashMap<String, Object>();
        List<StomYxEntity> data = deviceHealthStateService.getStomDetail();
//		List<StomAlarmEntity> Alarm = deviceHealthStateService.getStomAlarm();
        List<StomYxEntity> dataList = new ArrayList<StomYxEntity>();
        Date dt = new Date();
        Long time = dt.getTime();
        for (int ii = 0; ii < data.size(); ii++) {
            StomYxEntity AlarmData = data.get(ii);
            if (null != AlarmData.getSampleTime()) {
                String dateString = AlarmData.getSampleTime();
                dateString = dateString.split(" ")[0];
                Long time2 = 0L;
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = sdf.parse(dateString);
                    time2 = date.getTime() + 86400000;
                } catch (ParseException e) {
                    System.out.println(e.getMessage());
                }
                if (time.compareTo(time2) > 0) {
                    AlarmData.setType("超时");
                } else {
                    AlarmData.setType("正常");
                }
            } else {
                AlarmData.setType("无实时数据");
            }

            dataList.add(AlarmData);
        }
        jsonMap.put("dataList", dataList);
        HtmlUtil.writerJson(response, jsonMap);
    }

    @RequestMapping("/getSf6DetailListExportWord")
    public void getSf6DetailListExportWord(
            HttpServletResponse response) throws Exception {
        List<Sf6YxEntity> data = deviceHealthStateService.getSf6Detail();
//		List<Sf6AlarmEntity> Alarm = deviceHealthStateService.getSf6Alarm();
        List<Sf6YxEntity> dataList = new ArrayList<Sf6YxEntity>();
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        Date dt = new Date();
        Long time = dt.getTime();
        for (int ii = 0; ii < data.size(); ii++) {
            Sf6YxEntity AlarmData = data.get(ii);
            if (null != AlarmData.getSampleTime()) {
                String dateString = AlarmData.getSampleTime();
                dateString = dateString.split(" ")[0];
                Long time2 = 0L;
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = sdf.parse(dateString);
                    time2 = date.getTime() + 86400000;
                } catch (ParseException e) {
                    System.out.println(e.getMessage());
                }
                if (time.compareTo(time2) > 0) {
                    AlarmData.setType("超时");
                } else {
                    AlarmData.setType("正常");
                }
            } else {
                AlarmData.setType("无实时数据");
            }
            dataList.add(AlarmData);
        }
        jsonMap.put("dataList", dataList);
        HtmlUtil.writerJson(response, jsonMap);
    }

    @RequestMapping("/getSmoamDetailListExportWord")
    public void getSmoamDetailListExportWord(
            HttpServletResponse response) throws Exception {
        List<SmoamYxEntity> data = deviceHealthStateService.getSmoamDetail();
//		List<SmoamAlarmEntity> Alarm = deviceHealthStateService.getSmoamAlarm();
        List<SmoamYxEntity> dataList = new ArrayList<SmoamYxEntity>();
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        Date dt = new Date();
        Long time = dt.getTime();
        for (int ii = 0; ii < data.size(); ii++) {
            SmoamYxEntity AlarmData = data.get(ii);
            if (null != AlarmData.getSampleTime()) {
                String dateString = AlarmData.getSampleTime();
                dateString = dateString.split(" ")[0];
                Long time2 = 0L;
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = sdf.parse(dateString);
                    time2 = date.getTime() + 86400000;
                } catch (ParseException e) {
                    System.out.println(e.getMessage());
                }
                if (time.compareTo(time2) > 0) {
                    AlarmData.setType("超时");
                } else {
                    AlarmData.setType("正常");
                }
            } else {
                AlarmData.setType("无实时数据");
            }
            dataList.add(AlarmData);
        }
        jsonMap.put("dataList", dataList);
        HtmlUtil.writerJson(response, jsonMap);
    }

    @RequestMapping("/getScomDetailListExportWord")
    public void getScomDetailListExportWord(
            HttpServletResponse response) throws Exception {
        List<ScomYxEntity> data = deviceHealthStateService.getScomDetail();
//		List<ScomAlarmEntity> Alarm = deviceHealthStateService.getScomAlarm();
        List<ScomYxEntity> dataList = new ArrayList<ScomYxEntity>();
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        Date dt = new Date();
        Long time = dt.getTime();
        for (int ii = 0; ii < data.size(); ii++) {
            ScomYxEntity AlarmData = data.get(ii);
            if (null != AlarmData.getSampleTime()) {
                String dateString = AlarmData.getSampleTime();
                dateString = dateString.split(" ")[0];
                Long time2 = 0L;
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = sdf.parse(dateString);
                    time2 = date.getTime() + 86400000;
                } catch (ParseException e) {
                    System.out.println(e.getMessage());
                }
                if (time.compareTo(time2) > 0) {
                    AlarmData.setType("超时");
                } else {
                    AlarmData.setType("正常");
                }
            } else {
                AlarmData.setType("无实时数据");
            }
            dataList.add(AlarmData);
        }
        jsonMap.put("dataList", dataList);
        HtmlUtil.writerJson(response, jsonMap);
    }

    @RequestMapping("/getSpdmDetailListExportWord")
    public void getSpdmDetailListExportWord(HistoryPage page,
                                            HttpServletResponse response, HttpServletRequest request) throws Exception {
        List<SpdmYxEntity> data = deviceHealthStateService.getSpdmDetail();
//		List<SpdmAlarmEntity> Alarm = deviceHealthStateService.getSpdmAlarm();
        List<SpdmYxEntity> dataList = new ArrayList<SpdmYxEntity>();
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        Date dt = new Date();
        Long time = dt.getTime();
        for (int ii = 0; ii < data.size(); ii++) {
            SpdmYxEntity AlarmData = data.get(ii);
            if (null != AlarmData.getSampleTime()) {
                String dateString = AlarmData.getSampleTime();
                dateString = dateString.split(" ")[0];
                Long time2 = 0L;
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = sdf.parse(dateString);
                    time2 = date.getTime() + 86400000;
                } catch (ParseException e) {
                    System.out.println(e.getMessage());
                }
                if (time.compareTo(time2) > 0) {
                    AlarmData.setType("超时");
                } else {
                    AlarmData.setType("正常");
                }
            } else {
                AlarmData.setType("无实时数据");
            }
            dataList.add(AlarmData);
        }
        jsonMap.put("dataList", dataList);
        HtmlUtil.writerJson(response, jsonMap);
    }

    @RequestMapping("/getSf6DetailList")
    public void getSf6DetailList(boolean _off, String _time, HistoryPage page,
                                 HttpServletResponse response, HttpServletRequest request) throws Exception {
        List<Sf6YxEntity> dataList = new ArrayList<Sf6YxEntity>();
        if (_off == false) {
            List<Sf6YxEntity> date = deviceHealthStateService.getSf6DetailDate();
            List<Sf6YxEntity> data = deviceHealthStateService.getSf6Detail();
//			List<Sf6AlarmEntity> Alarm = deviceHealthStateService.getSf6Alarm();
            for (int ii = 0; ii < data.size(); ii++) {
                dataList.add(date.get(ii));
                dataList.add(data.get(ii));
            }
        } else {
            List<Sf6YxEntity> date = deviceHealthStateService.getSf6DetailDateByDate(_time);
            List<Sf6YxEntity> data = deviceHealthStateService.getSf6DetailByDate(_time);
//    		List<Sf6AlarmEntity> Alarm = deviceHealthStateService.getSf6Alarm();
            for (int ii = 0; ii < data.size(); ii++) {
                dataList.add(date.get(ii));
                dataList.add(data.get(ii));
            }
        }
        HtmlUtil.writerJson(response, dataList);
    }

    @RequestMapping("/getSmoamDetailList")
    public void getSmoamDetailList(boolean _off, String _time, HistoryPage page,
                                   HttpServletResponse response, HttpServletRequest request) throws Exception {
        List<SmoamYxEntity> dataList = new ArrayList<SmoamYxEntity>();
        if (_off == false) {
            List<SmoamYxEntity> date = deviceHealthStateService.getSmoamDetailDate();
            List<SmoamYxEntity> data = deviceHealthStateService.getSmoamDetail();
//			List<SmoamAlarmEntity> Alarm = deviceHealthStateService.getSmoamAlarm();
            for (int ii = 0; ii < data.size(); ii++) {
                dataList.add(date.get(ii));
                dataList.add(data.get(ii));
            }
        } else {
            List<SmoamYxEntity> date = deviceHealthStateService.getSmoamDetailDateByDate(_time);
            List<SmoamYxEntity> data = deviceHealthStateService.getSmoamDetailByDate(_time);
//			List<SmoamAlarmEntity> Alarm = deviceHealthStateService.getSmoamAlarm();
            for (int ii = 0; ii < data.size(); ii++) {
                dataList.add(date.get(ii));
                dataList.add(data.get(ii));
            }
        }
        HtmlUtil.writerJson(response, dataList);
    }

    @RequestMapping("/getScomDetailList")
    public void getScomDetailList(boolean _off, String _time, HistoryPage page,
                                  HttpServletResponse response, HttpServletRequest request) throws Exception {
        List<ScomYxEntity> dataList = new ArrayList<ScomYxEntity>();
        if (_off == false) {
            List<ScomYxEntity> date = deviceHealthStateService.getScomDetailDate();
            List<ScomYxEntity> data = deviceHealthStateService.getScomDetail();
//			List<ScomAlarmEntity> Alarm = deviceHealthStateService.getScomAlarm();

            for (int ii = 0; ii < data.size(); ii++) {
                dataList.add(date.get(ii));
                dataList.add(data.get(ii));
            }
        } else {
            List<ScomYxEntity> date = deviceHealthStateService.getScomDetailDateByDate(_time);
            List<ScomYxEntity> data = deviceHealthStateService.getScomDetailByDate(_time);
//    		List<ScomAlarmEntity> Alarm = deviceHealthStateService.getScomAlarm();
            for (int ii = 0; ii < data.size(); ii++) {
                dataList.add(date.get(ii));
                dataList.add(data.get(ii));
            }
        }
        HtmlUtil.writerJson(response, dataList);
    }

    @RequestMapping("/getSpdmDetailList")
    public void getSpdmDetailList(boolean _off, String _time,
                                  HttpServletResponse response) throws Exception {
        List<SpdmYxEntity> dataList = new ArrayList<SpdmYxEntity>();
        if (_off == false) {
            List<SpdmYxEntity> date = deviceHealthStateService.getSpdmDetailDate();
            List<SpdmYxEntity> data = deviceHealthStateService.getSpdmDetail();
//			List<SpdmAlarmEntity> Alarm = deviceHealthStateService.getSpdmAlarm();

            for (int ii = 0; ii < data.size(); ii++) {
                dataList.add(date.get(ii));
                dataList.add(data.get(ii));
            }
        } else {
            List<SpdmYxEntity> date = deviceHealthStateService.getSpdmDetailDateByDate(_time);
            List<SpdmYxEntity> data = deviceHealthStateService.getSpdmDetailByDate(_time);
//			List<SpdmAlarmEntity> Alarm = deviceHealthStateService.getSpdmAlarm();

            for (int ii = 0; ii < data.size(); ii++) {
                dataList.add(date.get(ii));
                dataList.add(data.get(ii));
            }
        }
        HtmlUtil.writerJson(response, dataList);
    }

    //根据区域名称找到stom的数据列表
    @RequestMapping("/getStomDetailListBySpace")
    public void getStomDetailListBySpace(String space,
                                         HttpServletResponse response) throws Exception {
        space = new String(space.getBytes("iso8859-1"), "utf-8");
        List<StomYxEntity> date = deviceHealthStateService.getStomDetailDateBySpace(space);
        List<StomYxEntity> data = deviceHealthStateService.getStomDetailBySpace(space);
//		List<StomAlarmEntity> Alarm = deviceHealthStateService.getStomAlarm();
        List<StomYxEntity> dataList = new ArrayList<StomYxEntity>();
        for (int ii = 0; ii < data.size(); ii++) {
            dataList.add(date.get(ii));
            dataList.add(data.get(ii));
        }
        HtmlUtil.writerJson(response, dataList);
    }


    //根据区域名称查找sf6的数据信息
    @RequestMapping("/getSf6DetailListBySpace")
    public void getSf6DetailListBySpace(String space,
                                        HttpServletResponse response) throws Exception {
        space = new String(space.getBytes("iso8859-1"), "utf-8");
        List<Sf6YxEntity> date = deviceHealthStateService.getSf6DetailDateBySpace(space);
        List<Sf6YxEntity> data = deviceHealthStateService.getSf6DetailBySpace(space);
//		List<Sf6AlarmEntity> Alarm = deviceHealthStateService.getSf6Alarm();
        List<Sf6YxEntity> dataList = new ArrayList<Sf6YxEntity>();
        for (int ii = 0; ii < data.size(); ii++) {
            dataList.add(date.get(ii));
            dataList.add(data.get(ii));
        }
        HtmlUtil.writerJson(response, dataList);
    }


    @RequestMapping("/getSmoamDetailListBySpace")
    public void getSmoamDetailListBySpace(String space,
                                          HttpServletResponse response) throws Exception {
        space = new String(space.getBytes("iso8859-1"), "utf-8");
        List<SmoamYxEntity> date = deviceHealthStateService.getSmoamDetailDateBySpace(space);
        List<SmoamYxEntity> data = deviceHealthStateService.getSmoamDetailBySpace(space);
//		List<SmoamAlarmEntity> Alarm = deviceHealthStateService.getSmoamAlarm();
        List<SmoamYxEntity> dataList = new ArrayList<SmoamYxEntity>();
        for (int ii = 0; ii < data.size(); ii++) {
            dataList.add(date.get(ii));
            dataList.add(data.get(ii));
        }
        HtmlUtil.writerJson(response, dataList);
    }

    @RequestMapping("/getScomDetailListBySpace")
    public void getScomDetailListBySpace(String space,
                                         HttpServletResponse response) throws Exception {
        space = new String(space.getBytes("iso8859-1"), "utf-8");
        List<ScomYxEntity> date = deviceHealthStateService.getScomDetailDateBySpace(space);
        List<ScomYxEntity> data = deviceHealthStateService.getScomDetailBySpace(space);
//		List<ScomAlarmEntity> Alarm = deviceHealthStateService.getScomAlarm();
        List<ScomYxEntity> dataList = new ArrayList<ScomYxEntity>();
        for (int ii = 0; ii < data.size(); ii++) {
            dataList.add(date.get(ii));
            dataList.add(data.get(ii));
        }
        HtmlUtil.writerJson(response, dataList);
    }

    @RequestMapping("/getSpdmDetailListBySpace")
    public void getSpdmDetailListBySpace(String space,
                                         HttpServletResponse response) throws Exception {
        space = new String(space.getBytes("iso8859-1"), "utf-8");
        List<SpdmYxEntity> date = deviceHealthStateService.getSpdmDetailDateBySpace(space);
        List<SpdmYxEntity> data = deviceHealthStateService.getSpdmDetailBySpace(space);
//		List<SpdmAlarmEntity> Alarm = deviceHealthStateService.getSpdmAlarm();
        List<SpdmYxEntity> dataList = new ArrayList<SpdmYxEntity>();
        for (int ii = 0; ii < data.size(); ii++) {
            dataList.add(date.get(ii));
            dataList.add(data.get(ii));
        }
        HtmlUtil.writerJson(response, dataList);
    }

    /**
     * 获取油色谱历史数据
     *
     * @param url
     * @param classifyId
     * @return
     * @throws Exception
     */
    @RequestMapping("/getstomHistoryData")
    public void getstomHistoryData(HistoryPage page,
                                   HttpServletResponse response) throws Exception {
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
     * @throws Exception
     */
    //套管
    @RequestMapping("/getSbushHistoryData")
    public void getSbushHistoryData(HistoryPage page,
                                    HttpServletResponse response) throws Exception {
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
     * @throws Exception
     */
    @RequestMapping("/exportstomHistoryData")
    public void exportstomexportData(HistoryPage page,
                                     HttpServletResponse response) throws Exception {
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
     * @throws Exception
     */
    @RequestMapping("/getInfraredHistoryData")
    public void getInfraredHistoryData(HistoryPage page,
                                       HttpServletResponse response) throws Exception {
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
     * @throws Exception
     */
    @RequestMapping("/getSf6HistoryData")
    public void getSf6HistoryData(HistoryPage page,
                                  HttpServletResponse response) throws Exception {
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
     * @throws Exception
     */
    @RequestMapping("/exportSf6HistoryData")
    public void exportSf6HistoryData(HistoryPage page,
                                     HttpServletResponse response) throws Exception {
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
     * @throws Exception
     */
    @RequestMapping("/getSMOAMHistoryData")
    public void getSMOAMHistoryData(HistoryPage page,
                                    HttpServletResponse response) throws Exception {
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
     * @throws Exception
     */
    @RequestMapping("/exportSMOAMHistoryData")
    public void exportSMOAMHistoryData(HistoryPage page,
                                       HttpServletResponse response) throws Exception {
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
     * @throws Exception
     */
    @RequestMapping("/getSCOMHistoryData")
    public void getSCOMHistoryData(HistoryPage page,
                                   HttpServletResponse response) throws Exception {
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
     * @throws Exception
     */
    @RequestMapping("/exportSCOMHistoryData")
    public void exportSCOMHistoryData(HistoryPage page,
                                      HttpServletResponse response) throws Exception {
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
     * @throws Exception
     */
    @RequestMapping("/getSPDMHistoryData")
    public void getSconditionHistoryData(HistoryPage page,
                                         HttpServletResponse response) throws Exception {
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        List<Spdm_dataEntity> dataList = deviceHealthStateService.getSpdmHistoryData(page);
        jsonMap.put("total", page.getPager().getRowCount());
        jsonMap.put("rows", dataList);
        HtmlUtil.writerJson(response, jsonMap);

    }

    @RequestMapping("/exportSPDMHistoryData")
    public void exportSconditionHistoryData(HistoryPage page,
                                            HttpServletResponse response) throws Exception {
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
     * @throws Exception
     */
    @RequestMapping("/getDeviceByType")
    public void getDeviceByType(String DeviceType,
                                HttpServletResponse response) throws Exception {
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
     * @throws Exception
     */
    @RequestMapping("/getInfraredChart_Value")
    public void getInfraredChart_Value(
            HttpServletResponse response, HttpServletRequest request) throws Exception {
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
     * @throws Exception
     */
    @RequestMapping("/getSF6ChartValue")
    public void getSF6ChartValue(
            HttpServletResponse response, HttpServletRequest request) throws Exception {
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
     * @throws Exception
     */
    @RequestMapping("/getStomChartValue")
    public void getStomChartValue(
            HttpServletResponse response, HttpServletRequest request) throws Exception {
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
     * @throws Exception
     */
    @RequestMapping("/getSmoamChartValue")
    public void getSmoamChartValue(
            HttpServletResponse response, HttpServletRequest request) throws Exception {
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
     * @throws Exception
     */
    @RequestMapping("/getScomChartValue")
    public void getScomChartValue(
            HttpServletResponse response, HttpServletRequest request) throws Exception {
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
     * @throws Exception
     */
    @RequestMapping("/getSpdmChart_Value")
    public void getSpdmChart_history(
            HttpServletResponse response, HttpServletRequest request) throws Exception {
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
     * @throws Exception
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
     * @throws Exception
     */


    @RequestMapping("/getstomYXHistoryData")
    public void getstomYXHistoryData(HistoryPage page,
                                     HttpServletResponse response) throws Exception {
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        List<YXhistroyData> dataList = deviceHealthStateService.getstomYXHistoryData(page);
        jsonMap.put("total", page.getPager().getRowCount());
        jsonMap.put("rows", dataList);
        HtmlUtil.writerJson(response, jsonMap);
    }

    @RequestMapping("/getSf6YXHistoryData")
    public void getSf6YXHistoryData(HistoryPage page,
                                    HttpServletResponse response) throws Exception {
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        List<YXhistroyData> dataList = deviceHealthStateService.getSf6YXHistoryData(page);
        jsonMap.put("total", page.getPager().getRowCount());
        jsonMap.put("rows", dataList);
        HtmlUtil.writerJson(response, jsonMap);
    }

    @RequestMapping("/getSmoamYXHistory")
    public void getSmoamYXHistory(HistoryPage page,
                                  HttpServletResponse response) throws Exception {
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        List<YXhistroyData> dataList = deviceHealthStateService.getSmoamYXHistoryData(page);
        jsonMap.put("total", page.getPager().getRowCount());
        jsonMap.put("rows", dataList);
        HtmlUtil.writerJson(response, jsonMap);
    }

    @RequestMapping("/getScomYXHistoryData")
    public void getScomYXHistoryData(HistoryPage page,
                                     HttpServletResponse response) throws Exception {
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        List<YXhistroyData> dataList = deviceHealthStateService.getScomYXHistoryData(page);
        jsonMap.put("total", page.getPager().getRowCount());
        jsonMap.put("rows", dataList);
        HtmlUtil.writerJson(response, jsonMap);
    }

    @RequestMapping("/getSpdmYXHistoryData")
    public void getSpdmYXHistoryData(HistoryPage page,
                                     HttpServletResponse response) throws Exception {
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
}

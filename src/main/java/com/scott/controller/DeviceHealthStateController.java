package com.scott.controller;

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


    // Servrice start
    @Autowired(required = false)
    // 自动注入，不需要生成set方法了，required=false表示没有实现类，也不会报错。
    private DeviceHealthStateService<DeviceHealthStateEntity> deviceHealthStateService;
    private TreeDeviceService<TreeDeviceEntity, DeviceStateEntity, Sf6_dataEntity, stom_dataEntity, SMOAM_dataEntity, SCOM_dataEntity, Spdm_dataEntity> treeDeviceService;


    /**
     * 跳转健康信息页
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("/list")
    public ModelAndView list() {
        Map<String, Object> context = getRootMap();
        return forword("scott/demo/deviceHealthState", context);
    }

    /**
     * 跳转设备页
     *
     * @return
     * @throws Exception
     */

    @RequestMapping("/DeviceDetail")
    public ModelAndView DeviceDetail(HttpServletRequest request) throws Exception {
        Map<String, Object> context = getRootMap();
        //获取传过来的条件值并存到session中
        String id = request.getParameter("DeviceID");
        HttpSession session = request.getSession();
        session.removeAttribute("DeviceID");
        session.setAttribute("DeviceID", id);
        String DeviceType = request.getParameter("DeviceType");
        session.removeAttribute("DeviceType");
        session.setAttribute("DeviceType", DeviceType);
        String name = request.getParameter("DeviceName");
        name = new String(name.getBytes("ISO-8859-1"), "UTF-8");
        name = name.replace("@", "#");
        int index = name.indexOf(",");
        if (index > 0) {
            name = name.substring(0, index);
        }
        name = name.replaceAll(" ", "");
        session.removeAttribute("DeviceName");
        session.setAttribute("DeviceName", name);
        //跳转到设备健康页面
        return forword("scott/demo/deviceDetailState", context);
    }

    /**
     * 跳转红外测温设备页
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("/SpaceDetail")
    public ModelAndView SpaceDetail(HttpServletRequest request) throws Exception {
//        request.setCharacterEncoding("utf-8");
//        response.setCharacterEncoding("utf-8");
        Map<String, Object> context = getRootMap();
        HttpSession session = request.getSession();
        String DeviceType = request.getParameter("DeviceType");
        session.removeAttribute("DeviceType");
        session.setAttribute("DeviceType", DeviceType);
        String name = request.getParameter("DeviceName");
        name = new String(name.getBytes("ISO-8859-1"), "UTF-8");
        name = name.replace("@", "#");
        int index = name.indexOf(",");
        if (index > 0) {
            name = name.substring(0, index);
        }
        name = name.replaceAll(" ", "");
        System.out.println(name);
        session.removeAttribute("DeviceName");
        session.setAttribute("DeviceName", name);
        return forword("scott/demo/spaceDetailState", context);
    }

    /**
     * 跳转二级设备页
     *
     * @return
     * @throws Exception
     */

    @RequestMapping("/EquipmentDetail")
    public ModelAndView EquipmentDetail(HttpServletRequest request) {
//        request.setCharacterEncoding("utf-8");
//        response.setCharacterEncoding("utf-8");
        Map<String, Object> context = getRootMap();
        String id = request.getParameter("DeviceID");
        HttpSession session = request.getSession();
        session.removeAttribute("DeviceID");
        session.setAttribute("DeviceID", id);
        String DeviceType = request.getParameter("DeviceType");
        session.removeAttribute("DeviceType");
        session.setAttribute("DeviceType", DeviceType);
        String name = request.getParameter("DeviceName");
        name = name.replace("@", "#");
        int index = name.indexOf(",");
        if (index > 0) {
            name = name.substring(0, index);
        }
        //name=new String(name.getBytes("ISO-8859-1"),"UTF-8");
        name = name.replaceAll(" ", "");
        session.removeAttribute("DeviceName");
        session.setAttribute("DeviceName", name);
        return forword("scott/demo/EquipmentDetailState", context);
    }


    @RequestMapping("/getDetailListDevice")
    public void getDetailListDevice(String Type,
                                    HttpServletResponse response) {
        List<DeviceEntity> date = deviceHealthStateService.getDetailListDevice(Type);
        HtmlUtil.writerJson(response, date);
    }

//    //二级设备页面
//    @RequestMapping("/getEquipmentDetailList")
//    public void getEquipmentDetailList(String equipmentId,
//                                       HttpServletResponse response) throws Exception {
//        List<Object> dataList = new ArrayList<Object>();
//        List<DeviceEntity> equipment = deviceHealthStateService.getEquipment(equipmentId);
//        for (int n = 0; n < equipment.size(); n++) {
//            String type = equipment.get(n).getDeviceType();
//            if ("1".equals(type)) {
//                List<StomYxEntity> data = deviceHealthStateService.getStomDetail(equipmentId);
//                for (int ii = 0; ii < data.size(); ii++) {
//                    dataList.add(data.get(ii));
//                }
//            } else if ("2".equals(type)) {
////		    	List<Sf6YxEntity> date = deviceHealthStateService.getSf6DetailDate();
//                List<Sf6YxEntity> data = deviceHealthStateService.getSf6Detail(equipmentId);
//                for (int ii = 0; ii < data.size(); ii++) {
////					dataList.add(date.get(ii));
//                    dataList.add(data.get(ii));
//                }
//            } else if ("3".equals(type)) {
////		    	List<SmoamYxEntity> date = deviceHealthStateService.getSmoamDetailDate();
//                List<SmoamYxEntity> data = deviceHealthStateService.getSmoamDetail(equipmentId);
//                for (int ii = 0; ii < data.size(); ii++) {
////					dataList.add(date.get(ii));
//                    dataList.add(data.get(ii));
//                }
//            } else if ("4".equals(type)) {
////		    	List<ScomYxEntity> date = deviceHealthStateService.getScomDetailDate();
//                List<ScomYxEntity> data = deviceHealthStateService.getScomDetail(equipmentId);
//                for (int ii = 0; ii < data.size(); ii++) {
////					dataList.add(date.get(ii));
//                    dataList.add(data.get(ii));
//                }
//            } else if ("8".equals(type)) {
////		    	List<WeatherEntity> date = deviceHealthStateService.getWeatherDetailDate();
//                List<WeatherEntity> data = deviceHealthStateService.getWeatherDetail(equipmentId);
//                for (int ii = 0; ii < data.size(); ii++) {
////					dataList.add(date.get(ii));
//                    dataList.add(data.get(ii));
//                }
//            } else if ("19".equals(type)) {
////		    	List<SpdmYxEntity> date = deviceHealthStateService.getSpdmDetailDate();
//                List<SpdmYxEntity> data = deviceHealthStateService.getSpdmDetail(equipmentId);
//                for (int ii = 0; ii < data.size(); ii++) {
////					dataList.add(date.get(ii));
//                    dataList.add(data.get(ii));
//                }
//            }
//        }
//
//        HtmlUtil.writerJson(response, dataList);
//    }

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

//    @RequestMapping("/getWeatherDetailList")
//    public void getWeatherDetailList(boolean _off, String _time,
//                                     HttpServletResponse response) throws Exception {
//        List<WeatherEntity> dataList = new ArrayList<WeatherEntity>();
//        if (_off == false) {
//            List<WeatherEntity> date = deviceHealthStateService.getWeatherDetailDate();
//            List<WeatherEntity> data = deviceHealthStateService.getWeatherDetail();
////			List<WeatherEntity> Alarm = new ArrayList<WeatherEntity>();
//
//            for (int ii = 0; ii < data.size(); ii++) {
//                dataList.add(date.get(ii));
//                dataList.add(data.get(ii));
//            }
//        } else {
//            List<WeatherEntity> date = deviceHealthStateService.getWeatherDetailDateByDate(_time);
//            List<WeatherEntity> data = deviceHealthStateService.getWeatherDetailByDate(_time);
////    		List<WeatherEntity> Alarm = new ArrayList<WeatherEntity>();
//            for (int ii = 0; ii < data.size(); ii++) {
//                dataList.add(date.get(ii));
//                dataList.add(data.get(ii));
//            }
//        }
//        HtmlUtil.writerJson(response, dataList);
//    }

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

//    @RequestMapping("/getWeatherDetailListBySpace")
//    public void getWeatherDetailListBySpace(String space,
//                                            HttpServletResponse response) throws Exception {
//        space = new String(space.getBytes("iso8859-1"), "utf-8");
//        List<WeatherEntity> date = deviceHealthStateService.getWeatherDetailDateBySpace(space);
//        List<WeatherEntity> data = deviceHealthStateService.getWeatherDetailBySpace(space);
//        List<WeatherEntity> Alarm = new ArrayList<WeatherEntity>();
//        List<WeatherEntity> dataList = new ArrayList<WeatherEntity>();
//        for (int ii = 0; ii < data.size(); ii++) {
//            dataList.add(date.get(ii));
//            dataList.add(data.get(ii));
//        }
//        HtmlUtil.writerJson(response, dataList);
//    }

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
     * 获取YX历史数据的Refname
     * @param url
     * @param classifyId
     * @return
     * @throws Exception
     */
    /*@RequestMapping("/getYXHistoryRefname")
    public void getYXHistoryRefname(HistoryPage page,
            HttpServletResponse response,HttpServletRequest request) throws Exception {
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        //获取ln和ld
        request.setCharacterEncoding("utf-8");
        String DeviceType = request.getParameter("ln_inst_name");
        String UTF8_DeviceType=new String(DeviceType.getBytes("ISO-8859-1"),"UTF-8");
        page.setLn_inst_name(UTF8_DeviceType);
        String id = request.getParameter("id");
        String str="";
        str = deviceHealthStateService.getyxlnldByid(id);
        if(str==null){
            page.setLd_inst_name(UTF8_DeviceType.split("-")[1]);
            page.setLn_inst_name(UTF8_DeviceType.split("-")[0]);
        }else{
            page.setLd_inst_name(str.split("-")[0]);
            page.setLn_inst_name(str.split("-")[1]);
        }
        List<DataEntity> dataList =deviceHealthStateService.getYXHistoryRefname(page);
        jsonMap.put("total", page.getPager().getRowCount());
        jsonMap.put("rows", dataList);
        HtmlUtil.writerJson(response, jsonMap);
    }*/

//    /**
//     * 通过当前选择的Refname获取YX历史数据
//     *
//     * @param url
//     * @param classifyId
//     * @return
//     * @throws Exception
//     */
//    @RequestMapping("/getYXHistoryDataByRefname") //注解为getYXHistoryDataByRefname.do的AJAX请求
//    public void getYXHistoryDataByRefname(HistoryPage page, //HistoryPage实体类里必须包含有请求发送来的data用于接收
//                                          HttpServletResponse response, HttpServletRequest request) throws Exception {
//        Map<String, Object> jsonMap = new HashMap<String, Object>();
//        //获取ln和ld
//        request.setCharacterEncoding("utf-8");
//        String DeviceType = request.getParameter("ln_inst_name");
//        DeviceType = DeviceType.replace("@", "#");
//        int index = DeviceType.indexOf(",");
//        if (index > 0) {
//            DeviceType = DeviceType.substring(0, index);
//        }
//        String UTF8_DeviceType = new String(DeviceType.getBytes("ISO-8859-1"), "UTF-8");
//        String Devicerefname = request.getParameter("refname");
//        String UTF8_Devicerefname;
//        if (Devicerefname != null) {
//            UTF8_Devicerefname = new String(Devicerefname.getBytes("ISO-8859-1"), "UTF-8");
//            if (UTF8_Devicerefname.indexOf(",") >= 0) {
//                UTF8_Devicerefname = UTF8_Devicerefname.replaceAll(",", "#");
//            }
//        } else {
//            UTF8_Devicerefname = UTF8_DeviceType;
//        }
//        page.setRefname(UTF8_Devicerefname);
////        page.setRefname("");
//        String id = request.getParameter("id");
//        if (id.indexOf("A") > -1) {
//            id = id.substring(2);
//        }
//
//        List<DataEntity> dataList = deviceHealthStateService.getYXHistoryByrefname(page);//引用Service中的方法
//        jsonMap.put("total", page.getPager().getRowCount());
//        jsonMap.put("rows", dataList);
//        HtmlUtil.writerJson(response, jsonMap);
//    }

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

//    /**
//     * 获取气象历史数据
//     *
//     * @param url
//     * @param classifyId
//     * @return
//     * @throws Exception
//     */
//    @RequestMapping("/getWeatherHistoryData")
//    public void getWeatherHistoryData(HistoryPage page,
//                                      HttpServletResponse response) throws Exception {
//        Map<String, Object> jsonMap = new HashMap<String, Object>();
//        List<WeatherEntity> dataList = deviceHealthStateService.getWeatherHistory(page);
//        jsonMap.put("total", page.getPager().getRowCount());
//        jsonMap.put("rows", dataList);
//        HtmlUtil.writerJson(response, jsonMap);
//    }

//    /**
//     * 导出气象历史数据
//     *
//     * @param url
//     * @param classifyId
//     * @return
//     * @throws Exception
//     */
//    @RequestMapping("/exportWeatherHistoryData")
//    public void exportWeatherHistoryData(HistoryPage page,
//                                         HttpServletResponse response) throws Exception {
//        Map<String, Object> jsonMap = new HashMap<String, Object>();
//        List<WeatherEntity> dataList = deviceHealthStateService.getWeatherHistory(page);
//        jsonMap.put("total", page.getPager().getRowCount());
//        jsonMap.put(" dataList", dataList);
//        HtmlUtil.writerJson(response, jsonMap);
//    }

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

//    @RequestMapping("/exportYXHistory")
//    public void exportYXHistory(HistoryPage page,
//                                HttpServletResponse response, HttpServletRequest request) throws Exception {
//        Map<String, Object> jsonMap = new HashMap<String, Object>();
//        request.setCharacterEncoding("utf-8");
//        String DeviceType = request.getParameter("ln_inst_name");
//        DeviceType = DeviceType.replace("@", "#");
//        int index = DeviceType.indexOf(",");
//        if (index > 0) {
//            DeviceType = DeviceType.substring(0, index);
//        }
//        String UTF8_DeviceType = new String(DeviceType.getBytes("ISO-8859-1"), "UTF-8");
//        String Devicerefname = request.getParameter("refname");
//        String UTF8_Devicerefname;
//        if (Devicerefname != null) {
//            UTF8_Devicerefname = new String(Devicerefname.getBytes("ISO-8859-1"), "UTF-8");
//            if (UTF8_Devicerefname.indexOf(",") >= 0) {
//                UTF8_Devicerefname = UTF8_Devicerefname.replaceAll(",", "#");
//            }
//        } else {
//            UTF8_Devicerefname = UTF8_DeviceType;
//        }
//        page.setRefname(UTF8_Devicerefname);
//        String id = request.getParameter("id");
//        if (id.indexOf("A") > -1) {
//            id = id.substring(2);
//        }
//        List<DataEntity> dataList = deviceHealthStateService.exportYXHistory(page);
//        jsonMap.put("dataList", dataList);
//        HtmlUtil.writerJson(response, jsonMap);
//    }

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

//    /**
//     * 获取气象图表中需要的数据
//     *
//     * @param url
//     * @param classifyId
//     * @return
//     * @throws Exception
//     */
//    @RequestMapping("/getWeatherChart_Value")
//    public void getWeatherChart_history(
//            HttpServletResponse response, HttpServletRequest request) throws Exception {
//        Map<String, Object> param = new HashMap<String, Object>();
//        Map<String, List<WeatherEntity>> maps = new HashMap<String, List<WeatherEntity>>();
//        String startTime = request.getParameter("_startTime");
//        String endTime = request.getParameter("_endTime");
//        if (startTime == null || startTime == "") {
//            startTime = getStatetime();
//        }
//        param.put("startTime", startTime);
//        param.put("endTime", endTime);
//        String id = request.getParameter("DeviceID");
//        String[] ids = id.split(",");
//        for (int nn = 1; nn < ids.length; nn++) {
//            id = ids[nn].split("_")[1];
//            param.put("id", id);
//            List<WeatherEntity> dataList = null;
//            dataList = deviceHealthStateService.getWeatherChart_history(param);
//            if (dataList.size() > 0) {
//                maps.put("dataList" + nn, dataList);
//            }
//        }
//        HtmlUtil.writerJson(response, maps);
//    }

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

    //获取遥信数据
    @RequestMapping("/getStomYx")
    public void getStomYx(String DeviceID, HttpServletResponse response)
            throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();
        List<StomYxEntity> EntityList;
        List<StomYxEntity> DateList;

        param.put("DeviceID", DeviceID);
        EntityList = deviceHealthStateService.getStomYx(param);
        DateList = deviceHealthStateService.getStomYxDate(param);
        if (EntityList.size() == 0) {
            StomYxEntity StomYxEntity_null = new StomYxEntity(1);
            EntityList.add(StomYxEntity_null);
        }
        if (EntityList.get(0) == null) {
            EntityList.remove(0);
            StomYxEntity StomYxEntity_null = new StomYxEntity(1);
            EntityList.add(StomYxEntity_null);
        }
        if (DateList.size() == 0) {
            StomYxEntity StomYxEntity_null = new StomYxEntity(1);
            DateList.add(StomYxEntity_null);
        }
        if (DateList.get(0) == null) {
            DateList.remove(0);
            StomYxEntity StomYxEntity_null = new StomYxEntity(1);
            DateList.add(StomYxEntity_null);
        }
        DateList.add(EntityList.get(0));

        HtmlUtil.writerJson(response, DateList);
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

//    @RequestMapping("/getYCData")
//    public void getYCData(HistoryPage page,
//                          HttpServletResponse response, HttpServletRequest request) throws Exception {
//        Map<String, Object> jsonMap = new HashMap<String, Object>();
//        //获取ln和ld
//        request.setCharacterEncoding("utf-8");
//        page.setLd_inst_name("");
//        page.setLn_inst_name("");
////	      String str = deviceHealthStateService.getLnAndLd(data);
//        String id = request.getParameter("id");
//        String id_find;
//        if (id.indexOf("A") > -1) {
//            id = id.substring(2);
//            if (id.indexOf("y") > -1) {
//                id_find = id.substring(2);
//            } else {
//                id_find = id;
//            }
//        } else {
//            if (id.indexOf("y") > -1) {
//                id_find = id.substring(2);
//            } else {
//                id_find = id;
//            }
//        }
//
//        page.setId(id_find);
//        List<DataEntity> dataList = null;
//        if (id.indexOf("yc") > -1) {
//            dataList = deviceHealthStateService.getYCData_yc(page);
//        } else if (id.indexOf("yx") > -1) {
//            dataList = deviceHealthStateService.getYCData_yx(page);
//        } else {
//            //dataList =deviceHealthStateService.getYCData(page);
//        }
//        jsonMap.put("total", page.getPager().getRowCount());
//        jsonMap.put("rows", dataList);
//        HtmlUtil.writerJson(response, jsonMap);
//    }

    /**
     * 获取红外测温遥信数据
     *
     * @param DeviceID
     * @param response
     * @throws Exception
     */
    @RequestMapping("/getInfraredYx")
    public void getInfraredYx(String DeviceID, HttpServletResponse response)
            throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();
        List<Sf6YxEntity> EntityList;
        List<Sf6YxEntity> DateList;

        param.put("DeviceID", DeviceID);
        EntityList = deviceHealthStateService.getInfraredYx(param);
        DateList = deviceHealthStateService.getInfraredYxDate(param);
        if (EntityList.size() == 0) {
            Sf6YxEntity SpdmYxEntity_null = new Sf6YxEntity(1);
            EntityList.add(SpdmYxEntity_null);
        }
        if (EntityList.get(0) == null) {
            EntityList.remove(0);
            Sf6YxEntity SpdmYxEntity_null = new Sf6YxEntity(1);
            EntityList.add(SpdmYxEntity_null);
        }
        if (DateList.size() == 0) {
            Sf6YxEntity SpdmYxEntity_null = new Sf6YxEntity(1);
            DateList.add(SpdmYxEntity_null);
        }
        if (DateList.get(0) == null) {
            DateList.remove(0);
            Sf6YxEntity SpdmYxEntity_null = new Sf6YxEntity(1);
            DateList.add(SpdmYxEntity_null);
        }
        DateList.add(EntityList.get(0));
        HtmlUtil.writerJson(response, DateList);
    }

    @RequestMapping("/getSf6Yx")
    public void getSf6Yx(String DeviceID, HttpServletResponse response)
            throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();
        List<Sf6YxEntity> EntityList;
        List<Sf6YxEntity> DateList;

        param.put("DeviceID", DeviceID);
        EntityList = deviceHealthStateService.getSf6Yx(param);
        DateList = deviceHealthStateService.getSf6YxDate(param);
        if (EntityList.size() == 0) {
            Sf6YxEntity Sf6YxEntity_null = new Sf6YxEntity(1);
            EntityList.add(Sf6YxEntity_null);
        }
        if (EntityList.get(0) == null) {
            EntityList.remove(0);
            Sf6YxEntity Sf6YxEntity_null = new Sf6YxEntity(1);
            EntityList.add(Sf6YxEntity_null);
        }
        if (DateList.size() == 0) {
            Sf6YxEntity Sf6YxEntity_null = new Sf6YxEntity(1);
            DateList.add(Sf6YxEntity_null);
        }
        if (DateList.get(0) == null) {
            DateList.remove(0);
            Sf6YxEntity Sf6YxEntity_null = new Sf6YxEntity(1);
            DateList.add(Sf6YxEntity_null);
        }
        DateList.add(EntityList.get(0));

        HtmlUtil.writerJson(response, DateList);
    }

    @RequestMapping("/getSmoamYx")
    public void getSmoamYx(String DeviceID, HttpServletResponse response)
            throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();
        List<SmoamYxEntity> EntityList;
        List<SmoamYxEntity> DateList;

        param.put("DeviceID", DeviceID);
        EntityList = deviceHealthStateService.getSmoamYx(param);
        DateList = deviceHealthStateService.getSmoamYxDate(param);
        if (EntityList.size() == 0) {

            SmoamYxEntity SmoamYxEntity_null = new SmoamYxEntity(1);
            EntityList.add(SmoamYxEntity_null);
        }
        if (EntityList.get(0) == null) {
            EntityList.remove(0);
            SmoamYxEntity SmoamYxEntity_null = new SmoamYxEntity(1);
            EntityList.add(SmoamYxEntity_null);
        }
        if (DateList.size() == 0) {

            SmoamYxEntity SmoamYxEntity_null = new SmoamYxEntity(1);
            DateList.add(SmoamYxEntity_null);
        }
        if (DateList.get(0) == null) {
            DateList.remove(0);
            SmoamYxEntity SmoamYxEntity_null = new SmoamYxEntity(1);
            DateList.add(SmoamYxEntity_null);
        }
        DateList.add(EntityList.get(0));
        HtmlUtil.writerJson(response, DateList);
    }

    @RequestMapping("/getScomYx")
    public void getScomYx(String DeviceID, HttpServletResponse response)
            throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();
        List<ScomYxEntity> EntityList;
        List<ScomYxEntity> DateList;

        param.put("DeviceID", DeviceID);
        EntityList = deviceHealthStateService.getScomYx(param);
        DateList = deviceHealthStateService.getScomYxDate(param);
        if (EntityList.size() == 0) {
            ScomYxEntity ScomYxEntity_null = new ScomYxEntity(1);
            EntityList.add(ScomYxEntity_null);
        }
        if (EntityList.get(0) == null) {
            EntityList.remove(0);
            ScomYxEntity ScomYxEntity_null = new ScomYxEntity(1);
            EntityList.add(ScomYxEntity_null);
        }
        if (DateList.size() == 0) {
            ScomYxEntity ScomYxEntity_null = new ScomYxEntity(1);
            DateList.add(ScomYxEntity_null);
        }
        if (DateList.get(0) == null) {
            DateList.remove(0);
            ScomYxEntity ScomYxEntity_null = new ScomYxEntity(1);
            DateList.add(ScomYxEntity_null);
        }
        DateList.add(EntityList.get(0));
        HtmlUtil.writerJson(response, DateList);
    }

    @RequestMapping("/getSpdmYx")
    public void getSpdmYx(String DeviceID, HttpServletResponse response)
            throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();
        List<SpdmYxEntity> EntityList;
        List<SpdmYxEntity> DateList;

        param.put("DeviceID", DeviceID);
        EntityList = deviceHealthStateService.getSpdmYx(param);
        DateList = deviceHealthStateService.getSpdmYxDate(param);
        if (EntityList.size() == 0) {
            SpdmYxEntity SpdmYxEntity_null = new SpdmYxEntity(1);
            EntityList.add(SpdmYxEntity_null);
        }
        if (EntityList.get(0) == null) {
            EntityList.remove(0);
            SpdmYxEntity SpdmYxEntity_null = new SpdmYxEntity(1);
            EntityList.add(SpdmYxEntity_null);
        }
        if (DateList.size() == 0) {
            SpdmYxEntity SpdmYxEntity_null = new SpdmYxEntity(1);
            DateList.add(SpdmYxEntity_null);
        }
        if (DateList.get(0) == null) {
            DateList.remove(0);
            SpdmYxEntity SpdmYxEntity_null = new SpdmYxEntity(1);
            DateList.add(SpdmYxEntity_null);
        }
        DateList.add(EntityList.get(0));
        HtmlUtil.writerJson(response, DateList);
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

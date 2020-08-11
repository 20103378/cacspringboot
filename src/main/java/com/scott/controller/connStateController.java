package com.scott.controller;

import com.base.entity.BaseEntity;
import com.base.util.HtmlUtil;
import com.base.web.BaseAction;
import com.scott.entity.CacCagConnStateEntity;
import com.scott.entity.CacExeStateEntity;
import com.scott.entity.CacIedConnStateEntity;
import com.scott.entity.TreeConnEntity;
import com.scott.service.ConnStateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 系统状态--事件记录页
 */
@Controller
@RequestMapping("/connState")
public class connStateController extends BaseAction {


    @Autowired(required = false)
    // 自动注入，不需要生成set方法了，required=false表示没有实现类，也不会报错。
    private ConnStateService<BaseEntity> connStateService;

    /**
     * @param url
     * @param classifyId
     * @return
     * @throws Exception
     */
    @RequestMapping("/list")
    public ModelAndView list() {
        Map<String, Object> context = getRootMap();
        return forword("scott/demo/connState", context);
    }

    @RequestMapping("/getEmuListData")
    public void getEmuListData(HttpServletResponse response, HttpServletRequest request) throws Exception {
        List<TreeConnEntity> EntityList = connStateService.getEmuListData();
        TreeConnEntity scac = null;
        for (int n = 0; n < EntityList.size(); n++) {
            scac = EntityList.get(n);
            if (scac.getText().equals("SCAC-3000")) {
                EntityList.remove(n);
                EntityList.add(0, scac);
            }
        }
        if (EntityList != null && EntityList.size() > 0) {
            EntityList.get(0).setChecked(true);
        }
        HtmlUtil.writerJson(response, EntityList);
    }

    @RequestMapping("/getDataByID")
    public void getDataByID(HttpServletResponse response, HttpServletRequest request) throws Exception {
        String id = request.getParameter("id");
        List<TreeConnEntity> EntityList = connStateService.getDataByID(id);
        HtmlUtil.writerJson(response, EntityList);
    }

    @RequestMapping("/getHistoryData")
    public void getHistoryData(HttpServletResponse response, HttpServletRequest request) throws Exception {
        String id = request.getParameter("id");
        String time = "%" + request.getParameter("time") + "%";
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        jsonMap.put("id", id);
        jsonMap.put("time", time);
        List<TreeConnEntity> EntityList = connStateService.getHistoryData(jsonMap);
        HtmlUtil.writerJson(response, EntityList);
    }

    @RequestMapping("/get104connData")
    public void get104connData(HttpServletResponse response, HttpServletRequest request) throws Exception {
        String id = request.getParameter("id");
        List<TreeConnEntity> EntityList = connStateService.get104connData();
        HtmlUtil.writerJson(response, EntityList);
    }

    @RequestMapping("/get104connSelect")
    public void get104connSelect(HttpServletResponse response, HttpServletRequest request) throws Exception {
        String id = request.getParameter("id");
        List<TreeConnEntity> EntityList = connStateService.getAMCListData();
        HtmlUtil.writerJson(response, EntityList);
    }

    //得到程序运行状态
    @RequestMapping("/getProgramListData")
    public void getProgramListData(HttpServletResponse response, HttpServletRequest request) throws Exception {
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        List<CacExeStateEntity> dataList = connStateService.getProgramListData();
        jsonMap.put("total", dataList.size());
        jsonMap.put("rows", dataList);
        HtmlUtil.writerJson(response, jsonMap);
    }

    //cac与各装置通讯日志
    @RequestMapping("/getCACStateListData")
    public void getCACStateListData(HttpServletResponse response, HttpServletRequest request) throws Exception {
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        List<CacIedConnStateEntity> dataList = connStateService.getCACStateListData();
        jsonMap.put("total", dataList.size());
        jsonMap.put("rows", dataList);
        HtmlUtil.writerJson(response, jsonMap);
    }

    //cac与cag通讯日志
    @RequestMapping("/getCAGStateListData")
    public void getCAGStateListData(HttpServletResponse response, HttpServletRequest request) throws Exception {
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        List<CacCagConnStateEntity> dataList = connStateService.getCAGStateListData();
        jsonMap.put("total", dataList.size());
        jsonMap.put("rows", dataList);
        HtmlUtil.writerJson(response, jsonMap);
    }
}

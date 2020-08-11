package com.scott.controller;

import com.base.entity.BaseEntity;
import com.base.util.HtmlUtil;
import com.base.web.BaseAction;
import com.scott.entity.ProcstateEntity;
import com.scott.service.SystemStateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 系统状态-系统使用状态
 */
@Controller
@RequestMapping("/systemState")
public class SystemStateController extends BaseAction {


    @Autowired(required = false)
    // 自动注入，不需要生成set方法了，required=false表示没有实现类，也不会报错。
    private SystemStateService<BaseEntity> systemStateService;

    /**
     * 转换到系统使用状态页面
     * @return
     */
    @RequestMapping("/list")
    public ModelAndView list(){
        Map<String, Object> context = getRootMap();
        return forword("scott/demo/systemState", context);
    }

    @RequestMapping("/getData")
    public void getData(HttpServletResponse response) {
        List<ProcstateEntity> dataList = systemStateService.getData();
        List<ProcstateEntity> OsList = systemStateService.getOsData();
        List<ProcstateEntity> CpList = systemStateService.getCpData();
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        jsonMap.put("dataList", dataList);
        jsonMap.put("OsList", OsList);
        jsonMap.put("CpList", CpList);
        HtmlUtil.writerJson(response, jsonMap);
    }

}

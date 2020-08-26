package com.scott.controller;
import com.base.util.UrlUtil;
import com.base.util.edit.ICDUtils;
import com.base.web.BaseAction;
import com.scott.entity.OsicfgEntity;
import com.scott.entity.TreeDeviceEntity;
import com.scott.service.LEDConfigurationService;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * <b>LED接入页面控制类<br>
 */
@Controller
@RequestMapping("/LEDConfiguration")
public class LEDConfigurationController extends BaseAction {


    // Servrice start
    @Autowired(required = false)
    // 自动注入，不需要生成set方法了，required=false表示没有实现类，也不会报错。
    private LEDConfigurationService<TreeDeviceEntity> LEDService;

    @RequestMapping("/list")
    public String list(){
        return "scott/demo/LEDConfiguration";
    }

    /**
     * 1。ied接入配置查询
     * @return
     */
    @RequestMapping("/getOsicfgXml")
    @ResponseBody
    public List getOsicfgXml(){
        return LEDService.find_iec61850_ied_inst();
    }

    /**
     * 2。动态编辑icd文件
     * @param entity
     * @param response
     * @throws Exception
     */
    @RequestMapping("/commitled")
    @ResponseBody
    public String commitled(OsicfgEntity entity) throws Exception {
        ICDUtils.commitled(entity, LEDService);
        return "修改成功";
    }

    /**
     * ied接入配置，删除配置
     * @param arName
     * @return
     * @throws Exception
     */
    @RequestMapping("/del")
    @ResponseBody
    public List del(String arName) throws Exception {
        List<OsicfgEntity> osicfgList = new ArrayList<OsicfgEntity>();
        SAXBuilder bulider = new SAXBuilder();
        InputStream inSt = new FileInputStream(UrlUtil.getUrlUtil().getOsicfg() + "osicfg.xml");
        Document document = bulider.build(inSt);
        Element root = document.getRootElement();        //获取根节点对象
        List<Element> networkList = root.getChildren("NetworkAddressing");

        //开始删除xml
        for (Element el : networkList) {
            List<Element> remoteAddressList = el.getChildren("RemoteAddressList");
            for (Element el2 : remoteAddressList) {
                List<Element> remoteAddress = el2.getChildren("RemoteAddress");
                Element del_e = null;
                for (Element el3 : remoteAddress) {
                    Element AR_Name = el3.getChild("AR_Name");
                    if (AR_Name.getText().equals(arName)) {
                        del_e = el3;
                        break;
                    }
                }
                el2.removeContent(del_e);
            }
        }
        //开始同步删除数据库
        LEDService.del_iec61850_ied_inst(arName);
        //开始删除整个led文件夹
        XMLOutputter out = new XMLOutputter(Format.getPrettyFormat());
        out.output(document, new FileOutputStream(UrlUtil.getUrlUtil().getOsicfg() + "osicfg.xml"));
        return osicfgList;
    }

    /**
     * 测量量删除配置
     * @param entity
     * @return
     * @throws Exception
     */
    @RequestMapping("/delsql")
    @ResponseBody
    public List delsql(OsicfgEntity entity) throws Exception {
        List<OsicfgEntity> osicfgList = new ArrayList<OsicfgEntity>();
        Document document;
        String file_path = "";
        file_path = UrlUtil.getUrlUtil().getOsicfg();
        String xmlName = "osicfg.xml";
        SAXBuilder bulider = new SAXBuilder();
        InputStream inSt = new FileInputStream(file_path + xmlName);
        document = bulider.build(inSt);
        Element root = document.getRootElement();        //获取根节点对象
        List<Element> Networklist = root.getChildren("NetworkAddressing");
        //开始删除xml
        for (Element el : Networklist) {
            List<Element> RemoteAddressList = el.getChildren("RemoteAddressList");
            for (Element el2 : RemoteAddressList) {
                List<Element> RemoteAddress = el2.getChildren("RemoteAddress");
                Element del_e = null;
                for (Element el3 : RemoteAddress) {
                    Element AR_Name = el3.getChild("AR_Name");
                    if (AR_Name.getText().equals(entity.getArName())) {
                        del_e = el3;
                        break;
                    }
                }
                el2.removeContent(del_e);
            }
        }
        //开始同步删除数据库
        LEDService.del_iec61850_ied_inst(entity.getArName());
        LEDService.del_yc_inst(entity.getArName());
        LEDService.del_yx_inst(entity.getArName());
        LEDService.del_yk_inst(entity.getArName());
        //开始删除整个led文件夹
        XMLOutputter out = new XMLOutputter(Format.getPrettyFormat());
        out.output(document, new FileOutputStream(file_path + xmlName));
        return osicfgList;
    }

    public String getId(){
        int i_id;
        String id = LEDService.getId();
        if (id == null) {
            i_id = 1;
        } else {
            i_id = Integer.parseInt(id.substring(1)) + 1;
        }
        if (i_id < 10) {
            id = "I00" + i_id;
        } else if (i_id < 100) {
            id = "I0" + i_id;
        } else {
            id = "I" + i_id;
        }
        return id;
    }

}

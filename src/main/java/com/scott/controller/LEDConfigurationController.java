package com.scott.controller;

import com.base.util.HtmlUtil;
import com.base.util.UrlUtil;
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
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    public ModelAndView list() throws Exception {
        Map<String, Object> context = getRootMap();
        return forword("scott/demo/LEDConfiguration", context);
    }

    @RequestMapping("/getOsicfgXml")
    public void getOsicfgXml(HttpServletResponse response) throws Exception {
        List<OsicfgEntity> osicfgList = LEDService.findOsicfg();
//        Document document;
//        SAXBuilder bulider = new SAXBuilder();
//        InputStream inSt = new FileInputStream(UrlUtil.getUrlUtil().getOsicfg() + "osicfg.xml");
//        document = bulider.build(inSt);
//        Element root = document.getRootElement(); // 获取根节点对象
//        List<Element> Networklist = root.getChildren("NetworkAddressing");
//        for (Element el : Networklist) {
//            List<Element> RemoteAddressList = el.getChildren("RemoteAddressList");
//            for (Element el2 : RemoteAddressList) {
//                List<Element> RemoteAddress = el2.getChildren("RemoteAddress");
//                for (Element el3 : RemoteAddress) {
//                    OsicfgEntity temp = new OsicfgEntity();
//                    List<Element> AR_Name = el3.getChildren("AR_Name");
//                    temp.setAR_Name(AR_Name.get(0).getText());
//                    List<Element> NetAddr = el3.getChildren("NetAddr");
//                    temp.setNetAddr(NetAddr.get(0).getText());
//                    osicfgList.add(temp);
//                }
//            }
//        }
//		if(a==1){
//			for (int i = 0; i <= osicfgList.size() - 1; i++) {
//				OsicfgEntity OE = new OsicfgEntity();
//				OE.setAR_Name(osicfgList.get(i).getAR_Name());
//				OE.setNetAddr(osicfgList.get(i).getNetAddr());
//				OE.setAR_Name_old(osicfgList.get(i).getAR_Name());
//				String id = getId();
//				OE.setIedid(id);
//				LEDService.add_osicfg(OE);
//			}
//		}
        HtmlUtil.writerJson(response, osicfgList);
    }

    @RequestMapping("/commitled")
    public void commitled(OsicfgEntity entity, HttpServletResponse response) throws Exception {
        List<OsicfgEntity> osicfgList = new ArrayList<OsicfgEntity>();
        SAXBuilder bulider = new SAXBuilder();
        InputStream inSt = new FileInputStream(UrlUtil.getUrlUtil().getOsicfg() + "osicfg.xml");
        Document document = bulider.build(inSt);
        Element root = document.getRootElement();        //获取根节点对象
        List<Element> Networklist = root.getChildren("NetworkAddressing");
        int flag = LEDService.findIfUsed(entity.getAR_Name_old());
        if (flag == 1) {
            //开始修改xml
            for (Element el : Networklist) {
                List<Element> RemoteAddressList = el.getChildren("RemoteAddressList");
                for (Element el2 : RemoteAddressList) {
                    List<Element> RemoteAddress = el2.getChildren("RemoteAddress");
                    for (Element el3 : RemoteAddress) {
                        Element AR_Name = el3.getChild("AR_Name");
                        Element NetAddr = el3.getChild("NetAddr");
                        if (AR_Name.getText().equals(entity.getAR_Name_old())) {
                            AR_Name.setText(entity.getAR_Name());
                            NetAddr.setText(entity.getNetAddr());
                        }
                    }
                }
            }
            //开始更新数据库
            LEDService.update_osicfg(entity);
        }
        if (flag == 0) {
            Element new_el = new Element("RemoteAddress");
            Element new_AR_Name = new Element("AR_Name");
            new_AR_Name.setText(entity.getAR_Name());
            Element new_AP_Title = new Element("AP_Title");
            new_AP_Title.setText("1 3 9999 23");
            Element new_AE_Qualifier = new Element("AE_Qualifier");
            new_AE_Qualifier.setText("23");
            Element new_Psel = new Element("Psel");
            new_Psel.setText("00 00 00 01");
            Element new_Ssel = new Element("Ssel");
            new_Ssel.setText("00 01");
            Element new_Tsel = new Element("Tsel");
            new_Tsel.setText("00 01");
            Element new_NetAddr = new Element("NetAddr");
            new_NetAddr.setText(entity.getNetAddr());
            new_NetAddr.setAttribute("Type", "IPADDR");
            new_el.addContent(new_AR_Name);
            new_el.addContent(new_AP_Title);
            new_el.addContent(new_AE_Qualifier);
            new_el.addContent(new_Psel);
            new_el.addContent(new_Ssel);
            new_el.addContent(new_Tsel);
            new_el.addContent(new_NetAddr);
            //开始添加xml
            for (Element el : Networklist) {
                List<Element> RemoteAddressList = el.getChildren("RemoteAddressList");
                RemoteAddressList.get(0).addContent(new_el);
            }
            //开始更新数据库
            //获取加入序号
            String id = getId();
            entity.setIedid(id);
            LEDService.add_osicfg(entity);
        }
        XMLOutputter out = new XMLOutputter(Format.getPrettyFormat());
        out.output(document, new FileOutputStream(UrlUtil.getUrlUtil().getOsicfg() + "osicfg.xml"));
        HtmlUtil.writerJson(response, osicfgList);
    }

    @RequestMapping("/del")
    public void del(OsicfgEntity entity,
                    HttpServletResponse response, HttpServletRequest request) throws Exception {
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
                    if (AR_Name.getText().equals(entity.getAR_Name())) {
                        del_e = el3;
                        break;
                    }
                    ;
                }
                el2.removeContent(del_e);
            }
        }
        //开始同步删除数据库
        LEDService.del_osicfg(entity);
        //开始删除整个led文件夹
        XMLOutputter out = new XMLOutputter(Format.getPrettyFormat());
        out.output(document, new FileOutputStream(file_path + xmlName));
        HtmlUtil.writerJson(response, osicfgList);
    }

    @RequestMapping("/delsql")
    public void delsql(OsicfgEntity entity, HttpServletResponse response, HttpServletRequest request) throws Exception {
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
                    if (AR_Name.getText().equals(entity.getAR_Name())) {
                        del_e = el3;
                        break;
                    }
                    ;
                }
                el2.removeContent(del_e);
            }
        }
        //开始同步删除数据库
        LEDService.del_osicfg(entity);
        LEDService.del_yc_inst(entity);
        LEDService.del_yx_inst(entity);
        LEDService.del_yk_inst(entity);
        //开始删除整个led文件夹
        XMLOutputter out = new XMLOutputter(Format.getPrettyFormat());
        out.output(document, new FileOutputStream(file_path + xmlName));
        HtmlUtil.writerJson(response, osicfgList);
    }

    public String getId() throws Exception {
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

    public void commitled(String dir_iedName, String dir_ip, LEDConfigurationService<TreeDeviceEntity> server) throws Exception {
        this.LEDService = server;
        OsicfgEntity entity = new OsicfgEntity();
        entity.setAR_Name(dir_iedName);
        entity.setAR_Name_old(dir_iedName);
        entity.setNetAddr(dir_ip);

        InputStream inSt = new FileInputStream(UrlUtil.getUrlUtil().getOsicfg() + "osicfg.xml");

        SAXBuilder bulider = new SAXBuilder();
        Document document = bulider.build(inSt);
        Element root = document.getRootElement();        //获取根节点对象
        List<Element> networklist = root.getChildren("NetworkAddressing");

        int flag = LEDService.findIfUsed(entity.getAR_Name_old());
        if (flag == 1) {
            //开始修改xml
            for (Element el : networklist) {
                List<Element> RemoteAddressList = el.getChildren("RemoteAddressList");
                for (Element el2 : RemoteAddressList) {
                    List<Element> RemoteAddress = el2.getChildren("RemoteAddress");
                    for (Element el3 : RemoteAddress) {
                        Element AR_Name = el3.getChild("AR_Name");
                        Element NetAddr = el3.getChild("NetAddr");
                        if (AR_Name.getText().equals(entity.getAR_Name_old())) {
                            AR_Name.setText(entity.getAR_Name());
                            NetAddr.setText(entity.getNetAddr());
                        }
                    }
                }
            }
            //开始更新数据库
            LEDService.update_osicfg(entity);
        }
        if (flag == 0) {
            Element new_el = new Element("RemoteAddress");
            Element new_AR_Name = new Element("AR_Name");
            new_AR_Name.setText(entity.getAR_Name());
            Element new_AP_Title = new Element("AP_Title");
            new_AP_Title.setText("1 3 9999 23");
            Element new_AE_Qualifier = new Element("AE_Qualifier");
            new_AE_Qualifier.setText("23");
            Element new_Psel = new Element("Psel");
            new_Psel.setText("00 00 00 01");
            Element new_Ssel = new Element("Ssel");
            new_Ssel.setText("00 01");
            Element new_Tsel = new Element("Tsel");
            new_Tsel.setText("00 01");
            Element new_NetAddr = new Element("NetAddr");
            new_NetAddr.setText(entity.getNetAddr());
            new_NetAddr.setAttribute("Type", "IPADDR");
            new_el.addContent(new_AR_Name);
            new_el.addContent(new_AP_Title);
            new_el.addContent(new_AE_Qualifier);
            new_el.addContent(new_Psel);
            new_el.addContent(new_Ssel);
            new_el.addContent(new_Tsel);
            new_el.addContent(new_NetAddr);
            //开始添加xml
            for (Element el : networklist) {
                List<Element> remoteAddressList = el.getChildren("RemoteAddressList");
                int count=0;
                for (Element el2 : remoteAddressList) {
                    List<Element> RemoteAddress = el2.getChildren("RemoteAddress");
                    for (Element el3 : RemoteAddress) {
                        Element AR_Name = el3.getChild("AR_Name");
                        Element NetAddr = el3.getChild("NetAddr");
                        if (AR_Name.getText().equals(entity.getAR_Name_old())) {
                            AR_Name.setText(entity.getAR_Name());
                            NetAddr.setText(entity.getNetAddr());
                            count++;
                        }
                    }
                }
                if(count==0){
                    remoteAddressList.get(0).addContent(new_el);
                }
            }
            //开始更新数据库
            //获取加入序号
            String id = getId();
            entity.setIedid(id);
            LEDService.add_osicfg(entity);
        }
        XMLOutputter out = new XMLOutputter(Format.getPrettyFormat());
        out.output(document, new FileOutputStream(UrlUtil.getUrlUtil().getOsicfg() + "osicfg.xml"));
    }
}

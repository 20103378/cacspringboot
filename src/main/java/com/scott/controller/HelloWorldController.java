//package com.scott.controller;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//import com.base.util.excel.RefnameCell;
//import com.base.util.excel.ExcelUtils;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.multipart.MultipartFile;
//
//import com.alibaba.fastjson.JSON;
//
//import javax.servlet.http.HttpServletResponse;
//
//@Controller
//@RequestMapping("/api/hello")
//public class HelloWorldController{
//    private static final Logger log = LogManager.getLogger(HelloWorldController.class);
//
//    @RequestMapping(value = "/exportExcel", method = RequestMethod.GET)
//    public void exportExcel(HttpServletResponse response)  throws IOException {
//        List<RefnameCell> resultList = new ArrayList<RefnameCell>();
//        RefnameCell refnameCell = new RefnameCell();
//        refnameCell.setCityCode("a1");
//        refnameCell.setMarkId("a4");
//        resultList.add(refnameCell);
//
//        refnameCell = new RefnameCell();
//        refnameCell.setCityCode("b1");
//        refnameCell.setMarkId("b4");
//        resultList.add(refnameCell);
//
//        long t1 = System.currentTimeMillis();
//        ExcelUtils.writeExcel(response, resultList, RefnameCell.class);
//        long t2 = System.currentTimeMillis();
//        System.out.println(String.format("write over! cost:%sms", (t2 - t1)));
//    }
//
//    @RequestMapping(value = "/readExcel", method = RequestMethod.POST)
//    public void readExcel(@RequestParam(value="uploadFile", required = false) MultipartFile file){
//        long t1 = System.currentTimeMillis();
//        List<RefnameCell> list = ExcelUtils.readExcel("", RefnameCell.class, file);
//        long t2 = System.currentTimeMillis();
//        System.out.println(String.format("read over! cost:%sms", (t2 - t1)));
//        list.forEach(
//                b -> System.out.println(JSON.toJSONString(b))
//        );
//    }
//}
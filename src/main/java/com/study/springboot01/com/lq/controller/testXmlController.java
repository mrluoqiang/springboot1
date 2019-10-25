package com.study.springboot01.com.lq.controller;

import com.study.springboot01.com.lq.util.Stringutil.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.io.InputStream;
import java.util.Iterator;

/**
 * About:
 * Other:
 * Created: qiang luo
 * Date: 2019-10-24 11:15
 * Editored:
 */
@Controller
@Slf4j
@RequestMapping(value = "xml")
public class testXmlController {

    @ResponseBody
    @RequestMapping(value = "testxml")
  public String solveXml(){
        //创建dom4j解析器
        SAXReader saxReader = new SAXReader();
        try {
            ClassPathResource classPathResource = new ClassPathResource("templates/test.xml");
            InputStream inputStream =classPathResource.getInputStream();
            Document document = saxReader.read(inputStream);
            Element rootElement = document.getRootElement();
            System.out.println("根节点名字："+rootElement.getName());
            ShowXML(rootElement);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
  }

    public void ShowXML(Element rootElement){
        Iterator iterator = rootElement.elementIterator();
        while (iterator.hasNext()){
            Element element = (Element) iterator.next();
            String s = element.getName();
            System.out.println("节点的名字:"+s);
            if(!element.getText().trim().isEmpty()){
                System.out.println("节点的值:"+element.getText());
            }
            Iterator iterator1 = element.elementIterator();
            if(iterator1.hasNext()){
                ShowXML(element);
            }
        }
    }
}

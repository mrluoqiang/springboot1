package com.study.springboot01.com.lq.controller;

import com.study.springboot01.com.lq.common.BaseResp;
import com.study.springboot01.com.lq.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.net.URL;

/**
 * About:
 * Other:
 * Created: qiang luo
 * Date: 2019-09-20 15:52
 * Editored:
 */
@Controller
@Slf4j
@RequestMapping(value = "resource")
public class ResourceController {
    @Value("${resourse.url}")
    private String  url;

    @RequestMapping(value = "download")
    @ResponseBody
    public BaseResp ResDownload(HttpServletRequest request, HttpServletResponse response){
        BaseResp baseResp=new BaseResp();
        BufferedInputStream inStream=null;
        ServletOutputStream outputStream=null;
        try {
            URL urlstr=new URL(url);
            String[] split = url.split("/");
            String name = split[split.length - 1];
             inStream = new BufferedInputStream(urlstr.openStream());
            outputStream=response.getOutputStream();
            response.setContentType("application/octet-stream");
            // 设置在下载框默认显示的文件名
            response.setHeader("Content-Disposition", "attachment;filename=" + name);
            int i;
            while ((i = inStream.read()) != -1) {
                outputStream.write(i);
            }
            outputStream.flush();
        }catch (Exception e){
            log.error("下载失败:"+e);
            baseResp.setCode(Constants.FAIL);
            baseResp.setMsg("下载失败");
            return baseResp;
        }finally {
            try {
                if(inStream!=null){
                    inStream.close();
                }
                if(outputStream!=null){
                    outputStream.close();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        baseResp.setCode(Constants.SUCCESS);
        baseResp.setMsg("下载成功");
        return baseResp;
    }
}

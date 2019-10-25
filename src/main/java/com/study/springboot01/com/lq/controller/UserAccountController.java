package com.study.springboot01.com.lq.controller;

import com.study.springboot01.com.lq.common.BaseResp;
import com.study.springboot01.com.lq.common.Constants;
import com.study.springboot01.com.lq.dao.UserRepository;
import com.study.springboot01.com.lq.pojo.UserAccount;
import com.study.springboot01.com.lq.service.UserInfoService;
import com.study.springboot01.com.lq.util.AliyunUtil.AliyunSMSUtil;
import com.study.springboot01.com.lq.util.RedisUtil.RedisUtil;
import com.study.springboot01.com.lq.util.Stringutil.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.MessageDigest;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * About:
 * Other:
 * Created: qiang luo
 * Date: 2019-06-17 14:17
 * Editored:
 */
@Slf4j
@RequestMapping("/user")
@Controller
public class UserAccountController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private AliyunSMSUtil aliyunSMSUtil;

    @RequestMapping("/findbyall")
    public String findByAll(Model mode){
        List<UserAccount> list = userRepository.findAll();
        mode.addAttribute("list",list);
        return "user";
    }
    @RequestMapping("/adduser")
    public String adduser(UserAccount userAccount){
        try {
            if(StringUtil.isNotEmpty(userAccount)){
                // 确定计算方法
                MessageDigest md5 = MessageDigest.getInstance("MD5");
                Base64.Encoder base64Encoder = Base64.getEncoder();
                // 加密字符串
                String md5password = base64Encoder.encodeToString(md5.digest(userAccount.getPassword().getBytes("utf-8")));
                userAccount.setMd5password(md5password);
            }
        }catch (Exception e){
            System.out.println("出错了！！");
        }
        userAccount.setState(1);
        UserAccount save = userRepository.save(userAccount);
        System.out.println(save);
        return "redirect:/user/findbyall";
    }
    @RequestMapping("/checkLogin")
    @ResponseBody
    public UserAccount checkLogin(UserAccount userAccount){
        UserAccount user=null;
        if(StringUtil.isNotEmpty(userAccount)){
            Object o = redisUtil.getByObject(userAccount.getUseraccount());
            if(!StringUtil.isNotEmpty(o)){
                 user = userRepository.findUserAccountByUseraccountAndPassword(userAccount.getUseraccount(),userAccount.getPassword());
                 if(user!=null){

                     redisUtil.setByexpire(userAccount.getUseraccount(),user,1L, TimeUnit.HOURS);
                 }else{
                     log.error("用户不存在");
                 }
            }else{
                user=(UserAccount)o;
            }
        }
        return user;
    }
    //发送短信,获取验证码
    @RequestMapping("/sendSMS")
    @ResponseBody
    public BaseResp sendSMS(String userAccount){
        BaseResp baseResp=new BaseResp();
        Map paramMap=new HashMap();
        paramMap.put("number", userAccount);
        paramMap.put("VerifyCode", aliyunSMSUtil.getSMSValidateCode());
        paramMap.put("msgType",Constants.BACKPWDTYPE);
        UserAccount user=null;
        try {
            if (StringUtil.isNotEmpty(userAccount)) {
                Object o = redisUtil.getByObject(userAccount);
                if (!StringUtil.isNotEmpty(o)) {
                    user = userRepository.findUserAccountByUseraccount(userAccount);
                    if (user != null) {
                        baseResp = userInfoService.sendSms(paramMap);
                        if (baseResp.getCode() == Constants.SUCCESS) {
                            //相关信息已在service限制
                            log.info("成功：修改密码模块获取短信验证码");
                            baseResp.setCode(Constants.SUCCESS);
                            baseResp.setMsg("获取验证码成功");
                            return baseResp;
                        }
                    } else {
                        baseResp.setCode(Constants.FAIL);
                        baseResp.setMsg("账号不存在");
                        log.info("账号不存在");
                        return baseResp;
                    }
                }else {
                    baseResp = userInfoService.sendSms(paramMap);
                    if (baseResp.getCode() == Constants.SUCCESS) {
                        //相关信息已在service限制
                        log.info("成功：修改密码模块获取短信验证码");
                        baseResp.setCode(Constants.SUCCESS);
                        baseResp.setMsg("获取验证码成功");
                        return baseResp;
                    }
                }
            }
        }catch (Exception e){
            log.error("获取验证码失败："+e);
        }
        baseResp.setCode(Constants.FAIL);
        baseResp.setMsg("获取验证码失败");
        return baseResp;
    }
}


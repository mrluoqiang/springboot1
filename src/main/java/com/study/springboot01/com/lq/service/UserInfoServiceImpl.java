package com.study.springboot01.com.lq.service;

import com.study.springboot01.com.lq.common.BaseResp;
import com.study.springboot01.com.lq.config.AliyunConfig;
import com.study.springboot01.com.lq.pojo.UserAccount;
import com.study.springboot01.com.lq.util.AliyunUtil.AliyunSMSUtil;
import com.study.springboot01.com.lq.util.Stringutil.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * About:
 * Other:
 * Created: qiang luo
 * Date: 2019-09-06 14:14
 * Editored:
 */
@Service
public class UserInfoServiceImpl implements UserInfoService {
    @Autowired
    private AliyunSMSUtil aliyunSMSUtil;
    @Override
    public BaseResp sendSms(Map paramMap)throws Exception{
        String number = StringUtil.getObjStr(paramMap.get("number"));
        String VerifyCode = StringUtil.getObjStr(paramMap.get("VerifyCode"));
        Integer msgType = Integer.parseInt(StringUtil.getObjStr(paramMap.get("msgType")));
        BaseResp baseResp =aliyunSMSUtil.sendBySmsService(number, VerifyCode, msgType);
        return baseResp ;
    }
}

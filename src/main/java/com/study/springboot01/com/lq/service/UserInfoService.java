package com.study.springboot01.com.lq.service;

import com.study.springboot01.com.lq.common.BaseResp;
import com.study.springboot01.com.lq.pojo.UserAccount;

import java.util.Map;

/**
 * About:
 * Other:
 * Created: qiang luo
 * Date: 2019-09-06 14:09
 * Editored:
 */
public interface UserInfoService{

    public BaseResp sendSms(Map map)throws Exception;
}

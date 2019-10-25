package com.study.springboot01.com.lq.config;

import lombok.Data;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * About:
 * Other:
 * Created: qiang luo
 * Date: 2019-09-06 11:13
 * Editored:
 */
@Configurable
@Getter
@Component
public class AliyunConfig {

    @Value("${accessId}")
    private String accessId;
    @Value("${accessKey}")
    private String accessKey;
    //短信发送模式：1-使用消息服务接口 2-使用短信服务接口
    @Value("${aliyunSendMode}")
    private Integer aliyunSendMode;
    //访问阿里云MNS消息服务的接入地址
    @Value("${accountEndpoint}")
    private String accountEndpoint;
    //主题名称
    @Value("${topicName}")
    private String topicName;
    //短信签名
    @Value("${sMSSignName}")
    private String sMSSignName;
    //短信模板--注册验证码模板
    @Value("${sMSTemplateCode}")
    private String sMSTemplateCode;
    //短信模板--修改密码验证码模板
    @Value("${sMSTemplateCodeResetpwd}")
    private String sMSTemplateCodeResetpwd;
    //短信模板中参数对应的值
    @Value("${sMSTemplateParamKey}")
    private String sMSTemplateParamKey;

}

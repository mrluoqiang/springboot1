package com.study.springboot01.com.lq.util.AliyunUtil;

import com.aliyun.mns.client.CloudAccount;
import com.aliyun.mns.client.CloudTopic;
import com.aliyun.mns.client.MNSClient;
import com.aliyun.mns.model.BatchSmsAttributes;
import com.aliyun.mns.model.MessageAttributes;
import com.aliyun.mns.model.RawTopicMessage;
import com.aliyun.mns.model.TopicMessage;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsResponse;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.study.springboot01.com.lq.common.BaseResp;
import com.study.springboot01.com.lq.common.Constants;
import com.study.springboot01.com.lq.config.AliyunConfig;
import lombok.extern.slf4j.Slf4j;
import org.patchca.service.ConfigurableCaptchaService;
import org.patchca.word.RandomWordFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * About:
 * Other:
 * Created: qiang luo
 * Date: 2019-09-06 10:08
 * Editored:
 */
@Slf4j
@Component
public class AliyunSMSUtil {
    private  final ConfigurableCaptchaService configurableCaptchaService = new ConfigurableCaptchaService();
    @Autowired
    private  AliyunConfig aliyunConfig;
    /**
     * 产品名称:云通信短信API产品,开发者无需替换
     */
    final String product = "Dysmsapi";
    /**
     * 产品域名,开发者无需替换
     */
     final String domain = "dysmsapi.aliyuncs.com";
    /**
     * 阿里云帐号
     */
    public CloudAccount account = null;
    /**
     * 阿里云消息服务客户端
     */
    public  MNSClient client = null;
    /**
     * 阿里云主题
     */
    public  CloudTopic topic = null;
    /**
     * 向服务发消息的结构
     */
    public  RawTopicMessage msg = null;
    /**
     * 消息属性
     */
    public  MessageAttributes messageAttributes = null;

    /*************************以下为通过阿里云消息服务接口发送短信****************************/
    // Step 1. 获取主题引用
    /**
     * 设置SMS消息体（必须）及生成SMS消息属性
     * @param phoneNum
     * @param validCode
     * @param msgType
     */
    public  void setSmsFormat(String phoneNum, String validCode, Integer msgType){
        account = new CloudAccount(aliyunConfig.getAccessId(), aliyunConfig.getAccessKey(), aliyunConfig.getAccountEndpoint());
        client = account.getMNSClient();
        // 填免费topic即可（如果要用收费topic请参考阿里云相关sdk示例文档），
        // 阿里云有免费提供的主题，如：sms.topic-cn-hangzhou即为杭州域的免费topic
        topic = client.getTopicRef(aliyunConfig.getTopicName());

        msg = new RawTopicMessage();
        // 注：目前暂时不支持消息内容为空，需要指定消息内容，不为空即可。
        // 现版本sdk中，MessageBody可以随便填，其实不生效，但是必须填，不填会出错，下一版本sdk我们会把这参数去掉。
        msg.setMessageBody("sms-message");

        messageAttributes = new MessageAttributes();

        BatchSmsAttributes batchSmsAttributes = new BatchSmsAttributes();

        // 1 设置发送短信的签名（SMSSignName）
        // 类似于企业简称（如：“白熊”，短信验证码下发时，阿里云会自动在短信前加上“【白熊】”）
        batchSmsAttributes.setFreeSignName(aliyunConfig.getSMSSignName());

        // 2 设置发送短信使用的模板（SMSTempateCode）
        // 阿里云平台通过审核的模板CODE，以SMS_ 开头的字符串
        if(msgType==Constants.BACKPWDTYPE){
            batchSmsAttributes.setTemplateCode(aliyunConfig.getSMSTemplateCodeResetpwd());
        }else{
            batchSmsAttributes.setTemplateCode(aliyunConfig.getSMSTemplateCode());
        }
        // 3 设置发送短信所使用的模板中参数对应的值（在短信模板中定义的，没有可以不用设置）
        // 由于短信验证码由ITAS后台生成，所以此变量（占位符）需要在申请短信模版的时候填写，待审核通过即可使用
        BatchSmsAttributes.SmsReceiverParams smsReceiverParams = new BatchSmsAttributes.SmsReceiverParams();
        smsReceiverParams.setParam(aliyunConfig.getSMSTemplateParamKey(), validCode);

        // 4 增加接收短信的号码
        batchSmsAttributes.addSmsReceiver(phoneNum, smsReceiverParams);
        messageAttributes.setBatchSmsAttributes(batchSmsAttributes);
    }

    /**
     * 发送消息
     * @param phoneNum
     * @param validCode
     * @param msgType
     * @return
     */
    public  BaseResp sendByMsgService(String phoneNum, String validCode, Integer msgType) {
        BaseResp baseResp = new BaseResp();
        // 设置短信格式和属性
        setSmsFormat(phoneNum, validCode, msgType);
        try {
            // 调用阿里云接口下发短信验证码
            TopicMessage ret = topic.publishMessage(msg, messageAttributes);
            System.out.println(ret);
            log.info("调用阿里云平台发送短信成功！");
            baseResp.setCode(Constants.SUCCESS);;
            baseResp.setMsg("调用阿里云平台发送短信成功");;
        } catch (Exception e) {
            e.printStackTrace();
            baseResp.setCode(Constants.FAIL);;
            log.error("调用阿里云平台发送短信失败！", e);
        }
        return baseResp;
    }

    /*************************以下为通过阿里云消息服务接口发送短信****************************/
    /**
     * 通过阿里云短信服务接口发送短信
     * @param phoneNum
     * @param validCode
     * @param msgType
     * @return
     * @throws ClientException
     * @throws InterruptedException
     */
    public BaseResp sendBySmsService(String phoneNum, String validCode, Integer msgType) throws Exception{
        BaseResp baseResp = new BaseResp();
        //发短信
        SendSmsResponse response = sendByAliyunSmsService(phoneNum, validCode, msgType);
        log.info("短信接口返回的数据----------------");
        log.info("Code=" + response.getCode());
        log.info("Message=" + response.getMessage());
        log.info("RequestId=" + response.getRequestId());
        log.info("BizId=" + response.getBizId());

        String as = "OK";
        if(response.getCode() != null && response.getCode().equals(as)) {
            QuerySendDetailsResponse querySendDetailsResponse = querySendDetails(response.getBizId(), phoneNum);
            log.info("短信明细查询接口返回数据----------------");
            log.info("Code=" + querySendDetailsResponse.getCode());
            log.info("Message=" + querySendDetailsResponse.getMessage());
            int i = 0;
            for(QuerySendDetailsResponse.SmsSendDetailDTO smsSendDetailDTO : querySendDetailsResponse.getSmsSendDetailDTOs())
            {
                log.info("SmsSendDetailDTO["+i+"]:");
                log.info("Content=" + smsSendDetailDTO.getContent());
                log.info("ErrCode=" + smsSendDetailDTO.getErrCode());
                log.info("OutId=" + smsSendDetailDTO.getOutId());
                log.info("PhoneNum=" + smsSendDetailDTO.getPhoneNum());
                log.info("ReceiveDate=" + smsSendDetailDTO.getReceiveDate());
                log.info("SendDate=" + smsSendDetailDTO.getSendDate());
                log.info("SendStatus=" + smsSendDetailDTO.getSendStatus());
                log.info("Template=" + smsSendDetailDTO.getTemplateCode());
            }
            log.info("TotalCount=" + querySendDetailsResponse.getTotalCount());
            log.info("RequestId=" + querySendDetailsResponse.getRequestId());

            log.info("调用阿里云平台发送短信成功！");
            baseResp.setCode(Constants.SUCCESS);
        } else {
            log.error("调用阿里云平台发送短信失败！");
            baseResp.setCode(Constants.FAIL);
            baseResp.setData(response.getMessage());
        }
        return baseResp;
    }

    public  SendSmsResponse sendByAliyunSmsService(String phoneNum, String validCode, Integer msgType) throws ClientException {
        //可自助调整超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");

        //初始化acsClient,暂不支持region化
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", aliyunConfig.getAccessId(), aliyunConfig.getAccessKey());
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        IAcsClient acsClient = new DefaultAcsClient(profile);

        //组装请求对象-具体描述见控制台-文档部分内容
        SendSmsRequest request = new SendSmsRequest();
        //必填:待发送手机号
        request.setPhoneNumbers(phoneNum);
        //必填:短信签名-可在短信控制台中找到
        request.setSignName(aliyunConfig.getSMSSignName());
        //必填:短信模板-可在短信控制台中找到
        // 阿里云平台通过审核的模板CODE，以SMS_ 开头的字符串
        if (Constants.NORMALTYPE==msgType) {
            request.setTemplateCode(aliyunConfig.getSMSTemplateCode());
        } else {
            request.setTemplateCode(aliyunConfig.getSMSTemplateCodeResetpwd());
        }
        //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
        request.setTemplateParam("{\"code\":" + validCode + "}");

        //hint 此处可能会抛出异常，注意catch
        SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);

        return sendSmsResponse;
    }

    public  QuerySendDetailsResponse querySendDetails(String bizId, String phoneNum) throws ClientException {
        //可自助调整超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");

        //初始化acsClient,暂不支持region化
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", aliyunConfig.getAccessId(), aliyunConfig.getAccessKey());
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        IAcsClient acsClient = new DefaultAcsClient(profile);

        //组装请求对象
        QuerySendDetailsRequest request = new QuerySendDetailsRequest();
        //必填-号码
        request.setPhoneNumber(phoneNum);
        //可选-流水号
        request.setBizId(bizId);
        //必填-发送日期 支持30天内记录查询，格式yyyyMMdd
        SimpleDateFormat ft = new SimpleDateFormat("yyyyMMdd");
        request.setSendDate(ft.format(new Date()));
        //必填-页大小
        request.setPageSize(10L);
        //必填-当前页码从1开始计数
        request.setCurrentPage(1L);

        //hint 此处可能会抛出异常，注意catch
        QuerySendDetailsResponse querySendDetailsResponse = acsClient.getAcsResponse(request);

        return querySendDetailsResponse;
    }

    /*************************结束********************************************/

    /**
     * 生成短信验证码
     * @return
     */
    public  String getSMSValidateCode() {
        RandomWordFactory rwf = ((RandomWordFactory) configurableCaptchaService.getWordFactory());
        rwf.setCharacters("123456789");
        rwf.setMaxLength(6);
        rwf.setMinLength(6);

        return rwf.getNextWord();
    }
}

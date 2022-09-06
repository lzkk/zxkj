package com.zxkj.order.service.pay;

import com.zxkj.common.util.net.IPUtils;
import com.zxkj.common.util.security.Signature;
import com.zxkj.order.condition.OrderInfoCondition;
import com.zxkj.order.condition.OrderRefundCondition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Component
public class WeixinPayParam {

    @Resource
    private Signature signature;


    /*****
     * 支付数据处理
     */
    public String weixinRefundParam(OrderRefundCondition orderRefund) throws Exception {
        //预支付下单需要用到的数据
        Map<String, String> data = new HashMap<String, String>();
        data.put("out_trade_no", orderRefund.getOrderNo());    //订单号
        data.put("out_refund_no", orderRefund.getId());     //退款订单号
        //data.put("total_fee",String.valueOf(orderRefund.getMoney()));     //支付金额
        data.put("total_fee", "1");     //支付金额
        data.put("refund_fee", "1");     //退款金额
        data.put("total_fee", "1");     //支付金额
        data.put("notify_url", "http://2cw4969042.wicp.vip:48847/wx/refund/result");  //回调地址（退款申请结果通知地址）
        // TrreMap->MD5->Map->JSON->AES
        return signature.security(data);
    }


    /*****
     * 支付数据处理
     */
    public String weixinParam(OrderInfoCondition order, HttpServletRequest request) throws Exception {
        //预支付下单需要用到的数据
        Map<String, String> data = new HashMap<String, String>();
        data.put("body", "SpringCloud Alibaba商城");
        data.put("out_trade_no", order.getId());    //订单号
        data.put("device_info", "PC");
        data.put("fee_type", "CNY");    // 币种
        //data.put("total_fee", String.valueOf(order.getMoneys()*100));     //支付金额
        data.put("total_fee", "1");     //支付金额
        data.put("spbill_create_ip", IPUtils.getIpAddr(request));  //客户端IP
        data.put("notify_url", "http://2cw4969042.wicp.vip:48847/wx/result");  //回调地址（支付结果通知地址）
        data.put("trade_type", "NATIVE");  // 此处指定为扫码支付

        // TrreMap->MD5->Map->JSON->AES
        return signature.security(data);
    }
}

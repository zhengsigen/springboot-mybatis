package com.c1801.spring.text.demo.wx;


import okhttp3.*;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;

@RestController
@RequestMapping("/wx")
public class WXPayController {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @PostMapping("/qrpay")
    public Map createQR(@RequestParam Long fee) throws IOException {
        System.out.print("11111111111111111111111111");
        OkHttpClient client = new OkHttpClient();
        String nonceStr = UUID.randomUUID().toString().replaceAll("-", "");
        String orderId = UUID.randomUUID().toString().replaceAll("-", "");
        //生成预支付订单
        Map<String, Object> params = new TreeMap<>();
        params.put("appid", "wx0f0a4bbe2fc2fc3a");
        //商家id
        params.put("mch_id", "1508262781");
        //随机码唯一性
        params.put("nonce_str", nonceStr);
        params.put("body", "多抓鱼支付");
        params.put("out_trade_no", orderId);
        //支付金额
        params.put("total_fee", fee);
        //ip
        params.put("spbill_create_ip", "112.95.205.28");
        //支付方式，二维码
        params.put("trade_type", "NATIVE");
        //支付通知地址
        params.put("notify_url", "https://wx.panqingshan.cn/notice/1/zsg/dzy/order/pay/notice");
        //签名
        params.put("sign", sign(params));
        // XML
        String xml = toXml(params);
        // 设置请求体
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/xml"), xml);
        // 构建请求
        Request request = new Request.Builder().url("https://api.mch.weixin.qq.com/pay/unifiedorder").post(requestBody).build();
        // 发送请求 - 接收响应
        Response response = client.newCall(request).execute();
        ResponseBody responseBody = response.body();
        String stringXML = responseBody.string();
        //将返回的响应转换成map集合
        Map<String, String> xmlMap = XmlHelper.of(stringXML).toMap();

        // 支付配置
        Map<String, Object> packageParams = new TreeMap<>();
        packageParams.put("img", ZXingUtil.getBase64QRCodeData(xmlMap.get("code_url"), 200, 200));
        return packageParams;
    }
    @RequestMapping("/callback")
    public String getWeChatPayReturn(HttpServletRequest request){
        System.out.println("@2222222222222222222222");
        try {
            InputStream inStream = request.getInputStream();
            int _buffer_size = 1024;
            if (inStream != null) {
                ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                byte[] tempBytes = new byte[_buffer_size];
                int count = -1;
                while ((count = inStream.read(tempBytes, 0, _buffer_size)) != -1) {
                    outStream.write(tempBytes, 0, count);
                }
                tempBytes = null;
                outStream.flush();
                //将流转换成字符串
                String result = new String(outStream.toByteArray(), "UTF-8");
                System.out.println(result);
                //将字符串解析成XML
//                Document doc = DocumentHelper.parseText(result);
//                //将XML格式转化成MAP格式数据
//                Map<String, Object> resultMap = XmlMapHandle.Dom2Map(doc);
//                //后续具体自己实现
            }
            //通知微信支付系统接收到信息
            return "<xml><return_code><![CDATA[SUCCESS]]></return_code> <return_msg><![CDATA[OK]]></return_msg> </xml>";
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        //如果失败返回错误，微信会再次发送支付信息
        return "fail";
    }





    //生成签名处理
    public String sign(Map<String, Object> params){
        Set<String> packageSet = params.keySet();
        StringBuffer sign = new StringBuffer();
        for (String param : packageSet) {
            if(param == null || param.trim().length() == 0) continue;
            if(sign.length() > 0){
                sign.append("&");
            }
            sign.append(param).append("=").append(params.get(param));
        }
        sign.append("&key=").append("lakJYzxp5znq5Pz1JYDBGInzrUNVAeYj");
        return DigestUtils.md5Hex(sign.toString());
    }

    //String转换成XML
    public static String toXml(Map<String, Object> params) {
        StringBuilder xml = new StringBuilder();
        xml.append("<xml>");
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            String key = entry.getKey();
            String value = String.valueOf(entry.getValue());
            // 略过空值
            if (StringUtils.isEmpty(value))
                continue;
            xml.append("<").append(key).append(">");
            xml.append(entry.getValue());
            xml.append("</").append(key).append(">");
        }
        xml.append("</xml>");
        return xml.toString();
    }






}

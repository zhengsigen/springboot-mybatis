package com.c1801.spring.text.demo.wx;

import com.alibaba.fastjson.JSON;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/dzy/order/pay")
public class text {
    // 支付订单
    // 订单通知
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @PostMapping("/notice")
    @ResponseBody
    public String notice(@RequestBody String xml) {
        logger.info("xml", xml);
        System.out.println(xml);
        Map<String, String> map = XmlHelper.of(xml).toMap();
        logger.info("map", map);
        logger.trace("支付通知:=req> \n{} => {}", JSON.toJSONString(map, true), xml);

        Map result = new HashMap<>();
        result.put("return_code", "SUCCESS");
        result.put("return_msg", "OK");
        logger.info("result", toXml(result));
        return toXml(result);
    }

    public String toXml(Map<String, Object> params) {
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

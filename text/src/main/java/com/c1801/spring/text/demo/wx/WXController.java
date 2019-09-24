package com.c1801.spring.text.demo.wx;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

@RestController
@RequestMapping("/wx")
public class WXController {
    RestTemplate restTemplate = new RestTemplate();

    private Map<String, LinkedHashMap> USERS = new LinkedHashMap<>();

    @GetMapping("address")
    public String getAuthorizedAddress(){
        String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxe3e07d4b4cfae325&redirect_uri=http://192.168.6.182/wx/login/&response_type=code&scope=snsapi_userinfo&state="+UUID.randomUUID().toString().replaceAll("-","")+"#wechat_redirect";
        return url;
    }

    @GetMapping("/login")
    public LinkedHashMap getCode(String code, String state,HttpSession session, HttpServletResponse response) {
        System.out.println(state);
        System.out.println(code);
        //根据code(后台)获取token
        String tokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=wxe3e07d4b4cfae325&secret=4c5c4b297f6ecbda72ece68c121e88d4&code=" + code + "&grant_type=authorization_code";
        ResponseEntity<String> tokenlist = restTemplate.getForEntity(tokenUrl, String.class);
        //根据token获取用户
        LinkedHashMap hashMap = JSON.parseObject(tokenlist.getBody(), LinkedHashMap.class);
        String access_token = (String) hashMap.get("access_token");
        System.out.println(access_token);
        String openId = (String) hashMap.get("openid");
        String userUrl = "https://api.weixin.qq.com/sns/userinfo?access_token=" + access_token + "&openid=" + openId + "&lang=zh_CN";
        ResponseEntity<String> user = restTemplate.getForEntity(userUrl, String.class);
        LinkedHashMap userInfoMap = JSON.parseObject(user.getBody(), LinkedHashMap.class);
        session.setAttribute("USER", userInfoMap);
        System.out.println(userInfoMap);
        return userInfoMap;
    }

    @GetMapping("/users")
    public Object users(HttpSession session) {
        LinkedHashMap user = (LinkedHashMap) session.getAttribute("USER");
        if (user == null) return "没有登录";
        return USERS;
    }

    @GetMapping("/config")
    public Map config(@RequestHeader("Referer") String url) {
        System.out.println(url);
        // 1. accessToken
        String forObject = restTemplate.getForObject("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=wxe3e07d4b4cfae325&secret=4c5c4b297f6ecbda72ece68c121e88d4", String.class);
        JSONObject jsonObject = JSON.parseObject(forObject);
        String accessToken = jsonObject.getString("access_token");
        System.out.println("forObject = " + forObject);

        // 2. jsapiTicket
        String jsapiTicketJSON = restTemplate.getForObject(String.format("https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=%s&type=jsapi", accessToken), String.class);
        JSONObject jsapiTicketJsonObject = JSON.parseObject(jsapiTicketJSON);
        String ticket = jsapiTicketJsonObject.getString("ticket");
        System.out.println("forObject = " + jsapiTicketJsonObject);

        // 3. 签名(完全性)
        Map<String, String> packageParams = new TreeMap<>();
        packageParams.put("timestamp", System.currentTimeMillis() / 1000 + "");
        packageParams.put("noncestr", UUID.randomUUID().toString().replaceAll("-", ""));
        packageParams.put("jsapi_ticket", ticket);
        packageParams.put("url", url);
        // timestamp=&noncestr=&jsapi_ticket=&url=
        Set<String> params = packageParams.keySet();
        StringBuffer sign = new StringBuffer();
        for (String param : params) {
            if (sign.length() > 0) {
                sign.append("&");
            }
            sign.append(param).append("=").append(packageParams.get(param));
        }
        String sha1Sign = DigestUtils.sha1Hex(sign.toString());

        // 4. 返回前端
        Map<String, String> configParams = new HashMap<>();
        configParams.put("appId", "wxe3e07d4b4cfae325");
        configParams.put("timestamp", packageParams.get("timestamp"));
        configParams.put("nonceStr", packageParams.get("noncestr"));
        configParams.put("signature", sha1Sign);
        return configParams;
    }


    @GetMapping
    public String getHeader(HttpServletRequest request) {
        String userAgent = request.getHeader("user-agent");
        System.out.println(userAgent);
        if (userAgent.indexOf("Android") != -1) {
            String defaultFailureUrl = "/login_moblie.jsp";
            System.out.println("Android访问！！！" + "没有登录,返回的页面===" + defaultFailureUrl);
        } else if (userAgent.indexOf("iPhone") != -1 || userAgent.indexOf("iPad") != -1) {
            String defaultFailureUrl = "/login_moblie.jsp";
            System.out.println("iPhone/iPad访问！！！" + "没有登录,返回的页面===" + defaultFailureUrl);
        } else { // 电脑
            String defaultFailureUrl = "/login.jsp";
            System.out.println("电脑访问！！！" + "没有登录,返回的页面===" + defaultFailureUrl);
        }
        return "null";
    }
}

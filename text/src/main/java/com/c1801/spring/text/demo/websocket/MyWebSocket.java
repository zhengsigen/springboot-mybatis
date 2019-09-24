package com.c1801.spring.text.demo.websocket;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

//每个客户端连接成功的时候在后台都会创建一个相应的MyWebsocket类
@Controller
@RequestMapping(value="/socketcontroller")
@ServerEndpoint("/mywebsocket")
public class MyWebSocket {

    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。若要实现服务端与单一客户端通信的话，可以使用Map来存放，其中Key可以为用户标识
    private static CopyOnWriteArraySet<MyWebSocket> websocketPools = new CopyOnWriteArraySet<MyWebSocket>();

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    @RequestMapping(value = "/viewsocket")
    public ModelAndView index() {
        System.out.println("初始化访问");
        ModelAndView mv = new ModelAndView();
        mv.setViewName("sendmessage");
        System.out.println(mv);
        return mv;
    }

    /**
     * 连接建立成功调用的方法
     *
     * @param session 可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    @OnOpen
    public void onOpen(Session session) {
        System.out.println("open");
        this.session = session;
        websocketPools.add(this);
    }

    @OnClose
    public void onClose() {
        System.out.println("close");
        websocketPools.remove(this);
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("OnMessage");
        for (MyWebSocket item : websocketPools) {
            try {
                item.send(message);
            } catch (IOException e) {
                //TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private void send(String message) throws IOException {
        System.out.println("send");
        this.session.getAsyncRemote().sendText(message);
        //this.session.getBasicRemote().sendText(message);

    }

    @OnError
    public void onError(Session session, Throwable error) {
        System.out.println("发生错误");
        error.printStackTrace();
    }
}
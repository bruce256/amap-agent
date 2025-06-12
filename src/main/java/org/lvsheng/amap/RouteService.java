package org.lvsheng.amap;


import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

/**
 * @auther: LvSheng
 * @date: 2025/6/12
 * @description:
 */
@Service
public class RouteService {
    
    @Autowired
    private ChatClient routeAgent;
    
    public String planRoute(String userQuery) {
        return routeAgent.prompt()
                .user(userQuery)
                .call()
                .content();
    }
    
    // 流式响应（适合前端实时展示）
    public Flux<String> planRouteStream(String userQuery) {
        return routeAgent.prompt()
                .user(userQuery)
                .stream()
                .content();
    }
}

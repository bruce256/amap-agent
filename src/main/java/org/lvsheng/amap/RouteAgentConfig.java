package org.lvsheng.amap;


import io.modelcontextprotocol.client.McpAsyncClient;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.deepseek.DeepSeekChatModel;
import org.springframework.ai.mcp.AsyncMcpToolCallbackProvider;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @auther: LvSheng
 * @date: 2025/6/12
 * @description:
 */
@Configuration
public class RouteAgentConfig {
    
    @Bean
    public ChatClient routeAgent(DeepSeekChatModel deepseekModel, AsyncMcpToolCallbackProvider toolProvider) {
        return ChatClient.builder(deepseekModel)
                .defaultToolCallbacks(toolProvider.getToolCallbacks())
                .defaultOptions(ChatOptions.builder()
                        .model("deepseek-chat")
                        .temperature(0.3).build())
                .defaultSystem(
                        """
                        你是一个智能路线规划助手，根据用户请求调用高德地图工具。
                        支持工具：amap.drivingRoute（驾车）、amap.walkingRoute（步行）、amap.busRoute（公交）
                        """)
                .build();
    }
}

package org.lvsheng.amap;


import io.modelcontextprotocol.client.McpAsyncClient;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
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
	private final int MAX_MESSAGES = 100;
	
	private final InMemoryChatMemoryRepository chatMemoryRepository    = new InMemoryChatMemoryRepository();
	private final MessageWindowChatMemory      messageWindowChatMemory = MessageWindowChatMemory.builder()
																								.chatMemoryRepository(chatMemoryRepository)
																								.maxMessages(MAX_MESSAGES)
																								.build();
	
	@Bean
	public ChatClient routeAgent(DeepSeekChatModel deepseekModel, AsyncMcpToolCallbackProvider toolProvider) {
		
		return ChatClient.builder(deepseekModel)
						 .defaultToolCallbacks(toolProvider.getToolCallbacks())
						 .defaultOptions(
								 ChatOptions.builder()
											.model("deepseek-reasoner")
											.temperature(0.3)
											.build()
						 )
						 .defaultSystem("""
												你是一个智能路线规划助手，根据用户请求调用高德地图工具。同时需要考虑天气因素！请把所有调用了高德地图的mcp服务的输入输出都打印出来
												高德地图所有的工具你都能用。
												""")
						 .defaultAdvisors(
								 MessageChatMemoryAdvisor.builder(messageWindowChatMemory)
														 .build())
						 //支持工具：amap.drivingRoute（驾车）、amap.walkingRoute（步行）、amap.busRoute（公交）
						 .build();
	}
}

package org.lvsheng.amap;


import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.deepseek.DeepSeekChatModel;
import org.springframework.ai.mcp.AsyncMcpToolCallbackProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
	public ChatClient routeAgent(DeepSeekChatModel deepseekModel,
								 @Autowired(required = true) AsyncMcpToolCallbackProvider toolProvider) {
		ChatClient.Builder builder = ChatClient.builder(deepseekModel);
		
		// 只有当 MCP 工具回调可用时才添加
		if (toolProvider != null) {
			builder.defaultToolCallbacks(toolProvider.getToolCallbacks());
		}
		
		return builder.defaultOptions(
							  ChatOptions.builder()
										 .model("deepseek-chat")
										 .temperature(0.7)
										 .build()
					  )
					  .defaultSystem("""
											 你是一个智能路线规划助手，根据用户请求调用高德地图工具。同时需要考虑天气因素！请把所有调用了高德地图的 mcp 服务的输入输出都打印出来
											 高德地图所有的工具你都能用。
											 """)
					  .defaultAdvisors(
							  MessageChatMemoryAdvisor.builder(messageWindowChatMemory)
													  .build())
					  //支持工具：amap.drivingRoute（驾车）、amap.walkingRoute（步行）、amap.busRoute（公交）
					  .build();
	}
}

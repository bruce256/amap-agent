package org.lvsheng.amap;


import io.modelcontextprotocol.client.McpAsyncClient;
import io.modelcontextprotocol.spec.McpSchema;
import org.springframework.ai.chat.client.ChatClient;
import static org.springframework.ai.chat.memory.ChatMemory.CONVERSATION_ID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

/**
 * @auther: LvSheng
 * @date: 2025/6/8
 * @description:
 */

@RestController
@RequestMapping("/amap")
public class AmapAssistantController {
	
	@Autowired
	private ChatClient routeAgent;
	
	@GetMapping(value = "/completion"/*, produces = MediaType.TEXT_EVENT_STREAM_VALUE*/)
	public String reactCodeGeneration(@RequestParam String question,
											@RequestParam(value = "conversation_id", defaultValue = "yingzi") String conversationId) {
		return routeAgent.prompt()
						 .user(question)
						 .advisors(
								 advisorSpec -> advisorSpec.param(CONVERSATION_ID, conversationId)
						 )
						 .call()
						 .content();
		
	}
	
	@Autowired
	List<McpAsyncClient> mcpAsyncClients;
	
	@RequestMapping("/test")
	public Mono<McpSchema.CallToolResult> test() {
		var mcpClient = mcpAsyncClients.get(0);
		
		return mcpClient.listTools()
						.flatMap(tools -> {
							System.out.println("tools: " + tools);
							
							return mcpClient.callTool(
									new McpSchema.CallToolRequest(
											"maps_weather",
											Map.of("city", "北京")
									)
							);
						});
	}
}

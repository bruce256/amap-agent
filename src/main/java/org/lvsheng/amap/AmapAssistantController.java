package org.lvsheng.amap;


import io.modelcontextprotocol.client.McpAsyncClient;
import io.modelcontextprotocol.spec.McpSchema;
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
    private RouteService amapService;
    
    @GetMapping(value = "/completion"/*, produces = MediaType.TEXT_EVENT_STREAM_VALUE*/)
    public Flux<String> reactCodeGeneration(@RequestParam String question) {
        return amapService.planRouteStream(question);
        
        
    }
    @Autowired
    List<McpAsyncClient> mcpAsyncClients;
    
    @RequestMapping("/test")
    public Mono<McpSchema.CallToolResult> test() {
        var mcpClient = mcpAsyncClients.get(0);
        
        return mcpClient.listTools()
                .flatMap(tools -> {
                    System.out.println("tools: " +  tools);
                    
                    return mcpClient.callTool(
                            new McpSchema.CallToolRequest(
                                    "maps_weather",
                                    Map.of("city", "北京")
                            )
                    );
                });
    }
}

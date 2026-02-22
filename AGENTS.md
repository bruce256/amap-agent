# Repository Guidelines

所有的回答都用中文

## 项目结构与模块组织
- `src/main/java/org/lvsheng/amap`：应用主代码。
- `Application.java`：Spring Boot 启动入口。
- `AmapAssistantController.java`：`/amap` 下的 HTTP 接口。
- `RouteAgentConfig.java` 与 `McpConfig.java`：AI 模型、会话记忆与 MCP 传输配置。
- `src/main/resources/application.yml`：运行时配置（端口、DeepSeek、MCP 客户端）。
- `src/test/java/org/lvsheng/amap`：测试代码（当前为 `AppTest.java`）。
- `target/`：Maven 构建产物目录，请勿手动编辑。

## 构建、测试与开发命令
- `mvn clean compile`：清理并编译项目。
- `mvn test`：执行 `src/test/java` 下的单元测试。
- `mvn spring-boot:run`：本地启动应用（默认端口 `9090`）。
- `mvn clean package`：打包可运行 JAR 到 `target/`。

本地运行示例：
```bash
mvn spring-boot:run
# 然后调用：GET http://localhost:9090/amap/completion?question=...
```

## 代码风格与命名约定
- Java 代码风格：4 空格缩进、UTF-8 编码、每个文件仅一个顶级 `public` 类。
- 包名使用小写（例如 `org.lvsheng.amap`）。
- 类名使用 `PascalCase`；方法/字段使用 `camelCase`；常量使用 `UPPER_SNAKE_CASE`。
- 控制器方法保持轻量；模型与工具装配放在 `@Configuration` 配置类中。
- 新代码优先使用构造器注入，避免新增字段注入。

## 测试规范
- 当前测试框架为 JUnit 3 风格（`junit.framework`）。新增测试建议使用 JUnit Jupiter（`org.junit.jupiter`）。
- 测试类名以 `Test` 结尾，并与源码包路径保持对应。
- 每次提交 PR 前运行 `mvn test`；修复缺陷时需补充回归测试。

## 提交与 Pull Request 规范
- 历史提交以简短中文祈使句为主（例如：`清理无用依赖`、`修改为同步返回`），请保持风格统一、粒度聚焦。
- 每个提交只包含一个逻辑变更。
- PR 应包含：
  - 变更摘要与动机，
  - 测试证据（`mvn test` 结果），
  - 若有接口变更，提供请求/响应示例，
  - 可关联的 issue/任务编号。

## 安全与配置提示
- 严禁提交真实 API Key 或 MCP 密钥。请将 `application.yml` 中敏感信息改为环境变量。
- 推荐占位方式：`${DEEPSEEK_API_KEY}` 与 `${AMAP_MCP_KEY}`，并在本地使用覆盖配置。

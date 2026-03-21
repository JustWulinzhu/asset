# AI服务动态切换架构说明

## 概述
本项目重构了AI服务调用架构，实现了火山引擎API和智谱API之间的动态切换，无需手动修改代码。

## 架构设计

### 1. 接口层
- `AIService` - 统一的AI服务接口
  - `chatCall(String input)` - 普通聊天调用
  - `generateSQL(String input, String systemPrompt)` - 自然语言转SQL

### 2. 实现层
- `HuoshanAIService` - 火山引擎API实现
- `ZhipuAIService` - 智谱API实现

### 3. 配置层
- `AIConfig` - 配置管理类，使用Spring Boot配置属性绑定
- `AIServiceFactory` - 服务工厂，根据配置动态选择实现

### 4. 适配层
- `HuoshanServiceImpl` - 原有服务的适配层，向后兼容

## 配置说明

### 配置文件 (`application.properties`)
```properties
# AI服务配置
# 可选值: huoshan, zhipu
ai.provider=huoshan

# 火山引擎API配置
ai.huoshan.api-url=https://ark.cn-beijing.volces.com/api/v3/chat/completions
ai.huoshan.api-key=your_api_key_here
ai.huoshan.model=doubao-seed-2-0-pro-260215

# 智谱API配置
ai.zhipu.api-url=https://open.bigmodel.cn/api/paas/v4/chat/completions
ai.zhipu.api-key=your_api_key_here
ai.zhipu.model=glm-4-flash
```

### 切换API服务商
只需修改 `ai.provider` 的值：
- 设置为 `huoshan` 使用火山引擎API
- 设置为 `zhipu` 使用智谱API

重启应用后，所有AI调用将自动切换到相应的服务商。

## 使用方式

### 1. 原有代码无需修改
原有的 `HuoshanService` 接口和实现保持不变，调用方式完全兼容。

### 2. 新增功能
- **统一接口**：新增 `AIService` 接口，用于统一API调用
- **动态选择**：通过 `AIServiceFactory` 可以动态选择API实现

### 3. 测试端点
新增测试控制器 `AIConfigController`：

```
# 获取当前配置
GET /api/ai/config/current

# 获取火山引擎配置
GET /api/ai/config/huoshan

# 获取智谱配置
GET /api/ai/config/zhipu

# 测试AI服务
GET /api/ai/config/test
GET /api/ai/config/test?provider=huoshan
GET /api/ai/config/test?provider=zhipu
```

### 4. 代码示例
```java
// 使用当前配置的AI服务
AIService aiService = aiServiceFactory.getCurrentAIService();
String response = aiService.chatCall("你好");

// 使用指定服务商的AI服务
AIService huoshanService = aiServiceFactory.getAIService("huoshan");
AIService zhipuService = aiServiceFactory.getAIService("zhipu");
```

## 扩展说明

### 添加新的AI服务商
1. 实现 `AIService` 接口
2. 在 `AIConfig` 中添加配置类
3. 在 `AIServiceFactory` 中添加服务选择逻辑
4. 更新配置文件

### 优点
1. **解耦**：API调用逻辑与具体实现分离
2. **可扩展**：易于添加新的AI服务商
3. **配置驱动**：无需修改代码即可切换服务
4. **向后兼容**：原有代码无需修改
5. **统一管理**：所有API调用通过统一接口管理

## 注意事项
1. 不同API的请求/响应格式可能不同，需要在各自的实现类中处理
2. 配置属性名称使用kebab-case格式（如 `api-url`）
3. 新增服务商时需确保模型名称和API端点配置正确
4. 建议在切换服务商前进行充分的测试
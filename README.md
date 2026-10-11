# 校园闲置物品交易平台

## 一、项目简介

本项目是一个面向高校师生的校园闲置物品交易平台，支持商品发布、浏览、搜索、下单、订单管理、留言沟通、购物车管理等功能，并集成了 AI 辅助生成商品描述功能（调用 DeepSeek API）。

## 二、技术栈

| 层次 | 技术 |
|:---|:---|
| 后端 | Spring Boot 2.7.18 + Spring MVC + MyBatis |
| 前端 | Vue.js 2.x + Element UI + Axios |
| 数据库 | MySQL 8.0 |
| 开发工具 | IDEA + VSCode |
| AI 服务 | DeepSeek API |

## 三、开发环境

| 软件 | 版本 | 说明 |
|:---|:---|:---|
| JDK | 17 | 后端运行环境 |
| Maven | 3.6+ | 后端依赖管理 |
| MySQL | 8.0 | 数据库 |
| Node.js | 16+ | 前端运行环境 |

## 四、后端配置与启动

### 1. 导入数据库

打开 Navicat，新建数据库 `second_hand_db`，执行 `second_hand_db.sql` 脚本。

### 2. 修改数据库配置

打开 `src/main/resources/application.properties`，修改数据库密码：

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/second_hand_db?useSSL=false&serverTimezone=Asia/Shanghai&characterEncoding=utf8
spring.datasource.username=root
spring.datasource.password=你的MySQL密码
```

### 3. 修改图片上传路径

打开 `src/main/java/com/test/controller/ProductController.java`，修改上传路径：

```java
private static final String UPLOAD_DIR = "你的项目路径/uploads/products/";
```

打开 `src/main/java/com/test/config/WebConfig.java`，修改静态资源路径：

```java
registry.addResourceHandler("/uploads/**")
        .addResourceLocations("file:你的项目路径/uploads/");
```

### 4. 配置 DeepSeek API Key

打开 `src/main/java/com/test/config/AiConfig.java`，填入你的 API Key：

```java
public static final String API_KEY = "sk-你的DeepSeek API Key";
```

### 5. 启动后端

在 IDEA 中运行 `Application.java`，看到 `Started Application` 即成功。

## 五、前端配置与启动

### 1. 安装依赖

在终端中执行：

```bash
cd second-hand-frontend
npm install
```

### 2. 启动前端

```bash
npm run serve
```

### 3. 访问系统

浏览器打开 `http://localhost:8081`

## 六、默认账号

| 角色 | 用户名 | 密码 |
|:---|:---|:---|
| 管理员 | admin | admin123 |
| 普通用户 | 自行注册 | - |

## 七、功能说明

- 用户注册、登录、权限管理
- 商品发布、审核、浏览、搜索、分类筛选
- 在线下单、订单管理（买家/卖家双向）
- 买家与卖家留言沟通
- 购物车管理
- AI 辅助生成商品描述（调用 DeepSeek API）
package com.test.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.test.dto.Result;
import okhttp3.*;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/ai")
@CrossOrigin(origins = {"http://localhost:8080", "http://127.0.0.1:8080", "http://localhost:8081", "http://127.0.0.1:8081"}, allowCredentials = "true")
public class AiController {

    private static final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build();

    @PostMapping("/generate-desc")
    public Result<Map<String, String>> generateDescription(@RequestParam String title,
                                                           @RequestParam(required = false) String category) {
        if (title == null || title.trim().isEmpty()) {
            return Result.error("商品标题不能为空");
        }

        try {
            // 构造提示词
            String prompt = "你是一个校园二手交易平台的商品描述助手。" +
                    "请根据以下商品信息，生成一段简洁、真实、口语化的商品描述，50-100字左右。" +
                    "要求：语气自然，像学生自己写的一样，不要浮夸，不要用'亲'等淘宝腔调，直接输出描述内容，不要加任何前缀。\n\n" +
                    "商品标题：" + title + "\n" +
                    (category != null ? "商品分类：" + category + "\n" : "");

            // 构造请求体
            JSONObject requestBody = new JSONObject();
            requestBody.put("model", com.test.config.AiConfig.MODEL);
            JSONArray messages = new JSONArray();

            JSONObject systemMsg = new JSONObject();
            systemMsg.put("role", "system");
            systemMsg.put("content", "你是一个校园二手交易平台的商品描述助手，帮助学生写出真实自然的商品描述。");
            messages.add(systemMsg);

            JSONObject userMsg = new JSONObject();
            userMsg.put("role", "user");
            userMsg.put("content", prompt);
            messages.add(userMsg);

            requestBody.put("messages", messages);
            requestBody.put("temperature", 0.7);
            requestBody.put("max_tokens", 300);

            // 发送请求（注意：RequestBody 和 MediaType 用完整包名，避免与 Spring 的 @RequestBody 冲突）
            Request request = new Request.Builder()
                    .url(com.test.config.AiConfig.API_URL)
                    .addHeader("Authorization", "Bearer " + com.test.config.AiConfig.API_KEY)
                    .addHeader("Content-Type", "application/json")
                    .post(okhttp3.RequestBody.create(
                            requestBody.toJSONString(),
                            okhttp3.MediaType.parse("application/json")))
                    .build();

            Response response = client.newCall(request).execute();

            if (!response.isSuccessful()) {
                return Result.error("AI 服务调用失败：" + response.code());
            }

            String responseBody = response.body().string();
            JSONObject jsonResponse = JSON.parseObject(responseBody);
            JSONArray choices = jsonResponse.getJSONArray("choices");

            if (choices == null || choices.isEmpty()) {
                return Result.error("AI 未返回有效内容");
            }

            String content = choices.getJSONObject(0)
                    .getJSONObject("message")
                    .getString("content");

            Map<String, String> data = new HashMap<>();
            data.put("description", content.trim());
            return Result.success(data);

        } catch (IOException e) {
            e.printStackTrace();
            return Result.error("AI 服务连接失败：" + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("生成失败：" + e.getMessage());
        }
    }
}

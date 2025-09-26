package com.csi.util;

import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class AiClient {

    private static final String API_KEY = "f44f9327-08d8-4001-9a71-b8420a41f775";
    private static final String ENDPOINT = "https://ark.cn-beijing.volces.com/api/v3";
    private static final String MODEL = "doubao-seed-1-6-250615";

    public String chat(String prompt) {
        try {
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .build();

            MediaType JSON = MediaType.get("application/json; charset=utf-8");

            // 请求体：加上 max_tokens 和 temperature
            String bodyStr = "{"
                    + "\"model\":\"" + MODEL + "\","
                    + "\"messages\":[{\"role\":\"user\",\"content\":\"" + prompt + "\"}],"
                    + "\"max_tokens\":512,"
                    + "\"temperature\":0.7"
                    + "}";

            RequestBody body = RequestBody.create(bodyStr, JSON);

            Request request = new Request.Builder()
                    .url(ENDPOINT + "/chat/completions")
                    .addHeader("Authorization", "Bearer " + API_KEY)
                    .addHeader("Content-Type", "application/json")
                    .post(body)
                    .build();

            Response response = client.newCall(request).execute();
            String respStr = response.body().string();

            // 打印原始返回，方便排查
            System.out.println("🔥 AI原始返回：" + respStr);

            JSONObject json = new JSONObject(respStr);

            // 如果返回包含 error
            if (json.has("error")) {
                return "AI接口报错：" + json.getJSONObject("error").getString("message");
            }

            // 如果返回包含 choices
            if (json.has("choices")) {
                JSONArray choices = json.getJSONArray("choices");
                if (choices.length() > 0) {
                    JSONObject message = choices.getJSONObject(0).getJSONObject("message");
                    return message.getString("content");
                }
            }

            return "AI接口响应异常：未找到 choices 字段";

        } catch (Exception e) {
            return "AI生成失败：" + e.getMessage();
        }
    }
}

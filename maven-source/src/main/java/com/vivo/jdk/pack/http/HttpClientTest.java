package com.vivo.jdk.pack.http;

import com.alibaba.fastjson.JSONObject;
import okhttp3.*;

import java.io.IOException;

/**
 * 类描述：
 *
 * @author 汤旗
 * @date 2019-10-11 17:59
 */
public class HttpClientTest {

    public static void main(String[] args) throws IOException {
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "[\n    {\n        \"adId\": 20000000,\n        \"requestId\": \"b084c3f293bebeac8e0c074c5fce4134\",\n        \"imei\": \"324321231231231\",\n        \"clickTime\": 1570779881858,\n        \"oaid\": \"68b8d2699b67917bc3bf\",\n        \"ip\": \"172.25.227.105\"\n    }\n]");
        Request request = new Request.Builder()
                .url("https://test-apis.520yidui.com/tmarket/clicks/feedback/vivonew")
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("cache-control", "no-cache")
                .addHeader("Postman-Token", "48d51537-4fbd-417e-b712-d9f8dcdd4cba")
                .build();

        Response response = client.newCall(request).execute();
        System.out.println(String.format("响应:%s", JSONObject.toJSONString(response)));
    }
}

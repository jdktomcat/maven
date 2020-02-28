package com.vivo.modal;

import okhttp3.*;

import java.io.IOException;

/**
 * 类描述：
 *
 * @author 11072131
 * @date 2020-02-2020/2/21 17:38
 */
public class OpenApiClient {

    public static void main(String[] args) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "apiUuid=0fe135800699918c7183ba504b6d4fb5&requestStr={\"reportDate\":\"20200205\"}&sign=BFC9D136F9D4D9229B7CDE09F6792C4C348499BDAC779169895B8EE5B4EFE29D");
        Request request = new Request.Builder()
                .url("http://ad-market.vivo.com.cn/v1/downloadImei/query")
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();
        Response response = client.newCall(request).execute();
    }
}

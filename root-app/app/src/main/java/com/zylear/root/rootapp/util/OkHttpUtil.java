package com.zylear.root.rootapp.util;

import okhttp3.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created by xiezongyu on 2018/8/12.
 */
public class OkHttpUtil {

    public static Response syncExecJsonPost(String url, String jsonString) throws IOException {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build();

        //MediaType  设置Content-Type 标头中包含的媒体类型值
        RequestBody requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8"), jsonString);
//        RequestBody body = new FormBody.Builder()
//                .add("键", "值")
//                .add("键", "值")
//                .build();

        Request request = new Request.Builder()
                .url(url)//请求的url
                .post(requestBody)
                .build();

        //创建/Call
        Call call = okHttpClient.newCall(request);
        return call.execute();
    }

    public static String syncExeJsonPostGetString(String url, String jsonString) {
        String content = null;
        try {
            Response response = syncExecJsonPost(url, jsonString);
            content = response.body().string();
            response.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

}

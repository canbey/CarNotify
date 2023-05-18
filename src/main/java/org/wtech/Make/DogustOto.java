package org.wtech.Make;

import okhttp3.*;
import org.wtech.Main;

import java.io.IOException;

public class DogustOto  {
    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");

    static OkHttpClient client = new OkHttpClient();

    Response dogusOtoService(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Accept", "application/json, text/plain, */*")
                .addHeader("Accept-Language", "tr-TR,tr;q=0.9,en-US;q=0.8,en;q=0.7")
                .addHeader("Cache-Control", "no-cache")
                .addHeader("Connection", "keep-alive")
                .addHeader("Content-Type", "application/json;charset=UTF-8")
                .addHeader("Cookie", "ASP.NET_SessionId=3tnzjwxnzfni4kg1vohvp1zn; ...")
                .addHeader("Origin", "https://www.dogusoto.com.tr")
                .addHeader("Pragma", "no-cache")
                .addHeader("Referer", "https://www.dogusoto.com.tr/arac-arama?q=volkswagen")
                .addHeader("Sec-Fetch-Dest", "empty")
                .addHeader("Sec-Fetch-Mode", "cors")
                .addHeader("Sec-Fetch-Site", "same-origin")
                .addHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/102.0.0.0 Safari/537.36")
                .addHeader("sec-ch-ua", "\" Not A;Brand\";v=\"99\", \"Chromium\";v=\"102\", \"Google Chrome\";v=\"102\"")
                .addHeader("sec-ch-ua-mobile", "?0")
                .addHeader("sec-ch-ua-platform", "\"macOS\"")
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response;
        }
    }

    public static void main() throws IOException {
        DogustOto fetch = new DogustOto();
        String json = "{\"SearchText\":\"\",\"ShortResult\":false}";
        Response response = fetch.dogusOtoService("https://www.dogusoto.com.tr/api/vehicle/getvehiclesearch", json);
        if(fetch.dogusCarFound(response)) {
            System.out.println("Volkswagen aracı bulundu");
            Main.sendTelegramMessage(client,"Volkswagen aracı bulundu");
        }
        else {
            System.out.println("Volkswagen aracı bulunamadı");
        }


    }
    public boolean dogusCarFound(Response response) {
        return response.toString().contains("Volkswagen");
    }
}

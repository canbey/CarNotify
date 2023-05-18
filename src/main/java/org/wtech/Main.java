package org.wtech;
import okhttp3.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.wtech.Make.DogustOto;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class Main {

    private static final String URL = "https://shop.mercedes-benz.com/smsc-backend-os/dcp-api/v2/market-tr/products/search?lang=tr&query=%3Aprice-asc%3AallCategories%3Amarket-tr-vehicles&currentPage=0&pageSize=50&fields=FULL";
    private static final String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/111.0.0.0 Safari/537.36";
    private static final long PERIOD = 5 * 1000L;  // 1 minute


    private static final String TELEGRAM_BOT_TOKEN = System.getenv("TELEGRAM_BOT_TOKEN");
    private static final String TELEGRAM_CHAT_ID = System.getenv("TELEGRAM_CHAT_ID");


    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");


    private static JsonArray lastProductList = new JsonArray();

    public static void main(String[] args) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                fetchProducts();
                try {
                    DogustOto.main();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }, 0, PERIOD);
    }

    private static void fetchProducts() {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(URL)
                .header("User-Agent", USER_AGENT)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String responseBody = response.body().string();
                JsonObject jsonResponse = JsonParser.parseString(responseBody).getAsJsonObject();
                JsonArray productList = jsonResponse.getAsJsonArray("products");

                if (!productList.equals(lastProductList)) {
                    System.out.println("Product list has changed!");
                    lastProductList = productList;
                    String carCount = productList.size() + "   Sayıda değişiklik oldu yeni araç sayısı";
                    String description = "";
                    StringBuilder messageBuilder = new StringBuilder();
                    for (JsonElement productElement : productList) {
                        JsonObject product = productElement.getAsJsonObject();
                                description = product.get("description").getAsString();
                        String vehicleId = product.get("code").getAsString();
                        JsonObject listPrice = product.get("listPrice").getAsJsonObject();
                        String formattedValue = listPrice.get("formattedValue").getAsString();
                        String message = "https://www.mercedes-benz.com.tr/passengercars/buy/new-car/product.html/"+vehicleId;
                        messageBuilder.append(message).append("\n");
                        sendTelegramMessage(client, description+" ---  ");
                        sendTelegramMessage(client,message);
                    }

                    String message = messageBuilder.toString();

                    //sendTelegramMessage(client, message);
                }else {
                    System.out.println("Product list has not changed!");
                }
            } else {
                throw new IOException("Unexpected code " + response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendTelegramMessage(OkHttpClient client, String message) {
        String telegramApiUrl = "https://api.telegram.org/" + TELEGRAM_BOT_TOKEN + "/sendMessage";
        String payload = "{ \"chat_id\": \"" + TELEGRAM_CHAT_ID + "\", \"text\": \"" + message + "\" }";

        RequestBody body = RequestBody.create(payload, JSON);
        Request request = new Request.Builder()
                .url(telegramApiUrl)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

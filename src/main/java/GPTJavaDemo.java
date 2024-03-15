import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class GPTJavaDemo {

    private static final String OPENAI_API_KEY = "your-api-key";
    private static final String MODEL_ENDPOINT = "https://api.openai.com/v1/completions";

    public static void main(String[] args) throws IOException {
        String prompt = "Once upon a time";
        int maxTokens = 50; // 最大生成标记数
        String response = generateText(prompt, maxTokens);
        System.out.println("Generated text: " + response);
    }

    public static String generateText(String prompt, int maxTokens) throws IOException {
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType,
                "{\"model\":\"text-davinci-003\"," +
                        "\"prompt\":\"" + prompt + "\"," +
                        "\"max_tokens\":" + maxTokens + "}");

        Request request = new Request.Builder()
                .url(MODEL_ENDPOINT)
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + OPENAI_API_KEY)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            String responseBody = response.body().string();
            JSONObject jsonResponse = new JSONObject(responseBody);
            String text = jsonResponse.getJSONArray("choices").getJSONObject(0).getString("text");
            return text;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}

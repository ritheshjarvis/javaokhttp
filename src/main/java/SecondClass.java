import okhttp3.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;

public class SecondClass {
    public static void main(String[] args) throws IOException, ParseException {
        OkHttpClient client = new OkHttpClient().newBuilder().build();

        MediaType mediaType = MediaType.parse("application/json");

//        RequestBody body = RequestBody.create(mediaType, "{\n\t\"email\":\"tub@getnada.com\",\n\t\"password\":\"tub@123\"\n}");
        RequestBody body = RequestBody.create(mediaType, "{\n\t\"email\":\"rithesh535@yopmail.com\",\n\t\"password\":\"Admin123\"\n}");

        Request request = new Request.Builder()
                .url("https://pikachu.outagedetection.com/users/authenticate")
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .build();

        Response response = client.newCall(request).execute();
//        System.out.println(response.getClass());
//        System.out.println(response.body().string());
        String jsonData = response.body().string();
//        System.out.println(jsonData);
        JSONParser parser = new JSONParser();
        JSONObject data = (JSONObject) parser.parse(jsonData);
        System.out.println(data);
        String accessToken = null;
        accessToken = data.get("accessToken").toString();
        System.out.println(accessToken);


        OkHttpClient client1 = new OkHttpClient().newBuilder()
                .build();
        Request request1 = new Request.Builder()
                .url("https://pikachu.outagedetection.com/device-list")
                .method("GET", null)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", accessToken)
                .build();
        Response response1 = client1.newCall(request1).execute();
        String jsonData1 = response1.body().string();
        JSONParser parser1 = new JSONParser();
        JSONObject data1 = (JSONObject) parser1.parse(jsonData1);
        System.out.println(data1);
        System.out.println("------------------------------->");
//        String result1 = data1.get("connectedDevices").toString();
//        JSONArray philips_device = (JSONArray) data1.get("connectedDevices");
        JSONArray philips_device = (JSONArray) data1.get("connectedDevices");
        System.out.println(philips_device);

        JSONObject jsonobj_1 = null;
        for (int i = 0; i < philips_device.size(); i++) {
            if (i == 0) {
                continue;
            }
            jsonobj_1 = (JSONObject) philips_device.get(i);
        }
        System.out.println(jsonobj_1);

        JSONArray philips_device_states = (JSONArray) jsonobj_1.get("states");
        System.out.println(philips_device_states);

        // To retrieve Power state object from State array object
        JSONObject power_state = null;
        for (int i = 0; i < philips_device_states.size(); i++) {
            if (i == 1) {
                power_state = (JSONObject) philips_device_states.get(i);
                break;
            }
            continue;
        }
        System.out.println("---> Power State Object");
        System.out.println(power_state);

        System.out.println("---> Power State value_human_readable value (Unknown-> OFF, On -> ON)");
        String philips_bulb_powerstate = power_state.get("value_human_readable").toString();
        System.out.println(philips_bulb_powerstate);
    }
}

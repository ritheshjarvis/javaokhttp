import okhttp3.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;

public class testhttp {

    public static String state_value_human_readable(JSONArray device_states, String device_state_name){
        JSONObject device_state = null;
        String device_state_name_value = "";
        for (int i = 0; i < device_states.size(); i++) {
            device_state = (JSONObject) device_states.get(i);
            device_state_name_value = device_state.get("name").toString();
            if (device_state_name_value.equals(device_state_name)) {
                break;
            }
        }
        String device_state_value_human_readable = device_state.get("value_human_readable").toString();
        return device_state_value_human_readable;
    }

    public static JSONArray get_device_sates_array(String jsonData1, String devie_type) throws ParseException {
        JSONParser parser_data = new JSONParser();
        JSONObject parser_data_json_object = (JSONObject) parser_data.parse(jsonData1);
        JSONArray connected_device_array = (JSONArray) parser_data_json_object.get("connectedDevices");

        JSONObject required_device = null;
        String required_device_type = "";
        for (int i = 0; i < connected_device_array.size(); i++) {
            required_device = (JSONObject) connected_device_array.get(i);
            required_device_type = required_device.get("type").toString();;
            if (required_device_type.contains(devie_type)){
                break;
            }
        }
        JSONArray required_device_states = (JSONArray) required_device.get("states");
        return required_device_states;
    }

    public static void main(String[] args) throws IOException, ParseException {
        /**
         https://mkyong.com/java/okhttp-how-to-send-http-requests/
         https://www.vogella.com/tutorials/JavaLibrary-OkHttp/article.html
         for parsing:
         https://dzone.com/articles/how-to-parse-json-data-from-a-rest-api-using-simpl
         */
        OkHttpClient client = new OkHttpClient().newBuilder().build();

        MediaType mediaType = MediaType.parse("application/json");

        RequestBody body = RequestBody.create(mediaType, "{\n\t\"email\":\"tub@getnada.com\",\n\t\"password\":\"tub@123\"\n}");
//        RequestBody body = RequestBody.create(mediaType, "{\n\t\"email\":\"rithesh535@yopmail.com\",\n\t\"password\":\"Admin123\"\n}");

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
        String response_body_data = response1.body().string();

        JSONArray required_device_states_array = get_device_sates_array(response_body_data, "philips_hue_bulb");
        System.out.println(state_value_human_readable(required_device_states_array, "Color Temperature"));
    }
}

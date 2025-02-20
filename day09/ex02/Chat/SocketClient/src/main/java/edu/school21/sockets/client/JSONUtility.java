package edu.school21.sockets.client;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class JSONUtility {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");


    public static JSONObject toJSON(JSONData data) {
        JSONObject json = new JSONObject();
        json.putAll(data.getFields());
        if (data.getTime() != null) {
            json.put("time", data.getTime().format(FORMATTER));
        }
        return json;
    }


    public static JSONData fromJSON(String jsonString) {
        try {
            JSONObject jsonObject = (JSONObject) new JSONParser().parse(jsonString);
            JSONData data = new JSONData();

            for (Object key : jsonObject.keySet()) {
                String keyStr = (String) key;
                Object value = jsonObject.get(keyStr);
                data.addField(keyStr, value);
            }

            if (jsonObject.containsKey("time")) {
                data.setTime(LocalDateTime.parse((String) jsonObject.get("time"), FORMATTER));
            }
            return data;
        } catch (ParseException e) {
            System.err.println("Error parsing JSON: " + e.getMessage());
            return null;
        }
    }
}

package edu.school21.sockets.server;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Data
@NoArgsConstructor
public class JSONData {
    private LocalDateTime time;
    private Map<String, Object> fields = new LinkedHashMap<>();


    public void addField(String key, Object value) {
        fields.put(key, value);
    }

    public Object getField(String key) {
        return fields.get(key);
    }
}
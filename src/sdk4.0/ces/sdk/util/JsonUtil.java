package ces.sdk.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * JSON格式与对象互转工具类
 * Created by WILL on 2015/3/13.
 */
public class JsonUtil extends ObjectMapper {

    private static ObjectMapper mapper = null;

    static {
        if (mapper == null) {
            mapper = new ObjectMapper();
            mapper.configure(JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS, true);
            mapper.getSerializerProvider().setNullValueSerializer(new JsonSerializer<Object>() {
                @Override
                public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
                    jsonGenerator.writeString("");
                }
            });

        }
    }

    public static String toJSON(Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            return "ERROR";
        }
    }

    public static <T> T fromJSON(String json, Class<T> clz) {
        try {
            return mapper.readValue(json, clz);
        } catch (IOException e) {
            return null;
        }
    }

    public static <T> T fromJSON(String json, TypeReference<T> type) {
        try {
            return mapper.readValue(json, type);
        } catch (IOException e) {
            return null;
        }
    }

}

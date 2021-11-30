package crushers.util;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

public class Json {
    
    public static ObjectMapper json = new ObjectMapper();

    public static String stringify(Object value) throws JsonProcessingException {
        return json.writeValueAsString(value);
    }
    public static <T> T parse(String jsonString, Class<T> type) throws JsonMappingException, JsonProcessingException{
        return json.readValue(jsonString, type);
    }
    public static <T> List<T> parseList(String jsonString, Class<T> type) throws JsonMappingException, JsonProcessingException{
        final CollectionType collType = json.getTypeFactory().constructCollectionType(List.class, type);
        return json.readValue(jsonString, collType);
    }

}

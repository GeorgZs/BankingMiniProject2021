package crushers.util;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

public class Json {
    
    public static ObjectMapper json = getObjectMapper();

    public static ObjectMapper getObjectMapper(){
        ObjectMapper json = new ObjectMapper();
        json.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return json;
    }

    public static String stringify(Object value) throws JsonProcessingException {
        return json.writeValueAsString(value);
    }
    public static <T> T parse(String jsonString, Class<T> type) throws JsonMappingException, JsonProcessingException{
        return json.readValue(jsonString, type);
    }

    public static <T> ArrayList<T> parseList(String jsonString, Class<T> type) throws JsonProcessingException {
        final CollectionType collType = json.getTypeFactory().constructCollectionType(ArrayList.class, type);
        return json.readValue(jsonString, collType);
    }
    
    public static JsonNode toNode(String jsonString) throws JsonMappingException, JsonProcessingException{
        return json.readTree(jsonString);
    }

    public static String toString(JsonNode node) throws JsonProcessingException{
        return json.writeValueAsString(node);
    }
    public static JsonNode getEmptyNode(){
        return json.createObjectNode();
    }
}

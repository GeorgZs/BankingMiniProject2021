package crushers.util;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.type.CollectionType;

public class Json {
    
    public static ObjectMapper json = getObjectMapper();

    public static ObjectMapper getObjectMapper(){
        ObjectMapper json = new ObjectMapper();
        json.findAndRegisterModules();
        json.disable(MapperFeature.DEFAULT_VIEW_INCLUSION);
        json.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        json.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
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
    public static JsonNode objectToNode(Object obj) throws JsonMappingException, JsonProcessingException{
        return toNode(stringify(obj));
    }

    /*
    JsonNode methods
    */
    public static JsonNode getEmptyNode(){
        return json.createObjectNode();
    }
    // public static JsonNode nodeWithFields(Object... keyValuePair){
    //     JsonNode node = getEmptyNode();
    //     for(int i=0; i<keyValuePair.length; i=i+2){
    //         String key = String.valueOf(keyValuePair[i]);
    //         Object value = keyValuePair[i+1];
    //         ((ObjectNode)node).set(key, objectToNode(value));
    //     }
    // }

}

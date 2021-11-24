package crushers.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.type.MapLikeType;

public class Json {
  // Singleton pattern
  public final static Json instance = new Json();

  private ObjectMapper json;
  private ObjectWriter jsonWriter;

  private Json() {
    this.json = new ObjectMapper();
    json.findAndRegisterModules();
    json.disable(MapperFeature.DEFAULT_VIEW_INCLUSION);
    json.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    json.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    this.jsonWriter = json.writer(new DefaultPrettyPrinter());
  }

  //the toString method of Json - convert json-object to a normal object
  //then make it a readable string
  public String stringify(Object value) throws JsonProcessingException {
    return json.writeValueAsString(value);
  }

  //writes an object to the specified json file encoded into json
  public <T> void write(Object value, File file, Class<T> type) throws IOException {
    final MapLikeType mapType = json.getTypeFactory().constructMapLikeType(
      LinkedHashMap.class,
      Integer.class,
      type
    );
    
    jsonWriter.forType(mapType).writeValue(file, value);
  }

  //the parse function allows us to return an json-encoded object for further
  //use in the code
  public <T> T parse(String jsonString, Class<T> type) throws JsonProcessingException {
    return json.readValue(jsonString, type);
  }

  //does the same as the function above however it creates a LinkedHashMap and returns that with
  //the values decoded from the json file
  public <T> LinkedHashMap<Integer, T> parseMap(InputStream jsonStream, Class<T> type) throws IOException {
    final MapLikeType mapType = json.getTypeFactory().constructMapLikeType(
      LinkedHashMap.class,
      Integer.class,
      type
    );
    
    return json.readValue(jsonStream, mapType);
  }

}

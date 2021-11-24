package crushers;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.fasterxml.jackson.databind.type.MapLikeType;

public class Json {
  // Singleton pattern
  public final static Json instance = new Json();

  private static ObjectMapper json;
  private ObjectWriter jsonWriter;

  private Json() {
    this.json = new ObjectMapper();
    json.findAndRegisterModules();
    json.disable(MapperFeature.DEFAULT_VIEW_INCLUSION);
    json.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    json.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    this.jsonWriter = json.writer(new DefaultPrettyPrinter());
  }

  //the toString method of Json - convert json-object to an normal object
  //then make it a readable string
  public static String stringify(Object value) throws JsonProcessingException {
    return json.writeValueAsString(value);
  }

  //writes an object to the specified json file encoded into json
  public <Type> void write(Object value, File file, Class<Type> type) throws IOException {
    final MapLikeType mapType = json.getTypeFactory().constructMapLikeType(
      LinkedHashMap.class,
      Integer.class,
      type
    );
    
    jsonWriter.forType(mapType).writeValue(file, value);
  }

  //the parse function allows us to return an json-encoded object for further
  //use in the code
  public static <Type> Type parse(String jsonString, Class<Type> type) throws JsonProcessingException {
    return json.readValue(jsonString, type);
  }

  //does the same as the function above however it creates a LinkedHashMap and returns that with
  //the values decoded from the json file
  public <Type> LinkedHashMap<Integer, Type> parseMap(InputStream jsonStream, Class<Type> type) throws IOException {
    final MapLikeType mapType = json.getTypeFactory().constructMapLikeType(
      LinkedHashMap.class,
      Integer.class,
      type
    );
    return json.readValue(jsonStream, mapType);
  }

  public static <Type> List<Type> parseList(String jsonStream, Class<Type> type) throws IOException {
    final CollectionLikeType listType = json.getTypeFactory().constructCollectionType(
            ArrayList.class,
            type
    );

    return json.readValue(jsonStream, listType);
  }
}
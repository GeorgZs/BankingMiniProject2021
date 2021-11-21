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

  public String stringify(Object value) throws JsonProcessingException {
    return json.writeValueAsString(value);
  }

  public void write(Object value, File file) throws IOException {
    jsonWriter.writeValue(file, value);
  }

  public <T> T parse(String jsonString, Class<T> type) throws JsonProcessingException {
    return json.readValue(jsonString, type);
  }

  public <T> LinkedHashMap<Integer, T> parseMap(InputStream jsonStream, Class<T> type) throws IOException {
    final MapLikeType mapType = json.getTypeFactory().constructMapLikeType(
      LinkedHashMap.class,
      Integer.class,
      type
    );
    
    return json.readValue(jsonStream, mapType);
  }

}

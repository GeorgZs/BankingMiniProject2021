package crushers.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.type.CollectionLikeType;

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

  /**
   * Takes an object we have here at the staff app, for example Manager Georg, and creates a JSON
   * string representation of it.
   * 
   * @example
   * It turns this object (a thing only programmers know, way easier to work with in java):
   * Manager {
   *  name: "Georg",
   *  age: 42
   * }
   * 
   * into this string (everyone can read text, even servers):
   * "{ \"name\": \"Georg\", \"age\": 42 }"
   */
  public <Type> String stringify(Object value, Class<Type> type) throws JsonProcessingException {
    return jsonWriter.forType(type).writeValueAsString(value);
  }

  /**
   * Takes a JSON string representation of an object, for example Manager Georg, and creates a java 
   * object we have here at the staff app of it.
   * 
   * @example
   * It turns this string (everyone can read text, even servers):
   * "{ \"name\": \"Georg\", \"age\": 42 }"
   * 
   * into this object (a thing only programmers know, way easier to work with in java):
   * Manager {
   *  name: "Georg",
   *  age: 42
   * }
   */
  public <Type> Type parse(String jsonString, Class<Type> type) throws JsonProcessingException {
    return json.readValue(jsonString, type);
  }

  /**
   * Does the same as the above method {@code parse}, but it is for collections of data.
   * @see #parse(String, Class)
   */
  public <Type> List<Type> parseList(String jsonString, Class<Type> type) throws IOException {
    final CollectionLikeType listType = json.getTypeFactory().constructCollectionType(
      ArrayList.class,
      type
    );

    return json.readValue(jsonString, listType);
  }
}

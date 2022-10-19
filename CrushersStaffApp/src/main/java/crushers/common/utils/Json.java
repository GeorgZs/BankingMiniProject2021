package crushers.common.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.fasterxml.jackson.databind.type.MapLikeType;

/**
 * Singleton class for parsing and stringifying JSON data with Jackson.
 */
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
  public <T> String stringify(Object value, Class<T> type) throws JsonProcessingException {
    return jsonWriter.forType(type).writeValueAsString(value);
  }

  /**
   * Does the same as the above method {@code stringify}, but for collections of data.
   * @see #stringify(Object, Class)
   */
  public <T> String stringifyList(Collection<T> value, Class<T> type) throws JsonProcessingException {
    ArrayList<T> list = new ArrayList<T>();
    list.addAll(value);

    final CollectionLikeType listType = json.getTypeFactory().constructCollectionType(
      ArrayList.class,
      type
    );

    return jsonWriter.forType(listType).writeValueAsString(list);
  }

  /**
   * Writes a whole map of objects to a file by stringifying the map to a JSON representation.
   * @see #stringify(Object) for more details on this process.
   */
  public <T> void writeMap(Object value, File file, Class<T> type) throws IOException {
    final MapLikeType mapType = json.getTypeFactory().constructMapLikeType(
      LinkedHashMap.class,
      Integer.class,
      type
    );
    
    jsonWriter.forType(mapType).writeValue(file, value);
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
  public <T> T parse(String jsonString, Class<T> type) throws JsonProcessingException {
    return json.readValue(jsonString, type);
  }

  /**
   * Does the same as the above method {@code parse}, but for collections of data.
   * @see #parse(String, Class)
   */
  public <Type> List<Type> parseList(String jsonString, Class<Type> type) throws IOException {
    final CollectionLikeType listType = json.getTypeFactory().constructCollectionType(
      ArrayList.class,
      type
    );

    return json.readValue(jsonString, listType);
  }

  /**
   * Does the same as the above method {@code parse}, but for maps of data.
   * @see #parse(String, Class)
   */
  public <T> LinkedHashMap<Integer, T> parseMap(InputStream jsonStream, Class<T> type) throws IOException {
    final MapLikeType mapType = json.getTypeFactory().constructMapLikeType(
      LinkedHashMap.class,
      Integer.class,
      type
    );
    
    return json.readValue(jsonStream, mapType);
  }

}

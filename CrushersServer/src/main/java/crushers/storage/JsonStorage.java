package crushers.storage;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.type.MapLikeType;

import java.io.*;
import java.util.Collection;
import java.util.LinkedHashMap;

/**
 * An implementation of the Storage interface which uses json files to store objects on disk.
 * This class is just a stub and still needs to be implemented.
 */
public class JsonStorage<Type extends Storable> implements Storage<Type> {

  private ObjectMapper json;
  private InputStream inputStream;
  private MapLikeType typeReference;
  private LinkedHashMap<Integer ,Type> linkedHashMap;
  private ObjectWriter writer;
  private File file;
  private int nextId;

  public JsonStorage(File jsonFile, Class<Type> type) throws IOException {
    try {
      // Object Mapper used to read and write from JSON files
      // InputStream is to establish a connection to the JSON file specified
      // TypeReference is there to specify what type the information is structured as when read from file
      this.json = new ObjectMapper();
      this.inputStream = new FileInputStream(jsonFile);
      this.typeReference = json.getTypeFactory().constructMapLikeType(LinkedHashMap.class, Integer.class, type);

      // linkedHashMap created by reading off the Json file and adding
      // the entire hashmap as specified to the local hashmap deserialized
      this.linkedHashMap = json.readValue(inputStream, typeReference);
      this.writer = json.writer(new DefaultPrettyPrinter());
      this.file = jsonFile;
      this.nextId = 1001;
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }

  @Override
  public Type get(int id) {
    return this.linkedHashMap.get(id);
  }

  @Override
  public Collection<Type> getAll() throws IOException {
    return this.linkedHashMap.values();
  }

  @Override
  public Type create(Type obj) throws IOException {
    obj.setId(nextId++);
    this.linkedHashMap.put(obj.getId(), obj);
    writer.writeValue(this.file, this.linkedHashMap);
    return null;
  }

  @Override
  public Type update(int id, Type obj) throws IOException {
    this.linkedHashMap.replace(id, obj);
    this.writer.writeValue(this.file, this.linkedHashMap);
    return null;
  }

  @Override
  public Type delete(int id) throws IOException {
    this.linkedHashMap.remove(id);
    this.writer.writeValue(this.file, this.linkedHashMap);
    return null;
  }
}

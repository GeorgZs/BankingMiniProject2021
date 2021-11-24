package crushers.storage;

import crushers.utils.Json;

import java.io.*;
import java.util.Collection;
import java.util.LinkedHashMap;

/**
 * An implementation of the Storage interface which uses json files to store objects on disk.
 * This class is just a stub and still needs to be implemented.
 */
public class JsonStorage<Type extends Storable> implements Storage<Type> {

  private File file;
  private Class<Type> type;
  private int nextId;
  protected LinkedHashMap<Integer, Type> data;

  //in the constructor it creates the json storage with the json file,
  //and the specific class type that the json file should store
  //like the clerk class or the bank class - storing only objects of that type
  //// also creates a linkedHashMap so that we can save and retrieve objects
  //// easier and disallow repeated objects, and they would be sorted by ID number
  public JsonStorage(File jsonFile, Class<Type> type) throws IOException {
    this.file = jsonFile;
    this.type = type;
    this.nextId = 1001;
    this.data = new LinkedHashMap<>();

    if (jsonFile.exists()) {
      InputStream existingJsonData = new FileInputStream(jsonFile);
      this.data = Json.instance.parseMap(existingJsonData, type);
      this.nextId += data.size();
    }

    Json.instance.write(this.data, jsonFile, type);
  }

  @Override
  public Type get(int id) {
    return this.data.get(id);
  }

  @Override
  public Collection<Type> getAll() throws IOException {
    return this.data.values();
  }

  @Override
  public Type create(Type newObj) throws IOException, Exception {
    newObj.setId(nextId++);
    this.data.put(newObj.getId(), newObj);
    Json.instance.write(this.data, this.file, this.type);
    return newObj;
  }

  @Override
  public Type update(int id, Type newObjData) throws IOException {
    this.data.replace(id, newObjData);
    Json.instance.write(this.data, this.file, this.type);
    return newObjData;
  }

  @Override
  public Type delete(int id) throws IOException {
    Type deletedObj = this.data.remove(id);
    Json.instance.write(this.data, this.file, this.type);
    return deletedObj;
  }
}

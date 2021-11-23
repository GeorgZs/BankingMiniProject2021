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
  private int nextId;
  private LinkedHashMap<Integer, Type> linkedHashMap;

  //in the constructor it creates the json storage with the json file,
  //and the specific class type that the json file should store
  //like the clerk class or the bank class - storing only objects of that type
  //// also creates a linkedHashMap so that we can save and retrieve objects
  //// easier and disallow repeated objects, and they would be sorted by ID number
  public JsonStorage(File jsonFile, Class<Type> type) throws IOException {
    this.file = jsonFile;
    this.nextId = 1001;
    this.linkedHashMap = new LinkedHashMap<>();

    if (jsonFile.exists()) {
      InputStream existingJsonData = new FileInputStream(jsonFile);
      this.linkedHashMap = Json.instance.parseMap(existingJsonData, type);
    }

    Json.instance.write(this.linkedHashMap, jsonFile);
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
  public Type create(Type newObj) throws IOException {
    newObj.setId(nextId++);
    this.linkedHashMap.put(newObj.getId(), newObj);
    Json.instance.write(this.linkedHashMap, this.file);
    return newObj;
  }

  @Override
  public Type update(int id, Type newObjData) throws IOException {
    this.linkedHashMap.replace(id, newObjData);
    Json.instance.write(this.linkedHashMap, this.file);
    return newObjData;
  }

  @Override
  public Type delete(int id) throws IOException {
    Type deletedObj = this.linkedHashMap.remove(id);
    Json.instance.write(this.linkedHashMap, this.file);
    return deletedObj;
  }
}

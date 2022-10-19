package crushers.storage;

import java.io.*;
import java.time.Instant;
import java.util.Collection;
import java.util.LinkedHashMap;

import crushers.common.utils.Storable;
import crushers.common.utils.Json;

/**
 * An implementation of the Storage interface which uses json files to store objects on disk.
 * This class is just a stub and still needs to be implemented.
 */
public class JsonStorage<Type extends Storable> implements Storage<Type> {

  // Gets the unix timestamp in seconds, thus every time the server restarts the ids are in a 
  // different range and not overlapping.
  private static final int START_ID = (int)Instant.now().getEpochSecond();

  private File file;
  private Class<Type> type;
  private int nextId;
  protected LinkedHashMap<Integer, Type> data;

  /**
   * Creates the json storage with the json file for a specific class type.
   * e.g. Clerk, Bank, etc. - Only objects of that type will be stored.
   * 
   * Also creates a linkedHashMap as a cache for quick retrieval of objects and checking for duplicates.
   */
  public JsonStorage(File jsonFile, Class<Type> type) throws IOException {
    this.file = jsonFile;
    this.type = type;
    this.nextId = START_ID;
    this.data = new LinkedHashMap<>();

    if (jsonFile.exists()) {
      InputStream existingJsonData = new FileInputStream(jsonFile);
      this.data = Json.instance.parseMap(existingJsonData, type);
    }

    Json.instance.writeMap(this.data, jsonFile, type);
  }

  /**
   * Gets an object with a given id from the storage.
   */
  @Override
  public Type get(int id) {
    return this.data.get(id);
  }

  /**
   * Gets all objects from the storage.
   */
  @Override
  public Collection<Type> getAll() throws IOException {
    return this.data.values();
  }

  /**
   * Creates a new object in the storage and assigns it a unique id.
   * @return the created object with its id.
   */
  @Override
  public Type create(Type newObj) throws IOException, Exception {
    newObj.setId(nextId++);
    this.data.put(newObj.getId(), newObj);
    Json.instance.writeMap(this.data, this.file, this.type);
    return newObj;
  }

  /**
   * Updates an object with a given id in the storage.
   * @return the updated object.
   */
  @Override
  public Type update(int id, Type newObjData) throws IOException {
    newObjData.setId(id);
    this.data.replace(id, newObjData);
    Json.instance.writeMap(this.data, this.file, this.type);
    return newObjData;
  }

  /**
   * Deletes an object with a given id from the storage.
   */
  @Override
  public Type delete(int id) throws IOException {
    Type deletedObj = this.data.remove(id);
    Json.instance.writeMap(this.data, this.file, this.type);
    return deletedObj;
  }
}

package crushers.storage;

import java.util.*;

/**
 * An implementation of the Storage interface which uses a HashMap to store objects in memory.
 */
public class MemoryStorage<Type extends Storable> implements Storage<Type> {
  private final Map<Integer, Type> storage;
  private int nextId;

  public MemoryStorage() {
    this.storage = new LinkedHashMap<>();
    this.nextId = 1001;
  }

  @Override
  public Type get(int id) {
    return storage.get(id);
  }

  @Override
  public Collection<Type> getAll() {
    return storage.values();
  }

  @Override
  public Type create(Type obj) {
    obj.setId(nextId++);
    return storage.put(obj.getId(), obj);
  }

  @Override
  public Type update(int id, Type obj) {
    return storage.put(id, obj);
  }

  @Override
  public Type delete(int id) {
    return storage.remove(id);
  }
  
}

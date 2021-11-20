package crushers.storage;

import java.io.File;
import java.util.Collection;

/**
 * An implementation of the Storage interface which uses json files to store objects on disk.
 * This class is just a stub and still needs to be implemented.
 */
public class JsonStorage<Type extends Storable> implements Storage<Type> {

  public JsonStorage(File jsonFile) {
    // gets the jsonFile to store the data in
    // configure the jackson json mapper here
  }

  @Override
  public Type get(int id) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Collection<Type> getAll() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Type create(Type obj) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Type update(int id, Type obj) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Type delete(int id) {
    // TODO Auto-generated method stub
    return null;
  }

}

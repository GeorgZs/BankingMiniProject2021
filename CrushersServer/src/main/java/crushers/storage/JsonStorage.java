package crushers.storage;

import java.io.File;
import java.util.Collection;

/**
 * An implementation of the Storage interface which uses json files to store objects on disk.
 * This class is just a stub and still needs to be implemented.
 */
public class JsonStorage<Type extends Storable> implements Storage<Storable> {

  public JsonStorage(File jsonFile) {
    // gets the jsonFile to store the data in
    // configure the jackson json mapper here
  }

  @Override
  public Storable get(int id) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Collection<Storable> getAll() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Storable create(Storable obj) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Storable update(int id, Storable obj) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Storable delete(int id) {
    // TODO Auto-generated method stub
    return null;
  }

}

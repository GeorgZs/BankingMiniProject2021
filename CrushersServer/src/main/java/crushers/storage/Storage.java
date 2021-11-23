package crushers.storage;

import java.io.IOException;
import java.util.Collection;

/**
 * A basic crud storage interface which allows for storing and retrieving objects.
 */
public interface Storage<Type extends Storable> {
  Type get(int id);
  Collection<Type> getAll() throws IOException;

  Type create(Type obj) throws IOException, Exception;
  Type update(int id, Type obj) throws IOException;
  Type delete(int id) throws IOException;
}

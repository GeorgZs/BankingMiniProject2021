package crushers.storage;

import java.util.Collection;

/**
 * A basic crud storage interface which allows for storing and retrieving objects.
 */
public interface Storage<Type extends Storable> {
  Type get(int id);
  Collection<Type> getAll();

  Type create(Type obj);
  Type update(int id, Type obj);
  Type delete(int id);
}

package crushers.services.ducks;

import java.io.IOException;
import java.util.Collection;

import crushers.models.Duck;
import crushers.storage.Storage;

/**
 * The service for ducks, which contains all the business logic and constraints. It uses a storage
 * which is injected via the constructor.
 */
public class DuckService {
  private final Storage<Duck> storage;

  public DuckService(Storage<Duck> storage) {
    this.storage = storage;
  }

  public Collection<Duck> getAll() throws IOException {
    if (storage.getAll().isEmpty()) {
      // add some default ducks
      System.out.println("No ducks found, adding some default ducks");
      storage.create(new Duck("Donald", "blue"));
      storage.create(new Duck("Daisy", "red"));
    }

    return storage.getAll();
  }

  public Duck get(int id) {
    if (id <= 1000) throw new IllegalArgumentException("Id must be greater than 1000");
    return storage.get(id);
  }
  
}

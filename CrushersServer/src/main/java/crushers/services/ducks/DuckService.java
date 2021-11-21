package crushers.services.ducks;

import java.io.FileNotFoundException;
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

  public Collection<Duck> getAll() throws Exception {
    if (storage.getAll().isEmpty()) {
      // add some default ducks
      System.out.println("No ducks found, adding some default ducks");
      storage.create(new Duck("Donald", "blue"));
      storage.create(new Duck("Daisy", "red"));
    }

    return storage.getAll();
  }

  public Duck get(int id) throws Exception {
    if (id <= 1000) throw new IllegalArgumentException("Id must be greater than 1000.");
    Duck duck = storage.get(id);

    if (duck == null) {
      throw new FileNotFoundException("No duck found with id " + id);
    }

    return duck;
  }

  public Duck create(Duck duck) throws Exception {
    if (duck == null) throw new IllegalArgumentException("Data for the creation of a duck must be given.");
    if (duck.name.isBlank()) throw new IllegalArgumentException("Name must not be empty.");
    return storage.create(duck);
  }
  
}

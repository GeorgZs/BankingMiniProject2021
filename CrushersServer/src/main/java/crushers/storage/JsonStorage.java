package crushers.storage;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * An implementation of the Storage interface which uses json files to store objects on disk.
 * This class is just a stub and still needs to be implemented.
 */
public class JsonStorage<Type extends Storable> implements Storage<Type> {

  private ObjectMapper json;
  private InputStream inputStream;
  private TypeReference<List<Storable>> typeReference;
  private List<Storable> list;
  private ObjectWriter writer;
  private ObjectReader reader;
  private File file;

  public JsonStorage(File jsonFile) throws IOException {
    try {
      this.json = new ObjectMapper();
      this.inputStream = new FileInputStream(jsonFile);
      this.typeReference = new TypeReference<List<Storable>>() {
      };
      this.list = json.readValue(inputStream, typeReference);
      this.writer = json.writer(new DefaultPrettyPrinter());
      this.reader = json.reader();
      this.file = jsonFile;
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }

  @Override
  public Type get(int id) {
    // TODO Auto-generated method stub

    //Can't do this because Jackson typeReference restricts use of Maps, so I can't get by Key
    return null;
  }

  @Override
  public Collection<Type> getAll() throws IOException {
    List<Type> list = Arrays.asList(reader.readValue(inputStream));
    return list;
  }

  @Override
  public Storable create(Storable obj) throws IOException {
    list.add(obj);
    writer.writeValue(file, list);
    return null;
  }

  @Override
  public Type update(int id, Type obj) {
    return null;
  }

  @Override
  public Type delete(int id) {
    return null;
  }
}

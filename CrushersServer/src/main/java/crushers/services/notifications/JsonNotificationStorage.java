package crushers.services.notifications;

import java.io.File;
import java.io.IOException;
import java.util.*;

import crushers.common.models.*;
import crushers.storage.JsonStorage;

public class JsonNotificationStorage extends JsonStorage<Notification> {

  private final Map<Bank, Set<Notification>> bankNotifications;

  public JsonNotificationStorage(File jsonFile) throws IOException {
    super(jsonFile, Notification.class);
    this.bankNotifications = new HashMap<>();

    for (Notification notification : this.data.values()) {
      addToMaps(notification);
    }
  }

  /**
   * @param newNotification to be added to the Notification Storage
   * @return the Notification that was successfully added to the Storage
   * @throws Exception if the Notification creation was in any way unsuccessful
   */
  @Override
  public Notification create(Notification newNotification) throws Exception {
    Notification createdNotification = super.create(newNotification);
    addToMaps(createdNotification);
    return createdNotification;
  }

  /**
   * @param id of the specified notification
   * @return the notification that was deleted just as a precaution to see what notification was deleted
   * @throws IOException if notification deletion was in any way unsuccessful
   */
  @Override
  public Notification delete(int id) throws IOException {
    Notification deletedNotification = super.delete(id);

    if (deletedNotification != null) {
      bankNotifications.get(deletedNotification.getSender()).remove(deletedNotification);
    }

    return deletedNotification;
  }

  /**
   * @param bank of the logged-in Clerk
   * @return the Set of Notifications sent by the Bank
   */
  public Set<Notification> getNotificationsOfBank(Bank bank) {
    return bankNotifications.get(bank) == null ? new HashSet<>() : bankNotifications.get(bank);
  }

  /**
   * @param notification added to the Local Map of Notifications which are retrieved from the JSON-files
   * adds the Notification specified in the method signature to each of the maps
   *
   * checks whether each Map key has a set associated to it, if not it creates a new empty set for the Key
   */
  protected void addToMaps(Notification notification) {
    if (!bankNotifications.containsKey(notification.getSender())) {
      bankNotifications.put(notification.getSender(), new HashSet<>());
    }

    bankNotifications.get(notification.getSender()).add(notification);
  }
}
